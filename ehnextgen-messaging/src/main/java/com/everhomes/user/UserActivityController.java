package com.everhomes.user;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.locale.LocalAppProvier;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.local.AppVersionCommand;
import com.everhomes.rest.local.GetAppVersion;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.user.AddUserFavoriteCommand;
import com.everhomes.rest.user.CancelUserFavoriteCommand;
import com.everhomes.rest.user.CommunityStatusResponse;
import com.everhomes.rest.user.Contact;
import com.everhomes.rest.user.ContactDTO;
import com.everhomes.rest.user.CreateInvitationCommand;
import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.InvitationCommandResponse;
import com.everhomes.rest.user.ListContactRespose;
import com.everhomes.rest.user.ListContactsCommand;
import com.everhomes.rest.user.ListPostResponse;
import com.everhomes.rest.user.ListPostedActivityByOwnerIdCommand;
import com.everhomes.rest.user.ListPostedTopicByOwnerIdCommand;
import com.everhomes.rest.user.ListRecipientCommand;
import com.everhomes.rest.user.ListSignupActivitiesCommand;
import com.everhomes.rest.user.ListTreasureResponse;
import com.everhomes.rest.user.ListUserFavoriteActivityCommand;
import com.everhomes.rest.user.ListUserFavoriteTopicCommand;
import com.everhomes.rest.user.SyncActivityCommand;
import com.everhomes.rest.user.SyncBehaviorCommand;
import com.everhomes.rest.user.SyncInsAppsCommand;
import com.everhomes.rest.user.SyncLocationCommand;
import com.everhomes.rest.user.SyncUserContactCommand;
import com.everhomes.rest.user.UserInvitationsDTO;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

/**
 * controller
 * 
 * @author elians
 *
 */
@RestController
@RestDoc(value = "User controller", site = "messaging")
@RequestMapping("/user")
public class UserActivityController extends ControllerBase {
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private LocalAppProvier localAppProvier;

    public UserActivityController() {
    }

    /**
     * 查询当前小区入住状态 <b>url:/user/listUsersInCurrentCommunity</b>
     * 
     * @return {@link CommunityStatusResponse}
     */
    @RequestMapping(value = "listUsersInCurrentCommunity")
    @RestReturn(CommunityStatusResponse.class)
    public RestResponse listUsersInCurrentCommunity() throws Exception {
        CommunityStatusResponse result = userActivityService.listCurrentCommunityStatus();
        return new RestResponse(result);
    }

    /**
     * 同步用户地理位置 <b>url:/user/syncLocation</b>
     * 
     * @return OK
     */
    @RequestMapping(value = "syncLocation")
    @RestReturn(String.class)
    public RestResponse syncLocation(@Valid SyncLocationCommand cmd) throws Exception {
        userActivityService.updateLocation(cmd);
        return new RestResponse("OK");
    }

    /**
     * 同步用户行为 <b>url:/user/syncBehavior</b>
     * 
     * @return OK
     */
    @RequestMapping(value = "syncBehavior")
    @RestReturn(String.class)
    public RestResponse syncBehavior(@Valid SyncBehaviorCommand cmd) throws Exception {
        userActivityService.updateUserBehavoir(cmd);
        return new RestResponse("OK");
    }

    /**
     * 同步用户活动数据 <b>url:/user/syncActivity</b>
     * 
     * @return OK
     */
    @RequestMapping(value = "syncActivity")
    @RestReturn(String.class)
    public RestResponse syncActivity(@Valid SyncActivityCommand cmd) throws Exception {
    	final Logger STATISTICS_LOGGER = LoggerFactory.getLogger("statisticslog");
    	STATISTICS_LOGGER.debug(String.format("user activities：%s", StringHelper.toJsonString(cmd)));
        userActivityService.updateActivity(cmd);
        return new RestResponse("OK");
    }

    /**
     * 同步用户安装的app <b> url:/user/syncInstalledApps</b>
     * 
     * @return OK
     */
    @RequestMapping(value = "syncInstalledApps")
    @RestReturn(String.class)
    public RestResponse syncInstalledApps(@Valid SyncInsAppsCommand cmd) throws Exception {
        userActivityService.syncInstalledApps(cmd);
        return new RestResponse("OK");
    }

    /**
     * 查询通讯录中左邻用户 <b>url:/user/listContacts</b>
     * 
     * @return {@link Contact}
     */
    @RequestMapping(value = "listContacts")
    @RestReturn(value = ListContactRespose.class)
    public RestResponse listContacts(@Valid ListContactsCommand cmd) throws Exception {
        Tuple<Long, List<ContactDTO>> result = userActivityService.listUserContacts(cmd.getAnchor());
        ListContactRespose rsp = new ListContactRespose(result.second(), result.first());
        return new RestResponse(rsp);
    }

    /**
     * 同步用户通讯录 <b>url:/user/syncContacts</b>
     * 
     * @return {@link Contact}
     */
    @RequestMapping("syncContacts")
    @RestReturn(String.class)
    public RestResponse syncUserContacts(@Valid SyncUserContactCommand cmd) {
        userActivityService.updateContacts(cmd);
        return new RestResponse("OK");
    }

