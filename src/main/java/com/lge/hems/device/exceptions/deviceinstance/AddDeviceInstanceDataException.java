package com.lge.hems.device.exceptions.deviceinstance;

import com.lge.hems.device.exceptions.deviceinstance.InstanceException;

/**
 * Created by netsga on 2016. 6. 11..
 */
public class AddDeviceInstanceDataException extends InstanceException {
    public AddDeviceInstanceDataException(String s) {
        super(s);
    }
    public AddDeviceInstanceDataException(String s, String logicalDeviceId) {
        super(s, logicalDeviceId);
    }
    public AddDeviceInstanceDataException(String s, Throwable t) {
        super(s, t);
    }
}
