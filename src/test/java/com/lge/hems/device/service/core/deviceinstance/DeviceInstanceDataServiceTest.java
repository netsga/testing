package com.lge.hems.device.service.core.deviceinstance;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lge.hems.HemsPlatformApplication;
import com.lge.hems.device.exceptions.ModelReadFailException;
import com.lge.hems.device.exceptions.NullModelException;
import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.*;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.utilities.CollectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by netsga on 2016. 6. 22..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HemsPlatformApplication.class)
public class DeviceInstanceDataServiceTest {
    private static final String userId = "instanceService-user";
    private static final String preLdid = "instanceService-ldid";

    @Autowired
    private DeviceInstanceDataService dataService;

    @Autowired
    private DeviceInstanceService instanceService;

    private String logicalDeviceId;

    @Before
    public void setUp() throws DuplicateDeviceException, NullModelException, AddDeviceInstanceDataException, InstanceReadFailException, ModelReadFailException, NullRequestException {
        DeviceInstanceInformation info = new DeviceInstanceInformation();
        info.setNameTag("TestName");
        info.setModelName("902010/25");
        info.setDeviceType("iot.smartplug");
        info.setModelId("188f318680d3612f90e05366fbd69ab0");
        info.setVersion("0");
        info.setVendor("BitronHome");
        info.setServiceType("10002");
        info.setDeviceId("instanceService-raw-id");
        info.setLogicalDeviceId(preLdid);
        this.logicalDeviceId = instanceService.createDeviceInstance(info, userId);
    }

    @Test
    public void testGetDeviceInstanceData() throws Exception {
//        String expected = "{\"PSMT_001\":{\"DC\":{\"DeviceInfo\":{\"vendor\":\"BitronHome\"},\"LogicalInfo\":{\"logicalId\":\"instanceService-ldid\"}},\"CO\":{\"Tgl\":{\"ctlVal\":\"false\"}},\"MX\":{\"LinQual\":{\"meaValI\":\"0\"}}}}";
//
//        List<String> requestKeys = CollectionFactory.newList();
//        requestKeys.add("PSMT_001.DC.LogicalInfo.logicalId");
//        requestKeys.add("PSMT_001.CO.Tgl.ctlVal");
//        requestKeys.add("PSMT_001.MX.LinQual.meaValI");
//        requestKeys.add("PSMT_001.DC.DeviceInfo.vendor");
//
//        String actual = dataService.postDeviceInstanceData(this.logicalDeviceId, requestKeys).toString();
//        assertEquals(expected, actual);
    }

    @Test
    public void testGetDeviceInstanceAllData() throws Exception {
        JsonParser parser = new JsonParser();
        String expectedStr = "{\"PSMT_001\":{\"CO\":{\"Op\":{\"ctlVal\":\"false\",\"operTm\":\"0\"},\"Tgl\":{\"ctlVal\":\"false\",\"operTm\":\"0\"}},\"MX\":{\"LinQual\":{\"t\":0,\"unit\":\"12\",\"meaValI\":\"0\"},\"WattHour\":{\"meaValD\":\"0\",\"unit\":\"7\",\"t\":0},\"Watt\":{\"meaValD\":\"0\",\"unit\":\"6\",\"t\":0}},\"ST\":{\"Pos\":{\"t\":0,\"stVal\":\"false\"},\"LastCommTm\":{\"t\":0,\"stVal\":\"false\"}},\"DC\":{\"DeviceInfo\":{\"vendor\":\"BitronHome\",\"model\":\"902010/25\",\"swRev\":\"15\",\"hwRev\":\"0\",\"serialNumber\":\"00124b00088dcd30\"},\"LogicalInfo\":{\"logicalId\":\"instanceService-ldid\",\"nameTag\":\"TestName\"}}}}";
        JsonObject expected = parser.parse(expectedStr).getAsJsonObject();
        JsonObject actual = dataService.getDeviceInstanceAllData(this.logicalDeviceId);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateDeviceInstanceDataMulti() throws Exception {
        // prepare update
        Map<String, Object> request = CollectionFactory.newMap();
        request.put("PSMT_001.ST.Pos.stVal", "true");
        request.put("PSMT_001.DC.LogicalInfo.nameTag", "testUpdateDeviceInstanceData");
        request.put("PSMT_001.DC.LogicalInfo.subNameTag", "디스이스 스파르따");

        JsonObject expected = convertMapToJsonObject(request);
//        JsonObject actual = dataService.updateDeviceInstanceData(this.logicalDeviceId, request);
//        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateDeviceInstanceDataSingle() throws Exception {
        // prepare update
        Map<String, Object> request = CollectionFactory.newMap();
        request.put("PSMT_001.DC.LogicalInfo.nameTag", "test value for tag");

        JsonObject expected = convertMapToJsonObject(request);
        JsonObject actual = dataService.updateDeviceInstanceData(this.logicalDeviceId, "PSMT_001.DC.LogicalInfo.nameTag", "test value for tag");
        assertEquals(expected, actual);
    }

    @After
    public void destroy() throws InstanceDeleteException, NullInstanceException {
        instanceService.deleteDeviceInstance(this.logicalDeviceId, userId);
    }

    private JsonObject convertMapToJsonObject(Map<String, Object> reqMap) throws RequestParameterException {
        JsonObject result = new JsonObject();

        for(Map.Entry<String, Object> entry: reqMap.entrySet()) {
            JsonObject resultContentPtr = result;

            String[] strata = StringUtils.split(entry.getKey(), ".");
            for(int i = 0; i < strata.length; i++) {
                if(i == strata.length - 1) {
                    Object o = entry.getValue();

                    // 형변환 관련 체크해야 함
                    if(strata[i].equals("meaVal")) {
                        resultContentPtr.addProperty(strata[i], Double.valueOf(o.toString()));
                    } else if(strata[i].equals("t")) {
                        resultContentPtr.addProperty(strata[i], Long.valueOf(o.toString()));
                    } else if(o instanceof Boolean) {
                        resultContentPtr.addProperty(strata[i], (Boolean)o);
                    } else {
                        try {
                            resultContentPtr.addProperty(strata[i], o.toString());
                        } catch (NullPointerException e) {
                            throw new RequestParameterException("Unknown tag name", strata[i], null);
                        }
                    }
                } else {
                    JsonObject tempJson;
                    if(resultContentPtr.has(strata[i])) {
                        tempJson = resultContentPtr.getAsJsonObject(strata[i]);
                    } else {
                        tempJson = new JsonObject();
                        resultContentPtr.add(strata[i], tempJson);
                    }
                    resultContentPtr = tempJson;
                }
            }
        }
        return result;
    }
}