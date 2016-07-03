package com.lge.hems.device.controller;

import com.lge.hems.device.HemsDeviceManagerApplication;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.DuplicateDeviceException;
import com.lge.hems.device.model.common.DeviceDataTagValue;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.controller.request.DeviceControlRequest;
import com.lge.hems.device.model.controller.request.DeviceDataUpdateRequest;
import com.lge.hems.device.model.controller.request.DeviceInstanceCreateRequest;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.service.core.verification.VerificationErrorCode;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.customize.JsonConverter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by netsga on 2016. 5. 26..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsDeviceManagerApplication.class)
public class DeviceDataControllerTest {
    @Autowired
    private JsonConverter jsonConv;

    @Autowired
    private DeviceDataController controller;

    @Autowired
    private DeviceInstanceService instanceService;

    /**
     * GetDeviceData의 내부 logic이 정상적인지 검사
     * @throws Exception
     */
    @Test
    public void testGetDeviceData() throws Exception {
        String userId = "testUser55";
        DeviceInstanceCreateRequest request = new DeviceInstanceCreateRequest();
        DeviceInstanceInformation info = new DeviceInstanceInformation();
        info.setNameTag("TestName");
        info.setModelName("902010/25");
        info.setDeviceType("iot.smartplug");
        info.setModelId("188f318680d3612f90e05366fbd69ab0");
        info.setVersion("0");
        info.setVendor("BitronHome");
        info.setServiceType("10002");
        info.setDeviceId("1235rdfwefew");
        info.setLogicalDeviceId("testGetDeviceData-logicalDeviceId");
        String logcialDeviceId = null;
        try {
            logcialDeviceId = instanceService.createDeviceInstance(info, userId);
        } catch (DuplicateDeviceException e) {
            logcialDeviceId = e.getLogicalDeviceId();
        }

        String tags = "PSMT_001.ST.Pos.t;PSMT_001.ST.Pos.stVal;PSMT_001.MX.Watt.unit;PSMT_001.DC.LogicalInfo.logicalId;PSMT_001.DC.DeviceInfo.vendor";
        byte[] encoded = Base64.encodeBase64(tags.getBytes(StandardCharsets.UTF_8));
        BaseResponse resp = null;
        try {
            resp = controller.getDeviceData(logcialDeviceId, new String(encoded), true);
        } catch (RequestParameterException e) {
            e.printStackTrace();
            throw e;
        }

        instanceService.deleteDeviceInstance(logcialDeviceId, userId);
        assertEquals("BaseResponse{resultCode=200, result={\"PSMT_001\":{\"ST\":{\"Pos\":{\"stVal\":\"false\",\"t\":0}},\"DC\":{\"DeviceInfo\":{\"vendor\":\"BitronHome\"},\"LogicalInfo\":{\"logicalId\":\"testGetDeviceData-logicalDeviceId\"}},\"MX\":{\"Watt\":{\"unit\":\"6\"}}}}}", resp.toString());
    }

    @Test (expected = RequestParameterException.class)
    public void testGetDeviceData_unknown_tag() throws Exception {
        String userId = "testUser55";
        DeviceInstanceCreateRequest request = new DeviceInstanceCreateRequest();
        DeviceInstanceInformation info = new DeviceInstanceInformation();
        info.setNameTag("TestName");
        info.setModelName("902010/25");
        info.setDeviceType("iot.smartplug");
        info.setModelId("188f318680d3612f90e05366fbd69ab0");
        info.setVersion("0");
        info.setVendor("BitronHome");
        info.setServiceType("10002");
        info.setDeviceId("1235rdfwefew");
        info.setLogicalDeviceId("testGetDeviceData-logicalDeviceId");
        String logcialDeviceId = null;
        try {
            logcialDeviceId = instanceService.createDeviceInstance(info, userId);
        } catch (DuplicateDeviceException e) {
            logcialDeviceId = e.getLogicalDeviceId();
        }

        String tags = "PSMT_001.ST.Pos.t;PSMT_001.ST.Pos.stVal;PSMT_001.MX.Watt.unit;PSMT_001.DC.LogicalInfo.logicalId22;PSMT_001.DC.DeviceInfo.vendor";
        byte[] encoded = Base64.encodeBase64(tags.getBytes(StandardCharsets.UTF_8));
        try {
            controller.getDeviceData(logcialDeviceId, new String(encoded), true);
        } catch (RequestParameterException e) {
            assertEquals("logicalId22", e.getParameterName());
            instanceService.deleteDeviceInstance(logcialDeviceId, userId);
            throw e;
        }
    }

