package com.lge.hems.device.exceptions.base;

/**
 * Created by netsga on 2016. 6. 9..
 */
public class ModelException extends Exception {
    private String modelId;

    public ModelException() {
        super();
    }

    public ModelException(String s) {
        super(s);
    }

    public ModelException(Throwable t) {
        super(t);
    }

    public ModelException(String s, String modelId) {
        super(s);
        this.modelId = modelId;
    }

    public ModelException(String s, String modelId, Throwable t) {
        super(s, t);
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
