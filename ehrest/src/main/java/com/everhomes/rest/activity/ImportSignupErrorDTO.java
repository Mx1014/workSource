//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>rowNum: rowNum</li>
 *     <li>handleType: handleType</li>
 *     <li>description: description</li>
 * </ul>
 */
public class ImportSignupErrorDTO {
	private Integer rowNum;

	private Byte handleType;

	private String description;

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getHandleType() {
		return handleType;
	}

	public void setHandleType(Byte handleType) {
		this.handleType = handleType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
