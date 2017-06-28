package com.everhomes.test.junit.print;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.everhomes.rest.print.PrintOrderDTO;
import com.everhomes.rest.print.PrintSettingColorTypeDTO;
import com.everhomes.rest.print.PrintSettingPaperSizePriceDTO;
import com.everhomes.rest.print.UnlockPrinterCommand;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.http.HttpUtils;
import com.everhomes.util.StringHelper;
/**
 * 
 * 并发测试方式:
 * 	1,启动测试用例 testPayPrintOrder 支付订单
 *  2，启动main函数不断生产订单
 *  3，观察统计日志，看看钱总和是不是 504 
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
	
	public static final String buffer_jobdata = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+"<data>"
			+"  <job>"
			+"    <job_id/>"
			+"    <job_status>FinishJob</job_status>"
			+"    <final_result>1</final_result>"
			+"    <group_name>___OAUTH___</group_name>"
			+"    <user_name>1-240111044331048623</user_name>"
			+"    <user_display_name />"
			+"    <client_ip />"
			+"    <client_name />"
			+"    <client_mac />"
			+"    <driver_name />"
			+"    <job_type/>"
			+"    <job_in_time>2017-06-22 15:27:04</job_in_time>"
			+"    <job_out_time>2017-06-22 15:27:13</job_out_time>"
			+"    <document_name>CUsersAdministratorDesktop打印测试文档.docx</document_name>"
			+"    <level_name>公开</level_name>"
			+"    <project_name />"
			+"    <printer_name>FX-ApeosPort-VI C3370</printer_name>"
			+"    <collate>0</collate>"
			+"    <paper_size/>"
			+"    <paper_height>0</paper_height>"
			+"    <paper_width>0</paper_width>"
			+"    <duplex>1</duplex>"
			+"    <copy_count>1</copy_count>"
			+"    <surface_count>2</surface_count>"
			+"    <color_surface_count/>"
			+"    <mono_surface_count/>"
			+"    <page_count>2</page_count>"
			+"    <color_page_count>0</color_page_count>"
			+"    <mono_page_count>2</mono_page_count>"
			+"    <total_cost>0.2</total_cost>"
			+"    <color_cost>0.0</color_cost>"
			+"    <mono_cost>0.2</mono_cost>"
			+"  </job>"
			+"</data>";
	
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
		cmd.setOwnerType("community");
		cmd.setOwnerId(1L);
		cmd.setHotline("12345");
		cmd.setColorTypeDTO(new PrintSettingColorTypeDTO());
		PrintSettingColorTypeDTO dto = new PrintSettingColorTypeDTO();
		dto.setBlackWhitePrice(new BigDecimal(1));
		dto.setColorPrice(new BigDecimal(1));
		PrintSettingPaperSizePriceDTO colorDtos = new PrintSettingPaperSizePriceDTO();
		cmd.setPaperSizePriceDTO(colorDtos);
		dto = new PrintSettingColorTypeDTO();
		dto.setBlackWhitePrice(new BigDecimal(1));
		dto.setColorPrice(new BigDecimal(1));
		colorDtos.setAthreePrice(dto);
		dto = new PrintSettingColorTypeDTO();
		dto.setBlackWhitePrice(new BigDecimal(1));
		dto.setColorPrice(new BigDecimal(1));
		colorDtos.setAfourPrice(dto);
		dto = new PrintSettingColorTypeDTO();
		dto.setBlackWhitePrice(new BigDecimal(1));
		dto.setColorPrice(new BigDecimal(1));
		colorDtos.setAfivePrice(dto);
		dto = new PrintSettingColorTypeDTO();
		dto.setBlackWhitePrice(new BigDecimal(1));
		dto.setColorPrice(new BigDecimal(1));
		colorDtos.setAsixPrice(dto);
		
		StringRestResponse response = httpClientService.restPost(url,cmd,StringRestResponse.class);
		assertNotNull(response);
	}
	public void testGetPrintStat(){
		String url = SIYINPRINT_GETPRINTSTAT;

		GetPrintStatCommand cmd = new GetPrintStatCommand();
		cmd.setOwnerId(240111044331048623L);
		cmd.setOwnerType("community");
		GetPrintStatRestResponse response = httpClientService.restPost(url,cmd,GetPrintStatRestResponse.class);
		System.err.println("getCopyStat:"+response.getResponse().getCopyStat());
		System.err.println("getScanStat:"+response.getResponse().getScanStat());
		System.err.println("getPrintStat:"+response.getResponse().getPrintStat());
		System.err.println("getAllStat:"+response.getResponse().getAllStat());
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
	public List<PrintOrderDTO> testListPrintOrders(){
		String url = SIYINPRINT_LISTPRINTORDERS;
		ListPrintOrdersCommand cmd = new ListPrintOrdersCommand();
		cmd.setOwnerId(240111044331048623L);
		cmd.setOwnerType("community");
		ListPrintOrdersRestResponse response = httpClientService.restPost(url,cmd,ListPrintOrdersRestResponse.class);
		List<PrintOrderDTO> list = response.getResponse().getPrintOrdersList();
		return list;
	}
	@Test
	public void testGetPrintUnpaidOrder(){
		String url = SIYINPRINT_GETPRINTUNPAIDORDER;
		logon();

		GetPrintUnpaidOrderCommand cmd = new GetPrintUnpaidOrderCommand();
		cmd.setOwnerId(240111044331048623L);
		cmd.setOwnerType("community");
		GetPrintUnpaidOrderRestResponse response = httpClientService.restPost(url,cmd,GetPrintUnpaidOrderRestResponse.class);
	}
	
	@Test
	public void testPayPrintOrder(){
		String url = SIYINPRINT_PAYPRINTORDER;
		logon();

		while(true){
			try {
				Thread.sleep(10000);
				List<PrintOrderDTO> dtos = testListPrintOrders();
				if(dtos != null)
					for (PrintOrderDTO printOrderDTO : dtos) {
						PayPrintOrderCommand cmd = new PayPrintOrderCommand();
						cmd.setOrderId(printOrderDTO.getId());
						PayPrintOrderRestResponse response = httpClientService.restPost(url,cmd,PayPrintOrderRestResponse.class);
					}
				testGetPrintStat();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
	
	public void testJobLogNotification(){
		String[] jobTypes = {"<job_type>PRINT</job_type>","<job_type>COPY</job_type>","<job_type>SCAN</job_type>"};
		String[] paperSizes = {"<paper_size>A3</paper_size>","<paper_size>A4</paper_size>","<paper_size>A5</paper_size>","<paper_size>A6</paper_size>"};
		
		for (int ix = 0; ix < 20; ix++) {
			for (int i = 0; i < paperSizes.length; i++) {
				for (int j = 0; j < jobTypes.length; j++) {
					testConcurrency(jobTypes[j], paperSizes[i]);
				}
			}
		}
		
	
	}
	
	public void testConcurrency(String jobType, String paperSize){
		String url = "http://10.1.110.46:8080/evh/"+SIYINPRINT_JOBLOGNOTIFICATION;
		String jobData = buffer_jobdata.toString();
		String jobid = UUID.randomUUID().toString();
		jobData = jobData.replace("<job_id/>", "<job_id>"+jobid+"</job_id>");
		jobData = jobData.replace("<job_type/>", jobType);
		jobData = jobData.replace("<paper_size/>", paperSize);
		jobData = jobData.replace("<color_surface_count/>", "<color_surface_count>10</color_surface_count>");
		jobData = jobData.replace("<mono_surface_count/>", "<mono_surface_count>11</mono_surface_count>");
		jobData = Base64.getEncoder().encodeToString(jobData.getBytes());
		Map<String,String> param  = new HashMap<>();
		param.put("jobData",jobData);
		try {
			String string = HttpUtils.post(url, param);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new SiyinPrintTest().testJobLogNotification();
	}
	
}
