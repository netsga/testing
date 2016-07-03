package com.lge.hems.device.model.controller.request;

import com.lge.hems.device.model.common.entity.ChildDeviceInformation;

/**
 * Created by netsga on 2016. 5. 26..
 */
public class DeviceRelationCreateRequest implements BaseRequest {
    private ChildDeviceInformation request;

    public ChildDeviceInformation getRequest() {
        return request;
    }

    public void setRequest(ChildDeviceInformation request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "request=" + request +
                '}';
    }
}
