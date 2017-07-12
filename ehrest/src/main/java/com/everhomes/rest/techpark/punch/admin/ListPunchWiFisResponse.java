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
 * <li>wifis: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO}</li>
 * </ul>
 */
public class ListPunchWiFisResponse {
	private Long nextPageAnchor;

	@ItemType(PunchWiFiDTO.class)
	private List<PunchWiFiDTO> wifis;

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

	public List<PunchWiFiDTO> getWifis() {
		return wifis;
	}

	public void setWifis(List<PunchWiFiDTO> wifis) {
		this.wifis = wifis;
	}
 
 

}
