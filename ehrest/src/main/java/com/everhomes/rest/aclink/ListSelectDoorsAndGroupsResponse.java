// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * </ul>
 *
 */
public class ListSelectDoorsAndGroupsResponse {

	@ItemType(DoorsAndGroupsDTO.class)
	private List<DoorsAndGroupsDTO> doors;

	public List<DoorsAndGroupsDTO> getDoors() {
		return doors;
	}

	public void setDoors(List<DoorsAndGroupsDTO> doors) {
		this.doors = doors;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
