package com.lge.hems.device.model.controller.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by netsga on 2016. 5. 27..
 */
public class ServiceRequestHeader {
    @SerializedName("x-session-key")
    private String sessionKey;
    @SerializedName("x-service-id")
    private String serviceId;
    @SerializedName("hems-user-id")
    private String userId;

    // 3rd party access token
    @SerializedName("3rd-access-token")
    private String accessToken;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "ServiceRequestHeader{" +
                "sessionKey='" + sessionKey + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
