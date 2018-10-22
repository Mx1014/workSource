// @formatter:off
package com.everhomes.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.ListGeneralFormsCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.order.CreateWechatJsPayOrderResp;
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
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.order.CommonOrderDTO;
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
     * <b>URL: /activity/signup</b>
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
     * <b>URL: /activity/createSignupOrder</b>
     * <p>创建活动报名收费订单</p>
     */
    @RequestMapping("createSignupOrder")
    @RestReturn(value=CommonOrderDTO.class)
    public RestResponse createSignupOrder(@Valid CreateSignupOrderCommand cmd) {
    	CommonOrderDTO dto = activityService.createSignupOrder(cmd);
    	RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/createWechatJsSignupOrder</b>
     * <p>创建微信公众号活动报名收费订单</p>
     */
    @RequestMapping("createWechatJsSignupOrder")
    @RestReturn(value=CreateWechatJsPayOrderResp.class)
    public RestResponse createWechatJsSignupOrder(@Valid CreateWechatJsSignupOrderCommand cmd) {
        CreateWechatJsPayOrderResp dto = activityService.createWechatJsSignupOrder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/createSignupOrderV2</b>
     * <p>支付2.0</p>
     */
    @RequestMapping("createSignupOrderV2")
    @RestReturn(value=PreOrderDTO.class)
    public RestResponse createSignupOrderV2(@Valid CreateSignupOrderV2Command cmd) {
        PreOrderDTO dto = activityService.createUniteSignupOrder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
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
    @RestReturn(value=ImportSignupInfoResponse.class)
    public RestResponse importSignupInfo(@Valid ImportSignupInfoCommand cmd, @RequestParam("attachment") MultipartFile[] files) {
        ImportSignupInfoResponse importSignupInfoResponse = activityService.importSignupInfo(cmd, files);
    	RestResponse response = new RestResponse(importSignupInfoResponse);
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
    public void exportSignupInfo(@Valid ExportSignupInfoCommand cmd) {
    	activityService.exportActivitySignupNew(cmd);
    }

//    /**
//     *
//     * <p>导出活动错误信息</p>
//     * <b>URL: /activity/exportErrorInfo</b>
//     */
//    @RequestMapping("exportErrorInfo")
//    @RestReturn(value=String.class)
//    public RestResponse exportErrorInfo(@Valid ExportErrorInfoCommand cmd, HttpServletResponse response) {
//        activityService.exportErrorInfo(cmd, response);
//        RestResponse restResponse = new RestResponse();
//        restResponse.setErrorCode(ErrorCodes.SUCCESS);
//        restResponse.setErrorDescription("OK");
//        return restResponse;
//    }

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
    @RequireAuthentication(false)
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
    public RestResponse videoCallback(@Valid VideoCallbackCommand cmd) {
        activityService.videoCallback(cmd);
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
     * <b>URL: /activity/setActivityPayee</b>
     * <p>设置活动收款方</p>
     */
    @RequestMapping("setActivityPayee")
    @RestReturn(value=String .class)
    public RestResponse setActivityPayee(CreateOrUpdateActivityPayeeCommand cmd){
        activityService.createOrUpdateActivityPayee(cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: /activity/getActivityPayee</b>
     * <p>获取已设置的活动收款方</p>
     */
    @RequestMapping("getActivityPayee")
    @RestReturn(value=GetActivityPayeeDTO.class)
    public RestResponse getActivityPayee(GetActivityPayeeCommand cmd){
        return new RestResponse(activityService.getActivityPayee(cmd));
    }

    /**
     * <b>URL: /activity/listActivityPayee</b>
     * <p>获取活动收款方列表</p>
     */
    @RequestMapping("listActivityPayee")
    @RestReturn(value=ActivityPayeeDTO.class,collection = true)
    public RestResponse listActivityPayee(ListActivityPayeeCommand cmd){
        List<ActivityPayeeDTO> list = activityService.listActivityPayee(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/checkPayeeIsUseful</b>
     * <p>校验收款方是否可用</p>
     */
    @RequestMapping("checkPayeeIsUseful")
    @RestReturn(value=CheckPayeeIsUsefulResponse.class)
    public RestResponse checkPayeeIsUseful(CheckPayeeIsUsefulCommand cmd){
        return new RestResponse(activityService.checkPayeeIsUseful(cmd));
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
     * <b>URL: /activity/setRosterOrderSetting</b>
     * <p>设置订单支付有效期</p>
     */
    @RequestMapping("setRosterOrderSetting")
    @RestReturn(value=RosterOrderSettingDTO.class)
    public RestResponse setRosterOrderSetting(SetRosterOrderSettingCommand cmd){
    	return new RestResponse(activityService.setRosterOrderSetting(cmd));
    }

    /**
     * <b>URL: /activity/getRosterOrderSetting</b>
     * <p>查询订单支付有效期</p>
     */
    @RequestMapping("getRosterOrderSetting")
    @RestReturn(value=RosterOrderSettingDTO.class)
    public RestResponse getRosterOrderSetting(GetRosterOrderSettingCommand cmd){
    	return new RestResponse(activityService.getRosterOrderSetting(cmd));
    }

    /**
     * <b>URL: /activity/setActivityTime</b>
     * <p>设置活动提醒、订单有效期</p>
     */
    @RequestMapping("setActivityTime")
    @RestReturn(value=ActivityTimeResponse.class)
    public RestResponse setActivityTime(SetActivityTimeCommand cmd){
    	return new RestResponse(activityService.setActivityTime(cmd));
    }

    /**
     * <b>URL: /activity/getActivityTime</b>
     * <p>查询活动提醒、订单有效期</p>
     */
    @RequestMapping("getActivityTime")
    @RestReturn(value=ActivityTimeResponse.class)
    public RestResponse getActivityTime(GetActivityTimeCommand cmd){
    	return new RestResponse(activityService.getActivityTime(cmd));
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

    /**
     * <b>URL: /activity/statisticsSummary</b>
     * <p>统计总览</p>
     */
    @RequestMapping("statisticsSummary")
    @RestReturn(value=StatisticsSummaryResponse.class)
    public RestResponse statisticsSummary(StatisticsSummaryCommand cmd){
    	StatisticsSummaryResponse result = activityService.statisticsSummary(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }

    /**
     * <b>URL: /activity/statisticsActivity</b>
     * <p>统计活动</p>
     */
    @RequestMapping("statisticsActivity")
    @RestReturn(value=StatisticsActivityResponse.class)
    public RestResponse statisticsActivity(StatisticsActivityCommand cmd){
    	StatisticsActivityResponse result = activityService.statisticsActivity(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }

    /**
     * <b>URL: /activity/statisticsOrganization</b>
     * <p>统计企业</p>
     */
    @RequestMapping("statisticsOrganization")
    @RestReturn(value=StatisticsOrganizationResponse.class)
    public RestResponse statisticsOrganization(StatisticsOrganizationCommand cmd){
    	StatisticsOrganizationResponse result = activityService.statisticsOrganization(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }

    /**
     * <b>URL: /activity/statisticsTag</b>
     * <p>统计标签</p>
     */
    @RequestMapping("statisticsTag")
    @RestReturn(value=StatisticsTagResponse.class)
    public RestResponse statisticsTag(StatisticsTagCommand cmd){
    	StatisticsTagResponse result = activityService.statisticsTag(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }

    /**
     * <b>URL: /activity/listActivitiesByCategoryId</b>
     * <p>通过categoryId查询整个域空间的活动帖子，不区分园区、可见性等</p>
     */
    @RequestMapping("listActivitiesByCategoryId")
    @RestReturn(value=ListActivitiesByCategoryIdResponse.class)
    public RestResponse listActivitiesByCategoryId(ListActivitiesByCategoryIdCommand cmd){
        ListActivitiesByCategoryIdResponse result = activityService.listActivitiesByCategoryId(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/payNotify</b>
     * <p>支付模块回调接口，通知支付结果</p>
     */
    @RequestMapping("payNotify")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse payNotify(@Valid OrderPaymentNotificationCommand cmd) {
        activityService.payNotify(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/exportActivity</b>
     * <p>导出活动数据</p>
     */
    @RequestMapping("exportActivity")
    public void exportActivity(ExportActivityCommand cmd) {
        this.activityService.exportActivity(cmd);
    }

    /**
     * <b>URL: /activity/exportOrganization</b>
     * <p>导出活动企业分布数据</p>
     */
    @RequestMapping("exportOrganization")
    public void exportOrganization(ExportOrganizationCommand cmd) {
        this.activityService.exportOrganization(cmd);
    }

    /**
     * <b>URL: /activity/exportTag</b>
     * <p>导出活动标签分布数据</p>
     */
    @RequestMapping("exportTag")
    public void exportTag(ExportTagCommand cmd) {
        this.activityService.exportTag(cmd);
    }

    /**
     * <b>URL: /activity/exportActivitySignupTemplate</b>
     * <p>活动报名导入模板</p>
     */
    @RequestMapping("exportActivitySignupTemplate")
    @RestReturn(value = String.class)
    public RestResponse exportActivitySignupTemplate(ExportActivitySignupTemplateCommand cmd, HttpServletResponse httpResponse){
        activityService.exportActivitySignupTemplate(cmd,httpResponse);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /activity/listActivityForms</b>
     * <p> 获取活动报名表单列表 </p>
     * @return
     */
    @RequestMapping("listActivityForms")
    @RestReturn(value=ListGeneralFormResponse.class)
    public RestResponse listApprovalForms(@Valid ListGeneralFormsCommand cmd) {
        ListGeneralFormResponse result = activityService.listActivitySignupGeneralForms(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /activity/updateActivityForm</b>
     * <p> 修改 活动的表单    </p>
     * @return
     */
    @RequestMapping("updateActivityForm")
    @RestReturn(value=GeneralFormDTO.class)
    public RestResponse updateActivityForm(@Valid UpdateActivityFormCommand cmd) {
        GeneralFormDTO generalFormDTO = activityService.updateGeneralForm(cmd);
        RestResponse response = new RestResponse(generalFormDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }
}
