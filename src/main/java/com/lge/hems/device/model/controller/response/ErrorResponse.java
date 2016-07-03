package com.lge.hems.device.model.controller.response;

import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;

/**
 * Created by netsga on 2016. 5. 25..
 */
public class ErrorResponse implements Response {
    private String logicalDeviceId;
    private String errorId;
    private String errorKey;
    private String errorReason;
    private String errorMessage;

    public String getLogicalDeviceId() {
        return logicalDeviceId;
    }

    public void setLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
