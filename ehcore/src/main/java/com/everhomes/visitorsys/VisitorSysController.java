// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

@RestDoc(value = "visitorsys Controller", site = "core")
@RestController
@RequestMapping("/visitorsys")
public class VisitorSysController extends ControllerBase {

	@Autowired
	private VisitorSysService visitorSysService;
	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public void initListBinder(WebDataBinder binder) {
		super.initListBinder(binder);
		binder.registerCustomEditor(Timestamp.class,new TimestampEditor());
	}
	/*------------------------------web后端，企业访客管理和园区访客管理-----------------------------*/
	/**
	 * <b>URL: /visitorsys/listBookedVisitors</b>
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
	 * <b>URL: /visitorsys/getBookedVisitorById</b>
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
	 * <b>URL: /visitorsys/listOfficeLocations</b>
	 * <p>
	 * 3.获取办公地点列表-后台管理
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
	 * <b>URL: /visitorsys/listCommunityOrganizations</b>
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
	 * <b>URL: /visitorsys/listVisitReasons</b>
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
	 * <b>URL: /visitorsys/createOrUpdateVisitor</b>
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
	 * <b>URL: /visitorsys/sendVisitorSMS</b>
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
	 * <b>URL: /visitorsys/deleteVisitor</b>
	 * <p>
	 * 8.删除访客-后台管理
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
	 * <b>URL: /visitorsys/deleteVisitorAppoint</b>
	 * <p>
	 * 9.删除预约-后台管理
	 * </p>
	 */
	@RequestMapping("deleteVisitorAppoint")
	@RestReturn(String.class)
	public RestResponse deleteVisitorAppoint(GetBookedVisitorByIdCommand cmd) {
		visitorSysService.deleteVisitorAppoint(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/confirmVisitor</b>
	 * <p>
	 * 10.访客到访确认接口-后台管理
	 * </p>
	 */
	@RequestMapping("confirmVisitor")
	@RestReturn(String.class)
	public RestResponse confirmVisitor(CreateOrUpdateVisitorCommand cmd) {
		visitorSysService.confirmVisitor(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getStatistics</b>
	 * <p>
	 * 11.统计接口-后台管理
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
	 * <b>URL: /visitorsys/addDevice</b>
	 * <p>
	 * 12.添加设备(ipad/打印机)接口-后台管理
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
	 * <b>URL: /visitorsys/listDevices</b>
	 * <p>
	 * 13.获取设备(ipad/打印机)接口列表-后台管理
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
	 * <b>URL: /visitorsys/deleteDevice</b>
	 * <p>
	 * 14.删除设备(ipad/打印机)-后台管理
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
	 * <b>URL: /visitorsys/createOrUpdateOfficeLocation</b>
	 * <p>
	 * 15.添加/更新企业办公地点-后台管理
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
	 * <b>URL: /visitorsys/deleteOfficeLocation</b>
	 * <p>
	 * 16.删除企业办公地点-后台管理
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
	 * <b>URL: /visitorsys/getConfiguration</b>
	 * <p>
	 * 17.获取配置-后台管理
	 * </p>
	 */
	@RequestMapping("getConfiguration")
	@RestReturn(GetConfigurationResponse.class)
	public RestResponse getConfiguration(GetConfigurationCommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.getConfiguration(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/updateConfiguration</b>
	 * <p>
	 * 18.更新配置-后台管理
	 * </p>
	 */
	@RequestMapping("updateConfiguration")
	@RestReturn(GetConfigurationResponse.class)
	public RestResponse updateConfiguration(UpdateConfigurationCommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.updateConfiguration(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/listBlackLists</b>
	 * <p>
	 * 19.获取黑名单列表-后台管理
	 * </p>
	 */
	@RequestMapping("listBlackLists")
	@RestReturn(ListBlackListsResponse.class)
	public RestResponse listBlackLists(ListBlackListsCommand cmd) {
		ListBlackListsResponse baseResponse = visitorSysService.listBlackLists(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/deleteBlackList</b>
	 * <p>
	 * 20.删除黑名单-后台管理
	 * </p>
	 */
	@RequestMapping("deleteBlackList")
	@RestReturn(String.class)
	public RestResponse deleteBlackList(DeleteBlackListCommand cmd) {
		visitorSysService.deleteBlackList(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/createOrUpdateBlackList</b>
	 * <p>
	 * 21.添加/更新黑名单-后台管理
	 * </p>
	 */
	@RequestMapping("createOrUpdateBlackList")
	@RestReturn(CreateOrUpdateBlackListResponse.class)
	public RestResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd) {
		CreateOrUpdateBlackListResponse baseResponse = visitorSysService.createOrUpdateBlackList(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /visitorsys/listDoorGuards</b>
	 * <p>
	 * 22.门禁列表-后台管理(从门禁系统获取)
	 * </p>
	 */
	@RequestMapping("listDoorGuards")
	@RestReturn(ListDoorGuardsResponse.class)
	public RestResponse listDoorGuards(ListDoorGuardsCommand cmd) {
		ListDoorGuardsResponse baseResponse = visitorSysService.listDoorGuards(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/listDoorAccess</b>
	 * <p>
	 * 22.门禁授权规则列表-后台管理
	 * </p>
	 */
	@RequestMapping("listDoorAccess")
	@RestReturn(value = VisitorSysDoorAccessDTO.class,collection = true)
	public RestResponse listDoorAccess(BaseVisitorsysCommand cmd) {
		RestResponse response = new RestResponse(visitorSysService.listDoorAccess(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/createDoorAccess</b>
	 * <p>
	 * 22.创建门禁授权规则-后台管理
	 * </p>
	 */
	@RequestMapping("createDoorAccess")
	@RestReturn(String.class)
	public RestResponse createDoorAccess(CreateOrUpdateDoorAccessCommand cmd) {
		visitorSysService.createDoorAccess(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/deleteDoorAccess</b>
	 * <p>
	 * 22.删除门禁授权规则-后台管理
	 * </p>
	 */
	@RequestMapping("deleteDoorAccess")
	@RestReturn(String.class)
	public RestResponse deleteDoorAccess(DeleteDoorAccessCommand cmd) {
		visitorSysService.deleteDoorAccess(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/setDefaultAccess</b>
	 * <p>
	 * 22.设置默认门禁授权规则-后台管理
	 * </p>
	 */
	@RequestMapping("setDefaultAccess")
	@RestReturn(String.class)
	public RestResponse setDefaultAccess(CreateOrUpdateDoorAccessCommand cmd) {
		visitorSysService.setDefaultAccess(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/exportBookedVisitors</b>
	 * <p>
	 * 23.导出预约访客/访客管理列表-后台管理
	 * </p>
	 */
	@RequestMapping("exportBookedVisitors")
	@RestReturn(String.class)
	public RestResponse exportBookedVisitors(ListBookedVisitorsCommand cmd,HttpServletResponse resp){
		visitorSysService.exportBookedVisitors(cmd,resp);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/rejectVisitor</b>
	 * <p>
	 * 24.拒绝访客-后台管理
	 * </p>
	 */
	@RequestMapping("rejectVisitor")
	@RestReturn(String.class)
	public RestResponse rejectVisitor(CreateOrUpdateVisitorCommand cmd) {
		visitorSysService.rejectVisitor(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/syncVisitor</b>
	 * <p>
	 * 25.同步访客/预约-后台管理
	 * </p>
	 */
	@RequestMapping("syncVisitor")
	@RestReturn(String.class)
	public RestResponse syncVisitor(BaseVisitorsysCommand cmd) {
		visitorSysService.syncVisitor(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getForm</b>
	 * <p>
	 * 26.根据Owner获取所在园区的表单，或者根据Owner和enterpriseId获取对应公司表单配置-后台管理
	 * </p>
	 */
	@RequestMapping("getForm")
	@RestReturn(GetFormResponse.class)
	public RestResponse getForm(GetFormCommand cmd) {
		GetFormResponse baseResponse = visitorSysService.getForm(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/transferQrcode</b>
	 * <p>
	 * 27.根据提供的qrcode，返回二维码流-后台管理
	 * </p>
	 */
	@RequestMapping("transferQrcode")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse transferQrcode(TransferQrcodeCommand cmd, HttpServletResponse resp) {
		visitorSysService.transferQrcode(cmd,resp);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/*------------------------------web版本的访客录入和访客邀请函-----------------------------*/
	/**
	 * <b>URL: /visitorsys/getConfigurationForWeb</b>
	 * <p>
	 * 1.获取配置-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getConfigurationForWeb")
	@RestReturn(GetConfigurationResponse.class)
	@RequireAuthentication(false)
	public RestResponse getConfigurationForWeb(GetConfigurationForWebCommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.getConfigurationForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getFormForWeb</b>
	 * <p>
	 * 2..根据Owner获取所在园区的表单，或者根据Owner和enterpriseId获取对应公司表单配置-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getFormForWeb")
	@RestReturn(GetFormForWebResponse.class)
	@RequireAuthentication(false)
	public RestResponse getFormForWeb(GetFormForWebCommand cmd) {
		GetFormForWebResponse baseResponse = visitorSysService.getFormForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/listBookedVisitorsForWeb</b>
	 * <p>
	 * 3.获取预约访客列表-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listBookedVisitorsForWeb")
	@RestReturn(ListBookedVisitorsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listBookedVisitorsForWeb(ListBookedVisitorsCommand cmd) {
		ListBookedVisitorsResponse baseResponse = visitorSysService.listBookedVisitorsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getBookedVisitorByIdForWeb</b>
	 * <p>
	 * 4.获取预约详情-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getBookedVisitorByIdForWeb")
	@RestReturn(GetBookedVisitorByIdResponse.class)
	@RequireAuthentication(false)
	public RestResponse getBookedVisitorByIdForWeb(GetBookedVisitorByIdCommand cmd) {
		GetBookedVisitorByIdResponse baseResponse = visitorSysService.getBookedVisitorByIdForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/listVisitReasonsForWeb</b>
	 * <p>
	 * 5.获取事由列表（此接口否使用接口待定，可以使用枚举）-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listVisitReasonsForWeb")
	@RestReturn(ListVisitReasonsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listVisitReasonsForWeb(BaseVisitorsysCommand cmd) {
		ListVisitReasonsResponse baseResponse = visitorSysService.listVisitReasonsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/createOrUpdateVisitorForWeb</b>
	 * <p>
	 * 6.创建临时/预约访客-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("createOrUpdateVisitorForWeb")
	@RestReturn(GetBookedVisitorByIdResponse.class)
	@RequireAuthentication(false)
	public RestResponse createOrUpdateVisitorForWeb(CreateOrUpdateVisitorCommand cmd) {
		GetBookedVisitorByIdResponse baseResponse = visitorSysService.createOrUpdateVisitorForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/deleteVisitorAppointForWeb</b>
	 * <p>
	 * 7.删除访客预约-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("deleteVisitorAppointForWeb")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse deleteVisitorAppointForWeb(GetBookedVisitorByIdCommand cmd) {
		visitorSysService.deleteVisitorAppointForWeb(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listOfficeLocationsForWeb</b>
	 * <p>
	 * 8.获取企业办公地点列表(企业访客)-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listOfficeLocationsForWeb")
	@RestReturn(ListOfficeLocationsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listOfficeLocationsForWeb(ListOfficeLocationsCommand cmd) {
		ListOfficeLocationsResponse baseResponse = visitorSysService.listOfficeLocationsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/listCommunityOrganizationsForWeb</b>
	 * <p>
	 * 9.获取园区企业列表-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("listCommunityOrganizationsForWeb")
	@RestReturn(ListCommunityOrganizationsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listCommunityOrganizationsForWeb(ListCommunityOrganizationsCommand cmd) {
		ListCommunityOrganizationsResponse baseResponse = visitorSysService.listCommunityOrganizationsForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /ui/visitorsys/getInvitationLetterForWeb</b>
	 * <p>
	 * 10.获取访客邀请函(不登录)-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getInvitationLetterForWeb")
	@RestReturn(GetInvitationLetterForWebResponse.class)
	@RequireAuthentication(false)
	public RestResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd) {
		GetInvitationLetterForWebResponse baseResponse = visitorSysService.getInvitationLetterForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/sendSMSVerificationCodeForWeb</b>
	 * <p>
	 * 11.发送短信验证码-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("sendSMSVerificationCodeForWeb")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse sendSMSVerificationCodeForWeb(SendSMSVerificationCodeForWebCommand cmd) {
		visitorSysService.sendSMSVerificationCodeForWeb(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /visitorsys/confirmVerificationCodeForWeb</b>
	 * <p>
	 * 12.验证短信验证码(并返回预约列表)-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("confirmVerificationCodeForWeb")
	@RestReturn(ListBookedVisitorsResponse.class)
	@RequireAuthentication(false)
	public RestResponse confirmVerificationCodeForWeb(ConfirmVerificationCodeForWebCommand cmd) {
		ListBookedVisitorsResponse baseResponse = visitorSysService.confirmVerificationCodeForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /visitorsys/getUploadFileTokenForWeb</b>
	 * <p>
	 * 13.获取上传文件token和contentserver-h5（客户端/微信端）
	 * </p>
	 */
	@RequestMapping("getUploadFileTokenForWeb")
	@RestReturn(GetUploadFileTokenResponse.class)
	@RequireAuthentication(false)
	public RestResponse getUploadFileTokenForWeb(GetUploadFileTokenCommand cmd) {
		GetUploadFileTokenResponse baseResponse = visitorSysService.getUploadFileTokenForWeb(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL:  /visitorsys/checkBlackListForWeb</b>
	 * <p>
	 * 14.检查手机号码是否在黑名单,返回码 200是正常，1407 1408是手机号在黑名单
	 * </p>
	 */
	@RequestMapping("checkBlackListForWeb")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse checkBlackListForWeb(CheckBlackListForWebCommand cmd) {
		visitorSysService.checkBlackListForWeb(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/*------------------------------web版本的客户端，企业访客管理和园区访客管理-----------------------------*/
	/**
	 * <b>URL: /visitorsys/listBookedVisitorsForManage</b>
	 * <p>
	 * 1.获取预约访客列表-h5 100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("listBookedVisitorsForManage")
	@RestReturn(ListBookedVisitorsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listBookedVisitorsForManage(ListBookedVisitorsCommand cmd) {
		ListBookedVisitorsResponse baseResponse = visitorSysService.listBookedVisitorsForManage(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getBookedVisitorByIdForManage</b>
	 * <p>
	 * 2.获取预约详情-h5 100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("getBookedVisitorByIdForManage")
	@RestReturn(GetBookedVisitorByIdResponse.class)
	@RequireAuthentication(false)
	public RestResponse getBookedVisitorByIdForManage(GetBookedVisitorByIdCommand cmd) {
		GetBookedVisitorByIdResponse baseResponse = visitorSysService.getBookedVisitorByIdForManage(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/listVisitReasonsForManage</b>
	 * <p>
	 * 3.获取事由列表 100055 权限校验失败（此接口否使用接口待定，可以使用枚举）-h5（（客户端/微信端 web企业访客/园区访客管理用））
	 * </p>
	 */
	@RequestMapping("listVisitReasonsForManage")
	@RestReturn(ListVisitReasonsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listVisitReasonsForManage(BaseVisitorsysCommand cmd) {
		ListVisitReasonsResponse baseResponse = visitorSysService.listVisitReasonsForManage(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/createOrUpdateVisitorForManage</b>
	 * <p>
	 * 4.创建临时/预约访客-h5 100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("createOrUpdateVisitorForManage")
	@RestReturn(GetBookedVisitorByIdResponse.class)
	@RequireAuthentication(false)
	public RestResponse createOrUpdateVisitorForManage(CreateOrUpdateVisitorCommand cmd) {
		GetBookedVisitorByIdResponse baseResponse = visitorSysService.createOrUpdateVisitorForManage(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/confirmVisitorForManage</b>
	 * <p>
	 * 5.访客到访确认接口- 拒绝访客- 返回码：071701 状态已被更新；100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("confirmVisitorForManage")
	@RestReturn(String.class)
	public RestResponse confirmVisitorForManage(CreateOrUpdateVisitorCommand cmd) {
		visitorSysService.confirmVisitorForManage(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/rejectVisitorForManage</b>
	 * <p>
	 * 6.拒绝访客- 返回码：071701 状态已被更新；100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("rejectVisitorForManage")
	@RestReturn(String.class)
	public RestResponse rejectVisitorForManage(CreateOrUpdateVisitorCommand cmd) {
		visitorSysService.rejectVisitorForManage(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/addMessageReceiverForManage</b>
	 * <p>
	 * 7.园区访客管理决定接受（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("addMessageReceiverForManage")
	@RestReturn(String.class)
	public RestResponse updateMessageReceiverForManage(UpdateMessageReceiverCommand cmd) {
		visitorSysService.updateMessageReceiverForManage(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /visitorsys/getMessageReceiverForManage</b>
	 * <p>
	 * 7.园区访客管理决定接受（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("getMessageReceiverForManage")
	@RestReturn(GetMessageReceiverForManageResponse.class)
	public RestResponse getMessageReceiverForManage(BaseVisitorsysCommand cmd) {
		GetMessageReceiverForManageResponse baseResponse = visitorSysService.getMessageReceiverForManage(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getIDCardInfo</b>
	 * <p>
	 * 身份正读卡器接口
	 * </p>
	 */
	@RequestMapping("getIDCardInfo")
	@RestReturn(IdentifierCardDTO.class)
	public RestResponse getIDCardInfo() {

		RestResponse response = new RestResponse(visitorSysService.getIDCardInfo());
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/getConfigurationForManage</b>
	 * <p>
	 * 1.获取配置-h5 100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("getConfigurationForManage")
	@RestReturn(GetConfigurationResponse.class)
	@RequireAuthentication(false)
	public RestResponse getConfigurationForManage(GetConfigurationCommand cmd) {
		GetConfigurationResponse baseResponse = visitorSysService.getConfigurationForManage(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/listDoorAccessForManage</b>
	 * <p>
	 * 22.门禁授权规则列表-h5 100055 权限校验失败（客户端/微信端 web企业访客/园区访客管理用）
	 * </p>
	 */
	@RequestMapping("listDoorAccessForManage")
	@RestReturn(value = VisitorSysDoorAccessDTO.class,collection = true)
	@RequireAuthentication(false)
	public RestResponse listDoorAccessForManage(BaseVisitorsysCommand cmd) {
		RestResponse response = new RestResponse(visitorSysService.listDoorAccessForManage(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/syncHKWSUsers</b>
	 * <p>
	 * 同步海康威视用户
	 * </p>
	 */
	@RequestMapping("syncHKWSUsers")
	@RestReturn(value = String.class)
	@RequireAuthentication(false)
	public RestResponse syncHKWSUsers(BaseVisitorsysCommand cmd) {
		visitorSysService.syncHKWSUsers();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	@RequestMapping("HKWSTest")
	@RestReturn(value = String.class)
	@RequireAuthentication(false)
	public RestResponse HKWSTest(BaseVisitorsysCommand cmd) {
		visitorSysService.HKWSTest(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /visitorsys/listFreqVisitors</b>
	 * <p>
	 * 获取常用访客信息
	 * </p>
	 */
	@RequestMapping("listFreqVisitors")
	@RestReturn(ListFreqVisitorsResponse.class)
	public RestResponse listFreqVisitors(ListFreqVisitorsCommand cmd) {
		ListFreqVisitorsResponse baseResponse = visitorSysService.listFreqVisitors(cmd);

		RestResponse response = new RestResponse(baseResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /visitorsys/syncFreqVisitors</b>
	 * <p>
	 * 更新常用访客信息
	 * </p>
	 */
	@RequestMapping("syncFreqVisitors")
	@RestReturn(String.class)
	public RestResponse syncFreqVisitors(BaseVisitorsysCommand cmd) {
		visitorSysService.syncFreqVisitors(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	@RequestMapping("v")
	@RequireAuthentication(false)
	public Object doorVisitor(GetInvitationUrlCommand cmd) {
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			if(cmd.getVisitorToken() != null) {
				String invitationLinkTemp = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITATION_LINK,"/visitor-appointment/build/invitation.html?visitorToken=");
				httpHeaders.setLocation(new URI(invitationLinkTemp + cmd.getVisitorToken()));
			}
		} catch (URISyntaxException e) {
		}
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

}
