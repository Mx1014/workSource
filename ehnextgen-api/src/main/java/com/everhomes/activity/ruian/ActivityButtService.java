package com.everhomes.activity.ruian;

import com.everhomes.rest.activity.ruian.ActivityCategoryModel;
import com.everhomes.rest.activity.ruian.ActivityModel;

import java.util.List;

public interface ActivityButtService {
    /**
     *
     * @return
     */
    public List<ActivityCategoryModel> getCategoryList(Integer namespaceId, Long level1Id);


    public List<ActivityModel> getActivityList(Integer namespaceId, Long activityCategoryID, Long activitySubCategoryID , Byte state , Integer pageSize , Integer pageIndex);

}
