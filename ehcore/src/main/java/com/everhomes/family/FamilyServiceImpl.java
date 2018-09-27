// @formatter:off
package com.everhomes.family;

import ch.hsr.geohash.GeoHash;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Role;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pm.OrganizationOwnerAddress;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.point.UserPointService;
import com.everhomes.recommend.RecommendationService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.aclink.DeleteAuthByOwnerCommand;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.AddressServiceErrorCode;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.QuestionMetaActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.family.*;
import com.everhomes.rest.family.admin.ListAllFamilyMembersAdminCommand;
import com.everhomes.rest.family.admin.ListWaitApproveFamilyAdminCommand;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.group.GroupPrivacy;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressAuthType;
import com.everhomes.rest.organization.pm.OrganizationOwnerBehaviorType;
import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.point.PointType;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.*;

import org.apache.lucene.spatial.DistanceUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Family inherits from Group for family member management. GroupDiscriminator.FAMILY
 * distinguishes it from other group objects 
 * 
 */
@Component
public class FamilyServiceImpl implements FamilyService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyServiceImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private RegionProvider regionProvider;
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private UserPointService userPointService;
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired 
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private UserGroupHistoryProvider userGroupHistoryProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private GroupMemberLogProvider groupMemberLogProvider;

    @Autowired
    private DoorAccessService doorAccessService;
    
    @Override
    public Family getOrCreatefamily(Address address, User u)      {
    	if(null == u){
    		u = UserContext.current().getUser();
    	}
    	final User user = u;
        long uid = user.getId();
        Community community = this.communityProvider.findCommunityById(address.getCommunityId());
        Region region;
        if(community.getAreaId() != null)
            region = this.regionProvider.findRegionById(community.getAreaId());
        else
            region = this.regionProvider.findRegionById(address.getCityId());
        assert(region != null);

        Tuple<Family, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_FAMILY.getCode()).enter(()-> {
            long lqStartTime = System.currentTimeMillis(); 
            Family family = this.familyProvider.findFamilyByAddressId(address.getId());
            long lqEndTime = System.currentTimeMillis();
            LOGGER.info("find family in the lock,elapse=" + (lqEndTime - lqStartTime));
            
            long lcStartTime = System.currentTimeMillis(); 
            if(null == address.getMemberStatus()){
            	address.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
            }
            if(family == null) {
                family = this.dbProvider.execute((TransactionStatus status) -> {
                    Family f = new Family();
                    f.setName(address.getAddress());

                    //生成的域空间原来是使用Namespace.DEFAULT_NAMESPACE，但是这查询的时候根据当前域空间查询查不出来，此处使用当前域空间  edit by yanjun 20170731
                    Integer namespaceId = UserContext.getCurrentNamespaceId();
                    if(namespaceId == null){
                        namespaceId = Namespace.DEFAULT_NAMESPACE;
                    }
                    f.setNamespaceId(namespaceId);
                    f.setDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    f.setAddressId(address.getId());
                    f.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
                    f.setCreatorUid(uid);
                    f.setMemberCount(0L);   // initialize it to 0
                    f.setCommunityId(address.getCommunityId());
                    f.setCityId(address.getCityId());
                    
                    this.groupProvider.createGroup(f);
                    
                    GroupMember m = new GroupMember();
                    m.setGroupId(f.getId());
                    m.setMemberNickName(user.getNickName() == null ? user.getAccountName() : user.getNickName());
                    m.setMemberType(EntityType.USER.getCode());
                    m.setMemberId(uid);
                    m.setMemberAvatar(user.getAvatar());
                    m.setMemberRole(Role.ResourceCreator);
                    
                    m.setMemberStatus(address.getMemberStatus());
                    m.setCreatorUid(uid);
                    m.setInviteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    this.groupProvider.createGroupMember(m);

                    UserGroup userGroup = new UserGroup();
                    userGroup.setOwnerUid(uid);
                    userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    userGroup.setGroupId(f.getId());
                    userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
                    userGroup.setRegionScopeId(community.getId());
                    userGroup.setMemberRole(Role.ResourceCreator);
                    userGroup.setMemberStatus(address.getMemberStatus());
                    this.userProvider.createUserGroup(userGroup);
                    
                    sendFamilyNotificationForReqJoinFamily(address,f,m);
                    
                    return f;
                });
            } else {
                final Family f = family;
                GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(f.getId(), EntityType.USER.getCode(), uid);
                if(member != null){
                    //说明该用户已经存在，但是目前该用户是否已经被批准加入家庭，还不确定，需要判断
                    if(member.getMemberStatus() == GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()){
                        //说明该成员现在已经申请加入该家庭了，等待管理员的批准，所以我们需要给前端提示信息
                        throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_FAMILY_WAITING_FOR_APPROVAL,
                                "user waiting for approval");
                    }else if(member.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()){
                        //说明该用户已经被批准加入该家庭了，所以我们需要给出前端提示信息
                        throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE,FamilyServiceErrorCode.ERROR_USER_FAMILY_EXIST,"user has join in family");
                    }
                }
                // add transaction
                this.dbProvider.execute((TransactionStatus status) -> {
                    GroupMember m = new GroupMember();
                    m.setGroupId(f.getId());
                    m.setMemberType(EntityType.USER.getCode());
                    m.setMemberId(uid);
                    m.setMemberNickName(user.getNickName() == null ? user.getAccountName() : user.getNickName());
                    m.setMemberAvatar(user.getAvatar());
                    m.setMemberRole(Role.ResourceUser);
//                    m.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                    m.setMemberStatus(address.getMemberStatus());
                    m.setCreatorUid(uid);
                    m.setInviteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    this.groupProvider.createGroupMember(m);
                    
                    UserGroup userGroup = new UserGroup();
                    userGroup.setOwnerUid(uid);
                    userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    userGroup.setGroupId(f.getId());
                    userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
                    userGroup.setRegionScopeId(community.getId());
                    userGroup.setMemberRole(Role.ResourceUser);
//                    userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                    userGroup.setMemberStatus(address.getMemberStatus());
                    this.userProvider.createUserGroup(userGroup);
                    
                    sendFamilyNotificationForReqJoinFamily(address,f,m);
                    return f;
                });
               
            }
            long lcEndTime = System.currentTimeMillis();
            LOGGER.info("create family in the lock,elapse=" + (lcEndTime - lcStartTime));
            return family;
        });
        
        if(result.second()){
            if(result.first() != null)
                return result.first();
            else
              throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_FAMILY_EXIST, 
              "User has in family,please don't join it again.");
        }
        
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");

    }
    
    private void sendFamilyNotificationForReqJoinFamily(Address address, Group group, GroupMember member) {
        // send notification to the applicant
        try {
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,0L,0L);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification to who is add address
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_REQ_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            
            //sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_REQ_FOR_OPERATOR;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            QuestionMetaObject metaObject = createGroupQuestionMetaObject(group, member, null);
            metaObject.setRequestInfo(notifyTextForOthers);

            List<Long> includeList = getFamilyIncludeList(group.getId(), null, member.getMemberId());
            if (includeList != null && includeList.size() > 0) {
                metaObject.setRequestInfo(notifyTextForOthers);
                QuestionMetaActionData actionData = new QuestionMetaActionData();
                actionData.setMetaObject(metaObject);

                String routerUri = RouterBuilder.build(Router.FAMILY_MEMBER_APPLY, actionData);
                sendRouterFamilyNotificationUseSystemUser(includeList,null,notifyTextForOthers, routerUri);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    private String fullAddress(long communityId , String buildingName, String apartmentName){
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null){
            LOGGER.error("Community is not found.communityId=" + communityId);
            return null;
        }
        
        return FamilyUtils.joinDisplayName(community.getCityName(),community.getAreaName(), community.getName(), buildingName, apartmentName);
        
    }
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        // messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
     //TODO when user group session?
    private void sendFamilyNotificationGroupSession(Long familyId,List<Long> includeList, List<Long> excludeList, 
            String message, MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        if(message != null && message.length() != 0) {
            String channelType = MessageChannelType.GROUP.getCode();
            String channelToken = String.valueOf(familyId);
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(channelType, channelToken));
            messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
            if(includeList != null && includeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.INCLUDE, StringHelper.toJsonString(includeList));
            }
            if(excludeList != null && excludeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.EXCLUDE, StringHelper.toJsonString(excludeList));
            }
            if(metaObjectType != null && metaObject != null) {
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, metaObjectType.getCode());
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
            }
            messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType, 
                channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());

            LOGGER.info("Send family notification,familyId=" + familyId + ",includeList=" + includeList + ",exculdeList=" + excludeList);
        }
    }
    
    private void sendFamilyNotification(Long familyId,List<Long> includeList, List<Long> excludeList,
            String message, MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        //User session
        Map<String, String> meta = null;
        if(metaObjectType != null && metaObject != null) {
            meta = new HashMap<String, String>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, metaObjectType.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        }
        
        if(includeList != null && includeList.size() > 0) {
            for(Long userId : includeList) {
                sendMessageToUser(userId, message, meta);
            }
            return;
        }
        
        Map<Long, Long> exclude = new HashMap<Long, Long>();
        if(excludeList != null) {
            for(Long userId : excludeList) {
                exclude.put(userId, 1l);
            }
        }
        
        ListingLocator locator = new ListingLocator(familyId);
        List<GroupMember> members = this.groupProvider.listGroupMembers(locator, 1000);
        for(GroupMember gm : members) {
            if(exclude.get(gm.getMemberId()) == null && gm.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()) {
                sendMessageToUser(gm.getMemberId(), message, meta);
            }
        }
    }

    private void sendFamilyNotificationUseSystemUser(List<Long> includeList, List<Long> excludeList, String message, Map<String, String> meta) {
        if(message == null || message.isEmpty()) {
            return;
        }

        if(includeList != null && includeList.size() > 0) {
            if (excludeList != null && excludeList.size() > 0) {
                includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
            }

            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_FAMILY);

          //add by huanlm 20180628 .fix 缺陷 #25651 ,the meta loss metaObjectType
            if(meta == null) {
            	meta = new HashMap<>();
            }
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.FAMILY_AGREE_TO_JOIN.getCode());
            //update by huanlm 20180628
            messageDto.setMeta(meta);

                       
            includeList.stream().distinct().forEach(targetId -> {
                messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                        AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                        messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            });
        }
    }

    private void sendRouterFamilyNotificationUseSystemUser(List<Long> includeList, List<Long> excludeList, String message, String routerUri) {
        if(message == null || message.isEmpty()) {
            return;
        }

        if(includeList != null && includeList.size() > 0) {
            if (excludeList != null && excludeList.size() > 0) {
                includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
            }

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("sendRouterFamilyNotificationUseSystemUser includeList {}", includeList);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("sendRouterFamilyNotificationUseSystemUser excludeList {}", excludeList);

            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_FAMILY);

            RouterMetaObject mo = new RouterMetaObject();
            mo.setUrl(routerUri);
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
            messageDto.setMeta(meta);

            includeList.stream().distinct().forEach(targetId -> {
                messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                        AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                        messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            });
        }
    }

 
    @Override
    public void joinFamily(JoinFamilyCommand cmd) {
    	User user = UserContext.current().getUser();
    	
    	checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
    	long userId = user.getId();
    	long familyId = cmd.getId();
    	Group group = this.groupProvider.findGroupById(familyId);
        if(!group.getNamespaceId().equals(UserContext.getCurrentNamespaceId()))
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_FAMILY_NOT_EXIST,
                    "Invalid familyId parameter");
        boolean flag = this.dbProvider.execute((TransactionStatus status) -> {
    		
    		GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), userId);
    		if(m != null)
    			throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_FAMILY_EXIST, 
    					"user has joined to the family");
    		m = new GroupMember();
    		m.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
    		m.setGroupId(familyId);
    		m.setMemberId(userId);
    		m.setMemberNickName(user.getNickName());
    		m.setMemberAvatar(user.getAvatar());
    		m.setMemberRole(Role.ResourceUser);
    		m.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
    		m.setMemberType(EntityType.USER.getCode());
    		this.groupProvider.createGroupMember(m);
    		
			UserGroup userGroup = new UserGroup();
		    userGroup.setOwnerUid(userId);
		    userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
		    userGroup.setGroupId(familyId);
		    userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
		    userGroup.setRegionScopeId(group.getIntegralTag2());
		    userGroup.setMemberRole(Role.ResourceUser);
		    userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
		    this.userProvider.createUserGroup(userGroup);
		    
		    sendFamilyNotificationForReqJoinFamily(null, group, m);
		    
    		return true;
    	});
        LOGGER.info("User join in family success.userId=" + userId + ",familyId=" + familyId);
        if(flag)
            return;
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
    }

    @Override
    public FamilyDTO getOwningFamilyById(GetOwningFamilyByIdCommand cmd) {
        User user = UserContext.current().getUser();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        
        Long familyId = cmd.getId();
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), user.getId());
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(user.getId(), familyId);
        if(m == null || userGroup == null)
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family");
        
        final FamilyDTO[] results = new FamilyDTO[1];
        List<Long> familyIds = new ArrayList<Long>();
        familyIds.add(familyId);
        
        List<FamilyDTO> dtos = getUserOwningFamiliesByIds(familyIds,user.getId());
        if(dtos == null || dtos.isEmpty()){
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_FAMILY_NOT_EXIST, 
                    "Family is not exists.familyId=" + familyId);
        }
        if(dtos != null && !dtos.isEmpty())
            results[0] = dtos.get(0);
        return results[0];
    }
    
    private List<FamilyDTO> getUserOwningFamiliesByIds(List<Long> familyIds,Long userId){
        if(familyIds == null || familyIds.isEmpty())
            return null;
        List<FamilyDTO> familyList = new ArrayList<FamilyDTO>();
        
        for(Long familyId : familyIds){
            Group group = this.groupProvider.findGroupById(familyId);
            
            if(group == null || !group.getDiscriminator().equals(GroupDiscriminator.FAMILY.getCode())){
                LOGGER.error("Family is not exits.familyId=" + familyId);
                throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_FAMILY_NOT_EXIST, 
                        "Family is not exitsor group is not family with the id.familyId=" + familyId);
            }
            FamilyDTO family = ConvertHelper.convert(group,FamilyDTO.class);
            family.setAvatarUrl((parserUri(group.getAvatar(),EntityType.FAMILY.getCode(),group.getCreatorUid())));
            family.setAvatarUri(group.getAvatar());
            family.setAddressId(group.getIntegralTag1());
            long communityId = group.getIntegralTag2();
            Community community = this.communityProvider.findCommunityById(communityId);
            if(community != null){
                family.setCommunityId(communityId);
                family.setCommunityName(community.getName());
                family.setCityId(community.getCityId());
                family.setCityName(community.getCityName());
            }
            if(group.getCreatorUid().longValue() == userId.longValue())
                family.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
            
            GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
                    EntityType.USER.getCode(), userId);
            if(member != null)
                family.setMembershipStatus(member.getMemberStatus());
            
            Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
            if(address != null){
                family.setBuildingName(address.getBuildingName());
                family.setApartmentName(address.getApartmentName());
                family.setAddressStatus(address.getStatus());
                String addrStr = FamilyUtils.joinDisplayName(community.getCityName(),community.getAreaName(), community.getName(), 
                                address.getBuildingName(), address.getApartmentName());
                family.setDisplayName(addrStr);
                family.setAddress(addrStr);
            }
            familyList.add(family);
        }
        
        return familyList;
    }

    @Override
    public List<FamilyDTO> getUserOwningFamilies() {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
       
        List<FamilyDTO> families = this.familyProvider.getUserFamiliesByUserId(userId);
        Map<Long, Long> checkList = new HashMap<Long, Long>();
        if(null != families) {
          for(FamilyDTO f : families) {
                checkList.put(f.getAddressId(), 1l);
            }     
        
        }
        
        if(families == null) {
        	families = new ArrayList<FamilyDTO>();
        }
        //Merge histories, add by Janson  update by huangliangming 20180830 后台拒绝后不应该显示出来,所以这张表相当于无用了,不查询.
	       /* List<UserGroupHistory> histories = this.userGroupHistoryProvider.queryUserGroupHistoryByUserId(userId);
	        for(UserGroupHistory o : histories) {
	            if(!checkList.containsKey(o.getAddressId())) {
	                
	                checkList.put(o.getAddressId(), 1l);
	                
	                FamilyDTO family = new FamilyDTO();
	                family.setId(o.getId());
	                family.setMembershipStatus(GroupMemberStatus.INACTIVE.getCode());
	                Community community = this.communityProvider.findCommunityById(o.getCommunityId());
	                if(community != null){
	                    family.setCommunityId(o.getCommunityId());
	                    family.setCommunityName(community.getName());
	                    family.setCityId(community.getCityId());
	                    family.setCityName(community.getCityName()+community.getAreaName());
	                    family.setCommunityType(community.getCommunityType());
	                    family.setDefaultForumId(community.getDefaultForumId());
	                    family.setFeedbackForumId(community.getFeedbackForumId());
	                    }
	                
	                Address address = this.addressProvider.findAddressById(o.getAddressId());
	                if(address != null){
	                    family.setBuildingName(address.getBuildingName());
	                    family.setApartmentName(address.getApartmentName());
	                    family.setAddressStatus(address.getStatus());
	                    String addrStr = FamilyUtils.joinDisplayName(community.getCityName(),community.getAreaName(), community.getName(), 
	                            address.getBuildingName(), address.getApartmentName());
	                    family.setDisplayName(addrStr);
	                    family.setAddress(addrStr);
	                }
	                
	                families.add(family);
	                
	            }
	        }*/
        
        return families;
    }

    @Override
    public void leave(LeaveFamilyCommand cmd, User u) {
    	if(null == u){
    		u = UserContext.current().getUser();
    	}
    	
    	final User user = u;
        long userId = user.getId();
        long familyId = cmd.getId();
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Group group = this.groupProvider.findGroupById(familyId);
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(member == null){
            LOGGER.error("User not join in family.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User not join in family.");
        }
        LOGGER.info("User leave family,userId=" + userId + ",familyId=" + familyId);
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());

        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(userId, group.getId());
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User not in user group.");
        }
        this.familyProvider.leaveFamilyAtAddress(address, userGroup);

        //离开家庭，增加日志记录 add by 梁燕龙 20180920
        leaveGroupMemberLog(member, group);

        // 离开家庭，删除大堂门禁
        DeleteAuthByOwnerCommand deleteAuthByOwnerCommand = new DeleteAuthByOwnerCommand();
        deleteAuthByOwnerCommand.setNamespaceId(user.getNamespaceId());
        deleteAuthByOwnerCommand.setOwnerId(group.getIntegralTag2());
        deleteAuthByOwnerCommand.setOwnerType((byte)0);
        deleteAuthByOwnerCommand.setUserId(userId);
        this.doorAccessService.deleteAuthByOwner(deleteAuthByOwnerCommand);
        setCurrentFamilyAfterApproval(userGroup.getOwnerUid(),0,1);
        
        sendFamilyNotificationForLeaveFamily(address, group, member);
    }
    
    private void sendFamilyNotificationForLeaveFamily(Address address, Group group, GroupMember member) {
        // send notification to the applicant
        try {
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,0,Role.ResourceOperator);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification to who is add address
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_MEMBER_LEAVE_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            //sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_MEMBER_LEAVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            //List<Long> includeList = getFamilyIncludeList(group.getId(), null, member.getMemberId());
            sendGroupNotificationToExcludeUsers(group.getId(),null,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }

    @Override
    public void revokeMember(RevokeMemberCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long memberUid = cmd.getMemberUid();
        Long familyId = cmd.getId();
        
        if(memberUid == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid memberUid parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        
        if(memberUid.longValue() == userId)
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_REVOKE_SELF, 
                    "Invalid memberUid parameter,can not revoke self");
        
        LOGGER.info("User revoked family,operatorId=" + userId + ",memberId=" + memberUid + ",familyId=" + familyId);
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(memberUid, group.getId());
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User not in user group.");
        }
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), memberUid);
        if(member == null){
            LOGGER.error("User not join in family.memberUid=" + memberUid);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_FAMILY_NOT_EXIST, 
                    "User not join in family.");
        }
        cmd.setOperatorRole(cmd.getOperatorRole() == null ? Role.ResourceOperator : cmd.getOperatorRole());
        this.familyProvider.leaveFamilyAtAddress(address, userGroup);
        setCurrentFamilyAfterApproval(userGroup.getOwnerUid(),0,1);
        if(cmd.getOperatorRole() == Role.ResourceOperator)
            sendFamilyNotificationForMemberRevokedFamily(address,group,member,userId);
        
    }
    
    private void sendFamilyNotificationForMemberRevokedFamily(Address address, Group group, 
            GroupMember member, long operatorId) {
        // send notification to the applicant
        try {
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.ResourceOperator);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who revoked
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_MEMBER_REVOKE_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            //sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            //send notification to operator
            code = FamilyNotificationTemplateCode.FAMILY_MEMBER_REVOKE_FOR_OPERATOR;
            String notifyTextForOpeator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendFamilyNotificationToIncludeUser(group.getId(), operatorId, notifyTextForOpeator);
            //sendFamilyNotification(operatorId, notifyTextForOpeator);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_MEMBER_LEAVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            List<Long> includeList = getFamilyIncludeList(group.getId(), operatorId, member.getMemberId());
//            sendFamilyNotification(group.getId(),includeList,null,notifyTextForOthers,null,null);
            sendGroupNotificationToExcludeUsers(group.getId(),operatorId,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }


    @Override
    public void rejectMember(RejectMemberCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long memberUid = cmd.getMemberUid();
        Long familyId = cmd.getId();
        if(memberUid == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid memberUid parameter");
        if(memberUid == userId)
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_REJECT_SELF, 
                    "User can not reject self.");
        LOGGER.info("User rejected family,operatorId=" + userId + ",memberId=" + memberUid + ",familyId=" + familyId);
        Group group = this.groupProvider.findGroupById(familyId);
        cmd.setOperatorRole((cmd.getOperatorRole() == null ? Role.ResourceOperator : cmd.getOperatorRole()));
        //家庭成员拒绝
        if(cmd.getOperatorRole() != Role.SystemAdmin){
            GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                    EntityType.USER.getCode(), userId);
            if(member == null || member.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode()){
                throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_STATUS_INVALID, 
                        "User status is not active,has not privilege.");
            }
        }
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), memberUid);
        if(member == null){
        	LOGGER.error("User not in familly.userId=" + memberUid);
        	return ;
//            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
//                    "User not in familly.");
        }
        if(member.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()){
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_FAMILY_EXIST, 
                    "User has already join in family,fail to reject.");
        }
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(memberUid, group.getId());
        if(userGroup == null) {
            LOGGER.error("User not in user group.userId=" + memberUid);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in familly.");
        }
        
        this.familyProvider.leaveFamilyAtAddress(address, userGroup);
        setCurrentFamilyAfterApproval(userGroup.getOwnerUid(),0,1);
        member.setMemberStatus(GroupMemberStatus.REJECT.getCode());
        addGroupMemberLog(member, group);
        //Create reject history  // update by huangliangming 20180830 后台拒绝后不应该显示出来,所以这张表相当于无用了,不查询.
        /*UserGroupHistory history = new UserGroupHistory();
        history.setGroupId(familyId);
        history.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
        history.setOwnerUid(memberUid);
        history.setAddressId(address.getId());
        history.setCommunityId(group.getIntegralTag2());
        this.userGroupHistoryProvider.createUserGroupHistory(history);*/
        
        if(cmd.getOperatorRole() == Role.ResourceOperator)
            sendFamilyNotificationForMemberRejectFamily(address,group,member,userId);
        else if(cmd.getOperatorRole() == Role.SystemAdmin)
            sendFamilyNotificationForMemberRejectFamilyByAdmin(address,group,member,userId);
    }

    private void addGroupMemberLog(GroupMember member, Group group) {
        GroupMemberLog memberLog = ConvertHelper.convert(member, GroupMemberLog.class);
        memberLog.setNamespaceId(group.getNamespaceId());
        memberLog.setMemberStatus(member.getMemberStatus());
        memberLog.setOperatorUid(UserContext.currentUserId());
        memberLog.setApproveTime(DateUtils.currentTimestamp());
        memberLog.setGroupMemberId(member.getId());
        memberLog.setCreatorUid(UserContext.currentUserId());
        memberLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        memberLog.setCommunityId(group.getFamilyCommunityId());
        memberLog.setAddressId(group.getFamilyAddressId());
        groupMemberLogProvider.createGroupMemberLog(memberLog);
    }

    private void leaveGroupMemberLog(GroupMember member, Group group) {
        GroupMemberLog memberLog = ConvertHelper.convert(member, GroupMemberLog.class);
        memberLog.setNamespaceId(group.getNamespaceId());
        memberLog.setMemberStatus(GroupMemberStatus.INACTIVE.getCode());
        memberLog.setOperatorUid(UserContext.currentUserId());
        memberLog.setApproveTime(DateUtils.currentTimestamp());
        memberLog.setGroupMemberId(member.getId());
        memberLog.setCreatorUid(UserContext.currentUserId());
        memberLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        memberLog.setCommunityId(group.getFamilyCommunityId());
        memberLog.setAddressId(group.getFamilyAddressId());
        groupMemberLogProvider.createGroupMemberLog(memberLog);
    }
    
    private void sendFamilyNotificationForMemberRejectFamilyByAdmin(Address address, Group group, GroupMember member,long operatorId) {
        // send notification to the applicant
        try {
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.SystemAdmin);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who revoked
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
           // sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            //sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            //给客户端发一条通知 ,MetaObjectType由于没设置拒绝类型,但同意类型应该也可以用,因为都只是触发刷新地址而已
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.FAMILY_AGREE_TO_JOIN.getCode());
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant, meta);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            List<Long> includeList = getFamilyIncludeList(group.getId(), null, member.getMemberId());
//            sendFamilyNotification(group.getId(),includeList,null,notifyTextForOthers,null,null);
            sendGroupNotificationToExcludeUsers(group.getId(),null,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    private void sendFamilyNotificationForMemberRejectFamily(Address address, Group group, GroupMember member, long operatorId) {
        // send notification to the applicant
        try {
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.ResourceOperator);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who revoked
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            //sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            //给客户端发一条通知 ,MetaObjectType由于没设置拒绝类型,但同意类型应该也可以用,因为都只是触发刷新地址而已
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.FAMILY_AGREE_TO_JOIN.getCode());
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant, meta);
            
            //send notification to operator
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_OPERATOR;
            String notifyTextForOpeator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            sendFamilyNotificationToIncludeUser(group.getId(), operatorId, notifyTextForOpeator);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            List<Long> includeList = getFamilyIncludeList(group.getId(), operatorId, member.getMemberId());
