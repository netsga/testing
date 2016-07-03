package com.lge.hems.device.model.common;

/**
 * Created by netsga on 2016. 5. 24..
 */
public enum ResultCode {
    SUCCESS(200),
    PARAMETER_ERROR(400),
    FAIL(503);

    private Integer resultCode;
    ResultCode(Integer arg){
        this.resultCode = arg;
    }
    public Integer getResultCode(){
        return resultCode;
    }
}
