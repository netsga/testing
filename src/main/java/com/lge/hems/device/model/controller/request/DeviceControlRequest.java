package com.lge.hems.device.model.controller.request;

import com.lge.hems.device.model.common.DeviceDataTagValue;

import java.util.Map;

/**
 * Created by netsga on 2016. 5. 26..
 */
public class DeviceControlRequest implements BaseRequest {
    private Map<String, Object> request;

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "DeviceControlRequest{" +
                "request=" + request +
                '}';
    }
}
