package com.everhomes.test.junit.punch;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsRestResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingRestResponse;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchSchedulingMonthCommand;
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
		initRuleData();
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
	private String LIST_SCHEDULING_URI =  "/punch/listPunchScheduling";
	private String UPDATE_SCHEDULING_URI =  "/punch/updatePunchSchedulings"; 
	private final Long MILLISECONDGMT = 8 * 3600 * 1000L;

	private Long convertTimeToGMTMillisecond(Time time) {
		if (null != time) {
			// 从8点开始计算
			return time.getTime() + MILLISECONDGMT;
		}
		return null;
	}
	@Test
	public void testMain(){
		testUpdateSchedulings();
		testListSchedulings();
	}
	public void testUpdateSchedulings(){
		logon(null, userIdentifier, plainTexPassword);

		UpdatePunchSchedulingMonthCommand cmd = new UpdatePunchSchedulingMonthCommand();
		List<PunchSchedulingDTO> schedulingList = new ArrayList<PunchSchedulingDTO>();
		PunchSchedulingDTO dto1 =new PunchSchedulingDTO();
		dto1.setOwnerType(ownerType);
		dto1.setOwnerId(ownerId);
		dto1.setTargetId(ownerId);
		dto1.setTargetType(ownerType);
		dto1.setRuleDate(1488412800000L);
		dto1.setTimeRuleId(2L); 
		schedulingList.add(dto1);

		PunchSchedulingDTO dto2 =new PunchSchedulingDTO();
		dto2.setOwnerType(ownerType);
		dto2.setOwnerId(ownerId);
		dto2.setTargetId(ownerId);
		dto2.setTargetType(ownerType);
		dto2.setRuleDate(1488499200000L);
		dto2.setTimeRuleId(2L); 
		schedulingList.add(dto2);
		
		cmd.setSchedulings(schedulingList);
		
		RestResponse response = httpClientService.restGet(UPDATE_SCHEDULING_URI, cmd,
				RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
	}

	public void testListSchedulings(){
		logon(null, userIdentifier, plainTexPassword);

		ListPunchSchedulingMonthCommand cmd = new ListPunchSchedulingMonthCommand(); 
		cmd.setOwnerType(ownerType);
		cmd.setOwnerId(ownerId);
		cmd.setTargetId(ownerId);
		cmd.setTargetType(ownerType);
		cmd.setQueryTime(1488412800000L); 
 
		
		ListPunchSchedulingRestResponse response = httpClientService.restGet(LIST_SCHEDULING_URI, cmd,
				ListPunchSchedulingRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		List<PunchSchedulingDTO> schedulings = response.getResponse().getSchedulings();
		for(PunchSchedulingDTO sche : schedulings ){
			if(sche.getRuleDate().equals(1488499200000L)){
				assertEquals(2L, sche.getTimeRuleId().longValue());
			}
		}
	}
	public void testListPunchTimeRules(){

		
	}
}
