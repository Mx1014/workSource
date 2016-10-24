package com.everhomes.test.junit.punch;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleRestResponse;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.UpdateTargetPunchAllRuleCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class PunchRuleTest extends BaseLoginAuthTestCase {
	Integer namespaceId = 0;
	String userIdentifier = "10001";
	String plainTexPassword = "123456";
	String ownerType = PunchOwnerType.ORGANIZATION.getCode();
	Long ownerId = 100600L;

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

	}

	protected void initRuleData() {

		String jsonPath = "data/json/2.0.0-punch-test-data-addbill-base-rules-160810.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

	}

	protected void initRuleMapData() {

		String jsonPath = "data/json/2.0.0-punch-test-data-addbill-rules-160810.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
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

	@Test
	public void test() throws ParseException {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/updateTargetPunchAllRule";

		UpdateTargetPunchAllRuleCommand cmd = new UpdateTargetPunchAllRuleCommand();

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setTargetId(ownerId);
		cmd.setTargetType(ownerType);

		cmd.setTimeRule(new PunchTimeRuleDTO());
		cmd.getTimeRule().setName("上班9-10,中午11-14,下班18-19");
		cmd.getTimeRule().setPunchTimesPerDay((byte) 4);
		cmd.getTimeRule().setStartEarlyTime(9 * 3600 * 1000L);
		cmd.getTimeRule().setStartLateTime(10 * 3600 * 1000L);
		cmd.getTimeRule().setEndEarlyTime(18 * 3600 * 1000L);
		cmd.getTimeRule().setNoonLeaveTime(11 * 3600 * 1000L);
		cmd.getTimeRule().setAfternoonArriveTime(14 * 3600 * 1000L);
		cmd.getTimeRule().setDaySplitTime(3 * 3600 * 1000L);

		cmd.setWifiRule(new PunchWiFiRuleDTO());
		cmd.getWifiRule().setName("wifi");
		cmd.getWifiRule().setDescription("科技园·金融基地");
		cmd.getWifiRule().setWifis(new ArrayList<PunchWiFiDTO>());
		PunchWiFiDTO wifi = new PunchWiFiDTO();
		wifi.setMacAddress("mac-address-01");
		cmd.getWifiRule().getWifis().add(wifi);

		cmd.setWorkdayRule(new PunchWorkdayRuleDTO());
		cmd.getWorkdayRule().setName("排班时间");
		cmd.getWorkdayRule().setDescription("每周只上1天班");
		cmd.getWorkdayRule().setHolidays(new ArrayList<Long>());
		cmd.getWorkdayRule().getHolidays().add(dateSF.parse("2016-07-27").getTime());
		cmd.getWorkdayRule().setWorkdays(new ArrayList<Long>());
		cmd.getWorkdayRule().getWorkdays().add(dateSF.parse("2016-07-28").getTime());
		cmd.getWorkdayRule().setWorkWeekDates(new ArrayList<Integer>());
		cmd.getWorkdayRule().getWorkWeekDates().add(4);
		cmd.getWorkdayRule().getWorkWeekDates().add(5);

		cmd.setLocationRule(new PunchLocationRuleDTO());
		cmd.getLocationRule().setName("地点随便来吧");
		cmd.getLocationRule().setDescription("科技园·金融基地");
		cmd.getLocationRule().setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
		PunchGeoPointDTO point = new PunchGeoPointDTO();
		point.setDescription("科技园·金融基地-1栋(高新科技园科苑路6号)");
		point.setLatitude(22.549205);
		point.setLongitude(113.953529);
		point.setDistance(300.0);
		cmd.getLocationRule().getPunchGeoPoints().add(point);

		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// // 总共1个

		commandRelativeUri = "/punch/getTargetPunchAllRule";

		GetTargetPunchAllRuleCommand getCMD = new GetTargetPunchAllRuleCommand();
		getCMD.setOwnerType(this.ownerType);
		getCMD.setOwnerId(ownerId);
		getCMD.setTargetId(ownerId);
		getCMD.setTargetType(ownerType);

		GetTargetPunchAllRuleRestResponse getResp = httpClientService.restGet(commandRelativeUri, getCMD,
				GetTargetPunchAllRuleRestResponse.class, context);

		if (cmd.getLocationRule() != null) {
			if (null != cmd.getLocationRule().getPunchGeoPoints())
				assertEquals(cmd.getLocationRule().getPunchGeoPoints().size(), getResp.getResponse().getLocationRule()
						.getPunchGeoPoints().size());
		}
		if (cmd.getTimeRule() != null) {
			if (null != cmd.getTimeRule().getAfternoonArriveTime())
				assertEquals(cmd.getTimeRule().getAfternoonArriveTime().longValue(), getResp.getResponse().getTimeRule()
						.getAfternoonArriveTime().longValue());
			if (null != cmd.getTimeRule().getDaySplitTime())
				assertEquals(cmd.getTimeRule().getDaySplitTime().longValue(), getResp.getResponse().getTimeRule().getDaySplitTime()
						.longValue());
			if (null != cmd.getTimeRule().getEndEarlyTime())
				assertEquals(cmd.getTimeRule().getEndEarlyTime().longValue(), getResp.getResponse().getTimeRule().getEndEarlyTime()
						.longValue());
			if (null != cmd.getTimeRule().getNoonLeaveTime())
				assertEquals(cmd.getTimeRule().getNoonLeaveTime().longValue(), getResp.getResponse().getTimeRule().getNoonLeaveTime()
						.longValue());
			if (null != cmd.getTimeRule().getPunchTimesPerDay())
				assertEquals(cmd.getTimeRule().getPunchTimesPerDay().byteValue(), getResp.getResponse().getTimeRule()
						.getPunchTimesPerDay().byteValue());
			if (null != cmd.getTimeRule().getStartEarlyTime())
				assertEquals(cmd.getTimeRule().getStartEarlyTime().longValue(), getResp.getResponse().getTimeRule()
						.getStartEarlyTime().longValue());
			if (null != cmd.getTimeRule().getStartLateTime())
				assertEquals(cmd.getTimeRule().getStartLateTime().longValue(), getResp.getResponse().getTimeRule().getStartLateTime()
						.longValue());
		}
		if (cmd.getWifiRule() != null) {
			if (null != cmd.getWifiRule().getWifis())
				assertEquals(cmd.getWifiRule().getWifis().size(), getResp.getResponse().getWifiRule().getWifis().size());
		}
		if (cmd.getWorkdayRule() != null) {
			if (null != cmd.getWorkdayRule().getHolidays())
				assertEquals(cmd.getWorkdayRule().getHolidays().size(), getResp.getResponse().getWorkdayRule().getHolidays().size());
			if (null != cmd.getWorkdayRule().getWorkdays())
				assertEquals(cmd.getWorkdayRule().getWorkdays().size(), getResp.getResponse().getWorkdayRule().getWorkdays().size());
			if (null != cmd.getWorkdayRule().getWorkWeekDates())
				assertEquals(cmd.getWorkdayRule().getWorkWeekDates().size(), getResp.getResponse().getWorkdayRule().getWorkWeekDates()
						.size());
		}
		//DELETE
		commandRelativeUri = "/punch/deleteTargetPunchAllRule";

		response = httpClientService.restGet(commandRelativeUri, getCMD, RestResponse.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		//DELETE之后 再查就没了
		commandRelativeUri = "/punch/getTargetPunchAllRule";
		getResp = httpClientService.restGet(commandRelativeUri, getCMD, GetTargetPunchAllRuleRestResponse.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertNull(getResp.getResponse());
	}

}
