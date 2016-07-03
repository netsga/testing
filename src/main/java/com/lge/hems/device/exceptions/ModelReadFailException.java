package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.base.ModelException;

/**
 * Created by netsga on 2016. 6. 8..
 */
public class ModelReadFailException extends ModelException {

    public ModelReadFailException(String s) {
        super(s);
    }

    public ModelReadFailException(String s, String modelId) {
        super(s, modelId);
    }

    public ModelReadFailException(String s, String modelId, Exception e) {
        super(s, modelId, e);
    }
}
