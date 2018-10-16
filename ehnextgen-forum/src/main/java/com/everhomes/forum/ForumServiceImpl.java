// @formatter:off

package com.everhomes.forum;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.activity.*;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.XssCleaner;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.hotTag.HotTagService;
import com.everhomes.hotTag.HotTag;
import com.everhomes.link.Link;
import com.everhomes.link.LinkProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.organization.*;
import com.everhomes.point.UserPointService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.blacklist.BlacklistErrorCode;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.comment.OwnerTokenDTO;
import com.everhomes.rest.comment.OwnerType;
import com.everhomes.rest.common.ActivityDetailActionData;
import com.everhomes.rest.common.PostDetailActionData;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.forum.admin.PostAdminDTO;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.forum.StickPostCommand;
import com.everhomes.rest.group.*;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.hotTag.*;
import com.everhomes.rest.link.LinkContentType;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.point.PointType;
import com.everhomes.rest.poll.PollItemDTO;
import com.everhomes.rest.poll.PollPostCommand;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.ui.forum.*;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.HotTagSearcher;
import com.everhomes.search.PostAdminQueryFilter;
import com.everhomes.search.PostSearcher;
import com.everhomes.sensitiveWord.SensitiveWordService;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.*;
import net.greghaines.jesque.Job;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ForumServiceImpl implements ForumService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumServiceImpl.class);
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private ActivityProivider activityProivider;

	@Autowired
	JesqueClientFactory jesqueClientFactory;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ActivityProivider activityProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private RegionProvider regionProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private PostSearcher postSearcher;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private UserPointService userPointService;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private HotPostService hotPostService;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private SmsProvider smsProvider;
    
    @Autowired
    private NamespacesProvider namespacesProvider;

    @Value("${server.contextPath:}")
    private String serverContectPath;

    @Autowired
    private HotTagSearcher hotTagSearcher;

    @Autowired
    private HotTagService hotTagService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private LinkProvider linkProvider;

    @Autowired
    private ActivitySignupTimeoutService activitySignupTimeoutService;

    @Override
    public boolean isSystemForum(long forumId, Long communityId) {
        if(forumId == ForumConstants.SYSTEM_FORUM) {
            return true;
        }
        
        // 加入园区后，社区论坛就不只一个，此时小区中配置的default_forum都是社区论坛 by lqs 20160426
        boolean result = false;
        if(communityId != null) {
            Community community = communityProvider.findCommunityById(communityId);
            if(community != null && community.getDefaultForumId() != null) {
                result = (forumId == community.getDefaultForumId());
            }
        }
        
        return result;
    }
    //敏感词检测  --add by yanlong.liang 20180614
    private void filterWords(NewTopicCommand cmd) {
        List<String> textList = new ArrayList<>();
        FilterWordsCommand command = new FilterWordsCommand();
        command.setModuleType(cmd.getModuleType());
        command.setCommunityId(cmd.getCommunityId());
        if (!StringUtils.isEmpty(cmd.getSubject())) {
            textList.add(cmd.getSubject());
        }
        if (!StringUtils.isEmpty(cmd.getContent())) {
            textList.add(cmd.getContent());
        }
        if (cmd.getEmbeddedAppId() != null && cmd.getEmbeddedAppId().equals(AppConstants.APPID_POLL)) {
            PollPostCommand pollPostCommand = (PollPostCommand)StringHelper.fromJsonString(cmd.getEmbeddedJson(),
                    PollPostCommand.class);
            if (pollPostCommand != null) {
                List<PollItemDTO> list = pollPostCommand.getItemList();
                if (!CollectionUtils.isEmpty(list)) {
                    for (PollItemDTO pollItemDTO : list) {
                        if (!StringUtils.isEmpty(pollItemDTO.getSubject())) {
                            textList.add(pollItemDTO.getSubject());
                        }
                    }
                }
            }
        }else{
            ActivityPostCommand activityPostCommand = (ActivityPostCommand) StringHelper.fromJsonString(cmd.getEmbeddedJson(),
                    ActivityPostCommand.class);
            if (activityPostCommand != null) {
                if (!StringUtils.isEmpty(activityPostCommand.getDescription())) {
                    textList.add(activityPostCommand.getDescription());
                }
                if (!StringUtils.isEmpty(activityPostCommand.getContent())) {
                    textList.add(activityPostCommand.getContent());
                }
                if (!StringUtils.isEmpty(activityPostCommand.getLocation())) {
                    textList.add(activityPostCommand.getLocation());
                }
            }
        }
        command.setTextList(textList);
        this.sensitiveWordService.filterWords(command);
    }

    @Override
    public PostDTO createTopic(NewTopicCommand cmd) {
        //xss过滤
        String content = XssCleaner.clean(cmd.getContent());
        cmd.setContent(content);
        //敏感词过滤
        filterWords(cmd);


        if(cmd.getEmbeddedAppId() != null && cmd.getEmbeddedAppId().equals(AppConstants.APPID_ACTIVITY)) {
            if (cmd.getOldId() != null) {
                Post temp = forumProvider.findPostById(cmd.getOldId());
                if (null == temp) {
                    LOGGER.error("post is null.");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "post is null.");
                }
                ActivityPostCommand tempCmd = (ActivityPostCommand) StringHelper.fromJsonString(temp.getEmbeddedJson(),
                        ActivityPostCommand.class);
                if (tempCmd != null && tempCmd.getStatus()!= null && tempCmd.getStatus() == (byte)2) {
                    PostDTO post = this.updateTopic(cmd);
                    return post;
                }
            }
        }
        //全部都加上论坛入口Id为0，现在没办法真的区分不了这个帖子时属于哪个应用模块，活动、论坛、俱乐部、公告  add by yanjun 20171109
        if(cmd.getForumEntryId() == null){
            cmd.setForumEntryId(0L);
        }

        //没有forumId，则设置当前域空间默认的forumId
        if(cmd.getForumId() == null) {
            setNamespaceDefaultForumId(cmd);
        }

        //这个原来只用一行代码的方法终于要发挥他的作用啦。
        PostDTO dto = new PostDTO();

        if(cmd.getVisibleRegionIds() != null && cmd.getVisibleRegionIds().size() == 1){
            cmd.setVisibleRegionId(cmd.getVisibleRegionIds().get(0));
            cmd.setVisibleRegionIds(null);
        }

        //发送到一个特定对象的帖子或者全部范围的帖子
        if(cmd.getVisibleRegionId() != null || cmd.getVisibleRegionType() == VisibleRegionType.ALL.getCode()){
            cmd.setCloneFlag(PostCloneFlag.NORMAL.getCode());
            dto = createTopic(cmd, UserContext.current().getUser().getId());
        }else if(cmd.getVisibleRegionIds() != null && cmd.getVisibleRegionIds().size() > 1){

            //else发送到多个目标的帖子，需要发送一个真身帖，一个全部范围的帖子，以及各个目标的帖子
            cmd.setVisibleRegionId(null);
            cmd.setCloneFlag(PostCloneFlag.REAL.getCode());
            dto = createTopic(cmd, UserContext.current().getUser().getId());

            cmd.setCloneFlag(PostCloneFlag.CLONE.getCode());
            cmd.setRealPostId(dto.getId());

            for(int i= 0; i<cmd.getVisibleRegionIds().size(); i++){
                cmd.setVisibleRegionId(cmd.getVisibleRegionIds().get(i));
                createTopic(cmd, UserContext.current().getUser().getId());
            }

            cmd.setVisibleRegionId(null);
            cmd.setVisibleRegionType(VisibleRegionType.ALL.getCode());
            createTopic(cmd, UserContext.current().getUser().getId());
        }

        //对接积分，论坛、活动、公告、俱乐部等  add by yanjun 20171211
        // createPostEvent(dto);

        final PostDTO tempDto = dto;
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(tempDto.getCreatorUid());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            //add by liangming.huang 20180813
            context.setCommunityId(cmd.getCommunityId());

            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(tempDto.getId());
            Long embeddedAppId = tempDto.getEmbeddedAppId() != null ? tempDto.getEmbeddedAppId() : 0;

            event.setEventName(SystemEvent.FORUM_POST_CREATE.suffix(
                    tempDto.getModuleType(), tempDto.getModuleCategoryId(), embeddedAppId));

            event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
            event.addParam("postDTO", StringHelper.toJsonString(tempDto));
        });

        return dto;
    }

    @Override
    public PostDTO updateTopic(NewTopicCommand cmd) {
        if (null == cmd.getOldId()) {
            LOGGER.error("oldId is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "oldId is null.");
        }
        Post post = this.forumProvider.findPostById(cmd.getOldId());
        if (null == post) {
            LOGGER.error("post is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "post is null.");
        }
        Map map = new HashMap();
        map.put("postName", post.getSubject());

        List<String> changeList = new ArrayList<>();
        if (!StringUtils.isEmpty(cmd.getSubject()) && !cmd.getSubject().equals(post.getSubject())) {
            post.setSubject(cmd.getSubject());
        }
        if (!StringUtils.isEmpty(cmd.getTag()) && !cmd.getTag().equals(post.getTag())) {
            post.setTag(cmd.getTag());
        }
        if (null != post.getStartTime() && cmd.getStartTime() != post.getStartTime().getTime()) {
            Timestamp startTime = new Timestamp(cmd.getStartTime());
            post.setStartTime(startTime);
        }
        if (null != post.getEndTime() && cmd.getEndTime() != post.getEndTime().getTime()) {
            Timestamp endTime = new Timestamp(cmd.getEndTime());
            post.setEndTime(endTime);
        }
        if (cmd.getLongitude() != post.getLongitude() || cmd.getLatitude() != post.getLatitude()) {
            post.setLatitude(cmd.getLatitude());
            post.setLongitude(cmd.getLongitude());
        }
        if (!StringUtils.isEmpty(cmd.getContent()) && !cmd.getContent().equals(post.getContent())) {
            post.setContent(cmd.getContent());
        }
        Activity activity = setActivityPostCommand(post, cmd, changeList);
        this.forumProvider.updatePostAfterPublish(post);

        //更新了活动后，先删除所有的附件，再保存附件
        this.forumProvider.deleteAttachments(post.getId());
        processPostAttachments(UserContext.current().getUser().getId(), cmd.getAttachments(), post);

        //消息推送

        Integer code = getSendMessage(changeList,map,activity);
        List<ActivityRoster> activityRosterList = this.activityProvider.listRosters(activity.getId(),ActivityRosterStatus.NORMAL);
        if (changeList.size() > 0 && activityRosterList.size() > 0) {
            ActivityDetailActionData actionData = new ActivityDetailActionData();
            actionData.setForumId(post.getForumId());
            actionData.setTopicId(post.getId());
            String url =  RouterBuilder.build(Router.ACTIVITY_DETAIL, actionData);
            String subject = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
                    String.valueOf(ActivityLocalStringCode.ACTIVITY_HAVE_CHANGE),
                    UserContext.current().getUser().getLocale(),
                    "Activity Have been Change");
            Map<String, String> meta = createActivityRouterMeta(url, subject);
            for (ActivityRoster activityRoster : activityRosterList) {
                if (activity.getCreatorUid() != null && activity.getCreatorUid().equals(activityRoster.getUid())) {
                    continue;
                }
                sendMessageCode(activityRoster.getUid(), UserContext.current().getUser().getLocale(), map, code, meta);
            }
        }

        return ConvertHelper.convert(post, PostDTO.class);
    }

    private Activity setActivityPostCommand(Post post, NewTopicCommand cmd, List<String> changeList) {
        ActivityPostCommand oldCmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                ActivityPostCommand.class);

        ActivityPostCommand newCmd = (ActivityPostCommand) StringHelper.fromJsonString(cmd.getEmbeddedJson(),
                ActivityPostCommand.class);

        Activity activity = this.activityProvider.findSnapshotByPostId(post.getId());
        if (null == activity) {
            LOGGER.error("activity is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "activity is null.");
        }
        if (!StringUtils.isEmpty(newCmd.getSubject()) && !newCmd.getSubject().equals(oldCmd.getSubject())) {
            oldCmd.setSubject(newCmd.getSubject());
            activity.setSubject(newCmd.getSubject());
            changeList.add("subject");
        }
        if (newCmd.getContentCategoryId() != oldCmd.getContentCategoryId()) {
            oldCmd.setContentCategoryId(newCmd.getContentCategoryId());
            activity.setContentCategoryId(newCmd.getContentCategoryId());
        }
        if (!StringUtils.isEmpty(newCmd.getTag()) && !newCmd.getTag().equals(oldCmd.getTag())) {
            oldCmd.setTag(newCmd.getTag());
            activity.setTag(newCmd.getTag());
        }
        if (!StringUtils.isEmpty(newCmd.getPosterUri()) && !newCmd.getPosterUri().equals(oldCmd.getPosterUri())) {
            oldCmd.setPosterUri(newCmd.getPosterUri());
            activity.setPosterUri(newCmd.getPosterUri());
        }

        if ((!StringUtils.isEmpty(newCmd.getStartTime()) && !newCmd.getStartTime().equals(oldCmd.getStartTime())) ||
                (!StringUtils.isEmpty(newCmd.getEndTime()) && !newCmd.getEndTime().equals(oldCmd.getEndTime()))) {
            oldCmd.setStartTime(newCmd.getStartTime());
            Timestamp startTime = Timestamp.valueOf(newCmd.getStartTime());
            activity.setStartTime(startTime);
            activity.setStartTimeMs(startTime.getTime());

            oldCmd.setEndTime(newCmd.getEndTime());
            Timestamp endTime = Timestamp.valueOf(newCmd.getEndTime());
            activity.setEndTime(endTime);
            activity.setEndTimeMs(endTime.getTime());
            changeList.add("time");
        }
        if (newCmd.getAllDayFlag() != null && newCmd.getAllDayFlag() != oldCmd.getAllDayFlag()) {
            oldCmd.setAllDayFlag(newCmd.getAllDayFlag());
            activity.setAllDayFlag(newCmd.getAllDayFlag());
            if (!changeList.contains("time")) {
                changeList.add("time");
            }
        }
        if (!StringUtils.isEmpty(newCmd.getLocation()) && !newCmd.getLocation().equals(oldCmd.getLocation())) {
            oldCmd.setLatitude(newCmd.getLatitude());
            oldCmd.setLongitude(newCmd.getLongitude());
            oldCmd.setLocation(newCmd.getLocation());
            activity.setLatitude(newCmd.getLatitude());
            activity.setLongitude(newCmd.getLongitude());
            activity.setLocation(newCmd.getLocation());
            changeList.add("address");
        }
        if (!StringUtils.isEmpty(newCmd.getContent()) && !newCmd.getContent().equals(oldCmd.getContent())) {
            oldCmd.setContent(newCmd.getContent());
        }
        if (!StringUtils.isEmpty(newCmd.getDescription()) && !newCmd.getDescription().equals(oldCmd.getDescription())) {
            oldCmd.setDescription(newCmd.getDescription());
            activity.setDescription(newCmd.getDescription());
        }
        if (!StringUtils.isEmpty(newCmd.getContentType()) && !newCmd.getContentType().equals(oldCmd.getContentType())) {
            oldCmd.setContentType(newCmd.getContentType());
            activity.setContentType(newCmd.getContentType());
        }
        post.setEmbeddedJson(StringHelper.toJsonString(oldCmd));
        this.activityProvider.updateActivity(activity);
        return activity;
    }


    private Integer getSendMessage(List changeList, Map map, Activity activity){
        Integer code = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (activity.getAllDayFlag() != null && activity.getAllDayFlag() == (byte)1) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        switch (changeList.size()) {
            case 0 : break;
            case 1 :
            {
                String type = changeList.get(0).toString();
                if ("subject".equals(type)) {
                    map.put("newPostName", activity.getSubject());
                    code = ActivityNotificationTemplateCode.ACTIVITY_CHANGE_SUBJECT;
                }
                if ("time".equals(type)) {
                    map.put("startTime", sdf.format(activity.getStartTime()));
                    map.put("endTime", sdf.format(activity.getEndTime()));
                    code =  ActivityNotificationTemplateCode.ACTIVITY_CHANGE_TIME;
                }
                if ("address".equals(type)) {
                    map.put("address", activity.getLocation());
                    code =  ActivityNotificationTemplateCode.ACTIVITY_CHANGE_ADDRESS;
                }
            };break;
            case 2 :
            {
                String type1 = changeList.get(0).toString();
                String type2 = changeList.get(1).toString();
                if ("subject".equals(type1) && "time".equals(type2)) {
                    map.put("newPostName", activity.getSubject());
                    map.put("startTime", sdf.format(activity.getStartTime()));
                    map.put("endTime", sdf.format(activity.getEndTime()));
                    code = ActivityNotificationTemplateCode.ACTIVITY_CHANGE_SUBJECT_TIME;
                }
                if ("subject".equals(type1) && "address".equals(type2)) {
                    map.put("newPostName", activity.getSubject());
                    map.put("address", activity.getLocation());
                    code =  ActivityNotificationTemplateCode.ACTIVITY_CHANGE_SUBJECT_ADDRESS;
                }
                if ("time".equals(type1) && "address".equals(type2)) {
                    map.put("startTime", sdf.format(activity.getStartTime()));
                    map.put("endTime", sdf.format(activity.getEndTime()));
                    map.put("address", activity.getLocation());
                    code =  ActivityNotificationTemplateCode.ACTIVITY_CHANGE_TIME_ADDRESS;
                }
            };break;
            case 3 :
            {
                map.put("newPostName", activity.getSubject());
                map.put("startTime", sdf.format(activity.getStartTime()));
                map.put("endTime", sdf.format(activity.getEndTime()));
                map.put("address", activity.getLocation());
                code = ActivityNotificationTemplateCode.ACTIVITY_CHANGE_SUBJECT_TIME_ADDRESS;
            };break;
            default:
                break;

        }
        return code;
    }

    private Map<String, String> createActivityRouterMeta(String url, String subject){
        Map<String, String> meta = new HashMap<String, String>();
        RouterMetaObject routerMetaObject = new RouterMetaObject();
        routerMetaObject.setUrl(url);
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, "message.router");
        meta.put(MessageMetaConstant.META_OBJECT, routerMetaObject.toString());
        if(subject != null){
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subject);
        }
        return meta;
    }

    private void sendMessageCode(Long uid, String locale, Map<String, String> map, int code, Map<String, String> meta) {

        String scope = ActivityNotificationTemplateCode.SCOPE;

        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");


        sendMessageToUser(uid, notifyTextForOther, meta);
    }


    /*private void createPostEvent(PostDTO dto){
        //对接积分 add by yanjun 20171211
        String eventName = null;
        switch (ForumModuleType.fromCode(dto.getModuleType())){
            case FORUM:
                eventName = SystemEvent.FORUM_POST_CREATE.suffix(dto.getModuleCategoryId());
                break;
            case ACTIVITY:
                eventName = SystemEvent.ACTIVITY_ACTIVITY_CREATE.suffix(dto.getModuleCategoryId());
                break;
            case ANNOUNCEMENT:
                break;
            case CLUB:
                break;
            case GUILD:
                break;
            case FEEDBACK:
                eventName = SystemEvent.FORUM_POST_CREATE.suffix(dto.getModuleCategoryId());
                break;
        }
        if(eventName == null){
            return;
        }

        final String finalEventName = eventName;

        Long  userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(dto.getId());
            event.setEventName(finalEventName);
        });
    }*/


//
//
//    private void checkVersionForforumEntry(NewTopicCommand cmd){
//
//        if(cmd.getForumEntryId() != null){
//            return;
//        }
//
//        String versionRealm = UserContext.current().getVersionRealm();
//        LOGGER.info("UserContext current getVersion , versionRealm = {}", versionRealm);
//
//        //来自客户端的请求
//        if(versionRealm != null && (versionRealm.contains("Android_") || versionRealm.contains("iOS_"))){
//
//            String version = UserContext.current().getVersion();
//
//            LOGGER.info("UserContext current getVersion , version = {}", version);
//            if(version == null){
//                return;
//            }
//            VersionRange versionRange = new VersionRange("["+version+","+version+")");
//            VersionRange versionRangeMin = new VersionRange("[4.10.4,4.10.4)");
//
//
//            //来自客户端小于4.10.4的版本
//            if(((int)versionRange.getUpperBound()) < ((int)versionRangeMin.getUpperBound())){
//                cmd.setForumEntryId(0L);
//            }
//
//        }
//    }


    @Override
    public PostDTO createTopic(NewTopicCommand cmd, Long creatorUid) {

        //check权限 by sfyan 20170329
        checkCreateTopicPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getContentCategory(), cmd.getCurrentOrgId());

        //黑名单权限校验 by sfyan 20161213
        checkBlacklist(null, null, cmd.getContentCategory(), cmd.getForumId());

        //报名人数限制必须在1到10000之间，add by tt, 20161013
        if (cmd.getEmbeddedAppId() != null && cmd.getEmbeddedAppId().longValue() == AppConstants.APPID_ACTIVITY && cmd.getMaxQuantity()!= null) {
            if (cmd.getMaxQuantity() < 1) {
                throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_QUANTITY_MUST_GREATER_THAN_ZERO,
                        "max quantity should greater than 0!");
            }else if (cmd.getMaxQuantity() > 10000) {
                throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_QUANTITY_MUST_NOT_GREATER_THAN_10000,
                        "max quantity should not greater than 10000");
            }
        }
        //checkForumPostPrivilege(cmd.getForumId());
        long startTime = System.currentTimeMillis();

        // 阻止黑名单用户发帖(临时解决方案)
        this.checkUserBlacklist(creatorUid);
        
        //如果是之前暂存过的帖子，参数中要传oldId，用户删除老数据  add by yanjun 20170515  ----start
        if(cmd.getOldId() != null){
        	this.deletePost(cmd.getForumId(), cmd.getOldId(), true, null, null, null);
        }
        //如果是之前暂存过的帖子，参数中要传oldId，用户删除老数据  add by yanjun 20170515  ----end

        Post post = processTopicCommand(creatorUid, cmd);

        Long embededAppId = cmd.getEmbeddedAppId();
        ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
        if(handler != null) {
            handler.preProcessEmbeddedObject(post);
            forumProvider.createPost(post);
            handler.postProcessEmbeddedObject(post, cmd.getCommunityId());
        } else {
            forumProvider.createPost(post);

            //将tag保存到搜索引擎，此处仅用于普通话题，因为活动和投票已经在自己的postProcessEmbeddedObject中处理
            // add by yanjun 20170613
            feedDocTopicTag(post);
        }


        // Save the attachments after the post is saved
        processPostAttachments(creatorUid, cmd.getAttachments(), post);

        // Populate the result post the same as query
        Long communityId = null;
        if(VisibleRegionType.COMMUNITY == VisibleRegionType.fromCode(cmd.getVisibleRegionType())) {
            communityId = cmd.getVisibleRegionId();
        }
        populatePost(creatorUid, post, communityId, false);

        //暂存的帖子不添加到搜索引擎，到发布的时候添加到搜索引擎，不计算积分    add by yanjun 20170609
        //客户端传来的status可能为空，edit by yanjun
        if(PostStatus.fromCode(post.getStatus()) == PostStatus.ACTIVE || PostStatus.fromCode(post.getStatus()) == null) {
            try {
                postSearcher.feedDoc(post);

                AddUserPointCommand pointCmd = new AddUserPointCommand(creatorUid, PointType.CREATE_TOPIC.name(),
                        userPointService.getItemPoint(PointType.CREATE_TOPIC), creatorUid);
                userPointService.addPoint(pointCmd);
            } catch (Exception e) {
                LOGGER.error("Failed to add post to search engine, userId=" + creatorUid + ", postId=" + post.getId(), e);
            }
        }

        /**
         * 发任务贴的时候 指定发给收消息的人
         */
        if(null != cmd.getEmbeddedAppId() && AppConstants.APPID_ORGTASK == cmd.getEmbeddedAppId()){
        	if(VisibleRegionType.COMMUNITY == VisibleRegionType.fromCode(cmd.getVisibleRegionType())) {
				this.sendTaskMsgToMembers(this.getOrganizationTaskType(cmd.getContentCategory()).getCode(), EntityType.COMMUNITY.getCode(), communityId, post.getSubject());
            }else if(VisibleRegionType.REGION == VisibleRegionType.fromCode(cmd.getVisibleRegionType())){
            	this.sendTaskMsgToMembers(this.getOrganizationTaskType(cmd.getContentCategory()).getCode(), EntityType.ORGANIZATIONS.getCode(), cmd.getVisibleRegionId(), post.getSubject());
            }

        }

        // 发活动的时候判断要不要设置定时任务
        if (null != cmd.getEmbeddedAppId() && AppConstants.APPID_ACTIVITY == cmd.getEmbeddedAppId().longValue()) {
			setActivitySchedule(post.getEmbeddedId());
			//对真身贴或者正常贴且设置了最低人数的帖子设置定时任务
			if (PostCloneFlag.NORMAL.getCode().equals(post.getCloneFlag()) || PostCloneFlag.REAL.getCode().equals(post.getCloneFlag())) {
                if (PostStatus.ACTIVE.getCode() == post.getStatus() && post.getMinQuantity() != null && post.getMinQuantity() >0) {
                    this.activitySignupTimeoutService.pushTimeout(post);
                }
            }
		}

		//公告模块要
		if(ForumModuleType.fromCode(cmd.getModuleType()) == ForumModuleType.ANNOUNCEMENT){

        }


        PostDTO postDto = ConvertHelper.convert(post, PostDTO.class);

        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Create a new post, userId=" + creatorUid + ", postId=" + postDto.getId()
                + ", elapse=" + (endTime - startTime));
        }
        return postDto;
    }

    /**
     * 将tag保存到搜索引擎，此处仅用于普通话题，因为活动和投票已经在自己的postProcessEmbeddedObject中处理  add by yanjun 20170613
     * @param post
     */
    private void feedDocTopicTag(Post post){
        if(post.getEmbeddedAppId()!=null && post.getEmbeddedAppId() == 0L && !StringUtils.isEmpty(post.getTag())){
            try{
                HotTag tag = new HotTag();

                Integer namespaceId = UserContext.getCurrentNamespaceId(post.getNamespaceId());
                tag.setNamespaceId(namespaceId);

                //TODO 业务类型、入口ID
                tag.setServiceType(HotTagServiceType.TOPIC.getCode());

                tag.setName(post.getTag());
                tag.setHotFlag(HotFlag.NORMAL.getCode());

                hotTagSearcher.feedDoc(tag);
            }catch (Exception e){
                LOGGER.error("feedDoc topic tag error",e);
            }
        }
    }
    private void checkUserBlacklist(Long userId) {
        String blackListStr = configProvider.getValue(UserContext.getCurrentNamespaceId(), "createTopic.blacklist", "");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(blackListStr)) {
            UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
            String[] blackListArr = blackListStr.split(",");
            List<String> blackList = Arrays.asList(blackListArr);
            if (blackList.contains(identifier.getIdentifierToken().trim())) {
                LOGGER.error("Black list user create topic, userId = {}", userId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                        ForumServiceErrorCode.ERROR_BLACK_LIST_USER_CREATE_TOPIC, "Sorry, you have disallow to post.");
            }
        }
    }

    private void setActivitySchedule(Long activityId) {
    	Activity activity = activityProivider.findActivityById(activityId);
    	if (activity == null) {
			return;
		}
    	ActivityWarningResponse queryActivityWarningResponse = activityService.queryActivityWarning(new GetActivityWarningCommand(activity.getNamespaceId(), activity.getCategoryId()));
    	//判断如果提醒时间大于当前时间并且要落在使用轮循+定时的方式无法找到区间内才设置提醒
    	if (activity.getStartTime().getTime() - queryActivityWarningResponse.getTime() > new Date().getTime() 
    			&& DateUtils.getCurrentHour().getTime() == DateUtils.formatHour(new Date(activity.getStartTime().getTime() - queryActivityWarningResponse.getTime())).getTime()) {
    		final Job job1 = new Job(
					WarnActivityBeginningAction.class.getName(),
					new Object[] { String.valueOf(activity.getId()) });
			jesqueClientFactory.getClientPool().delayedEnqueue(WarnActivityBeginningAction.QUEUE_NAME, job1,
					activity.getStartTime().getTime() - queryActivityWarningResponse.getTime());
			LOGGER.debug("设置了一个活动提醒："+activity.getId());
		}
	}

	private OrganizationTaskType getOrganizationTaskType(Long contentCategoryId) {
		if(contentCategoryId != null) {
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_NOTICE) {
				return OrganizationTaskType.NOTICE;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_REPAIRS) {
				return OrganizationTaskType.REPAIRS;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_CONSULT_APPEAL) {
				return OrganizationTaskType.CONSULT_APPEAL;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE) {
				return OrganizationTaskType.COMPLAINT_ADVICE;
			}
			
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_CLEANING) {
				return OrganizationTaskType.CLEANING;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_HOUSE_KEEPING) {
				return OrganizationTaskType.HOUSE_KEEPING;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_MAINTENANCE) {
				return OrganizationTaskType.MAINTENANCE;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_EMERGENCY_HELP) {
				return OrganizationTaskType.EMERGENCY_HELP;
			}
		}
		LOGGER.error("Content category is not matched in organization type.contentCategoryId=" + contentCategoryId);
		return null;
	}
    
    private void sendTaskMsgToMembers(String taskType, String ownerType, Long ownerId, String subject){
    	
    	User user = UserContext.current().getUser();
    	Integer namespaceId = UserContext.getCurrentNamespaceId();
    	
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
    	
    	Map<String,Object> map = new LinkedHashMap<String, Object>();
    	
    	map.put("createUName", user.getNickName());
    	map.put("createUToken", userIdentifier.getIdentifierToken());
    	map.put("subject", subject);
    	
    	String msg = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_NEW, user.getLocale(), map, "");
    	
		List<Long> pushUserIds = new ArrayList<Long>();
		List<String> phones = new ArrayList<String>();
    	
    	if(!StringUtils.isEmpty(msg)) {
			String channelType = MessageChannelType.USER.getCode();
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
			messageDto.setBody(msg);
			messageDto.setMetaAppId(AppConstants.APPID_DEFAULT);

			List<OrganizationTaskTarget> targets = organizationProvider.listOrganizationTaskTargetsByOwner(ownerType, ownerId, taskType);
			for (OrganizationTaskTarget target : targets) {
				if(EntityType.fromCode(target.getTargetType()) == EntityType.ORGANIZATIONS){
					Organization org =  organizationProvider.findOrganizationById(target.getId());
					org.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
					List<OrganizationMember> members = organizationProvider.listOrganizationPersonnels(namespaceId,null, org, null, null,new CrossShardListingLocator(), 10000);
					for (OrganizationMember member : members) {
						if(MessageType.fromCode(target.getMessageType()) == MessageType.PUSH){
							if(OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER){
								pushUserIds.add(member.getTargetId());
							}
						}else if(MessageType.fromCode(target.getMessageType()) == MessageType.SMS){
							phones.add(member.getContactToken());
						}
						
						
					}
				}
				
				if(EntityType.fromCode(target.getTargetType()) == EntityType.USER){
					if(MessageType.fromCode(target.getMessageType()) == MessageType.PUSH){
						pushUserIds.add(target.getTargetId());
					}else if(MessageType.fromCode(target.getMessageType()) == MessageType.SMS){
						UserIdentifier userIdentify = userProvider.findClaimedIdentifierByOwnerAndType(target.getTargetId(), IdentifierType.MOBILE.getCode());
						phones.add(userIdentify.getIdentifierToken());
					}
					
				}
				
			}
			
			for (Long userId : pushUserIds) {
				String channelToken = String.valueOf(userId);
				messageDto.setChannels(new MessageChannel(channelType, channelToken));
				messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType,
						channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
			}
			
			List<Tuple<String, Object>> variables = null;
			
			for (String key : map.keySet()) {
				if(null == variables){
					variables = smsProvider.toTupleList(key, map.get(key));
				}else{
					smsProvider.addToTupleList(variables, key, map.get(key));
				}
				
			}
			for (String phone : phones) {
				smsProvider.sendSms(namespaceId, phone, SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_PUSH_MSG_CODE, user.getLocale(), variables);
			}

		}
    	
    	
    }
    
    @Override
    public PostDTO getTopic(GetTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        // 分享出去的帖来查详情时没有userId
        User user = UserContext.current().getUser();
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        Long postId = cmd.getTopicId();
        Post post = checkPostParameter(userId, null, postId, "getTopic");
        cmd.setForumId(post.getForumId());
        if (cmd.getCommunityId() == null) {
            cmd.setCommunityId(post.getCommunityId());
        }

        //先查帖子再查论坛，可能没有forumId  edit by yanjun 20170830
        checkForumParameter(userId, cmd.getForumId(), "getTopic");


        if(post != null) {
            try {
                this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                    // 重新读帖子信息，使得数字准确
                    Post tmpPost = this.forumProvider.findPostById(postId);
                    tmpPost.setViewCount(tmpPost.getViewCount() + 1);
                    this.forumProvider.updatePost(tmpPost);
                    UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(tmpPost.getCreatorUid(),IdentifierType.MOBILE.getCode());
                    String creatorUser = userIdentifier.getIdentifierToken();
                    String[] hotusers = configProvider.getValue(ConfigConstants.HOT_USERS, "").split(",");
                    boolean flag = false;
                    for(String hotuser : hotusers){
                    	if(creatorUser == hotuser || creatorUser.equals(hotuser)){
                    		flag = true;
                    	}
                    }
                    if(Byte.valueOf(PostAssignedFlag.ASSIGNED.getCode()).equals(tmpPost.getAssignedFlag()) || flag){
                    	HotPost hotpost = new HotPost();
                        hotpost.setPostId(postId);
                        hotpost.setTimeStamp(Calendar.getInstance().getTimeInMillis());
                        hotpost.setModifyType(HotPostModifyType.VIEW.getCode());
                        hotPostService.push(hotpost);
                    }
                    
                    return null;
                });
            } catch(Exception e) {
                LOGGER.error("Failed to update the view count of post, userId=" + userId + ", postId=" + postId, e);
            }
            PostDTO postDto =  getTopicById(postId, cmd.getCommunityId(), true, true);

            //填充VisibleRegionIds，用于编辑活动  add by yanjun 20170830
            fillVisibleRegionIds(postDto);

            /*根据客户端的要求 控制任务操作*/
            if(null != postDto && null != postDto.getEmbeddedAppId()){
            	if(postDto.getEmbeddedAppId().equals(AppConstants.APPID_ORGTASK)){
            		OrganizationTask task = organizationProvider.findOrganizationTaskById(postDto.getEmbeddedId());
                	if(null != task){
                		OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getTargetId(), task.getOrganizationId());
        				if(null != member){
        		    		task.setTargetName(member.getContactName());
        		    		task.setTargetToken(member.getContactToken());
        				}
                		task.setOption(cmd.getOption());
                		task.setEntrancePrivilege(cmd.getEntrancePrivilege());
                		postDto.setEmbeddedJson(StringHelper.toJsonString(task));
                	}
            	}
            	
            }
            
            long endTime = System.currentTimeMillis();
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Get topic details, userId=" + userId + ", postId=" + postId 
                    + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
            }
            
            // 如果是活动，返回活动内容的链接，add by tt, 20161013
            if (postDto.getEmbeddedAppId() != null && postDto.getEmbeddedAppId().longValue() == AppConstants.APPID_ACTIVITY) {
				postDto.setContentUrl(getActivityContentUrl(postDto.getId()));
            }
            
            return postDto;
