// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.news.NewsService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "/ui/ListCommunityOrganizationsResponse Controller", site = "core")
@RestController
@RequestMapping("/ui/ListCommunityOrganizationsResponse")
@RequireAuthentication(false)
public class VisitorSysUiController extends ControllerBase {

	@Autowired
	private VisitorSysService visitorSysService;

//	/**
//	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listBookedVisitors</b>
//	 * <p>
//	 * 1.获取预约访客列表-原生客户端
//	 * </p>
//	 */
//	@RequestMapping("listBookedVisitors")
//	@RestReturn(ListBookedVisitorsResponse.class)
//	public RestResponse listBookedVisitors(ListBookedVisitorsCommand cmd) {
//		CreateNewsResponse createNewsResponse = visitorSysService.listBookedVisitors(cmd);
//
//		RestResponse response = new RestResponse(createNewsResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/getPairingCode</b>
	 * <p>
	 * 1.获取配对码-原生客户端
	 * </p>
	 */
	@RequestMapping("getPairingCode")
	@RestReturn(GetPairingCodeResponse.class)
	public RestResponse getPairingCode(GetPairingCodeCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.getPairingCode(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/confirmPairingCode</b>
	 * <p>
	 * 2.验证配对码是否配对成功(延迟接口)-原生客户端
	 * </p>
	 */
	@RequestMapping("confirmPairingCode")
	@RestReturn(ConfirmPairingCodeResponse.class)
	public RestResponse confirmPairingCode(ConfirmPairingCodeCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.confirmPairingCode(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/getConfiguration</b>
	 * <p>
	 * 3.获取配置-原生客户端
	 * </p>
	 */
	@RequestMapping("getConfiguration")
	@RestReturn(GetConfigurationResponse.class)
	public RestResponse getConfiguration(GetConfigurationCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.getConfiguration(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/createOrUpdateVisitor</b>
	 * <p>
	 * 4.创建临时/预约访客-原生客户端
	 * </p>
	 */
	@RequestMapping("createOrUpdateVisitor")
	@RestReturn(CreateOrUpdateVisitorResponse.class)
	public RestResponse createOrUpdateVisitor(CreateOrUpdateVisitorCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.createOrUpdateVisitor(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listOfficeLocations</b>
	 * <p>
	 * 5.获取企业办公地点列表(企业访客)-原生客户端
	 * </p>
	 */
	@RequestMapping("listOfficeLocations")
	@RestReturn(ListOfficeLocationsResponse.class)
	public RestResponse listOfficeLocations(ListOfficeLocationsCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.listOfficeLocations(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listCommunityOrganizations</b>
	 * <p>
	 * 6.获取园区企业列表(园区访客，此接口否使用接口待定)-原生客户端
	 * </p>
	 */
	@RequestMapping("listCommunityOrganizations")
	@RestReturn(ListCommunityOrganizationsResponse.class)
	public RestResponse listCommunityOrganizations(ListCommunityOrganizationsCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.listCommunityOrganizations(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listVisitReasons</b>
	 * <p>
	 * 7.获取事由列表（此接口否使用接口待定，可以使用枚举）-原生客户端
	 * </p>
	 */
	@RequestMapping("listVisitReasons")
	@RestReturn(ListVisitReasonsResponse.class)
	public RestResponse listVisitReasons(ListVisitReasonsCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.listVisitReasons(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
//
//	/**
//	 * <b>URL: /ui/ListCommunityOrganizationsResponse/deleteVisitor</b>
//	 * <p>
//	 * 4.删除访客-原生客户端
//	 * </p>
//	 */
//	@RequestMapping("deleteVisitor")
//	@RestReturn(DeleteVisitorResponse.class)
//	public RestResponse deleteVisitor(DeleteVisitorCommand cmd) {
//		CreateNewsResponse createNewsResponse = visitorSysService.deleteVisitor(cmd);
//
//		RestResponse response = new RestResponse(createNewsResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}


	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/sendSMSVerificationCode</b>
	 * <p>
	 * 8.发送短信验证码-原生客户端
	 * </p>
	 */
	@RequestMapping("sendSMSVerificationCode")
	@RestReturn(SendSMSVerificationCodeResponse.class)
	public RestResponse sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.sendSMSVerificationCode(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/confirmVerificationCode</b>
	 * <p>
	 * 9.验证短信验证码(并返回预约列表)-原生客户端
	 * </p>
	 */
	@RequestMapping("confirmVerificationCode")
	@RestReturn(ConfirmVerificationCodeResponse.class)
	public RestResponse sendSMSVerificationCode(ConfirmVerificationCodeCommand cmd) {
		CreateNewsResponse createNewsResponse = visitorSysService.confirmVerificationCode(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
