// @formatter:off
package com.everhomes.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>communityId: 小区名称</li>
 * <li>targetType：注册用户类型</li>
 * <li>targetId：注册用户对应的userId</li>
 * <li>pmGroup：物业角色类型</li>
 * <li>contactName：未注册成员名称</li>
 * <li>contactType：未注册成员类型：0-手机，1-邮箱</li>
 * <li>contactToken：未注册成员标识</li>
 * <li>contactDescription：描述</li>
 * <li>status：状态</li>
 * </ul>
 */
public class UpdatePropMemberCommand {
    @NotNull
    private java.lang.Long   id;
    private java.lang.Long   communityId;
	private java.lang.String targetType;
	private java.lang.Long   targetId;
	private java.lang.String pmGroup;
	private java.lang.String contactName;
	private java.lang.Byte   contactType;
	private java.lang.String contactToken;
	private java.lang.String contactDescription;
	private java.lang.Byte   status;
	
	
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(java.lang.Long communityId) {
		this.communityId = communityId;
	}
	public java.lang.String getTargetType() {
		return targetType;
	}
	public void setTargetType(java.lang.String targetType) {
		this.targetType = targetType;
	}
	public java.lang.Long getTargetId() {
		return targetId;
	}
	public void setTargetId(java.lang.Long targetId) {
		this.targetId = targetId;
	}
	public java.lang.String getPmGroup() {
		return pmGroup;
	}
	public void setPmGroup(java.lang.String pmGroup) {
		this.pmGroup = pmGroup;
	}
	public java.lang.String getContactName() {
		return contactName;
	}
	public void setContactName(java.lang.String contactName) {
		this.contactName = contactName;
	}
	public java.lang.Byte getContactType() {
		return contactType;
	}
	public void setContactType(java.lang.Byte contactType) {
		this.contactType = contactType;
	}
	public java.lang.String getContactToken() {
		return contactToken;
	}
	public void setContactToken(java.lang.String contactToken) {
		this.contactToken = contactToken;
	}
	public java.lang.String getContactDescription() {
		return contactDescription;
	}
	public void setContactDescription(java.lang.String contactDescription) {
		this.contactDescription = contactDescription;
	}
	public java.lang.Byte getStatus() {
		return status;
	}
	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}

    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
