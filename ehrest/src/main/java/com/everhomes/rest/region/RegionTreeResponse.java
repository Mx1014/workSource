package com.everhomes.rest.region;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>list: 树状结构</li>
 * </ul>
 */
public class RegionTreeResponse {
	@ItemType(RegionTreeDTO.class)
	private List<RegionTreeDTO> list;

	public List<RegionTreeDTO> getList() {
		return list;
	}

	public void setList(List<RegionTreeDTO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
