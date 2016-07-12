package com.lge.hems.device.exceptions.deviceinstance;

import com.lge.hems.device.exceptions.deviceinstance.InstanceException;

/**
 * Created by netsga on 2016. 6. 11..
 */
public class NullInstanceException extends InstanceException {

    public NullInstanceException(String s, String logicalDeviceId) {
        super(s, logicalDeviceId);
    }
}