//            post = this.forumProvider.findPostById(postId);
//            this.forumProvider.populatePostAttachments(post);
//            populatePost(userId, post, cmd.getCommunityId(), true);
//            
//            return ConvertHelper.convert(post, PostDTO.class);
        } else {
            LOGGER.error("Forum post not found, userId=" + userId + ", forumId=" + cmd.getForumId()
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }
    }
    
    private String getActivityContentUrl(Long id){
    	Integer namespaceId = UserContext.getCurrentNamespaceId();
    	String homeUrl = configProvider.getValue(namespaceId, ConfigConstants.HOME_URL, "");
		String contentUrl = configProvider.getValue(namespaceId, ConfigConstants.ACTIVITY_CONTENT_URL, "");
		if (homeUrl.length() == 0 || contentUrl.length() == 0) {
			LOGGER.error("Invalid home url or content url, homeUrl=" + homeUrl + ", contentUrl=" + contentUrl);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid home url or content url");
		} else {
			return homeUrl + contentUrl + "?id=" + id;
		}
    }
    
    public List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail) {
        return getTopicById(topicIds, communityId, isDetail, false);
    }
    
    @Override
    public List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail, boolean getByOwnerId) {
        List<PostDTO> postDtoList = new ArrayList<PostDTO>();

        PostDTO postDto = null;
        for(Long topicId : topicIds) {
            try {
                postDto = getTopicById(topicId, communityId, isDetail, getByOwnerId);
                if(postDto != null) 
                	postDtoList.add(postDto);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        return postDtoList;
    }
    
    @Override
    public PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail) {
        return getTopicById(topicId, communityId, isDetail, false);
    }
    
    @Override
    public PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail, boolean getByOwnerId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
      //deal with eh_organization_tasks apply_entity_id = 0 issue modified by xiongying 20160729
//      Post post = checkPostParameter(userId, -1L, topicId, "getTopicById");
        if(topicId != null) {
        	Post post = this.forumProvider.findPostById(topicId);
	        if(post != null) {
	            if(PostStatus.ACTIVE != PostStatus.fromCode(post.getStatus()) && PostStatus.WAITING_FOR_CONFIRMATION != PostStatus.fromCode(post.getStatus())) {
	            	
	            	//查我发的贴&&当前用户=发帖人 && 发帖人=删帖人时 可以看到该帖 modified by xiongying 20160617
	            	if(getByOwnerId && post.getCreatorUid().equals(userId) && post.getCreatorUid().equals(post.getDeleterUid())) {
	            		this.forumProvider.populatePostAttachments(post);
	                    populatePost(userId, post, communityId, isDetail, getByOwnerId);
	                    return ConvertHelper.convert(post, PostDTO.class);
	            	} else {
	            		// 帖子被删除了抛出异常，add by tt, 20170323
	            		LOGGER.error("Forum post already deleted, userId=" + userId + ", topicId=" + topicId);
	            		throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
	                    		ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED, "post was deleted"); 
	            	}
	            }
	                //Added by Janson
	//                if( (!(getByOwnerId && post.getCreatorUid().equals(userId)))
	//                        && (post.getCreatorUid().equals(post.getDeleterUid()) || post.getCreatorUid() != userId.longValue()) ){
	//            		LOGGER.error("Forum post already deleted, userId=" + userId + ", topicId=" + topicId);
	////            		throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
	////            				ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED, "Forum post already deleted");
	//            		return null;
	//            	}
	//            }
	//            
	            this.forumProvider.populatePostAttachments(post);
	            populatePost(userId, post, communityId, isDetail, getByOwnerId);
	            
	            //add favoriteflag of topic and activity is also a topic modified by xiongying 20160629
	            PostDTO dto = ConvertHelper.convert(post, PostDTO.class);
	            if(post.getStartTime() != null){
                    dto.setStartTime(post.getStartTime().getTime());
                }

                if(post.getEndTime() != null){
                    dto.setEndTime(post.getEndTime().getTime());
                }
	            
	            //attachment也要转成dto modified by xiongying 20160920
	            List<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();
        		for(Attachment attachment : post.getAttachments()) {
        			attachments.add(ConvertHelper.convert(attachment, AttachmentDTO.class));
        		}
        		dto.setAttachments(attachments);
        		
	            List<UserFavoriteDTO> favoriteTopic = userActivityProvider.findFavorite(userId, UserFavoriteTargetType.TOPIC.getCode(), post.getId());
	            List<UserFavoriteDTO> favoriteActivity = userActivityProvider.findFavorite(userId, UserFavoriteTargetType.ACTIVITY.getCode(), post.getId());
	            if((favoriteTopic == null || favoriteTopic.size() == 0) && (favoriteActivity == null || favoriteActivity.size() == 0)) {
	            	dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
	            } else {
	            	dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
	            }
	            
	            return dto;
	        } else {
	            LOGGER.error("Forum post not found, userId=" + userId + ", topicId=" + topicId);
	            return null;
	//            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
	//                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
	        }
        } else {
        	LOGGER.error("Forum post id may not be null, operatorId=" + userId );
            return null;
        }
    }

    @Override
    public void deletePost(Long forumId, Long postId) {
        deletePost(forumId, postId, true, null, null, null);
    }

    @Override
    public void deletePost(Long forumId, Long postId, Long currentOrgId, String ownerType, Long ownerId) {
        deletePost(forumId, postId, true, currentOrgId, ownerType, ownerId);
    }

    private void checkDeletePostPrivilege(Long currentOrgId, String ownerType, Long ownerId, Post post){
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if(CategoryConstants.CATEGORY_ID_NOTICE == post.getContentCategory()){
            if(post.getParentPostId() != null && post.getParentPostId() != 0){
                resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.DELETE_NOTIC_COMMENT);
            }else{
                resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.DELETE_NOTIC_TOPIC);
            }
        }else if(CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY == post.getContentCategory()){
            if(post.getParentPostId() != null && post.getParentPostId() != 0){
                resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.DELETE_ACTIVITY_COMMENT1);
            }else{
                resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.DELETE_ACTIVITY_TOPIC1);
            }
        }else{
            if(post.getParentPostId() != null && post.getParentPostId() != 0){
                resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.DELETE_OHTER_COMMENT);
            }else{
                resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.DELETE_OHTER_TOPIC);
            }
        }
    }

    @Override
    public void deletePost(Long forumId, Long postId, boolean deleteUserPost){
        deletePost(forumId, postId, true, null, null, null);
    }

    @Override
    public void deletePost(Long forumId, Long postId, boolean deleteUserPost, Long currentOrgId, String ownerType, Long ownerId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();

        //为了兼容新的统一评论接口，先检查post再检查forum，因为新的统一接口没有传来forumId  add by yanjun 20170601
        Post post = checkPostParameter(userId, forumId, postId, "deletePost");
        if(forumId == null && post!= null){
            forumId = post.getForumId();
        }
        checkForumParameter(userId, forumId, "getTopic");

        Post pPost = null;
        if(post.getParentPostId() != null && post.getParentPostId() != 0) {
            pPost = this.forumProvider.findPostById(post.getParentPostId());
            if(null != pPost){
                post.setContentCategory(pPost.getContentCategory());
                post.setActivityCategoryId(pPost.getActivityCategoryId());
            }
        }
        Post parentPost = pPost;
        //check权限 add sfyan 20170228
        // 后台园区删除帖子或者评论的时候 currentOrgId 或者 ownerId 一定非空， 其他地方调用删除接口，暂时不做处理,代表不需要权限校验
        if((null != ownerType && null != ownerId) || null != currentOrgId){
            //不是自己发的贴或者评论 就要进行权限校验
            if(post.getCreatorUid().longValue() != userId.longValue()){
                checkDeletePostPrivilege(currentOrgId, ownerType, ownerId, post);
            }
        }


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
        
        if(PostStatus.fromCode(post.getStatus()) == PostStatus.ACTIVE) {
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
//                        parentPost.setChildCount(parentPost.getChildCount() - 1);
//                        this.forumProvider.updatePost(parentPost);

                        //更新前需要在锁内部重新查询，因为当前的parentPost可能已经是过期的数据了   edit by yanjun 20171207
                        Post updateParentPost = forumProvider.findPostById(parentPost.getId());
                        if(updateParentPost != null){
                            updateParentPost.setChildCount(updateParentPost.getChildCount() - 1);
                            forumProvider.updatePost(updateParentPost);
                        }
                    }
                    if(deleteUserPost) {
                        if(userId.equals(post.getCreatorUid())){
                            this.userActivityProvider.deletePostedTopic(userId, postId); 
                            userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, -1);
                            }
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
                    //改用新的handler方法，原来的方法TM是创建时候用的，这里是删除 add by yanjun 20171227
//                    handler.postProcessEmbeddedObject(post);
                    handler.afterPostDelete(post);
                } 

                //新增handler.afterPostDelete，搬到里面了
//                //进行退款，取消报名   add by yanjun 20170519
//                if (embededAppId.longValue() == AppConstants.APPID_ACTIVITY) {
//                	Activity activity = activityProivider.findActivityById(post.getEmbeddedId());
//                	if (activity != null) {
//                		List<ActivityRoster> activityRosters = activityProivider.listRosters(activity.getId(), ActivityRosterStatus.NORMAL);
//                		for( int i=0; i< activityRosters.size(); i++){
//                			//如果有退款，先退款再取消订单
//                			ActivityRoster tempRoster = activityRosters.get(i);
//                			if(tempRoster.getStatus() != null && tempRoster.getStatus().byteValue() == ActivityRosterStatus.NORMAL.getCode()){
//                				activityService.signupOrderRefund(activity, tempRoster.getUid());
//
//                				tempRoster.setStatus(ActivityRosterStatus.CANCEL.getCode());
//                				tempRoster.setCancelTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//      			             	activityProvider.updateRoster(tempRoster);
//                			}
//
//                		}
//
//                        activity.setStatus(PostStatus.INACTIVE.getCode());
//                        activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                        activityProvider.updateActivity(activity);
//            		}
//				}

                //删除克隆帖子  add by yanjun 20170830
                deletePostAndActivity(post, userId);
                
            } catch(Exception e) {
                LOGGER.error("Failed to update the post status, userId=" + userId + ", postId=" + postId, e);
            }
        }else if(PostStatus.fromCode(post.getStatus()) == PostStatus.WAITING_FOR_CONFIRMATION) {
        	post.setStatus(PostStatus.INACTIVE.getCode());
            post.setDeleterUid(userId);
            post.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            try {
                this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                    this.forumProvider.updatePost(post);
                    return null;
                });
                this.postSearcher.deleteById(post.getId());
                Activity activity = activityProvider.findSnapshotByPostId(postId);
                if(activity != null){
                	activity.setStatus((byte)0);
            		activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            		activityProvider.updateActivity(activity);
                }

                //删除克隆帖子  add by yanjun 20170830
                deletePostAndActivity(post, userId);
                
            } catch(Exception e) {
                LOGGER.error("Failed to update the post status, userId=" + userId + ", postId=" + postId, e);
            }
        }else {
            //Added by Janson
            if(deleteUserPost) {
                try {
                        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                            if(userId.equals(post.getCreatorUid())){
                                //Try delete the exists inactive post
                                if(this.userActivityProvider.deletePostedTopic(userId, postId) > 0) {
                                    userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, -1);    
                                    }
                                }
                            if(userIds.size() != 0){
                            	for(Long id:userIds){
                            		userActivityProvider.deleteFavorite(id, postId, UserFavoriteTargetType.TOPIC.getCode());
                            		userActivityProvider.deleteFavorite(id, postId, UserFavoriteTargetType.ACTIVITY.getCode());
                            	}
                            }
                            //如果是活动创建者删除活动，通知到已报名的人，add by tt, 20161012
                            if (post.getCreatorUid().longValue() == userId.longValue() && embededAppId.longValue() == AppConstants.APPID_ACTIVITY) {
        						sendMessageWhenCreatorDeleteActivity(post.getEmbeddedId(), userId);
        					}
                        return null;
                        });
                } catch(Exception e) {
                    LOGGER.error("Failed to update the post status, userId=" + userId + ", postId=" + postId, e);
                }
            }
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post is already deleted, userId=" + userId + ", postId=" + postId);
            }
        }

        //删除帖子对接积分  add by yanjun 20171211
        // deletePostPoints(post.getId(), post.getModuleType(), post.getModuleCategoryId());

        final Post tempPost = post;
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(tempPost.getId());
            Long embeddedAppId = tempPost.getEmbeddedAppId() != null ? tempPost.getEmbeddedAppId() : 0;
            event.setEventName(SystemEvent.FORUM_POST_DELETE.suffix(
                    tempPost.getModuleType(), tempPost.getModuleCategoryId(), embeddedAppId));

            event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
            event.addParam("post", StringHelper.toJsonString(tempPost));
            event.addParam("parentPost", StringHelper.toJsonString(parentPost));
        });
    }

    /*private void deletePostPoints(Long postId, Byte moduleType, Long moduleCategoryId){
        String eventName = null;
        switch (ForumModuleType.fromCode(moduleType)){
            case FORUM:
                eventName = SystemEvent.FORUM_POST_DELETE.suffix(moduleCategoryId);
                break;
            case ACTIVITY:
                eventName = SystemEvent.ACTIVITY_ACTIVITY_DELETE.suffix(moduleCategoryId);
                break;
            case ANNOUNCEMENT:
                break;
            case CLUB:
                break;
            case GUILD:
                break;
            case FEEDBACK:
                eventName = SystemEvent.FORUM_POST_DELETE.dft();
                break;
        }
        if(eventName == null){
            return;
        }

        final String finalEventName = eventName;

        Long  userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(postId);
            event.setEventName(finalEventName);
        });
    }*/

    
    private Long getFamilyId() {
        User user = UserContext.current().getUser();
        if (user != null && user.getAddressId() != null) {
            Family family = familyProvider.findFamilyByAddressId(user.getAddressId());
            if (family == null) {
                return null;
            }
            return family.getId();
        }
        return null;
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
                    Activity r_activity = activityProvider.findSnapshotByPostId(r.getId());
                    if(r_activity != null){
                        r_activity.setStatus(PostStatus.INACTIVE.getCode());
                        r_activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        activityProvider.updateActivity(r_activity);
                    }
                });
            }
        }
    }
    //当创建者删除活动时发消息通知已报名的人
    private void sendMessageWhenCreatorDeleteActivity(Long activityId, Long userId){
    	Activity activity = activityProivider.findActivityById(activityId);
    	if (activity == null) {
			return ;
		}
    	List<ActivityRoster> activityRosters = activityProivider.listRosters(activityId, ActivityRosterStatus.NORMAL);
    	String scope = ActivityNotificationTemplateCode.SCOPE;
		int code = ActivityNotificationTemplateCode.CREATOR_DELETE_ACTIVITY;
		Map<String, Object> map = new HashMap<>();
		map.put("tag", activity.getTag());
		map.put("title", activity.getSubject());
		final String content = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
    	activityRosters.forEach(r->{
    		if (r.getUid().longValue() != userId.longValue()) {
    			sendMessageToUser(r.getUid().longValue(), content, null);
			}
    	});
    }
    
    @Override
    public ListPostCommandResponse queryTopicsByEntityAndCategory(QueryTopicByEntityAndCategoryCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        PostEntityTag entityTag = PostEntityTag.fromCode(cmd.getEntityTag());
        ListPostCommandResponse response = null;
        if(entityTag != null) {
            switch(entityTag) {
            case USER:
                QueryTopicByCategoryCommand simpleUserCmd = new QueryTopicByCategoryCommand();
                simpleUserCmd.setForumId(cmd.getForumId());
                simpleUserCmd.setCommunityId(cmd.getCommunityId());
                simpleUserCmd.setEntityTag(cmd.getTargetTag());
                simpleUserCmd.setContentCategory(cmd.getContentCategory());
                simpleUserCmd.setActionCategory(cmd.getActionCategory());
                simpleUserCmd.setPageAnchor(cmd.getPageAnchor());
                simpleUserCmd.setPageSize(cmd.getPageSize());
                response = queryTopicsByCategory(simpleUserCmd);
                break;
            case PM:
            case GARC:
            case GANC:
            case GAPS:
            case GACW:
                QueryOrganizationTopicCommand gaUserCmd = new QueryOrganizationTopicCommand();
                gaUserCmd.setOrganizationId(cmd.getEntityId());
                gaUserCmd.setContentCategory(cmd.getContentCategory());
                gaUserCmd.setActionCategory(cmd.getActionCategory());
                gaUserCmd.setPageAnchor(cmd.getPageAnchor());
                gaUserCmd.setPageSize(cmd.getPageSize());
                response = queryOrganizationTopics(gaUserCmd);
                break;
            default:
                LOGGER.error("Enity tag is not supported, userId=" + userId + ", cmd=" + cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Unsuported entity tag");
            }
        } else {
            LOGGER.error("Enity tag is null, userId=" + userId + ", cmd=" + cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid entity tag");
        }
        
        if(LOGGER.isInfoEnabled()) {
            int size = 0;
            if(response.getPosts() == null) {
                size = response.getPosts().size();
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query topics by entity and category, userId=" + userId + ", size=" + size 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return response;
    }
    
    @Override
    public ListPostCommandResponse queryTopicsByCategory(QueryTopicByCategoryCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        String tag = "queryTopicsByCategory";
        Community community = checkCommunityParameter(userId, communityId, tag);
        
//        Long forumId = ForumConstants.SYSTEM_FORUM;
//        Forum forum = this.forumProvider.findForumById(forumId);
        Forum forum = checkForumParameter(userId, cmd.getForumId(), tag);
        
        PostEntityTag entityTag = PostEntityTag.fromCode(cmd.getEntityTag());
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
//        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
//        if(LOGGER.isInfoEnabled()) {
//            LOGGER.info("Ga regions for query topics by category, userId=" + userId 
//                + ", communityId=" + communityId + ", map=" + gaRegionIdMap);
//        }

        Condition visibilityCondition = buildPostCategoryQryCondition(user, entityTag, community, 
            cmd.getContentCategory(), cmd.getActionCategory());
        
        Condition cond = this.notEqPostCategoryCondition(cmd.getExcludeCategories(), cmd.getEmbeddedAppId(), cmd.getContentCategory());
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
//            query.addConditions(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
//            query.addConditions(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
            
            if(visibilityCondition != null) {
                query.addConditions(visibilityCondition);
            }
            
            if(null != cond){
            	query.addConditions(cond);
            }

            Timestamp timestemp = new Timestamp(System.currentTimeMillis());
            if(TopicPublishStatus.fromCode(cmd.getPublishStatus()) == TopicPublishStatus.UNPUBLISHED){
                query.addConditions(Tables.EH_FORUM_POSTS.START_TIME.gt(timestemp));
            }

            if(TopicPublishStatus.fromCode(cmd.getPublishStatus()) == TopicPublishStatus.PUBLISHED){
                query.addConditions(Tables.EH_FORUM_POSTS.START_TIME.lt(timestemp));
                query.addConditions(Tables.EH_FORUM_POSTS.END_TIME.gt(timestemp));
            }

            if(TopicPublishStatus.fromCode(cmd.getPublishStatus()) == TopicPublishStatus.EXPIRED){
                query.addConditions(Tables.EH_FORUM_POSTS.END_TIME.lt(timestemp));
            }


            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        
        populatePosts(userId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query topics by category, userId=" + userId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    private Condition buildPostCategoryQryCondition(Long userId, PostEntityTag entityTag, Community community, Long contentCategoryId, Long actionCategoryId) {
        Condition categoryCondition = null;
        
        Category contentCatogry = null;
        // contentCategoryId为0表示全部查，此时也不需要给category条件
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
        }
        if(contentCatogry != null) {
            categoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
        }
        
        Category actionCategory = null;
        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
            actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
        }
        if(actionCategory != null) {
            Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
            if(categoryCondition != null) {
                categoryCondition = categoryCondition.and(tempCondition);
            } else {
                categoryCondition = tempCondition;
            }
        }
        
        Condition entityCondition = null;
        if(entityTag != null) {
            // 既没有查询目标，也没有类型，则只查用户相关的帖子
            switch(entityTag) {
            case USER:
                entityCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
                entityCondition = entityCondition.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.USER.getCode()));
                break;
            case PM:
            case GARC:
            case GANC:
            case GAPS:
            case GACW:
                //entityCondition = buildGaRelatedPostQryConditionByCategory(user.getId(), entityTag, community, gaRegionIdMap);
                break;
            default:
                LOGGER.error("Unsupported entity tag for query topic by category, userId=" + userId + ", entityTag=" + entityTag);
                break;
            }
            Condition c2 = Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId);
        }
        
        return null;
    }
    
    @Override
    public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
    	
    	// 非登录用户只能看第一页 add by xiongying20161009
        // 允许用户一直浏览 add by yanlong.liang20180810
