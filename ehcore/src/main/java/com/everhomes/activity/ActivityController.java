// @formatter:off
package com.everhomes.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.AclProvider;
import com.everhomes.category.Category;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCategoryDTO;
import com.everhomes.rest.activity.ActivityCheckinCommand;
import com.everhomes.rest.activity.ActivityConfirmCommand;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityGoodsDTO;
import com.everhomes.rest.activity.ActivityListCommand;
import com.everhomes.rest.activity.ActivityListResponse;
import com.everhomes.rest.activity.ActivityRejectCommand;
import com.everhomes.rest.activity.ActivityShareDetailResponse;
import com.everhomes.rest.activity.ActivitySignupCommand;
import com.everhomes.rest.activity.ActivityTokenDTO;
import com.everhomes.rest.activity.ActivityVideoDTO;
import com.everhomes.rest.activity.ActivityWarningResponse;
import com.everhomes.rest.activity.CreateActivityAttachmentCommand;
import com.everhomes.rest.activity.CreateActivityGoodsCommand;
import com.everhomes.rest.activity.DeleteActivityAttachmentCommand;
import com.everhomes.rest.activity.DeleteActivityGoodsCommand;
import com.everhomes.rest.activity.DeleteSignupInfoCommand;
import com.everhomes.rest.activity.DownloadActivityAttachmentCommand;
import com.everhomes.rest.activity.ExportSignupInfoCommand;
import com.everhomes.rest.activity.GetActivityAchievementCommand;
import com.everhomes.rest.activity.GetActivityAchievementResponse;
import com.everhomes.rest.activity.GetActivityDetailByIdCommand;
import com.everhomes.rest.activity.GetActivityDetailByIdResponse;
import com.everhomes.rest.activity.GetActivityGoodsCommand;
import com.everhomes.rest.activity.GetActivityShareDetailCommand;
import com.everhomes.rest.activity.GetActivityVideoInfoCommand;
import com.everhomes.rest.activity.GetActivityWarningCommand;
import com.everhomes.rest.activity.GetVideoCapabilityCommand;
import com.everhomes.rest.activity.ImportSignupInfoCommand;
import com.everhomes.rest.activity.ListActivitiesByNamespaceIdAndTagCommand;
import com.everhomes.rest.activity.ListActivitiesByTagCommand;
import com.everhomes.rest.activity.ListActivitiesCommand;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.activity.ListActivityAttachmentsCommand;
import com.everhomes.rest.activity.ListActivityAttachmentsResponse;
import com.everhomes.rest.activity.ListActivityCategories;
import com.everhomes.rest.activity.ListActivityCategoriesCommand;
import com.everhomes.rest.activity.ListActivityEntryCategoriesCommand;
import com.everhomes.rest.activity.ListActivityGoodsCommand;
import com.everhomes.rest.activity.ListActivityGoodsResponse;
import com.everhomes.rest.activity.ListNearByActivitiesCommand;
import com.everhomes.rest.activity.ListNearByActivitiesCommandV2;
import com.everhomes.rest.activity.ListNearbyActivitiesResponse;
import com.everhomes.rest.activity.ListOfficialActivityByNamespaceCommand;
import com.everhomes.rest.activity.ListOfficialActivityByNamespaceResponse;
import com.everhomes.rest.activity.ListSignupInfoCommand;
import com.everhomes.rest.activity.ListSignupInfoResponse;
import com.everhomes.rest.activity.ManualSignupCommand;
import com.everhomes.rest.activity.SetActivityAchievementCommand;
import com.everhomes.rest.activity.SetActivityVideoInfoCommand;
import com.everhomes.rest.activity.SetActivityWarningCommand;
import com.everhomes.rest.activity.SignupInfoDTO;
import com.everhomes.rest.activity.UpdateActivityGoodsCommand;
import com.everhomes.rest.activity.UpdateSignupInfoCommand;
import com.everhomes.rest.activity.VertifyPersonByPhoneCommand;
import com.everhomes.rest.activity.VideoCapabilityResponse;
import com.everhomes.rest.activity.YzbVideoDeviceChangeCommand;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.ui.activity.ListActivityCategoryCommand;
import com.everhomes.rest.ui.activity.ListActivityCategoryReponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;

