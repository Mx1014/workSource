// @formatter:off
package com.everhomes.activity;

import com.everhomes.acl.AclProvider;
import com.everhomes.category.Category;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
	 * <b>URL: /yellowPage/listActivityEntryCategories</b> 
	 * <p> 列出活动类型 </p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("listActivityEntryCategories")
	@RestReturn(value = ActivityCategoryDTO.class, collection = true)
	public RestResponse listActivityEntryCategories(ListActivityEntryCategoriesCommand cmd) {
		return new RestResponse(activityService.listActivityEntryCategories(cmd));
	}
}
