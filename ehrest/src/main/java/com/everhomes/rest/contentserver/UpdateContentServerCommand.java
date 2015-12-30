package com.everhomes.rest.contentserver;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class UpdateContentServerCommand extends AddContentServerCommand {
    @NotNull
    private Long serverId;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
