// @formatter:off
package com.everhomes.rest.namespace.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>name: 名称</li>
 * <li>resourceType: 类型</li>
 * </ul>
 */
public class NamespaceInfoDTO {
	private Integer id;
	private String name;
	private String resourceType;

	
	public NamespaceInfoDTO() {
		super();
	}

	public NamespaceInfoDTO(Integer id, String name, String resourceType) {
		super();
		this.id = id;
		this.name = name;
		this.resourceType = resourceType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
