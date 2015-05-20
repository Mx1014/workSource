// @formatter:off
package com.everhomes.family;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import org.apache.lucene.spatial.DistanceUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;


import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
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
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import freemarker.core.ReturnInstruction.Return;



/**
 * Family inherits from Group for family member management. GroupDiscriminator.FAMILY
 * distinguishes it from other group objects 
 * 
 */
@Component
public class FamilyProviderImpl implements FamilyProvider {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
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

    @Cacheable(value="Family", key="#addressId")
    @Override
    public Family findFamilyByAddressId(long addressId) {
        final Family[] result = new Family[1];
        dbProvider.mapReduce(AccessSpec.readWriteWith(EhGroups.class), result, 
        (DSLContext context, Object reducingContext) -> {
            List<Family> list = context.select().from(Tables.EH_GROUPS)
                    .where(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(addressId))
                    .and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
                    .fetch().map((r) -> {
                        return ConvertHelper.convert(r, Family.class);
                    });
                
                if(list != null && !list.isEmpty()){
                    result[0] = list.get(0);
                    return false;
                }
            
            return true;
        });
        
        return result[0];
    }
    
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

        //全局锁导致入库超时，暂时去掉
        Tuple<Family, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_FAMILY.getCode()).enter(()-> {
            long lqStartTime = System.currentTimeMillis(); 
            Family family = findFamilyByAddressId(address.getId());
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
            }
            long lcEndTime = System.currentTimeMillis();
            LOGGER.info("create family in the lock,elapse=" + (lcEndTime - lcStartTime));
            return family;  
        });
        
        if(result.second())
            return result.first();
        
