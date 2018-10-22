// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id：设备类型id</li>
 * <li>firmwareName：固件名称</li>
 * <li>firmwareId：固件id</li>
 * <li>update: 0：非默认升级 1：默认升级</li>
 * </ul>
 */
public class UpdateFirmwareDTO {
    private Long id;
    private String firmware;
    private Long firmwareId;
    private Byte update;

    public Byte getUpdate() {
        return update;
    }

    public void setUpdate(Byte update) {
        this.update = update;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
