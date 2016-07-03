package com.lge.hems.device.exceptions.deviceinstance;

import java.util.Map;

/**
 * Created by netsga on 2016. 6. 30..
 */
public class NullLeafInformationException extends InstanceException {
    private String leafData;
    public NullLeafInformationException(String s, String logicalDeviceId, Map<String, String> leafData) {
        super(s, logicalDeviceId);
        this.leafData = leafData.toString();
    }
}
