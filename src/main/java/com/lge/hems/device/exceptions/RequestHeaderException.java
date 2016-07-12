package com.lge.hems.device.exceptions;

/**
 * Created by netsga on 2016. 5. 27..
 */
public class RequestHeaderException extends Exception {
    public RequestHeaderException() {
    }

    public RequestHeaderException(String message) {
        super(message);
    }

    public RequestHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHeaderException(Throwable cause) {
        super(cause);
    }
}
