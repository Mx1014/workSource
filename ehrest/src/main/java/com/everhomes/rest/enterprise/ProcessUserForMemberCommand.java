package com.everhomes.rest.enterprise;

import com.everhomes.rest.user.UserIdentifierDTO;
import com.everhomes.util.StringHelper;

public class ProcessUserForMemberCommand {

    private Integer namespaceId;

    private String identifierToken;

    private Long ownerId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
