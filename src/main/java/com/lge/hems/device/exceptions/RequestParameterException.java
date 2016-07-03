package com.lge.hems.device.exceptions;

import com.lge.hems.device.service.core.verification.VerificationErrorCode;

/**
 * Created by netsga on 2016. 5. 27..
 */
//@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="No such Order")
public class RequestParameterException extends Exception {
    private String parameterName;
    private VerificationErrorCode reason;

    public RequestParameterException() {
    }

    public RequestParameterException(String message) {
        super(message);
    }

    public RequestParameterException(String message, String parameterName, VerificationErrorCode reason) {
        super(message);
        this.parameterName = parameterName;
        this.reason = reason;
    }

    public RequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestParameterException(Throwable cause) {
        super(cause);
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public VerificationErrorCode getReason() {
        return reason;
    }

    public void setReason(VerificationErrorCode reason) {
        this.reason = reason;
    }
}
