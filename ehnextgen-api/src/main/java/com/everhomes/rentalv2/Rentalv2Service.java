package com.everhomes.rentalv2;

import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.rentalv2.AddItemAdminCommand;
import com.everhomes.rest.rentalv2.AddRentalBillCommand;
import com.everhomes.rest.rentalv2.AddRentalBillItemCommand;
import com.everhomes.rest.rentalv2.AddRentalBillItemCommandResponse;
import com.everhomes.rest.rentalv2.AddRentalBillItemV2Response;
import com.everhomes.rest.rentalv2.AddRentalOrderUsingInfoCommand;
import com.everhomes.rest.rentalv2.AddRentalOrderUsingInfoResponse;
import com.everhomes.rest.rentalv2.AddRentalOrderUsingInfoV2Response;
import com.everhomes.rest.rentalv2.CancelRentalBillCommand;
import com.everhomes.rest.rentalv2.ChangeRentalBillPayInfoCommand;
import com.everhomes.rest.rentalv2.CompleteRentalOrderCommand;
import com.everhomes.rest.rentalv2.DeleteItemAdminCommand;
import com.everhomes.rest.rentalv2.DeleteRentalBillCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteDayStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteDayStatusResponse;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteMonthStatusByWeekCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteMonthStatusByWeekResponse;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteMonthStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteMonthStatusResponse;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteWeekStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteWeekStatusResponse;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteYearStatusCommand;
import com.everhomes.rest.rentalv2.FindAutoAssignRentalSiteYearStatusResponse;
import com.everhomes.rest.rentalv2.FindRentalBillsCommand;
import com.everhomes.rest.rentalv2.FindRentalBillsCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteByIdCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteItemsAndAttachmentsCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteItemsAndAttachmentsResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteMonthStatusByWeekCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteMonthStatusByWeekCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteMonthStatusCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteMonthStatusCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteWeekStatusCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteYearStatusCommand;
import com.everhomes.rest.rentalv2.FindRentalSiteYearStatusCommandResponse;
import com.everhomes.rest.rentalv2.FindRentalSitesCommand;
import com.everhomes.rest.rentalv2.FindRentalSitesCommandResponse;
import com.everhomes.rest.rentalv2.GetCancelOrderTipCommand;
import com.everhomes.rest.rentalv2.GetCancelOrderTipResponse;
import com.everhomes.rest.rentalv2.GetItemListAdminCommand;
import com.everhomes.rest.rentalv2.GetItemListCommandResponse;
import com.everhomes.rest.rentalv2.GetRenewRentalOrderInfoCommand;
import com.everhomes.rest.rentalv2.GetRenewRentalOrderInfoResponse;
import com.everhomes.rest.rentalv2.GetRentalBillPayInfoCommand;
import com.everhomes.rest.rentalv2.GetRentalOrderDetailCommand;
import com.everhomes.rest.rentalv2.GetResourceRuleV2Command;
import com.everhomes.rest.rentalv2.GetResourceRuleV2Response;
import com.everhomes.rest.rentalv2.ListRentalBillsCommand;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.rentalv2.ListRentalOrdersCommand;
import com.everhomes.rest.rentalv2.ListRentalOrdersResponse;
import com.everhomes.rest.rentalv2.OnlinePayCallbackCommand;
import com.everhomes.rest.rentalv2.OnlinePayCallbackCommandResponse;
import com.everhomes.rest.rentalv2.RenewRentalOrderCommand;
import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.RentalBillRuleDTO;
import com.everhomes.rest.rentalv2.RentalOrderDTO;
import com.everhomes.rest.rentalv2.RentalSiteDTO;
import com.everhomes.rest.rentalv2.UpdateItemAdminCommand;
import com.everhomes.rest.rentalv2.admin.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Rentalv2Service { 

	void addItem(AddItemAdminCommand cmd);

	FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItems(
			FindRentalSiteItemsAndAttachmentsCommand cmd);

	FindRentalSitesCommandResponse findRentalSites(FindRentalSitesCommand cmd);

	RentalBillDTO addRentalBill(AddRentalBillCommand cmd);

	FindRentalBillsCommandResponse findRentalBills(FindRentalBillsCommand cmd);

	void addRentalSiteSimpleRules(AddRentalSiteRulesAdminCommand cmd);

	void changeRentalBillPayInfo(ChangeRentalBillPayInfoCommand cmd);

	void cancelRentalBill(CancelRentalBillCommand cmd,boolean ifAbsoredFlow);

	void deleteRentalSiteItem(DeleteItemAdminCommand cmd);

	GetItemListCommandResponse listRentalSiteItems(
			GetItemListAdminCommand cmd);

	AddRentalBillItemCommandResponse addRentalItemBill(AddRentalBillItemCommand cmd);

	AddRentalBillItemV2Response addRentalItemBillV2(AddRentalBillItemCommand cmd);

	ListRentalBillsCommandResponse listRentalBills(ListRentalBillsCommand cmd);

	ListRentalBillsCommandResponse listRentalBillsByOrdId(ListRentalBillsByOrdIdCommand cmd);

	ListRentalBillsCommandResponse listActiveRentalBills(ListRentalBillsCommand cmd);

	void deleteRentalBill(DeleteRentalBillCommand cmd);
 
	OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd);

	FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(
			FindRentalSiteWeekStatusCommand cmd);

