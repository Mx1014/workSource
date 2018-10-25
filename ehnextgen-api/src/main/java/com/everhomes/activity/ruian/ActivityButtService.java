package com.everhomes.activity.ruian;

import com.everhomes.rest.activity.ruian.*;
import com.everhomes.rest.ui.activity.ruian.ListRuianActivityBySceneReponse;

import java.util.List;

public interface ActivityButtService {

    public List<ActivityCategoryModel> getCategoryList(Integer namespaceId, Long communityId , Long level1Id);


    public List<ActivityModel> getActivityList(Integer namespaceId,Long communityId ,  Long activityCategoryID, Long activitySubCategoryID , Byte state , Integer pageSize , Integer pageIndex);

    public ActivityDetailModel getActivityDetail(Integer namespaceId, Long communityId , Long activityId);

    public ListRuianActivityBySceneReponse listActivityRuiAnEntitiesByScene();

}
