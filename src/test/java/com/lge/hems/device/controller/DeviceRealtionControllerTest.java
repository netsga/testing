package com.lge.hems.device.controller;

import com.lge.hems.HemsPlatformApplication;
import com.lge.hems.device.model.common.entity.ChildDeviceInformation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class DeviceRealtionControllerTest {
    @Autowired
    private DeviceRelationController controller;

    private ChildDeviceInformation dr1;
    private ChildDeviceInformation dr2;
    private ChildDeviceInformation dr3;

    @Before
    public void setUp() throws Exception {
        dr1 = mock(ChildDeviceInformation.class);
        when(dr1.toString()).thenReturn("dr1_test");
        dr2 = mock(ChildDeviceInformation.class);
        when(dr2.toString()).thenReturn("dr2_test");
        dr3 = mock(ChildDeviceInformation.class);
        when(dr3.toString()).thenReturn("dr3_test");
    }

    /**
     * 디바이스 간 연결관계 테스트를 위한 항목 Gateway - Device
     */
//    @Test
//    public void testCreateRelation() throws Exception {
//        DeviceInstanceInformation expected = new DeviceInstanceInformation();
//
//
//        expected.addDeviceRelation(dr1);
//        assertEquals(expected.toString(), controller.createRelation(dr1, "1").toString());
//
//        expected.addDeviceRelation(dr2);
//        assertEquals(expected.toString(), controller.createRelation(dr2, "1").toString());
//    }

    /**
     * 생성된 Device Read
     */
//    @Test
//    public void testReadRelation() throws Exception {
//        DeviceInstanceInformation expected = new DeviceInstanceInformation();
//
//        expected.addDeviceRelation(dr3);
//        expected.addDeviceRelation(dr1);
//        expected.addDeviceRelation(dr2);
//
//        controller.createRelation(dr3, "2").toString();
//        controller.createRelation(dr1, "2").toString();
//        controller.createRelation(dr2, "2").toString();
//
//        assertEquals(expected.toString(), controller.readRelation("2").toString());
//    }
}
