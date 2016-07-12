package com.lge.hems.device.model.controller.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

/**
 * Created by netsga on 2016. 5. 25..
 */
public class BaseResponse {
    private Integer resultCode;

    private Object result;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result =result;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "resultCode=" + resultCode +
                ", result=" + result +
                '}';
    }
}
