// @formatter:off
package com.everhomes.activity;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.activity.ActivityChargeFlag;
import com.everhomes.rest.activity.ActivityNotificationTemplateCode;
import com.everhomes.rest.activity.ActivityPostCommand;
import com.everhomes.rest.activity.ActivityRosterStatus;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.PostCloneFlag;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserFavoriteTargetType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.PostSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.util.CronDateUtils;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import net.greghaines.jesque.Job;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ActivitySignupTimeoutServiceImpl implements ActivitySignupTimeoutService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySignupTimeoutServiceImpl.class);

    @Autowired
    WorkerPoolFactory workerPoolFactory;

    @Autowired
    JesqueClientFactory jesqueClientFactory;

    @Autowired
    private ActivityProivider activityProivider;

    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Autowired
    private PostSearcher postSearcher;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    public void pushTimeout(Post post) {
        if (post != null) {
            Activity activity = this.activityProivider.findSnapshotByPostId(post.getId());
            if (activity != null && activity.getSignupEndTime() != null) {
                String triggerName = CoordinationLocks.ACTIVITY_SIGNUP_TIMEOUT.getCode() + System.currentTimeMillis();
                String jobName = CoordinationLocks.ACTIVITY_SIGNUP_TIMEOUT.getCode() + System.currentTimeMillis();
                String cronExpression = CronDateUtils.getCron(activity.getSignupEndTime());
                Map param = new HashMap();
                param.put("postId",post.getId());
                LOGGER.info("The first ActivitySignupTimeoutJob has been prepared at " + LocalDateTime.now());
                scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ActivitySignupTimeoutJob.class, param);
            }else {
                LOGGER.error("create ActivitySignupTimeout error! post={},activity={}",post,activity);
            }
        }else {
            LOGGER.error("create ActivitySignupTimeout error! post is null");
        }
    }

    @Override
    public void cancelTimeoutActivity(Long postId) {
        Post post = this.forumProvider.findPostById(postId);
        if (post == null) {
            LOGGER.error("post not found error");
        }
        Long userId = post.getCreatorUid();
        Post pPost = null;
        if(post.getParentPostId() != null && post.getParentPostId() != 0) {
            pPost = this.forumProvider.findPostById(post.getParentPostId());
            if(null != pPost){
                post.setContentCategory(pPost.getContentCategory());
                post.setActivityCategoryId(pPost.getActivityCategoryId());
            }
        }
        Post parentPost = pPost;


        Long embededAppId = post.getEmbeddedAppId();
        ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);

        List<Long> userIds = new ArrayList<Long>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null,
                (DSLContext context, Object reducingContext)-> {
                    context.select().from(Tables.EH_USER_FAVORITES)
                            .where(Tables.EH_USER_FAVORITES.TARGET_ID.eq(postId))
                            .and(Tables.EH_USER_FAVORITES.TARGET_TYPE.eq(UserFavoriteTargetType.TOPIC.getCode())
                                    .or(Tables.EH_USER_FAVORITES.TARGET_TYPE.eq(UserFavoriteTargetType.ACTIVITY.getCode())))
                            .fetch().map( (r) ->{

                        userIds.add(r.getValue(Tables.EH_USER_FAVORITES.OWNER_UID));

                        return null;
                    });
                    return true;
                });

        post.setStatus(PostStatus.INACTIVE.getCode());
        post.setDeleterUid(userId);
        post.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {

                if(handler != null) {
                    handler.beforePostDelete(post);
                }

                this.forumProvider.updatePost(post);
                // 删除评论时帖子的child count减1 mod by xiongying 20160428
                if(parentPost != null) {
                    //更新前需要在锁内部重新查询，因为当前的parentPost可能已经是过期的数据了   edit by yanjun 20171207
                    Post updateParentPost = forumProvider.findPostById(parentPost.getId());
                    if(updateParentPost != null){
                        updateParentPost.setChildCount(updateParentPost.getChildCount() - 1);
                        forumProvider.updatePost(updateParentPost);
                    }
                }
                if(userId.equals(post.getCreatorUid())){
                    this.userActivityProvider.deletePostedTopic(userId, postId);
                    userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, -1);
                }
                if(userIds.size() != 0){
                    for(Long id:userIds){
                        userActivityProvider.deleteFavorite(id, postId, UserFavoriteTargetType.TOPIC.getCode());
                        userActivityProvider.deleteFavorite(id, postId, UserFavoriteTargetType.ACTIVITY.getCode());
                    }
                }
                //如果是活动创建者删除活动，通知到已报名的人，add by tt, 20161012
                if (post.getCreatorUid().longValue() == userId.longValue() &&  embededAppId != null && embededAppId.longValue() == AppConstants.APPID_ACTIVITY) {
                    sendMessageWhenCreatorDeleteActivity(post.getEmbeddedId(), userId);
                }

                return null;
            });

            this.postSearcher.deleteById(post.getId());

            if(handler != null) {
                handler.afterPostDelete(post);
            }
            deletePostAndActivity(post, userId);

        } catch(Exception e) {
            LOGGER.error("Failed to update the post status, userId=" + userId + ", postId=" + postId, e);
        }
    }

    private ForumEmbeddedHandler getForumEmbeddedHandler(Long embededAppId) {
        ForumEmbeddedHandler handler = null;

        if(embededAppId != null && embededAppId.longValue() > 0) {
            String handlerPrefix = ForumEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + embededAppId);
        }

        return handler;
    }

    //当活动报名人数不足最低限制人数时，取消活动发消息通知已报名的人
    private void sendMessageWhenCreatorDeleteActivity(Long activityId, Long userId){
        Activity activity = activityProivider.findActivityById(activityId);
        if (activity == null) {
            return ;
        }
        User user = this.userProvider.findUserById(userId);
        List<ActivityRoster> activityRosters = activityProivider.listRosters(activityId, ActivityRosterStatus.NORMAL);
        String scope = ActivityNotificationTemplateCode.SCOPE;
        int code = ActivityNotificationTemplateCode.ACTIVITY_CANCEL_NO_PAY;
        if (ActivityChargeFlag.CHARGE.getCode().equals(activity.getChargeFlag())) {
            code = ActivityNotificationTemplateCode.ACTIVITY_CANCEL_PAY;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("subject", activity.getSubject());
        final String content = localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), map, "");
        if (activityRosters != null && activityRosters.size() > 0) {
            activityRosters.forEach(r->{
                if (r.getUid().longValue() != userId.longValue()) {
                    sendMessageToUser(r.getUid().longValue(), content, null);
                }
            });
        }
    }

    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        sendMessageToUser(uid, content, meta, MessageBodyType.TEXT.getCode());
    }

    private void sendMessageToUser(Long uid, String content, Map<String, String> meta, String bodyType) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(bodyType);
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }

        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("forum sendMessageToUser " + content + " uid= " + uid);
        }

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    private void deletePostAndActivity(Post post, Long userId){
        //删除克隆帖子  add by yanjun 20170830
        if(post.getCloneFlag() != null && post.getCloneFlag().byteValue() == PostCloneFlag.REAL.getCode()){
            List<Post> listClone= forumProvider.listPostsByRealPostId(post.getId());

            if(listClone != null && listClone.size() >0){
                listClone.forEach(r->{
                    r.setStatus(PostStatus.INACTIVE.getCode());
                    r.setDeleterUid(userId);
                    r.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    this.forumProvider.updatePost(r);

                    this.postSearcher.deleteById(r.getId());

                    //删除活动
                    Activity r_activity = activityProivider.findSnapshotByPostId(r.getId());
                    if(r_activity != null){
                        r_activity.setStatus(PostStatus.INACTIVE.getCode());
                        r_activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        activityProivider.updateActivity(r_activity);
                    }
                });
            }
        }
    }
}
