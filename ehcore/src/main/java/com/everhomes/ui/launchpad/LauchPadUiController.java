// @formatter:off
package com.everhomes.ui.launchpad;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.ui.launchpad.*;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.messaging.MessagingKickoffService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.business.CancelFavoriteBusinessCommand;
import com.everhomes.rest.business.FavoriteBusinessesCommand;
import com.everhomes.util.EtagHelper;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中，这类API属于比较高层的API，专门服务于界面</li>
 * </ul>
 */
@RestDoc(value="LaunchPadUi controller", site="launchpadui")
@RestController
@RequestMapping("/ui/launchpad")
public class LauchPadUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LauchPadUiController.class);
    
    private static final String MARKETDATA_ITEM_VERSION = "marketdata.item.version";
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private LaunchPadService launchPadService;

    @Autowired
    private MessagingKickoffService kickoffService;
    
    /**
     * <b>URL: /ui/launchpad/getLaunchPadItemsByScene</b>
     * <p>根据位置、layout组、指定场景和相应的实体对象获取item列表</p>
     */
    @RequestMapping("getLaunchPadItemsByScene")
    @RestReturn(value=GetLaunchPadItemsCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse getLaunchPadItemsByScene(@Valid GetLaunchPadItemsBySceneCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        GetLaunchPadItemsCommandResponse commandResponse = launchPadService.getLaunchPadItemsByScene(cmd, request);
        RestResponse resp =  new RestResponse(commandResponse);
//        if(commandResponse.getLaunchPadItems() != null && !commandResponse.getLaunchPadItems().isEmpty()){
//            int hashCode = configurationProvider.getIntValue(MARKETDATA_ITEM_VERSION, 0);
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
     * <b>URL: /ui/launchpad/getLastLaunchPadLayoutByScene</b>
     * <p>根据版本号获取可兼容的最新layout信息</p>
     */
    @RequestMapping("getLastLaunchPadLayoutByScene")
    @RestReturn(value=LaunchPadLayoutDTO.class)
    @RequireAuthentication(false)
    public RestResponse getLastLaunchPadLayoutByScene(@Valid GetLaunchPadLayoutBySceneCommand cmd, HttpServletRequest request,HttpServletResponse response) {
        LaunchPadLayoutDTO launchPadLayoutDTO = this.launchPadService.getLastLaunchPadLayoutByScene(cmd);
        RestResponse resp =  new RestResponse();
        // 有域空间时，这样判断ETAG会导致有些域空间拿不到数据（同一个域空间不同场景切换也有问题），先暂时不使用ETAG by lqs 20160514
//        if(launchPadLayoutDTO != null){
//            long hashCode = launchPadLayoutDTO.getVersionCode();
//            if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
//                resp.setResponseObject(launchPadLayoutDTO);
//            }
//        }
        resp.setResponseObject(launchPadLayoutDTO);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /ui/launchpad/favoriteBusinessesByScene</b>
     * <p>店铺收藏（放到服务市场首页）</p>
     */
    @RequestMapping("favoriteBusinessesByScene")
    @RestReturn(value=String.class)
    public RestResponse favoriteBusinessesByScene(FavoriteBusinessesBySceneCommand cmd) {
        
        this.launchPadService.favoriteBusinessesByScene(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/launchpad/cancelFavoriteBusinessByScene</b>
     * <p>用户取消收藏</p>
     */
    @RequestMapping("cancelFavoriteBusinessByScene")
    @RestReturn(value=String.class)
    public RestResponse cancelFavoriteBusinessByScene(CancelFavoriteBusinessBySceneCommand cmd) {
        
    	this.launchPadService.cancelFavoriteBusinessByScene(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/launchpad/getMoreItemsByScene</b>
     * <p>根据位置、layout组、指定场景和相应的实体对象更多的item</p>
     */
    @RequestMapping("getMoreItemsByScene")
    @RestReturn(value=GetLaunchPadItemsCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse getMoreItemsByScene(@Valid GetLaunchPadItemsBySceneCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        GetLaunchPadItemsCommandResponse commandResponse = this.launchPadService.getMoreItemsByScene(cmd, request);
    	RestResponse resp =  new RestResponse(commandResponse);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /ui/launchpad/getAllCategryItemsByScene</b>
     * <p>根据位置、layout组、指定场景和相应的实体对象以及类别获取全部的item</p>
     */
    @RequestMapping("getAllCategryItemsByScene")
    @RestReturn(value=CategryItemDTO.class, collection = true)
    @RequireAuthentication(false)
    public RestResponse getAllCategryItemsByScene(@Valid GetLaunchPadItemsBySceneCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        RestResponse resp =  new RestResponse(launchPadService.getAllCategryItemsByScene(cmd, request));
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /ui/launchpad/addLaunchPadItemByScene</b>
     * <p>添加item到桌面</p>
     */
    @RequestMapping("addLaunchPadItemByScene")
    @RestReturn(value=UserLaunchPadItemDTO.class)
    public RestResponse addLaunchPadItemByScene(AddLaunchPadItemBySceneCommand cmd) {
    	UserLaunchPadItemDTO userItemDTO = this.launchPadService.addLaunchPadItemByScene(cmd);
        RestResponse response =  new RestResponse(userItemDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/launchpad/deleteLaunchPadItemByScene</b>
     * <p>删除桌面的item</p>
     */
    @RequestMapping("deleteLaunchPadItemByScene")
    @RestReturn(value=UserLaunchPadItemDTO.class)
    public RestResponse deleteLaunchPadItemByScene(DeleteLaunchPadItemBySceneCommand cmd) {
    	UserLaunchPadItemDTO userItemDTO = this.launchPadService.deleteLaunchPadItemByScene(cmd);
        RestResponse response =  new RestResponse(userItemDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/launchpad/reorderLaunchPadItemByScene</b>
     * <p>排序桌面的item</p>
     */
    @RequestMapping("reorderLaunchPadItemByScene")
    @RestReturn(value=String.class)
    public RestResponse reorderLaunchPadItemByScene(ReorderLaunchPadItemBySceneCommand cmd) {
    	this.launchPadService.reorderLaunchPadItemByScene(cmd, ItemDisplayFlag.DISPLAY);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/launchpad/reorderMoreItemByScene</b>
     * <p>排序更多的item</p>
     */
    @RequestMapping("reorderMoreItemByScene")
    @RestReturn(value=String.class)
    public RestResponse reorderMoreItemByScene(ReorderLaunchPadItemBySceneCommand cmd) {
    	this.launchPadService.reorderLaunchPadItemByScene(cmd, ItemDisplayFlag.HIDE);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /ui/launchpad/editLaunchPadItemByScene</b>
     * <p>编辑服务广场的item，即最终点击完成时批量添加或者删除服务广场的item</p>
     */
    @RequestMapping("editLaunchPadItemByScene")
    @RestReturn(value=String.class)
    public RestResponse editLaunchPadItemByScene(EditLaunchPadItemBySceneCommand cmd) {
        this.launchPadService.editLaunchPadItemByScene(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
