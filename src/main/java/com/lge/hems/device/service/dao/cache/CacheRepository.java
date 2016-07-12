package com.lge.hems.device.service.dao.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by netsga on 2016. 5. 31..
 */
public interface CacheRepository {
    Boolean checkDeviceModelExistence(String key) throws Exception;
    Boolean addDeviceModel(String key, String modelStr) throws Exception;
    Object readDeviceModel(String key) throws Exception;
    void deleteDeviceModel(String... key) throws Exception;
    Boolean deleteSingleDeviceModel(String key) throws Exception;

    Map<String, Object> readAllDeviceModel() throws Exception;
    Set<String> readAllDeviceModeList() throws Exception;

    Boolean addDeviceInstance(String logicalDeviceId, Map<String, Object> map) throws Exception;
    Set<String>  readDeviceInstanceDataList(String logicalDeviceId) throws Exception;
    Boolean checkDeviceInstanceExistence(String logicalDeviceId) throws Exception;

    Map<String, Object> readDeviceInstance(String logicalDeviceId) throws Exception;
    Boolean deleteDeviceInstance(String logicalDeviceId) throws Exception;

    ///////////////////////////////////////////////////////////////////////////////
    // Device Instance Data Management Section
    ///////////////////////////////////////////////////////////////////////////////
    Map<String, Object> readDeviceInstanceData(String logicalDeviceId, List<String> keys) throws Exception;

    Object readDeviceInstanceData(String logicalDeviceId, String key) throws Exception;

    Boolean updateDeviceInstanceData(String logicalDeviceId, Map<String, Object> data) throws Exception;

    Boolean updateDeviceInstanceData(String logicalDeviceId, String key, Object data) throws Exception;
//    Map<String, DeviceModelInformation> searchDeviceModel(String deviceType, String vendor, String modelName, String version) throws Exception;
}
