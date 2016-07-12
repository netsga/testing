package com.lge.hems.device.exceptions.deviceinstance;

import java.util.List;

/**
 * Created by netsga on 2016. 6. 22..
 */
public class DeviceInstanceDataReadException extends InstanceException {
    private String logicalDeviceId;
    private List<String> keys;

    public DeviceInstanceDataReadException(String s, String logicalDeviceId, List<String> keys) {
        super(s, logicalDeviceId);
        this.logicalDeviceId = logicalDeviceId;
        this.keys = keys;
    }

    public DeviceInstanceDataReadException(String s, String logicalDeviceId, List<String> keys, Throwable t) {
        super(s, logicalDeviceId, t);
        this.logicalDeviceId = logicalDeviceId;
        this.keys = keys;
    }

    public DeviceInstanceDataReadException(String s, String logicalDeviceId) {
        super(s, logicalDeviceId);
        this.logicalDeviceId = logicalDeviceId;
    }
}
