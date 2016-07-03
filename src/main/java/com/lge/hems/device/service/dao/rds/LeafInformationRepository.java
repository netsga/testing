package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.model.common.entity.LeafInformation;
import com.lge.hems.device.model.common.entity.LeafInformationKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by netsga on 2016. 5. 24..
 */

public interface LeafInformationRepository extends JpaRepository<LeafInformation, String> {
    LeafInformation findByLeafInformationKey(LeafInformationKey leafInformationKey);
}