package com.everhomes.launchpad;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.everhomes.util.EtagHelper;

@RestDoc(value="LaunchPad controller", site="core")
@RestController
@RequestMapping("/launchpad")
public class LaunchPadController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadController.class);
    
    @Autowired
    private LaunchPadService launchPadService;

    /**
     * <b>URL: /launchpad/getLaunchPadItems</b>
     * <p>根据小区id、itemLocation、itemGroup获取item列表</p>
     */
    @RequestMapping("getLaunchPadItems")
    @RestReturn(value=GetLaunchPadItemsCommandResponse.class)
    public RestResponse getLaunchPadItems(@Valid GetLaunchPadItemsCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        
        GetLaunchPadItemsCommandResponse commandResponse = launchPadService.getLaunchPadItems(cmd);
        RestResponse resp =  new RestResponse();
        if(commandResponse.getLaunchPadItems() != null && !commandResponse.getLaunchPadItems().isEmpty()){
            int hashCode = commandResponse.getLaunchPadItems().hashCode();
            if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
                resp.setResponseObject(commandResponse);
            }
        }
       
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /launchpad/getLaunchPadItemById</b>
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
     * <b>URL: /launchpad/getLastLaunchPadLayoutByVersionCode</b>
     * <p>根据版本号获取可兼容的最新版信息</p>
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
    
    /**
     * <b>URL: /launchpad/getLaunchPadLayout</b>
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
     * <b>URL: /launchpad/findLaunchPadPostActionCategories</b>
     * <p>根据小区id、itemLocation、itemGroup获取PostActionCategory列表</p>
     */
    @RequestMapping("findLaunchPadPostActionCategories")
    @RestReturn(value=LaunchPadPostActionCategoryDTO.class,collection=true)
    public RestResponse findLaunchPadPostActionCategories(@Valid FindLaunchPadPostActionItemCategoriesCommand cmd) {
        
        List<LaunchPadPostActionCategoryDTO> result = launchPadService.findLaunchPadPostActionCategories(cmd);
        RestResponse resp =  new RestResponse(result);
        
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
}