//            sendFamilyNotification(group.getId(),includeList,null,notifyTextForOthers,null,null);
            sendGroupNotificationToExcludeUsers(group.getId(),operatorId,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    @Override
    public void approveMember(ApproveMemberCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long memberUid = cmd.getMemberUid();
        Long familyId = cmd.getId();
        if(memberUid == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid memberUid parameter");
        Group group = this.groupProvider.findGroupById(familyId);
        LOGGER.info("User approved family,operatorId=" + userId + ",memberId=" + memberUid + ",familyId=" + familyId);
        long startTime = System.currentTimeMillis();
        cmd.setOperatorRole(cmd.getOperatorRole() == null ? Role.ResourceOperator : cmd.getOperatorRole());
        if(cmd.getOperatorRole() != Role.SystemAdmin){
            GroupMember currMember = this.groupProvider.findGroupMemberByMemberInfo(familyId,
                    EntityType.USER.getCode(), userId);
            if(currMember == null || currMember.getMemberStatus().byteValue() != GroupMemberStatus.ACTIVE.getCode()){
                LOGGER.error("User permission denied, userId=" + userId);
                throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_STATUS_INVALID,
                        "User permission denied.");
            }
        }
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), memberUid);
        if(member == null){
            LOGGER.error("Invalid memberUid parameter,user has not apply join in family.memberUid=" + memberUid);
            return ;
//            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
//                    "User has not apply join in family, or other approve the user.");
        }



        Tuple<Boolean,Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.LEAVE_FAMILY.getCode()).enter(()-> {
            this.dbProvider.execute((TransactionStatus status) -> {
                
                member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                member.setOperatorUid(UserContext.current().getUser().getId());
                this.groupProvider.updateGroupMember(member);
                
                List<UserGroup> list = this.userProvider.listUserGroups(memberUid, GroupDiscriminator.FAMILY.getCode());
                list = list.stream().filter((userGroup) -> {
                    return userGroup.getGroupId().longValue() == group.getId().longValue();
                    
                }).collect(Collectors.toList());
                if(list != null && !list.isEmpty()) {
                    UserGroup userGroup = list.get(0);
                    userGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                    this.userProvider.updateUserGroup(userGroup);
                }
                group.setMemberCount(group.getMemberCount() + 1);
                groupProvider.updateGroup(group);

                addGroupMemberLog(member, group);// add by xq.tian  2017/07/12
                return true;
            });
           return true;
        });
       
        if(tuple.second()){
            // 认证organizationOwner
            autoApproveOrganizationOwner(cmd.getAddressId(), user.getNamespaceId(), memberUid);

            if(cmd.getOperatorRole().byteValue() == Role.ResourceOperator)
                sendFamilyNotificationForApproveMember(null,group,member,userId);
            else if(cmd.getOperatorRole().byteValue() == Role.SystemAdmin)
                sendFamilyNotificationForApproveMemberByAdmin(null,group,member,userId);
            //update current family
            setCurrentFamilyAfterApproval(memberUid,familyId,0);
            
            //通知小区用户(通知通讯录好友)有新用户入住
            recommendationService.communityNotify(memberUid, group.getIntegralTag1() , group.getIntegralTag2());
            //积分
            AddUserPointCommand pointCmd=new AddUserPointCommand(user.getId(), PointType.ADDRESS_APPROVAL.name(), 
                    userPointService.getItemPoint(PointType.ADDRESS_APPROVAL), memberUid); 
            userPointService.addPoint(pointCmd);
            long endTime = System.currentTimeMillis();
            LOGGER.info("Approve family elapse=" + (endTime - startTime));
            return;
        }
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
        
        
    }

    //
    // 认证organization owner
    // add by xq.tian   20160922
    //
    private void autoApproveOrganizationOwner(Long addressId, Integer namespaceId, Long memberUid) {
        if (addressId == null) {
            return;
        }
        Address address = addressProvider.findAddressById(addressId);
        if (address == null) {
            return;
        }
        User memberUser = userProvider.findUserById(memberUid);
        UserIdentifier userIdentifier = getMobileOfUserIdentifier(memberUid);
        if (memberUser != null && userIdentifier != null) {
            List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwnersByToken(namespaceId,
                    address.getCommunityId(), userIdentifier.getIdentifierToken());
            if (pmOwners != null && pmOwners.size() > 0) {
                for (CommunityPmOwner owner : pmOwners) {
                    OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                            namespaceId, owner.getId(), addressId);
                    if (ownerAddress != null) {
                        if (ownerAddress.getAuthType() != OrganizationOwnerAddressAuthType.ACTIVE.getCode()) {
                            ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.ACTIVE.getCode());
                            propertyMgrProvider.updateOrganizationOwnerAddress(ownerAddress);
                        }
                    }
                    // 不存在ownerAddress, 创建ownerAddress记录
                    else {
                        propertyMgrService.createOrganizationOwnerAddress(addressId, OrganizationOwnerBehaviorType.IMMIGRATION.getLivingStatus(),
                                memberUser.getNamespaceId(), owner.getId(), OrganizationOwnerAddressAuthType.ACTIVE);
                    }
                }
            }
            // 不存在organizationOwner, 根据用户资料创建organizationOwner记录
            else {
                // 只传递communityId
                memberUser.setCommunityId(address.getCommunityId());
                long ownerId = propertyMgrService.createOrganizationOwnerByUser(memberUser, userIdentifier.getIdentifierToken());
                // 创建ownerAddress
                propertyMgrService.createOrganizationOwnerAddress(addressId, OrganizationOwnerBehaviorType.IMMIGRATION.getLivingStatus(),
                        memberUser.getNamespaceId(), ownerId, OrganizationOwnerAddressAuthType.ACTIVE);
            }
        }
    }

    private void sendFamilyNotificationForApproveMember(Address address, Group group, GroupMember member,long operatorId) {
        // send notification to the applicant
        try {
            
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.ResourceOperator);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who applicant
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_APPROVE_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            //sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);

            //给客户端发一条通知
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.FAMILY_AGREE_TO_JOIN.getCode());
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant, meta);
            
            //send notification to operator
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_APPROVE_FOR_OPERATOR;
            String notifyTextForOpeator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendFamilyNotificationToIncludeUser(group.getId(), operatorId, notifyTextForOpeator);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_APPROVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            List<Long> includeList = getFamilyIncludeList(group.getId(), operatorId, member.getMemberId());
