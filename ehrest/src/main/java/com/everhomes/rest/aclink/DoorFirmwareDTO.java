// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;


/**
 * <ul>
 * <li>firmwareName:固件名称</li>
 * <li>firmwareRevision:固件版本</li>
 * <li>firmwareId:固件编号</li>
 * <li>firmwareDescription:固件描述</li>
 * <li>createTime:创建时间</li>
 * <li>bluetooth:蓝牙设备</li>
 * <li>wifi:Wi-fi设备</li>
 * <li>adaptableDoor:适用设备</li>
 *
 * </ul>
 */
public class DoorFirmwareDTO {
    private String firmwareName;
    private String firmwareRevision;
    private Long firmwareId;
    private String firmwareDescription;
    private Timestamp createTime;
    private String bluetooth;
    private String wifi;
    private String adaptableDoor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
