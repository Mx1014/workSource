package com.everhomes.rest.acl;


import java.io.Serializable;

/**
 * <p>权限</p>
 * <ul>
 * <li>privilegeId: 权限id</li>
 * <li>privilegeName: 权限名称</li>
 * </ul>
 */
public class PrivilegeDTO  implements Serializable {

	private Long privilegeId;
	private String    privilegeName;

	public PrivilegeDTO(){

	}
	public PrivilegeDTO(Long privilegeId, String privilegeName){
		this.privilegeId = privilegeId;
		this.privilegeName = privilegeName;
	}

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

