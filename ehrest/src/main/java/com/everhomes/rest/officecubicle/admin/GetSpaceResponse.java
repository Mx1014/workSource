package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>spaceid:空间id,如果是空则为初始状态</li>
 * </ul>
 */
public class GetSpaceResponse {

	private Long spaceId;

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
