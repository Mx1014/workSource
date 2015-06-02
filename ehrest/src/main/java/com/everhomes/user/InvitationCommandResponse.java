package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class InvitationCommandResponse {
    @ItemType(InvitationDTO.class)
    private List<InvitationDTO> recipientList;

    private Long nextAnchor;

    public List<InvitationDTO> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<InvitationDTO> recipientList) {
        this.recipientList = recipientList;
    }

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
