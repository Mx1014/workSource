package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namspaceId: 域空间</li>
 * <li>id: 类型id</li>
 * <li>parentId: 父类型ID</li>
 * <li>currentOrgId: 当前机构id</li>
 * </ul>
 */
public class DeleteTaskCategoryCommand {
	private Long id;
	private Long parentId;
    private Integer namespaceId;
	private  Long currentOrgId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
