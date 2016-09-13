// @formatter:off
package com.everhomes.bootstrap;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.acl.Acl;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.rest.rpc.server.ClientForwardPdu;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PingRequestPdu;
import com.everhomes.rest.rpc.server.PingResponsePdu;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;
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
public class CoreServerBootstrapBean implements ApplicationListener<ApplicationEvent> {
    //未用到这个依赖，先注释掉，否则生成rest包时报错，update by tt, 20160913
//    @Autowired
//    private PlatformBootstrap platformBootstrap;
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private LocalBusMessageClassRegistry messageClassRegistry;
    
    @Autowired
    private AppProvider appProvider;
    
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
            acl.setRoleId(Role.SystemAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType("extension");
            acl.setPrivilegeId(Privilege.Visible);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.SystemExtension);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // setup default GROUP resource ACL
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.Create);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.AuthenticatedUser);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.Write);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceCreator);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.Delete);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceCreator);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // everyone in the group can invite friends to join the group
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupInviteJoin);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceUser);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // Creator/admin can update group member info
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupUpdateMember);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceCreator);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupUpdateMember);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // Only admin can approve group member request
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupApproveMember);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // Only admin can reject group member request
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupRejectMember);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // Only admin can revoke group member
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupRevokeMember);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupListMember);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceUser);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupRequestAdminRole);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceUser);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupInviteAdminRole);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.GROUP.getCode());
            acl.setPrivilegeId(PrivilegeConstants.GroupAdminOps);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            // setup default FORUM resource ACL
            acl = new Acl();
            acl.setOwnerType(EntityType.FORUM.getCode());
            acl.setPrivilegeId(PrivilegeConstants.ForumNewTopic);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceUser);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);

            acl = new Acl();
            acl.setOwnerType(EntityType.FORUM.getCode());
            acl.setPrivilegeId(PrivilegeConstants.ForumDeleteTopic);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceCreator);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);

            acl = new Acl();
            acl.setOwnerType(EntityType.FORUM.getCode());
            acl.setPrivilegeId(PrivilegeConstants.ForumDeleteTopic);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.FORUM.getCode());
            acl.setPrivilegeId(PrivilegeConstants.ForumNewReply);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceUser);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
            
            acl = new Acl();
            acl.setOwnerType(EntityType.FORUM.getCode());
            acl.setPrivilegeId(PrivilegeConstants.ForumDeleteReply);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceCreator);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);

            acl = new Acl();
            acl.setOwnerType(EntityType.FORUM.getCode());
            acl.setPrivilegeId(PrivilegeConstants.ForumDeleteReply);
            acl.setGrantType((byte)1);
            acl.setCreatorUid(User.ROOT_UID);
            acl.setRoleId(Role.ResourceAdmin);
            acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.aclProvider.createAcl(acl);
        }
        
        App app = this.appProvider.findAppById(App.APPID_EXTENSION);
        if(app == null) {
            app = new App();
            app.setId(App.APPID_EXTENSION);
            app.setAppKey("b86ddb3b-ac77-4a65-ae03-7e8482a3db70");
            app.setSecretKey("2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ");
            app.setName("SystemExtension");
            app.setStatus((byte)1);
            this.appProvider.createApp(app);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) {
            if(messageClassRegistry.getPackages().isEmpty()) {
                messageClassRegistry.addPackage("com.everhomes");
                messageClassRegistry.scan(this.getClass().getClassLoader());
            }
            
            // package scan does not work in one jar solution provided in Spring Boot
            // still have to resgister class in other jar manually here
            messageClassRegistry.registerNameAnnotatedClass(PingRequestPdu.class);
            messageClassRegistry.registerNameAnnotatedClass(PingResponsePdu.class);
            messageClassRegistry.registerNameAnnotatedClass(DeviceRequestPdu.class);
            messageClassRegistry.registerNameAnnotatedClass(AclinkRemotePdu.class);
            messageClassRegistry.registerNameAnnotatedClass(ClientForwardPdu.class);
            messageClassRegistry.registerNameAnnotatedClass(PusherNotifyPdu.class);
            
            setup();
        }
    }
}
