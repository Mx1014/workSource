package com.everhomes.activity;

import java.util.List;

import javax.validation.Valid;

import com.everhomes.category.Category;
import com.everhomes.forum.Post;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCheckinCommand;
import com.everhomes.rest.activity.ActivityConfirmCommand;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityListCommand;
import com.everhomes.rest.activity.ActivityListResponse;
import com.everhomes.rest.activity.ActivityPostCommand;
import com.everhomes.rest.activity.ActivityRejectCommand;
import com.everhomes.rest.activity.ActivityShareDetailResponse;
import com.everhomes.rest.activity.ActivitySignupCommand;
import com.everhomes.rest.activity.ActivityTokenDTO;
import com.everhomes.rest.activity.GetActivityShareDetailCommand;
import com.everhomes.rest.activity.ListActivitiesByLocationCommand;
import com.everhomes.rest.activity.ListActivitiesByNamespaceIdAndTagCommand;
import com.everhomes.rest.activity.ListActivitiesByTagCommand;
import com.everhomes.rest.activity.ListActivitiesCommand;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.activity.ListActivityCategoriesCommand;
import com.everhomes.rest.activity.ListNearByActivitiesCommand;
import com.everhomes.rest.activity.ListNearByActivitiesCommandV2;
import com.everhomes.rest.activity.ListOrgNearbyActivitiesCommand;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.util.Tuple;

public interface ActivityService {

    void createPost(ActivityPostCommand cmd, Long postId);

    ActivityDTO signup(ActivitySignupCommand cmd);

    ActivityDTO cancelSignup(ActivityCancelSignupCommand cmd);

    ActivityDTO checkin(ActivityCheckinCommand cmd);

    ActivityListResponse findActivityDetails(ActivityListCommand cmd);

    ActivityDTO findSnapshotByPostId(Long postId);

    ActivityDTO confirm(ActivityConfirmCommand cmd);

    void rejectPost(ActivityRejectCommand cmd);

    ActivityListResponse findActivityDetailsByPostId(Post post);

    List<Category> listActivityCategories(ListActivityCategoriesCommand cmd);

    Tuple<Long, List<ActivityDTO>> listActivities(ListActivitiesCommand cmd);

    Tuple<Long, List<ActivityDTO>> listNearByActivities(ListNearByActivitiesCommand cmd);
    
    boolean isPostIdExist(Long postId);
    
    void updatePost(ActivityPostCommand cmd, Long postId);
    
    Tuple<Long, List<ActivityDTO>> listNearByActivitiesV2(ListNearByActivitiesCommandV2 cmdV2);
    
    Tuple<Long, List<ActivityDTO>> listCityActivities(ListNearByActivitiesCommandV2 cmdV2);
    
    ListActivitiesReponse listActivitiesByTag(ListActivitiesByTagCommand cmd);
    
    /**
     * <p>按经纬度、活动类型列出活动列表。</p>
     * <p>先把经纬度（可能多个）按GEO算法处理成位置字符串，再根据周边/同城范围/所有范围来取字符串个数，然后再使用like的方式获取符合位置条件的活动信息。</p>
     * @param cmd 经纬度、活动类型、分页参数
     * @return 活动列表
     */
    ListActivitiesReponse listActivitiesByLocation(ListActivitiesByLocationCommand cmd);
    
//    public Tuple<Long, List<ActivityDTO>> listActivitiesByNamespaceIdAndTag(
//			ListActivitiesByNamespaceIdAndTagCommand cmd);
    ListActivitiesReponse listActivitiesByNamespaceIdAndTag(ListActivitiesByNamespaceIdAndTagCommand cmd);
    
    ListActivitiesReponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd);

    ListActivitiesReponse listOrgNearbyActivities(ListOrgNearbyActivitiesCommand cmd);
    
    ActivityShareDetailResponse getActivityShareDetail(ActivityTokenDTO postToken);
}