//        Family family = findFamilyByAddressId(address.getId());
//        if(family == null) {
//            family = this.dbProvider.execute((TransactionStatus status) -> {
//                Family f = new Family();
//                f.setName(address.getAddress());
//                f.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
//                f.setDiscriminator(GroupDiscriminator.FAMILY.getCode());
//                f.setAddressId(address.getId());
//                f.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
//                f.setCreatorUid(uid);
//                f.setMemberCount(0L);   // initialize it to 0
//                f.setCommunityId(address.getCommunityId());
//                
//                this.groupProvider.createGroup(f);
//                
//                GroupMember m = new GroupMember();
//                m.setGroupId(f.getId());
//                m.setMemberNickName(user.getAccountName());
//                m.setMemberType(EntityType.USER.getCode());
//                m.setMemberId(uid);
//                m.setMemberRole(Role.ResourceCreator);
//                m.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
//                m.setCreatorUid(uid);
//                this.groupProvider.createGroupMember(m);
//                
//                UserGroup userGroup = new UserGroup();
//                userGroup.setOwnerUid(uid);
//                userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
//                userGroup.setGroupId(f.getId());
//                userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
//                userGroup.setRegionScopeId(community.getId());
//                userGroup.setMemberRole(Role.ResourceCreator);
//                userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
//                this.userProvider.createUserGroup(userGroup);
//                
//                return f;
//            });
//        } else {
//            GroupMember m = new GroupMember();
//            m.setGroupId(family.getId());
//            m.setMemberType(EntityType.USER.getCode());
//            m.setMemberId(uid);
//            m.setMemberNickName(user.getAccountName());
//            m.setMemberRole(Role.ResourceUser);
//            m.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
//            m.setCreatorUid(uid);
//            this.groupProvider.createGroupMember(m);
//            
//            UserGroup userGroup = new UserGroup();
//            userGroup.setOwnerUid(uid);
//            userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
//            userGroup.setGroupId(family.getId());
//            userGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
//            userGroup.setRegionScopeId(community.getId());
//            userGroup.setMemberRole(Role.ResourceUser);
//            userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
//            this.userProvider.createUserGroup(userGroup);
//        }
//        long endTime = System.currentTimeMillis();
//        LOGGER.info("Get or create family,elapse=" + (endTime - startTime));
//        if(family != null)
//            return family;
        
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
    }
    
    @Override
    public void leaveFamilyAtAddress(Address address, UserGroup userGroup) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.LEAVE_FAMILY.getCode()).enter(()-> {
            Family family = findFamilyByAddressId(address.getId());
            
            GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
            		EntityType.USER.getCode(), userGroup.getOwnerUid());
            assert(m != null);
            if(m != null) {
                this.groupProvider.deleteGroupMember(m);
                
                // retrieve family info after membership changes
                FamilyProvider self = PlatformContext.getComponent(FamilyProvider.class);
                family = self.findFamilyByAddressId(address.getId());
                
                if(family.getMemberCount() == 0) {
                    this.groupProvider.deleteGroup(family);
                } else {
                    if(m.getMemberRole() == Role.ResourceCreator) {
                        // reassign resource creator to other member
                        GroupMember newCreator = pickOneMemberToPromote(family);
                        newCreator.setMemberRole(Role.ResourceCreator);
                        this.groupProvider.updateGroupMember(newCreator);
                        
                        family.setCreatorUid(newCreator.getMemberId());
                    }
                    this.groupProvider.updateGroup(family);
                }
            }
            
            this.userProvider.deleteUserGroup(userGroup);
            return null;
        });
        
        setCurrentFamilyAfterApproval(userGroup.getOwnerUid(),0,1);
    }
    
    private GroupMember pickOneMemberToPromote(Family family) {
        CrossShardListingLocator locator = new CrossShardListingLocator(family.getId());
        
        List<GroupMember> members = this.groupProvider.listGroupMembers(locator, Integer.MAX_VALUE);
        return members.get(0);
    }

    @Override
    public Tuple<Integer, List<FamilyDTO>> findFamilByKeyword(String keyword) {
    	if(StringUtils.isEmpty(keyword))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword parameter");
        List<FamilyDTO> results = new ArrayList<FamilyDTO>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    String likeVal = keyword + "%";
                    context.selectDistinct(Tables.EH_GROUPS.ID, Tables.EH_ADDRESSES.ADDRESS, Tables.EH_ADDRESSES.COMMUNITY_ID,
                        Tables.EH_ADDRESSES.CITY_ID,Tables.EH_COMMUNITIES.NAME,Tables.EH_COMMUNITIES.CITY_NAME)
                        .from(Tables.EH_GROUPS)
                        .leftOuterJoin(Tables.EH_ADDRESSES)
                        .on(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(Tables.EH_ADDRESSES.ID))
                        .leftOuterJoin(Tables.EH_COMMUNITIES)
                        .on(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(Tables.EH_COMMUNITIES.ID))
                        .where((Tables.EH_GROUPS.NAME.like(likeVal)
                            .or(Tables.EH_GROUPS.DISPLAY_NAME.like(likeVal)))
                            .and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode())))
                        .fetch().map((r) -> {
                            FamilyDTO family = new FamilyDTO();
                            family.setId(r.getValue(Tables.EH_GROUPS.ID));
                            family.setName(r.getValue(Tables.EH_GROUPS.NAME));
                            family.setAddress(r.getValue(Tables.EH_ADDRESSES.ADDRESS));
                            family.setCommunityId(r.getValue(Tables.EH_ADDRESSES.COMMUNITY_ID));
                            family.setCommunityName(r.getValue(Tables.EH_COMMUNITIES.NAME));
                            family.setCityId(r.getValue(Tables.EH_ADDRESSES.CITY_ID));
                            family.setCityName(r.getValue(Tables.EH_COMMUNITIES.CITY_NAME));
                            results.add(family);
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<FamilyDTO>>(ErrorCodes.SUCCESS, results);
    }
    
    @Override
    public void joinFamily(Long familyId) {
    	User user = UserContext.current().getUser();
    	long userId = user.getId();
    	if(familyId == null)
    	    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        boolean flag = this.dbProvider.execute((TransactionStatus status) -> {
    		Group f = this.groupProvider.findGroupById(familyId);
    		if(f == null)
    			 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
    	                    "Invalid familyId parameter");
    		GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, EntityType.USER.getCode(), userId);
    		if(m != null)
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
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
		    userGroup.setRegionScopeId(f.getIntegralTag2());
		    userGroup.setMemberRole(Role.ResourceUser);
		    userGroup.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
		    this.userProvider.createUserGroup(userGroup);
    		return true;
    	});
        
        if(flag)
            return;
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
    }

    @Override
    public FamilyDTO getOwningFamilyById(Long familyId) {
        User user = UserContext.current().getUser();
        Long addressId = user.getAddressId();
        
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        FamilyProvider self = PlatformContext.getComponent(FamilyProvider.class);
        Family f = self.findFamilyByAddressId(addressId);
        if(f == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                       "Invalid addressId parameter");
        if(f.getId().longValue() != familyId.longValue())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,user not in family");
        
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
                family.setAvatar(parserUri(family.getAvatar(),"Family",group.getCreatorUid()));
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
        List<UserGroup> list = this.userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());
        
        if(list == null || list.isEmpty())
            return null;
        List<Long> familyIds = new ArrayList<Long>();
        for(UserGroup u : list){
            familyIds.add(u.getGroupId());
        }
       return getUserOwningFamiliesByIds(familyIds, userId);
       
    }

    @Override
    public void leave(Long familyId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
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
        leaveFamilyAtAddress(address, userGroup);
        
    }

    @Override
    public FamilyDTO getFamilyById(Long familyId) {

        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        FamilyDTO family = null;
        if(group != null){
            family = ConvertHelper.convert(group,FamilyDTO.class);
            family.setAddressId(group.getIntegralTag1());
            long communityId = group.getIntegralTag2();
            Community community = this.communityProvider.findCommunityById(communityId);
            if(community != null){
                family.setCommunityId(communityId);
                family.setCommunityName(community.getName());
                family.setCityId(community.getCityId());
                family.setCityName(community.getCityName());
            }
        }
            
        return family;
    }

    @Override
    public void revokeMember(Long familyId, Long memberUid, String reason) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        if(familyId == null || memberUid == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId or memberUid parameter");
        //剔除别人，是否保留历史??
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        if(memberUid.longValue() == userId)
            throw RuntimeErrorException.errorWith(FamilyProvideErrorCode.SCOPE, FamilyProvideErrorCode.ERROR_USER_REVOKE_SELF, 
                    "Invalid memberUid parameter,can not revoke self");
        
        
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(userId, group.getId());
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User not in user group.");
        }
        
        leaveFamilyAtAddress(address, userGroup);
        
    }


    @Override
    public void rejectMember(Long familyId, Long memberUid, String reason, Byte operatorRole) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        if(familyId == null || memberUid == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId or memberUid parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter.");
        //家庭成员拒绝
        if(memberUid != userId && operatorRole == 0){
            GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                    EntityType.USER.getCode(), userId);
            if(m != null && m.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode()){
                throw RuntimeErrorException.errorWith(FamilyProvideErrorCode.SCOPE, FamilyProvideErrorCode.ERROR_USER_STATUS_INVALID, 
                        "User status is not active.");
            }
        }
        
        
        
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        
        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(userId, group.getId());
        if(userGroup == null){
            LOGGER.error("User not in user group.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User not in user group.");
        }
        
        leaveFamilyAtAddress(address, userGroup);
        
        //发送通知??
    }
    @Override
    public void approveMember(Long familyId, Long memberUid) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        if(familyId == null || memberUid == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId or memberUid parameter");
        long startTime = System.currentTimeMillis();
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,family is not found.");
        }
