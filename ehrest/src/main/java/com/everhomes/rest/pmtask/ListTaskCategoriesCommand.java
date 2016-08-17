package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namspaceId: 域空间</li>
 * <li>parentId: 父类型Id</li>
 * </ul>
 */
public class ListTaskCategoriesCommand {
	private Integer namspaceId;
	private Long parentId;

	public Integer getNamspaceId() {
		return namspaceId;
	}

	public void setNamspaceId(Integer namspaceId) {
		this.namspaceId = namspaceId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
