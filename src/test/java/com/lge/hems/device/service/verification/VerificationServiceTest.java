package com.lge.hems.device.service.verification;

import com.lge.hems.device.service.core.verification.ParameterVerification;
import com.lge.hems.device.service.core.verification.VerificationErrorCode;
import com.lge.hems.device.service.core.verification.VerificationService;
import com.lge.hems.device.service.dao.rds.ParameterRestrictionRepository;
import com.lge.hems.HemsPlatformApplication;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.model.common.entity.ParameterRestriction;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by netsga on 2016. 5. 27..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class VerificationServiceTest {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private ParameterRestrictionRepository repository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private ParameterVerification parameterVerification;

    @Before
    public void setUp() throws Exception {
        Map<String, ParameterRestriction> restrictionMap = CollectionFactory.newMap();

        ParameterRestriction restriction = new ParameterRestriction();
        restriction.setParameterName("deviceId");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.STRING.toString());
        restriction.setRegex("^[0-9A-Za-z-_]*$");
        restriction.setLength(".{1,100}");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("serviceType");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.STRING.toString());
        restriction.setRegex("^[0-9]*$");
        restriction.setLength(".{5}");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("deviceType");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.STRING.toString());
        restriction.setRegex("^[0-9A-Za-z.]*$");
        restriction.setLength(".{1,100}");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("modelName");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.STRING.toString());
        restriction.setRegex("^[0-9A-Za-z-_]*$");
        restriction.setLength(".{1,30}");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("nameTag");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.STRING.toString());
        restriction.setRegex("^[0-9A-Za-z-_.,! 가-힣]*$");
        restriction.setLength(".{0,50}");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("testInt");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.NUMBER.toString());
        restriction.setRange("0-100");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("testInt1");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.NUMBER.toString());
        restriction.setRange("0-100");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("testInt2");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.NUMBER.toString());
        restriction.setRange("0-100");
        restrictionMap.put(restriction.getParameterName(), restriction);

        restriction = new ParameterRestriction();
        restriction.setParameterName("testInt3");
        restriction.setNeedCheck(true);
        restriction.setParameterType(ParameterRestriction.ParameterType.NUMBER.toString());
        restriction.setRange("50");
        restrictionMap.put(restriction.getParameterName(), restriction);

        repository.save(restrictionMap.values());

        logger.info(restrictionMap.toString());

        parameterVerification.setRestrictionMap(restrictionMap);
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * 정상적인 상태의 paramter를 verification 함.
     * @throws Exception
     */
    @Test
    public void testVerifyParameters_Correct() throws Exception {
        Map<String, Object> params = CollectionFactory.newMap();

        // regex: 0-9, A-Z, a-z, -, _ -> [0-9A-Za-z-_]{1,100}
        // max length: 100
        // min length: 1
        params.put("deviceId", "testDeviceId_0-1");

        // regex: 0-9
        // max length: 5
        // min length: 5
        params.put("serviceType", "00001");

        // regex: 0-9, A-Z, a-z, .
        // max length: 100
        // min length: 1
        params.put("deviceType", "energy.inverter");

        // regex: 0-9, A-Z, a-z, -, _
        // max length: 30
        // min length: 1
        params.put("modelName", "test_Devicename-01");

        // regex: 0-9, A-Z, a-z, -, _
        // max length: 50
        // min length: 0
        params.put("nameTag", "test_Tag-01");

        // range: 1-100
        params.put("testInt", 56);
        params.put("testInt1", 0);
        params.put("testInt2", 100);
        params.put("testInt3", 50);

        Boolean ret = verificationService.verifyParameters(true, params);
        assertTrue(ret);
    }

    @Test(expected = RequestParameterException.class)
    public void testVerifyParameters_Incorrect_UnsupportedParameterCase() throws Exception {
        Map<String, Object> params = CollectionFactory.newMap();
        params.put("deviceIdTest", "testDeviceId");
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.UNSUPPORTED_PARAMETER, e.getReason());
            assertEquals("deviceIdTest", e.getParameterName());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testVerifyParameters_Incorrect_Empty() throws Exception {
        Map<String, Object> params = CollectionFactory.newMap();
        params.put("deviceId", null);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.NULL_PARAMETER, e.getReason());
            assertEquals("deviceId", e.getParameterName());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testVerifyParameters_Incorrect_TypeMismatchCase() throws Exception {
        Map<String, Object> params = CollectionFactory.newMap();
        params.put("serviceType", 12444);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.MISMATCH_TYPE, e.getReason());
            assertEquals("serviceType", e.getParameterName());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testVerifyParameters_Incorrect_OversizeCase() throws Exception {
        Map<String, Object> params = CollectionFactory.newMap();
        params.put("serviceType", "000000000001");
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.UNFULFILLED_LENGTH, e.getReason());
            assertEquals("serviceType", e.getParameterName());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testVerifyParameters_Incorrect_UndersizeCase() throws Exception {
        String parameterKey = "deviceId";
        Map<String, Object> params = CollectionFactory.newMap();
        params.put(parameterKey, "");
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.UNFULFILLED_LENGTH, e.getReason());
            assertEquals("deviceId", e.getParameterName());
            throw e;
        }
    }

    @Test(expected = RequestParameterException.class)
    public void testVerifyParameters_Incorrect_UnsupportedCharacterCase() throws Exception {
        String parameterKey = "deviceId";
        Map<String, Object> params = CollectionFactory.newMap();
        params.put(parameterKey, "ere가나다라234");
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.UNSUPPORTED_CHARACTER, e.getReason());
            assertEquals("deviceId", e.getParameterName());
            throw e;
        }
    }

    @Test
    public void testVerifyParameters_Incorrect_NumberOutOfRangeCase_BVA() throws Exception {
        // Range is 0 - 100

        // TC NEG001
        // Value is -1
        String parameterKey = "testInt";
        Map<String, Object> params = CollectionFactory.newMap();
        params.put(parameterKey, -1);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.OUT_OF_RANGE, e.getReason());
            assertEquals("testInt", e.getParameterName());
        }

        // TC NEG002
        // Value is 101
        params = CollectionFactory.newMap();
        params.put(parameterKey, 101);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.OUT_OF_RANGE, e.getReason());
            assertEquals("testInt", e.getParameterName());
        }

        // TC NEG003
        // Value is java.lang.Double.MIN_VALUE
        params = CollectionFactory.newMap();
        params.put(parameterKey, Double.MIN_VALUE);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.OUT_OF_RANGE, e.getReason());
            assertEquals("testInt", e.getParameterName());
        }

        // TC NEG004
        // Value is java.lang.Double.MAX_VALUE
        params = CollectionFactory.newMap();
        params.put(parameterKey, Double.MAX_VALUE);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.OUT_OF_RANGE, e.getReason());
            assertEquals("testInt", e.getParameterName());
        }

        // TC NEG005
        // Value is java.lang.Double.MIN_VALUE - 1
        params = CollectionFactory.newMap();
        params.put(parameterKey, Double.MIN_VALUE - 1);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.OUT_OF_RANGE, e.getReason());
            assertEquals("testInt", e.getParameterName());
        }

        // TC NEG006
        // Value is java.lang.Double.MAX_VALUE + 1
        params = CollectionFactory.newMap();
        params.put(parameterKey, Double.MAX_VALUE + 1);
        try {
            verificationService.verifyParameters(true, params);
        } catch (RequestParameterException e) {
            Assert.assertEquals(VerificationErrorCode.OUT_OF_RANGE, e.getReason());
            assertEquals("testInt", e.getParameterName());
        }
    }

//
//    @Test
//    public void testVerifyParameters_Incorrect_MixedCase() throws Exception {
//        VerificationService verificationService = new VerificationService();
//
//        Map<String, Object> params = CollectionFactory.newMap();
//        params.put("deviceId", "testDeviceId");
//        Boolean ret = verificationService.verifyParameters(params);
//        assertFalse(ret);
//    }
//
//    @Test
//    public void testVerifyParameters_Correct_Incorrect_MixedCase() throws Exception {
//        VerificationService verificationService = new VerificationService();
//
//        Map<String, Object> params = CollectionFactory.newMap();
//        params.put("deviceId", "testDeviceId");
//        Boolean ret = verificationService.verifyParameters(params);
//        assertFalse(ret);
//    }
//
//
//
//    @Test
//    public void testVerifyUserSession() throws Exception {
//
//    }
//
//    @Test
//    public void testVerifyDeviceSession() throws Exception {
//
//    }
}