package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

public class SetCurrentCommunityCommand {
    @NotNull
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

}
