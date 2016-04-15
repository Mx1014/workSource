// @formatter:off
package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>targetId：对象ID</li>
 * <li>roleId：角色ID</li>
 * <li>organizationId：机构ID</li>
 * </ul>
 */
public class SetAclRoleAssignmentCommand {
	
	private Long targetId;
	
	private Long roleId;
	
	private Long organizationId;
	
	public SetAclRoleAssignmentCommand() {
    }

	public Long getTargetId() {
		return targetId;
	}



	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}



	public Long getRoleId() {
		return roleId;
	}



	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}



	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
