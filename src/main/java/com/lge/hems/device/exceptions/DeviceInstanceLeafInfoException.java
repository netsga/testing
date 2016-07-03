package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.deviceinstance.InstanceException;

/**
 * Created by netsga on 2016. 6. 30..
 */
public class DeviceInstanceLeafInfoException extends InstanceException {
    private String param;

    public DeviceInstanceLeafInfoException(String msg) {
        super(msg);
    }

    public DeviceInstanceLeafInfoException(String msg, String logicalDeviceId, String param) {
        super(msg, logicalDeviceId);
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
