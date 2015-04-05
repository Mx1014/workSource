// @formatter:off
package com.everhomes.family;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.Group.GroupMemberAdminStatus;
import com.everhomes.Group.GroupPrivacy;
import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.Community;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.CoordinationLocks;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.server.schema.Tables;
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
    
    @Override
    public Family getOrCreatefamily(Address address)      {
        final Long uid = UserContext.current().getUser().getId();
        Community community = this.addressProvider.findCommunityById(address.getCommunityId());
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
                    f.setMemberCount(1L);
                    f.setRegionScope(RegionScope.NEIGHBORHOOD.getCode());
                    f.setRegionScopeId(address.getCommunityId());
                    f.setLeafRegionPath(region.getPath());
                    
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
                    userGroup.setLeafRegionPath(region.getPath());
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
                
                family.setMemberCount(family.getMemberCount() + 1);
                this.groupProvider.updateGroup(family);
                
                UserGroup userGroup = new UserGroup();
                userGroup.setOwnerUid(uid);
                userGroup.setGroupDiscriminator(GroupDiscriminator.FAMILY.getCode());
                userGroup.setGroupId(family.getId());
                userGroup.setRegionScope(RegionScope.NEIGHBORHOOD.getCode());
                userGroup.setRegionScopeId(community.getId());
                userGroup.setLeafRegionPath(region.getPath());
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
    
    private Family findFamilyByAddressId(long addressId) {
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
}
