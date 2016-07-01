package com.everhomes.test.junit.rental;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.techpark.rental.AddRentalBillCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillRestResponse;
import com.everhomes.rest.techpark.rental.AddRentalItemBillRestResponse;
import com.everhomes.rest.techpark.rental.AttachmentDTO;
import com.everhomes.rest.techpark.rental.AttachmentType;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteDayStatusCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteDayStatusRestResponse;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteWeekStatusRestResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsRestResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusRestResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesRestResponse;
import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.techpark.rental.RentalSiteDayRulesDTO;
import com.everhomes.rest.techpark.rental.RentalSiteNumberDayRulesDTO;
import com.everhomes.rest.techpark.rental.RentalSiteNumberRuleDTO;
import com.everhomes.rest.techpark.rental.RentalSiteStatus;
import com.everhomes.rest.techpark.rental.SiteBillStatus;
import com.everhomes.rest.techpark.rental.SiteItemDTO;
import com.everhomes.rest.techpark.rental.rentalBillRuleDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalAppTest extends BaseLoginAuthTestCase {

	Integer namespaceId = 0;
	String userIdentifier = "10001";
	String plainTexPassword = "123456";
	
	private Long launchPadItemId = 510L;
	private String ownerType = RentalOwnerType.COMMUNITY.getCode();
	private Long ownerId = 419L;
	private Long organizationId = 1L;
	private Long hourSitenumberSiteId = 2L;
	private Long hourSiteId = 3L;
	private int[] weekdays={0,1,2,3,4,5,6,7};
	@Before
	public void setUp() {
		// super.setUp();
		truncateRentalTable();
		initSrouceData();

		// 清除缓存
		clearRedisCache();

		// 同步索引
		syncSequence();

	}

	private void truncateRentalTable() {

		String serverInitfilePath = "data/tables/rental2.0_truncate_tables.sql";
		dbProvider.runClassPathSqlFile(serverInitfilePath);

	}
	//查询接口放到一个test里，一面删数据加数据浪费时间
//	@Test
	public void testFindAPI(){
		testFindRentalSites();
		testFindRentalSiteWeekStatus();
		testFindAutoAssignRentalSiteDayStatus();
	}
	
	public void testFindRentalSites() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findRentalSites";

		FindRentalSitesCommand cmd = new FindRentalSitesCommand();
		cmd.setLaunchPadItemId(launchPadItemId);
		cmd.setOwnerType(ownerType);
		cmd.setOwnerId(ownerId);

		FindRentalSitesRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, FindRentalSitesRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 2號3號資源可以搜出，4號資源的launchpaditemid不對，5號資源的ownerId不對
		assertEquals(2, response.getResponse().getRentalSites().size());
		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	}

	
	public void testFindRentalSiteWeekStatus() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findRentalSiteWeekStatus";

		FindRentalSiteWeekStatusCommand cmd = new FindRentalSiteWeekStatusCommand();
		cmd.setSiteId(hourSiteId);
		cmd.setRuleDate(1467511200000L);

		FindRentalSiteWeekStatusRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,
						FindRentalSiteWeekStatusRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 搜3号资源-按小时预定的，不含场所号的
		assertEquals(hourSiteId.intValue(), response.getResponse().getRentalSiteId().intValue());
		assertEquals(2, response.getResponse().getAttachments().size());
		assertEquals(3, response.getResponse().getSitePics().size());
		assertEquals(7, response.getResponse().getSiteDays().size());
		//这个接口不给item
