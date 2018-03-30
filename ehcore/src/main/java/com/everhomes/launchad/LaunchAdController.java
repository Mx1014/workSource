// @formatter:off
package com.everhomes.launchad;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.launchad.GetLaunchAdCommand;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App启动广告模块
 * Created by xq.tian on 2016/12/9.
 */
@RestDoc(value = "Launch advertisement controller", site = "core")
@RequestMapping("/launchad")
@RestController
public class LaunchAdController extends ControllerBase {

    @Autowired
    private LaunchAdService launchAdService;

    /**
     * <b>URL: /launchad/getLaunchad</b>
     * <p>获取启动广告数据</p>
     */
    @RequestMapping("getLaunchad")
    @RestReturn(value = LaunchAdDTO.class)
    @RequireAuthentication(false)
    public RestResponse geLaunchAd(GetLaunchAdCommand cmd) {
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        return response(launchAdService.getLaunchAd(cmd));
    }

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
