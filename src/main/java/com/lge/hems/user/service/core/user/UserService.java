package com.lge.hems.user.service.core.user;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lge.hems.device.exceptions.ModelReadFailException;
import com.lge.hems.device.exceptions.NullModelException;
import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.exceptions.deviceinstance.AddDeviceInstanceDataException;
import com.lge.hems.device.exceptions.deviceinstance.DuplicateDeviceException;
import com.lge.hems.device.exceptions.deviceinstance.InstanceReadFailException;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.utilities.MD5;
import com.lge.hems.device.utilities.RestServiceUtil;
import com.lge.hems.user.model.JoinRequestForm;
import com.lge.hems.user.model.UserlInformation;
import com.lge.hems.user.service.dao.rds.UserDao;

@Repository
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
    private DeviceInstanceService deviceInstanceService;
	@Autowired
    private RestServiceUtil restServiceUtil;
	
	String THIRD_PARTY_VALIDATION_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";
	
	public List<UserlInformation> getUserInformation(String userId) {
		List<UserlInformation> userList = new ArrayList<UserlInformation>();
		
		userList = userDao.getUserInfo(userId);
		
		return userList;
	}
	
	public String createHemsId(String userId) throws Exception {
		String hemsId = MD5.getEncMD5(userId + ":" + System.currentTimeMillis());
		
		return hemsId;
	}
	
	public int registerUser(String hemsId, JoinRequestForm requestBody) {
		int result = userDao.registerUser(hemsId, requestBody);
		
		return result;
	}
	
	public int updateAccessToken(String hemsId, String accessToken) {
		int result = userDao.updateAccessToken(hemsId, accessToken);
		
		return result;
	}
	
	public String registerDeviceMapping(String hemsId, String emSn) throws ModelReadFailException, NullModelException, DuplicateDeviceException, AddDeviceInstanceDataException, InstanceReadFailException, NullRequestException {
		DeviceInstanceInformation gatewayInfo = new DeviceInstanceInformation();
		String result = null;
		
		gatewayInfo.setCreateTimestamp(System.currentTimeMillis());
		gatewayInfo.setDeviceId(emSn);
		gatewayInfo.setNameTag("Kiwigrid gateway");
		gatewayInfo.setServiceType("10001");
		gatewayInfo.setModelName("KIWIGRID/EMR");
		gatewayInfo.setDeviceType("energy.gateway");
		gatewayInfo.setVendor("KIWIGRID");
		
		result = deviceInstanceService.createDeviceInstance(gatewayInfo, hemsId);
		
		return result;
	}
	
	public String checkValidationIdByAccessToken(String token) throws Exception {
		String hemsId = null;
		String resultValid = null;
		String thirdPartyId = null;
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		
		String apiUrl = THIRD_PARTY_VALIDATION_URL + "?access_token=" + token;
		resultValid = restServiceUtil.callExternalApi(apiUrl);
		
		jsonObject = (JSONObject)jsonParser.parse(resultValid);
		thirdPartyId = (String)jsonObject.get("email");
		
		hemsId = getUserInformation(thirdPartyId).get(0).getHemsId();
		
		return hemsId;
	}
}