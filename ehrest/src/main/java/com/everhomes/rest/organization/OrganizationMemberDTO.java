// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>organizationId: 小区id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>memberGroup：组织角色类型 参考{@link com.everhomes.rest.organization.pm.PmMemberGroup}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>contactDescription：描述</li>
 * <li>status：状态</li>
 * </ul>
 */
public class OrganizationMemberDTO {
	@NotNull
    private Long   id;
	@NotNull
    private Long   organizationId;
	private String   organizationName;
	private String targetType;
    @NotNull
	private Long   targetId;

	private String memberGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;
	private Byte   status;
	
	@ItemType(AclRoleAssignmentsDTO.class)
	private List<AclRoleAssignmentsDTO> aclRoles;
	
	private Long groupId;
	
	private String groupName;
	
	private java.lang.String   nickName;
	private java.lang.String   avatar;
	
	private java.lang.Long creatorUid;
	
	public OrganizationMemberDTO() {
    }
	
	

	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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



	public String getMemberGroup() {
		return memberGroup;
	}



	public void setMemberGroup(String memberGroup) {
		this.memberGroup = memberGroup;
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



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getOrganizationName() {
		return organizationName;
	}



	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}






	public Long getGroupId() {
		return groupId;
	}



	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public List<AclRoleAssignmentsDTO> getAclRoles() {
		return aclRoles;
	}



	public void setAclRoles(List<AclRoleAssignmentsDTO> aclRoles) {
		this.aclRoles = aclRoles;
	}



	public java.lang.String getNickName() {
		return nickName;
	}



	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}



	public java.lang.String getAvatar() {
		return avatar;
	}



	public void setAvatar(java.lang.String avatar) {
		this.avatar = avatar;
	}



	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}



	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
