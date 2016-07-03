package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.HemsDeviceManagerApplication;
import com.lge.hems.device.model.common.entity.ChildDeviceInformation;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by netsga on 2016. 5. 24..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsDeviceManagerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProtoDeviceInstanceServiceInformationRepositoryTest {
    @Autowired
    private DeviceInstanceRepository instanceDao;
    @Autowired
    private DeviceRelationRepository relationDao;

    private ChildDeviceInformation dr1;
    private ChildDeviceInformation dr2;
    private ChildDeviceInformation dr3;
    private ChildDeviceInformation dr4;
    private String parentLdId = "test";

    @Before
    public void setUp() throws Exception {
        dr1 = new ChildDeviceInformation("device", UUID.randomUUID().toString());
        dr2 = new ChildDeviceInformation("device", UUID.randomUUID().toString());
        dr3 = new ChildDeviceInformation("device", UUID.randomUUID().toString());
        dr4 = new ChildDeviceInformation("device", UUID.randomUUID().toString());

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void dao_01_CreateGatewayInstance() {
        DeviceInstanceInformation gateway = new DeviceInstanceInformation();
        gateway.setDeviceType("energy.gateway");
        gateway.setLogicalDeviceId(parentLdId);
        instanceDao.save(gateway);
        assertEquals(1, instanceDao.count());
    }

    @Test
    public void dao_02_CreateDeviceInstance() {
        DeviceInstanceInformation device1 = new DeviceInstanceInformation();
        device1.setLogicalDeviceId("device 1");
        device1.setDeviceType("energy.inverter");
        DeviceInstanceInformation device2 = new DeviceInstanceInformation();
        device2.setLogicalDeviceId("device 2");
        device2.setDeviceType("energy.battery");
        DeviceInstanceInformation device3 = new DeviceInstanceInformation();
        device3.setLogicalDeviceId("device 3");
        device3.setDeviceType("energy.pv");

        instanceDao.save(device1);
        instanceDao.save(device2);
        instanceDao.save(device3);
        assertEquals(4, instanceDao.count());
    }

    @Test
    public void dao_03_CreateDeviceRelation() {
        DeviceInstanceInformation instance = instanceDao.findByLogicalDeviceId(parentLdId);

        dr1.setDeviceInstanceInformation(instance);
        dr2.setDeviceInstanceInformation(instance);
        dr3.setDeviceInstanceInformation(instance);

        relationDao.save(dr1);
        relationDao.save(dr2);
        relationDao.save(dr3);

        instanceDao.save(instance);
        assertEquals(3, instanceDao.findByLogicalDeviceId(parentLdId).getChildDeviceInformations().size());
    }

    @Test
    public void dao_04_AddMoreRelation() {
        DeviceInstanceInformation instance = instanceDao.findByLogicalDeviceId(parentLdId);

        dr4.setDeviceInstanceInformation(instance);

        relationDao.save(dr4);

        instanceDao.save(instance);
        assertEquals(4, instanceDao.findByLogicalDeviceId(parentLdId).getChildDeviceInformations().size());
    }

    @Test
    @Transactional(readOnly = true)
    public void dao_05_ReadDeviceRelation() {
        DeviceInstanceInformation instance = instanceDao.findByLogicalDeviceId(parentLdId);
        assertEquals(4, instanceDao.findByLogicalDeviceId(parentLdId).getChildDeviceInformations().size());
    }
}