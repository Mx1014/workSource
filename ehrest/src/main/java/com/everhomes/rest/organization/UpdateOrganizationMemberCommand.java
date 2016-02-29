// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>memberId: id</li>
 * <li>organizationId: 组织id</li>
 * <li>contactName：成员名称</li>
 * <li>groupId：部门ID</li>
 * <li>gender：性别</li>
 * <li>employeeNo：工号</li>
 * </ul>
 */
public class UpdateOrganizationMemberCommand {
    @NotNull
    private Long id;
    private Long   organizationId;
	private String contactName;
	private Long groupId;
	private Byte gender;
	private String employeeNo;
	
	public UpdateOrganizationMemberCommand() {
    }
	
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	
	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	

	public String getEmployeeNo() {
		return employeeNo;
	}



	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
