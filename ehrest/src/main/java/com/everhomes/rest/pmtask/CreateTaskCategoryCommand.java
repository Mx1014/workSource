package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namspaceId: 域空间</li>
 * <li>name: 类型名称</li>
 * <li>parentId: 父类型ID</li>
 * </ul>
 */
public class CreateTaskCategoryCommand {
	private Integer namspaceId;
	private String name;
	private Long parentId;
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getNamspaceId() {
		return namspaceId;
	}
	public void setNamspaceId(Integer namspaceId) {
		this.namspaceId = namspaceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
