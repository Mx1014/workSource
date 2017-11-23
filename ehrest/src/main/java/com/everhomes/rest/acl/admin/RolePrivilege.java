package com.everhomes.rest.acl.admin;


import java.io.Serializable;

/**
 * <ul>
 * <li>type: 类型 1 模块 3 权限</li>
 * <li>id: 模块或者权限id</li>
 * </ul>
 */
public class RolePrivilege implements Serializable {

	private Byte type;

	private Long id;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}