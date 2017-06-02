package com.everhomes.menu;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.TreeWebMenusCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestDoc(value="menu controller", site="core")
@RestController
@RequestMapping("/menu")
public class MenuController extends ControllerBase {
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private WebMenuService webMenuService;


    /**
     * <b>URL: /menu/listUserRelatedWebMenus</b>
     * <p>获取用户菜单</p>
     */
    @RequestMapping("listUserRelatedWebMenus")
    @RestReturn(value=WebMenuDTO.class, collection = true)
    public RestResponse listUserRelatedWebMenus(@Valid ListUserRelatedWebMenusCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setResponseObject(webMenuService.listUserRelatedWebMenus(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /menu/treeWebMenus</b>
     * <p>树状结构的菜单列表</p>
     */
    @RequestMapping("treeWebMenus")
    @RestReturn(value=WebMenuDTO.class, collection = true)
    public RestResponse treeWebMenus(@Valid TreeWebMenusCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
