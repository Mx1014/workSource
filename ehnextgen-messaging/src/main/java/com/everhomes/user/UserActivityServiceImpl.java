package com.everhomes.user;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
















































import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;





































import org.springframework.util.concurrent.ListenableFutureCallback;

import com.everhomes.activity.Activity;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.activity.ActivityStatus;
import com.everhomes.activity.CheckInStatus;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.business.Business;
import com.everhomes.business.BusinessProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.promotion.BizHttpRestCallProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.business.BusinessServiceErrorCode;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.forum.ForumConstants;
import com.everhomes.rest.forum.ForumServiceErrorCode;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostFavoriteFlag;
import com.everhomes.rest.forum.PostPrivacy;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.openapi.GetUserServiceAddressCommand;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.user.AddUserFavoriteCommand;
import com.everhomes.rest.user.BizOrderHolder;
import com.everhomes.rest.user.CancelUserFavoriteCommand;
import com.everhomes.rest.user.CommunityStatusResponse;
import com.everhomes.rest.user.Contact;
import com.everhomes.rest.user.ContactDTO;
import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.InvitationCommandResponse;
import com.everhomes.rest.user.InvitationDTO;
import com.everhomes.rest.user.ListPostResponse;
import com.everhomes.rest.user.ListPostedActivityByOwnerIdCommand;
import com.everhomes.rest.user.ListPostedTopicByOwnerIdCommand;
import com.everhomes.rest.user.ListSignupActivitiesCommand;
import com.everhomes.rest.user.ListTreasureResponse;
import com.everhomes.rest.user.ListUserFavoriteActivityCommand;
import com.everhomes.rest.user.ListUserFavoriteTopicCommand;
import com.everhomes.rest.user.SyncActivityCommand;
import com.everhomes.rest.user.SyncBehaviorCommand;
import com.everhomes.rest.user.SyncInsAppsCommand;
import com.everhomes.rest.user.SyncLocationCommand;
import com.everhomes.rest.user.SyncUserContactCommand;
import com.everhomes.rest.user.UserFavoriteDTO;
import com.everhomes.rest.user.UserFavoriteTargetType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.videoconf.BizConfHolder;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserProfilesDao;
import com.everhomes.server.schema.tables.pojos.EhUserProfiles;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StatusChecker;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;

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
    	
        UserActivity activity = new UserActivity();
        try {
            BeanUtils.copyProperties(cmd, activity, "activityType", "osType");
        } catch (Exception e) {
        	LOGGER.error("some thing error", e);
        }
        User user = UserContext.current().getUser();
        activity.setCreateTime(getCreateTime());
        activity.setUid((long) -1);
        if (user != null)
            activity.setUid(user.getId());
        activity.setActivityType(ActivityType.fromString(cmd.getActivityType()).getCode());
        activity.setOsType(OSType.fromString(cmd.getOsType()).getCode());
        if (user != null)
        	userActivityProvider.addActivity(activity, user.getId());
        
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
    public void updateFeedback(FeedbackCommand cmd) {
        User user = UserContext.current().getUser();
        Feedback feedback = ConvertHelper.convert(cmd, Feedback.class);
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
        
        if(cmd.getFeedbackType()== null || cmd.getFeedbackType() == 0){
        	NewTopicCommand ntc = new NewTopicCommand();
        	ntc.setForumId(ForumConstants.FEEDBACK_FORUM);
        	ntc.setCreatorTag(EntityType.USER.getCode());
        	ntc.setTargetTag(EntityType.USER.getCode());
        	ntc.setContentCategory(feedback.getContentCategory());
//        	ntc.setActionCategory(actionCategory);
        	ntc.setSubject(feedback.getSubject());
        	if(feedback.getProofResourceUri() == null || "".equals(feedback.getProofResourceUri()))
        		ntc.setContentType(PostContentType.TEXT.getCode());
        	else{
        		ntc.setContentType(PostContentType.IMAGE.getCode());
        	}
        	ntc.setContent(feedback.getContent());
        	ntc.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
        	ntc.setVisibleRegionId(0L);
        	
        	forumService.createTopic(ntc);
        }
    }

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
        
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        
        if(result.size() > pageSize) {
        	locator.setAnchor(result.get(result.size() - 1).getId());
        	result = result.subList(0, result.size() - 1);
        } else {
        	locator.setAnchor(null);
        }
        
        List<Long> ids = result.stream().map(r -> r.getTargetId()).collect(Collectors.toList());
        Collections.sort(ids);
        Collections.reverse(ids);
        List<PostDTO> posts = forumService.getTopicById(ids, cmd.getCommunityId(), false, true);
        
        Long nextPageAnchor = locator.getAnchor();
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
                    	Map<String,Object> data = (Map<String, Object>) holder.getBody();
                    	int orderCount = Double.valueOf(String.valueOf(data.get("orderCount"))).intValue();
                    	response.setOrderCount(orderCount);
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
        
        if(couponCount != null) {
        	rsp.setCouponCount(NumberUtils.toInt(couponCount.getItemValue(), 0));
        }
        rsp.setMyOrderUrl(getMyOrderUrl());
        rsp.setPointRuleUrl(getPointRuleUrl());
        rsp.setMyCoupon(getMyCoupon());
        
        rsp.setOrderCount(0);
        bizFindOrderCountByUserId(user.getId(), rsp);
        return rsp;
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
            return null;
        }
        
        if(result.size() > pageSize) {
        	locator.setAnchor(result.get(result.size() - 1).getId());
        	result = result.subList(0, result.size() - 1);
        } else {
        	locator.setAnchor(null);
        }
        
        Long nextPageAnchor = locator.getAnchor();
        List<Long> ids = result.stream().map(r -> r.getTargetId()).collect(Collectors.toList());
        Collections.sort(ids);
        Collections.reverse(ids);
        List<ActivityDTO> activities = new ArrayList<ActivityDTO>();
        for(Long postId : ids) {
        	Activity activity =  activityProivider.findSnapshotByPostId(postId);
        	if(activity != null && activity.getStatus() == PostStatus.ACTIVE.getCode()) {
        		ActivityDTO dto = convertToActivityDto(activity, uid);
        		
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
        
        List<ActivityRoster> result = activityProivider.findRostersByUid(uid, locator, pageSize + 1);
        
        if (CollectionUtils.isEmpty(result)) {
            return null;
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
        
        ActivityRoster roster = activityProivider.findRosterByUidAndActivityId(activity.getId(), uid);
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
}
