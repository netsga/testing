package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.model.common.entity.ChildDeviceInformation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by netsga on 2016. 5. 24..
 */

public interface DeviceRelationRepository extends JpaRepository<ChildDeviceInformation, String> {

}