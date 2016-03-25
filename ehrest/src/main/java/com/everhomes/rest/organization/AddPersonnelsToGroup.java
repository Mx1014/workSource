// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ids: 批量调整的通讯录ID</li>
 * <li>groupId: 部门id</li>
 * </ul>
 */
public class AddPersonnelsToGroup {
	
    @NotNull
    @ItemType(Long.class)
    private List<Long> ids;
    
	private Long groupId;
	
	public AddPersonnelsToGroup() {
    }
	

	public List<Long> getIds() {
		return ids;
	}


	public void setIds(List<Long> ids) {
		this.ids = ids;
	}


	public Long getGroupId() {
		return groupId;
	}


	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
