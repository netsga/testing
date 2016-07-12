package com.lge.hems.device.model.common;

/**
 * Created by netsga on 2016. 5. 26..
 */
public class DeviceDataTagValue {
    private String tagName;
    private Object value;

    public DeviceDataTagValue() {
    }

    public DeviceDataTagValue(String tagName, Object value) {
        this.tagName = tagName;
        this.value = value;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DeviceDataTagValue{" +
                "tagName='" + tagName + '\'' +
                ", value=" + value +
                '}';
    }
}
