package com.everhomes.launchpad.admin;


import javax.validation.Valid;

import com.everhomes.rest.launchpad.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommand;
import com.everhomes.rest.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommandResponse;
import com.everhomes.rest.launchpad.admin.ListLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.UpdateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.UpdateLaunchPadLayoutAdminCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="LaunchPad admin controller", site="core")
@RestController
@RequestMapping("/admin/launchpad")
public class LaunchPadAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadAdminController.class);
    
    @Autowired
    private LaunchPadService launchPadService;

    /**
     * <b>URL: /admin/launchpad/getLaunchPadItemsByKeyword</b>
     * <p>根据id获取配置信息</p>
     */
    @RequestMapping("getLaunchPadItemsByKeyword")
    @RestReturn(value=GetLaunchPadItemsByKeywordAdminCommandResponse.class)
    public RestResponse getLaunchPadItemsByKeyword(@Valid GetLaunchPadItemsByKeywordAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        GetLaunchPadItemsByKeywordAdminCommandResponse cmdResponse = this.launchPadService.getLaunchPadItemsByKeyword(cmd);
        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/getLaunchPadItemById</b>
     * <p>根据id获取配置信息</p>
     */
    @RequestMapping("getLaunchPadItemById")
    @RestReturn(value=LaunchPadItemDTO.class)
    public RestResponse getLaunchPadItems(@Valid GetLaunchPadItemByIdCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        LaunchPadItemDTO launchPadItem = launchPadService.getLaunchPadItemById(cmd);
        RestResponse resp =  new RestResponse(launchPadItem);

        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /admin/launchpad/createLaunchPadItem</b>
     * <p>创建服务市场条目</p>
     */
    @RequestMapping("createLaunchPadItem")
    @RestReturn(value=String.class)
    public RestResponse createLaunchPadItem(@Valid CreateLaunchPadItemAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        launchPadService.createLaunchPadItem(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/deleteLaunchPadItem</b>
     * <p>删除服务市场Item列表</p>
     */
    @RequestMapping("deleteLaunchPadItem")
    @RestReturn(value=String.class)
    public RestResponse deleteLaunchPadItem(@Valid DeleteLaunchPadItemAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.launchPadService.deleteLaunchPadItem(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/updateLaunchPadItem</b>
     * <p>更新服务市场Item信息</p>
     */
    @RequestMapping("updateLaunchPadItem")
    @RestReturn(value=String.class)
    public RestResponse updateLaunchPadItem(@Valid UpdateLaunchPadItemAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.launchPadService.updateLaunchPadItem(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/launchpad/createLaunchPadLayout</b>
     * <p>创建服务市场版本样式layout</p>
     */
    @RequestMapping("createLaunchPadLayout")
    @RestReturn(value=String.class)
    public RestResponse createLaunchPadLayout(@Valid CreateLaunchPadLayoutAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.launchPadService.createLaunchPadLayout(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/deleteLaunchPadLayout</b>
     * <p>删除服务市场样式，逻辑删除</p>
     */
    @RequestMapping("deleteLaunchPadLayout")
    @RestReturn(value=String.class)
    public RestResponse deleteLaunchPadLayout(@Valid DeleteLaunchPadLayoutAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.launchPadService.deleteLaunchPadLayout(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/updateLaunchPadLayout</b>
     * <p>更新服务市场样式</p>
     */
    @RequestMapping("updateLaunchPadLayout")
    @RestReturn(value=String.class)
    public RestResponse updateLaunchPadLayout(@Valid UpdateLaunchPadLayoutAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.launchPadService.updateLaunchPadLayout(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/getLaunchPadLayout</b>
     * <p>根据id获取服务市场样式</p>
     */
    @RequestMapping("getLaunchPadLayout")
    @RestReturn(value=LaunchPadLayoutDTO.class)
    public RestResponse getLaunchPadLayout(@Valid GetLaunchPadLayoutCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        LaunchPadLayoutDTO dto = this.launchPadService.getLaunchPadLayout(cmd);
        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/launchpad/listLaunchPadLayoutByKeyword</b>
     * <p>获取服务市场样式列表</p>
     */
    @RequestMapping("listLaunchPadLayoutByKeyword")
    @RestReturn(value=LaunchPadLayoutDTO.class)
    public RestResponse listLaunchPadLayoutByKeyword(@Valid ListLaunchPadLayoutAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	ListLaunchPadLayoutCommandResponse result = this.launchPadService.listLaunchPadLayoutByKeyword(cmd);
        RestResponse response =  new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpad/createItemServiceCategry</b>
     * <p>添加服务类别</p>
     */
    @RequestMapping("createItemServiceCategry")
    @RestReturn(value=String.class)
    public RestResponse createItemServiceCategry(CreateItemServiceCategryCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
