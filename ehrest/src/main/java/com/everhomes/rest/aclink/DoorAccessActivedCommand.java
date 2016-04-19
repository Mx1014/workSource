package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId: 门禁 ID</li>
 * <li>hardwareId: 门禁 mac 地址</li>
 * <li>time: 门禁 设备当前时间</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessActivedCommand {
    @NotNull
    Long doorId;
    
    @NotNull
    String hardwareId;
    
    @NotNull
    Long time;
    
    //The response message from INIT_SERVER_KEY command, base64 encode
    String content;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
