package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>list: 企业列表 参考 {@link com.everhomes.rest.activity.StatisticsOrganizationDTO}</li>
 * </ul>
 */
public class StatisticsOrganizationResponse {

	@ItemType(value = StatisticsOrganizationDTO.class)
	private List<StatisticsOrganizationDTO> list;
	
	public List<StatisticsOrganizationDTO> getList() {
		return list;
	}

	public void setList(List<StatisticsOrganizationDTO> list) {
		this.list = list;
	}

	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
