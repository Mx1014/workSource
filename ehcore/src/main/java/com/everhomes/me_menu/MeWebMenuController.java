package com.everhomes.me_menu;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.menu.WebMenuService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.me_menu.ListMeWebMenusCommand;
import com.everhomes.rest.me_menu.ListMeWebMenusResponse;
import com.everhomes.rest.me_menu.MeWebMenuDTO;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.TreeWebMenusCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestDoc(value="mewebmenu controller", site="core")
@RestController
@RequestMapping("/mewebmenu")
public class MeWebMenuController extends ControllerBase {

    @Autowired
    private MeWebMenuService meWebMenuService;


    /**
     * <b>URL: /menu/listMeWebMenus</b>
     * <p>获取菜单</p>
     */
    @RequestMapping("listMeWebMenus")
    @RestReturn(value=MeWebMenuDTO.class, collection = true)
    public RestResponse listMeWebMenus(ListMeWebMenusCommand cmd) {
        ListMeWebMenusResponse res = meWebMenuService.listMeWebMenus(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
