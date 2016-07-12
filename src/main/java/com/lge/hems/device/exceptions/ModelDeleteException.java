package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.base.ModelException;

/**
 * Created by netsga on 2016. 6. 9..
 */
public class ModelDeleteException extends ModelException {

    public ModelDeleteException(String s, String modelId) {
        super(s, modelId);
    }
}
