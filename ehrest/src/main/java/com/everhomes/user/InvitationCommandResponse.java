package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *recipientList :邀请人列表
 *nextAnchor:下一个锚点
 */
public class InvitationCommandResponse {
    @ItemType(InvitationDTO.class)
    private List<InvitationDTO> recipientList;

    private Long pageAnchor;

    public List<InvitationDTO> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<InvitationDTO> recipientList) {
        this.recipientList = recipientList;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long nextAnchor) {
        this.pageAnchor = nextAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
