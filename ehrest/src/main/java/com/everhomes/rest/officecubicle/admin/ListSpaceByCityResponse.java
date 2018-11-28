package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>space:空间列表{@link com.everhomes.rest.officecubicle.admin.SpaceForAppDTO}</li>
 * </ul>
 */
public class ListSpaceByCityResponse {

	private List<SpaceForAppDTO> space;

	public List<SpaceForAppDTO> getSpace() {
		return space;
	}



	public void setSpace(List<SpaceForAppDTO> space) {
		this.space = space;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
