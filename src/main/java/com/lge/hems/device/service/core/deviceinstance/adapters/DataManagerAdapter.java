package com.lge.hems.device.service.core.deviceinstance.adapters;

import com.lge.hems.device.service.core.deviceinstance.DeviceInstanceService;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by netsga on 2016. 6. 28..
 */
@Component
public class DataManagerAdapter implements DeviceInstanceDataAdapter{
    // local variables
    @SuppressWarnings("unused")
    @LoggerImpl
    private Logger logger;

    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;
    @Autowired
    private DeviceInstanceService instanceService;
    @Autowired
    private JsonConverter conv;

}
