package com.lge.hems.device.exceptions;

/**
 * Created by netsga on 2016. 6. 7..
 */
public class CheckModelExistenceException extends Exception {
    private String modelId;

    public CheckModelExistenceException(String modelId) {
        super();
        this.modelId = modelId;
    }

    public CheckModelExistenceException(String s, String modelId) {
        super(s);
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
