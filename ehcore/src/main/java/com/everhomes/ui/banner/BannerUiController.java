// @formatter:off
package com.everhomes.ui.banner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.banner.BannerService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.MessagingKickoffService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.util.EtagHelper;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中，这类API属于比较高层的API，专门服务于界面</li>
 * </ul>
 */
@RestDoc(value="BannerUi controller", site="bannerui")
@RestController
@RequestMapping("/ui/banner")
public class BannerUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerUiController.class);
    private static final String MARKETDATA_ITEM_VERSION = "marketdata.item.version";
    
    @Autowired
    private ConfigurationProvider configurationProvider;
   
    @Autowired
    private BannerService bannerService;

    @Autowired
    private MessagingKickoffService kickoffService;
    
    /**
     * <b>URL: /ui/banner/getBannersByScene</b>
     * <p>获取指定位置、layout组、场景、实体对象相关的banner</p>
     */
    @RequestMapping("getBannersByScene")
    @RestReturn(value=BannerDTO.class,collection=true)
    @RequireAuthentication(false)
    public RestResponse getBannersByScene(@Valid GetBannersBySceneCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        List<BannerDTO> result = bannerService.getBannersByScene(cmd, request);
        RestResponse resp =  new RestResponse(result);
        // 在多场景的情况下，etag不能使用，以免不同的场景拿不到数据 by lqs 20160617
//        int hashCode = configurationProvider.getIntValue(MARKETDATA_ITEM_VERSION, 0);
//        if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
//            resp.setResponseObject(result);
//        }
        
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
}
