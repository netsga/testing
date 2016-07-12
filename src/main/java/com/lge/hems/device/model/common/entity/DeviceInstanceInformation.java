package com.lge.hems.device.model.common.entity;

import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.customize.GsonExclude;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by netsga on 2016. 5. 24..
 */
@Entity(name = "tbl_device_instance_information")
@Inheritance(strategy = InheritanceType.JOINED)
public class DeviceInstanceInformation {
    @Id
    @Column(length = 128, unique = true, nullable = false)
    @GsonExclude
    private String logicalDeviceId;

    @Column(length = 100, unique = false, nullable = false)
    private String deviceType;

    @Column(length = 128, unique = false, nullable = false)
    private String deviceId;

    @Column(unique = false, nullable = false)
    private String serviceType;

    @Column(unique = false, nullable = false)
    private String modelName;

    @Column(unique = false, nullable = true)
    private String nameTag;

    @Column(unique = false, nullable = true)
    private String subNameTag;

    @Column(unique = false, nullable = true)
    private String subSubNameTag;

    @Column(unique = false, nullable = false)
    private String vendor;

    @Column(unique = false, nullable = false)
    private String version;

    @Column(unique = false, nullable = true)
    private String location;

    @Column(unique = false, nullable = false)
    @GsonExclude
    private String modelId;

    @Column(unique = false, nullable = false)
    @GsonExclude
    private Long createTimestamp;

    @OneToOne()
    @JoinColumn(name="binding_info", nullable = true)
    @GsonExclude
    private UserDeviceBindingInfo bindingInfo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "deviceInstanceInformation")
    @GsonExclude
    private Set<ChildDeviceInformation> childDeviceInformations;

    public DeviceInstanceInformation() {
    }

    public DeviceInstanceInformation(String logicalDeviceId, String deviceType) {
        this.logicalDeviceId = logicalDeviceId;
        this.deviceType = deviceType;
    }

    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    public void setLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
    }

    public Set<ChildDeviceInformation> getChildDeviceInformations() {
        return childDeviceInformations;
    }

    public void setChildDeviceInformations(Set<ChildDeviceInformation> childDeviceInformations) {
        this.childDeviceInformations = childDeviceInformations;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public String getSubNameTag() {
        return subNameTag;
    }

    public void setSubNameTag(String subNameTag) {
        this.subNameTag = subNameTag;
    }

    public String getSubSubNameTag() {
        return subSubNameTag;
    }

    public void setSubSubNameTag(String subSubNameTag) {
        this.subSubNameTag = subSubNameTag;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public UserDeviceBindingInfo getBindingInfo() {
        return bindingInfo;
    }

    public void setBindingInfo(UserDeviceBindingInfo bindingInfo) {
        this.bindingInfo = bindingInfo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean addDeviceRelation(ChildDeviceInformation e) {
        if(this.childDeviceInformations == null) {
            this.childDeviceInformations = CollectionFactory.newSet();
        }
        return this.childDeviceInformations.add(e);
    }

    public boolean isEmpty() {
        return this.childDeviceInformations.isEmpty();
    }

    @Override
    public String toString() {
        return "DeviceInstanceInformation{" +
                "logicalDeviceId='" + logicalDeviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", modelName='" + modelName + '\'' +
                ", nameTag='" + nameTag + '\'' +
                ", subNameTag='" + subNameTag + '\'' +
                ", subSubNameTag='" + subSubNameTag + '\'' +
                ", vendor='" + vendor + '\'' +
                ", version='" + version + '\'' +
                ", modelId='" + modelId + '\'' +
                ", location='" + location + '\'' +
                ", createTimestamp=" + createTimestamp +
                '}';
    }

}
