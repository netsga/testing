package com.lge.hems.device.service.core.deviceinstance.adapters.mock;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lge.hems.device.exceptions.DeviceInstanceLeafInfoException;
import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.RestRequestException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.exceptions.deviceinstance.NullLeafInformationException;
import com.lge.hems.device.model.common.InternalCommonKey;
import com.lge.hems.device.model.common.entity.LeafInformation;
import com.lge.hems.device.model.common.entity.LeafInformationKey;
import com.lge.hems.device.service.core.deviceinstance.adapters.DeviceInstanceDataAdapter;
import com.lge.hems.device.service.dao.rds.LeafInformationRepository;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.LeafUtil;

@Component
public class HistoryMockAdapter implements DeviceInstanceDataAdapter{
    @Autowired
    private LeafInformationRepository repository;
    
    private static final String MODULE_TYPE = "mock";
    private static final String READ = "read";

    public Map<String, Object> getDeviceInstanceData(String logicalDeviceId, Map<String, Map<String, Object>> leafDataMap, Map<String, String> requestInfo) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException, DeviceInstanceLeafInfoException, NullLeafInformationException, RestRequestException, NullRequestException {
        Map<String, Object> result = CollectionFactory.newMap();

        for(Map.Entry<String, Map<String, Object>> entry:leafDataMap.entrySet()) {
            Map<String, String> leafData = LeafUtil.leafInfoExtractor((String) entry.getValue().get(READ));
            if(!MODULE_TYPE.equals(leafData.get(LeafUtil.TYPE))) {
                continue;
            }

            // paramArr[0] is source name, not leaf model parameter
            String[] paramArr = StringUtils.split(leafData.get(LeafUtil.PARAMETERS), ",");
            if(paramArr.length < 1) {
                throw new DeviceInstanceLeafInfoException("Check leaf information of device model", logicalDeviceId, leafData.get(LeafUtil.PARAMETERS));
            }

            LeafInformation leafInfo = repository.findByLeafInformationKey(new LeafInformationKey(leafData.get(LeafUtil.TYPE), leafData.get(LeafUtil.METHOD), paramArr[0].trim()));
            if(leafInfo == null) {
                throw new NullLeafInformationException("Null Leaf Information", logicalDeviceId, leafData);
            }

            // add more information of requestInfo from data leaf model.
            for(int i = 3; i < paramArr.length; i++) {
                requestInfo.put(paramArr[i].substring(0, paramArr[i].indexOf(":")).trim(), paramArr[i].substring(paramArr[i].indexOf(":") + 1, paramArr[i].length()).trim());
            }

            result.put(entry.getKey(), createMockData(requestInfo.get(InternalCommonKey.FROM), requestInfo.get(InternalCommonKey.TO),
					requestInfo.get(InternalCommonKey.MIN), requestInfo.get(InternalCommonKey.MAX),
					requestInfo.get(InternalCommonKey.METHOD), requestInfo.get(InternalCommonKey.AGGREGATE)));
        }

        return result;
    }
    
    
    ///////////////////// PRIVATE SECTION ////////////////////////
    private JsonElement createMockData(String fromStr, String toStr, String minStr, String maxStr, String method, String aggregate) {
    	Long from = Long.valueOf(fromStr);
    	Long to = Long.valueOf(toStr);
    	Integer min = Integer.valueOf(minStr);
    	Integer max = Integer.valueOf(maxStr);
    	
    	JsonArray result = new JsonArray();
    	int maxLoop = -1;
    	if("hour".equalsIgnoreCase(aggregate)) {
			maxLoop = (int) ((to - from) / 3600);
		} else if("day".equalsIgnoreCase(aggregate)) {
			maxLoop = (int) ((to - from) / 3600 * 24);
		} else if("month".equalsIgnoreCase(aggregate)) {
			maxLoop = (int) ((to - from) / 3600 * 24 * 30);
		} else if("year".equalsIgnoreCase(aggregate)) {
			maxLoop = (int) ((to - from) / 3600 * 24 * 365);
		}
    			
    	for(int i = 0; i <= maxLoop; i++) {
    		JsonObject obj = new JsonObject();
    		obj.addProperty("value", ThreadLocalRandom.current().nextInt(min, max + 1));
    		if("hour".equalsIgnoreCase(aggregate)) {
    			obj.addProperty("timestamp", from + (i * 3600));
    		} else if("day".equalsIgnoreCase(aggregate)) {
    			obj.addProperty("timestamp", from + (i * 3600 * 24));
    		} else if("month".equalsIgnoreCase(aggregate)) {
    			obj.addProperty("timestamp", from + (i * 3600 * 24 * 30));
    		} else if("year".equalsIgnoreCase(aggregate)) {
    			obj.addProperty("timestamp", from + (i * 3600 * 24 * 365));
    		}
    	}
    	
    	return result;
    }


}
