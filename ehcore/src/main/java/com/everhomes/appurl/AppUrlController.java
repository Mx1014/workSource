package com.everhomes.appurl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/appUrl")
public class AppUrlController extends ControllerBase {
	
	@Autowired
	private AppUrlService appUrlService;

	@RequireAuthentication(false)
	@RequestMapping("getAppInfo")
    @RestReturn(value=AppUrlDTO.class)
    public RestResponse getAppInfo(@Valid GetAppInfoCommand cmd) {

    	AppUrlDTO cmdResponse = this.appUrlService.getAppInfo(cmd);
        return new RestResponse(cmdResponse);
    }
}
