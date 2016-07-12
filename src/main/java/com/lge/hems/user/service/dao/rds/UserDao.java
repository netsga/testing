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
	
	/*
	public List<Mode> getModeList(String user_id) {
		String query = "SELECT * FROM " + getPrefixDB() + "lasdb.mode_additional_data WHERE user_id=?";
		return (List<Mode>) template.query(query, new Object[] { user_id }, new BeanPropertyRowMapper(Mode.class));
	}
	
	public String getTemplateRecipe(String templateRecipe) {
		String query = "SELECT recipe_meta FROM " + getPrefixDB() + "rcdb.re_recipe_content_" + region + "_" + country_code + "_" + phase + " WHERE recipe_id=?";
		return template.queryForObject(query, new Object[] { templateRecipe }, String.class);
	}

	public int registerMode(Mode requestBody) {
		String query = "INSERT INTO " + getPrefixDB() + "lasdb.mode_additional_data("
															   + "user_id, "
															   + "title, "
															   + "event_id, "
															   + "condition_data_type1, "
															   + "condition_data1, "
															   + "condition_data_type2, "
															   + "condition_data2"
															   + ") "
															   + "VALUES(?,?,?,?,?,?,?)";
		return template.update(query, requestBody.getUser_id(), 
									  requestBody.getTitle(), 
									  requestBody.getEvent_id(), 
									  requestBody.getCondition_data_type1(),
									  requestBody.getCondition_data1(),
									  requestBody.getCondition_data_type2(),
									  requestBody.getCondition_data2()
				);
	}
	
	public int modifyMode(Mode requestBody, int mode_no) {
		String query = "UPDATE " + getPrefixDB() + "lasdb.mode_additional_data SET "
															   + "mode_flag = ?, "
															   + "title = ?, "
															   + "event_id = ?, "
															   + "condition_data_type1 = ?, "
															   + "condition_data1 = ?, "
															   + "condition_data_type2 = ?, "
															   + "condition_data2 = ? "
															   + "WHERE mode_no = ?";
		return template.update(query, requestBody.getMode_flag(),
									  requestBody.getTitle(),
									  requestBody.getEvent_id(), 
									  requestBody.getCondition_data_type1(),
									  requestBody.getCondition_data1(),
									  requestBody.getCondition_data_type2(),
									  requestBody.getCondition_data2(),
									  mode_no
				);
	}
	
	public int deleteModeByModeId(int mode_no) {
		String query = "DELETE FROM " + getPrefixDB() + "lasdb.mode_additional_data WHERE mode_no=?";
		return template.update(query, mode_no);
	}
	
	public int getRecipeCountByRecipeId(String recipe_id) {
		String query = "SELECT count(*) FROM " + getPrefixDB() + "rcdb.re_recipe_content_" + region + "_" + country_code + "_" + phase + " WHERE recipe_id=?";
		return template.queryForObject(query, new Object[] { recipe_id }, Integer.class);
	}
	*/
}