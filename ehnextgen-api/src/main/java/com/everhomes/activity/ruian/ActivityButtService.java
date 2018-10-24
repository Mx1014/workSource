package com.everhomes.activity.ruian;

import com.everhomes.rest.activity.ruian.ActivityCategoryModel;
import com.everhomes.rest.activity.ruian.ActivityDetailDTO;
import com.everhomes.rest.activity.ruian.ActivityDetailModel;
import com.everhomes.rest.activity.ruian.ActivityModel;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneReponse;

import java.util.List;

public interface ActivityButtService {

    public List<ActivityCategoryModel> getCategoryList(Integer namespaceId, Long level1Id);


    public List<ActivityModel> getActivityList(Integer namespaceId, Long activityCategoryID, Long activitySubCategoryID , Byte state , Integer pageSize , Integer pageIndex);

    public ActivityDetailModel getActivityDetail(Integer namespaceId, Long activityId);

    public ListActivityPromotionEntitiesBySceneReponse listActivityRuiAnEntitiesByScene(ListActivityPromotionEntitiesBySceneCommand cmd);

}
