package com.everhomes.rentalv2;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.AddCheckOperatorCommand;
import com.everhomes.rest.rentalv2.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.AddRentalSiteRulesAdminCommand;
import com.everhomes.rest.rentalv2.admin.AddResourceAdminCommand;
import com.everhomes.rest.rentalv2.admin.CloseResourceTypeCommand;
import com.everhomes.rest.rentalv2.admin.CreateResourceTypeCommand;
import com.everhomes.rest.rentalv2.admin.DefaultRuleDTO;
import com.everhomes.rest.rentalv2.admin.DeleteResourceCommand;
import com.everhomes.rest.rentalv2.admin.DeleteResourceTypeCommand;
import com.everhomes.rest.rentalv2.admin.GetRefundOrderListCommand;
import com.everhomes.rest.rentalv2.admin.GetRefundOrderListResponse;
import com.everhomes.rest.rentalv2.admin.GetRefundUrlCommand;
import com.everhomes.rest.rentalv2.admin.GetRentalBillCommand;
import com.everhomes.rest.rentalv2.admin.GetResourceListAdminCommand;
import com.everhomes.rest.rentalv2.admin.GetResourceListAdminResponse;
import com.everhomes.rest.rentalv2.admin.GetResourceRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.GetResourceTypeListCommand;
import com.everhomes.rest.rentalv2.admin.GetResourceTypeListResponse;
import com.everhomes.rest.rentalv2.admin.OpenResourceTypeCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.rest.rentalv2.admin.UpdateDefaultAttachmentRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateDefaultDateRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateItemsAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateResourceAttachmentCommand;
import com.everhomes.rest.rentalv2.admin.UpdateRentalDateCommand;
import com.everhomes.rest.rentalv2.admin.UpdateRentalSiteDiscountAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateRentalSiteRulesAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateResourceAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateResourceTypeCommand;

public interface Rentalv2Service { 
//	void updateRentalRule(UpdateRentalRuleCommand cmd);

//	Long addRentalSite(AddRentalSiteCommand cmd);

	void addItem(AddItemAdminCommand cmd);
 

//	FindRentalSitesStatusCommandResponse findRentalSiteDayStatus(
//			FindRentalSitesStatusCommand cmd);

//	GetRentalSiteTypeResponse findRentalSiteTypes();

	FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItems(
			FindRentalSiteItemsAndAttachmentsCommand cmd);

	FindRentalSitesCommandResponse findRentalSites(FindRentalSitesCommand cmd);

	FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd);

	RentalBillDTO addRentalBill(AddRentalBillCommand cmd);

	FindRentalBillsCommandResponse findRentalBills(FindRentalBillsCommand cmd);

//	GetRentalTypeRuleCommandResponse getRentalTypeRule(
//			GetRentalTypeRuleCommand cmd);

	void addRentalSiteSimpleRules(AddRentalSiteRulesAdminCommand cmd);
 
	void deleteRentalSiteRules(DeleteRentalSiteRulesCommand cmd);

	void cancelRentalBill(CancelRentalBillCommand cmd);

	void updateRentalSite(UpdateRentalSiteCommand cmd);

	void deleteRentalSite(DeleteRentalSiteCommand cmd);

	void deleteRentalSiteItem(DeleteItemAdminCommand cmd);

	GetItemListCommandResponse listRentalSiteItems(
			GetItemListAdminCommand cmd);

	AddRentalBillItemCommandResponse addRentalItemBill(AddRentalBillItemCommand cmd);

	ListRentalBillsCommandResponse listRentalBills(ListRentalBillsCommand cmd);

	void deleteRentalBill(DeleteRentalBillCommand cmd);
 
	OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd);

	FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(
			FindRentalSiteWeekStatusCommand cmd);

	RentalBillDTO confirmBill(ConfirmBillCommand cmd);

	RentalBillDTO completeBill(CompleteBillCommand cmd);

	RentalBillDTO incompleteBill(IncompleteBillCommand cmd);

	void disableRentalSite(DisableRentalSiteCommand cmd);

	void enableRentalSite(EnableRentalSiteCommand cmd);
 

	BatchCompleteBillCommandResponse batchIncompleteBill(
			BatchIncompleteBillCommand cmd);

	BatchCompleteBillCommandResponse batchCompleteBill(
			BatchCompleteBillCommand cmd);

	ListRentalBillCountCommandResponse listRentalBillCount(
			ListRentalBillCountCommand cmd);

