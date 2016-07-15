package com.lge.hems.user.service.dao.rds;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lge.hems.user.model.JoinRequestForm;
import com.lge.hems.user.model.UserlInformation;;

@Repository
public class UserDao {
	private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);
	
	@Autowired
	private JdbcTemplate template;
	
	public List<UserlInformation> getUserInfo(String user_id) {
		String query = "SELECT * FROM tbl_user_information WHERE user_id=?";
		return (List<UserlInformation>) template.query(query, new Object[] { user_id }, new BeanPropertyRowMapper(UserlInformation.class));
	}
	
	public int registerUser(String hemsId, JoinRequestForm requestBody) {
		String query = "INSERT INTO tbl_user_information ("
															   + "hems_id, "
															   + "user_id, "
															   + "account_type, "
															   + "access_token"
															   + ") "
															   + "VALUES(?,?,?,?)";
		return template.update(query, hemsId.toUpperCase(),
									  requestBody.getUserId(), 
									  requestBody.getAccountType(), 
									  requestBody.getAccessToken()
				);
	}
	
	public int updateAccessToken(String hemsId, String accessToken) {
		String query = "UPDATE tbl_user_information SET "
															   + "access_token = ? "
															   + "WHERE hems_id = ?";
		return template.update(query, accessToken,
									  hemsId
				);
	}
}