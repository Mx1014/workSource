// @formatter:off
package com.everhomes.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>communityId: 小区名称</li>
 * <li>contactName：业主名称</li>
 * <li>contactType：业主类型：0-手机，1-邮箱</li>
 * <li>contactToken：业主标识</li>
 * <li>creatorUid：创建者id</li>
 * <li>createTime：创建时间</li>
 * </ul>
 */
public class UpdatePropOwnerCommand {
    @NotNull
    private java.lang.Long     id;
	private java.lang.Long     communityId;
	private java.lang.String   contactName;
	private java.lang.Byte     contactType;
	private java.lang.String   contactToken;
	private java.lang.Long     creatorUid;
	private java.sql.Timestamp createTime;
	
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
	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
