package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>roleId: 角色id</li>
 * <li>organizationId: 机构id </li>
 * <li>targetType: 对象类型 参考{@link com.everhomes.entity.EntityType} </li>
 * <li>targetId: 对象id </li>
 * </ul>
 */
public class DeleteAclRoleAssignmentCommand {
	
	private Long organizationId;
	
	private Long roleId;
	
	private String targetType;
	
	private Long targetId;
    
    
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