//		assertEquals(2, response.getResponse().getSiteItems().size());
		for(RentalSiteDayRulesDTO dayRules : response.getResponse().getSiteDays()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(dayRules.getRentalDate()));
			if(Arrays.asList(weekdays).contains(calendar.get(Calendar.DAY_OF_WEEK)) )
				assertEquals(4, response.getResponse().getSiteDays().get(0).getSiteRules().size());
		}		
	}

	public void testFindAutoAssignRentalSiteWeekStatus() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findAutoAssignRentalSiteWeekStatus";

		FindAutoAssignRentalSiteWeekStatusCommand cmd = new FindAutoAssignRentalSiteWeekStatusCommand();
		cmd.setSiteId(hourSitenumberSiteId);
		cmd.setRuleDate(1467511200000L);

		FindAutoAssignRentalSiteWeekStatusRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,
						FindAutoAssignRentalSiteWeekStatusRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 搜3号资源-按小时预定的，不含场所号的
		assertEquals(hourSitenumberSiteId.intValue(), response.getResponse().getRentalSiteId().intValue());
		assertEquals(2, response.getResponse().getAttachments().size());
		assertEquals(3, response.getResponse().getSitePics().size());
		assertEquals(7, response.getResponse().getSiteDays().size());
		//这个接口不给item
//		assertEquals(2, response.getResponse().getSiteItems().size());
		for(RentalSiteNumberDayRulesDTO dayRules : response.getResponse().getSiteDays()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(dayRules.getRentalDate()));
			if(Arrays.asList(weekdays).contains(calendar.get(Calendar.DAY_OF_WEEK)) ){
				//每天10个场所
					assertEquals(10, dayRules.getSiteNumbers().size());
					for(RentalSiteNumberRuleDTO siteNumberRules : dayRules.getSiteNumbers()){
						//每个场所4个rule
						assertEquals(4, siteNumberRules.getSiteRules().size());
					}
			}
			
		}		
	}
	

	public void testFindAutoAssignRentalSiteDayStatus() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findAutoAssignRentalSiteDayStatus";

		FindAutoAssignRentalSiteDayStatusCommand cmd = new FindAutoAssignRentalSiteDayStatusCommand();
		cmd.setSiteId(hourSitenumberSiteId);
		cmd.setRuleDate(1467511200000L);

		FindAutoAssignRentalSiteDayStatusRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,
						FindAutoAssignRentalSiteDayStatusRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 搜3号资源-按小时预定的，不含场所号的
		assertEquals(hourSitenumberSiteId.intValue(), response.getResponse().getRentalSiteId().intValue());
		assertEquals(2, response.getResponse().getAttachments().size());
		assertEquals(3, response.getResponse().getSitePics().size());
		assertEquals(10, response.getResponse().getSiteNumbers().size());
		//这个接口不给item
