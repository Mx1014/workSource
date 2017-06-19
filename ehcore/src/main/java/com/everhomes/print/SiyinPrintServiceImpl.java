// @formatter:off
package com.everhomes.print;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.everhomes.rest.print.LogonPrintResponse;
import com.everhomes.rest.print.PayPrintOrderCommand;
import com.everhomes.rest.print.PayPrintOrderResponse;
import com.everhomes.rest.print.PrintImmediatelyCommand;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;

public class SiyinPrintServiceImpl implements SiyinPrintService {
	
	@Autowired
	private SiyinPrintEmailProvider siyinPrintEmailProvider;
	@Autowired
	private SiyinPrintOrderProvider siyinPrintOrderProvider;
	@Autowired
	private SiyinPrintPrinterProvider siyinPrintPrinterProvider;
	@Autowired
	private SiyinPrintRecordProvider siyinPrintRecordProvider;
	@Autowired
	private SiyinPrintSettingProvider siyinPrintSettingProvider;

	@Override
	public GetPrintSettingResponse getPrintSetting(GetPrintSettingCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		
		List<SiyinPrintSetting> printSettingList = siyinPrintSettingProvider.listSiyinPrintSettingByOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		return processPrintSettingResponse(printSettingList);
	}


	@Override
	public void updatePrintSetting(UpdatePrintSettingCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public GetPrintStatResponse getPrintStat(GetPrintStatCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintRecordsResponse listPrintRecords(ListPrintRecordsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintJobTypesResponse listPrintJobTypes(ListPrintJobTypesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintOrderStatusResponse listPrintOrderStatus(ListPrintOrderStatusCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintUserOrganizationsResponse listPrintUserOrganizations(ListPrintUserOrganizationsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePrintUserEmail(UpdatePrintUserEmailCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public GetPrintUserEmailResponse getPrintUserEmail(GetPrintUserEmailCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetPrintLogonUrlResponse getPrintLogonUrl(GetPrintLogonUrlCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogonPrintResponse logonPrint(LogonPrintCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InformPrintResponse informPrint(InformPrintCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printImmediately(PrintImmediatelyCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public ListPrintOrdersResponse listPrintOrders(ListPrintOrdersCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetPrintUnpaidOrderResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayPrintOrderResponse payPrintOrder(PayPrintOrderCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintingJobsResponse listPrintingJobs(ListPrintingJobsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkOwner(String ownerType, Long ownerId) {
		// TODO Auto-generated method stub
		
	}
	
	private GetPrintSettingResponse processPrintSettingResponse(List<SiyinPrintSetting> printSettingList) {
		// TODO Auto-generated method stub
		return null;
	}
}
