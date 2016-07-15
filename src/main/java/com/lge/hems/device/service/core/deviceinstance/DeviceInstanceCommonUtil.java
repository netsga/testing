package com.lge.hems.device.service.core.deviceinstance;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataUpdateException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.service.core.devicemodel.DeviceModelService;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.customize.JsonConverter;

@Component
public class DeviceInstanceCommonUtil {
    @Autowired
    private DeviceInstanceService instanceService;
    @Autowired
    private DeviceModelService modelService;
	private JsonParser parser = new JsonParser();
    /**
     * Read leaf information from redis cache or memory cache
     *
     * @param logicalDeviceId
     * @param requestKeys
     * @return
     * @throws NullInstanceException
     * @throws DeviceInstanceDataReadException
     */
    public Map<String, Map<String, Object>> readLeafInfo(String logicalDeviceId, List<String> requestKeys) throws NullInstanceException, DeviceInstanceDataReadException {
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
    
    public void checkUpdateKeyAvailable(String logicalDeviceId, List<String> updateKey) throws NullInstanceException, DeviceInstanceDataUpdateException {
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

    public void checkUpdateKeyAvailable(String logicalDeviceId, String key) throws NullInstanceException, DeviceInstanceDataUpdateException {
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

    public JsonObject convertMapToJsonObject(Map<String, Object> reqMap) throws RequestParameterException {
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
                    } else if(strata[i].equals("history")) {
                    	resultContentPtr.add(strata[i], parser.parse(o.toString()));
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