//    	if(cmd.getPageAnchor() != null ) {
//    		 if(!userService.isLogon()){
//    			 LOGGER.error("Not logged in.");
//  			   throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
//  					   "Not logged in.");
//
//    		 }
//    	}
    	
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long operatorId = user.getId();
        
        Long forumId = cmd.getForumId();
        Forum forum = checkForumParameter(operatorId, forumId, "listTopics");
        
        // 对创客空间论坛的帖子进行特殊处理：为所有园区的创客空间建一个固定的论坛，通过visible_region_type/id来区分不同园区的帖子；
        // 这样做主要是为了减轻listTopics接口偶合性，使得创客空间的帖子查询条件可以单独定制，对其改动也不影响原来的帖子查询；
        // 由于老版本的创客空间已经调用了该接口，故需要在些插入这段来进行兼容处理 by lqs 20160815
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if(isMakerzoneForum(namespaceId, forumId, cmd.getCommunityId())) {
            return listMakerzoneForumTopics(forum, cmd);
        }
        
        ListPostCommandResponse cmdResponse = null;
        if(isSystemForum(forumId, cmd.getCommunityId())) {
            cmdResponse = listSystemForumTopicsByUser(forum, cmd);
        } else {
            cmdResponse = listSimpleForumTopicsByUser(forum, cmd);
        }
        
        if(LOGGER.isInfoEnabled()) {
            int size = 0;
            if(cmdResponse.getPosts() == null) {
                size = cmdResponse.getPosts().size();
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("List topics, userId=" + operatorId + ", size=" + size 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return cmdResponse;
    }
    
    @Override
    public ListPostCommandResponse listUserRelatedTopics(ListUserRelatedTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(operatorId, communityId, "listUserRelatedTopics");
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
        // Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
        
        // Condition condition = buildPostQryConditionByUserRelated(operator, community, gaRegionIdMap);
        Condition condition = buildPostQryConditionByUserRelated(operator, community);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        
        populatePosts(operatorId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List topics, userId=" + operatorId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    @Override
    public ListPostCommandResponse listActivityPostByCategoryAndTag(ListActivityTopicByCategoryAndTagCommand cmd) {
    	
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(operatorId, communityId, "listActivityPostByCategoryAndTag");
        
        Condition condition = buildActivityPostByCategoryAndTag(operatorId, community, cmd);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });
        
        this.forumProvider.populatePostAttachments(posts);
        
        populatePosts(operatorId, posts, communityId, false);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
        	
        	PostDTO dto = ConvertHelper.convert(r, PostDTO.class);
        	if(r != null && r.getAttachments() != null && r.getAttachments().size() > 0) {
        		List<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();
        		for(Attachment attachment : r.getAttachments()) {
        			attachments.add(ConvertHelper.convert(attachment, AttachmentDTO.class));
        		}
        		dto.setAttachments(attachments);
        	}
          return dto;  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List activity topics by category and tag, userId=" + operatorId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    private Condition buildActivityPostByCategoryAndTag(Long userId, Community community, ListActivityTopicByCategoryAndTagCommand cmd) {
        Condition condition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(community.getDefaultForumId());
        condition = condition.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(AppConstants.APPID_ACTIVITY));
        Category contentCatogry = null;
        Long contentCategoryId = cmd.getCategoryId();
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
        }
        if(contentCatogry != null) {
            condition = condition.and(Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%"));
        }
        
        if(!StringUtils.isEmpty(cmd.getTag())) {
            condition = condition.and(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
        }
        
        return condition;
    }
    
    public CheckUserPostDTO checkUserPostStatus(CheckUserPostCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(operatorId, communityId, "listUserRelatedTopics");
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
        // Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
        
        // Condition condition = buildPostQryConditionByUserRelated(operator, community, gaRegionIdMap);
        Condition condition = buildPostQryConditionByUserRelated(operator, community);
        
        int pageSize = 1; // 只查最新一个
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });
        
        CheckUserPostDTO dto = ConvertHelper.convert(cmd, CheckUserPostDTO.class);
        dto.setStatus(CheckUserPostStatus.NONE.getCode());
        if(posts.size() > 0) {
            Timestamp createTime = posts.get(0).getCreateTime();
            // 客户端指定时间戳时，有新帖超过时间戳则认识为新帖
            if(cmd.getTimestamp() != null) {
                if(createTime != null && cmd.getTimestamp() < createTime.getTime()) {
                    dto.setTimestamp(createTime.getTime());
                    dto.setStatus(CheckUserPostStatus.NEW_POST.getCode());
                }
            } else {
                // 客户端不指定时间戳时，有帖则认为有新帖
                dto.setTimestamp(createTime.getTime());
                dto.setStatus(CheckUserPostStatus.NEW_POST.getCode());
            }
        }
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Check the user post status, operatorId=" + operatorId + ", elapse=" + (endTime - startTime) + ", result=" + dto);
        }
        
        return dto;
    }
    
    
    public ListPostCommandResponse listOrgTopics(QueryOrganizationTopicCommand cmd){
    	 long startTime = System.currentTimeMillis();
    	 User operator = UserContext.current().getUser();
    	 Long operatorId = operator.getId();
    	 Long organizationId = cmd.getOrganizationId();
         Long communityId = cmd.getCommunityId();
        Integer namespaceId = cmd.getNamespaceId();
        if(namespaceId == null){
            namespaceId = UserContext.getCurrentNamespaceId();
        }
         List<Long> forumIds = new ArrayList<Long>();
         //Organization organization = checkOrganizationParameter(operatorId, organizationId, "listOrganizationTopics");
         List<Long> communityIdList = new ArrayList<Long>();

         //获取园区id和论坛Id,并返回orgId，因为当查询域空间时需要orgid来查发送到“全部”的帖子 edit by yanjun 20170830
         organizationId = populateCommunityIdAndForumId(communityId, organizationId, namespaceId, communityIdList, forumIds);

         //重复了，去重
         Set forumIdset = new HashSet();
        forumIdset.addAll(forumIds);
        forumIdset.remove(null);
        forumIds = new ArrayList<Long>();
        forumIds.addAll(forumIdset);

         if(null != cmd.getEmbeddedAppId() && cmd.getEmbeddedAppId().longValue() == AppConstants.APPID_ACTIVITY) {
        	 ListActivitiesReponse response = activityService.listOfficialActivities(cmd);
        	 
        	 ListPostCommandResponse postResponse = new ListPostCommandResponse();
        	 if(null != response) {
        		 if(response.getNextPageAnchor() != null) {
        			 postResponse.setNextPageAnchor(response.getNextPageAnchor());
        		 }
        		
        		 if(response.getActivities() != null) {
        			 List<Post> posts = response.getActivities().stream().map((activity) -> {
        				 Post post = forumProvider.findPostById(activity.getPostId());
        				 return post;
        			 }).collect(Collectors.toList());
        			 
        			 this.forumProvider.populatePostAttachments(posts);
        			 
        			 populatePosts(operatorId, posts, communityId, false);
         	        
         	        List<PostDTO> postDtoList = posts.stream().map((r) -> {
         	          PostDTO dto = ConvertHelper.convert(r, PostDTO.class);
         	          //填充VisibleRegionIds，用于编辑活动  add by yanjun 20170830
                      fillVisibleRegionIds(dto);
         	          return dto;
         	        }).collect(Collectors.toList());
         	        
         	       postResponse.setPosts(postDtoList);
        		 }
        	 }
        	  return postResponse;
         }
         else {
	         Condition privateCond = null;
	         if(PostPrivacy.PRIVATE == PostPrivacy.fromCode(cmd.getPrivateFlag())){
	        	 Condition creatorCondition = Tables.EH_FORUM_POSTS.CREATOR_UID.eq(operator.getId());
	             
	             // 只有公开的帖子才能查到
	        	 privateCond = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.notEqual(PostPrivacy.PRIVATE.getCode());
	        	 privateCond = creatorCondition.or(privateCond);
	         }
	         
	        
	         
	         Condition unCateGoryCondition = notEqPostCategoryCondition(cmd.getExcludeCategories(), cmd.getEmbeddedAppId(), cmd.getContentCategory());

             //             范围                帖子
//
//             一个园区            community + normal
//             多个园区            all + clone,    community + real,   多个community + clone
//             一个公司            organization + normal
//             全部                all + normal
	         Condition communityCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
	         communityCondition = communityCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(communityIdList));
//             communityCondition = communityCondition.and(Tables.EH_FORUM_POSTS.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode()));

	         Condition regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
	         regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(organizationId));
             regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode()));


	         Condition condition = communityCondition
                     .or(regionCondition)
                     .or(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode())
                             .and(Tables.EH_FORUM_POSTS.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode())))
                     .and(Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIds));

	         if(null != cmd.getEmbeddedAppId()){
	        	 condition = condition.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(cmd.getEmbeddedAppId()));
	        	 //如果是活动且查询官方活动，则加上官方活动条件
	//        	 if (cmd.getEmbeddedAppId().longValue() == AppConstants.APPID_ACTIVITY && OfficialFlag.fromCode(cmd.getOfficialFlag())==OfficialFlag.YES) {
	//				condition = condition.and(Tables.EH_FORUM_POSTS.OFFICIAL_FLAG.eq(OfficialFlag.YES.getCode()));
	//        	 }
	        	 // 如果officialFlag传了值，按传的值算，如果没传值则查所有，updated by tt, 20161017
	        	 if (cmd.getEmbeddedAppId().longValue() == AppConstants.APPID_ACTIVITY && OfficialFlag.fromCode(cmd.getOfficialFlag())!=null) {
	        		 condition = condition.and(Tables.EH_FORUM_POSTS.OFFICIAL_FLAG.eq(cmd.getOfficialFlag()));
	        	 }
	         }
	         if(null != unCateGoryCondition){
	        	 condition = condition.and(unCateGoryCondition);
	         }
	         
	         if(null != cmd.getContentCategory()){
	        	 Category contentCatogry = this.categoryProvider.findCategoryById(cmd.getContentCategory());
	        	 condition = condition.and(Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%"));
	         }
	         
	         int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
	         CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
	         locator.setAnchor(cmd.getPageAnchor());
	         
	         if(null != privateCond){
	        	 condition = condition.and(privateCond);
	         }

             //支持按话题、活动、投票来查询数据   add by yanjun 20170612
             if(cmd.getCategoryId() != null){
	             //1和1001都是话题，老客户端传的1web传的1001，新客户端会改成1001，很久很久以后可以刷一遍数据改掉这里的代码  add by yanjun 20171221
	             if(CategoryConstants.CATEGORY_ID_TOPIC == cmd.getCategoryId().longValue() || CategoryConstants.CATEGORY_ID_TOPIC_COMMON == cmd.getCategoryId().longValue()){
                     condition = condition.and(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC)
                             .or(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC_COMMON)));
                 }else {
                     condition = condition.and(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(cmd.getCategoryId()));
                 }
             }

             //支持标签搜索  add by yanjun 20170712
             if(!StringUtils.isEmpty(cmd.getTag())){
                 condition = condition.and(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
             }

             //论坛多入口，老客户端没有这个参数，默认入口为0  add by yanjun 20171025
             if(StringUtils.isEmpty(cmd.getForumEntryId())){
                 condition = condition.and(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(0L));
             }else {
                 condition = condition.and(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(cmd.getForumEntryId()));
             }

	         List<PostDTO> dtos = this.getOrgTopics(locator, pageSize, condition, cmd.getPublishStatus(), cmd.getNeedTemporary());
	    	 if(LOGGER.isInfoEnabled()) {
	             long endTime = System.currentTimeMillis();
	             LOGGER.info("Query organization topics, userId=" + operatorId + ", size=" + dtos.size() 
	                 + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
	         }   
	    	 return new ListPostCommandResponse(locator.getAnchor(), dtos); 
	    }
    }
    @Override
    public Long populateCommunityIdAndForumId(Long communityId, Long organizationId, Integer namespaceId, List<Long> communityIds, List<Long> forumIds){
        if(communityId != null){
            Community community = communityProvider.findCommunityById(communityId);
            communityIds.add(community.getId());
            forumIds.add(community.getDefaultForumId());
        }else if(organizationId != null){
            // 如果发送范围选择的公司圈，需要加上公司的论坛，add by tt, 20170307
            Organization organization = organizationProvider.findOrganizationById(organizationId);
            if (organization.getGroupId() != null) {
                Group group = groupProvider.findGroupById(organization.getGroupId());
                if (group != null) {
                    forumIds.add(group.getOwningForumId());
                }
            }


            // 因为左邻管理后台的原因
            // 左邻0域空间只传一个organizationId则只查organization和organization所在园区，
            // 其他情况下沿用原来的逻辑查organization和底下管理的园区   add by yanjun 20170911
            Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
            if(currentNamespaceId != null && currentNamespaceId == 0){
                OrganizationCommunityRequest OrganizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organizationId);
                if(OrganizationCommunityRequest != null){
                    communityIds.add(OrganizationCommunityRequest.getCommunityId());
                    Community community = communityProvider.findCommunityById(OrganizationCommunityRequest.getCommunityId());
                    if(community != null){
                        forumIds.add(community.getDefaultForumId());
                    }
                }
            }else {
                List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organization.getId());
                if(null != communities){
                    for (CommunityDTO communityDTO : communities) {
                        communityIds.add(communityDTO.getId());
                        forumIds.add(communityDTO.getDefaultForumId());
                    }
                }
            }

        }else if(namespaceId != null && namespaceId != 0){
            //0域空间有25万园区，直接会把系统搞挂了  add by yanjun 20170906
            ListingLocator locator = new CrossShardListingLocator();
            locator.setAnchor(null);
            List<Community> communities = communityProvider.listCommunitiesByKeyWord(locator, 1000000, null, namespaceId, null);
            if(null != communities){
                for (Community community : communities) {
                    communityIds.add(community.getId());
                    forumIds.add(community.getDefaultForumId());

                    // 如果发送范围选择的公司圈，需要加上管理公司的论坛
                    GetOrgDetailCommand getOrgDetailCommand = new GetOrgDetailCommand();
                    getOrgDetailCommand.setCommunityId(community.getId());
                    getOrgDetailCommand.setOrganizationType(PostEntityTag.PM.getCode());
                    OrganizationDTO organization = organizationService.getOrganizationByComunityidAndOrgType(getOrgDetailCommand);
                    if (organization != null && organization.getGroupId() != null) {
                        //加上发送到全部的
                        organizationId = organization.getId();

                        Group group = groupProvider.findGroupById(organization.getGroupId());
                        if (group != null) {
                            forumIds.add(group.getOwningForumId());
                        }
                    }
                }
            }
        }
        return organizationId;
    }

    /**
     * 独立出一个方法来专门查询官方活动，以简化帖子条件的查询条件；而原来使用listOrgTopics(QueryOrganizationTopicCommand cmd)
     * 存在着BUG：当一个机构没有管理小区或者以普通机构的身份访问时会查不到帖子；
     */
    // 这个方法没有地方引用，删除接口中此方法并改为私有方法, add by tt, 20160307
    private ListPostCommandResponse listOfficialActivityTopics(QueryOrganizationTopicCommand cmd){
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long organizationId = cmd.getOrganizationId();
        Long communityId = cmd.getCommunityId();
        List<Long> forumIds = new ArrayList<Long>();
        
        List<Long> communityIdList = new ArrayList<Long>();
        // 获取所管理的所有小区对应的社区论坛
        if(organizationId != null) {
            ListCommunitiesByOrganizationIdCommand command = new ListCommunitiesByOrganizationIdCommand();
            command.setOrganizationId(organizationId);
            List<CommunityDTO> communities = organizationService.listCommunityByOrganizationId(command).getCommunities();
            if(communities != null){
                for (CommunityDTO communityDTO : communities) {
                    communityIdList.add(communityDTO.getId());
                    forumIds.add(communityDTO.getDefaultForumId());
                }
            }
        }
        // 办公地点所在园区对应的社区论坛
        if(communityId != null) {
            Community community = communityProvider.findCommunityById(communityId);
            communityIdList.add(community.getId());
            forumIds.add(community.getDefaultForumId());
        }
        
        // 当论坛list为空时，JOOQ的IN语句会变成1=0，导致条件永远不成立，也就查不到东西
        if(forumIds.size() == 0) {
            LOGGER.error("Forum not found for offical activities, cmd={}", cmd);
            return null;
        }
        
        Condition forumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIds);
        
        // 可见性条件：如果有当前小区/园区，则加上小区条件；如果有对应的管理机构，则加上机构条件；这两个条件为或的关系；
        Condition communityCondition = null;
        if(communityId != null) {
            communityCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            communityCondition = communityCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
        }
        Condition orgCondition = null;
        if(organizationId != null) {
            orgCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            orgCondition = orgCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(organizationId));
        }
        Condition visibleCondition = communityCondition;
        if(visibleCondition == null) {
            visibleCondition = orgCondition;
        } else {
        	if (orgCondition != null) {
        		visibleCondition = visibleCondition.or(orgCondition);
			}
        }
        
        Condition condition = forumCondition;
        if(visibleCondition != null) {
            condition = condition.and(visibleCondition);
        }
        condition = condition.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(AppConstants.APPID_ACTIVITY));
        condition = condition.and(Tables.EH_FORUM_POSTS.OFFICIAL_FLAG.eq(OfficialFlag.YES.getCode()));
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        // TODO: Locator里设置系统论坛ID存在着分区的风险，因为上面的条件是多个论坛，需要后面理顺  by lqs 20160730
        CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(cmd.getPageAnchor());
        
        List<PostDTO> dtos = this.getOrgTopics(locator, pageSize, condition, cmd.getPublishStatus(), null);
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query offical activity topics, userId=" + operatorId + ", size=" + dtos.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }   
        return new ListPostCommandResponse(locator.getAnchor(), dtos); 
   }
    
    
    public ListPostCommandResponse queryOrganizationTopics(QueryOrganizationTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator == null ? 0 : operator.getId();
        Long organizationId = cmd.getOrganizationId();
        Long communityId = cmd.getCommunityId();
        final Long forumId = cmd.getForumId();
        Organization organization = checkOrganizationParameter(operatorId, organizationId, "listOrganizationTopics");
        List<Long> communityIdList = new ArrayList<Long>();
        if(null == communityId){
        	ListCommunitiesByOrganizationIdCommand command = new ListCommunitiesByOrganizationIdCommand();
        	command.setOrganizationId(organization.getId());
        	List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organization.getId());
        	if(null != communities){
        		for (CommunityDTO communityDTO : communities) {
        			communityIdList.add(communityDTO.getId());
				}
        	}
        }else{
        	communityIdList.add(communityId);
        }
        if(communityIdList.size() == 0) {
            LOGGER.error("Organization community is not found, operatorId=" + operatorId + ", organizationId=" + organizationId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_ORGANIZATION_COMMUNITY_NOT_FOUND, "Organization community not found");
        }
        
        List<Long> categorys = new ArrayList<Long>();
        
        //modify contentCategory not for null by sfyan 20160430
        if(null != cmd.getContentCategory()){
        	categorys.add(cmd.getContentCategory());
        }
        
        if(null != cmd.getEmbeddedAppId()){
        	if(AppConstants.APPID_ORGTASK == cmd.getEmbeddedAppId() && null == cmd.getContentCategory()){
        		categorys = CategoryConstants.GA_PRIVACY_CATEGORIES;
        	}
        	
        }
        Condition condition = null;
        
        if(null != cmd.getEmbeddedAppId()){
        	condition = Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(cmd.getEmbeddedAppId());
        }
        
        
        Condition visibilityCondition = buildGaRelatedPostQryConditionByOrganization(operatorId, organization, communityIdList);

        Long actionCategoryId = cmd.getActionCategory();
        
        Condition categoryCondition = buildPostCategoryCondition(categorys, actionCategoryId);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(cmd.getPageAnchor());
        
        if(null != condition){
        	if(null != visibilityCondition)
        		condition = condition.and(visibilityCondition);
        	if(null != categoryCondition)
        		condition = condition.and(categoryCondition);
        }else{
        	if(null != visibilityCondition)
        		condition = visibilityCondition;
        	if(null == condition){
        		condition = categoryCondition;
        	}else if(null != categoryCondition){
        		condition = condition.and(categoryCondition);
        	}
        }
        
        Condition cond = this.notEqPostCategoryCondition(cmd.getExcludeCategories(), cmd.getEmbeddedAppId(), cmd.getContentCategory());
        
        if(null != cond){
        	condition = condition.and(cond);
        }
        List<PostDTO> dtos = this.getOrgTopics(locator, pageSize, condition, cmd.getPublishStatus(), cmd.getNeedTemporary());
        
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query organization topics, userId=" + operatorId + ", size=" + dtos.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }        
        
        return new ListPostCommandResponse(locator.getAnchor(), dtos);
    }
    
    private List<PostDTO> getOrgTopics(CrossShardListingLocator locator,Integer pageSize, Condition condition, String publishStatus, Byte needTemporary ){
    	User user = UserContext.current().getUser();
        Long userId = user.getId();
    	
    	Timestamp timestemp = new Timestamp(DateHelper.currentGMTTime().getTime());
        
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));

//            //新增暂存活动，后台管理员在web端要看到暂存的活动  add by yanjun 20170518
//            if(needTemporary != null && needTemporary.byteValue() == 1){
//            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.in(PostStatus.ACTIVE.getCode(), PostStatus.WAITING_FOR_CONFIRMATION.getCode()));
//            }else{
//            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
//            }

            Condition temporaryCondition = getTemporaryCondition(needTemporary);
            if(temporaryCondition != null){
                query.addConditions(temporaryCondition);
            }

            
            if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.UNPUBLISHED){
            	query.addConditions(Tables.EH_FORUM_POSTS.START_TIME.gt(timestemp));
            }
            
            if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.PUBLISHED){
            	query.addConditions(Tables.EH_FORUM_POSTS.START_TIME.lt(timestemp));
            	query.addConditions(Tables.EH_FORUM_POSTS.END_TIME.gt(timestemp));
            }
            
            if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.EXPIRED){
            	query.addConditions(Tables.EH_FORUM_POSTS.END_TIME.lt(timestemp));
            }

            if (TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.PUBLISHING_AND_EXPIRED) {
                query.addConditions(Tables.EH_FORUM_POSTS.START_TIME.lt(timestemp));
            }
            
            
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });

        // 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170807
        populateRealPost(posts);


        this.forumProvider.populatePostAttachments(posts);

        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            Long nextpageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
            locator.setAnchor(nextpageAnchor);
        }else {
            locator.setAnchor(null);
        }
        
        populatePosts(user.getId(), posts, null, false);
        
        return posts.stream().map((r) -> {
        	Timestamp s = r.getStartTime();
        	Timestamp e = r.getEndTime();
        	PostDTO dto= ConvertHelper.convert(r, PostDTO.class);

            //填充VisibleRegionIds，用于编辑活动  add by yanjun 20170830
            fillVisibleRegionIds(dto);

        	if(null != s && null != e){
        		dto.setStartTime(s.getTime());
            	dto.setEndTime(e.getTime());
        		if(s.getTime() > timestemp.getTime()){
        			dto.setPublishStatus(TopicPublishStatus.UNPUBLISHED.getCode());
        		}
        		
        		if(s.getTime() < timestemp.getTime() && e.getTime() > timestemp.getTime()){
        			dto.setPublishStatus(TopicPublishStatus.PUBLISHED.getCode());
        		}
        		
        		if(e.getTime() < timestemp.getTime()){
        			dto.setPublishStatus(TopicPublishStatus.EXPIRED.getCode());
        		}
        	}
        	
        	//添加用户是否收藏，add by tt, 20160928
        	List<UserFavoriteDTO> favoriteTopic = userActivityProvider.findFavorite(userId, UserFavoriteTargetType.TOPIC.getCode(), r.getId());
            List<UserFavoriteDTO> favoriteActivity = userActivityProvider.findFavorite(userId, UserFavoriteTargetType.ACTIVITY.getCode(), r.getId());
            if((favoriteTopic == null || favoriteTopic.size() == 0) && (favoriteActivity == null || favoriteActivity.size() == 0)) {
            	dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            } else {
            	dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            }
            
          return dto;  
        }).collect(Collectors.toList());
    }


    private Condition getTemporaryCondition(Byte needTemporary){

        Condition condition = null;
        //新增暂存活动，后台管理员在web端要看到暂存的活动 add by yanjun 20170513
        if(needTemporary == null || needTemporary.byteValue() == NeedTemporaryType.PUBLISH.getCode()){
            condition = Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode());
        }else if(needTemporary.byteValue() == NeedTemporaryType.ALL.getCode()){
            condition = Tables.EH_FORUM_POSTS.STATUS.in(PostStatus.ACTIVE.getCode(), PostStatus.WAITING_FOR_CONFIRMATION.getCode());
        }else if(needTemporary.byteValue() == NeedTemporaryType.TEMPORARY.getCode()){
            condition = Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.WAITING_FOR_CONFIRMATION.getCode());
        }
        return  condition;
    }

    private void fillVisibleRegionIds(PostDTO dto){
        if(dto == null){
            return;
        }

        List<Long> visibleRegionIds = new ArrayList<>();
        if(dto.getCloneFlag() != null && dto.getCloneFlag().byteValue() == PostCloneFlag.REAL.getCode()){
            List<Post> list= forumProvider.listPostsByRealPostId(dto.getId());
            if (list != null && list.size() >0){
                list.forEach(r->{
                    if(r.getVisibleRegionId() != null){
                        visibleRegionIds.add(r.getVisibleRegionId());
                    }
                });
            }
        }else if(dto.getVisibleRegionId() != null){
            visibleRegionIds.add(dto.getVisibleRegionId());
        }

        if(visibleRegionIds.size() > 0){
            dto.setVisibleRegionIds(visibleRegionIds);
        }
    }

    @Override
    public void likeTopic(LikeTopicCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "likeTopic";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long topicId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, topicId, tag);
        
        try {
            Tuple<Post, Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(() -> {
                this.forumProvider.likePost(operatorId, topicId);
                Post tmpPost = this.forumProvider.findPostById(topicId);
                String creatorUid = Long.toString(tmpPost.getCreatorUid());
                String[] hotusers = configProvider.getValue(ConfigConstants.HOT_USERS, "").split(",");
                boolean flag = false;
                for (String hotuser : hotusers) {
                    if (creatorUid == hotuser) {
                        flag = true;
                    }
                }
                if (Byte.valueOf(PostAssignedFlag.ASSIGNED.getCode()).equals(tmpPost.getAssignedFlag()) || flag) {
                    HotPost hotpost = new HotPost();
                    hotpost.setPostId(topicId);
                    hotpost.setTimeStamp(Calendar.getInstance().getTimeInMillis());
                    hotpost.setModifyType(HotPostModifyType.LIKE.getCode());
                    hotPostService.push(hotpost);
                }
                return tmpPost;
            });

            final Post tempPost = tuple.first();
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setUid(UserContext.currentUserId());
                context.setNamespaceId(UserContext.getCurrentNamespaceId());
                event.setContext(context);

                event.setEntityType(EhForumPosts.class.getSimpleName());
                event.setEntityId(tempPost.getId());
                Long embeddedAppId = tempPost.getEmbeddedAppId() != null ? tempPost.getEmbeddedAppId() : 0;
                event.setEventName(SystemEvent.FORUM_POST_LIKE.suffix(
                        tempPost.getModuleType(), tempPost.getModuleCategoryId(), embeddedAppId));

                event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
                event.addParam("post", StringHelper.toJsonString(tempPost));
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the like count of post, userId=" + operatorId + ", topicId=" + topicId, e);
        }

        //点赞对接积分  add by yanjun 20171211
        // likeTopicEvent(topicId);
    }


    /*private void likeTopicEvent(Long postId) {
        final Post post = this.forumProvider.findPostById(postId);
        String eventName = null;
        switch (ForumModuleType.fromCode(post.getModuleType())) {
            case FORUM:
                eventName = SystemEvent.FORUM_POST_LIKE.suffix(post.getModuleCategoryId());
                break;
            case ACTIVITY:
                eventName = SystemEvent.ACTIVITY_ACTIVITY_LIKE.suffix(post.getModuleCategoryId());
                break;
            case ANNOUNCEMENT:
                break;
            case CLUB:
                break;
            case GUILD:
                break;
            case FEEDBACK:
                break;
        }
        if(eventName == null){
            return;
        }

        final String finalEventName = eventName;

        Long  userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(post.getId());
            event.setEventName(finalEventName);
        });
    }*/


    @Override
    public void cancelLikeTopic(CancelLikeTopicCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "cancelLikeTopic";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long topicId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, topicId, tag);
        
        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                this.forumProvider.cancelLikePost(operatorId, topicId);
               return null;
            });

            final Post tempPost = this.forumProvider.findPostById(topicId);
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setUid(UserContext.currentUserId());
                context.setNamespaceId(UserContext.getCurrentNamespaceId());
                event.setContext(context);

                event.setEntityType(EhForumPosts.class.getSimpleName());
                event.setEntityId(tempPost.getId());
                Long embeddedAppId = tempPost.getEmbeddedAppId() != null ? tempPost.getEmbeddedAppId() : 0;
                event.setEventName(SystemEvent.FORUM_POST_LIKE_CANCEL.suffix(
                        tempPost.getModuleType(), tempPost.getModuleCategoryId(), embeddedAppId));

                event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
                event.addParam("post", StringHelper.toJsonString(tempPost));
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the dislike count of post, userId=" + operatorId + ", topicId=" + topicId, e);
        }
        //取消点赞对接积分 add by yanjun 20171211
        // cancelLikeTopicPoints(forumId);
    }

    /*private void cancelLikeTopicPoints(Long postId){

        final Post post = this.forumProvider.findPostById(postId);

        String eventName = null;
        switch (ForumModuleType.fromCode(post.getModuleType())){
            case FORUM:
                eventName = SystemEvent.FORUM_POST_LIKE_CANCEL.suffix(post.getModuleCategoryId());
                break;
            case ACTIVITY:
                eventName = SystemEvent.ACTIVITY_ACTIVITY_LIKE_CANCEL.suffix(post.getModuleCategoryId());
                break;
            case ANNOUNCEMENT:
                break;
            case CLUB:
                break;
            case GUILD:
                break;
            case FEEDBACK:
                break;
        }
        if(eventName == null){
            return;
        }

        final String finalEventName = eventName;

        Long  userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(post.getId());
            event.setEventName(finalEventName);
        });
    }*/

    @Override
    public void stickPost(StickPostCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();

        checkStickPostPrivilege(operatorId, cmd.getOrganizationId(), cmd.getPostId());
        checkStickPostParameter(operatorId, cmd.getPostId(), cmd.getStickFlag());

        List<Long> postIds = new ArrayList<>();
        postIds.add(cmd.getPostId());

        List<Post> posts = forumProvider.listPostsByRealPostId(cmd.getPostId());
        if(posts != null && posts.size() > 0){
            for (Post post: posts){
                postIds.add(post.getId());
            }
        }


        dbProvider.execute((status) -> {

            for (Long postId: postIds){
                Post post = this.forumProvider.findPostById(postId);

                post.setStickFlag(cmd.getStickFlag());

                if(StickFlag.fromCode(cmd.getStickFlag()) == StickFlag.TOP){
                    post.setStickTime(new Timestamp(System.currentTimeMillis()));
                }else {
                    post.setStickTime(null);
                }

                forumProvider.updatePost(post);
                if(post.getEmbeddedAppId() != null &&  post.getEmbeddedAppId().longValue() == AppConstants.APPID_ACTIVITY){
                    Activity activity = activityProvider.findSnapshotByPostId(postId);
                    activity.setStickFlag(cmd.getStickFlag());

                    if(StickFlag.fromCode(cmd.getStickFlag()) == StickFlag.TOP){
                        activity.setStickTime(new Timestamp(System.currentTimeMillis()));
                    }else {
                        activity.setStickTime(null);
                    }

                    activityProivider.updateActivity(activity);
                }
            }

            return null;
        });
    }

    private void checkStickPostPrivilege(Long operatorId, Long organizationId, Long postId) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");

        //检查园区企业管理员
        if(resolver.checkSuperAdmin(operatorId, organizationId)){
            return;
        }

        //检查该用户是否是该该帖子所在群组的创建者。
        boolean result = checkGroupCreater(operatorId, postId);
        if(result){
            return;
        }

        //检查超级管理员，此处不成立会报错
        resolver.checkUserPrivilege(operatorId, 0);

    }


    private boolean checkGroupCreater(Long userId, Long postId){

        boolean result = false;

        Post postById = forumProvider.findPostById(postId);
        Group group = null;
        if(postById != null){
            group = groupService.findGroupByForumId(postById.getForumId());
        }

        if(group != null && userId != null && userId.equals(group.getCreatorUid())){
            result = true;
        }
        return result;
    }

    private Post checkStickPostParameter(Long operatorId, Long postId, Byte stickFlag) {
        if(postId == null || StickFlag.fromCode(stickFlag) == null) {
            LOGGER.error("invalid parameter, postId or stickFlag is invalid, operatorId={}, postId={}, stickFlag={}", operatorId, postId, stickFlag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter, postId or stickFlag is null");
        }

        Post post = this.forumProvider.findPostById(postId);
        if(post == null) {
            LOGGER.error("Forum post not found, operatorId={}, postId={}, stickFlag={}", operatorId, postId, stickFlag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }

        return post;
    }

    @Override
    public ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd) {
    	// 非登录用户只能看第一页 add by xiongying20161009
    	if(cmd.getPageAnchor() != null ) {
    		 if(!userService.isLogon()){
    			 LOGGER.error("Not logged in.");
  			   throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
  					   "Not logged in.");

    		 }
    	}
    	
    	
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "listTopicComments";

        //为了兼容新的统一评论接口，先检查post再检查forum，因为新的统一接口没有传来forumId  add by yanjun 20170601
        Long topicId = cmd.getTopicId();
        Post post = checkPostParameter(operatorId, null, topicId, tag);
        if(post != null && cmd.getForumId() == null){
            cmd.setForumId(post.getForumId());
        }
        Long forumId = cmd.getForumId();
        Forum forum = checkForumParameter(operatorId, forumId, tag);

        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forumId);
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
//            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(topicId));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            //query.addConditions(Tables.EH_FORUM_POSTS.DELETER_UID.ne(0L));
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        
        populatePosts(operatorId, posts, null, true);

        List<PostDTO> postDtoList = posts.stream().map((r) -> {
            PostDTO postdto = ConvertHelper.convert(r, PostDTO.class);

            //如果有附件，则将附件也转换一下，不转换的话可能会报错。 例如：postDTO.getAttachments().get(0)   add by yanjun 20170605
            if(r.getAttachments() != null && r.getAttachments().size() > 0){
                List<AttachmentDTO> listAttachementdto = r.getAttachments().stream().map((tt) -> {
                    return ConvertHelper.convert(tt, AttachmentDTO.class);
                }).collect(Collectors.toList());
                postdto.setAttachments(listAttachementdto);
            }
            return postdto;

        }).collect(Collectors.toList());
        
        //add commentCount when listTopicComments modified by xiongying 20160629
        ListPostCommandResponse response = new ListPostCommandResponse();
        response.setNextPageAnchor(nextPageAnchor);
        response.setPosts(postDtoList);
        response.setCommentCount(post.getChildCount());
        return response;
    }
    
    public void updatePostPrivacy(Long forumId, Long postId, PostPrivacy privacy) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "updatePostPrivacy";
        
        checkForumParameter(operatorId, forumId, tag);
        
        Post post = checkPostParameter(operatorId, forumId, postId, tag);
        
        if(privacy == null) {
            LOGGER.error("Invalid privacy, operatorId=" + operatorId + ", forumId=" + forumId 
                + ", postId=" + postId + ", privacy=" + privacy);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid privacy");
        }
        
        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                post.setPrivateFlag(privacy.getCode());
                this.forumProvider.updatePost(post);
               return null;
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the privacy of post, userId=" + operatorId + ", forumId=" + forumId 
                + ", postId=" + postId + ", privacy=" + privacy, e);
        }
    }
    
    @Override
    public PostDTO createComment(NewCommentCommand cmd) {
        //checkForumPostPrivilege(cmd.getForumId());
        
        User user = UserContext.current().getUser();
        Long userId = user.getId();

        //为了兼容新的统一评论接口，先检查活动, 因为新的统一接口没有传来forumId  add by yanjun 20170601
        Post ownerPost = checkPostParameter(userId, null, cmd.getTopicId(), "createComment");
        cmd.setForumId(ownerPost.getForumId());

        Post post = processCommentCommand(userId, cmd);

        //黑名单权限校验 by sfyan20161213
        checkBlacklist(null, null, post.getContentCategory(), post.getForumId());
        post.setContentCategory(null);

        Long embededAppId = cmd.getEmbeddedAppId();
        ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
        if(handler != null) {
            handler.preProcessEmbeddedObject(post);
            forumProvider.createPost(post);
            handler.preProcessEmbeddedObject(post);
        } else {
            forumProvider.createPost(post);
        }

        // Save the attachments after the post is saved
        processPostAttachments(userId, cmd.getAttachments(), post);
        
        // Populate the result post the same as query
        populatePost(userId, post, null, true);
        
        //Send message to post creator
        
        AddUserPointCommand pointCmd = new AddUserPointCommand(userId, PointType.CREATE_COMMENT.name(), 
            userPointService.getItemPoint(PointType.CREATE_COMMENT), userId);  
        userPointService.addPoint(pointCmd);
        
//        Post topic = this.forumProvider.findPostById(post.getParentPostId());
//        if(topic != null && !topic.getCreatorUid().equals(userId) && !topic.getStatus().equals(PostStatus.INACTIVE.getCode())) {
//            //Send message to creator
//            Map<String, String> map = new HashMap<String, String>();
//            String userName = (user.getNickName() == null) ? "" : user.getNickName();
//            String subject = (topic.getSubject() == null) ? "" : topic.getSubject();
//            map.put("userName", userName);
//            map.put("postName", subject);
//            sendMessageCode(topic.getCreatorUid(), user.getLocale(), map, ForumNotificationTemplateCode.FORUM_REPLAY_ONE_TO_CREATOR);
//        }
        
        //发表评论发消息给创建者或父评论者，add by tt, 20170314
        sendMessageToCreatorOrParent(user, post);

//        return ConvertHelper.convert(post, PostDTO.class);

        //如果有附件，则将附件也转换一下，不转换的话可能会报错。 例如：AttachmentDTO at = postDTO.getAttachments().get(0)   add by yanjun 20170605
        PostDTO postdto = ConvertHelper.convert(post, PostDTO.class);
        if(post.getAttachments() != null && post.getAttachments().size() > 0){
            List<AttachmentDTO> listAttachementdto = post.getAttachments().stream().map((tt) -> {
                return ConvertHelper.convert(tt, AttachmentDTO.class);
            }).collect(Collectors.toList());
            postdto.setAttachments(listAttachementdto);
        }

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(UserContext.currentUserId());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(postdto.getId());
            Long embeddedAppId = ownerPost.getEmbeddedAppId() != null ? ownerPost.getEmbeddedAppId() : 0;
            event.setEventName(SystemEvent.FORUM_COMMENT_CREATE.suffix(
                    ownerPost.getModuleType(), ownerPost.getModuleCategoryId(), embeddedAppId));

            event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
            event.addParam("post", StringHelper.toJsonString(ownerPost));
        });
        return postdto;

    }
    
    private void sendMessageToCreatorOrParent(User user, Post comment) {
    	// 评论所在的帖子
    	Post post = forumProvider.findPostById(comment.getParentPostId());
    	// 评论的父评论
    	Post parentComment = null;
    	if (comment.getParentCommentId() != null) {
			parentComment = forumProvider.findPostById(comment.getParentCommentId());
		}
    	
    	// 如果是帖子创建者评论自己的帖子不用给创建者发消息
    	// 如果评论的是帖子创建者发表的评论不用给创建者发消息
    	if (post != null && !post.getStatus().equals(PostStatus.INACTIVE.getCode())
    			&& post.getCreatorUid().longValue() != comment.getCreatorUid().longValue()
    			&& (parentComment == null || post.getCreatorUid().longValue() != parentComment.getCreatorUid().longValue())) {
			sendMessageToCreator(user, post);
		}
    	
    	// 如果评论的是自己发表的评论不用发消息
    	if (post != null && !post.getStatus().equals(PostStatus.INACTIVE.getCode())
    			&& parentComment != null && parentComment.getCreatorUid().longValue() != comment.getCreatorUid().longValue()) {
			sendMessageToParent(user, post, parentComment);
		}
    	
	}

	private void sendMessageToParent(User user, Post post, Post parentComment) {
		sendMessageToUserWhenComment(user, post, parentComment.getCreatorUid(), ForumNotificationTemplateCode.FORUM_COMMENT_TO_PARENT);
	}

	private void sendMessageToCreator(User user, Post post) {
		sendMessageToUserWhenComment(user, post, post.getCreatorUid(), ForumNotificationTemplateCode.FORUM_COMMENT_TO_CREATOR);
	}
	
	private void sendMessageToUserWhenComment(User user, Post post, Long toUserId, int code) {
		Map<String, Object> map = new HashMap<String, Object>();
		String userName = user.getNickName() == null ? "" : user.getNickName();
        map.put("userName", userName);
        String postName = post.getSubject() == null ? "" : post.getSubject();
        map.put("postName", postName);
        String scope = ForumNotificationTemplateCode.SCOPE;
        String text = localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), map, "");
		String[] textSplit = text.split("\t");
		String title = textSplit[0];
		String content = textSplit[1];
		
		RouterMetaObject mo = new RouterMetaObject();
        mo.setUrl(getPostNameUrl(post));
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, title);
		
        sendMessageToUser(toUserId, content, meta, MessageBodyType.TEXT.getCode());
		
		// 改成消息2.0的方式，需要后台拼好返回给客户端，commented by tt, 20170503
