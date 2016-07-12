package com.lge.hems.device.model.common.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by netsga on 2016. 6. 13..
 */
@Entity(name = "tbl_user_device_binding_info")
public class UserDeviceBindingInfo {
    private String userId;

    @Id
    private String logicalDeviceId;

    @OneToOne(fetch=FetchType.LAZY, mappedBy="bindingInfo")
    private DeviceInstanceInformation deviceInstanceInformation;

    public UserDeviceBindingInfo() {
    }

    public UserDeviceBindingInfo(String logicalDeviceId, String userId) {
        this.logicalDeviceId = logicalDeviceId;
        this.userId = userId;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    public void setLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
    }

    public DeviceInstanceInformation getDeviceInstanceInformation() {
        return deviceInstanceInformation;
    }

    public void setDeviceInstanceInformation(DeviceInstanceInformation deviceInstanceInformation) {
        this.deviceInstanceInformation = deviceInstanceInformation;
    }

    @Override
    public String toString() {
        return "UserDeviceBindingInfo{" +
                "userId='" + userId + '\'' +
                ", logicalDeviceId='" + logicalDeviceId + '\'' +
                '}';
    }
}
