package com.everhomes.bootstrap;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.Acl;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.user.User;
import com.everhomes.util.DateHelper;

/**
 * Server bootstrap
 * 
 * @author Kelven Yang
 *
 */
@Component
public class CoreServerBootstrapBean {
    
    @Autowired
    private PlatformBootstrap platformBootstrap;
    
    @Autowired
    private AclProvider aclProvider;
    
    @PostConstruct
    private void setup() {
        // Create default ACL
        List<Long> roles = aclProvider.getRolesFromResourceAssignments("system", null, EhUsers.class.getSimpleName(), User.ROOT_UID, null);
        if(roles.size() == 0) {
            RoleAssignment assignment = new RoleAssignment();
            assignment.setCreatorUid(User.ROOT_UID);
            assignment.setOwnerType("system");
            assignment.setTargetType(EhUsers.class.getSimpleName());
            assignment.setTargetId(User.ROOT_UID);
            assignment.setRoleId(Role.SystemAdmin);
            assignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createRoleAssignment(assignment);
            
            Acl acl = new Acl();
            acl.setOwnerType("system");
            acl.setPrivilegeId(Privilege.All);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
        }
    }
}
