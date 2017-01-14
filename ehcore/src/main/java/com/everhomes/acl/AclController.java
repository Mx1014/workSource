package com.everhomes.acl;


import java.util.List;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.admin.*;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestDoc(value="acl controller", site="core")
@RestController
@RequestMapping("/acl")
public class AclController extends ControllerBase {
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private ServiceModuleService serviceModuleService;


    /**
     * <b>URL: /acl/createOrganizationAdmin</b>
     * <p>创建公司管理员</p>
     */
    @RequestMapping("createOrganizationAdmin")
    @RestReturn(value=String.class)
    public RestResponse createOrganizationAdmin(@Valid CreateOrganizationAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if(!resolver.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE)){
            resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ENTERPRISE_ADMIN_MANAGE);
        }
        rolePrivilegeService.createOrganizationAdmin(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/createOrganizationSuperAdmin</b>
     * <p>创建超级管理员</p>
     */
    @RequestMapping("createOrganizationSuperAdmin")
    @RestReturn(value=String.class)
    public RestResponse createOrganizationSuperAdmin(@Valid CreateOrganizationAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE);
        rolePrivilegeService.createOrganizationSuperAdmin(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/createServiceModuleAdmin</b>
     * <p>创建业务模块管理员</p>
     */
    @RequestMapping("createServiceModuleAdmin")
    @RestReturn(value=String.class)
    public RestResponse createServiceModuleAdmin(@Valid CreateServiceModuleAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.ADMIN_MANAGE);
        rolePrivilegeService.createServiceModuleAdmin(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/authorizationServiceModule</b>
     * <p>业务模块授权</p>
     */
    @RequestMapping("authorizationServiceModule")
    @RestReturn(value=String.class)
    public RestResponse authorizationServiceModule(@Valid AuthorizationServiceModuleCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
        rolePrivilegeService.authorizationServiceModule(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listServiceModuleAdministrators</b>
     * <p>业务模块管理员列表</p>
     */
    @RequestMapping("listServiceModuleAdministrators")
    @RestReturn(value=OrganizationContactDTO.class, collection = true)
    public RestResponse listServiceModuleAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE);
        RestResponse response = new RestResponse(rolePrivilegeService.listServiceModuleAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteServiceModuleAdministrators</b>
     * <p>删除业务模块管理员</p>
     */
    @RequestMapping("deleteServiceModuleAdministrators")
    @RestReturn(value=OrganizationContactDTO.class, collection = true)
    public RestResponse deleteServiceModuleAdministrators(@Valid DeleteServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.ADMIN_MANAGE);
        rolePrivilegeService.deleteServiceModuleAdministrators(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listAuthorizationServiceModules</b>
     * <p>业务模块授权列表</p>
     */
    @RequestMapping("listAuthorizationServiceModules")
    @RestReturn(value=AuthorizationServiceModuleDTO.class, collection = true)
    public RestResponse listAuthorizationServiceModules(@Valid ListAuthorizationServiceModuleCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
        RestResponse response = new RestResponse(rolePrivilegeService.listAuthorizationServiceModules(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listServiceModules</b>
     * <p>业务模块列表</p>
     */
    @RequestMapping("listServiceModules")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModules(@Valid ListServiceModulesCommand cmd) {
    	List<ServiceModuleDTO> dto = serviceModuleService.listServiceModules(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listTreeServiceModules</b>
     * <p>业务模块树列表</p>
     */
    @RequestMapping("listTreeServiceModules")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse listTreeServiceModules(@Valid ListServiceModulesCommand cmd) {
    	List<ServiceModuleDTO> dto = serviceModuleService.listTreeServiceModules(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/listAuthorizationServiceModuleMembers</b>
     * <p>业务模块授权人员列表</p>
     */
    @RequestMapping("listAuthorizationServiceModuleMembers")
    @RestReturn(value=AuthorizationServiceModuleMembersDTO.class, collection = true)
    public RestResponse listAuthorizationServiceModuleMembers(@Valid ListAuthorizationServiceModuleCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
        RestResponse response = new RestResponse(rolePrivilegeService.listAuthorizationServiceModuleMembers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteOrganizationAdministrators</b>
     * <p>删除公司管理员</p>
     */
    @RequestMapping("deleteOrganizationAdministrators")
    @RestReturn(value=String.class)
    public RestResponse deleteOrganizationAdministrators(@Valid DeleteOrganizationAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if(!resolver.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE)){
            resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ENTERPRISE_ADMIN_MANAGE);
        }
        rolePrivilegeService.deleteOrganizationAdministrators(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteOrganizationSuperAdministrators</b>
     * <p>删除超级管理员</p>
     */
    @RequestMapping("deleteOrganizationSuperAdministrators")
    @RestReturn(value=String.class)
    public RestResponse deleteOrganizationSuperAdministrators(@Valid DeleteOrganizationAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE);
        rolePrivilegeService.deleteOrganizationSuperAdministrators(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteAuthorizationServiceModule</b>
     * <p>删除业务模块授权</p>
     */
    @RequestMapping("deleteAuthorizationServiceModule")
    @RestReturn(value=String.class)
    public RestResponse deleteAuthorizationServiceModule(@Valid DeleteAuthorizationServiceModuleCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
        rolePrivilegeService.deleteAuthorizationServiceModule(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listOrganizationSuperAdministrators</b>
     * <p>超级管理员列表</p>
     */
    @RequestMapping("listOrganizationSuperAdministrators")
    @RestReturn(value=OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationSuperAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE);
        RestResponse response = new RestResponse(rolePrivilegeService.listOrganizationSuperAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listOrganizationAdministrators</b>
     * <p>公司管理员列表</p>
     */
    @RequestMapping("listOrganizationAdministrators")
    @RestReturn(value=OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if(!resolver.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ADMIN_MANAGE)){
            resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.ENTERPRISE_ADMIN_MANAGE);
        }
        RestResponse response = new RestResponse(rolePrivilegeService.listOrganizationAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/listUserRelatedProjectByMenuId</b>
     * <p>用户的在这个菜单下的项目列表</p>
     */
    @RequestMapping("listUserRelatedProjectByMenuId")
    @RestReturn(value=ProjectDTO.class, collection = true)
    public RestResponse listUserRelatedProjectByMenuId(@Valid ListUserRelatedProjectByMenuIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.listUserRelatedProjectByMenuId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listUserRelatedPrivilegeByModuleId</b>
     * <p>获取用户模块权限</p>
     */
    @RequestMapping("listUserRelatedPrivilegeByModuleId")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse listUserRelatedPrivilegeByModuleId(@Valid ListUserRelatedPrivilegeByModuleIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.listUserRelatedPrivilegeByModuleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
