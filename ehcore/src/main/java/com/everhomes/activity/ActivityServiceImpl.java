// @formatter:off
package com.everhomes.activity;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.Condition;
import org.jooq.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.hsr.geohash.GeoHash;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.group.GroupService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCheckinCommand;
import com.everhomes.rest.activity.ActivityConfirmCommand;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityListCommand;
import com.everhomes.rest.activity.ActivityListResponse;
import com.everhomes.rest.activity.ActivityLocalStringCode;
import com.everhomes.rest.activity.ActivityMemberDTO;
import com.everhomes.rest.activity.ActivityNotificationTemplateCode;
import com.everhomes.rest.activity.ActivityPostCommand;
import com.everhomes.rest.activity.ActivityRejectCommand;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.ActivityShareDetailResponse;
import com.everhomes.rest.activity.ActivitySignupCommand;
import com.everhomes.rest.activity.ActivityTokenDTO;
import com.everhomes.rest.activity.GeoLocation;
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
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.ListActivityTopicByCategoryAndTagCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostFavoriteFlag;
import com.everhomes.rest.group.LeaveGroupCommand;
import com.everhomes.rest.group.ListNearbyGroupCommand;
import com.everhomes.rest.group.ListNearbyGroupCommandResponse;
import com.everhomes.rest.group.RejectJoinGroupRequestCommand;
import com.everhomes.rest.group.RequestToJoinGroupCommand;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.ui.user.ActivityLocationScope;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.user.UserFavoriteDTO;
import com.everhomes.rest.user.UserFavoriteTargetType;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.StatusChecker;
import com.everhomes.util.Tuple;


@Component
public class ActivityServiceImpl implements ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);
    

