package com.lge.hems.device.service.core.deviceinstance.adapters;

import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.exceptions.deviceinstance.DeviceInstanceDataReadException;
import com.lge.hems.device.exceptions.deviceinstance.NullInstanceException;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.LeafUtil;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by netsga on 2016. 6. 28..
 */
@Component
public class InstanceDataAdapter implements DeviceInstanceDataAdapter{
    // local variables
    @SuppressWarnings("unused")
    @LoggerImpl
    private Logger logger;

    private static final String READ="read";
    private static final String MODULE_TYPE="instance";

    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;

    public Map<String, Object> getDeviceInstanceData(String logicalDeviceId, Map<String, Map<String, Object>> leafInfoMap) throws NullInstanceException, DeviceInstanceDataReadException, RequestParameterException {
        Map<String, Object> readResp;
        List<String> requestKeys = CollectionFactory.newList();
        requestKeys.addAll(leafInfoMap.entrySet().stream().filter(e -> MODULE_TYPE.equals(LeafUtil.leafInfoExtractor((String)e.getValue().get(READ)).get(LeafUtil.TYPE))).map(Map.Entry::getKey).collect(Collectors.toList()));

        try {
            readResp = cacheRepository.readDeviceInstanceData(logicalDeviceId, requestKeys);
        } catch (Exception e) {
            throw new DeviceInstanceDataReadException("Device data read error", logicalDeviceId, requestKeys, e);
        }

        return readResp;
    }

}
