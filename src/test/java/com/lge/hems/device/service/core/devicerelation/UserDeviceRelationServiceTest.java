package com.lge.hems.device.service.core.devicerelation;

import com.lge.hems.device.HemsDeviceManagerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by netsga on 2016. 6. 16..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HemsDeviceManagerApplication.class)
public class UserDeviceRelationServiceTest {

    @Autowired
    private UserDeviceRelationService service;

    @Test
    public void testCheckUserDeviceMatch() throws Exception {

    }
}