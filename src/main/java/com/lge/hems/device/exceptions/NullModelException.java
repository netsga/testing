package com.lge.hems.device.exceptions;

import com.lge.hems.device.exceptions.base.ModelException;

/**
 * Created by netsga on 2016. 6. 7..
 */
public class NullModelException extends ModelException {
    public NullModelException() {
        super();
    }

    public NullModelException(String s) {
        super(s);
    }

    public NullModelException(String s, String modelId) {
        super(s, modelId);
    }
}
