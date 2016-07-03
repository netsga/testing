package com.lge.hems.device.model.controller.response;

import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;

/**
 * Created by netsga on 2016. 5. 25..
 */
public class DeviceInstanceResponse implements Response {
    private String logicalDeviceId;
    private String registeredDate;
    private String sessionKey;
    private DeviceInstanceInformation deviceInformation;

    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    public void setLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public DeviceInstanceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(DeviceInstanceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }
}
