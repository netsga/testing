package com.lge.hems.device.service.core.deviceinstance.converter;

import com.jayway.jsonpath.JsonPath;
import com.lge.hems.device.utilities.customize.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by netsga on 2016. 6. 28..
 */
@Component
public class JsonMessageConverter implements ExternalConverter {
    @Autowired
    private JsonConverter conv;

    public Object getValueFromMessage(String message, String path) {
        Object obj = JsonPath.read(message, path);
        return obj;
    }
}
