package com.everhomes.rest.acl;




/**
 * <p>权限</p>
 * <ul>
 * <li>privilegeId: 权限id</li>
 * <li>privilegeName: 权限名称</li>
 * </ul>
 */
public class PrivilegeDTO {

	private Long privilegeId;
	private String    privilegeName;

	public Long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}
}

