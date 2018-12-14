package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>spaceId: 空间id</li> 
 * </ul>
 */
public class GetLongRentPriceCommand {
	private Long spaceId;


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
}
