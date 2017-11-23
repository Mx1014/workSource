package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class DeleteDoorAuthLevelCommand {
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
