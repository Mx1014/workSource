// @formatter:off
package com.everhomes.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.lucene.spatial.DistanceUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
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
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;


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
        
        Tuple<Family, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_FAMILY.getCode()).enter(()-> {
            Family family = findFamilyByAddressId(address.getId());
            if(family == null) {
                family = this.dbProvider.execute((TransactionStatus status) -> {
                    Family f = new Family();
                    f.setName(address.getApartmentName());
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
            
            return family;  
        });
        
        if(result.second())
            return result.first();
        
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
                family = findFamilyByAddressId(address.getId());
                
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
                        Tables.EH_ADDRESSES.CITY_ID,Tables.EH_COMMUNITIES.NAME,Tables.EH_REGIONS.NAME)
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
                            family.setCityName(r.getValue(Tables.EH_REGIONS.NAME));
                            results.add(family);
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<FamilyDTO>>(ErrorCodes.SUCCESS, results);
    }
    
    @Override
    public void joinFamily(long familyId) {
    	User user = UserContext.current().getUser();
    	long userId = user.getId();
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
		    userGroup.setRegionScopeId(familyId);
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
        Family f = findFamilyByAddressId(addressId);
        if(f == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                       "Invalid addressId parameter");
        if(f.getId() != familyId)
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
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select(Tables.EH_GROUPS.ID,Tables.EH_GROUPS.NAME,Tables.EH_GROUPS.AVATAR,Tables.EH_GROUPS.CREATOR_UID,
                            Tables.EH_GROUPS.DISPLAY_NAME,Tables.EH_GROUPS.MEMBER_COUNT,Tables.EH_COMMUNITIES.CITY_ID,
                            Tables.EH_COMMUNITIES.ID,Tables.EH_COMMUNITIES.NAME,Tables.EH_COMMUNITIES.CITY_NAME)
                        .from(Tables.EH_GROUPS)
                        .leftOuterJoin(Tables.EH_COMMUNITIES)
                        .on(Tables.EH_GROUPS.INTEGRAL_TAG2.equal(Tables.EH_COMMUNITIES.ID))
                        .where(Tables.EH_GROUPS.ID.in(familyIds))
                        .fetch().map((r) -> {
                            FamilyDTO family = new FamilyDTO();
                            family.setId(r.getValue(Tables.EH_GROUPS.ID));
                            family.setName(r.getValue(Tables.EH_GROUPS.NAME));
                            family.setCommunityId(r.getValue(Tables.EH_COMMUNITIES.ID));
                            family.setCommunityName(r.getValue(Tables.EH_COMMUNITIES.NAME));
                            family.setCityId(r.getValue(Tables.EH_COMMUNITIES.CITY_ID));
                            family.setCityName(r.getValue(Tables.EH_COMMUNITIES.CITY_NAME));
                            family.setMemberCount(r.getValue(Tables.EH_GROUPS.MEMBER_COUNT));
                            family.setAvatar(r.getValue(Tables.EH_GROUPS.AVATAR));
                            if(r.getValue(Tables.EH_GROUPS.CREATOR_UID) == userId)
                                family.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());
                            
                            GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
                                    EntityType.USER.getCode(), userId);
                            if(member != null)
                                family.setMembershipStatus(member.getMemberStatus());
                            familyList.add(family);
                            return null;
                        });
                    
                return true;
            });
        
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
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        List<UserGroup> list = this.userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());
        list = list.stream().filter((userGroup) ->{
            return userGroup.getGroupId() == group.getId();
            
        }).collect(Collectors.toList());
        
        leaveFamilyAtAddress(address, list.get(0));
        
    }

    @Override
    public FamilyDTO getFamilyById(Long familyId) {
        
        final FamilyDTO[] result = new FamilyDTO[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select(Tables.EH_GROUPS.ID,Tables.EH_GROUPS.NAME,Tables.EH_GROUPS.AVATAR,Tables.EH_GROUPS.CREATOR_UID,
                            Tables.EH_GROUPS.DISPLAY_NAME,Tables.EH_GROUPS.MEMBER_COUNT,Tables.EH_COMMUNITIES.CITY_ID,
                            Tables.EH_COMMUNITIES.ID,Tables.EH_COMMUNITIES.NAME,Tables.EH_COMMUNITIES.CITY_NAME)
                        .from(Tables.EH_GROUPS)
                        .leftOuterJoin(Tables.EH_COMMUNITIES)
                        .on(Tables.EH_GROUPS.INTEGRAL_TAG2.equal(Tables.EH_COMMUNITIES.ID))
                        .where(Tables.EH_GROUPS.ID.eq(familyId))
                        .fetch().map((r) -> {
                            FamilyDTO family = new FamilyDTO();
                            family.setId(r.getValue(Tables.EH_GROUPS.ID));
                            family.setName(r.getValue(Tables.EH_GROUPS.NAME));
                            family.setCommunityId(r.getValue(Tables.EH_COMMUNITIES.ID));
                            family.setCommunityName(r.getValue(Tables.EH_COMMUNITIES.NAME));
                            family.setCityId(r.getValue(Tables.EH_COMMUNITIES.CITY_ID));
                            family.setCityName(r.getValue(Tables.EH_COMMUNITIES.CITY_NAME));
                            family.setMemberCount(r.getValue(Tables.EH_GROUPS.MEMBER_COUNT));
                            family.setAvatar(r.getValue(Tables.EH_GROUPS.AVATAR));
                            
                            result[0] = family;
                            return null;
                        });
                    
                return true;
            });
        
        return result[0];
    }

    @Override
    public void ejectMember(Long familyId, Long memberUid, String reason) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        //剔除别人，是否保留历史??

        if(familyId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        if(memberUid == userId)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid memberUid parameter,can not eject youself");
        Group group = this.groupProvider.findGroupById(familyId);
        
        Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
        List<UserGroup> list = this.userProvider.listUserGroups(memberUid, GroupDiscriminator.FAMILY.getCode());
        list = list.stream().filter((userGroup) ->{
            return userGroup.getGroupId() == group.getId();
            
        }).collect(Collectors.toList());
        
        leaveFamilyAtAddress(address, list.get(0));
        
    }

    @Override
    public void approveMember(Long familyId, Long memberUid) {
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,family is not found.");
        }
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), memberUid);
        if(member == null){
            LOGGER.error("Invalid memberUid parameter,user not apply join in family.memberUid=" + memberUid);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid memberUid parameter,can not eject youself");
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
        
        if(flag)
            return;
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Unable to save into database, SQL exception or storage full?");
        //??发送更新命令
    }

    @Override
    public List<FamilyMemberDTO> listOwningFamilyMembers(Long familyId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(familyId, 
                EntityType.USER.getCode(), userId);
        if(member == null){
            LOGGER.error("Invalid familyId parameter,user not in family.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,user not in family.");
        }
        ListingLocator locator = new ListingLocator();
        locator.setEntityId(familyId);
        
        
        List<GroupMember> groupMemberList = this.groupProvider.queryGroupMembers(locator, Integer.MAX_VALUE, null);
        List<FamilyMemberDTO> results = new ArrayList<FamilyMemberDTO>();
        groupMemberList.stream().forEach((groupMember) -> {
            FamilyMemberDTO f = new FamilyMemberDTO();
            f.setFamilyId(groupMember.getGroupId());
            f.setId(groupMember.getId());
            f.setMemberUid(groupMember.getMemberId());
            f.setMemberName(groupMember.getMemberNickName());
            f.setMemberAvatar(groupMember.getMemberAvatar());
            results.add(f);

        });
        return results;
    }

    @Override
    public void setCurrentFamily(Long familyId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        User u = this.userProvider.findUserById(userId);
        Address address = getAddressByFamilyId(familyId);
        if(address != null){
            u.setAddressId(address.getId());
            u.setAddress(address.getAddress());
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
        if(cmd == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter");
        }
        
        if(cmd.getFamilyId() == null){
            LOGGER.error("Invalid familyId parameter,familyId=" + cmd.getFamilyId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid family parameter");
        }
        
        Group group = this.groupProvider.findGroupById(cmd.getFamilyId());
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found,familyId=" + cmd.getFamilyId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        }
        if(!StringUtils.isEmpty(cmd.getFamilyAvatar())){
            group.setAvatar(cmd.getFamilyAvatar());
        }
        if(!StringUtils.isEmpty(cmd.getFamilyDescription())){
            group.setDescription(cmd.getFamilyDescription());
        }
        if(!StringUtils.isEmpty(cmd.getFamilyName())){
            group.setName(cmd.getFamilyName());
        }
        this.groupProvider.updateGroup(group);
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(cmd.getFamilyId(), 
                EntityType.USER.getCode(), userId);
        if(member == null)
            return;
        if(!StringUtils.isEmpty(cmd.getMemberAvatar())){
            member.setMemberAvatar(cmd.getMemberAvatar());
        }
        if(!StringUtils.isEmpty(cmd.getMemberNickName())){
            member.setMemberNickName(cmd.getMemberNickName());
        }
        this.groupProvider.updateGroupMember(member);
        //更新之后，发送命令
    }

    @Override
    public List<FamilyMembershipRequestDTO> listFamilyRequests(Long familyId, Long pageOffset) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        Group group = this.groupProvider.findGroupById(familyId);
        if(group == null){
            LOGGER.error("Invalid familyId parameter,family is not found,familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter");
        }
        if(pageOffset == null){
            LOGGER.warn("Invalid pageOffset parameter,pageOffset=" + pageOffset);
            pageOffset = 0L;
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
                           member.setFamilyAvatar(group.getAvatar());
                           member.setFamilyName(group.getName());
                           member.setRequestorUid(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
                           member.setRequestorAvatar(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR));
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
    public List<NeighborUserDTO> listNeighborUsers(Long familyId, Long pageOffset) {
        User user = UserContext.current().getUser();
        if(user.getAddressId() == null || user.getAddressId() == 0){
            LOGGER.error("User has not address.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User has not address.");
        }
        
        Family family = findFamilyByAddressId(user.getAddressId());
        if(family == null || family.getId() != familyId){
            LOGGER.error("Invalid familyId parameter,user not in family.familyId=" + familyId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,user not in family.");
        }
        
        List<Long> addresIds = new ArrayList<Long>();
        long communityId = family.getCommunityId();
        List<NeighborUserDetailDTO> userDetailList = new ArrayList<NeighborUserDetailDTO>();
        Address myaddress = this.addressProvider.findAddressById(user.getAddressId());;
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select(Tables.EH_ADDRESSES.ID).from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
                        .fetch().map( (r) ->{
                            //排除自己
                            if(r.getValue(Tables.EH_ADDRESSES.ID) != user.getAddressId())
                                addresIds.add(r.getValue(Tables.EH_ADDRESSES.ID));
                            
                            return null;
                        });
                    
                    if(addresIds.size() > 0){
                        context.select(Tables.EH_USERS.ID,Tables.EH_USERS.STATUS_LINE,Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME
                                ,Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR,Tables.EH_ADDRESSES.APARTMENT_FLOOR,Tables.EH_ADDRESSES.BUILDING_NAME)
                        .from(Tables.EH_GROUP_MEMBERS)
                        .leftOuterJoin(Tables.EH_GROUPS).on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
                        .leftOuterJoin(Tables.EH_ADDRESSES).on(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(Tables.EH_ADDRESSES.ID))
                        .leftOuterJoin(Tables.EH_USERS).on(Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(Tables.EH_USERS.ID))
                        .where(Tables.EH_ADDRESSES.ID.in(addresIds))
                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode()))
                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.notEqual(user.getId()))
                        .fetch().map( (r) ->{
                            NeighborUserDetailDTO n = new NeighborUserDetailDTO();
                            n.setUserId(r.getValue(Tables.EH_USERS.ID));
                            n.setUserStatusLine(r.getValue(Tables.EH_USERS.STATUS_LINE));
                            n.setUserName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
                            n.setUserAvatar(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR));
                            n.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
                            n.setApartmentFloor(r.getValue(Tables.EH_ADDRESSES.APARTMENT_FLOOR));
                            userDetailList.add(n);
                            return null;
                        });
                        
                    }
                   
                    return true;
                });
        pageOffset = pageOffset == null ? 1 : pageOffset;
        return processNeighborUserInfo(userDetailList,myaddress,pageOffset);
    }

    private List<NeighborUserDTO> processNeighborUserInfo(List<NeighborUserDetailDTO> userDetailList,Address myaddress,long pageOffset) {
        if(userDetailList == null || userDetailList.isEmpty())
            return null;

        List<NeighborUserDTO> results = new ArrayList<NeighborUserDTO>();
        for(NeighborUserDetailDTO dto : userDetailList){
            NeighborUserDTO user = ConvertHelper.convert(dto, NeighborUserDTO.class);
            if(myaddress.getApartmentFloor() == null || dto.getApartmentFloor() == null){
                if(myaddress.getApartmentFloor().equals(dto.getApartmentFloor()))
                    user.setNeighborhoodRelation((byte) 1);
                else if(myaddress.getBuildingName().equals(dto.getBuildingName())){
                    user.setNeighborhoodRelation((byte) 2);
                }
            }
            
            else
                user.setNeighborhoodRelation((byte) 0);
            results.add(user);
        }
        
        final int pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        int totalCount = results == null ? 0 : results.size();
        int totalPage = totalCount / pageSize;
        if ((totalCount % pageSize) > 0)
        {
            totalPage++;
        }
        int currentPage = (int) pageOffset;
        if(pageOffset <= totalPage){
            sortNeighborUser(results);
            int endIndex = Math.min(results.size(), currentPage*pageSize);
            results = results.subList((currentPage-1)*pageSize,endIndex);
        }
        
        return results;
        
    }

    @Override
    public List<NeighborUserDTO> listNearbyNeighborUsers(ListNearbyNeighborUserCommand cmd) {
        
        if(cmd.getLatitude() == null || cmd.getLongitude() == null){
            LOGGER.error("Invalid parameter.latitude or longitude is null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter,latitude or longitude is null.");
        }
        User user = UserContext.current().getUser();
        if(user.getAddressId() == null || user.getAddressId() == 0){
            LOGGER.error("User has not address.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "User has not address.");
        }
        
        Family family = findFamilyByAddressId(user.getAddressId());
        if(family == null || (cmd.getFamilyId() != null && family.getId() != cmd.getFamilyId())){
            LOGGER.error("Invalid familyId parameter,user not in family.familyId=" + cmd.getFamilyId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid familyId parameter,user not in family.");
        }
            
        long communityId = family.getCommunityId();
        List<Long> userIds = new ArrayList<Long>();
        List<NeighborUserDTO> results = new ArrayList<NeighborUserDTO>();
        
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
                        String geoHash = GeoHashUtils.encode(cmd.getLongitude(), cmd.getLatitude());
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
                            n.setUserAvatar(r.getValue(Tables.EH_USERS.AVATAR));
                            //计算距离
                            double lat = r.getValue(Tables.EH_USER_LOCATIONS.LATITUDE);
                            double lon = r.getValue(Tables.EH_USER_LOCATIONS.LONGITUDE);
                            double distince = DistanceUtils.getDistanceMi(cmd.getLatitude(),cmd.getLongitude(),lat , lon);
                            n.setDistance(distince);
                            results.add(n);
                            return null;
                        });
                        
                    }
                   
                    return true;
                });
        long pageOffset = cmd.getPageOffset() == null? 0 : cmd.getPageOffset();
        
        processNeighborUserInfo(results,pageOffset);
        
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
}
