package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.base.ModelException;

/**
 * Created by netsga on 2016. 6. 7..
 */
public class ModelCreateException extends ModelException {
    public ModelCreateException(String s) {
        super(s);
    }

    public ModelCreateException(String s, String modelId) {
        super(s, modelId);
    }
}
