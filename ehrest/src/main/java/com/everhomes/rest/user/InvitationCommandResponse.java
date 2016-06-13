package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *recipientList :邀请人列表
 *nextPageAnchor:下一个锚点
 */
public class InvitationCommandResponse {
    @ItemType(InvitationDTO.class)
    private List<InvitationDTO> recipientList;

    private Long nextPageAnchor;

    public List<InvitationDTO> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<InvitationDTO> recipientList) {
        this.recipientList = recipientList;
    }

    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
