// @formatter:off
package com.everhomes.pm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

@Component("EhCommunities")
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

        List<PmMember> members = propertyMgrProvider.findPmMemberByCommunityAndTarget(resourceId, 
        		PmTargetType.USER.getCode(), user.getId());
        if(members != null && members.size() > 0) {
        	for(PmMember member : members) {
        		String group = member.getPmGroup();
        		PmGroup pmGroup = PmGroup.fromCode(group);
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
