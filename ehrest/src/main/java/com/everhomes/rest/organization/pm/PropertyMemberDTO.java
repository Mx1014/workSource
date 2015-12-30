// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>communityId: 小区id</li>
 * <li>communityName: 小区名称</li>
 * <li>organizationId: 组织id</li>
 * <li>organizationName: 组织名称</li>
 * <li>targetType：注册用户类型</li>
 * <li>targetId：注册用户对应的userId</li>
 * <li>pmGroup：物业角色类型 参考{@link com.everhomes.PmMemberGroup.PmGroup}</li>
 * <li>contactName：未注册成员名称</li>
 * <li>contactType：未注册成员类型：0-手机，1-邮箱</li>
 * <li>contactToken：未注册成员标识</li>
 * <li>contactDescription：描述</li>
 * <li>status：状态 参考{@link com.everhomes.pm.PmMemberStatus}</li>
 * </ul>
 */
public class PropertyMemberDTO {
	private Long   id;
	private Long   communityId;
	private String communityName;
	private Long   organizationId;
	private String organizationName;
	private String targetType;
	private Long   targetId;
	private String pmGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;
	private Byte   status;
	
    
    public PropertyMemberDTO() {
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

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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

	public String getPmGroup() {
		return pmGroup;
	}

	public void setPmGroup(String pmGroup) {
		this.pmGroup = pmGroup;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
