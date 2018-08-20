package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 点击类型</li>
 * <li>name: 点击类型名称</li>
 * </ul>
 */
public class ClickTypeDTO {
	
	private Byte type;
	private String name;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

}
