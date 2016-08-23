// @formatter:off
package com.everhomes.rest.namespace.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>name: 名称</li>
 * <li>resourceType: 类型</li>
 * </ul>
 */
public class CreateNamespaceCommand {
	private String name;
	private String resourceType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
