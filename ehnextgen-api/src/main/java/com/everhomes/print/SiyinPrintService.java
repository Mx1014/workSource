// @formatter:off
package com.everhomes.print;

import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.print.*;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;

public interface SiyinPrintService {
	GetPrintSettingResponse getPrintSetting(GetPrintSettingCommand cmd);

	void updatePrintSetting(UpdatePrintSettingCommand cmd);

	GetPrintStatResponse getPrintStat(GetPrintStatCommand cmd);

	ListPrintRecordsResponse listPrintRecords(ListPrintRecordsCommand cmd);

	ListPrintJobTypesResponse listPrintJobTypes(ListPrintJobTypesCommand cmd);

	ListPrintOrderStatusResponse listPrintOrderStatus(ListPrintOrderStatusCommand cmd);

	ListPrintUserOrganizationsResponse listPrintUserOrganizations(ListPrintUserOrganizationsCommand cmd);

	void updatePrintUserEmail(UpdatePrintUserEmailCommand cmd);

	GetPrintUserEmailResponse getPrintUserEmail(GetPrintUserEmailCommand cmd);

	GetPrintLogonUrlResponse getPrintLogonUrl(GetPrintLogonUrlCommand cmd);

	DeferredResult<RestResponse> logonPrint(String identifierToken);

	InformPrintResponse informPrint(InformPrintCommand cmd);

	ListPrintOrdersResponse listPrintOrders(ListPrintOrdersCommand cmd);

	GetPrintUnpaidOrderResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd);

	CommonOrderDTO payPrintOrder(PayPrintOrderCommand cmd);

	PreOrderDTO payPrintOrderV2(PayPrintOrderCommandV2 cmd);

	ListPrintingJobsResponse listPrintingJobs(ListPrintingJobsCommand cmd);

	void unlockPrinter(UnlockPrinterCommand cmd);

	void jobLogNotification(String jobData);
}
