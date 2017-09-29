package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * 
 *<ul>
 *<li>nextPageAnchor:下一页</li>
 *<li>activities: 活动列表参考{@link ActivityDTO}</li>
 *</ul>
 */
public class ListActivitiesReponse {
    private Long nextPageAnchor;
    @ItemType(value = ActivityDTO.class)
    private List<ActivityDTO> activities;

    public ListActivitiesReponse(Long nextPageAnchor, List<ActivityDTO> activities) {
        super();
        this.nextPageAnchor = nextPageAnchor;
        this.activities = activities;
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

   

}
