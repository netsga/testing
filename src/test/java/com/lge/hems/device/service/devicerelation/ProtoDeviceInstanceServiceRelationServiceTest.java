package com.lge.hems.device.service.devicerelation;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lge.hems.HemsPlatformApplication;

import static org.mockito.Mockito.mock;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class ProtoDeviceInstanceServiceRelationServiceTest {
//    @Autowired
//    DeviceRelationService service;
//
//    private ChildDeviceInformation dr1;
//    private ChildDeviceInformation dr2;
//    private ChildDeviceInformation dr3;
//    private String ldId;
//
//    @Before
//    public void setUp() throws Exception {
//        dr1 = rds(ChildDeviceInformation.class);
//        when(dr1.toString()).thenReturn("dr1_test");
//        dr2 = rds(ChildDeviceInformation.class);
//        when(dr2.toString()).thenReturn("dr2_test");
//        dr3 = rds(ChildDeviceInformation.class);
//        when(dr3.toString()).thenReturn("dr3_test");
//        ldId = "test";
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//    @Test
//    public void testCreateDeviceRelations() throws Exception {
//        assertTrue(service.createDeviceRelations(dr1, ldId));
//        assertTrue(service.createDeviceRelations(dr2, ldId));
//        assertTrue(service.createDeviceRelations(dr3, ldId));
//    }
//
//    @Test
//    public void testReadDeviceRelations() throws Exception {
//        DeviceInstanceInformation expected = new DeviceInstanceInformation();
//        expected.addDeviceRelation(dr1);
//        expected.addDeviceRelation(dr2);
//        expected.addDeviceRelation(dr3);
//
//        assertEquals(expected.toString(), service.readDeviceRelations(ldId).toString());
//    }
}