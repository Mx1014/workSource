package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

public class PushJumpObject {
    private String jump;
    private Long id;
    
    public String getJump() {
        return jump;
    }
    public void setJump(String jump) {
        this.jump = jump;
    }
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
