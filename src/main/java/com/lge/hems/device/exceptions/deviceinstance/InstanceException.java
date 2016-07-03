package com.lge.hems.device.exceptions.deviceinstance;

/**
 * Created by netsga on 2016. 6. 11..
 */
public class InstanceException extends Exception{
    private String logicalDeviceId;

    public InstanceException() {
        super();
    }

    public InstanceException(String s) {
        super(s);
    }

    public InstanceException(String s, String logicalDeviceId) {
        super(s);
        this.logicalDeviceId = logicalDeviceId;
    }

    public InstanceException(Throwable t) {
        super(t);
    }

    public InstanceException(String s, Throwable t) {
        super(s, t);
    }

    public InstanceException(String s, String logicalDeviceId, Throwable t) {
        super(s, t);
        this.logicalDeviceId = logicalDeviceId;
    }

    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    public void setLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
    }
}
