package com.lge.hems.device.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.lge.hems.device.exceptions.DeviceControlRequestException;
import com.lge.hems.device.exceptions.deviceinstance.NotRegisteredDeviceException;
import com.lge.hems.device.model.common.InternalCommonKey;
import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.controller.request.DeviceControlRequest;
import com.lge.hems.device.model.controller.request.DeviceDataUpdateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceReadDataService;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceReadHistoryService;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceWriteDataService;
import com.lge.hems.device.service.core.devicerelation.UserDeviceRelationService;
import com.lge.hems.device.service.core.verification.ParameterName;
import com.lge.hems.device.service.core.verification.VerificationService;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import com.lge.hems.user.service.core.user.UserService;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RestController
@RequestMapping("/devices")
public class DeviceDataController {
    @LoggerImpl
    private Logger logger;
    @Autowired
    private VerificationService verificationService;
    @Autowired(required=false)
    private HttpServletRequest httpRequest;
    @Autowired
    private UserDeviceRelationService userDeviceRelationService;
    @Autowired
    private DeviceInstanceWriteDataService dataWriteService;
    @Autowired
    private DeviceInstanceReadDataService dataReadService;
    @Autowired
    private DeviceInstanceReadHistoryService historyReadService;
    @Autowired
    private UserService userService;

