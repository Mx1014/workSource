// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>firmwareName:固件名称</li>
 * <li>firmwareRevision:固件版本</li>
 * <li>firmwareId:固件编号</li>
 * <li>firmwareDescription:固件描述</li>
 * <li>bluetooth:蓝牙设备</li>
 * <li>wifi:Wi-fi设备</li>
 * <li>adaptableDoor:适用设备</li>
 *
 * </ul>
 */
public class AddFirmwareCommand {
    private String firmwareName;
    private String firmwareRevision;
    private Long firmwareId;
    private String firmwareDescription;
    private String bluetooth;
    private String wifi;
    private String adaptableDoor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
