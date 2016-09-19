package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class DeleteUserImpersonationCommand {
    Long userImpersonationId;

    public Long getUserImpersonationId() {
        return userImpersonationId;
    }

    public void setUserImpersonationId(Long userImpersonationId) {
        this.userImpersonationId = userImpersonationId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