    /**
     * Update device data 정상 검사
     * @throws Exception
     */
    @Test
    public void testUpdateDeviceData() throws Exception {
        String expected = "BaseResponse{resultCode=200, result={\"PSMT_001\":{\"ST\":{\"Pos\":{\"stVal\":\"false\"}},\"DC\":{\"LogicalInfo\":{\"subNameTag\":\"디스이스 스파르따\",\"nameTag\":\"testUpdateDeviceInstanceData\"}}}}}";

        String userId = "testUser_testUpdateDeviceData";
        DeviceInstanceInformation info = new DeviceInstanceInformation();
        info.setNameTag("TestName");
        info.setModelName("902010/25");
        info.setDeviceType("iot.smartplug");
        info.setModelId("188f318680d3612f90e05366fbd69ab0");
        info.setVersion("0");
        info.setVendor("BitronHome");
        info.setServiceType("10002");
        info.setDeviceId("testUpdateDeviceData_device");
        info.setLogicalDeviceId("testUpdateDeviceData-ldid");
        String logcialDeviceId = null;
        try {
            logcialDeviceId = instanceService.createDeviceInstance(info, userId);
        } catch (DuplicateDeviceException e) {
            logcialDeviceId = e.getLogicalDeviceId();
        }

        DeviceDataUpdateRequest request = new DeviceDataUpdateRequest();
        Map<String, Object> requestContent = CollectionFactory.newMap();
        requestContent.put("PSMT_001.ST.Pos.stVal", "true");
        requestContent.put("PSMT_001.DC.LogicalInfo.nameTag", "testUpdateDeviceInstanceData");
        requestContent.put("PSMT_001.DC.LogicalInfo.subNameTag", "디스이스 스파르따");
        request.setRequest(requestContent);

        String actual = controller.updateDeviceData(logcialDeviceId, request, true).toString();
        assertEquals(expected, actual);
    }

    /**
     * Control device 정상 검사
     * @throws Exception
     */
    @Test
    public void testControlDeviceData() throws Exception {
        DeviceControlRequest request = new DeviceControlRequest();
        request.setRequest(new HashMap<String, Object>(){{
            put("inverter.CO.logicalInfo.deviceName","1");
        }});

        BaseResponse resp = controller.controlDeviceSinglePoint("1234567890", request);
        assertEquals("{\"resultCode\":200,\"result\":{\"inverter\":{\"CO\":{\"logicalInfo\":{\"deviceName\":\"1\"}}}}}", jsonConv.toJson(resp));
    }

