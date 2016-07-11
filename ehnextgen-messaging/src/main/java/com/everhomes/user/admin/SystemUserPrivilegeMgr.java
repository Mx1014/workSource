package com.everhomes.user.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;






import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

@Component("SystemUser")
public class SystemUserPrivilegeMgr implements UserPrivilegeMgr {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private AclProvider aclProvider;
    @Override
    public void checkUserPrivilege(long userId, long ownerId) {
        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.USER.getCode());
        List<Long> roles = resolver.determineRoleInResource(userId, null, EntityType.USER.getCode(), null);
        if(!this.aclProvider.checkAccess("system", null, EntityType.USER.getCode(),
                userId, Privilege.All, roles))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Insufficient privilege");
    }
    
    
}
