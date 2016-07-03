package com.lge.hems.device.exceptions.deviceinstance;

/**
 * Created by netsga on 2016. 6. 11..
 */
public class InstanceReadFailException extends InstanceException {
    public InstanceReadFailException(String s, String logicalDeviceId) {
        super(s, logicalDeviceId);
    }
}
