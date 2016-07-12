package com.lge.hems.device.service.devicemodel;

import com.google.gson.JsonObject;
import com.lge.hems.HemsPlatformApplication;
import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.service.core.devicemodel.DeviceModelService;
import com.lge.hems.device.utilities.CollectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by netsga on 2016. 6. 8..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class DeviceModelServiceTest {

    @Autowired
    private DeviceModelService service;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testReadDeviceModel() throws Exception {
        service.deleteSingleDeviceModel("bbbbb");
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test", "bbbbb");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(service.addDeviceModel("bbbbb", model));
        DeviceModelInformation readResp = service.readDeviceModel("bbbbb");
        assertEquals(model.toString(), readResp.toString());
        assertTrue(service.deleteSingleDeviceModel("bbbbb"));
    }

    @Test
    public void testAddDeviceModel() throws Exception {
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","aaaaa");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(service.addDeviceModel("aaaaa", model));
        assertTrue(service.deleteSingleDeviceModel("aaaaa"));
    }

    @Test
    public void testCheckExistenceModel() throws Exception {
        service.deleteSingleDeviceModel("ddddd");
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","ddddd");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(service.addDeviceModel("ddddd", model));
        assertTrue(service.checkExistenceModel("ddddd"));
        assertTrue(service.deleteSingleDeviceModel("ddddd"));
        assertFalse(service.checkExistenceModel("ddddd"));
    }

    @Test
    public void testDeleteSingleDeviceModel() throws Exception {
        service.deleteSingleDeviceModel("ccccc");
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","ccccc");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(service.addDeviceModel("ccccc", (model)));
        assertTrue(service.deleteSingleDeviceModel("ccccc"));
        assertFalse(service.checkExistenceModel("ccccc"));
    }

    @Test
    public void testReadAllDeviceModel() throws Exception {
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","test1234");
        model.setModel(modelObj);
        model.setModelName("aa");
        model.setDeviceType("energy.battery");
        model.setId("aa");
        model.setVendor("LG");
        model.setVersion("v1.0");

        DeviceModelInformation model1 = new DeviceModelInformation();
        modelObj.addProperty("test","test1234");
        model1.setModel(modelObj);
        model1.setModelName("bb");
        model1.setDeviceType("energy.battery");
        model1.setId("bb");
        model1.setVendor("LG");
        model1.setVersion("v1.0");

        DeviceModelInformation model2 = new DeviceModelInformation();
        modelObj.addProperty("test","test1234");
        model2.setModel(modelObj);
        model2.setModelName("cc");
        model2.setDeviceType("energy.battery");
        model2.setId("cc");
        model2.setVendor("LG");
        model2.setVersion("v1.0");

        DeviceModelInformation model3 = new DeviceModelInformation();
        modelObj.addProperty("test","test1234");
        model3.setModel(modelObj);
        model3.setModelName("dd");
        model3.setDeviceType("energy.battery");
        model3.setId("dd");
        model3.setVendor("LG");
        model3.setVersion("v1.0");

        assertTrue(service.addDeviceModel("aa", (model)));
        assertTrue(service.addDeviceModel("bb", (model1)));
        assertTrue(service.addDeviceModel("cc", (model2)));
        assertTrue(service.addDeviceModel("dd", (model3)));

        List<DeviceModelInformation> resp = service.readAllDeviceModel();
        service.deleteSingleDeviceModel("aa");
        service.deleteSingleDeviceModel("bb");
        service.deleteSingleDeviceModel("cc");
        service.deleteSingleDeviceModel("dd");

        assertTrue(resp.contains(model));
        assertTrue(resp.contains(model1));
        assertTrue(resp.contains(model2));
        assertTrue(resp.contains(model3));
    }

    @Test
    public void testReadAllDeviceModelList() throws Exception {
        Set<String> respBefore = CollectionFactory.newSet();
        try {
            respBefore = service.readAllDeviceModelList();
        } catch (Exception e) {
            System.out.println("Empty");
        }

        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","test1234");
        model.setModel(modelObj);
        model.setModelName("aa");
        model.setDeviceType("energy.battery");
        model.setId("aa");
        model.setVendor("LG");
        model.setVersion("v1.0");

        DeviceModelInformation model1 = new DeviceModelInformation();
        modelObj.addProperty("test","test1234");
        model1.setModel(modelObj);
        model1.setModelName("bb");
        model1.setDeviceType("energy.battery");
        model1.setId("bb");
        model1.setVendor("LG");
        model1.setVersion("v1.0");

        DeviceModelInformation model2 = new DeviceModelInformation();
        modelObj.addProperty("test","test1234");
        model2.setModel(modelObj);
        model2.setModelName("cc");
        model2.setDeviceType("energy.battery");
        model2.setId("cc");
        model2.setVendor("LG");
        model2.setVersion("v1.0");

        DeviceModelInformation model3 = new DeviceModelInformation();
        modelObj.addProperty("test","test1234");
        model3.setModel(modelObj);
        model3.setModelName("dd");
        model3.setDeviceType("energy.battery");
        model3.setId("dd");
        model3.setVendor("LG");
        model3.setVersion("v1.0");


        assertTrue(service.addDeviceModel("aa", (model)));
        assertTrue(service.addDeviceModel("bb", (model1)));
        assertTrue(service.addDeviceModel("cc", (model2)));
        assertTrue(service.addDeviceModel("dd", (model3)));

        Set<String> resp = service.readAllDeviceModelList();
        service.deleteSingleDeviceModel("aa");
        service.deleteSingleDeviceModel("bb");
        service.deleteSingleDeviceModel("cc");
        service.deleteSingleDeviceModel("dd");

        assertEquals(respBefore.size() + 4, resp.size());
    }

    @Test
    public void testSearchDeviceModel() throws Exception {
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("searchTest","test1234");
        model.setModel(modelObj);
        model.setModelName("goodbattery");
        model.setDeviceType("energy.battery");
        model.setId("goodbattery");
        model.setVendor("LG1");
        model.setVersion("v1.01");

        DeviceModelInformation model1 = new DeviceModelInformation();
        model1.setModel(modelObj);
        model1.setModelName("goodinverter");
        model1.setDeviceType("energy.inverter");
        model1.setId("goodinverter");
        model1.setVendor("LG2");
        model1.setVersion("v1.0");

        DeviceModelInformation model2 = new DeviceModelInformation();
        model2.setModel(modelObj);
        model2.setModelName("goodgateway");
        model2.setDeviceType("gateway");
        model2.setId("goodgateway");
        model2.setVendor("LG1");
        model2.setVersion("v0.1");

        DeviceModelInformation model3 = new DeviceModelInformation();
        model3.setModel(modelObj);
        model3.setModelName("goodpv");
        model3.setDeviceType("energy.pv");
        model3.setId("goodpv");
        model3.setVendor("LG2");
        model3.setVersion("v3.0");

        DeviceModelInformation model4 = new DeviceModelInformation();
        model4.setModel(modelObj);
        model4.setModelName("goodinverter22");
        model4.setDeviceType("energy.inverter");
        model4.setId("goodinverter22");
        model4.setVendor("SSS");
        model4.setVersion("v1.1");

        assertTrue(service.addDeviceModel("goodbattery", model));
        assertTrue(service.addDeviceModel("goodinverter", model1));
        assertTrue(service.addDeviceModel("goodgateway", model2));
        assertTrue(service.addDeviceModel("goodpv", model3));
        assertTrue(service.addDeviceModel("goodinverter22", model4));

        List<DeviceModelInformation> resp = service.searchDeviceModel("gateway", null, null, null);
        assertEquals(1, resp.size());
        assertTrue(resp.contains(model2));

        resp = service.searchDeviceModel("energy.inverter", null, null, null);
        assertEquals(2, resp.size());
        assertTrue(resp.contains(model1));
        assertTrue(resp.contains(model4));
//
//        resp = service.searchDeviceModel(null, "LG2", null, null);
//        assertEquals(2, resp.size());
//        assertEquals(model1, resp.get("goodinverter"));
//        assertEquals(model3, resp.get("goodpv"));
//
//        resp = service.searchDeviceModel(null, null, "goodinverter", null);
//        assertEquals(1, resp.size());
//        assertEquals(model1, resp.get("goodinverter"));
//
//        resp = service.searchDeviceModel(null, null, null, "v3.0");
//        assertEquals(1, resp.size());
//        assertEquals(model3, resp.get("goodpv"));
//
//        resp = service.searchDeviceModel(null, null, "goodbattery", "v1.01");
//        assertEquals(1, resp.size());
//        assertEquals(model, resp.get("goodbattery"));
//
//        resp = service.searchDeviceModel("gateway", "LG1", null, null);
//        assertEquals(1, resp.size());
//        assertEquals(model2, resp.get("goodgateway"));
//
//        resp = service.searchDeviceModel("energy.inverter", "SSS", null, "v1.1");
//        assertEquals(1, resp.size());
//        assertEquals(model4, resp.get("goodinverter22"));

        service.deleteSingleDeviceModel("goodbattery");
        service.deleteSingleDeviceModel("goodinverter");
        service.deleteSingleDeviceModel("goodgateway");
        service.deleteSingleDeviceModel("goodpv");
        service.deleteSingleDeviceModel("goodinverter22");
    }




    @Test
    public void testReadDeviceModel_없는거() throws Exception {
        DeviceModelInformation readResp = service.readDeviceModel("ccccc");
        assertNull(readResp);
    }

    @Test (expected = NullRequestException.class)
    public void testReadDeviceModel_null값() throws Exception {
        DeviceModelInformation readResp = service.readDeviceModel(null);
    }

    @Test
    public void testCheckExistenceModel_없는거() throws Exception {
        assertFalse(service.checkExistenceModel("ddddd"));
    }

    @Test (expected = NullRequestException.class)
    public void testCheckExistenceModel_null값() throws Exception {
        service.checkExistenceModel(null);
    }

    @Test
    public void testDeleteSingleDeviceModel_없는거() throws Exception {
        assertFalse(service.deleteSingleDeviceModel("ccccc"));
    }

    @Test (expected = NullRequestException.class)
    public void testDeleteSingleDeviceModel_null값() throws Exception {
        service.deleteSingleDeviceModel(null);
    }

    @Test
    public void testReadAllDeviceModel_모델없을때() throws Exception {
        List<DeviceModelInformation>  resp = service.readAllDeviceModel();
        assertEquals(0, resp.size());
    }

    @Test
    public void testReadAllDeviceModelList_모델없을때() throws Exception {
        Set<String> resp = service.readAllDeviceModelList();
        assertEquals(0, resp.size());
    }

    @Test
    public void testSearchDeviceModel_검색안될때() throws Exception {
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("searchTest","test1234");
        model.setModel(modelObj);
        model.setModelName("goodbattery");
        model.setDeviceType("energy.battery");
        model.setId("goodbattery");
        model.setVendor("LG1");
        model.setVersion("v1.01");

        assertTrue(service.addDeviceModel("goodbattery", model));

        List<DeviceModelInformation> resp = service.searchDeviceModel("energy.batteryeee", null, null, null);
        assertEquals(0, resp.size());

        service.deleteSingleDeviceModel("goodbattery");
    }
}