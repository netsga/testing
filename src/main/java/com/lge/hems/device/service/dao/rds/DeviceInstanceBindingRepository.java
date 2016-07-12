package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.common.entity.UserDeviceBindingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by netsga on 2016. 5. 24..
 */

public interface DeviceInstanceBindingRepository extends JpaRepository<UserDeviceBindingInfo, String> {

    List<UserDeviceBindingInfo> findByLogicalDeviceId(String logicalDeviceId);
    List<UserDeviceBindingInfo> findByLogicalDeviceIdAndUserId(String logicalDeviceId, String userId);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT d FROM tbl_device_instance_information d JOIN d.bindingInfo b WHERE b.userId = :userId")
    List<DeviceInstanceInformation> findByUserId(@Param("userId")String userId);

}