//		String templateString = getLocalTemplateString(user.getNamespaceId(), ForumNotificationTemplateCode.SCOPE, code, user.getLocale());
//		String[] templateStringSplit = templateString.split("\t");
//		String title = templateStringSplit[0];
//		String template = templateStringSplit[1];
//		
//		InnerLinkBody innerLinkBody = new InnerLinkBody(title, template);
//		String content = innerLinkBody.toString();
//		
//		Map<String, String> meta = new HashMap<>();
//		String userName = user.getNickName() == null ? "" : user.getNickName();
//		meta.put("userName", new MessageMetaContent(userName).toString());
//		String postName = post.getSubject() == null ? "" : post.getSubject();
//		String postNameUrl = getPostNameUrl(post);
//		meta.put("postName", new MessageMetaContent(postName, postNameUrl).toString());
//		
//		sendMessageToUser(toUserId, content, meta, MessageBodyType.INNER_LINK.getCode());
	}

	@Override
    public void sendMessageToUserWhenCommentNotSupport(User user) {
        String text = localeStringService.getLocalizedString(ForumLocalStringCode.SCOPE,String.valueOf(ForumLocalStringCode.POST_COMMENT_NOT_SUPPORT), user.getLocale(), "comment not support");

        sendMessageToUser(user.getId(), text, null, MessageBodyType.TEXT.getCode());
    }
	
	private String getPostNameUrl(Post post) {
		if (null != post.getEmbeddedAppId() && AppConstants.APPID_ACTIVITY == post.getEmbeddedAppId().longValue()) {
            return RouterBuilder.build(Router.ACTIVITY_DETAIL, new ActivityDetailActionData(post.getForumId(), post.getId()));
            // return new ActivityDetailActionData(post.getForumId(), post.getId()).toUrlString(ActionType.ACTIVITY_DETAIL.getUrl());
		}
        return RouterBuilder.build(Router.POST_DETAIL, new PostDetailActionData(post.getForumId(), post.getId()));
		// return new PostDetailActionData(post.getForumId(), post.getId()).toUrlString(ActionType.POST_DETAILS.getUrl());
	}

	private String getLocalTemplateString(Integer namespaceId, String scope, int code, String locale)	{
		LocaleTemplate localeTemplate = localeTemplateService.getLocalizedTemplate(namespaceId, scope, code, locale);
		if (localeTemplate == null) {
			localeTemplate = localeTemplateService.getLocalizedTemplate(Namespace.DEFAULT_NAMESPACE, scope, code, locale);
		}
		return localeTemplate.getText();
	}

	private void sendMessageCode(Long uid, String locale, Map<String, String> map, int code) {
        String scope = ForumNotificationTemplateCode.SCOPE;
        
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(uid, notifyTextForOther, null);
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
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
    	sendMessageToUser(uid, content, meta, MessageBodyType.TEXT.getCode());
    }
    
    @Override
    public void assignTopicScope(AssignTopicScopeCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "assignTopicScope";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long postId = cmd.getTopicId();
        Post post = checkPostParameter(operatorId, forumId, postId, tag);
        
        PostAssignedFlag assignedFlag = PostAssignedFlag.fromCode(cmd.getAssignedFlag());
        if(assignedFlag == null) {
            LOGGER.error("Invalid assigned flag, operatorId=" + operatorId + ", cmd=" + cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid assigned flag");
        }
        
        PostAssignedFlag postAssignedFlag = PostAssignedFlag.fromCode(post.getAssignedFlag());
        switch(assignedFlag) {
        case NONE:
            if(PostAssignedFlag.ASSIGNED == postAssignedFlag) {
                this.dbProvider.execute((status) -> {
                    List<AssignedScope> scopeList = this.forumProvider.findAssignedScopeByOwnerId(postId);
                    for(AssignedScope scope : scopeList) {
                        this.forumProvider.deleteAssignedScope(scope);
                    }
                    post.setAssignedFlag(PostAssignedFlag.NONE.getCode());
                    this.forumProvider.updatePost(post);
                    
                    return null;
                });
            } else {
                LOGGER.error("Forum post is not in assigned state, operatorId=" + operatorId + ", forumId=" + forumId 
                    + ", postId=" + postId + ", assignFlag=" + post.getAssignedFlag());
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                    ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_ASSIGNED, "Forum post is not in assigned state");
            }
            break;
        case ASSIGNED:
            ForumAssignedScopeCode scopeCode = ForumAssignedScopeCode.fromCode(cmd.getScopeCode());
            if(scopeCode == null) {
                LOGGER.error("Invalid assigned scope code, operatorId=" + operatorId + ", cmd=" + cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid assigned scope code");
            }
            
            List<Long> scopeIds = cmd.getScopeIds();
            if(scopeIds == null || scopeIds.size() == 0) {
                LOGGER.error("Assigned scope ids may not be empty, operatorId=" + operatorId + ", cmd=" + cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Assigned scope ids may not be empty");
            }
            
            if(PostAssignedFlag.NONE == postAssignedFlag) {
                this.dbProvider.execute((status) -> {
                    for(Long scopeId : scopeIds) {
                        AssignedScope scope = new AssignedScope();
                        scope.setOwnerId(postId);
                        scope.setScopeCode(scopeCode.getCode());
                        scope.setScopeId(scopeId);
                        this.forumProvider.createAssignedScope(scope);
                    }
                    post.setAssignedFlag(PostAssignedFlag.ASSIGNED.getCode());
                    this.forumProvider.updatePost(post);
                    
                    return null;
                });
            } else {
                LOGGER.error("Forum post is not in assigned state, operatorId=" + operatorId + ", forumId=" + forumId 
                    + ", postId=" + postId + ", assignFlag=" + post.getAssignedFlag());
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                    ForumServiceErrorCode.ERROR_FORUM_TOPIC_ALREADY_ASSIGNED, "Forum post is already assigned");
            }
            break;
        }
    }
    
    @Override
    public List<AssignedScopeDTO> listTopicAssignedScope(ListTopicAssignedScopeCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "listTopicAssignedScope";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long postId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, postId, tag);
        
        List<AssignedScopeDTO> scopeDtoList = new ArrayList<AssignedScopeDTO>();
        
        AssignedScopeDTO scopeDto = null;
        ForumAssignedScopeCode scopeCode = null;
        List<AssignedScope> scopeList = this.forumProvider.findAssignedScopeByOwnerId(postId);
        for(AssignedScope scope : scopeList) {
            scopeDto = ConvertHelper.convert(scope, AssignedScopeDTO.class);
            scopeDtoList.add(scopeDto);
            
            scopeCode = ForumAssignedScopeCode.fromCode(scope.getScopeCode());
            if(scopeCode != null) {
                switch(scopeCode) {
                case ALL:
                    break;
                case COMMUNITY:
                    Community community = this.communityProvider.findCommunityById(scope.getScopeId());
                    if(community != null) {
                        scopeDto.setScopeName(community.getName());
                    } else {
                        LOGGER.error("Community not found, operatorId=" + operatorId + ", postId=" + postId + ", scope=" + scopeDto);
                    }
                    break;
                case CITY:
                    Region region = this.regionProvider.findRegionById(scope.getScopeId());
                    if(region != null) {
                        scopeDto.setScopeName(region.getName());
                    } else {
                        LOGGER.error("City not found, operatorId=" + operatorId + ", postId=" + postId + ", scope=" + scopeDto);
                    }
                    break;
                }
            }
        }
        
        return scopeDtoList;
    }
    
    @Override
    public SearchTopicAdminCommandResponse searchTopic(SearchTopicAdminCommand cmd) {
        PostAdminQueryFilter filter = new PostAdminQueryFilter();
        String keyword = cmd.getKeyword();
        if(!StringUtils.isEmpty(keyword)) {
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CONTENT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_SUBJECT);
            filter.setQueryString(keyword);
        }
        
        List<String> phones = cmd.getSenderPhones();
        if(phones != null && phones.size() > 0) {
            filter.includeFilter(PostAdminQueryFilter.TERM_IDENTIFY, phones);
        }
        
        List<String> nickNames = cmd.getSenderNickNames();
        if(nickNames != null && nickNames.size() > 0) {
            filter.includeFilter(PostAdminQueryFilter.TERM_SENDERNAME, nickNames);
        }
        
        List<Long> parentPostId = new ArrayList<Long>();
        parentPostId.add(0L);
        filter.includeFilter(PostAdminQueryFilter.TERM_PARENTPOSTID, parentPostId);
        
        Long startTime = cmd.getStartTime();
        if(startTime != null) {
            filter.dateFrom(new Date(startTime));
        }
        
        Long endTime = cmd.getEndTime();
        if(endTime != null) {
            filter.dateTo(new Date(endTime));
        }
        
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        } else {
            pageNum = 0;
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        filter.setPageInfo(pageNum, pageSize);
        ListPostCommandResponse queryResponse = postSearcher.query(filter);
        
        SearchTopicAdminCommandResponse response = new SearchTopicAdminCommandResponse();
        response.setKeywords(queryResponse.getKeywords());
        response.setNextPageAnchor(queryResponse.getNextPageAnchor());
        
        List<PostAdminDTO> adminPostList = new ArrayList<PostAdminDTO>();
        List<PostDTO> postList = queryResponse.getPosts();
        for(PostDTO post : postList) {
            try {
                PostDTO temp = getTopicById(post.getId(), cmd.getCommunityId(), false);
                PostAdminDTO adminPost = ConvertHelper.convert(temp, PostAdminDTO.class);
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(temp.getCreatorUid(),
                    IdentifierType.MOBILE.getCode());
                if(identifier != null) {
                    adminPost.setCreatorPhone(identifier.getIdentifierToken());
                }
                
                adminPostList.add(adminPost);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        response.setPosts(adminPostList);
        return response;
    }

    @Override
    public void updateUsedAndRental(UsedAndRentalCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        Long postId = cmd.getId();
        Post post = checkPostParameter(operatorId, -1L, postId, "updateUsedAndRental");
        
        UsedAndRentalStatus status = UsedAndRentalStatus.fromCode(cmd.getStatus());
        if(status == null) {
            LOGGER.error("Invalid used and rental status, operatorId=" + operatorId + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid status parameter");
        }
        
        if(!operatorId.equals(post.getCreatorUid())) {
            LOGGER.error("The operator is not the creator, operatorId=" + operatorId + ", creatorId=" + post.getCreatorUid() 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                "Access denied: insufficient privilege");
        }
        
        Long embeddedAppId = post.getEmbeddedAppId();
        if(embeddedAppId == null || AppConstants.APPID_USED_AND_RENTAL != embeddedAppId.longValue()) {
            LOGGER.error("The topic is not the used and rental type, operatorId=" + operatorId + ", postId=" + postId 
                + ", expectEmbeddedAppId=" + AppConstants.APPID_USED_AND_RENTAL + ", realEmbeddedAppId=" + embeddedAppId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id parameter");
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.forumProvider.updatePost(post);
           return null;
        });
    }

    @Override
    public void updateFreeStuff(FreeStuffCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        Long postId = cmd.getId();
        Post post = checkPostParameter(operatorId, -1L, postId, "updateFreeStuff");
        
        FreeStuffStatus status = FreeStuffStatus.fromCode(cmd.getStatus());
        if(status == null) {
            LOGGER.error("Invalid free stuff status, operatorId=" + operatorId + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid status parameter");
        }
        
        if(!operatorId.equals(post.getCreatorUid())) {
            LOGGER.error("The operator is not the creator, operatorId=" + operatorId + ", creatorId=" + post.getCreatorUid() 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                "Access denied: insufficient privilege");
        }
        
        Long embeddedAppId = post.getEmbeddedAppId();
        if(embeddedAppId == null || AppConstants.APPID_FREE_STUFF != embeddedAppId.longValue()) {
            LOGGER.error("The topic is not the free stuff type, operatorId=" + operatorId + ", postId=" + postId 
                + ", expectEmbeddedAppId=" + AppConstants.APPID_USED_AND_RENTAL + ", realEmbeddedAppId=" + embeddedAppId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id parameter");
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.forumProvider.updatePost(post);
           return null;
        });
    }

    @Override
    public void updateLostAndFound(LostAndFoundCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        Long postId = cmd.getId();
        Post post = checkPostParameter(operatorId, -1L, postId, "updateFreeStuff");
        
        LostAndFoundStatus status = LostAndFoundStatus.fromCode(cmd.getStatus());
        if(status == null) {
            LOGGER.error("Invalid lost and found status, operatorId=" + operatorId + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid status parameter");
        }
        
        if(!operatorId.equals(post.getCreatorUid())) {
            LOGGER.error("The operator is not the creator, operatorId=" + operatorId + ", creatorId=" + post.getCreatorUid() 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                "Access denied: insufficient privilege");
        }
        
        Long embeddedAppId = post.getEmbeddedAppId();
        if(embeddedAppId == null || AppConstants.APPID_LOST_AND_FOUND != embeddedAppId.longValue()) {
            LOGGER.error("The topic is not the lost and found type, operatorId=" + operatorId + ", postId=" + postId 
                + ", expectEmbeddedAppId=" + AppConstants.APPID_USED_AND_RENTAL + ", realEmbeddedAppId=" + embeddedAppId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id parameter");
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.forumProvider.updatePost(post);
           return null;
        });
    }
    
    @Override
    public void checkForumPostPrivilege(Long forumId) {
        if(forumId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.findForumById(forumId.longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        User user = UserContext.current().getUser();
        if(!this.aclProvider.checkAccess(
                EntityType.FORUM.getCode(), forum.getId(), 
                EntityType.USER.getCode(), user.getId(), 
                PrivilegeConstants.ForumNewTopic, 
                roleResolve.determineRoleInResource(user.getId(), forum.getOwnerId(), 
                        EntityType.FORUM.getCode(), forum.getId()))) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
    }

    @Override
    public void checkForumModifyItemPrivilege(Long forumId, Long topicId) {
        if(forumId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.findForumById(forumId.longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        if(topicId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid topicId parameter");
            
        Post post = this.forumProvider.findPostById(topicId);
        if(post == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the post object");
        
        User user = UserContext.current().getUser();
        List<Long> roles = roleResolve.determineRoleInResource(user.getId(), forum.getOwnerId(),
                EntityType.FORUM.getCode(), forum.getId());
        
        if(post.getCreatorUid() != null && post.getCreatorUid().longValue() == user.getId())
            roles.add(Role.ResourceCreator);
        if(!this.aclProvider.checkAccess(
                EntityType.POST.getCode(), topicId, 
                EntityType.USER.getCode(), user.getId(), 
                PrivilegeConstants.Write, 
                roles)) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
    }

    @Override
    public void checkForumDeleteItemPrivilege(Long forumId, Long topicId) {
        if(forumId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.findForumById(forumId.longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        if(topicId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid topicId parameter");
            
        Post post = this.forumProvider.findPostById(topicId);
        if(post == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the post object");
        
        User user = UserContext.current().getUser();
        List<Long> roles = roleResolve.determineRoleInResource(user.getId(), forum.getOwnerId(),
                EntityType.FORUM.getCode(), forum.getId());
        
        if(post.getCreatorUid() != null && post.getCreatorUid().longValue() == user.getId())
            roles.add(Role.ResourceCreator);
        if(!this.aclProvider.checkAccess(
                EntityType.POST.getCode(), topicId, 
                EntityType.USER.getCode(), user.getId(), 
                Privilege.Delete, 
                roles)) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
    }
    
    private Forum checkForumParameter(Long operatorId, Long forumId, String tag) {
        if(forumId == null) {
            LOGGER.error("Forum id may not be null, operatorId=" + operatorId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid forum id");
        }
        
        Forum forum = this.forumProvider.findForumById(forumId);
        if(forum == null) {
            LOGGER.error("Forum not found, operatorId=" + operatorId + ", forumId=" + forumId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "Forum not found");
        }
        
        return forum;
    }
    
    private Post checkPostParameter(Long operatorId, Long forumId, Long postId, String tag) {
        if(postId == null) {
            LOGGER.error("Forum post id may not be null, operatorId=" + operatorId 
                + ", tag=" + tag + ", forumId=" + forumId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid forum post id");
        }
        
        Post post = this.forumProvider.findPostById(postId);
        if(post == null) {
            LOGGER.error("Forum post not found, operatorId=" + operatorId + ", forumId=" + forumId 
                + ", postId=" + postId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }
        if (post.getStatus() != null && post.getStatus().equals(PostStatus.INACTIVE.getCode())) {
            int code = ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED;
            if (post.getContentCategory() != null && post.getContentCategory() == CategoryConstants.CATEGORY_ID_NOTICE) {
                code = ForumServiceErrorCode.ERROR_ANNOUNCEMENT_DELETED;
            }
            LOGGER.error("Forum post is deleted, operatorId=" + operatorId + ", forumId=" + forumId
                    + ", postId=" + postId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    code, "Forum post is deleted");
        }
        return post;
    }
    
    private Community checkCommunityParameter(Long operatorId, Long communityId, String tag) {
        if(communityId == null) {
            LOGGER.error("Community id may not be null, operatorId=" + operatorId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid community id");
        }
        
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null) {
            LOGGER.error("Community is not found, operatorId=" + operatorId + ", communityId=" + communityId
                + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_COMMUNITY_NOT_FOUND, "Community not found");
        } 
        
        return community;
    }
    
    private Organization checkOrganizationParameter(Long operatorId, Long organizationId, String tag) {
        if(organizationId == null) {
            LOGGER.error("Organization id may not be null, operatorId=" + operatorId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid organization id");
        }
        
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if(organization == null) {
            LOGGER.error("Organization is not found, operatorId=" + operatorId + ", organizationId=" + organizationId
                + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ForumServiceErrorCode.ERROR_FORUM_ORGANIZATION_NOT_FOUND, "Organization not found");
        } 
        
        return organization;
    }
    
//    private ListPostCommandResponse listSystemForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
//        User user = UserContext.current().getUser();
//        long userId = user.getId();
//        Long communityId = cmd.getCommunityId();
//        
//        Community community = checkCommunityParameter(userId, communityId, "listSystemForumTopicsByUser");
//        
//        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
//        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
//
//        // 根据查帖指定的可见性创建查询条件
//        VisibilityScope scope = VisibilityScope.fromCode(cmd.getVisibilityScope());
//        if(scope == null) {
//            scope = VisibilityScope.NEARBY_COMMUNITIES;
//        }
//        Condition visibilityCondition = buildPostVisibilityQryCondition(user, community, gaRegionIdMap, scope);
//        
//        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
//        locator.setAnchor(cmd.getPageAnchor());
//        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
//            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
//                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
//            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
//            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
//            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
//            if(visibilityCondition != null) {
//                query.addConditions(visibilityCondition);
//            }
//            
//            return query;
//        });
//        this.forumProvider.populatePostAttachments(posts);
//        
//        Long nextPageAnchor = null;
//        if(posts.size() > pageSize) {
//            posts.remove(posts.size() - 1);
//            nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();
//        }
//        
//        populatePosts(userId, posts, communityId, false);
//        
//        List<PostDTO> postDtoList = posts.stream().map((r) -> {
//          return ConvertHelper.convert(r, PostDTO.class);  
//        }).collect(Collectors.toList());
//        
//        
//        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
//    }
    
    /**
     * 查询住宅小区的社区论坛或商业园区的默认论坛里的帖。
     * 为了兼容新增的商业园区，把条件重新调整编写一遍，上面注释掉的为原住宅小区查询逻辑。by lqs 20151105
     * @param forum
     * @param cmd
     * @return
     */
    private ListPostCommandResponse listSystemForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        
        Community community = checkCommunityParameter(userId, communityId, "listSystemForumTopicsByUser");

        // 根据查帖指定的可见性创建查询条件
        VisibilityScope scope = VisibilityScope.fromCode(cmd.getVisibilityScope());
        Condition visibilityCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);

        //单个 -- 查询单个目标的（正常、clone），或者发送到“全部”的（正常）   add by yanjun 20170809
        Condition cloneCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode())
                .and(Tables.EH_FORUM_POSTS.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode()));

        Condition condition= this.notEqPostCategoryCondition(cmd.getExcludeCategories(), null);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            
//            //新增暂存活动，后台管理员在web端要看到暂存的活动  add by yanjun 20170518
//            if(cmd.getNeedTemporary() != null && cmd.getNeedTemporary().byteValue() == 1){
//            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.in(PostStatus.ACTIVE.getCode(), PostStatus.WAITING_FOR_CONFIRMATION.getCode()));
//            }else{
//            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
//            }
            Condition temporaryCondition = getTemporaryCondition(cmd.getNeedTemporary());
            if(temporaryCondition != null){
                query.addConditions(temporaryCondition);
            }

            //支持按话题、活动、投票来查询数据   add by yanjun 20170612
            if(cmd.getCategoryId() != null){
                //1和1001都是话题，老客户端传的1web传的1001，新客户端会改成1001，很久很久以后可以刷一遍数据改掉这里的代码  add by yanjun 20171221
                if(CategoryConstants.CATEGORY_ID_TOPIC == cmd.getCategoryId().longValue() || CategoryConstants.CATEGORY_ID_TOPIC_COMMON == cmd.getCategoryId().longValue()){
                    query.addConditions(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC)
                            .or(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC_COMMON)));
                }else {
                    query.addConditions(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(cmd.getCategoryId()));
                }
            }

            //支持标签搜索  add by yanjun 20170712
            if(!StringUtils.isEmpty(cmd.getTag())){
                query.addConditions(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
            }

            //论坛多入口，老客户端没有这个参数，默认入口为0  add by yanjun 20171025
            if(StringUtils.isEmpty(cmd.getForumEntryId())){
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(0L));
            }else {
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(cmd.getForumEntryId()));
            }
            
            if(visibilityCondition != null) {
                //单个 -- 查询单个目标的（正常、clone），或者发送到“全部”的（正常）   add by yanjun 20170809
                query.addConditions(visibilityCondition.or(cloneCondition));
            }
            
            if(null != condition){
            	query.addConditions(condition);
            }

            return query;
        });

        // 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170809
        populateRealPost(posts);

        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        
        populatePosts(userId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    /**
     * 对于小区/园区在默认论坛里的帖，不管creatorTag和targetTag是谁，其可见范围由visibleRegionType和VisibleRegionId决定；
     * 条件一：帖子必须是公开的。
     * 条件二：如果类型是小区/园区，由于住宅小区含周边社区概念，故对于住宅小区需要增加周边小区范围，而商业园区则只限于本园区；
     * 条件三：如果类型是片区，则需要找覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求。
     * 条件四：自己发的帖自己永远能看。
     * 三个条件关系：(条件一 and (条件二 or 条件三)) or 条件四
     * @param user 当前用户信息
     * @param community 小区信息
     * @param scope 范围（住宅小区才使用，里面指定是否需要扩展到周边小区）
     * @return 条件
     */
    private Condition buildDefaultForumPostQryConditionForCommunity(User user, Community community, VisibilityScope scope) {
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        Condition creatorCondition = Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId);
        
        // 只有公开的帖子才能查到
        Condition c1 = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.notEqual(PostPrivacy.PRIVATE.getCode());
        c1 = creatorCondition.or(c1);
        
        if(community == null) {
            LOGGER.error("Community not found, userId=" + userId);
            return creatorCondition;
        }
        
        // 如果类型是小区/园区，由于住宅小区含周边社区概念，故对于住宅小区需要增加周边小区范围，而商业园区则只限于本园区；
        Condition c2 = buildDefaultForumPostQryConditionForNearbyCommunity(user, community, scope);
        
        // 如果类型是片区，则需要找覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求。
        Condition c3 = buildDefaultForumPostQryConditionByOrganization(user, community);
        
        if(c3 == null) {
            return c1.and(c2);
        } else {
            return c1.and(c2.or(c3));
        }
    }
    
    /**
     * 如果类型是小区/园区，由于住宅小区含周边社区概念，故对于住宅小区需要增加周边小区范围，而商业园区则只限于本园区；
     * 参考buildPostQryConditionForCommunity的条件二。
     * @param user 当前用户信息
     * @param community 小区信息
     * @param scope 范围（住宅小区才使用，里面指定是否需要扩展到周边小区）
     * @return 条件
     */
    private Condition buildDefaultForumPostQryConditionForNearbyCommunity(User user, Community community, VisibilityScope scope) {
        Long userId = getUserIdWithDefault(user);
        
        Long communityId = community.getId();
        CommunityType communityType = CommunityType.fromCode(community.getCommunityType());
        if(communityType == null) {
            LOGGER.error("Community type not found, userId=" + userId + ", communityId=" + community.getId() 
                + ", communityType=" + communityType + ", scope=" + scope);
            communityType = CommunityType.RESIDENTIAL;
        }
        
        // 不管是商业园区还是住宅小区都需要包含本小区ID
        List<Long> cmntyIdList = new ArrayList<Long>();
        cmntyIdList.add(communityId);
        // 对于住宅小区还存在周边社区的概述（商业园区没有此概念），此时如果不指明是“本小区”则添加上周边各小区
        if(communityType == CommunityType.RESIDENTIAL && scope != VisibilityScope.COMMUNITY && community.getStatus() == CommunityAdminStatus.ACTIVE.getCode()) {
            List<Community> nearbyCmntyList = communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(), communityId);
            for(Community c : nearbyCmntyList) {
                cmntyIdList.add(c.getId());
            }
        }
        Condition condition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        condition = condition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(cmntyIdList));
        
        return condition;
    }
    
    /**
     * 如果类型是片区，则需要找覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求。
     * @param user 当前用户信息
     * @param community 小区信息
     * @return 条件
     */
    private Condition buildDefaultForumPostQryConditionByOrganization(User user, Community community) {
        Long communityId = community.getId();
        // 对于发给机构的帖或者是机构发的帖，凡是覆盖该小区的所有机构都满足要求
//        List<Organization> organizationList = this.organizationService.getOrganizationTreeUpToRoot(communityId);
//        List<Long> organizationIdList = new ArrayList<Long>();
//        for(Organization organization : organizationList) {
//            organizationIdList.add(organization.getId());
//        }
        List<Long> organizationIdList = organizationService.getOrganizationIdsTreeUpToRoot(communityId);
        Condition condition = null;
        if(organizationIdList.size() > 0) {
            condition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            condition = condition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(organizationIdList));
        }
        
        return condition;
    }
    
    private Long getUserIdWithDefault(User user) {
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("User id is not found, -1 will be used");
            }
        }
        
        return userId;
    }
    
    private ListPostCommandResponse listSimpleForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        Condition condition = this.notEqPostCategoryCondition(cmd.getExcludeCategories(), null);

        //整个论坛 -- 该论坛的正常帖（正常），或者发送到“全部”的（clone、正常） add by yanjun 20170809
        Condition cloneConditin = Tables.EH_FORUM_POSTS.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode())
                .or(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode()));
        
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId())); 
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            
//            //新增暂存活动，后台管理员在web端要看到暂存的活动  add by yanjun 20170518
//            if(cmd.getNeedTemporary() != null && cmd.getNeedTemporary().byteValue() == 1){
//            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.in(PostStatus.ACTIVE.getCode(), PostStatus.WAITING_FOR_CONFIRMATION.getCode()));
//            }else{
//            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
//            }
            Condition temporaryCondition = getTemporaryCondition(cmd.getNeedTemporary());
            if(temporaryCondition != null){
                query.addConditions(temporaryCondition);
            }

            //支持按话题、活动、投票来查询数据   add by yanjun 20170612
            if(cmd.getCategoryId() != null){
                //1和1001都是话题，老客户端传的1web传的1001，新客户端会改成1001，很久很久以后可以刷一遍数据改掉这里的代码  add by yanjun 20171221
                if(CategoryConstants.CATEGORY_ID_TOPIC == cmd.getCategoryId().longValue() || CategoryConstants.CATEGORY_ID_TOPIC_COMMON == cmd.getCategoryId().longValue()){
                    query.addConditions(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC)
                            .or(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC_COMMON)));
                }else {
                    query.addConditions(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(cmd.getCategoryId()));
                }
            }
            //支持标签搜索  add by yanjun 20170712
            if(!StringUtils.isEmpty(cmd.getTag())){
                query.addConditions(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
            }

            //论坛多入口，老客户端没有这个参数，默认入口为0  add by yanjun 20171025
            if(StringUtils.isEmpty(cmd.getForumEntryId())){
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(0L));
            }else {
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(cmd.getForumEntryId()));
            }

            if(null != condition){
            	query.addConditions(condition);
            }

            //整个论坛 -- 该论坛的正常帖（正常），或者发送到“全部”的（clone、正常） add by yanjun 20170809
            if(cloneConditin != null){
                query.addConditions(cloneConditin);
            }
            return query;
        });

        // 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170808
        populateRealPost(posts);

        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        
        populatePosts(userId, posts, cmd.getCommunityId(), false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    private Post processTopicCommand(Long userId, NewTopicCommand cmd) {
        Post post = new Post();

        Long forumId = cmd.getForumId();
        checkForumParameter(userId, forumId, "processTopicCommand");
        
        processPostCategory(userId, cmd, post);
        processPostLocation(userId, cmd, post);
//        processPostVisibility(userId, forumId, cmd, post);
        post.setVisibleRegionType(cmd.getVisibleRegionType());
        post.setVisibleRegionId(cmd.getVisibleRegionId());
        
        post.setForumId(cmd.getForumId());
        post.setParentPostId(0L);
        
        post.setCreatorUid(userId);
        PostEntityTag creatorTag = PostEntityTag.fromCode(cmd.getCreatorTag());
        if(creatorTag == null) {
            creatorTag = PostEntityTag.USER;
        }
        post.setCreatorTag(creatorTag.getCode());

        PostEntityTag targetTag = PostEntityTag.fromCode(cmd.getTargetTag());
        if(targetTag == null) {
            targetTag = PostEntityTag.USER;
        }
        post.setTargetTag(targetTag.getCode());        
        
        post.setSubject(cmd.getSubject());
        post.setContentType(cmd.getContentType());
        post.setContent(cmd.getContent());
        Long embededAppId = cmd.getEmbeddedAppId();
        post.setEmbeddedAppId(embededAppId);
        post.setEmbeddedId(cmd.getEmbeddedId());
        post.setEmbeddedJson(cmd.getEmbeddedJson());
        post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        if(null != cmd.getStartTime() && null != cmd.getEndTime()){
        	post.setStartTime(new Timestamp(cmd.getStartTime()));
        	post.setEndTime(new Timestamp(cmd.getEndTime()));
        }
        
        PostPrivacy privateFlag = PostPrivacy.fromCode(cmd.getPrivateFlag());
        if(privateFlag == null) {
            // 政府机构发的维修之类的帖，默认不公开
            if(CategoryConstants.GA_PRIVACY_CATEGORIES.contains(post.getCategoryId())) {
                privateFlag =  PostPrivacy.PRIVATE;
            } else {
                privateFlag =  PostPrivacy.PUBLIC;
            }
        }
        post.setPrivateFlag(privateFlag.getCode());
        
        post.setAssignedFlag(PostAssignedFlag.NONE.getCode());
        
        if (MediaDisplayFlag.fromCode(cmd.getMediaDisplayFlag()) != MediaDisplayFlag.NO) {
			post.setMediaDisplayFlag(MediaDisplayFlag.YES.getCode());
		}else{
			post.setMediaDisplayFlag(cmd.getMediaDisplayFlag());
		}
        
        //添加人数限制，add by tt, 20161012
        post.setMaxQuantity(cmd.getMaxQuantity());
        post.setMinQuantity(cmd.getMinQuantity());
        
        //添加活动状态，用于暂存或者立刻发布，不传默认2是立刻发布 add by yanjun 20170510
        if(cmd.getStatus() != null){
        	post.setStatus(cmd.getStatus());
        }

        //添加标签普通话题的标签通过此字段从前台出来。 add by yanjun 20170613
        post.setTag(cmd.getTag());

        //设置帖子的克隆状态和真身帖   add by yanjun 20170807
        post.setCloneFlag(cmd.getCloneFlag());
        post.setRealPostId(cmd.getRealPostId());

        post.setForumEntryId(cmd.getForumEntryId());

        post.setInteractFlag(InteractFlag.SUPPORT.getCode());
        post.setStickFlag(StickFlag.DEFAULT.getCode());

        post.setModuleType(cmd.getModuleType());
        post.setModuleCategoryId(cmd.getModuleCategoryId());

        return post;
    }
    
    private Post processCommentCommand(long userId, NewCommentCommand cmd) {
        Post commentPost = new Post();

        Long forumId = cmd.getForumId();
        
        Long topicId = cmd.getTopicId();
        if(topicId == null) {
            LOGGER.error("Topic id is null, userId=" + userId + ", forumId=" + forumId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid topic id");
        }
        Post topic = this.forumProvider.findPostById(topicId);
        if(topic == null) {
            LOGGER.error("Topic not found, userId=" + userId + ", forumId=" + forumId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "Topic not found");
        }
        
        commentPost.setForumId(cmd.getForumId());
        commentPost.setParentPostId(topic.getId());
        commentPost.setCreatorUid(userId);
        commentPost.setCreatorTag(PostEntityTag.USER.getCode());
        commentPost.setTargetTag(PostEntityTag.USER.getCode());
        commentPost.setContentType(cmd.getContentType());
        commentPost.setContent(cmd.getContent());
        commentPost.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        commentPost.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
        commentPost.setAssignedFlag(PostAssignedFlag.NONE.getCode());
        commentPost.setStatus(PostStatus.ACTIVE.getCode());
        commentPost.setContentCategory(topic.getContentCategory());
        
        // 添加父评论id字段, add by tt, 20170314
        commentPost.setParentCommentId(cmd.getParentCommentId());
        return commentPost;
    }
    
    private void processPostCategory(long userId, NewTopicCommand cmd, Post post) {
        Long contentCategory = cmd.getContentCategory();
        if(contentCategory == null || contentCategory.longValue() == 0 || contentCategory.longValue() == -1) {
            contentCategory = CategoryConstants.CATEGORY_ID_TOPIC_COMMON;
        }
        
        Category category = this.categoryProvider.findCategoryById(contentCategory);
        if(category == null) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Content category not found, userId=" + userId + ", cmd=" + cmd);
            }
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid content category");
        }
        
        post.setCategoryId(contentCategory);
        post.setCategoryPath(category.getPath());
        
        if(cmd.getActionCategory() != null && cmd.getActionCategory().longValue() > 0) {
            category = this.categoryProvider.findCategoryById(cmd.getActionCategory());
            if(category == null) {
                if(LOGGER.isErrorEnabled()) {
                    LOGGER.error("Action category not found, userId=" + userId + ", cmd=" + cmd);
                }
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid build category");
            }
            
            post.setActionCategory(cmd.getActionCategory());
            post.setActionCategoryPath(category.getPath());
        }
    }
    
    private void processPostVisibility(long userId, Long forumId, NewTopicCommand cmd, Post post) {
        // 私有圈和兴趣圈也涉及可见区域（用发帖那一刻的小区ID）        
        VisibleRegionType regionType = VisibleRegionType.fromCode(cmd.getVisibleRegionType());
        if(regionType == null) {
            LOGGER.error("Invaild visible region type, userId=" + userId + ", forumId=" + forumId 
                + ", regionType=" + cmd.getVisibleRegionType());
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid visible region type");
        }
        
        switch(regionType) {
        case COMMUNITY:
            Long communityId = cmd.getVisibleRegionId();
            if(communityId == null) {
                LOGGER.error("Invaild visible region id, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", communityId=" + communityId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid visible region id");
            }
            Community community = this.communityProvider.findCommunityById(communityId);
            if(community == null) {
                LOGGER.error("Visible region not found, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", communityId=" + communityId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ForumServiceErrorCode.ERROR_FORUM_VISIBLE_REGION_NOT_FOUND, "Visible region not found");
            }
            break;
        case REGION:
            Long regionId = cmd.getVisibleRegionId();
            if(regionId == null) {
                LOGGER.error("Invaild visible region id, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", regionId=" + regionId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid visible region id");
            }
            
            Region region = this.regionProvider.findRegionById(regionId);
            if(region == null) {
                LOGGER.error("Visible region not found, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", regionId=" + regionId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ForumServiceErrorCode.ERROR_FORUM_VISIBLE_REGION_NOT_FOUND, "Visible region not found");
            }
            break;
        default:
            LOGGER.error("Invaild visible region type, userId=" + userId + ", forumId=" + forumId 
                + ", regionType=" + regionType);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "Invalid visible region type");
        }
        
        post.setVisibleRegionType(regionType.getCode());
        post.setVisibleRegionId(cmd.getVisibleRegionId());
    }
    
    private void processPostAttachments(long userId, List<AttachmentDescriptor> attachmentList, Post post) {
        List<Attachment> results = null;
        
        if(attachmentList != null) {
            results = new ArrayList<Attachment>();
            
            Attachment attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
                attachment = new Attachment();
                attachment.setCreatorUid(userId);
                attachment.setPostId(post.getId());
                attachment.setContentType(descriptor.getContentType());
                attachment.setContentUri(descriptor.getContentUri());
                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                
                // Make sure we can save as many attachments as possible even if any of them failed
                try {
                    forumProvider.createPostAttachment(attachment);
                    results.add(attachment);
                } catch(Exception e) {
                    LOGGER.error("Failed to save the attachment, userId=" + userId 
                        + ", attachment=" + attachment, e);
                }
            }
            
            post.setAttachments(results);
        }
    }
    
    private void processPostLocation(long userId, NewTopicCommand cmd, Post post) {
        Double longitude = cmd.getLongitude();
        Double latitude = cmd.getLatitude();
        
        if(longitude != null && latitude != null) {
            post.setLongitude(longitude);
            post.setLatitude(latitude);
            String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            post.setGeohash(geohash);
        }
    }
    
