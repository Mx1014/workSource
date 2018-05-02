// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.CreateNewsResponse;
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
	 * <b>URL: /ui/visitorsys/getPairingCode</b>
	 * <p>
	 * 1.获取配对码-原生客户端
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
	 * 2.验证配对码是否配对成功(延迟接口)-原生客户端
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
	 * 3.获取配置-原生客户端
	 * </p>
	 */
	@RequestMapping("getConfiguration")
	@RestReturn(GetConfigurationResponse.class)
	public RestResponse getUIConfiguration(BaseVisitorsysUICommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.getUIConfiguration(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/createOrUpdateVisitor</b>
	 * <p>
	 * 4.创建临时/预约访客-原生客户端
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
	 * 5.获取企业办公地点列表(企业访客)-原生客户端
	 * </p>
	 */
	@RequestMapping("listOfficeLocations")
	@RestReturn(ListOfficeLocationsResponse.class)
	public RestResponse listOfficeLocations(BaseVisitorsysUICommand cmd) {
		ListOfficeLocationsResponse baseResponse = visitorSysService.listUIOfficeLocations(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listCommunityOrganizations</b>
	 * <p>
	 * 6.获取园区企业列表(园区访客，此接口否使用接口待定)-原生客户端
	 * </p>
	 */
	@RequestMapping("listCommunityOrganizations")
	@RestReturn(ListCommunityOrganizationsResponse.class)
	public RestResponse listCommunityOrganizations(BaseVisitorsysUICommand cmd) {
		ListOfficeLocationsResponse baseResponse  = visitorSysService.listUICommunityOrganizations(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listVisitReasons</b>
	 * <p>
	 * 7.获取事由列表（此接口否使用接口待定，可以使用枚举）-原生客户端
	 * </p>
	 */
	@RequestMapping("listVisitReasons")
	@RestReturn(ListVisitReasonsResponse.class)
	public RestResponse listVisitReasons(BaseVisitorsysUICommand cmd) {
		ListVisitReasonsResponse createNewsResponse = visitorSysService.listUIVisitReasons(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/sendSMSVerificationCode</b>
	 * <p>
	 * 8.发送短信验证码-原生客户端
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
	 * 9.验证短信验证码(并返回预约列表)-原生客户端
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
