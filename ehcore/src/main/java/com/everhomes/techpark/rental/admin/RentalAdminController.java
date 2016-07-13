package com.everhomes.techpark.rental.admin;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.rental.AddItemAdminCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommandResponse;
import com.everhomes.rest.techpark.rental.BatchIncompleteBillCommand;
import com.everhomes.rest.techpark.rental.CompleteBillCommand;
import com.everhomes.rest.techpark.rental.DeleteItemAdminCommand;
import com.everhomes.rest.techpark.rental.GetItemListAdminCommand;
import com.everhomes.rest.techpark.rental.GetItemListCommandResponse;
import com.everhomes.rest.techpark.rental.IncompleteBillCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.RentalBillDTO;
import com.everhomes.rest.techpark.rental.UpdateItemAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListCommand;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListResponse;
import com.everhomes.rest.techpark.rental.admin.GetRefundUrlCommand;
import com.everhomes.rest.techpark.rental.admin.GetRentalBillCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminResponse;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.rest.techpark.rental.admin.RefundOrderDTO;
import com.everhomes.rest.techpark.rental.admin.UpdateDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateItemsAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateRentalSiteDiscountAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateResourceAdminCommand;
import com.everhomes.techpark.rental.RentalService;

/**
 * <ul>
 * 预约后台系统：
 * <li>后台维护某种场所的预约规则</li>
 * <li>维护具体场所</li>
 * <li>查询预约情况</li>
 * </ul>
 */
@RestDoc(value = "rental admin controller", site = "ehcore")
@RestController
@RequestMapping("/rental/admin")
public class RentalAdminController extends ControllerBase {

	@Autowired
	private RentalService rentalService;
	
//	/**
//	 * 
//	 * <b>URL: /rental/admin/addDefaultRule<b>
//	 * <p>
//	 * 添加默认规则
//	 * </p>
//	 */
//	@RequestMapping("addDefaultRule")
//	@RestReturn(String.class)
//	public RestResponse addDefaultRule(@Valid AddDefaultRuleAdminCommand cmd) {
//		this.rentalService.addDefaultRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

