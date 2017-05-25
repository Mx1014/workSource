// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>list: 活动列表信息 {@link com.everhomes.rest.activity.StatisticsActivityDTO}</li>
 * </ul>
 */
public class StatisticsActivityResponse {
	
	@ItemType(value = StatisticsActivityDTO.class)
	private List<StatisticsActivityDTO> list;
	
	public List<StatisticsActivityDTO> getList() {
		return list;
	}

	public void setList(List<StatisticsActivityDTO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
