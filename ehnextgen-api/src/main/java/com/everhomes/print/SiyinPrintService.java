// @formatter:off
package com.everhomes.print;

import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.print.GetPrintLogonUrlCommand;
import com.everhomes.rest.print.GetPrintLogonUrlResponse;
import com.everhomes.rest.print.GetPrintSettingCommand;
import com.everhomes.rest.print.GetPrintSettingResponse;
import com.everhomes.rest.print.GetPrintStatCommand;
import com.everhomes.rest.print.GetPrintStatResponse;
import com.everhomes.rest.print.GetPrintUnpaidOrderCommand;
import com.everhomes.rest.print.GetPrintUnpaidOrderResponse;
import com.everhomes.rest.print.GetPrintUserEmailCommand;
import com.everhomes.rest.print.GetPrintUserEmailResponse;
import com.everhomes.rest.print.InformPrintCommand;
import com.everhomes.rest.print.InformPrintResponse;
import com.everhomes.rest.print.ListPrintJobTypesCommand;
import com.everhomes.rest.print.ListPrintJobTypesResponse;
import com.everhomes.rest.print.ListPrintOrderStatusCommand;
import com.everhomes.rest.print.ListPrintOrderStatusResponse;
import com.everhomes.rest.print.ListPrintOrdersCommand;
import com.everhomes.rest.print.ListPrintOrdersResponse;
import com.everhomes.rest.print.ListPrintRecordsCommand;
import com.everhomes.rest.print.ListPrintRecordsResponse;
import com.everhomes.rest.print.ListPrintUserOrganizationsCommand;
import com.everhomes.rest.print.ListPrintUserOrganizationsResponse;
import com.everhomes.rest.print.ListPrintingJobsCommand;
import com.everhomes.rest.print.ListPrintingJobsResponse;
import com.everhomes.rest.print.LogonPrintCommand;
import com.everhomes.rest.print.PayPrintOrderCommand;
import com.everhomes.rest.print.PrintImmediatelyCommand;
import com.everhomes.rest.print.UnlockPrinterCommand;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;

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

	DeferredResult<RestResponse> logonPrint(LogonPrintCommand cmd);

	InformPrintResponse informPrint(InformPrintCommand cmd);

	ListPrintOrdersResponse listPrintOrders(ListPrintOrdersCommand cmd);

	GetPrintUnpaidOrderResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd);

	CommonOrderDTO payPrintOrder(PayPrintOrderCommand cmd);

	ListPrintingJobsResponse listPrintingJobs(ListPrintingJobsCommand cmd);

	void unlockPrinter(UnlockPrinterCommand cmd);

	void jobLogNotification(String jobData);
}
