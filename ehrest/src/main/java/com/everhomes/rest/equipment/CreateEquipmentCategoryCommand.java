package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 类型名称</li>
 * <li>parentId: 父类型ID</li>
 * </ul>
 */
public class CreateEquipmentCategoryCommand {

	private String name;
	@NotNull
	private Long parentId;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
