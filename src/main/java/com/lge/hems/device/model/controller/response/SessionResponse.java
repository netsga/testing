package com.lge.hems.device.model.controller.response;

/**
 * Created by netsga on 2016. 5. 27..
 */
public class SessionResponse implements Response {
    private Boolean validSession;
    private Boolean validKey;
    private Boolean available;
    private String sessionKey;

    public SessionResponse() {
    }

    public SessionResponse(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public SessionResponse(Boolean validSession, Boolean validKey, Boolean available) {
        this.validSession = validSession;
        this.validKey = validKey;
        this.available = available;
    }

    public Boolean getValidSession() {
        return validSession;
    }

    public void setValidSession(Boolean validSession) {
        this.validSession = validSession;
    }

    public Boolean getValidKey() {
        return validKey;
    }

    public void setValidKey(Boolean validKey) {
        this.validKey = validKey;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public String toString() {
        return "SessionResponse{" +
                "validSession=" + validSession +
                ", validKey=" + validKey +
                ", available=" + available +
                '}';
    }
}
