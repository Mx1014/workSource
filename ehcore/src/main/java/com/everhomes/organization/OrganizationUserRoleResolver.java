package com.everhomes.organization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.entity.EntityType;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

@Component("EhOrganizations")
public class OrganizationUserRoleResolver implements ResourceUserRoleResolver {

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Override
    public List<Long> determineRoleInResource(long userId, Long ownerId, String resourceType, Long resourceId) {
        List<Long> userInRoles = new ArrayList<>();
        User user = this.userProvider.findUserById(userId);
        if(user != null) {
            userInRoles.add(Role.AuthenticatedUser);
            
            Organization organization = organizationProvider.findOrganizationById(ownerId);
            
//            GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(ownerId.longValue(), 
//                    EntityType.USER.getCode(), user.getId());
//            if(member != null) {
//                userInRoles.add(member.getMemberRole());
//                if(member.getMemberRole().longValue() == Role.ResourceAdmin || 
//                    member.getMemberRole().longValue() == Role.ResourceOperator ||
//                    member.getMemberRole().longValue() == Role.ResourceCreator) {
//                    if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.ACTIVE)
//                        userInRoles.add(Role.ResourceUser);
//                }
//                
//                if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.ACTIVE) {
//                	if(member.getMemberRole().longValue() == Role.ResourceAdmin || 
//                            member.getMemberRole().longValue() == Role.ResourceOperator ||
//                            member.getMemberRole().longValue() == Role.ResourceCreator) {
//                		userInRoles.add(Role.ResourceUser);
//                	}
//                	
//                	// The resource creator is default to be admin
//                	if(member.getMemberRole().longValue() == Role.ResourceCreator) {
//                		userInRoles.add(Role.ResourceAdmin);
//                	}
//                }
//            }
        } else {
            userInRoles.add(Role.Anonymous);
        }
        
        return userInRoles;
    }
}
