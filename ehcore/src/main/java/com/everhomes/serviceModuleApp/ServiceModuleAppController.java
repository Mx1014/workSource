package com.everhomes.serviceModuleApp;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicehotline.*;
import com.everhomes.rest.servicemoduleapp.InstallAppCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerResponse;
import com.everhomes.rest.servicemoduleapp.UninstallAppCommand;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/servicemoduleapp")
public class ServiceModuleAppController extends ControllerBase {

	@Autowired
	private ServiceModuleAppService serviceModuleAppService;


	/**
	 * banner定制的查询应用信息
	 * @param cmd
	 * @return
	 */
	@RequestMapping("listServiceModuleAppsForBanner")
	@RestReturn(value=ListServiceModuleAppsForBannerResponse.class)
	public RestResponse listServiceModuleAppsForBanner(ListServiceModuleAppsForBannerCommand cmd) {
		ListServiceModuleAppsForBannerResponse res = serviceModuleAppService.listServiceModuleAppsForBanner(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * 给公司安装应用
	 * @param cmd
	 * @return
	 */
	@RequestMapping("installApp")
	@RestReturn(value=ServiceModuleAppDTO.class)
	public RestResponse installApp(InstallAppCommand cmd) {
		ServiceModuleAppDTO res = serviceModuleAppService.installApp(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 给公司安装应用
	 * @param cmd
	 * @return
	 */
	@RequestMapping("uninstallApp")
	@RestReturn(value=String.class)
	public RestResponse uninstallApp(UninstallAppCommand cmd) {
		serviceModuleAppService.uninstallApp(cmd);
		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