//    private static final String SIGNUP_AUTO_COMMENT = "signup.auto.comment";
//
//    private static final String CHECKIN_AUTO_COMMENT = "checkin.auto.comment";
//    
//    private static final String CONFIRM_AUTO_COMMENT="confirm.auto.comment";
//    
//    private static final String CANCEL_AUTO_COMMENT="cancel.auto.comment";
//    
//    private static final String REJECT_AUTO_COMMENT="reject.auto.comment";
    private static final String DEFAULT_HOME_URL = "default.server.url";
    @Autowired
    private ForumService forumService;

    @Autowired
    private ActivityProivider activityProvider;

    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private FamilyProvider familyProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private UserActivityProvider userActivityProvider;

    @Override
    public void createPost(ActivityPostCommand cmd, Long postId) {
        User user = UserContext.current().getUser();
        Activity activity = ConvertHelper.convert(cmd, Activity.class);
        activity.setId(cmd.getId());
        activity.setPostId(postId);
        activity.setNamespaceId(0);
        activity.setCreatorUid(user.getId());
        activity.setGroupDiscriminator(EntityType.ACTIVITY.getCode());
        Integer namespaceId = (cmd.getNamespaceId() == null) ? 0 : cmd.getNamespaceId();
        activity.setNamespaceId(namespaceId);
        activity.setGuest(cmd.getGuest());
        
        // avoid nullpoint
        activity.setCheckinAttendeeCount(0);
        //status:状态，0-无效、1-草稿、2-已发布
        activity.setStatus((byte)2);
        activity.setCheckinFamilyCount(0);
        activity.setConfirmAttendeeCount(0);
        activity.setConfirmFamilyCount(0);
        activity.setSignupAttendeeCount(0);
        activity.setSignupFamilyCount(0);
        activity.setSignupFlag(cmd.getCheckinFlag());
        if(cmd.getLongitude()!=null&&cmd.getLatitude()!=null){
            String geohash=GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            activity.setGeohash(geohash);
            }
        
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        //if date time format is not ok,return now
        Date convertStartTime = convert(cmd.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date convertEndTime = convert(cmd.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        long endTimeMs=  DateHelper.currentGMTTime().getTime();
        long startTimeMs = DateHelper.currentGMTTime().getTime();
        if(convertStartTime!=null){
            startTimeMs=convertStartTime.getTime();
        }
        if(convertEndTime!=null){
            endTimeMs=convertEndTime.getTime();
        }
        activity.setPosterUri(cmd.getPosterUri());
        activity.setStartTime(new Timestamp(startTimeMs));
        activity.setEndTime(new Timestamp(endTimeMs));
        activity.setStartTimeMs(startTimeMs);
        activity.setEndTimeMs(endTimeMs);
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        activityProvider.createActity(activity);
        
        ActivityRoster roster = new ActivityRoster();
        roster.setFamilyId(user.getAddressId());
        roster.setUid(user.getId());
        roster.setUuid(UUID.randomUUID().toString());
        roster.setActivityId(activity.getId());
        roster.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setConfirmFamilyId(user.getAddressId());
        roster.setAdultCount(0);
        roster.setChildCount(0);
        activityProvider.createActivityRoster(roster);
        
    }

    //活动报名
    @Override
    public ActivityDTO signup(ActivitySignupCommand cmd) {
        User user = UserContext.current().getUser();
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        ActivityRoster roster = createRoster(cmd, user, activity);
        dbProvider.execute((status) -> {
        	//去掉报名评论 by xiongying 20160615
//            Post comment = new Post();
//            comment.setParentPostId(post.getId());
//            comment.setForumId(post.getForumId());
//            comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            comment.setCreatorUid(user.getId());
//            comment.setContentType(PostContentType.TEXT.getCode());
////            String template = configurationProvider.getValue(SIGNUP_AUTO_COMMENT, "");
//            String template = localeStringService.getLocalizedString(
//            		ActivityLocalStringCode.SCOPE,
//                    String.valueOf(ActivityLocalStringCode.ACTIVITY_SIGNUP),
//                    UserContext.current().getUser().getLocale(),
//                    "");
//
//            if (!StringUtils.isEmpty(template)) {
//                comment.setContent(template);
//                forumProvider.createPost(comment);
//            }
            if (activity.getGroupId() != null) {
                RequestToJoinGroupCommand joinCmd = new RequestToJoinGroupCommand();
                joinCmd.setGroupId(activity.getGroupId());
                joinCmd.setRequestText("request to join activity group");
                try{
                    groupService.requestToJoinGroup(joinCmd);
                }catch(Exception e){
                    LOGGER.error("join to group failed",e);
                }
               
            }
            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()+cmd.getAdultCount()+cmd.getChildCount());
            if(user.getAddressId()!=null){
                activity.setSignupFamilyCount(activity.getSignupFamilyCount()+1);
            }
            activityProvider.createActivityRoster(roster);
            activityProvider.updateActivity(activity);
            return status;
        });
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        
        //Send message to creator
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", user.getNickName());
        map.put("postName", activity.getSubject());
        sendMessageCode(activity.getCreatorUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_TO_CREATOR);
        
        return dto;
    }
    
    private void sendMessageCode(Long uid, String locale, Map<String, String> map, int code) {
    	
        String scope = ActivityNotificationTemplateCode.SCOPE;
        
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(uid, notifyTextForOther, null);
    }
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    private ActivityRoster createRoster(ActivitySignupCommand cmd, User user, Activity activity) {
        ActivityRoster roster = ConvertHelper.convert(cmd, ActivityRoster.class);
        roster.setFamilyId(user.getAddressId());
        roster.setUid(user.getId());
        roster.setUuid(UUID.randomUUID().toString());
        roster.setActivityId(activity.getId());
        roster.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setConfirmFamilyId(user.getAddressId());
        return roster;
    }

    @Override
    public ActivityDTO cancelSignup(ActivityCancelSignupCommand cmd) {
        User user = UserContext.current().getUser();
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
         activityProvider.cancelSignup(activity, user.getId(), getFamilyId());
        if (activity.getGroupId() != null) {
            LeaveGroupCommand leaveCmd = new LeaveGroupCommand();
            leaveCmd.setGroupId(activity.getGroupId());
            //remove from group or not
           // groupService.leaveGroup(leaveCmd);
        }
//        Post p = createPost(user.getId(), post, null, "");
////        p.setContent(configurationProvider.getValue(CANCEL_AUTO_COMMENT, ""));
//        p.setContent(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
//                    String.valueOf(ActivityLocalStringCode.ACTIVITY_CANCEL), UserContext.current().getUser().getLocale(), ""));
//        forumProvider.createPost(p);
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setForumId(post.getForumId());
        dto.setUserActivityStatus(ActivityStatus.UN_SIGNUP.getCode());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        
        //Send message to creator
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", user.getNickName());
        map.put("postName", activity.getSubject());
        sendMessageCode(activity.getCreatorUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_CANCEL_TO_CREATOR);
        
        return dto;
    }

    @Override
    public ActivityDTO checkin(ActivityCheckinCommand cmd) {
        User user = UserContext.current().getUser();
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        
        ActivityRoster acroster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
        
        dbProvider.execute(status->{
        	if(activity.getConfirmFlag() == null || activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode() 
        			|| (activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode() && acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue())){
        		
        		ActivityRoster roster = activityProvider.checkIn(activity, user.getId(), getFamilyId());
//                Post p = createPost(user.getId(),post,null,"");
////                p.setContent(configurationProvider.getValue(CHECKIN_AUTO_COMMENT, ""));
//                p.setContent(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
//                    String.valueOf(ActivityLocalStringCode.ACTIVITY_CHECKIN), UserContext.current().getUser().getLocale(), ""));
                Long familyId = getFamilyId();
                if (familyId != null)
                    activity.setSignupFamilyCount(activity.getSignupFamilyCount() + 1);
                activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()
                        + (roster.getAdultCount() + roster.getChildCount()));
                roster.setCheckinFlag((byte)1);
//                forumProvider.createPost(p);
                LOGGER.debug("roster={}", roster);
        	}
            
            return status;
        });
        
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setProcessStatus(getStatus(activity).getCode());
//        //是否需要确认，要确认则确认后才能签到
//        if(activity.getConfirmFlag()==null) {
//        	dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
//        }
//        else {
//        	if(activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode())
//        		dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
//        	if(activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode())
//        		//tobecontinue
//        		;
//        }
        
        if(activity.getConfirmFlag() == null || activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode() 
    			|| (activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode() && acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue())){
        	dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
        
        }
        
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setGroupId(activity.getGroupId());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        return dto;
    }

    private ProcessStatus getStatus(Activity activity) {
        return StatusChecker.getProcessStatus(activity.getStartTimeMs(), activity.getEndTimeMs());
    }

    @Override
    public ActivityListResponse findActivityDetails(ActivityListCommand cmd) {
        User user = UserContext.current().getUser();
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getAnchor()==null?0L:cmd.getAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }
        List<ActivityRoster> rosterList = activityProvider.listRosterPagination(locator, cmd.getPageSize(),
                activity.getId());
        ActivityRoster userRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
        LOGGER.info("find roster {}",userRoster);
        ActivityListResponse response = new ActivityListResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setForumId(post.getForumId());
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setActivityId(activity.getId());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        /////////////////////////////////////
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        ////////////////////////////////////
        response.setActivity(dto);
        List<ActivityMemberDTO> result = rosterList.stream().map(r -> {
            ActivityMemberDTO d = ConvertHelper.convert(r, ActivityMemberDTO.class);
            d.setConfirmFlag(convertToInt(r.getConfirmFlag()));
            d.setCreatorFlag(0);
            if (r.getUid().longValue() == post.getCreatorUid().longValue())
                d.setCreatorFlag(1);
            d.setLotteryWinnerFlag(convertToInt(r.getLotteryFlag()));
            d.setCheckinFlag(r.getCheckinFlag()==null?0:r.getCheckinFlag().intValue());
            d.setConfirmTime(r.getConfirmTime()==null?null:r.getConfirmTime().toString());
            if (r.getFamilyId() != null) {
                FamilyDTO family = familyProvider.getFamilyById(r.getFamilyId());
                if (family != null) {
                    d.setFamilyName(family.getName());
                    d.setFamilyId(r.getFamilyId());
                }
            }
            
            User currentUser = userProvider.findUserById(r.getUid());
            d.setId(r.getId());
            if (currentUser != null) {
                d.setUserAvatar(contentServerService.parserUri(currentUser.getAvatar(), EntityType.ACTIVITY.getCode(), activity.getId()));
                d.setUserName(currentUser.getNickName());
                
                List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(currentUser.getId());
                
                List<String> phones = identifiers.stream().filter((a)-> { return IdentifierType.fromCode(a.getIdentifierType()) == IdentifierType.MOBILE; })
                        .map((a) -> { return a.getIdentifierToken(); })
                        .collect(Collectors.toList());
                d.setPhone(phones);
            }

            
            return d;
        }).collect(Collectors.toList());
        if(rosterList.size()<cmd.getPageSize()){
            response.setNextAnchor(null);
        }else{
            response.setNextAnchor(locator.getAnchor());
        }
        response.setRoster(result);
        response.setCreatorFlag(0);
        // current user is sender?
        if (user.getId().equals(activity.getCreatorUid())) {
            // return url
            String baseDir = configurationProvider.getValue(DEFAULT_HOME_URL, "");
            response.setCheckinQRUrl(baseDir + "/activity/checkin?activityId=" + activity.getId());
            response.setCreatorFlag(1);
        }
        return response;
    }

    private Integer convertToInt(Long val) {
        if (val == null) {
            return null;
        }
        return val.intValue();
    }

    private Integer convertToInt(Byte code) {
        if (code == null)
            return null;
        return code.intValue();
    }

    private ActivityStatus getActivityStatus(ActivityRoster userRoster) {
        LOGGER.info("check roster the current roster is {}",userRoster);
        if(userRoster==null){
            return ActivityStatus.UN_SIGNUP;
        }
        if (CheckInStatus.CHECKIN.getCode().equals(userRoster.getCheckinFlag())) {
            return ActivityStatus.CHECKEINED;
        }
        if (userRoster.getConfirmFlag() != null&&userRoster.getConfirmFlag().longValue()!=0L) {
            return ActivityStatus.CONFIRMED;
        }
        return ActivityStatus.SIGNUP;

    }

    private Long getFamilyId() {
        User user = UserContext.current().getUser();
        if (user.getAddressId() != null) {
            Family family = familyProvider.findFamilyByAddressId(user.getAddressId());
            if (family == null) {
                return null;
            }
            return family.getId();
        }
        return null;
    }

    //活动确认
    @Override
    public ActivityDTO confirm(ActivityConfirmCommand cmd) {
        ActivityRoster item = activityProvider.findRosterById(cmd.getRosterId());
        if (item == null) {
            LOGGER.error("cannnot find roster record in database");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER,
                    "cannnot find roster record in database id=" + cmd.getRosterId());
        }
        Activity activity = activityProvider.findActivityById(item.getActivityId());
        if (activity == null) {
            LOGGER.error("cannnot find activity record in database");
            // TODO
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "cannnot find activity record in database id="
                            + cmd.getRosterId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        //validate post status
        if (post == null) {
            LOGGER.error("cannnot find post record in database");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID,
                    "cannnot find post record in database id=" + cmd.getRosterId());
        }
        
        User user = UserContext.current().getUser();
        if (post.getCreatorUid().longValue() != user.getId().longValue()) {
            LOGGER.error("the user is invalid.cannot confirm");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_USER,
                    "the user is invalid.cannot confirm id=" + cmd.getRosterId());
        }
        dbProvider.execute(status -> {
 //           forumProvider.createPost(createPost(user.getId(), post, cmd.getConfirmFamilyId(), cmd.getTargetName()));
            activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + item.getChildCount()
                    + item.getChildCount());
            activity.setConfirmFamilyCount(activity.getConfirmFamilyCount() + 1);
            activity.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
            activityProvider.updateActivity(activity);
            item.setConfirmUid(user.getId());
            item.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            item.setConfirmFamilyId(cmd.getConfirmFamilyId());
            item.setConfirmFlag(Long.valueOf(ConfirmStatus.CONFIRMED.getCode()));
            activityProvider.updateRoster(item);
            return status;
        });

        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setUserActivityStatus(getActivityStatus(item).getCode());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setGroupId(activity.getGroupId());
        
        //管理员同意活动的报名
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", user.getNickName());
        map.put("postName", activity.getSubject());
        sendMessageCode(item.getUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_CREATOR_CONFIRM_TO_USER);
        return dto;
    }

    private Post createPost(Long uid, Post p, Long familyId, String targetName) {
        // for checkin
        Post post = new Post();
        post.setParentPostId(p.getId());
        post.setForumId(p.getForumId());
//        String template = configurationProvider.getValue(CONFIRM_AUTO_COMMENT, "");
        String template = localeStringService.getLocalizedString(
        		ActivityLocalStringCode.SCOPE,
                String.valueOf(ActivityLocalStringCode.ACTIVITY_CONFIRM),
                UserContext.current().getUser().getLocale(),
                "");
        post.setContent(TemplatesConvert.convert(template, new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", targetName);
            }
        }, ""));
        post.setCreatorUid(uid);
        post.setContentType(PostContentType.TEXT.getCode());
        post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        post.setCreatorUid(UserContext.current().getUser().getId());
        return post;
    }

    @Override
    public ActivityDTO findSnapshotByPostId(Long postId) {
        Activity activity = activityProvider.findSnapshotByPostId(postId);
        if (activity == null) {
            LOGGER.error("cannot find activity for post.postId={}",postId);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "cannot find activity");
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        User user = UserContext.current().getUser();
        ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
        LOGGER.info("find roster {}",roster);
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setForumId(post.getForumId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setGroupId(activity.getGroupId());
        return dto;
    }

    @Override
    public void rejectPost(ActivityRejectCommand cmd) {
        User user = UserContext.current().getUser();
        ActivityRoster roster = activityProvider.findRosterById(cmd.getRosterId());
        if (roster == null) {
            LOGGER.error("cannot reject the roster");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER, "invalid roster id");
        }

        Activity activity = activityProvider.findActivityById(roster.getActivityId());
        if (activity == null) {
            LOGGER.error("invalid activity.id={}", roster.getActivityId());
            throw RuntimeErrorException
                    .errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                            "invalid activity id=" + roster.getActivityId());
        }

        Long postId = activity.getPostId();
        Post post = forumProvider.findPostById(postId);
      //validate post status
        if (post == null) {
            LOGGER.error("invalid post.id={}", postId);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id=" + postId);
        }
        if (user.getId().longValue() != post.getCreatorUid().longValue()) {
            LOGGER.error("No permission to reject the roster.rosterId={}", cmd.getRosterId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_USER, "invalid post id=" + postId);
        }

        int total = roster.getAdultCount() + roster.getChildCount();
        dbProvider.execute(status->{
            //need lock
            activityProvider.deleteRoster(roster);
            if (roster.getConfirmFlag() == Long.valueOf(ConfirmStatus.CONFIRMED.getCode())) {
                int result = activity.getCheckinAttendeeCount() - total;
                activity.setConfirmAttendeeCount(result < 0 ? 0 : result);
                if(roster.getConfirmFamilyId()!=null)
                    activity.setConfirmFamilyCount(activity.getConfirmFamilyCount()-1);
            }
            if (CheckInStatus.CHECKIN.getCode().equals(roster.getCheckinFlag())) {
                int result = activity.getCheckinAttendeeCount() - total;
                activity.setCheckinAttendeeCount(result < 0 ? 0 : result);
                if(roster.getConfirmFamilyId()!=null)
                    activity.setCheckinFamilyCount(activity.getCheckinFamilyCount()-1);
            }
            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()-total);
            if(roster.getConfirmFamilyId()!=null)
                activity.setSignupFamilyCount(activity.getSignupFamilyCount()-1);
            activityProvider.updateActivity(activity);
            return status;
        });
        User queryUser = userProvider.findUserById(roster.getUid());
        if (activity.getGroupId() != null) {
            RejectJoinGroupRequestCommand rejectCmd=new RejectJoinGroupRequestCommand();
            rejectCmd.setGroupId(activity.getGroupId());
            rejectCmd.setUserId(roster.getUid());
            rejectCmd.setRejectText(cmd.getReason());
            //reject to join group
            //groupService.rejectJoinGroupRequest(rejectCmd);
        }
        Post comment = createPost(user.getId(), post, null, "");
