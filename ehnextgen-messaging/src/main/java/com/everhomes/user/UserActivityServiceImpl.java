package com.everhomes.user;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.activity.*;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.business.Business;
import com.everhomes.business.BusinessProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.flow.FlowService;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.point.PointService;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.promotion.BizHttpRestCallProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.business.BusinessServiceErrorCode;
import com.everhomes.rest.common.ActivityListStyleFlag;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.flow.GetFlowCaseCountResponse;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.openapi.GetUserServiceAddressCommand;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.ui.user.UserProfileDTO;
import com.everhomes.rest.user.*;
import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.rest.version.VersionUrlResponse;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.statistics.terminal.AppVersion;
import com.everhomes.statistics.terminal.StatTerminalProvider;
import com.everhomes.util.*;
import com.everhomes.version.VersionService;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserActivityServiceImpl implements UserActivityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserActivityServiceImpl.class);
    

    private static final String COMMUNITY_COUNT_ADJUST = "community.count.adjust";

    private static final String USER_CONTACT_COUNT = "contact.count";

    private static final String[] prefix = { "+86", "(+86)", "+", "86" };
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Autowired
    private FamilyProvider familyProvider;

    @Autowired
    private ForumService forumService;
    
    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private BusinessProvider businessProvider;
    
    @Autowired
    private RegionProvider regionProvider;
    
    @Autowired
    private ActivityProivider activityProivider;
    
    @Autowired
    BizHttpRestCallProvider bizHttpRestCallProvider;
    
    @Autowired
    private ActivityVideoProvider activityVideoProvider;

    @Autowired
    private NamespacesService namespacesService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private VersionService versionService;

    @Autowired
    private StatTerminalProvider statTerminalProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    protected ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private YellowPageProvider yellowPageProvider;

    @Autowired
    private PointService pointService;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Autowired
    private TaskService taskService;

    @Override
    public CommunityStatusResponse listCurrentCommunityStatus() {
        User user = UserContext.current().getUser();
        if (user.getCommunityId() == null || user.getCommunityId() == 0L) {
            LOGGER.error("cannot find community for user");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_PARAMS, "invalid community");
        }
        int countUser = familyProvider.countUserByCommunityId(user.getCommunityId());
        int familyCount = familyProvider.countFamiliesByCommunityId(user.getCommunityId());

        // adjust
        int adjust_number = configurationProvider.getIntValue(COMMUNITY_COUNT_ADJUST, 8);
        int userCount = adjustCount(familyCount, countUser, adjust_number);
        int showContactsNumber = configurationProvider.getIntValue(USER_CONTACT_COUNT, 20);

        List<UserContact> cresult = userActivityProvider.listContactByUid(user.getId(), new ListingLocator(),
                showContactsNumber);
        List<Contact> contacts = cresult.stream().map(item -> ConvertHelper.convert(item, Contact.class))
                .collect(Collectors.toList());
        CommunityStatusResponse rsp = new CommunityStatusResponse();
        rsp.setUserCount(userCount);
        rsp.setContacts(contacts);
        return rsp;

    }

    private int adjustCount(int countApt, int countUser, double point) {
        double p = point;
        double d = countUser + countApt * (p / 100);
        return (int) d;
    }

    @Override
    public void updateLocation(SyncLocationCommand cmd) {
        User user = UserContext.current().getUser();
        String geohash = GeoHashUtils.encode(cmd.getLatitude().doubleValue(), cmd.getLongitude().doubleValue());
        UserLocation location = ConvertHelper.convert(cmd, UserLocation.class);
        location.setUid(user.getId());
        location.setCreateTime(getCreateTime());
        location.setGeohash(geohash);
        userActivityProvider.addLocation(location, user.getId());
    }

    @Override
    public void updateActivity(SyncActivityCommand cmd) {
        VersionRealmType realmType = VersionRealmType.fromCode(UserContext.current().getVersionRealm());
        if (realmType == null) {
            LOGGER.warn("invalid version realm type of [{}], ignored.", UserContext.current().getVersionRealm());
            return;
        }

        OSType osType = OSType.fromString(cmd.getOsType());
        if (osType == OSType.Unknown) {
            if (realmType.getCode().toLowerCase().startsWith("ios")) {
                osType = OSType.IOS;
            } else if (realmType.getCode().toLowerCase().startsWith("android")) {
                osType = OSType.Android;
            }
        }
        String type = osType.name().toLowerCase();

        String versionName = cmd.getAppVersionName();
        if (StringUtils.isEmpty(versionName) || versionName.split("\\.").length < 3) {
            LOGGER.warn("invalid version name of [{}], ignored.", versionName);
            return;
        }

        String[] tmp = new String[3];
        String[] arr = versionName.split("\\.");
        System.arraycopy(arr, 0, tmp, 0, 3);

        versionName = StringUtils.join(tmp, ".");
        Version version = null;
        try {
            version = Version.fromVersionString(versionName);
        } catch (Exception e) {
            LOGGER.warn("invalid version name of [{}], ignored.", versionName);
            return;
        }

        AppVersion appVersion = statTerminalProvider.findAppVersion(UserContext.getCurrentNamespaceId(), versionName, type);
        if(null == appVersion) {
            appVersion = new AppVersion();
            appVersion.setName(versionName);
            appVersion.setType(type);
            appVersion.setNamespaceId(UserContext.getCurrentNamespaceId());
            appVersion.setRealm(realmType.getCode());
            appVersion.setDefaultOrder((int) version.getEncodedValue());
            statTerminalProvider.createAppVersion(appVersion);
        }
    }

    private static Timestamp getCreateTime() {
        return new Timestamp(DateHelper.currentGMTTime().getTime());
    }

    @Override
    public void updateUserBehavoir(SyncBehaviorCommand cmd) {
        User user = UserContext.current().getUser();
        UserBehavior userBehavior = new UserBehavior();
        BeanUtils.copyProperties(cmd, userBehavior, "contentType");
        userBehavior.setCreateTime(getCreateTime());
        userBehavior.setUid(user.getId());
        userBehavior.setContentType((byte) cmd.getContentType().intValue());
        userActivityProvider.addBehavior(userBehavior, user.getId());
    }

    @Override
    public void syncInstalledApps(SyncInsAppsCommand installApps) {
        if (CollectionUtils.isEmpty(installApps.getAppInfos())) {
            return;
        }
        User user = UserContext.current().getUser();
        List<UserInstalledApp> apps = new ArrayList<UserInstalledApp>(installApps.getAppInfos().size());
        installApps.getAppInfos().forEach(app -> {
            UserInstalledApp userApp = ConvertHelper.convert(app, UserInstalledApp.class);
            userApp.setCreateTime(getCreateTime());
            userApp.setUid(user.getId());
            apps.add(userApp);
        });
        userActivityProvider.addInstalledApp(apps, user.getId());
    }

    @Override
    public void updateContacts(SyncUserContactCommand cmd) {
        if (CollectionUtils.isEmpty(cmd.getContacts())) {
            LOGGER.warn("contacts is empty");
            return;
        }
        User user = UserContext.current().getUser();
        List<UserContact> contacts = new ArrayList<UserContact>();
        cmd.getContacts().forEach(contact -> {
            if (StringUtils.isEmpty(contact.getContactPhone())) {
                return;
            }

            String phone = covnertPhone(contact.getContactPhone());
            contact.setContactPhone(phone);
            UserContact c = ConvertHelper.convert(contact, UserContact.class);
            c.setUid(user.getId());
            c.setCreateTime(getCreateTime());
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(user.getNamespaceId(), 
                contact.getContactPhone());
            if (userIdentifier != null)
                c.setContactId(userIdentifier.getOwnerUid());
            contacts.add(c);
        });
        List<UserContact> needUpdate = new ArrayList<UserContact>();
        userActivityProvider.listContacts(user.getId()).forEach(
                item -> {
                    UserContact userItem = ConvertHelper.convert(item, UserContact.class);
                    int index = contacts.indexOf(userItem);
                    if (index > 0) {
                        UserContact contact = contacts.get(index);
                        if (contact.getContactId() == null || contact.getContactId() == 0) {
                            contacts.remove(userItem);
                        } else if (userItem.getContactId() != null && userItem.getContactId() != 0
                                && userItem.getContactId().longValue() == contact.getContactId().longValue()) {
                            contacts.remove(userItem);
                        } else {
                            needUpdate.add(contact);
                        }

                    }
                });
        if (CollectionUtils.isNotEmpty(contacts))
            userActivityProvider.addContacts(contacts, user.getId());
        if (CollectionUtils.isNotEmpty(needUpdate))
            userActivityProvider.updateContact(needUpdate, user.getId());
    }

    private String covnertPhone(String phone) {
        phone = phone.replaceAll(" ", "").replaceAll("-", "");
        for (String s : prefix) {
            if (phone.startsWith(s)) {
                phone = phone.replace(s, "");
            }
        }
        return phone;
    }

    @Override
    public InvitationCommandResponse listInvitedUsers(Long anchor) {
        User user = UserContext.current().getUser();
        InvitationCommandResponse response = new InvitationCommandResponse();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(anchor);
        int pageSize = this.configurationProvider.getIntValue("pagination.page.size",
                AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserInvitation> invitationDto = userActivityProvider.listInvitationByUid(user.getId(), locator,
                pageSize + 1);
        Map<Long, InvitationDTO> mutilMap = new HashMap<Long, InvitationDTO>();
        invitationDto.forEach(item -> {
            mutilMap.put(item.getId(), ConvertHelper.convert(item, InvitationDTO.class));
        });
        Map<String, InvitationDTO> newCache = new HashMap<>();
        List<UserInvitationRoster> rosters = userActivityProvider.listInvitationRoster(mutilMap.keySet());
        rosters.forEach(roster -> {
            InvitationDTO val = mutilMap.get(roster.getInviteId());
            if (val == null)
                return;
            if (StringUtils.isNotEmpty(val.getIdentifier())) {
                InvitationDTO newBean = ConvertHelper.convert(val, InvitationDTO.class);
                newBean.setIdentifier(roster.getContact());
                newCache.put(roster.getContact(), newBean);
            } else {
                val.setIdentifier(roster.getContact());
                newCache.put(roster.getContact(), val);
            }
        });
        Map<Long, String> cacheUsers = new HashMap<Long, String>();
        Set<String> phones = newCache.keySet();

        List<UserIdentifier> userIndeIdentifiers = userActivityProvider.listUserIdentifiers(new ArrayList<String>(
                phones));
        userIndeIdentifiers.forEach(identifier -> {
            cacheUsers.put(identifier.getOwnerUid(), identifier.getIdentifierToken());
        });

        List<User> users = userActivityProvider.listUsers(new ArrayList<Long>(cacheUsers.keySet()));
        users.forEach(item -> {
            newCache.get(cacheUsers.get(item.getId())).setName(item.getAccountName());
        });
        ArrayList<InvitationDTO> values = new ArrayList<InvitationDTO>(newCache.values());
        if (invitationDto.size() > pageSize) {
            response.setRecipientList(values.subList(0, values.size() - 1));
            response.setNextPageAnchor(locator.getAnchor());
        } else {
            response.setRecipientList(values);
            response.setNextPageAnchor(null);
        }
        return response;
    }

    @Override
    public Tuple<Long, List<ContactDTO>> listUserContacts(Long pageAchor) {
        User user = UserContext.current().getUser();
        int pageSize = this.configurationProvider.getIntValue("pagination.page.size",
                AppConstants.PAGINATION_DEFAULT_SIZE);
        List<ContactDTO> contacts = new ArrayList<ContactDTO>();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(pageAchor);
        List<UserContact> dto = userActivityProvider.listContactByUid(user.getId(), locator, pageSize + 1);
        dto.forEach(c -> {
            ContactDTO cd = ConvertHelper.convert(c, ContactDTO.class);
            cd.setCreateTime((c.getCreateTime().getTime()));
            contacts.add(cd);
        });
        Map<Long, ContactDTO> cache = new HashMap<Long, ContactDTO>();
        contacts.forEach(contact -> {
            cache.put(contact.getContactId(), contact);
        });
        contacts.clear();
        List<User> users = userActivityProvider.listUsers(new ArrayList<Long>(cache.keySet()));
        users.forEach(euser -> {
            User contactUser = ConvertHelper.convert(euser, User.class);
            ContactDTO contact = cache.get(contactUser.getId());
            contact.setEhUsername(contactUser.getNickName());
            String avatar = "";
            try {
                avatar = contentServerService.parserUri(contactUser.getAvatar(), EntityType.USER.getCode(),
                        user.getId());
            } catch (Exception e) {

            }
            contact.setContactAvatar(avatar);
            if (contactUser.getCommunityId() != null) {
                Community c = communityProvider.findCommunityById(contactUser.getCommunityId());
                if (c != null)
                    contact.setCommunityName(c.getName());
            }
        });
        List<ContactDTO> values = new ArrayList<ContactDTO>(cache.values());
        if (cache.size() < pageSize) {
            locator.setAnchor(null);
        } else {
            values = values.subList(0, values.size() - 1);
        }
        return new Tuple<Long, List<ContactDTO>>(locator.getAnchor(), values);
    }

    @Override
    public List<UserContact> listUserRetainIdentifiers(String identifer) {
        // TODO: think about namespace id
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(identifer);
        if (userIdentifier == null) {
            LOGGER.error("the user does not claimed");
            return null;
        }
        return userActivityProvider.listRetainUserContactByUid(userIdentifier.getOwnerUid());
    }

    @Override
    public void addFeedback(FeedbackCommand cmd) {


        FeedbackHandler handler = getFeedBackHandler(cmd.getTargetType());

        dbProvider.execute(status -> {

            if(handler != null){
                //业务实现
                handler.beforeAddFeedback(cmd);
            }

            Feedback feedback = addFeedbackCommon(cmd);

            if(handler != null){
                //业务实现
                handler.afterAddFeedback(feedback);
            }

            return null;
        });

    }

    private Feedback addFeedbackCommon(FeedbackCommand cmd){
        User user = UserContext.current().getUser();

        Feedback feedback = ConvertHelper.convert(cmd, Feedback.class);
        feedback.setNamespaceId(UserContext.getCurrentNamespaceId());
        feedback.setStatus(Byte.parseByte("0"));
        if (user == null) {
            feedback.setOwnerUid(1L);
        } else {
            feedback.setOwnerUid(user.getId());
        }
        if (StringUtils.isEmpty(cmd.getContact())) {
            UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(),
                    IdentifierType.MOBILE.getCode());
            if (identifier != null)
                feedback.setContact(identifier.getIdentifierToken());
        }
        feedback.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        userActivityProvider.addFeedback(feedback, feedback.getOwnerUid());
        return feedback;
    }

    @Override
    public ListFeedbacksResponse listFeedbacks(ListFeedbacksCommand cmd) {
    	ListFeedbacksResponse response = new ListFeedbacksResponse();
    	CrossShardListingLocator locator = new CrossShardListingLocator();
    	locator.setAnchor(cmd.getPageAnchor());
    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//    	List<Feedback> results = userActivityProvider.ListFeedbacks(locator, UserContext.getCurrentNamespaceId(), FeedbackTargetType.POST.getCode(), cmd.getStatus(), pageSize + 1);
    	List<Feedback> results = userActivityProvider.ListFeedbacks(locator, UserContext.getCurrentNamespaceId(), null, cmd.getStatus(), pageSize + 1);
    	if (null == results)
    		return response;
    	Long nextPageAnchor = null;
    	if (results != null && results.size() > pageSize) {
    		results.remove(results.size() - 1);
    		nextPageAnchor = results.get(results.size() - 1).getId();
    	}
    	
    	List<FeedbackDTO> feedbackDtos = new ArrayList<FeedbackDTO>();
    	results.forEach(r -> {
    		FeedbackDTO feedbackDto = ConvertHelper.convert(r, FeedbackDTO.class);
    		User user = userProvider.findUserById(feedbackDto.getOwnerUid());
    		if(user != null){
    			feedbackDto.setOwnerNickName(user.getNickName());
    		}

    		FeedbackContentCategoryType contentCategory = FeedbackContentCategoryType.fromStatus(feedbackDto.getContentCategory().byteValue());
    		feedbackDto.setContentCategoryText(contentCategory.getText());

            FeedbackHandler handler = getFeedBackHandler(feedbackDto.getTargetType());
            if(handler != null){
                //业务实现
                handler.populateFeedbackDTO(feedbackDto);
            }

    		feedbackDtos.add(feedbackDto);
    	});
    	response.setNextPageAnchor(nextPageAnchor); 
    	response.setFeedbackDtos(feedbackDtos);
    	return response;
    }

    @Override
    public void exportFeedbacks(ExportFeedbacksCommand cmd) {
        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        if(cmd.getNamespaceId() != null){
            params.put("namespaceId", cmd.getNamespaceId());
        }

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "activityTagList";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        if (namespace != null) {
            fileName = namespace.getName();
        }
        SimpleDateFormat fileNameSdf = new SimpleDateFormat("yyyyMMdd");
        fileName += "_举报管理_" + fileNameSdf.format(new Date()) + ".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), FeedbackApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
	public void updateFeedback(UpdateFeedbackCommand cmd) {
        Feedback feedback = userActivityProvider.findFeedbackById(cmd.getId());
        if (feedback == null) {
            LOGGER.error("feedback is not exist");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_PARAMS, "feedback is not exist");
        }
        feedback.setStatus((byte) 1);
        feedback.setVerifyType(cmd.getVerifyType());
        feedback.setHandleType(cmd.getHandleType());
        FeedbackHandler handler = getFeedBackHandler(feedback.getTargetType());

        dbProvider.execute(status -> {

            if (handler != null) {
                //业务实现
                handler.beforeUpdateFeedback(cmd);
            }

            //更新自己的状态
            userActivityProvider.updateFeedback(feedback);
            //如果处理方式是删除，将相同目标帖子举报的核实状态更新为已处理，处理方式为无
            if (feedback.getHandleType() == FeedbackHandleType.DELETE.getCode()) {
                userActivityProvider.updateOtherFeedback(feedback.getTargetId(), feedback.getId(), feedback.getVerifyType(), FeedbackHandleType.NONE.getCode());
            }

            if (handler != null) {
                //业务实现
                handler.afterUpdateFeedback(feedback);
            }

            return null;
        });

        //事件处理（比如扣积分等）
        if (handler != null) {
            handler.feedbackEvent(feedback);
        }
    }

    private FeedbackHandler getFeedBackHandler(Byte feedbackTargetType){
        FeedbackHandler handler = null;
        if(feedbackTargetType != null) {
            handler = PlatformContext.getComponent(FeedbackHandler.FEEDBACKHANDLER + feedbackTargetType);
        }
        return handler;
    }
