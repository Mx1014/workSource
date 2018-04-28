// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "ListCommunityOrganizationsResponse Controller", site = "core")
@RestController
@RequestMapping("/ListCommunityOrganizationsResponse")
public class VisitorSysController extends ControllerBase {

	@Autowired
	private VisitorSysService visitorSysService;

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listBookedVisitors</b>
	 * <p>
	 * 1.获取预约访客/访客管理列表-后台管理
	 * </p>
	 */
	@RequestMapping("listBookedVisitors")
	@RestReturn(ListBookedVisitorsResponse.class)
	public RestResponse listBookedVisitors(ListBookedVisitorsCommand cmd) {
		ListBookedVisitorsResponse baseResponse = visitorSysService.listBookedVisitors(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/getBookedVisitorById</b>
	 * <p>
	 * 2.获取预约详情-后台管理
	 * </p>
	 */
	@RequestMapping("getBookedVisitorById")
	@RestReturn(GetBookedVisitorByIdResponse.class)
	public RestResponse getBookedVisitorById(GetBookedVisitorByIdCommand cmd) {
		GetBookedVisitorByIdResponse baseResponse = visitorSysService.getBookedVisitorById(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listOfficeLocations</b>
	 * <p>
	 * 3.获取企业办公地点列表-后台管理
	 * </p>
	 */
	@RequestMapping("listOfficeLocations")
	@RestReturn(ListOfficeLocationsResponse.class)
	public RestResponse listOfficeLocations(ListOfficeLocationsCommand cmd) {
		ListOfficeLocationsResponse baseResponse = visitorSysService.listOfficeLocations(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listCommunityOrganizations</b>
	 * <p>
	 * 4.获取园区企业列表(园区访客，此接口否使用接口待定)-后台管理
	 * </p>
	 */
	@RequestMapping("listCommunityOrganizations")
	@RestReturn(ListCommunityOrganizationsResponse.class)
	public RestResponse listCommunityOrganizations(ListCommunityOrganizationsCommand cmd) {
		ListCommunityOrganizationsResponse baseResponse = visitorSysService.listCommunityOrganizations(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listVisitReasons</b>
	 * <p>
	 * 5.获取访客事由列表-后台管理
	 * </p>
	 */
	@RequestMapping("listVisitReasons")
	@RestReturn(ListVisitReasonsResponse.class)
	public RestResponse listVisitReasons(BaseVisitorsysCommand cmd) {
		ListVisitReasonsResponse baseResponse = visitorSysService.listVisitReasons(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/createOrUpdateVisitor</b>
	 * <p>
	 * 6.创建临时/预约访客-后台管理
	 * </p>
	 */
	@RequestMapping("createOrUpdateVisitor")
	@RestReturn(GetBookedVisitorByIdResponse.class)
	public RestResponse createOrUpdateVisitor(CreateOrUpdateVisitorCommand cmd) {
		GetBookedVisitorByIdResponse baseResponse = visitorSysService.createOrUpdateVisitor(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/sendVisitorSMS</b>
	 * <p>
	 * 7.给访客发送邀请函-后台管理
	 * </p>
	 */
	@RequestMapping("sendVisitorSMS")
	@RestReturn(String.class)
	public RestResponse sendVisitorSMS(GetBookedVisitorByIdCommand cmd) {
		visitorSysService.sendVisitorSMS(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/deleteVisitor</b>
	 * <p>
	 * 8.删除访客邀请-后台管理
	 * </p>
	 */
	@RequestMapping("deleteVisitor")
	@RestReturn(String.class)
	public RestResponse deleteVisitor(GetBookedVisitorByIdCommand cmd) {
		visitorSysService.deleteVisitor(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/confirmVisitor</b>
	 * <p>
	 * 9.访客到访确认接口-后台管理
	 * </p>
	 */
	@RequestMapping("confirmVisitor")
	@RestReturn(String.class)
	public RestResponse confirmVisitor(GetBookedVisitorByIdCommand cmd) {
		visitorSysService.confirmVisitor(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/getStatistics</b>
	 * <p>
	 * 10.统计接口-后台管理
	 * </p>
	 */
	@RequestMapping("getStatistics")
	@RestReturn(GetStatisticsResponse.class)
	public RestResponse getStatistics(GetStatisticsCommand cmd) {
		GetStatisticsResponse baseResponse = visitorSysService.getStatistics(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/addDevice</b>
	 * <p>
	 * 11.添加设备(ipad/打印机)接口-后台管理
	 * </p>
	 */
	@RequestMapping("addDevice")
	@RestReturn(AddDeviceResponse.class)
	public RestResponse addDevices(AddDeviceCommand cmd) {
		AddDeviceResponse baseResponse = visitorSysService.addDevice(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listDevices</b>
	 * <p>
	 * 12.获取设备(ipad/打印机)接口列表-后台管理
	 * </p>
	 */
	@RequestMapping("listDevices")
	@RestReturn(ListDevicesResponse.class)
	public RestResponse listDevices(BaseVisitorsysCommand cmd) {
		ListDevicesResponse baseResponse = visitorSysService.listDevices(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/deleteDevice</b>
	 * <p>
	 * 13.删除设备(ipad/打印机)-后台管理
	 * </p>
	 */
	@RequestMapping("deleteDevice")
	@RestReturn(String.class)
	public RestResponse deleteDevice(DeleteDeviceCommand cmd) {
		visitorSysService.deleteDevice(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/createOrUpdateOfficeLocation</b>
	 * <p>
	 * 14.添加/更新企业办公地点-后台管理
	 * </p>
	 */
	@RequestMapping("createOrUpdateOfficeLocation")
	@RestReturn(BaseOfficeLocationDTO.class)
	public RestResponse createOrUpdateOfficeLocation(CreateOrUpdateOfficeLocationCommand cmd) {
		BaseOfficeLocationDTO baseResponse = visitorSysService.createOrUpdateOfficeLocation(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/deleteOfficeLocation</b>
	 * <p>
	 * 15.删除企业办公地点-后台管理
	 * </p>
	 */
	@RequestMapping("deleteOfficeLocation")
	@RestReturn(String.class)
	public RestResponse deleteOfficeLocation(DeleteOfficeLocationCommand cmd) {
		visitorSysService.deleteOfficeLocation(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/getConfiguration</b>
	 * <p>
	 * 16.获取配置-后台管理
	 * </p>
	 */
	@RequestMapping("getConfiguration")
	@RestReturn(GetConfigurationResponse.class)
	public RestResponse getConfiguration(BaseVisitorsysCommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.getConfiguration(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/updateConfiguration</b>
	 * <p>
	 * 17.更新配置-后台管理
	 * </p>
	 */
	@RequestMapping("updateConfiguration")
	@RestReturn(String.class)
	public RestResponse updateConfiguration(UpdateConfigurationCommand cmd) {
		baseResponse baseResponse = visitorSysService.updateConfiguration(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listBlackLists</b>
	 * <p>
	 * 18.获取黑名单列表-后台管理
	 * </p>
	 */
	@RequestMapping("listBlackLists")
	@RestReturn(ListBlackListsResponse.class)
	public RestResponse listBlackLists(BaseVisitorsysCommand cmd) {
		baseResponse baseResponse = visitorSysService.listBlackLists(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/deleteBlackList</b>
	 * <p>
	 * 19.删除黑名单-后台管理
	 * </p>
	 */
	@RequestMapping("deleteBlackList")
	@RestReturn(String.class)
	public RestResponse deleteBlackList(DeleteBlackListCommand cmd) {
		baseResponse baseResponse = visitorSysService.deleteBlackList(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/createOrUpdateBlackList</b>
	 * <p>
	 * 20.添加/更新黑名单-后台管理
	 * </p>
	 */
	@RequestMapping("createOrUpdateBlackList")
	@RestReturn(CreateOrUpdateBlackListResponse.class)
	public RestResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd) {
		baseResponse baseResponse = visitorSysService.createOrUpdateBlackList(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listDoorGuards</b>
	 * <p>
	 * 21.门禁列表-后台管理
	 * </p>
	 */
	@RequestMapping("listDoorGuards")
	@RestReturn(ListDoorGuardsResponse.class)
	public RestResponse listDoorGuards(ListDoorGuardsCommand cmd) {
		baseResponse baseResponse = visitorSysService.listDoorGuards(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/getConfigurationForWeb</b>
	 * <p>
	 * 1.获取配置-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getConfigurationForWeb")
	@RestReturn(GetConfigurationForWebResponse.class)
	public RestResponse getConfigurationForWeb(GetConfigurationForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.getConfigurationForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listBookedVisitorsForWeb</b>
	 * <p>
	 * 2.获取预约访客列表-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listBookedVisitorsForWeb")
	@RestReturn(ListBookedVisitorsForWebResponse.class)
	public RestResponse listBookedVisitorsForWeb(ListBookedVisitorsForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.listBookedVisitorsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/getBookedVisitorByIdForWeb</b>
	 * <p>
	 * 3.获取预约详情-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getBookedVisitorByIdForWeb")
	@RestReturn(getBookedVisitorByIdForWebResponse.class)
	public RestResponse getBookedVisitorByIdForWeb(getBookedVisitorByIdForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.getBookedVisitorByIdForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/listVisitReasonsForWeb</b>
	 * <p>
	 * 4.获取事由列表（此接口否使用接口待定，可以使用枚举）-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listVisitReasonsForWeb")
	@RestReturn(ListVisitReasonsForWebResponse.class)
	public RestResponse listVisitReasonsForWeb(ListVisitReasonsForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.listVisitReasonsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/createOrUpdateVisitorForWeb</b>
	 * <p>
	 * 5.创建临时/预约访客-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("createOrUpdateVisitorForWeb")
	@RestReturn(CreateOrUpdateVisitorForWebResponse.class)
	public RestResponse createOrUpdateVisitorForWeb(CreateOrUpdateVisitorForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.createOrUpdateVisitorForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ListCommunityOrganizationsResponse/deleteVisitorForWeb</b>
	 * <p>
	 * 6.删除访客-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("deleteVisitorForWeb")
	@RestReturn(DeleteVisitorForWebResponse.class)
	public RestResponse deleteVisitorForWeb(DeleteVisitorForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.deleteVisitorForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listOfficeLocationsForWeb</b>
	 * <p>
	 * 7.获取企业办公地点列表(企业访客)-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listOfficeLocationsForWeb")
	@RestReturn(ListOfficeLocationsResponse.class)
	public RestResponse listOfficeLocationsForWeb(ListOfficeLocationsCommand cmd) {
		baseResponse baseResponse = visitorSysService.listOfficeLocationsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/listCommunityOrganizationsForWeb</b>
	 * <p>
	 * 8.获取园区企业列表(园区访客，此接口否使用接口待定)-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listCommunityOrganizationsForWeb")
	@RestReturn(ListCommunityOrganizationsResponse.class)
	public RestResponse listCommunityOrganizationsForWeb(ListCommunityOrganizationsCommand cmd) {
		baseResponse baseResponse = visitorSysService.listCommunityOrganizationsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/ListCommunityOrganizationsResponse/getInvitationLetterForWeb</b>
	 * <p>
	 * 9.获取访客邀请函(不登录)-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getInvitationLetterForWeb")
	@RestReturn(GetInvitationLetterForWebResponse.class)
	@RequireAuthentication(false)
	public RestResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd) {
		baseResponse baseResponse = visitorSysService.getInvitationLetterForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
