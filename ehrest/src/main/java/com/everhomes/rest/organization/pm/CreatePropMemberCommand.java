// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.OrganizationMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>memberGroup：组织角色类型 参考{@link com.everhomes.rest.organization.OrganizationMemberGroupType}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>contactDescription：描述</li>
 * </ul>
 */
public class CreatePropMemberCommand {
    @NotNull
    private Long   communityId;
   
	private String targetType;
    @NotNull
	private Long   targetId;

	private String memberGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;
	
	public CreatePropMemberCommand() {
    }
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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
	public String getContactDescription() {
		return contactDescription;
	}
	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}
	
	
	public String getMemberGroup() {
		return memberGroup;
	}

	public void setMemberGroup(String memberGroup) {
		this.memberGroup = memberGroup;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
