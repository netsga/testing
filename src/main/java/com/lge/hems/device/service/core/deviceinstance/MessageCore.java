package com.lge.hems.device.service.core.deviceinstance;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by netsga on 2016. 6. 28..
 */
@Component
public class MessageCore {
    public String createLogicalDeviceId(String deviceId) {
        return UUID.randomUUID().toString();
    }
}
