package com.lge.hems.device.model.common.entity;

import javax.persistence.*;

/**
 * Created by netsga on 2016. 5. 27..
 */
@Entity(name = "tbl_certification_information")
public class CertificationInformation {
    @Id
    private String keyName;
    private String password;
    
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "CertificationInformation [keyName=" + keyName + ", password=" + password + "]";
	}
}
