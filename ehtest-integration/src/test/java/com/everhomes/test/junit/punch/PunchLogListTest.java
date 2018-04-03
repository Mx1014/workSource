package com.everhomes.test.junit.punch;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.ListMonthPunchLogsCommand;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.techpark.punch.PunchClockCommand;
import com.everhomes.rest.techpark.punch.PunchListMonthPunchLogsRestResponse;
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Items;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class PunchLogListTest extends BaseLoginAuthTestCase {
	Integer namespaceId = 0;
	String userIdentifier = "10001";
	String userIdentifier2 = "10002";
	String plainTexPassword = "123456";
	String ownerType = PunchOwnerType.ORGANIZATION.getCode();
	Long ownerId = 100600L;
	
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

		String userInfoFilePath = "data/json/2.0.0-punch-data-userinfo_160830.txt";
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

		jsonPath = "data/json/2.0.0-punch-test-data-logs-160810.txt";
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
	public void listMonthPunchLogsTest() {
		logon(null, userIdentifier, plainTexPassword);

		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		String commandRelativeUri = "/techpark/punch/listMonthPunchLogs";

		ListMonthPunchLogsCommand cmd = new ListMonthPunchLogsCommand();
		cmd.setEnterpriseId(ownerId);
		// 查8月
		cmd.setQueryTime(1470672000000L);
		PunchListMonthPunchLogsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd,
				PunchListMonthPunchLogsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		for (PunchLogsDay dayLog : response.getResponse().getPunchLogsMonthList().get(0).getPunchLogsDayList()) {

			// 8月10 上午缺勤 下午忘打卡
			if (dayLog.getPunchDay().equals("10")) {
				assertEquals(PunchStatus.UNPUNCH, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.UNPUNCH, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
				assertEquals(PunchStatus.FORGOT, PunchStatus.fromCode(dayLog.getAfternoonPunchStatusNew()));
			}
			// 8月9 上午早退 下午迟到
			else if (dayLog.getPunchDay().equals("9")) {
				assertEquals(PunchStatus.LEAVEEARLY, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.BELATE, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月8 上午迟到 下午正常
			if (dayLog.getPunchDay().equals("8")) {
				assertEquals(PunchStatus.BELATE, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月5 加班 加班要减去午休时间
			if (dayLog.getPunchDay().equals("5")) {
				assertEquals(PunchStatus.OVERTIME, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.OVERTIME, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
				assertEquals((8*60*60+2)*1000, dayLog.getWorkTime().intValue());
			}
			// 8月4 上午 早退且迟到 下午 早退且迟到
			if (dayLog.getPunchDay().equals("4")) {
				assertEquals(PunchStatus.BLANDLE, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.BLANDLE, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月3 正常 正常
			if (dayLog.getPunchDay().equals("3")) {
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月2 正常 早退
			if (dayLog.getPunchDay().equals("2")) {
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.LEAVEEARLY, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
		}
		List<EhPunchDayLogs> result1 =new ArrayList<EhPunchDayLogs>();
		DSLContext dslContext = dbProvider.getDslContext();
		try {
			dslContext.select()
					.from(Tables.EH_PUNCH_DAY_LOGS)
					.where(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(ownerId))
					.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(Long.valueOf(userIdentifier)))
					.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(new java.sql.Date(dateSF.parse("2016-8-10").getTime())))
					.fetch()
					.map((r) -> {
						result1.add(ConvertHelper.convert(r,
								EhPunchDayLogs.class));
						return null;
					});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(NormalFlag.NO.getCode(), result1.get(0).getDeviceChangeFlag().byteValue()); 
		List<EhPunchDayLogs> result2 =new ArrayList<EhPunchDayLogs>(); 
		try {
			dslContext.select()
					.from(Tables.EH_PUNCH_DAY_LOGS)
					.where(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(ownerId))
					.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(Long.valueOf(userIdentifier)))
					.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(new java.sql.Date(dateSF.parse("2016-8-9").getTime())))
					.fetch()
					.map((r) -> {
						result2.add(ConvertHelper.convert(r,
								EhPunchDayLogs.class));
						return null;
					});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(NormalFlag.YES.getCode(), result2.get(0).getDeviceChangeFlag().byteValue());
		listMonthPunchLogsTest2();
	}
	 /**测试点:
	  * 1.没有workday的规则默认是周一到周五
	  * 2.给个人设置规则映射
	  * */
	public void listMonthPunchLogsTest2() {
		logon(null, userIdentifier2, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/listMonthPunchLogs";

		ListMonthPunchLogsCommand cmd = new ListMonthPunchLogsCommand();
		cmd.setEnterpriseId(ownerId);
		// 查8月
		cmd.setQueryTime(1470672000000L);
		PunchListMonthPunchLogsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd,
				PunchListMonthPunchLogsRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		for (PunchLogsDay dayLog : response.getResponse().getPunchLogsMonthList().get(0).getPunchLogsDayList()) {

			// 8月10 上午缺勤 下午忘打卡
			if (dayLog.getPunchDay().equals("10")) {
				assertEquals(PunchStatus.UNPUNCH, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.UNPUNCH, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
				assertEquals(PunchStatus.FORGOT, PunchStatus.fromCode(dayLog.getAfternoonPunchStatusNew()));
			}
			// 8月9 上午早退 下午迟到
			else if (dayLog.getPunchDay().equals("9")) {
				assertEquals(PunchStatus.LEAVEEARLY, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.BELATE, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月8 上午迟到 下午正常
			if (dayLog.getPunchDay().equals("8")) {
				assertEquals(PunchStatus.BELATE, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月5 加班 加班
			if (dayLog.getPunchDay().equals("5")) {
				assertEquals(PunchStatus.UNPUNCH, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.FORGOT, PunchStatus.fromCode(dayLog.getMorningPunchStatusNew()));
				assertEquals(PunchStatus.UNPUNCH, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
				assertEquals(PunchStatus.FORGOT, PunchStatus.fromCode(dayLog.getAfternoonPunchStatusNew()));
			}
			// 8月4 上午 早退且迟到 下午 早退且迟到
			if (dayLog.getPunchDay().equals("4")) {
				assertEquals(PunchStatus.BLANDLE, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.BLANDLE, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月3 正常 正常
			if (dayLog.getPunchDay().equals("3")) {
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
			// 8月2 正常 早退
			if (dayLog.getPunchDay().equals("2")) {
				assertEquals(PunchStatus.NORMAL, PunchStatus.fromCode(dayLog.getMorningPunchStatus()));
				assertEquals(PunchStatus.LEAVEEARLY, PunchStatus.fromCode(dayLog.getAfternoonPunchStatus()));
			}
		}

	}
}