//        String template=configurationProvider.getValue(REJECT_AUTO_COMMENT, "");
        String template = localeStringService.getLocalizedString(
        		ActivityLocalStringCode.SCOPE,
                String.valueOf(ActivityLocalStringCode.ACTIVITY_REJECT),
                UserContext.current().getUser().getLocale(),
                "");
        comment.setContent(TemplatesConvert.convert(template, new HashMap<String, String>(){/**
             * 
             */
            private static final long serialVersionUID = 8928858603520552572L;

        {
            put("username",queryUser.getNickName()==null?queryUser.getAccountName():queryUser.getNickName());
            put("reason",cmd.getReason());
        }}, ""));
//        forumProvider.createPost(comment);
        
        
        
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(user.getId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), queryUser.getId().toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(user.getId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(comment.getContent());
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        
        UserLogin u = userService.listUserLogins(user.getId()).get(0);
        messagingService.routeMessage(u, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
        		queryUser.getId().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
        
    }

    @Override
    public ActivityListResponse findActivityDetailsByPostId(Post post) {
       User user=UserContext.current().getUser();
        Activity activity = activityProvider.findSnapshotByPostId(post.getId());
        if(activity==null){
            return null;
        }
        List<ActivityRoster> rosterList = activityProvider.listRosters(activity.getId());
        ActivityRoster userRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), UserContext
                .current().getUser().getId());
        ActivityListResponse response = new ActivityListResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        dto.setForumId(post.getForumId());
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        response.setActivity(dto);
        List<ActivityMemberDTO> result = rosterList.stream().map(r -> {
            ActivityMemberDTO d = ConvertHelper.convert(r, ActivityMemberDTO.class);
            d.setConfirmFlag(convertToInt(r.getConfirmFlag()));
            d.setCreatorFlag(0);
            if (r.getUid().longValue() == post.getCreatorUid().longValue())
                d.setCreatorFlag(1);
            d.setLotteryWinnerFlag(convertToInt(r.getLotteryFlag()));
            d.setCheckinFlag(r.getCheckinFlag()==null?0:r.getCheckinFlag().intValue());
            d.setConfirmTime(r.getConfirmTime()==null?null:r.getConfirmTime().toString());
            if (r.getFamilyId() != null) {
                FamilyDTO family = familyProvider.getFamilyById(r.getFamilyId());
                if (family != null) {
                    d.setFamilyName(family.getName());
                    d.setFamilyId(r.getFamilyId());
                }
                User currentUser = userProvider.findUserById(r.getUid());
                d.setId(r.getId());
                if (currentUser != null) {
                    d.setUserAvatar(contentServerService.parserUri(currentUser.getAvatar(), EntityType.ACTIVITY.getCode(), activity.getId()));
                    d.setUserName(currentUser.getAccountName());
                    
                    List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(currentUser.getId());
                    
                    List<String> phones = identifiers.stream().filter((a)-> { return IdentifierType.fromCode(a.getIdentifierType()) == IdentifierType.MOBILE; })
                            .map((a) -> { return a.getIdentifierToken(); })
                            .collect(Collectors.toList());
                    d.setPhone(phones);
                }

            }
            return d;
        }).collect(Collectors.toList());
        response.setRoster(result);
        response.setCreatorFlag(0);
        // current user is sender?
        if (user.getId().equals(activity.getCreatorUid())) {
            // return url
            String baseDir = configurationProvider.getValue(DEFAULT_HOME_URL, "");
            response.setCheckinQRUrl(baseDir + "/activity/checkin?activityId=" + activity.getId());
            response.setCreatorFlag(1);
        }
        return response;
    }
    
    private static Date convert(String time,String format){
        SimpleDateFormat f=new SimpleDateFormat(format);
        try {
            return f.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> listActivityCategories(ListActivityCategoriesCommand cmd) {
    	User user = UserContext.current().getUser();
    	Integer namespaceId = ( null == cmd.getNamespaceId() ) ? user.getNamespaceId() : cmd.getNamespaceId();
    	
    	namespaceId = ( namespaceId == null ) ? 0 : namespaceId;
    	
    	Long parentId = ( null == cmd.getParentId() ) ? CategoryConstants.CATEGORY_ID_ACTIVITY : cmd.getParentId();
    	
        Tuple[] orderBy = new Tuple[1]; 
        orderBy[0] = new Tuple<String, SortOrder>("default_order", SortOrder.ASC);
        List<Category> result = categoryProvider.listChildCategories(namespaceId, parentId, CategoryAdminStatus.ACTIVE,orderBy);
        
//        if(cmd != null && cmd.getCommunityFlagId() != null  && CommunityAppType.TECHPARK.getCode() == cmd.getCommunityFlagId()) {
//        	List<Category> tech = categoryProvider.listChildCategories(namespaceId, CategoryConstants.CATEGORY_ID_TECH_ACTIVITY, CategoryAdminStatus.ACTIVE,orderBy);
//        	result.addAll(tech);
//        }
        return result;
    }

    @Override
    public Tuple<Long,List<ActivityDTO>>  listActivities(ListActivitiesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        CrossShardListingLocator locator=new CrossShardListingLocator();
        if(cmd.getAnchor()!=null){
            locator.setAnchor(cmd.getAnchor());
        }
        
        // 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
//        Condition condtion=null;
//        if(!StringUtils.isEmpty(cmd.getTag())){
//            condtion= Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag());
//        }
//        List<Condition> conditions =new ArrayList<Condition>();
//        if(cmd.getLatitude()!=null&&cmd.getLongitude()!=null){     
//            double latitude= cmd.getLatitude();
//            double longitude=cmd.getLongitude();
//            GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
//            GeoHash[] adjacents = geo.getAdjacent();
//            List<String> geoHashCodes = new ArrayList<String>();
//            geoHashCodes.add(geo.toBase32());
//            for(GeoHash g : adjacents) {
//                geoHashCodes.add(g.toBase32());
//            }
//            conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
//        }

        List<String> geoHashCodes = new ArrayList<String>();
        if(cmd.getLatitude()!=null&&cmd.getLongitude()!=null){     
            double latitude= cmd.getLatitude();
            double longitude=cmd.getLongitude();
            GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
            GeoHash[] adjacents = geo.getAdjacent();
            geoHashCodes.add(geo.toBase32());
            for(GeoHash g : adjacents) {
                geoHashCodes.add(g.toBase32());
            }
        }
        
        Condition condtion = buildNearbyActivityCondition(namespaceId, geoHashCodes, cmd.getTag());
        
        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        //List<Activity> ret = activityProvider.listActivities(locator, value+1,condtion,Operator.OR, conditions.toArray(new Condition[conditions.size()]));
        List<Activity> ret = activityProvider.listActivities(locator, value+1, condtion);
        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
            Post post = forumProvider.findPostById(activity.getPostId());
            if(post==null){
                return null;
            }
            if(activity.getPosterUri()==null){
            	this.forumProvider.populatePostAttachments(post);
            	List<Attachment> attachmentList = post.getAttachments();
            	if(attachmentList != null && attachmentList.size() != 0){
            		for(Attachment attachment : attachmentList){
            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
            				activity.setPosterUri(attachment.getContentUri());
            			break;
            		}
            	}
            }
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setGroupId(activity.getGroupId());
            dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
            dto.setForumId(post.getForumId());
            return dto;
        }).filter(r->r!=null).collect(Collectors.toList());
        if(activityDtos.size()<value){
            locator.setAnchor(null);
        }
        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
    }

    @Override
    public Tuple<Long, List<ActivityDTO>> listNearByActivities(ListNearByActivitiesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        double latitude= cmd.getLatitude();
       double longitude=cmd.getLongitude();
       GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
       GeoHash[] adjacents = geo.getAdjacent();
       List<String> geoHashCodes = new ArrayList<String>();
       geoHashCodes.add(geo.toBase32());
       for(GeoHash g : adjacents) {
           geoHashCodes.add(g.toBase32());
       }
       CrossShardListingLocator locator=new CrossShardListingLocator();
       locator.setAnchor(cmd.getAnchor());
       int pageSize=configurationProvider.getIntValue("pagination.page.size", 20);
       // 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
       //List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
       //List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1,null,Operator.OR,conditions.toArray(new Condition[conditions.size()])).stream().map(activity->{
       Condition condition = buildNearbyActivityCondition(namespaceId, geoHashCodes, null);
       List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition).stream().map(activity->{
          ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
          dto.setActivityId(activity.getId());
          Post post = forumProvider.findPostById(activity.getPostId());
          if(post==null){
              return null;
          }
          if(activity.getPosterUri()==null){
          	this.forumProvider.populatePostAttachments(post);
          	List<Attachment> attachmentList = post.getAttachments();
          	if(attachmentList != null && attachmentList.size() != 0){
          		for(Attachment attachment : attachmentList){
          			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
          				activity.setPosterUri(attachment.getContentUri());
          			break;
          		}
          	}
          }
          dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
          dto.setEnrollUserCount(activity.getSignupAttendeeCount());
          dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
          dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
          dto.setProcessStatus(getStatus(activity).getCode());
          dto.setFamilyId(activity.getCreatorFamilyId());
          dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
          dto.setStartTime(activity.getStartTime().toString());
          dto.setStopTime(activity.getEndTime().toString());
          dto.setGroupId(activity.getGroupId());
          dto.setForumId(post.getForumId());
          return dto;
       }).collect(Collectors.toList());
       if(result.size()<pageSize)
       {
           locator.setAnchor(null);
       }
        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
    }

	@Override
	public boolean isPostIdExist(Long postId) {
		Activity activity = activityProvider.findSnapshotByPostId(postId);
		
		if(activity == null || "".equals(activity))
			return false;
		
		return true;
	}

	@Override
	public void updatePost(ActivityPostCommand cmd, Long postId) {
		Activity activity = activityProvider.findSnapshotByPostId(postId);
		 
		activity.setStatus((byte)0);
		activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		activityProvider.updateActivity(activity);
		 
	}

	@Override
	public  Tuple<Long, List<ActivityDTO>> listNearByActivitiesV2(ListNearByActivitiesCommandV2 cmdV2) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(cmdV2.getCommunity_id());
		
		List<String> geoHashCodes = new ArrayList<String>();

		for(CommunityGeoPoint geoPoint : geoPoints){
			
			double latitude = geoPoint.getLatitude();
			double longitude = geoPoint.getLongitude();
			
			GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 5);
			
			GeoHash[] adjacents = geo.getAdjacent();
			geoHashCodes.add(geo.toBase32());
	        for(GeoHash g : adjacents) {
	           geoHashCodes.add(g.toBase32());
	        }
		}

		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmdV2.getAnchor());
		int pageSize=configurationProvider.getIntValue("pagination.page.size", 20);
		// 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
		// List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
		// List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1,null,Operator.OR,conditions.toArray(new Condition[conditions.size()])).stream().map(activity->{
		Condition condition = buildNearbyActivityCondition(namespaceId, geoHashCodes, null);
		List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition).stream().map(activity->{
			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
			dto.setActivityId(activity.getId());
			Post post = forumProvider.findPostById(activity.getPostId());
			if(post==null){
                return null;
            }
            if(activity.getPosterUri()==null){
            	this.forumProvider.populatePostAttachments(post);
            	List<Attachment> attachmentList = post.getAttachments();
            	if(attachmentList != null && attachmentList.size() != 0){
            		for(Attachment attachment : attachmentList){
            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
            				activity.setPosterUri(attachment.getContentUri());
            			break;
            		}
            	}
            }
			dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
			dto.setEnrollUserCount(activity.getSignupAttendeeCount());
			dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
			dto.setGroupId(activity.getGroupId());
			dto.setForumId(post.getForumId());
			return dto;
       
		}).collect(Collectors.toList());
       
		if(result.size()<pageSize){
			
			locator.setAnchor(null);
		}
		return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
	}

	@Override
	public Tuple<Long, List<ActivityDTO>> listCityActivities(ListNearByActivitiesCommandV2 cmdV2) {
	    Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(cmdV2.getCommunity_id());
		
		List<String> geoHashCodes = new ArrayList<String>();
		
		for(CommunityGeoPoint geoPoint : geoPoints){
			
			double latitude = geoPoint.getLatitude();
			double longitude = geoPoint.getLongitude();
			
			GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 4);
			
			GeoHash[] adjacents = geo.getAdjacent();
			geoHashCodes.add(geo.toBase32());
	        for(GeoHash g : adjacents) {
	           geoHashCodes.add(g.toBase32());
	        }
		}
	        
	        
		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmdV2.getAnchor());
		int pageSize=configurationProvider.getIntValue("pagination.page.size", 20);
		// 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
		// List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
		// List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1,null,Operator.OR,conditions.toArray(new Condition[conditions.size()])).stream().map(activity->{
		Condition condition = buildNearbyActivityCondition(namespaceId, geoHashCodes, null);
		List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition).stream().map(activity->{
			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
			dto.setActivityId(activity.getId());
			Post post = forumProvider.findPostById(activity.getPostId());
			if(post==null){
                return null;
            }
            if(activity.getPosterUri()==null){
            	this.forumProvider.populatePostAttachments(post);
            	List<Attachment> attachmentList = post.getAttachments();
            	if(attachmentList != null && attachmentList.size() != 0){
            		for(Attachment attachment : attachmentList){
            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
            				activity.setPosterUri(attachment.getContentUri());
            			break;
            		}
            	}
            }
			dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
			dto.setEnrollUserCount(activity.getSignupAttendeeCount());
			dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
			dto.setGroupId(activity.getGroupId());
			dto.setForumId(post.getForumId());
			return dto;
       
		}).collect(Collectors.toList());
       
		if(result.size()<pageSize){
			
			locator.setAnchor(null);
		}
		return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
	}

	@Override
	public ListActivitiesReponse listActivitiesByTag(ListActivitiesByTagCommand cmd) {
        List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(cmd.getCommunity_id());
        
		StringBuilder strBuilder = new StringBuilder();
		List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
		for(CommunityGeoPoint geoPoint : geoPoints){
		    if(geoPoint.getLatitude() != null && geoPoint.getLongitude() != null) {
    		    GeoLocation geoLocation = new GeoLocation();
    		    geoLocation.setLatitude(geoPoint.getLatitude());
    		    geoLocation.setLongitude(geoPoint.getLongitude());
    		    geoLocationList.add(geoLocation);
		    } else {
		        if(LOGGER.isWarnEnabled()) {
		            LOGGER.warn("Invalid latitude or longitude, geoPoint={}", geoPoint);
		        }
		    }
		    
            if (strBuilder.length() > 0) {
                strBuilder.append(",");
            } else {
                strBuilder.append(geoPoint.getCommunityId());
            }
		}
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, communityId=" + cmd.getCommunity_id() + ", communities="
                + strBuilder);
        }
        
        ActivityLocationScope scope = ActivityLocationScope.NEARBY;
        if(cmd.getRange() == 4) {
            scope = ActivityLocationScope.SAME_CITY;
        }
        
        ListActivitiesByLocationCommand execCmd = new ListActivitiesByLocationCommand();
        execCmd.setLocationPointList(geoLocationList);
        execCmd.setPageAnchor(cmd.getAnchor());
        execCmd.setScope(scope.getCode());
        execCmd.setTag(cmd.getTag());
        execCmd.setPageSize(cmd.getPageSize());
        execCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        return listActivitiesByLocation(execCmd);
		
        // 把公用的代码转移到listActivitiesByLocation()中，公司场景和小区场景获取经纬度的方式不一样 by lqs 20160419
