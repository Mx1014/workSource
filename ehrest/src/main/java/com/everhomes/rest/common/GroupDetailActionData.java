package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为GROUP_DETAILS跳转到圈详情、可点击加入圈（可能含邀请人）
 * <li>id: 圈ID</li>
 * <li>inviterId: 邀请人用户ID，有值时认为是邀请的，调邀请接口</li>
 * <li>privateFlag: group公有、私有标记，0-公有、1-私有</li>
 * <li>scanJoinUrl: 扫描入群的web_url</li>
 * </ul>
 */
public class GroupDetailActionData implements Serializable{
    private static final long serialVersionUID = 4265129665290482898L;
    //{"groupId": 1,"inviterId":1,"privateFlag":1} 
    private Long groupId;
    private Long inviterId;
    private Byte privateFlag;
    private String scanJoinUrl;
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

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    public String getScanJoinUrl() {
        return scanJoinUrl;
    }

    public void setScanJoinUrl(String scanJoinUrl) {
        this.scanJoinUrl = scanJoinUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
