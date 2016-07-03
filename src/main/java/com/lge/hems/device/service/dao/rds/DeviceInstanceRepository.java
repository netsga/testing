package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by netsga on 2016. 5. 24..
 */

public interface DeviceInstanceRepository extends JpaRepository<DeviceInstanceInformation, String> {
    DeviceInstanceInformation findByLogicalDeviceId(String logicalDeviceId);
    DeviceInstanceInformation findByDeviceIdAndModelNameAndDeviceType(String deviceId, String modelName, String deviceType);
    DeviceInstanceInformation findByDeviceIdAndModelNameAndDeviceTypeAndLogicalDeviceId(String deviceId, String modelName, String deviceType, String logicalDeviceId);
}