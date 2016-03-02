package com.everhomes.aclink;

import javax.validation.constraints.NotNull;

public class DoorAccessActivedCommand {
    @NotNull
    Long doorId;
    
    @NotNull
    String hardwareId;
    
    @NotNull
    Long time;

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
}
