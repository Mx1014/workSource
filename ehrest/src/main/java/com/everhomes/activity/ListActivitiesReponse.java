package com.everhomes.activity;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListActivitiesReponse {
    private Long pageAnchor;
    @ItemType(value = ActivityDTO.class)
    private List<ActivityDTO> activities;

    public ListActivitiesReponse(Long pageAnchor, List<ActivityDTO> activities) {
        super();
        this.pageAnchor = pageAnchor;
        this.activities = activities;
    }

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
