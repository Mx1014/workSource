package com.everhomes.activity;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListActivitiesReponse {
    private Long nextAnchor;
    @ItemType(value = ActivityDTO.class)
    private List<ActivityDTO> activities;

    public ListActivitiesReponse(Long nextAnchor, List<ActivityDTO> activities) {
        super();
        this.nextAnchor = nextAnchor;
        this.activities = activities;
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

}