//	VerifyRentalBillCommandResponse VerifyRentalBill(AddRentalBillCommand cmd);
	
	public HttpServletResponse exportRentalBills(@Valid ListRentalBillsCommand cmd,HttpServletResponse response);
	void mappingRentalBillDTO(RentalBillDTO dto, RentalOrder bill);
	void addDefaultRule(AddDefaultRuleAdminCommand cmd);
	QueryDefaultRuleAdminResponse queryDefaultRule(QueryDefaultRuleAdminCommand cmd);
	GetResourceListAdminResponse getResourceList(GetResourceListAdminCommand cmd);
	void addResource(AddResourceAdminCommand cmd);
	void updateResource(UpdateResourceAdminCommand cmd);
	void updateItem(UpdateItemAdminCommand cmd);
	void updateItems(UpdateItemsAdminCommand cmd);
	FindAutoAssignRentalSiteWeekStatusResponse findAutoAssignRentalSiteWeekStatus(
			FindAutoAssignRentalSiteWeekStatusCommand cmd);
	FindAutoAssignRentalSiteDayStatusResponse findAutoAssignRentalSiteDayStatus(
			FindAutoAssignRentalSiteDayStatusCommand cmd);
	void updateRentalSiteSimpleRules(UpdateRentalSiteRulesAdminCommand cmd);
	void updateRentalSiteDiscount(UpdateRentalSiteDiscountAdminCommand cmd);
	void valiRentalBill(List<RentalBillRuleDTO> ruleDTOs);
	void valiRentalBill(Double rentalcount, List<RentalBillRuleDTO> ruleDTOs);

	void updateDefaultRule(UpdateDefaultRuleAdminCommand cmd);

	GetRefundOrderListResponse getRefundOrderList(GetRefundOrderListCommand cmd);

	RentalBillDTO getRentalBill(GetRentalBillCommand cmd);

	String getRefundUrl(GetRefundUrlCommand cmd);

	GetResourceTypeListResponse getResourceTypeList(
			GetResourceTypeListCommand cmd);

	void createResourceType(CreateResourceTypeCommand cmd);

	void deleteResourceType(DeleteResourceTypeCommand cmd);

	void updateResourceType(UpdateResourceTypeCommand cmd);

	void closeResourceType(CloseResourceTypeCommand cmd);

	void openResourceType(OpenResourceTypeCommand cmd);

	FindRentalSiteMonthStatusCommandResponse findRentalSiteMonthStatus(
			FindRentalSiteMonthStatusCommand cmd);


	void deleteResource(DeleteResourceCommand cmd);


	void addOrderSendMessage(RentalOrder rentalBill);


	void cancelOrderSendMessage(RentalOrder rentalBill);


	void sendRentalSuccessSms(Integer namespaceId, String phoneNumber,
			RentalOrder order);


	QueryDefaultRuleAdminResponse getResourceRule(
			GetResourceRuleAdminCommand cmd);


	FindAutoAssignRentalSiteMonthStatusResponse findAutoAssignRentalSiteMonthStatus(
			FindAutoAssignRentalSiteMonthStatusCommand cmd);


	void addCheckOperator(AddCheckOperatorCommand cmd);


	void deleteCheckOperator(AddCheckOperatorCommand cmd);


	void onOrderSuccess(RentalOrder bill);


	void onOrderCancel(RentalOrder order);


	void updateRentalDate(UpdateRentalDateCommand cmd);


	void updateResourceAttachment(UpdateResourceAttachmentCommand cmd);


	void updateDefaultDateRule(UpdateDefaultDateRuleAdminCommand cmd);


	void updateDefaultAttachmentRule(UpdateDefaultAttachmentRuleAdminCommand cmd);


	void changeRentalOrderStatus(RentalOrder order, Byte status, Boolean cancelOtherOrderFlag);


	void sendMessageCode(Long uid, String locale, Map<String, String> map, int code);

	RentalSiteDTO findRentalSiteById(FindRentalSiteByIdCommand cmd);

}
