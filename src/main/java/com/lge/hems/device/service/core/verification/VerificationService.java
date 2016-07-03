package com.lge.hems.device.service.core.verification;

import com.lge.hems.device.exceptions.RequestHeaderException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.model.common.DeviceDataTagValue;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by netsga on 2016. 5. 24..
 */
@Service
public class VerificationService {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private ParameterVerification paramVerification;

    @SuppressWarnings("unchecked")
    public Boolean verifyParameters(Boolean mandatory, Map<String, Object> params) throws RequestParameterException {
        Boolean result = true;
        for(Map.Entry<String, Object> e:params.entrySet()) {
            if(e.getValue() instanceof List) {
                List<Object> objectList = (List<Object>)e.getValue();
                for(Object o:objectList) {
                    paramVerification.checkParameter(e.getKey(), o, mandatory);
                }
            } else {
                paramVerification.checkParameter(e.getKey(), e.getValue(), mandatory);
            }
        }
        return result;
    }

    public Boolean verifyParameters(Boolean mandatory, String key, Object value) throws RequestParameterException {
        Boolean result = true;
        if(value instanceof List) {
            List<Object> objectList = (List<Object>)value;
            for(Object o:objectList) {
                paramVerification.checkParameter(key, o, mandatory);
            }
        } else {
            paramVerification.checkParameter(key, value, mandatory);
        }

        return result;
    }

    public void updatableCheck(List<String> tags) throws RequestParameterException {
        for(String e: tags) {
            String[] tagTempArray = StringUtils.split(e, ".");
            if("CO".equals(tagTempArray[1])) {
                throw new RequestParameterException("CO CONSTRAINT TAG IS NOT AVAILABLE TO UPDATE.", "tagName", VerificationErrorCode.UNSUPPORTED_PARAMETER);
            }
        }
    }

    public void controllableCheck(String tag) throws RequestParameterException {
        String[] tagTempArray = StringUtils.split(tag, ".");
        if(!"CO".equals(tagTempArray[1])) {
            throw new RequestParameterException("ST,MX,DC,SF CONSTRAINT TAG IS NOT AVAILABLE TO CONTROL.", "tagName", VerificationErrorCode.UNSUPPORTED_PARAMETER);
        }
    }

    public Boolean verifyParameters(Boolean mandatory, Map.Entry<String, Object>... args) throws RequestParameterException {
        Boolean result = true;
        for(Map.Entry<String, Object> d:args) {
            if(d.getValue() instanceof List) {
                List<Object> objectList = (List<Object>)d.getValue();
                for(Object o:objectList) {
                    paramVerification.checkParameter(d.getKey(), o, mandatory);
                }
            } else {
                paramVerification.checkParameter(d.getKey(), d.getValue(), mandatory);
            }
        }

        return result;
    }

    public boolean verifyUserSession(String sessionKey, String serviceId, String accessToken, String userId) throws RequestHeaderException {
        return true;
    }

    public boolean verifyDeviceSession(String sessionKey, String serviceId) throws RequestHeaderException {
        return true;
    }
}
