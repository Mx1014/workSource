// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 *<ul>
 *<li>nextPageAnchor:下一页</li>
 *<li>activities: 活动列表，参考{@link com.everhomes.rest.activity.ActivityDTO}</li>
 *</ul>
 */
public class ListOfficialActivityByNamespaceResponse {
	private Long nextPageAnchor;
	@ItemType(value = ActivityDTO.class)
	private List<ActivityDTO> activities;

	public ListOfficialActivityByNamespaceResponse(Long nextPageAnchor, List<ActivityDTO> activities) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.activities = activities;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ActivityDTO> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityDTO> activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
