package com.everhomes.activity;

import com.everhomes.category.Category;
import com.everhomes.forum.Post;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.CreateWechatJsPayOrderResp;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneReponse;
import com.everhomes.rest.ui.activity.ListActivityCategoryCommand;
import com.everhomes.rest.ui.activity.ListActivityCategoryReponse;
import com.everhomes.rest.ui.user.GetVideoPermissionInfoCommand;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.ui.user.RequestVideoPermissionCommand;
import com.everhomes.rest.ui.user.UserVideoPermissionDTO;
import com.everhomes.util.Tuple;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface ActivityService {

    void createPost(ActivityPostCommand cmd, Long postId);

    ActivityDTO signup(ActivitySignupCommand cmd);

    CommonOrderDTO createSignupOrder(CreateSignupOrderCommand cmd);

	CreateWechatJsPayOrderResp createWechatJsSignupOrder(CreateWechatJsSignupOrderCommand cmd);

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

	ListActivitiesReponse listOfficialActivitiesByScene(
			ListNearbyActivitiesBySceneCommand command);

    UserVideoPermissionDTO GetVideoPermisionInfo(GetVideoPermissionInfoCommand cmd);

    UserVideoPermissionDTO requestVideoPermission(RequestVideoPermissionCommand cmd);

    ActivityVideoDTO setActivityVideo(SetActivityVideoInfoCommand cmd);

    void videoCallback(VideoCallbackCommand cmd);

    ActivityVideoDTO getActivityVideo(GetActivityVideoInfoCommand cmd);

	ListOfficialActivityByNamespaceResponse listOfficialActivityByNamespace(ListOfficialActivityByNamespaceCommand cmd);

    void createScheduleForActivity(Activity act);

    void onActivityFinished(Long activityId, Long endTime);

    void onVideoDeviceChange(YzbVideoDeviceChangeCommand cmd);

    VideoCapabilityResponse getVideoCapability(GetVideoCapabilityCommand cmd);

	GetActivityDetailByIdResponse getActivityDetailById(GetActivityDetailByIdCommand cmd);

	ActivityWarningResponse setActivityWarning(SetActivityWarningCommand cmd);

	ActivityWarningResponse queryActivityWarning(GetActivityWarningCommand cmd);
	
	ActivityTimeResponse setActivityTime(SetActivityTimeCommand cmd);

	ActivityTimeResponse getActivityTime(GetActivityTimeCommand cmd);
	
	RosterOrderSettingDTO setRosterOrderSetting(SetRosterOrderSettingCommand cmd);

	RosterOrderSettingDTO getRosterOrderSetting(GetRosterOrderSettingCommand cmd);

	void activityWarningSchedule();
	
	ListActivitiesReponse listOfficialActivities(QueryOrganizationTopicCommand cmd);
	
	List<ActivityCategoryDTO> listActivityEntryCategories(ListActivityEntryCategoriesCommand cmd);

    /**
     * 根据场景获取活动运营数据
     */
    ListActivityPromotionEntitiesBySceneReponse listActivityPromotionEntitiesByScene(ListActivityPromotionEntitiesBySceneCommand cmd);

    void setActivityAchievement(SetActivityAchievementCommand cmd);

    GetActivityAchievementResponse getActivityAchievement(GetActivityAchievementCommand cmd);

    void createActivityAttachment(CreateActivityAttachmentCommand cmd);

    void deleteActivityAttachment(DeleteActivityAttachmentCommand cmd);

    ListActivityAttachmentsResponse listActivityAttachments(ListActivityAttachmentsCommand cmd);

    void downloadActivityAttachment(DownloadActivityAttachmentCommand cmd);

    void createActivityGoods(CreateActivityGoodsCommand cmd);

    void updateActivityGoods(UpdateActivityGoodsCommand cmd);

    void deleteActivityGoods(DeleteActivityGoodsCommand cmd);

    ListActivityGoodsResponse listActivityGoods(ListActivityGoodsCommand cmd);

    ActivityGoodsDTO getActivityGoods(GetActivityGoodsCommand cmd);

	SignupInfoDTO manualSignup(ManualSignupCommand cmd);

	SignupInfoDTO updateSignupInfo(UpdateSignupInfoCommand cmd);

	ImportSignupInfoResponse importSignupInfo(ImportSignupInfoCommand cmd, MultipartFile[] files);

	ListSignupInfoResponse listSignupInfo(ListSignupInfoCommand cmd);

	void exportSignupInfo(ExportSignupInfoCommand cmd, HttpServletResponse response);

	void deleteSignupInfo(DeleteSignupInfoCommand cmd);

	ListActivityCategoryReponse listActivityCategory(ListActivityCategoryCommand cmd);

	SignupInfoDTO vertifyPersonByPhone(VertifyPersonByPhoneCommand cmd);
	
	/**
	 * 统计总览
	 * @return
	 */
	StatisticsSummaryResponse statisticsSummary(StatisticsSummaryCommand cmd);
	/**
	 * 按活动统计
	 * @param cmd
	 * @return
	 */
	StatisticsActivityResponse statisticsActivity(StatisticsActivityCommand cmd);
	/**
	 * 按企业统计
	 * @param cmd
	 * @return
	 */
	StatisticsOrganizationResponse statisticsOrganization(StatisticsOrganizationCommand cmd);
	/**
	 * 按标签统计
	 * @param cmd
	 * @return
	 */
	StatisticsTagResponse statisticsTag(StatisticsTagCommand cmd);
	
	void signupOrderRefund(Activity activity, Long userId);
	
	/**
	 * 同步报名人数
	 */
	void syncActivitySignupAttendeeCount();

	void exportErrorInfo(ExportErrorInfoCommand cmd, HttpServletResponse response);
}

