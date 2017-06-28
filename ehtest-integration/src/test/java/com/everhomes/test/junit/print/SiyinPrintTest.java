package com.everhomes.test.junit.print;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.print.GetPrintLogonUrlCommand;
import com.everhomes.rest.print.GetPrintLogonUrlRestResponse;
import com.everhomes.rest.print.GetPrintSettingCommand;
import com.everhomes.rest.print.GetPrintSettingRestResponse;
import com.everhomes.rest.print.GetPrintStatCommand;
import com.everhomes.rest.print.GetPrintStatRestResponse;
import com.everhomes.rest.print.GetPrintUnpaidOrderCommand;
import com.everhomes.rest.print.GetPrintUnpaidOrderRestResponse;
import com.everhomes.rest.print.GetPrintUserEmailCommand;
import com.everhomes.rest.print.GetPrintUserEmailRestResponse;
import com.everhomes.rest.print.InformPrintCommand;
import com.everhomes.rest.print.InformPrintRestResponse;
import com.everhomes.rest.print.ListPrintJobTypesCommand;
import com.everhomes.rest.print.ListPrintJobTypesRestResponse;
import com.everhomes.rest.print.ListPrintOrderStatusCommand;
import com.everhomes.rest.print.ListPrintOrderStatusRestResponse;
import com.everhomes.rest.print.ListPrintOrdersCommand;
import com.everhomes.rest.print.ListPrintOrdersRestResponse;
import com.everhomes.rest.print.ListPrintRecordsCommand;
import com.everhomes.rest.print.ListPrintRecordsRestResponse;
import com.everhomes.rest.print.ListPrintUserOrganizationsCommand;
import com.everhomes.rest.print.ListPrintUserOrganizationsRestResponse;
import com.everhomes.rest.print.ListPrintingJobsCommand;
import com.everhomes.rest.print.ListPrintingJobsRestResponse;
import com.everhomes.rest.print.LogonPrintCommand;
import com.everhomes.rest.print.PayPrintOrderCommand;
import com.everhomes.rest.print.PayPrintOrderRestResponse;
import com.everhomes.rest.print.PrintImmediatelyCommand;
import com.everhomes.rest.print.UnlockPrinterCommand;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li> : </li>
 * </ul>
 *
 *  @author:dengs 2017年6月28日
 */
public class SiyinPrintTest extends BaseLoginAuthTestCase {
	private static final String SIYINPRINT_GETPRINTSETTING = "/siyinprint/getPrintSetting";
	private static final String SIYINPRINT_UPDATEPRINTSETTING = "/siyinprint/updatePrintSetting";
	private static final String SIYINPRINT_GETPRINTSTAT = "/siyinprint/getPrintStat";
	private static final String SIYINPRINT_LISTPRINTRECORDS = "/siyinprint/listPrintRecords";
	private static final String SIYINPRINT_LISTPRINTJOBTYPES = "/siyinprint/listPrintJobTypes";
	private static final String SIYINPRINT_LISTPRINTORDERSTATUS = "/siyinprint/listPrintOrderStatus";
	private static final String SIYINPRINT_LISTPRINTUSERORGANIZATIONS = "/siyinprint/listPrintUserOrganizations";
	private static final String SIYINPRINT_UPDATEPRINTUSEREMAIL = "/siyinprint/updatePrintUserEmail";
	private static final String SIYINPRINT_GETPRINTUSEREMAIL = "/siyinprint/getPrintUserEmail";
	private static final String SIYINPRINT_GETPRINTLOGONURL = "/siyinprint/getPrintLogonUrl";
	private static final String SIYINPRINT_LOGONPRINT = "/siyinprint/logonPrint";
	private static final String SIYINPRINT_INFORMPRINT = "/siyinprint/informPrint";
	private static final String SIYINPRINT_PRINTIMMEDIATELY = "/siyinprint/printImmediately";
	private static final String SIYINPRINT_LISTPRINTORDERS = "/siyinprint/listPrintOrders";
	private static final String SIYINPRINT_GETPRINTUNPAIDORDER = "/siyinprint/getPrintUnpaidOrder";
	private static final String SIYINPRINT_PAYPRINTORDER = "/siyinprint/payPrintOrder";
	private static final String SIYINPRINT_LISTPRINTINGJOBS = "/siyinprint/listPrintingJobs";
	private static final String SIYINPRINT_UNLOCKPRINTER = "/siyinprint/unlockPrinter";
	private static final String SIYINPRINT_JOBLOGNOTIFICATION = "/siyinprint/jobLogNotification";
	
	Integer namespaceId = 0;
	String userIdentifier = "root";
	String plainTexPassword = "123456";
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@After
	public void tearDown() {
		logoff();
	}
	
