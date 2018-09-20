package com.everhomes.launchpad;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.*;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestDoc(value="LaunchPad controller", site="core")
@RestController
@RequestMapping("/launchpad")
public class LaunchPadController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadController.class);
    private static final String MARKETDATA_ITEM_VERSION = "marketdata.item.version";
    
    @Autowired
    private LaunchPadService launchPadService;
    @Autowired
    private ConfigurationProvider configurationProvider;

    /**
     * <b>URL: /launchpad/getLaunchPadItems</b>
     * <p>根据小区id、itemLocation、itemGroup获取item列表</p>
     */
    @RequestMapping("getLaunchPadItems")
    @RestReturn(value=GetLaunchPadItemsCommandResponse.class)
    public RestResponse getLaunchPadItems(@Valid GetLaunchPadItemsCommand cmd,HttpServletRequest request,HttpServletResponse response) {
    	
        GetLaunchPadItemsCommandResponse commandResponse = launchPadService.getLaunchPadItems(cmd,request);
        RestResponse resp =  new RestResponse(commandResponse);
//        if(commandResponse.getLaunchPadItems() != null && !commandResponse.getLaunchPadItems().isEmpty()){
//            //int hashCode = configurationProvider.getIntValue(MARKETDATA_ITEM_VERSION, 0);
//            int resultCode = commandResponse.hashCode();
//            LOGGER.info("result code : " + resultCode);
//            if(EtagHelper.checkHeaderEtagOnly(30,resultCode+"", request, response)) {
//                resp.setResponseObject(commandResponse);
//            }
//        }
       
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
    public RestResponse getLastLaunchPadLayoutByVersionCode(@Valid GetLaunchPadLayoutByVersionCodeCommand cmd, HttpServletRequest request,HttpServletResponse response) {
        
        LaunchPadLayoutDTO launchPadLayoutDTO = this.launchPadService.getLastLaunchPadLayoutByVersionCode(cmd, ScopeType.ALL, 0L);
        RestResponse resp =  new RestResponse();
        if(launchPadLayoutDTO != null){
            long hashCode = launchPadLayoutDTO.getVersionCode();
            if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
                resp.setResponseObject(launchPadLayoutDTO);
            }
        }
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
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
     * <b>URL: /launchpad/deleteLaunchPadById</b>
     * <p>用户取消收藏</p>
     */
    @RequestMapping("deleteLaunchPadById")
    @RestReturn(value=String.class)
    public RestResponse deleteLaunchPadById(DeleteLaunchPadByIdCommand cmd) {
        
        this.launchPadService.deleteLaunchPadById(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpad/listItemServiceCategries</b>
     * <p>item服务类别</p>
     */
    @RequestMapping("listItemServiceCategries")
    @RestReturn(value=ItemServiceCategryDTO.class, collection = true)
    public RestResponse listItemServiceCategries() {
        RestResponse response =  new RestResponse(launchPadService.listItemServiceCategries());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchpad/updateUserApps</b>
     * <p>编辑用户首页数据</p>
     */
    @RequestMapping("updateUserApps")
    @RestReturn(value=String.class)
    public RestResponse updateUserApps(UpdateUserAppsCommand cmd) {
        launchPadService.updateUserApps(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchpad/listAllApps</b>
     * <p>广场根据组件获取全部应用</p>
     */
    @RequestMapping("listAllApps")
    @RestReturn(value=ListAllAppsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listAllApps(ListAllAppsCommand cmd) {

        ListAllAppsResponse res = launchPadService.listAllApps(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

}
