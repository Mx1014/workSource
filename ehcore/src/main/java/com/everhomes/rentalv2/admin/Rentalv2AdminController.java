package com.everhomes.rentalv2.admin;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.rentalv2.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rentalv2.Rentalv2Service;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.rentalv2.AddItemAdminCommand;
import com.everhomes.rest.rentalv2.BatchCompleteBillCommand;
import com.everhomes.rest.rentalv2.BatchCompleteBillCommandResponse;
import com.everhomes.rest.rentalv2.BatchIncompleteBillCommand;
import com.everhomes.rest.rentalv2.CompleteBillCommand;
import com.everhomes.rest.rentalv2.DeleteItemAdminCommand;
import com.everhomes.rest.rentalv2.GetItemListAdminCommand;
import com.everhomes.rest.rentalv2.GetItemListCommandResponse;
import com.everhomes.rest.rentalv2.IncompleteBillCommand;
import com.everhomes.rest.rentalv2.ListRentalBillsCommand;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.UpdateItemAdminCommand;

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
public class Rentalv2AdminController extends ControllerBase {

	@Autowired
	private Rentalv2Service rentalService;
	
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
	 * <b>URL: /rental/admin/getResourceTypeList<b>
	 * <p>
	 * 查询资源类型列表
	 * </p>
	 */
	@RequestMapping("getResourceTypeList")
	@RestReturn(GetResourceTypeListResponse.class)
	public RestResponse getResourceTypeList(@Valid GetResourceTypeListCommand cmd) {
		GetResourceTypeListResponse resp = this.rentalService.getResourceTypeList(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/createResourceType<b>
	 * <p>
	 * 添加资源类型
	 * </p>
	 */
	@RequestMapping("createResourceType")
	@RestReturn(String.class)
	public RestResponse createResourceType(@Valid CreateResourceTypeCommand cmd) {
		this.rentalService.createResourceType(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/deleteResourceType<b>
	 * <p>
	 * 删除资源类型
	 * </p>
	 */
	@RequestMapping("deleteResourceType")
	@RestReturn(String.class)
	public RestResponse deleteResourceType(@Valid DeleteResourceTypeCommand cmd) {
		this.rentalService.deleteResourceType(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * 
	 * <b>URL: /rental/admin/deleteResource<b>
	 * <p>
	 * 删除资源 
	 * </p>
	 */
	@RequestMapping("deleteResource")
	@RestReturn(String.class)
	public RestResponse deleteResource(DeleteResourceCommand cmd){
		this.rentalService.deleteResource(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * 
	 * <b>URL: /rental/admin/closeResourceType<b>
	 * <p>
	 * 关闭资源类型
	 * </p>
	 */
	@RequestMapping("closeResourceType")
	@RestReturn(String.class)
	public RestResponse closeResourceType(@Valid CloseResourceTypeCommand cmd) {
		this.rentalService.closeResourceType(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/openResourceType<b>
	 * <p>
	 * 开启资源类型
	 * </p>
	 */
	@RequestMapping("openResourceType")
	@RestReturn(String.class)
	public RestResponse openResourceType(@Valid OpenResourceTypeCommand cmd) {
		this.rentalService.openResourceType(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/updateResourceType<b>
	 * <p>
	 * 修改资源类型
	 * </p>
	 */
	@RequestMapping("updateResourceType")
	@RestReturn(String.class)
	public RestResponse updateResourceType(@Valid UpdateResourceTypeCommand cmd) {
		this.rentalService.updateResourceType(cmd);
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

	/**
	 * 
	 * <b>URL: /rental/admin/getResourceRule<b>
	 * <p>
	 * 查询资源的规则
	 * </p>
	 */
	@RequestMapping("getResourceRule")
	@RestReturn(QueryDefaultRuleAdminResponse.class)
	public RestResponse getResourceRule(@Valid GetResourceRuleAdminCommand cmd) {
		QueryDefaultRuleAdminResponse queryDefaultRuleAdminResponse = this.rentalService.getResourceRule(cmd);
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
	 * <b>URL: /rental/admin/updateDefaultDateRule<b>
	 * <p>
	 * 修改默认规则时间
	 * </p>
	 */
	@RequestMapping("updateDefaultDateRule")
	@RestReturn(String.class)
	public RestResponse updateDefaultDateRule(@Valid UpdateDefaultDateRuleAdminCommand cmd) {
		this.rentalService.updateDefaultDateRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * 
	 * <b>URL: /rental/admin/updateDefaultAttachmentRule<b>
	 * <p>
	 * 修改默认规则提示文字
	 * </p>
	 */
	@RequestMapping("updateDefaultAttachmentRule")
	@RestReturn(String.class)
	public RestResponse updateDefaultAttachmentRule(@Valid UpdateDefaultAttachmentRuleAdminCommand cmd) {
		this.rentalService.updateDefaultAttachmentRule(cmd);
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
	 *
	 * <b>URL: /rental/admin/updateResourceOrder<b>
	 * <p>
	 * 更新资源顺序
	 * </p>
	 */
	@RequestMapping("updateResourceOrder")
	@RestReturn(String.class)
	public RestResponse updateResourceOrder(@Valid UpdateResourceOrderAdminCommand cmd){
		this.rentalService.updateResourceOrder(cmd);
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
	 * <b>URL: /rental/admin/updateResourceAttachment</b>
	 * <p>
	 * 添加修改资源的提交信息
	 * </p>
	 */

	@RequestMapping("updateResourceAttachment")
	@RestReturn(value = String.class)
	public RestResponse updateResourceAttachment(@Valid UpdateResourceAttachmentCommand cmd) {
		rentalService.updateResourceAttachment(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/updateRentalDate</b>
	 * <p>
	 * 添加修改资源的开放时间
	 * </p>
	 */

	@RequestMapping("updateRentalDate")
	@RestReturn(value = String.class)
	public RestResponse updateRentalDate(@Valid UpdateRentalDateCommand cmd) {
		rentalService.updateRentalDate(cmd);
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
	@RestReturn(value = GetRefundOrderListResponse.class)
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
	 * <b>URL: /re	ntal/admin/getRefundUrl</b>
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
	

	/**
	 * <b>URL: /rental/admin/addCheckOperator</b>
	 * <p>
	 * 新增签到的管理员
	 * </p>
	 */
	@RequestMapping("addCheckOperator")
	@RestReturn(value = String.class)
	public RestResponse addCheckOperator(@Valid AddCheckOperatorCommand cmd) {
		rentalService.addCheckOperator(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /rental/admin/deleteCheckOperator</b>
	 * <p>
	 * 删除签到的管理员
	 * </p>
	 */
	@RequestMapping("deleteCheckOperator")
	@RestReturn(value = String.class)
	public RestResponse deleteCheckOperator(@Valid AddCheckOperatorCommand cmd) {
		rentalService.deleteCheckOperator(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
