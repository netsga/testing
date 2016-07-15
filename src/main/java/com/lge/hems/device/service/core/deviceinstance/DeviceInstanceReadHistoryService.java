/**
 * Created by netsga on 2016. 6. 18..
 *
 */

package com.lge.hems.device.service.core.deviceinstance;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.model.common.InternalCommonKey;
import com.lge.hems.device.service.core.deviceinstance.adapters.mock.HistoryMockAdapter;
import com.lge.hems.device.service.core.deviceinstance.adapters.rest.HttpGetAdapter;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.service.dao.rds.DeviceInstanceRepository;
import com.lge.hems.device.utilities.CollectionFactory;


@Service
public class DeviceInstanceReadHistoryService {
    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;
    @Autowired
    private HttpGetAdapter httpGetAdapter;
    @Autowired
    private HistoryMockAdapter historyMockAdapter;
    @Autowired
    private DeviceInstanceCommonUtil commonService;
    @Autowired
    private DeviceInstanceRepository instanceRepository;

    public JsonObject getDeviceInstanceHistoryData(String logicalDeviceId, List<String> requestKeys, Map<String, String> reqInfo) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException {

        Map<String, Object> readResp = CollectionFactory.newMap();

        try {
        	reqInfo.put(InternalCommonKey.RAW_DEVICE_ID, instanceRepository.findByLogicalDeviceId(logicalDeviceId).getDeviceId());
        	
            // 현재는 아래처럼 모든 type을 호출하지만 차후 factory를 통해서 알아서 가져오도록 만들어야 함.
            // read leaf information
            Map<String, Map<String, Object>> leafInfo = commonService.readLeafInfo(logicalDeviceId, requestKeys);

            // read all instance type data
//            readResp.putAll(instanceDataAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo));

            // read all http get type data
            readResp.putAll(httpGetAdapter.getDeviceInstanceHistoryData(logicalDeviceId, leafInfo, reqInfo));

            // read all http post type data

            // read all datamanager type data
            
            // read all mock type data
            readResp.putAll(historyMockAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo, reqInfo));

        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Device data read error", logicalDeviceId, requestKeys, e);
        }

        return commonService.convertMapToJsonObject(readResp);
    }
}
