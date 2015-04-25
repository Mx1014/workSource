// @formatter:off
package com.everhomes.family;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberAdminStatus;
import com.everhomes.group.GroupPrivacy;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

/**
 * Family inherits from Group for family member management. GroupDiscriminator.FAMILY
 * distinguishes it from other group objects 
 * 
 */
@Component
public class FamilyProviderImpl implements FamilyProvider {
    
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

    @Override
    public Family findFamilyByAddressId(long addressId) {
        final Family[] result = new Family[1];
        dbProvider.mapReduce(AccessSpec.readWriteWith(EhGroups.class), result, 
        (DSLContext context, Object reducingContext) -> {
            result[0] = context.select().from(Tables.EH_GROUPS)
                .where(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(addressId))
                .and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
                .fetchOne().map((r) -> {
                    return ConvertHelper.convert(r, Family.class);
                });
            
            if(result[0] != null)
                return false;
            
            return true;
        });
        
        return result[0];
    }
    
    @Override
    public Family getOrCreatefamily(Address address)      {
        final Long uid = UserContext.current().getUser().getId();
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
                    m.setMemberType(EhUsers.class.getSimpleName());
                    m.setMemberId(uid);
                    m.setMemberRole(Role.ResourceCreator);
                    m.setMemberStatus(GroupMemberAdminStatus.WAITING_FOR_APPROVAL.getCode());
                    m.setCreatorUid(uid);
                    this.groupProvider.createGroupMember(m);
                    
                    UserGroup userGroup = new UserGroup();
                    userGroup.setOwnerUid(uid);
                    userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    userGroup.setGroupId(f.getId());
                    userGroup.setRegionScope(RegionScope.NEIGHBORHOOD.getCode());
                    userGroup.setRegionScopeId(community.getId());
                    userGroup.setMemberRole(Role.ResourceCreator);
                    userGroup.setMemberStatus(GroupMemberAdminStatus.WAITING_FOR_APPROVAL.getCode());
                    this.userProvider.createUserGroup(userGroup);
                    
                    return f;
                });
            } else {
                GroupMember m = new GroupMember();
                m.setGroupId(family.getId());
                m.setMemberType(EhUsers.class.getSimpleName());
                m.setMemberId(uid);
                m.setMemberRole(Role.ResourceUser);
                m.setMemberStatus(GroupMemberAdminStatus.WAITING_FOR_APPROVAL.getCode());
                m.setCreatorUid(uid);
                this.groupProvider.createGroupMember(m);
                
                UserGroup userGroup = new UserGroup();
                userGroup.setOwnerUid(uid);
                userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                userGroup.setGroupId(family.getId());
                userGroup.setRegionScope(RegionScope.NEIGHBORHOOD.getCode());
                userGroup.setRegionScopeId(community.getId());
                userGroup.setMemberRole(Role.ResourceUser);
                userGroup.setMemberStatus(GroupMemberAdminStatus.WAITING_FOR_APPROVAL.getCode());
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
               EhUsers.class.getSimpleName(), userGroup.getOwnerUid());
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
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    String likeVal = keyword + "%";
                    context.selectDistinct(Tables.EH_ADDRESSES.ID, Tables.EH_ADDRESSES.ADDRESS, Tables.EH_ADDRESSES.COMMUNITY_ID,
                        Tables.EH_ADDRESSES.CITY_ID,Tables.EH_COMMUNITIES.NAME,Tables.EH_REGIONS.NAME)
                        .from(Tables.EH_ADDRESSES)
                        .leftOuterJoin(Tables.EH_COMMUNITIES)
                        .on(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(Tables.EH_COMMUNITIES.ID))
                        .leftOuterJoin(Tables.EH_REGIONS)
                        .on(Tables.EH_ADDRESSES.CITY_ID.equal(Tables.EH_REGIONS.ID))
                        .where((Tables.EH_ADDRESSES.ADDRESS.like(likeVal)
                            .or(Tables.EH_ADDRESSES.ADDRESS_ALIAS.like(likeVal)))
                            .and(Tables.EH_REGIONS.SCOPE_CODE.eq(RegionScope.CITY.getCode())))
                        .fetch().map((r) -> {
                            FamilyDTO family = new FamilyDTO();
                            family.setId(r.getValue(Tables.EH_ADDRESSES.ID));
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
}
