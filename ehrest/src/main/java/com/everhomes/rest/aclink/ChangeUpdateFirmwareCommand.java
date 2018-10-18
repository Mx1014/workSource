// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorType：门禁类型</li>
 * <li>firmwareName：固件名称</li>
 * <li>firmwareId：固件id</li>
 * </ul>
 */
public class ChangeUpdateFirmwareCommand {
    private Long id;
    private String firmware;
    private Long firmwareId;

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
