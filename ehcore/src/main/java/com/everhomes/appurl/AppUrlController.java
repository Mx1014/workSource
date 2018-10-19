package com.everhomes.appurl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.CreateAppInfoCommand;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.appurl.AppInfoByNamespaceIdDTO;
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
	
	/**
     * <b>URL: /appUrl/createAppInfo</b>
     * <p>保存appurl信息</p>
     */
	@RequestMapping("createAppInfo")
    @RestReturn(value=String.class)
	public RestResponse createAppInfo(CreateAppInfoCommand  cmd) {

	    this.appUrlService.createAppInfo(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
        return response;
	}
	
	/**
     * <b>URL: /appUrl/getAppInfoByNamespaceId</b>
     * <p>通过namespaceId 查询appurl信息</p>
     */
	@RequireAuthentication(false)
	@RequestMapping("getAppInfoByNamespaceId")
    @RestReturn(value=AppInfoByNamespaceIdDTO.class)
	public RestResponse getAppInfoByNamespaceId(GetAppInfoCommand cmd) {

		AppInfoByNamespaceIdDTO dto = this.appUrlService.getAppInfoByNamespaceId(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
        return response;
	}
}
