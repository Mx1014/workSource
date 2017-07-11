package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>name: 类型名称</li>
 * <li>parentId: 父类型ID</li>
 * <li>currentOrgId: 当前所在公司ID</li>
 * </ul>
 */
public class CreateTaskCategoryCommand {
	private Integer namespaceId;
	private String name;
	private Long parentId;

	private  Long currentOrgId;
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
