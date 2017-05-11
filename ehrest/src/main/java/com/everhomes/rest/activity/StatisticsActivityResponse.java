// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>list: 活动列表信息</li>
 * </ul>
 */
public class StatisticsActivityResponse {
	
	@ItemType(value = ActivityDTO.class)
	private List<ActivityDTO> list;
	
	public List<ActivityDTO> getList() {
		return list;
	}

	public void setList(List<ActivityDTO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
