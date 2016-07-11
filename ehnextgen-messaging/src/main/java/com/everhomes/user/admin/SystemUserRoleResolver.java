package com.everhomes.user.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.everhomes.acl.AclProvider;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.entity.EntityType;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

@Component("EhUsers")
public class SystemUserRoleResolver implements ResourceUserRoleResolver {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private AclProvider aclProvider;
    
    @Override
    public List<Long> determineRoleInResource(long userId, Long ownerId, String resourceType, Long resourceId) {
        List<Long> userInRoles = new ArrayList<>();
        User user = this.userProvider.findUserById(userId);
        if(user != null) {
            userInRoles.add(Role.AuthenticatedUser);
            List<RoleAssignment> roles = aclProvider.getAllRoleAssignments();
            if(roles != null && !roles.isEmpty()){
                roles.forEach(r ->{
                    if(r.getOwnerType().equalsIgnoreCase("system") && 
                            r.getTargetType().equals(EntityType.USER.getCode()) 
                            && r.getTargetId().longValue() == user.getId()){
                        userInRoles.add(Role.SystemAdmin);
                    }
                });
            }
        } else {
            userInRoles.add(Role.Anonymous);
        }
        
        return userInRoles;
    }
    
    
}
