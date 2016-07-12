package com.lge.hems.device.model.common.entity;

import javax.persistence.*;

/**
 * Created by netsga on 2016. 5. 27..
 */
@Entity(name = "tbl_parameter_restriction")
public class ParameterRestriction {
    public enum ParameterType {
        NUMBER("NUMBER"),
        STRING("STRING"),
        BOOLEAN("BOOLEAN"),
        CHARACTER("CHARACTER");

        private String parameterType;
        ParameterType(String arg){
            this.parameterType = arg;
        }
        public String getParameterType(){
            return parameterType;
        }
    }
    private Boolean needCheck;
    @Id
    private String parameterName;
    private String parameterType;
    private String regex;
    private String length;
    @Column(name = "number_range", nullable = true)
    private String range;

    public Boolean getNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(Boolean needCheck) {
        this.needCheck = needCheck;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "ParameterRestriction{" +
                "needCheck=" + needCheck +
                ", parameterName='" + parameterName + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", regex='" + regex + '\'' +
                ", length='" + length + '\'' +
                ", range='" + range + '\'' +
                '}';
    }

}