//            sendFamilyNotification(group.getId(),includeList,null,notifyTextForOthers,null,null);
            sendGroupNotificationToExcludeUsers(group.getId(),operatorId,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    private void sendFamilyNotificationForApproveMemberByAdmin(Address address, Group group, GroupMember member,long operatorId) {
        // send notification to the applicant
        try {
            
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.SystemAdmin);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who applicant
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_ADMIN_APPROVE_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            //给用户发一条
            //sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);

            //给客户端发一条通知
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.FAMILY_AGREE_TO_JOIN.getCode());
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant, meta);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_ADMIN_APPROVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            List<Long> includeList = getFamilyIncludeList(group.getId(), null, member.getMemberId());
//            sendFamilyNotification(group.getId(),includeList,null,notifyTextForOthers,null,null);
            sendGroupNotificationToExcludeUsers(group.getId(),null,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    
    private Map<String, Object> bulidMapBeforeSendFamilyNotification(Address address, Group group, 
            GroupMember member,long operatorId, long operatorRole){
        if(address == null){
            address = addressProvider.findAddressById(group.getIntegralTag1());
        }
        String addressStr = fullAddress(address.getCommunityId(),address.getBuildingName(),address.getApartmentName());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("address", addressStr);
        map.put("userName", member.getMemberNickName());
        
        if(operatorId != 0){
            if(operatorRole != Role.SystemAdmin){
                GroupMember operator = this.groupProvider.findGroupMemberByMemberInfo(group.getId(), 
                    EntityType.USER.getCode(), operatorId);
                assert(operator != null);
                map.put("operatorName", operator.getMemberNickName());
            }
            else{ 
                map.put("operatorName", "管理员");
            }
            
        }
       
        return map;
    }

    private void setCurrentFamilyAfterApproval(long userId, long familyId, int status){
        //set with first pass
        //after delete random set one if exists
        List<UserGroup> list = this.userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());
        if(list != null && !list.isEmpty()){
            if(status == 0){
                list = list.stream().filter((UserGroup u) -> {
                   return u.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode();
                }).collect(Collectors.toList());
                if(list.size() == 1){
                    updateUserContext(userId, familyId);
                }
            }
            else {
                updateUserContext(userId, list.get(0).getGroupId());
            }
            
        }else {
            updateUserContext(userId, familyId);
        }
    }
    

    @Override
    public void adminApproveMember(ApproveMemberCommand cmd) {
        cmd.setOperatorRole(Role.SystemAdmin);
        //if address status is unactive,changeEnergyMeter status
        Address address = getAddressByFamilyId(cmd.getId());
        if(address == null){
            LOGGER.error("Address is not exists,find by familyId.familyId=" + cmd.getId());
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST, 
                    "Address is not exists.");
        }
        if(address.getStatus().byteValue() != AddressAdminStatus.ACTIVE.getCode()){
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            this.addressProvider.updateAddress(address);
        }
        approveMember(cmd);
        
       // 用户通过认证家庭事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(cmd.getMemberUid());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EntityType.USER.getCode());
            event.setEntityId(cmd.getMemberUid());
            event.setEventName(SystemEvent.ACCOUNT__FAMILY_AUTH_SUCCESS.dft());
            LOGGER.info("publish event :[{}]",event);
        });
    }

    @Override
    public List<FamilyMemberDTO> listOwningFamilyMembers(ListOwningFamilyMembersCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());

        Long familyId = cmd.getId();
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(member == null){
            LOGGER.error("User not in family.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family.");
        }
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(userId, familyId);
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + userId);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in familly.");
        }
        ListingLocator locator = new ListingLocator();
        locator.setEntityId(familyId);
        
        
        List<GroupMember> groupMemberList = this.groupProvider.queryGroupMembers(locator, Integer.MAX_VALUE, null);
        List<FamilyMemberDTO> results = new ArrayList<FamilyMemberDTO>();
        groupMemberList.stream().forEach((groupMember) -> {
            if(groupMember.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()){
                FamilyMemberDTO f = new FamilyMemberDTO();
                f.setFamilyId(groupMember.getGroupId());
                f.setId(groupMember.getId());
                f.setMemberUid(groupMember.getMemberId());
//                f.setMemberName(groupMember.getMemberNickName());
//                f.setMemberAvatarUrl((parserUri(groupMember.getMemberAvatar(),EntityType.USER.getCode(),groupMember.getCreatorUid())));
//                f.setMemberAvatarUri(groupMember.getMemberAvatar());
                //UserInfo userInfo = this.userService.getUserSnapshotInfo(groupMember.getMemberId());
                UserInfo userInfo = this.userService.getUserInfo(groupMember.getMemberId());
                if(userInfo != null){
                	//产品要求家庭昵称和个人昵称一致
                	f.setMemberName(userInfo.getNickName());
                	f.setMemberAvatarUrl(userInfo.getAvatarUrl());
                    f.setMemberAvatarUri(userInfo.getAvatarUri());
                    f.setBirthday(userInfo.getBirthday());
                    f.setGender(userInfo.getGender());
                    f.setStatusLine(userInfo.getStatusLine());
                    f.setOccupation(userInfo.getOccupation());
                    if(userInfo.getPhones() != null && !userInfo.getPhones().isEmpty())
                        f.setCellPhone(userInfo.getPhones().get(0));
                }
                results.add(f);
            }
        });
        return results;
    }
    
    private UserIdentifier getMobileOfUserIdentifier(long userId){
        List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(userId);
        userIdentifiers.stream().filter((u) ->{
            return u.getIdentifierType().byteValue() == IdentifierType.MOBILE.getCode();
        }).collect(Collectors.toList());
        if(userIdentifiers != null && !userIdentifiers.isEmpty())
            return userIdentifiers.get(0);
        return null;

    }

    @Override
    public void setCurrentFamily(SetCurrentFamilyCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        if(cmd.getId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        updateUserContext(userId,cmd.getId());
    }
    
    private void updateUserContext(long userId, long familyId){
        
        User u = this.userProvider.findUserById(userId);
        if(familyId != 0){
            Address address = getAddressByFamilyId(familyId);
            if(address != null){
                u.setAddressId(address.getId());
                u.setAddress(address.getAddress());
                u.setCommunityId(address.getCommunityId());

                userService.updateUserCurrentCommunityToProfile(userId, address.getCommunityId(), u.getNamespaceId());
                
                long timestemp = DateHelper.currentGMTTime().getTime();
                String key = UserCurrentEntityType.FAMILY.getUserProfileKey();
                userActivityProvider.updateUserCurrentEntityProfile(userId, key, familyId, timestemp, u.getNamespaceId());
            }
        }
        else {
            u.setAddressId(0L);
            u.setAddress(null);
        }
       
        this.userProvider.updateUser(u);
    }
    
    private Address getAddressByFamilyId(long familyId){
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        return this.addressProvider.findAddressById(group.getIntegralTag1());
        
    }

    @Override
    public void updateFamilyInfo(UpdateFamilyInfoCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found,familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        }
        String familyAvatar = cmd.getFamilyAvatarUri();
        if(!StringUtils.isEmpty(familyAvatar)){
            group.setAvatar(familyAvatar);
        }
        String familyDescription = cmd.getFamilyDescription();
        if(!StringUtils.isEmpty(familyDescription)){
            group.setDescription(familyDescription);
        }
        String familyName = cmd.getFamilyName();
        if(!StringUtils.isEmpty(familyName)){
            group.setName(familyName);
        }
        
        this.groupProvider.updateGroup(group);
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(member == null)
            return;
        String memberAvatar = cmd.getMemberAvatarUri();
        if(!StringUtils.isEmpty(memberAvatar)){
            member.setMemberAvatar(memberAvatar);
        }
        String memberNickName = cmd.getMemberNickName();
        if(!StringUtils.isEmpty(memberNickName)){
            member.setMemberNickName(memberNickName);
        }
        String proofResourceUri = cmd.getProofResourceUri();
        if(!StringUtils.isEmpty(proofResourceUri)){
            member.setProofResourceUri(proofResourceUri);
        }
        this.groupProvider.updateGroupMember(member);
        
        //更新之后，发送命令
        //Add by Janson
//        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
//        if(null == address) {
//            return;
//        }
//        
//        Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,0L,0L);
//        
//        String scope = FamilyNotificationTemplateCode.SCOPE;
//        int code = FamilyNotificationTemplateCode.FAMILY_MEMBER_QUICK_APPLICANT;
//        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), map, "");
//        sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
    }

    @Override
    public ListFamilyRequestsCommandResponse listFamilyRequests(ListFamilyRequestsCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        Group group = this.groupProvider.findGroupById(familyId);
        
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(m == null || m.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode()){
            LOGGER.error("User not in family or member status is not active");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "member status is not active");
        }
        
        List<FamilyMembershipRequestDTO> results = new ArrayList<FamilyMembershipRequestDTO>();
        if(cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(group.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<GroupMember> listGroupMembers = this.familyProvider.listFamilyMembers(locator, pageSize + 1,
                (loc, query) -> {
                    Condition c1 = Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(familyId);
                    c1 = c1.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));

                    Condition c2 = Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(userId);
                    c2 = c2.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode()));
                    
                    query.addConditions(c1.or(c2));
                    return query;
                });
        
        Long nextPageAnchor = null;
        if(listGroupMembers != null && listGroupMembers.size() > pageSize) {
            listGroupMembers.remove(listGroupMembers.size() - 1);
            nextPageAnchor = listGroupMembers.get(listGroupMembers.size() -1).getId();
        }
        
        listGroupMembers.forEach((r) ->{
            FamilyMembershipRequestDTO member = new FamilyMembershipRequestDTO();
            member.setFamilyId(familyId);
            member.setFamilyAvatarUrl(parserUri(group.getAvatar(),EntityType.FAMILY.getCode(),group.getCreatorUid()));
            member.setFamilyAvatarUri(group.getAvatar());
            member.setFamilyName(group.getName());
            member.setRequestorUid(r.getMemberId());
            member.setRequestorAvatar(parserUri(r.getMemberAvatar(),EntityType.USER.getCode(),r.getCreatorUid()));
            member.setRequestorComment(r.getRequestorComment());
            member.setRequestingTime(r.getCreateTime().toString());
            
            results.add(member);
        });
        ListFamilyRequestsCommandResponse response = new ListFamilyRequestsCommandResponse();
        response.setNextPageAnchor(nextPageAnchor);
        response.setRequests(results);
        return response;
    }

    @Override
    public ListNeighborUsersCommandResponse listNeighborUsers(ListNeighborUsersCommand cmd) {
        User user = UserContext.current().getUser();
        
        if(null == cmd.getIsPinyin()){
        	cmd.setIsPinyin(0);
        }
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        Group group = this.groupProvider.findGroupById(familyId);
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), user.getId());
        
        if(member == null || member.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode())
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family or user status is not active.");
        
        List<Family> familyList = new ArrayList<Family>();
        long communityId = group.getIntegralTag2();
        List<NeighborUserDetailDTO> userDetailList = new ArrayList<NeighborUserDetailDTO>();
        Address myaddress = this.addressProvider.findAddressById(group.getIntegralTag1());
        
        long startTime = System.currentTimeMillis();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select().from(Tables.EH_GROUPS)
                        .where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
                        .fetch().map( (r) ->{
                            //排除自己家庭
                            if(!r.getValue(Tables.EH_GROUPS.INTEGRAL_TAG1).equals(group.getIntegralTag1()))
                                familyList.add(ConvertHelper.convert(r,Family.class));
                            return null;
                        });
                   
                    return true;
                });
        List<Long> neighborUserIds = new ArrayList<Long>();

        familyList.stream().forEach((f) ->{
            if(f == null) return;
            
            Address address = this.addressProvider.findAddressById(f.getAddressId());
            if(address != null){
                
                    List<GroupMember> members = this.groupProvider.findGroupMemberByGroupId(f.getId());
                if(members != null && !members.isEmpty()){
                    for(GroupMember m : members){
                        if(m.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()
                                && m.getMemberType().equals(EntityType.USER.getCode())
                                        && m.getMemberId().longValue() != user.getId().longValue()){
                            //去重
                            if(neighborUserIds.contains(m.getMemberId())) 
                                continue;
                            neighborUserIds.add(m.getMemberId());
                            NeighborUserDetailDTO n = new NeighborUserDetailDTO();
                            User u = this.userProvider.findUserById(m.getMemberId());
                            n.setUserId(u.getId());
                            n.setUserName(u.getNickName());
                            n.setUserAvatarUrl(parserUri(u.getAvatar(),EntityType.USER.getCode(),u.getId()));
                            n.setUserAvatarUri(u.getAvatar());
                            n.setUserStatusLine(u.getStatusLine());
                            n.setOccupation(u.getOccupation());
                            n.setBuildingName(address.getBuildingName());
                            n.setApartmentFloor(address.getApartmentFloor());
                            userDetailList.add(n);
                        }
                    }
                }
            }
            
        });
        
        Long pageOffset = cmd.getPageOffset();
        pageOffset = pageOffset == null ? 1 : pageOffset;
        List<NeighborUserDTO> results = processNeighborUserInfo(userDetailList,myaddress,pageOffset);
        
        if(results != null && results.size() > 0) {
            if(cmd.getIsPinyin().equals(1)){
            	results = this.convertPinyin(results);
            	results = results.stream().map(r->{
            		r.setInitial(r.getInitial().replace("~", "#"));
            		return r;
            	}).collect(Collectors.toList());
            }
        }
        
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query neighbor user of community,elapse=" + (endTime - startTime));
        ListNeighborUsersCommandResponse response = new ListNeighborUsersCommandResponse();
        response.setNeighborUserList(results);
        response.setNeighborCount(results == null ? null : results.size());
        return response;
    }

    private List<NeighborUserDTO> convertPinyin(List<NeighborUserDTO> neighborUserDTOs){
		
    	neighborUserDTOs = neighborUserDTOs.stream().map((c) ->{
    		String pinyin = PinYinHelper.getPinYin(c.getUserName());
			c.setFullInitial(PinYinHelper.getFullCapitalInitial(pinyin));
			c.setFullPinyin(pinyin.replaceAll(" ", ""));
			c.setInitial(PinYinHelper.getCapitalInitial(c.getFullPinyin()));
			return c;
		}).collect(Collectors.toList());
		
		Collections.sort(neighborUserDTOs);
		
		return neighborUserDTOs;
	}
    
    private List<NeighborUserDTO> processNeighborUserInfo(List<NeighborUserDetailDTO> userDetailList,
            Address myaddress,long pageOffset) {
        
        if(userDetailList == null || userDetailList.isEmpty())
            return null;
        long startTime = System.currentTimeMillis();
        List<NeighborUserDTO> results = new ArrayList<NeighborUserDTO>();
        for(NeighborUserDetailDTO dto : userDetailList){
            NeighborUserDTO user = ConvertHelper.convert(dto, NeighborUserDTO.class);
            if(myaddress.getApartmentFloor() != null && dto.getApartmentFloor() != null
                    && !myaddress.getApartmentFloor().trim().equals("")
                    && !dto.getApartmentFloor().trim().equals("")){
                if(myaddress.getApartmentFloor().equals(dto.getApartmentFloor()))
                    user.setNeighborhoodRelation(NeighborhoodRelation.SAMEFLOOR.getCode());
            }
            else if(myaddress.getBuildingName().trim().equals(dto.getBuildingName().trim())){
                user.setNeighborhoodRelation(NeighborhoodRelation.SAMEBUILDING.getCode());
            }
            
            else
                user.setNeighborhoodRelation(NeighborhoodRelation.UNKNOWN.getCode());
            results.add(user);
        }
        //屏蔽分页
//        final int pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
//                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
//        int totalCount = results == null ? 0 : results.size();
//        int totalPage = totalCount / pageSize;
//        if ((totalCount % pageSize) > 0)
//        {
//            totalPage++;
//        }
//        int currentPage = (int) pageOffset;
//        if(pageOffset <= totalPage){
//            //sortNeighborUser(results);
//            int endIndex = Math.min(results.size(), currentPage*pageSize);
//            results = results.subList((currentPage-1)*pageSize,endIndex);
//        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("Process neighbor user,elapse=" + (endTime - startTime));
        return results;
        
    }

    @Override
    public List<NeighborUserDTO> listNearbyNeighborUsers(ListNearbyNeighborUserCommand cmd) {
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        User user = UserContext.current().getUser();
        Double latitude = cmd.getLatitude();
        Double longitude = cmd.getLongitude();
        if(latitude == null || longitude == null){
            LOGGER.error("Invalid parameter.latitude or longitude is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter,latitude or longitude is null.");
        }
        Long familyId = cmd.getId();
        Group group = this.groupProvider.findGroupById(familyId);
        
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), user.getId());
        if(m == null || m.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode())
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family or user status is not active.");
        
        List<Family> familyList = new ArrayList<Family>();
        long communityId = group.getIntegralTag2();
        List<Long> userIds = new ArrayList<Long>();
        List<NeighborUserDTO> results = new ArrayList<NeighborUserDTO>();
        
        long startTime = System.currentTimeMillis();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    context.select().from(Tables.EH_GROUPS)
                        .where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
                        .fetch().map( (r) ->{
                            //排除自己家庭
                            if(r.getValue(Tables.EH_GROUPS.INTEGRAL_TAG1) != group.getIntegralTag1())
                                familyList.add(ConvertHelper.convert(r,Family.class));

                            return null;
                        });
                    return true;
                });
        
        familyList.stream().forEach((f) ->{
            if(f == null) return;
            
            Address address = this.addressProvider.findAddressById(f.getAddressId());
            if(address != null){
                
                List<GroupMember> members = this.groupProvider.findGroupMemberByGroupId(f.getId());
                if(members != null && !members.isEmpty()){
                    for(GroupMember gm : members){
                        
                        if(gm.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode() 
                                && gm.getMemberType().equals(EntityType.USER.getCode())
                                        && gm.getMemberId().longValue() != user.getId().longValue()){
                            //去重
                            if(userIds.contains(gm.getMemberId())) 
                                return;
                            userIds.add(gm.getMemberId());
                        }
                    }
                }
            }
            
        });
        
        if(userIds.size() > 0){
            LOGGER.info("Community neary by user,userIds=" + userIds);

            List<UserLocation> listUserLocations = listCommunityUserLocation(userIds,latitude,longitude);
            final double MILES_KILOMETRES_RATIO = 1.609344;
            if(listUserLocations != null && !listUserLocations.isEmpty()){
                
                listUserLocations.forEach((userLocation) ->{
                    UserInfo u = this.userService.getUserSnapshotInfo(userLocation.getUid());
                    
                    NeighborUserDTO n = new NeighborUserDTO();
                    n.setUserId(u.getId());
                    n.setUserStatusLine(u.getStatusLine());
                    n.setUserName(u.getNickName());
                    n.setUserAvatarUrl(u.getAvatarUrl());
                    n.setUserAvatarUri(u.getAvatarUri());
                    n.setOccupation(u.getOccupation());
                    //计算距离
                    double lat = userLocation.getLatitude();
                    double lon = userLocation.getLongitude();
                    //getDistanceMi计算的是英里
                    double distince = DistanceUtils.getDistanceMi(latitude,longitude,lat , lon) * MILES_KILOMETRES_RATIO * 1000;
                    n.setDistance(distince);
                    results.add(n);
                    
                });
                
            }

        }
        Long pageOffset = cmd.getPageOffset();
        pageOffset = pageOffset == null ? 1 : pageOffset;
        
        processNeighborUserInfo(results,pageOffset);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query nearby neibhor user of communtiy,elapse=" + (endTime - startTime));
        return results;
    }
    
    private List<UserLocation> listCommunityUserLocation(List<Long> userIds,double latitude,double longitude) {
        long startTime = System.currentTimeMillis();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));
        
        final List<UserLocation> members = new ArrayList<UserLocation>();
        SelectQuery<?> query = context.selectQuery(Tables.EH_USER_LOCATIONS);
        Condition c = Tables.EH_USER_LOCATIONS.UID.in(userIds);
        Condition geoCondition = null;
        List<String> geoHashCodeList = getGeoHashCodeList(latitude,longitude);
        for(String geoHashCode : geoHashCodeList){
            if(geoCondition == null){
                geoCondition = Tables.EH_USER_LOCATIONS.GEOHASH.like(geoHashCode + "%");
            }else{
                geoCondition = geoCondition.or(Tables.EH_USER_LOCATIONS.GEOHASH.like(geoHashCode + "%"));
            }
        }
        if(geoCondition != null)
            c = c.and(geoCondition);
        query.addConditions(c);
        query.addOrderBy(Tables.EH_USER_LOCATIONS.CREATE_TIME.desc());
        //嵌套子查询??
        SelectQuery<?> query1 = context.selectQuery();
        Table<?> t = query.asTable();
        query1.addFrom(t);
        query1.addGroupBy(t.field("uid"));

        query1.fetch().map((r) -> {
            members.add(ConvertHelper.convert(r, UserLocation.class));
            return null;
        });
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query community user with latitude and longitude,esplse=" + (endTime - startTime));
        
        return members;
    }
    
    private List<String> getGeoHashCodeList(double latitude, double longitude){

        GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
        GeoHash[] adjacents = geo.getAdjacent();
        List<String> geoHashCodes = new ArrayList<String>();
        geoHashCodes.add(geo.toBase32());
        for(GeoHash g : adjacents) {
            geoHashCodes.add(g.toBase32());
        }
        return geoHashCodes;
    }
    
    
    private void processNeighborUserInfo(List<NeighborUserDTO> results ,long pageOffset) {
        if(results == null || results.isEmpty())
            return;
        final int pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        int totalCount = results == null ? 0 : results.size();
        int totalPage = totalCount / pageSize;
        if ((totalCount % pageSize) > 0)
        {
            totalPage++;
        }
        int currentPage = (int)pageOffset;
        if(pageOffset <= totalPage){
            sortNeighborUser(results);
            int endIndex = Math.min(results.size(), currentPage*pageSize);
            results = results.subList((currentPage-1)*pageSize,endIndex);
        }
        
    }

    //根据距离排序
    private void sortNeighborUser(List<NeighborUserDTO> results)
    {
        Collections.sort(results, new Comparator<NeighborUserDTO>()
        {
            @Override
            public int compare(NeighborUserDTO o1, NeighborUserDTO o2)
            {
               return (int) (o1.getDistance() - o2.getDistance());
            }
        });
    }

    @Override
    public FamilyDTO getFamilyDetailByAddressId(FindFamilyByAddressIdCommand cmd) {
        Long addressId = cmd.getAddressId();
        if(addressId == null) return null;
        User user = UserContext.current().getUser();

        Family family = this.familyProvider.findFamilyByAddressId(addressId);
        if(family == null){
            LOGGER.warn("Family is not found with addressId=" + addressId);
            return null;
        }
        
//        FamilyDTO familyDTO = ConvertHelper.convert(family, FamilyDTO.class);
//        //familyDTO.setApartmentId(cmd.getApartmentId());
//        if(family.getCreatorUid() == user.getId())
//            familyDTO.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
//        
//        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
//                EntityType.USER.getCode(), user.getId());
//        if(m != null){
//            familyDTO.setMemberUid(m.getMemberId());
//            familyDTO.setMemberNickName(m.getMemberNickName());
//            familyDTO.setMemberAvatarUrl(parserUri(m.getMemberAvatar(),EntityType.USER.getCode(),m.getCreatorUid()));
//            familyDTO.setMemberAvatarUri(m.getMemberAvatar());
//            familyDTO.setMembershipStatus(m.getMemberStatus());
//            Address address = this.addressProvider.findAddressById(addressId);
//            if(address != null){
//                familyDTO.setAddress(address.getAddress());
//                familyDTO.setApartmentId(address.getId());
//                familyDTO.setApartmentName(address.getApartmentName());
//                familyDTO.setBuildingName(address.getBuildingName());
//                familyDTO.setAddressStatus(address.getStatus());
//            }
//            
//            Community community = communityProvider.findCommunityById(family.getCommunityId());
//            if(community != null){
//                familyDTO.setCityName(community.getCityName());
//                familyDTO.setAreaName(community.getAreaName());
//                familyDTO.setCommunityName(community.getName());
//            }
//            
//        }
//        return familyDTO;
        
        return toFamilyDto(user.getId(), family);
    }

    @Override
    public ListWaitApproveFamilyCommandResponse listWaitApproveFamily(ListWaitApproveFamilyAdminCommand cmd) {

        Long pageOffset = cmd.getPageOffset();
        pageOffset = pageOffset == null ? 1L : pageOffset;
        
        Long pageSize = cmd.getPageSize();
        long communityId = cmd.getCommunityId() == null ? 0L : cmd.getCommunityId();
        
        pageSize =  (pageSize == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : pageSize);

        long offset = (int) PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        ListWaitApproveFamilyCommandResponse response = new ListWaitApproveFamilyCommandResponse();
        
        List<FamilyDTO> list = this.familyProvider.listWaitApproveFamily(communityId, offset, pageSize);
        if(list != null && !list.isEmpty()){
            response.setRequests(list);
            if(list.size() == pageSize){
                response.setNextPageOffset(pageOffset + 1);
            }
        }
        return response;
    }
    
    
    private String parserUri(String uri,String ownerType, Long ownerId){
        try {
            if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
                return contentServerService.parserUri(uri,ownerType,ownerId);
            
        } catch (Exception e) {
            LOGGER.error("Parser uri is error." + e.getMessage());
        }
        return null;

    }
    
    public List<GroupMember> listCommunityFamilyMember(Long communityId){
        if(communityId == null){
            LOGGER.error("Invalid communityId parameter.communityId=" + communityId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId parameter.");
        }
        Community community = this.communityProvider.findCommunityById(communityId);
        if(community == null){
            LOGGER.error("Community is not found.communityId=" + communityId);
        }
        List<Group> listGroup = this.familyProvider.listCommunityFamily(communityId);
        if(listGroup == null || listGroup.isEmpty()) 
            return null;
        List<GroupMember> results = new ArrayList<GroupMember>();
        
        for(Group group : listGroup){
            List<GroupMember> groupMembers = findGroupMemberByGroupId(group.getId());
            if(groupMembers != null && !groupMembers.isEmpty())
                results.addAll(groupMembers);
        }
        return null;
    }
    
    public List<GroupMember> findGroupMemberByGroupId(Long groupId){
        List<GroupMember> groupMembers = this.groupProvider.findGroupMemberByGroupId(groupId);
        groupMembers = groupMembers.stream().filter((GroupMember m) ->{
            if(m.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode())
                return true;
            return false;
                
        }).collect(Collectors.toList());
        return groupMembers;
    }

//    private void checkFamilyPrivilege(long uid, long familyId, long privilege) {
//        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.FAMILY.getCode());
//        List<Long> roles = resolver.determineRoleInResource(uid, familyId, EntityType.FAMILY.getCode(), familyId);
//        if(!this.aclProvider.checkAccess(EntityType.FAMILY.getCode(), familyId, EntityType.USER.getCode(), uid, privilege, 
//                roles))
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
//                    "Insufficient privilege");
//    }
    
    private boolean checkParamIsValid(Byte type, Long id) {
        if(id == null || type == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid id or type parameter");
        if(type.byteValue() != ParamType.FAMILY.getCode() && type.byteValue() != ParamType.COMMUNITY.getCode())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid id parameter");
        
        if(type == ParamType.FAMILY.getCode()){
            Group group = this.groupProvider.findGroupById(id);
            if(group == null)
                throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_FAMILY_NOT_EXIST, 
                           "Invalid familyId parameter");
            return true;
        }
        if(type == ParamType.COMMUNITY.getCode()){
            //TODO
        }
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id or type parameter");
    }

    @Override
    public FamilyDTO getFamilyById(GetFamilyCommand cmd) {
        if(cmd == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid id or type parameter");
        }
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        User user = UserContext.current().getUser();
        long userId = user.getId();
        FamilyDTO dto = this.familyProvider.getFamilyById(familyId);
        if(dto == null){
            LOGGER.error("Family is not exists,id=" + cmd.getId() + ",userId=" + userId);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_FAMILY_NOT_EXIST, 
                    "Family is not exits.");
        }
        return dto;
    }

    @Override
    public void approveMembersByFamily(Family family) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        long familyId = family.getId();
        Group group = ConvertHelper.convert(family, Group.class);
        List<GroupMember> groupMembers = this.groupProvider.findGroupMemberByGroupId(familyId);
        if(groupMembers != null){

            groupMembers.forEach((GroupMember member) ->{
                this.dbProvider.execute((TransactionStatus status) -> {
                    member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                    member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    member.setOperatorUid(userId);
                    this.groupProvider.updateGroupMember(member);
                    UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(member.getMemberId(), familyId);
                    if(userGroup != null){
                        userGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                        this.userProvider.updateUserGroup(userGroup);
                    }
                    sendFamilyNotificationCorrectAddressByAdmin(null,group,member,userId);
                    return null;
                });
                
            });
        }
        
    }
    
    private void sendFamilyNotificationCorrectAddressByAdmin(Address address, Group group, GroupMember member,long operatorId) {
        // send notification to the applicant
        try {
            
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.SystemAdmin);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who applicant
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_ADMIN_CORRECT_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendFamilyNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_ADMIN_CORRECT_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            List<Long> includeList = getFamilyIncludeList(group.getId(), null, member.getMemberId());
//            sendFamilyNotification(group.getId(),includeList,null,notifyTextForOthers,null,null);
            sendGroupNotificationToExcludeUsers(group.getId(),null,member.getMemberId(),notifyTextForOthers);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    private void sendGroupNotificationToExcludeUsers(Long groupId, Long operatorId, Long targetId, String message) {
        List<Long> excludeList = new ArrayList<Long>();
        if(operatorId != null) {
            excludeList.add(operatorId);
        }
        if(targetId != null) {
            excludeList.add(targetId);
        }
        // sendFamilyNotification(groupId, null, excludeList, message, null, null);
        sendFamilyNotification(groupId, null, excludeList, message, null, null);
    }
    
    private void sendFamilyNotificationToIncludeUser(Long groupId, Long userId, String message) {
        List<Long> includeList = new ArrayList<Long>();
        includeList.add(userId);
        // sendFamilyNotification(groupId, includeList, null, message, null, null);
        sendFamilyNotificationUseSystemUser(includeList, null, message, null);
    }

    private void sendFamilyNotificationToIncludeUser(Long groupId, Long userId, String message, Map<String, String> meta) {
        List<Long> includeList = new ArrayList<Long>();
        includeList.add(userId);
        // sendFamilyNotification(groupId, includeList, null, message, null, null);
        sendFamilyNotificationUseSystemUser(includeList, null, message, meta);
    }
    
    private List<Long> getFamilyIncludeList(Long groupId, Long operatorId, Long targetId) {
        CrossShardListingLocator locator = new CrossShardListingLocator(groupId);
        List<GroupMember> members = this.groupProvider.queryGroupMembers(locator, Integer.MAX_VALUE,(loc, query) -> {
            Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });
        List<Long> includeList = new ArrayList<Long>();
        for(GroupMember member : members) {
            if(targetId != null && member.getMemberId().longValue() == targetId)
                continue;
            if(operatorId != null && member.getMemberId().longValue() == operatorId)
                continue;
            includeList.add(member.getMemberId());
        }
        
        return includeList;
    }
    
    private QuestionMetaObject createGroupQuestionMetaObject(Group group, GroupMember requestor, GroupMember target) {
        QuestionMetaObject metaObject = new QuestionMetaObject();
        
        if(group != null) {
            metaObject.setResourceType(EntityType.FAMILY.getCode());
            metaObject.setResourceId(group.getId());
        }
        
        if(requestor != null) {
            metaObject.setRequestorUid(requestor.getMemberId());
            metaObject.setRequestTime(requestor.getCreateTime());
            metaObject.setRequestorNickName(requestor.getMemberNickName());
            String avatar = requestor.getMemberAvatar();
            metaObject.setRequestorAvatar(avatar);
            if(avatar != null && avatar.length() > 0) {
                try{
                    String url = contentServerService.parserUri(avatar, EntityType.FAMILY.getCode(), group.getId());
                    metaObject.setRequestorAvatarUrl(url);
                }catch(Exception e){
                    LOGGER.error("Failed to parse avatar uri of group member, groupId=" + group.getId() 
                        + ", memberId=" + requestor.getMemberId(), e);
                }
            }
            metaObject.setRequestId(requestor.getId());
        }
        
        if(target != null) {
            metaObject.setTargetType(EntityType.USER.getCode());
            metaObject.setTargetId(target.getMemberId());
            metaObject.setRequestId(target.getId());
        }
        
        return metaObject;
    }
    @Override
    public ListAllFamilyMembersCommandResponse listAllFamilyMembers(ListAllFamilyMembersAdminCommand cmd) {
        
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());  
        int pageOffset = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        
        List<GroupMember> members = this.familyProvider.listAllFamilyMembers(offset, pageSize);
        
        List<FamilyMemberFullDTO> results = new ArrayList<FamilyMemberFullDTO>();
        if(members != null && !members.isEmpty()){
            members.forEach((member) -> {
                Group group = this.groupProvider.findGroupById(member.getGroupId());
                FamilyMemberFullDTO familyMember = new FamilyMemberFullDTO();
                familyMember.setMemberUid(member.getMemberId());
                familyMember.setMemberNickName(member.getMemberNickName());
                familyMember.setMembershipStatus(member.getMemberStatus());
                familyMember.setId(member.getId());
                familyMember.setCreateTime(member.getCreateTime());
                if(group != null){
                    familyMember.setFamilyId(group.getId());
                    familyMember.setFamilyName(group.getName());
                    long communityId = group.getIntegralTag2();
                    Community community = this.communityProvider.findCommunityById(communityId);
                    if(community != null){
                        familyMember.setCommunityId(communityId);
                        familyMember.setCommunityName(community.getName());
                        familyMember.setCityId(community.getCityId());
                        familyMember.setCityName(community.getCityName());
                        familyMember.setAreaName(community.getAreaName());
                    }
                    
                    Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
                    if(address != null){
                        familyMember.setBuildingName(address.getBuildingName());
                        familyMember.setApartmentName(address.getApartmentName());
                        familyMember.setAddressStatus(address.getStatus());
                        
                    }
                   
                }
                UserIdentifier userIdentifier = getMobileOfUserIdentifier(member.getMemberId());
                if(userIdentifier != null)
                    familyMember.setCellPhone(userIdentifier.getIdentifierToken());
                results.add(familyMember);
            });
        }
        ListAllFamilyMembersCommandResponse response = new ListAllFamilyMembersCommandResponse();
        if(results != null && results.size() == pageSize){
            response.setNextPageOffset(pageOffset + 1);
        }
        response.setRequests(results);
        
        return response;
    }
    
    @Override
    public List<FamilyMemberDTO> listFamilyMembersByCityId(long cityId,int pageOffset,int pageSize){
        pageSize = pageSize == 0 ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : pageSize;
        
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        List<GroupMember> groupMembers = this.familyProvider.listFamilyMembersByCityId(cityId,offset,pageSize);
        List<FamilyMemberDTO> result = groupMembers.stream().map(r ->{
            FamilyMemberDTO dto = new FamilyMemberDTO();
            dto.setId(r.getId());
            dto.setFamilyId(r.getGroupId());
            dto.setMemberUid(r.getMemberId());
            dto.setMemberName(r.getMemberNickName());
            dto.setMemberAvatarUri(r.getMemberAvatar());
            return dto;
        }).collect(Collectors.toList());
        
        return result;
    }
    @Override
    public List<FamilyMemberDTO> listFamilyMembersByCommunityId(long communityId,int pageOffset,int pageSize){
        pageSize = pageSize == 0 ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : pageSize;
        
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        List<GroupMember> groupMembers = this.familyProvider.listFamilyMembersByCommunityId(communityId,offset,pageSize);
        List<FamilyMemberDTO> result = groupMembers.stream().map(r ->{
            FamilyMemberDTO dto = new FamilyMemberDTO();
            dto.setId(r.getId());
            dto.setFamilyId(r.getGroupId());
            dto.setMemberUid(r.getMemberId());
            dto.setMemberName(r.getMemberNickName());
            dto.setMemberAvatarUri(r.getMemberAvatar());
            return dto;
        }).collect(Collectors.toList());
        
        return result;
    }
    
    @Override
    public List<FamilyMemberDTO> listFamilyMembersByFamilyId(long familyId,int pageOffset,int pageSize){
        pageSize = pageSize == 0 ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : pageSize;
        
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        List<GroupMember> groupMembers = this.familyProvider.listFamilyMembersByFamilyId(familyId,offset,pageSize);
        List<FamilyMemberDTO> result = groupMembers.stream().map(r ->{
            FamilyMemberDTO dto = new FamilyMemberDTO();
            dto.setId(r.getId());
            dto.setFamilyId(r.getGroupId());
            dto.setMemberUid(r.getMemberId());
            dto.setMemberName(r.getMemberNickName());
            dto.setMemberAvatarUri(r.getMemberAvatar());
            return dto;
        }).collect(Collectors.toList());
        
        return result;
    }
    
    private FamilyDTO toFamilyDto(Long userId, Family family) {
        FamilyDTO familyDto = ConvertHelper.convert(family, FamilyDTO.class);
        
        if(family.getCreatorUid() == userId)
            familyDto.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
        
        familyDto.setAvatarUrl(parserUri(family.getAvatar(),EntityType.FAMILY.getCode(),family.getCreatorUid()));
        familyDto.setAvatarUri(family.getAvatar());
        familyDto.setAddressId(family.getIntegralTag1());
        Long communityId = family.getIntegralTag2();
        Community community = this.communityProvider.findCommunityById(communityId);
        if(community != null){
            familyDto.setCommunityId(communityId);
            familyDto.setCommunityName(community.getName());
            familyDto.setCityId(community.getCityId());
            familyDto.setCityName(community.getCityName()+community.getAreaName());
            familyDto.setCommunityType(community.getCommunityType());
            familyDto.setDefaultForumId(community.getDefaultForumId());
            familyDto.setFeedbackForumId(community.getFeedbackForumId());
        }
        
      if(family.getCreatorUid().longValue() == userId.longValue())
          familyDto.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
    
      GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyDto.getId(), 
              EntityType.USER.getCode(), userId);
      if(member != null){
          familyDto.setMemberUid(member.getMemberId());
          familyDto.setMemberNickName(member.getMemberNickName());
          familyDto.setMembershipStatus(member.getMemberStatus());
          familyDto.setMemberAvatarUrl(parserUri(member.getMemberAvatar(), EntityType.USER.getCode(), member.getCreatorUid()));
          familyDto.setMemberAvatarUri(member.getMemberAvatar());
          familyDto.setProofResourceUri(member.getProofResourceUri());
          familyDto.setProofResourceUrl(parserUri(member.getProofResourceUri(), EntityType.USER.getCode(), member.getCreatorUid()));
      }
    
      Address address = this.addressProvider.findAddressById(family.getIntegralTag1());
      if(address != null){
          familyDto.setBuildingName(address.getBuildingName());
          familyDto.setApartmentName(address.getApartmentName());
          familyDto.setAddressStatus(address.getStatus());
          String addrStr = FamilyUtils.joinDisplayName(community.getCityName(),community.getAreaName(), community.getName(), 
                  address.getBuildingName(), address.getApartmentName());
          familyDto.setDisplayName(addrStr);
          familyDto.setAddress(addrStr);
          familyDto.setAddressId(address.getId());
      }
             
      return familyDto;
    }

    @Override
    public void deleteHistoryById(Long id) {
        UserGroupHistory history = this.userGroupHistoryProvider.getHistoryById(id);
        if(history != null) {
            this.userGroupHistoryProvider.deleteUserGroupHistory(history);
        }
    }

	@Override
	public void adminBatchApproveMember(BatchApproveMemberCommand cmd) {
		if(cmd.getMembers() == null )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "invalid parameter members is null ");
		for(ApproveMemberCommand member : cmd.getMembers()){
			this.adminApproveMember(member);
		}
	}

	@Override
	public void batchRejectMember(BatchRejectMemberCommand cmd) {
		if(cmd.getMembers() == null )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "invalid parameter members is null ");
		for(RejectMemberCommand member : cmd.getMembers()){
			member.setOperatorRole(Role.SystemAdmin);
			this.rejectMember(member);
		}
        
	}

    /**
     * 格式：锁的key + 逗号 + 时间(毫秒) + 锁获取的编号
     *                                    其中锁的key必填，参考：{@link com.everhomes.coordinator.CoordinationLocks}
     *                                    时间以毫秒为单位，不填是默认为5秒；锁的编号用于在日志中识别是第几个锁，不填则默认为时间戳；
     *                                    这几项信息使用逗号分隔
     * @param cmd
     */
	public void testLockAquiring(TestLockAquiringCommand cmd) {
	    String lockCode = cmd.getLockCode(); // 锁对应的key，如 CoordinationLocks.CREATE_RESOURCE.getCode()
	    long millis = (cmd.getLockTimeout() == null) ? 5000L : cmd.getLockTimeout();  // 持有锁的时间
	    long number = (cmd.getNumber() == null) ? System.currentTimeMillis() : cmd.getNumber(); // 锁的编号
	    
	    if(lockCode != null) {
	        long startTime = System.currentTimeMillis();
	        final long tmpMillis = millis;
    	    coordinationProvider.getNamedLock(lockCode).enter(() -> {
                Thread.sleep(tmpMillis);
                return null;
            });
    	    long endTime = System.currentTimeMillis();
    	    if(LOGGER.isDebugEnabled()) {
    	        LOGGER.debug("Test aquiring lock(release), lockCode={}, millis={}, number={}, elapse={}", 
    	                lockCode, millis, number, (endTime - startTime));
    	    }
	    }
	}
}
