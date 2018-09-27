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
import com.everhomes.rest.me_menu.ListMeWebMenusCommand;
import com.everhomes.rest.menu.*;

import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


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
     * <b>URL: /menu/listUserAppCategory</b>
     * <p>获取园区场景的菜单</p>
     */
    @RequestMapping("listUserAppCategory")
    @RestReturn(value=AppCategoryDTO.class, collection = true)
    public RestResponse listUserAppCategory(ListUserAppCategoryCommand cmd) {

        List<AppCategoryDTO> res = webMenuService.listUserAppCategory(cmd);
        RestResponse response =  new RestResponse(res);
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

    /**
     * <b>URL: /menu/getTreeWebMenusByNamespace</b>
     * <p>树状结构的菜单列表，并根据当前域空间标记是否选中</p>
     */
    @RequestMapping("getTreeWebMenusByNamespace")
    @RestReturn(value=WebMenuDTO.class, collection = true)
    public RestResponse getTreeWebMenusByNamespace(GetTreeWebMenusByNamespaceCommand cmd) {
        GetTreeWebMenusByNamespaceResponse res = webMenuService.getTreeWebMenusByNamespace(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /menu/updateMenuScopesByNamespace</b>
     * <p>根据树状结构更新菜单scope</p>
     */
    @RequestMapping("updateMenuScopesByNamespace")
    @RestReturn(value=String.class)
    public RestResponse updateMenuScopesByNamespace(UpdateMenuScopesByNamespaceCommand cmd) {
        webMenuService.updateMenuScopesByNamespace(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /menu/listMenuForPcEntry</b>
     * <p>获取pc门户菜单</p>
     */
    @RequestMapping("listMenuForPcEntry")
    @RestReturn(value=ListMenuForPcEntryResponse.class)
    @RequireAuthentication(false)
    public RestResponse listMenuForPcEntry(ListMenuForPcEntryCommand cmd) {
        ListMenuForPcEntryResponse res = webMenuService.listMenuForPcEntry(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /menu/listMenuForPcEntryServices</b>
     * <p>获取pc门户聚焦服务菜单</p>
     */
    @RequestMapping("listMenuForPcEntryServices")
    @RestReturn(value=ListMenuForPcEntryServicesResponse.class)
    @RequireAuthentication(false)
    public RestResponse listMenuForPcEntryServices(ListMenuForPcEntryServicesCommand cmd) {
        ListMenuForPcEntryServicesResponse res = webMenuService.listMenuForPcEntryServices(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }




}
