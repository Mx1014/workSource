// @formatter:off
package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

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
    private Long     id;
	private Long     communityId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	private Long     creatorUid;
	private String createTime;
	
	public UpdatePropOwnerCommand() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
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
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
