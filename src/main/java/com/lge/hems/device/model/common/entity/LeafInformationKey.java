package com.lge.hems.device.model.common.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by netsga on 2016. 6. 29..
 */
@Embeddable
public class LeafInformationKey implements Serializable {
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String method;
    @Column(nullable = false)
    private String sourceName;

    public LeafInformationKey() {
    }

    public LeafInformationKey(String type, String method, String sourceName) {
        this.type = type;
        this.method = method;
        this.sourceName = sourceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return "LeafInformationKey{" +
                "type='" + type + '\'' +
                ", method='" + method + '\'' +
                ", sourceName='" + sourceName + '\'' +
                '}';
    }
}