    /**
     * Request encoding for read data API
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/encode", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String encodingDeviceKeyArray(@RequestBody List<String> request) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = request.iterator();
        while(it.hasNext()) {
            sb.append(it.next());
            if(it.hasNext()) {
                sb.append(";");
            }
        }

        return new String(Base64.encodeBase64(sb.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    /**
     * logicalDeviceId에 해당하는 device instance의 tag data를 받아오는 기능.
     * device instance의 데이터를 조회하여 저장되어 있는 data source로부터 데이터를 가져와
     * 응답해줌.
     * tags는 inverter.DC.deviceInfo;inverter.MX.PowerYieldSum.meaVal.double;inverter.MX.PowerYieldSum.unit 처럼 각 tag를 ;로 구분하여
     * array 형식으로 넣고 base64 encoding을 시켜서 보내야 한다.
     *
     * @param logicalDeviceId
     * @param tags base64 encoding이 되어있어야 한다.
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{logicalDeviceId}/data", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public BaseResponse getDeviceData(@PathVariable String logicalDeviceId, @RequestParam("tags") String tags) throws Exception {
        return getDeviceData(logicalDeviceId, tags, null);
    }

    public BaseResponse getDeviceData(String logicalDeviceId, String tags, Boolean testFlag) throws Exception {
        BaseResponse base = new BaseResponse();
        Map<String, String> reqInfo = CollectionFactory.newMap();

        byte[] decoded = Base64.decodeBase64(tags.getBytes());
        String[] tagArray = StringUtils.split(new String(decoded), ";");
        List<String> tagList = new ArrayList<>(Arrays.asList(tagArray));

        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", logicalDeviceId);
        verificationService.verifyParameters(true, "tagName", tagList);

        // user verification step
        if(testFlag == null || !testFlag) {
        	String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
        	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
        	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
            if (!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
                throw new NotRegisteredDeviceException(logicalDeviceId);
            }
            reqInfo.put(InternalCommonKey.USER_ID, userId);
            reqInfo.put(InternalCommonKey.HEMS_ID, userId);
            reqInfo.put(InternalCommonKey.LOGICAL_DEVICE_ID, logicalDeviceId);
        }

        JsonObject resultContent = dataReadService.getDeviceInstanceData(logicalDeviceId, tagList, reqInfo);

        base.setResult(resultContent);
        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        return base;
    }
    
    /**
     * Timeseries data를 받아오기 위한 것으로 url, query string, 응답 데이터의 형식만 다르고 나머지는 동일하다.
     * 
     * @param logicalDeviceId
     * @param tags
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{logicalDeviceId}/history", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public BaseResponse getDeviceHistoryData(@PathVariable String logicalDeviceId, @RequestParam(value = "tags") String tags, 
    		@RequestParam("from") Long from, @RequestParam("to") Long to, @RequestParam("method") String method, 
    		@RequestParam("aggregate") String aggregate) throws Exception {
        return getDeviceHistoryData(logicalDeviceId, tags, from, to, method, aggregate, null);
    }
    public BaseResponse getDeviceHistoryData(String logicalDeviceId, String tags, Long from, Long to, String method, String aggregate, Boolean testFlag) throws Exception {
        BaseResponse base = new BaseResponse();
        Map<String, String> reqInfo = CollectionFactory.newMap();

        byte[] decoded = Base64.decodeBase64(tags.getBytes());
        String[] tagArray = StringUtils.split(new String(decoded), ";");
        List<String> tagList = new ArrayList<>(Arrays.asList(tagArray));

        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", logicalDeviceId);
        verificationService.verifyParameters(true, "tagName", tagList);

     // user verification step
        if(testFlag == null || !testFlag) {
        	String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
        	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
        	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
            if (!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
                throw new NotRegisteredDeviceException(logicalDeviceId);
            }
            reqInfo.put(InternalCommonKey.USER_ID, userId);
            reqInfo.put(InternalCommonKey.HEMS_ID, userId);
            reqInfo.put(InternalCommonKey.LOGICAL_DEVICE_ID, logicalDeviceId);
            reqInfo.put(InternalCommonKey.FROM, String.valueOf(from));
            reqInfo.put(InternalCommonKey.TO, String.valueOf(to));
            reqInfo.put(InternalCommonKey.METHOD, String.valueOf(method));
            reqInfo.put(InternalCommonKey.AGGREGATE, String.valueOf(aggregate));
        }

        JsonObject resultContent = historyReadService.getDeviceInstanceHistoryData(logicalDeviceId, tagList, reqInfo);

        base.setResult(resultContent);
        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        return base;
    }

    /**
     * Device data tag list를 받아 지정한 device instance에 값을 입력하고
     * 정상적으로 들어갔는지 확인하기 위해 그 입력된 값을 다시 읽어 응답해준다.
     * 다만 전체 device instance를 응답해주지 않으며 update 한 것에 대해서만 응답해줌.
     * 또한 CO를 제외한 모든 data가 가능하다. CO는 Controllable point로 control api를 사용하도록 한다.
     *
     * @param logicalDeviceId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{logicalDeviceId}/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8", consumes = "application/json;charset=utf-8")
    public BaseResponse updateDeviceData(@PathVariable String logicalDeviceId, @RequestBody DeviceDataUpdateRequest request) throws Exception {
        return updateDeviceData(logicalDeviceId, request, null);
    }

    public BaseResponse updateDeviceData(String logicalDeviceId, DeviceDataUpdateRequest request, Boolean testFlag) throws Exception {
        JsonObject resultContent;
        Map<String, Object> content = request.getRequest();
        List<String> tagNames = new ArrayList<>(content.keySet());
        Map<String, String> reqInfo = CollectionFactory.newMap();
        
        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", logicalDeviceId);
        verificationService.updatableCheck(tagNames);
        verificationService.verifyParameters(true, "tagName", tagNames);

        // user verification step
        if(testFlag == null || !testFlag) {
        	String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
        	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
        	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
            if (!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
                throw new NotRegisteredDeviceException(logicalDeviceId);
            }
            reqInfo.put(InternalCommonKey.USER_ID, userId);
            reqInfo.put(InternalCommonKey.HEMS_ID, userId);
            reqInfo.put(InternalCommonKey.LOGICAL_DEVICE_ID, logicalDeviceId);
        }

        resultContent = dataWriteService.updateDeviceInstanceData(logicalDeviceId, content, reqInfo);

        BaseResponse base = new BaseResponse();
        base.setResult(resultContent);
        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        return base;
    }

    /**
     * 제어할 Device data tag를 받아 지정한 device instance를 읽은 후
     * tag를 검색하여 control endpoint를 확인하고 그 source로 제어명령을 보낸다.
     * 정상적으로 들어갔는지 확인하기 위해 그 입력된 값을 다시 읽어 응답해준다.
     * update와는 다르게 "CO" constraint를 보유한 tag만 사용 가능하며 single point만 가능하다.
     *
     * @param logicalDeviceId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{logicalDeviceId}/control", method = RequestMethod.POST, produces = "application/json;charset=utf-8", consumes = "application/json;charset=utf-8")
    public BaseResponse controlDeviceSinglePoint(@PathVariable String logicalDeviceId, @RequestBody DeviceControlRequest request) throws Exception {
        return controlDeviceSinglePoint(logicalDeviceId, request, null);
    }

    public BaseResponse controlDeviceSinglePoint(String logicalDeviceId, DeviceControlRequest request, Boolean testFlag) throws Exception {
        JsonObject resultContent;
        Map<String, Object> content = request.getRequest();
        Map<String, String> reqInfo = CollectionFactory.newMap();

        // parameter verification step
        if(content.size() != 1) {
            throw new DeviceControlRequestException("Control message must have single request value.", logicalDeviceId);
        }

        String controlKey = content.keySet().iterator().next();
        verificationService.verifyParameters(true, "logicalDeviceId", logicalDeviceId);
        verificationService.controllableCheck(controlKey);
        verificationService.verifyParameters(true, "tagName", controlKey);

        // user verification step
        if(testFlag == null || !testFlag) {
        	String accessToken = httpRequest.getHeader(ParameterName.ACCESS_TOKEN);
        	String testAccessMail = httpRequest.getHeader(ParameterName.TEST_ACCESS_FLAG);
        	String userId = userService.checkValidationIdByAccessToken(accessToken, testAccessMail);
            if (!userDeviceRelationService.checkUserDeviceMatch(userId, logicalDeviceId)) {
                throw new NotRegisteredDeviceException(logicalDeviceId);
            }
            reqInfo.put(InternalCommonKey.USER_ID, userId);
            reqInfo.put(InternalCommonKey.HEMS_ID, userId);
            reqInfo.put(InternalCommonKey.LOGICAL_DEVICE_ID, logicalDeviceId);
        }

        // control
        resultContent = dataWriteService.updateDeviceInstanceData(logicalDeviceId, controlKey, content.values().iterator().next(), reqInfo);

        BaseResponse base = new BaseResponse();
        base.setResult(resultContent);
        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        return base;
    }
}

