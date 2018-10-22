// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupName:门禁组名称</li>
 * </ul>
 */
public class CreateDoorAccessGroupCommand {
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