//    private Condition buildPostCategoryQryCondition(User user, PostEntityTag entityTag, Community community,  
//        Map<String, Long> gaRegionIdMap, Long contentCategoryId, Long actionCategoryId) {
//        Condition categoryCondition = null;
//        
//        Category contentCatogry = null;
//        // contentCategoryId为0表示全部查，此时也不需要给category条件
//        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
//            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
//        }
//        if(contentCatogry != null) {
//            categoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
//        }
//        
//        Category actionCategory = null;
//        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
//            actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
//        }
//        if(actionCategory != null) {
//            Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
//            if(categoryCondition != null) {
//                categoryCondition = categoryCondition.and(tempCondition);
//            } else {
//                categoryCondition = tempCondition;
//            }
//        }
//        
//        Condition userCondition = null;
//        if(entityTag != null) {
//            switch(entityTag) {
//            case USER:
//                userCondition = buildPostQryConditionBySimpleUser(community, VisibilityScope.NEARBY_COMMUNITIES);
//                break;
//            case PM:
//            case GARC:
//            case GANC:
//            case GAPS:
//            case GACW:
//                userCondition = buildGaRelatedPostQryConditionByCategory(user.getId(), entityTag, community, gaRegionIdMap);
//                break;
//            default:
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Entity tag is not supported, userId=" + user.getId() + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
//                }
//            }
//        } else {
//            if(LOGGER.isInfoEnabled()) {
//                LOGGER.info("Entity tag is null, userId=" + user.getId() + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
//            }
//        }
//        
//        if(userCondition == null) {
//            return categoryCondition;
//        } else {
//            if(categoryCondition == null) {
//                return userCondition;
//            } else {
//                return categoryCondition.and(userCondition);
//            }
//        }
//    }
    
    private Condition buildPostCategoryQryCondition(User user, PostEntityTag entityTag, Community community,  
        Long contentCategoryId, Long actionCategoryId) {
        Condition categoryCondition = null;
        
        Category contentCatogry = null;
        // contentCategoryId为0表示全部查，此时也不需要给category条件
        
        List<Long> contentCategoryIds = new ArrayList<Long>();
        
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
        	contentCategoryIds.add(contentCategoryId);
        }else{
        	// 為0或者null的情況下，默認查詢全部的任務貼
        	contentCategoryIds =  CategoryConstants.GA_PRIVACY_CATEGORIES;
        }
        
        Condition cond = null;
        for (Long categoryId : contentCategoryIds) {
        	contentCatogry = this.categoryProvider.findCategoryById(categoryId);
        	if(contentCatogry != null) {
        		if(null == cond){
        			cond = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
        		}else{
        			cond = cond.or(Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%"));
        		}
            }
		}
        
        categoryCondition = cond;
        
        Category actionCategory = null;
        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
            actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
        }
        if(actionCategory != null) {
            Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
            if(categoryCondition != null) {
                categoryCondition = categoryCondition.and(tempCondition);
            } else {
                categoryCondition = tempCondition;
            }
        }
        
        Condition userCondition = null;
        // 对于entityTag为普通用户，则是查普通用户所有能看到的帖（加上类型限制）：
        // 1、若是政府相关类型的帖，住宅小区相关的帖的可见范围缩小到本小区范围
        // 2、其它帖按指定小区进行限制可见范围
        if(entityTag == null || entityTag == PostEntityTag.USER) {
            VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
            if(contentCategoryId != null && CategoryConstants.GA_RELATED_CATEGORIES.contains(contentCategoryId)) {
                scope = VisibilityScope.COMMUNITY;
            }
            userCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
        } else {
            // 对于指定entityTag，则对creatorTag和targetTag进行限制
            // Condition privacyCondition = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.notEqual(PostPrivacy.PRIVATE.getCode());
            Condition entityCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(entityTag.getCode());
            entityCondition = entityCondition.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(entityTag.getCode()));
            //Condition visibleCondition = buildDefaultForumPostQryConditionByOrganization(user, community);
            // 对于物业等政府相关的类型，则只查本小区的
            VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
            if(contentCatogry != null && CategoryConstants.GA_RELATED_CATEGORIES.contains(contentCatogry.getId())) {
                scope = VisibilityScope.COMMUNITY;
            }
            Condition visibleCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
            if(visibleCondition != null) {
                userCondition = entityCondition.and(visibleCondition);
            } else {
                userCondition = entityCondition;
            }
        }
        
        if(userCondition == null) {
            return categoryCondition;
        } else {
            if(categoryCondition == null) {
                return userCondition;
            } else {
                return categoryCondition.and(userCondition);
            }
        }
    }
    
//    private Condition buildPostQryConditionByUserRelated(User user, Community community, Map<String, Long> gaRegionIdMap) {
//        VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
//        Condition systemForumCondition = buildPostVisibilityQryCondition(user, community, gaRegionIdMap, scope);
//        systemForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(ForumConstants.SYSTEM_FORUM).and(systemForumCondition);
//        
//        List<UserGroup> userGroupList = userProvider.listUserGroups(user.getId(), GroupDiscriminator.GROUP.getCode());
//        List<Long> forumIdList = new ArrayList<Long>();
//        for(UserGroup userGroup : userGroupList) {
//            Group group = groupProvider.findGroupById(userGroup.getGroupId());
//            // 不查私有圈对应的论坛帖
//            if(group != null && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC 
//                && group.getOwningForumId() != null) {
//                forumIdList.add(group.getOwningForumId());
//            }
//        }
//        
//        Condition otherForumCondition = null;
//        if(forumIdList.size() > 0) {
//            otherForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIdList);
//        }
//        
//        if(systemForumCondition != null) {
//            if(otherForumCondition == null) {
//                return systemForumCondition;
//            } else {
//                return systemForumCondition.or(otherForumCondition);
//            }
//        } else {
//            return otherForumCondition;
//        }
//    }
    
    private Condition buildPostQryConditionByUserRelated(User user, Community community) {
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
        Condition systemForumCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
        Long forumId = community.getDefaultForumId();
        if(forumId == null || forumId <= 0) {
            LOGGER.error("No default forum is found for community, system forum will used, userId=" + userId 
                + ", communityId=" + community.getId());
            forumId = ForumConstants.SYSTEM_FORUM;
        }
        systemForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(forumId).and(systemForumCondition);
        
        List<UserGroup> userGroupList = userProvider.listUserGroups(user.getId(), GroupDiscriminator.GROUP.getCode());
        List<Long> forumIdList = new ArrayList<Long>();
        for(UserGroup userGroup : userGroupList) {
            Group group = groupProvider.findGroupById(userGroup.getGroupId());
            // 不查私有圈对应的论坛帖
            if(group != null && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC 
                && group.getOwningForumId() != null) {
                forumIdList.add(group.getOwningForumId());
            }
        }
        
        Condition otherForumCondition = null;
        if(forumIdList.size() > 0) {
            otherForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIdList);
        }
        
        if(otherForumCondition == null) {
            return systemForumCondition;
        } else {
            return systemForumCondition.or(otherForumCondition);
        }
    }
    
    private Condition buildPostVisibilityQryCondition(User user, Community community, Map<String, Long> gaRegionIdMap, VisibilityScope scope) {
        Condition simpleUserCondition = buildPostQryConditionBySimpleUser(community, scope);
        Condition gaRelatedCondition = buildGaRelatedPostQryConditionByUser(user.getId(), community, gaRegionIdMap);
        Condition assignedScopeCondition = buildPostQryConditionByAssignedScope(community);
        
        return simpleUserCondition.or(gaRelatedCondition).or(assignedScopeCondition);
    }
    
    private Condition buildPostQryConditionBySimpleUser(Community community, VisibilityScope scope) {
    	long communityId = community.getId();;
    	
        Condition c1 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        c1 = c1.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.USER.getCode()));
        c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        switch(scope) {
        case COMMUNITY:
            c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
            break;
        default: // default to query topics in nearby communities
            List<Long> nearbyCmntyIdList = new ArrayList<Long>();
            List<Community> nearbyCmntyList = new ArrayList<Community>();
            if(community.getStatus() == CommunityAdminStatus.ACTIVE.getCode())
            	nearbyCmntyList = communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(), communityId);
            
            for(Community c : nearbyCmntyList) {
                nearbyCmntyIdList.add(c.getId());
            }
            
            List<Long> tmpCmntyIdList = new ArrayList<Long>();
            tmpCmntyIdList.add(communityId);
            if(nearbyCmntyIdList != null && nearbyCmntyIdList.size() > 0) {
                tmpCmntyIdList.addAll(nearbyCmntyIdList);
            }
            c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(tmpCmntyIdList));
        }
        
        return c1;
    }
    
    /**
     * 普通用户查询政府相关帖子，包含两种情况：
     * 1、普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
     * 2、物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的
     * @param userId 用户ID
     * @param community 用户所在小区
     * @param gaRegionIdMap 政府机构类型和区域ID的映射表
     * @return 查询条件
     */
    private Condition buildGaRelatedPostQryConditionByUser(long userId, Community community, Map<String, Long> gaRegionIdMap) {
        long communityId = community.getId();
                
        // 普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
        Condition c1 = Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.PM.getCode());
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GARC.getCode()));
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GANC.getCode()));
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GAPS.getCode()));
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GACW.getCode()));


        Condition c2 = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.eq(PostPrivacy.PUBLIC.getCode());
        c2 = c2.or(Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId));
        
        Condition c3 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        c3 = c3.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
        c3 = c1.and(c2).and(c3);
        
        
        // 物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的
        Condition pmCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.PM.getCode());
        pmCondition = pmCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        pmCondition = pmCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.PM.getCode())));
        
        Condition garcCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GARC.getCode());
        garcCondition = garcCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        garcCondition = garcCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GARC.getCode())));
        
        Condition gancCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GANC.getCode());
        gancCondition = gancCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()));
        gancCondition = gancCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GANC.getCode())));
        
        Condition gapsCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GAPS.getCode());
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()));
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GAPS.getCode())));
        
        Condition gacwCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GACW.getCode());
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()));
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GACW.getCode())));
        
        return c3.or(pmCondition).or(pmCondition).or(garcCondition).or(gancCondition).or(gapsCondition).or(gacwCondition);
    }

    
    /**
     * 普通用户查询政府相关帖子，包含两种情况：
     * 1、普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
     * 2、物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的
     * @param userId 用户ID
     * @param community 用户所在小区
     * @param gaRegionIdMap 政府机构类型和区域ID的映射表
     * @return 查询条件
     */
    private Condition buildGaRelatedPostQryConditionByCategory(Long userId, PostEntityTag entityTag, Community community, Map<String, Long> gaRegionIdMap) {
        // 普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
        // 普通用户发的帖均在自己小区内（不管是发给物业的还是给公安的）
        Condition userCondition1 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        userCondition1 = userCondition1.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(entityTag.getCode()));
        Condition userCondition2 = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.eq(PostPrivacy.PUBLIC.getCode());
        userCondition2 = userCondition2.or(Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId));
        Condition userCondition3 = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        userCondition3 = userCondition3.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(community.getId()));
        Condition userCondition = userCondition1.and(userCondition2).and(userCondition3);
                
        // 物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的（所发的区域ID有可能是小区ID也有可能是片区ID）
        Condition gaCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(entityTag.getCode());
        Condition regionCondition = null;
        switch(entityTag) {
        case PM:
        case GARC:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            break;
        case GANC:
        case GAPS:
        case GACW:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            break;
        default:
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Entity tag is not supported, userId=" + userId + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
            }
        }
        if(regionCondition != null) {
            regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(entityTag.getCode())));
            gaCondition = gaCondition.and(regionCondition);
        } else {
            LOGGER.error("Region condition is null, userId=" + userId + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
            return null;
        }
        
        return userCondition.or(gaCondition);
    }
    
    /**
     * 机构自己查自己可看的帖子列表
     * @param userId 用户ID
     * @param organization 机构
     * @param communityIdList 机构管理的小区列表
     * @return 条件
     */
    private Condition buildGaRelatedPostQryConditionByOrganization(Long userId, Organization organization, List<Long> communityIdList) {
        OrganizationType orgType = OrganizationType.fromCode(organization.getOrganizationType());
        if(orgType == null) {
            LOGGER.error("Unsupported organization type, userId=" + userId + ", organization=" + organization);
            return null;
        }
        List<Long> forumIds = new ArrayList<Long>();
        for (Long commnityId : communityIdList) {
        	 Community community = communityProvider.findCommunityById(commnityId);
        	 forumIds.add(community.getDefaultForumId());
		}
       
        
        // 普通用户发给该机构的（物业/业委/居委/公安/社区工作站等），该机构都能看得到（不管是否公开）
        Condition userCondition1 = Tables.EH_FORUM_POSTS.TARGET_TAG.eq(orgType.getCode());
        Condition userCondition3 = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        userCondition3 = userCondition3.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(communityIdList));
       
        Condition userCondition = userCondition1.and(userCondition3);
        if(0 == forumIds.size()){
        	userCondition = userCondition.and(Tables.EH_FORUM_POSTS.FORUM_ID.eq(ForumConstants.SYSTEM_FORUM));
        }else{
        	userCondition = userCondition.and(Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIds));
        }
        // 机构（物业/业委/居委/公安/社区工作站等）自己发的，该机构都能看得到
        Condition gaCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(orgType.getCode());
        Condition regionCondition = null;
        switch(orgType) {
        case PM:
        case GARC:
            Condition conditionCommnity = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            conditionCommnity = conditionCommnity.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(communityIdList));
            Condition conditionOrg = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            conditionOrg = conditionOrg.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(organization.getId()));
            regionCondition = conditionCommnity.or(conditionOrg);
            
            gaCondition = gaCondition.and(regionCondition);
            break;
        case GANC:
        case GAPS:
        case GACW:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(organization.getId()));
            gaCondition = gaCondition.and(regionCondition);
            break;
        default:
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Entity tag is not supported, userId=" + userId + ", organizationId=" + organization.getId() 
                    + ", communityIdList=" + communityIdList);
            }
        }
        
        return userCondition.or(gaCondition);
    }
    
    private Condition buildPostQryConditionByAssignedScope(Community community) {
        long communityId = community.getId();
        
        Condition c1 = Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_CODE.eq(VisibilityScope.COMMUNITY.getCode());
        c1 = c1.and(Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_ID.eq(communityId));
        
        Condition c2 = Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_CODE.eq(VisibilityScope.CITY.getCode());
        c2 = c2.and(Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_ID.eq(community.getCityId()));

        Condition c3 = Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_CODE.eq(VisibilityScope.ALL.getCode());
        
        return c1.or(c2).or(c3);
    }
    
    private Condition notEqPostCategoryCondition(List<Long> unCategorys, Long embeddedAppId) {
    	return notEqPostCategoryCondition(unCategorys, embeddedAppId, null);
    }
    
    private Condition notEqPostCategoryCondition(List<Long> unCategorys, Long embeddedAppId, Long contentCategory) {
    	// 排除掉公告，直接在这添加，客户端和前端不用改，add by tt, 170223
    	Condition condition = null;
    	if (contentCategory == null || contentCategory.longValue() != CategoryConstants.CATEGORY_ID_NOTICE) {
    		Category category = this.categoryProvider.findCategoryById(CategoryConstants.CATEGORY_ID_NOTICE);
    		if (category != null) {
    			condition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.notLike(category.getPath() + "%");
    		}
		}
    	
    	//所有查帖子的地方排除掉官方活动，但不排除个人活动，add by tt, 160720
    	//但是查官方活动的时候不能排除
    	if (embeddedAppId != null && embeddedAppId.longValue() == AppConstants.APPID_ACTIVITY) {
			return condition;
		}
    	
    	if (condition == null) {
			return Tables.EH_FORUM_POSTS.OFFICIAL_FLAG.eq(OfficialFlag.NO.getCode());
		}
    	return condition.and(Tables.EH_FORUM_POSTS.OFFICIAL_FLAG.eq(OfficialFlag.NO.getCode()));
    	
//    	if(null == unCategorys || 0 == unCategorys.size()){
//       	 	unCategorys = new ArrayList<Long>();
//        }
//        
//        Condition condition = null;
//        // contentCategoryId为0表示全部查，此时也不需要给category条件
//        for (Long categoryId : unCategorys) {
//        	Category contentCatogry = this.categoryProvider.findCategoryById(categoryId);
//            if(contentCatogry != null) {
//                if(null == condition){
//                	condition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.notLike(contentCatogry.getPath() + "%");
//                }else{
//    				//此处应该用and, add by tangtong，20160712
//                	condition = condition.or(Tables.EH_FORUM_POSTS.CATEGORY_PATH.notLike(contentCatogry.getPath() + "%"));
//                }
//                	 
//            }
//		}
//        return condition;
    }
    
    private Condition buildPostCategoryCondition(List<Long> categorys, Long actionCategoryId) {
        Condition contentCategoryCondition = null;
        // contentCategoryId为0表示全部查，此时也不需要给category条件
        if(categorys != null && categorys.size() > 0) {
        	for (Long categoryId : categorys) {
        		 Category contentCatogry = this.categoryProvider.findCategoryById(categoryId);
                 if(contentCatogry != null) {
                	 if(null == contentCategoryCondition){
                		 contentCategoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
                	 }else{
                		 contentCategoryCondition = contentCategoryCondition.or(Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%"));
                	 }
                	 
                 }
                 
                 
			}
           
        }
        
        Condition actionCategoryCondition = null;
        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
            Category actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
            if(actionCategory != null) {
            	actionCategoryCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
            }
        }
        
        Condition categoryCondition = null;
        if(null != contentCategoryCondition){
        	if(null !=  actionCategoryCondition){
        		categoryCondition = contentCategoryCondition.and(actionCategoryCondition);
        	}else{
        		categoryCondition = contentCategoryCondition;
        	}
        }
        
        return categoryCondition;
    }
    
    /**
     * 填充帖子中每个帖子的信息，主要是补充发帖/评论人信息和附件信息
     * @param userId 查帖人
     * @param postList 帖子/评论列表
     * @param communityId 当前用户所在小区ID
     * @param isDetail 是否查的是详情（详情的填充内容和列表填充内容略有不同，后者简略一些）
     */
    private void populatePosts(long userId, List<Post> postList, Long communityId, boolean isDetail) {
        if(postList == null || postList.size() == 0) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post list is empty, userId=" + userId);
            }
            return;
        } else {
            for(Post post : postList) {
                populatePost(userId, post, communityId, isDetail);
            }
        }
    }
    
    private void populatePost(long userId, Post post, Long communityId, boolean isDetail) {
        populatePost(userId, post, communityId, isDetail, false);
    }
    
    /**
     * 填充帖子信息，主要是补充发帖/评论人信息和附件信息
     * @param userId 查帖人
     * @param post 帖子/评论
     * @param communityId 用户当前所在的小区ID
     * @param isDetail 是否查的是详情（详情的填充内容和列表填充内容略有不同，后者简略一些）
     */
    private void populatePost(long userId, Post post, Long communityId, boolean isDetail, boolean getOwnTopics) {
        long startTime = System.currentTimeMillis();
        if(post == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post is null, userId=" + userId);
            }
        } else {
            try {
                Long embededAppId = post.getEmbeddedAppId();
                ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
                if(handler != null) {
                    String snapshot = null;
                    if(isDetail) {
                        snapshot = handler.renderEmbeddedObjectDetails(post);
                    } else {
                        snapshot = handler.renderEmbeddedObjectSnapshot(post);
                    }
                    post.setEmbeddedJson(snapshot);
                }


                
                post.setCommunityId(communityId);
                
                populatePostCreatorCommunityName(post);
                
                populatePostCreatorInfo(userId, post);
                
                populatePostAttachements(userId, post, post.getAttachments());
                
                populatePostStatus(userId, post, getOwnTopics);
                
                populatePostRegionInfo(userId, post);
                
                populatePostForumNameInfo(userId, post);
                
                processLocation(post);

                //添加ownerToken, 当前字段在评论时使用 add by yanjun 20170601
                populateOwnerToken(post);

                //添加是否支持评论字段 add by yanjun 20171025
                Byte flag = getInteractFlagByPost(post);
                post.setInteractFlag(flag);
                
                String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
                String relativeUrl = configProvider.getValue(ConfigConstants.POST_SHARE_URL, "");

                Integer namespaceId = post.getNamespaceId();
                if(namespaceId == null){
                    namespaceId = UserContext.getCurrentNamespaceId();
                }

                if(homeUrl.length() == 0 || relativeUrl.length() == 0) {
                    LOGGER.error("Invalid home url or post sharing url, homeUrl=" + homeUrl
                        + ", relativeUrl=" + relativeUrl + ", postId=" + post.getId());
                } else {
                    //单独处理活动的分享链接 modified by xiongying 20160622
                    if((post.getCategoryId() != null && post.getCategoryId() == 1010) || (post.getEmbeddedAppId() != null && post.getEmbeddedAppId().equals(AppConstants.APPID_ACTIVITY))) {
                        ActivityDTO activity = activityService.findSnapshotByPostId(post.getId());
                        relativeUrl = configProvider.getValue(ConfigConstants.ACTIVITY_SHARE_URL, "");
//                		ActivityTokenDTO dto = new ActivityTokenDTO();
//                		dto.setPostId(post.getId());
//                		dto.setForumId(post.getForumId());
//                		String encodeStr = WebTokenGenerator.getInstance().toWebToken(dto);
//                		post.setShareUrl(homeUrl + relativeUrl + "?id=" + encodeStr);

                        //改用直接传输的方式。因为增加微信报名活动后，涉及到报名取消支付等操作，这些操作都要编码的话，工作量会巨大。
                        //添加命名空间ns
                        // 增加是否支持微信报名wechatSignup  add by yanjun 20170620
                        Byte wechatSignup = 0;
                        if(activity != null && activity.getWechatSignup() != null){
                            wechatSignup = activity.getWechatSignup();
                            post.setShareUrl(homeUrl + relativeUrl + "?namespaceId=" + namespaceId + "&forumId=" + post.getForumId() + "&topicId=" + post.getId() + "&wechatSignup=" + wechatSignup + "&categoryId=" + activity.getCategoryId());
                        }else {
                            post.setShareUrl(homeUrl + relativeUrl + "?namespaceId=" + namespaceId + "&forumId=" + post.getForumId() + "&topicId=" + post.getId() + "&wechatSignup=" + wechatSignup);
                        }
                	} else if(post.getCategoryId() != null && post.getCategoryId() == CategoryConstants.CATEGORY_ID_TOPIC_POLLING) {
                        //投票帖子用自己的分享链接 modified by yanjun 220171227
                        relativeUrl = configProvider.getValue(ConfigConstants.POLL_SHARE_URL, "");
                	    post.setShareUrl(homeUrl + relativeUrl + "?namespaceId=" + namespaceId + "&forumId=" + post.getForumId() + "&topicId=" + post.getId()  + "&communityId=" + communityId);
                    }else {
                	    if (embededAppId != null && embededAppId.equals(AppConstants.APPID_LINK)) {
                            Link link = linkProvider.findLinkByPostId(post.getId());
                            if(link != null && LinkContentType.FORWARD.getCode().equals(link.getContentType())){
                                relativeUrl = configProvider.getValue(ConfigConstants.POST_LINK_SHARE_URL, "");
                            }
                        }
                		post.setShareUrl(homeUrl + relativeUrl + "?namespaceId=" + namespaceId + "&forumId=" + post.getForumId() + "&topicId=" + post.getId() + "&communityId=" + communityId + "&userId=" + userId);
                	}
                }
            } catch(Exception e) {
                LOGGER.error("Failed to populate the post info, userId=" + userId + ", postId=" + post.getId(), e);
            }
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Populate post, userId=" + userId + ", postId=" + post.getId() + ", elapse=" + (endTime - startTime));
        }
    }

    /**
     *添加是否支持评论字段 add by yanjun 20171025
     * @param post
     */
    @Override
    public Byte getInteractFlagByPost(Post post){
        //如果帖子时关闭评论的，直接是关闭
        if(post.getInteractFlag() != null && InteractFlag.fromCode(post.getInteractFlag()) == InteractFlag.UNSUPPORT){
            return InteractFlag.UNSUPPORT.getCode();
        }

        //帖子时开放的，查看入口配置，没有配置或者配置成1的话是开放的，其他时候关闭
        InteractSetting setting = findInteractSettingByPost(post);
        if(setting == null || InteractFlag.fromCode(setting.getInteractFlag()) == InteractFlag.SUPPORT){
            return InteractFlag.SUPPORT.getCode();
        }else {
            return InteractFlag.UNSUPPORT.getCode();
        }
    }


    @Override
    public InteractSetting findInteractSettingByPost(Post post){
//        InteractSetting setting = null;

        if(post.getModuleType() == null){
            return null;
        }

        Integer namespaceId = post.getNamespaceId();
        if(namespaceId == null){
            namespaceId = UserContext.getCurrentNamespaceId();
        }


        InteractSetting setting = forumProvider.findInteractSetting(namespaceId, post.getModuleType(), post.getModuleCategoryId());


//        //非常不靠谱的判断，可是真的没有其他办法，急需在创建帖子的时候从来源处传来是哪个应用的  add by yanjun 20171109
//        if(post.getActivityCategoryId() != null && post.getActivityCategoryId().longValue() != 0){
//            //活动应用的帖子
//            setting = forumProvider.findInteractSetting(namespaceId, post.getForumId(), InteractSettingType.ACTIVITY.getCode(), post.getActivityCategoryId());
//        }else if(post.getForumEntryId() != null && (post.getCategoryId() == null || post.getCategoryId() != 1003)){
//            //论坛应用的帖子
//            setting = forumProvider.findInteractSetting(namespaceId, post.getForumId(), InteractSettingType.FORUM.getCode(), post.getForumEntryId());
//        }else if(post.getCategoryId() != null && post.getCategoryId() == 1003){
//            //公告应用的帖子
//            setting = forumProvider.findInteractSetting(namespaceId, post.getForumId(), InteractSettingType.ANNOUNCEMENT.getCode(), null);
//        }

        return setting;
    }

    /**
     *添加ownerToken, 当前字段在评论时使用 add by yanjun 20170601
     * @param post
     */
    private void populateOwnerToken(Post post){
        OwnerTokenDTO ownerTokenDto = new OwnerTokenDTO();
        ownerTokenDto.setId(post.getId());
        ownerTokenDto.setType(OwnerType.FORUM.getCode());

        String ownerTokenStr = WebTokenGenerator.getInstance().toWebToken(ownerTokenDto);
        post.setOwnerToken(ownerTokenStr);
    }

    private void processLocation(Post post){
    	// 如果安卓没获取到经纬度会传特别小的数字过来，IOS无法解析出来，add by tt, 20161021
    	if (post != null && post.getLatitude() != null && post.getLongitude() != null
    			&& Math.abs(post.getLatitude().doubleValue()) < 0.000000001 && Math.abs(post.getLongitude().doubleValue()) < 0.000000001) {
			post.setLatitude(null);
			post.setLongitude(null);
		}
    }
    
