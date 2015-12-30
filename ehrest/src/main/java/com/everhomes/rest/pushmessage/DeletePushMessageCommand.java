package com.everhomes.rest.pushmessage;

import javax.validation.constraints.NotNull;

public class DeletePushMessageCommand {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}
