package com.lge.hems.device.controller;

import com.lge.hems.device.exceptions.*;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.controller.request.DeviceModelCreateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.service.core.devicemodel.DeviceModelService;
import com.lge.hems.device.service.core.verification.VerificationService;
import com.lge.hems.device.utilities.MD5;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RestController
@RequestMapping("/devices/models")
public class DeviceModelController {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private JsonConverter gson;

    @Autowired
    private VerificationService verificationService;

    @PostConstruct
    private void init() {

    }

    @Autowired
    private DeviceModelService deviceModelService;


    /**
     * Device model을 받아오는 기능. 다만 logical device id를 사용하여 해당 장치에 대한
     * model, vendor, version, type 정보를 instance에서 얻어와 이를 사용해
     * device model을 받아오는 역할을 수행함.
     *
     * @param logicalDeviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getDeviceModelByInstanceId(@RequestParam("ldId") String logicalDeviceId) throws Exception {
        BaseResponse result = new BaseResponse();
        // parameter verification step
        verificationService.verifyParameters(true, "logicalDeviceId", logicalDeviceId);

        String modelStr = "{\"inverter\":{\"DC\":{\"deviceInfo\":{\"vendor\":\"LG\"},\"logicalInfo\":{\"b\":\"b\",\"ldid\":\"test\"}},\"SF\":{\"logicalInfo\":{\"location\":\"dddddd\",\"deviceName\":\"wefqwefq\"}},\"MX\":{\"PowerYieldSum\":{\"meaVal\":{\"double\":0.0}}}}}";
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        result.setResult(gson.toJson(modelStr));

        return result;
    }

    /**
     * 생성시 받았던 model id를 url path에 함께 사용하여
     * 해당 id의 상세 정보를 받아온다.
     *
     * @param modelId 생성 당시 응답 받았던 ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getDeviceModel(@PathVariable String modelId) throws Exception {
        BaseResponse result = new BaseResponse();
        // parameter verification step
        verificationService.verifyParameters(true, "modelId", modelId);

        // get device model
        DeviceModelInformation resp = null;
        try {
            resp = deviceModelService.readDeviceModel(modelId);
        } catch (NullRequestException e) {
            e.printStackTrace();
            throw e;
        } catch (NullModelException e) {
            e.printStackTrace();
            throw e;
        } catch (ModelReadFailException e) {
            e.printStackTrace();
            throw e;
        }

        result.setResultCode(ResultCode.SUCCESS.getResultCode());

        if(resp == null) {
            result.setResult("nothing");
        } else {
            result.setResult(resp);
        }

        return result;
    }

    /**
     * DeviceModel을 생성함.
     * DeviceModel에는 modelname, vendor, version, type 정보들과 함께
     * storage에 저장할 json 형식의 model이 포함됨.
     *
     * 여기에 model name, vendor, version, type 을 사용하여 저장할 path를 만들고
     * storage에 저장한다.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public BaseResponse createDeviceModel(@RequestBody DeviceModelCreateRequest request) throws Exception {
        DeviceModelInformation model = request.getRequest();
        BaseResponse result = new BaseResponse();

        // parameter verification step
        verificationService.verifyParameters(true, new SimpleEntry("modelName", model.getModelName()),
                                new SimpleEntry("deviceType", model.getDeviceType()),
                                new SimpleEntry("vendor", model.getVendor()),
                                new SimpleEntry("version", model.getVersion()));

        // create modelId
        String modelKey = model.getDeviceType() + ":" + model.getVendor() + ":" + model.getModelName() + ":" + model.getVersion();
        String modelId = MD5.getEncMD5(modelKey);
        model.setId(modelId);

        try {
            // add model template
            Boolean createResp = deviceModelService.addDeviceModel(modelId, model);
            if(createResp) {
                // create response
                DeviceModelInformation respContent = deviceModelService.readDeviceModel(modelId);
                result.setResultCode(ResultCode.SUCCESS.getResultCode());
                result.setResult(respContent);
            } else {
                result.setResultCode(ResultCode.FAIL.getResultCode());
            }
        } catch (ModelCreateException e) {
            e.printStackTrace();
            throw e;
        } catch (ModelSerializeException e) {
            e.printStackTrace();
            throw e;
        } catch (NullRequestException e) {
            e.printStackTrace();
            throw e;
        } catch (CheckModelExistenceException e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    /**
     * vendor, version, type, model 을 사용하여 model template을 찾아온다.
     *
     * @param vendor
     * @param version
     * @param deviceType
     * @param modelName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse searchDeviceModel(@RequestParam(required=false) String deviceType, @RequestParam(required=false) String vendor, @RequestParam(required=false) String modelName, @RequestParam(required=false) String version) throws Exception {
        BaseResponse result = new BaseResponse();

        // parameter verification step
        verificationService.verifyParameters(false, new SimpleEntry("deviceType", deviceType),
                                        new SimpleEntry("vendor", vendor),
                                        new SimpleEntry("modelName", modelName),
                                        new SimpleEntry("version", version));

        // search model
        List<DeviceModelInformation> resultContents = deviceModelService.searchDeviceModel(deviceType, vendor, modelName, version);

        // create result
        result.setResultCode(ResultCode.SUCCESS.getResultCode());
        if(resultContents.isEmpty()) {
            result.setResult("nothing");
        } else {
            result.setResult(resultContents);
        }

        return result;
    }

    /**
     * 기 등록된 model을 생성 당시 응답 받았던 model id를 사용하여 제거한다.
     *
     * @param modelId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{modelId}", method = RequestMethod.DELETE, produces = "application/json")
    public BaseResponse deleteDeviceModel(@PathVariable String modelId) throws Exception {
        // parameter verification step
        verificationService.verifyParameters(true, "modelId", modelId);

        // delete model
        Boolean deleteResp = deviceModelService.deleteSingleDeviceModel(modelId);

        // create result
        BaseResponse base = new BaseResponse();
        base.setResultCode(ResultCode.SUCCESS.getResultCode());
        base.setResult(deleteResp);

        return base;
    }

}

