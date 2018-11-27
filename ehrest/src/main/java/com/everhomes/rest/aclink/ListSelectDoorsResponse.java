// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> groupRels: 门禁组关系列表，参考{@link DoorAccessGroupRelDTO}</li>
 * </ul>
 *
 */
public class ListSelectDoorsResponse {

	@ItemType(DoorAccessDTO.class)
	private List<DoorAccessDTO> doors;

	public List<DoorAccessDTO> getDoors() {
		return doors;
	}

	public void setDoors(List<DoorAccessDTO> doors) {
		this.doors = doors;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
