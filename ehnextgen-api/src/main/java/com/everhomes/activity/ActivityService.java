package com.everhomes.activity;

import com.everhomes.category.Category;
import com.everhomes.forum.Post;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.ListGeneralFormsCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.CreateWechatJsPayOrderResp;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneReponse;
import com.everhomes.rest.ui.activity.ListActivityCategoryCommand;
import com.everhomes.rest.ui.activity.ListActivityCategoryReponse;
import com.everhomes.rest.ui.user.GetVideoPermissionInfoCommand;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.ui.user.RequestVideoPermissionCommand;
import com.everhomes.rest.ui.user.UserVideoPermissionDTO;
import com.everhomes.util.Tuple;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface ActivityService {

    void createPost(ActivityPostCommand cmd, Long postId, Long communityId);

    ActivityDTO signup(ActivitySignupCommand cmd);

    CommonOrderDTO createSignupOrder(CreateSignupOrderCommand cmd);

    //更换使用新的支付V3
//    PreOrderDTO createSignupOrderV2(CreateSignupOrderV2Command cmd);

//    PreOrderDTO createSignupOrderV3(CreateSignupOrderV2Command cmd);

    PreOrderDTO createUniteSignupOrder(CreateSignupOrderV2Command cmd);

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

    ListSignupInfoByOrganizationIdResponse listSignupInfoByOrganizationId(Long organizationId, Integer namespaceId, Long pageAnchor, int pageSize);

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

	/**
	 * 通过categoryId查询整个域空间的活动帖子，不区分园区、可见性等
	 * @param cmd
	 * @return
	 */
	ListActivitiesByCategoryIdResponse listActivitiesByCategoryId(ListActivitiesByCategoryIdCommand cmd);
	
	void signupOrderRefund(Activity activity, Long userId);
	
	/**
	 * 同步报名人数
	 */
	void syncActivitySignupAttendeeCount();

    GetActivityPayeeDTO getActivityPayee(GetActivityPayeeCommand cmd);

	List<ActivityPayeeDTO> listActivityPayee(ListActivityPayeeCommand cmd);

	void createOrUpdateActivityPayee(CreateOrUpdateActivityPayeeCommand cmd);

	CheckPayeeIsUsefulResponse checkPayeeIsUseful(CheckPayeeIsUsefulCommand cmd);

	void payNotify(OrderPaymentNotificationCommand cmd);

	void exportActivity(ExportActivityCommand cmd);

	void exportOrganization(ExportOrganizationCommand cmd);

	void exportTag(ExportTagCommand cmd);

//	void exportErrorInfo(ExportErrorInfoCommand cmd, HttpServletResponse response);
	void exportActivitySignupTemplate(ExportActivitySignupTemplateCommand cmd, HttpServletResponse httpResponse);

	void exportActivitySignupNew(ExportSignupInfoCommand cmd);

    OutputStream getActivitySignupExportStream(ExportSignupInfoCommand cmd, Long taskId);

    ListGeneralFormResponse listActivitySignupGeneralForms(ListGeneralFormsCommand cmd);

	GeneralFormDTO updateGeneralForm(UpdateActivityFormCommand cmd);
    void deleteActivityFormById(ActivityFormIdCommand cmd);
}

