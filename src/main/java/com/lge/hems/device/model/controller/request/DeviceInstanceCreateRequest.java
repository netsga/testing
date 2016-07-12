package com.lge.hems.device.model.controller.request;

import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;

/**
 * Created by netsga on 2016. 5. 26..
 */
public class DeviceInstanceCreateRequest implements BaseRequest {
    private DeviceInstanceInformation request;

    public DeviceInstanceInformation getRequest() {
        return request;
    }

    public void setRequest(DeviceInstanceInformation request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "request=" + request +
                '}';
    }
}
