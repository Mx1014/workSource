package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * 
 * @author elians
 *nextPageAnchor:分页参数
 */
public class ListNearbyActivitiesResponse {
    
    @ItemType(ActivityDTO.class)
    private List<ActivityDTO> activities;
    
    private Long nextPageAnchor;

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
