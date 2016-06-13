package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

public class DeleteUserIdentifierCommand {
    @NotNull
    private Long userIdentifierId;

    public Long getUserIdentifierId() {
        return userIdentifierId;
    }

    public void setUserIdentifierId(Long userIdentifierId) {
        this.userIdentifierId = userIdentifierId;
    }

}
