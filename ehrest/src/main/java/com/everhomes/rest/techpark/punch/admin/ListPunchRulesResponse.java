package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>punchRuleDTOs: 结果{@link com.everhomes.rest.techpark.punch.PunchRuleDTO}</li>
 * </ul>
 */
public class ListPunchRulesResponse {
	private Long nextPageAnchor;

	@ItemType(PunchRuleDTO.class)
	private List<PunchRuleDTO> punchRuleDTOs;

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

	public List<PunchRuleDTO> getPunchRuleDTOs() {
		return punchRuleDTOs;
	}

	public void setPunchRuleDTOs(List<PunchRuleDTO> punchRuleDTOs) {
		this.punchRuleDTOs = punchRuleDTOs;
	} 
 

}
