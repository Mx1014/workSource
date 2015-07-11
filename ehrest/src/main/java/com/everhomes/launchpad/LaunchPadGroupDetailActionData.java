package com.everhomes.launchpad;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为GROUP_DETAILS跳转到圈详情、可点击加入圈（可能含邀请人）
 * <li>id: 圈ID</li>
 * <li>inviterId: 邀请人用户ID，有值时认为是邀请的，调邀请接口</li>
 * </ul>
 */
public class LaunchPadGroupDetailActionData implements Serializable{
    private static final long serialVersionUID = 4265129665290482898L;
    //{"groupId": 1,"inviterId":1} 
    private Long groupId;
    private Long inviterId;
    
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
