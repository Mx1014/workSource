package com.everhomes.test.junit.rental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusRestResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesRestResponse;
import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.RentalSiteDayRulesDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
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
	private int[] weekdays={1,2,3,4};
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

	@Test
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

	@Test
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
		assertEquals(2, response.getResponse().getSiteItems().size());
		for(RentalSiteDayRulesDTO dayRules : response.getResponse().getSiteDays()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(dayRules.getRentalDate()));
			if(Arrays.asList(weekdays).contains(calendar.get(Calendar.DAY_OF_WEEK)) )
				assertEquals(4, response.getResponse().getSiteDays().get(0).getSiteRules().size());
		}		
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

		sourceInfoFilePath = "data/json/rental2.0-test-data-siterules-hour-160628.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		sourceInfoFilePath = "data/json/rental2.0-test-data-items-160627.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}

}
