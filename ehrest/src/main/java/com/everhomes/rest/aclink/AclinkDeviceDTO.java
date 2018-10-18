// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * </ul>
 * @author janson
 *
 */
public class AclinkDeviceDTO {
    private Long id;
    private String name;
    private Byte type;
    private String description;
    private Byte supportBt;
    private Byte supportQr;
    private Byte supportFace;
    private Byte supportTempauth;
    private String firmware;
    private Long firmwareId;
    private Byte update;
    private Byte status;
    private Timestamp createtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getSupportBt() {
        return supportBt;
    }

    public void setSupportBt(Byte supportBt) {
        this.supportBt = supportBt;
    }

    public Byte getSupportQr() {
        return supportQr;
    }

    public void setSupportQr(Byte supportQr) {
        this.supportQr = supportQr;
    }

    public Byte getSupportFace() {
        return supportFace;
    }

    public void setSupportFace(Byte supportFace) {
        this.supportFace = supportFace;
    }

    public Byte getSupportTempauth() {
        return supportTempauth;
    }

    public void setSupportTempauth(Byte supportTempauth) {
        this.supportTempauth = supportTempauth;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public Long getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Long firmwareId) {
        this.firmwareId = firmwareId;
    }

    public Byte getUpdate() {
        return update;
    }

    public void setUpdate(Byte update) {
        this.update = update;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
