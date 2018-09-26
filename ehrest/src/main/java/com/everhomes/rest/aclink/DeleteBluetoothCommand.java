// @formatter:off
package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>firmwareId:固件Id</li>
 * <li>firmwareName:固件名称</li>
 * </ul>
 */
public class DeleteBluetoothCommand {
    private Long firmwareId;

    private String firmwareName;

    public Long getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Long firmwareId) {
        this.firmwareId = firmwareId;
    }

    public String getFirmwareName() {
        return firmwareName;
    }

    public void setFirmwareName(String firmwareName) {
        this.firmwareName = firmwareName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
