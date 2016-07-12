package com.lge.hems.device.model.common;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by netsga on 2016. 5. 25..
 */
public class DeviceModelInformation implements Serializable, Comparable {
    private String id;
    private String modelName;
    private String vendor;
    private String version;
    private String deviceType;
    private long timestamp;
    private JsonElement model;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public JsonElement getModel() {
        return model;
    }

    public void setModel(JsonElement model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceModelInformation that = (DeviceModelInformation) o;

        if (!id.equals(that.id)) return false;
        if (!modelName.equals(that.modelName)) return false;
        if (!vendor.equals(that.vendor)) return false;
        if (!version.equals(that.version)) return false;
        if (!deviceType.equals(that.deviceType)) return false;
        return model.equals(that.model);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + modelName.hashCode();
        result = 31 * result + vendor.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + deviceType.hashCode();
        result = 31 * result + model.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DeviceModelInformation{" +
                "id='" + id + '\'' +
                ", modelName='" + modelName + '\'' +
                ", vendor='" + vendor + '\'' +
                ", version='" + version + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", timestamp=" + timestamp +
                ", model=" + model +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if(timestamp > ((DeviceModelInformation)o).getTimestamp()) {
            return 1;
        } else if (timestamp < ((DeviceModelInformation)o).getTimestamp()) {
            return -1;
        } else {
            return 0;
        }
    }
}