//    private void populatePostCreatorInfo(long userId, Post post) {
//        Long forumId = post.getForumId();
//        if(forumId != null) {
//            String creatorNickName = post.getCreatorNickName();
//            String creatorAvatar = post.getCreatorAvatar();
//            
//            // 社区论坛的意见反馈论坛直接使用USER表中的信息作为发帖人的信息
//            if(forumId == ForumConstants.SYSTEM_FORUM || forumId == ForumConstants.FEEDBACK_FORUM) {
//                User creator = userProvider.findUserById(post.getCreatorUid());
//                if(creator != null) {
//                    // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
//                    if(creatorNickName == null || creatorNickName.trim().length() == 0) {
//                        post.setCreatorNickName(creator.getNickName());
//                    }
//                    if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
//                        post.setCreatorAvatar(creator.getAvatar());
//                    }
//                }
//                // TODO: set the admin flag of system forum
//            } else {
//                // 普通圈使用圈成员的信息
//                Forum forum = forumProvider.findForumById(forumId);
//                if(forum != null && EntityType.GROUP.getCode().equalsIgnoreCase(forum.getOwnerType())) {
//                    GroupMember member = groupProvider.findGroupMemberByMemberInfo(forum.getOwnerId(), 
//                        EntityType.USER.getCode(), post.getCreatorUid());
//                    if(member != null) {
//                        String nickName = member.getMemberNickName();
//                        String avatar = member.getMemberAvatar();
//                        if(nickName == null || nickName.trim().length() == 0) {
//                            User creator = userProvider.findUserById(post.getCreatorUid());
//                            if(creator != null) {
//                                nickName = creator.getNickName();
//                            }
//                        }
//                        if(avatar == null || avatar.trim().length() == 0){
//                        	User creator = userProvider.findUserById(post.getCreatorUid());
//                        	if(creator != null) {
//                        		avatar = creator.getAvatar();
//                            }
//                        }
//                        // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
//                        if(creatorNickName == null || creatorNickName.trim().length() == 0) {
//                            post.setCreatorNickName(nickName);
//                        }
//                        if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
//                            post.setCreatorAvatar(avatar);
//                        }
//                    }
//                }
//            }
//            
//            creatorAvatar = post.getCreatorAvatar();
//            if(creatorAvatar != null && creatorAvatar.length() > 0) {
//                String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, 
//                    EntityType.USER.getCode(), post.getCreatorUid());
//                post.setCreatorAvatarUrl(avatarUrl);
//            }
//        }
//        
//        UserLike userLike = this.userProvider.findUserLike(userId, EntityType.POST.getCode(), post.getId());
//        if(userLike != null && UserLikeType.LIKE == UserLikeType.fromCode(userLike.getLikeType())) {
//            post.setLikeFlag(UserLikeType.LIKE.getCode());
//        } else {
//            post.setLikeFlag(UserLikeType.NONE.getCode());
//        }
//    }
    
    //填充小区名
    private void populatePostCreatorCommunityName(Post post){
    	User user  = userProvider.findUserById(post.getCreatorUid());
    	if(user != null && user.getCommunityId() != null){
    		Community community = communityProvider.findCommunityById(user.getCommunityId());
    		if(community != null){
    			post.setCreatorCommunityName(community.getName());
    		}
    	}
    }

    private void populatePostCreatorInfo(long userId, Post post) {
        String creatorNickName = "";
        String creatorAvatar = "";
        //直接使用登录用户的头像和昵称
        User creator = userProvider.findUserById(post.getCreatorUid());
        if(creator != null) {
            creatorNickName = creator.getNickName();
            creatorAvatar = creator.getAvatar();
        }

        post.setCreatorNickName(creatorNickName);
        post.setCreatorAvatar(creatorAvatar);
        /*解决web 帖子头像问题，当帖子创建者没有头像时，取默认头像   by sw */
        if(StringUtils.isEmpty(creatorAvatar)) {

            //防止creator空指针  add by yanjun 20171011
            Integer namespaceId = 0;
            if(creator != null && creator.getNamespaceId() != null){
                namespaceId = creator.getNamespaceId();
            }
        	creatorAvatar = configProvider.getValue(namespaceId, "user.avatar.default.url", "");
        }
        
        if(creatorAvatar != null && creatorAvatar.length() > 0) {
            String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, 
                EntityType.USER.getCode(), post.getCreatorUid());
            post.setCreatorAvatarUrl(avatarUrl);
        }
        
        UserLike userLike = this.userProvider.findUserLike(userId, EntityType.POST.getCode(), post.getId());
        if(userLike != null && UserLikeType.LIKE == UserLikeType.fromCode(userLike.getLikeType())) {
            post.setLikeFlag(UserLikeType.LIKE.getCode());
        } else {
            post.setLikeFlag(UserLikeType.NONE.getCode());
        }
    }
    
    private void populatePostStatus(long userId, Post post, boolean getOwnTopics) {
        if(getOwnTopics && post.getCreatorUid().equals(userId)) {
            return;
        }
        
        if(PostStatus.INACTIVE == PostStatus.fromCode(post.getStatus())) {
            User user = this.userProvider.findUserById(userId);
            String locale = user.getLocale();
            String scope = ForumLocalStringCode.SCOPE;
            int code = 0;
            String content = null;
            if(post.getParentPostId() == null || post.getParentPostId().longValue() == 0) {
                code = ForumLocalStringCode.FORUM_TOPIC_DELETED;
                content = localeStringService.getLocalizedString(scope, String.valueOf(code), locale, "");
            } else {
                code = ForumLocalStringCode.FORUM_COMMENT_DELETED;
                content = localeStringService.getLocalizedString(scope, String.valueOf(code), locale, "");
            }
            post.setContent(content);
            post.setContentAbstract(content);
            
            post.setEmbeddedAppId(null);
            post.setEmbeddedId(null);
            post.setEmbeddedJson(null);
            post.setEmbeddedVersion(0);
            post.setAttachments(null);
        }
    }
    
    private void populatePostRegionInfo(long userId, Post post) {
    	// 根据产品姚绮云要求，不要显示@xxxx, add by tt, 20170307
//        VisibleRegionType regionType = VisibleRegionType.fromCode(post.getVisibleRegionType());
//        Long regionId = post.getVisibleRegionId();
//        
//        if(regionType != null && regionId != null) {
//            String creatorNickName = post.getCreatorNickName();
//            if(creatorNickName == null) {
//                creatorNickName = "";
//            }
//            switch(regionType) {
//            case COMMUNITY:
//                Community community = communityProvider.findCommunityById(regionId);
//                if(community != null)
//                	creatorNickName = creatorNickName + "@" + community.getName();
//                break;
//            case REGION:
//                Organization organization = organizationProvider.findOrganizationById(regionId);
//                // 根据产品姚绮云要求，当以公司名义发送时，使用公司所入驻的小区，而不是使用公司名称 by lqs 20161217
////                if(organization !=null)
////                	creatorNickName = creatorNickName + "@" + organization.getName();
//                if(organization !=null) {
//                	String regionName = organization.getName();
//                	Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
//                    if(communityId != null) {
//                        community = communityProvider.findCommunityById(communityId);
//                        if(community != null) {
//                            regionName = community.getName();
//                        } else {
//                            LOGGER.error("Community not found, userId={}, communityId={}, , regionType={}, postId={}", userId, communityId, regionType, post.getId());
//                        }
//                    } else {
//                        LOGGER.error("No community id found in organization, organizationId={}", organization.getId());
//                    }
//                    creatorNickName = creatorNickName + "@" + regionName;
//                }
//                break;
//            default:
//                LOGGER.error("Unsupported visible region type, userId=" + userId 
//                    + ", regionType=" + regionType + ", postId=" + post.getId());
//            }
//            post.setCreatorNickName(creatorNickName);
//        } else {
//            LOGGER.error("Region type or id is null, userId=" + userId + ", postId=" + post.getId());
//        }
//        
    }
    
    private void populatePostForumNameInfo(long userId, Post post) {
        Long forumId = post.getForumId();
        Forum forum = forumProvider.findForumById(forumId);

        // 补充namespaceId，使得在分享的时候可以根据域空间ID来获取版本信息以便确定是否要下载APP  by lqs 20170418
        if(forum != null) {
            post.setNamespaceId(forum.getNamespaceId());
        }
        
        if(forumId == ForumConstants.SYSTEM_FORUM) {
            VisibleRegionType regionType = VisibleRegionType.fromCode(post.getVisibleRegionType());
            Long regionId = post.getVisibleRegionId();
            if(regionType != null && regionId != null) {
                String forumName = forum.getName();
                if(forumName == null) {
                    forumName = "";
                }
                switch(regionType) {
                case COMMUNITY:
                    Community community = communityProvider.findCommunityById(regionId);
                    if(community != null)
                        // 按测试要求不加上“社区论坛”这些字样，故需要把forumName去掉，只显示小区名称 by lqs 20160505
                    	//forumName = community.getName() + forumName;
                        forumName = community.getName();
                    break;
                case REGION:
                    Organization organization = organizationProvider.findOrganizationById(regionId);
                    if(organization !=null)
                    	forumName = organization.getName();
                    break;
                default:
                    LOGGER.error("Unsupported visible region type, userId=" + userId 
                        + ", regionType=" + regionType + ", postId=" + post.getId());
                }
                post.setForumName(forumName);
            } else {
                LOGGER.error("Region type or id is null, userId=" + userId + ", postId=" + post.getId());
            }
        } else {
            if(forumId == ForumConstants.FEEDBACK_FORUM) {
                post.setForumName(forum.getName());
            } else {
                Group group = null;
                if(forum.getOwnerId() != null) {
                    group = this.groupProvider.findGroupById(forum.getOwnerId());
                }
                if(group != null) {
                    post.setForumName(group.getName());
                }
            }
        }
    }
    
    private void populatePostAttachements(long userId, Post post, List<Attachment> attachmentList) {
        if(attachmentList == null || attachmentList.size() == 0) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post attachment list is empty, userId=" + userId + ", postId=" + post.getId());
            }
        } else {
            for(Attachment attachment : attachmentList) {
                populatePostAttachement(userId, post, attachment);
            }
        }
    }
    
    private void populatePostAttachement(long userId, Post post, Attachment attachment) {
        if(attachment == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post attachment is null, userId=" + userId + ", postId=" + post.getId());
            }
        } else {
            String contentUri = attachment.getContentUri();
            if(contentUri != null && contentUri.length() > 0) {
                try{
                    String url = contentServerService.parserUri(contentUri, EntityType.TOPIC.getCode(), post.getId());
                    attachment.setContentUrl(url);
                    
                    ContentServerResource resource = contentServerService.findResourceByUri(contentUri);
                    if(resource != null) {
                        attachment.setSize(resource.getResourceSize());
                        attachment.setMetadata(resource.getMetadata());
                    }
                }catch(Exception e){
                    LOGGER.error("Failed to parse attachment uri, userId=" + userId 
                        + ", postId=" + post.getId() + ", attachmentId=" + attachment.getId(), e);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("The content uri is empty, userId=" + userId + ", attchmentId=" + attachment.getId());
                }
            }
        }
    }
    
    private String getResourceUrlByUir(long userId, String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, userId=" + userId + ", uri=" + uri 
                    + ", ownerType=" + ownerType + ", ownerId=" + ownerId, e);
            }
        }
        
        return url;
    }
    
    private ForumEmbeddedHandler getForumEmbeddedHandler(Long embededAppId) {
        ForumEmbeddedHandler handler = null;

        if(embededAppId != null && embededAppId.longValue() > 0) {
            String handlerPrefix = ForumEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + embededAppId);
        }

        return handler;
    }

	@Override
	public SearchTopicAdminCommandResponse searchComment(SearchTopicAdminCommand cmd) {
		PostAdminQueryFilter filter = new PostAdminQueryFilter();
        String keyword = cmd.getKeyword();
        if(!StringUtils.isEmpty(keyword)) {
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CONTENT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_SUBJECT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CREATORNICKNAME);
            filter.setQueryString(keyword);
        }
        
        List<String> phones = cmd.getSenderPhones();
        if(phones != null && phones.size() > 0) {
            filter.includeFilter(PostAdminQueryFilter.TERM_IDENTIFY, phones);
        }
        
