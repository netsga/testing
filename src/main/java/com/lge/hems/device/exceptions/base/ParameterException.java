package com.lge.hems.device.exceptions.base;

/**
 * Created by netsga on 2016. 6. 9..
 */
public class ParameterException extends Exception {
    private String key;

    public ParameterException() {
        super();
    }

    public ParameterException(String s) {
        super(s);
    }

    public ParameterException(Throwable t) {
        super(t);
    }

    public ParameterException(String s, String key) {
        super(s);
        this.key = key;
    }

    public ParameterException(String s, String key, Throwable t) {
        super(s, t);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
