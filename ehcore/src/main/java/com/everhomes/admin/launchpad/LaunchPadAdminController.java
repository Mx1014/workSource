package com.everhomes.admin.launchpad;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchpad.CreateLaunchPadItemCommand;
import com.everhomes.launchpad.CreateLaunchPadLayoutCommand;
import com.everhomes.launchpad.DeleteLaunchPadItemCommand;
import com.everhomes.launchpad.DeleteLaunchPadLayoutCommand;
import com.everhomes.launchpad.GetLaunchPadItemByIdCommand;
import com.everhomes.launchpad.GetLaunchPadItemsByKeywordCommand;
import com.everhomes.launchpad.GetLaunchPadItemsByKeywordCommandResponse;
import com.everhomes.launchpad.GetLaunchPadLayoutCommand;
import com.everhomes.launchpad.LaunchPadItemDTO;
import com.everhomes.launchpad.LaunchPadLayoutDTO;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.launchpad.ListLaunchPadLayoutCommand;
import com.everhomes.launchpad.ListLaunchPadLayoutCommandResponse;
import com.everhomes.launchpad.UpdateLaunchPadItemCommand;
import com.everhomes.launchpad.UpdateLaunchPadLayoutCommand;
import com.everhomes.rest.RestResponse;

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
    @RestReturn(value=GetLaunchPadItemsByKeywordCommandResponse.class)
    public RestResponse getLaunchPadItemsByKeyword(@Valid GetLaunchPadItemsByKeywordCommand cmd) {
        
        GetLaunchPadItemsByKeywordCommandResponse cmdResponse = this.launchPadService.getLaunchPadItemsByKeyword(cmd);
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
    public RestResponse createLaunchPadItem(@Valid CreateLaunchPadItemCommand cmd) {
        
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
    public RestResponse deleteLaunchPadItem(@Valid DeleteLaunchPadItemCommand cmd) {
        
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
    public RestResponse updateLaunchPadItem(@Valid UpdateLaunchPadItemCommand cmd) {
        
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
    public RestResponse createLaunchPadLayout(@Valid CreateLaunchPadLayoutCommand cmd) {
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
    public RestResponse deleteLaunchPadLayout(@Valid DeleteLaunchPadLayoutCommand cmd) {
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
    public RestResponse updateLaunchPadLayout(@Valid UpdateLaunchPadLayoutCommand cmd) {
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
    public RestResponse listLaunchPadLayoutByKeyword(@Valid ListLaunchPadLayoutCommand cmd) {
    	ListLaunchPadLayoutCommandResponse result = this.launchPadService.listLaunchPadLayoutByKeyword(cmd);
        RestResponse response =  new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
