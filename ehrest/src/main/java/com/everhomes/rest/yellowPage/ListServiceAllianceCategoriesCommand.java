package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> parentId: 父id</li>
 * </ul>
 */
public class ListServiceAllianceCategoriesCommand {

	private Long parentId;

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
