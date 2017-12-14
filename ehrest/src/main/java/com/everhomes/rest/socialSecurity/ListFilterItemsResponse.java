// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>filterItems: 筛选项</li>
 * </ul>
 */
public class ListFilterItemsResponse {

	@ItemType(FilterItemDTO.class)
	private List<FilterItemDTO> filterItems;

	public ListFilterItemsResponse() {

	}

	public ListFilterItemsResponse(List<FilterItemDTO> filterItems) {
		super();
		this.filterItems = filterItems;
	}

	public List<FilterItemDTO> getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(List<FilterItemDTO> filterItems) {
		this.filterItems = filterItems;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
