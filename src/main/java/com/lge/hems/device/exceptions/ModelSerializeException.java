package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.base.ModelException;

/**
 * Created by netsga on 2016. 6. 7..
 */
public class ModelSerializeException extends ModelException {
    public ModelSerializeException(String s) {
        super(s);
    }
    public ModelSerializeException(String s, String modelId) {
        super(s, modelId);
    }

}
