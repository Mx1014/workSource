package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>list: 企业列表 参考 {@link com.everhomes.rest.activity.StatisticsTagDTO}</li>
 * </ul>
 */
public class StatisticsTagResponse {

	@ItemType(value = StatisticsTagDTO.class)
	private List<StatisticsTagDTO> list;
	
	public List<StatisticsTagDTO> getList() {
		return list;
	}

	public void setList(List<StatisticsTagDTO> list) {
		this.list = list;
	}

	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
