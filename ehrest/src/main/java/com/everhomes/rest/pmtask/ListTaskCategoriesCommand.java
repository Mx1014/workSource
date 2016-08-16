package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namspaceId: 域空间</li>
 * </ul>
 */
public class ListTaskCategoriesCommand {
	private Integer namspaceId;

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
}
