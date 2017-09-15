// @formatter:off
package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>id：成员的detailId</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>memberGroup：组织角色类型 参考{@link com.everhomes.rest.organization.pm.PmMemberGroup}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>contactDescription：描述</li>
 * <li>status：状态</li>
 * <li>department：部门</li>
 * </ul>
 */
public class OrganizationMemberDetailDTO {
	
	private Long id;
	private String targetType;
	private Long   targetId;
	private String memberGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;
	private Byte   status;
	private Long groupId;
	private String groupName;
	private String   avatar;
	private Long   employeeNo;
	private Byte   gender;
	private String department;
	
	private OrganizationDetailDTO organizationDetailDTO;
	
	public OrganizationMemberDetailDTO() {
    }

	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
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



	public String getAvatar() {
		return avatar;
	}



	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}



	public Long getEmployeeNo() {
		return employeeNo;
	}



	public void setEmployeeNo(Long employeeNo) {
		this.employeeNo = employeeNo;
	}



	public Byte getGender() {
		return gender;
	}



	public void setGender(Byte gender) {
		this.gender = gender;
	}



	public OrganizationDetailDTO getOrganizationDetailDTO() {
		return organizationDetailDTO;
	}



	public void setOrganizationDetailDTO(OrganizationDetailDTO organizationDetailDTO) {
		this.organizationDetailDTO = organizationDetailDTO;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getDepartment() {
		return department;
	}



	public void setDepartment(String department) {
		this.department = department;
	}
}
