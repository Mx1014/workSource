package com.everhomes.contentserver;

import javax.validation.constraints.NotNull;

public class UpdateContentServerCommand extends AddContentServerCommand {
    @NotNull
    private Long serverId;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

}
