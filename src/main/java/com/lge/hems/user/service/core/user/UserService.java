package com.lge.hems.user.service.core.user;

import org.springframework.stereotype.Repository;

@Repository
public class UserService {
	public String createUserNo(String userId) {
		System.out.println("System.currentTimeMillis():"+System.currentTimeMillis());
		return userId;
	}

}
