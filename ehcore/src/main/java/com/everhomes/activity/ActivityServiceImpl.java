// @formatter:off
package com.everhomes.activity;

import ch.hsr.geohash.GeoHash;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.organization.*;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.group.LeaveGroupCommand;
import com.everhomes.rest.group.RejectJoinGroupRequestCommand;
import com.everhomes.rest.group.RequestToJoinGroupCommand;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.rest.promotion.ModulePromotionInfoDTO;
import com.everhomes.rest.promotion.ModulePromotionInfoType;
import com.everhomes.rest.ui.activity.ListActivityCategoryCommand;
import com.everhomes.rest.ui.activity.ListActivityCategoryReponse;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneReponse;
import com.everhomes.rest.ui.forum.SelectorBooleanFlag;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityCategories;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import net.greghaines.jesque.Job;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;
import static com.everhomes.util.RuntimeErrorException.errorWith;


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
	JesqueClientFactory jesqueClientFactory;
	
    @Autowired
    private ActivityProivider activityProvider;
    
    @Autowired
    private WarningSettingProvider warningSettingProvider;

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
    private GroupProvider groupProvider;
    
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
    
    @Autowired
    private ActivityVideoProvider activityVideoProvider;
    
    @Autowired
    private YzbDeviceProvider yzbDeviceProvider;
    
    @Autowired
    private YzbVideoService yzbVideoService;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Autowired
    private NamespacesProvider namespacesProvider;

    @Autowired
    private WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(WarnActivityBeginningAction.QUEUE_NAME);
    }

    @Override
    public void createPost(ActivityPostCommand cmd, Long postId) {
        User user = UserContext.current().getUser();
        Activity activity = ConvertHelper.convert(cmd, Activity.class);
        activity.setId(cmd.getId());
        activity.setPostId(postId);
        activity.setNamespaceId(0);
        activity.setCreatorUid(user.getId());
        activity.setGroupDiscriminator(EntityType.ACTIVITY.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
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
        Date signupEndTime = null;
        if (org.apache.commons.lang.StringUtils.isBlank(cmd.getSignupEndTime())) {
			signupEndTime = convertStartTime;
		}else {
			signupEndTime = convert(cmd.getSignupEndTime(), "yyyy-MM-dd HH:mm:ss");
		}
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
        activity.setSignupEndTime(new Timestamp(signupEndTime.getTime()));
        activity.setStartTimeMs(startTimeMs);
        activity.setEndTimeMs(endTimeMs);
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        //added by janson
        if(cmd.getIsVideoSupport() == null) {
            cmd.setIsVideoSupport((byte)0);
        }
        activity.setIsVideoSupport(cmd.getIsVideoSupport());
        activity.setVideoUrl(cmd.getVideoUrl());
        activity.setVideoState(VideoState.UN_READY.getCode());
        
        //add by tt, 添加版本号，20161018
        activity.setVersion(UserContext.current().getVersion());
        
        //add by xiongying, 添加类型id， 20161117
        activity.setCategoryId(cmd.getCategoryId());
        activity.setContentCategoryId(cmd.getContentCategoryId());
        
        activityProvider.createActity(activity);
        createScheduleForActivity(activity);
        
        ActivityRoster roster = new ActivityRoster();
        roster.setFamilyId(user.getAddressId());
        roster.setUid(user.getId());
        roster.setUuid(UUID.randomUUID().toString());
        roster.setActivityId(activity.getId());
        roster.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setConfirmFamilyId(user.getAddressId());
        roster.setAdultCount(0);
        roster.setChildCount(0);
        roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        roster.setCheckinFlag(CheckInStatus.CHECKIN.getCode());
        
        // 添加活动报名时新增的姓名、职位等信息, add by tt, 20170228
        addAdditionalInfo(roster, user, activity);
        
        activityProvider.createActivityRoster(roster);
        
    }

    //活动报名
    @Override
    public ActivityDTO signup(ActivitySignupCommand cmd) {
    	// 把锁放在查询语句的外面，update by tt, 20170210
        return (ActivityDTO)this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+cmd.getActivityId()).enter(()-> {
	        return (ActivityDTO)dbProvider.execute((status) -> {

		        User user = UserContext.current().getUser();
		        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
		        if (activity == null) {
		            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
		        }
		        //检查是否超过报名人数限制, add by tt, 20161012
		        if (activity.getMaxQuantity() != null && activity.getSignupAttendeeCount() >= activity.getMaxQuantity().intValue()) {
		        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
							"beyond contraint quantity!");
				}
		        
		        // 添加报名截止时间检查，add by tt, 20170228
		        Timestamp signupEndTime = getSignupEndTime(activity);
		        if (System.currentTimeMillis() > signupEndTime.getTime()) {
		        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_BEYOND_ACTIVITY_SIGNUP_END_TIME,
							"beyond activity signup end time!");
				}
        
		        Post post = forumProvider.findPostById(activity.getPostId());
		        if (post == null) {
		            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
		        }
		        ActivityRoster roster = createRoster(cmd, user, activity);
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
	            if (activity.getGroupId() != null && activity.getGroupId() != 0) {
	                RequestToJoinGroupCommand joinCmd = new RequestToJoinGroupCommand();
	                joinCmd.setGroupId(activity.getGroupId());
	                joinCmd.setRequestText("request to join activity group");
	                try{
	                    groupService.requestToJoinGroup(joinCmd);
	                }catch(Exception e){
	                    LOGGER.error("join to group failed",e);
	                }
	               
	            }
	            int adult = cmd.getAdultCount() == null ? 0 : cmd.getAdultCount();
	            int child = cmd.getChildCount() == null ? 0 : cmd.getChildCount();
	            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()+adult+child);
	            if(user.getAddressId()!=null){
	                activity.setSignupFamilyCount(activity.getSignupFamilyCount()+1);
	            }
	            activityProvider.createActivityRoster(roster);
	            activityProvider.updateActivity(activity);
//	            return status;
	            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
	            dto.setActivityId(activity.getId());
	            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
	            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
	            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
	            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
	            dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
	            dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
	            dto.setUserActivityStatus(getActivityStatus(roster).getCode());
	            dto.setFamilyId(activity.getCreatorFamilyId());
	            dto.setGroupId(activity.getGroupId());
	            dto.setStartTime(activity.getStartTime().toString());
	            dto.setStopTime(activity.getEndTime().toString());
	            dto.setSignupEndTime(getSignupEndTime(activity).toString());
	            dto.setProcessStatus(getStatus(activity).getCode());
	            dto.setForumId(post.getForumId());
	            dto.setPosterUrl(getActivityPosterUrl(activity));
	            fixupVideoInfo(dto);//added by janson
	            
	            //Send message to creator
	            Map<String, String> map = new HashMap<String, String>();
	            map.put("userName", user.getNickName());
	            map.put("postName", activity.getSubject());
	            sendMessageCode(activity.getCreatorUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_TO_CREATOR);
	            
	            return dto;
	        });
        }).first();
	 }
    
    @Override
	public SignupInfoDTO manualSignup(ManualSignupCommand cmd) {
    	Activity outActivity = checkActivityExist(cmd.getActivityId());
    	ActivityRoster activityRoster = (ActivityRoster)this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode() + cmd.getActivityId()).enter(()-> {
	        return (ActivityRoster)dbProvider.execute((status) -> {
		    	User user = UserContext.current().getUser();
		    	// 锁里面要重新查询活动
		        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
		        if (activity == null) {
		            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
		        }
		        //检查是否超过报名人数限制, add by tt, 20161012
		        if (activity.getMaxQuantity() != null && activity.getSignupAttendeeCount() >= activity.getMaxQuantity().intValue()) {
		        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
							"beyond contraint quantity!");
				}
		        
		        Post post = forumProvider.findPostById(activity.getPostId());
		        if (post == null) {
		            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
		        }
		        ActivityRoster roster = newRoster(cmd, user, activity);
		        
	            if (activity.getGroupId() != null && activity.getGroupId() != 0) {
	                RequestToJoinGroupCommand joinCmd = new RequestToJoinGroupCommand();
	                joinCmd.setGroupId(activity.getGroupId());
	                joinCmd.setRequestText("request to join activity group");
	                try{
	                    groupService.requestToJoinGroup(joinCmd);
	                }catch(Exception e){
	                    LOGGER.error("join to group failed",e);
	                }
	               
	            }
	            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()+1);
	            activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + 1);
	            activityProvider.createActivityRoster(roster);
	            activityProvider.updateActivity(activity);
	            return roster;
	        });
        }).first();
    	
		return convertActivityRoster(activityRoster, outActivity);
	}

	private SignupInfoDTO convertActivityRoster(ActivityRoster activityRoster, Activity activity) {
		SignupInfoDTO signupInfoDTO = ConvertHelper.convert(activityRoster, SignupInfoDTO.class);
		User user = getUserFromPhone(activityRoster.getPhone());
		signupInfoDTO.setNickName(user.getNickName());
		signupInfoDTO.setType(getAuthFlag(user));
		if (activity != null && activityRoster.getUid().longValue() == activity.getCreatorUid().longValue()) {
			signupInfoDTO.setCreateFlag((byte)1);
		}else {
			signupInfoDTO.setCreateFlag((byte)0);
		}
		
		return signupInfoDTO;
	}

	private Byte getAuthFlag(User user) {
		if (user.getId().longValue() == 0L) {
			return UserAuthFlag.NOT_REGISTER.getCode();
		}
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(user.getId());
		for (OrganizationMember organizationMember : members) {
			if (OrganizationMemberStatus.fromCode(organizationMember.getStatus()) == OrganizationMemberStatus.ACTIVE) {
				return UserAuthFlag.AUTH.getCode();
			}
		}
		return UserAuthFlag.NOT_AUTH.getCode();
	}

	private ActivityRoster newRoster(ManualSignupCommand cmd, User createUser, Activity activity) {
		User user = getUserFromPhone(cmd.getPhone());
		ActivityRoster roster = new ActivityRoster();
		roster.setUuid(UUID.randomUUID().toString());
		roster.setActivityId(activity.getId());
		roster.setUid(user.getId());
        roster.setAdultCount(1);
        roster.setChildCount(0);
        roster.setCheckinFlag(CheckInStatus.UN_CHECKIN.getCode());
        roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        roster.setConfirmUid(createUser.getId());
        roster.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setLotteryFlag((byte) 0);
        roster.setPhone(cmd.getPhone());
        roster.setRealName(cmd.getRealName());
        roster.setGender(cmd.getGender());
        roster.setCommunityName(cmd.getCommunityName());
        roster.setOrganizationName(cmd.getOrganizationName());
        roster.setPosition(cmd.getPosition());
        roster.setLeaderFlag(cmd.getLeaderFlag());
        roster.setSourceFlag(ActivityRosterSourceFlag.BACKEND_ADD.getCode());
        
        return roster;
	}

	private User getUserFromPhone(String phone) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
		if (userIdentifier != null) {
			User user = userProvider.findUserById(userIdentifier.getOwnerUid());
			if (user != null) {
				return user;
			}
		}
		User user = new User();
		user.setId(0L);
		user.setExecutiveTag((byte) 0);
		return user;
	}

	@Override
	public SignupInfoDTO updateSignupInfo(UpdateSignupInfoCommand cmd) {
		ActivityRoster activityRoster = (ActivityRoster)this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY_ROSTER.getCode()+cmd.getId()).enter(()-> {
	        return (ActivityRoster)dbProvider.execute((status) -> {
	        	ActivityRoster roster = convertToRoster(cmd);
	        	if (cmd.getCheckinFlag().byteValue() != roster.getCheckinFlag().byteValue()) {
	        		updateActivityCheckin(roster, cmd.getCheckinFlag());
	        		roster.setCheckinFlag(cmd.getCheckinFlag());
	        		roster.setCheckinUid(UserContext.current().getUser().getId());
	    		}
	        	activityProvider.updateRoster(roster);
	        	return roster;
	        });
		}).first();
		return convertActivityRoster(activityRoster, null);
	}

	private ActivityRoster convertToRoster(UpdateSignupInfoCommand cmd) {
		ActivityRoster roster = activityProvider.findRosterById(cmd.getId());
		roster.setRealName(cmd.getRealName());
		roster.setGender(cmd.getGender());
		roster.setCommunityName(cmd.getCommunityName());
		roster.setOrganizationName(cmd.getOrganizationName());
		roster.setPosition(cmd.getPosition());
		roster.setLeaderFlag(cmd.getLeaderFlag());
		return roster;
	}

	private void updateActivityCheckin(ActivityRoster roster, Byte toCheckinFlag) {
		// 签到的话活动表对应字段+1
		if (CheckInStatus.fromCode(roster.getCheckinFlag()) == CheckInStatus.UN_CHECKIN && CheckInStatus.fromCode(toCheckinFlag) == CheckInStatus.CHECKIN) {
			this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+roster.getActivityId()).enter(()-> {
				Activity activity = activityProvider.findActivityById(roster.getActivityId());
				activity.setCheckinAttendeeCount(activity.getCheckinAttendeeCount()
		                + (roster.getAdultCount() + roster.getChildCount()));
		        if (roster.getFamilyId() != null) {
		        	activity.setCheckinFamilyCount(activity.getCheckinFamilyCount() + 1);
		        }
		        activityProvider.updateActivity(activity);
				return null;
			});
			//取消签到的话活动表对应字段-1
		}else if (CheckInStatus.fromCode(roster.getCheckinFlag()) == CheckInStatus.CHECKIN && CheckInStatus.fromCode(toCheckinFlag) == CheckInStatus.UN_CHECKIN) {
			this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+roster.getActivityId()).enter(()-> {
				int result = 0;
				Activity activity = activityProvider.findActivityById(roster.getActivityId());
				activity.setCheckinAttendeeCount((result = activity.getCheckinAttendeeCount() - (roster.getAdultCount() + roster.getChildCount())) < 0 ? 0 : result);
		        if (roster.getFamilyId() != null){
		        	activity.setCheckinFamilyCount((result = activity.getCheckinFamilyCount() - 1) < 0 ? 0 : result);
		        }
		        activityProvider.updateActivity(activity);
				return null;
			});
		}
	}

	@Override
	public void importSignupInfo(ImportSignupInfoCommand cmd, MultipartFile[] files) {
		this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+cmd.getActivityId()).enter(()-> {
			User user = UserContext.current().getUser();
			Activity activity = checkActivityExist(cmd.getActivityId());
			List<ActivityRoster> rosters = getRostersFromExcel(files[0]);
			//检查是否超过报名人数限制, add by tt, 20161012
	        if (activity.getMaxQuantity() != null && activity.getSignupAttendeeCount().intValue() + rosters.size() > activity.getMaxQuantity().intValue()) {
	        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                    ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
						"beyond contraint quantity!");
			}
			
			dbProvider.execute(s->{
				rosters.forEach(r -> {
					r.setConfirmUid(user.getId());
					r.setActivityId(cmd.getActivityId());
					activityProvider.createActivityRoster(r);
				});
				activity.setSignupAttendeeCount(activity.getSignupAttendeeCount() + rosters.size());
	            activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + rosters.size());
	            activityProvider.updateActivity(activity);
	            
				return null;
			});
			return null;
		});
		
	}

	private List<ActivityRoster> getRostersFromExcel(MultipartFile file) {
		@SuppressWarnings("rawtypes")
		ArrayList rows = processorExcel(file);
		List<ActivityRoster> rosters = new ArrayList<>();
		for(int i=1, len=rows.size(); i<len; i++) {
			RowResult row = (RowResult) rows.get(i);
			if (org.apache.commons.lang.StringUtils.isBlank(row.getA())) {
				continue;
			}
			if (row.getA().trim().length() != 11 || !row.getA().trim().startsWith("1")) {
				throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                    ActivityServiceErrorCode.ERROR_PHONE, "invalid phone " + row.getA());
			}
			User user = getUserFromPhone(row.getA().trim());
			ActivityRoster roster = new ActivityRoster();
			roster.setUuid(UUID.randomUUID().toString());
			roster.setUid(user.getId());
	        roster.setAdultCount(1);
	        roster.setChildCount(0);
	        roster.setCheckinFlag(CheckInStatus.UN_CHECKIN.getCode());
	        roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
	        roster.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	        roster.setLotteryFlag((byte) 0);
	        roster.setPhone(row.getA().trim());
	        roster.setRealName(row.getB().trim());
	        roster.setGender(getGender(row.getC().trim()));
	        roster.setCommunityName(row.getD().trim());
	        roster.setOrganizationName(row.getE().trim());
	        roster.setPosition(row.getF().trim());
	        roster.setLeaderFlag(getLeaderFlag(row.getG().trim()));
	        roster.setSourceFlag(ActivityRosterSourceFlag.BACKEND_ADD.getCode());
	        
	        rosters.add(roster);
		}
		return rosters;
	}

	private Byte getLeaderFlag(String leaderFlag) {
		if ("是".equals(leaderFlag)) {
			return TrueOrFalseFlag.TRUE.getCode();
		}
		return TrueOrFalseFlag.FALSE.getCode();
	}

	private Byte getGender(String gender) {
		if ("男".equals(gender)) {
			return UserGender.MALE.getCode();
		}else if ("女".equals(gender)) {
			return UserGender.FEMALE.getCode();
		}
		return UserGender.UNDISCLOSURED.getCode();
	}

	@SuppressWarnings("rawtypes")
	private ArrayList processorExcel(MultipartFile file) {
		try {
            return PropMrgOwnerHandler.processorExcel(file.getInputStream());
        } catch (IOException e) {
			LOGGER.error("Process excel error.", e);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Process excel error.");
        }
	}
	
	private Activity checkActivityExist(Long activityId) {
		Activity activity = activityProvider.findActivityById(activityId);
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", activityId);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + activityId);
        }
        return activity;
	}

	@Override
	public ListSignupInfoResponse listSignupInfo(ListSignupInfoCommand cmd) {
		Activity activity = checkActivityExist(cmd.getActivityId());
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		List<ActivityRoster> rosters = activityProvider.listActivityRoster(cmd.getActivityId(), cmd.getPageAnchor(), pageSize+1);
		Long nextPageAnchor = null;
		if (rosters.size() > pageSize) {
			rosters.remove(rosters.size()-1);
			nextPageAnchor = rosters.get(rosters.size()-1).getId();
		}
		return new ListSignupInfoResponse(nextPageAnchor, rosters.stream().map(r->convertActivityRoster(r,activity)).collect(Collectors.toList()));
	}

	@Override
	public void exportSignupInfo(ExportSignupInfoCommand cmd, HttpServletResponse response) {
		Activity activity = checkActivityExist(cmd.getActivityId());
		List<ActivityRoster> rosters = activityProvider.listActivityRoster(cmd.getActivityId(), null, 100000);
		if (rosters.size() > 0) {
			List<SignupInfoDTO> signupInfoDTOs = rosters.stream().map(r->convertActivityRosterForExcel(r, activity)).collect(Collectors.toList());
			String fileName = String.format("报名信息_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "报名信息");
			List<String> propertyNames = new ArrayList<String>(Arrays.asList("phone", "nickName", "realName", "genderText", "organizationName", "position", "leaderFlagText",
					"typeText", "sourceFlagText"));
			List<String> titleNames = new ArrayList<String>(Arrays.asList("手机号", "用户昵称", "真实姓名", "性别", "公司", "职位", "是否高管", "类型", "报名来源"));
			List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 20, 10, 20, 20, 10, 20, 20));
			
			if (ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
				propertyNames.add("confirmFlagText");
				titleNames.add("报名确认");
				titleSizes.add(20);
			}
			if (CheckInStatus.fromCode(activity.getSignupFlag()) == CheckInStatus.CHECKIN) {
				propertyNames.add("checkinFlagText");
				titleNames.add("是否签到");
				titleSizes.add(10);
			}
			
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, signupInfoDTOs);
		}else {
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_NO_ROSTER, "no roster in this activity");
		}
	}
	
	private SignupInfoDTO convertActivityRosterForExcel(ActivityRoster roster, Activity activity) {
		SignupInfoDTO signupInfoDTO = convertActivityRoster(roster, activity);
		signupInfoDTO.setGenderText(UserGender.fromCode(signupInfoDTO.getGender())==null?UserGender.UNDISCLOSURED.getText():UserGender.fromCode(signupInfoDTO.getGender()).getText());
		signupInfoDTO.setLeaderFlagText(TrueOrFalseFlag.fromCode(signupInfoDTO.getLeaderFlag())==null?TrueOrFalseFlag.FALSE.getText():TrueOrFalseFlag.fromCode(signupInfoDTO.getLeaderFlag()).getText());
		signupInfoDTO.setTypeText(UserAuthFlag.fromCode(signupInfoDTO.getType())==null?UserAuthFlag.NOT_REGISTER.getText():UserAuthFlag.fromCode(signupInfoDTO.getType()).getText());
		signupInfoDTO.setSourceFlagText(ActivityRosterSourceFlag.fromCode(signupInfoDTO.getSourceFlag()).getText());
		signupInfoDTO.setConfirmFlagText(ConfirmStatus.fromCode(signupInfoDTO.getConfirmFlag())==null?ConfirmStatus.UN_CONFIRMED.getText():ConfirmStatus.fromCode(signupInfoDTO.getConfirmFlag()).getText());
		signupInfoDTO.setCheckinFlagText(CheckInStatus.fromCode(signupInfoDTO.getCheckinFlag())==null?CheckInStatus.UN_CHECKIN.getText():CheckInStatus.fromCode(signupInfoDTO.getCheckinFlag()).getText());
		return signupInfoDTO;
	}

	@Override
	public void deleteSignupInfo(DeleteSignupInfoCommand cmd) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY_ROSTER.getCode()+cmd.getId()).enter(()-> {
	        dbProvider.execute((status) -> {
	        	ActivityRoster roster = activityProvider.findRosterById(cmd.getId());
	        	activityProvider.deleteRoster(roster);
	        	updateActivityWhenDeleteRoster(roster);
	        	return null;
	        });
	        return null;
		});
	}

	private void updateActivityWhenDeleteRoster(ActivityRoster roster) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+roster.getActivityId()).enter(()-> {
			int total = roster.getAdultCount() + roster.getChildCount();
			int result = 0;
			Activity activity = activityProvider.findActivityById(roster.getActivityId());
			activity.setSignupAttendeeCount(activity.getSignupAttendeeCount() - total);
			if (ConfirmStatus.fromCode(roster.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
				activity.setConfirmAttendeeCount((result = activity.getConfirmAttendeeCount() - total) < 0 ? 0 : result);
				if (roster.getFamilyId() != null) {
					activity.setConfirmFamilyCount((result = activity.getConfirmFamilyCount() -1) < 0 ? 0 : result);
				}
			}
			if (CheckInStatus.fromCode(roster.getCheckinFlag()) == CheckInStatus.CHECKIN) {
				activity.setCheckinAttendeeCount((result = activity.getCheckinAttendeeCount() - total) < 0 ? 0 : result);
				if (roster.getFamilyId() != null) {
					activity.setCheckinFamilyCount((result = activity.getCheckinFamilyCount() - 1) < 0 ? 0 : result);
				}
			}
			activityProvider.updateActivity(activity);
			return null;
		});
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
        if(ConfirmStatus.UN_CONFIRMED == ConfirmStatus.fromCode(activity.getConfirmFlag())){
        	roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        }
        
        // 添加活动报名时新增的姓名、职位等信息, add by tt, 20170228
        addAdditionalInfo(roster, user, activity);
        
        return roster;
    }

    private void addAdditionalInfo(ActivityRoster roster, User user, Activity activity) {
    	SignupInfoDTO signupInfoDTO = verifyPerson(activity.getNamespaceId(), user);
    	roster.setPhone(signupInfoDTO.getPhone());
    	roster.setRealName(signupInfoDTO.getRealName());
    	roster.setGender(signupInfoDTO.getGender());
    	roster.setCommunityName(signupInfoDTO.getCommunityName());
    	roster.setOrganizationName(signupInfoDTO.getOrganizationName());
    	roster.setPosition(signupInfoDTO.getPosition());
    	roster.setLeaderFlag(signupInfoDTO.getLeaderFlag());
    	roster.setSourceFlag(ActivityRosterSourceFlag.SELF.getCode());
	}
    
    private SignupInfoDTO verifyPerson(Integer namespaceId, User user) {
    	SignupInfoDTO signupInfoDTO = new SignupInfoDTO();
    	List<UserIdentifier> userIdentifiers = userProvider.listUserIdentifiersOfUser(user.getId());
    	if (userIdentifiers != null && userIdentifiers.size() > 0) {
    		signupInfoDTO.setPhone(userIdentifiers.get(0).getIdentifierToken());
		}
    	signupInfoDTO.setGender(user.getGender());
    	signupInfoDTO.setLeaderFlag(user.getExecutiveTag()==null?TrueOrFalseFlag.FALSE.getCode():user.getExecutiveTag());
    	signupInfoDTO.setNickName(user.getNickName());
    	
    	OrganizationMember organizationMember = findAnyOrganizationMember(namespaceId, user.getId());
    	if (organizationMember != null) {
    		signupInfoDTO.setRealName(organizationMember.getContactName());
    		Organization organization = organizationProvider.findOrganizationById(organizationMember.getOrganizationId());
    		//如果是职位，取之
			if (OrganizationGroupType.fromCode(organizationMember.getGroupType()) == OrganizationGroupType.JOB_POSITION) {
				if (organization != null) {
					signupInfoDTO.setPosition(organization.getName());
				}
			}
			//找出公司
			Long organizationId = Long.parseLong(organization.getPath().split("/")[1]);
			Organization rootOrganization = organizationProvider.findOrganizationById(organizationId);
			if (rootOrganization != null) {
				signupInfoDTO.setOrganizationName(rootOrganization.getName());
				//找出园区
				OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(rootOrganization.getId());
				if (organizationCommunityRequest != null) {
					Community community = communityProvider.findCommunityById(organizationCommunityRequest.getCommunityId());
					if (community != null) {
						signupInfoDTO.setCommunityName(community.getName());
					}
				}
			}
		}
    	
    	return signupInfoDTO;
    }

	@Override
	public SignupInfoDTO vertifyPersonByPhone(VertifyPersonByPhoneCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getPhone());
		if (identifier != null) {
			User user = userProvider.findUserById(identifier.getOwnerUid());
			if (user != null) {
				return verifyPerson(namespaceId, user);
			}
		}
		return null;
	}

	private OrganizationMember findAnyOrganizationMember(Integer namespaceId, Long userId) {
		OrganizationMember organizationMember = organizationProvider.findAnyOrganizationMemberByNamespaceIdAndUserId(namespaceId, userId, OrganizationGroupType.JOB_POSITION.getCode());
		if (organizationMember == null) {
			organizationMember = organizationProvider.findAnyOrganizationMemberByNamespaceIdAndUserId(namespaceId, userId, OrganizationGroupType.ENTERPRISE.getCode());
		}
		return organizationMember;
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
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setForumId(post.getForumId());
        dto.setUserActivityStatus(ActivityStatus.UN_SIGNUP.getCode());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        fixupVideoInfo(dto);//added by janson
        
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
        if(acroster == null) {
        	LOGGER.error("handle activityRoster error ,the activityRoster does not exsit.activityId={}, userId = {}",cmd.getActivityId()
        			, user.getId());
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_CHECKIN_UN_CONFIRMED, "check in error id = {}, userId = {}", cmd.getActivityId(), user.getId());
        }
        // 签到增加异常消息 modify sfyan 20160712
        if(acroster.getConfirmFlag() == null) {
        	acroster.setConfirmFlag(ConfirmStatus.UN_CONFIRMED.getCode());
        }
        
        LOGGER.info("activity ConfirmFlag = " + activity.getConfirmFlag() + ", acroster ConfirmFlag = " + acroster.getConfirmFlag());
    	LOGGER.info("activity.getConfirmFlag() == null is " + (activity.getConfirmFlag() == null) 
    			+ "activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode() is " + (activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode())
    			+ "activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode() is " + (activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode())
    			+ "acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue()" + (acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue()));
        
    	if(ConfirmStatus.fromCode(acroster.getConfirmFlag()) != ConfirmStatus.CONFIRMED){
    		LOGGER.error("check in error ,has not officially Join activities.activityId = {}, userId = {}, confirmFlag = {} ",cmd.getActivityId()
        			, user.getId(), acroster.getConfirmFlag());
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_CHECKIN_UN_CONFIRMED, "check in error id = {}, userId = {}", cmd.getActivityId(), user.getId());
    	}
    	
    	if(CheckInStatus.fromCode(acroster.getCheckinFlag()) == CheckInStatus.CHECKIN){
    		LOGGER.error("check in error , already check in. activityId = {}, userId = {}, checkinFlag = {} ",cmd.getActivityId()
        			, user.getId(), acroster.getCheckinFlag());
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_CHECKIN_ALREADY, "check in error id = {}, userId = {}", cmd.getActivityId(), user.getId());
    	}
    	
    	dbProvider.execute(status->{
        	if(activity.getConfirmFlag() == null || ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.UN_CONFIRMED 
        			|| (ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.CONFIRMED && acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue())){
        		
        		Long familyId = getFamilyId();
        		
        		ActivityRoster roster = activityProvider.checkIn(activity, user.getId(), familyId);
//                Post p = createPost(user.getId(),post,null,"");
////                p.setContent(configurationProvider.getValue(CHECKIN_AUTO_COMMENT, ""));
//                p.setContent(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
//                    String.valueOf(ActivityLocalStringCode.ACTIVITY_CHECKIN), UserContext.current().getUser().getLocale(), ""));
                
                //字段错了 签到应记录到checkin字段中 modified by xiongying 20160708
                if (familyId != null)
                    activity.setCheckinFamilyCount(activity.getCheckinFamilyCount() + 1);
                activity.setCheckinAttendeeCount(activity.getCheckinAttendeeCount()
                        + (roster.getAdultCount() + roster.getChildCount()));
                
//                roster.setCheckinFlag((byte)1);
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
        
        //dto checkin名字不用 要手动转 modified by xiongying 20160708
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
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
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        fixupVideoInfo(dto);//added by janson
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
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        /////////////////////////////////////
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        fixupVideoInfo(dto);//added by janson
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
                d.setUserName(populateUserName(currentUser, activity.getPostId()));
                
                List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(currentUser.getId());
                
                List<String> phones = identifiers.stream().filter((a)-> { return IdentifierType.fromCode(a.getIdentifierType()) == IdentifierType.MOBILE; })
                        .map((a) -> { return a.getIdentifierToken(); })
                        .collect(Collectors.toList());
                d.setPhone(phones);
            }else {
				d.setUserName(r.getRealName());
				d.setPhone(Arrays.asList(r.getPhone()));
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
    
    private String populateUserName(User user, long postId) {
    	// 根据产品姚绮云要求，不要显示@xxxx, add by tt, 20170307
    	return user.getNickName();
//    	Post post = this.forumProvider.findPostById(postId);
//        VisibleRegionType regionType = VisibleRegionType.fromCode(post.getVisibleRegionType());
//        Long regionId = post.getVisibleRegionId();
//        
//        if(regionType != null && regionId != null) {
//            String creatorNickName = user.getNickName();
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
//                if(organization !=null)
//                	creatorNickName = creatorNickName + "@" + organization.getName();
//                break;
//            default:
//                LOGGER.error("Unsupported visible region type, userId=" + user.getId() 
//                    + ", regionType=" + regionType + ", postId=" + post.getId());
//            }
//            return creatorNickName;
//        } else {
//            LOGGER.error("Region type or id is null, userId=" + user.getId() + ", postId=" + post.getId());
//        }
//        
//        return "";
        
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
        
        // 后台管理系统确认不用判断是不是创建者
        User user = UserContext.current().getUser();
//        if (post.getCreatorUid().longValue() != user.getId().longValue()) {
//            LOGGER.error("the user is invalid.cannot confirm");
//            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
//                    ActivityServiceErrorCode.ERROR_INVALID_USER,
//                    "the user is invalid.cannot confirm id=" + cmd.getRosterId());
//        }
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
            item.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
            activityProvider.updateRoster(item);
            return status;
        });

        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setUserActivityStatus(getActivityStatus(item).getCode());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        fixupVideoInfo(dto);//added by janson
        
        //管理员同意活动的报名
        if (item.getUid().longValue() != 0L) {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("userName", user.getNickName());
        	map.put("postName", activity.getSubject());
        	sendMessageCode(item.getUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_CREATOR_CONFIRM_TO_USER);
		}
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
        //活动添加是否有活动附件标识 add by xiongying 20161207
        boolean existAttachments = activityProvider.existActivityAttachments(activity.getId());
        dto.setActivityAttachmentFlag(existAttachments);

        dto.setActivityId(activity.getId());
        dto.setForumId(post.getForumId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String convertStartTime = format.format(activity.getStartTime().getTime());
//        String convertEndTime = format.format(activity.getEndTime().getTime());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        fixupVideoInfo(dto);//added by janson
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
        // 后台管理系统不用判断是不是创建者
//        if (user.getId().longValue() != post.getCreatorUid().longValue()) {
//            LOGGER.error("No permission to reject the roster.rosterId={}", cmd.getRosterId());
//            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
//                    ActivityServiceErrorCode.ERROR_INVALID_USER, "invalid post id=" + postId);
//        }

        int total = roster.getAdultCount() + roster.getChildCount();
        dbProvider.execute(status->{
            //need lock
            activityProvider.deleteRoster(roster);
            if (ConfirmStatus.fromCode(roster.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
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
        
        if (roster.getUid().longValue() != 0L) {
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
//            String template=configurationProvider.getValue(REJECT_AUTO_COMMENT, "");
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
                put("subject", activity.getSubject());
                put("reason",cmd.getReason()==null?"":cmd.getReason());
                put("username",queryUser.getNickName()==null?queryUser.getAccountName():queryUser.getNickName());
                
            }}, ""));
//            forumProvider.createPost(comment);
            
            
            
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

        //活动添加是否有活动附件标识 add by xiongying 20161207
        boolean existAttachments = activityProvider.existActivityAttachments(activity.getId());
        dto.setActivityAttachmentFlag(existAttachments);

        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String convertStartTime = format.format(activity.getStartTime().getTime());
//        String convertEndTime = format.format(activity.getEndTime().getTime());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setForumId(post.getForumId());
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        fixupVideoInfo(dto);//added by janson
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
                }else {
					d.setUserName(r.getRealName());
					d.setPhone(Arrays.asList(r.getPhone()));
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
    	Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

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
        List<Activity> ret = activityProvider.listActivities(locator, value+1, condtion, false);
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
            dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
            dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setSignupEndTime(getSignupEndTime(activity).toString());
            dto.setGroupId(activity.getGroupId());
            dto.setPosterUrl(getActivityPosterUrl(activity));
            dto.setForumId(post.getForumId());
            fixupVideoInfo(dto);//added by janson
            return dto;
            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
         // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
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
       List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition, false).stream().map(activity->{
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
          dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
          dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
          dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
          dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
          dto.setProcessStatus(getStatus(activity).getCode());
          dto.setFamilyId(activity.getCreatorFamilyId());
          dto.setPosterUrl(getActivityPosterUrl(activity));
          dto.setStartTime(activity.getStartTime().toString());
          dto.setStopTime(activity.getEndTime().toString());
          dto.setSignupEndTime(getSignupEndTime(activity).toString());
          dto.setGroupId(activity.getGroupId());
          dto.setForumId(post.getForumId());
          User user = UserContext.current().getUser();
          if (user != null) {
        	  List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
        	  if (favorite == null || favorite.size() == 0) {
        		  dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
        	  } else {
        		  dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
        	  }
        	  //add UserActivityStatus by xiongying 20160628
        	  ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
        	  dto.setUserActivityStatus(getActivityStatus(roster).getCode());
          }else {
        	  dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
          }
          fixupVideoInfo(dto);//added by janson
          return dto;
           //全部查速度太慢，先把查出的部分排序 by xiongying20161208
       // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
       })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
       if(result.size()<pageSize)
       {
           locator.setAnchor(null);
       }
        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
    }

    private Timestamp getSignupEndTime(Activity activity) {
    	if (activity.getSignupEndTime() == null) {
			return activity.getStartTime();
		}
    	return activity.getSignupEndTime();
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
		
		createScheduleForActivity(activity);
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
		List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition, false).stream().map(activity->{
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
	        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
	        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
			dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setPosterUrl(getActivityPosterUrl(activity));
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
	        dto.setSignupEndTime(getSignupEndTime(activity).toString());
			dto.setGroupId(activity.getGroupId());
			dto.setForumId(post.getForumId());
			User user = UserContext.current().getUser();
			if (user != null) {
            	List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
            	if (favorite == null || favorite.size() == 0) {
            		dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            	} else {
            		dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            	}
            	//add UserActivityStatus by xiongying 20160628
            	ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
            	dto.setUserActivityStatus(getActivityStatus(roster).getCode());
			}else {
				dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
			}
			fixupVideoInfo(dto);//added by janson
			return dto;

            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
			// 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
       
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
		List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition, false).stream().map(activity->{
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
	        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
	        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
			dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setPosterUrl(getActivityPosterUrl(activity));
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
	        dto.setSignupEndTime(getSignupEndTime(activity).toString());
			dto.setGroupId(activity.getGroupId());
			dto.setForumId(post.getForumId());
			fixupVideoInfo(dto);//added by janson
			return dto;

            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
			// 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
       
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
        execCmd.setOfficialFlag(cmd.getOfficialFlag());
        execCmd.setCategoryId(cmd.getCategoryId());
        execCmd.setContentCategoryId(cmd.getContentCategoryId());
        execCmd.setActivityStatusList(cmd.getActivityStatusList());
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
//            dto.setPosterUrl(getActivityPosterUrl(activity));
//            dto.setForumId(post.getForumId());
//            return dto;
//        }).filter(r->r!=null).collect(Collectors.toList());
//        if(activityDtos.size()<value){
//            locator.setAnchor(null);
//        }
//        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
	}
	
	private List<Long> getViewedActivityIds() {
		
		List<Long> ids = new ArrayList<Long>();
		UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(UserContext.current().getUser().getId(), 
				UserProfileContstant.VIEWED_ACTIVITY_NEW);
        
		if(userProfile != null) {
	        String idString = userProfile.getStringTag1();
	        String id = idString.substring(1, idString.toString().length()-1);
	        if(!StringUtils.isEmpty(id)) {
	        	String[] idArr = id.split(", ");
				Long[] idLong = new Long[idArr.length];
				for(int i = 0; i < idArr.length; i++) {
					idLong[i] = Long.parseLong(idArr[i]);
				}
				ids = Arrays.asList(idLong);
			}
        }
		
		return ids;
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

        //comment by tt, 20170116
//        if(null != cmd.getOfficialFlag()){
//            condition = condition.and(Tables.EH_ACTIVITIES.OFFICIAL_FLAG.eq(cmd.getOfficialFlag()));
//        }
//
//        //增加活动类型判断add by xiongying 20161117
//        if(null != cmd.getCategoryId()) {
//            ActivityCategories category = activityProvider.findActivityCategoriesById(cmd.getCategoryId());
//            if (category != null) {
//            	if(SelectorBooleanFlag.TRUE.equals(SelectorBooleanFlag.fromCode(category.getDefaultFlag()))) {
//                    condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.in(cmd.getCategoryId(), 0L));
//                } else {
//                    condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
//                }
//			}
//        }
        
        // 旧版本查询活动时，只有officialFlag标记，新版本查询活动时有categoryId，当然更老的版本两者都没有
        // 为了兼容，规定categoryId为0对应发现里的活动（非官方活动），categoryId为1对应原官方活动
        if (cmd.getCategoryId() == null) {
        	OfficialFlag officialFlag = OfficialFlag.fromCode(cmd.getOfficialFlag());
			if(officialFlag == null) officialFlag = OfficialFlag.NO;
			Long categoryId = officialFlag == OfficialFlag.YES?1L:0L;
			cmd.setCategoryId(categoryId);
		}
//        else {
//			officialFlag = categoryId.longValue() == 1L?OfficialFlag.YES:OfficialFlag.NO;
//		}
        // 把officialFlag换成categoryId一个条件
        condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
        
        //增加活动主题分类，add by tt, 20170109
        if (cmd.getContentCategoryId() != null) {
        	ActivityCategories category = activityProvider.findActivityCategoriesById(cmd.getContentCategoryId());
        	//如果没有查到分类或者分类的allFlag为是，则表示查询全部，不用加条件
        	if (category != null && TrueOrFalseFlag.FALSE == TrueOrFalseFlag.fromCode(category.getAllFlag())) {
        		condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(cmd.getContentCategoryId()));
			}
		}
        
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        List<Activity> activities=new ArrayList<Activity>();
        
        //查第一页时，一部分为上次查询过后新发的贴 modified by xiongying 20160707
        //产品1.6需求：去掉查一部分新发的贴的功能 modified by xiongying 20161208
//        if (locator.getAnchor() == null || locator.getAnchor() == 0L){
//        	Timestamp lastViewedTime = null;
//        	String counts = configurationProvider.getValue(ConfigConstants.ACTIVITY_LIST_NUM, "3");
//        	UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(UserContext.current().getUser().getId(),
//        								UserProfileContstant.VIEWED_ACTIVITY_NEW);
//
//        	if(profile != null) {
//        		lastViewedTime = new Timestamp(Long.valueOf(profile.getItemValue()));
//        	}
//        	List<Activity> newActivities = activityProvider.listNewActivities(locator, Integer.valueOf(counts), lastViewedTime, condition);
//	    	if(newActivities != null && newActivities.size() > 0) {
//	    		activities.addAll(newActivities);
//	    	}
//        }
//
//		List<Long> ids = getViewedActivityIds();

        // 添加活动状态筛选     add by xq.tian  2017/01/24
        if (cmd.getActivityStatusList() != null) {
            Condition statusCondition = this.buildActivityProcessStatusCondition(cmd.getActivityStatusList());
            if (statusCondition != null) {
                condition = condition.and(statusCondition);
            }
        }

        List<Activity> ret = activityProvider.listActivities(locator, pageSize - activities.size() + 1, condition, false);
        
//        if(ret != null && ret.size() > 0) {
//        	for(Activity act : ret) {
//        		if(!ids.contains(act.getId())) {
//        			activities.add(act);
//            	}
//        	}
//        }


        activities.addAll(ret);
        List<ActivityDTO> activityDtos = activities.stream().map(activity->{
            Post post = forumProvider.findPostById(activity.getPostId());
            if (activity.getPosterUri() == null && post != null) {
                this.forumProvider.populatePostAttachments(post);
                List<Attachment> attachmentList = post.getAttachments();
                if (attachmentList != null && attachmentList.size() != 0) {
                    for (Attachment attachment : attachmentList) {
                        if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
                            activity.setPosterUri(attachment.getContentUri());
                        break;
                    }
                }
            }
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
            dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
            dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setSignupEndTime(getSignupEndTime(activity).toString());
            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(getActivityPosterUrl(activity));
            String posterUrl = getActivityPosterUrl(activity);
            dto.setPosterUrl(posterUrl);
            if (post != null) {
                dto.setForumId(post.getForumId());
            }
            List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(uid, UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
            if (favorite == null || favorite.size() == 0) {
                dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            } else {
                dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            }
            //add UserActivityStatus by xiongying 20160628
            ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), uid);
            dto.setUserActivityStatus(getActivityStatus(roster).getCode());
            fixupVideoInfo(dto);//added by janson

            return dto;
            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
         // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/
        collect(Collectors.toList());

        Long nextPageAnchor = locator.getAnchor();

        response = new ListActivitiesReponse(nextPageAnchor, activityDtos);
        return response;
	}

    private Condition buildActivityProcessStatusCondition(List<Integer> activityStatusList) {
        Timestamp currTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        Condition notStartCond = null;
        Condition underWayCond = null;
        Condition endCond = null;
        if (activityStatusList.contains(NOTSTART.getCode())) {
            notStartCond = Tables.EH_ACTIVITIES.START_TIME.gt(currTime);
        }
        if (activityStatusList.contains(UNDERWAY.getCode())) {
            underWayCond = Tables.EH_ACTIVITIES.START_TIME.le(currTime).and(Tables.EH_ACTIVITIES.END_TIME.gt(currTime));
        }
        if (activityStatusList.contains(ProcessStatus.END.getCode())) {
            endCond = Tables.EH_ACTIVITIES.END_TIME.lt(currTime);
        }
        Optional<Condition> condition = Stream.of(notStartCond, underWayCond, endCond).filter(Objects::nonNull).reduce(Condition::or);
        return condition.isPresent() ? condition.get() : null;
    }

    private String getActivityPosterUrl(Activity activity) {
		
		if(activity.getPosterUri() == null) {
			String posterUrl = contentServerService.parserUri(configurationProvider.getValue(ConfigConstants.ACTIVITY_POSTER_DEFAULT_URL, ""), EntityType.ACTIVITY.getCode(), activity.getId());
			return posterUrl;
		} else {
			String posterUrl = contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId());
			if(posterUrl.equals(activity.getPosterUri())) {
				posterUrl = contentServerService.parserUri(configurationProvider.getValue(ConfigConstants.ACTIVITY_POSTER_DEFAULT_URL, ""), EntityType.ACTIVITY.getCode(), activity.getId());
			}
			return posterUrl;
		}
		
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
//            dto.setPosterUrl(getActivityPosterUrl(activity));
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
		    	        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
		    	        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
		    	        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
		    	        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
		    	        dto.setProcessStatus(getStatus(activity).getCode());
		    	        dto.setFamilyId(activity.getCreatorFamilyId());
		    	        dto.setStartTime(activity.getStartTime().toString());
		    	        dto.setStopTime(activity.getEndTime().toString());
		    	        dto.setSignupEndTime(getSignupEndTime(activity).toString());
		    	        dto.setGroupId(activity.getGroupId());
		    	        dto.setPosterUrl(getActivityPosterUrl(activity));
		    	        dto.setForumId(r.getForumId());
		    	        dto.setGuest(activity.getGuest());
		    	        fixupVideoInfo(dto);//added by janson
		    			
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
//            execOrgCmd.setCommunityId(sceneTokenDto.getEntityId());
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
		
		// 非登录用户只能看第一页 add by xiongying20161010
    	if(cmd.getPageAnchor() != null ) {
    		 if(!userService.isLogon()){
    			 LOGGER.error("Not logged in.");
  			   throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
  					   "Not logged in.");

    		 }
    	}
		
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
        //检查游客是否能继续访问此场景 by sfyan 20161009
        userService.checkUserScene(sceneType);
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
		Integer namespaceId = UserContext.getCurrentNamespaceId();
	    if(communityId != null) {
    	    ListActivitiesByTagCommand execCmd = new ListActivitiesByTagCommand();
            execCmd.setCommunity_id(communityId);
            execCmd.setAnchor(cmd.getPageAnchor());
            execCmd.setPageSize(cmd.getPageSize());
            execCmd.setTag(cmd.getTag());
            execCmd.setRange(geoCharCount);
            execCmd.setCategoryId(cmd.getCategoryId());
            execCmd.setContentCategoryId(cmd.getContentCategoryId());
            execCmd.setActivityStatusList(cmd.getActivityStatusList());
            if(999987L == namespaceId){
            	execCmd.setOfficialFlag(OfficialFlag.NO.getCode());
            }
            return listActivitiesByTag(execCmd);
	    } else {
	        LOGGER.error("Community not found to query nearby activities, sceneTokenDto={}, communityId={}", sceneTokenDto, communityId);
	        return null;
	    }
	}
	
	@Override
    public ListActivitiesReponse listOrgNearbyActivities(ListOrgNearbyActivitiesCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
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
        execCmd.setCategoryId(cmd.getCategoryId());
        execCmd.setContentCategoryId(cmd.getContentCategoryId());
        execCmd.setActivityStatusList(cmd.getActivityStatusList());
        if(999987L == namespaceId){
        	execCmd.setOfficialFlag(OfficialFlag.NO.getCode());
        }
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
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        fixupVideoInfo(dto);//added by janson
        response.setActivity(dto);
        
        response.setContent(post.getContent());
        response.setChildCount(post.getChildCount());
        response.setViewCount(post.getViewCount());
        response.setNamespaceId(activity.getNamespaceId());
        response.setAttachments(post.getAttachments());
        response.setSubject(post.getSubject());
        response.setCreatorNickName(post.getCreatorNickName());
        response.setCreateTime(post.getCreateTime());
        response.setCreatorAvatarUrl(post.getCreatorAvatarUrl());
        
		return response;
	}

//	@Override
//	public ListActivitiesReponse listOfficialActivitiesByScene(ListNearbyActivitiesBySceneCommand command) {
//		Long userId = UserContext.current().getUser().getId();
//		QueryOrganizationTopicCommand cmd = new QueryOrganizationTopicCommand();
//		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(command.getSceneToken(), SceneTokenDTO.class);
//		processOfficalActivitySceneToken(userId, sceneTokenDTO, cmd);
//		cmd.setContentCategory(CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
//		cmd.setEmbeddedAppId(AppConstants.APPID_ACTIVITY);
//		cmd.setOfficialFlag(OfficialFlag.YES.getCode());
//		cmd.setPageAnchor(command.getPageAnchor());
//		cmd.setPageSize(command.getPageSize());
//		
//		// 由于listOrgTopics查询官方活动时，当一个机构下面没有管理小区或者以普通公司的身份查询的时候，会查不到东西，使用新的方法来解决 by lqs 20160730
//		// ListPostCommandResponse postResponse = forumService.listOrgTopics(cmd);
//		ListPostCommandResponse postResponse = forumService.listOfficialActivityTopics(cmd);
//		List<PostDTO> posts = postResponse.getPosts();		
//		final List<ActivityDTO> activities = new ArrayList<>();
//		if (posts != null && posts.size() > 0) {
//			posts.forEach(p->{
//				//吐槽：这里ActivityPostCommand和ActivityDTO中相同的字段，名字竟然不一样，如postUri和postUrl
//				ActivityDTO activity = (ActivityDTO) StringHelper.fromJsonString(p.getEmbeddedJson().replace("posterUri", "posterUrl"), ActivityDTO.class);
//				activity.setFavoriteFlag(p.getFavoriteFlag());
//				fixupVideoInfo(activity);//added by janson
//				activities.add(activity);
//			});
//		}
//		
//		ListActivitiesReponse reponse = new ListActivitiesReponse(postResponse.getNextPageAnchor(), activities);
//		return reponse;
//	}
	
	@Override
	public ListActivitiesReponse listOfficialActivitiesByScene(ListNearbyActivitiesBySceneCommand command) {
		Long userId = UserContext.current().getUser().getId();
		QueryOrganizationTopicCommand cmd = new QueryOrganizationTopicCommand();
		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(command.getSceneToken(), SceneTokenDTO.class);
		processOfficalActivitySceneToken(userId, sceneTokenDTO, cmd);
		cmd.setContentCategory(CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
		cmd.setEmbeddedAppId(AppConstants.APPID_ACTIVITY);
		cmd.setOfficialFlag(OfficialFlag.YES.getCode());
		cmd.setPageAnchor(command.getPageAnchor());
		cmd.setPageSize(command.getPageSize());
		cmd.setCategoryId(command.getCategoryId());
		cmd.setContentCategoryId(command.getContentCategoryId());
        cmd.setActivityStatusList(command.getActivityStatusList());
		
		ListActivitiesReponse activities = listOfficialActivities(cmd);
		
		return activities;
	}
	
	@Override
	public ListActivitiesReponse listOfficialActivities(QueryOrganizationTopicCommand cmd) {
		long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long organizationId = cmd.getOrganizationId();
        Long communityId = cmd.getCommunityId();
        List<Long> forumIds = new ArrayList<Long>();
        
        List<Long> communityIdList = new ArrayList<Long>();
        // 获取所管理的所有小区对应的社区论坛
//        if(organizationId != null) {
//            ListCommunitiesByOrganizationIdCommand command = new ListCommunitiesByOrganizationIdCommand();
//            command.setOrganizationId(organizationId);
//            List<CommunityDTO> communities = organizationService.listCommunityByOrganizationId(command).getCommunities();
//            if(communities != null){
//                for (CommunityDTO communityDTO : communities) {
//                    communityIdList.add(communityDTO.getId());
//                    forumIds.add(communityDTO.getDefaultForumId());
//                }
//            }
//        }
//        // 办公地点所在园区对应的社区论坛
//        if(communityId != null) {
//            Community community = communityProvider.findCommunityById(communityId);
//            communityIdList.add(community.getId());
//            forumIds.add(community.getDefaultForumId());
//        }

        if(null == communityId){
        	// 如果发送范围选择的公司圈，需要加上公司的论坛，add by tt, 20170307
        	Organization organization = organizationProvider.findOrganizationById(organizationId);
        	if (organization != null) {
        		if (organization.getGroupId() != null) {
        			Group group = groupProvider.findGroupById(organization.getGroupId());
        			if (group != null) {
        				forumIds.add(group.getOwningForumId());
        			}
        		}
			}
            List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organizationId);
            if(null != communities){
                for (CommunityDTO communityDTO : communities) {
                    communityIdList.add(communityDTO.getId());
                    forumIds.add(communityDTO.getDefaultForumId());
                }
            }
        }else{
            Community community = communityProvider.findCommunityById(communityId);
            communityIdList.add(community.getId());
            forumIds.add(community.getDefaultForumId());
        }

        // 当论坛list为空时，JOOQ的IN语句会变成1=0，导致条件永远不成立，也就查不到东西
        if(forumIds.size() == 0) {
            LOGGER.error("Forum not found for offical activities, cmd={}", cmd);
            return null;
        }
        
        Condition activityCondition = Tables.EH_ACTIVITIES.FORUM_ID.in(forumIds);
        
        //增加categoryId add by xiongying 20161118
        if(null != cmd.getCategoryId()) {
            ActivityCategories category = activityProvider.findActivityCategoriesById(cmd.getCategoryId());
            if (category != null) {
            	if(SelectorBooleanFlag.TRUE.equals(SelectorBooleanFlag.fromCode(category.getDefaultFlag()))) {
                    activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.in(cmd.getCategoryId(), 0L));
                } else {
                    activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
                }
			}
        }
        //增加活动主题分类，add by tt, 20170109
        if (cmd.getContentCategoryId() != null) {
        	ActivityCategories category = activityProvider.findActivityCategoriesById(cmd.getContentCategoryId());
        	//如果没有查到分类或者分类的allFlag为是，则表示查询全部，不用加条件
        	if (category != null && TrueOrFalseFlag.FALSE == TrueOrFalseFlag.fromCode(category.getAllFlag())) {
        		activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(cmd.getContentCategoryId()));
			}
		}
        
        // 可见性条件：如果有当前小区/园区，则加上小区条件；如果有对应的管理机构，则加上机构条件；这两个条件为或的关系；
        Condition communityCondition = null;
        if(communityIdList != null) {
            communityCondition = Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            communityCondition = communityCondition.and(Tables.EH_ACTIVITIES.VISIBLE_REGION_ID.in(communityIdList));
        }
        Condition orgCondition = null;
        if(organizationId != null) {
            orgCondition = Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            orgCondition = orgCondition.and(Tables.EH_ACTIVITIES.VISIBLE_REGION_ID.eq(organizationId));
        }
        Condition visibleCondition = communityCondition;
        if(visibleCondition == null) {
            visibleCondition = orgCondition;
        } else {
        	if (orgCondition != null) {
        		visibleCondition = visibleCondition.or(orgCondition);
			}
        }
        
        Condition condition = activityCondition;
        if(visibleCondition != null) {
            condition = condition.and(visibleCondition);
        }
        condition = condition.and(Tables.EH_ACTIVITIES.OFFICIAL_FLAG.eq(OfficialFlag.YES.getCode()));

        // 添加活动状态筛选     add by xq.tian  2017/01/24
        if (cmd.getActivityStatusList() != null) {
            Condition statusCondition = this.buildActivityProcessStatusCondition(cmd.getActivityStatusList());
            if (statusCondition != null) {
                condition = condition.and(statusCondition);
            }
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        // TODO: Locator里设置系统论坛ID存在着分区的风险，因为上面的条件是多个论坛，需要后面理顺  by lqs 20160730
        CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(cmd.getPageAnchor());

        Boolean orderByCreateTime = false;
        if(cmd.getOrderByCreateTime() != null && cmd.getOrderByCreateTime() == 1) {
            orderByCreateTime = true;
        }
        List<ActivityDTO> dtos = this.getOrgActivities(locator, pageSize, condition, cmd.getPublishStatus(), orderByCreateTime);
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query offical activities, userId=" + operatorId + ", size=" + dtos.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }   
        
        ListActivitiesReponse response = new ListActivitiesReponse(locator.getAnchor(), dtos);
        return response;
	}
	
	private List<ActivityDTO> getOrgActivities(CrossShardListingLocator locator,Integer pageSize, Condition condition, String publishStatus, Boolean orderByCreateTime){
    	User user = UserContext.current().getUser();
    	
    	Timestamp timestemp = new Timestamp(DateHelper.currentGMTTime().getTime());
    	
    	
    	condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));
        
        if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.UNPUBLISHED){
        	condition = condition.and(Tables.EH_ACTIVITIES.START_TIME.gt(timestemp));
        }
        
        if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.PUBLISHED){
        	condition = condition.and(Tables.EH_ACTIVITIES.START_TIME.lt(timestemp));
        	condition = condition.and(Tables.EH_ACTIVITIES.END_TIME.gt(timestemp));
        }
        
        if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.EXPIRED){
        	condition = condition.and(Tables.EH_ACTIVITIES.END_TIME.lt(timestemp));
        }

        if(orderByCreateTime == null) {
            orderByCreateTime = false;
        }
        List<Activity> activities = this.activityProvider.listActivities(locator, pageSize + 1, condition, orderByCreateTime);

        if(orderByCreateTime) {
            List<ActivityDTO> activityDtos = activities.stream().map(activity -> {
                Post post = forumProvider.findPostById(activity.getPostId());
                if (post == null) {
                    return null;
                }
                if (activity.getPosterUri() == null) {
                    this.forumProvider.populatePostAttachments(post);
                    List<Attachment> attachmentList = post.getAttachments();
                    if (attachmentList != null && attachmentList.size() != 0) {
                        for (Attachment attachment : attachmentList) {
                            if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
                                activity.setPosterUri(attachment.getContentUri());
                            break;
                        }
                    }
                }
                ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
                dto.setActivityId(activity.getId());
                dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
                dto.setEnrollUserCount(activity.getSignupAttendeeCount());
                dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
                dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
                dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
                dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
                dto.setProcessStatus(getStatus(activity).getCode());
                dto.setFamilyId(activity.getCreatorFamilyId());
                dto.setStartTime(activity.getStartTime().toString());
                dto.setStopTime(activity.getEndTime().toString());
                dto.setSignupEndTime(getSignupEndTime(activity).toString());
                dto.setGroupId(activity.getGroupId());
                dto.setPosterUrl(getActivityPosterUrl(activity));
                if (user != null) {
                	List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
                    if (favorite == null || favorite.size() == 0) {
                        dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
                    } else {
                        dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
                    }
                    //add UserActivityStatus by xiongying 20160628
                    ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
                    dto.setUserActivityStatus(getActivityStatus(roster).getCode());
				}else {
					dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
				}
                fixupVideoInfo(dto);
                return dto;
            }).filter(r -> r != null).collect(Collectors.toList());

            return activityDtos;
        } else {
            List<ActivityDTO> activityDtos = activities.stream().map(activity -> {
                Post post = forumProvider.findPostById(activity.getPostId());
                if (post == null) {
                    return null;
                }
                if (activity.getPosterUri() == null) {
                    this.forumProvider.populatePostAttachments(post);
                    List<Attachment> attachmentList = post.getAttachments();
                    if (attachmentList != null && attachmentList.size() != 0) {
                        for (Attachment attachment : attachmentList) {
                            if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
                                activity.setPosterUri(attachment.getContentUri());
                            break;
                        }
                    }
                }
                ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
                dto.setActivityId(activity.getId());
                dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
                dto.setEnrollUserCount(activity.getSignupAttendeeCount());
                dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
                dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
                dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
                dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
                dto.setProcessStatus(getStatus(activity).getCode());
                dto.setFamilyId(activity.getCreatorFamilyId());
                dto.setStartTime(activity.getStartTime().toString());
                dto.setStopTime(activity.getEndTime().toString());
                dto.setSignupEndTime(getSignupEndTime(activity).toString());
                dto.setGroupId(activity.getGroupId());
                dto.setPosterUrl(getActivityPosterUrl(activity));
                if (user != null) {
                	List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
                    if (favorite == null || favorite.size() == 0) {
                        dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
                    } else {
                        dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
                    }
                    //add UserActivityStatus by xiongying 20160628
                    ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId());
                    dto.setUserActivityStatus(getActivityStatus(roster).getCode());
				}else {
					dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
				}
                fixupVideoInfo(dto);
                return dto;
                //全部查速度太慢，先把查出的部分排序 by xiongying20161208
             // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
            })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());

            return activityDtos;
        }
    }

	private void processOfficalActivitySceneToken(Long userId, SceneTokenDTO sceneTokenDTO, QueryOrganizationTopicCommand cmd) {
		Long organizationId = null;
		Long communityId = null;
	    SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());
        //检查游客是否能继续访问此场景 by sfyan 20161009
        userService.checkUserScene(sceneType);
	    switch(sceneType) {
	    case DEFAULT:
	    case PARK_TOURIST:
	        communityId = sceneTokenDTO.getEntityId();
			List<OrganizationCommunityDTO> list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
			if (list != null && list.size() > 0) {
				organizationId = list.get(0).getOrganizationId();
			}
	        break;
	    case FAMILY:
	        FamilyDTO family = familyProvider.getFamilyById(sceneTokenDTO.getEntityId());
	        if(family != null) {
	            communityId = family.getCommunityId();
	            list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
				if (list != null && list.size() > 0) {
					organizationId = list.get(0).getOrganizationId();
				}
	        } else {
	            if(LOGGER.isWarnEnabled()) {
	                LOGGER.warn("Family not found, sceneToken=" + sceneTokenDTO);
	            }
	        }
	        break;
        case ENTERPRISE: 
        case ENTERPRISE_NOAUTH: 
            // 对于普通公司，也需要取到其对应的管理公司，以便拿到管理公司所发的公告 by lqs 20160730
            OrganizationDTO org = organizationService.getOrganizationById(sceneTokenDTO.getEntityId());
            if(org != null) {
                communityId = org.getCommunityId();
                if(communityId == null) {
                    LOGGER.error("No community found for organization, organizationId={}, cmd={}, sceneToken={}", 
                        sceneTokenDTO.getEntityId(), cmd, sceneTokenDTO);
                } else {
                    list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
                    if (list != null && list.size() > 0) {
                        organizationId = list.get(0).getOrganizationId();
                    }
                }
            } else {
                LOGGER.error("Organization not found, organizationId={}, cmd={}, sceneToken={}", sceneTokenDTO.getEntityId(), cmd, sceneTokenDTO);
            }
            break;
        case PM_ADMIN:
        	organizationId = sceneTokenDTO.getEntityId();
        	org = organizationService.getOrganizationById(sceneTokenDTO.getEntityId());
            if(org != null) {
                communityId = org.getCommunityId();
                if(communityId == null) {
                    LOGGER.error("No community found for organization, organizationId={}, cmd={}, sceneToken={}", 
                        sceneTokenDTO.getEntityId(), cmd, sceneTokenDTO);
                } 
            }
            break;
	    default:
	        LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDTO);
	        break;
	    }
	    
	    cmd.setOrganizationId(organizationId);
	    // 补充小区/园区ID，方便后面构建查询条件：既要查本园区的官方活动，又要查对应的管理公司发给所有园区的官方活动 by lqs 20160730
	    cmd.setCommunityId(communityId);
	}

	@Override
	public ListOfficialActivityByNamespaceResponse listOfficialActivityByNamespace(
			ListOfficialActivityByNamespaceCommand cmd) {
		if (cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters, cmd="+cmd);
		}
		ListPostCommandResponse postResponse = forumService.listOfficialActivityByNamespace(cmd);
		List<PostDTO> posts = postResponse.getPosts();
		final List<ActivityDTO> activities = new ArrayList<>();
		if (posts != null && posts.size() > 0) {
			posts.forEach(p->{
				//吐槽：这里ActivityPostCommand和ActivityDTO中相同的字段，名字竟然不一样，如postUri和postUrl
				ActivityDTO activity = (ActivityDTO) StringHelper.fromJsonString(p.getEmbeddedJson().replace("posterUri", "posterUrl"), ActivityDTO.class);
				activity.setFavoriteFlag(p.getFavoriteFlag());
				activities.add(activity);
			});
		}
		
		ListOfficialActivityByNamespaceResponse reponse = new ListOfficialActivityByNamespaceResponse(postResponse.getNextPageAnchor(), activities);
		return reponse;
	}
	
	@Override
	public UserVideoPermissionDTO requestVideoPermission(RequestVideoPermissionCommand cmd) {
        User user = UserContext.current().getUser();
        UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.YZB_VIDEO_PERMISION);
        if(null != profile) {
            profile.setItemValue(cmd.toString());
            userActivityProvider.updateUserProfile(profile);
        } else {
            UserProfile p2 = new UserProfile();
            p2.setItemName(UserProfileContstant.YZB_VIDEO_PERMISION);
            p2.setItemKind((byte)0);
            p2.setItemValue(cmd.toString());
            p2.setOwnerId(user.getId());
            userActivityProvider.addUserProfile(p2);
        }
        
        return GetVideoPermisionInfo(new GetVideoPermissionInfoCommand());
	}
	
	@Override
	public UserVideoPermissionDTO GetVideoPermisionInfo(GetVideoPermissionInfoCommand cmd) {
	    UserVideoPermissionDTO dto = new UserVideoPermissionDTO();
	    User user = UserContext.current().getUser();
	    UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.YZB_VIDEO_PERMISION);
        if(profile == null || null == profile.getItemValue()) {
            return dto;
        }
        
        RequestVideoPermissionCommand req = (RequestVideoPermissionCommand)StringHelper.fromJsonString(profile.getItemValue(), RequestVideoPermissionCommand.class);
        
        dto.setVideoToken(req.getVideoToken());
        dto.setSessionId(req.getSessionId());
        
        return dto;
	}
	
	//live from normal user
	private ActivityVideoDTO setUserActivityVideo(SetActivityVideoInfoCommand cmd) {
	    ActivityVideo video = new ActivityVideo();
        //app sdk
        video.setIntegralTag1(0l);
        video.setVideoSid(cmd.getVid());
        
        YzbDevice oldDev = yzbDeviceProvider.findYzbDeviceByActivityId(cmd.getActivityId());
        if(oldDev != null) {
            oldDev.setState(VideoState.UN_READY.getCode());
            yzbDeviceProvider.updateYzbDevice(oldDev);
            
            yzbVideoService.setContinue(oldDev.getDeviceId(), 0); 
        }
        
        User user = UserContext.current().getUser();
        
        if(video.getVideoSid() == null) {
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_VIDEO_PARAM_ERROR, "video client params error");
        }
        
        ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(cmd.getActivityId());
        if(oldVideo != null) {
            //new activity, delete the old one
            oldVideo.setVideoState(VideoState.INVALID.getCode());
            activityVideoProvider.updateActivityVideo(oldVideo);
        }
        
        video.setCreatorUid(user.getId());
        video.setManufacturerType(VideoManufacturerType.YZB.toString());
        video.setRoomType(ActivityVideoRoomType.YZB.toString());
        video.setIntegralTag1(0l);

        VideoState videoState = VideoState.fromCode(cmd.getState());

        if (videoState != null) {
            video.setVideoState(videoState.getCode());
        }

        // 直播开始，设置开始时间为当前时间
        if (videoState == VideoState.LIVE) {
            video.setStartTime(System.currentTimeMillis());
        } else if (videoState == VideoState.RECORDING && oldVideo != null) {
            // 直播结束，设置这条记录的开始时间为直播开始时间
            video.setStartTime(oldVideo.getStartTime());
        }

        video.setOwnerType("activity");
        video.setOwnerId(cmd.getActivityId());
        activityVideoProvider.createActivityVideo(video);
        ActivityVideoDTO dto = ConvertHelper.convert(video, ActivityVideoDTO.class);
        dto.setVideoUrl("yzb://" + video.getVideoSid());
        dto.setRmtp("");
        
        return dto;
	}
	
	@Override
	public ActivityVideoDTO setActivityVideo(SetActivityVideoInfoCommand cmd) {	   
	    if(cmd.getRoomId() == null) {
	        return setUserActivityVideo(cmd);
	    } else {
	        //live from device
	        ActivityVideo video = new ActivityVideo();
	        String rmtp = "";
	        boolean setContinue = true;
	        
            //default, use device
	        video.setRoomId(cmd.getRoomId());
	        video.setIntegralTag1(1l);
	        video.setVideoState(VideoState.UN_READY.getCode());
	        video.setVideoSid("");
            video.setStartTime(System.currentTimeMillis());
	        
	        YzbDevice device = null;
	        YzbDevice oldDev = yzbDeviceProvider.findYzbDeviceByActivityId(cmd.getActivityId());
	        if(oldDev != null && oldDev.getRoomId().equals(cmd.getRoomId())) {
	            //found old device
	            device = oldDev;
	            oldDev = null;
	        } else {
	            device = yzbDeviceProvider.findYzbDeviceById(cmd.getRoomId());    
	        }
            if(oldDev != null) {
                //set old device to no ready
                oldDev.setState(VideoState.UN_READY.getCode());
                yzbDeviceProvider.updateYzbDevice(oldDev);
                yzbVideoService.setContinue(oldDev.getDeviceId(), 0);
            }
	        
	        if(device == null) {
	            //create new device
	            device = new YzbDevice();
	            device.setDeviceId(cmd.getRoomId());
	            if(cmd.getNamespaceId() == null) {
	                device.setNamespaceId(UserContext.getCurrentNamespaceId());
	            }
	            device.setState(VideoState.UN_READY.getCode());
	            device.setStatus((byte)1); //valid
               device.setRelativeType("activity");
               device.setRelativeId(0l);
               device.setLastVid("");
               device.setRoomId(cmd.getRoomId());
	            yzbDeviceProvider.createYzbDevice(device);
	            
	            setContinue = false;
	        } else {
	            if(!cmd.getActivityId().equals(device.getRelativeId())
	                    && device.getState().equals(VideoState.LIVE.getCode())) {
	                LOGGER.warn("new living but device is already living, cmd=" + cmd + " device=" + device.getRelativeId());
	                //Close old living right now
	                ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(device.getRelativeId());
	                if(oldVideo != null 
	                        && oldVideo.getVideoSid() != null 
	                        && !oldVideo.getVideoState().equals(VideoState.RECORDING.getCode())
	                        ) {
	                    oldVideo.setVideoState(VideoState.RECORDING.getCode());
	                    oldVideo.setEndTime(System.currentTimeMillis());
	                    activityVideoProvider.updateActivityVideo(oldVideo);
	                }
	            }
	            
	            if(device.getRelativeId() == null 
	                    || !cmd.getActivityId().equals(device.getRelativeId())
	                    || !device.getState().equals(VideoState.LIVE.getCode())) {
	                setContinue = false;
	            }
	            
	        }
	        
            ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(cmd.getActivityId());
            if(oldVideo != null) {
                video.setVideoSid(oldVideo.getVideoSid());
                video.setVideoState(oldVideo.getVideoState());
                video.setStartTime(oldVideo.getStartTime());
                
                //new activity, delete the old one
                oldVideo.setVideoState(VideoState.INVALID.getCode());
                activityVideoProvider.updateActivityVideo(oldVideo);
            }
            
            if(setContinue) {
                //continue use old vid
                yzbVideoService.setContinue(cmd.getRoomId(), 1);
            } else {
                //start new vid
               yzbVideoService.setContinue(cmd.getRoomId(), 0);
            }
            
            if(cmd.getActivityId().equals(device.getRelativeId())) {
                video.setVideoSid(device.getLastVid());
            }
            
            device.setRelativeId(cmd.getActivityId());
            yzbDeviceProvider.updateYzbDevice(device); 
            
            User user = UserContext.current().getUser();
            video.setCreatorUid(user.getId());
            video.setManufacturerType(VideoManufacturerType.YZB.toString());
            video.setRoomType(ActivityVideoRoomType.YZB.toString());
            video.setOwnerType("activity");
            video.setOwnerId(cmd.getActivityId());
            activityVideoProvider.createActivityVideo(video);
            ActivityVideoDTO dto = ConvertHelper.convert(video, ActivityVideoDTO.class);
            dto.setVideoUrl("yzb://" + video.getVideoSid());
            dto.setRmtp(rmtp);
            
            return dto;
	    }
	}
	
   @Override
    public ActivityVideoDTO getActivityVideo(GetActivityVideoInfoCommand cmd) {
       ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(cmd.getActivityId());
       return ConvertHelper.convert(video, ActivityVideoDTO.class);
    }
   
   @Override
   public void createScheduleForActivity(Activity act) {
       if(act.getIsVideoSupport() == null || act.getIsVideoSupport() <= 0) {
           return;
       }
       
       String triggerName = YzbConstant.SCHEDULE_TARGET_NAME + System.currentTimeMillis();
       String jobName = triggerName;
       
       Long now = System.currentTimeMillis();
       Long endTime = act.getEndTimeMs();
       if(endTime == null) {
           endTime = act.getEndTime().getTime();
       }
       
       Map<String, Object> map = new HashMap<String, Object>();
       //String cronExpression = "0/5 * * * * ?";
       map.put("id", act.getId().toString());
       map.put("endTime", endTime.toString());
       map.put("now", now.toString());
       
       scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(endTime + 60*1000), ActivityVideoScheduleJob.class, map);
   }
   
   @Override
   public void onActivityFinished(Long activityId, Long endTime) {
       Activity activity = activityProvider.findActivityById(activityId);
       if(activity == null || !activity.getEndTimeMs().equals(endTime)) {
           LOGGER.warn("invalid activityId=" + activityId + " endTime=" + endTime);
           return;
       }
       
       ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(activityId);
       if(oldVideo != null) {
           if(oldVideo.getVideoState().equals(VideoState.LIVE.getCode())) {
               oldVideo.setVideoState(VideoState.RECORDING.getCode());
               oldVideo.setEndTime(System.currentTimeMillis());
               activityVideoProvider.updateActivityVideo(oldVideo);
           }
           
           YzbDevice dev = yzbDeviceProvider.findYzbDeviceByActivityId(activityId);
           if(dev != null && dev.getState().equals(VideoState.LIVE.getCode())) {
               dev.setState(VideoState.UN_READY.getCode());
               dev.setRelativeId(0l);
               yzbDeviceProvider.updateYzbDevice(dev);
           }
           
       }
   }
   
   @Override 
   public void onVideoDeviceChange(YzbVideoDeviceChangeCommand cmd) {
       LOGGER.info("video ondevicechange=" + cmd);
       if(cmd.getDevid() == null) {
           return;
       }
       
       YzbDevice device = yzbDeviceProvider.findYzbDeviceById(cmd.getDevid());
       if(device == null) {
       return;    
       }
       
       if(cmd.getOptcode().equals("livestart")) {
           ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(device.getRelativeId());
           LOGGER.info("video ondevicechange, video=" + video);
           if(video != null && video.getIntegralTag1().equals(1l) 
                   && !video.getVideoState().equals(VideoState.RECORDING.getCode())
                   && cmd.getVid() != null && !cmd.getVid().isEmpty()) {
               video.setVideoSid(cmd.getVid());
               video.setVideoState(VideoState.LIVE.getCode());
               activityVideoProvider.updateActivityVideo(video);

               device.setLastVid(cmd.getVid());
               device.setState(VideoState.LIVE.getCode());
               yzbDeviceProvider.updateYzbDevice(device);
               
           }
       } else if(cmd.getOptcode().equals("livestop")) {
           LOGGER.info("video livestop");
           device.setState(VideoState.UN_READY.getCode());
           yzbDeviceProvider.updateYzbDevice(device);
//           ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(device.getRelativeId());
//           if(video != null && video.getVideoState().equals(VideoState.LIVE.getCode())) {
//               video.setVideoState(VideoState.RECORDING.getCode());
//               activityVideoProvider.updateActivityVideo(video);
//           }
           
       }
      
   }
   
   @Override
   public VideoCapabilityResponse getVideoCapability(GetVideoCapabilityCommand cmd) {

       Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
   
       VideoCapabilityResponse obj = new VideoCapabilityResponse();
       if(cmd.getOfficialFlag() == null || cmd.getOfficialFlag().equals(OfficialFlag.NO.getCode())) {
           Long official = this.configurationProvider.getLongValue(namespaceId
                   , YzbConstant.VIDEO_NONE_OFFICIAL_SUPPORT, (long)VideoSupportType.NO_SUPPORT.getCode());
           obj.setVideoSupportType(official.byteValue());
       } else {
           Long official = this.configurationProvider.getLongValue(namespaceId
                   , YzbConstant.VIDEO_OFFICIAL_SUPPORT, (long)VideoSupportType.NO_SUPPORT.getCode());
           obj.setVideoSupportType(official.byteValue()); 
       }
       return obj;
   }
   
   private void fixupVideoInfo(ActivityDTO dto) {
       if(dto.getVideoUrl() != null) {
           return;
       }
       
       if(dto.getIsVideoSupport() == null) {
           dto.setIsVideoSupport((byte)0);
       }
       dto.setVideoState(VideoState.UN_READY.getCode());
       
       if(dto.getIsVideoSupport() != null && dto.getIsVideoSupport().byteValue() > 0) {
           ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(dto.getActivityId());
           if(video != null && video.getVideoSid() != null) {
               dto.setVideoUrl("yzb://" + video.getVideoSid());
               dto.setVideoState(video.getVideoState());
           }
       }
   }

	@Override
	public GetActivityDetailByIdResponse getActivityDetailById(GetActivityDetailByIdCommand cmd) {
		return forumService.getActivityDetailById(cmd);
	}

	@Override
	public ActivityWarningResponse setActivityWarning(SetActivityWarningCommand cmd) {
		if (cmd.getNamespaceId()==null || cmd.getDays() == null || cmd.getHours() == null || cmd.getHours().intValue() == 0) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cmd="+cmd);
		}
		WarningSetting warningSetting = findWarningSetting(cmd.getNamespaceId());
		if (warningSetting != null && warningSetting.getId() != null) {
			warningSetting.setTime((long) ((cmd.getDays()*24+cmd.getHours())*3600*1000));
			warningSetting.setUpdateTime(warningSetting.getCreateTime());
			warningSetting.setOperatorUid(warningSetting.getCreatorUid());
			
			warningSettingProvider.updateWarningSetting(warningSetting);
		}else {
			warningSetting = new WarningSetting();
			warningSetting.setNamespaceId(cmd.getNamespaceId());
			warningSetting.setTime((long) ((cmd.getDays()*24+cmd.getHours())*3600*1000));
			warningSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			warningSetting.setCreatorUid(UserContext.current().getUser().getId());
			warningSetting.setUpdateTime(warningSetting.getCreateTime());
			warningSetting.setOperatorUid(warningSetting.getCreatorUid());
			warningSetting.setType(EhActivities.class.getSimpleName());
			
			warningSettingProvider.createWarningSetting(warningSetting);
		}
		
		return ConvertHelper.convert(cmd, ActivityWarningResponse.class);
	}

	@Override
	public ActivityWarningResponse queryActivityWarning(GetActivityWarningCommand cmd) {
		if (cmd.getNamespaceId()==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cmd="+cmd);
		}
		
		WarningSetting warningSetting = findWarningSetting(cmd.getNamespaceId());
		
		if (warningSetting != null) {
			Integer days = (int) (warningSetting.getTime() / 1000 / 3600 / 24);
			Integer hours  = (int) (warningSetting.getTime() / 1000 / 3600 % 24);
			return new ActivityWarningResponse(warningSetting.getNamespaceId(), days, hours, warningSetting.getTime());
		}
		
		return new ActivityWarningResponse(cmd.getNamespaceId(), 0, 1, 3600*1000L);
	}
	
	private WarningSetting findWarningSetting(Integer namespaceId){
		WarningSetting warningSetting =  warningSettingProvider.findWarningSettingByNamespaceAndType(namespaceId, EhActivities.class.getSimpleName());
		if (warningSetting == null) {
			warningSetting = new WarningSetting();
			warningSetting.setNamespaceId(namespaceId);
			warningSetting.setTime(3600*1000L);
		}
		return warningSetting;
	}
   
	/**
	 * 活动开始前的提醒，采用轮循+定时两种方式执行定时任务
	 * 轮循时按域空间设置的活动提前时间取出相应的活动（只取当前时间+n~当前时间+n+1之间的活动），再把这些活动设置成定时的任务
	 **/ 
    @Scheduled(cron="0 0 * * * ?")
    @Override
	public void activityWarningSchedule() {
    	//使用tryEnter方法可以防止分布式部署时重复执行
    	coordinationProvider.getNamedLock(CoordinationLocks.WARNING_ACTIVITY_SCHEDULE.getCode()).tryEnter(()->{
        	
        	final Date now = DateUtils.getCurrentHour();
        	List<NamespaceInfoDTO> namespaces = namespacesProvider.listNamespace();
        	namespaces.add(new NamespaceInfoDTO(0,"zuolin",""));
        	
        	//遍历每个域空间
        	namespaces.forEach(n->{
        		WarningSetting warningSetting = findWarningSetting(n.getId());
        		Timestamp queryStartTime = new Timestamp(now.getTime()+warningSetting.getTime());
        		Timestamp queryEndTime = new Timestamp(now.getTime()+warningSetting.getTime()+3600*1000);
        		
        		// 对于这个域空间时间范围内的活动，再单独设置定时任务
        		List<Activity> activities = activityProvider.listActivitiesForWarning(n.getId(), queryStartTime, queryEndTime);
        		activities.forEach(a->{
        			if (a.getSignupAttendeeCount() != null && a.getSignupAttendeeCount() > 0 && a.getStartTime().getTime() - warningSetting.getTime() >= new Date().getTime()) {
        				final Job job1 = new Job(
        						WarnActivityBeginningAction.class.getName(),
        						new Object[] { String.valueOf(a.getId()) });
        				
//        				jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
//        						new Date().getTime()+10000);
        				jesqueClientFactory.getClientPool().delayedEnqueue(WarnActivityBeginningAction.QUEUE_NAME, job1,
        						a.getStartTime().getTime() - warningSetting.getTime());
        				LOGGER.debug("设置了一个活动提醒："+a.getId());
					}
        		});
        	});
    	});
	}

	@Override
	public List<ActivityCategoryDTO> listActivityEntryCategories(
			ListActivityEntryCategoriesCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<ActivityCategories> entityResultList = this.activityProvider.listActivityEntryCategories(namespaceId,
                cmd.getOwnerType(), cmd.getOwnerId(), null, CategoryAdminStatus.ACTIVE);
        return entityResultList.stream().map(r -> {
        	ActivityCategoryDTO dto = ConvertHelper.convert(r, ActivityCategoryDTO.class);
            return dto;
        }).collect(Collectors.toList());
	}

    @Override
    public ListActivityPromotionEntitiesBySceneReponse listActivityPromotionEntitiesByScene(ListActivityPromotionEntitiesBySceneCommand cmd) {

        ListNearbyActivitiesBySceneCommand listCmd = new ListNearbyActivitiesBySceneCommand();
        listCmd.setCategoryId(cmd.getCategoryId());
        listCmd.setSceneToken(cmd.getSceneToken());
        listCmd.setPageSize(cmd.getPageSize());
        listCmd.setPageAnchor(cmd.getPageAnchor());
        // 只要查询预告中与进行中的活动
        listCmd.setActivityStatusList(Arrays.asList(NOTSTART.getCode(), UNDERWAY.getCode()));

        ListActivitiesReponse activityReponse;
        if (OfficialFlag.fromCode(cmd.getPublishPrivilege()) == OfficialFlag.YES) {
            // 官方活动
            activityReponse = this.listOfficialActivitiesByScene(listCmd);
        } else {
            // 非官方活动
            activityReponse = this.listNearbyActivitiesByScene(listCmd);
        }
        ListActivityPromotionEntitiesBySceneReponse promotionReponse = new ListActivityPromotionEntitiesBySceneReponse();

        if (activityReponse != null && activityReponse.getActivities() != null) {
            List<ModulePromotionEntityDTO> entities = activityReponse.getActivities().stream()
                    .map(this::toModulePromotionEntityDTO).collect(Collectors.toList());
            promotionReponse.setEntities(entities);
        }
        return promotionReponse;
    }

    private ModulePromotionEntityDTO toModulePromotionEntityDTO(ActivityDTO activityDTO) {
        ModulePromotionEntityDTO dto = new ModulePromotionEntityDTO();
        dto.setId(activityDTO.getActivityId());
        dto.setDescription(activityDTO.getDescription());
        dto.setPosterUrl(activityDTO.getPosterUrl());
        dto.setSubject(activityDTO.getTag() + " | " + activityDTO.getSubject());

        Map<String, Long> metadata = new HashMap<>();
        metadata.put("forumId", activityDTO.getForumId());
        metadata.put("topicId", activityDTO.getPostId());

        dto.setMetadata(StringHelper.toJsonString(metadata));

        ModulePromotionInfoDTO infoDTO = new ModulePromotionInfoDTO();
        LocalDateTime startTime = LocalDateTime.parse(activityDTO.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"));
        infoDTO.setInfoType(ModulePromotionInfoType.TEXT.getCode());
        infoDTO.setContent(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dto.setInfoList(Collections.singletonList(infoDTO));
        return dto;
    }

    public void setActivityAchievement(SetActivityAchievementCommand cmd) {

        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }

        activity.setAchievement(cmd.getAchievement());
        activity.setAchievementType(cmd.getAchievementType());
        activity.setAchievementRichtextUrl(cmd.getAchievementRichtextUrl());
        activityProvider.updateActivity(activity);
    }

    @Override
    public GetActivityAchievementResponse getActivityAchievement(GetActivityAchievementCommand cmd) {
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }

        String achievement = activity.getAchievement();
        String achievementType = activity.getAchievementType();

        GetActivityAchievementResponse response = new GetActivityAchievementResponse();
        response.setAchievement(achievement);
        response.setAchievementType(achievementType);
        return response;
    }

    @Override
    public void createActivityAttachment(CreateActivityAttachmentCommand cmd) {
        ActivityAttachment attachment = ConvertHelper.convert(cmd, ActivityAttachment.class);
        attachment.setCreatorUid(UserContext.current().getUser().getId());
        ContentServerResource resource = contentServerService.findResourceByUri(cmd.getContentUri());
        Integer size = resource.getResourceSize();
        attachment.setFileSize(size);
        activityProvider.createActivityAttachment(attachment);
    }

    @Override
    public void deleteActivityAttachment(DeleteActivityAttachmentCommand cmd) {
        activityProvider.deleteActivityAttachment(cmd.getAttachmentId());
    }

    @Override
    public ListActivityAttachmentsResponse listActivityAttachments(ListActivityAttachmentsCommand cmd) {
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }

        ListActivityAttachmentsResponse response = new ListActivityAttachmentsResponse();
        List<ActivityAttachment> attachments = activityProvider.listActivityAttachments(locator, cmd.getPageSize() + 1, cmd.getActivityId());
        if(attachments != null && attachments.size() > 0) {
            if(attachments.size() > cmd.getPageSize()) {
                attachments.remove(attachments.size() - 1);
                response.setNextPageAnchor(attachments.get(attachments.size() - 1).getId());
            }

            List<ActivityAttachmentDTO> dtos = attachments.stream().map((r) -> {
                ActivityAttachmentDTO dto = ConvertHelper.convert(r, ActivityAttachmentDTO.class);
                String contentUrl = contentServerService.parserUri(dto.getContentUri(), EntityType.ACTIVITY.getCode(), dto.getActivityId());
                User creator = userProvider.findUserById(dto.getCreatorUid());
                if(creator != null) {
                    dto.setCreatorName(creator.getNickName());
                }
                dto.setContentUrl(contentUrl);
                return dto;
            }).collect(Collectors.toList());
            response.setAttachments(dtos);
        }

        return response;
    }

    @Override
    public void downloadActivityAttachment(DownloadActivityAttachmentCommand cmd) {

    	if(cmd.getAttachmentId() == null || cmd.getActivityId() == null) {
    		return ;
    	}
    	
        ActivityAttachment attachment = activityProvider.findByActivityAttachmentId(cmd.getAttachmentId());
        if(attachment == null) {
            LOGGER.error("handle activity attachment error ,the activity attachment does not exsit.cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ATTACHMENT_ID, "invalid activity attachment id " + cmd.getAttachmentId());
        }

        attachment.setDownloadCount(attachment.getDownloadCount() + 1);
        activityProvider.updateActivityAttachment(attachment);
    }

    @Override
    public void createActivityGoods(CreateActivityGoodsCommand cmd) {
        ActivityGoods goods = ConvertHelper.convert(cmd, ActivityGoods.class);
        goods.setCreatorUid(UserContext.current().getUser().getId());
        activityProvider.createActivityGoods(goods);
    }

    @Override
    public void updateActivityGoods(UpdateActivityGoodsCommand cmd) {
        ActivityGoods goods = activityProvider.findActivityGoodsById(cmd.getId());
        if(goods == null) {
            LOGGER.error("handle activity goods error ,the activity goods does not exsit.cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_GOODS_ID, "invalid activity goods id " + cmd.getId());
        }

        goods.setName(cmd.getName());
        goods.setPrice(cmd.getPrice());
        goods.setQuantity(cmd.getQuantity());
        goods.setTotalPrice(cmd.getTotalPrice());
        goods.setHandlers(cmd.getHandlers());

        activityProvider.updateActivityGoods(goods);
    }

    @Override
    public void deleteActivityGoods(DeleteActivityGoodsCommand cmd) {
        activityProvider.deleteActivityGoods(cmd.getGoodId());
    }

    @Override
    public ListActivityGoodsResponse listActivityGoods(ListActivityGoodsCommand cmd) {
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }

        ListActivityGoodsResponse response = new ListActivityGoodsResponse();
        List<ActivityGoods> goods = activityProvider.listActivityGoods(locator, cmd.getPageSize() + 1, cmd.getActivityId());
        if(goods != null && goods.size() > 0) {
            if(goods.size() > cmd.getPageSize()) {
                goods.remove(goods.size() - 1);
                response.setNextPageAnchor(goods.get(goods.size() - 1).getId());
            }

            List<ActivityGoodsDTO> dtos = goods.stream().map((r) -> {
                ActivityGoodsDTO dto = ConvertHelper.convert(r, ActivityGoodsDTO.class);
                return dto;
            }).collect(Collectors.toList());
            response.setGoods(dtos);
        }

        return response;
    }

    @Override
    public ActivityGoodsDTO getActivityGoods(GetActivityGoodsCommand cmd) {
        ActivityGoods goods = activityProvider.findActivityGoodsById(cmd.getGoodId());
        if(goods == null) {
            return null;
        } else {
            ActivityGoodsDTO dto = ConvertHelper.convert(goods, ActivityGoodsDTO.class);
            return dto;
        }
    }

	@Override
	public void videoCallback() {
		LOGGER.info("video callbacll................");
	}

	@Override
	public ListActivityCategoryReponse listActivityCategory(ListActivityCategoryCommand cmd) {
		if (cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespaceId cannot be null");
		}
		
		List<ActivityCategories> ActivityCategoryList = activityProvider.listActivityCategory(cmd.getNamespaceId(), cmd.getCategoryId());
		
		List<ActivityCategoryDTO> resultList = ActivityCategoryList.stream().map(r->toActivityCategoryDTO(r)).collect(Collectors.toList());
		
		return new ListActivityCategoryReponse(resultList);
	}

	private ActivityCategoryDTO toActivityCategoryDTO(ActivityCategories activityCategory){
		ActivityCategoryDTO categoryDTO = ConvertHelper.convert(activityCategory, ActivityCategoryDTO.class);
		categoryDTO.setIconUrl(parserUri(categoryDTO.getIconUri(), EhActivityCategories.class.getSimpleName(), categoryDTO.getId()));
		categoryDTO.setSelectedIconUrl(parserUri(categoryDTO.getSelectedIconUri(), EhActivityCategories.class.getSimpleName(), categoryDTO.getId()));
		if (categoryDTO.getSelectedIconUrl() == null) {
			categoryDTO.setSelectedIconUrl(categoryDTO.getIconUrl());
		}
		return categoryDTO;
	}
	
	private String parserUri(String uri,String ownerType, long ownerId){
		try {
			if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
				return contentServerService.parserUri(uri,ownerType,ownerId);
		} catch (Exception e) {
			LOGGER.error("Parser uri is error." + e.getMessage());
		}
		return null;
	}
}
