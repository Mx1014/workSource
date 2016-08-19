package com.everhomes.test.junit.punch;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.ExceptionStatus;
import com.everhomes.rest.techpark.punch.ListMonthPunchLogsCommand;
import com.everhomes.rest.techpark.punch.PunchClockCommand;
import com.everhomes.rest.techpark.punch.PunchListMonthPunchLogsRestResponse;
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsRestResponse;
import com.everhomes.rest.techpark.punch.admin.PunchDayDetailDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class PunchListDetailsTest extends BaseLoginAuthTestCase {
	Integer namespaceId = 0;
	String userIdentifier = "10001";
	String userIdentifier2 = "10002";
	String plainTexPassword = "123456";
	String ownerType = PunchOwnerType.ORGANIZATION.getCode();
	Long ownerId = 178945L;

	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	@Before
	public void setUp() {
		super.setUp();

	}

	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}

	protected void initCustomData() {

		String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		String jsonPath = "data/json/2.0.0-punch-test-data-addbill-base-rules-160810.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		jsonPath = "data/json/2.0.0-punch-test-data-addbill-rules-160810.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		jsonPath = "data/json/2.0.0-punch-test-data-addbill-rules-map-and-organization-160810.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		jsonPath = "data/json/2.0.0-punch-test-data-list-punch-logs-160818.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}

	private final Long MILLISECONDGMT = 8 * 3600 * 1000L;

	private Long convertTimeToGMTMillisecond(Time time) {
		if (null != time) {
			// 从8点开始计算
			return time.getTime() + MILLISECONDGMT;
		}
		return null;
	}

	 /**测试点:
	  * 1.四次打卡的状态
	  * 2.给上层设置规则,下层部门没有规则自动找上层的规则
	  * */
	@Test
	public void listPunchDetailsTest() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchDetails";

		ListPunchDetailsCommand cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-12").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		ListPunchDetailsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertEquals(cmd.getEndDay().longValue(), response.getResponse().getPunchDayDetails().get(0).getPunchDate().longValue());
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertNotNull(dayLog.getExceptionStatus());
		} 
		//正常 

		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-12").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertEquals(cmd.getEndDay().longValue(), response.getResponse().getPunchDayDetails().get(0).getPunchDate().longValue());
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertEquals(ExceptionStatus.NORMAL.getCode(), dayLog.getExceptionStatus().byteValue());
		} 
		
		//异常
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-12").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertEquals(cmd.getEndDay().longValue(), response.getResponse().getPunchDayDetails().get(0).getPunchDate().longValue());
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertEquals(ExceptionStatus.EXCEPTION.getCode(), dayLog.getExceptionStatus().byteValue());
		} 
		
	} 
}
