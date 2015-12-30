package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为FAMILY_DETAILS跳转到家庭详情、可点击加入家庭（可能含邀请人）
 * <li>id: 家庭ID</li>
 * <li>inviterId: 邀请人用户ID，有值时认为是邀请的，调邀请接口</li>
 * </ul>
 */
public class FamilyDetailActionData implements Serializable{
    private static final long serialVersionUID = -5594052479250042128L;
    //{"familyId": 1,"inviterId":1} 
    private Long familyId;
    private Long inviterId;

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
