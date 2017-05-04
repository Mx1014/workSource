package com.everhomes.module;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.TreeWebMenusCommand;
import com.everhomes.rest.module.GetServiceModuleCommand;
import com.everhomes.rest.module.TreeServiceModuleCommand;
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
     * <b>URL: /menu/listServiceModules</b>
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
     * <b>URL: /menu/treeServiceModules</b>
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

}
