// @formatter:off
package com.everhomes.activity;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.app.AppConstants;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostContentType;
import com.everhomes.group.GroupService;
import com.everhomes.group.LeaveGroupCommand;
import com.everhomes.group.RejectJoinGroupRequestCommand;
import com.everhomes.group.RequestToJoinGroupCommand;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StatusChecker;
import com.everhomes.util.Tuple;


@Component
public class ActivityServiceImpl implements ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private static final String SIGNUP_AUTO_COMMENT = "signup.auto.comment";

    private static final String CHECKIN_AUTO_COMMENT = "checkin.auto.comment";
    
    private static final String CONFIRM_AUTO_COMMENT="confirm.auto.comment";
    private static final String CANCEL_AUTO_COMMENT="cancel.auot.comment";

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

    @Override
    public void createPost(ActivityPostCommand cmd, Long postId) {
        User user = UserContext.current().getUser();
        Activity activity = ConvertHelper.convert(cmd, Activity.class);
        activity.setPostId(postId);
        activity.setNamespaceId(0);
        activity.setCreatorUid(user.getId());
        activity.setGroupDiscriminator(EntityType.ACTIVITY.getCode());
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
        
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        //if date time format is not ok,return now
        long startTimeMs=convert(cmd.getStartTime(), "yyyy-MM-dd HH:mm:ss").getTime();
        long endTimeMs=convert(cmd.getEndTime(), "yyyy-MM-dd HH:mm:ss").getTime();
        activity.setStartTime(new Timestamp(startTimeMs));
        activity.setEndTime(new Timestamp(endTimeMs));
        activity.setStartTimeMs(startTimeMs);
        activity.setEndTimeMs(endTimeMs);
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        activityProvider.createActity(activity);
    }

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
            Post comment = new Post();
            comment.setParentPostId(post.getId());
            comment.setForumId(post.getForumId());
            comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            comment.setCreatorUid(user.getId());
            comment.setContentType(PostContentType.TEXT.getCode());
            String template = configurationProvider.getValue(SIGNUP_AUTO_COMMENT, "");

            if (!StringUtils.isEmpty(template)) {
                comment.setContent(template);
                forumProvider.createPost(comment);
            }
            if (activity.getGroupId() != null) {
                RequestToJoinGroupCommand joinCmd = new RequestToJoinGroupCommand();
                joinCmd.setGroupId(activity.getGroupId());
                joinCmd.setRequestText("request to join activity group");
                groupService.requestToJoinGroup(joinCmd);
            }
            activityProvider.createActivityRoster(roster);
            activityProvider.updateActivity(activity);
            return status;
        });
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setProcessStatus(getStatus(activity).getCode());
        return dto;
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
        Post p = createPost(user.getId(), post, null, "");
        p.setContent(configurationProvider.getValue(CANCEL_AUTO_COMMENT, ""));
        forumProvider.createPost(p);
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setUserActivityStatus(ActivityStatus.UN_SIGNUP.getCode());
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
        dbProvider.execute(status->{
            ActivityRoster roster = activityProvider.checkIn(activity, user.getId(), getFamilyId());
            Post p = createPost(user.getId(),post,null,"");
            p.setContent(configurationProvider.getValue(CHECKIN_AUTO_COMMENT, ""));
            Long familyId = getFamilyId();
            if (familyId != null)
                activity.setSignupFamilyCount(activity.getSignupFamilyCount() + 1);
            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()
                    + (roster.getAdultCount() + roster.getChildCount()));
            roster.setCheckinFlag((byte)1);
            forumProvider.createPost(p);
            return status;
        });
        
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setGroupId(activity.getGroupId());
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
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setActivityId(activity.getId());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        response.setActivity(dto);
        List<ActivityMemberDTO> result = rosterList.stream().map(r -> {
            ActivityMemberDTO d = ConvertHelper.convert(r, ActivityMemberDTO.class);
            d.setConfirmFlag(convertToInt(r.getConfirmFlag()));
            if (r.getUid().longValue() == post.getCreatorUid().longValue())
                d.setCreatorFlag(1);
            d.setLotteryWinnerFlag(convertToInt(r.getLotteryFlag()));
            d.setCheckinFlag(ActivityStatus.SIGNUP.getCode());
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
                }

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
        if (user.getId() == activity.getCreatorUid()) {
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
        if (userRoster.getCheckinFlag() == CheckInStatus.CHECKIN.getCode()) {
            return ActivityStatus.CHECKEINED;
        }
        if (userRoster.getConfirmFlag() != null&&userRoster.getConfirmFlag()!=0) {
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
            forumProvider.createPost(createPost(user.getId(), post, cmd.getConfirmFamilyId(), cmd.getTargetName()));
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
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setUserActivityStatus(getActivityStatus(item).getCode());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setGroupId(activity.getGroupId());
        return dto;
    }

    private Post createPost(Long uid, Post p, Long familyId, String targetName) {
        // for checkin
        Post post = new Post();
        post.setParentPostId(p.getId());
        post.setForumId(p.getForumId());
        String template = configurationProvider.getValue(CONFIRM_AUTO_COMMENT, "");
        post.setContent(TemplatesConvert.convert(template, new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", targetName);
            }
        }, ""));
        post.setCreatorUid(uid);
        post.setContentType(PostContentType.TEXT.getCode());
        post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
        User user = UserContext.current().getUser();
        ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
        LOGGER.info("find roster {}",roster);
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setProcessStatus(getStatus(activity).getCode());
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
        activityProvider.deleteRoster(roster);
        if (roster.getConfirmFlag() == Long.valueOf(ConfirmStatus.CONFIRMED.getCode())) {
            int result = activity.getCheckinAttendeeCount() - total;
            activity.setConfirmAttendeeCount(result < 0 ? 0 : result);
        }
        if (roster.getCheckinFlag() == CheckInStatus.CHECKIN.getCode()) {
            int result = activity.getCheckinAttendeeCount() - total;
            activity.setCheckinAttendeeCount(result < 0 ? 0 : result);
        }
        activityProvider.updateActivity(activity);
        if (activity.getGroupId() != null) {
            RejectJoinGroupRequestCommand rejectCmd=new RejectJoinGroupRequestCommand();
            rejectCmd.setGroupId(activity.getGroupId());
            rejectCmd.setUserId(roster.getUid());
            rejectCmd.setRejectText(cmd.getReason());
            //reject to join group
            groupService.rejectJoinGroupRequest(rejectCmd);
        }
    }

    @Override
    public ActivityListResponse findActivityDetailsByPostId(Post post) {
       User user=UserContext.current().getUser();
        Activity activity = activityProvider.findSnapshotByPostId(post.getId());
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
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        response.setActivity(dto);
        List<ActivityMemberDTO> result = rosterList.stream().map(r -> {
            ActivityMemberDTO d = ConvertHelper.convert(r, ActivityMemberDTO.class);
            d.setConfirmFlag(convertToInt(r.getConfirmFlag()));
            if (user.getId().intValue() == post.getCreatorUid().intValue())
                d.setCreatorFlag(1);
            d.setLotteryWinnerFlag(convertToInt(r.getLotteryFlag()));
            d.setCheckinFlag(ActivityStatus.SIGNUP.getCode());
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
                }

            }
            return d;
        }).collect(Collectors.toList());
        response.setRoster(result);
        response.setCreatorFlag(0);
        // current user is sender?
        if (user.getId() == activity.getCreatorUid()) {
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
    public List<Category> listActivityCategories() {
        List<Category> result = categoryProvider.listChildCategories(10001L, CategoryAdminStatus.ACTIVE);
        return result;
    }

    @Override
    public Tuple<Long,List<ActivityDTO>>  listActivities(ListActivitiesCommand cmd) {
        CrossShardListingLocator locator=new CrossShardListingLocator();
        if(cmd.getAnchor()!=null){
            locator.setAnchor(cmd.getAnchor());
        }
        Condition condtion=null;
        if(!StringUtils.isEmpty(cmd.getTag())){
            condtion= Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag());
        }
        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<Activity> ret = activityProvider.listActivities(locator, value+1, condtion);
        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setGroupId(activity.getGroupId());
            return dto;
        }).collect(Collectors.toList());
        if(activityDtos.size()<value){
            locator.setAnchor(null);
        }
        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
    }
    
}
