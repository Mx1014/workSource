// @formatter:off
package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>firmwareId:固件Id</li>
 * </ul>
 */
public class DeleteFirmwareCommand {
    private Long firmwareId;

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
