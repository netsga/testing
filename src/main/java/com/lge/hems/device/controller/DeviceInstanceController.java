package com.lge.hems.device.controller;

import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.NotRegisteredDeviceException;
import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.controller.request.DeviceInstanceCreateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.model.controller.response.DeviceInstanceResponse;
import com.lge.hems.device.model.controller.response.Response;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.service.core.devicerelation.UserDeviceRelationService;
import com.lge.hems.device.service.core.verification.ParameterName;
import com.lge.hems.device.service.core.verification.VerificationService;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import com.lge.hems.user.service.core.user.UserService;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RestController
@RequestMapping("/devices")
public class DeviceInstanceController {
    @LoggerImpl
    private Logger logger;

    // beans
    @Autowired
    private JsonConverter gson;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private DeviceInstanceService deviceInstanceService;
    @Autowired(required=false)
    private HttpServletRequest httpRequest;
    @Autowired
    private UserDeviceRelationService userDeviceRelationService;
    @Autowired
    private UserService userService;

    // member variables
    private SimpleDateFormat sdf;

    @PostConstruct
    private void init() {
        sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.sss");
    }


    /**
     * Device instance 생성하고 특정 storage에 저장한다.
     * key는 logical device id가 된다.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=utf-8", consumes = "application/json;charset=utf-8")
    public BaseResponse createDeviceInstance(@RequestBody DeviceInstanceCreateRequest request) throws Exception {
        BaseResponse result = new BaseResponse();
        DeviceInstanceResponse resultContent = new DeviceInstanceResponse();
        DeviceInstanceInformation deviceInfo = request.getRequest();
        String createLdId;

        if(deviceInfo == null) {
            throw new RequestParameterException("Request content is null");
        }

        // parameter verification step
        verificationService.verifyParameters(true, new SimpleEntry(ParameterName.DEVICE_ID, deviceInfo.getDeviceId()),
                new SimpleEntry(ParameterName.DEVICE_TYPE, deviceInfo.getDeviceType()),
                new SimpleEntry(ParameterName.MODEL_NAME, deviceInfo.getModelName()),
                new SimpleEntry(ParameterName.VENDOR, deviceInfo.getVendor()),
                new SimpleEntry(ParameterName.SERVICE_TYPE, deviceInfo.getServiceType()));

        verificationService.verifyParameters(false, new SimpleEntry(ParameterName.NAME_TAG, deviceInfo.getNameTag()),
                new SimpleEntry(ParameterName.SUB_NAME_TAG, deviceInfo.getSubNameTag()),
                new SimpleEntry(ParameterName.SUBSUB_NAME_TAG, deviceInfo.getSubSubNameTag()),
                new SimpleEntry(ParameterName.VERSION, deviceInfo.getVersion()),
                new SimpleEntry(ParameterName.LOGICAL_DEVICE_ID, deviceInfo.getLogicalDeviceId()));

        // user verification step
        String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
    	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
    	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
    	
        try {
            createLdId = deviceInstanceService.createDeviceInstance(deviceInfo, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        resultContent.setLogicalDeviceId(createLdId);
        resultContent.setDeviceInformation(deviceInfo);
        resultContent.setRegisteredDate(sdf.format(new Date(deviceInfo.getCreateTimestamp())));
//        deviceInfo.setHideFieldForJson();

//        resultContent.setSessionKey("ttttt");
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        result.setResult(resultContent);
        return result;
    }

    /**
     * service type, device type, model name을 받아 자기 자신에게 등록된 장치 중 일치하는 장치 정보를 응답해줌.
     *
     * @param serviceType
     * @param deviceType
     * @param modelName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse searchDeviceInformation(@RequestParam(required=false) String serviceType, @RequestParam(required=false) String deviceType, @RequestParam(required=false) String modelName, @RequestParam(required=false) String deviceId) throws Exception {
        BaseResponse result = new BaseResponse();
        List<Response> contentList = CollectionFactory.newList();

        // parameter verification step
        verificationService.verifyParameters(false, new SimpleEntry(ParameterName.DEVICE_TYPE, deviceType),
                new SimpleEntry(ParameterName.MODEL_NAME, modelName),
                new SimpleEntry(ParameterName.SERVICE_TYPE, serviceType),
                new SimpleEntry(ParameterName.DEVICE_ID, deviceId));


        // user verification step
        String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
    	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
    	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
    	
        List<DeviceInstanceInformation> resultContents = deviceInstanceService.searchDeviceInstances(userId, serviceType, deviceType, modelName, deviceId);

        for(DeviceInstanceInformation info: resultContents) {
            DeviceInstanceResponse content = new DeviceInstanceResponse();
            content.setLogicalDeviceId(info.getLogicalDeviceId());
            content.setRegisteredDate(sdf.format(new Date(info.getCreateTimestamp())));

            content.setDeviceInformation(info);
            contentList.add(content);
        }

        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        result.setResult(contentList);

        return result;
    }

    /**
     * 생성 당시 받았던 Logical device id를 사용하여 해당 장치의 기본 정보를 받아오는 method
     *
     * @param logicalDeviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{logicalDeviceId}", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse readDeviceInformation(@PathVariable String logicalDeviceId) throws Exception {
        BaseResponse result = new BaseResponse();
        DeviceInstanceResponse resultContent = new DeviceInstanceResponse();

        // parameter verification step
        verificationService.verifyParameters(true, ParameterName.LOGICAL_DEVICE_ID, logicalDeviceId);

        // user verification step
        String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
    	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
    	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
    	
        if(!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
            throw new NotRegisteredDeviceException(logicalDeviceId);
        }

        // read device information
        DeviceInstanceInformation deviceInfo = deviceInstanceService.readDeviceInstanceInformation(logicalDeviceId);

        // create result
        resultContent.setLogicalDeviceId(deviceInfo.getLogicalDeviceId());
        resultContent.setDeviceInformation(deviceInfo);
        resultContent.setRegisteredDate(sdf.format(new Date(deviceInfo.getCreateTimestamp())));
//        deviceInfo.setHideFieldForJson();

        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        result.setResult(resultContent);

        return result;
    }

    /**
     * 생성 당시 받았던 logical device id를 사용하여 해당 장치를 삭제하는 method
     *
     * @param logicalDeviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{logicalDeviceId}", method = RequestMethod.DELETE, produces = "application/json")
    public BaseResponse deleteDeviceInformation(@PathVariable String logicalDeviceId) throws Exception {
        BaseResponse result = new BaseResponse();
        // parameter verification step
        verificationService.verifyParameters(true, ParameterName.LOGICAL_DEVICE_ID, logicalDeviceId);

        // user verification step
        String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
    	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
    	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
    	
        if(!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
            throw new NotRegisteredDeviceException(logicalDeviceId);
        }

        Boolean deleteResp = deviceInstanceService.deleteDeviceInstance(logicalDeviceId, userId);
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        result.setResult(deleteResp);
        return result;
    }

    /**
     * 생성 당시 받았던 logical device id를 사용해 해당 장치에 대한 상태를 모니터링
     * link Status는 디바이스가 잘 붙어 있는 지 확인 하기 위한 것
     * device Status는 현재 장치에 문제가 있는지 전달해주기 위한 것.-> 다만 직접 조회를 해오거나 diagnosis를 수행하는 것이 아니고
     * instance에 저장된대로만 가져옴.
     *
     * @param logicalDeviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check/{logicalDeviceId}", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse checkDevice(@PathVariable String logicalDeviceId) throws Exception {
        BaseResponse base = new BaseResponse();
        // parameter verification step
        verificationService.verifyParameters(true, ParameterName.LOGICAL_DEVICE_ID, logicalDeviceId);

        // user verification step
        String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
    	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
    	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
    	
        if(!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
            throw new NotRegisteredDeviceException(logicalDeviceId);
        }

        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        JsonObject resultContent = new JsonObject();
        resultContent.addProperty("linkStatus", true);
        resultContent.addProperty("deviceStatus", 1);
        base.setResult(resultContent);

        return base;
    }
}

