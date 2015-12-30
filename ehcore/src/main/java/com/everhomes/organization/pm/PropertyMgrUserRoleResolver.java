// @formatter:off
package com.everhomes.organization.pm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.organization.pm.PmMemberGroup;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

public class PropertyMgrUserRoleResolver implements ResourceUserRoleResolver {

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
	private PropertyMgrProvider propertyMgrProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Override
    public List<Long> determineRoleInResource(long userId, Long ownerId, String resourceType, Long resourceId) {
    	List<Long> roles = new ArrayList<Long>();
        
        Community community = this.communityProvider.findCommunityById(resourceId);
        if(community == null) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "The property management related community does not exist");
        }
    	
        User user = this.userProvider.findUserById(userId);
        if(user == null) {
        	roles.add(Role.Anonymous);
        } else {
        	roles.add(Role.AuthenticatedUser);
        }

        List<CommunityPmMember> members = propertyMgrProvider.findPmMemberByCommunityAndTarget(resourceId, 
        		PmTargetType.USER.getCode(), user.getId());
        if(members != null && members.size() > 0) {
        	for(CommunityPmMember member : members) {
        		String group = member.getMemberGroup();
        		PmMemberGroup pmGroup = PmMemberGroup.fromCode(group);
        		switch(pmGroup) {
        		case MANAGER:
        			// TODO: add the specific role
        			//userInRoles.add(Role.Anonymous);
        			break;
        		}
        	}
        }
        
        return roles;
    }
}
