package com.everhomes.acl.admin;




import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;



@RestDoc(value="acl controller", site="core")
@RestController
@RequestMapping("/admin/acl")
public class AclAdminController extends ControllerBase {
    
    @Autowired
    private AclProvider aclProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private UserProvider userProvider;
    /**
     * <b>URL: /admin/acl/assignUserRole</b>
     * <p>分配用户角色</p>
     */
    @RequestMapping("assignUserRole")
    @RestReturn(value=String.class)
    public RestResponse assignUserRole(@Valid AssignUserRoleAdminCommand cmd) {
        if(cmd.getOwnerType() == null || cmd.getTargetType() == null || cmd.getTargetId() == null 
                || cmd.getRoleId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter ownerType or targetType or targetId or roleId.");
        }
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        User user = UserContext.current().getUser();
        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setCreatorUid(user.getId());
        roleAssignment.setOwnerId(cmd.getOwnerId());
        roleAssignment.setOwnerType(cmd.getOwnerType());
        roleAssignment.setRoleId(cmd.getRoleId());
        roleAssignment.setTargetType(cmd.getTargetType());
        roleAssignment.setTargetId(cmd.getTargetId());
        roleAssignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        aclProvider.createRoleAssignment(roleAssignment);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/deleteUserRole</b>
     * <p>移除用户角色</p>
     */
    @RequestMapping("deleteUserRole")
    @RestReturn(value=String.class)
    public RestResponse deleteConfiguration(@Valid DeleteUserRoleAdminCommand cmd) {
        if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter id.");
        }
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.aclProvider.deleteRoleAssignment(cmd.getId());
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/listUserRoles</b>
     * <p>查询用户角色列表</p>
     */
    @RequestMapping("listUserRoles")
    @RestReturn(value=ListUserRolesAdminCommandResponse.class)
    public RestResponse listConfigurations(){
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ListUserRolesAdminCommandResponse response = new ListUserRolesAdminCommandResponse();
        List<RoleAssignment> roleAssignments = this.aclProvider.getAllRoleAssignments();
        List<AclRoleAssignmentsDTO> result = new ArrayList<AclRoleAssignmentsDTO>();
        result = roleAssignments.stream().map(r -> {
            
            AclRoleAssignmentsDTO dto = ConvertHelper.convert(r, AclRoleAssignmentsDTO.class);
            if(r.getTargetType().equals(EntityType.USER.getCode())){
                User user = this.userProvider.findUserById(r.getTargetId());
                if(user != null){
                    dto.setTargetName(user.getNickName());
                }
            }
            return dto;
        }).collect(Collectors.toList());
        response.setRequests(result);
        
        return new RestResponse(response);
    }
    
    /**
     * <b>URL: /admin/acl/listUserRoles</b>
     * <p>查询角色列表</p>
     */
    @RequestMapping("listAclRoles")
    @RestReturn(value=Role.class, collection = true)
    public RestResponse listAclRoles(ListAclRolesCommand cmd) {
    	
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	List<Role> roles = this.aclProvider.getRolesByApp(cmd.getAppId());
    	
    	RestResponse response =  new RestResponse(roles);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
