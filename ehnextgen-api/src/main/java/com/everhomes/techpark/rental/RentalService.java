package com.everhomes.techpark.rental;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.techpark.rental.AddItemAdminCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommandResponse;
import com.everhomes.rest.techpark.rental.AddRentalSiteCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommandResponse;
import com.everhomes.rest.techpark.rental.BatchIncompleteBillCommand;
import com.everhomes.rest.techpark.rental.CancelRentalBillCommand;
import com.everhomes.rest.techpark.rental.CompleteBillCommand;
import com.everhomes.rest.techpark.rental.ConfirmBillCommand;
import com.everhomes.rest.techpark.rental.DeleteItemAdminCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalBillCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.DisableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.EnableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteDayStatusCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteDayStatusResponse;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteWeekStatusResponse;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsAndAttachmentsCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsAndAttachmentsResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteMonthStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteMonthStatusCommandResponse;
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
import com.everhomes.rest.techpark.rental.IncompleteBillCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommandResponse;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommand;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommandResponse;
import com.everhomes.rest.techpark.rental.RentalBillDTO;
import com.everhomes.rest.techpark.rental.UpdateItemAdminCommand;
import com.everhomes.rest.techpark.rental.UpdateRentalRuleCommand;
import com.everhomes.rest.techpark.rental.UpdateRentalSiteCommand;
import com.everhomes.rest.techpark.rental.VerifyRentalBillCommandResponse;
import com.everhomes.rest.techpark.rental.GetItemListAdminCommand;
import com.everhomes.rest.techpark.rental.GetItemListCommandResponse;
import com.everhomes.rest.techpark.rental.rentalBillRuleDTO;
import com.everhomes.rest.techpark.rental.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.CloseResourceTypeCommand;
import com.everhomes.rest.techpark.rental.admin.CreateResourceTypeCommand;
import com.everhomes.rest.techpark.rental.admin.DefaultRuleDTO;
import com.everhomes.rest.techpark.rental.admin.DeleteResourceTypeCommand;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListCommand;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListResponse;
import com.everhomes.rest.techpark.rental.admin.GetRefundUrlCommand;
import com.everhomes.rest.techpark.rental.admin.GetRentalBillCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminResponse;
import com.everhomes.rest.techpark.rental.admin.GetResourceTypeListCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceTypeListResponse;
import com.everhomes.rest.techpark.rental.admin.OpenResourceTypeCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.rest.techpark.rental.admin.UpdateDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateItemsAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateRentalSiteDiscountAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateResourceTypeCommand;

public interface RentalService { 
	void updateRentalRule(UpdateRentalRuleCommand cmd);

	Long addRentalSite(AddRentalSiteCommand cmd);

	void addItem(AddItemAdminCommand cmd);
 

//	FindRentalSitesStatusCommandResponse findRentalSiteDayStatus(
//			FindRentalSitesStatusCommand cmd);

	GetRentalSiteTypeResponse findRentalSiteTypes();

	FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItems(
			FindRentalSiteItemsAndAttachmentsCommand cmd);

	FindRentalSitesCommandResponse findRentalSites(FindRentalSitesCommand cmd);

	FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd);

	RentalBillDTO addRentalBill(AddRentalBillCommand cmd);

	FindRentalBillsCommandResponse findRentalBills(FindRentalBillsCommand cmd);

	GetRentalTypeRuleCommandResponse getRentalTypeRule(
			GetRentalTypeRuleCommand cmd);

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
	void mappingRentalBillDTO(RentalBillDTO dto, RentalBill bill);
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
	void valiRentalBill(List<rentalBillRuleDTO> ruleDTOs);
	void valiRentalBill(Double rentalcount, List<rentalBillRuleDTO> ruleDTOs);

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

}