//        CrossShardListingLocator locator=new CrossShardListingLocator();
//        if(cmd.getAnchor()!=null){
//            locator.setAnchor(cmd.getAnchor());
//        }
//        Condition condtion=null;
//        if(!StringUtils.isEmpty(cmd.getTag())){
//            condtion= Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag());
//        }
//        
//        int range = cmd.getRange();
//        range = (range <= 0) ? 6 : range;
//        
//        List<String> geoHashCodes = new ArrayList<String>();
//		StringBuilder strBuilder = new StringBuilder();
//		for(CommunityGeoPoint geoPoint : geoPoints){
//			
//			double latitude = geoPoint.getLatitude();
//			double longitude = geoPoint.getLongitude();
//			
//			GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, range);
//			
//			GeoHash[] adjacents = geo.getAdjacent();
//			geoHashCodes.add(geo.toBase32());
//	        for(GeoHash g : adjacents) {
//	           geoHashCodes.add(g.toBase32());
//	        }
//	        
//	        if(strBuilder.length() > 0) {
//	            strBuilder.append(",");
//	        } else {
//	            strBuilder.append(geoPoint.getCommunityId());
//	        }
//		}
//		if(LOGGER.isDebugEnabled()) {
//		    LOGGER.debug("Query activities by geohash, communityId=" + cmd.getCommunity_id() + ", communities=" + strBuilder);
//		}
//		List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
//        
//        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
//        List<Activity> ret = activityProvider.listActivities(locator, value+1,condtion,Operator.OR, conditions.toArray(new Condition[conditions.size()]));
//        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
//            Post post = forumProvider.findPostById(activity.getPostId());
//            if(post==null){
//                return null;
//            }
//            if(activity.getPosterUri()==null){
//            	this.forumProvider.populatePostAttachments(post);
//            	List<Attachment> attachmentList = post.getAttachments();
//            	if(attachmentList != null && attachmentList.size() != 0){
//            		for(Attachment attachment : attachmentList){
//            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
//            				activity.setPosterUri(attachment.getContentUri());
//            			break;
//            		}
//            	}
//            }
//            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
//            dto.setActivityId(activity.getId());
//            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
//            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
//            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
//            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
//            dto.setProcessStatus(getStatus(activity).getCode());
//            dto.setFamilyId(activity.getCreatorFamilyId());
//            dto.setStartTime(activity.getStartTime().toString());
//            dto.setStopTime(activity.getEndTime().toString());
//            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
//            dto.setForumId(post.getForumId());
//            return dto;
//        }).filter(r->r!=null).collect(Collectors.toList());
//        if(activityDtos.size()<value){
//            locator.setAnchor(null);
//        }
//        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
	}
	
	@Override
	public ListActivitiesReponse listActivitiesByLocation(ListActivitiesByLocationCommand cmd) {
		User user = UserContext.current().getUser();
		Long uid = user.getId();
	    ListActivitiesReponse response = null;
	    List<GeoLocation> geoLocationList = cmd.getLocationPointList();
	    if(geoLocationList == null || geoLocationList.size() == 0) {
	        if(LOGGER.isInfoEnabled()) {
	            LOGGER.info("The location point list is empty, cmd={}", cmd);
	        }
	        return response;
	    }
	    
	    int geoCharCount = convertLocationScopeToGeoCharCount(cmd.getScope());
	    List<String> geoHashCodes = convertLocationToGeoCodes(geoLocationList, geoCharCount);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, geoCharCount={}, cmd={}, geoHashCodes={}", geoCharCount, cmd, geoHashCodes);
        }
        
        //List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
        Condition condition = buildNearbyActivityCondition(cmd.getNamespaceId(), geoHashCodes, cmd.getTag());
        
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int ipageSize = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<Activity> ret = activityProvider.listActivities(locator, ipageSize + 1, condition);
        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
            Post post = forumProvider.findPostById(activity.getPostId());
            if(activity.getPosterUri() == null && post != null){
                this.forumProvider.populatePostAttachments(post);
                List<Attachment> attachmentList = post.getAttachments();
                if(attachmentList != null && attachmentList.size() != 0){
                    for(Attachment attachment : attachmentList){
                        if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
                            activity.setPosterUri(attachment.getContentUri());
                        break;
                    }
                }
            }
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setGroupId(activity.getGroupId());
            dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
            if(post != null) {
                dto.setForumId(post.getForumId());
            }
            List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(uid, UserFavoriteTargetType.ACTIVITY.getCode(), activity.getId());
            if(favorite == null || favorite.size() == 0) {
            	dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            } else {
            	dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            }
            
            return dto;
        }).filter(r->r!=null).collect(Collectors.toList());
        
        Long nextPageAnchor = null;
        if(activityDtos.size() > ipageSize) {
            activityDtos.remove(activityDtos.size() - 1);
            nextPageAnchor = activityDtos.get(activityDtos.size() - 1).getActivityId();
        }
        
        response = new ListActivitiesReponse(nextPageAnchor, activityDtos);
        return response;
	}
	
	private Condition buildNearbyActivityCondition(Integer namespaceId, List<String> geoHashCodes, String tag) {
	    Condition condition = null;
	    if(namespaceId != null) {
	        condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
	    }
	    
        if(!StringUtils.isEmpty(tag)){
            if(condition == null) {
                condition = Tables.EH_ACTIVITIES.TAG.eq(tag);
            } else {
                condition = condition.and(Tables.EH_ACTIVITIES.TAG.eq(tag));
            }
        } 
        
        Condition geoCondition = buildActivityGeoLocationCondition(geoHashCodes);
        if(geoCondition != null) {
            if(condition == null) {
                condition = geoCondition;
            } else {
                condition = condition.and(geoCondition);
            }
        }
        
        return condition;
	}
	
	private Condition buildActivityGeoLocationCondition(List<String> geoHashCodes) {
	    Condition condition = null;
	    
	    if(geoHashCodes != null) {
	        for(String geoHashCode : geoHashCodes) {
	            if(condition == null) {
	                condition = Tables.EH_ACTIVITIES.GEOHASH.like(geoHashCode + "%");
	            } else {
	                condition = condition.or(Tables.EH_ACTIVITIES.GEOHASH.like(geoHashCode + "%"));
	            }
	        }
	    }
	    
	    return condition;
	}
	
	private int convertLocationScopeToGeoCharCount(Byte locationScope) {
	    ActivityLocationScope scope = ActivityLocationScope.fromCode(locationScope);
        int geoCharCount = 6; // 默认在3公里内，按GEO算法最接近的字符数为6
        if(scope != null) {
            switch(scope) {
            case ALL:
                geoCharCount = 1;
                break;
            case NEARBY:
                geoCharCount = 6;
                break;
            case SAME_CITY:
                geoCharCount = 4;
            }
        }
        
        return geoCharCount;
	}
	
	private List<String> convertLocationToGeoCodes(List<GeoLocation> geoLocationList, int geoCharCount) {
	    List<String> geoHashCodes = new ArrayList<String>();
        for(GeoLocation geoLocation : geoLocationList){
            Double latitude = geoLocation.getLatitude();
            Double longitude = geoLocation.getLongitude();
            
            GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, geoCharCount);
            
            GeoHash[] adjacents = geo.getAdjacent();
            geoHashCodes.add(geo.toBase32());
            for(GeoHash g : adjacents) {
               geoHashCodes.add(g.toBase32());
            }
        }
        
        return geoHashCodes;
	}
	
