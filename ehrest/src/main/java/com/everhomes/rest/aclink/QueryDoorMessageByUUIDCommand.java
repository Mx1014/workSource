package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class QueryDoorMessageByUUIDCommand {
    @NotNull
    private Long id;
    
    @ItemType(DoorMessage.class)
    List<DoorMessage> inputs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DoorMessage> getInputs() {
        return inputs;
    }

    public void setInputs(List<DoorMessage> inputs) {
        this.inputs = inputs;
    }
}
