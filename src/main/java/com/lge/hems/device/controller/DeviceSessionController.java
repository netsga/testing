package com.lge.hems.device.controller;

import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.controller.request.ServiceRequestHeader;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.model.controller.response.SessionResponse;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RestController
@RequestMapping("/devices/sessions")
public class DeviceSessionController {
    @LoggerImpl
    private Logger logger;

    @Autowired(required=false)
    private HttpServletRequest request;

    @Autowired
    private JsonConverter gson;

    @PostConstruct
    private void init() {

    }

//    @Autowired
//    @Qualifier("mockDeviceInstanceService")
//    private DeviceInstanceService deviceInstanceService;


    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public BaseResponse checkSession() throws Exception {
        BaseResponse base = new BaseResponse();
        SessionResponse resultContent = new SessionResponse(true, true, true);

        logger.info(request.getHeader("x-session-key"));
        logger.info(request.getHeader("x-service-id"));

        base.setResult(resultContent);
        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        return base;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public BaseResponse refreshSession() throws Exception {
        BaseResponse base = new BaseResponse();
        ServiceRequestHeader resultContent = new ServiceRequestHeader();
        resultContent.setSessionKey("testsessionkey");

        base.setResult(resultContent);
        base.setResultCode(ResultCode.SUCCESS.getResultCode());

        return base;
    }
}

