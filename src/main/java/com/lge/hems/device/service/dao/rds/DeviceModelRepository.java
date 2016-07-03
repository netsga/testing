package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by netsga on 2016. 5. 24..
 */

public interface DeviceModelRepository extends JpaRepository<DeviceInstanceInformation, String> {

    DeviceInstanceInformation findByLogicalDeviceId(String logicalDeviceId);
}