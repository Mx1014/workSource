package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

public class ListAesUserKeyByUserIdCommand {
    @NotNull
    Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    
}
