package com.everhomes.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * 
 * @author elians
 *pageAnchor:分页参数
 */
public class ListNearbyActivitiesResponse {
    
    @ItemType(ActivityDTO.class)
    private List<ActivityDTO> activities;
    
    private Long pageAnchor;

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

}
