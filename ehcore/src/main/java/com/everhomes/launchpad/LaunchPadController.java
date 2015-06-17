package com.everhomes.launchpad;


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
import com.everhomes.rest.RestResponse;

@RestDoc(value="LaunchPad controller", site="core")
@RestController
@RequestMapping("/launchpad")
public class LaunchPadController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadController.class);
    
    @Autowired
    private LaunchPadService launchPadService;

    /**
     * <b>URL: /launchpad/listLaunchPadByCommunityId</b>
     * <p>获取服务市场列表</p>
     */
    @RequestMapping("listLaunchPadByCommunityId")
    @RestReturn(value=ListLaunchPadByCommunityIdCommandResponse.class)
    public RestResponse listLaunchPadByCommunityId(@Valid ListLaunchPadByCommunityIdCommand cmd) {
        
        ListLaunchPadByCommunityIdCommandResponse commandResponse = launchPadService.listLaunchPadByCommunityId(cmd);
        RestResponse response =  new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /launchpad/createLaunchPadItem</b>
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
     * <b>URL: /launchpad/userDefinedLaunchPad</b>
     * <p>用户自定义服务市场</p>
     */
    @RequestMapping("userDefinedLaunchPad")
    @RestReturn(value=String.class)
    public RestResponse userDefinedLaunchPad(@Valid UserDefinedLaunchPadCommand cmd) {
        
        launchPadService.userDefinedLaunchPad(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /launchpad/deleteLaunchPadItem</b>
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
     * <b>URL: /launchpad/getLastLaunchPadLayoutByVersionCode</b>
     * <p>根据版本号获取可兼容的最新版本号</p>
     */
    @RequestMapping("getLastLaunchPadLayoutByVersionCode")
    @RestReturn(value=LaunchPadLayoutDTO.class)
    public RestResponse getLastLaunchPadLayoutByVersionCode(@Valid GetLaunchPadLayoutByVersionCodeCommand cmd) {
        
        LaunchPadLayoutDTO launchPadLayoutDTO = this.launchPadService.getLastLaunchPadLayoutByVersionCode(cmd);
        
        RestResponse response =  new RestResponse(launchPadLayoutDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
