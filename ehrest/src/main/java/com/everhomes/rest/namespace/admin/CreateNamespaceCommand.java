// @formatter:off
package com.everhomes.rest.namespace.admin;

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
}