//		//更新自己的状态
//		userActivityProvider.updateFeedback(feedback);
//
//		//如果处理方式是删除，将相同目标帖子举报的核实状态更新为已处理，处理方式为无
//		if(feedback.getHandleType() == FeedbackHandleType.DELETE.getCode()){
//			userActivityProvider.updateOtherFeedback(feedback.getTargetId(), feedback.getId(), feedback.getVerifyType(), FeedbackHandleType.NONE.getCode());
//		}
//
//		//当前只对post类型的举报做实际处理，处理的方式只有删除
//		if(feedback.getTargetType() == FeedbackTargetType.POST.getCode() && feedback.getHandleType() == FeedbackHandleType.DELETE.getCode()){
//			 Post post = forumProvider.findPostById(feedback.getTargetId());
//			 if(post != null){
//				 forumService.deletePost(post.getForumId(), post.getId(), null, null, null);
//             }
//        }


//    }

    /*private void feedbackEvent(Feedback feedback) {
        Post post = forumProvider.findPostById(feedback.getTargetId());
        if(post == null) {
            return;
        }
        Post parentPost = null;
        if (post.getParentPostId() != null && post.getParentPostId() != 0) {
            parentPost = forumProvider.findPostById(post.getParentPostId());
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        Post tempParentPost = parentPost;
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(post.getCreatorUid());
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(post.getId());
            Long embeddedAppId = post.getEmbeddedAppId() != null ? post.getEmbeddedAppId() : 0;
            event.setEventName(SystemEvent.FORUM_POST_REPORT.suffix(
                    post.getModuleType(), post.getModuleCategoryId(), embeddedAppId));

            event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
            event.addParam("feedback", StringHelper.toJsonString(feedback));
            event.addParam("post", StringHelper.toJsonString(post));
            if (tempParentPost != null) {
                event.addParam("parentPost", StringHelper.toJsonString(tempParentPost));
            }
        });
    }*/
	
    @Override
    public void addUserFavorite(AddUserFavoriteCommand cmd) {
    	
    	//收藏时先判断帖子或商家是否存在by xiongying 20160504
    	if(UserFavoriteTargetType.TOPIC.getCode().equals(cmd.getTargetType()) || UserFavoriteTargetType.ACTIVITY.getCode().equals(cmd.getTargetType())) {
    		Post post = forumProvider.findPostById(cmd.getTargetId());
    		
    		if(post == null || post.getStatus() != PostStatus.ACTIVE.getCode()) {
    			LOGGER.error("post was deleted");
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                		ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED, "post was deleted"); 
    		}
    		
    	}
    	
    	if("biz".equals(cmd.getTargetType())) {
    		Business business = businessProvider.findBusinessById(cmd.getTargetId());
    		if(business == null) {
    			LOGGER.error("business not exist");
                throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE,
                		BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, "business not exist"); 
    		}
    	}
    	
        User user = UserContext.current().getUser();
        // 先判断是否存在收藏，再进行插入；对于收藏，由userId-targetType-targetId组成一个唯一key，此key重复也会报错，需要把该错截取处理 by lqs 20160422
        List<UserFavoriteDTO> dbFavorites = userActivityProvider.findFavorite(user.getId(), cmd.getTargetType(), cmd.getTargetId());
        if(dbFavorites == null || dbFavorites.size() == 0) {
            UserFavorite userFavorite = ConvertHelper.convert(cmd, UserFavorite.class);
            userFavorite.setOwnerUid(user.getId());
            userFavorite.setTargetType(cmd.getTargetType());
            userFavorite.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            try {
                userActivityProvider.addUserFavorite(userFavorite);
            } catch (DuplicateKeyException e) {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("The target is already favorited(duplicated), cmd={}", cmd, e);
                }
            } catch (DataAccessException e) {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("The target is already favorited, cmd={}", cmd, e);
                }
            }
        }
    }

    @Override
    public ListPostResponse listPostedTopics(ListPostedTopicByOwnerIdCommand cmd) {
        Long uid = cmd.getOwnerUid();
        if (uid == null) {
            User user = UserContext.current().getUser();
            uid = user.getId();
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<UserPost> result = userActivityProvider.listPostedTopics(uid, UserFavoriteTargetType.TOPIC.getCode(), locator, pageSize + 1);

        Long nextPageAnchor = locator.getAnchor();

        // 去除最后一条数据返回pageSize  add by yanjun 20170802
        if(result != null && result.size() > pageSize) {
            result.remove(pageSize);
        }

        if (CollectionUtils.isEmpty(result)) {
        	ListPostResponse response = new ListPostResponse();
            response.setPostDtos(new ArrayList<PostDTO>());
            return response;
        }
        
        // 由于每次都是从前往后取，取完之后再倒排，导致回去的anchor一直都是最小值，
        // 也就是下一页和第一页取得的结果是一样的，需要倒排着查，修改后anchor的设置在provider里 by lqs 20160928
//        if(result.size() > pageSize) {
//        	locator.setAnchor(result.get(result.size() - 1).getId());
//        	result = result.subList(0, result.size() - 1);
//        } else {
//        	locator.setAnchor(null);
//        }
        
        List<Long> ids = result.stream().map(r -> r.getTargetId()).collect(Collectors.toList());
        // 由于每次都是从前往后取，取完之后再倒排，导致回去的anchor一直都是最小值，
        // 也就是下一页和第一页取得的结果是一样的，需要倒排着查，修改后不需要再额外排序 by lqs 20160928        
//        Collections.sort(ids);
//        Collections.reverse(ids);
        List<PostDTO> posts = forumService.getTopicById(ids, cmd.getCommunityId(), false, true);
        
        ListPostResponse response = new ListPostResponse();
        response.setPostDtos(posts);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }
    
    private void bizFindOrderCountByUserId(Long userId, ListTreasureResponse response) {
        Integer nonce = (int)(Math.random()*1000);
        Long timestamp = System.currentTimeMillis();       
        Map<String,String> params = new HashMap<String, String>();
        params.put("nonce", nonce+"");
        params.put("timestamp", timestamp+"");
        params.put("userId", userId+"");
        
        try {
            bizHttpRestCallProvider.restCall("/rest/openapi/order/findOrderCountByUserId", params, new ListenableFutureCallback<ResponseEntity<String>> () {

                @Override
                public void onSuccess(ResponseEntity<String> result) {
                	Gson gson = new Gson();
                    BizOrderHolder holder = gson.fromJson(result.getBody(), BizOrderHolder.class);
                    if(holder.getResult()){
                    	OrderCountDTO orderCount = gson.fromJson(holder.getBody(), new TypeToken<OrderCountDTO>() {}.getType());
                    	response.setOrderCount(Integer.parseInt(orderCount.getOrderCount()));
                    }
                    
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error("find error", ex);
                    
                }
                
            });
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("find error", e);
        }
    }

    @Override
    public ListTreasureResponse getUserTreasure() {
        // 检查是否登陆，没登陆则只返回游客访问的电商url by sfyan 20161009
    	if(!userService.isLogon()){
            ListTreasureResponse rsp = new ListTreasureResponse();
            rsp.setBusinessUrl(getTouristBusinessUrl());
            rsp.setBusinessRealm(getBusinessRealm());
            //设置活动列表的默认列表样式, add by tt, 20170116
            rsp.setActivityDefaultListStyle(getActivityDefaultListStyle());
            rsp.setPoints(0L);
            rsp.setPointsUrlStatus(TrueOrFalseFlag.FALSE.getCode());
            return rsp;
        }
        //2016-07-29:modify by liujinwen.get orderCount's value like couponCount
        User user = UserContext.current().getUser();
        ListTreasureResponse rsp = ConvertHelper.convert(user, ListTreasureResponse.class);
        UserProfile item = userActivityProvider.findUserProfileBySpecialKey(user.getId(),
                UserProfileContstant.POSTED_TOPIC_COUNT);
        UserProfile fav = userActivityProvider.findUserProfileBySpecialKey(user.getId(),
                UserProfileContstant.FAVOTITE_TOPIC_COUNT);
        UserProfile applied = userActivityProvider.findUserProfileBySpecialKey(user.getId(),
                UserProfileContstant.IS_APPLIED_SHOP);
        UserProfile couponCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), 
        		UserProfileContstant.RECEIVED_COUPON_COUNT);
        UserProfile orderCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), 
        		UserProfileContstant.RECEIVED_ORDER_COUNT);
        UserProfile shakeOpenDoorUser = userActivityProvider.findUserProfileBySpecialKey(user.getId(), 
          		UserProfileContstant.SHAKE_OPEN_DOOR);
        
        if (item != null)
            rsp.setSharedCount(NumberUtils.toInt(item.getItemValue(), 0));
        if (fav != null)
            rsp.setTopicFavoriteCount(NumberUtils.toInt(fav.getItemValue(), 0));
        
        rsp.setApplyShopUrl(getApplyShopUrl());
        if (applied != null) {
        	rsp.setIsAppliedShop(NumberUtils.toInt(applied.getItemValue(), 0));
        	if (NumberUtils.toInt(applied.getItemValue(), 0) != 0) 
        		rsp.setApplyShopUrl(getManageShopUrl(user.getId()));
        }
        //#17132
        //INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('always.store.management', 'true', '是否始终是店铺管理', 0, NULL);
        boolean alwaysStoreManage = configurationProvider.getBooleanValue("always.store.management", false);
        if(alwaysStoreManage){
        	rsp.setIsAppliedShop(1);
            rsp.setApplyShopUrl(getManageShopUrl(user.getId()));
        }
        
        if(couponCount != null) {
        	rsp.setCouponCount(NumberUtils.toInt(couponCount.getItemValue(), 0));
        }else {
        	rsp.setCouponCount(0);
        }
        rsp.setMyOrderUrl(getMyOrderUrl());
        rsp.setPointRuleUrl(getPointRuleUrl());
        rsp.setMyCoupon(getMyCoupon());

        if(orderCount != null) {
        	rsp.setOrderCount(NumberUtils.toInt(orderCount.getItemValue(), 0));
        } else {
        	rsp.setOrderCount(0);
        }
        //bizFindOrderCountByUserId(user.getId(), rsp);
        
        rsp.setBusinessUrl(getBusinessUrl());
        rsp.setBusinessRealm(getBusinessRealm());
        
        //设置活动列表的默认列表样式, add by tt, 20170116
        rsp.setActivityDefaultListStyle(getActivityDefaultListStyle());
        
        //设置用户的摇一摇开门是否开启
        if(shakeOpenDoorUser != null){
        	rsp.setShakeOpenDoorUser(Byte.parseByte(shakeOpenDoorUser.getItemValue()));
        }else{
        	rsp.setShakeOpenDoorUser(Byte.parseByte("0"));
        }
        String shakeOpenDoorNamespace = getShakeOpenDoor();
        if(shakeOpenDoorNamespace != null){
        	rsp.setShakeOpenDoorNamespace(Byte.parseByte(shakeOpenDoorNamespace));
        }else{
        	rsp.setShakeOpenDoorNamespace(Byte.parseByte("0"));
        }
        return rsp;
    }

    @Override
    public GetUserTreasureResponse getUserTreasureV2() {
        GetUserTreasureResponse rsp = new GetUserTreasureResponse();

        UserTreasureDTO coupon = new UserTreasureDTO();
        coupon.setCount(0L);
        coupon.setStatus(TrueOrFalseFlag.TRUE.getCode());
        coupon.setUrlStatus(TrueOrFalseFlag.FALSE.getCode());

        UserTreasureDTO order = new UserTreasureDTO();
        order.setCount(0L);
        order.setStatus(TrueOrFalseFlag.TRUE.getCode());
        order.setUrlStatus(TrueOrFalseFlag.FALSE.getCode());

        rsp.setCoupon(coupon);
        rsp.setOrder(order);

        UserTreasureDTO point = pointService.getPointTreasure();
        rsp.setPoint(point);

        if(!userService.isLogon()) {
            return rsp;
        }

        User user = UserContext.current().getUser();

        BizMyUserCenterCountResponse response = fetchBizMyUserCenterCount(user);

        // UserProfile couponCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.RECEIVED_COUPON_COUNT);
        // UserProfile orderCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.RECEIVED_ORDER_COUNT);

        if (response != null && response.getResponse() != null) {
            long promotionCount = response.getResponse().promotionCount;
            long shoppingCardCount = response.getResponse().shoppingCardCount;
            long orderCount = response.getResponse().orderCount;

            coupon.setCount(promotionCount + shoppingCardCount);
            order.setCount(orderCount);
        }

        coupon.setUrl(getMyCoupon());
        coupon.setUrlStatus(TrueOrFalseFlag.TRUE.getCode());

        order.setUrl(getMyOrderUrl());
        order.setUrlStatus(TrueOrFalseFlag.TRUE.getCode());

        return rsp;
    }

    @Override
    public GetUserTreasureNewResponse getUserTreasureNew() {
        GetUserTreasureNewResponse rsp = new GetUserTreasureNewResponse();

        if(!userService.isLogon()) {
            return rsp;
        }

        User user = UserContext.current().getUser();
        rsp.setVipLevelText(user.getVipLevelText());
        BizMyUserCenterCountResponse response = fetchBizMyUserCenterCount(user);

        // UserProfile couponCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.RECEIVED_COUPON_COUNT);
        // UserProfile orderCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.RECEIVED_ORDER_COUNT);

        if (response != null && response.getResponse() != null) {
            long promotionCount = response.getResponse().promotionCount;
            long shoppingCardCount = response.getResponse().shoppingCardCount;
            long orderCount = response.getResponse().orderCount;

            rsp.setCoupon(promotionCount + shoppingCardCount);
            rsp.setOrder(orderCount);
        }
        if (rsp.getCoupon() == null) {
            rsp.setCoupon(0L);
        }
        if (rsp.getOrder() == null) {
            rsp.setOrder(0L);
        }
        UserTreasureDTO point = new UserTreasureDTO();
        try {
            point = pointService.getPointTreasure();
        }catch (Exception e) {
            LOGGER.error("get point exception");
            e.printStackTrace();
        }
        rsp.setPoint(point.getCount());
        rsp.setPointUrl(point.getUrl());
        if (rsp.getPoint() == null) {
            rsp.setPoint(0L);
        }
        return rsp;
    }

    @Override
    public GetUserTreasureForRuiAnResponse getUserTreasureForRuiAn() {
        GetUserTreasureForRuiAnResponse response = new GetUserTreasureForRuiAnResponse();
        //积分
        UserTreasureDTO point = new UserTreasureDTO();
        try {
            point = pointService.getPointTreasure();
        }catch (Exception e) {
            LOGGER.error("get point exception");
            e.printStackTrace();
        }
        response.setPoint(point.getCount());
        String pointUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.RUIAN_POINT_URL, "https://m.mallcoo.cn/a/user/10764/Point/List");
        response.setPointUrl(pointUrl);

        //会员
        User user = this.userProvider.findUserById(UserContext.currentUserId());
        if (user != null) {
            response.setVipLevelText(user.getVipLevelText());
        }
        String vipUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.RUIAN_VIP_URL, "https://m.mallcoo.cn/a/custom/10764/xtd/Rights");
        response.setVipUrl(vipUrl);

        //任务
        SearchFlowCaseCommand caseCommand = new SearchFlowCaseCommand();
        caseCommand.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
        caseCommand.setFlowCaseStatus(FlowCaseStatus.PROCESS.getCode());
        GetFlowCaseCountResponse task = this.flowService.getFlowCaseCount(caseCommand);
        response.setTask(0);
        if (task != null && task.getCount() != null) {
            response.setTask(task.getCount());
        }


        //订单
        BizMyUserCenterCountResponse biz = fetchBizMyUserCenterCount(user);
        if (biz != null && biz.getResponse() != null) {

            long orderCount = biz.getResponse().orderCount;
            response.setOrder(orderCount);
        }
        String orderHomeUrl = this.configurationProvider.getValue(0,"personal.order.home.url","https://biz.zuolin.com");
        String url = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),ConfigConstants.RUIAN_ORDER_URL,
                "/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix");
        response.setOrderUrl(orderHomeUrl + url);

        //卡包
        return response;
    }

    private BizMyUserCenterCountResponse fetchBizMyUserCenterCount(User user) {
        Map<String, Object> param = new HashMap<>();
        param.put("namespaceId", user.getNamespaceId());
        param.put("userId", user.getId());

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("body", param);

        String paramJson = StringHelper.toJsonString(bodyMap);

        BizMyUserCenterCountResponse response = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fetch user treasure from biz, param = {}", paramJson);
            }
            ResponseEntity<String> responseEntity = bizHttpRestCallProvider.syncRestCall(
                    "/rest/openapi/myCenter/myUserCenterCount", paramJson);

            String body = responseEntity.getBody();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fetch user treasure from biz, response = {}", body);
            }
            response = (BizMyUserCenterCountResponse) StringHelper.fromJsonString(body, BizMyUserCenterCountResponse.class);
        } catch (Exception e) {
            LOGGER.error("User treasure biz call error", e);
        }
        return response;
    }

    @Override
	public ListBusinessTreasureResponse getUserBusinessTreasure() {
    	// 检查是否登陆，没登陆则只返回游客访问的电商url by sfyan 20161009
    	if(!userService.isLogon()){
    		ListBusinessTreasureResponse rsp = new ListBusinessTreasureResponse();
            rsp.setBusinessUrl(getTouristBusinessUrl());
            rsp.setBusinessRealm(getBusinessRealm());
            return rsp;
        }
        //2016-07-29:modify by liujinwen.get orderCount's value like couponCount
        User user = UserContext.current().getUser();
        ListBusinessTreasureResponse rsp = ConvertHelper.convert(user, ListBusinessTreasureResponse.class);
        UserProfile applied = userActivityProvider.findUserProfileBySpecialKey(user.getId(),
                UserProfileContstant.IS_APPLIED_SHOP);
        UserProfile couponCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), 
        		UserProfileContstant.RECEIVED_COUPON_COUNT);
        UserProfile orderCount = userActivityProvider.findUserProfileBySpecialKey(user.getId(), 
        		UserProfileContstant.RECEIVED_ORDER_COUNT);
        
        rsp.setApplyShopUrl(getApplyShopUrl());
        if (applied != null) {
        	rsp.setIsAppliedShop(NumberUtils.toInt(applied.getItemValue(), 0));
        	if (NumberUtils.toInt(applied.getItemValue(), 0) != 0) 
        		rsp.setApplyShopUrl(getManageShopUrl(user.getId()));
        }
        
        if(couponCount != null) {
        	rsp.setCouponCount(NumberUtils.toInt(couponCount.getItemValue(), 0));
        }else {
        	rsp.setCouponCount(0);
        }
        rsp.setMyOrderUrl(getMyOrderUrl());
        rsp.setMyCoupon(getMyCoupon());
        
        if(orderCount != null) {
        	rsp.setOrderCount(NumberUtils.toInt(orderCount.getItemValue(), 0));
        } else {
        	rsp.setOrderCount(0);
        }
        
        rsp.setBusinessUrl(getBusinessUrl());
        rsp.setBusinessRealm(getBusinessRealm());
        
        return rsp;
	}

	private Byte getActivityDefaultListStyle() {
    	String activityDefaultListStyle = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.ACTIVITY_DEFAULT_LIST_STYLE, String.valueOf(ActivityListStyleFlag.ZUOLIN_COMMON.getCode()));
    	return Byte.parseByte(activityDefaultListStyle);
	}

	private String getBusinessRealm() {
    	String businessRealm = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.BUSINESS_REALM, "");
        if(businessRealm.length() == 0) {
            LOGGER.error("Invalid business url path, businessRealm=" + businessRealm);
            return null;
        } else {
            return businessRealm;
        }
	}

	private String getBusinessUrl() {
    	String businessUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.BUSINESS_URL, "");
        if(businessUrl.length() == 0) {
            LOGGER.error("Invalid business url path, businessUrl=" + businessUrl);
            return null;
        } else {
            return businessUrl;
        }
	}

    private String getTouristBusinessUrl() {
        String touristBusinessUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.TOURIST_BUSINESS_URL, "");
        if(touristBusinessUrl.length() == 0) {
            LOGGER.error("Invalid business url path, businessUrl=" + touristBusinessUrl);
            return null;
        } else {
            return touristBusinessUrl;
        }
    }

	private String getMyOrderUrl() {
        String homeurl = configurationProvider.getValue(ConfigConstants.PREFIX_URL, "");
        String orderPath = configurationProvider.getValue(ConfigConstants.USER_ORDER_URL, "");
        if(homeurl.length() == 0 || orderPath.length() == 0) {
            LOGGER.error("Invalid home url or order path, homeUrl=" + homeurl + ", orderPath=" + orderPath);
            return null;
        } else {
            return homeurl + orderPath;
        }
    }
    
    private String getApplyShopUrl() {
        String homeurl = configurationProvider.getValue(ConfigConstants.PREFIX_URL, "");
        String applyShopPath = configurationProvider.getValue(ConfigConstants.APPLY_SHOP_URL, "");
        if(homeurl.length() == 0 || applyShopPath.length() == 0) {
            LOGGER.error("Invalid home url or apply path, homeUrl=" + homeurl + ", applyShopPath=" + applyShopPath);
            return null;
        } else {
            return homeurl + applyShopPath;
        }
    }
    
    private String getManageShopUrl(Long userId) {
        String homeurl = configurationProvider.getValue(ConfigConstants.PREFIX_URL, "");
        String manageShopPath = configurationProvider.getValue(ConfigConstants.MANAGE_SHOP_URL, "");
//        List<Business> business = businessProvider.findBusinessByCreatorId(userId);
//        String shopId = null;
//        for(Business buz : business){
//        	if( BusinessTargetType.ZUOLIN.getCode() == buz.getTargetType()){
//        		shopId = buz.getTargetId();
//        		 break;
//        	}
//        }
        if(homeurl.length() == 0 || manageShopPath.length() == 0) {
            LOGGER.error("Invalid home url or manage path, homeUrl=" + homeurl + ", manageShopPath=" + manageShopPath);
            return null;
        } else {
//            return homeurl + manageShopPath + shopId;
        	return homeurl + manageShopPath;
        }
    }

    private String getPointRuleUrl() {
        String scorePath = configurationProvider.getValue(ConfigConstants.USER_SCORE_URL, "");
        if(scorePath.length() == 0) {
            LOGGER.error("Invalid home url or score path,  scorePath=" + scorePath);
            return null;
        } else {
            return scorePath;
        }
    }

    private String getMyCoupon() {
    	String homeurl = configurationProvider.getValue(ConfigConstants.PREFIX_URL, "");
    	String couponPath = configurationProvider.getValue(ConfigConstants.USER_COUPON_URL, "");
        if(homeurl.length() == 0 || couponPath.length() == 0) {
        	LOGGER.error("Invalid home url or coupon path, homeUrl=" + homeurl + ", couponPath=" + couponPath);
        	return null;
        } else {
        	return homeurl + couponPath;
        }
    }

    @Override
    public ListPostResponse listFavoriteTopics(ListUserFavoriteTopicCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
//        List<UserFavoriteDTO> favorites = userActivityProvider.findFavorite(user.getId(), "topic", locator, pageSize + 1);
//
//        Long nextPageAnchor = null;
//        if(favorites != null && favorites.size() > pageSize) {
//        	nextPageAnchor = favorites.get(favorites.size() - 1).getId();
//        	favorites = favorites.subList(0, favorites.size() - 1);
//        }
//        
//        List<Long> ret = favorites.stream().map(r->r.getTargetId()).collect(Collectors.toList());
        List<Long> ret = getUserFavoriteTargetIds(UserFavoriteTargetType.TOPIC.getCode(), locator, pageSize);
        Long nextPageAnchor = locator.getAnchor();
        
        List<PostDTO> posts = forumService.getTopicById(ret, cmd.getCommunityId(), false);
        
        ListPostResponse response = new ListPostResponse();
        response.setPostDtos(posts);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }
    
    /*
     * 按收藏人id和收藏类型分页查 by xiongying 20160614
     */
    private List<Long> getUserFavoriteTargetIds(String targetType, CrossShardListingLocator locator, int count) {
    	User user = UserContext.current().getUser();
    	List<UserFavoriteDTO> favorites = userActivityProvider.findFavorite(user.getId(), targetType, locator, count + 1);

        
        if(favorites != null && favorites.size() > count) {
        	locator.setAnchor(favorites.get(favorites.size() - 1).getId());
        	favorites = favorites.subList(0, favorites.size() - 1);
        } else {
        	locator.setAnchor(null);
        }
        
        List<Long> ret = favorites.stream().map(r->r.getTargetId()).collect(Collectors.toList());
        return ret;
    }

    @Override
    public void cancelFavorite(CancelUserFavoriteCommand cmd) {
        User user = UserContext.current().getUser();
        userActivityProvider.deleteFavorite(user.getId(), cmd.getTargetId(), cmd.getTargetType());
    }


    @Override
    public List<UserServiceAddressDTO> getUserRelateServiceAddress() {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        GetUserServiceAddressCommand cmd = new GetUserServiceAddressCommand();
        cmd.setUserId(userId);
        return getUserServiceAddress(cmd);
        
    }

	@Override
	public void addUserShop(Long userId) {
        userActivityProvider.addUserShop(userId);
		
	}

	
	@Override
	public void cancelShop(Long userId) {
		userActivityProvider.deleteShop(userId);
		
	}

    @Override
    public List<UserServiceAddressDTO> getUserServiceAddress(GetUserServiceAddressCommand cmd) {
        if(cmd.getUserId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is not found");
        }
        User user = this.userProvider.findUserById(cmd.getUserId());
        if(user == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is not found");
        }
        
        List<UserServiceAddress> serviceAddresses = this.userActivityProvider.findUserRelateServiceAddresses(user.getId());
        if(serviceAddresses == null)
            return null;
        List<UserServiceAddressDTO> result = new ArrayList<UserServiceAddressDTO>();
//        List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
//        List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
//            .map((r) -> { return r.getIdentifierToken(); })
//            .collect(Collectors.toList());
        
        serviceAddresses.forEach(r ->{
            Address addr = this.addressProvider.findAddressById(r.getAddressId());
            if(addr == null){
                LOGGER.error("Address is not found,addressId=" + r.getAddressId());
                return;
            }
            UserServiceAddressDTO address = new UserServiceAddressDTO();
            Region city = this.regionProvider.findRegionById(addr.getCityId());
            if(city != null){
                Region province = this.regionProvider.findRegionById(city.getParentId());
                if(province != null)
                    address.setProvince(province.getName());
            }
            address.setId(addr.getId());
            address.setCity(addr.getCityName());
            address.setArea(addr.getAreaName());
            address.setAddress(addr.getAddress());
            address.setUserName(r.getContactName());
            address.setCallPhone(r.getContactToken());
            
            result.add(address);
            
        });
        return result;
    }

	@Override
	public void receiveCoupon(Long userId) {
		userActivityProvider.updateProfileIfNotExist(userId, UserProfileContstant.RECEIVED_COUPON_COUNT, 1);
		
	}

	@Override
	public void invalidCoupon(Long userId) {
		userActivityProvider.updateProfileIfNotExist(userId, UserProfileContstant.RECEIVED_COUPON_COUNT, -1);
	}

	@Override
	public void updateUserProfile(Long userId, String name, String value) {
		UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(userId,name);
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setOwnerId(userId);
            userProfile.setItemName(name);
            userProfile.setItemKind((byte) 1);
            userProfile.setItemValue(value);
            userActivityProvider.addUserProfile(userProfile);
            return;
        }
        userProfile.setItemValue(value);
        userActivityProvider.updateUserProfile(userProfile);
	}

	@Override
	public ListActivitiesReponse listActivityFavorite(
			ListUserFavoriteActivityCommand cmd) {
		User user = UserContext.current().getUser();
		Long uid = user.getId();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
//        List<UserFavoriteDTO> favorites = userActivityProvider.findFavorite(user.getId(), "topic", locator, pageSize + 1);
//
//        Long nextPageAnchor = null;
//        if(favorites != null && favorites.size() > pageSize) {
//        	nextPageAnchor = favorites.get(favorites.size() - 1).getId();
//        	favorites = favorites.subList(0, favorites.size() - 1);
//        }
//        
//        List<Long> ret = favorites.stream().map(r->r.getTargetId()).collect(Collectors.toList());
        List<Long> ret = getUserFavoriteTargetIds(UserFavoriteTargetType.ACTIVITY.getCode(), locator, pageSize);
        Long nextPageAnchor = locator.getAnchor();
        
        List<ActivityDTO> activities = new ArrayList<ActivityDTO>();
        for(Long postId : ret) {
        	Activity activity =  activityProivider.findSnapshotByPostId(postId);
        	if(activity != null && activity.getStatus() == PostStatus.ACTIVE.getCode()) {
        		ActivityDTO dto = convertToActivityDto(activity, uid);
        		fixupVideoInfo(dto); // added by janson
        		
        		activities.add(dto);
        	}
        }
        
        ListActivitiesReponse response = new ListActivitiesReponse(nextPageAnchor, activities);
        return response;
	}

	private ProcessStatus getStatus(Activity activity) {
        return StatusChecker.getProcessStatus(activity.getStartTimeMs(), activity.getEndTimeMs());
    }
	
	@Override
	public ListActivitiesReponse listPostedActivities(
			ListPostedActivityByOwnerIdCommand cmd) {
		Long uid = cmd.getOwnerUid();
        if (uid == null) {
            User user = UserContext.current().getUser();
            uid = user.getId();
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<UserPost> result = userActivityProvider.listPostedTopics(uid, UserFavoriteTargetType.ACTIVITY.getCode(), locator, pageSize + 1);
        
        if (CollectionUtils.isEmpty(result)) {
            return new ListActivitiesReponse(null, new ArrayList<ActivityDTO>());
        }

        // 由于每次都是从前往后取，取完之后再倒排，导致回去的anchor一直都是最小值，
        // 也就是下一页和第一页取得的结果是一样的，需要倒排着查，修改后anchor的设置在provider里 by lqs 20160928
//        if(result.size() > pageSize) {
//        	locator.setAnchor(result.get(result.size() - 1).getId());
//        	result = result.subList(0, result.size() - 1);
//        } else {
//        	locator.setAnchor(null);
//        }
        
        Long nextPageAnchor = locator.getAnchor();

        // 去除最后一条数据返回pageSize  add by yanjun 20170802
        if(result != null && result.size() > pageSize) {
            result.remove(pageSize);
        }

        List<Long> ids = result.stream().map(r -> r.getTargetId()).collect(Collectors.toList());
        // 由于每次都是从前往后取，取完之后再倒排，导致回去的anchor一直都是最小值，
        // 也就是下一页和第一页取得的结果是一样的，需要倒排着查，修改后不需要再额外排序 by lqs 20160928   
//        Collections.sort(ids);
//        Collections.reverse(ids);
        List<ActivityDTO> activities = new ArrayList<ActivityDTO>();
        for(Long postId : ids) {
        	Activity activity =  activityProivider.findSnapshotByPostId(postId);
        	if(activity != null && activity.getStatus() == PostStatus.ACTIVE.getCode()) {
        		ActivityDTO dto = convertToActivityDto(activity, uid);
        		fixupVideoInfo(dto); // added by janson
        		
        		
        		activities.add(dto);
        	} 
        }
        
        ListActivitiesReponse response = new ListActivitiesReponse(nextPageAnchor, activities);
        return response;
	}

	@Override
	public ListActivitiesReponse listSignupActivities(ListSignupActivitiesCommand cmd) {

		User user = UserContext.current().getUser();
        Long uid = user.getId();
        
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<ActivityRoster> result = activityProivider.findRostersByUid(uid, locator, pageSize + 1, ActivityRosterStatus.NORMAL.getCode());
        
        if (CollectionUtils.isEmpty(result)) {
            return new ListActivitiesReponse(null, new ArrayList<ActivityDTO>());
        }
        
        if(result.size() > pageSize) {
        	locator.setAnchor(result.get(result.size() - 1).getCreateTime().getTime());
        	result = result.subList(0, result.size() - 1);
        } else {
        	locator.setAnchor(null);
        }
        
        Long nextPageAnchor = locator.getAnchor();
        List<Long> ids = result.stream().map(r -> r.getActivityId()).collect(Collectors.toList());
        Collections.sort(ids);
        Collections.reverse(ids);
        List<ActivityDTO> activities = new ArrayList<ActivityDTO>();
        for(Long id : ids) {
        	Activity activity =  activityProivider.findActivityById(id);
        	if(activity != null && activity.getStatus() == PostStatus.ACTIVE.getCode()) {
        		ActivityDTO dto = convertToActivityDto(activity, uid);
        		fixupVideoInfo(dto); // added by janson
        		
        		activities.add(dto);
        	}
        }
        
        ListActivitiesReponse response = new ListActivitiesReponse(nextPageAnchor, activities);
        return response;
	}
	
	private ActivityDTO convertToActivityDto(Activity activity, Long uid) {
		ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
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
		if(post != null) {
            dto.setForumId(post.getForumId());
        }
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
        String posterUrl = getActivityPosterUrl(activity);
        dto.setPosterUrl(posterUrl);
        fixupVideoInfo(dto); // added by janson
        
        ActivityRoster roster = activityProivider.findRosterByUidAndActivityId(activity.getId(), uid, ActivityRosterStatus.NORMAL.getCode());
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        
        List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(uid, UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
        if(favorite == null || favorite.size() == 0) {
        	dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
        } else {
        	dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
        }
        
		return dto;
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

	@Override
	public UserProfileDTO findUserProfileBySpecialKey(Long userId, String itemName) {
		if(userId==null|| StringUtils.isEmpty(itemName)){
			LOGGER.error("userId or itemName is null");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_PARAMS, "userId or itemName is null");
		}
		UserProfile profile =  userActivityProvider.findUserProfileBySpecialKey(userId, itemName);
		if(profile==null)
			return null;
		return ConvertHelper.convert(profile, UserProfileDTO.class);
	}

	@Override
	public void updateProfileIfNotExist(Long userId, String itemName, Integer itemValue) {
		if(userId==null||StringUtils.isEmpty(itemName)||itemValue==null){
			LOGGER.error("userId or itemName or itemValue is null");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_PARAMS, "userId or itemName or itemValue is null");
		}
		userActivityProvider.updateProfileIfNotExist(userId, itemName, itemValue);
	}

	@Override
	public RequestTemplateDTO getCustomRequestTemplate(
			GetCustomRequestTemplateCommand cmd) {
		RequestTemplates template = this.userActivityProvider.getCustomRequestTemplate(cmd.getTemplateType());
		if(template != null) {
			RequestTemplateDTO dto = ConvertHelper.convert(template, RequestTemplateDTO.class);
			List<FieldDTO> fields = analyzefields(template.getFieldsJson());
			dto.setDtos(fields);
			
			return dto;
		}
		
		return null;
	}
	
	private List<FieldDTO> analyzefields(String fieldsJson) {
		Gson gson = new Gson();
		FieldTemplateDTO fields = gson.fromJson(fieldsJson, new TypeToken<FieldTemplateDTO>() {}.getType());
		List<FieldDTO> dto = fields.getFields();
		return dto;
	}

	@Override
	public List<RequestTemplateDTO> getCustomRequestTemplateByNamespace() {
		List<RequestTemplateDTO> dtos = new ArrayList<RequestTemplateDTO>();
		List<RequestTemplatesNamespaceMapping> mappings = this.userActivityProvider.getRequestTemplatesNamespaceMappings(UserContext.getCurrentNamespaceId());
		//配了mapping的从mapping里捞 ，没配的把所有模板都返回
		if(mappings != null && mappings.size() > 0) {
			dtos = mappings.stream().map(mapping -> {
				RequestTemplates template = this.userActivityProvider.getCustomRequestTemplate(mapping.getTemplateId());
				RequestTemplateDTO dto = null;
				if(template != null) {
					dto = ConvertHelper.convert(template, RequestTemplateDTO.class);
					List<FieldDTO> fields = analyzefields(template.getFieldsJson());
					dto.setDtos(fields);
				}
				return dto;
			}).filter(r->r!=null).collect(Collectors.toList());
		
		} else {
			List<RequestTemplates> templates = this.userActivityProvider.listCustomRequestTemplates();
			if(templates != null && templates.size() > 0) {
				dtos = templates.stream().map(template -> {
					RequestTemplateDTO dto = ConvertHelper.convert(template, RequestTemplateDTO.class);
					List<FieldDTO> fields = analyzefields(template.getFieldsJson());
					dto.setDtos(fields);
					return dto;
				}).filter(r->r!=null).collect(Collectors.toList());
			}
		}
		
		return dtos;
	}

	@Override
	public void addCustomRequest(AddRequestCommand cmd) {

        //与工作流对接
        CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
        Long userId = UserContext.current().getUser().getId();
        cmd21.setApplyUserId(userId);
        cmd21.setReferType(cmd.getTemplateType());
        cmd21.setProjectId(cmd.getOwnerId());
        cmd21.setProjectType(EhCommunities.class.getName());
        String content= "";
        User user = UserContext.current().getUser();

        ServiceAlliances serviceOrg = yellowPageProvider.findServiceAllianceById(cmd.getServiceAllianceId(), null, null);
        if(serviceOrg != null) {
            content += "服务名称 : " + serviceOrg.getName() + "\n";
            ServiceAllianceCategories category = yellowPageProvider.findCategoryById(serviceOrg.getParentId());
            cmd21.setTitle(category.getName());
        }
//        if (user.getNickName()!=null)
//            content += CustomRequestConstants.USER_NAME+":"+user.getNickName()+"\n";


        cmd21.setContent(content);
        cmd21.setCurrentOrganizationId(cmd.getCreatorOrganizationId());
        RequestTemplates template = this.userActivityProvider.getCustomRequestTemplate(cmd.getTemplateType());



        //创建一个空的flow
        GeneralModuleInfo gm = new GeneralModuleInfo();
        gm.setNamespaceId(UserContext.getCurrentNamespaceId());
        gm.setOrganizationId(cmd.getCreatorOrganizationId());
        gm.setProjectId(cmd.getOwnerId());
        gm.setProjectType(EntityType.COMMUNITY.getCode());
        gm.setOwnerType(FlowOwnerType.CUSTOM_REQUEST.getCode());
        gm.setOwnerId(template.getId());
        gm.setModuleId(40500l);//服务联盟id
        ServiceModule module = serviceModuleProvider.findServiceModuleById(40500l);
        gm.setModuleType(module.getName());


        CustomRequestHandler handler = getCustomRequestHandler(cmd.getTemplateType());
		Long id = handler.addCustomRequest(cmd);
		cmd21.setReferId(id);
        flowService.createDumpFlowCase(gm, cmd21);
	}

	@Override
	public GetRequestInfoResponse getCustomRequestInfo(GetRequestInfoCommand cmd) {
		CustomRequestHandler handler = getCustomRequestHandler(cmd.getTemplateType());
		
		GetRequestInfoResponse dto = handler.getCustomRequestInfo(cmd.getId());
		return dto;
	}
	
	private CustomRequestHandler getCustomRequestHandler(String templateType) {
		CustomRequestHandler handler = null;
        
        if(!StringUtils.isEmpty(templateType)) {
            String handlerPrefix = CustomRequestHandler.CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX;
//            if(templateType.length() > 7 && CustomRequestConstants.RESERVE_REQUEST_CUSTOM.equals(templateType.substring(0, 7))) {
//            	templateType = CustomRequestConstants.RESERVE_REQUEST_CUSTOM;
//            }
            if(templateType.startsWith(CustomRequestConstants.RESERVE_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.RESERVE_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.APARTMENT_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.APARTMENT_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.SETTLE_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.SETTLE_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.INVEST_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.INVEST_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.GOLF_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.GOLF_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.GYM_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.GYM_REQUEST_CUSTOM;
            } else if(templateType.startsWith(CustomRequestConstants.SERVER_REQUEST_CUSTOM)) {
                templateType = CustomRequestConstants.SERVER_REQUEST_CUSTOM;
            }
            handler = PlatformContext.getComponent(handlerPrefix + templateType);
        }
        
        return handler;
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
	public ListActiveStatResponse listActiveStat(ListActiveStatCommand cmd) { 
		ListActiveStatResponse response = new ListActiveStatResponse();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<StatActiveUser>  results = this.userActivityProvider.listActiveStats(cmd.getBeginDate(),cmd.getEndDate(),namespaceId,locator, pageSize + 1);
        if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);        
		response.setActiveStats(new ArrayList<UserActiveStatDTO>());
        for(StatActiveUser stat : results){
        	UserActiveStatDTO dto = ConvertHelper.convert(stat, UserActiveStatDTO.class);
        	dto.setStatDate(stat.getStatDate().getTime());
        	response.getActiveStats().add(dto);
        }
		return response;
	}
	
	/**
	 * 每天早上3点10分刷前一天的活跃度
	 * */
	@Scheduled(cron = "0 10 3 * * ?") 
	public void addAnyDayActive(){
        if (Objects.equals(scheduleProvider.getRunningFlag(), RunningFlag.TRUE.getCode())) {
            Date statDate = new Date();
            List<NamespaceInfoDTO>  namespaces = namespacesService.listNamespace();
            for(NamespaceInfoDTO namespace : namespaces){
                addAnyDayActive(statDate, namespace.getId());
            }
        }
	}
	
	@Override
	public void addAnyDayActive(Date statDate ,Integer namespaceId){
		StatActiveUser stat = this.userActivityProvider.findStatActiveUserByDate(new java.sql.Date(statDate.getTime()),namespaceId);
		if(null == stat){
			stat = new StatActiveUser();
			stat.setCreateTime(getCreateTime());
			stat.setNamespaceId(namespaceId);
			stat.setStatDate(new java.sql.Date(statDate.getTime()));
			//总人数
			int totalCount = this.userProvider.countUserByNamespaceId(namespaceId, null);
			stat.setTotalCount(totalCount);
			
			//活跃人数计算
			String activeCountInterval = this.configurationProvider.getValue(namespaceId,"active.count", "0-1");
			String[] arg = activeCountInterval.split("-");
			int minIntRate = Integer.valueOf(arg[0]);
			int minInt = totalCount*minIntRate/100;
			int maxIntRate = Integer.valueOf(arg[1]);
			int maxInt = totalCount*maxIntRate/100;
			int activeCount = (int)(minInt+Math.random()*(maxInt-minInt+1));
			stat.setActiveCount(activeCount);
			
			this.userActivityProvider.createStatActiveUser(stat);
		}
	}

	@Override
	public String getBizUrl() {
		final String SIGN_SUFFIX = "#sign_suffix";
		
		String bizUrl = getBusinessUrl();
		
		String preBizUrl = "";
		String afterBizUrl = "";
		if(bizUrl.indexOf(SIGN_SUFFIX) > 0) {
			preBizUrl = bizUrl.substring(0, bizUrl.indexOf(SIGN_SUFFIX));
			afterBizUrl = bizUrl.substring(bizUrl.indexOf(SIGN_SUFFIX), bizUrl.length());
		} else {
			preBizUrl = bizUrl;
			afterBizUrl = "";
		}
		
		String oauthServer = getoAuthServer();
		String realm = getBusinessRealm();
		String bizVersionUrl = getVersionUrl(realm);
		String format = "%s&oAuthServer=%s&realm=%s&target=%s";
		
		String url = String.format(format, preBizUrl, URLEncoder.encode(oauthServer), realm, URLEncoder.encode(bizVersionUrl));
		return url + afterBizUrl;
	}

	private String getoAuthServer() {
		String oauthServer = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.OAUTH_SERVER, "");
		if(oauthServer.length() == 0) {
            LOGGER.error("Invalid oauth server path, oauthServer=" + oauthServer);
            return null;
        } else {
            return oauthServer;
        }
	}

	private String getVersionUrl(String realm) {
		VersionRequestCommand cmd = new VersionRequestCommand();
		cmd.setRealm(realm);
		VersionUrlResponse versionUrl = versionService.getVersionUrls(cmd);
		if(versionUrl != null) {
			return versionUrl.getDownloadUrl();
		}
		return null;
	}

	private String getHomeUrl() {
		String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.HOME_URL, "");
		if(homeUrl.length() == 0) {
            LOGGER.error("Invalid home url path, homeUrl=" + homeUrl);
            return null;
        } else {
            return homeUrl;
        }
	}
	
	private String getShakeOpenDoor() {
		String shakeOpenDoor = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.SHAKE_OPEN_DOOR, "");
		if(shakeOpenDoor.length() == 0) {
            LOGGER.error("Invalid shake open door configuration, shakeOpenDoor=" + shakeOpenDoor);
            return null;
        } else {
            return shakeOpenDoor;
        }
	}
	
	@Override
	public void updateShakeOpenDoor(Byte shakeOpenDoor) {
		String namespaceOpen = getShakeOpenDoor();
		User user = UserContext.current().getUser();
		
		if("1".equals(namespaceOpen)){
			updateUserProfile(user.getId(), UserProfileContstant.SHAKE_OPEN_DOOR, shakeOpenDoor.toString());
		}else{
			LOGGER.error("namespace configuration is false, then user configuration is prohibited");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_FORBIDDEN, "namespace configuration is false, then user configuration is prohibited");
		}
		
	}
}
