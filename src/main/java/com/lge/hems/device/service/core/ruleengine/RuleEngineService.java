package com.lge.hems.device.service.core.ruleengine;

import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by netsga on 2016. 5. 24..
 */
@Service
public class RuleEngineService {
    @LoggerImpl
    private Logger logger;

    @PostConstruct
    private void init() {
        logger.info(this.getClass().getName());
    }
}
