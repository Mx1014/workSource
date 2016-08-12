package com.everhomes.test.junit.punch;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.rest.techpark.punch.PunchRuleMapDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchLocationRulesRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesCommonCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchTimeRulesRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFiRulesRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWorkdayRulesRestResponse;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.server.schema.tables.pojos.EhPunchHolidays;
import com.everhomes.server.schema.tables.pojos.EhPunchLocationRules;
import com.everhomes.server.schema.tables.pojos.EhPunchRuleOwnerMap;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchTimeRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWifiRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWifis;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkdayRules;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class PunchTimeRuleTest extends BaseLoginAuthTestCase{
	Integer namespaceId = 0;
	String userIdentifier = "10001";
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
	
    private final Long MILLISECONDGMT=8*3600*1000L;
	private Long convertTimeToGMTMillisecond(Time time) {
		if (null != time) {
			//从8点开始计算
			return time.getTime()+MILLISECONDGMT;
		}
		return null;
	}
	

	@Test
	public void defaultTest(){
		addTimeRuleTest();
		addLocationRuleTest();
		addWIFIRuleTest();
		addPunchWorkdayRuleTest();
		addRuleTest();
		addRuleMapTest();
	}

	public void addTimeRuleTest(){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/addPunchTimeRule";

		AddPunchTimeRuleCommand cmd = new AddPunchTimeRuleCommand();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("上班9-10,中午11-14,下班18-19");
		cmd.setPunchTimesPerDay((byte)4);
		cmd.setStartEarlyTime(9*3600*1000L);
		cmd.setStartLateTime(10*3600*1000L);
		cmd.setEndEarlyTime(18*3600*1000L);
		cmd.setNoonLeaveTime(11*3600*1000L);
		cmd.setAfternoonArriveTime(14*3600*1000L);
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchTimeRules> result = new ArrayList<EhPunchTimeRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_TIME_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchTimeRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchTimeRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType());
		assertEquals(cmd.getPunchTimesPerDay(), rule.getPunchTimesPerDay());
		assertEquals(cmd.getEndEarlyTime().longValue() , convertTimeToGMTMillisecond(rule.getStartEarlyTime())+convertTimeToGMTMillisecond(rule.getWorkTime()).longValue());
		assertEquals(cmd.getAfternoonArriveTime().longValue(), convertTimeToGMTMillisecond(rule.getAfternoonArriveTime()).longValue());
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		updateTimeRuleTest(rule.getId());
	}
	

	public void updateTimeRuleTest(Long id){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/updatePunchTimeRule";

		UpdatePunchTimeRuleCommand cmd = new UpdatePunchTimeRuleCommand();
		cmd.setId(id);

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("上班9-10,中午11-14,下班18-19");
		cmd.setPunchTimesPerDay((byte)4);
		cmd.setStartEarlyTime(11*3600*1000L);
		cmd.setStartLateTime(12*3600*1000L);
		cmd.setEndEarlyTime(20*3600*1000L);
		cmd.setNoonLeaveTime(13*3600*1000L);
		cmd.setAfternoonArriveTime(14*3600*1000L);
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchTimeRules> result = new ArrayList<EhPunchTimeRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_TIME_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchTimeRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchTimeRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType());
		assertEquals(cmd.getPunchTimesPerDay(), rule.getPunchTimesPerDay());
		assertEquals(cmd.getEndEarlyTime().longValue() , convertTimeToGMTMillisecond(rule.getStartEarlyTime())+convertTimeToGMTMillisecond(rule.getWorkTime()).longValue());
		assertEquals(cmd.getAfternoonArriveTime().longValue(), convertTimeToGMTMillisecond(rule.getAfternoonArriveTime()).longValue());
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		listTimeRuleTest(cmd);
	}
	
	public void listTimeRuleTest(UpdatePunchTimeRuleCommand cmd1){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchTimeRules";

		ListPunchRulesCommonCommand cmd = new ListPunchRulesCommonCommand(); 

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		ListPunchTimeRulesRestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, ListPunchTimeRulesRestResponse.class,
				context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getTimeRules().size());
		PunchTimeRuleDTO dto = response.getResponse().getTimeRules().get(0);

		assertEquals(cmd1.getName(), dto.getName()); 
		assertEquals(cmd1.getPunchTimesPerDay(), dto.getPunchTimesPerDay());
		assertEquals(cmd1.getEndEarlyTime().longValue() ,  dto.getEndEarlyTime().longValue());
		assertEquals(cmd1.getAfternoonArriveTime().longValue(), dto.getAfternoonArriveTime().longValue()); 
	}

	public void addLocationRuleTest(){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/addPunchLocationRule";

		PunchLocationRuleDTO cmd = new PunchLocationRuleDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("地点随便来吧");
		cmd.setDescription("科技园·金融基地");
		cmd.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
		PunchGeoPointDTO point = new PunchGeoPointDTO();
		point.setDescription("科技园·金融基地-1栋(高新科技园科苑路6号)");
		point.setLatitude(22.549205);
		point.setLongitude(113.953529);
		cmd.getPunchGeoPoints().add(point);
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchLocationRules> result = new ArrayList<EhPunchLocationRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_LOCATION_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchLocationRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchLocationRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 

		List<EhPunchGeopoints> result2 = new ArrayList<EhPunchGeopoints>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_GEOPOINTS)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhPunchGeopoints.class));
					return null;
				});
		assertEquals(1, result2.size());
		EhPunchGeopoints geo = result2.get(0); 
		assertEquals(cmd.getOwnerType(), geo.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), geo.getOwnerId()); 
		assertEquals(point.getLatitude(), geo.getLatitude());
		assertEquals(point.getLongitude(), geo.getLongitude());
		
		
		
		updatePunchLocationRuleTest(rule.getId());
	}
	

	public void updatePunchLocationRuleTest(Long id){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/updatePunchLocationRule";

		PunchLocationRuleDTO cmd = new PunchLocationRuleDTO();
		cmd.setId(id);
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("地点随便来吧2");
		cmd.setDescription("武汉大学深圳产学研大楼 ");
		cmd.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
		PunchGeoPointDTO point = new PunchGeoPointDTO();
		point.setDescription("武汉大学深圳产学研大楼(南山区科苑南路武汉大学A202)");
		point.setLatitude(22.536287);
		point.setLongitude(113.951336);
		cmd.getPunchGeoPoints().add(point);
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchLocationRules> result = new ArrayList<EhPunchLocationRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_LOCATION_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchLocationRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchLocationRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 

		List<EhPunchGeopoints> result2 = new ArrayList<EhPunchGeopoints>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_GEOPOINTS)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhPunchGeopoints.class));
					return null;
				});
		assertEquals(1, result2.size());
		EhPunchGeopoints geo = result2.get(0); 
		assertEquals(cmd.getOwnerType(), geo.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), geo.getOwnerId()); 
		assertEquals(point.getLatitude(), geo.getLatitude());
		assertEquals(point.getLongitude(), geo.getLongitude());
		
		listPunchLocationRulesTest(cmd);
		
	}
	
	public void listPunchLocationRulesTest(PunchLocationRuleDTO rule){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchLocationRules";

		ListPunchRulesCommonCommand cmd = new ListPunchRulesCommonCommand(); 

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		ListPunchLocationRulesRestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, ListPunchLocationRulesRestResponse.class,
				context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getLocationRules().size());
		PunchLocationRuleDTO dto = response.getResponse().getLocationRules().get(0);

		assertEquals(rule.getName(), dto.getName()); 
		PunchGeoPointDTO point = rule.getPunchGeoPoints().get(0);
		PunchGeoPointDTO geo = dto.getPunchGeoPoints().get(0);
		assertEquals(point.getLatitude(), geo.getLatitude());
		assertEquals(point.getLongitude(), geo.getLongitude()); 
	}
	

	public void addWIFIRuleTest(){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/addPunchWiFiRule";

		PunchWiFiRuleDTO cmd = new PunchWiFiRuleDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("wifi");
		cmd.setDescription("科技园·金融基地");
		cmd.setWifis(new ArrayList<PunchWiFiDTO>()); 
		PunchWiFiDTO wifi = new PunchWiFiDTO(); 
		wifi.setMacAddress("mac-address-01");
		cmd.getWifis().add(wifi);
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchWifiRules> result = new ArrayList<EhPunchWifiRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_WIFI_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchWifiRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchWifiRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 

		List<EhPunchWifis> result2 = new ArrayList<EhPunchWifis>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_WIFIS)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhPunchWifis.class));
					return null;
				});
		assertEquals(1, result2.size());
		EhPunchWifis db = result2.get(0); 
		assertEquals(cmd.getOwnerType(), db.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), db.getOwnerId()); 
		assertEquals(wifi.getMacAddress(), db.getMacAddress()); 
		
		
		
		updatePunchWifiRuleTest(rule.getId());
	}
	

	public void updatePunchWifiRuleTest(Long id){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/updatePunchWiFiRule";


		PunchWiFiRuleDTO cmd = new PunchWiFiRuleDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("wifi-01");
		cmd.setDescription("打卡wifi");
		cmd.setWifis(new ArrayList<PunchWiFiDTO>()); 
		PunchWiFiDTO wifi = new PunchWiFiDTO(); 
		wifi.setMacAddress("mac-address-03");
		cmd.getWifis().add(wifi);
		cmd.setId(id); 
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchWifiRules> result = new ArrayList<EhPunchWifiRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_WIFI_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchWifiRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchWifiRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 

		List<EhPunchWifis> result2 = new ArrayList<EhPunchWifis>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_WIFIS)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhPunchWifis.class));
					return null;
				});
		assertEquals(1, result2.size());
		EhPunchWifis db = result2.get(0); 
		assertEquals(cmd.getOwnerType(), db.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), db.getOwnerId()); 
		assertEquals(wifi.getMacAddress(), db.getMacAddress()); 
		
		
		listPunchWIFIRulesTest(cmd);
		
	}
	
	public void listPunchWIFIRulesTest(PunchWiFiRuleDTO rule){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchWiFiRules";

		ListPunchRulesCommonCommand cmd = new ListPunchRulesCommonCommand(); 

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		ListPunchWiFiRulesRestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, ListPunchWiFiRulesRestResponse.class,
				context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getWifiRules().size());
		PunchWiFiRuleDTO dto = response.getResponse().getWifiRules().get(0);

		assertEquals(rule.getName(), dto.getName()); 
		PunchWiFiDTO wifi = rule.getWifis().get(0);
		PunchWiFiDTO db = dto.getWifis().get(0);
		assertEquals(wifi.getMacAddress(), db.getMacAddress()); 
	}

	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	public void addPunchWorkdayRuleTest(){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/addPunchWorkdayRule";

		
		try {
			PunchWorkdayRuleDTO cmd = new PunchWorkdayRuleDTO();
			cmd.setOwnerType(this.ownerType);
			cmd.setOwnerId(ownerId);
			cmd.setName("排班时间");
			cmd.setDescription("每周只上1天班"); 
			cmd.setHolidays(new ArrayList<Long>());
		
			cmd.getHolidays().add(dateSF.parse("2016-07-25").getTime());
		
			cmd.setWorkdays(new ArrayList<Long>());
			cmd.getWorkdays().add(dateSF.parse("2016-07-26").getTime());
			cmd.setWorkWeekDates(new ArrayList<Integer>());
			cmd.getWorkWeekDates().add(0);
			cmd.getWorkWeekDates().add(2);
			
			RestResponse  response = httpClientService.restGet(
					commandRelativeUri, cmd, RestResponse.class,
					context);
	
			assertNotNull("The reponse of may not be null", response);
			assertTrue("The user scenes should be get from server, response="
					+ StringHelper.toJsonString(response),
					httpClientService.isReponseSuccess(response));
			
	
			DSLContext dslContext = dbProvider.getDslContext();
			List<EhPunchWorkdayRules> result = new ArrayList<EhPunchWorkdayRules>();
			dslContext
					.select()
					.from(Tables.EH_PUNCH_WORKDAY_RULES)
	//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
					.fetch()
					.map((r) -> {
						result.add(ConvertHelper.convert(r,
								EhPunchWorkdayRules.class));
						return null;
					});
			assertEquals(1, result.size());
			EhPunchWorkdayRules rule = result.get(0);
			assertEquals(cmd.getName(), rule.getName());
			assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
			assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
			assertEquals("0000101", rule.getWorkWeekDates()); 
	
			List<EhPunchHolidays> result2 = new ArrayList<EhPunchHolidays>();
			dslContext
					.select()
					.from(Tables.EH_PUNCH_HOLIDAYS)
					.where(Tables.EH_PUNCH_HOLIDAYS.STATUS.eq(DateStatus.HOLIDAY.getCode()))
	//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
					.fetch()
					.map((r) -> {
						result2.add(ConvertHelper.convert(r,
								EhPunchHolidays.class));
						return null;
					});
			assertEquals(1, result2.size());
			EhPunchHolidays db = result2.get(0); 
			assertEquals(cmd.getOwnerType(), db.getOwnerType()); 
			assertEquals(cmd.getOwnerId(), db.getOwnerId()); 
			assertEquals(dateSF.parse("2016-07-25").getTime(), db.getRuleDate().getTime()); 
			

			List<EhPunchHolidays> result3 = new ArrayList<EhPunchHolidays>();
			dslContext
					.select()
					.from(Tables.EH_PUNCH_HOLIDAYS)
					.where(Tables.EH_PUNCH_HOLIDAYS.STATUS.eq(DateStatus.WORKDAY.getCode()))
	//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
					.fetch()
					.map((r) -> {
						result3.add(ConvertHelper.convert(r,
								EhPunchHolidays.class));
						return null;
					});
			assertEquals(1, result3.size());
			EhPunchHolidays db2 = result3.get(0); 
			assertEquals(cmd.getOwnerType(), db2.getOwnerType()); 
			assertEquals(cmd.getOwnerId(), db2.getOwnerId()); 
			assertEquals(dateSF.parse("2016-07-26").getTime(), db2.getRuleDate().getTime()); 
			
			
			
			updatePunchWorkdayRuleTest(rule.getId());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void updatePunchWorkdayRuleTest(Long id) throws ParseException{
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/updatePunchWorkdayRule";

		PunchWorkdayRuleDTO cmd = new PunchWorkdayRuleDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("排班时间");
		cmd.setDescription("每周只上1天班"); 
		cmd.setHolidays(new ArrayList<Long>());
	
		cmd.getHolidays().add(dateSF.parse("2016-07-27").getTime());
	
		cmd.setWorkdays(new ArrayList<Long>());
		cmd.getWorkdays().add(dateSF.parse("2016-07-28").getTime());
		cmd.setWorkWeekDates(new ArrayList<Integer>());
		cmd.getWorkWeekDates().add(4);
		cmd.getWorkWeekDates().add(5);
		
		cmd.setId(id); 
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchWorkdayRules> result = new ArrayList<EhPunchWorkdayRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_WORKDAY_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchWorkdayRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchWorkdayRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		assertEquals("0110000", rule.getWorkWeekDates()); 

		List<EhPunchHolidays> result2 = new ArrayList<EhPunchHolidays>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_HOLIDAYS)
				.where(Tables.EH_PUNCH_HOLIDAYS.STATUS.eq(DateStatus.HOLIDAY.getCode()))
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhPunchHolidays.class));
					return null;
				});
		assertEquals(1, result2.size());
		EhPunchHolidays db = result2.get(0); 
		assertEquals(cmd.getOwnerType(), db.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), db.getOwnerId()); 
		assertEquals(dateSF.parse("2016-07-27").getTime(), db.getRuleDate().getTime()); 
		

		List<EhPunchHolidays> result3 = new ArrayList<EhPunchHolidays>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_HOLIDAYS)
				.where(Tables.EH_PUNCH_HOLIDAYS.STATUS.eq(DateStatus.WORKDAY.getCode()))
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result3.add(ConvertHelper.convert(r,
							EhPunchHolidays.class));
					return null;
				});
		assertEquals(1, result3.size());
		EhPunchHolidays db2 = result3.get(0); 
		assertEquals(cmd.getOwnerType(), db2.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), db2.getOwnerId()); 
		assertEquals(dateSF.parse("2016-07-28").getTime(), db2.getRuleDate().getTime()); 
		
		
		listPunchWorkRulesTest(cmd);
		
	}
	
	public void listPunchWorkRulesTest(PunchWorkdayRuleDTO rule){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchWorkdayRules";

		ListPunchRulesCommonCommand cmd = new ListPunchRulesCommonCommand(); 

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		ListPunchWorkdayRulesRestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, ListPunchWorkdayRulesRestResponse.class,
				context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getWorkdayRules().size());
		PunchWorkdayRuleDTO dto = response.getResponse().getWorkdayRules().get(0);

		assertEquals(rule.getName(), dto.getName()); 
		for(Long i : rule.getHolidays()){
			assertTrue(dto.getHolidays().contains(i));
		}
		for(Long i : rule.getWorkdays()){
			assertTrue(dto.getWorkdays().contains(i));
		}
		for(Integer i : rule.getWorkWeekDates()){
			assertTrue(dto.getWorkWeekDates().contains(i));
		}
		
		
		for(Long i : dto.getHolidays()){
			assertTrue( rule.getHolidays().contains(i));
		}
		for(Long i : dto.getWorkdays()){
			assertTrue(rule.getWorkdays().contains(i));
		}
		for(Integer i : dto.getWorkWeekDates()){
			assertTrue(rule.getWorkWeekDates().contains(i));
		}
	}
	
	


	public void addRuleTest(){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/addPunchRule";

		PunchRuleDTO cmd = new PunchRuleDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setName("wifi");
		cmd.setLocationRuleId(2L);
		cmd.setTimeRuleId(2L);
		cmd.setWifiRuleId(2L);
		cmd.setWorkdayRuleId(2L);
		
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchRules> result = new ArrayList<EhPunchRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		assertEquals(cmd.getLocationRuleId(), rule.getLocationRuleId());
		assertEquals(cmd.getWifiRuleId(), rule.getWifiRuleId());
		assertEquals(cmd.getWorkdayRuleId(), rule.getWorkdayRuleId());
		assertEquals(cmd.getTimeRuleId(), rule.getTimeRuleId());
		 
		
		updatePunchRuleTest(rule.getId());
	}
	

	public void updatePunchRuleTest(Long id){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/updatePunchRule";

		PunchRuleDTO cmd = new PunchRuleDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		cmd.setName("wifi"); 
		cmd.setTimeRuleId(2L);
		cmd.setWifiRuleId(2L);
		cmd.setWorkdayRuleId(2L);
		
		cmd.setId(id); 
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchRules> result = new ArrayList<EhPunchRules>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_RULES)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchRules.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchRules rule = result.get(0);
		assertEquals(cmd.getName(), rule.getName());
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		assertEquals(cmd.getLocationRuleId(), rule.getLocationRuleId());
		assertEquals(cmd.getWifiRuleId(), rule.getWifiRuleId());
		assertEquals(cmd.getWorkdayRuleId(), rule.getWorkdayRuleId());
		assertEquals(cmd.getTimeRuleId(), rule.getTimeRuleId());
		 
		listPunchRulesTest(cmd);
		
	}
	
	public void listPunchRulesTest(PunchRuleDTO rule){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchRules";

		ListPunchRulesCommonCommand cmd = new ListPunchRulesCommonCommand(); 

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		ListPunchRulesRestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, ListPunchRulesRestResponse.class,
				context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getPunchRuleDTOs().size());
		PunchRuleDTO dto = response.getResponse().getPunchRuleDTOs().get(0);
 
		assertEquals(rule.getOwnerType(), dto.getOwnerType()); 
		assertEquals(rule.getOwnerId(), dto.getOwnerId()); 
		assertEquals(rule.getLocationRuleId(), dto.getLocationRuleId());
		assertEquals(rule.getWifiRuleId(), dto.getWifiRuleId());
		assertEquals(rule.getWorkdayRuleId(), dto.getWorkdayRuleId());
		assertEquals(rule.getTimeRuleId(), dto.getTimeRuleId());
	}

	



	public void addRuleMapTest(){
		logon(null, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/punch/addPunchRuleMap";

		PunchRuleMapDTO cmd = new PunchRuleMapDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		cmd.setTargetId(ownerId);
		cmd.setTargetType(ownerType); 
		cmd.setPunchRuleId(3L);
		cmd.setDescription("");
		
		
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchRuleOwnerMap> result = new ArrayList<EhPunchRuleOwnerMap>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_RULE_OWNER_MAP)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchRuleOwnerMap.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchRuleOwnerMap rule = result.get(0); 
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		assertEquals(cmd.getTargetType(), rule.getTargetType()); 
		assertEquals(cmd.getTargetId(), rule.getTargetId()); 
		assertEquals(cmd.getPunchRuleId(), rule.getPunchRuleId());  
		
		 
		
		updatePunchRuleMapTest(rule.getId());
	}
	

	public void updatePunchRuleMapTest(Long id){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/addPunchRuleMap";

		PunchRuleMapDTO cmd = new PunchRuleMapDTO();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		cmd.setTargetId(ownerId);
		cmd.setTargetType(ownerType); 
		cmd.setPunchRuleId(2L);
		cmd.setDescription("");
		
		
		RestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhPunchRuleOwnerMap> result = new ArrayList<EhPunchRuleOwnerMap>();
		dslContext
				.select()
				.from(Tables.EH_PUNCH_RULE_OWNER_MAP)
//				.where(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhPunchRuleOwnerMap.class));
					return null;
				});
		assertEquals(1, result.size());
		EhPunchRuleOwnerMap rule = result.get(0); 
		assertEquals(cmd.getOwnerType(), rule.getOwnerType()); 
		assertEquals(cmd.getOwnerId(), rule.getOwnerId()); 
		assertEquals(cmd.getTargetType(), rule.getTargetType()); 
		assertEquals(cmd.getTargetId(), rule.getTargetId()); 
		assertEquals(cmd.getPunchRuleId(), rule.getPunchRuleId());  
		
		 
		listPunchRuleMapTest(cmd);
		
	}
	
	public void listPunchRuleMapTest(PunchRuleMapDTO rule){
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/punch/listPunchRuleMaps";

		ListPunchRuleMapsCommand cmd = new ListPunchRuleMapsCommand(); 

		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(ownerId); 
		cmd.setTargetId(ownerId);
		cmd.setTargetType(ownerType); 
		
		ListPunchRuleMapsRestResponse  response = httpClientService.restGet(
				commandRelativeUri, cmd, ListPunchRuleMapsRestResponse.class,
				context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getPunchRuleMaps().size());
		PunchRuleMapDTO dto = response.getResponse().getPunchRuleMaps().get(0);
 
		assertEquals(dto.getOwnerType(), rule.getOwnerType()); 
		assertEquals(dto.getOwnerId(), rule.getOwnerId()); 
		assertEquals(dto.getTargetType(), rule.getTargetType()); 
		assertEquals(dto.getTargetId(), rule.getTargetId()); 
		assertEquals(dto.getPunchRuleId(), rule.getPunchRuleId());  
		
		 
	}

	
}
