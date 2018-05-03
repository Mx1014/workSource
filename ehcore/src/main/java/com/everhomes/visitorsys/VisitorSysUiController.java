// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestDoc(value = "/ui/visitorsys Controller", site = "core")
@RestController
@RequestMapping("/ui/visitorsys")
@RequireAuthentication(false)
public class VisitorSysUiController extends ControllerBase {

	@Autowired
	private VisitorSysService visitorSysService;

	/**
	 * <b>URL: /ui/visitorsys/getHomePageConfiguration</b>
	 * <p>
	 * 1.获取首页配置-原生客户端
	 * </p>
	 */
	@RequestMapping("getHomePageConfiguration")
	@RestReturn(GetHomePageConfigurationResponse.class)
	public RestResponse getHomePageConfiguration() {
		GetHomePageConfigurationResponse baseResponse = visitorSysService.getHomePageConfiguration();

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/getPairingCode</b>
	 * <p>
	 * 2.获取配对码-原生客户端
	 * </p>
	 */
	@RequestMapping("getPairingCode")
	@RestReturn(GetPairingCodeResponse.class)
	public RestResponse getPairingCode(GetPairingCodeCommand cmd) {
		GetPairingCodeResponse baseResponse = visitorSysService.getPairingCode(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/confirmPairingCode</b>
	 * <p>
	 * 3.验证配对码是否配对成功(延迟接口)-原生客户端
	 * </p>
	 */
	@RequestMapping("confirmPairingCode")
	@RestReturn(String.class)
	public DeferredResult<RestResponse> confirmPairingCode(ConfirmPairingCodeCommand cmd) {
		return visitorSysService.confirmPairingCode(cmd);
	}

	/**
	 * <b>URL: /ui/visitorsys/getConfiguration</b>
	 * <p>
	 * 4.获取配置-原生客户端
	 * </p>
	 */
	@RequestMapping("getConfiguration")
	@RestReturn(GetUIConfigurationResponse.class)
	public RestResponse getUIConfiguration(GetUIConfigurationCommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.getUIConfiguration(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/getEnterpriseForm</b>
	 * <p>
	 * 5.根据选择的公司获取表单配置(园区访客用)-原生客户端
	 * </p>
	 */
	@RequestMapping("getEnterpriseConfiguration")
	@RestReturn(GetEnterpriseFormResponse.class)
	public RestResponse getEnterpriseForm(GetEnterpriseFormCommand cmd) {
		GetEnterpriseFormResponse baseResponse = visitorSysService.getEnterpriseForm(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/createOrUpdateVisitor</b>
	 * <p>
	 * 6.创建临时/预约访客-原生客户端
	 * </p>
	 */
	@RequestMapping("createOrUpdateVisitor")
	@RestReturn(CreateOrUpdateVisitorUIResponse.class)
	public RestResponse createOrUpdateVisitor(CreateOrUpdateVisitorUICommand cmd) {
		CreateOrUpdateVisitorUIResponse baseResponse = visitorSysService.createOrUpdateUIVisitor(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listOfficeLocations</b>
	 * <p>
	 * 7.获取企业办公地点列表(企业访客)-原生客户端
	 * </p>
	 */
	@RequestMapping("listOfficeLocations")
	@RestReturn(ListUIOfficeLocationsResponse.class)
	public RestResponse listOfficeLocations(ListUIOfficeLocationsCommand cmd) {
		ListUIOfficeLocationsResponse baseResponse = visitorSysService.listUIOfficeLocations(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listCommunityOrganizations</b>
	 * <p>
	 * 8.获取园区企业列表(园区访客)-原生客户端
	 * </p>
	 */
	@RequestMapping("listCommunityOrganizations")
	@RestReturn(ListUICommunityOrganizationsResponse.class)
	public RestResponse listCommunityOrganizations(ListUICommunityOrganizationsCommand cmd) {
		ListUICommunityOrganizationsResponse baseResponse  = visitorSysService.listUICommunityOrganizations(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listVisitReasons</b>
	 * <p>
	 * 9.获取事由列表（此接口否使用接口待定，可以使用枚举）-原生客户端
	 * </p>
	 */
	@RequestMapping("listVisitReasons")
	@RestReturn(ListUIVisitReasonsResponse.class)
	public RestResponse listVisitReasons(BaseVisitorsysUICommand cmd) {
		ListUIVisitReasonsResponse createNewsResponse = visitorSysService.listUIVisitReasons(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/sendSMSVerificationCode</b>
	 * <p>
	 * 10.发送短信验证码-原生客户端
	 * </p>
	 */
	@RequestMapping("sendSMSVerificationCode")
	@RestReturn(String.class)
	public RestResponse sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd) {
		visitorSysService.sendSMSVerificationCode(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /ui/visitorsys/confirmVerificationCode</b>
	 * <p>
	 * 11.验证短信验证码(并返回预约列表)-原生客户端
	 * </p>
	 */
	@RequestMapping("confirmVerificationCode")
	@RestReturn(String.class)
	public RestResponse sendSMSVerificationCode(ConfirmVerificationCodeCommand cmd) {
		visitorSysService.confirmVerificationCode(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
