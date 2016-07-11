package com.lge.hems.user.controller;

import com.google.gson.JsonObject;
import com.lge.hems.device.exceptions.DeviceControlRequestException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.UserDeviceMultipleBindingException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataUpdateException;
import com.lge.hems.device.exceptions.deviceinstance.NotRegisteredDeviceException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.model.common.InternalCommonKey;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceDataService;
import com.lge.hems.device.service.core.devicerelation.UserDeviceRelationService;
import com.lge.hems.device.service.core.verification.ParameterName;
import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.model.common.DeviceDataTagValue;
import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.controller.request.DeviceControlRequest;
import com.lge.hems.device.model.controller.request.DeviceDataUpdateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.service.core.verification.VerificationErrorCode;
import com.lge.hems.device.service.core.verification.VerificationService;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by netsga on 2016. 5. 24..
 */

@Controller
public class WebController {
	@RequestMapping("/")
	@ResponseBody
	public String home(Model model){
		model.addAttribute("name","hello springBoot");
		return "Home";
	}
	
	@RequestMapping("/index")
	public String indexPage(){
		return "index";
	}
	
	@RequestMapping("/success")
	public String successPage(){
		return "success";
	}
	
	@RequestMapping("/logout")
	public String logoutPage(){
		return "logout";
	}
	
	@RequestMapping("/join")
	public String joinPage(){
		return "join";
	}
}