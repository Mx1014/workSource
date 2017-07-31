// @formatter:off
package com.everhomes.sms;

import com.everhomes.util.StringHelper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  云之讯短信报告
 * <request>
 *     <type>1</type>
 *     <smsid>******</smsid>
 *     <status>******</status>
 *     <reportTime>******</reportTime>
 *     <desc>******</desc>
 * </request>
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "request")
public class YzxSmsReport {

    private Byte type;
    private String smsId;
    private String status;
    private String desc;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    @XmlElement(name = "smsid")
    public String getSmsId() {
        return smsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
