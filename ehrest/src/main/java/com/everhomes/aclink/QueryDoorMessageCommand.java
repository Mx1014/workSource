package com.everhomes.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class QueryDoorMessageCommand {
    @NotNull
    Long doorId;
    
    Byte urgent;
    
    @ItemType(DoorMessageResp.class)
    List<DoorMessageResp> inputs;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public List<DoorMessageResp> getInputs() {
        return inputs;
    }

    public void setInputs(List<DoorMessageResp> inputs) {
        this.inputs = inputs;
    }

    public Byte getUrgent() {
        return urgent;
    }

    public void setUrgent(Byte urgent) {
        this.urgent = urgent;
    }
    
    
}
