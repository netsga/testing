package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.deviceinstance.InstanceException;

/**
 * Created by netsga on 2016. 6. 27..
 */
public class DeviceControlRequestException extends InstanceException {
    public DeviceControlRequestException(String s, String logicalDeviceId) {
        super(s, logicalDeviceId);
    }
}
