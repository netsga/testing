package com.lge.hems.device.model.controller.request;

import com.lge.hems.device.model.common.DeviceDataTagValue;
import com.lge.hems.device.model.controller.request.BaseRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by netsga on 2016. 5. 26..
 */
public class DeviceDataUpdateRequest implements BaseRequest {
    public Map<String, Object> request;

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "DeviceDataUpdateRequest{" +
                "request=" + request +
                '}';
    }
}
