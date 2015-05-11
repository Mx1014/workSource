package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class InvitationCommandResponse {
    @ItemType(InvitationRecord.class)
    private List<InvitationRecord> recipientList;

    public List<InvitationRecord> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<InvitationRecord> recipientList) {
        this.recipientList = recipientList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
