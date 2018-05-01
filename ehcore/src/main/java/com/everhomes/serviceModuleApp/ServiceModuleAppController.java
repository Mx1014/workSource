package com.everhomes.serviceModuleApp;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicemoduleapp")
public class ServiceModuleAppController extends ControllerBase {

	@Autowired
	private ServiceModuleAppService serviceModuleAppService;


	/**
	 * <p>banner定制的查询应用信息</p>
	 * <b>URL: /servicemoduleapp/listServiceModuleAppsForBanner</b>
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
	 * <p>给公司安装应用</p>
	 * <b>URL: /servicemoduleapp/installApp</b>
	 */
	@RequestMapping("installApp")
	@RestReturn(value=ServiceModuleAppDTO.class)
	public RestResponse installApp(InstallAppCommand cmd) {
		//TODO 需要对接权限，当前公司的管理员才可以操作
		ServiceModuleAppDTO res = serviceModuleAppService.installApp(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>给公司卸载应用</p>
	 * <b>URL: /servicemoduleapp/uninstallApp</b>
	 */
	@RequestMapping("uninstallApp")
	@RestReturn(value=String.class)
	public RestResponse uninstallApp(UninstallAppCommand cmd) {
		//TODO 需要对接权限，当前公司的管理员才可以操作
		serviceModuleAppService.uninstallApp(cmd);
		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>启用、禁用app</p>
	 * <b>URL: /servicemoduleapp/updateStatus</b>
	 */
	@RequestMapping("updateStatus")
	@RestReturn(value=String.class)
	public RestResponse updateStatus(UpdateStatusCommand cmd) {
		//TODO 需要对接权限，当前公司的管理员才可以操作
		serviceModuleAppService.updateStatus(cmd);
		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>查询公司应用，按照类型、是否安装等条件查询</p>
	 * <b>URL: /servicemoduleapp/listServiceModuleApps</b>
	 */
	@RequestMapping("listServiceModuleApps")
	@RestReturn(value=ListServiceModuleAppsByOrgIdResponse.class)
	public RestResponse listServiceModuleAppsByOrganization(ListServiceModuleAppsByOrgIdCommand cmd) {
		ListServiceModuleAppsByOrgIdResponse res = serviceModuleAppService.listServiceModuleAppsByOrgId(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>园区是否跟随默认方案</p>
	 * <b>URL: /servicemoduleapp/changeCommunityConfigFlag</b>
	 */
	@RequestMapping("changeCommunityConfigFlag")
	@RestReturn(value=String.class)
	public RestResponse changeCommunityConfigFlag(ChangeCommunityConfigFlagCommand cmd) {
		//TODO 需要对接权限，当前公司的管理员才可以操作
		serviceModuleAppService.changeCommunityConfigFlag(cmd);
		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取园区自定义app配置信息</p>
	 * <b>URL: /servicemoduleapp/listAppCommunityConfigs</b>
	 */
	@RequestMapping("listAppCommunityConfig")
	@RestReturn(value=ListAppCommunityConfigsResponse.class)
	public RestResponse listAppCommunityConfigs(ListAppCommunityConfigsCommand cmd) {
		ListAppCommunityConfigsResponse res = serviceModuleAppService.listAppCommunityConfigs(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>更新园区自定义app配置信息</p>
	 * <b>URL: /servicemoduleapp/updateAppCommunityConfig</b>
	 */
	@RequestMapping("updateAppCommunityConfig")
	@RestReturn(value=String.class)
	public RestResponse updateAppCommunityConfig(UpdateAppCommunityConfigCommand cmd) {
		//TODO 需要对接权限，当前公司的管理员才可以操作
		serviceModuleAppService.updateAppCommunityConfig(cmd);
		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