//	RentalBillDTO completeBill(CompleteBillCommand cmd);
//
//	RentalBillDTO incompleteBill(IncompleteBillCommand cmd);

//	BatchCompleteBillCommandResponse batchIncompleteBill(
//			BatchIncompleteBillCommand cmd);
//
//	BatchCompleteBillCommandResponse batchCompleteBill(
//			BatchCompleteBillCommand cmd);

	void exportRentalBills(ListRentalBillsCommand cmd,HttpServletResponse response);
	void exportRentalBills(SearchRentalOrdersCommand cmd,HttpServletResponse response);
	void mappingRentalBillDTO(RentalBillDTO dto, RentalOrder bill, RentalResource rs);
	void convertRentalOrderDTO(RentalOrderDTO dto, RentalOrder bill);
	void addRule(AddDefaultRuleAdminCommand cmd);
	QueryDefaultRuleAdminResponse queryDefaultRule(QueryDefaultRuleAdminCommand cmd);
	GetResourceListAdminResponse getResourceList(GetResourceListAdminCommand cmd);
	void addResource(AddResourceAdminCommand cmd);
	void updateResource(UpdateResourceAdminCommand cmd);
	void updateResourceStatus(UpdateResourceAdminCommand cmd);
	void updateResourceOrder(UpdateResourceOrderAdminCommand cmd);
	void updateItem(UpdateItemAdminCommand cmd);
	void updateItems(UpdateItemsAdminCommand cmd);
	FindAutoAssignRentalSiteWeekStatusResponse findAutoAssignRentalSiteWeekStatus(
			FindAutoAssignRentalSiteWeekStatusCommand cmd);
	FindAutoAssignRentalSiteDayStatusResponse findAutoAssignRentalSiteDayStatus(
			FindAutoAssignRentalSiteDayStatusCommand cmd);
	void updateRentalSiteCellRule(UpdateRentalSiteCellRuleAdminCommand cmd);
	void validateRentalBill(List<RentalBillRuleDTO> ruleDTOs, RentalResource rs,
							RentalDefaultRule rule);

	void updateDefaultRule(UpdateDefaultRuleAdminCommand cmd);

	GetRefundOrderListResponse getRefundOrderList(GetRefundOrderListCommand cmd);

	RentalBillDTO getRentalBill(GetRentalBillCommand cmd);

	String getRefundUrl(GetRefundUrlCommand cmd);

	GetResourceTypeListResponse getResourceTypeList(
			GetResourceTypeListCommand cmd);

	ResourceTypeDTO getResourceType(
			GetResourceTypeCommand cmd);

//	void createResourceType(CreateResourceTypeCommand cmd);
//
//	void deleteResourceType(DeleteResourceTypeCommand cmd);
//
//	void updateResourceType(UpdateResourceTypeCommand cmd);
//
//	void closeResourceType(CloseResourceTypeCommand cmd);
//
//	void openResourceType(OpenResourceTypeCommand cmd);

	FindRentalSiteMonthStatusCommandResponse findRentalSiteMonthStatus(
			FindRentalSiteMonthStatusCommand cmd);

	FindRentalSiteMonthStatusByWeekCommandResponse findRentalSiteMonthStatusByWeek(
			FindRentalSiteMonthStatusByWeekCommand cmd);


	void deleteResource(DeleteResourceCommand cmd);


	void addOrderSendMessage(RentalOrder rentalBill);


	void cancelOrderSendMessage(RentalOrder rentalBill);