//		assertEquals(2, response.getResponse().getSiteItems().size());
		 
		for(RentalSiteNumberRuleDTO siteNumberRules : response.getResponse().getSiteNumbers()){
			//每个场所4个rule
			assertEquals(4, siteNumberRules.getSiteRules().size());
		}
			 
	}
	
	//正常订单
	@Test
	public void testAddRentalBill (){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findAutoAssignRentalSiteDayStatus";

		FindAutoAssignRentalSiteDayStatusCommand cmd1 = new FindAutoAssignRentalSiteDayStatusCommand();
		cmd1.setSiteId(hourSitenumberSiteId);
		
		//取两天之后的单元格 保证不会超时
		cmd1.setRuleDate(System.currentTimeMillis()+86400000*2);

		FindAutoAssignRentalSiteDayStatusRestResponse response1 = httpClientService
				.restGet(commandRelativeUri, cmd1,
						FindAutoAssignRentalSiteDayStatusRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response1);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response1),
				httpClientService.isReponseSuccess(response1));
		
		commandRelativeUri = "/rental/addRentalBill";

		AddRentalBillCommand cmd = new AddRentalBillCommand();
		cmd.setRentalDate(response1.getResponse().getSiteNumbers().get(0).getSiteRules().get(0).getRuleDate());
		cmd.setRentalSiteId(cmd1.getSiteId());
		cmd.setRules(new ArrayList<rentalBillRuleDTO>());
		rentalBillRuleDTO dto = new rentalBillRuleDTO();
		dto.setRuleId(response1.getResponse().getSiteNumbers().get(0).getSiteRules().get(0).getId());
		dto.setRentalCount(1.0);
		cmd.getRules().add(dto);
		AddRentalBillRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,AddRentalBillRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		commandRelativeUri = "/rental/findRentalSiteItems";
		FindRentalSiteItemsCommand itemCmd = new FindRentalSiteItemsCommand();
		itemCmd.setRentalSiteId(hourSitenumberSiteId);
		itemCmd.setRentalSiteRuleIds(new ArrayList<Long>());
		itemCmd.getRentalSiteRuleIds().add(dto.getRuleId());
		FindRentalSiteItemsRestResponse itemResponse = httpClientService
				.restGet(commandRelativeUri, itemCmd,FindRentalSiteItemsRestResponse.class, context);


		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		
		commandRelativeUri = "/rental/addRentalItemBill";
		AddRentalBillItemCommand itemBillCmd = new AddRentalBillItemCommand(); 
		itemBillCmd.setRentalAttachments(new ArrayList<AttachmentDTO>());
		AttachmentDTO attach = new AttachmentDTO();
		attach.setAttachmentType(AttachmentType.SHOW_CONTENT.getCode());
		attach.setContent("显示内容是：我真帅");
		itemBillCmd.getRentalAttachments().add(attach);
		attach = new AttachmentDTO();
		attach.setAttachmentType(AttachmentType.LICENSE_NUMBER.getCode());
		attach.setContent("粤A39342");
		itemBillCmd.getRentalAttachments().add(attach);
		
		itemBillCmd.setRentalBillId(response.getResponse().getRentalBillId());
		itemBillCmd.setRentalItems(new ArrayList<SiteItemDTO>());
		SiteItemDTO itemDTO = new SiteItemDTO();
		itemDTO.setCounts(2);
		itemDTO.setId(2L);
		itemBillCmd.getRentalItems().add(itemDTO);
		itemDTO = new SiteItemDTO();
		itemDTO.setCounts(70);
		itemDTO.setId(1L);
		itemBillCmd.getRentalItems().add(itemDTO); 
		AddRentalItemBillRestResponse itemBillResponse = httpClientService
				.restGet(commandRelativeUri, itemBillCmd,AddRentalItemBillRestResponse.class, context);


		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalBills> resultBill = new ArrayList<EhRentalBills>();
		dslContext
				.select()
				.from(Tables.EH_RENTAL_BILLS)
				.where(Tables.EH_RENTAL_BILLS.ID.eq(response.getResponse().getRentalBillId()))
				.fetch()
				.map((r) -> {
					resultBill.add(ConvertHelper.convert(r,
							EhRentalBills.class));
					return null;
				});
		assertEquals(SiteBillStatus.PAYINGFINAL.getCode(), resultBill.get(0).getStatus().byteValue());
		try {
			Thread.sleep(1000*60*15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<EhRentalBills> resultBill2 = new ArrayList<EhRentalBills>();
		dslContext
				.select()
				.from(Tables.EH_RENTAL_BILLS)
				.where(Tables.EH_RENTAL_BILLS.ID.eq(response.getResponse().getRentalBillId()))
				.fetch()
				.map((r) -> {
					resultBill2.add(ConvertHelper.convert(r,
							EhRentalBills.class));
					return null;
				});
		assertEquals(SiteBillStatus.FAIL.getCode(), resultBill2.get(0).getStatus().byteValue());
	}
	
	//预定时间过早的订单
	
//	@Test
	public void testAddEarlyRentalBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
//
//		String commandRelativeUri = "/rental/findAutoAssignRentalSiteDayStatus";
//
//		FindAutoAssignRentalSiteDayStatusCommand cmd1 = new FindAutoAssignRentalSiteDayStatusCommand();
//		cmd1.setSiteId(hourSitenumberSiteId);
//		
//		//取两天之后的单元格 保证不会超时
//		cmd1.setRuleDate(System.currentTimeMillis()+86400000*12);
//
//		FindAutoAssignRentalSiteDayStatusRestResponse response1 = httpClientService
//				.restGet(commandRelativeUri, cmd1,
//						FindAutoAssignRentalSiteDayStatusRestResponse.class, context);
//
//		assertNotNull("The reponse of may not be null", response1);
//		assertTrue("The user scenes should be get from server, response="
//				+ StringHelper.toJsonString(response1),
//				httpClientService.isReponseSuccess(response1));
		
		String commandRelativeUri = "/rental/addRentalBill";

		AddRentalBillCommand cmd = new AddRentalBillCommand();
		cmd.setRentalDate(1471428000000L);
		cmd.setRentalSiteId(hourSitenumberSiteId);
		cmd.setRules(new ArrayList<rentalBillRuleDTO>());
		rentalBillRuleDTO dto = new rentalBillRuleDTO();
		dto.setRuleId(2451L);
		dto.setRentalCount(1.0);
		cmd.getRules().add(dto);
		AddRentalBillRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,AddRentalBillRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				response.getErrorCode().equals(RentalServiceErrorCode.ERROR_RESERVE_TOO_EARLY));
		
	}
	
	//预定时间过晚的订单


