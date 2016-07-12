package com.lge.hems.device.service.dao.cache;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lge.hems.HemsPlatformApplication;
import com.lge.hems.device.exceptions.deviceinstance.DuplicateDeviceException;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.utilities.CollectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class RedisRepositoryTest {
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository repo;

    @Autowired
    private DeviceInstanceService instanceService;

    private Gson gson = new Gson();

    private String logicalDeviceId;

    @Before
    public void setUp() throws Exception {

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

        assertTrue(repo.addDeviceModel("aaaaa", gson.toJson(model)));
        assertTrue(repo.deleteSingleDeviceModel("aaaaa"));
    }


    @Test
    public void testReadDeviceModel() throws Exception {
        repo.deleteSingleDeviceModel("bbbbb");
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test", "bbbbb");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(repo.addDeviceModel("bbbbb", gson.toJson(model)));
        Object readResp = repo.readDeviceModel("bbbbb");
        assertNotNull(readResp);
        assertEquals(gson.toJson(model), readResp.toString());
        assertTrue(repo.deleteSingleDeviceModel("bbbbb"));

    }

    @Test
    public void testDeleteSingleDeviceModel() throws Exception {
        repo.deleteSingleDeviceModel("ccccc");
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","ccccc");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(repo.addDeviceModel("ccccc", gson.toJson(model)));
        assertTrue(repo.deleteSingleDeviceModel("ccccc"));
        assertFalse(repo.checkDeviceModelExistence("ccccc"));
    }

    @Test
    public void testCheckExistence() throws Exception {
        repo.deleteSingleDeviceModel("ddddd");
        DeviceModelInformation model = new DeviceModelInformation();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("test","ddddd");
        model.setModel(modelObj);
        model.setModelName("testModel");
        model.setDeviceType("energy.battery");
        model.setId("tttt");
        model.setVendor("LG");
        model.setVersion("v1.0");

        assertTrue(repo.addDeviceModel("ddddd", gson.toJson(model)));
        assertTrue(repo.checkDeviceModelExistence("ddddd"));
        assertTrue(repo.deleteSingleDeviceModel("ddddd"));
        assertFalse(repo.checkDeviceModelExistence("ddddd"));
    }

    @Test
    public void testGetAllDeviceModel() throws Exception {

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

        assertTrue(repo.addDeviceModel("aa", gson.toJson(model)));
        assertTrue(repo.addDeviceModel("bb", gson.toJson(model1)));
        assertTrue(repo.addDeviceModel("cc", gson.toJson(model2)));
        assertTrue(repo.addDeviceModel("dd", gson.toJson(model3)));

        Map<String, Object> resp = repo.readAllDeviceModel();
        repo.deleteDeviceModel("aa");
        repo.deleteDeviceModel("bb");
        repo.deleteDeviceModel("cc");
        repo.deleteDeviceModel("dd");

        assertEquals(resp.get("aa"), gson.toJson(model));
        assertEquals(resp.get("bb"), gson.toJson(model1));
        assertEquals(resp.get("cc"), gson.toJson(model2));
        assertEquals(resp.get("dd"), gson.toJson(model3));


    }

    @Test
    public void testReadAllDeviceModeList() throws Exception {
        Set<String> respBefore = CollectionFactory.newSet();
        try {
            respBefore = repo.readAllDeviceModeList();
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

        assertTrue(repo.addDeviceModel("aa", gson.toJson(model)));
        assertTrue(repo.addDeviceModel("bb", gson.toJson(model1)));
        assertTrue(repo.addDeviceModel("cc", gson.toJson(model2)));
        assertTrue(repo.addDeviceModel("dd", gson.toJson(model3)));

        Set<String> resp = repo.readAllDeviceModeList();
        repo.deleteDeviceModel("aa");
        repo.deleteDeviceModel("bb");
        repo.deleteDeviceModel("cc");
        repo.deleteDeviceModel("dd");
        assertEquals(respBefore.size() + 4, resp.size());
    }

    @Test
    public void testReadDeviceInstanceData() throws Exception {
        List<String> requestKeys = CollectionFactory.newList();
        requestKeys.add("PSMT_001.DC.LogicalInfo.logicalId");
        requestKeys.add("PSMT_001.CO.Tgl.ctlVal");
        requestKeys.add("PSMT_001.MX.LinQual.meaValI");
        requestKeys.add("PSMT_001.DC.DeviceInfo.vendor");

        System.out.println(repo.readDeviceInstanceData("091cbf82-8593-441d-a7d9-64eb3848157e", requestKeys));
    }

    @Test
    public void testCheckDeviceModelExistence() throws Exception {

    }

    @Test
    public void testDeleteDeviceModel() throws Exception {

    }

    @Test
    public void testReadAllDeviceModel() throws Exception {

    }

    @Test
    public void testAddDeviceInstance() throws Exception {

    }

    @Test
    public void testReadDeviceInstanceDataList() throws Exception {

    }

    @Test
    public void testCheckDeviceInstanceExistence() throws Exception {

    }

    @Test
    public void testDeleteDeviceInstance() throws Exception {

    }

    @Test
    public void testReadDeviceInstance() throws Exception {

    }

    @Test
    public void testUpdateDeviceInstanceData() throws Exception {
        DeviceInstanceInformation info = new DeviceInstanceInformation();
        info.setNameTag("TestName");
        info.setModelName("902010/25");
        info.setDeviceType("iot.smartplug");
        info.setModelId("188f318680d3612f90e05366fbd69ab0");
        info.setVersion("0");
        info.setVendor("BitronHome");
        info.setServiceType("10002");
        info.setDeviceId("fkqjh34iru2");
        info.setLogicalDeviceId("testUpdateDeviceInstanceData");

        String logicalDeviceId;
        try {
            logicalDeviceId = instanceService.createDeviceInstance(info, "testUser33");
        } catch (DuplicateDeviceException e) {
            logicalDeviceId = e.getLogicalDeviceId();
        }

        Map<String, Object> request = CollectionFactory.newMap();

        request.put("PSMT_001.ST.Pos.stVal", "true");
        request.put("PSMT_001.DC.LogicalInfo.nameTag", "testUpdateDeviceInstanceData");
        request.put("PSMT_001.DC.LogicalInfo.subNameTag", "디스이스 스파르따");

        Boolean result = repo.updateDeviceInstanceData(logicalDeviceId, request);
        instanceService.deleteDeviceInstance(logicalDeviceId, "testUser33");
        assertTrue(result);
    }

    @Test
    public void testUpdateDeviceInstanceDataSingle() throws Exception {
        DeviceInstanceInformation info = new DeviceInstanceInformation();
        info.setNameTag("TestName");
        info.setModelName("902010/25");
        info.setDeviceType("iot.smartplug");
        info.setModelId("188f318680d3612f90e05366fbd69ab0");
        info.setVersion("0");
        info.setVendor("BitronHome");
        info.setServiceType("10002");
        info.setDeviceId("123456666");
        info.setLogicalDeviceId("testUpdateDeviceInstanceDataSingle");

        String logicalDeviceId;
        try {
            logicalDeviceId = instanceService.createDeviceInstance(info, "testUser3");
        } catch (DuplicateDeviceException e) {
            logicalDeviceId = e.getLogicalDeviceId();
        }

        Boolean result = repo.updateDeviceInstanceData(logicalDeviceId, "PSMT_001.ST.Pos.stVal", "true");

        instanceService.deleteDeviceInstance(logicalDeviceId, "testUser3");

        assertTrue(result);
    }
//
//    @Test
//    public void testSearchDeviceModel() throws Exception {
//        DeviceModelInformation model = new DeviceModelInformation();
//        JsonObject modelObj = new JsonObject();
//        modelObj.addProperty("searchTest","test1234");
//        model.setModel(modelObj);
//        model.setModelName("goodbattery");
//        model.setDeviceType("energy.battery");
//        model.setId("goodbattery");
//        model.setVendor("LG1");
//        model.setVersion("v1.01");
//
//        DeviceModelInformation model1 = new DeviceModelInformation();
//        model1.setModel(modelObj);
//        model1.setModelName("goodinverter");
//        model1.setDeviceType("energy.inverter");
//        model1.setId("goodinverter");
//        model1.setVendor("LG2");
//        model1.setVersion("v1.0");
//
//        DeviceModelInformation model2 = new DeviceModelInformation();
//        model2.setModel(modelObj);
//        model2.setModelName("goodgateway");
//        model2.setDeviceType("gateway");
//        model2.setId("goodgateway");
//        model2.setVendor("LG1");
//        model2.setVersion("v0.1");
//
//        DeviceModelInformation model3 = new DeviceModelInformation();
//        model3.setModel(modelObj);
//        model3.setModelName("goodpv");
//        model3.setDeviceType("energy.pv");
//        model3.setId("goodpv");
//        model3.setVendor("LG2");
//        model3.setVersion("v3.0");
//
//        DeviceModelInformation model4 = new DeviceModelInformation();
//        model4.setModel(modelObj);
//        model4.setModelName("goodinverter22");
//        model4.setDeviceType("energy.inverter");
//        model4.setId("goodinverter22");
//        model4.setVendor("SSS");
//        model4.setVersion("v1.1");
//
//        assertTrue(repo.addDeviceModel("goodbattery", model));
//        assertTrue(repo.addDeviceModel("goodinverter", model1));
//        assertTrue(repo.addDeviceModel("goodgateway", model2));
//        assertTrue(repo.addDeviceModel("goodpv", model3));
//        assertTrue(repo.addDeviceModel("goodinverter22", model4));
//
//        Map<String, DeviceModelInformation> resp = repo.searchDeviceModel("gateway", null, null, null);
//        assertEquals(1, resp.size());
//        assertEquals(model2, resp.get("goodgateway"));
//
//        resp = repo.searchDeviceModel("energy.inverter", null, null, null);
//        assertEquals(2, resp.size());
//        assertEquals(model1, resp.get("goodinverter"));
//        assertEquals(model4, resp.get("goodinverter22"));
//
//        resp = repo.searchDeviceModel(null, "LG2", null, null);
//        assertEquals(2, resp.size());
//        assertEquals(model1, resp.get("goodinverter"));
//        assertEquals(model3, resp.get("goodpv"));
//
//        resp = repo.searchDeviceModel(null, null, "goodinverter", null);
//        assertEquals(1, resp.size());
//        assertEquals(model1, resp.get("goodinverter"));
//
//        resp = repo.searchDeviceModel(null, null, null, "v3.0");
//        assertEquals(1, resp.size());
//        assertEquals(model3, resp.get("goodpv"));
//
//        resp = repo.searchDeviceModel(null, null, "goodbattery", "v1.01");
//        assertEquals(1, resp.size());
//        assertEquals(model, resp.get("goodbattery"));
//
//        resp = repo.searchDeviceModel("gateway", "LG1", null, null);
//        assertEquals(1, resp.size());
//        assertEquals(model2, resp.get("goodgateway"));
//
//        resp = repo.searchDeviceModel("energy.inverter", "SSS", null, "v1.1");
//        assertEquals(1, resp.size());
//        assertEquals(model4, resp.get("goodinverter22"));
//
//        repo.deleteDeviceModel("goodbattery");
//        repo.deleteDeviceModel("goodinverter");
//        repo.deleteDeviceModel("goodgateway");
//        repo.deleteDeviceModel("goodpv");
//        repo.deleteDeviceModel("goodinverter22");
//    }

}