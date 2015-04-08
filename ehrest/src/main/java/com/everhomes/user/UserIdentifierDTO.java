package com.everhomes.user;

import com.everhomes.util.StringHelper;

public class UserIdentifierDTO {
    private java.lang.Long     id;
    private java.lang.Byte     identifierType;
    private java.lang.String   identifierToken;
    private java.lang.Byte     claimStatus;

    public UserIdentifierDTO() {
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Byte getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(java.lang.Byte identifierType) {
        this.identifierType = identifierType;
    }

    public java.lang.String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(java.lang.String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public java.lang.Byte getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(java.lang.Byte claimStatus) {
        this.claimStatus = claimStatus;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
