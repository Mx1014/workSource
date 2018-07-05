package com.everhomes.module;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.module.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.TreeServiceModuleAppsResponse;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestDoc(value = "module controller", site = "core")
@RestController
@RequestMapping("/module")
public class ModuleController extends ControllerBase {

    @Autowired
    private ServiceModuleService serviceModuleService;

    /**
     * <b>URL: /module/getServiceModule</b>
     * <p>
     * 获取业务模块信息
     * </p>
     */
    @RequestMapping("getServiceModule")
    @RestReturn(value = ServiceModuleDTO.class)
    public RestResponse getServiceModule(@Valid GetServiceModuleCommand cmd) {
        ServiceModuleDTO dto = serviceModuleService.getServiceModule(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listServiceModules</b>
     * <p>
     * 业务模块列表
     * </p>
     */
    @RequestMapping("listServiceModules")
    @RestReturn(value = ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModules(@Valid ListServiceModulesCommand cmd) {
        List<ServiceModuleDTO> dto = serviceModuleService.listServiceModules(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/treeServiceModules</b>
     * <p>
     * 树状结构的业务模块列表
     * </p>
     */
    @RequestMapping("treeServiceModules")
    @RestReturn(value = ServiceModuleDTO.class, collection = true)
    public RestResponse treeServiceModules(@Valid TreeServiceModuleCommand cmd) {
        List<ServiceModuleDTO> dto = serviceModuleService.treeServiceModules(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/treeServiceModuleApps</b>
     * <p>
     * 树状结构的业务模块列表
     * </p>
     */
    @RequestMapping("treeServiceModuleApps")
    @RestReturn(value = TreeServiceModuleAppsResponse.class)
    public RestResponse treeServiceModuleApps(@Valid TreeServiceModuleCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleService.treeServiceModuleApps(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listUserRelatedCategoryProjectByModuleId</b>
     * <p>用户的在这个模块下的分类项目列表</p>
     */
    @RequestMapping("listUserRelatedCategoryProjectByModuleId")
    @RestReturn(value=ProjectDTO.class, collection = true)
    public RestResponse listUserRelatedCategoryProjectByModuleId(@Valid ListUserRelatedProjectByModuleCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleService.listUserRelatedCategoryProjectByModuleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/checkUserRelatedProjectAllFlag</b>
     * <p>查询用户是否在某个应用下具有全部的项目</p>
     */
    @RequestMapping("checkUserRelatedProjectAllFlag")
    @RestReturn(value=ProjectDTO.class, collection = true)
    public RestResponse checkUserRelatedProjectAllFlag(@Valid ListUserRelatedProjectByModuleCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleService.checkUserRelatedProjectAllFlag(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /module/listUserRelatedProjectByModuleId</b>
     * <p>用户的在这个模块下的项目列表</p>
     */
    @RequestMapping("listUserRelatedProjectByModuleId")
    @RestReturn(value=ProjectDTO.class, collection = true)
    public RestResponse listUserRelatedProjectByModuleId(@Valid ListUserRelatedProjectByModuleCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleService.listUserRelatedProjectByModuleId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listServiceModulePrivileges</b>
     * <p>
     * 业务模块权限列表
     * </p>
     */
    @RequestMapping("listServiceModulePrivileges")
    @RestReturn(value = ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModulePrivileges(@Valid ListServiceModulePrivilegesCommand cmd) {
        List<ServiceModuleDTO> dto = serviceModuleService.listServiceModulePrivileges(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/assignmentServiceModule</b>
     * <p>
     * 业务模块分配，包括添加和编辑
     * </p>
     */
    @RequestMapping("assignmentServiceModule")
    @RestReturn(value = String.class)
    public RestResponse assignmentServiceModule(@Valid AssignmentServiceModuleCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.MODULE_CONF_RELATION_CREATE);
        this.serviceModuleService.assignmentServiceModule(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/deleteAssignmentServiceModule</b>
     * <p>
     * 删除业务模块分配关系
     * </p>
     */
    @RequestMapping("deleteServiceModuleAssignmentRelation")
    @RestReturn(value = String.class)
    public RestResponse deleteServiceModuleAssignmentRelation(@Valid DeleteServiceModuleAssignmentRelationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.MODULE_CONF_RELATION_DELETE);
        this.serviceModuleService.deleteServiceModuleAssignmentRelation(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listServiceModuleAssignmentRelations</b>
     * <p>
     * 业务模块分配关系列表
     * </p>
     */
    @RequestMapping("listServiceModuleAssignmentRelations")
    @RestReturn(value = ServiceModuleAssignmentRelationDTO.class, collection = true)
    public RestResponse listServiceModuleAssignmentRelations(@Valid ListServiceModuleAssignmentRelationsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkCurrentUserAuthority(cmd.getOwnerId(), PrivilegeConstants.MODULE_CONF_RELATION_LIST);
        List<ServiceModuleAssignmentRelationDTO> relationList = this.serviceModuleService.listServiceModuleAssignmentRelations(cmd);
        RestResponse response = new RestResponse(relationList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/checkModuleManage</b>
     * <p>
     * 校验有没有模块管理权限
     * </p>
     */
    @RequestMapping("checkModuleManage")
    @RestReturn(value = Byte.class)
    public RestResponse checkModuleManage(@Valid CheckModuleManageCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleService.checkModuleManage(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/createServiceModule</b>
     * <p>
     *  创建模块
     * </p>
     */
    @RequestMapping("createServiceModule")
    @RestReturn(value = String.class)
    public RestResponse createServiceModule(@Valid CreateServiceModuleCommand cmd) {
        return new RestResponse(serviceModuleService.createServiceModule(cmd));
    }

    /**
     * <b>URL: /module/updateServiceModule</b>
     * <p>
     *  修改模块
     * </p>
     */
    @RequestMapping("updateServiceModule")
    @RestReturn(value = String.class)
    public RestResponse updateServiceModule(@Valid UpdateServiceModuleCommand cmd) {
        return new RestResponse(serviceModuleService.updateServiceModule(cmd));
    }

    /**
     * <b>URL: /module/deleteServiceModule</b>
     * <p>
     *  删除模块
     * </p>
     */
    @RequestMapping("deleteServiceModule")
    @RestReturn(value = String.class)
    public RestResponse deleteServiceModule(@Valid DeleteServiceModuleCommand cmd) {
        serviceModuleService.deleteServiceModule(cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: /module/listAllServiceModules</b>
     * <p>
     * 全部的业务模块列表
     * </p>
     */
    @RequestMapping("listAllServiceModules")
    @RestReturn(value = ListServiceModulesResponse.class, collection = true)
    public RestResponse listAllServiceModules(@Valid ListServiceModulesCommand cmd) {
        return new RestResponse(serviceModuleService.listAllServiceModules(cmd));
    }

    /**
     * <b>URL: /module/listUserServiceModulefunctions</b>
     * 个人在业务模块下的功能
     */
    @RequestMapping("listUserServiceModulefunctions")
    @RestReturn(value = Long.class, collection = true)
    public RestResponse listUserServiceModulefunctions(@Valid ListServiceModulefunctionsCommand cmd) {
        return new RestResponse(serviceModuleService.listServiceModulefunctions(cmd));
    }

    /**
     * <b>URL: /module/findServiceModuleApp</b>
     * 查询应用信息
     */
    @RequestMapping("findServiceModuleApp")
    @RestReturn(value = ServiceModuleAppDTO.class)
    public RestResponse findServiceModuleApp(@Valid FindServiceModuleAppCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setResponseObject(serviceModuleService.findServiceModuleAppById(cmd.getId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /module/listServiceModuleEntries</b>
     * 查询模块入口
     */
    @RequestMapping("listServiceModuleEntries")
    @RestReturn(value = ServiceModuleEntryDTO.class)
    public RestResponse listServiceModuleEntries(ListServiceModuleEntriesCommand cmd) {
        ListServiceModuleEntriesResponse res = serviceModuleService.listServiceModuleEntries(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /module/updateServiceModuleEntries</b>
     * 更新模块入口
     */
    @RequestMapping("updateServiceModuleEntries")
    @RestReturn(value = String.class)
    public RestResponse updateServiceModuleEntries(UpdateServiceModuleEntriesCommand cmd) {
        serviceModuleService.updateServiceModuleEntries(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
