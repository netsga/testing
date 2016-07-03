package com.lge.hems.device.service.core.devicerelation;

import com.lge.hems.device.service.dao.rds.DeviceRelationRepository;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.common.entity.ChildDeviceInformation;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by netsga on 2016. 5. 24..
 */
@Service
public class DeviceRelationService {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private DeviceRelationRepository relationRepo;

    @PostConstruct
    public void init() {
        logger.info(this.getClass().getName());
    }


    public boolean createDeviceRelations(ChildDeviceInformation child, String parentLdId) {

//        dao.get
        return true;
    }

    public DeviceInstanceInformation readDeviceRelations(String parentLdId) {
        return null;
//        return storage.readRelations(parentLdId);

    }
}
