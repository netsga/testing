package com.lge.hems.device.model.controller.request;

import com.lge.hems.device.model.common.DeviceModelInformation;

/**
 * Created by netsga on 2016. 5. 26..
 */
public class DeviceModelCreateRequest implements BaseRequest {
    private DeviceModelInformation request;

    public DeviceModelInformation getRequest() {
        return request;
    }

    public void setRequest(DeviceModelInformation request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "request=" + request +
                '}';
    }
}
