package com.lge.hems.device.service.dao.rds;

import com.lge.hems.device.model.common.entity.ParameterRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by netsga on 2016. 5. 27..
 */
public interface ParameterRestrictionRepository extends JpaRepository<ParameterRestriction, String> {
    ParameterRestriction findByParameterName(String parameterName);
    ParameterRestriction findByParameterNameAndNeedCheck(String parameterName, String NeedCheck);
}
