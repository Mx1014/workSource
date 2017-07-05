package com.everhomes.module;

import java.util.List;

import javax.validation.Valid;

import com.everhomes.rest.acl.*;
import com.everhomes.rest.module.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

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
}
