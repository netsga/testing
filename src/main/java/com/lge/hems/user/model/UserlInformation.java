package com.lge.hems.user.model;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by pje on 2016. 7. 7..
 */
public class UserlInformation {
	private String hemsId;
    private String userId;
    private String accountType;
    private String accessToken;
    private String userRegDate;
    
	public String getHemsId() {
		return hemsId;
	}
	public void setHemsId(String hemsId) {
		this.hemsId = hemsId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUserRegDate() {
		return userRegDate;
	}
	public void setUserRegDate(String userRegDate) {
		this.userRegDate = userRegDate;
	}
}
