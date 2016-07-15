/**
 * Created by netsga on 2016. 6. 18..
 *
 */

package com.lge.hems.device.service.core.deviceinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.lge.hems.device.exceptions.DeviceInstanceLeafInfoException;
import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.RestRequestException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataUpdateException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.exceptions.deviceinstance.NullLeafInformationException;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.model.common.InternalCommonKey;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.service.core.deviceinstance.adapters.kiwigrid.KiwigridPatchAdapter;
import com.lge.hems.device.service.core.devicemodel.DeviceModelService;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.service.dao.rds.DeviceInstanceRepository;
import com.lge.hems.device.utilities.CollectionFactory;


@Service
public class DeviceInstanceWriteDataService {
    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;
    @Autowired
    private DeviceInstanceRepository instanceRepository;
    @Autowired
    private DeviceInstanceService instanceService;
    @Autowired
    private DeviceModelService modelService;
    @Autowired
    private KiwigridPatchAdapter kiwigridPatchAdapter;
    @Autowired
    private DeviceInstanceCommonUtil commonService;

    /////////////////////////////////////// UPDATE ///////////////////////////////////////////
    public JsonObject updateDeviceInstanceData(String logicalDeviceId, Map<String, Object> updateData, Map<String, String> reqInfo) throws DeviceInstanceDataUpdateException, NullInstanceException, NullRequestException, RestRequestException {
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
        
        reqInfo.put(InternalCommonKey.RAW_DEVICE_ID, instanceRepository.findByLogicalDeviceId(logicalDeviceId).getDeviceId());
        // 현재는 아래처럼 모든 type을 호출하지만 차후 factory를 통해서 알아서 가져오도록 만들어야 함.
        // read leaf information
        Map<String, Map<String, Object>> leafInfo;
		try {
			leafInfo = commonService.readLeafInfo(logicalDeviceId, updateKeys);
			try {
				kiwigridPatchAdapter.patchDeviceInstanceData(logicalDeviceId, leafInfo, reqInfo);
			} catch (DeviceInstanceLeafInfoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullLeafInformationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequestParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (DeviceInstanceDataReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        try {
            Map<String, Object> readResp = cacheRepository.readDeviceInstanceData(logicalDeviceId, updateKeys);
            result = convertMapToJsonObject(readResp);
        } catch (Exception e) {
            throw new DeviceInstanceDataUpdateException("Read fail of updated data", logicalDeviceId, e);
        }

        return result;
    }

    public JsonObject updateDeviceInstanceData(String logicalDeviceId, String key, Object data, Map<String, String> reqInfo) throws DeviceInstanceDataUpdateException, NullInstanceException, NullRequestException, RestRequestException, DeviceInstanceLeafInfoException, NullLeafInformationException, RequestParameterException, DeviceInstanceDataReadException {
        JsonObject result;

        // key availability check
        checkUpdateKeyAvailable(logicalDeviceId, key);
        
        reqInfo.put(InternalCommonKey.RAW_DEVICE_ID, instanceRepository.findByLogicalDeviceId(logicalDeviceId).getDeviceId());
        reqInfo.put("value", String.valueOf(data));
        
        // 현재는 아래처럼 모든 type을 호출하지만 차후 factory를 통해서 알아서 가져오도록 만들어야 함.
        // read leaf information
        Map<String, Map<String, Object>> leafInfo;
        List<String> updateKeys = CollectionFactory.newList();
        updateKeys.add(key);
		leafInfo = commonService.readLeafInfo(logicalDeviceId, updateKeys);
		
		// leafinfo가 kiwigrid인 경우 아래것을 실행해야 한다.
		kiwigridPatchAdapter.patchDeviceInstanceData(logicalDeviceId, leafInfo, reqInfo);
		
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
