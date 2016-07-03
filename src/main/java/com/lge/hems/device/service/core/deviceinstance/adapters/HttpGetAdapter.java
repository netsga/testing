package com.lge.hems.device.service.core.deviceinstance.adapters;

import com.jayway.jsonpath.JsonPath;
import com.lge.hems.device.exceptions.DeviceInstanceLeafInfoException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.exceptions.deviceinstance.NullLeafInformationException;
import com.lge.hems.device.model.common.entity.LeafInformation;
import com.lge.hems.device.model.common.entity.LeafInformationKey;
import com.lge.hems.device.service.core.deviceinstance.converter.JsonMessageConverter;
import com.lge.hems.device.service.dao.rds.LeafInformationRepository;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.LeafUtil;
import com.lge.hems.device.utilities.RestServiceUtil;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by netsga on 2016. 6. 28..
 */
@Component
public class HttpGetAdapter implements DeviceInstanceDataAdapter{
    // local variables
    @SuppressWarnings("unused")
    @LoggerImpl
    private Logger logger;

    @Autowired
    private LeafInformationRepository repository;

    @Autowired
    private RestServiceUtil restServiceUtil;

    private static final String MODULE_TYPE = "http_get";
    private static final String READ = "read";

    public Map<String, Object> getDeviceInstanceData(String logicalDeviceId, Map<String, Map<String, Object>> leafDataMap, Map<String, String> requestInfo) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException, DeviceInstanceLeafInfoException, NullLeafInformationException {
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
            for(int i = 2; i < paramArr.length; i++) {
                requestInfo.put(paramArr[i].substring(0, paramArr[i].indexOf(":")).trim(), paramArr[i].substring(paramArr[i].indexOf(":") + 1, paramArr[i].length()).trim());
            }

            // header 및 body는 두가지 parameter type을 가지고 있다.
            // #로 시작하는 request시 발생하는 parameter ex) user-id, logical device id, 현재 leaf에 해당하는 value
            // $으로 시작하는 model leaf에 애초에 define 되어있는 parameter ex) tag list
            // 사용자에 의해 가변되는 paramter는 $로 시작하는 parameter임.

            // header에서 필요로 하는 Data (reqParam)을 requestInfo를 통해 채운다.
            // key는 동일해야 함. 다만 static하게 default로 고정된 value는 가이드 제공 필요

            try {
                String headerStr = createMessageString(leafInfo.getHeader(), requestInfo);
                String urlStr = createMessageString(leafInfo.getUrl(), requestInfo);

                result.put(entry.getKey(), requestValueFromTargetUrl(urlStr, headerStr, paramArr[1]));

            } catch (DeviceInstanceLeafInfoException e) {
                e.setLogicalDeviceId(logicalDeviceId);
                e.setParam(requestInfo.toString());
                throw e;
            }
        }

        return result;
    }

    ////////////////////////////// PRIVATE /////////////////////////////

    private String requestValueFromTargetUrl(String url, String headerStr, String parseFormat) {

        Map.Entry<HttpStatus, String> restResp = restServiceUtil.requestGetMethod(url, headerStr);

        Object obj = JsonPath.read(restResp.getValue(), parseFormat);
        return String.valueOf(obj);
    }


    private String createMessageString(String targetStr, Map<String, String> requestInfo) throws DeviceInstanceLeafInfoException {
        String result = targetStr;
        Map<String, String> reqParam = LeafUtil.parameterExtractor(targetStr);
        for(Map.Entry<String, String> p:reqParam.entrySet()) {
            if(!requestInfo.containsKey(p.getKey())) {
                throw new DeviceInstanceLeafInfoException("Check leaf information of device model");
            }
            result = StringUtils.replace(result, p.getValue(), requestInfo.get(p.getKey()));
        }

        return result;
    }
}
