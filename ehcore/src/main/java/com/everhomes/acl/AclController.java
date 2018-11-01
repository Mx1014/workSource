package com.everhomes.acl;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.admin.*;
import com.everhomes.rest.customer.createSuperAdminCommand;
import com.everhomes.rest.module.ListServiceModuleAppsAdministratorResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestDoc(value="acl controller", site="core")
@RestController
@RequestMapping("/acl")
public class AclController extends ControllerBase {
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private ServiceModuleService serviceModuleService;


//    /**
//     * <b>URL: /acl/createOrganizationAdmin</b>
//     * <p>创建公司管理员</p>
//     */
//    @RequestMapping("createOrganizationAdmin")
//    @RestReturn(value=OrganizationContactDTO.class)
//    public RestResponse createOrganizationAdmin(@Valid CreateOrganizationAdminCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        if(!resolver.checkOrganizationAdmin(UserContext.current().getUser().getId(), cmd.getOwnerId())){
//            resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.ORG_ADMIN_CREATE);
//        }
//        RestResponse response =  new RestResponse(rolePrivilegeService.createOrganizationAdmin(cmd));
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /acl/createOrganizationSuperAdmin</b>
     * <p>创建超级管理员</p>
     */
    @RequestMapping("createOrganizationSuperAdmin")
    @RestReturn(value=String.class)
    public RestResponse createOrganizationSuperAdmin(@Valid CreateOrganizationAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_CREATE);
        RestResponse response =  new RestResponse(rolePrivilegeService.createOrganizationSuperAdmin(cmd));
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
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
        rolePrivilegeService.authorizationServiceModule(cmd);
        RestResponse response =  new RestResponse();
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
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
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
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOrganizationId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
        RestResponse response = new RestResponse(rolePrivilegeService.listAuthorizationServiceModuleMembers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /acl/deleteOrganizationAdministrators</b>
//     * <p>删除公司管理员</p>
//     */
//    @RequestMapping("deleteOrganizationAdministrators")
//    @RestReturn(value=String.class)
//    public RestResponse deleteOrganizationAdministrators(@Valid DeleteOrganizationAdminCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        if(!resolver.checkOrganizationAdmin(UserContext.current().getUser().getId(), cmd.getOwnerId())){
//            resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.ORG_ADMIN_DELETE);
//        }
//        rolePrivilegeService.deleteOrganizationAdministrators(cmd);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /acl/deleteOrganizationSuperAdministrators</b>
     * <p>删除超级管理员</p>
     */
    @RequestMapping("deleteOrganizationSuperAdministrators")
    @RestReturn(value=String.class)
    public RestResponse deleteOrganizationSuperAdministrators(@Valid DeleteOrganizationAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_DELETE);
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
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserAuthority(UserContext.current().getUser().getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOwnerId(), PrivilegeConstants.SERVICE_AUTHORIZATION);
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
    @Deprecated
    @RequestMapping("listOrganizationSuperAdministrators")
    @RestReturn(value=OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationSuperAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listOrganizationSuperAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /acl/listOrganizationAdministrators</b>
//     * <p>公司管理员列表</p>
//     */
//    @RequestMapping("listOrganizationAdministrators")
//    @RestReturn(value=OrganizationContactDTO.class, collection = true)
//    public RestResponse listOrganizationAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        if(!resolver.checkOrganizationAdmin(UserContext.current().getUser().getId(), cmd.getOwnerId())){
//            resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.ORG_ADMIN_LIST);
//        }
//        RestResponse response = new RestResponse(rolePrivilegeService.listOrganizationAdministrators(cmd));
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }


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
     * <p>业务模块下用户的有关权限</p>
     */
    @RequestMapping("listUserRelatedPrivilegeByModuleId")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse listUserRelatedPrivilegeByModuleId(@Valid ListUserRelatedPrivilegeByModuleIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.listUserRelatedPrivilegeByModuleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/getPrivilegeIdsByRoleId</b>
     * <p>查询角色的权限集合</p>
     */
    @RequestMapping("getPrivilegeIdsByRoleId")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse getPrivilegeIdsByRoleId(@Valid ListPrivilegesByRoleIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.getPrivilegeIdsByRoleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /acl/getPrivilegeInfosByRoleId</b>
     * <p>查询角色的权限集合</p>
     */
    @RequestMapping("getPrivilegeInfosByRoleId")
    @RestReturn(value=AclPrivilegeInfoResponse.class)
    public RestResponse getPrivilegeInfosByRoleId(@Valid ListPrivilegesByRoleIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.getPrivilegeInfosByRoleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/createRole</b>
     * <p>创建角色</p>
     */
    @RequestMapping("createRole")
    @RestReturn(value=String.class)
    public RestResponse createRole(@Valid CreateRoleCommand cmd) {
        rolePrivilegeService.createRole(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/updateRole</b>
     * <p>修改角色</p>
     */
    @RequestMapping("updateRole")
    @RestReturn(value=String.class)
    public RestResponse updateRole(@Valid UpdateRoleCommand cmd) {
        rolePrivilegeService.updateRole(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/updateRolePrivileges</b>
     * <p>修改角色和权限的关系</p>
     */
    @RequestMapping("updateRolePrivileges")
    @RestReturn(value=String.class)
    public RestResponse updateRolePrivileges(@Valid UpdateRolePrivilegesCommand cmd) {
        rolePrivilegeService.updateRolePrivileges(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/getPrivilegeByRoleId</b>
     * <p>获取角色的权限</p>
     */
    @RequestMapping("getPrivilegeByRoleId")
    @RestReturn(value=GetPrivilegeByRoleIdResponse.class)
    public RestResponse getPrivilegeByRoleId(@Valid ListPrivilegesByRoleIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.getPrivilegeByRoleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteRolePrivileges</b>
     * <p>删除角色和权限的关系</p>
     */
    @RequestMapping("deleteRolePrivileges")
    @RestReturn(value=String.class)
    public RestResponse deleteRolePrivileges(@Valid DeleteRolePrivilegesCommand cmd) {
        rolePrivilegeService.deleteRolePrivileges(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listRoles</b>
     * <p>角色列表</p>
     */
    @RequestMapping("listRoles")
    @RestReturn(value=ListRolesResponse.class)
    public RestResponse listRoles(@Valid ListRolesCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.listRoles(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/listAuthorizationRelations</b>
     * <p>对象授权关系列表</p>
     */
    @RequestMapping("listAuthorizationRelations")
    @RestReturn(value=ListAuthorizationRelationsResponse.class)
    public RestResponse listAuthorizationRelations(@Valid ListAuthorizationRelationsCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.AUTH_RELATION_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listAuthorizationRelations(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/createAuthorizationRelation</b>
     * <p>创建对象授权关系</p>
     */
    @RequestMapping("createAuthorizationRelation")
    @RestReturn(value=String.class)
    public RestResponse createAuthorizationRelation(@Valid CreateAuthorizationRelationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.AUTH_RELATION_CREATE);
        rolePrivilegeService.createAuthorizationRelation(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/updateAuthorizationRelation</b>
     * <p>修改对象授权关系</p>
     */
    @RequestMapping("updateAuthorizationRelation")
    @RestReturn(value=String.class)
    public RestResponse updateAuthorizationRelation(@Valid UpdateAuthorizationRelationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        rolePrivilegeService.updateAuthorizationRelation(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteAuthorizationRelation</b>
     * <p>删除业务授权人员</p>
     */
    @RequestMapping("deleteAuthorizationRelation")
    @RestReturn(value=String.class)
    public RestResponse deleteAuthorizationRelation(@Valid DeleteAuthorizationRelationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        rolePrivilegeService.deleteAuthorizationRelation(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/listRoleAdministrators</b>
     * <p>角色管理员列表</p>
     */
    @RequestMapping("listRoleAdministrators")
    @RestReturn(value=RoleAuthorizationsDTO.class, collection = true)
    public RestResponse listRoleAdministrators(@Valid ListRoleAdministratorsCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.listRoleAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteRoleAdministrators</b>
     * <p>角色模块管理员</p>
     */
    @RequestMapping("deleteRoleAdministrators")
    @RestReturn(value=String.class)
    public RestResponse deleteRoleAdministrators(@Valid DeleteRoleAdministratorsCommand cmd) {
        rolePrivilegeService.deleteRoleAdministrators(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/createRoleAdministrators</b>
     * <p>创建角色模块管理员</p>
     */
    @RequestMapping("createRoleAdministrators")
    @RestReturn(value=String.class)
    public RestResponse createRoleAdministrators(@Valid CreateRoleAdministratorsCommand cmd) {
        rolePrivilegeService.createRoleAdministrators(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/updateRoleAdministrators</b>
     * <p>创建角色模块管理员</p>
     */
    @RequestMapping("updateRoleAdministrators")
    @RestReturn(value=String.class)
    public RestResponse updateRoleAdministrators(@Valid CreateRoleAdministratorsCommand cmd) {
        rolePrivilegeService.updateRoleAdministrators(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/checkRoleAdministrators</b>
     * <p>校验角色模块管理员</p>
     */
    @RequestMapping("checkRoleAdministrators")
    @RestReturn(value=RoleAuthorizationsDTO.class)
    public RestResponse checkRoleAdministrators(@Valid CheckRoleAdministratorsCommand cmd) {
        RestResponse response =  new RestResponse(rolePrivilegeService.checkRoleAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    // by lei.lv 废弃代码
//    /**
//     * <b>URL: /acl/createServiceModuleAdministrators</b>
//     * <p>创建业务模块管理员</p>
//     */
//    @RequestMapping("createServiceModuleAdministrators")
//    @RestReturn(value=String.class)
//    public RestResponse createServiceModuleAdministrators(@Valid CreateServiceModuleAdministratorsCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_CREATE);
//        rolePrivilegeService.createServiceModuleAdministrators(cmd);
//        RestResponse response =  new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//
//    /**
//     * <b>URL: /acl/updateServiceModuleAdministrators</b>
//     * <p>修改业务模块管理员</p>
//     */
//    @RequestMapping("updateServiceModuleAdministrators")
//    @RestReturn(value=String.class)
//    public RestResponse updateServiceModuleAdministrators(@Valid UpdateServiceModuleAdministratorsCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_UPDATE);
//        rolePrivilegeService.updateServiceModuleAdministrators(cmd);
//        RestResponse response =  new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /acl/listServiceModuleAdministrators</b>
     * <p>业务模块管理员列表</p>
     */
    @RequestMapping("listServiceModuleAdministrators")
    @RestReturn(value=ServiceModuleAuthorizationsDTO.class, collection = true)
    public RestResponse listServiceModuleAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_LIST);
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
    @RestReturn(value=String.class)
    public RestResponse deleteServiceModuleAdministrators(@Valid DeleteServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_DELETE);
        rolePrivilegeService.deleteServiceModuleAdministrators(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/deleteServiceModuleAppsAdministrators</b>
     * <p>删除业务模块应用管理员</p>
     */
    @RequestMapping("deleteServiceModuleAppsAdministrators")
    @RestReturn(value=String.class)
    public RestResponse deleteServiceModuleAppsAdministrators(@Valid DeleteServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_DELETE);
        rolePrivilegeService.deleteServiceModuleAppsAdministrators(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listServiceModulesByTarget</b>
     * <p>查询管理员对象授权业务模块</p>
     */
    @RequestMapping("listServiceModulesByTarget")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModulesByTarget(@Valid ListServiceModulesByTargetCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.MODULE_ADMIN_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listServiceModulesByTarget(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listServiceModulePrivilegesByTarget</b>
     * <p>查询对象模块权限</p>
     */
    @RequestMapping("listServiceModulePrivilegesByTarget")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModulePrivilegesByTarget(@Valid ListServiceModulePrivilegesByTargetCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.AUTH_RELATION_LIST);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /acl/checkModuleAdmin</b>
//     * <p>查询对象模块权限</p>
//     */
//    @RequestMapping("checkModuleAdmin")
//    @RestReturn(value=ServiceModuleDTO.class, collection = true)
//    public RestResponse listServiceModulePrivilegesByTarget(@Valid ListServiceModulePrivilegesByTargetCommand cmd) {
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    /**
     * <b>URL: /acl/createOrganizationSuperAdmins</b>
     * <p>批量创建超级管理员</p>
     */
    @RequestMapping("createOrganizationSuperAdmins")
    @RestReturn(value=String.class)
    public RestResponse createOrganizationSuperAdmins(@Valid CreateOrganizationAdminsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_CREATE);
        rolePrivilegeService.createOrganizationSuperAdmins(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/transferOrganizationSuperAdmin</b>
     * <p>移交管理员权限</p>
     */
    @RequestMapping("transferOrganizationSuperAdmin")
    @RestReturn(value=String.class)
    public RestResponse transferOrganizationSuperAdmin(@Valid TransferOrganizationSuperAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_CREATE);
        rolePrivilegeService.transferOrganizationSuperAdmin(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/resetServiceModuleAdministrators</b>
     * <p>重建对象授权关系</p>
     */
    @RequestMapping("resetServiceModuleAdministrators")
    @RestReturn(value=String.class)
    public RestResponse resetServiceModuleAdministrators(@Valid ResetServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.AUTH_RELATION_CREATE);
        rolePrivilegeService.resetServiceModuleAdministrators(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/listServiceModuleAppsAdministrators</b>
     * <p>业务模块应用管理员列表</p>
     */
    @RequestMapping("listServiceModuleAppsAdministrators")
    @RestReturn(value=ListServiceModuleAppsAdministratorResponse.class)
    public RestResponse listServiceModuleAppsAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listServiceModuleAppsAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listServiceModuleAppsAdministratorTargetIds</b>
     * <p>业务模块应用管理员targetIds列表</p>
     */
    @RequestMapping("listServiceModuleAppsAdministratorTargetIds")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse listServiceModuleAppsAdministratorTargetIds(@Valid ListServiceModuleAdministratorsCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ADMIN_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listServiceModuleAppsAdministratorTargetIds(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listOrganizationTopAdministrator</b>
     * <p>超级管理员列表（标准版）</p>
     */
    @RequestMapping("listOrganizationTopAdministrator")
    @RestReturn(value=OrganizationContactDTO.class)
    public RestResponse listOrganizationTopAdministrator(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listOrganizationTopAdministrator(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/listOrganizationSystemAdministrators</b>
     * <p>系统管理员列表（标准版）</p>
     */
    @RequestMapping("listOrganizationSystemAdministrators")
    @RestReturn(value=OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationSystemAdministrators(@Valid ListServiceModuleAdministratorsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_LIST);
        RestResponse response = new RestResponse(rolePrivilegeService.listOrganizationSystemAdministrators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/getAdministratorInfosByUserId</b>
     * <p>根据用户id获取是否是管理员（标准版）</p>
     */
    @RequestMapping("getAdministratorInfosByUserId")
    @RestReturn(value=GetAdministratorInfosByUserIdResponse.class)
    public RestResponse listAdministratorInfosByUserId(@Valid GetAdministratorInfosByUserIdCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.getAdministratorInfosByUserId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/getPersonelInfoByToken</b>
     * <p>根据手机号获取个人信息（标准版）</p>
     */
    @RequestMapping("getPersonelInfoByToken")
    @RestReturn(value=GetPersonelInfoByTokenResponse.class)
    public RestResponse getPersonelInfoByToken(@Valid GetPersonelInfoByTokenCommand cmd) {
        RestResponse response = new RestResponse(rolePrivilegeService.getPersonelInfoByToken(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/updateTopAdminstrator</b>
     * <p>更改超级管理员（标准版）</p>
     */
    @RequestMapping("updateTopAdminstrator")
    @RestReturn(value=String.class)
    public RestResponse updateTopAdminstrator(@Valid CreateOrganizationAdminCommand cmd) {
        rolePrivilegeService.updateTopAdminstrator(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /acl/createSystemAdminstrator</b>
     * <p>创建系统管理员（标准版）</p>
     */
    @RequestMapping("createSystemAdminstrator")
    @RestReturn(value=String.class)
    public RestResponse createSystemAdminstrator(@Valid CreateOrganizationAdminCommand cmd) {
        rolePrivilegeService.createOrganizationSuperAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /acl/createSuperAdmin</b>
     * <p>创建企业管理员</p>
     */
    @RequestMapping("createSuperAdmin")
    @RestReturn(value = String.class)
    public RestResponse createSuperAdmin(createSuperAdminCommand cmd) {
        rolePrivilegeService.updateSuperAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }



}
