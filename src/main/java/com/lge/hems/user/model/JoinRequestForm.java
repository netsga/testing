package com.lge.hems.user.model;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by pje on 2016. 7. 7..
 */
public class JoinRequestForm {
    private String userId;
    private String accessToken;
    private String accountType;
    private String givenName;
    private String familyName;
    private String email;
    private String emSN;
    private String emPassword;
    
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
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmSN() {
		return emSN;
	}
	public void setEmSN(String emSN) {
		this.emSN = emSN;
	}
	public String getEmPassword() {
		return emPassword;
	}
	public void setEmPassword(String emPassword) {
		this.emPassword = emPassword;
	}
}