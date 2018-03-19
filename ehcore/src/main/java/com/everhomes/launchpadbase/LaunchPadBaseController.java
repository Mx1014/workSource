package com.everhomes.launchpadbase;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="LaunchPadBase controller", site="core")
@RestController
@RequestMapping("/launchpadbase")
public class LaunchPadBaseController extends ControllerBase {

    /**
     * <b>URL: /launchpad/listIndex</b>
     * <p>获取主页签</p>
     */
    @RequestMapping("listIndex")
    @RestReturn(value=IndexDTO.class)
    public RestResponse listIndex() {
        RestResponse response =  new RestResponse();
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
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpad/listLaunchPadApps</b>
     * <p>广场根据组件获取应用</p>
     */
    @RequestMapping("listLaunchPadApps")
    @RestReturn(value=AppDTO.class)
    public RestResponse listLaunchPadApps(ListLaunchPadAppsCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchpad/listLaunchPadOAApps</b>
     * <p>工作台获取OA应用</p>
     */
    @RequestMapping("listLaunchPadOAApps")
    @RestReturn(value=AppDTO.class)
    public RestResponse listLaunchPadOAApps(ListLaunchPadOAAppsCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpad/listLaunchPadManagerApps</b>
     * <p>工作台获取园区管理应用</p>
     */
    @RequestMapping("listLaunchPadManagerApps")
    @RestReturn(value=AppDTO.class)
    public RestResponse listLaunchPadManagerApps(ListLaunchPadManagerAppsCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpad/listOPPushCards</b>
     * <p>获取运营板块信息</p>
     */
    @RequestMapping("listOPPushCards")
    @RestReturn(value=OPPushCards.class)
    public RestResponse listOPPushCards(ListOPPushCardsCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
