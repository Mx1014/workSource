// @formatter:off
package com.everhomes.print;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.print.*;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

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

	UnlockPrinterResponse unlockPrinter(UnlockPrinterCommand cmd);

	void jobLogNotification(String jobData);

	ListQueueJobsResponse listQueueJobs(ListQueueJobsCommand cmd);

	void releaseQueueJobs(ReleaseQueueJobsCommand cmd);

	void deleteQueueJobs(DeleteQueueJobsCommand cmd);

	void mfpLogNotification(String jobData, HttpServletResponse response);

	void getPrintQrcode(HttpServletRequest req, HttpServletResponse rps);

    List<ListBizPayeeAccountDTO> listPayeeAccount(ListPayeeAccountCommand cmd);

	void createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd);

	BusinessPayeeAccountDTO getBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd);

	void mfpLogNotificationV2(MfpLogNotificationV2Command cmd, HttpServletResponse response);

    void notifySiyinprintOrderPaymentV2(MerchantPaymentNotificationCommand cmd);

    void initPayeeAccount(MultipartFile[] files);

	String getSiyinServerUrl();

	void updatePrintOrder(SiyinPrintOrder order, String payOrderNo);

	PayPrintGeneralOrderResponse payPrintGeneralOrder(PayPrintGeneralOrderCommand cmd);

	GetPrintOrdersResponse getPrintOrder(GetPrintOrdersCommand cmd);
}
