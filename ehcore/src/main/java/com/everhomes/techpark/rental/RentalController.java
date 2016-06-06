package com.everhomes.techpark.rental;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.everhomes.rest.techpark.rental.AddRentalBillCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommandResponse;
import com.everhomes.rest.techpark.rental.AddRentalSiteCommand;
import com.everhomes.rest.techpark.rental.CancelRentalBillCommand;
import com.everhomes.rest.techpark.rental.ConfirmBillCommand;
import com.everhomes.rest.techpark.rental.DeleteItemAdminCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalBillCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.DisableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.EnableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteRulesCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesStatusCommandResponse;
import com.everhomes.rest.techpark.rental.GetRentalSiteTypeResponse;
import com.everhomes.rest.techpark.rental.GetRentalTypeRuleCommand;
import com.everhomes.rest.techpark.rental.GetRentalTypeRuleCommandResponse;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommandResponse;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommand;
import com.everhomes.rest.techpark.rental.RentalBillDTO;
import com.everhomes.rest.techpark.rental.UpdateRentalRuleCommand;
import com.everhomes.rest.techpark.rental.UpdateRentalSiteCommand;
import com.everhomes.rest.techpark.rental.VerifyRentalBillCommandResponse;
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
@RequestMapping("/techpark/rental")
public class RentalController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchController.class);

	@Autowired
	private RentalService rentalService;
	/**
	 * <b>URL: /techpark/rental/getRentalSiteType</b>
	 * <p>
	 * 获取场所类型
	 * </p>
	 */
	@RequestMapping("getRentalSiteType")
	@RestReturn(value = GetRentalSiteTypeResponse.class)
	public RestResponse getRentalSiteType() {
		GetRentalSiteTypeResponse updateRentalRuleCommandResponse = rentalService
				.findRentalSiteTypes();
		RestResponse response = new RestResponse(
				updateRentalRuleCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /techpark/rental/getRentalTypeRule</b>
	 * <p>
	 * 查询某类场所的通用设置
	 * </p>
	 */
	@RequestMapping("getRentalTypeRule")
	@RestReturn(value = GetRentalTypeRuleCommandResponse.class)
	public RestResponse getRentalTypeRule(
			@Valid GetRentalTypeRuleCommand cmd) {
		GetRentalTypeRuleCommandResponse getRentalTypeRuleCommandResponse = rentalService
				.getRentalTypeRule(cmd);
		RestResponse response = new RestResponse(
				getRentalTypeRuleCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	/**
	 * <b>URL: /techpark/rental/updateRentalRule</b>
	 * <p>
	 * 设置通用设置
	 * </p>
	 */
	@RequestMapping("updateRentalRule")
	@RestReturn(value = String.class)
	public RestResponse updateRentalRule(@Valid UpdateRentalRuleCommand cmd) {
		rentalService.updateRentalRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/rental/addRentalSite</b>
	 * <p>
	 * 添加具体场所
	 * </p>
	 */
	@RequestMapping("addRentalSite")
	@RestReturn(value = String.class)
	public RestResponse addRentalSite(@Valid AddRentalSiteCommand cmd) {
		Long siteId = rentalService.addRentalSite(cmd);
		RestResponse response = new RestResponse(siteId);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /techpark/rental/updateRentalSite</b>
	 * <p>
	 * 更新具体场所
	 * </p>
	 */
	@RequestMapping("updateRentalSite")
	@RestReturn(value = String.class)
	public RestResponse updateRentalSite(@Valid UpdateRentalSiteCommand cmd) {
		rentalService.updateRentalSite(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /techpark/rental/deleteRentalSite</b>
	 * <p>
	 * 删除具体场所
	 * </p>
	 */
	@RequestMapping("deleteRentalSite")
	@RestReturn(value = String.class)
	public RestResponse deleteRentalSite(@Valid DeleteRentalSiteCommand cmd) {
		rentalService.deleteRentalSite(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	/**
	 * <b>URL: /techpark/rental/deleteRentalSite</b>
	 * <p>
	 * 删除具体场所
	 * </p>
	 */
	@RequestMapping("disableRentalSite")
	@RestReturn(value = String.class)
	public RestResponse disableRentalSite(@Valid DisableRentalSiteCommand cmd) {
		rentalService.disableRentalSite(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /techpark/rental/deleteRentalSite</b>
	 * <p>
	 * 删除具体场所
	 * </p>
	 */
	@RequestMapping("enableRentalSite")
	@RestReturn(value = String.class)
	public RestResponse enableRentalSite(@Valid EnableRentalSiteCommand cmd) {
		rentalService.enableRentalSite(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/rental/findRentalSites</b>
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
	
//	
//	/**
//	 * <b>URL: /techpark/rental/addRentalSiteItems</b>
//	 * <p>
//	 * 添加具体场所商品信息
//	 * </p>
//	 */
//	@RequestMapping("addRentalSiteItems")
//	@RestReturn(value = String.class)
//	public RestResponse addRentalSiteItems(@Valid AddRentalSiteItemsCommand cmd) {
//		rentalService.addRentalSiteItems(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /techpark/rental/listRentalSiteItems</b>
//	 * <p>
//	 * 查询具体场所商品信息
//	 * </p>
//	 */
//	@RequestMapping("listRentalSiteItems")
//	@RestReturn(value = ListRentalSiteItemsCommandResponse.class)
//	public RestResponse listRentalSiteItems(@Valid ListRentalSiteItemsCommand cmd) {
//		ListRentalSiteItemsCommandResponse listRentalSiteItemsCommandResponse =  rentalService.listRentalSiteItems(cmd);
//		RestResponse response = new RestResponse(listRentalSiteItemsCommandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

	
	/**
	 * <b>URL: /techpark/rental/deleteRentalSiteItem</b>
	 * <p>
	 * 删除具体场所商品信息
	 * </p>
	 */
	@RequestMapping("deleteRentalSiteItem")
	@RestReturn(value = String.class)
	public RestResponse deleteRentalSiteItem(@Valid DeleteItemAdminCommand cmd) {
		rentalService.deleteRentalSiteItem(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

//	/**
//	 * <b>URL: /techpark/rental/addRentalSiteRules</b>
//	 * <p>
//	 * 添加具体场所预定规则
//	 * </p>
//	 */
//
//	@RequestMapping("addRentalSiteRules")
//	@RestReturn(value = String.class)
//	public RestResponse addRentalSiteRules(@Valid AddRentalSiteRulesCommand cmd) {
//		rentalService.addRentalSiteRules(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

//	/**
//	 * <b>URL: /techpark/rental/addRentalSiteSimpleRules</b>
//	 * <p>
//	 * 添加具体场所预定规则-简单模式
//	 * </p>
//	 */
//
//	@RequestMapping("addRentalSiteSimpleRules")
//	@RestReturn(value = String.class)
//	public RestResponse addRentalSiteSimpleRules(@Valid AddRentalSiteRulesCommand cmd) {
//		rentalService.addRentalSiteSimpleRules(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /techpark/rental/deleteRentalSiteSimpleRules</b>
	 * <p>
	 * 关闭日期-删除某些日期的rules
	 * </p>
	 */

	@RequestMapping("deleteRentalSiteRules")
	@RestReturn(value = String.class)
	public RestResponse deleteRentalSiteRules(@Valid DeleteRentalSiteRulesCommand cmd) {
		rentalService.deleteRentalSiteRules(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /techpark/rental/findRentalSiteRules</b>
	 * <p>
	 * 查询某场所的预定规则
	 * </p>
	 */
	@RequestMapping("findRentalSiteRules")
	@RestReturn(value = FindRentalSiteRulesCommandResponse.class)
	public RestResponse findRentalSiteRules(@Valid FindRentalSiteRulesCommand cmd) {
		FindRentalSiteRulesCommandResponse findRentalSiteRulesCommandResponse = rentalService
				.findRentalSiteRules(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteRulesCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/rental/addRentalBill</b>
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
//	/**
//	 * <b>URL: /techpark/rental/addRentalBill</b>
//	 * <p>
//	 * 添加订单
//	 * </p>
//	 */
//	@RequestMapping("verifyServiceRentalBill")
//	@RestReturn(value = VerifyRentalBillCommandResponse.class)
//	public RestResponse verifyServiceRentalBill(@Valid AddRentalBillCommand cmd) {
//		VerifyRentalBillCommandResponse res = rentalService.VerifyRentalBill(cmd); 
//		RestResponse response = new RestResponse(res);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	/**
	 * <b>URL: /techpark/rental/addRentalItemBill</b>
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
	 * <b>URL: /techpark/rental/cancelRentalBill</b>
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
	 * <b>URL: /techpark/rental/deleteRentalBill</b>
	 * <p>
	 * 取消订单
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
	 * <b>URL: /techpark/rental/onlinePayCallback</b>
	 * <p>
	 * 取消订单
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
	 * <b>URL: /techpark/rental/findRentalBills</b>
	 * <p>
	 * 查询订单
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
	 * <b>URL: /techpark/rental/listRentalBills</b>
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
//	/**
//	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
//	 * <p>
//	 * 查询某日某场所的状态
//	 * </p>
//	 */
//
//	@RequestMapping("findRentalSitesStatus")
//	@RestReturn(value = FindRentalSitesStatusCommandResponse.class)
//	public RestResponse findRentalSitesStatus(@Valid FindRentalSitesStatusCommand cmd) {
//		FindRentalSitesStatusCommandResponse findRentalSiteDayStatusCommandResponse = rentalService
//				.findRentalSiteDayStatus(cmd);
//		RestResponse response = new RestResponse(
//				findRentalSiteDayStatusCommandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

	/**
	 * <b>URL: /techpark/rental/findRentalSiteItems</b>
	 * <p>
	 * 查询某场所的可预订物品
	 * </p>
	 */
	@RequestMapping("findRentalSiteItems")
	@RestReturn(value = FindRentalSiteItemsCommandResponse.class)
	public RestResponse findRentalSiteItems(@Valid FindRentalSiteItemsCommand cmd) { 
		FindRentalSiteItemsCommandResponse findRentalSiteItemsCommandResponse = rentalService
				.findRentalSiteItems(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteItemsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	 
	
	/**
	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
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
	
//	
//	/**
//	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
//	 * <p>
//	 * 确认预约- 状态置为成功
//	 * </p>
//	 */
//
//	@RequestMapping("confirmBill")
//	@RestReturn(value = String.class)
//	public RestResponse confirmBill(@Valid ConfirmBillCommand cmd) {
//		RentalBillDTO bill = rentalService.confirmBill(cmd);
//		RestResponse response = new RestResponse(bill);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//
//	/**
//	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
//	 * <p>
//	 * 完成预约- 状态置为已完成
//	 * </p>
//	 */
//
//	@RequestMapping("batchCompleteBill")
//	@RestReturn(value = BatchCompleteBillCommandResponse.class )
//	public RestResponse batchCompleteBill(@Valid BatchCompleteBillCommand cmd) {
//		BatchCompleteBillCommandResponse res= rentalService.batchCompleteBill(cmd);
//		RestResponse response = new RestResponse(res);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
//	 * <p>
//	 * 完成预约- 状态置为已完成
//	 * </p>
//	 */
//
//	@RequestMapping("completeBill")
//	@RestReturn(value = RentalBillDTO.class)
//	public RestResponse completeBill(@Valid CompleteBillCommand cmd) {
//		RentalBillDTO bill = rentalService.completeBill(cmd);
//		RestResponse response = new RestResponse(bill);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	/**
//	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
//	 * <p>
//	 * 未完成预约- 状态置为未完成
//	 * </p>
//	 */
//
//	@RequestMapping("batchIncompleteBill")
//	@RestReturn(value = BatchCompleteBillCommandResponse.class)
//	public RestResponse batchIncompleteBill(@Valid BatchIncompleteBillCommand cmd) {
//		BatchCompleteBillCommandResponse res = rentalService.batchIncompleteBill(cmd);
//		RestResponse response = new RestResponse(res);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
//	 * <p>
//	 * 未完成预约- 状态置为未完成
//	 * </p>
//	 */
//
//	@RequestMapping("incompleteBill")
//	@RestReturn(value = RentalBillDTO.class)
//	public RestResponse incompleteBill(@Valid IncompleteBillCommand cmd) {
//		RentalBillDTO bill = rentalService.incompleteBill(cmd);
//		RestResponse response = new RestResponse(bill);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	


	/**
	 * <b>URL: /techpark/rental/listRentalBillCount</b>
	 * <p>
	 * 数据统计
	 * </p>
	 */

	@RequestMapping("listRentalBillCount")
	@RestReturn(value = ListRentalBillCountCommandResponse.class)
	public RestResponse listRentalBillCount(@Valid ListRentalBillCountCommand cmd) {
		ListRentalBillCountCommandResponse bill = rentalService.listRentalBillCount(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <b>URL: /techpark/rental/exportRentalBills</b>
	 * <p>
	 * 导出预订详情
	 * </p>
	 */
	@RequestMapping("exportRentalBills")
	public String exportRentalBills(@Valid ListRentalBillsCommand cmd,HttpServletResponse response) {
		HttpServletResponse commandResponse = rentalService.exportRentalBills(cmd, response );
		return null;
	}
	
	
}