	@Override
	protected void initCustomData() {
		String cardIssuerFilePath = "data/json/print.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
	}
	
	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}
	
	@Test
	public void testGetPrintSetting(){
		String url = SIYINPRINT_GETPRINTSETTING;
		logon();

		GetPrintSettingCommand cmd = new GetPrintSettingCommand();
		GetPrintSettingRestResponse response = httpClientService.restPost(url,cmd,GetPrintSettingRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testUpdatePrintSetting(){
		String url = SIYINPRINT_UPDATEPRINTSETTING;
		logon();

		UpdatePrintSettingCommand cmd = new UpdatePrintSettingCommand();
		StringRestResponse response = httpClientService.restPost(url,cmd,StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testGetPrintStat(){
		String url = SIYINPRINT_GETPRINTSTAT;
		logon();

		GetPrintStatCommand cmd = new GetPrintStatCommand();
		GetPrintStatRestResponse response = httpClientService.restPost(url,cmd,GetPrintStatRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testListPrintRecords(){
		String url = SIYINPRINT_LISTPRINTRECORDS;
		logon();

		ListPrintRecordsCommand cmd = new ListPrintRecordsCommand();
		ListPrintRecordsRestResponse response = httpClientService.restPost(url,cmd,ListPrintRecordsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testListPrintJobTypes(){
		String url = SIYINPRINT_LISTPRINTJOBTYPES;
		logon();

		ListPrintJobTypesCommand cmd = new ListPrintJobTypesCommand();
		ListPrintJobTypesRestResponse response = httpClientService.restPost(url,cmd,ListPrintJobTypesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testListPrintOrderStatus(){
		String url = SIYINPRINT_LISTPRINTORDERSTATUS;
		logon();

		ListPrintOrderStatusCommand cmd = new ListPrintOrderStatusCommand();
		ListPrintOrderStatusRestResponse response = httpClientService.restPost(url,cmd,ListPrintOrderStatusRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testListPrintUserOrganizations(){
		String url = SIYINPRINT_LISTPRINTUSERORGANIZATIONS;
		logon();

		ListPrintUserOrganizationsCommand cmd = new ListPrintUserOrganizationsCommand();
		ListPrintUserOrganizationsRestResponse response = httpClientService.restPost(url,cmd,ListPrintUserOrganizationsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testUpdatePrintUserEmail(){
		String url = SIYINPRINT_UPDATEPRINTUSEREMAIL;
		logon();

		UpdatePrintUserEmailCommand cmd = new UpdatePrintUserEmailCommand();
		StringRestResponse response = httpClientService.restPost(url,cmd,StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testGetPrintUserEmail(){
		String url = SIYINPRINT_GETPRINTUSEREMAIL;
		logon();

		GetPrintUserEmailCommand cmd = new GetPrintUserEmailCommand();
		GetPrintUserEmailRestResponse response = httpClientService.restPost(url,cmd,GetPrintUserEmailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testGetPrintLogonUrl(){
		String url = SIYINPRINT_GETPRINTLOGONURL;
		logon();

		GetPrintLogonUrlCommand cmd = new GetPrintLogonUrlCommand();
		GetPrintLogonUrlRestResponse response = httpClientService.restPost(url,cmd,GetPrintLogonUrlRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testLogonPrint(){
		String url = SIYINPRINT_LOGONPRINT;
		logon();

		LogonPrintCommand cmd = new LogonPrintCommand();
		StringRestResponse response = httpClientService.restPost(url,cmd,StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testInformPrint(){
		String url = SIYINPRINT_INFORMPRINT;
		logon();

		InformPrintCommand cmd = new InformPrintCommand();
		InformPrintRestResponse response = httpClientService.restPost(url,cmd,InformPrintRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testPrintImmediately(){
		String url = SIYINPRINT_PRINTIMMEDIATELY;
		logon();

		PrintImmediatelyCommand cmd = new PrintImmediatelyCommand();
		StringRestResponse response = httpClientService.restPost(url,cmd,StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testListPrintOrders(){
		String url = SIYINPRINT_LISTPRINTORDERS;
		logon();

		ListPrintOrdersCommand cmd = new ListPrintOrdersCommand();
		ListPrintOrdersRestResponse response = httpClientService.restPost(url,cmd,ListPrintOrdersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testGetPrintUnpaidOrder(){
		String url = SIYINPRINT_GETPRINTUNPAIDORDER;
		logon();

		GetPrintUnpaidOrderCommand cmd = new GetPrintUnpaidOrderCommand();
		GetPrintUnpaidOrderRestResponse response = httpClientService.restPost(url,cmd,GetPrintUnpaidOrderRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testPayPrintOrder(){
		String url = SIYINPRINT_PAYPRINTORDER;
		logon();

		PayPrintOrderCommand cmd = new PayPrintOrderCommand();
		PayPrintOrderRestResponse response = httpClientService.restPost(url,cmd,PayPrintOrderRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testListPrintingJobs(){
		String url = SIYINPRINT_LISTPRINTINGJOBS;
		logon();

		ListPrintingJobsCommand cmd = new ListPrintingJobsCommand();
		ListPrintingJobsRestResponse response = httpClientService.restPost(url,cmd,ListPrintingJobsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testUnlockPrinter(){
		String url = SIYINPRINT_UNLOCKPRINTER;
		logon();

		UnlockPrinterCommand cmd = new UnlockPrinterCommand();
		StringRestResponse response = httpClientService.restPost(url,cmd,StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	@Test
	public void testJobLogNotification(){
		String url = SIYINPRINT_JOBLOGNOTIFICATION;
		logon();

		StringRestResponse response = httpClientService.restPost(url,null,StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
}
