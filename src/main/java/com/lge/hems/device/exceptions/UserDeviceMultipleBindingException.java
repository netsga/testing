package com.lge.hems.device.exceptions;

/**
 * Created by netsga on 2016. 6. 16..
 */
public class UserDeviceMultipleBindingException extends Exception {
    private String logicalDeviceId;
    private String userId;

    public UserDeviceMultipleBindingException(String logicalDeviceId, String userId) {
        this.logicalDeviceId = logicalDeviceId;
        this.userId = userId;
    }

    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    public void setLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
