package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>wifiRules: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO}</li>
 * </ul>
 */
public class ListPunchWiFiRuleListResponse {
	private Long nextPageAnchor;

	@ItemType(PunchWiFiRuleDTO.class)
	private List<PunchWiFiRuleDTO> wifiRules;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PunchWiFiRuleDTO> getWifiRules() {
		return wifiRules;
	}

	public void setWifiRules(List<PunchWiFiRuleDTO> wifiRules) {
		this.wifiRules = wifiRules;
	} 
 

}
