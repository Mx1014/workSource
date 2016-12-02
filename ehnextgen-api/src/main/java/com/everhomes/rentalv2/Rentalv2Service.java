package com.everhomes.rentalv2;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.rentalv2.AddItemAdminCommand;
import com.everhomes.rest.rentalv2.AddRentalBillCommand;
import com.everhomes.rest.rentalv2.AddRentalBillItemCommand;
import com.everhomes.rest.rentalv2.AddRentalBillItemCommandResponse;
import com.everhomes.rest.rentalv2.AddRentalSiteCommand;
import com.everhomes.rest.rentalv2.BatchCompleteBillCommand;
import com.everhomes.rest.rentalv2.BatchCompleteBillCommandResponse;
import com.everhomes.rest.rentalv2.BatchIncompleteBillCommand;
import com.everhomes.rest.rentalv2.CancelRentalBillCommand;
import com.everhomes.rest.rentalv2.CompleteBillCommand;
import com.everhomes.rest.rentalv2.ConfirmBillCommand;
import com.everhomes.rest.rentalv2.DeleteItemAdminCommand;
import com.everhomes.rest.rentalv2.DeleteRentalBillCommand;
import com.everhomes.rest.rentalv2.DeleteRentalSiteCommand;
import com.everhomes.rest.rentalv2.DeleteRentalSiteRulesCommand;
import com.everhomes.rest.rentalv2.DisableRentalSiteCommand;
import com.everhomes.rest.rentalv2.EnableRentalSiteCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteDayStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteDayStatusResponse;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteMonthStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteMonthStatusResponse;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteWeekStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteWeekStatusResponse;
import com.everhomes.rest.rentalv2.FindRentalBillsCommand;
import com.everhomes.rest.rentalv2.FindRentalBillsCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteItemsAndAttachmentsCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteItemsAndAttachmentsResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteMonthStatusCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteMonthStatusCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteRulesCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteRulesCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteWeekStatusCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSitesCommand;
import com.everhomes.rest.rentalv2.FindRentalSitesCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSitesStatusCommand;
import com.everhomes.rest.rentalv2.FindRentalSitesStatusCommandResponse;
import com.everhomes.rest.rentalv2.GetItemListAdminCommand;
import com.everhomes.rest.rentalv2.GetItemListCommandResponse;
import com.everhomes.rest.rentalv2.GetRentalSiteTypeResponse;
import com.everhomes.rest.rentalv2.GetRentalTypeRuleCommand;
import com.everhomes.rest.rentalv2.GetRentalTypeRuleCommandResponse;
import com.everhomes.rest.rentalv2.IncompleteBillCommand;
import com.everhomes.rest.rentalv2.ListRentalBillCountCommand;
import com.everhomes.rest.rentalv2.ListRentalBillCountCommandResponse;
import com.everhomes.rest.rentalv2.ListRentalBillsCommand;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.rentalv2.OnlinePayCallbackCommand;
import com.everhomes.rest.rentalv2.OnlinePayCallbackCommandResponse;
import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.UpdateItemAdminCommand;
import com.everhomes.rest.rentalv2.UpdateRentalRuleCommand;
import com.everhomes.rest.rentalv2.UpdateRentalSiteCommand;
import com.everhomes.rest.rentalv2.VerifyRentalBillCommandResponse;
import com.everhomes.rest.rentalv2.RentalBillRuleDTO;
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
import com.everhomes.rest.rentalv2.admin.UpdateDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateItemsAdminCommand;
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

}
