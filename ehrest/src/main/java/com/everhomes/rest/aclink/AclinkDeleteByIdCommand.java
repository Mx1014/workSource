package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkDeleteByIdCommand {
    private Long id;

    private Long ownerId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
