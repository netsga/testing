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
import com.lge.hems.device.service.core.deviceinstance.adapters.InstanceDataAdapter;
import com.lge.hems.device.service.core.deviceinstance.adapters.rest.HttpGetAdapter;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.service.dao.rds.DeviceInstanceRepository;
import com.lge.hems.device.utilities.CollectionFactory;


@Service
public class DeviceInstanceReadDataService {
    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;
    @Autowired
    private DeviceInstanceRepository instanceRepository;
    @Autowired
    private InstanceDataAdapter instanceDataAdapter;
    @Autowired
    private HttpGetAdapter httpGetAdapter;
    @Autowired
    private DeviceInstanceCommonUtil commonService;



    ///////////////////////////////////////// READ ///////////////////////////////////////////
    /**
     * leaf 정보를 받아서 디바이스 타입별로 번갈아가며 각 어댑터를 호출해서 정보를 map 형식으로 받아옴.
     * leaf 하나하나마다 데이터를 호출하면 시간이 많이 소요될 수 있으므로
     * adapter 별로 덩어리를 통째로 가져가서 각기 최적화를 수행함.
     * 다만 HTTP와 같이 Common한 성격을 지닌 것은 하나로 처리됨.
     *
     * @param logicalDeviceId
     * @param requestKeys
     * @return
     * @throws NullInstanceException
     * @throws DeviceInstanceDataReadException
     * @throws RequestParameterException
     */
    public JsonObject getDeviceInstanceData(String logicalDeviceId, List<String> requestKeys, Map<String, String> reqInfo) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException {

        Map<String, Object> readResp = CollectionFactory.newMap();

        try {
        	reqInfo.put(InternalCommonKey.RAW_DEVICE_ID, instanceRepository.findByLogicalDeviceId(logicalDeviceId).getDeviceId());
            // 현재는 아래처럼 모든 type을 호출하지만 차후 factory를 통해서 알아서 가져오도록 만들어야 함.
            // read leaf information
            Map<String, Map<String, Object>> leafInfo = commonService.readLeafInfo(logicalDeviceId, requestKeys);
            // read all instance type data
            readResp.putAll(instanceDataAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo));
            // read all http get type data
            readResp.putAll(httpGetAdapter.getDeviceInstanceData(logicalDeviceId, leafInfo, reqInfo));
            // read all datamanager type data

        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Device data read error", logicalDeviceId, requestKeys, e);
        }

        return commonService.convertMapToJsonObject(readResp);
    }
    
    public JsonObject getDeviceInstanceAllData(String logicalDeviceId) throws RequestParameterException, DeviceInstanceDataReadException {
        Map<String, Object> readResp;
        try {
            readResp = cacheRepository.readDeviceInstance(logicalDeviceId);
        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Find error in device instance storage.", logicalDeviceId);
        }
        return commonService.convertMapToJsonObject(readResp);
    }
}
