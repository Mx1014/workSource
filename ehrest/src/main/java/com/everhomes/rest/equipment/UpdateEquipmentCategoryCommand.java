package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 类型名称</li>
 * <li>id: id</li>
 * </ul>
 */
public class UpdateEquipmentCategoryCommand {

	private String name;
	@NotNull
	private Long id;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
