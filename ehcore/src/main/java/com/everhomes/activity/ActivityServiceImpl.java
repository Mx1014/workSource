//Formative
package com.everhomes.activity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StatusChecker;

@Component
public class ActivityServiceImpl implements ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private static final String SIGNUP_AUTO_COMMENT = "auto.comment";

    private static final String CHECKIN_AUTO_COMMENT = "checkin.auto.comment";

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
    private DbProvider dbProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public void createPost(ActivityPostCommand cmd, Long postId) {
        Activity activity = ConvertHelper.convert(cmd, Activity.class);
        activity.setPostId(postId);
        // avoid nullpoint
        activity.setCheckinAttendeeCount(0);
        activity.setCheckinFamilyCount(0);
        activity.setConfirmAttendeeCount(0);
        activity.setConfirmFamilyCount(0);
        activity.setSignupAttendeeCount(0);
        activity.setSignupFamilyCount(0);
        activity.setCreateTime(new Timestamp(cmd.getCreateTime()));
        activity.setStartTime(new Timestamp(cmd.getStartTimeMs()));
        activity.setEndTime(new Timestamp(cmd.getEndTimeMs()));
        if (cmd.getDeleteTime() != null)
            activity.setDeleteTime(new Timestamp(cmd.getDeleteTime()));
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
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        ActivityRoster roster = createRoster(cmd, user, activity);
        dbProvider.execute((status) -> {
            activityProvider.createActivityRoster(roster);
            activity.setSignupFamilyCount(activity.getSignupFamilyCount() + 1);
            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()
                    + (cmd.getAdultCount() + cmd.getChildCount()));
            activityProvider.updateActivity(activity);
            Post comment = new Post();
            comment.setParentPostId(post.getId());
            comment.setCreatorUid(user.getId());
            //set content type
            comment.setContentType("text");
            String template = configurationProvider.getValue(SIGNUP_AUTO_COMMENT, "");

            if (!StringUtils.isEmpty(template)) {
                comment.setContent(template);
                forumProvider.createPost(comment);
            }
            return status;
        });
        // notify all people?
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivities.class, activity.getId());
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
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
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        activityProvider.cancelSignup(activity, user.getId());
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setProcessStatus(getStatus(activity).getCode());
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
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        Family family = familyProvider.findFamilyByAddressId(user.getAddressId());
        if (family == null) {
            LOGGER.error("handle family error.cannot find family");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_CONTACT_FAMILY, "invalid famliy id");
        }
        activityProvider.checkIn(activity, user.getId(), family.getId());
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
        return dto;
    }

    private ProcessStatus getStatus(Activity activity) {
        return StatusChecker.getProcessStatus(activity.getStartTimeMs(), activity.getEndTimeMs());
    }

    @Override
    public ActivityListResponse listActivities(ActivityListCommand cmd) {
        User user = UserContext.current().getUser();
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        List<ActivityRoster> rsp = activityProvider.listRosterPagination(cmd.getPageOffset(), cmd.getPageSize(),
                activity.getId());
        ActivityRoster userRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), UserContext
                .current().getUser().getId());
        ActivityListResponse response = new ActivityListResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        response.setActivity(dto);
        List<ActivityMemberDTO> result = rsp.stream().map(r -> {
            return ConvertHelper.convert(r, ActivityMemberDTO.class);
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

    private ActivityStatus getActivityStatus(ActivityRoster userRoster) {
        if (userRoster.getCheckinFlag() == CheckInStatus.CHECKIN.getCode()) {
            return ActivityStatus.CHECKEINED;
        }
        if (userRoster.getConfirmFlag() != null) {
            return ActivityStatus.CONFIRMED;
        }
        return ActivityStatus.SIGNUP;

    }

    @Override
    public ActivityDTO confirm(ActivityComfirmCommand cmd) {
        ActivityRoster item = activityProvider.findRosterById(cmd.getActivityRosterId());
        if (item == null) {
            LOGGER.error("cannnot find roster record in database");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ROSTER, "cannnot find roster record in database id="
                            + cmd.getActivityRosterId());
        }
        Activity activity = activityProvider.findActivityById(item.getActivityId());
        if (activity == null) {
            LOGGER.error("cannnot find activity record in database");
            // TODO
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ID,
                    "cannnot find activity record in database id=" + cmd.getActivityRosterId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("cannnot find post record in database");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_POST_ID,
                    "cannnot find post record in database id=" + cmd.getActivityRosterId());
        }
        User user = UserContext.current().getUser();
        if (post.getCreatorUid() != user.getId()) {
            LOGGER.error("the user is invalid.cannot confirm");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_USER,
                    "the user is invalid.cannot confirm id=" + cmd.getActivityRosterId());
        }
        activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + item.getChildCount()
                + item.getChildCount());
        activity.setConfirmFamilyCount(activity.getConfirmFamilyCount() + 1);
        activity.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        activityProvider.updateActivity(activity);
        item.setConfirmUid(user.getId());
        item.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        item.setConfirmFamilyId(cmd.getConfirmFamilyId());
        // set confirm
        item.setConfirmFlag(Long.valueOf(ConfirmStatus.CONFIRMED.getCode()));
        activityProvider.updateRoster(item);
        forumProvider.createPost(createPost(user.getId(), post.getId(), cmd.getConfirmFamilyId(), cmd.getTargetName()));
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setUserActivityStatus(getActivityStatus(item).getCode());
        dto.setProcessStatus(getStatus(activity).getCode());
        return dto;
    }

    private Post createPost(Long uid, Long parentPostId, Long familyId, String targetName) {
        // for checkin
        Post post = new Post();
        post.setParentPostId(parentPostId);
        String template = configurationProvider.getValue(CHECKIN_AUTO_COMMENT, "");
        post.setContent(TemplatesConvert.convert(template, new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", targetName);
            }
        }, ""));
        post.setCreatorUid(uid);
        return post;
    }

    @Override
    public ActivityDTO findSnapshotByPostId(Long postId) {
        Activity activity = activityProvider.findSnapshotByPostId(postId);
        if (activity == null) {
            LOGGER.error("cannot find activity for post");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_POST_ID, "cannot find activity");
        }
        User user = UserContext.current().getUser();
        ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        return dto;
    }
}
