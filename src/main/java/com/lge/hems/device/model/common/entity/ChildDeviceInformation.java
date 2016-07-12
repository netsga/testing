package com.lge.hems.device.model.common.entity;

import javax.persistence.*;

/**
 * Created by netsga on 2016. 5. 24..
 */
@Entity(name = "tbl_child_device_information")
public class ChildDeviceInformation {
    @Id
    @Column(name="logical_device_id", length = 128, unique = true, nullable = false)
    private String logicalDeviceId;
    @Column(name="device_type", length = 100, nullable = false)
    private String deviceType;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private DeviceInstanceInformation deviceInstanceInformation;

    public ChildDeviceInformation() {
    }

    public ChildDeviceInformation(String deviceType, String logicalDeviceId) {
        this.deviceType = deviceType;
        this.logicalDeviceId = logicalDeviceId;
    }

    public ChildDeviceInformation(String logicalDeviceId, String deviceType, DeviceInstanceInformation deviceInstanceInformation) {
        this.logicalDeviceId = logicalDeviceId;
        this.deviceType = deviceType;
        this.deviceInstanceInformation = deviceInstanceInformation;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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
        return "ChildDeviceInformation{" +
                "deviceType='" + deviceType + '\'' +
                ", logicalDeviceId='" + logicalDeviceId + '\'' +
                '}';
    }
}
