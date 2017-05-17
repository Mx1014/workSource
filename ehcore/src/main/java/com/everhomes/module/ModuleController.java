package com.everhomes.module;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.TreeWebMenusCommand;
import com.everhomes.rest.module.*;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestDoc(value="module controller", site="core")
@RestController
@RequestMapping("/module")
public class ModuleController extends ControllerBase {
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private ServiceModuleService serviceModuleService;


    /**
     * <b>URL: /module/getServiceModule</b>
     * <p>获取业务模块信息</p>
     */
    @RequestMapping("getServiceModule")
    @RestReturn(value=ServiceModuleDTO.class)
    public RestResponse getServiceModule(@Valid GetServiceModuleCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listServiceModules</b>
     * <p>业务模块列表</p>
     */
    @RequestMapping("listServiceModules")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModules(@Valid ListServiceModulesCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/treeServiceModules</b>
     * <p>树状结构的业务模块列表</p>
     */
    @RequestMapping("treeServiceModules")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse treeServiceModules(@Valid TreeServiceModuleCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listServiceModulePrivileges</b>
     * <p>业务模块权限列表</p>
     */
    @RequestMapping("listServiceModulePrivileges")
    @RestReturn(value=ServiceModuleDTO.class, collection = true)
    public RestResponse listServiceModulePrivileges(@Valid ListServiceModulePrivilegesCommand cmd) {
        RestResponse response =  new RestResponse(serviceModuleService.listServiceModulePrivileges(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/assignmentServiceModule</b>
     * <p>业务模块分配，包括添加和编辑</p>
     */
    @RequestMapping("assignmentServiceModule")
    @RestReturn(value=String.class)
    public RestResponse assignmentServiceModule(@Valid AssignmentServiceModuleCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/deleteAssignmentServiceModule</b>
     * <p>删除业务模块分配关系</p>
     */
    @RequestMapping("deleteServiceModuleAssignmentRelation")
    @RestReturn(value=String.class)
    public RestResponse deleteServiceModuleAssignmentRelation(@Valid DeleteServiceModuleAssignmentRelationCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/listServiceModuleAssignmentRelations</b>
     * <p>业务模块分配关系列表</p>
     */
    @RequestMapping("listServiceModuleAssignmentRelations")
    @RestReturn(value=ServiceModuleAssignmentRelationDTO.class, collection = true)
    public RestResponse listServiceModuleAssignmentRelations(@Valid ListServiceModuleAssignmentRelationsCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
