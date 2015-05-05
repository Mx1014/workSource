// @formatter:off
package com.everhomes.pm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

@Component("EhCommunityProp")
public class PropertyUserRoleResolver implements ResourceUserRoleResolver {

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    
    @Override
    public List<Long> determineRoleInResource(long userId, Long ownerId, String resourceType, Long resourceId) {
        User user = this.userProvider.findUserById(userId);
        if(user == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Could not identify the user");
        
        if(user.getCommunityId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "User has not assigned with a community");

//        Community community = this.communityProvider.findCommunityById(user.getCommunityId());
//        if(community == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
//                    "User assigned community does not exist");
            
        List<Long> roles = new ArrayList<>();
        roles.add(Role.ResourceUser);
        
        return roles;
    }
}
