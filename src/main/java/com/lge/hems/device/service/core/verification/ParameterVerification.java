package com.lge.hems.device.service.core.verification;

import com.lge.hems.device.service.dao.rds.ParameterRestrictionRepository;
import com.lge.hems.device.exceptions.ParameterRestrictionException;
import com.lge.hems.device.exceptions.RequestParameterException;
import com.lge.hems.device.model.common.entity.ParameterRestriction;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

/**
 * Created by netsga on 2016. 5. 27..
 */
@Component
public class ParameterVerification {
    @LoggerImpl
    private Logger logger;

    @Autowired
    private ParameterRestrictionRepository repository;

    private Map<String, ParameterRestriction> restrictionMap;

    @PostConstruct
    private void init() {
        restrictionMap = CollectionFactory.newMap();

        List<ParameterRestriction> prList = repository.findAll();
        for(ParameterRestriction pr:prList) {
            restrictionMap.put(pr.getParameterName(), pr);
        }
    }


    public Boolean checkParameter(String key, Object value, Boolean mandatory) throws RequestParameterException {
        Boolean result = true;

        ParameterRestriction restriction = getRestriction(key);
        if(restriction == null) {
            throw new RequestParameterException("PARAMETER IS NOT SUPPORTED.", key, VerificationErrorCode.UNSUPPORTED_PARAMETER);
        }

        if(mandatory && isNull(value)) {
            throw new RequestParameterException("MANDATORY PARAMETER IS NULL.", key, VerificationErrorCode.NULL_PARAMETER);
        } else if((!mandatory) && isNull(value)) {
            result = true;
        } else {
            if (!isCorrectType(restriction, value)) {
                throw new RequestParameterException("VALUE TYPE IS WRONG.", key, VerificationErrorCode.MISMATCH_TYPE);
            }

            if (value instanceof String) {
                try {
                    if (!isCorrectLength(restriction, value)) {
                        throw new RequestParameterException("VALUE LENGTH IS WRONG.", key, VerificationErrorCode.UNFULFILLED_LENGTH);
                    }

                    if (!isCorrectCharacters(restriction, value)) {
                        throw new RequestParameterException("VALUE HAS WRONG CHARACTERS.", key, VerificationErrorCode.UNSUPPORTED_CHARACTER);
                    }
                } catch (PatternSyntaxException e) {
                    logger.error("Regular expression pattern error.", e);
                }
            } else if (value instanceof Number) {
                try {
                    if (!isCorrectRange(restriction, value)) {
                        throw new RequestParameterException("VALUE IS OUT OF RANGE.", key, VerificationErrorCode.OUT_OF_RANGE);
                    }
                } catch (ParameterRestrictionException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return result;
    }

    private ParameterRestriction getRestriction(String key) {
        ParameterRestriction result = null;
        if(restrictionMap.containsKey(key)) {
            result = restrictionMap.get(key);
        }

        return result;
    }

    private Boolean isNull(Object value) {
        return value == null;
    }

    private Boolean isCorrectType(ParameterRestriction restriction, Object value) {
        Boolean result = false;
        String parameterType = restriction.getParameterType();
        ParameterRestriction.ParameterType valueType = ParameterRestriction.ParameterType.valueOf(parameterType);

        if((ParameterRestriction.ParameterType.NUMBER == valueType) && (value instanceof Number)) {
            result = true;
        } else if((ParameterRestriction.ParameterType.STRING == valueType) && (value instanceof String)) {
            result = true;
        } else if((ParameterRestriction.ParameterType.BOOLEAN == valueType) && (value instanceof Boolean)) {
            result = true;
        } else if((ParameterRestriction.ParameterType.CHARACTER == valueType) && (value instanceof Character)) {
            result = true;
        }
        return result;
    }

    private Boolean isCorrectLength(ParameterRestriction restriction, Object value) throws PatternSyntaxException {
        String valueStr = String.valueOf(value);
        return valueStr.matches(restriction.getLength());
    }

    private Boolean isCorrectRange(ParameterRestriction restriction, Object value) throws ParameterRestrictionException {
        Boolean result = false;
        BigDecimal currentLength = new BigDecimal(String.valueOf(value));
        String[] rangeTemp = restriction.getRange().split("-");
        BigDecimal minValue, maxValue;
        if(rangeTemp.length == 2) {
            minValue = new BigDecimal(rangeTemp[0]);
            maxValue = new BigDecimal(rangeTemp[1]);
        } else if(rangeTemp.length == 1) {
            minValue = new BigDecimal(rangeTemp[0]);
            maxValue = new BigDecimal(rangeTemp[0]);
        } else {
            throw new ParameterRestrictionException("Range error in " + restriction.getParameterName());
        }

        int minRes = minValue.compareTo(currentLength);
        int maxRes = maxValue.compareTo(currentLength);

        if(minRes == 0 || maxRes == 0) {
            result = true;
        } else if(minRes == -1 && maxRes == 1) {
            result = true;
        }

        return result;
    }

    private Boolean isCorrectCharacters(ParameterRestriction restriction, Object value) throws PatternSyntaxException {
        String valueStr = String.valueOf(value);
        return valueStr.matches(restriction.getRegex());
    }

    public Map<String, ParameterRestriction> getRestrictionMap() {
        return restrictionMap;
    }

    public void setRestrictionMap(Map<String, ParameterRestriction> restrictionMap) {
        this.restrictionMap = restrictionMap;
    }
}
