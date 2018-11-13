package com.everhomes.rentalv2;

import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.rentalv2.*;
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

	void cancelRentalBill(CancelRentalBillCommand cmd);

	void deleteRentalSiteItem(DeleteItemAdminCommand cmd);

	GetItemListCommandResponse listRentalSiteItems(
			GetItemListAdminCommand cmd);

	AddRentalBillItemCommandResponse addRentalItemBill(AddRentalBillItemCommand cmd);

	AddRentalBillItemV2Response addRentalItemBillV2(AddRentalBillItemCommand cmd);

	AddRentalBillItemV3Response addRentalItemBillV3(AddRentalBillItemCommand cmd);

	ListRentalBillsCommandResponse listRentalBills(ListRentalBillsCommand cmd);

	ListRentalBillsCommandResponse listRentalBillsByOrdId(ListRentalBillsByOrdIdCommand cmd);

	ListRentalBillsCommandResponse listActiveRentalBills(ListRentalBillsCommand cmd);

	void deleteRentalBill(DeleteRentalBillCommand cmd);

	OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd);

	FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(
			FindRentalSiteWeekStatusCommand cmd);


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
							RentalDefaultRule rule,Byte rentalType);

	void updateDefaultRule(UpdateDefaultRuleAdminCommand cmd);

	GetRefundOrderListResponse getRefundOrderList(GetRefundOrderListCommand cmd);

	RentalBillDTO getRentalBill(GetRentalBillCommand cmd);

	String getRefundUrl(GetRefundUrlCommand cmd);

	GetResourceTypeListResponse getResourceTypeList(
			GetResourceTypeListCommand cmd);

	ResourceTypeDTO getResourceType(
			GetResourceTypeCommand cmd);



	FindRentalSiteMonthStatusCommandResponse findRentalSiteMonthStatus(
			FindRentalSiteMonthStatusCommand cmd);

	FindRentalSiteMonthStatusByWeekCommandResponse findRentalSiteMonthStatusByWeek(
			FindRentalSiteMonthStatusByWeekCommand cmd);


	void deleteResource(DeleteResourceCommand cmd);


	void addOrderSendMessage(RentalOrder rentalBill);


	void cancelOrderSendMessage(RentalOrder rentalBill);



	QueryDefaultRuleAdminResponse getResourceRule(
			GetResourceRuleAdminCommand cmd);


	FindAutoAssignRentalSiteMonthStatusResponse findAutoAssignRentalSiteMonthStatus(
			FindAutoAssignRentalSiteMonthStatusCommand cmd);

	FindAutoAssignRentalSiteMonthStatusByWeekResponse findAutoAssignRentalSiteMonthStatusByWeek(
			FindAutoAssignRentalSiteMonthStatusByWeekCommand cmd);



	void onOrderSuccess(RentalOrder bill);


	void onOrderCancel(RentalOrder order);


	void updateRentalDate(UpdateRentalDateCommand cmd);


	void updateResourceAttachment(UpdateResourceAttachmentCommand cmd);


	void updateDefaultDateRule(UpdateDefaultDateRuleAdminCommand cmd);


	void updateDefaultAttachmentRule(UpdateDefaultAttachmentRuleAdminCommand cmd);


	void changeRentalOrderStatus(RentalOrder order, Byte status, Boolean cancelOtherOrderFlag);

	RentalSiteDTO findRentalSiteById(FindRentalSiteByIdCommand cmd);

	CommonOrderDTO getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd);

	PreOrderDTO getRentalBillPayInfoV2(GetRentalBillPayInfoCommand cmd);

	AddRentalBillItemV3Response getRentalBillPayInfoV3(GetRentalBillPayInfoCommand cmd);

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

	List<Long> getHolidayCloseDates(GetHolidayCloseDatesCommand cmd);

	void renewOrderSuccess(RentalOrder rentalBill,Double rentalCount);

	GetResourceUsingInfoResponse getResourceUsingInfo(FindRentalSiteByIdCommand cmd);

	String parseSceneToken(String sceneToken);

	void test();

}
