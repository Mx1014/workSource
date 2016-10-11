package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * @author wh
 *<ul>
 *<li>nextPageAnchor:下一页</li>
 *<li>activeStats: 活跃信息列表 {@link com.everhomes.rest.activity.UserActiveStatDTO}</li>
 *</ul>
 */
public class ListActiveStatResponse {
    private Long nextPageAnchor;
    @ItemType(value = UserActiveStatDTO.class)
    private List<UserActiveStatDTO> activeStats;
 
 
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<UserActiveStatDTO> getActiveStats() {
		return activeStats;
	}

	public void setActiveStats(List<UserActiveStatDTO> activeStats) {
		this.activeStats = activeStats;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
