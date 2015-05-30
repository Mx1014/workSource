package com.everhomes.launchpad;





import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;


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
    
}
