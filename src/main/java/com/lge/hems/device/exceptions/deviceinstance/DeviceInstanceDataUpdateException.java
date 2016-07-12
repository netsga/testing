package com.lge.hems.device.exceptions.deviceinstance;

import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.InstanceException;

import java.util.Map;

/**
 * Created by netsga on 2016. 6. 27..
 */
public class DeviceInstanceDataUpdateException extends InstanceException {
    private Map<String ,Object> updateData;
    private String key;
    private String data;

    public DeviceInstanceDataUpdateException(String s, String logicalDeviceId, Map<String, Object> updateData) {
        super(s, logicalDeviceId);
        this.updateData = updateData;
    }

    public DeviceInstanceDataUpdateException(String s, String logicalDeviceId, String key, String data) {
        super(s, logicalDeviceId);
        this.key = key;
        this.data = data;
    }

    public DeviceInstanceDataUpdateException(String s, String logicalDeviceId, String key) {
        super(s, logicalDeviceId);
        this.key = key;
    }

    public DeviceInstanceDataUpdateException(String s, String logicalDeviceId, Map<String, Object> updateData, Exception e) {
        super(s, logicalDeviceId, e);
        this.updateData = updateData;
    }

    public DeviceInstanceDataUpdateException(String s, String logicalDeviceId, Exception e) {
        super(s, logicalDeviceId, e);
    }

    public Map<String, Object> getUpdateData() {
        return updateData;
    }

    public void setUpdateData(Map<String, Object> updateData) {
        this.updateData = updateData;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