@RestController
@RequestMapping("/activity")
public class ActivityController extends ControllerBase {

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private AclProvider aclProvider;
    
//    @RequestMapping("post")
//    @RestReturn(value=ActivityDTO.class)
//    public RestResponse signup(@Valid ActivityPostCommand cmd) {
//        ActivityDTO result = activityService.createPost(cmd);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        response.setResponseObject(result);
//        return response;
//    }
    /**
     * 报名
     * @return {@link ActivityDTO}
     */
    @RequestMapping("signup")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse signup(@Valid ActivitySignupCommand cmd) {
        ActivityDTO result = activityService.signup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 
     * <p>后台手动添加活动报名</p>
     * <b>URL: /activity/manualSignup</b>
     */
    @RequestMapping("manualSignup")
    @RestReturn(value=SignupInfoDTO.class)
    public RestResponse manualSignup(@Valid ManualSignupCommand cmd) {
    	SignupInfoDTO result = activityService.manualSignup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 
     * <p>修改活动报名</p>
     * <b>URL: /activity/updateSignupInfo</b>
     */
    @RequestMapping("updateSignupInfo")
    @RestReturn(value=SignupInfoDTO.class)
    public RestResponse updateSignupInfo(@Valid UpdateSignupInfoCommand cmd) {
    	SignupInfoDTO result = activityService.updateSignupInfo(cmd);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	response.setResponseObject(result);
    	return response;
    }
    
    /**
     * 
     * <p>导入活动报名</p>
     * <b>URL: /activity/importSignupInfo</b>
     */
    @RequestMapping("importSignupInfo")
    @RestReturn(value=String.class)
    public RestResponse importSignupInfo(@Valid ImportSignupInfoCommand cmd, @RequestParam("attachment") MultipartFile[] files) {
    	activityService.importSignupInfo(cmd, files);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
     * 
     * <p>列出活动报名信息</p>
     * <b>URL: /activity/listSignupInfo</b>
     */
    @RequestMapping("listSignupInfo")
    @RestReturn(value=ListSignupInfoResponse.class)
    public RestResponse listSignupInfo(@Valid ListSignupInfoCommand cmd) {
    	ListSignupInfoResponse result = activityService.listSignupInfo(cmd);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	response.setResponseObject(result);
    	return response;
    }
    
    /**
     * 
     * <p>导出活动报名信息</p>
     * <b>URL: /activity/exportSignupInfo</b>
     */
    @RequestMapping("exportSignupInfo")
    @RestReturn(value=String.class)
    public RestResponse exportSignupInfo(@Valid ExportSignupInfoCommand cmd, HttpServletResponse response) {
    	activityService.exportSignupInfo(cmd, response);
    	RestResponse restResponse = new RestResponse();
    	restResponse.setErrorCode(ErrorCodes.SUCCESS);
    	restResponse.setErrorDescription("OK");
    	return restResponse;
    }
    
    /**
     * 
     * <p>删除活动报名信息</p>
     * <b>URL: /activity/deleteSignupInfo</b>
     */
    @RequestMapping("deleteSignupInfo")
    @RestReturn(value=String.class)
    public RestResponse deleteSignupInfo(@Valid DeleteSignupInfoCommand cmd) {
    	activityService.deleteSignupInfo(cmd);
    	RestResponse restResponse = new RestResponse();
    	restResponse.setErrorCode(ErrorCodes.SUCCESS);
    	restResponse.setErrorDescription("OK");
    	return restResponse;
    }
    
    /**
     * 
     * <p>检查手机号</p>
     * <b>URL: /activity/vertifyPersonByPhone</b>
     */
    @RequestMapping("vertifyPersonByPhone")
    @RestReturn(value=SignupInfoDTO.class)
    public RestResponse vertifyPersonByPhone(@Valid VertifyPersonByPhoneCommand cmd) {
    	SignupInfoDTO signupInfoDTO = activityService.vertifyPersonByPhone(cmd);
    	RestResponse restResponse = new RestResponse();
    	restResponse.setErrorCode(ErrorCodes.SUCCESS);
    	restResponse.setErrorDescription("OK");
    	restResponse.setResponseObject(signupInfoDTO);
    	return restResponse;
    }
    
    /**
     * 
     * @return {@link }
     */
    @RequestMapping("list")
    @RestReturn(value=ListActivitiesReponse.class)
   public RestResponse list(@Valid ListActivitiesCommand cmd){
        Tuple<Long, List<ActivityDTO>> tuple = activityService.listActivities(cmd);
        ListActivitiesReponse rsp=new ListActivitiesReponse(tuple.first(),tuple.second());
       return new RestResponse(rsp);
   }
    
    /**
     * 取消报名
     * @return {@link ActivityDTO}
     */
    @RequestMapping("cancelSignup")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse cacnelSignup(@Valid ActivityCancelSignupCommand cmd) {
        ActivityDTO result = activityService.cancelSignup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 签到
     * @return {@link ActivityDTO}
     */
    @RequestMapping("checkin")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse checkin(@Valid ActivityCheckinCommand cmd) {
        ActivityDTO result = activityService.checkin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 查询活动详情
     * @return {@link ActivityListResponse}
     */
    @RequestMapping("findActivityDetails")
    @RestReturn(value=ActivityListResponse.class)
    public RestResponse list(@Valid ActivityListCommand cmd) {
        ActivityListResponse activities = activityService.findActivityDetails(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(activities);
        return response;
    }
    
    /**
     * 确认
     * @return {@link ActivityDTO}
     */
    @RequestMapping("confirm")
    @RestReturn(ActivityDTO.class)
    public RestResponse confirm(@Valid ActivityConfirmCommand cmd){
        ActivityDTO confirm = activityService.confirm(cmd);
        return new RestResponse(confirm);
    }
    
    /**
     * 拒绝
     * @return {@link String}
     */
    @RequestMapping("reject")
    @RestReturn(String.class)
    public RestResponse reject(@Valid ActivityRejectCommand cmd){
        activityService.rejectPost(cmd);
        return new RestResponse("OK");
    }
    
    /**
     * 查询活动category
     * @return {@link ListActivityCategories}
     */
    @RequestMapping("listActivityCategories")
    @RestReturn(ListActivityCategories.class)
    public RestResponse listActivityCategories(ListActivityCategoriesCommand cmd){
        List<Category> result = activityService.listActivityCategories(cmd);
        if(CollectionUtils.isEmpty(result))
            return new RestResponse(new ListActivityCategories());
        ListActivityCategories categories=new ListActivityCategories();
        categories.setActivityCategories(result.stream().map(r->ConvertHelper.convert(r, CategoryDTO.class)).collect(Collectors.toList()));
        return new RestResponse(categories);
    }
    
    /**
     * 查询周边活动
     * @return
     */
    @RequestMapping("listNearbyActivities")
    @RestReturn(ListNearbyActivitiesResponse.class)
    public RestResponse listNearbyActivities(@Valid ListNearByActivitiesCommand cmd){
        Tuple<Long, List<ActivityDTO>> ret = activityService.listNearByActivities(cmd);
        ListNearbyActivitiesResponse rsp=new ListNearbyActivitiesResponse();
        rsp.setActivities(ret.second());
        rsp.setNextPageAnchor(ret.first());
        return new RestResponse(rsp);
    }
    
    /**
     * 查询周边活动2.0
     * @return
     */
    @RequestMapping("listNearbyActivitiesV2")
    @RestReturn(ListNearbyActivitiesResponse.class)
    public RestResponse listNearbyActivitiesV2(@Valid ListNearByActivitiesCommandV2 cmdV2){
    	
        Tuple<Long, List<ActivityDTO>> ret = activityService.listNearByActivitiesV2(cmdV2);
        ListNearbyActivitiesResponse rsp=new ListNearbyActivitiesResponse();
        rsp.setActivities(ret.second());
        rsp.setNextPageAnchor(ret.first());
        return new RestResponse(rsp);
    }
    
    /**
     * 查询同城活动
     * @return
     */
    @RequestMapping("listCityActivities")
    @RestReturn(ListNearbyActivitiesResponse.class)
    public RestResponse listCityActivities(@Valid ListNearByActivitiesCommandV2 cmdV2){
    	
        Tuple<Long, List<ActivityDTO>> ret = activityService.listCityActivities(cmdV2);
        ListNearbyActivitiesResponse rsp=new ListNearbyActivitiesResponse();
        rsp.setActivities(ret.second());
        rsp.setNextPageAnchor(ret.first());
        return new RestResponse(rsp);
    }
    
    /**
     * 查询周边和同城活动：周边活动range传入5，同城活动range传入4
     * @return {@link }
     */
    @RequestMapping("listActivitiesByTag")
    @RestReturn(value=ListActivitiesReponse.class)
   public RestResponse listActivitiesByTag(@Valid ListActivitiesByTagCommand cmd){
        //Tuple<Long, List<ActivityDTO>> tuple = activityService.listActivitiesByTag(cmd);
        //ListActivitiesReponse rsp=new ListActivitiesReponse(tuple.first(),tuple.second());
        ListActivitiesReponse rsp = activityService.listActivitiesByTag(cmd);
        RestResponse response = new RestResponse(rsp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }
    
    /**
     * 查询活动，根据namespaseId，tag
     * @return {@link }
     */
    @RequestMapping("listActivitiesByNamespaceIdAndTag")
    @RestReturn(value=ListActivitiesReponse.class)
   public RestResponse listActivitiesByNamespaceIdAndTag(@Valid ListActivitiesByNamespaceIdAndTagCommand cmd){
        ListActivitiesReponse cmdResponse = activityService.listActivitiesByNamespaceIdAndTag(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }
    
    /**
     * 查询分享出去的活动信息
     */
    @RequireAuthentication(false)
    @RequestMapping("getActivityShareDetail")
    @RestReturn(value=ActivityShareDetailResponse.class)
    public RestResponse getActivityShareDetail(@Valid GetActivityShareDetailCommand cmd) {
    	if(cmd == null || StringUtils.isEmpty(cmd.getPostToken())) {
    		return new RestResponse();
    	}
    	WebTokenGenerator webToken = WebTokenGenerator.getInstance();
 	    ActivityTokenDTO postToken = webToken.fromWebToken(cmd.getPostToken(), ActivityTokenDTO.class);
        ActivityShareDetailResponse activity = activityService.getActivityShareDetail(postToken);
        RestResponse response = new RestResponse(activity);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /activity/getActivityVideoInfo</b>
     * <p>获取直播信息详情</p>
     */
    @RequestMapping("getActivityVideoInfo")
    @RestReturn(value=ActivityVideoDTO.class)
    public RestResponse getActivityVideoInfo(@Valid GetActivityVideoInfoCommand cmd) {
        RestResponse response = new RestResponse(activityService.getActivityVideo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /activity/setActivityVideoInfo</b>
     * <p>更新直播信息</p>
     */
    @RequestMapping("setActivityVideoInfo")
    @RestReturn(value=ActivityVideoDTO.class)
    public RestResponse setActivityVideoInfo(@Valid SetActivityVideoInfoCommand cmd) {
        RestResponse response = new RestResponse(activityService.setActivityVideo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/videoCallback</b>
     * <p>直播回调</p>
     */
    @RequestMapping("videoCallback")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse videoCallback() {
        activityService.videoCallback();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /activity/devicechange</b>
     * <p>更新直播信息</p>
     */
    @RequestMapping("devicechange")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse videoDeviceChange(@Valid YzbVideoDeviceChangeCommand cmd) {
        activityService.onVideoDeviceChange(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /activity/getVideoCapability</b>
     * <p>获取直播的能力</p>
     */
    @RequestMapping("getVideoCapability")
    @RestReturn(value=VideoCapabilityResponse.class)
    public RestResponse getVideoCapability(@Valid GetVideoCapabilityCommand cmd) {
        RestResponse response = new RestResponse(activityService.getVideoCapability(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /*
     * 
     * <p>按namespace查询官方活动</p>
     * <b>URL: /activity/listOfficialActivityByNamespace</b>
     */
    @RequestMapping("listOfficialActivityByNamespace")
    @RestReturn(value=ListOfficialActivityByNamespaceResponse.class)
    public RestResponse listOfficialActivityByNamespace(ListOfficialActivityByNamespaceCommand cmd) {
    	return new RestResponse(activityService.listOfficialActivityByNamespace(cmd));
    }

    /**
     * <b>URL: /activity/getActivityDetailById</b>
     * <p>查询活动详情里面的内容</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getActivityDetailById")
    @RestReturn(value=GetActivityDetailByIdResponse.class)
    public RestResponse getActivityDetailById(GetActivityDetailByIdCommand cmd){
    	return new RestResponse(activityService.getActivityDetailById(cmd));
    }

    /**
     * <b>URL: /activity/setActivityWarning</b>
     * <p>设置活动提醒</p>
     */
    @RequestMapping("setActivityWarning")
    @RestReturn(value=ActivityWarningResponse.class)
    public RestResponse setActivityWarning(SetActivityWarningCommand cmd){
    	return new RestResponse(activityService.setActivityWarning(cmd));
    }
    
    /**
     * <b>URL: /activity/getActivityWarning</b>
     * <p>查询活动提醒</p>
     */
    @RequestMapping("getActivityWarning")
    @RestReturn(value=ActivityWarningResponse.class)
    public RestResponse getActivityWarning(GetActivityWarningCommand cmd){
    	return new RestResponse(activityService.queryActivityWarning(cmd));
    }
    
    /**
	 * <b>URL: /activity/listActivityEntryCategories</b>
	 * <p> 列出活动类型 </p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("listActivityEntryCategories")
	@RestReturn(value = ActivityCategoryDTO.class, collection = true)
	public RestResponse listActivityEntryCategories(ListActivityEntryCategoriesCommand cmd) {
		return new RestResponse(activityService.listActivityEntryCategories(cmd));
	}

    /**
     * <b>URL: /activity/setActivityAchievement</b>
     * <p> 设置活动成果 </p>
     */
    @RequestMapping("setActivityAchievement")
    @RestReturn(value = String.class)
    public RestResponse setActivityAchievement(SetActivityAchievementCommand cmd) {
        activityService.setActivityAchievement(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/getActivityAchievement</b>
     * <p> 查询活动成果 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getActivityAchievement")
    @RestReturn(value = GetActivityAchievementResponse.class)
    public RestResponse getActivityAchievement(GetActivityAchievementCommand cmd) {
        GetActivityAchievementResponse achievement = activityService.getActivityAchievement(cmd);

        RestResponse response = new RestResponse(achievement);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/createActivityAttachment</b>
     * <p> 添加活动附件 </p>
     */
    @RequestMapping("createActivityAttachment")
    @RestReturn(value = String.class)
    public RestResponse createActivityAttachment(CreateActivityAttachmentCommand cmd) {
        activityService.createActivityAttachment(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/deleteActivityAttachment</b>
     * <p> 删除活动附件 </p>
     */
    @RequestMapping("deleteActivityAttachment")
    @RestReturn(value = String.class)
    public RestResponse deleteActivityAttachment(DeleteActivityAttachmentCommand cmd) {
        activityService.deleteActivityAttachment(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/listActivityAttachments</b>
     * <p> 查询活动附件列表 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("listActivityAttachments")
    @RestReturn(value = ListActivityAttachmentsResponse.class)
    public RestResponse listActivityAttachments(ListActivityAttachmentsCommand cmd) {
        ListActivityAttachmentsResponse achievement = activityService.listActivityAttachments(cmd);

        RestResponse response = new RestResponse(achievement);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/downloadActivityAttachment</b>
     * <p> 下载活动附件 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("downloadActivityAttachment")
    @RestReturn(value = String.class)
    public RestResponse downloadActivityAttachment(DownloadActivityAttachmentCommand cmd) {
        activityService.downloadActivityAttachment(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/createActivityGoods</b>
     * <p> 添加活动物资 </p>
     */
    @RequestMapping("createActivityGoods")
    @RestReturn(value = String.class)
    public RestResponse createActivityGoods(CreateActivityGoodsCommand cmd) {
        activityService.createActivityGoods(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/updateActivityGoods</b>
     * <p> 修改活动物资 </p>
     */
    @RequestMapping("updateActivityGoods")
    @RestReturn(value = String.class)
    public RestResponse updateActivityGoods(UpdateActivityGoodsCommand cmd) {
        activityService.updateActivityGoods(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/deleteActivityGoods</b>
     * <p> 删除活动物资 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("deleteActivityGoods")
    @RestReturn(value = String.class)
    public RestResponse deleteActivityGoods(DeleteActivityGoodsCommand cmd) {
        activityService.deleteActivityGoods(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/listActivityGoods</b>
     * <p> 查询活动物资列表 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("listActivityGoods")
    @RestReturn(value = ListActivityGoodsResponse.class)
    public RestResponse listActivityGoods(ListActivityGoodsCommand cmd) {
        ListActivityGoodsResponse goods = activityService.listActivityGoods(cmd);

        RestResponse response = new RestResponse(goods);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/getActivityGoods</b>
     * <p> 查询活动物资 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getActivityGoods")
    @RestReturn(value = ActivityGoodsDTO.class)
    public RestResponse getActivityGoods(GetActivityGoodsCommand cmd) {
        ActivityGoodsDTO goods = activityService.getActivityGoods(cmd);

        RestResponse response = new RestResponse(goods);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /activity/listActivityCategory</b>
     * <p>列出活动分类</p>
     */
    @RequestMapping("listActivityCategory")
    @RestReturn(value=ListActivityCategoryReponse.class)
    @RequireAuthentication(false)
    public RestResponse listActivityCategory(ListActivityCategoryCommand cmd){
        RestResponse response = new RestResponse(activityService.listActivityCategory(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }
}