    /**
     * 生成邀请码 <b> url:/user/createInvitationCode</b>
     * 
     * @return {@link Contact}
     */
    @RequestMapping("createInvitationCode")
    @RestReturn(UserInvitationsDTO.class)
    public RestResponse createInvitationCode(@Valid CreateInvitationCommand cmd) {
        UserInvitationsDTO code = this.userService.createInvatation(cmd);
        return new RestResponse(code);
    }

    /**
     * 查看邀请信息 <b>url:/user/listRecipient</b>
     * 
     * @return {@link InvitationCommandResponse}
     */
    @RequestMapping("listRecipient")
    @RestReturn(InvitationCommandResponse.class)
    public RestResponse listInvitedUsers(@Valid ListRecipientCommand cmd) {
        return new RestResponse(userActivityService.listInvitedUsers(cmd.getAnchor()));
    }

    /**
     * 用户反馈 <b>url:/user/feedback</b>
     * 
     * @param cmd {@link FeedbackCommand}
     */
    @RequestMapping("feedback")
    @RequireAuthentication(false)
    @RestReturn(String.class)
    public RestResponse feedback(@Valid FeedbackCommand cmd) {
        userActivityService.updateFeedback(cmd);
        return new RestResponse("OK");
    }

    /**
     * 查询版本信息 <b>url:/user/appversion</b>
     * 
     * @param appversion
     */
    @RequestMapping("appversion")
    @RequireAuthentication(false)
    @RestReturn(GetAppVersion.class)
    public RestResponse getAppversion(@Valid AppVersionCommand appversion) {
        GetAppVersion ret = localAppProvier.findAppVersion(appversion);
        return new RestResponse(ret);
    }

    /**
     * 用户收藏 <b>url:/user/userFavorite</b>
     * 
     * @param cmd
     */
    @RequestMapping("userFavorite")
    @RestReturn(String.class)
    public RestResponse addUserFavorite(@Valid AddUserFavoriteCommand cmd) {
        userActivityService.addUserFavorite(cmd);
        return new RestResponse("OK");
    }
    
    @RequestMapping("cancelFavorite")
    @RestReturn(String.class)
    public RestResponse cancelFavorite(@Valid CancelUserFavoriteCommand cmd) {
        userActivityService.cancelFavorite(cmd);
        return new RestResponse("OK");
    }

    /**
     * 用户收藏的帖子：<b>url:/user/listTopicFavorite</b>
     * @return
     */
    @RequestMapping("listTopicFavorite")
    @RestReturn(value=ListPostResponse.class)
    public RestResponse listTopicFavorite(ListUserFavoriteTopicCommand cmd) {
    	ListPostResponse result = userActivityService.listFavoriteTopics(cmd);
        return new RestResponse(result);
    }

    /**
     * 用户分享的帖子：<b>url:/user/listPostedTopics</b>
     * @return
     */
    @RequestMapping("listPostedTopics")
    @RestReturn(value=ListPostResponse.class)
    public RestResponse listPostTopics(@Valid ListPostedTopicByOwnerIdCommand cmd) {
    	ListPostResponse result = userActivityService.listPostedTopics(cmd);
        return new RestResponse(result);
    }
    
    /**
     * 用户财富：<b>url:/user/listTreasure</b>
     */
    @RequestMapping("listTreasure")
    @RestReturn(ListTreasureResponse.class)
    public RestResponse listTreasure(){
        return new RestResponse(userActivityService.getUserTreasure());
    }
    
    
    /**
     * <b>URL: /user/getUserRelateServiceAddress</b>
     * <p>获取用户相关的服务地址</p>
     */
    @RequestMapping("getUserRelateServiceAddress")
    @RestReturn(value=UserServiceAddressDTO.class, collection=true)
    public RestResponse getUserRelateServiceAddress() {
        List<UserServiceAddressDTO> result = this.userActivityService.getUserRelateServiceAddress();
        
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 用户收藏的活动：<b>url:/user/listActivityFavorite</b>
     * @return
     */
    @RequestMapping("listActivityFavorite")
    @RestReturn(value=ListActivitiesReponse.class)
    public RestResponse listActivityFavorite(ListUserFavoriteActivityCommand cmd) {
    	ListActivitiesReponse response = userActivityService.listActivityFavorite(cmd);
        return new RestResponse(response);
    }

    /**
     * 用户发的活动：<b>url:/user/listPostedActivities</b>
     * @return
     */
    @RequestMapping("listPostedActivities")
    @RestReturn(value=ListActivitiesReponse.class)
    public RestResponse listPostedActivities(@Valid ListPostedActivityByOwnerIdCommand cmd) {
    	ListActivitiesReponse response = userActivityService.listPostedActivities(cmd);
        return new RestResponse(response);
    }
    
    /**
     * 用户报名的活动：<b>url:/user/listSignupActivities</b>
     * @return
     */
    @RequestMapping("listSignupActivities")
    @RestReturn(value=ListActivitiesReponse.class)
    public RestResponse listSignupActivities(@Valid ListSignupActivitiesCommand cmd) {
    	ListActivitiesReponse response = userActivityService.listSignupActivities(cmd);
        return new RestResponse(response);
    }
}