	/**
	 * 
	 * <b>URL: /rental/admin/queryDefaultRule<b>
	 * <p>
	 * 查询默认规则
	 * </p>
	 */
	@RequestMapping("queryDefaultRule")
	@RestReturn(QueryDefaultRuleAdminResponse.class)
	public RestResponse queryDefaultRule(@Valid QueryDefaultRuleAdminCommand cmd) {
		QueryDefaultRuleAdminResponse queryDefaultRuleAdminResponse = this.rentalService.queryDefaultRule(cmd);
		RestResponse response = new RestResponse(queryDefaultRuleAdminResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/updateDefaultRule<b>
	 * <p>
	 * 修改默认规则
	 * </p>
	 */
	@RequestMapping("updateDefaultRule")
	@RestReturn(String.class)
	public RestResponse updateDefaultRule(@Valid UpdateDefaultRuleAdminCommand cmd) {
		this.rentalService.updateDefaultRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * 
	 * <b>URL: /rental/admin/getResourceList<b>
	 * <p>
	 * 获取资源列表
	 * </p>
	 */
	@RequestMapping("getResourceList")
	@RestReturn(GetResourceListAdminResponse.class)
	public RestResponse getResourceList(@Valid GetResourceListAdminCommand cmd){
		GetResourceListAdminResponse list = this.rentalService.getResourceList(cmd);
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/addResource<b>
	 * <p>
	 * 添加一个预约资源
	 * </p>
	 */
	@RequestMapping("addResource")
	@RestReturn(String.class)
	public RestResponse addResource(@Valid AddResourceAdminCommand cmd){
		this.rentalService.addResource(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/updateResource<b>
	 * <p>
	 * 更新资源
	 * </p>
	 */
	@RequestMapping("updateResource")
	@RestReturn(String.class)
	public RestResponse updateResource(@Valid UpdateResourceAdminCommand cmd){
		this.rentalService.updateResource(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/admin/getItemList</b>
	 * <p>
	 * 查询资源的商品列表
	 * </p>
	 */
	@RequestMapping("getItemList")
	@RestReturn(value = GetItemListCommandResponse.class)
	public RestResponse getItemList(@Valid GetItemListAdminCommand cmd) {
		GetItemListCommandResponse listRentalSiteItemsCommandResponse =  rentalService.listRentalSiteItems(cmd);
		RestResponse response = new RestResponse(listRentalSiteItemsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /rental/admin/addItem</b>
	 * <p>
	 * 添加具体资源的商品信息
	 * </p>
	 */
	@RequestMapping("addItem")
	@RestReturn(value = String.class)
	public RestResponse addRentalSiteItem(@Valid AddItemAdminCommand cmd) {
		rentalService.addItem(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	

	
	/**
	 * <b>URL: /rental/admin/updateItem</b>
	 * <p>
	 * 更新具体资源商品信息
	 * </p>
	 */
	@RequestMapping("updateItem")
	@RestReturn(value = String.class)
	public RestResponse updateItem(@Valid UpdateItemAdminCommand cmd) {
		rentalService.updateItem(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/admin/deleteItem</b>
	 * <p>
	 * 删除具体场所商品信息
	 * </p>
	 */
	@RequestMapping("deleteItem")
	@RestReturn(value = String.class)
	public RestResponse deleteItem(@Valid DeleteItemAdminCommand cmd) {
		rentalService.deleteRentalSiteItem(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/admin/updateItems</b>
	 * <p>
	 *批量更新items
	 * </p>
	 */
	@RequestMapping("updateItems")
	@RestReturn(value = String.class)
	public RestResponse updateItems(@Valid UpdateItemsAdminCommand cmd) {
		rentalService.updateItems(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	/**
	 * <b>URL: /rental/admin/addRentalSiteRules</b>
	 * <p>
	 * 添加具体资源的规则
	 * </p>
	 */

	@RequestMapping("addRentalSiteRules")
	@RestReturn(value = String.class)
	public RestResponse addRentalSiteRules(@Valid AddRentalSiteRulesAdminCommand cmd) {
		rentalService.addRentalSiteSimpleRules(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /rental/admin/updateRentalSiteRules</b>
	 * <p>
	 * 更新单元格信息
	 * </p>
	 */

	@RequestMapping("updateRentalSiteRules")
	@RestReturn(value = String.class)
	public RestResponse updateRentalSiteSimpleRules(@Valid  UpdateRentalSiteRulesAdminCommand cmd) {
		rentalService.updateRentalSiteSimpleRules(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/admin/updateRentalSiteDiscount</b>
	 * <p>
	 * 更新优惠信息
	 * </p>
	 */

	@RequestMapping("updateRentalSiteDiscount")
	@RestReturn(value = String.class)
	public RestResponse updateRentalSiteDiscount(@Valid  UpdateRentalSiteDiscountAdminCommand cmd) {
		rentalService.updateRentalSiteDiscount(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/listRentalBills</b>
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
	 * <b>URL: /rental/admin/exportRentalBills</b>
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
	 * <b>URL: /rental/admin/findRentalSitesStatus</b>
	 * <p>
	 * 批量完成预约- 状态置为已完成
	 * </p>
	 */

	@RequestMapping("batchCompleteBill")
	@RestReturn(value = BatchCompleteBillCommandResponse.class )
	public RestResponse batchCompleteBill(@Valid BatchCompleteBillCommand cmd) {
		BatchCompleteBillCommandResponse res= rentalService.batchCompleteBill(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/completeBill</b>
	 * <p>
	 * 完成预约- 状态置为已完成
	 * </p>
	 */

	@RequestMapping("completeBill")
	@RestReturn(value = RentalBillDTO.class)
	public RestResponse completeBill(@Valid CompleteBillCommand cmd) {
		RentalBillDTO bill = rentalService.completeBill(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /rental/admin/batchIncompleteBill</b>
	 * <p>
	 * 批量未完成预约- 状态置为未完成
	 * </p>
	 */

	@RequestMapping("batchIncompleteBill")
	@RestReturn(value = BatchCompleteBillCommandResponse.class)
	public RestResponse batchIncompleteBill(@Valid BatchIncompleteBillCommand cmd) {
		BatchCompleteBillCommandResponse res = rentalService.batchIncompleteBill(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/incompleteBill</b>
	 * <p>
	 * 未完成预约- 状态置为未完成
	 * </p>
	 */

	@RequestMapping("incompleteBill")
	@RestReturn(value = RentalBillDTO.class)
	public RestResponse incompleteBill(@Valid IncompleteBillCommand cmd) {
		RentalBillDTO bill = rentalService.incompleteBill(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	
	/**
	 * <b>URL: /rental/admin/getRefundOrderList</b>
	 * <p>
	 * 查询退款-根据时间段，渠道，图标，退款状态（暂时没用）查询
	 * </p>
	 */

	@RequestMapping("getRefundOrderList")
	@RestReturn(value = RefundOrderDTO.class)
	public RestResponse getRefundOrderList(@Valid GetRefundOrderListCommand cmd) {
		GetRefundOrderListResponse resp = rentalService.getRefundOrderList(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/admin/getRentalBill</b>
	 * <p>
	 * 查询单个订单
	 * </p>
	 */
	@RequestMapping("getRentalBill")
	@RestReturn(value = RentalBillDTO.class)
	public RestResponse getRentalBill(@Valid GetRentalBillCommand cmd) {
		RentalBillDTO dto = rentalService.getRentalBill(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	
	/**
	 * <b>URL: /rental/admin/getRefundUrl</b>
	 * <p>
	 * 拿到退款URL
	 * </p>
	 */
	@RequestMapping("getRefundUrl")
	@RestReturn(value = String.class)
	public RestResponse getRefundUrl(@Valid GetRefundUrlCommand cmd) {
		String resp = rentalService.getRefundUrl(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
