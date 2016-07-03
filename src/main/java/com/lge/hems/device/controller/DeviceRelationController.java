package com.lge.hems.device.controller;

import com.lge.hems.device.model.common.entity.ChildDeviceInformation;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.controller.request.DeviceRelationCreateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.service.core.devicerelation.DeviceRelationService;
import com.lge.hems.device.service.core.verification.VerificationService;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RestController
@RequestMapping("/devices")
public class DeviceRelationController {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private DeviceRelationService deviceRelationService;

    @Autowired
    private VerificationService verificationService;

    /**
     * Device relation을 생성하고 특정 storage에 저장한다.
     * key는 logical device id가 된다.
     * 응답시 현재 생성된 연결을 포함하여 parent device에 연결된 모든 relation device를 응답해준다.
     *
     * @param request
     * @param ldId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{ldId}/relations")
    public BaseResponse createRelation(@RequestBody DeviceRelationCreateRequest request, @PathVariable String ldId) throws Exception {
        ChildDeviceInformation requestContent = request.getRequest();

        // parameter verification step
        verificationService.verifyParameters(true, new AbstractMap.SimpleEntry("logicalDeviceId", ldId),
                new AbstractMap.SimpleEntry("deviceType", requestContent.getDeviceType()),
                new AbstractMap.SimpleEntry("logicalDeviceId", requestContent.getLogicalDeviceId()));

        BaseResponse result = new BaseResponse();
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        List<DeviceInstanceInformation> resultContent = CollectionFactory.newList();

        resultContent.add(new DeviceInstanceInformation("test1_inverter_ldid", "energy.inverter"));
        resultContent.add(new DeviceInstanceInformation("test1_battery_ldid", "energy.battery"));
        resultContent.add(new DeviceInstanceInformation(request.getRequest().getLogicalDeviceId(), request.getRequest().getDeviceType()));

        result.setResult(resultContent);
        return result;
    }

    /**
     * Device relation가 저장된 storage로부터 logical device id 를 사용하여
     * 자식 device들을 받아온다.
     *
     * @param ldId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{ldId}/relations")
    public BaseResponse readRelation(@PathVariable String ldId) throws Exception {
        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", ldId);


        BaseResponse result = new BaseResponse();
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        List<DeviceInstanceInformation> resultContent = CollectionFactory.newList();

        resultContent.add(new DeviceInstanceInformation("test1_inverter_ldid", "energy.inverter"));
        resultContent.add(new DeviceInstanceInformation("test1_battery_ldid", "energy.battery"));

        result.setResult(resultContent);
        return result;
    }

    /**
     * Device relation을 제거하기 위한 기능으로 parameter가 없으면 현재 하위 모든 child를 제거하며
     * childId가 명시되면 해당 child 장치의 연결만 끊는다.
     * 응답시에는 현재 연결을 삭제한 장치를 제외하고 남아있는 연결에 대해서만 응답해준다.
     * 다만 childId가 없는 경우는 전체 연결을 끊으므로 200만 넘겨준다.
     *
     * @param parentId
     * @param childId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{parentId}/relations/{childId}")
    public BaseResponse deleteRelation(@PathVariable String parentId, @PathVariable String childId) throws Exception {
        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", parentId);
        verificationService.verifyParameters(true, "logicalDeviceId", childId);

        BaseResponse result = new BaseResponse();
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        List<DeviceInstanceInformation> resultContent = CollectionFactory.newList();

        resultContent.add(new DeviceInstanceInformation("test1_inverter_ldid", "energy.inverter"));
        result.setResult(resultContent);
        return result;
    }

    /**
     * Device relation을 제거하기 위한 기능으로 parameter가 없으면 현재 하위 모든 child를 제거하며
     * childId가 명시되면 해당 child 장치의 연결만 끊는다.
     * 응답시에는 현재 연결을 삭제한 장치를 제외하고 남아있는 연결에 대해서만 응답해준다.
     * 다만 childId가 없는 경우는 전체 연결을 끊으므로 200만 넘겨준다.
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{parentId}/relations")
    public BaseResponse deleteAllRelation(@PathVariable String parentId) throws Exception {
        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", parentId);

        BaseResponse result = new BaseResponse();
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        return result;
    }
}

