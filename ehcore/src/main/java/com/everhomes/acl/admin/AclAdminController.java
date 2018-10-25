package com.everhomes.acl.admin;




import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.admin.*;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



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
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    
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
     * <b>URL: /admin/acl/listAclRoles</b>
     * <p>查询角色列表</p>
     */
    @RequestMapping("listAclRoles")
    @RestReturn(value=RoleDTO.class, collection = true)
    public RestResponse listAclRoles(ListAclRolesCommand cmd) {
    	
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	List<Role> roles = this.aclProvider.getRolesByApp(cmd.getAppId());
    	List<RoleDTO> roleDto = new ArrayList<RoleDTO>();
    	roleDto = roles.stream().map(r -> {
    		RoleDTO dto = ConvertHelper.convert(r, RoleDTO.class);
    		
    		return dto;
    	}).collect(Collectors.toList());
    	RestResponse response =  new RestResponse(roleDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /admin/acl/listWebMenu</b>
     * <p>查询系统菜单</p>
     */
    @RequestMapping("listWebMenu")
    @RestReturn(value=ListWebMenuResponse.class)
    public RestResponse listWebMenu(@Valid ListWebMenuCommand cmd) {
    	
    	RestResponse response =  new RestResponse(rolePrivilegeService.listWebMenu(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/listWebMenuPrivilege</b>
     * <p>查询权限集</p>
     */
    @RequestMapping("listWebMenuPrivilege")
    @RestReturn(value=ListWebMenuPrivilegeDTO.class, collection = true)
    public RestResponse listWebMenuPrivilege(@Valid ListWebMenuPrivilegeCommand cmd) {
    	RestResponse response =  new RestResponse(rolePrivilegeService.listWebMenuPrivilege(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    
    /**
     * <b>URL: /admin/acl/qryRolePrivileges</b>
     * <p>查看角色权限</p>
     */
    @RequestMapping("qryRolePrivileges")
    @RestReturn(value=ListWebMenuPrivilegeDTO.class, collection = true)
    public RestResponse qryRolePrivileges(@Valid QryRolePrivilegesCommand cmd) {
    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.RolePrivilegeUpdate);
    	rolePrivilegeService.checkAdministrators(cmd.getOrganizationId());
    	RestResponse response =  new RestResponse(rolePrivilegeService.qryRolePrivileges(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/acl/listAclRoleByOrganizationId</b>
     * <p>根据机构获取角色列表</p>
     */
    @RequestMapping("listAclRoleByOrganizationId")
    @RestReturn(value=RoleDTO.class, collection = true)
    public RestResponse listAclRoleByOrganizationId(@Valid ListAclRolesCommand cmd) {
    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.RolePrivilegeList);
    	rolePrivilegeService.checkAdministrators(cmd.getOrganizationId());
    	RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
//    /**
//     * <b>URL: /admin/acl/createOrganizationSuperAdmin </b>
//     * <p>创建超级管理员</p>
//     */
//    @RequestMapping("createOrganizationSuperAdmin")
//    @RestReturn(value=String.class)
//    public RestResponse createOrganizationSuperAdmin(@Valid CreateOrganizationAdminCommand cmd) {
//    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.OrgAdminUpdate);
//    	rolePrivilegeService.createOrganizationSuperAdmin(cmd);
//    	RestResponse response =  new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

//    /**
//     * <b>URL: /admin/acl/createOrganizationOrdinaryAdmin  </b>
//     * <p>创建普通管理员</p>
//     */
//    @RequestMapping("createOrganizationOrdinaryAdmin")
//    @RestReturn(value=String.class)
//    public RestResponse createOrganizationOrdinaryAdmin(@Valid CreateOrganizationAdminCommand cmd) {
//    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.OrgAdminUpdate);
//    	rolePrivilegeService.createOrganizationOrdinaryAdmin(cmd);
//    	RestResponse response =  new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /admin/acl/updateOrganizationSuperAdmin </b>
     * <p>修改超级管理员</p>
     */
    @RequestMapping("updateOrganizationSuperAdmin")
    @RestReturn(value=String.class)
    public RestResponse updateOrganizationSuperAdmin(@Valid UpdateOrganizationAdminCommand cmd) {
    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.OrgAdminUpdate);
    	rolePrivilegeService.updateOrganizationSuperAdmin(cmd);
    	RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/updateOrganizationOrdinaryAdmin  </b>
     * <p>修改普通管理员</p>
     */
    @RequestMapping("updateOrganizationOrdinaryAdmin")
    @RestReturn(value=String.class)
    public RestResponse updateOrganizationOrdinaryAdmin(@Valid UpdateOrganizationAdminCommand cmd) {
    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.OrgAdminUpdate);
    	rolePrivilegeService.updateOrganizationOrdinaryAdmin(cmd);
    	RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    

    /**
     * <b>URL: /admin/acl/listOrganizationAdministrators</b>
     * <p>管理员列表</p>
     */
    @RequestMapping("listOrganizationAdministrators")
    @RestReturn(value=ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrganizationAdministrators(@Valid ListOrganizationAdministratorCommand cmd) {
    	rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.OrgAdminList);
    	ListOrganizationMemberCommandResponse res = rolePrivilegeService.listOrganizationAdministrators(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/deleteAclRoleAssignment</b>
     * <p>删除角色人员</p>
     */
    @RequestMapping("deleteAclRoleAssignment")
    @RestReturn(value=String.class)
    public RestResponse deleteAclRoleAssignment(@Valid DeleteAclRoleAssignmentCommand cmd) {
    	rolePrivilegeService.deleteAclRoleAssignment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/addAclRoleAssignment</b>
     * <p>添加角色人员</p>
     */
    @RequestMapping("addAclRoleAssignment")
    @RestReturn(value=String.class)
    public RestResponse addAclRoleAssignment(@Valid AddAclRoleAssignmentCommand cmd) {
    	rolePrivilegeService.addAclRoleAssignment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/batchAddTargetRoles</b>
     * <p>设置角色人员</p>
     */
    @RequestMapping("batchAddTargetRoles")
    @RestReturn(value=String.class)
    public RestResponse batchAddTargetRoles(@Valid BatchAddTargetRoleCommand cmd) {
    	rolePrivilegeService.batchAddTargetRoles(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/exportRoleAssignmentPersonnelXls</b>
     * <p>导出角色人员</p>
     */
    @RequestMapping("exportRoleAssignmentPersonnelXls")
    @RestReturn(value=String.class)
    public RestResponse exportRoleAssignmentPersonnelXls(@Valid ExcelRoleExcelRoleAssignmentPersonnelCommand cmd, HttpServletResponse httpResponse) {
    	rolePrivilegeService.exportRoleAssignmentPersonnelXls(cmd, httpResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/importRoleAssignmentPersonnelXls</b>
     * <p>导入角色人员</p>
     */
    @RequestMapping("importRoleAssignmentPersonnelXls")
    @RestReturn(value=String.class)
    public RestResponse importRoleAssignmentPersonnelXls(@Valid ExcelRoleExcelRoleAssignmentPersonnelCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        RestResponse response = new RestResponse(rolePrivilegeService.importRoleAssignmentPersonnelXls(cmd, files));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/acl/findTopAdminByOrgId</b>
     * <p>找到公司的超级管理员</p>
     */
    @RequestMapping("findTopAdminByOrgId")
    @RestReturn(value=String.class)
    public RestResponse findTopAdminByOrgId(FindTopAdminByOrgIdCommand cmd) {
    	rolePrivilegeService.findTopAdminByOrgId(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
