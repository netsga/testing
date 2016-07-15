package com.lge.hems.device.service.dao.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.lge.hems.device.model.common.entity.CertificationInformation;

public interface CertificationInformationRepository extends JpaRepository<CertificationInformation, String> {
//    @Query("SELECT d FROM tbl_device_instance_information d JOIN d.bindingInfo b WHERE b.userId = :userId")
    CertificationInformation findByKeyName(@Param("keyName")String keyName);
}
