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
import com.everhomes.rest.techpark.punch.TimeCompareFlag;
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
	  * 测试过滤条件：时间段，正常，异常，工作时间大于，工作时间小于，上班时间大于，上班时间小于 ，下班时间大于，上班时间小于，综合搜索
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
		cmd = new ListPunchDetailsCommand();
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
		cmd = new ListPunchDetailsCommand();
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
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertEquals(ExceptionStatus.EXCEPTION.getCode(), dayLog.getExceptionStatus().byteValue());
		} 

		//工作时长大于7小时
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setWorkTimeCompareFlag(TimeCompareFlag.GREATEROREQUAL.getCode());
		cmd.setWorkTime(7*60*60*1000L);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getWorkTime()>=cmd.getWorkTime());
		} 

		//工作时长小于7小时
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setWorkTimeCompareFlag(TimeCompareFlag.LESSOREQUAL.getCode());
		cmd.setWorkTime(7*60*60*1000L);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime()); 
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getWorkTime()<=cmd.getWorkTime());
		} 

		//上班时间超过9点
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setArriveTimeCompareFlag(TimeCompareFlag.GREATEROREQUAL.getCode());
		cmd.setArriveTime(9*60*60*1000L);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getArriveTime()>=cmd.getArriveTime());
		} 


		//上班时间不到9点
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setArriveTimeCompareFlag(TimeCompareFlag.LESSOREQUAL.getCode());
		cmd.setArriveTime(9*60*60*1000L);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getArriveTime()<=cmd.getArriveTime());
		} 
		


		//下班时间超过19点
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setLeaveTimeCompareFlag(TimeCompareFlag.GREATEROREQUAL.getCode());
		cmd.setLeaveTime(19*60*60*1000L);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getLeaveTime()>=cmd.getLeaveTime());
		} 

		//下班时间不到19点
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setLeaveTimeCompareFlag(TimeCompareFlag.LESSOREQUAL.getCode());
		cmd.setLeaveTime(19*60*60*1000L);
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getLeaveTime()<=cmd.getLeaveTime());
		} 

		//综合搜索
		cmd = new ListPunchDetailsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setArriveTimeCompareFlag(TimeCompareFlag.GREATEROREQUAL.getCode());
		cmd.setArriveTime(9*60*60*1000L);
		cmd.setLeaveTimeCompareFlag(TimeCompareFlag.LESSOREQUAL.getCode());
		cmd.setLeaveTime(19*60*60*1000L);
		cmd.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
		try {
			cmd.setStartDay(dateSF.parse("2016-08-01").getTime());
			cmd.setEndDay(dateSF.parse("2016-08-15").getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		response = httpClientService.restGet(commandRelativeUri, cmd,
				ListPunchDetailsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		for (PunchDayDetailDTO dayLog : response.getResponse().getPunchDayDetails()) {
			assertNotNull(dayLog);
			assertNotNull(dayLog.getUserName());
			assertNotNull(dayLog.getDeptName());
			assertTrue(dayLog.getLeaveTime()<=cmd.getLeaveTime());
			assertTrue(dayLog.getArriveTime()>=cmd.getArriveTime());
			assertEquals(ExceptionStatus.NORMAL.getCode(), cmd.getExceptionStatus().byteValue());
		} 
	} 
}
