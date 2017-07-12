package com.everhomes.rentalv2;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.oauth2.RequireOAuth2Authentication;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.GetResourceRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.punch.PunchController;

/**
 * <ul>
 * 预定系统：
 * <li>后台维护 某种场所 的预定规则 维护 具体场所</li>
 * <li>客户端可以查询某日，某具体场所 的开放时间，预定状态</li>
 * <li>客户端下预定订单，付费</li>
 * </ul>
 */
@RestDoc(value = "rental controller", site = "ehcore")
@RestController
@RequestMapping("/rental")
public class Rentalv2Controller extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchController.class);
	public static final Long moduleId = 40400L;
	@Autowired
	private Rentalv2Service rentalService;

	/**
	 * <b>URL: /rental/findRentalSites</b>
	 * <p>
	 * 查询某园区某图标下的资源列表
	 * </p>
	 */
	@RequestMapping("findRentalSites")
	@RestReturn(value = FindRentalSitesCommandResponse.class)
	public RestResponse findRentalSites(@Valid FindRentalSitesCommand cmd) { 
		FindRentalSitesCommandResponse findRentalSitesCommandResponse = rentalService
				.findRentalSites(cmd);
		RestResponse response = new RestResponse(
				findRentalSitesCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findRentalSiteById</b>
	 * <p>
	 * 根据id查询某园区某图标下的资源
	 * </p>
	 */
	@RequestMapping("findRentalSiteById")
	@RestReturn(value = RentalSiteDTO.class)
	@RequireAuthentication()
	public RestResponse findRentalSiteById(@Valid FindRentalSiteByIdCommand cmd) {

		RestResponse response = new RestResponse(rentalService.findRentalSiteById(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findRentalSiteWeekStatus</b>
	 * <p>
	 * 查询某服务预约某周的状态
	 * </p>
	 */

	@RequestMapping("findRentalSiteWeekStatus")
	@RestReturn(value = FindRentalSiteWeekStatusCommandResponse.class)
	public RestResponse findRentalSiteWeekStatus(@Valid FindRentalSiteWeekStatusCommand cmd) {
		FindRentalSiteWeekStatusCommandResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findRentalSiteWeekStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /rental/findRentalSiteMonthStatus</b>
	 * <p>
	 * 查询某服务预约某月的状态
	 * </p>
	 */

	@RequestMapping("findRentalSiteMonthStatus")
	@RestReturn(value = FindRentalSiteMonthStatusCommandResponse.class)	
	public RestResponse findRentalSiteMonthStatus(@Valid FindRentalSiteMonthStatusCommand cmd) {
		FindRentalSiteMonthStatusCommandResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findRentalSiteMonthStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /rental/findRentalSiteYearStatus</b>
	 * <p>
	 * 查询某服务预约某年的状态
	 * </p>
	 */
	
	@RequestMapping("findRentalSiteYearStatus")
	@RestReturn(value = FindRentalSiteYearStatusCommandResponse.class)	
	public RestResponse findRentalSiteYearStatus(@Valid FindRentalSiteYearStatusCommand cmd) {
		FindRentalSiteYearStatusCommandResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findRentalSiteYearStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findAutoAssignRentalSiteMonthStatus</b>
	 * <p>
	 * 查询带场所编号的资源一月的单元格
	 * </p>
	 */

	@RequestMapping("findAutoAssignRentalSiteMonthStatus")
	@RestReturn(value = FindAutoAssignRentalSiteMonthStatusResponse.class)
	public RestResponse findAutoAssignRentalSiteMonthStatus(@Valid FindAutoAssignRentalSiteMonthStatusCommand cmd) {
		FindAutoAssignRentalSiteMonthStatusResponse resp = rentalService
				.findAutoAssignRentalSiteMonthStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/findAutoAssignRentalSiteYearStatus</b>
	 * <p>
	 * 查询带场所编号的资源一年的单元格
	 * </p>
	 */
	
	@RequestMapping("findAutoAssignRentalSiteYearStatus")
	@RestReturn(value = FindAutoAssignRentalSiteYearStatusResponse.class)
	public RestResponse findAutoAssignRentalSiteYearStatus(@Valid FindAutoAssignRentalSiteYearStatusCommand cmd) {
		FindAutoAssignRentalSiteYearStatusResponse resp = rentalService
				.findAutoAssignRentalSiteYearStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findAutoAssignRentalSiteWeekStatus</b>
	 * <p>
	 * 查询带场所编号的资源一周的单元格 (已经没有使用，可能老版本客户端会使用)
	 * </p>
	 */
	@Deprecated
	@RequestMapping("findAutoAssignRentalSiteWeekStatus")
	@RestReturn(value = FindAutoAssignRentalSiteWeekStatusResponse.class)
	public RestResponse findAutoAssignRentalSiteWeekStatus(@Valid FindAutoAssignRentalSiteWeekStatusCommand cmd) {
		FindAutoAssignRentalSiteWeekStatusResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findAutoAssignRentalSiteWeekStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /rental/findAutoAssignRentalSiteDayStatus</b>
	 * <p>
	 * 查询带场所编号的资源一天的单元格
	 * </p>
	 */

	@RequestMapping("findAutoAssignRentalSiteDayStatus")
	@RestReturn(value = FindAutoAssignRentalSiteDayStatusResponse.class)
	public RestResponse findAutoAssignRentalSiteDayStatus(@Valid FindAutoAssignRentalSiteDayStatusCommand cmd) {
		FindAutoAssignRentalSiteDayStatusResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findAutoAssignRentalSiteDayStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/addRentalBill</b>
	 * <p>
	 * 添加订单
	 * </p>
	 */
	@RequestMapping("addRentalBill")
	@RestReturn(value = RentalBillDTO.class)
	public RestResponse addRentalBill(@Valid AddRentalBillCommand cmd) {
		RentalBillDTO res = rentalService.addRentalBill(cmd); 
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findRentalSiteItemsAndAttachments</b>
	 * <p>
	 * 查询某场所的可预订物品和附件
	 * </p>
	 */
	@RequestMapping("findRentalSiteItemsAndAttachments")
	@RestReturn(value = FindRentalSiteItemsAndAttachmentsResponse.class)
	public RestResponse findRentalSiteItems(@Valid FindRentalSiteItemsAndAttachmentsCommand cmd) { 
		FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItemsCommandResponse = rentalService.findRentalSiteItems(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteItemsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	 
	
	
	/**
	 * <b>URL: /rental/addRentalItemBill</b>
	 * <p>
	 * 添加场所商品订单
	 * </p>
	 */
	@RequestMapping("addRentalItemBill")
	@RestReturn(value = AddRentalBillItemCommandResponse.class)
	public RestResponse addRentalItemBill(@Valid AddRentalBillItemCommand cmd) {
		AddRentalBillItemCommandResponse result = rentalService.addRentalItemBill(cmd); 
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/getRentalBillPayInfo</b>
	 * <p>
	 * 获取订单支付信息
	 * </p>
	 */
	@RequestMapping("getRentalBillPayInfo")
	@RestReturn(value = CommonOrderDTO.class)
	public RestResponse getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd) {
		CommonOrderDTO result = rentalService.getRentalBillPayInfo(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/cancelRentalBill</b>
	 * <p>
	 * 取消订单
	 * </p>
	 */
	@RequestMapping("cancelRentalBill")
	@RestReturn(value = String.class)
	public RestResponse CancelRentalBill(@Valid CancelRentalBillCommand cmd) {
		rentalService.cancelRentalBill(cmd); 
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/deleteRentalBill</b>
	 * <p>
	 * 删除订单-前端不展示
	 * </p>
	 */
	@RequestMapping("deleteRentalBill")
	@RestReturn(value = String.class)
	public RestResponse deleteRentalBill(@Valid DeleteRentalBillCommand cmd) {
		rentalService.deleteRentalBill(cmd); 
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/onlinePayCallback</b>
	 * <p>
	 * 付款成功的回调函数
	 * </p>
	 */
	@RequestMapping("onlinePayCallback")
	@RestReturn(value =String.class)
	public RestResponse onlinePayCallback(@Valid OnlinePayCallbackCommand cmd)  {
		rentalService.onlinePayCallback(cmd); 
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/findUserRentalBills</b>
	 * <p>
	 * 查询用户的预约记录
	 * </p>
	 */
	@RequestMapping("findUserRentalBills")
	@RestReturn(value = FindRentalBillsCommandResponse.class)
	public RestResponse findUserRentalBills(@Valid FindRentalBillsCommand cmd) {
		FindRentalBillsCommandResponse findRentalBillsCommandResponse = rentalService
				.findRentalBills(cmd);
		RestResponse response = new RestResponse(
				findRentalBillsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /rental/listRentalBills</b>
	 * <p>
	 * 查询订单
	 * </p>
	 */
	@RequestMapping("listRentalBills")
	@RestReturn(value = ListRentalBillsCommandResponse.class)
	public RestResponse listRentalBills(@Valid ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse lsitRentalBillsCommandResponse = rentalService
				.listRentalBills(cmd);
		RestResponse response = new RestResponse(lsitRentalBillsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/exportRentalBills</b>
	 * <p>
	 * 导出预订详情
	 * </p>
	 */
	@RequestMapping("exportRentalBills")
	public String exportRentalBills(@Valid ListRentalBillsCommand cmd,HttpServletResponse response) {
		HttpServletResponse commandResponse = rentalService.exportRentalBills(cmd, response );
		return null;
	}

	/**
	 *
	 * <b>URL: /rental/getResourceRuleById<b>
	 * <p>
	 * 查询资源的规则
	 * </p>
	 */
	@RequestMapping("getResourceRuleById")
	@RestReturn(QueryDefaultRuleAdminResponse.class)
	public RestResponse getResourceRule(@Valid GetResourceRuleAdminCommand cmd) {
		QueryDefaultRuleAdminResponse queryDefaultRuleAdminResponse = this.rentalService.getResourceRule(cmd);
		RestResponse response = new RestResponse(queryDefaultRuleAdminResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
