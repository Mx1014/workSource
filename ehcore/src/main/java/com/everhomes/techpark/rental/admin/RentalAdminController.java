package com.everhomes.techpark.rental.admin;

import java.util.ArrayList;
import java.util.List;

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
import com.everhomes.rest.techpark.rental.IncompleteBillCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.RentalBillDTO;
import com.everhomes.rest.techpark.rental.UpdateItemAdminCommand;
import com.everhomes.rest.techpark.rental.getItemListAdminCommand;
import com.everhomes.rest.techpark.rental.getItemListCommandResponse;
import com.everhomes.rest.techpark.rental.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminResponse;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminResponse;
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
	
	/**
	 * 
	 * <b>URL: /rental/admin/addDefaultRule<b>
	 * <p>
	 * 添加默认规则
	 * </p>
	 */
	@RequestMapping("addDefaultRule")
	@RestReturn(String.class)
	public RestResponse addDefaultRule(@Valid AddDefaultRuleAdminCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

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
	
	@RequestMapping("getResourceList")
	@RestReturn(GetResourceListAdminResponse.class)
	public RestResponse getResourceList(@Valid GetResourceListAdminCommand cmd){
		List<GetResourceListAdminResponse> list = new ArrayList<GetResourceListAdminResponse>();
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("addResource")
	@RestReturn(String.class)
	public RestResponse addResource(@Valid AddResourceAdminCommand cmd){
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("updateResource")
	@RestReturn(String.class)
	public RestResponse updateResource(@Valid UpdateResourceAdminCommand cmd){

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/rental/listRentalSiteItems</b>
	 * <p>
	 * 查询具体场所商品信息
	 * </p>
	 */
	@RequestMapping("getItemList")
	@RestReturn(value = getItemListCommandResponse.class)
	public RestResponse getItemList(@Valid getItemListAdminCommand cmd) {
		getItemListCommandResponse listRentalSiteItemsCommandResponse =  rentalService.listRentalSiteItems(cmd);
		RestResponse response = new RestResponse(listRentalSiteItemsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /techpark/rental/addRentalSiteItems</b>
	 * <p>
	 * 添加具体场所商品信息
	 * </p>
	 */
	@RequestMapping("addItems")
	@RestReturn(value = String.class)
	public RestResponse addRentalSiteItem(@Valid AddItemAdminCommand cmd) {
		rentalService.addRentalSiteItems(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	

	
	/**
	 * <b>URL: /techpark/rental/deleteRentalSiteItem</b>
	 * <p>
	 * 删除具体场所商品信息
	 * </p>
	 */
	@RequestMapping("updateItem")
	@RestReturn(value = String.class)
	public RestResponse updateItem(@Valid UpdateItemAdminCommand cmd) {
//		rentalService.deleteRentalSiteItem(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/rental/deleteRentalSiteItem</b>
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
	 * <b>URL: /techpark/rental/deleteRentalSiteItem</b>
	 * <p>
	 *批量更新items
	 * </p>
	 */
	@RequestMapping("updateItems")
	@RestReturn(value = String.class)
	public RestResponse UpdateItems(@Valid UpdateItemsAdminCommand cmd) {
//		rentalService.deleteRentalSiteItem(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	/**
	 * <b>URL: /techpark/rental/addRentalSiteSimpleRules</b>
	 * <p>
	 * 添加具体场所预定规则-简单模式
	 * </p>
	 */

	@RequestMapping("addRentalSiteRules")
	@RestReturn(value = String.class)
	public RestResponse addRentalSiteRules(@Valid AddRentalSiteRulesAdminCommand cmd) {
//		rentalService.addRentalSiteSimpleRules(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /techpark/rental/addRentalSiteSimpleRules</b>
	 * <p>
	 * 更新单元格信息
	 * </p>
	 */

	@RequestMapping("updateRentalSiteRules")
	@RestReturn(value = String.class)
	public RestResponse updateRentalSiteSimpleRules(@Valid  UpdateRentalSiteRulesAdminCommand cmd) {
//		rentalService.updateRentalSiteRules(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/rental/addRentalSiteSimpleRules</b>
	 * <p>
	 * 更新优惠信息
	 * </p>
	 */

	@RequestMapping("updateRentalSiteDiscount")
	@RestReturn(value = String.class)
	public RestResponse updateRentalSiteDiscount(@Valid  UpdateRentalSiteDiscountAdminCommand cmd) {
//		rentalService.updateRentalSiteRules(cmd);
		RestResponse response = new RestResponse();
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


	/**
	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
	 * <p>
	 * 完成预约- 状态置为已完成
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
	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
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
	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
	 * <p>
	 * 未完成预约- 状态置为未完成
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
	 * <b>URL: /techpark/rental/findRentalSitesStatus</b>
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
	

}
