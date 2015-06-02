// @formatter:off
package com.everhomes.family;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;






















import org.apache.lucene.spatial.DistanceUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import ch.hsr.geohash.GeoHash;
import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.AppConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberStatus;
import com.everhomes.group.GroupPrivacy;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLocation;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;



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

    
    @Override
    public Family getOrCreatefamily(Address address)      {
        final User user = UserContext.current().getUser();
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
            if(family == null) {
                family = this.dbProvider.execute((TransactionStatus status) -> {
                    Family f = new Family();
                    f.setName(address.getAddress());
                    f.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
                    f.setDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    f.setAddressId(address.getId());
                    f.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
                    f.setCreatorUid(uid);
                    f.setMemberCount(0L);   // initialize it to 0
                    f.setCommunityId(address.getCommunityId());
                    
                    this.groupProvider.createGroup(f);
                    
                    GroupMember m = new GroupMember();
                    m.setGroupId(f.getId());
                    m.setMemberNickName(user.getAccountName());
                    m.setMemberType(EntityType.USER.getCode());
                    m.setMemberId(uid);
                    m.setMemberRole(Role.ResourceCreator);
                    m.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                    m.setCreatorUid(uid);
                    this.groupProvider.createGroupMember(m);

                    UserGroup userGroup = new UserGroup();
                    userGroup.setOwnerUid(uid);
                    userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    userGroup.setGroupId(f.getId());
                    userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
                    userGroup.setRegionScopeId(community.getId());
                    userGroup.setMemberRole(Role.ResourceCreator);
                    userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                    this.userProvider.createUserGroup(userGroup);
                    
                    sendFamilyNotificationForReqJoinFamily(address,f,m);
                    
                    return f;
                });
            } else {
                GroupMember m = new GroupMember();
                m.setGroupId(family.getId());
                m.setMemberType(EntityType.USER.getCode());
                m.setMemberId(uid);
                m.setMemberNickName(user.getAccountName());
                m.setMemberRole(Role.ResourceUser);
                m.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                m.setCreatorUid(uid);
                this.groupProvider.createGroupMember(m);
                
                UserGroup userGroup = new UserGroup();
                userGroup.setOwnerUid(uid);
                userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                userGroup.setGroupId(family.getId());
                userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
                userGroup.setRegionScopeId(community.getId());
                userGroup.setMemberRole(Role.ResourceUser);
                userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                this.userProvider.createUserGroup(userGroup);
                
                sendFamilyNotificationForReqJoinFamily(address,family,m);
            }
            long lcEndTime = System.currentTimeMillis();
            LOGGER.info("create family in the lock,elapse=" + (lcEndTime - lcStartTime));
            return family;  
        });
        
        if(result.second())
            return result.first();
        
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

            sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_REQ_FOR_OPERATOR;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            groupProvider.iterateGroupMembers(1000, group.getId(), null,
            (groupMember) -> {
                if(groupMember.getMemberId() != member.getMemberId()) {
                    sendFamilyNotification(groupMember.getMemberId(), notifyTextForOthers);
                }
            });
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, familyId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    private String fullAddress(long communityId , String buildingName, String apartmentName){
        Community community = communityProvider.findCommunityById(communityId);
        assert(community != null);
        return FamilyUtils.joinDisplayName(community.getCityName(), community.getName(), buildingName, apartmentName);
        
    }
    
    private void sendFamilyNotification(long userId, String message) {
        if(message != null && message.length() != 0) {
            if(message != null && message.length() != 0) {
                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
                messageDto.setSenderUid(User.SYSTEM_UID);
                messageDto.setChannels(new MessageChannel("user", String.valueOf(userId)));
                messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
                messageDto.setBody(message);
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_FAMILY, "user", 
                    String.valueOf(userId), messageDto, MessagingService.MSG_FLAG_STORED);
            }
        }
    }

 
    @Override
    public void joinFamily(JoinFamilyCommand cmd) {
    	User user = UserContext.current().getUser();
    	
    	checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
    	long userId = user.getId();
    	long familyId = cmd.getId();
    	Group group = this.groupProvider.findGroupById(familyId);
    	
        boolean flag = this.dbProvider.execute((TransactionStatus status) -> {
    		
    		GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), userId);
    		if(m != null)
    			throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_FAMILY_EXIST, 
    					"user has joined to the family");
    		m = new GroupMember();
    		m.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
    		m.setGroupId(familyId);
    		m.setMemberId(userId);
    		m.setMemberNickName(user.getAccountName());
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
        
        if(flag)
            return;
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
    }

    @Override
    public FamilyDTO getOwningFamilyById(GetOwningFamilyByIdCommand cmd) {
        User user = UserContext.current().getUser();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        
        long familyId = cmd.getId();
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), user.getId());
        
        if(m == null)
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family");
        
        final FamilyDTO[] results = new FamilyDTO[1];
        List<Long> familyIds = new ArrayList<Long>();
        familyIds.add(familyId);
        
        List<FamilyDTO> dtos = getUserOwningFamiliesByIds(familyIds,user.getId());
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
            
            if(group != null){

                FamilyDTO family = ConvertHelper.convert(group,FamilyDTO.class);
                family.setAvatarUrl((parserUri(group.getAvatar(),"Family",group.getCreatorUid())));
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
                    String addrStr = FamilyUtils.joinDisplayName(community.getCityName(), community.getName(), 
                                    address.getBuildingName(), address.getApartmentName());
                    family.setDisplayName(addrStr);
                    family.setAddress(addrStr);
                }
                familyList.add(family);
            }
        }
        
        return familyList;
    }

    @Override
    public List<FamilyDTO> getUserOwningFamilies() {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
       
        return this.familyProvider.getUserFamiliesByUserId(userId);
    }

    @Override
    public void leave(LeaveFamilyCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
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
        
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());

        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(userId, group.getId());
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User not in user group.");
        }
        this.familyProvider.leaveFamilyAtAddress(address, userGroup);
        
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

            sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_MEMBER_LEAVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            groupProvider.iterateGroupMembers(1000, group.getId(), null,
            (groupMember) -> {
                if(groupMember.getMemberId() != member.getMemberId()) {
                    sendFamilyNotification(groupMember.getMemberId(), notifyTextForOthers);
                }
            });
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

            sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            //send notification to operator
            code = FamilyNotificationTemplateCode.FAMILY_MEMBER_REVOKE_FOR_OPERATOR;
            String notifyTextForOpeator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            sendFamilyNotification(operatorId, notifyTextForOpeator);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_MEMBER_LEAVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            groupProvider.iterateGroupMembers(1000, group.getId(), null,
            (groupMember) -> {
                if(groupMember.getMemberId().longValue() != member.getMemberId().longValue() 
                        && groupMember.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()) {
                    sendFamilyNotification(groupMember.getMemberId(), notifyTextForOthers);
                }
            });
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
        
        Group group = this.groupProvider.findGroupById(familyId);
        cmd.setOperatorRole((cmd.getOperatorRole() == null ? Role.ResourceOperator : cmd.getOperatorRole()));
        //家庭成员拒绝
        if(cmd.getOperatorRole() != Role.ResourceAdmin){
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
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in familly.");
        }
        
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(memberUid, group.getId());
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + memberUid);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in familly.");
        }
        
        this.familyProvider.leaveFamilyAtAddress(address, userGroup);
        setCurrentFamilyAfterApproval(userGroup.getOwnerUid(),0,1);
        
        if(cmd.getOperatorRole() == Role.ResourceOperator)
            sendFamilyNotificationForMemberRejectFamily(address,group,member,userId);
        else if(cmd.getOperatorRole() == Role.ResourceAdmin)
            sendFamilyNotificationForMemberRejectFamilyByAdmin(address,group,member,userId);
    }
    
    private void sendFamilyNotificationForMemberRejectFamilyByAdmin(Address address, Group group, GroupMember member,long operatorId) {
        // send notification to the applicant
        try {
            Map<String, Object> map = bulidMapBeforeSendFamilyNotification(address,group,member,operatorId,Role.ResourceAdmin);
            
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            if(locale == null) locale = "zh_CN";
            
            // send notification member who revoked
            String scope = FamilyNotificationTemplateCode.SCOPE;
            int code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            groupProvider.iterateGroupMembers(1000, group.getId(), null,
            (groupMember) -> {
                if(groupMember.getMemberId().longValue() != member.getMemberId().longValue()) {
                    sendFamilyNotification(groupMember.getMemberId(), notifyTextForOthers);
                }
            });
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

            sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            //send notification to operator
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_OPERATOR;
            String notifyTextForOpeator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            sendFamilyNotification(operatorId, notifyTextForOpeator);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_REJECT_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            groupProvider.iterateGroupMembers(1000, group.getId(), null,
            (groupMember) -> {
                if(groupMember.getMemberId().longValue() != member.getMemberId().longValue()) {
                    sendFamilyNotification(groupMember.getMemberId(), notifyTextForOthers);
                }
            });
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
        
        long startTime = System.currentTimeMillis();

//        GroupMember currMember = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
//                EntityType.USER.getCode(), userId);
//        if(currMember != null && currMember.getMemberStatus().byteValue() != GroupMemberStatus.ACTIVE.getCode()){
//            LOGGER.error("The status of user in family invalid.userId=" + userId);
//            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_STATUS_INVALID, 
//                    "The status of user in family invalid.");
//        }
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), memberUid);
        if(member == null){
            LOGGER.error("Invalid memberUid parameter,user has not apply join in family.memberUid=" + memberUid);
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "Invalid memberUid parameter,user has not apply join in family.");
        }
        boolean flag = this.dbProvider.execute((TransactionStatus status) -> {
            
            member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            member.setOperatorUid(UserContext.current().getUser().getId());
            this.groupProvider.updateGroupMember(member);
            
            List<UserGroup> list = this.userProvider.listUserGroups(memberUid, GroupDiscriminator.FAMILY.getCode());
            list = list.stream().filter((userGroup) ->{
                return userGroup.getGroupId().longValue() == group.getId().longValue();
                
            }).collect(Collectors.toList());
            if(list != null && !list.isEmpty()){
                UserGroup userGroup = list.get(0);
                userGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                this.userProvider.updateUserGroup(userGroup);
            }
            
            return true;
            
        });
        
        if(flag){
            if(cmd.getOperatorRole().byteValue() == Role.ResourceOperator)
                sendFamilyNotificationForApproveMember(null,group,member,userId);
            else if(cmd.getOperatorRole().byteValue() == Role.ResourceAdmin)
                sendFamilyNotificationForApproveMember(null,group,member,userId);
            //update current family
            setCurrentFamilyAfterApproval(memberUid,familyId,0);
            
            //通知小区用户(通知通讯录好友)有新用户入住
            sendNotifyToCommunityUserAndContactUser(memberUid, familyId, group.getIntegralTag2());
            long endTime = System.currentTimeMillis();
            LOGGER.info("Approve family elapse=" + (endTime - startTime));
            return;
        }
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
        
        
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

            sendFamilyNotification(member.getMemberId(), notifyTextForApplicant);
            
            //send notification to operator
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_APPROVE_FOR_OPERATOR;
            String notifyTextForOpeator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            sendFamilyNotification(operatorId, notifyTextForOpeator);
            
            // send notification to family other members
            code = FamilyNotificationTemplateCode.FAMILY_JOIN_MEMBER_APPROVE_FOR_OTHER;
            final String notifyTextForOthers = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            groupProvider.iterateGroupMembers(1000, group.getId(), null,
            (groupMember) -> {
                if(groupMember.getMemberId().longValue() != member.getMemberId().longValue() 
                        && groupMember.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()) {
                    sendFamilyNotification(groupMember.getMemberId(), notifyTextForOthers);
                }
            });
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
            if(operatorRole != Role.ResourceAdmin){
                GroupMember operator = this.groupProvider.findGroupMemberByMemberInfo(group.getId(), 
                    EntityType.USER.getCode(), operatorId);
                assert(operator != null);
                map.put("operatorName", operator.getMemberNickName());
            }
            else{ 
//                User operator = this.userProvider.findUserById(operatorId);
//                assert(operator != null);
                map.put("operatorName", "管理员");
            }
            
        }
       
        return map;
    }
    
    private void sendNotifyToCommunityUserAndContactUser(Long memberUid, Long familyId, Long communityId) {
        // TODO send notify to relate user 
        
        
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
                f.setMemberName(groupMember.getMemberNickName());
                f.setMemberAvatarUrl((parserUri(groupMember.getMemberAvatar(),"User",groupMember.getCreatorUid())));
                f.setMemberAvatarUri(groupMember.getMemberAvatar());
                List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(groupMember.getMemberId());
                userIdentifiers.forEach((u) ->{
                   if(u.getIdentifierType().byteValue() == 0){
                       f.setCellPhone(u.getIdentifierToken());
                   }
                });
                
                results.add(f);
            }
        });
        return results;
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
        this.groupProvider.updateGroupMember(member);
        //更新之后，发送命令
    }

    @Override
    public List<FamilyMembershipRequestDTO> listFamilyRequests(ListFamilyRequestsCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        Long pageOffset = cmd.getPageOffset();
        Group group = this.groupProvider.findGroupById(familyId);

        if(pageOffset == null){
            LOGGER.warn("Invalid pageOffset parameter,pageOffset=" + pageOffset);
            pageOffset = 1L;
        }
        
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(m == null || m.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode()){
            LOGGER.error("User not in family or member status is not active");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "member status is not active");
        }

        List<FamilyMembershipRequestDTO> results = new ArrayList<FamilyMembershipRequestDTO>();
        
        List<GroupMember> listGroupMembers = this.familyProvider.listFamilyRequests(userId, familyId, pageOffset);
        listGroupMembers.forEach((r) ->{
            FamilyMembershipRequestDTO member = new FamilyMembershipRequestDTO();
            member.setFamilyId(familyId);
            member.setFamilyAvatarUrl(parserUri(group.getAvatar(),"Family",group.getCreatorUid()));
            member.setFamilyAvatarUri(group.getAvatar());
            member.setFamilyName(group.getName());
            member.setRequestorUid(r.getMemberId());
            member.setRequestorAvatar(parserUri(r.getMemberAvatar(),"User",r.getCreatorUid()));
            member.setRequestorComment(r.getRequestorComment());
            member.setRequestingTime(r.getCreateTime().toString());
            
            results.add(member);
        });
        
        return results;
    }

    @Override
    public ListNeighborUsersCommandResponse listNeighborUsers(ListNeighborUsersCommand cmd) {
        User user = UserContext.current().getUser();
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        Group group = this.groupProvider.findGroupById(familyId);
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), user.getId());
        
        if(member == null || member.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode())
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family");
        
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
                            //排除自己
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
                    for(GroupMember m : members){
                        if(m.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode() 
                                && m.getMemberType().equals(EntityType.USER.getCode())){
                            
                            NeighborUserDetailDTO n = new NeighborUserDetailDTO();
                            User u = this.userProvider.findUserById(m.getMemberId());
                            n.setUserId(u.getId());
                            n.setUserName(m.getMemberNickName());
                            n.setUserAvatarUrl(parserUri(m.getMemberAvatar(),"User",u.getId()));
                            n.setUserAvatarUri(m.getMemberAvatar());
                            n.setUserStatusLine(u.getStatusLine());
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
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query neighbor user of community,elapse=" + (endTime - startTime));
        ListNeighborUsersCommandResponse response = new ListNeighborUsersCommandResponse();
        response.setNeighborUserList(results);
        response.setNeighborCount(results == null ? null : results.size());
        return response;
    }

    private List<NeighborUserDTO> processNeighborUserInfo(List<NeighborUserDetailDTO> userDetailList,
            Address myaddress,long pageOffset) {
        
        if(userDetailList == null || userDetailList.isEmpty())
            return null;
        long startTime = System.currentTimeMillis();
        List<NeighborUserDTO> results = new ArrayList<NeighborUserDTO>();
        for(NeighborUserDetailDTO dto : userDetailList){
            NeighborUserDTO user = ConvertHelper.convert(dto, NeighborUserDTO.class);
            if(myaddress.getApartmentFloor() != null && dto.getApartmentFloor() != null){
                if(myaddress.getApartmentFloor().equals(dto.getApartmentFloor()))
                    user.setNeighborhoodRelation((byte) 1);
            }
            else if(myaddress.getBuildingName().equals(dto.getBuildingName())){
                user.setNeighborhoodRelation((byte) 2);
            }
            
            else
                user.setNeighborhoodRelation((byte) 0);
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
        if(m == null)
            throw RuntimeErrorException.errorWith(FamilyServiceErrorCode.SCOPE, FamilyServiceErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "User not in family");
            
        long communityId = group.getIntegralTag2();
        List<Long> userIds = new ArrayList<Long>();
        List<NeighborUserDTO> results = new ArrayList<NeighborUserDTO>();
        
        long startTime = System.currentTimeMillis();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    context.select(Tables.EH_USER_GROUPS.OWNER_UID).from(Tables.EH_USER_GROUPS)
                        .where(Tables.EH_USER_GROUPS.REGION_SCOPE.eq(RegionScope.COMMUNITY.getCode()))
                        .and(Tables.EH_USER_GROUPS.REGION_SCOPE_ID.eq(communityId))
                        .and(Tables.EH_USER_GROUPS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
                        .fetch().map( (r) ->{
                            userIds.add(r.getValue(Tables.EH_USER_GROUPS.OWNER_UID));
                            return null;
                        });
                    return true;
                });
        
        if(userIds.size() > 0){
            boolean isFirst = true;
            Condition c = Tables.EH_USER_LOCATIONS.UID.in(userIds);
            List<String> geoHashCodeList = getGeoHashCodeList(latitude,longitude);
            for(String geoHashCode : geoHashCodeList){
                if(isFirst){
                    c = Tables.EH_USER_LOCATIONS.GEOHASH.like(geoHashCode + "%");
                    isFirst = false;
                    continue;
                }
                if(!isFirst){
                    c = c.or(Tables.EH_USER_LOCATIONS.GEOHASH.like(geoHashCode + "%"));
                }
            }
            final Condition condition = c;
            
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        
                        SelectQuery<?> query = context.selectQuery(Tables.EH_USER_LOCATIONS);
                        
                        query.addConditions(condition);
                        query.addGroupBy(Tables.EH_USER_LOCATIONS.UID);
                        query.addOrderBy(Tables.EH_USER_LOCATIONS.CREATE_TIME.desc());
                        query.fetch().map((r) -> {
                            UserLocation userLocation = ConvertHelper.convert(r,UserLocation.class);
                            User u = this.userProvider.findUserById(userLocation.getUid());
                            NeighborUserDTO n = new NeighborUserDTO();
                            n.setUserId(u.getId());
                            n.setUserStatusLine(u.getStatusLine());
                            n.setUserName(u.getAccountName());
                            n.setUserAvatarUrl(parserUri(u.getAvatar(),"User",u.getId()));
                            n.setUserAvatarUri(u.getAvatar());
                            //计算距离
                            double lat = userLocation.getLatitude();
                            double lon = userLocation.getLongitude();
                            double distince = DistanceUtils.getDistanceMi(latitude,longitude,lat , lon);
                            n.setDistance(distince);
                            results.add(n);
                            return null;
                        });
                        
                        return true;
                    });
        }
        Long pageOffset = cmd.getPageOffset();
        pageOffset = pageOffset == null ? 1 : pageOffset;
        
        processNeighborUserInfo(results,pageOffset);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query nearby neibhor user of communtiy,elapse=" + (endTime - startTime));
        return results;
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
        
        FamilyDTO familyDTO = ConvertHelper.convert(family, FamilyDTO.class);
        //familyDTO.setAddressId(cmd.getAddressId());
        if(family.getCreatorUid() == user.getId())
            familyDTO.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
        
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
                EntityType.USER.getCode(), user.getId());
        if(m != null){
            
            
            familyDTO.setMemberUid(m.getMemberId());
            familyDTO.setMemberNickName(m.getMemberNickName());
            familyDTO.setMemberAvatarUrl(parserUri(m.getMemberAvatar(),"User",m.getCreatorUid()));
            familyDTO.setMemberAvatarUri(m.getMemberAvatar());
            familyDTO.setMembershipStatus(m.getMemberStatus());
            Address address = this.addressProvider.findAddressById(addressId);
            if(address != null){
            familyDTO.setAddress(address.getAddress());
                familyDTO.setAddressId(address.getId());
                familyDTO.setApartmentName(address.getApartmentName());
                familyDTO.setBuildingName(address.getBuildingName());
                familyDTO.setAddressStatus(address.getStatus());
            }
            
            Community community = communityProvider.findCommunityById(family.getCommunityId());
            if(community != null){
                familyDTO.setCityName(community.getCityName());
                familyDTO.setAreaName(community.getAreaName());
                familyDTO.setCommunityName(community.getName());
            }
            
        }
        return familyDTO;
    }

    @Override
    public List<FamilyDTO> listWaitApproveFamily(ListWaitApproveFamilyCommand cmd) {
        Long pageOffset = cmd.getPageOffset();
        pageOffset = pageOffset == null ? 1L : pageOffset;
        
        Long pageSize = cmd.getPageSize();
        Long communityId = cmd.getCommunityId();
         
        return this.familyProvider.listWaitApproveFamily(communityId, pageOffset, pageSize);
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
        if(listGroup == null || listGroup.isEmpty()) return null;
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


    @Override
    public boolean checkParamIsValid(Byte type, Long id) {
        if(id == null || type == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid id or type parameter");
        if(type.byteValue() != ParamType.FAMILY.getCode() && type.byteValue() != ParamType.COMMUNITY.getCode())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid id parameter");
        
        if(type == ParamType.FAMILY.getCode()){
            Group group = this.groupProvider.findGroupById(id);
            if(group == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
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
        
        checkParamIsValid(ParamType.fromCode(cmd.getType()).getCode() , cmd.getId());
        Long familyId = cmd.getId();
        
        return this.familyProvider.getFamilyById(familyId);
    }


}
