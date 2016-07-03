package com.lge.hems.device.model.common.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Created by netsga on 2016. 6. 29..
 */
@Entity(name="tbl_leaf_information")
public class LeafInformation {
    // ex) http_get o
    // ex) getDeviceData o
    // ex) localhems o
    // ex) http://localhost:8080/devices/${ldId}/data?tags=#{convTag}
    // ex) null
    // ex) http_get.getDeviceData(localhems,#params, ...)

    // ex) http_post
    // ex) putDeviceData
    // ex) localhems o
    // ex) http://localhost:8080/devices/${ldId}
    // ex) {\"request\":{\"test\":${value}}}
    // ex) http_post.setDeviceData(localhems, #params, ...)

    // ex) instance
    // ex) getDeviceData
    // ex) null
    // ex) null
    // ex) null
    // ex) instance.getDeviceData()

    // ex) instance
    // ex) putDeviceData
    // ex) null
    // ex) null
    // ex) null
    // ex) instance.setDeviceData()

    // ex) datamanager
    // ex) getDeviceData
    // ex) null
    // ex) null
    // ex) null
    // ex) datamanager.getDeviceData(ingredient name)

    // ex) datamanager
    // ex) putDeviceData
    // ex) null
    // ex) null
    // ex) null
    // ex) datamanager.setDeviceData(typecode,datasheet)

    @EmbeddedId
    private LeafInformationKey leafInformationKey;
    private String url;
    private String header;
    private String body;
    private String description;

    public LeafInformationKey getLeafInformationKey() {
        return leafInformationKey;
    }

    public void setLeafInformationKey(LeafInformationKey leafInformationKey) {
        this.leafInformationKey = leafInformationKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "LeafInformation{" +
                "leafInformationKey=" + leafInformationKey +
                ", url='" + url + '\'' +
                ", header='" + header + '\'' +
                ", body='" + body + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