//        List<String> nickNames = cmd.getSenderNickNames();
//        if(nickNames != null && nickNames.size() > 0) {
//            filter.includeFilter(PostAdminQueryFilter.TERM_CREATORNICKNAME, nickNames);
//        }
        
        List<Long> parentPostId = new ArrayList<Long>();
        parentPostId.add(0L);
        filter.excludeFilter(PostAdminQueryFilter.TERM_PARENTPOSTID, parentPostId);

        
        Long startTime = cmd.getStartTime();
        if(startTime != null) {
            filter.dateFrom(new Date(startTime));
        }
        
        Long endTime = cmd.getEndTime();
        if(endTime != null) {
            filter.dateTo(new Date(endTime));
        }
          
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        } else {
            pageNum = 0;
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        filter.setPageInfo(pageNum, pageSize);
        ListPostCommandResponse queryResponse = postSearcher.query(filter);
        
        SearchTopicAdminCommandResponse response = new SearchTopicAdminCommandResponse();
        response.setKeywords(queryResponse.getKeywords());
        response.setNextPageAnchor(queryResponse.getNextPageAnchor());
        
        List<PostAdminDTO> adminPostList = new ArrayList<PostAdminDTO>();
        List<PostDTO> postList = queryResponse.getPosts();
        for(PostDTO post : postList) {
            try {
                PostDTO temp = getTopicById(post.getId(), cmd.getCommunityId(), false);
                PostAdminDTO adminPost = ConvertHelper.convert(temp, PostAdminDTO.class);
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(temp.getCreatorUid(),
                    IdentifierType.MOBILE.getCode());
                if(identifier != null) {
                    adminPost.setCreatorPhone(identifier.getIdentifierToken());
                }
                
                List<FamilyDTO> families = familyProvider.getUserFamiliesByUserId(temp.getCreatorUid());
                if(families != null && families.size() > 0){
                	String address = families.get(0).getAddress();
                	adminPost.setCreatorAddress(address);
                }
                
                adminPostList.add(adminPost);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        response.setPosts(adminPostList);
        return response;
	}

//    @Override
//	public PostDTO createTopicByScene(NewTopicBySceneCommand cmd) {
//	    User user = UserContext.current().getUser();
//	    Long userId = user.getId();
//	    SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
//	    
//	    NewTopicCommand topicCmd = ConvertHelper.convert(cmd, NewTopicCommand.class);
//	    
//	    PostEntityTag creatorTag = PostEntityTag.USER;
//	    VisibleRegionType visibleRegionType = null;
//	    Long visibleRegionId = null;
//	    UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
//	    switch(entityType) {
//	    case COMMUNITY_RESIDENTIAL:
//	    case COMMUNITY_COMMERCIAL:
//	    case COMMUNITY:
//	        visibleRegionType = VisibleRegionType.COMMUNITY;
//	        visibleRegionId = sceneToken.getEntityId();
//	        break;
//	    case FAMILY:
//	        FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
//	        if(family != null) {
//	            visibleRegionType = VisibleRegionType.COMMUNITY;
//	            visibleRegionId = family.getCommunityId();
//	        } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
//                }
//            }
//	        break;
//	    case ORGANIZATION:
//	    	
//            Organization org = this.organizationProvider.findOrganizationById(sceneToken.getEntityId());
//            if(org != null) {
//            	String orgType = org.getOrganizationType();
//                
//                if(OrganizationType.isGovAgencyOrganization(orgType)) {
//                	if(VisibleRegionType.fromCode(cmd.getVisibleRegionType()) == VisibleRegionType.COMMUNITY){
//        	    		creatorTag = PostEntityTag.fromCode(orgType);
//            			visibleRegionType = VisibleRegionType.COMMUNITY;
//                        visibleRegionId = cmd.getVisibleRegionId();
//            		}else{
//            			creatorTag = PostEntityTag.fromCode(orgType);
//                        visibleRegionType = VisibleRegionType.REGION;
//                        visibleRegionId = sceneToken.getEntityId();
//            		}
//                    
//                }
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Organization not found, sceneToken=" + sceneToken);
//                }
//            }
//	        break;
//	    default:
//	        break;
//	    }
//	    
//	    if(creatorTag != null) {
//	        topicCmd.setCreatorTag(creatorTag.getCode());
//	    }
//	    if(visibleRegionType != null) {
//	        topicCmd.setVisibleRegionType(visibleRegionType.getCode());
//	    }
//	    topicCmd.setVisibleRegionId(visibleRegionId);
//	    
//	    return this.createTopic(topicCmd);
//	}

    private void checkCreateTopicPrivilege(String ownerType, Long ownerId, Long categoryId, Long currentOrgId){
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");

//        //至少有一项不能为空 才校验权限
//        if(null == EntityType.fromCode(ownerType) && null == ownerId && null == currentOrgId){
//            return;
//        }
        // 对接权限，先去掉权限校验 -- by yanjun
//        if(categoryId != null && CategoryConstants.CATEGORY_ID_NOTICE == categoryId){
//            resolver.checkUserAuthority(UserContext.current().getUser().getId(), ownerType, ownerId, currentOrgId, PrivilegeConstants.PUBLISH_NOTICE_TOPIC);
//        }
    }

    private void checkBlacklist(String ownerType, Long ownerId, Long categoryId, Long forumId){
        ownerType = StringUtils.isEmpty(ownerType) ? "" : ownerType;
        ownerId = null == ownerId ? 0L : ownerId;
        Long userId = UserContext.current().getUser().getId();
        Long privilegeId = null;
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if(null == categoryId || -1 == categoryId || 1 == categoryId || CategoryConstants.CATEGORY_ID_TOPIC_COMMON == categoryId|| CategoryConstants.CATEGORY_ID_TOPIC_POLLING == categoryId){
            privilegeId = PrivilegeConstants.BLACKLIST_COMMON_POLLING_POST;
        }else if(CategoryConstants.CATEGORY_ID_NOTICE == categoryId){
            privilegeId = PrivilegeConstants.BLACKLIST_NOTICE_POST;
        }else if(CategoryConstants.GA_PRIVACY_CATEGORIES.contains(categoryId)){
            privilegeId = PrivilegeConstants.BLACKLIST_PROPERTY_POST;
        }else if(CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY == categoryId){
            privilegeId = PrivilegeConstants.BLACKLIST_ACTIVITY_POST;
        }else{
            privilegeId = PrivilegeConstants.BLACKLIST_COMMON_POLLING_POST;

        }


        if(null != privilegeId){
            resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_ACTIVITY_POST);
        }

        //校验意见反馈论坛黑名单
        if(null != forumId){
            List<Community> communities = communityProvider.listCommunitiesByFeedbackForumId(forumId);
            if(communities.size() > 0){
                resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_FEEDBACK_FORUM);
            }
        }
    }

    @Override
    public PostDTO createTopicByScene(NewTopicBySceneCommand cmd) {
        //xss过滤
        String content = XssCleaner.clean(cmd.getContent());
        cmd.setContent(content);

        User user = UserContext.current().getUser();
        Long userId = user.getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        
        NewTopicCommand topicCmd = ConvertHelper.convert(cmd, NewTopicCommand.class);

        PostEntityTag creatorTag = PostEntityTag.USER;
        VisibleRegionType visibleRegionType = null;
        Long visibleRegionId = null;
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        Long currentOrgId = null;
        switch(sceneType) {
        case DEFAULT:
        case PARK_TOURIST:
            visibleRegionType = VisibleRegionType.COMMUNITY;
            visibleRegionId = sceneToken.getEntityId();
            
            // 在园区场景下，客户端可能使用错误的社区论坛（甚至不传），
            if(topicCmd.getForumId() == null || topicCmd.getForumId() == ForumConstants.SYSTEM_FORUM) {
                setCurrentForumId(topicCmd, visibleRegionId);
            }
            break;
        case FAMILY:
            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
            if(family != null) {
                visibleRegionType = VisibleRegionType.COMMUNITY;
                visibleRegionId = family.getCommunityId();
                
                if(topicCmd.getForumId() == null || topicCmd.getForumId() == ForumConstants.SYSTEM_FORUM) {
                    setCurrentForumId(topicCmd, visibleRegionId);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
                }
            }
            break;
        case PM_ADMIN:// 无小区ID
        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
            Organization org = this.organizationProvider.findOrganizationById(sceneToken.getEntityId());
            currentOrgId = org.getId();
            if(org != null) {
                String orgType = org.getOrganizationType();
                
                // 以前由于只有物业管理员场景故需要进行管理员判断，后来加上普通企业之后，就不需要进行这个判断；
                // 客户端会从发送范围里把visible_region_type/id传过来，如果不传则说明是偏老一点的版本，此时使用REGION类型 by lqs 20160601
//                if(OrganizationType.isGovAgencyOrganization(orgType)) {
//                    if(VisibleRegionType.fromCode(cmd.getVisibleRegionType()) == VisibleRegionType.COMMUNITY){
//                    	creatorTag = PostEntityTag.fromCode(orgType);
//                    	if(OrganizationType.fromCode(orgType) == OrganizationType.ENTERPRISE){
//                    		creatorTag = PostEntityTag.USER;
//                    	}
//                        visibleRegionType = VisibleRegionType.COMMUNITY;
//                        visibleRegionId = cmd.getVisibleRegionId();
//                    }else{
//                        creatorTag = PostEntityTag.fromCode(orgType);
//                        visibleRegionType = VisibleRegionType.REGION;
//                        visibleRegionId = sceneToken.getEntityId();
//                    }
//                    
//                }
                visibleRegionType = VisibleRegionType.fromCode(cmd.getVisibleRegionType());
                visibleRegionType = (visibleRegionType == null) ? VisibleRegionType.REGION : visibleRegionType;
                visibleRegionId = cmd.getVisibleRegionId();

                if(cmd.getVisibleRegionIds() == null || cmd.getVisibleRegionIds().size() == 0){
                    visibleRegionId = (visibleRegionId == null) ? org.getId() : visibleRegionId;
                }

                if(OrganizationType.isGovAgencyOrganization(orgType)) {
//                    if(VisibleRegionType.fromCode(cmd.getVisibleRegionType()) == VisibleRegionType.COMMUNITY){
//                    	creatorTag = PostEntityTag.fromCode(orgType);
//                    	if(OrganizationType.fromCode(orgType) == OrganizationType.ENTERPRISE){
//                    		creatorTag = PostEntityTag.USER;
//                    	}
//                        visibleRegionType = VisibleRegionType.COMMUNITY;
//                        visibleRegionId = cmd.getVisibleRegionId();
//                    }else{
//                        creatorTag = PostEntityTag.fromCode(orgType);
//                        visibleRegionType = VisibleRegionType.REGION;
//                        visibleRegionId = sceneToken.getEntityId();
//                    }
                    
                    creatorTag = PostEntityTag.fromCode(orgType);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Organization not found, sceneToken=" + sceneToken);
                }
            }
            break;
        default:
            break;
        }
        if(visibleRegionType == VisibleRegionType.COMMUNITY){
            topicCmd.setOwnerType(EntityType.COMMUNITY.getCode());
            topicCmd.setOwnerId(visibleRegionId);
        }
        topicCmd.setCurrentOrgId(currentOrgId);
        topicCmd.setStatus((byte)2);
        if (cmd.getEmbeddedAppId() != null && cmd.getEmbeddedAppId().equals(AppConstants.APPID_ACTIVITY) && !StringUtils.isEmpty(topicCmd.getEmbeddedJson())) {
            ActivityPostCommand tempCmd = (ActivityPostCommand) StringHelper.fromJsonString(topicCmd.getEmbeddedJson(),
                    ActivityPostCommand.class);
            if (tempCmd != null) {
                tempCmd.setStatus((byte)2);
                topicCmd.setEmbeddedJson(StringHelper.toJsonString(tempCmd));
            }
        }
        if(creatorTag != null) {
            topicCmd.setCreatorTag(creatorTag.getCode());
        }
        if(visibleRegionType != null) {
            topicCmd.setVisibleRegionType(visibleRegionType.getCode());
        }
        topicCmd.setVisibleRegionId(visibleRegionId);
        
        return this.createTopic(topicCmd);
    }
    
    @Override
    public ListPostCommandResponse listNoticeByScene(ListNoticeBySceneCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
//        List<Long> visibleRegionIds = new ArrayList<Long>();
//        VisibleRegionType visibleRegionType = VisibleRegionType.COMMUNITY;
//        switch(sceneType) {
//        case DEFAULT:
//        case PARK_TOURIST:
//        	visibleRegionIds.add(sceneToken.getEntityId());
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
//            if(family != null) {
//                visibleRegionIds.add(family.getCommunityId());
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
//                }
//            }
//            break;
//        case PM_ADMIN:// 无小区ID
//        case ENTERPRISE: 
//        case ENTERPRISE_NOAUTH: 
//            Organization org = this.organizationProvider.findOrganizationById(sceneToken.getEntityId());
//            if(org != null) {
//
//            	if(OrganizationType.ENTERPRISE == OrganizationType.fromCode(org.getOrganizationType())){
//            		OrganizationCommunityRequest  organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(org.getId());
//            		if(null != organizationCommunityRequest){
//                		visibleRegionIds.add(organizationCommunityRequest.getCommunityId());
//            		}
//            	}else{
////                	ListCommunitiesByOrganizationIdCommand command = new ListCommunitiesByOrganizationIdCommand();
////                	command.setCommunityId(org.getId());
////                	List<CommunityDTO> communityDTOs = organizationService.listCommunityByOrganizationId(command).getCommunities();
////                	for (CommunityDTO communityDTO : communityDTOs) {
////                		visibleRegionIds.add(communityDTO.getId());
////    				}
//            		OrganizationCommunityRequest  organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(org.getId());
//            		if(null != organizationCommunityRequest){
//                		visibleRegionIds.add(organizationCommunityRequest.getCommunityId());
//            		}
//            	}
//            	
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Organization not found, sceneToken=" + sceneToken);
//                }
//            }
//            break;
//        default:
//            break;
//        }
        
        //查全部公告，对于小区，需要找到上级所有机构，对于管理公司，需要管理公司及其所在小区，对于普通公司，需要其管理公司及其所在小区
        List<Long> communityIds = new ArrayList<>();
        List<Long> organizationIds = new ArrayList<>();
        //检查游客是否能继续访问此场景 by sfyan 20161009
        userService.checkUserScene(sceneType);
        Long communityId = null;
        switch(sceneType) {
	    case DEFAULT:
	    case PARK_TOURIST:
	    	communityId = sceneToken.getEntityId();
	    	communityIds.add(communityId);
			organizationIds.addAll(organizationService.getOrganizationIdsTreeUpToRoot(communityId));
	        break;
	    case FAMILY:
	        FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
	        if(family != null) {
	            communityId = family.getCommunityId();
		    	communityIds.add(communityId);
				organizationIds.addAll(organizationService.getOrganizationIdsTreeUpToRoot(communityId));
	        } else {
	            if(LOGGER.isWarnEnabled()) {
	                LOGGER.warn("Family not found, sceneToken=" + sceneToken);
	            }
	        }
	        break;
        case ENTERPRISE: 
        case ENTERPRISE_NOAUTH: 
            // 对于普通公司，也需要取到其对应的管理公司，以便拿到管理公司所发的公告
            OrganizationDTO org = organizationService.getOrganizationById(sceneToken.getEntityId());
            if(org != null) {
                communityId = org.getCommunityId();
                if(communityId == null) {
                    LOGGER.error("No community found for organization, organizationId={}, cmd={}, sceneToken={}", 
                        sceneToken.getEntityId(), cmd, sceneToken);
                } else {
        	    	communityIds.add(communityId);
        			organizationIds.addAll(organizationService.getOrganizationIdsTreeUpToRoot(communityId));
                }
            } else {
                LOGGER.error("Organization not found, organizationId={}, cmd={}, sceneToken={}", sceneToken.getEntityId(), cmd, sceneToken);
            }
            break;
        case PM_ADMIN:
        	Long organizationId = sceneToken.getEntityId();
        	org = organizationService.getOrganizationById(organizationId);
            if(org != null) {
            	organizationIds.add(organizationId);
                communityId = org.getCommunityId();
                if(communityId == null) {
                    LOGGER.error("No community found for organization, organizationId={}, cmd={}, sceneToken={}", 
                        sceneToken.getEntityId(), cmd, sceneToken);
                }else {
                	communityIds.add(communityId);
        			organizationIds.addAll(organizationService.getOrganizationIdsTreeUpToRoot(communityId));
                }
            }
            break;
	    default:
	        LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
	        break;
	    }
        
        
        return this.listNoticeTopic(organizationIds, communityIds, cmd.getPublishStatus(), cmd.getPageSize(), cmd.getPageAnchor());
    }

    @Override
    public ListPostCommandResponse listNoticeTopic(List<Long> organizationIds, List<Long> communityIds, String publishStatus, Integer pageSize, Long pageAnchor){
    	pageSize = PaginationConfigHelper.getPageSize(configProvider, pageSize);
    	CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(pageAnchor);
        
    	Category contentCatogry = this.categoryProvider.findCategoryById(CategoryConstants.CATEGORY_ID_NOTICE);
    	Condition condition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
    	
    	//拼接visible条件
    	Condition visibleCondition = null;
    	if (organizationIds != null && !organizationIds.isEmpty()) {
			Condition organizationCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode())
												.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(organizationIds));
			visibleCondition = organizationCondition;
		}
    	if (communityIds != null && !communityIds.isEmpty()) {
    		Condition communityCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode())
					.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(communityIds));
    		if (visibleCondition == null) {
				visibleCondition = communityCondition;
			}else{
				visibleCondition = visibleCondition.or(communityCondition);
			}
		}
    	
    	condition = condition.and(visibleCondition);
        List<PostDTO> dtos = this.getOrgTopics(locator, pageSize, condition, publishStatus, null);
        
        ListPostCommandResponse response = new ListPostCommandResponse();
        response.setPosts(dtos);
        response.setNextPageAnchor(locator.getAnchor());
        
        return response;
    }
    
    //comment by tt, 20160810, 修改为支持查全部
    @Override
    public ListPostCommandResponse listNoticeTopic(VisibleRegionType visibleRegionType, List<Long> visibleRegionIds, String publishStatus, Integer pageSize, Long pageAnchor){
    	return null;
//    	if(visibleRegionIds.size() == 0){
//    		LOGGER.error("visibleRegionIds is null, visibleRegionIds =" + visibleRegionIds);
//    		throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, ForumServiceErrorCode.ERROR_INVALID_PARAMETER, 
//   					"visibleRegionIds is null.");
//    	}
//    	pageSize = PaginationConfigHelper.getPageSize(configProvider, pageSize);
//    	CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
//        locator.setAnchor(pageAnchor);
//        
//    	Category contentCatogry = this.categoryProvider.findCategoryById(CategoryConstants.CATEGORY_ID_NOTICE);
//    	Condition condition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
//    	Condition communityCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(visibleRegionType.getCode());
//        communityCondition = communityCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(visibleRegionIds));
//        condition = condition.and(communityCondition);
//        
//        List<PostDTO> dtos = this.getOrgTopics(locator, pageSize, condition, publishStatus);
//        
//        ListPostCommandResponse response = new ListPostCommandResponse();
//        response.setPosts(dtos);
//        response.setNextPageAnchor(locator.getAnchor());
//        
//        return response;
    }
    
    /**
     * 
     * 小区场景下客户端传入默认论坛id时，找出当前小区论坛id代替 by xiongying 20160510
     */
    private void setCurrentForumId(NewTopicCommand topicCmd, Long communityId) {
    	
    	Community community = communityProvider.findCommunityById(communityId);
    	
    	if(community != null) {
    		topicCmd.setForumId(community.getDefaultForumId());
    	}
    }

    /**
     *
     * 设置当前域空间默认forumId
     */
    private void setNamespaceDefaultForumId(NewTopicCommand topicCmd) {

        CrossShardListingLocator locator = new CrossShardListingLocator();

        if(UserContext.getCurrentNamespaceId() == null){
            return;
        }

        List<Community> communities = communityProvider.listCommunities(UserContext.getCurrentNamespaceId(), locator, 1, null);

        if(communities != null && communities.size() > 0){
            topicCmd.setForumId(communities.get(0).getDefaultForumId());
        }
    }
    
    @Override
    public List<TopicFilterDTO> getTopicQueryFilters(GetTopicQueryFilterCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        
      //检查游客是否能继续访问此场景 by xiongying 20161009
        userService.checkUserScene(sceneType);

        
        // 增加园区场景，由于很多代码是重复复制的，故把它们转移到Handler里进行构造，方便以后增加新场景只需要增加相应的handler即可 by lqs 20160510
//        List<TopicFilterDTO> filterList = null;
//        PostFilterType filterType = PostFilterType.fromCode(cmd.getFilterType());
//        if(filterType != null) {
//            switch(filterType) {
//            case DISCOVERY:
//                filterList = getDiscoveryTopicQueryFilters(user, sceneToken);
//                break;
//            case GA_NOTICE:
//                break;
//            default:
//                LOGGER.error("Unsupported post filter type, cmd=" + cmd + ", sceneToken=" + sceneToken);
//                break;
//            }
//        } else {
//            LOGGER.error("Post filter type is null, cmd=" + cmd + ", sceneToken=" + sceneToken);
//        }
        List<TopicFilterDTO> filterList = null;
        PostFilterType filterType = PostFilterType.fromCode(cmd.getFilterType());
        if(filterType == null) {
            LOGGER.error("Unsupported post filter type, cmd={}, sceneToken={}", cmd, sceneToken);
            return filterList;
        }
        
        String handlerName = PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + filterType.getCode() + "_" + sceneToken.getScene();
        PostSceneHandler handler = PlatformContext.getComponent(handlerName);
        if(handler != null) {
            filterList = handler.getTopicQueryFilters(user, sceneToken); 
        } else {
            LOGGER.error("No handler found for post quering filter, cmd={}, sceneToken={}, handlerName={}", cmd, sceneToken, handlerName);
        }
        
        return filterList;
    }
    
    /**
     * 对于发现界面的帖子组件（不管是否是普通人还是物业管理员），通过场景信息构建查帖过滤器
     * @param sceneToken
     * @return
     */
    private List<TopicFilterDTO> getDiscoveryTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        List<TopicFilterDTO> filterList = null;
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        switch(sceneType) {
        case DEFAULT: // 普通用户
            filterList = getDiscoveryTopicQueryFiltersForUser(user, sceneToken);
            break;
        case PM_ADMIN: // 物业公司管理员
            filterList = getDiscoveryTopicQueryFiltersForPmAdmin(user, sceneToken);
            break;
        default:
            LOGGER.error("Unsupported scene type, sceneType=" + sceneType);
            break;
        }
        
        return filterList;
    }
    
    /**
     * 组装界面菜单（帖子列表过滤器）：对于发现界面的帖子组件和普通人场景，通过场景信息构建查帖过滤器
     * @param user 用户信息
     * @param sceneToken 场景信息
     * @return 过滤器菜单列表
     */
    private List<TopicFilterDTO> getDiscoveryTopicQueryFiltersForUser(User user, SceneTokenDTO sceneToken) {
        Community community = null;
        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
        switch(entityType) {
        case COMMUNITY_RESIDENTIAL:
        case COMMUNITY_COMMERCIAL:
        case COMMUNITY:
            community = communityProvider.findCommunityById(sceneToken.getEntityId());
            break;
        case FAMILY:
            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
            if(family != null) {
                community = communityProvider.findCommunityById(family.getCommunityId());
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
                }
            }
            break;
        case ORGANIZATION:
            LOGGER.error("Unsupported scene of organization for simple user, sceneToken=" + sceneToken);
            break;
        default:
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
            break;
        }
        
        String menuName = null;
        String scope = ForumLocalStringCode.SCOPE;
        String code = "";
        String actionUrl = null;
        String avatarUri = null;
        Integer namespaceId = sceneToken.getNamespaceId();
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        if(community != null) {
            long menuId = 1;
            
            // 菜单：小区圈
            long group1Id = menuId++;
            TopicFilterDTO filterDto = new TopicFilterDTO();
            filterDto.setId(group1Id);
            filterDto.setParentId(0L);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            filterDto.setName(menuName);
            filterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());;
            filterList.add(filterDto);

            // 菜单：周边小区
            filterDto = new TopicFilterDTO();
            filterDto.setId(menuId++);
            filterDto.setParentId(group1Id);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_NEARBY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            filterDto.setName(menuName);
            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
            filterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());; // 整组菜单只有一个是默认的
            actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s", serverContectPath, 
                "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.NEARBY_COMMUNITIES.getCode(), community.getId());
            filterDto.setActionUrl(actionUrl);
            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_nearby", "");
            filterDto.setAvatar(avatarUri);
            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            filterList.add(filterDto);

            // 菜单：本小区
            filterDto = new TopicFilterDTO();
            filterDto.setId(menuId++);
            filterDto.setParentId(group1Id);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_ONLY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            filterDto.setName(menuName);
            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
            actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s", serverContectPath, 
                "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.COMMUNITY.getCode(), community.getId());
            filterDto.setActionUrl(actionUrl);
            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_only", "");
            filterDto.setAvatar(avatarUri);
            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            filterList.add(filterDto);


            long group2Id = menuId++;
            // 各兴趣圈
            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
            ListPublicGroupCommand groupCmd = new ListPublicGroupCommand();
            groupCmd.setUserId(user.getId());
            List<GroupDTO> groupList = groupService.listPublicGroups(groupCmd);
            if(groupList != null && groupList.size() > 0) {
                for(GroupDTO groupDto : groupList) {
                    filterDto = new TopicFilterDTO();
                    filterDto.setId(menuId++);
                    filterDto.setParentId(group2Id);
                    filterDto.setName(groupDto.getName());
                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());;
                    actionUrl = String.format("%s%s?forumId=%s&communityId=%s", serverContectPath, 
                        "/forum/listTopics", groupDto.getOwningForumId(), community.getId());
                    filterDto.setActionUrl(actionUrl);
                    avatarUri = groupDto.getAvatar();
                    if(avatarUri == null || avatarUri.trim().length() == 0) {
                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.all", "");
                    }
                    filterDto.setAvatar(avatarUri);
                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    tmpFilterList.add(filterDto);
                }
            }
            // 当没有孩子节点时，组节点也不要显示 by lqs 20160426
            if(tmpFilterList.size() > 0) {
                // 菜单：兴趣圈组
                TopicFilterDTO group2FilterDto = new TopicFilterDTO();
                group2FilterDto.setId(group2Id);
                group2FilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_INTEREST_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                group2FilterDto.setName(menuName);
                group2FilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                group2FilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(group2FilterDto);
                
                // 各兴趣圈
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No interest group filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        }
        
        return filterList;
    }
    
    /**
     * 组装界面菜单（帖子列表过滤器）：对于发现界面的帖子组件和物业管理员场景，通过场景信息构建查帖过滤器
     * @param user 用户信息
     * @param sceneToken 场景信息
     * @return 过滤器菜单列表
     */
    private List<TopicFilterDTO> getDiscoveryTopicQueryFiltersForPmAdmin(User user, SceneTokenDTO sceneToken) {
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
        if(entityType != UserCurrentEntityType.ORGANIZATION) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneToken);
            return filterList;
        }

        Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());
        if(organization == null) {
            LOGGER.error("Organization not found, sceneToken={}", sceneToken);
            return filterList;
        }
        
        // 由于公司删除后只是置状态，故需要判断状态是否正常 by lqs 20160430
        OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
        if(orgStatus == OrganizationStatus.ACTIVE) {
            String menuName = null;
            String scope = ForumLocalStringCode.SCOPE;
            String code = "";
            String actionUrl = null;            
            long menuId = 1;
            String avatarUri = null;
            Integer namespaceId = sceneToken.getNamespaceId();
            // 由于有些分组没有，故默认的菜单也需要进行动态设置 by lqs 20160427
            boolean hasDefault = false; 

            long group1Id = menuId++;
            // 本公司
            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
            TopicFilterDTO filterDto = null;
            Group group = null;
            if(organization.getGroupId() != null) {
                if(organization.getGroupId() != null) {
                    group = groupProvider.findGroupById(organization.getGroupId());
                }
                if(group != null) {
                    filterDto = new TopicFilterDTO();
                    filterDto.setId(menuId++);
                    filterDto.setParentId(group1Id);
                    filterDto.setName(organization.getName());
                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                    actionUrl = String.format("%s%s?forumId=%s", serverContectPath, 
                        "/forum/listTopics", group.getOwningForumId());
                    filterDto.setActionUrl(actionUrl);
                    avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                    filterDto.setAvatar(avatarUri);
                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    tmpFilterList.add(filterDto);
                }
            } else {
                LOGGER.warn("The group id of organization is null, sceneToken=" + sceneToken);
            }

            // 子公司
            List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
            if(subOrgList != null && subOrgList.size() > 0) {
                for(Organization subOrg : subOrgList) {
                    if(subOrg.getGroupId() != null) {
                        if(subOrg.getGroupId() != null) {
                            group = groupProvider.findGroupById(subOrg.getGroupId());
                        }
                        if(group != null) {
                            filterDto = new TopicFilterDTO();
                            filterDto.setId(menuId++);
                            filterDto.setParentId(group1Id);
                            filterDto.setName(subOrg.getName());
                            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                            actionUrl = String.format("%s%s?forumId=%s", serverContectPath, 
                                "/forum/listTopics", group.getOwningForumId());
                            filterDto.setActionUrl(actionUrl);
                            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                            filterDto.setAvatar(avatarUri);
                            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                            tmpFilterList.add(filterDto);
                        }
                    } else {
                        LOGGER.warn("The group id of suborganization is null, subOrgId=" + subOrg.getId() + ", sceneToken=" + sceneToken);
                    }
                }
            }
            
            // 当公司和子公司都没有论坛的时候，此时不显示公司圈组 by lqs 20160426
            if(tmpFilterList.size() > 0) {
                // 菜单：公司圈组
                TopicFilterDTO orgGroupFilterDto = new TopicFilterDTO();
                orgGroupFilterDto.setId(group1Id);
                orgGroupFilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ORGANIZATION_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                orgGroupFilterDto.setName(menuName);
                orgGroupFilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                orgGroupFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(orgGroupFilterDto);
                
                // 公司全部
                TopicFilterDTO orgAllFilterDto = new TopicFilterDTO();
                orgAllFilterDto.setId(menuId++);
                orgAllFilterDto.setParentId(group1Id);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                orgAllFilterDto.setName(menuName);
                orgAllFilterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                orgAllFilterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode()); // 整组菜单只有一个是默认的
                hasDefault = true;
                actionUrl = String.format("%s%s?organizationId=%s&mixType=%s", serverContectPath, 
                    "/org/listOrgMixTopics", organization.getId(), OrganizationTopicMixType.CHILDREN_ALL.getCode());
                orgAllFilterDto.setActionUrl(actionUrl);
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.all", "");
                orgAllFilterDto.setAvatar(avatarUri);
                orgAllFilterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                filterList.add(orgAllFilterDto);
                
                // 公司圈及子公司圈
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization group filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }

            long group2Id = menuId++;
            tmpFilterList.clear();
            // 公司管理的单个小区
            List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organization.getId());
            for(CommunityDTO community : communities) {
                filterDto = new TopicFilterDTO();
                filterDto.setId(menuId++);
                filterDto.setParentId(group2Id);
                filterDto.setName(community.getName());
                filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s", serverContectPath, 
                    "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.COMMUNITY.getCode(), community.getId());
                filterDto.setActionUrl(actionUrl);
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                filterDto.setAvatar(avatarUri);
                filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                tmpFilterList.add(filterDto);
            }
            
            if(tmpFilterList.size() > 0) {
                // 所管理的小区
                TopicFilterDTO cmntyGroupFilterDto = new TopicFilterDTO();
                cmntyGroupFilterDto.setId(group2Id);
                cmntyGroupFilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                cmntyGroupFilterDto.setName(menuName);
                cmntyGroupFilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                cmntyGroupFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(cmntyGroupFilterDto);
                
                // 公司管理的全部小区
                TopicFilterDTO cmntyAllFilterDto = new TopicFilterDTO();
                cmntyAllFilterDto.setId(menuId++);
                cmntyAllFilterDto.setParentId(group2Id);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                cmntyAllFilterDto.setName(menuName);
                cmntyAllFilterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                if(hasDefault) {
                    cmntyAllFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                } else {
                    cmntyAllFilterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());
                    hasDefault = true;
                }
                actionUrl = String.format("%s%s?organizationId=%s&mixType=%s", serverContectPath, 
                    "/org/listOrgMixTopics", organization.getId(), OrganizationTopicMixType.COMMUNITY_ALL.getCode());
                cmntyAllFilterDto.setActionUrl(actionUrl);
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                cmntyAllFilterDto.setAvatar(avatarUri);
                cmntyAllFilterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                filterList.add(cmntyAllFilterDto);
                
                // 公司管理的各个小区
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization community filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        } else {
            LOGGER.error("Organization not in active state, sceneToken={}, orgStatus={}", sceneToken, orgStatus);
        }
        
        return filterList;
    }
    
    @Override
    public List<TopicScopeDTO> getTopicSentScopes(GetTopicSentScopeCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        
        // 增加园区场景，由于很多代码是重复复制的，故把它们转移到Handler里进行构造，方便以后增加新场景只需要增加相应的handler即可 by lqs 20160510
//        List<TopicScopeDTO> sentScopeList = null;
//        PostSentScopeType sentScopeType = PostSentScopeType.fromCode(cmd.getScopeType());
//        if(sentScopeType != null) {
//            switch(sentScopeType) {
//            case DISCOVERY:
//                sentScopeList = getDiscoveryTopicSentScopes(user, sceneToken);
//                break;
//            case GA:
//                break;
//            default:
//                LOGGER.error("Unsupported post filter type, cmd=" + cmd + ", sceneToken=" + sceneToken);
//                break;
//            }
//        } else {
//            LOGGER.error("Post filter type is null, cmd=" + cmd + ", sceneToken=" + sceneToken);
//        }
        
        List<TopicScopeDTO> sentScopeList = null;
        PostFilterType filterType = PostFilterType.fromCode(cmd.getScopeType());
        if(filterType == null) {
            LOGGER.error("Unsupported post sent scope type, cmd={}, sceneToken={}", cmd, sceneToken);
            return sentScopeList;
        }
        
        String handlerName = PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + filterType.getCode() + "_" + sceneToken.getScene();
        PostSceneHandler handler = PlatformContext.getComponent(handlerName);
        if(handler != null) {
            sentScopeList = handler.getTopicSentScopes(user, sceneToken);
        } else {
            LOGGER.error("No handler found for post sent scope, cmd={}, sceneToken={}, handlerName={}", cmd, sceneToken, handlerName);
        }
        
        
        return sentScopeList;
    }
    
    /**
     * 对于发现界面的帖子组件（不管是否是普通人还是物业管理员），通过场景信息构建发帖范围
     * @param sceneToken
     * @return
     */
    private List<TopicScopeDTO> getDiscoveryTopicSentScopes(User user, SceneTokenDTO sceneToken) {
        List<TopicScopeDTO> sentScopeList = null;
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        switch(sceneType) {
        case DEFAULT: // 普通用户
            sentScopeList = getDiscoveryTopicSentScopeForUser(user, sceneToken);
            break;
        case PM_ADMIN: // 物业公司管理员
            sentScopeList = getDiscoveryTopicSentScopesForPmAdmin(user, sceneToken);
            break;
        default:
            LOGGER.error("Unsupported scene type, sceneType=" + sceneType);
            break;
        }
        
        return sentScopeList;
    }

    
    /**
     * 组装界面菜单（帖子发送范围）：对于发现界面的帖子组件和普通人场景，通过场景信息构建发送范围
     * @param user 用户信息
     * @param sceneTokenDto 场景信息
     * @return 发送范围菜单列表
     */
    private List<TopicScopeDTO> getDiscoveryTopicSentScopeForUser(User user, SceneTokenDTO sceneTokenDto) {
        Community community = null;
        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
        switch(entityType) {
        case COMMUNITY_RESIDENTIAL:
        case COMMUNITY_COMMERCIAL:
        case COMMUNITY:
            community = communityProvider.findCommunityById(sceneTokenDto.getEntityId());
            break;
        case FAMILY:
            FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
            if(family != null) {
                community = communityProvider.findCommunityById(family.getCommunityId());
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
                }
            }
            break;
        case ORGANIZATION:
            LOGGER.error("Unsupported scene of organization for simple user, sceneToken=" + sceneTokenDto);
            break;
        default:
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
            break;
        }
        
        String menuName = null;
        String scope = ForumLocalStringCode.SCOPE;
        String code = "";
        Integer namespaceId = sceneTokenDto.getNamespaceId();
        List<TopicScopeDTO> filterList = new ArrayList<TopicScopeDTO>();
        if(community != null) {
            String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
            long menuId = 1;
            
            // 菜单：小区圈
            long group1Id = menuId++;
            TopicScopeDTO sentScopeDto = new TopicScopeDTO();
            sentScopeDto.setId(group1Id);
            sentScopeDto.setParentId(0L);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            sentScopeDto.setName(menuName);
            sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());;
            filterList.add(sentScopeDto);

            // 菜单：周边小区
            sentScopeDto = new TopicScopeDTO();
            sentScopeDto.setId(menuId++);
            sentScopeDto.setParentId(group1Id);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_NEARBY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            sentScopeDto.setName(menuName);
            sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
            sentScopeDto.setForumId(community.getDefaultForumId());
            sentScopeDto.setSceneToken(sceneToken);
            sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
            String avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_nearby", "");
            sentScopeDto.setAvatar(avatarUri);
            sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            filterList.add(sentScopeDto);

            // 菜单：本小区
            sentScopeDto = new TopicScopeDTO();
            sentScopeDto.setId(menuId++);
            sentScopeDto.setParentId(group1Id);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_ONLY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            sentScopeDto.setName(menuName);
            sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
            sentScopeDto.setForumId(community.getDefaultForumId());
            sentScopeDto.setSceneToken(sceneToken);
            sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_only", "");
            sentScopeDto.setAvatar(avatarUri);
            sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            filterList.add(sentScopeDto);

            // 各兴趣圈
            long group2Id = menuId++;
            List<TopicScopeDTO> tmpFilterList = new ArrayList<TopicScopeDTO>();
            ListPublicGroupCommand groupCmd = new ListPublicGroupCommand();
            groupCmd.setUserId(user.getId());
            List<GroupDTO> groupList = groupService.listPublicGroups(groupCmd);
            if(groupList != null && groupList.size() > 0) {
                for(GroupDTO groupDto : groupList) {
                    sentScopeDto = new TopicScopeDTO();
                    sentScopeDto.setId(menuId++);
                    sentScopeDto.setParentId(group2Id);
                    sentScopeDto.setName(groupDto.getName());
                    sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
                    sentScopeDto.setForumId(groupDto.getOwningForumId());
                    sentScopeDto.setSceneToken(sceneToken);
                    sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                    if(groupDto.getAvatar() != null) {
                        sentScopeDto.setAvatar(groupDto.getAvatar());
                        sentScopeDto.setAvatarUrl(groupDto.getAvatarUrl());
                    } else {
                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.group", "");
                        sentScopeDto.setAvatar(avatarUri);
                        sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    }
                    tmpFilterList.add(sentScopeDto);
                }
            }
            
            if(tmpFilterList.size() > 0) {
                // 菜单：兴趣圈
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(group2Id);
                sentScopeDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_INTEREST_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                sentScopeDto.setName(menuName);
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());;
                filterList.add(sentScopeDto);
                
                // 各兴趣圈
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No interest group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        }
        
        return filterList;
    }
    
    /**
     * 组装界面菜单（帖子发送范围列表）：对于发现界面的帖子组件和物业管理员场景，通过场景信息构建发送范围
     * @param user 用户信息
     * @param sceneTokenDto 场景信息
     * @return 发送范围菜单列表
     */
    private List<TopicScopeDTO> getDiscoveryTopicSentScopesForPmAdmin(User user, SceneTokenDTO sceneTokenDto) {
        List<TopicScopeDTO> sentScopeList = new ArrayList<TopicScopeDTO>();
        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
        if(entityType != UserCurrentEntityType.ORGANIZATION) {
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
            return sentScopeList;
        }

        Organization organization = organizationProvider.findOrganizationById(sceneTokenDto.getEntityId());
        if(organization == null) {
            LOGGER.error("Organization not found, sceneToken={}", sceneTokenDto);
            return sentScopeList;
        }
        
        // 由于公司删除后只是置状态，故需要判断状态是否正常 by lqs 20160430
        OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
        if(orgStatus == OrganizationStatus.ACTIVE) {
            String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
            String menuName = null;
            String scope = ForumLocalStringCode.SCOPE;
            String code = "";      
            long menuId = 1;
            String avatarUri = null;
            Integer namespaceId = sceneTokenDto.getNamespaceId();

            long group1Id = menuId++;
            List<TopicScopeDTO> tmpSentScopeList = new ArrayList<TopicScopeDTO>();
            
            // 本公司
            TopicScopeDTO sentScopeDto = null;
            Group groupDto = null;
            if(organization.getGroupId() != null) {
                groupDto = groupProvider.findGroupById(organization.getGroupId());
            }
            if(groupDto != null) {
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(menuId++);
                sentScopeDto.setParentId(group1Id);
                sentScopeDto.setName(organization.getName());
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                sentScopeDto.setForumId(groupDto.getOwningForumId());
                sentScopeDto.setSceneToken(sceneToken);
                sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                sentScopeDto.setAvatar(avatarUri);
                sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                tmpSentScopeList.add(sentScopeDto);
            }

            // 子公司
            List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
            if(subOrgList != null && subOrgList.size() > 0) {
                for(Organization subOrg : subOrgList) {
                    groupDto = null;
                    if(subOrg.getGroupId() != null) {
                        groupDto = groupProvider.findGroupById(subOrg.getGroupId());
                    }
                    if(groupDto != null) {
                        sentScopeDto = new TopicScopeDTO();
                        sentScopeDto.setId(menuId++);
                        sentScopeDto.setParentId(group1Id);
                        sentScopeDto.setName(subOrg.getName());
                        sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                        sentScopeDto.setForumId(groupDto.getOwningForumId());
                        sentScopeDto.setSceneToken(sceneToken);
                        sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                        sentScopeDto.setAvatar(avatarUri);
                        sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                        tmpSentScopeList.add(sentScopeDto);
                    }
                }
            }
            
            if(tmpSentScopeList.size() > 0) {
                // 菜单：公司圈
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(group1Id);
                sentScopeDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ORGANIZATION_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                sentScopeDto.setName(menuName);
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                sentScopeList.add(sentScopeDto);
                
                // 本公司及各子公司
                sentScopeList.addAll(tmpSentScopeList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
            

            long group2Id = menuId++;
            // 公司管理的单个小区
            tmpSentScopeList.clear();
            List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organization.getId());
            for(CommunityDTO community : communities) {
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(menuId++);
                sentScopeDto.setParentId(group2Id);
                sentScopeDto.setName(community.getName());
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
                sentScopeDto.setForumId(community.getDefaultForumId());
                sentScopeDto.setSceneToken(sceneToken);
                sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                sentScopeDto.setAvatar(avatarUri);
                sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                tmpSentScopeList.add(sentScopeDto);
            }
            
            if(tmpSentScopeList.size() > 0) {
                // 所管理的小区
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(group2Id);
                sentScopeDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                sentScopeDto.setName(menuName);
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());;
                sentScopeList.add(sentScopeDto);
                
                // 公司管理的各个小区
                sentScopeList.addAll(tmpSentScopeList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No community group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        } else {
            LOGGER.error("Organization not in active state, sceneToken={}, orgStatue", sceneTokenDto, orgStatus);
        }
        
        return sentScopeList;
    }    
    
    @Override
    public ListPostCommandResponse listTopicsByForums(ListTopicByForumCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        
        // 由外边传入的论坛ID可能重复，需要进行过虑，否则帖子会有重复 by lqs 20160429
        List<Long> forumIdList = removeDuplicatedForumIds(cmd.getForumIdList());
        
        if(forumIdList != null && forumIdList.size() > 0) {
        	
        	Condition cond = this.notEqPostCategoryCondition(cmd.getExcludeCategories(), null);
        	
            CrossShardListingLocator[] locators = new CrossShardListingLocator[forumIdList.size()];
            for(int i = 0; i < forumIdList.size(); i++) {
                Long forumId = forumIdList.get(i);
                locators[i] = new CrossShardListingLocator(forumId);
                locators[i].setAnchor(cmd.getPageAnchor());
            }
            
            int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
            List<Post> posts = forumProvider.queryPosts(locators, 10000, pageSize, (loc, query) -> {
                query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
                    Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
                query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
                query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
                if(null != cond){
                	query.addConditions(cond);
                }

                //支持按话题、活动、投票来查询数据   add by yanjun 20170612
                if(cmd.getCategoryId() != null){
                    //1和1001都是话题，老客户端传的1web传的1001，新客户端会改成1001，很久很久以后可以刷一遍数据改掉这里的代码  add by yanjun 20171221
                    if(CategoryConstants.CATEGORY_ID_TOPIC == cmd.getCategoryId().longValue() || CategoryConstants.CATEGORY_ID_TOPIC_COMMON == cmd.getCategoryId().longValue()){
                        query.addConditions(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC)
                                .or(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC_COMMON)));
                    }else {
                        query.addConditions(Tables.EH_FORUM_POSTS.CATEGORY_ID.eq(cmd.getCategoryId()));
                    }
                }

                //支持标签搜索  add by yanjun 20170712
                if(!StringUtils.isEmpty(cmd.getTag())){
                    query.addConditions(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
                }

                //论坛多入口，老客户端没有这个参数，默认入口为0  add by yanjun 20171025
                if(StringUtils.isEmpty(cmd.getForumEntryId())){
                    query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(0L));
                }else {
                    query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(cmd.getForumEntryId()));
                }

                //全部 -- 查询各个目标的（正常），或者发送到“全部”的（clone、正常）   add by yanjun 20170807
                Condition cloneCondition = Tables.EH_FORUM_POSTS.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode())
                        .or(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode()));

                query.addConditions(cloneCondition);

                
                return query;
            }, new PostCreateTimeDescComparator());


            // 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170808
            populateRealPost(posts);


            Long nextPageAnchor = null;
            // 在queryPosts已经进行附件填充，故可去掉 by lqs 20160429
            // this.forumProvider.populatePostAttachments(posts);
            
            if(posts.size() > pageSize) {
                posts.remove(posts.size() - 1);
                nextPageAnchor = cmd.getPageAnchor() == null ? 2 : cmd.getPageAnchor() + 1;
            }
            
            populatePosts(userId, posts, communityId, false);
            
            List<PostDTO> postDtoList = posts.stream().map((r) -> {
              return ConvertHelper.convert(r, PostDTO.class);  
            }).collect(Collectors.toList());
            
            return new ListPostCommandResponse(nextPageAnchor, postDtoList);
        }
        
        return null;
    }

	@Override
	public List<PostDTO> getTopicById(List<Long> topicIds, List<Long> communityIds, boolean isDetail) {
		
		List<PostDTO> postDtoList = new ArrayList<PostDTO>();
		for(Long communityId : communityIds) {
            try {
            	List<PostDTO> postDtos = getTopicById(topicIds, communityId, isDetail, false);
                postDtoList.addAll(postDtos);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
		return postDtoList;
	}
	
	private String getPostFilterDefaultAvatar(Integer namespaceId, Long userId, String avatarUri) {
	    if(avatarUri != null) {
	        try {
	            return contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), userId);
	        } catch (Exception e) {
	            LOGGER.error("Failed to parse post filter default avatar uri, namespaceId={}, userId={}, avatarUri={}", 
	                namespaceId, userId, avatarUri, e);
	        }
	    }
	    
	    return null;
	}
    
    private List<Long> removeDuplicatedForumIds(List<Long> forumIdList) {
        List<Long> dupForumIdList = new ArrayList<Long>();
        List<Long> uniqueForumIdList = new ArrayList<Long>();
        if(forumIdList != null && forumIdList.size() > 0) {
            for(Long id : forumIdList) {
                if(!uniqueForumIdList.contains(id)) {
                    uniqueForumIdList.add(id);
                } else {
                    dupForumIdList.add(id);
                }
            }
        }
        if(dupForumIdList.size() > 0 && LOGGER.isDebugEnabled()) {
            LOGGER.debug("Remove the duplicated forum ids, duplicatedForumIds={}, forumIdList={}", dupForumIdList, forumIdList);
        }
        
        return uniqueForumIdList;
    }
    
    /**
     * 把创客空间的帖子独立出来查询，免得全部耦合到listTopics()方法里。
     * 为所有园区的创客空间帖子建一个固定论坛（类似社区论坛）
     * @param forum
     * @param cmd
     * @return
     */
    public ListPostCommandResponse listMakerzoneForumTopics(Forum forum, ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        
        Community community = checkCommunityParameter(userId, communityId, "listMakerzoneForumTopics");

        // 根据查帖指定的可见性创建查询条件
        Condition cmntyCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        cmntyCondition = cmntyCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(community.getId()));
        
        final Condition condition = cmntyCondition;
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
                        
            if(condition != null){
                query.addConditions(condition);
            }

            //论坛多入口，老客户端没有这个参数，默认入口为0  add by yanjun 20171025
            if(StringUtils.isEmpty(cmd.getForumEntryId())){
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(0L));
            }else {
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(cmd.getForumEntryId()));
            }

            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = posts.get(posts.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
        }
        
        populatePosts(userId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isDebugEnabled()) {
            int size = (postDtoList == null) ? 0 : postDtoList.size();
            LOGGER.debug("Query maker zone topics, userId={}, size={}, cmd={}", userId, size, cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }    
    
    /**
     * 判断是否是创客空间论坛
     * @param namespaceId 域空间ID
     * @param forumId 论坛ID
     * @param communityId 小区ID
     * @return 如果是创客空间论坛则返回true，否则返回false
     */
    public boolean isMakerzoneForum(Integer namespaceId, Long forumId, Long communityId) {
        if(namespaceId == null || forumId == null) {
            return false;
        }
        
        long makerzoneForumId = this.configProvider.getLongValue(namespaceId, "makerzone.forum_id", 0L);
        if(makerzoneForumId == 0) {
            return false;
        }
        
        return forumId.equals(makerzoneForumId);
    }

	@Override
	public SearchContentsBySceneReponse searchContents(
			SearchContentsBySceneCommand cmd, SearchContentType contentType) {

		SearchTopicBySceneCommand command = new SearchTopicBySceneCommand();
		command.setPageAnchor(cmd.getPageAnchor());
		command.setPageSize(cmd.getPageSize());
		command.setQueryString(cmd.getKeyword());
		command.setSceneToken(cmd.getSceneToken());
		command.setSearchContentType(contentType.getCode());
		//是否全局搜索未设定
		SearchResponse rsp = postSearcher.searchByScene(command);
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
		SearchContentsBySceneReponse resp = analyzeSearchResponse(rsp, pageSize, anchor, contentType.getCode());
		return resp;
	}
	
	private String highlightText(Text[] texts) {
		StringBuilder sb = new StringBuilder();
		for(Text text: texts) {
			if(sb.length() != 0) {
				sb.append("..." + text);
			} else {
				sb.append(text);
			}
		}
		
		String str = sb.toString();
		
		return str;
	}
	
	private SearchContentsBySceneReponse analyzeSearchResponse(SearchResponse rsp, int pageSize, Long anchor, String searchContentType) {
    	SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();
    	
    	List<ContentBriefDTO> dtos  = new ArrayList<ContentBriefDTO>();

        SearchTypes searchType = userService.getSearchTypes(UserContext.getCurrentNamespaceId(), searchContentType);

        //找不到直接返回，没有searchType客户端会报错的。 add by yanjun 20170816
        if(searchType == null){
            response.setDtos(dtos);
            return response;
        }

    	SearchHit[] docs = rsp.getHits().getHits();
    	
        for (SearchHit sd : docs) {
        	ContentBriefDTO dto = new ContentBriefDTO();
        	dto.setId(Long.parseLong(sd.getId()));
        	
        	if(searchType != null) {
        		dto.setSearchTypeId(searchType.getId());
    			dto.setSearchTypeName(searchType.getName());
    			dto.setContentType(searchType.getContentType());
        	}
        	
        	Map<String, Object> source = sd.getSource();
        	Map<String, HighlightField> highlight = sd.getHighlightFields();
        	
        	dto.setForumId(Long.parseLong(source.get("forumId").toString()));
        	
        	if(StringUtils.isEmpty(String.valueOf(highlight.get("subject"))) || "null".equals(String.valueOf(highlight.get("subject")))){
        		dto.setSubject(String.valueOf(source.get("subject")));
			} else {
				String subject = highlightText(highlight.get("subject").getFragments());
				dto.setSubject(subject);
				
			}
        	
        	if(StringUtils.isEmpty(String.valueOf(highlight.get("content"))) || "null".equals(String.valueOf(highlight.get("content")))){
        		dto.setContent(String.valueOf(source.get("content")));
			} else {
				String content = highlightText(highlight.get("content").getFragments());
				dto.setContent(content);
			}

			//查询真身帖
            Post post = forumProvider.findPostById(dto.getId());
            if(post.getRealPostId() != null){
                dto.setId(post.getRealPostId());
            }

            PostDTO postDto =  getTopicById(dto.getId(), null, true, true);
        	if(postDto != null && postDto.getAttachments() != null && postDto.getAttachments().size() > 0) {
        		postDto.getAttachments();
        		postDto.getAttachments().get(0);
        		String postUrl = postDto.getAttachments().get(0).getContentUrl();
        		dto.setPostUrl(postUrl);
        	}
        	
        	ForumFootnoteHandler handler = getForumFootnoteHandler(searchContentType);
            if(handler != null) {
                handler.renderContentFootnote(dto, postDto);
            }
        	
        	dtos.add(dto);
        }
        
    	if(dtos.size() > pageSize) {
    		response.setNextPageAnchor(anchor+1);
    		dtos.remove(dtos.size() - 1);
    	}
    	
    	response.setDtos(dtos);

    	return response;
    }
    
    private ForumFootnoteHandler getForumFootnoteHandler(String searchContentType) {
    	ForumFootnoteHandler handler = null;
        
        if(!StringUtils.isEmpty(searchContentType)) {
            String handlerPrefix = ForumFootnoteHandler.FORUM_FOOTNOTE_RESOLVER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + searchContentType);
        }
        
        return handler;
    }

	@Override
	public ListPostCommandResponse listOfficialActivityByNamespace(ListOfficialActivityByNamespaceCommand cmd) {
		long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Integer namespaceId = cmd.getNamespaceId();
        List<Long> forumIds = new ArrayList<Long>();
        List<Long> communityIdList = new ArrayList<Long>();
        // 获取所管理的所有小区对应的社区论坛
        if(namespaceId != null) {
            List<NamespaceResource> namespaceResources = namespacesProvider.listNamespaceResources(namespaceId, NamespaceResourceType.COMMUNITY.getCode());
            List<Community> communities = communityProvider.findCommunitiesByIds(namespaceResources.stream().map(n->n.getResourceId()).collect(Collectors.toList()));            
            if(communities != null){
                for (Community community : communities) {
                    communityIdList.add(community.getId());
                    forumIds.add(community.getDefaultForumId());
                }
            }
        }
        
        // 当论坛list为空时，JOOQ的IN语句会变成1=0，导致条件永远不成立，也就查不到东西
        if(forumIds.size() == 0) {
            LOGGER.error("Forum not found for offical activities, cmd={}", cmd);
            return null;
        }
        
        Condition forumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIds);
        
        Condition condition = forumCondition;
        condition = condition.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(AppConstants.APPID_ACTIVITY));
        condition = condition.and(Tables.EH_FORUM_POSTS.OFFICIAL_FLAG.eq(OfficialFlag.YES.getCode()));
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        // TODO: Locator里设置系统论坛ID存在着分区的风险，因为上面的条件是多个论坛，需要后面理顺  by lqs 20160730
        CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(cmd.getPageAnchor());
        
        List<PostDTO> dtos = this.getOrgTopics(locator, pageSize, condition, null, null);
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query offical activity topics, userId=" + operatorId + ", size=" + dtos.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }   
        return new ListPostCommandResponse(locator.getAnchor(), dtos); 
	}

	@Override
	public GetActivityDetailByIdResponse getActivityDetailById(GetActivityDetailByIdCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id cannot be empty");
		}
		
		Post post = forumProvider.findPostById(cmd.getId());
		if (post != null) {
			String content = null;
			String contentType = null;
			Activity activity = activityProivider.findActivityById(post.getEmbeddedId());
			if (activity != null && activity.getDescription() != null) {
				content = activity.getDescription();
				contentType = activity.getContentType();
			}else {
				content = post.getContent();
				contentType = post.getContentType();
			}
			forumProvider.populatePostAttachments(post);
			populatePostAttachements(UserContext.current().getUser().getId(), post, post.getAttachments());
			
			return new GetActivityDetailByIdResponse(contentType, content, post.getAttachments().stream().map(a->ConvertHelper.convert(a, AttachmentDTO.class)).collect(Collectors.toList()));
		}
		
		return null;
	}

	//查询用户关注的俱乐部的帖子
	@Override
	public ListUserGroupPostResponse listUserGroupPost(VisibilityScope scope, Long communityId, List<Long> forumIdList, Long userId, Long pageAnchor, Integer pageSize) {
        
		final int thisPageSize = PaginationConfigHelper.getPageSize(configProvider, pageSize);
        
        Condition condition = this.notEqPostCategoryCondition(null, null);
        
        
        
        //由于论坛有可能是分库分表的，所以这里只能一个论坛一个论坛来查询，分页查询的时候需要每个论坛都查前20条，然后合并到一起再取前20条，最后再返回
        //如果是第2页，那就要每个论坛取前40条，合并到一起再取第21到第40条，依次类推
        //如果按照锚点来查的话，按照给定的锚点每个论坛取前20条，再合并到一起取前20条
        List<Post> totalPostList = new ArrayList<>();
        forumIdList.forEach(forumId->{
        	CrossShardListingLocator locator = new CrossShardListingLocator();
            locator.setAnchor(pageAnchor);
        	locator.setEntityId(forumId);
            List<Post> posts = this.forumProvider.queryPosts(locator, thisPageSize + 1, (loc, query) -> {
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forumId));
                query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
                query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
                if(null != condition){
                	query.addConditions(condition);
                }
                return query;
            });
            if (posts != null && posts.size() > 0) {
                totalPostList.addAll(posts);
			}
        });
        
        //此处按id排序而不是创建时间，因为有可能创建时间是一样的，那样在分界点的数据就会有问题
        
        //上面的注释已经过期，此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
        totalPostList.sort((post1, post2)->post1.getCreateTime().getTime() > post2.getCreateTime().getTime()?-1:1);
        
        //取前21条，多一条是为了后面判断是否有下一页
        List<Post> resultPostList = totalPostList.subList(0, totalPostList.size()>thisPageSize+1?thisPageSize+1:totalPostList.size());
        
        this.forumProvider.populatePostAttachments(resultPostList);
        
        Long nextPageAnchor = null;
        if(resultPostList.size() > thisPageSize) {
        	resultPostList.remove(resultPostList.size() - 1);
        	 //此处使用创建时间排序 ， 因为查询接口queryPosts使用了创建时间排序 add by yanjun 20170522
            //nextPageAnchor = resultPostList.get(resultPostList.size() - 1).getCreateTime().getTime();

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            nextPageAnchor = pageAnchor == null ? 2 : pageAnchor + 1;
        }
        
        populatePosts(userId, resultPostList, communityId, false);
        
        List<PostDTO> postDtoList = resultPostList.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        return new ListUserGroupPostResponse(nextPageAnchor, postDtoList);
	}

	@Override
	public void publisTopic(PublishTopicCommand cmd) {
		Activity activity = activityProvider.findSnapshotByPostId(cmd.getTopicId());
		if(activity == null){
			 LOGGER.error("invalid post id=", cmd.getTopicId());
	            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id=" + cmd.getTopicId());
		}

		Post post = forumProvider.findPostById(activity.getPostId());
		if(post == null){
			LOGGER.error("Forum post not found, topicId=" + activity.getPostId());
			throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
	        		ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "post not found"); 
		}
		//对设置了最低人数限制的帖子设置定时任务 add by yanlong.liang 20180713
        if (post.getEmbeddedAppId() != null && AppConstants.APPID_ACTIVITY == post.getEmbeddedAppId()
                && post.getMinQuantity() != null && post.getMinQuantity() >0) {
		    this.activitySignupTimeoutService.pushTimeout(post);
        }
        this.dbProvider.execute((status) -> {

		    //把真身帖子和克隆帖一块发布   edit by yanjun 20170830
            List<Post> list = new ArrayList<>();
		    if(post.getCloneFlag() != null && post.getCloneFlag().byteValue() == PostCloneFlag.REAL.getCode()){
                list= forumProvider.listPostsByRealPostId(post.getId());
            }
            list.add(post);

		    list.forEach(r ->{
		        //更新帖子
                r.setStatus(PostStatus.ACTIVE.getCode());
                //编辑后发布需要重新设置创建时间.
                Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
                r.setCreateTime(ts);
                ActivityPostCommand tempCmd = (ActivityPostCommand) StringHelper.fromJsonString(r.getEmbeddedJson(),
                        ActivityPostCommand.class);
                tempCmd.setStatus(PostStatus.ACTIVE.getCode());
                r.setEmbeddedJson(StringHelper.toJsonString(tempCmd));
                forumProvider.updatePost(r);

                //更新活动
                Activity r_activity = activityProvider.findSnapshotByPostId(r.getId());
                r_activity.setStatus(PostStatus.ACTIVE.getCode());
                r_activity.setCreateTime(ts);
                activityProvider.updateActivity(r_activity);

                //暂存的帖子不添加到搜索引擎，到发布的时候添加到搜索引擎，不计算积分    add by yanjun 20170609
                try {
                    postSearcher.feedDoc(r);

                    AddUserPointCommand pointCmd = new AddUserPointCommand(r.getCreatorUid(), PointType.CREATE_TOPIC.name(),
                            userPointService.getItemPoint(PointType.CREATE_TOPIC), r.getCreatorUid());
                    userPointService.addPoint(pointCmd);
                } catch (Exception e) {
                    LOGGER.error("Failed to add post to search engine, userId=" + r.getCreatorUid() + ", postId=" + r.getId(), e);
                }
            });

			return null;
		});
		
	}

    // 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170807
	private void populateRealPost(List<Post> posts){
        if(posts != null && posts.size()> 0){
            for(int i = 0; i < posts.size(); i++){
                if(PostCloneFlag.fromCode(posts.get(i).getCloneFlag()) == PostCloneFlag.CLONE){
                    Post temp = forumProvider.findPostById(posts.get(i).getRealPostId());
                    posts.set(i, temp);
                }
            }
        }
    }

    @Override
    public Forum findFourmByNamespaceId(Integer namespaceId){
        Forum forum = forumProvider.findForumByNamespaceId(namespaceId);
        return forum;
    }

    @Override
    public ListForumCategoryResponse listForumCategory(ListForumCategoryCommand cmd) {
        List<ForumCategory> list = forumProvider.listForumCategoryByForumId(cmd.getForumId());
        List<ForumCategoryDTO> dtos = list.stream().map(r -> ConvertHelper.convert(r, ForumCategoryDTO.class)).collect(Collectors.toList());

        ListForumCategoryResponse response = new ListForumCategoryResponse();
        response.setDtos(dtos);
        return response;
    }

    @Override
    public ForumCategoryDTO findForumCategory(FindForumCategoryCommand cmd) {
         ForumCategory category = forumProvider.findForumCategoryById(cmd.getId());
        return ConvertHelper.convert(category, ForumCategoryDTO.class);
    }

    @Override
    public GetForumSettingResponse getForumSetting(GetForumSettingCommand cmd) {
        GetForumSettingResponse response = new GetForumSettingResponse();

        //获取服务类型
        ListForumServiceTypesCommand serviceTypesCommand = new ListForumServiceTypesCommand();
        serviceTypesCommand.setNamespaceId(cmd.getNamespaceId());
        serviceTypesCommand.setModuleType(cmd.getModuleType());
        serviceTypesCommand.setCategoryId(cmd.getCategoryId());
        ListForumServiceTypesResponse serviceTypesDtos = listForumServiceTypes(serviceTypesCommand);
        response.setServiceTypes(serviceTypesDtos.getDtos());

        //话题的热门标签
        ListHotTagCommand tagCommand = new ListHotTagCommand();
        tagCommand.setNamespaceId(cmd.getNamespaceId());
        tagCommand.setModuleType(cmd.getModuleType());
        tagCommand.setCategoryId(cmd.getCategoryId());
        tagCommand.setServiceType(HotTagServiceType.TOPIC.getCode());
        List<TagDTO> tagDTOS = hotTagService.listHotTag(tagCommand);
        response.setTopicTags(tagDTOS);


        //活动的热门标签
        tagCommand.setServiceType(HotTagServiceType.ACTIVITY.getCode());
        tagDTOS = hotTagService.listHotTag(tagCommand);
        response.setActivityTags(tagDTOS);

        //投票的热门标签
        tagCommand.setServiceType(HotTagServiceType.POLL.getCode());
        tagDTOS = hotTagService.listHotTag(tagCommand);
        response.setPollTags(tagDTOS);

        //评论配置
        InteractSetting interactSetting = forumProvider.findInteractSetting(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId());
        if(interactSetting == null){
            response.setInteractFlag(InteractFlag.SUPPORT.getCode());
        }else {
            response.setInteractFlag(interactSetting.getInteractFlag());
        }


        return response;
    }

    @Override
    public void updateForumSetting(UpdateForumSettingCommand cmd) {

        List<ForumServiceType> newTypes = new ArrayList<>();

        if(cmd.getServiceTypes() != null){
            for (int i= 0; i<cmd.getServiceTypes().size(); i++){
                ForumServiceType type = ConvertHelper.convert(cmd.getServiceTypes().get(i), ForumServiceType.class);
                type.setId(null);
                type.setSortNum(i);
                type.setCreateTime(new Timestamp(System.currentTimeMillis()));
                type.setNamespaceId(cmd.getNamespaceId());
                type.setModuleType(cmd.getModuleType());
                type.setCategoryId(cmd.getCategoryId());
                newTypes.add(type);
            }
        }

        //页面同时点击报错导致了delete报错。这都被测试测出来了。加个锁吧。
        this.coordinationProvider.getNamedLock(CoordinationLocks.FORUM_SETTING.getCode()).enter(()-> {
            dbProvider.execute(status -> {

                List<ForumServiceType> oldTypes = forumProvider.listForumServiceTypes(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId());
                if (oldTypes != null && oldTypes.size() > 0) {
                    List<Long> oldTypeIds = oldTypes.stream().map(r -> r.getId()).collect(Collectors.toList());
                    forumProvider.deleteForumServiceTypes(oldTypeIds);
                }
                forumProvider.createForumServiceTypes(newTypes);

                ResetHotTagCommand resetHotTagCommand = new ResetHotTagCommand();
                resetHotTagCommand.setNamespaceId(cmd.getNamespaceId());
                resetHotTagCommand.setModuleType(cmd.getModuleType());
                resetHotTagCommand.setCategoryId(cmd.getCategoryId());

                //话题的热门标签
                if (cmd.getTopicTags() == null) {
                    cmd.setTopicTags(new ArrayList<>());
                }
                resetHotTagCommand.setServiceType(HotTagServiceType.TOPIC.getCode());
                resetHotTagCommand.setNames(cmd.getTopicTags().stream().map(r -> r.getName()).collect(Collectors.toList()));
                hotTagService.resetHotTag(resetHotTagCommand);

                //活动的热门标签
                if (cmd.getActivityTags() == null) {
                    cmd.setActivityTags(new ArrayList<>());
                }
                resetHotTagCommand.setServiceType(HotTagServiceType.ACTIVITY.getCode());
                resetHotTagCommand.setNames(cmd.getActivityTags().stream().map(r -> r.getName()).collect(Collectors.toList()));
                hotTagService.resetHotTag(resetHotTagCommand);

                //投票的热门标签
                if (cmd.getPollTags() == null) {
                    cmd.setPollTags(new ArrayList<>());
                }
                resetHotTagCommand.setServiceType(HotTagServiceType.POLL.getCode());
                resetHotTagCommand.setNames(cmd.getPollTags().stream().map(r -> r.getName()).collect(Collectors.toList()));
                hotTagService.resetHotTag(resetHotTagCommand);

                //更新评论开关
                saveInteractSetting(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId(), cmd.getInteractFlag());

                return null;

            });
            return null;
        });

    }

    @Override
    public InteractSettingDTO getInteractSetting(GetInteractSettingCommand cmd) {
        InteractSetting interactSetting = forumProvider.findInteractSetting(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId());
        InteractSettingDTO dto = null;
        if(interactSetting != null){
            dto =  ConvertHelper.convert(interactSetting, InteractSettingDTO.class);
        }else {
            dto = ConvertHelper.convert(cmd, InteractSettingDTO.class);
            dto.setInteractFlag(InteractFlag.SUPPORT.getCode());
        }
        return dto;
    }

    @Override
    public void updateInteractSetting(UpdateInteractSettingCommand cmd) {
        saveInteractSetting(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId(), cmd.getInteractFlag());
    }

    @Override
    public void saveInteractSetting(Integer namespaceId, Byte moduleType, Long categoryId, Byte interactFlag){

        //更新评论开关
        InteractSetting interactSetting = forumProvider.findInteractSetting(namespaceId, moduleType, categoryId);
        if(interactSetting != null){
            interactSetting.setInteractFlag(interactFlag);
            interactSetting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            forumProvider.updateInteractSetting(interactSetting);
        }else {
            interactSetting = new InteractSetting();
            interactSetting.setNamespaceId(namespaceId);
            interactSetting.setModuleType(moduleType);
            interactSetting.setCategoryId(categoryId);
            interactSetting.setInteractFlag(interactFlag);
            interactSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            forumProvider.createInteractSetting(interactSetting);
        }
    }

    @Override
    public ListForumServiceTypesResponse listForumServiceTypes(ListForumServiceTypesCommand cmd) {

        List<ForumServiceType> types = forumProvider.listForumServiceTypes(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId());

//        if(types == null || types.size() == 0){
//            types = forumProvider.listForumServiceTypes(0, null, null);
//        }

        ListForumServiceTypesResponse response = new ListForumServiceTypesResponse();

        if(types != null){
            List<ForumServiceTypeDTO> dtos = types.stream().map(r -> ConvertHelper.convert(r, ForumServiceTypeDTO.class)).collect(Collectors.toList());
            response.setDtos(dtos);
        }
        return response;

    }

    @Override
    public CheckModuleAppAdminResponse checkForumModuleAppAdmin(CheckModuleAppAdminCommand cmd) {
        CheckModuleAppAdminResponse res = new CheckModuleAppAdminResponse();

        Long userId = UserContext.currentUserId();
        if(userId == null){
            res.setFlag(TrueOrFalseFlag.FALSE.getCode());
            return res;
        }

        if(userPrivilegeMgr.checkSuperAdmin(userId, cmd.getCurrentOrgId())){
            LOGGER.debug("check super admin privilege success.userId={}, organizationId={}" , userId, cmd.getCurrentOrgId());
            res.setFlag(TrueOrFalseFlag.TRUE.getCode());
            return res;
        }

        Long moduleId = ForumModuleType.fromCode(cmd.getModuleType()).getModuleId();
        if(moduleId == null){
            LOGGER.debug("check moduleApp admin privilege fail, moduleId dest not exists. userId={}, forumModuleType={}, organizationId={}" , userId, cmd.getModuleType(), cmd.getCurrentOrgId());
            res.setFlag(TrueOrFalseFlag.FALSE.getCode());
            return res;
        }

        if(userPrivilegeMgr.checkModuleAdmin(EntityType.ORGANIZATIONS.getCode(), cmd.getCurrentOrgId(), userId, moduleId)){
            LOGGER.debug("check moduleApp admin privilege success. ownerType={}, ownerId={}, userId={}, moduleId={}" , EntityType.ORGANIZATIONS.getCode(), cmd.getCurrentOrgId(), userId, moduleId);
            res.setFlag(TrueOrFalseFlag.TRUE.getCode());
            return res;
        }

        res.setFlag(TrueOrFalseFlag.FALSE.getCode());
        return res;
    }



    @Override
    public FindDefaultForumResponse findDefaultForum(FindDefaultForumCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Community> communities = communityProvider.listCommunities(cmd.getNamespaceId(), locator, 1, null);

        FindDefaultForumResponse res = new FindDefaultForumResponse();
        if(communities != null && communities.size() > 0){
            Forum defaultForum = forumProvider.findForumById(communities.get(0).getDefaultForumId());
            if(defaultForum != null){
                res.setDefaultForum(ConvertHelper.convert(defaultForum, ForumDTO.class));
            }

            Forum feedbackForum = forumProvider.findForumById(communities.get(0).getFeedbackForumId());
            if(feedbackForum != null){
                res.setFeedbackForum(ConvertHelper.convert(feedbackForum, ForumDTO.class));
            }
        }
        return res;
    }


    @Override
    public ListTopicsByForumEntryIdResponse listTopicsByForumEntryId(ListTopicsByForumEntryIdCommand cmd) {

        if(cmd.getPageSize() == null) {
            cmd.setPageSize(20L);
        }

        FindDefaultForumCommand defaultForumCommand = new FindDefaultForumCommand();
        defaultForumCommand.setNamespaceId(cmd.getNamespaceId());
        FindDefaultForumResponse defaultForum = findDefaultForum(defaultForumCommand);

        if (cmd.getInstanceConfig() != null) {
            try {
                Map<String, String> insMap = (Map<String, String>) StringHelper.fromJsonString(cmd.getInstanceConfig(), Map.class);
                cmd.setTag(insMap.get("tag"));
                cmd.setForumEntryId(Long.valueOf(insMap.get("forumEntryId")));
            } catch (Exception e) {
                LOGGER.error("Instance config decode error, instanceConfig = " + cmd.getInstanceConfig(), e);
            }
        }

        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Post> posts = this.forumProvider.queryPosts(locator, cmd.getPageSize().intValue() + 1, (loc, query) -> {
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(defaultForum.getDefaultForum().getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ENTRY_ID.eq(cmd.getForumEntryId()));
            query.addConditions(Tables.EH_FORUM_POSTS.CLONE_FLAG.in(PostCloneFlag.NORMAL.getCode(), PostCloneFlag.REAL.getCode()));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            query.addConditions(Tables.EH_FORUM_POSTS.MODULE_TYPE.eq(ForumModuleType.FORUM.getCode()));
            if (cmd.getTag() != null) {
                query.addConditions(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
            }
            return query;
        });

        List<PostDTO> collect = posts.stream().map(r -> ConvertHelper.convert(r, PostDTO.class)).collect(Collectors.toList());
        ListTopicsByForumEntryIdResponse response = new ListTopicsByForumEntryIdResponse();
        response.setDtos(collect);
        return response;
    }

    @Override
    public PostDTO getPreviewTopic(NewTopicCommand cmd) {
        if (null == cmd) {
            LOGGER.error("cmd is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "cmd is null.");
        }
        PostDTO postDTO = ConvertHelper.convert(cmd, PostDTO.class);
        if (cmd.getAttachments() != null && cmd.getAttachments().size() > 0) {
            List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
            for (AttachmentDescriptor attachmentDescriptor : cmd.getAttachments()) {
                AttachmentDTO attachmentDTO = new AttachmentDTO();
                attachmentDTO.setContentUri(attachmentDescriptor.getContentUri());
                String url =  contentServerService.parserUri(attachmentDescriptor.getContentUri(), EntityType.TOPIC.getCode(), 0L);
                attachmentDTO.setContentUrl(url);
                attachmentDTO.setContentType(attachmentDescriptor.getContentType());
                attachmentDTOList.add(attachmentDTO);
            }
            postDTO.setAttachments(attachmentDTOList);
        }
        if (!StringUtils.isEmpty(cmd.getEmbeddedJson())) {
            ActivityPostCommand activityPostCommand = (ActivityPostCommand) StringHelper.fromJsonString(cmd.getEmbeddedJson(),
                    ActivityPostCommand.class);
            if (null != activityPostCommand) {
                String url =  contentServerService.parserUri(activityPostCommand.getPosterUri(), EntityType.TOPIC.getCode(), 0L);
                activityPostCommand.setPosterUrl(url);
            }
            postDTO.setEmbeddedJson(StringHelper.toJsonString(activityPostCommand));
            String embeddedJson = postDTO.getEmbeddedJson().replace("endTime","stopTime");
            postDTO.setEmbeddedJson(embeddedJson);
        }
        User user =  UserContext.current().getUser();
        if(user != null){
            if (user.getCommunityId() != null && StringUtils.isEmpty(postDTO.getCreatorCommunityName())) {
                Community community = communityProvider.findCommunityById(user.getCommunityId());
                if(community != null){
                    postDTO.setCreatorCommunityName(community.getName());
                }
            }
            if (StringUtils.isEmpty(postDTO.getCreatorNickName())) {
                postDTO.setCreatorNickName(user.getNickName());
            }
            String url =  contentServerService.parserUri(user.getAvatar(), EntityType.USER.getCode(), user.getId());
            postDTO.setCreatorAvatarUrl(url);
            postDTO.setCreatorAvatar(user.getAvatar());
        }
        if (postDTO.getCreateTime() == null) {
            Timestamp timeStamp = new Timestamp(new Date().getTime());
            postDTO.setCreateTime(timeStamp);
        }
        if(postDTO.getForumEntryId() == null){
            postDTO.setForumEntryId(0L);
        }
        //没有forumId，则设置当前域空间默认的forumId
        if(cmd.getForumId() == null) {
            setNamespaceDefaultForumId(cmd);
            postDTO.setForumId(cmd.getForumId());
        }
        if(postDTO.getInteractFlag() == null){
            postDTO.setInteractFlag(InteractFlag.SUPPORT.getCode());
        }
        if(postDTO.getLikeFlag() == null){
            postDTO.setLikeFlag(UserLikeType.NONE.getCode());
        }
        return postDTO;
    }
}
