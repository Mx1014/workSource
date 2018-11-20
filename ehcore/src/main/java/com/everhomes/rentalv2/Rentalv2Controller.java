package com.everhomes.rentalv2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.archives.ArchivesContactDTO;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
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
    @Autowired
    private Rentalv2PayService  rentalv2PayService;

	/**
	 * <b>URL: /rental/findRentalSites</b>
	 * <p>
	 * 查询某园区某图标下的资源列表
	 * </p>
	 */
	@RequestMapping("findRentalSites")
	@RestReturn(value = FindRentalSitesCommandResponse.class)
	public RestResponse findRentalSites(@Valid FindRentalSitesCommand cmd) {
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
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
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		RestResponse response = new RestResponse(rentalService.findRentalSiteById(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/getSceneType</b>
	 * <p>
	 * 获取用户当前的用户类型
	 * </p>
	 */
	@RequestMapping("getSceneType")
	@RestReturn(value = GetSceneTypeResponse.class)
	@RequireAuthentication()
	public RestResponse getSceneType(@Valid GetSceneTypeCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getSceneType(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/registerUser</b>
	 * <p>
	 * 注册用户到公司
	 * </p>
	 */
	@RequestMapping("registerUser")
	@RestReturn(value = ArchivesContactDTO.class)
	@RequireAuthentication()
	public RestResponse getSceneType(@Valid RegisterUserCommand cmd) {
		RestResponse response = new RestResponse(rentalService.registerUser(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
/*-------------------------------------- start ---------------------------------------------------*/
	/**
	 * <b>URL: /rental/findRentalSiteWeekStatus</b>
	 * <p>
	 * 查询某服务预约某周的状态
	 * </p>
	 */

	@RequestMapping("findRentalSiteWeekStatus")
	@RestReturn(value = FindRentalSiteWeekStatusCommandResponse.class)
	public RestResponse findRentalSiteWeekStatus(@Valid FindRentalSiteWeekStatusCommand cmd) {
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
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
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		FindRentalSiteMonthStatusCommandResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findRentalSiteMonthStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findRentalSiteMonthStatusByWeek</b>
	 * <p>
	 * 查询某服务预约某月的状态(粒度为自然周)
	 * </p>
	 */

	@RequestMapping("findRentalSiteMonthStatusByWeek")
	@RestReturn(value = FindRentalSiteMonthStatusByWeekCommandResponse.class)
	public RestResponse findRentalSiteMonthStatusByWeek(@Valid FindRentalSiteMonthStatusByWeekCommand cmd) {
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		FindRentalSiteMonthStatusByWeekCommandResponse findRentalSiteMonthStatusByWeekCommandResponse = rentalService
				.findRentalSiteMonthStatusByWeek(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteMonthStatusByWeekCommandResponse);
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
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
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
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		FindAutoAssignRentalSiteMonthStatusResponse resp = rentalService
				.findAutoAssignRentalSiteMonthStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/findAutoAssignRentalSiteMonthStatusByWeek</b>
	 * <p>
	 * 查询带场所编号的资源一月的单元格(以周为粒度)
	 * </p>
	 */

	@RequestMapping("findAutoAssignRentalSiteMonthStatusByWeek")
	@RestReturn(value = FindAutoAssignRentalSiteMonthStatusByWeekResponse.class)
	public RestResponse findAutoAssignRentalSiteMonthStatusByWeek(@Valid FindAutoAssignRentalSiteMonthStatusByWeekCommand cmd) {
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		FindAutoAssignRentalSiteMonthStatusByWeekResponse resp = rentalService
				.findAutoAssignRentalSiteMonthStatusByWeek(cmd);
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
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
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
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		FindAutoAssignRentalSiteDayStatusResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findAutoAssignRentalSiteDayStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/*-------------------------------------- end---------------------------------------------------*/

	/**
	 * <b>URL: /rental/addRentalBill</b>
	 * <p>
	 * 添加订单
	 * </p>
	 */
	@RequestMapping("addRentalBill")
	@RestReturn(value = RentalBillDTO.class)
	public RestResponse addRentalBill(@Valid AddRentalBillCommand cmd) {
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
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
	 * <b>URL: /rental/addRentalItemBillV2</b>
	 * <p>
	 * 添加场所商品订单(支付2.0)
	 * </p>
	 */
	@RequestMapping("addRentalItemBillV2")
	@RestReturn(value = AddRentalBillItemV2Response.class)
	public RestResponse addRentalItemBillV2(@Valid AddRentalBillItemCommand cmd) {
		AddRentalBillItemV2Response result = rentalService.addRentalItemBillV2(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/addRentalItemBillV3</b>
	 * <p>
	 * 添加场所商品订单(跳转同一订单支付)
	 * </p>
	 */
	@RequestMapping("addRentalItemBillV3")
	@RestReturn(value = AddRentalBillItemV3Response.class)
	public RestResponse addRentalItemBillV3(@Valid AddRentalBillItemCommand cmd) {
		AddRentalBillItemV3Response result = rentalService.addRentalItemBillV3(cmd);
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
	 * <b>URL: /rental/getRentalBillPayInfoV2</b>
	 * <p>
	 * 获取订单支付信息
	 * </p>
	 */
	@RequestMapping("getRentalBillPayInfoV2")
	@RestReturn(value = PreOrderDTO.class)
	public RestResponse getRentalBillPayInfoV2(GetRentalBillPayInfoCommand cmd) {
		PreOrderDTO result = rentalService.getRentalBillPayInfoV2(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/getRentalBillPayInfoV3</b>
	 * <p>
	 * 获取订单支付信息(统一订单使用)
	 * </p>
	 */
	@RequestMapping("getRentalBillPayInfoV3")
	@RestReturn(value = AddRentalBillItemV3Response.class)
	public RestResponse getRentalBillPayInfoV3(GetRentalBillPayInfoCommand cmd) {
		AddRentalBillItemV3Response result = rentalService.getRentalBillPayInfoV3(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/offlinePayOrder</b>
	 * <p>
	 * 线下支付确认
	 * </p>
	 */
	@RequestMapping("offlinePayOrder")
	@RestReturn(value = String.class)
	public RestResponse offlinePayOrder(OfflinePayOrderCommand cmd) {
		rentalService.offlinePayOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/changeRentalBillPayInfo</b>
	 * <p>
	 * 修改订单信息
	 * </p>
	 */
	@RequestMapping("changeRentalBillPayInfo")
	@RestReturn(value = String.class)
	public RestResponse changeRentalBillPayInfo(ChangeRentalBillPayInfoCommand cmd) {
		rentalService.changeRentalBillPayInfo(cmd);
		RestResponse response = new RestResponse();
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
	 * <b>URL: /rental/getUserClosestBill</b>
	 * <p>
	 * 查找用户最近的使用中 待使用订单
	 * </p>
	 */
	@RequestMapping("getUserClosestBill")
	@RestReturn(value = GetUserClosestBillResponse.class)
	public RestResponse getUserClosestBill(@Valid GetUserClosestBillCommand cmd) {
		GetUserClosestBillResponse getUserClosestBillResponse = rentalService.getUserClosestBill(cmd);
		RestResponse response = new RestResponse(
				getUserClosestBillResponse);
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
	public void exportRentalBills(@Valid ListRentalBillsCommand cmd,HttpServletResponse response) {
		rentalService.exportRentalBills(cmd, response );
	}

	/**
	 *
	 * <b>URL: /rental/getResourceRuleById</b>
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

	@RequestMapping("testSch")
	public String testSch(HttpServletRequest request,HttpServletResponse response){
		rentalService.rentalSchedule();
		return null;
	}

	/*--------------------------------start ---------------------------*/

	/**
	 * <b>URL: /rental/addRentalOrderUsingInfo</b>
	 * <p>
	 * 添加订单使用信息
	 * </p>
	 */
	@RequestMapping("addRentalOrderUsingInfo")
	@RestReturn(value = AddRentalOrderUsingInfoResponse.class)
	public RestResponse addRentalOrderUsingInfo(AddRentalOrderUsingInfoCommand cmd) {

		RestResponse response = new RestResponse(rentalService.addRentalOrderUsingInfo(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/addRentalOrderUsingInfoV2</b>
	 * <p>
	 * 添加订单使用信息
	 * </p>
	 */
	@RequestMapping("addRentalOrderUsingInfoV2")
	@RestReturn(value = AddRentalOrderUsingInfoV2Response.class)
	public RestResponse addRentalOrderUsingInfoV2(AddRentalOrderUsingInfoCommand cmd) {

		RestResponse response = new RestResponse(rentalService.addRentalOrderUsingInfoV2(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/listRentalOrders</b>
	 * <p>
	 * 查询订单
	 * </p>
	 */
	@RequestMapping("listRentalOrders")
	@RestReturn(value = ListRentalOrdersResponse.class)
	public RestResponse listRentalOrders(ListRentalOrdersCommand cmd) {

		RestResponse response = new RestResponse(rentalService.listRentalOrders(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/getRentalOrderDetail</b>
	 * <p>
	 * 查询单个订单
	 * </p>
	 */
	@RequestMapping("getRentalOrderDetail")
	@RestReturn(value = RentalOrderDTO.class)
	@RequireAuthentication(false)
	public RestResponse getRentalOrderDetail(GetRentalOrderDetailCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getRentalOrderDetail(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/getRenewRentalOrderInfo</b>
	 * <p>校验并获取续费信息</p>
	 */
	@RequestMapping("getRenewRentalOrderInfo")
	@RestReturn(value = GetRenewRentalOrderInfoResponse.class)
	public RestResponse getRenewRentalOrderInfo(GetRenewRentalOrderInfoCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getRenewRentalOrderInfo(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/renewRentalOrder</b>
	 * <p>订单续费</p>
	 */
	@RequestMapping("renewRentalOrder")
	@RestReturn(value = CommonOrderDTO.class)
	public RestResponse renewRentalOrder(RenewRentalOrderCommand cmd) {
		RestResponse response = new RestResponse(rentalService.renewRentalOrder(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/renewRentalOrderV2</b>
	 * <p>订单续费</p>
	 */
	@RequestMapping("renewRentalOrderV2")
	@RestReturn(value = PreOrderDTO.class)
	public RestResponse renewRentalOrderV2(RenewRentalOrderCommand cmd) {
		RestResponse response = new RestResponse(rentalService.renewRentalOrderV2(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/completeRentalOrder</b>
	 * <p>完成订单</p>
	 */
	@RequestMapping("completeRentalOrder")
	@RestReturn(value = RentalOrderDTO.class)
	public RestResponse completeRentalOrder(CompleteRentalOrderCommand cmd) {
		RestResponse response = new RestResponse(rentalService.completeRentalOrder(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 *
	 * <b>URL: /rental/getResourceRuleV2<b>
	 * <p>查询资源的规则</p>
	 */
	@RequestMapping("getResourceRuleV2")
	@RestReturn(GetResourceRuleV2Response.class)
	@RequireAuthentication(false)
	public RestResponse getResourceRuleV2(GetResourceRuleV2Command cmd) {
		if (cmd.getSceneType() == null || cmd.getSceneType().length() == 0)
			cmd.setSceneType(rentalService.parseSceneToken(cmd.getSceneToken()));
		RestResponse response = new RestResponse(rentalService.getResourceRuleV2(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/getCancelOrderTip<b>
	 * <p>获取订单取消文本</p>
	 */
	@RequestMapping("getCancelOrderTip")
	@RestReturn(GetCancelOrderTipResponse.class)
	public RestResponse getCancelOrderTip(GetCancelOrderTipCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getCancelOrderTip(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/getResourceUsingInfo<b>
	 * <p>获取资源当前使用状态</p>
	 */
	@RequireAuthentication(value = false)
	@RequestMapping("getResourceUsingInfo")
	@RestReturn(GetResourceUsingInfoResponse.class)
	public RestResponse getResourceUsingInfo(@Valid FindRentalSiteByIdCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getResourceUsingInfo(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/payNotify <b>
	 * <p>支付回调</p>
	 */
	@RequireAuthentication(false)
	@RequestMapping("payNotify")
	@RestReturn(String.class)
	public RestResponse payNotify(MerchantPaymentNotificationCommand cmd) {
	    this.rentalv2PayService.payNotify(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     *
     * <b>URL: /rental/refundNotify <b>
     * <p>退款回调</p>
     */
	@RequireAuthentication(false)
    @RequestMapping("refundNotify")
    @RestReturn(String.class)
    public RestResponse refundNotify(OrderPaymentNotificationCommand  cmd) {
        this.rentalv2PayService.refundNotify(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 *
	 * <b>URL: /rental/test<b>
	 * <p>test</p>
	 */
	@RequestMapping("test")
	public void test() {
		rentalService.test();

	}
}
