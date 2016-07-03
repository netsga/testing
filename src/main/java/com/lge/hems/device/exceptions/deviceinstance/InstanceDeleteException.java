package com.lge.hems.device.exceptions.deviceinstance;

/**
 * Created by netsga on 2016. 6. 11..
 */
public class InstanceDeleteException extends InstanceException {
    public InstanceDeleteException(String logicalDeviceId) {
        super(null, logicalDeviceId);
    }
    public InstanceDeleteException(String logicalDeviceId, Exception e) {
        super(null, logicalDeviceId, e);
    }
}