//	@Override
//	public Tuple<Long, List<ActivityDTO>> listActivitiesByNamespaceIdAndTag(
//			ListActivitiesByNamespaceIdAndTagCommand cmd) {
//		 
//		CrossShardListingLocator locator=new CrossShardListingLocator();
//		
//        if(null !=cmd.getAnchor()){
//            locator.setAnchor(cmd.getAnchor());
//        }
//        Condition condtion = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(cmd.getNamespaceId());
//        
//        if(!StringUtils.isEmpty(cmd.getTag())){
//            condtion= condtion.and(Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag()));
//        }
//
//        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
//        
//        List<Activity> ret = activityProvider.listActivities(locator, value+1,condtion,Operator.OR, null);
//        
//        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
//            Post post = forumProvider.findPostById(activity.getPostId());
//            if(post==null){
//                return null;
//            }
//            if(activity.getPosterUri()==null){
//            	this.forumProvider.populatePostAttachments(post);
//            	List<Attachment> attachmentList = post.getAttachments();
//            	if(attachmentList != null && attachmentList.size() != 0){
//            		for(Attachment attachment : attachmentList){
//            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
//            				activity.setPosterUri(attachment.getContentUri());
//            			break;
//            		}
//            	}
//            }
//            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
//            dto.setActivityId(activity.getId());
//            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
//            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
//            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
//            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
//            dto.setProcessStatus(getStatus(activity).getCode());
//            dto.setFamilyId(activity.getCreatorFamilyId());
//            dto.setStartTime(activity.getStartTime().toString());
//            dto.setStopTime(activity.getEndTime().toString());
//            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
//            dto.setForumId(post.getForumId());
//            dto.setGuest(activity.getGuest());
//            return dto;
//        }).filter(r->r!=null).collect(Collectors.toList());
//        if(activityDtos.size()<value){
//            locator.setAnchor(null);
//        }
//        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
//	}
	@Override
	public ListActivitiesReponse listActivitiesByNamespaceIdAndTag(ListActivitiesByNamespaceIdAndTagCommand cmd) {
		ListActivitiesReponse response = new ListActivitiesReponse(null, null);
		ListActivityTopicByCategoryAndTagCommand command = new ListActivityTopicByCategoryAndTagCommand();
		command.setCategoryId(cmd.getCategoryId());
		command.setCommunityId(cmd.getCommunityId());
		command.setPageAnchor(cmd.getPageAnchor());
		command.setPageSize(cmd.getPageSize());
		command.setTag(cmd.getTag());
		ListPostCommandResponse post = forumService.listActivityPostByCategoryAndTag(command);
	    if(post != null) {
	    	response.setNextPageAnchor(post.getNextPageAnchor());
	    	List<PostDTO> posts = post.getPosts();
	    	if(posts != null) {
	    		List<ActivityDTO> activityDtos = posts.stream().map(r -> {
	    			Activity activity = activityProvider.findActivityById(r.getEmbeddedId());
	    			if(activity != null) {
		    			if(activity.getPosterUri()==null){
		    	           
		    				List<AttachmentDTO> attachmentList = r.getAttachments();
				            if(attachmentList != null && attachmentList.size() != 0){
				                for(AttachmentDTO attachment : attachmentList){
				                    if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
				                    	activity.setPosterUri(attachment.getContentUri());
				                    break;
				                }
				            }
		    	        }
		    			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
		    			dto.setActivityId(activity.getId());
		    	        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
		    	        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
		    	        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
		    	        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
		    	        dto.setProcessStatus(getStatus(activity).getCode());
		    	        dto.setFamilyId(activity.getCreatorFamilyId());
		    	        dto.setStartTime(activity.getStartTime().toString());
		    	        dto.setStopTime(activity.getEndTime().toString());
		    	        dto.setGroupId(activity.getGroupId());
		    	        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
		    	        dto.setForumId(r.getForumId());
		    	        dto.setGuest(activity.getGuest());
		    			
		    			return dto;
	    			}
	    			else {
	    				return null;
	    			}
	    		}).filter(r->r!=null).collect(Collectors.toList());
	    		
	    		response.setActivities(activityDtos);
	    	}
	    }
	    
	    
	    
	    return response;
	}
	
	// 由于场景扩展到园区，单纯靠Entity实例已经不能满足要求，需要改为按场景来区分 by lqs 20160513
