package com.lge.hems.device.controller;

import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.HemsPlatformApplication;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.controller.request.DeviceInstanceCreateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.service.core.verification.VerificationErrorCode;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by netsga on 2016. 5. 30..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class DeviceInstanceControllerTest {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private JsonConverter jsonConv;

    @Autowired
    private DeviceInstanceController controller;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /*******************************************************************************************************************************
     * Device instance create가 정상적으로 이루어지는 경우를 테스트
     * @throws Exception
     */
    @Test
    public void testCreateDeviceInstance() throws Exception {
        DeviceInstanceCreateRequest request = new DeviceInstanceCreateRequest();
        DeviceInstanceInformation requestContent = new DeviceInstanceInformation();

        requestContent.setDeviceId("test_inverter_v01-001");
        requestContent.setDeviceType("energy.inverter");
        requestContent.setModelName("LG_inverter_01");
        requestContent.setNameTag("우리집 강아지 뽀삐");
        requestContent.setSubNameTag("멍멍!");
        requestContent.setServiceType("00003");
        request.setRequest(requestContent);

        BaseResponse resp = controller.createDeviceInstance(request);
        assertEquals("{\"resultCode\":200,\"result\":{\"logicalDeviceId\":\"test\",\"registeredDate\":\"2015~~~~\",\"seessionKey\":\"ttttt\",\"deviceInformation\":{\"deviceType\":\"energy.inverter\",\"deviceId\":\"test_inverter_v01-001\",\"serviceType\":\"00003\",\"modelName\":\"LG_inverter_01\",\"nameTag\":\"우리집 강아지 뽀삐\",\"subNameTag\":\"멍멍!\"}}}", jsonConv.toJson(resp));
    }

    @Test(expected = RequestParameterException.class)
    public void testCreateDeviceInstance_Incorrect_Wrong_Character_SubnameTag() throws Exception {
        DeviceInstanceCreateRequest request = new DeviceInstanceCreateRequest();
        DeviceInstanceInformation requestContent = new DeviceInstanceInformation();

        requestContent.setDeviceId("test_inverter_v01-001");
        requestContent.setDeviceType("energy.inverter");
        requestContent.setModelName("LG_inverter_01");
        requestContent.setNameTag("우리집 강아지 뽀삐");
        requestContent.setSubNameTag("멍멍!????");
        requestContent.setSubSubNameTag("왈왈!!!");
        requestContent.setServiceType("00003");
        request.setRequest(requestContent);

        try {
            BaseResponse resp = controller.createDeviceInstance(request);
        } catch (RequestParameterException e) {
            assertEquals("subNameTag", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_CHARACTER, e.getReason());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testCreateDeviceInstance_Incorrect_Wrong_Length_ServiceType() throws Exception {
        DeviceInstanceCreateRequest request = new DeviceInstanceCreateRequest();
        DeviceInstanceInformation requestContent = new DeviceInstanceInformation();

        requestContent.setDeviceId("test_inverter_v01-001");
        requestContent.setDeviceType("energy.inverter");
        requestContent.setModelName("LG_inverter_01");
        requestContent.setNameTag("우리집 강아지 뽀삐");
        requestContent.setSubNameTag("멍멍!");
        requestContent.setSubSubNameTag("왈왈!!!");
        requestContent.setServiceType("00000003");
        request.setRequest(requestContent);

        try {
            BaseResponse resp = controller.createDeviceInstance(request);
        } catch (RequestParameterException e) {
            assertEquals("serviceType", e.getParameterName());
            assertEquals(VerificationErrorCode.UNFULFILLED_LENGTH, e.getReason());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testCreateDeviceInstance_Incorrect_MandatoryCheck_ServiceType() throws Exception {
        DeviceInstanceCreateRequest request = new DeviceInstanceCreateRequest();
        DeviceInstanceInformation requestContent = new DeviceInstanceInformation();

        requestContent.setDeviceId("test_inverter_v01-001");
        requestContent.setDeviceType("energy.inverter");
        requestContent.setModelName("LG_inverter_01");
        requestContent.setNameTag("우리집 강아지 뽀삐");
        requestContent.setSubNameTag("멍멍!");
        requestContent.setSubSubNameTag("왈왈!!!");
        request.setRequest(requestContent);

        try {
            BaseResponse resp = controller.createDeviceInstance(request);
        } catch (RequestParameterException e) {
            assertEquals("serviceType", e.getParameterName());
            assertEquals(VerificationErrorCode.NULL_PARAMETER, e.getReason());
            throw e;
        }
    }

    /*******************************************************************************************************************************
     * 장치를 Search 하는 method를 test
     *
     * @throws Exception
     */
    @Test
    public void testSearchDeviceInformation() throws Exception {
        BaseResponse resp = controller.searchDeviceInformation("00001", null, null, null);
        resp = controller.searchDeviceInformation(null, "energy.inverter", null, null);
        resp = controller.searchDeviceInformation(null, null, "testmodel", null);
        resp = controller.searchDeviceInformation("00001", "energy.inverter", "testmodel", null);
    }

    @Test
    public void testGetDeviceInformation() throws Exception {

    }

    @Test
    public void testDeleteDeviceInformation() throws Exception {

    }

    @Test
    public void testCheckDevice() throws Exception {

    }
}