//	void sendRentalSuccessSms(Integer namespaceId, String phoneNumber,
//			RentalOrder order);


	QueryDefaultRuleAdminResponse getResourceRule(
			GetResourceRuleAdminCommand cmd);


	FindAutoAssignRentalSiteMonthStatusResponse findAutoAssignRentalSiteMonthStatus(
			FindAutoAssignRentalSiteMonthStatusCommand cmd);

	FindAutoAssignRentalSiteMonthStatusByWeekResponse findAutoAssignRentalSiteMonthStatusByWeek(
			FindAutoAssignRentalSiteMonthStatusByWeekCommand cmd);


//	void addCheckOperator(AddCheckOperatorCommand cmd);
//
//
//	void deleteCheckOperator(AddCheckOperatorCommand cmd);


	void onOrderSuccess(RentalOrder bill);


	void onOrderCancel(RentalOrder order,boolean ifAbsordFlow);


	void updateRentalDate(UpdateRentalDateCommand cmd);


	void updateResourceAttachment(UpdateResourceAttachmentCommand cmd);


	void updateDefaultDateRule(UpdateDefaultDateRuleAdminCommand cmd);


	void updateDefaultAttachmentRule(UpdateDefaultAttachmentRuleAdminCommand cmd);


	void changeRentalOrderStatus(RentalOrder order, Byte status, Boolean cancelOtherOrderFlag);


	void sendMessageCode(Long uid, String locale, Map<String, String> map, int code);

	RentalSiteDTO findRentalSiteById(FindRentalSiteByIdCommand cmd);

	CommonOrderDTO getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd);

	PreOrderDTO getRentalBillPayInfoV2(GetRentalBillPayInfoCommand cmd);

	FindRentalSiteYearStatusCommandResponse findRentalSiteYearStatus(FindRentalSiteYearStatusCommand cmd);


	FindAutoAssignRentalSiteYearStatusResponse findAutoAssignRentalSiteYearStatus(
			FindAutoAssignRentalSiteYearStatusCommand cmd);

	void rentalSchedule();

	void updateResourceTimeRule(UpdateResourceTimeRuleCommand cmd);

	ResourceTimeRuleDTO getResourceTimeRule(GetResourceTimeRuleCommand cmd);

	void updateResourcePriceRule(UpdateResourcePriceRuleCommand cmd);

	ResourcePriceRuleDTO getResourcePriceRule(GetResourcePriceRuleCommand cmd);

	void updateResourceRentalRule(UpdateResourceRentalRuleCommand cmd);

	ResourceRentalRuleDTO getResourceRentalRule(GetResourceRentalRuleCommand cmd);

	void updateResourceOrderRule(UpdateResourceOrderRuleCommand cmd);

	ResourceOrderRuleDTO getResourceOrderRule(GetResourceOrderRuleCommand cmd);

	ResourceAttachmentDTO getResourceAttachment(GetResourceAttachmentCommand cmd);

	ResourceSiteNumbersDTO getResourceSiteNumbers(GetResourceSiteNumbersCommand cmd);

	void updateResourceSiteNumbers(UpdateResourceSiteNumbersCommand cmd);

	void confirmRefund(ConfirmRefundCommand cmd);

	AddRentalOrderUsingInfoResponse addRentalOrderUsingInfo(AddRentalOrderUsingInfoCommand cmd);

	AddRentalOrderUsingInfoV2Response addRentalOrderUsingInfoV2(AddRentalOrderUsingInfoCommand cmd);

	ListRentalOrdersResponse listRentalOrders(ListRentalOrdersCommand cmd);

	RentalOrderDTO getRentalOrderDetail(GetRentalOrderDetailCommand cmd);

	SearchRentalOrdersResponse searchRentalOrders(SearchRentalOrdersCommand cmd);

	RentalOrderDTO getRentalOrderById(GetRentalBillCommand cmd);

	GetRenewRentalOrderInfoResponse getRenewRentalOrderInfo(GetRenewRentalOrderInfoCommand cmd);

	CommonOrderDTO renewRentalOrder(RenewRentalOrderCommand cmd);

	PreOrderDTO renewRentalOrderV2(RenewRentalOrderCommand cmd);

	RentalOrderDTO completeRentalOrder(CompleteRentalOrderCommand cmd);

	GetResourceRuleV2Response getResourceRuleV2(GetResourceRuleV2Command cmd);

	GetCancelOrderTipResponse getCancelOrderTip(GetCancelOrderTipCommand cmd);

	QueryRentalStatisticsResponse queryRentalStatistics(QueryRentalStatisticsCommand cmd);

	QueryOrgRentalStatisticsResponse queryOrgRentalStatistics (QueryRentalStatisticsCommand cmd);

	void renewOrderSuccess(RentalOrder rentalBill,Double rentalCount);


	void test();

}
