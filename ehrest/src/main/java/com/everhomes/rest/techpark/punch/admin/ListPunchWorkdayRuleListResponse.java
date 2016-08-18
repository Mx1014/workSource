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
 * <li>workdayRules: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO}</li>
 * </ul>
 */
public class ListPunchWorkdayRuleListResponse {
	private Long nextPageAnchor;

	@ItemType(PunchWorkdayRuleDTO.class)
	private List<PunchWorkdayRuleDTO> workdayRules;

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

	public List<PunchWorkdayRuleDTO> getWorkdayRules() {
		return workdayRules;
	}

	public void setWorkdayRules(List<PunchWorkdayRuleDTO> workdayRules) {
		this.workdayRules = workdayRules;
	}
 

}
