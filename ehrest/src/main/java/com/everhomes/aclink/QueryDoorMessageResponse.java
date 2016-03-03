package com.everhomes.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class QueryDoorMessageResponse {
    @NotNull
    Long doorId;
    
    @ItemType(DoorMessage.class)
    List<DoorMessage> outputs;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public List<DoorMessage> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<DoorMessage> outputs) {
        this.outputs = outputs;
    }
}