//	@Override
//	public ListActivitiesReponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd) {
//	    long startTime = System.currentTimeMillis();
//	    User user = UserContext.current().getUser();
//        Long userId = user.getId();
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        SceneTokenDTO sceneTokenDto = userService.checkSceneToken(userId, cmd.getSceneToken());
//        
//        int geoCharCount = 6; // 默认使用6位GEO字符
//        ActivityLocationScope scope = ActivityLocationScope.fromCode(cmd.getScope());
//        if(scope == ActivityLocationScope.SAME_CITY) {
//            geoCharCount = 4;
//        }
//        
//        ListActivitiesReponse resp = null;
//        Community community = null;
//        ListActivitiesByTagCommand execCmd = null;
//        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
//        switch(entityType) {
//        case COMMUNITY_RESIDENTIAL:
//        case COMMUNITY_COMMERCIAL:
//        case COMMUNITY:
//            community = communityProvider.findCommunityById(sceneTokenDto.getEntityId());
//            execCmd = new ListActivitiesByTagCommand();
//            execCmd.setCommunity_id(community.getId());
//            execCmd.setAnchor(cmd.getPageAnchor());
//            execCmd.setPageSize(cmd.getPageSize());
//            execCmd.setTag(cmd.getTag());
//            execCmd.setRange(geoCharCount);
//            resp = listActivitiesByTag(execCmd);
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
//            if(family != null) {
//                community = communityProvider.findCommunityById(family.getCommunityId());
//                execCmd = new ListActivitiesByTagCommand();
//                execCmd.setCommunity_id(community.getId());
//                execCmd.setAnchor(cmd.getPageAnchor());
//                execCmd.setPageSize(cmd.getPageSize());
//                execCmd.setTag(cmd.getTag());
//                execCmd.setRange(geoCharCount);
//                resp = listActivitiesByTag(execCmd);
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
//                }
//            }
//            break;
//        case ORGANIZATION:
//            ListOrgNearbyActivitiesCommand execOrgCmd = ConvertHelper.convert(cmd, ListOrgNearbyActivitiesCommand.class);
//            execOrgCmd.setOrganizationId(sceneTokenDto.getEntityId());
//            resp = listOrgNearbyActivities(execOrgCmd);
//            break;
//        default:
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
//            break;
//        }
//        
//        if(LOGGER.isDebugEnabled()) {
//            long endTime = System.currentTimeMillis();
//            LOGGER.debug("List nearby activities by scene, userId={}, namespaceId={}, elapse={}, cmd={}", 
//                userId, namespaceId, (endTime - startTime), cmd);
//        }
//        
//        return resp;
//	}
	
	@Override
	public ListActivitiesReponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd) {
	    long startTime = System.currentTimeMillis();
	    User user = UserContext.current().getUser();
	    Long userId = user.getId();
	    Integer namespaceId = UserContext.getCurrentNamespaceId();
	    SceneTokenDTO sceneTokenDto = userService.checkSceneToken(userId, cmd.getSceneToken());
	    
	    int geoCharCount = 6; // 默认使用6位GEO字符
	    ActivityLocationScope scope = ActivityLocationScope.fromCode(cmd.getScope());
	    if(scope == ActivityLocationScope.SAME_CITY) {
	        geoCharCount = 4;
	    }
	    
	    ListActivitiesReponse resp = null;
	    SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
	    switch(sceneType) {
	    case DEFAULT:
	    case PARK_TOURIST:
	        resp = listCommunityNearbyActivities(sceneTokenDto, cmd, geoCharCount, sceneTokenDto.getEntityId());
	        break;
	    case FAMILY:
	        FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
	        if(family != null) {
	            resp = listCommunityNearbyActivities(sceneTokenDto, cmd, geoCharCount, family.getCommunityId());
	        } else {
	            if(LOGGER.isWarnEnabled()) {
	                LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
	            }
	        }
	        break;
        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
	        Organization organization = organizationProvider.findOrganizationById(sceneTokenDto.getEntityId());
            if(organization != null) {
                Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
                resp = listCommunityNearbyActivities(sceneTokenDto, cmd, geoCharCount, communityId);
            }
            break;
	    case PM_ADMIN:
	        ListOrgNearbyActivitiesCommand execOrgCmd = ConvertHelper.convert(cmd, ListOrgNearbyActivitiesCommand.class);
	        execOrgCmd.setOrganizationId(sceneTokenDto.getEntityId());
	        resp = listOrgNearbyActivities(execOrgCmd);
	        break;
	    default:
	        LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
	        break;
	    }
	    
	    if(LOGGER.isDebugEnabled()) {
	        long endTime = System.currentTimeMillis();
	        LOGGER.debug("List nearby activities by scene, userId={}, namespaceId={}, elapse={}, cmd={}", 
	            userId, namespaceId, (endTime - startTime), cmd);
	    }
	    
	    return resp;
	}
	
	private ListActivitiesReponse listCommunityNearbyActivities(SceneTokenDTO sceneTokenDto, ListNearbyActivitiesBySceneCommand cmd, 
	        int geoCharCount, Long communityId) {
	    if(communityId != null) {
    	    ListActivitiesByTagCommand execCmd = new ListActivitiesByTagCommand();
            execCmd.setCommunity_id(communityId);
            execCmd.setAnchor(cmd.getPageAnchor());
            execCmd.setPageSize(cmd.getPageSize());
            execCmd.setTag(cmd.getTag());
            execCmd.setRange(geoCharCount);
            
            return listActivitiesByTag(execCmd);
	    } else {
	        LOGGER.error("Community not found to query nearby activities, sceneTokenDto={}, communityId={}", sceneTokenDto, communityId);
	        return null;
	    }
	}
	
	@Override
    public ListActivitiesReponse listOrgNearbyActivities(ListOrgNearbyActivitiesCommand cmd) {
        List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();

	    OrganizationDetail orgDetail = organizationProvider.findOrganizationDetailByOrganizationId(cmd.getOrganizationId());
	    if(orgDetail != null && orgDetail.getLatitude() != null && orgDetail.getLongitude() != null) {
            GeoLocation geoLocation = new GeoLocation();
            geoLocation.setLatitude(orgDetail.getLatitude());
            geoLocation.setLongitude(orgDetail.getLongitude());
            geoLocationList.add(geoLocation);
	    }
	    
	    // 由于存在大量的公司没有自身的经纬度，故取其所管理的小区来作为经纬度
	    if(geoLocationList.size() == 0) {
	        List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());
            for(CommunityDTO community : communities) {
                List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(community.getId());
                
                StringBuilder strBuilder = new StringBuilder();
                for(CommunityGeoPoint geoPoint : geoPoints){
                    if(geoPoint.getLatitude() != null && geoPoint.getLongitude() != null) {
                        GeoLocation geoLocation = new GeoLocation();
                        geoLocation.setLatitude(geoPoint.getLatitude());
                        geoLocation.setLongitude(geoPoint.getLongitude());
                        geoLocationList.add(geoLocation);
                    } else {
                        if(LOGGER.isWarnEnabled()) {
                            LOGGER.warn("Invalid latitude or longitude, cmd={}, geoPoint={}", cmd, geoPoint);
                        }
                    }
                }
            }
	    }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, cmd={}, geoLocationList={}", cmd, geoLocationList);
        }
        
        ListActivitiesByLocationCommand execCmd = new ListActivitiesByLocationCommand();
        execCmd.setLocationPointList(geoLocationList);
        execCmd.setPageAnchor(cmd.getPageAnchor());
        execCmd.setScope(cmd.getScope());
        execCmd.setTag(cmd.getTag());
        execCmd.setPageSize(cmd.getPageSize());
        execCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        
        return listActivitiesByLocation(execCmd);
   }

	@Override
	public ActivityShareDetailResponse getActivityShareDetail(
			ActivityTokenDTO postToken) {
		
        Activity activity = activityProvider.findSnapshotByPostId(postToken.getPostId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.postId={}", postToken.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid activity postId " + postToken.getPostId());
        }
        GetTopicCommand command = new GetTopicCommand();
        command.setTopicId(postToken.getPostId());
        command.setForumId(postToken.getForumId());
        PostDTO post = forumService.getTopic(command);
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        
        ActivityShareDetailResponse response = new ActivityShareDetailResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setForumId(post.getForumId());
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setActivityId(activity.getId());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(activity.getPosterUri()==null?null:contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId()));
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        response.setActivity(dto);
        
        response.setContent(post.getContent());
        response.setChildCount(post.getChildCount());
        response.setViewCount(post.getViewCount());
        response.setNamespaceId(activity.getNamespaceId());
        response.setAttachments(post.getAttachments());
        response.setSubject(post.getSubject());
        response.setCreatorNickName(post.getCreatorNickName());
        response.setCreateTime(post.getCreateTime());
        
		return response;
	}
}
