package com.everhomes.rest.ui.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.activity.ActivityDTO;
/**
 * 
 * @author elians
 *<ul>
 *<li>nextPageAnchor:下一页</li>
 *<li>activities: 活动列表</li>
 *</ul>
 */
public class ListOfficialActivitiesBySceneReponse {
    private Long nextPageAnchor;
    @ItemType(value = ActivityDTO.class)
    private List<ActivityDTO> activities;

    public ListOfficialActivitiesBySceneReponse(Long nextPageAnchor, List<ActivityDTO> activities) {
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
