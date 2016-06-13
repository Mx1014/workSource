package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class InvitationDTO {
    private String name;
    private String identifier;
    private Byte inviteType;
    private String inviteName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Byte getInviteType() {
        return inviteType;
    }

    public void setInviteType(Byte inviteType) {
        this.inviteType = inviteType;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
