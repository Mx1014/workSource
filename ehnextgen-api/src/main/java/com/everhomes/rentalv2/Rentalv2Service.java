package com.everhomes.rentalv2;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;

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
	void updateResourceOrder(@Valid UpdateResourceOrderAdminCommand cmd);
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

	CommonOrderDTO getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd);


	FindRentalSiteYearStatusCommandResponse findRentalSiteYearStatus(FindRentalSiteYearStatusCommand cmd);


	FindAutoAssignRentalSiteYearStatusResponse findAutoAssignRentalSiteYearStatus(
			FindAutoAssignRentalSiteYearStatusCommand cmd);
}
