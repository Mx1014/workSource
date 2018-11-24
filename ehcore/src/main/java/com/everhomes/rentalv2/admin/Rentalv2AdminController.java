package com.everhomes.rentalv2.admin;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rentalv2.Rentalv2PayService;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rentalv2.Rentalv2Service;
import com.everhomes.rest.RestResponse;

import java.util.List;

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
	@Autowired
	private Rentalv2PayService  rentalv2PayService;

	/**
	 * 
	 * <b>URL: /rental/admin/getResourceTypeList</b>
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
	 * <b>URL: /rental/admin/getResourceType</b>
	 * <p>
	 * 查询资源类型
	 * </p>
	 */
	@RequestMapping("getResourceType")
	@RestReturn(ResourceTypeDTO.class)
	public RestResponse getResourceType(@Valid GetResourceTypeCommand cmd) {
		ResourceTypeDTO resp = this.rentalService.getResourceType(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 *
	 * <b>URL: /rental/admin/deleteResource</b>
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
	 * <b>URL: /rental/admin/queryDefaultRule</b>
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
	 * <b>URL: /rental/admin/getResourceRule</b>
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
	 * <b>URL: /rental/admin/updateDefaultRule</b>
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
	 * <b>URL: /rental/admin/updateDefaultDateRule</b>
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
	 * <b>URL: /rental/admin/updateDefaultAttachmentRule</b>
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
	 * <b>URL: /rental/admin/getResourceList</b>
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
	 * <b>URL: /rental/admin/listResourceAbstract</b>
	 * <p>
	 * 获取资源摘要
	 * </p>
	 */
	@RequestMapping("listResourceAbstract")
	@RestReturn(GetResourceListAdminResponse.class)
	public RestResponse listResourceAbstract(@Valid GetResourceListAdminCommand cmd){
		GetResourceListAdminResponse list = this.rentalService.getResourceList(cmd);
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/addResource</b>
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
	 * <b>URL: /rental/admin/updateResource</b>
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
	 * <b>URL: /rental/admin/updateResourceStatus</b>
	 * <p>
	 * 开启/关闭 资源预约
	 * </p>
	 */
	@RequestMapping("updateResourceStatus")
	@RestReturn(String.class)
	public RestResponse updateResourceStatus(@Valid UpdateResourceAdminCommand cmd){
		this.rentalService.updateResourceStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/updateResourceOrder</b>
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
	 * <b>URL: /rental/admin/getStructureList</b>
	 * <p>
	 * 查询资源的基础设施
	 * </p>
	 */
	@RequestMapping("getStructureList")
	@RestReturn(value = GetStructureListResponse.class)
	public RestResponse getStructureList(@Valid GetStructureListAdminCommand cmd) {
		GetStructureListResponse getStructureListResponse =  rentalService.getStructureList(cmd);
		RestResponse response = new RestResponse(getStructureListResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/updateStructure</b>
	 * <p>
	 * 更新资源的基础设施
	 * </p>
	 */
	@RequestMapping("updateStructure")
	@RestReturn(value = String.class)
	public RestResponse updateStructure(@Valid UpdateStructureAdminCommand cmd) {
		rentalService.updateStructure(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/updateStructures</b>
	 * <p>
	 *	批量更新structures
	 * </p>
	 */
	@RequestMapping("updateStructures")
	@RestReturn(value = String.class)
	public RestResponse updateStructures(@Valid UpdateStructuresAdminCommand cmd) {
		rentalService.updateStructures(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /rental/admin/addRentalSiteRules</b>
	 * <p>
	 * 更新具体资源的规则
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
	public RestResponse updateRentalSiteCellRule(@Valid UpdateRentalSiteCellRuleAdminCommand cmd) {
		rentalService.updateRentalSiteCellRule(cmd);
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
	public RestResponse listRentalBills(@Valid com.everhomes.rest.rentalv2.ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse lsitRentalBillsCommandResponse = rentalService
				.listRentalBills(cmd);
		RestResponse response = new RestResponse(lsitRentalBillsCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/listActiveRentalBills</b>
	 * <p>
	 * 查询正在进行中的订单
	 * </p>
	 */
	@RequestMapping("listActiveRentalBills")
	@RestReturn(value = ListRentalBillsCommandResponse.class)
	public RestResponse listActiveRentalBills(@Valid com.everhomes.rest.rentalv2.ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse lsitRentalBillsCommandResponse = rentalService
				.listActiveRentalBills(cmd);
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
	public void exportRentalBills(@Valid com.everhomes.rest.rentalv2.ListRentalBillsCommand cmd, HttpServletResponse response) {
		rentalService.exportRentalBills(cmd, response );
	}

	/**
	 * <b>URL: /rental/admin/exportVipRentalBills</b>
	 * <p>
	 * 导出预订详情（vip车位预约用）
	 * </p>
	 */
	@RequestMapping("exportVipRentalBills")
	public void exportVipRentalBills(@Valid SearchRentalOrdersCommand cmd, HttpServletResponse response) {
		rentalService.exportRentalBills(cmd, response );
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


	/*-------------------------------------------新的更新资源规则接口---------------------------------- */

	/**
	 *
	 * <b>URL: /rental/admin/updateResourceTimeRule<b>
	 * <p>修改时间规则</p>
	 */
	@RequestMapping("updateResourceTimeRule")
	@RestReturn(String.class)
	public RestResponse updateResourceTimeRule(UpdateResourceTimeRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		rentalService.updateResourceTimeRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/getResourceTimeRule<b>
	 * <p>获取时间规则</p>
	 */
	@RequestMapping("getResourceTimeRule")
	@RestReturn(ResourceTimeRuleDTO.class)
	public RestResponse getResourceTimeRule(GetResourceTimeRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){ //一个资源应该只有一条规则 忽略ownerType ownerId防止产生多条
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		RestResponse response = new RestResponse(rentalService.getResourceTimeRule(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/updateResourcePriceRule<b>
	 * <p>修改价格规则</p>
	 */
	@RequestMapping("updateResourcePriceRule")
	@RestReturn(String.class)
	public RestResponse updateResourcePriceRule(UpdateResourcePriceRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		rentalService.updateResourcePriceRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/getResourcePriceRule<b>
	 * <p>获取价格规则</p>
	 */
	@RequestMapping("getResourcePriceRule")
	@RestReturn(ResourcePriceRuleDTO.class)
	public RestResponse getResourcePriceRule(GetResourcePriceRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		RestResponse response = new RestResponse(rentalService.getResourcePriceRule(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/updateResourceRentalRule<b>
	 * <p>修改预约规则</p>
	 */
	@RequestMapping("updateResourceRentalRule")
	@RestReturn(String.class)
	public RestResponse updateResourceRentalRule(UpdateResourceRentalRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		rentalService.updateResourceRentalRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/getResourceRentalRule<b>
	 * <p>获取预约规则</p>
	 */
	@RequestMapping("getResourceRentalRule")
	@RestReturn(ResourceRentalRuleDTO.class)
	public RestResponse getResourceRentalRule(GetResourceRentalRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		RestResponse response = new RestResponse(rentalService.getResourceRentalRule(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/updateResourceOrderRule<b>
	 * <p>修改订单规则</p>
	 */
	@RequestMapping("updateResourceOrderRule")
	@RestReturn(String.class)
	public RestResponse updateResourceOrderRule(UpdateResourceOrderRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		rentalService.updateResourceOrderRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 *
	 * <b>URL: /rental/admin/getResourceOrderRule<b>
	 * <p>获取订单规则</p>
	 */
	@RequestMapping("getResourceOrderRule")
	@RestReturn(ResourceOrderRuleDTO.class)
	public RestResponse getResourceOrderRule(GetResourceOrderRuleCommand cmd) {
		if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())){
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}
		RestResponse response = new RestResponse(rentalService.getResourceOrderRule(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/searchRentalOrders</b>
	 * <p>查询订单</p>
	 */
	@RequestMapping("searchRentalOrders")
	@RestReturn(value = SearchRentalOrdersResponse.class)
	public RestResponse searchRentalOrders(SearchRentalOrdersCommand cmd) {

		RestResponse response = new RestResponse(rentalService.searchRentalOrders(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /rental/admin/getRentalOrderById</b>
	 * <p>通过id查询订单</p>
	 */
	@RequestMapping("getRentalOrderById")
	@RestReturn(value = RentalOrderDTO.class)
	public RestResponse getRentalOrderById(GetRentalBillCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getRentalOrderById(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/*-------------------------------------------新的更新资源接口---------------------------------- */
	/**
	 * <b>URL: /rental/admin/getResourceAttachment</b>
	 * <p>
	 * 查询资源的提交信息
	 * </p>
	 */

	@RequestMapping("getResourceAttachment")
	@RestReturn(value = ResourceAttachmentDTO.class)
	public RestResponse getResourceAttachment(@Valid GetResourceAttachmentCommand cmd) {
		if (RuleSourceType.DEFAULT.getCode().equals(cmd.getSourceType())) {
			GetResourceTimeRuleCommand cmd2 = ConvertHelper.convert(cmd, GetResourceTimeRuleCommand.class);
			this.getResourceTimeRule(cmd2);//防止没有规则
		}
		RestResponse response = new RestResponse(rentalService.getResourceAttachment(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/getResourceSiteNumbers</b>
	 * <p>
	 * 查询资源的编号设置
	 * </p>
	 */

	@RequestMapping("getResourceSiteNumbers")
	@RestReturn(value = ResourceSiteNumbersDTO.class)
	public RestResponse getResourceSiteNumbers(@Valid GetResourceSiteNumbersCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getResourceSiteNumbers(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/updateResourceSiteNumbers</b>
	 * <p>
	 * 更改资源的编号设置
	 * </p>
	 */

	@RequestMapping("updateResourceSiteNumbers")
	@RestReturn(value = String.class)
	public RestResponse updateResourceSiteNumbers(@Valid UpdateResourceSiteNumbersCommand cmd) {
		rentalService.updateResourceSiteNumbers(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /rental/admin/confirmRefund</b>
	 * <p>
	 * 确认退款
	 * </p>
	 */

	@RequestMapping("confirmRefund")
	@RestReturn(value = String.class)
	public RestResponse confirmRefund( ConfirmRefundCommand cmd) {
		rentalService.confirmRefund(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /rental/admin/queryRentalStatistics</b>
	 * <p>
	 * 查询资源预约订单统计信息
	 * </p>
	 */
	@RequestMapping("queryRentalStatistics")
	@RestReturn(value = QueryRentalStatisticsResponse.class)
	public RestResponse queryRentalStatistics( QueryRentalStatisticsCommand cmd) {
		QueryRentalStatisticsResponse statisticsResponse = rentalService.queryRentalStatistics(cmd);
		RestResponse response = new RestResponse(statisticsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/queryOrgRentalStatistics</b>
	 * <p>
	 * 查询资源预约订单统计信息(公司)
	 * </p>
	 */
	@RequestMapping("queryOrgRentalStatistics")
	@RestReturn(value = QueryOrgRentalStatisticsResponse.class)
	public RestResponse queryOrgRentalStatistics( QueryRentalStatisticsCommand cmd) {
		QueryOrgRentalStatisticsResponse statisticsResponse = rentalService.queryOrgRentalStatistics(cmd);
		RestResponse response = new RestResponse(statisticsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	//====================================================企业账户设置================================================

	/**
	 * <b>URL: /rental/admin/listPayeeAccounts</b>
	 * <p>
	 * 查询企业账户信息
	 * </p>
	 */
	@RequestMapping("listPayeeAccounts")
	@RestReturn(value = ListBizPayeeAccountDTO.class,collection = true)
	public RestResponse listPayeeAccounts( ListPayeeAccountsCommand cmd) {
		List<ListBizPayeeAccountDTO> list =  rentalv2PayService.listPayeeAccounts(cmd);
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/getGeneralAccountSetting</b>
	 * <p>
	 * 获取通用支付账户设定
	 * </p>
	 */
	@RequestMapping("getGeneralAccountSetting")
	@RestReturn(value = ListBizPayeeAccountDTO.class)
	public RestResponse getGeneralAccountSetting( GetGeneralAccountSettingCommand cmd) {
		ListBizPayeeAccountDTO dto = rentalv2PayService.getGeneralAccountSetting(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/updateGeneralAccountSetting</b>
	 * <p>
	 * 更新通用支付账户设定
	 * </p>
	 */
	@RequestMapping("updateGeneralAccountSetting")
	@RestReturn(value = String.class)
	public RestResponse updateGeneralAccountSetting( UpdateGeneralAccountSettingCommand cmd) {
		rentalv2PayService.updateGeneralAccountSetting(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /rental/admin/getResourceAccountSetting</b>
	 * <p>
	 * 获取资源支付账户设定
	 * </p>
	 */
	@RequestMapping("getResourceAccountSetting")
	@RestReturn(value = GetResourceAccountSettingResponse.class)
	public RestResponse getResourceAccountSetting( GetResourceAccountSettingCommand cmd) {
        GetResourceAccountSettingResponse res = rentalv2PayService.getResourceAccountSetting(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /rental/admin/deleteResourceAccountSetting</b>
     * <p>
     * 删除资源支付账户
     * </p>
     */
    @RequestMapping("deleteResourceAccountSetting")
    @RestReturn(value = String.class)
    public RestResponse deleteResourceAccountSetting( UpdateResourceAccountSettingCommand cmd) {
		rentalv2PayService.deleteResourceAccountSetting(cmd.getId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /rental/admin/updateResourceAccountSetting</b>
     * <p>
     * 新增/更新资源支付账户
     * </p>
     */
    @RequestMapping("updateResourceAccountSetting")
    @RestReturn(value = String.class)
    public RestResponse updateResourceAccountSetting( UpdateResourceAccountSettingCommand cmd) {
		rentalv2PayService.updateResourceAccountSetting(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /rental/admin/getHolidayCloseDates</b>
	 * <p>
	 * 获取双休日和节假日关闭日期
	 * </p>
	 */
	@RequestMapping("getHolidayCloseDates")
	@RestReturn(value = Long.class,collection = true)
	public RestResponse getHolidayCloseDates( GetHolidayCloseDatesCommand cmd) {
		RestResponse response = new RestResponse(rentalService.getHolidayCloseDates(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
