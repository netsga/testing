/**
 * Created by netsga on 2016. 6. 18..
 *
 */

package com.lge.hems.device.service.core.deviceinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataUpdateException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.model.common.InternalCommonKey;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.service.core.deviceinstance.adapters.HttpGetAdapter;
import com.lge.hems.device.service.core.deviceinstance.adapters.InstanceDataAdapter;
import com.lge.hems.device.service.core.devicemodel.DeviceModelService;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.service.dao.rds.DeviceInstanceRepository;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.utilities.logger.LoggerImpl;


@Service
public class DeviceInstanceDataService {
    // local variables
    @SuppressWarnings("unused")
    @LoggerImpl
    private Logger logger;
//    private static Map<String, DeviceModelInformation> deviceModelCache = CollectionFactory.newMap();

    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;
    @Autowired
    private DeviceInstanceRepository instanceRepository;
    @Autowired
    private DeviceInstanceService instanceService;
    @Autowired
    private JsonConverter conv;
    @Autowired
    private DeviceModelService modelService;
    @Autowired
    private InstanceDataAdapter instanceDataAdapter;
    @Autowired
    private HttpGetAdapter httpGetAdapter;


    ///////////////////////////////////////// READ ///////////////////////////////////////////

    /**
     * leaf 정보를 받아서 디바이스 타입별로 번갈아가며 각 어댑터를 호출해서 정보를 map 형식으로 받아옴.
     * leaf 하나하나마다 데이터를 호출하면 시간이 많이 소요될 수 있으므로
     * adapter 별로 덩어리를 통째로 가져가서 각기 최적화를 수행함.
     * 다만 HTTP와 같이 Common한 성격을 지닌 것은 하나로 처리됨.
     *
     * @param logicalDeviceId
     * @param requestKeys
     * @return
     * @throws NullInstanceException
     * @throws DeviceInstanceDataReadException
     * @throws RequestParameterException
     */
    public JsonObject getDeviceInstanceData(String logicalDeviceId, List<String> requestKeys, Map<String, String> reqInfo) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException {

        Map<String, Object> readResp = CollectionFactory.newMap();

        try {
        	reqInfo.put(InternalCommonKey.RAW_DEVICE_ID, instanceRepository.findByLogicalDeviceId(logicalDeviceId).getDeviceId());
            // 현재는 아래처럼 모든 type을 호출하지만 차후 factory를 통해서 알아서 가져오도록 만들어야 함.
            // read leaf information
            Map<String, Map<String, Object>> leafInfo = readLeafInfo(logicalDeviceId, requestKeys);

            // read all instance type data
            readResp.putAll(instanceDataAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo));

            // read all http get type data
            readResp.putAll(httpGetAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo, reqInfo));

            // read all http post type data