//	@Test
	public void testAddLateRentalBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findAutoAssignRentalSiteDayStatus";

		FindAutoAssignRentalSiteDayStatusCommand cmd1 = new FindAutoAssignRentalSiteDayStatusCommand();
		cmd1.setSiteId(hourSitenumberSiteId);
		
		//取当天的单元格 保证超时
		cmd1.setRuleDate(System.currentTimeMillis());

		FindAutoAssignRentalSiteDayStatusRestResponse response1 = httpClientService
				.restGet(commandRelativeUri, cmd1,
						FindAutoAssignRentalSiteDayStatusRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response1);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response1),
				httpClientService.isReponseSuccess(response1));
		
		commandRelativeUri = "/rental/addRentalBill";

		AddRentalBillCommand cmd = new AddRentalBillCommand();
		cmd.setRentalDate(response1.getResponse().getSiteNumbers().get(0).getSiteRules().get(0).getRuleDate());
		cmd.setRentalSiteId(cmd1.getSiteId());
		cmd.setRules(new ArrayList<rentalBillRuleDTO>());
		rentalBillRuleDTO dto = new rentalBillRuleDTO();
		dto.setRuleId(response1.getResponse().getSiteNumbers().get(0).getSiteRules().get(0).getId());
		dto.setRentalCount(1.0);
		cmd.getRules().add(dto);
		AddRentalBillRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,AddRentalBillRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				response.getErrorCode().equals(RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE));
		 


		 
		
		
	} 
	//预定资源不够的订单
	

//	@Test
	public void testAddTooMuchRentalBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findAutoAssignRentalSiteDayStatus";

		FindAutoAssignRentalSiteDayStatusCommand cmd1 = new FindAutoAssignRentalSiteDayStatusCommand();
		cmd1.setSiteId(hourSitenumberSiteId);
		
		//取两天之后的单元格 保证不会超时
		cmd1.setRuleDate(System.currentTimeMillis()+86400000*2);

		FindAutoAssignRentalSiteDayStatusRestResponse response1 = httpClientService
				.restGet(commandRelativeUri, cmd1,
						FindAutoAssignRentalSiteDayStatusRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response1);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response1),
				httpClientService.isReponseSuccess(response1));
		
		commandRelativeUri = "/rental/addRentalBill";

		AddRentalBillCommand cmd = new AddRentalBillCommand();
		cmd.setRentalDate(response1.getResponse().getSiteNumbers().get(0).getSiteRules().get(0).getRuleDate());
		cmd.setRentalSiteId(cmd1.getSiteId());
		cmd.setRules(new ArrayList<rentalBillRuleDTO>());
		rentalBillRuleDTO dto = new rentalBillRuleDTO();
		dto.setRuleId(response1.getResponse().getSiteNumbers().get(0).getSiteRules().get(0).getId());
		//超过10个
		dto.setRentalCount(12.0);
		cmd.getRules().add(dto);
		AddRentalBillRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,AddRentalBillRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				response.getErrorCode().equals(RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES));
		
	}
	
	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}

	protected void initSrouceData() {
		String sourceInfoFilePath = "data/json/rental2.0-test-data-resource-160627.txt";
		String filePath = dbProvider
				.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		sourceInfoFilePath = "data/json/rental2.0-test-data-addbill-rules-160630.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		sourceInfoFilePath = "data/json/rental2.0-test-data-items-160627.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}

}