    /**
     * Update device data 중 ldid 잘못된 것 검사
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testUpdateDeviceData_WRONG_LDID() throws Exception {
        DeviceDataUpdateRequest request = new DeviceDataUpdateRequest();
        Map<String, Object> requestContent = CollectionFactory.newMap();
        requestContent.put("inverter.SF.logicalInfo.deviceName","1");
        requestContent.put("inverter.SF.logicalInfo.location", "1sdf");
        requestContent.put("inverter.MX.PowerYieldSum.meaVal.double",1234.3123);
        request.setRequest(requestContent);

        try{
            BaseResponse resp = controller.updateDeviceData("1234567=90", request, true);
        } catch (RequestParameterException e) {
            assertEquals("logicalDeviceId", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_CHARACTER, e.getReason());
            throw e;
        }
    }

    /**
     * UPdate device data 중 tag name 잘못된 경우 에러
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testUpdateDeviceData_WRONG_TAG_NAME() throws Exception {
        DeviceDataUpdateRequest request = new DeviceDataUpdateRequest();
        Map<String, Object> requestContent = CollectionFactory.newMap();
        requestContent.put("inverter.SF.logicalInfo.deviceN-ame", "1");
        requestContent.put("inverter.SF.logicalInfo.location", "1sdf");
        requestContent.put("inverter.MX.PowerYieldSum.meaVal.double", 1234.3123);
        request.setRequest(requestContent);

        try{
            BaseResponse resp = controller.updateDeviceData("1234567890", request, true);
        } catch (RequestParameterException e) {
            assertEquals("tagName", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_CHARACTER, e.getReason());
            throw e;
        }
    }

    /**
     * UPdate device data 중 CO tag 포함된 경우 에러
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testUpdateDeviceData_WRONG_FC() throws Exception {
        DeviceDataUpdateRequest request = new DeviceDataUpdateRequest();
        Map<String, Object> requestContent = CollectionFactory.newMap();
        requestContent.put("inverter.CO.logicalInfo.deviceName", "1");
        requestContent.put("inverter.SF.logicalInfo.location", "1sdf");
        requestContent.put("inverter.MX.PowerYieldSum.meaVal.double", 1234.3123);
        request.setRequest(requestContent);

        try{
            BaseResponse resp = controller.updateDeviceData("1234567890", request, true);
        } catch (RequestParameterException e) {
            assertEquals("tagName", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_PARAMETER, e.getReason());
            throw e;
        }
    }

    /**
     * Control device data 중 CO tag 가 아닌것이 포함된 경우 에러
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testControlDevice_WRONG_FC() throws Exception {
        DeviceControlRequest request = new DeviceControlRequest();
        request.setRequest(new HashMap<String, Object>(){{
            put("inverter.SF.logicalInfo.deviceName","1");
        }});

        try{
            BaseResponse resp = controller.controlDeviceSinglePoint("1234567890", request);
        } catch (RequestParameterException e) {
            assertEquals("tagName", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_PARAMETER, e.getReason());
            throw e;
        }
    }

    /**
     * Logical Device ID 이름에 허용되지 않는 문자가 있는 경우
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testGetDeviceData_WRONG_LDID_CHARACTER() throws Exception {
        String tags = "inverter.DC.deviceInfo;inverter.MX.PowerYieldSum.meaVal.double;inverter.MX.PowerYieldSum.unit";
        byte[] encoded = Base64.encodeBase64(tags.getBytes(StandardCharsets.UTF_8));
        try {
            BaseResponse resp = controller.getDeviceData("test12^567", new String(encoded));
        } catch (RequestParameterException e) {
            assertEquals("logicalDeviceId", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_CHARACTER, e.getReason());
            throw e;
        }
    }

    /**
     * Data Read시 tag 이름에 허용되지 않는 문자가 들어간 경우.
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testGetDeviceData_WRONG_TAG_CHARACTER() throws Exception {
        String tags = "inverter.DC.deviceInfo;inverter.MX.PowerYieldSum.meaVal-double;inverter.MX.PowerYieldSum.unit";
        byte[] encoded = Base64.encodeBase64(tags.getBytes(StandardCharsets.UTF_8));
        try {
            BaseResponse resp = controller.getDeviceData("test123567", new String(encoded));
        } catch (RequestParameterException e) {
            assertEquals("tagName", e.getParameterName());
            assertEquals(VerificationErrorCode.UNSUPPORTED_CHARACTER, e.getReason());
            throw e;
        }
    }

    /**
     * Data Read시 tag 이름이 허용된 길이보다 긴 경우
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testGetDeviceData_WRONG_TAG_LENGTH_OVER() throws Exception {
        String tags = "inverter.DC.deviceInfo;inverter.MX.PowerYieldSum.unit11111111111111111111111111111111111111111111111111111111111111111111111111111";
        byte[] encoded = Base64.encodeBase64(tags.getBytes(StandardCharsets.UTF_8));
        try {
            BaseResponse resp = controller.getDeviceData("test123567", new String(encoded));
        } catch (RequestParameterException e) {
            assertEquals("tagName", e.getParameterName());
            assertEquals(VerificationErrorCode.UNFULFILLED_LENGTH, e.getReason());
            throw e;
        }
    }

    /**
     * Data Read시 tag 이름이 허용된 길이보다 짧은 경우
     * @throws Exception
     */
    @Test(expected = RequestParameterException.class)
    public void testGetDeviceData_WRONG_TAG_LENGTH_UNDER() throws Exception {
        String tags = "inverter.DC.deviceInfo;8888;inverter.MX.PowerYieldSum.unit";
        byte[] encoded = Base64.encodeBase64(tags.getBytes(StandardCharsets.UTF_8));
        try {
            BaseResponse resp = controller.getDeviceData("test123567", new String(encoded));
        } catch (RequestParameterException e) {
            assertEquals("tagName", e.getParameterName());
            assertEquals(VerificationErrorCode.UNFULFILLED_LENGTH, e.getReason());
            throw e;
        }
    }
}