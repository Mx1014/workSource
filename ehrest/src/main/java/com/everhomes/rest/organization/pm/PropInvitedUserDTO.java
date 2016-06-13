// @formatter:off
package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 被邀请人Id</li>
 * <li>userName: 被邀请人名称</li>
 * <li>inviteType: 邀请类型</li>
 * <li>contactType: 联系类型</li>
 * <li>contactToken: 联系类型对应的联系方式，phone or email</li>
 * <li>registerTime: 被邀请人注册时间</li>
 * <li>invitorId: 邀请人Id</li>
 * <li>invitorName: 邀请人名称</li>
 * </ul>
 */
public class PropInvitedUserDTO {
    
    private Long userId;
    private String userName;
    private Byte inviteType;
    private Byte contactType;
    private String contactToken;
    private Timestamp registerTime;
    private Long invitorId;
    private String invitorName;
    
    public PropInvitedUserDTO() {
    }
    
	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Long getInvitorId() {
        return invitorId;
    }

    public void setInvitorId(Long invitorId) {
        this.invitorId = invitorId;
    }

    public String getInvitorName() {
        return invitorName;
    }

    public void setInvitorName(String invitorName) {
        this.invitorName = invitorName;
    }

    public Byte getInviteType() {
        return inviteType;
    }

    public void setInviteType(Byte inviteType) {
        this.inviteType = inviteType;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
