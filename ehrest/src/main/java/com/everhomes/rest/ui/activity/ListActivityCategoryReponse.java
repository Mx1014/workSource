package com.everhomes.rest.ui.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.activity.ActivityCategoryDTO;

/**
 * 
 * <ul>
 * <li>activityCategoryList: 分类列表，参考{@link com.everhomes.rest.activity.ActivityCategoryDTO}</li>
 * </ul>
 */
public class ListActivityCategoryReponse {
	@ItemType(ActivityCategoryDTO.class)
	private List<ActivityCategoryDTO> activityCategoryList;

	public ListActivityCategoryReponse() {
		super();
	}

	public ListActivityCategoryReponse(List<ActivityCategoryDTO> activityCategoryList) {
		super();
		this.activityCategoryList = activityCategoryList;
	}

	public List<ActivityCategoryDTO> getActivityCategoryList() {
		return activityCategoryList;
	}

	public void setActivityCategoryList(List<ActivityCategoryDTO> activityCategoryList) {
		this.activityCategoryList = activityCategoryList;
	}
	
}