            // read all datamanager type data

        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Device data read error", logicalDeviceId, requestKeys, e);
        }

        return convertMapToJsonObject(readResp);
    }
    
    public JsonObject getDeviceInstanceHistoryData(String logicalDeviceId, List<String> requestKeys, Map<String, String> reqInfo) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException {

        Map<String, Object> readResp = CollectionFactory.newMap();

        try {
            // 현재는 아래처럼 모든 type을 호출하지만 차후 factory를 통해서 알아서 가져오도록 만들어야 함.
            // read leaf information
            Map<String, Map<String, Object>> leafInfo = readLeafInfo(logicalDeviceId, requestKeys);

            // read all instance type data
//            readResp.putAll(instanceDataAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo));

            // read all http get type data
            readResp.putAll(httpGetAdapter.getDeviceInstanceHistoryData(logicalDeviceId, leafInfo, reqInfo));

            // read all http post type data

            // read all datamanager type data

        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Device data read error", logicalDeviceId, requestKeys, e);
        }

        return convertMapToJsonObject(readResp);
    }

    // adapter module을 만들어야 함.

    public JsonObject getDeviceInstanceAllData(String logicalDeviceId) throws RequestParameterException, DeviceInstanceDataReadException {
        Map<String, Object> readResp;
        try {
            readResp = cacheRepository.readDeviceInstance(logicalDeviceId);
        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Find error in device instance storage.", logicalDeviceId);
        }
        return convertMapToJsonObject(readResp);
    }

    /**
     * Read leaf information from redis cache or memory cache
     *
     * @param logicalDeviceId
     * @param requestKeys
     * @return
     * @throws NullInstanceException
     * @throws DeviceInstanceDataReadException
     */
    private Map<String, Map<String, Object>> readLeafInfo(String logicalDeviceId, List<String> requestKeys) throws NullInstanceException, DeviceInstanceDataReadException {
        DeviceModelInformation modelInfo;
        Map<String ,Map<String, Object>> result = CollectionFactory.newMap();

        // read device instance using instance service
        DeviceInstanceInformation dvcInfo = instanceService.readDeviceInstanceInformation(logicalDeviceId);

        // read model information from cache
        modelInfo = modelService.getModelInformationCache().get(dvcInfo.getModelId());
        String modelStr = modelInfo.getModel().toString();

        for(String key:requestKeys) {
            Object leafInfoObj = JsonPath.read(modelStr, key);
            Map<String, Object> leafInfo;

            if(leafInfoObj instanceof Map) {
                leafInfo = (Map<String, Object>) leafInfoObj;
            } else {
                throw new DeviceInstanceDataReadException("Not enough device template information.", logicalDeviceId);
            }
            result.put(key, leafInfo);
        }
        return result;
    }
    /////////////////////////////////////// UPDATE ///////////////////////////////////////////

    public JsonObject updateDeviceInstanceData(String logicalDeviceId, Map<String, Object> updateData) throws DeviceInstanceDataUpdateException, NullInstanceException {
        JsonObject result;
        List<String> updateKeys = new ArrayList<>(updateData.keySet());

        // key availability check
        checkUpdateKeyAvailable(logicalDeviceId, updateKeys);

        try {
            if(!cacheRepository.updateDeviceInstanceData(logicalDeviceId, updateData)) {
                throw new Exception("Fail to update");
            }
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Device data update error", logicalDeviceId, updateData, e);
        }

        try {
            Map<String, Object> readResp = cacheRepository.readDeviceInstanceData(logicalDeviceId, updateKeys);
            result = convertMapToJsonObject(readResp);
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Read fail of updated data", logicalDeviceId, e);
        }

        return result;
    }

    public JsonObject updateDeviceInstanceData(String logicalDeviceId, String key, Object data) throws DeviceInstanceDataUpdateException, NullInstanceException {
        JsonObject result;

        // key availability check
        checkUpdateKeyAvailable(logicalDeviceId, key);

        try {
            if(!cacheRepository.updateDeviceInstanceData(logicalDeviceId, key, data)) {
                throw new Exception("Fail to update");
            }
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Device data update error", logicalDeviceId, key, String.valueOf(data));
        }

        try {
            Object readResp = cacheRepository.readDeviceInstanceData(logicalDeviceId, key);
            Map<String, Object> tempResp = CollectionFactory.newMap();
            tempResp.put(key, readResp);
            result = convertMapToJsonObject(tempResp);
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Read fail of updated data", logicalDeviceId, e);
        }

        return result;
    }

    ////////////////////////// PRIVATE SECTION ///////////////////////////////

    private void checkUpdateKeyAvailable(String logicalDeviceId, List<String> updateKey) throws NullInstanceException, DeviceInstanceDataUpdateException {
        DeviceInstanceInformation info = instanceService.readDeviceInstanceInformation(logicalDeviceId);
        JsonObject model;

        try {
            DeviceModelInformation modelInfo = modelService.getModelInformationCache().get(info.getModelId());

            model = modelInfo.getModel().getAsJsonObject();
            if(model == null || model.isJsonNull()) {
                throw new Exception("Model is null [" + info.getModelId() + "]");
            }
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Device instance and model sync. error", logicalDeviceId, e);
        }

        for(String key:updateKey) {
            try {
                JsonPath.read(model.toString(), key);
            } catch (PathNotFoundException e) {
                throw new DeviceInstanceDataUpdateException("Unknown tag name", logicalDeviceId, key);
            }
        }
    }

    private void checkUpdateKeyAvailable(String logicalDeviceId, String key) throws NullInstanceException, DeviceInstanceDataUpdateException {
        DeviceInstanceInformation info = instanceService.readDeviceInstanceInformation(logicalDeviceId);
        JsonObject model;

        try {
            DeviceModelInformation modelInfo = modelService.getModelInformationCache().get(info.getModelId());
//            DeviceModelInformation modelInfo;
//            if(deviceModelCache.containsKey(info.getModelId())) {
//                modelInfo = deviceModelCache.get(info.getModelId());
//            } else {
//                String modelStr = String.valueOf(cacheRepository.readDeviceModel(info.getModelId()));
//                modelInfo = conv.fromJson(modelStr, DeviceModelInformation.class);
//                deviceModelCache.put(info.getModelId(), modelInfo);
//            }

            model = modelInfo.getModel().getAsJsonObject();
            if(model == null || model.isJsonNull()) {
                throw new Exception("Model is null [" + info.getModelId() + "]");
            }
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Device instance and model sync. error", logicalDeviceId, e);
        }

        try {
            JsonPath.read(model.toString(), key);
        } catch (PathNotFoundException e) {
            throw new DeviceInstanceDataUpdateException("Unknown tag name", logicalDeviceId, key);
        }
    }

    private JsonObject convertMapToJsonObject(Map<String, Object> reqMap) throws RequestParameterException {
        JsonObject result = new JsonObject();

        for(Map.Entry<String, Object> entry: reqMap.entrySet()) {
            JsonObject resultContentPtr = result;

            String[] strata = StringUtils.split(entry.getKey(), ".");
            for(int i = 0; i < strata.length; i++) {
                if(i == strata.length - 1) {
                    Object o = entry.getValue();

                    // 형변환 관련 체크해야 함
                    if(strata[i].equals("meaVal")) {
                        resultContentPtr.addProperty(strata[i], Double.valueOf(o.toString()));
                    } else if(strata[i].equals("t")) {
                        resultContentPtr.addProperty(strata[i], Long.valueOf(o.toString()));
                    } else if(o instanceof Boolean) {
                        resultContentPtr.addProperty(strata[i], (Boolean)o);
                    } else {
                        try {
                            resultContentPtr.addProperty(strata[i], o.toString());
                        } catch (NullPointerException e) {
                            throw new RequestParameterException("Unknown tag name", entry.getKey(), null);
                        }
                    }
                } else {
                    JsonObject tempJson;
                    if(resultContentPtr.has(strata[i])) {
                        tempJson = resultContentPtr.getAsJsonObject(strata[i]);
                    } else {
                        tempJson = new JsonObject();
                        resultContentPtr.add(strata[i], tempJson);
                    }
                    resultContentPtr = tempJson;
                }
            }
        }
        return result;
    }

}
