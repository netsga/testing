package com.lge.hems.user.controller;

import com.lge.hems.user.model.JoinRequestForm;
import com.lge.hems.user.model.UserlInformation;
import com.lge.hems.user.service.core.user.*;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import com.lge.hems.user.service.dao.rds.UserDao;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by jaeeun.pyo on 2016. 7. 6..
 */
@RestController
@RequestMapping("/users")
public class UserController {
	@LoggerImpl
	private Logger logger;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserService userService;
	@Autowired
    private DeviceInstanceService deviceInstanceService;
	
	String KIWI_API_URL = "https://lgservice-lg.appdev.kiwigrid.com";		//Appdev
	//String KIWI_API_URL = "https://lgservice.telstra.hemsportal.com";		//telstra
	String KIWI_AVAILABILITY_CHECK_URL = "/embinding/checkEmCredentials";
	String KIWI_USER_REGISTRATION_URL = "/usermanagement/updateUserMetaInformation";
	String KIWI_EM_BINDING_URL = "/embinding/bindEmToUser";
	
	@RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
	public UserlInformation inquire(@PathVariable(value = "user_id") String user_id) throws Exception {
		List<UserlInformation> userList = new ArrayList<UserlInformation>();
		UserlInformation userInfo = null;
		
		userList = userDao.getUserInfo(user_id);
		
		if (!userList.isEmpty()) {
			userInfo = userList.get(0); 
		}
		
		//1. DB 조회 User 확인
		//2. ID 없으면 구글 실행 후 ID 등록
		//3. ID 있으면 Token 확인, token 시간 확인
		//4. 만료면 다시 구글 로그인. 토큰 다시 등록
		//5. 완료되면 main

		return userInfo;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public UserlInformation joinHems(@RequestBody JoinRequestForm userBody) throws Exception {
		UserlInformation userInfo = null;
		String resultAvailCheck = null;
		
		userService.createUserNo(userBody.getUserId());
		
		JSONObject json_user = new JSONObject();
		JSONObject json_em = new JSONObject();
		JSONArray list = new JSONArray();
		
		//Kiwigrid availability check	
		resultAvailCheck = callGetActionToKiwigrid(KIWI_API_URL + KIWI_AVAILABILITY_CHECK_URL + "?emSN=" + userBody.getEmSN() + "&emPassword=" + userBody.getEmPassword());
		
		//resultAvailCheck의 result 값이 False면 Return Error. 아니면 다음 Step 진행
		
		//Kiwigrid user registration
		json_user.put("givenName", userBody.getGivenName());
		json_user.put("familyName", userBody.getFamilyName());
		json_user.put("email", userBody.getEmail());
		list.add("USER");
		json_user.put("roles", list);
		callPostActionToKiwigrid(KIWI_API_URL + KIWI_USER_REGISTRATION_URL + "?userId=" + userBody.getUserId(), json_user);
		
		//Kiwigrid EM Binding
		json_em.put("userId", userBody.getUserId());
		json_em.put("emSN", userBody.getEmSN());
		json_em.put("emPassword", userBody.getEmPassword());
		callGetActionToKiwigrid(KIWI_API_URL + KIWI_EM_BINDING_URL);
		
		//DB 저장
		userDao.registerUser(userBody);
		
		DeviceInstanceInformation gatewayInfo = new DeviceInstanceInformation();
		gatewayInfo.setCreateTimestamp(System.currentTimeMillis());
		gatewayInfo.setDeviceId(userBody.getEmSN());
		gatewayInfo.setNameTag("Kiwigrid gateway");
		gatewayInfo.setServiceType("10001");
		gatewayInfo.setModelName("KIWIGRID/EMR");
		gatewayInfo.setDeviceType("energy.gateway");
		deviceInstanceService.createDeviceInstance(gatewayInfo, userBody.getUserId());
		
		return userInfo;
	}
	
	private void callPostActionToKiwigrid(String api_url, JSONObject json) throws Exception {
		    URL url = new URL(api_url);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(getFactory("/ad4-lg.p12", "kiwigrid"));		//AppDev
			//conn.setSSLSocketFactory(getFactory("/pilot-telstra.p12", "kiwigrid"));		//pilot-telstra
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", "68");
			conn.setDoOutput(true);
			conn.connect();
		  
		  OutputStreamWriter clsOutput = new OutputStreamWriter( conn.getOutputStream() );
		  
		  System.out.println(json.toString());
		  
		  clsOutput.write(json.toString());
		  clsOutput.flush();
		  BufferedReader clsInput = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		  String inputLine;
		  while ((inputLine = clsInput.readLine()) != null)
		  {
		   System.out.println(inputLine);
		  }

		  clsOutput.close();
		  clsInput.close();
	}

	private String callGetActionToKiwigrid(String api_url) {
		String pingUrl = api_url;
		String result = null;
		try {
			URL url = new URL(pingUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(getFactory("ad4-lg.p12", "kiwigrid"));		//AppDev
			//conn.setSSLSocketFactory(getFactory("/pilot-telstra.p12", "kiwigrid"));		//pilot-telstra
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			in.close();
			conn.disconnect();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		System.out.println(result);
		
		return result;
	}

	private SSLSocketFactory getFactory(String pKeyFile, String pKeyPassword) throws Exception {
		String path = this.getClass().getResource("").getPath();
		InputStream keyInput = new FileInputStream(path + pKeyFile);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(keyInput, pKeyPassword.toCharArray());
		keyInput.close();

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

		SSLContext context = SSLContext.getInstance("TLS");
		// context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

		// add TrustManager
		X509TrustManager trustManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				try {
					KeyStore trustStore = KeyStore.getInstance("JKS");
					String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts"; // Trust store path should be different by system platform.
					trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray()); // Use default certification validation

					// Get Trust Manager
					TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(trustStore);
					TrustManager[] tms = tmf.getTrustManagers();
					((X509TrustManager) tms[0]).checkServerTrusted(arg0, arg1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		context.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { trustManager }, new SecureRandom());
		return context.getSocketFactory();
	}
}