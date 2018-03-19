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