//        GroupMember currMember = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
//                EntityType.USER.getCode(), userId);
//        if(currMember != null && currMember.getMemberStatus().byteValue() != GroupMemberStatus.ACTIVE.getCode()){
//            LOGGER.error("The status of user in family invalid.userId=" + userId);
//            throw RuntimeErrorException.errorWith(FamilyProvideErrorCode.SCOPE, FamilyProvideErrorCode.ERROR_USER_STATUS_INVALID, 
//                    "The status of user in family invalid.");
//        }
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), memberUid);
        if(member == null){
            LOGGER.error("Invalid memberUid parameter,user has not apply join in family.memberUid=" + memberUid);
            throw RuntimeErrorException.errorWith(FamilyProvideErrorCode.SCOPE, FamilyProvideErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "Invalid memberUid parameter,user has not apply join in family.");
        }
        boolean flag = this.dbProvider.execute((TransactionStatus status) -> {
            
            member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            member.setOperatorUid(UserContext.current().getUser().getId());
            this.groupProvider.updateGroupMember(member);
            
            List<UserGroup> list = this.userProvider.listUserGroups(memberUid, GroupDiscriminator.FAMILY.getCode());
            list = list.stream().filter((userGroup) ->{
                return userGroup.getGroupId() == group.getId();
                
            }).collect(Collectors.toList());
            if(list != null && !list.isEmpty()){
                UserGroup userGroup = list.get(0);
                userGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                this.userProvider.updateUserGroup(userGroup);
            }
            
            return true;
            
        });
        
        if(flag){
            setCurrentFamilyAfterApproval(memberUid,familyId,0);
            //??发送更新命令
            
            //通知小区用户有新用户入住
            
            //通知通讯录好友
            long endTime = System.currentTimeMillis();
            LOGGER.info("Approve family elapse=" + (endTime - startTime));
            return;
        }
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
        
        
    }
    private void setCurrentFamilyAfterApproval(long userId, long familyId, int status){
        //set with first pass or last delete
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
        }else {
            updateUserContext(userId, familyId);
        }
    }

    @Override
    public List<FamilyMemberDTO> listOwningFamilyMembers(Long familyId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(member == null){
            LOGGER.error("User not in family.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(FamilyProvideErrorCode.SCOPE, FamilyProvideErrorCode.ERROR_USER_NOT_IN_FAMILY, 
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
                f.setMemberAvatar(parserUri(groupMember.getMemberAvatar(),"User",groupMember.getCreatorUid()));
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
    public void setCurrentFamily(Long familyId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        updateUserContext(userId,familyId);
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
    public void updateFamilyInfo(Long familyId, String familyName, String familyDescription,
            String familyAvatar, String memberNickName, String memberAvatar) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found,familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        }
        if(!StringUtils.isEmpty(familyAvatar)){
            group.setAvatar(familyAvatar);
        }
        if(!StringUtils.isEmpty(familyDescription)){
            group.setDescription(familyDescription);
        }
        if(!StringUtils.isEmpty(familyName)){
            group.setName(familyName);
        }
        this.groupProvider.updateGroup(group);
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(member == null)
            return;
        if(!StringUtils.isEmpty(memberAvatar)){
            member.setMemberAvatar(memberAvatar);
        }
        if(!StringUtils.isEmpty(memberNickName)){
            member.setMemberNickName(memberNickName);
        }
        this.groupProvider.updateGroupMember(member);
        //更新之后，发送命令
    }

    @Override
    public List<FamilyMembershipRequestDTO> listFamilyRequests(Long familyId ,Long pageOffset) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found,familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        }

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
        final long pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        
        List<FamilyMembershipRequestDTO> results = new ArrayList<FamilyMembershipRequestDTO>();
        
        //查询待处理的审核，别人主动加入（WAITING_FOR_APPROVAL），被人拉入（WAITING_FOR_ACCEPTANCE）
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select().from(Tables.EH_GROUP_MEMBERS)
                        .where((Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(familyId)
                                .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
                                        .eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode())))
                                .or((Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(userId)
                                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
                                                .eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode()))))
                                )
                        .limit((int)pageSize).offset((int)offset)
                        .fetch().map((r) -> {
                           FamilyMembershipRequestDTO member = new FamilyMembershipRequestDTO();
                           member.setFamilyId(familyId);
                           member.setFamilyAvatar(parserUri(group.getAvatar(),"Family",group.getCreatorUid()));
                           member.setFamilyName(group.getName());
                           member.setRequestorUid(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
                           member.setRequestorAvatar(parserUri(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR),"User",r.getValue(Tables.EH_GROUP_MEMBERS.CREATOR_UID)));
                           member.setRequestorComment(r.getValue(Tables.EH_GROUP_MEMBERS.REQUESTOR_COMMENT));
                           member.setRequestingTime(r.getValue(Tables.EH_GROUP_MEMBERS.CREATE_TIME).toString());
                           
                           results.add(member);
                           return null;
                        });
                    
                return true;
            });
        
        return results;
    }

    @Override
    public List<NeighborUserDTO> listNeighborUsers(Long familyId ,Long pageOffset) {
        User user = UserContext.current().getUser();
        if(user.getAddressId() == null || user.getAddressId() == 0){
            LOGGER.error("User has not address.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User has not address.");
        }
        
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        FamilyProvider self = PlatformContext.getComponent(FamilyProvider.class);
        Family family = self.findFamilyByAddressId(user.getAddressId());
        if(family == null || family.getId().longValue() != familyId){
            LOGGER.error("Invalid familyId parameter,user not in family.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(FamilyProvideErrorCode.SCOPE, FamilyProvideErrorCode.ERROR_USER_NOT_IN_FAMILY, 
                    "Invalid familyId parameter,user not in family.");
        }
        
        List<Family> familyList = new ArrayList<Family>();
        long communityId = family.getCommunityId();
        List<NeighborUserDetailDTO> userDetailList = new ArrayList<NeighborUserDetailDTO>();
        Address myaddress = this.addressProvider.findAddressById(user.getAddressId());;
        
        long startTime = System.currentTimeMillis();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select().from(Tables.EH_GROUPS)
                        .where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
                        .fetch().map( (r) ->{
                            //排除自己
                            if(r.getValue(Tables.EH_GROUPS.INTEGRAL_TAG1) != user.getAddressId())
                                familyList.add(ConvertHelper.convert(r,Family.class));
                            return null;
                        });
                   
                    return true;
                });
        
        familyList.parallelStream().forEach((f) ->{
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
                            n.setUserAvatar(parserUri(m.getMemberAvatar(),"User",u.getId()));
                            n.setUserStatusLine(u.getStatusLine());
                            n.setBuildingName(address.getBuildingName());
                            n.setApartmentFloor(address.getApartmentFloor());
                            userDetailList.add(n);
                        }
                    }
                }
            }
            
        });
        pageOffset = pageOffset == null ? 1 : pageOffset;
        List<NeighborUserDTO> results = processNeighborUserInfo(userDetailList,myaddress,pageOffset);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query neighbor user of community,elapse=" + (endTime - startTime));
        return results;
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
    public List<NeighborUserDTO> listNearbyNeighborUsers(Long familyId ,Double longitude, Double latitude,Long pageOffset) {
        
        if(latitude == null || longitude == null){
            LOGGER.error("Invalid parameter.latitude or longitude is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter,latitude or longitude is null.");
        }
        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        
        User user = UserContext.current().getUser();
        if(user.getAddressId() == null || user.getAddressId() == 0){
            LOGGER.error("User has not address.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User has not address.");
        }
        FamilyProvider self = PlatformContext.getComponent(FamilyProvider.class);
        Family family = self.findFamilyByAddressId(user.getAddressId());
        if(family == null || (family != null && family.getId().longValue() != familyId.longValue())){
            LOGGER.error("Invalid familyId parameter,user not in family.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,user not in family.");
        }
            
        long communityId = family.getCommunityId();
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
                    
                    if(userIds.size() > 0){
                        String geoHash = GeoHashUtils.encode(longitude, latitude);
                        String likeVal = geoHash + "%";
                        context.selectDistinct(Tables.EH_USERS.ID,Tables.EH_USERS.STATUS_LINE,Tables.EH_USERS.ACCOUNT_NAME
                                ,Tables.EH_USERS.AVATAR,Tables.EH_USER_LOCATIONS.LATITUDE,Tables.EH_USER_LOCATIONS.LONGITUDE)
                        .from(Tables.EH_USER_LOCATIONS)
                        .leftOuterJoin(Tables.EH_USERS).on(Tables.EH_USER_LOCATIONS.UID.eq(Tables.EH_USERS.ID))
                        .where(Tables.EH_USER_LOCATIONS.UID.in(userIds))
                        .and(Tables.EH_USER_LOCATIONS.GEOHASH.like(likeVal))
                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.notEqual(user.getId()))
                        .fetch().map( (r) ->{
                            
                            NeighborUserDTO n = new NeighborUserDTO();
                            n.setUserId(r.getValue(Tables.EH_USERS.ID));
                            n.setUserStatusLine(r.getValue(Tables.EH_USERS.STATUS_LINE));
                            n.setUserName(r.getValue(Tables.EH_USERS.ACCOUNT_NAME));
                            n.setUserAvatar(parserUri(r.getValue(Tables.EH_USERS.AVATAR),"User",r.getValue(Tables.EH_USERS.ID)));
                            //计算距离
                            double lat = r.getValue(Tables.EH_USER_LOCATIONS.LATITUDE);
                            double lon = r.getValue(Tables.EH_USER_LOCATIONS.LONGITUDE);
                            double distince = DistanceUtils.getDistanceMi(latitude,longitude,lat , lon);
                            n.setDistance(distince);
                            results.add(n);
                            return null;
                        });
                        
                    }
                   
                    return true;
                });
        pageOffset = pageOffset == null ? 1 : pageOffset;
        
        processNeighborUserInfo(results,pageOffset);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query nearby neibhor user of communtiy,elapse=" + (endTime - startTime));
        return results;
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
                if (o1.getDistance() > o2.getDistance())
                    return 1;
                else if (o1.getDistance() < o2.getDistance())
                {
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public FamilyDTO findFamilyByAddressId(FindFamilyByAddressIdCommand cmd) {
        if(cmd.getAddressId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid addressId parameter.");
        }
        User user = UserContext.current().getUser();

        FamilyProvider self = PlatformContext.getComponent(FamilyProvider.class);
        Family family = self.findFamilyByAddressId(cmd.getAddressId());
        if(family == null)
            return null;
        
        FamilyDTO familyDTO = ConvertHelper.convert(family, FamilyDTO.class);
        //familyDTO.setAddressId(cmd.getAddressId());
        if(family.getCreatorUid() == user.getId())
            familyDTO.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
        
        GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
                EntityType.USER.getCode(), user.getId());
        if(m != null){
            familyDTO.setMemberUid(m.getMemberId());
            familyDTO.setMemberNickName(m.getMemberNickName());
            familyDTO.setMemberAvatar(parserUri(m.getMemberAvatar(),"User",m.getCreatorUid()));
            familyDTO.setMembershipStatus(m.getMemberStatus());
        }
        return familyDTO;
    }

    @Override
    public List<FamilyDTO> listWaitApproveFamily(Long comunityId, Long pageOffset, Long pageSize) {
        pageOffset = pageOffset == null ? 1L : pageOffset;
        
        int size = (int) (pageSize == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : pageSize);
        List<FamilyDTO> results = new ArrayList<FamilyDTO>();
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, size);
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    SelectConditionStep<Record> step = context.select().from(Tables.EH_GROUP_MEMBERS)
                    .leftOuterJoin(Tables.EH_GROUPS)
                    .on(Tables.EH_GROUPS.ID.eq(Tables.EH_GROUP_MEMBERS.GROUP_ID))
                    .where(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
                                .eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));
                    
                    if(comunityId != null){
                        step.and(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(comunityId));
                    }
                    step.orderBy(Tables.EH_GROUP_MEMBERS.PROOF_RESOURCE_URL.desc())
                    .limit(size).offset((int)offset)
                    .fetch().map((r) ->{
                        
                            Address address = this.addressProvider.findAddressById(r.getValue(Tables.EH_GROUPS.INTEGRAL_TAG1));
                            if (address == null) return null;
                            Community community = this.communityProvider.findCommunityById(address.getCommunityId());
                            if(community == null) return null;
                            FamilyDTO f = new FamilyDTO();
                            f.setAddress(address.getAddress());
                            f.setAddressId(address.getId());
                            f.setApartmentName(address.getApartmentName());
                            f.setBuildingName(address.getBuildingName());
                            f.setAddressStatus(address.getStatus());
                            
                            f.setCityName(community.getCityName());
                            f.setAreaName(community.getAreaName());
                            f.setCommunityId(community.getId());
                            f.setCommunityName(community.getName());
                            f.setId(r.getValue(Tables.EH_GROUPS.ID));
                            f.setMemberCount(r.getValue(Tables.EH_GROUPS.MEMBER_COUNT));
                            f.setMemberUid(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
                            f.setMemberNickName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
                            f.setMembershipStatus(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS));
                            f.setProofResourceUrl(parserUri(r.getValue(Tables.EH_GROUP_MEMBERS.PROOF_RESOURCE_URL),"Family",r.getValue(Tables.EH_GROUP_MEMBERS.CREATOR_UID)));
                            results.add(f);
                            return null;
                        });
                    
                    return true;
                });
        return results;
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
        List<Group> listGroup = listCommunityFamily(communityId);
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
    
    public List<Group> listCommunityFamily(Long communityId){
        List<Group> groupList = new ArrayList<Group>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
            groupList, (DSLContext context, Object reducingContext) -> {
                List<Group> list = context.select().from(Tables.EH_GROUPS)
                    .where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
                    .and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
                    .fetch().map((r) -> {
                        return ConvertHelper.convert(r, Group.class);
                    });

                if (list != null && !list.isEmpty()) {
                    groupList.addAll(list);
                }

                return true;
            });
        
        return groupList;
    }
}
