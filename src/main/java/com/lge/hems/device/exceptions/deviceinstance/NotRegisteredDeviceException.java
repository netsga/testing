package com.lge.hems.device.exceptions.deviceinstance;

/**
 * Created by netsga on 2016. 6. 13..
 */
public class NotRegisteredDeviceException extends InstanceException {
    public NotRegisteredDeviceException(String logicalDeviceId) {
        super("Not registered device", logicalDeviceId);
    }
}
