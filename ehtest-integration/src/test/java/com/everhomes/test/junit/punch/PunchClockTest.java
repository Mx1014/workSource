package com.everhomes.test.junit.punch;

import java.sql.Time;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.PunchClockCommand;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class PunchClockTest extends BaseLoginAuthTestCase {
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

		String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);


        String jsonPath = "data/json/2.0.0-punch-test-data-addbill-base-rules-160810.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
        dbProvider.loadJsonFileToDatabase(filePath, false);


		jsonPath = "data/json/2.0.0-punch-test-data-addbill-rules-map-and-organization-160810.txt";
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
	/**
	 * 测试:wifi打卡的正常/wifi错误/wifi空 的情况
	 * 测试:wifi加地址打卡的 wifi正确/wifi错误/wifi为空 地址正确/wifi和地址空 /wifi为空 且 地址错误
	 * */
	@Test
	public void testWIFIPunch(){

		String jsonPath = "data/json/2.0.0-punch-test-data-addbill-rules-160810.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(jsonPath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
		PunchClockTest1();
		PunchClockTest2();
		PunchClockTest3();
		PunchClockTest4();
		PunchClockTest5();
		PunchClockTest6();
		PunchClockTest7(); 
		PunchClockTest9();
	}
	/**wifi正确*/
	public void PunchClockTest1() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();

		cmd.setEnterpriseId(ownerId);
		cmd.setWifiMac("mac-address-01");
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

	}
	/**wifi错误 地址为空*/
	public void PunchClockTest2() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();
		
		cmd.setEnterpriseId(ownerId);
		cmd.setWifiMac("mac-address-02");
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue( response.getErrorCode().equals(13000));

	}

	/**wifi为空地址为空*/
	public void PunchClockTest3() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();
		
		cmd.setEnterpriseId(ownerId); 
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue( response.getErrorCode().equals(13000));

	}
	/**wifi为空地址正确*/
	public void PunchClockTest4() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();

		cmd.setLatitude(22.536289);
		cmd.setLongitude(113.951335);
		cmd.setEnterpriseId(ownerId); 
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);

		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

	}
	

	/**wifi为空地址错误*/
	public void PunchClockTest5() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();
		cmd.setLatitude(24.0);
		cmd.setLongitude(111.0);
		cmd.setEnterpriseId(ownerId); 
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue( response.getErrorCode().equals(10001));

	}


	/**wifi错误 地址错误*/
	public void PunchClockTest6() {
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();
		cmd.setWifiMac("mac-address-02");
		cmd.setLatitude(24.0);
		cmd.setLongitude(111.0);
		cmd.setEnterpriseId(ownerId); 
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue( response.getErrorCode().equals(10001));

	}
	

	/**只有wifi wifi错误*/
	public void PunchClockTest7() {
		logon(null, userIdentifier2, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();
		cmd.setWifiMac("mac-address-02"); 
		cmd.setLatitude(24.0);
		cmd.setLongitude(111.0);
		cmd.setEnterpriseId(ownerId); 
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue( response.getErrorCode().equals(10007));

	}

	/**只有wifi wifi错误*/
	public void PunchClockTest9() {
		logon(null, userIdentifier2, plainTexPassword);

		String commandRelativeUri = "/techpark/punch/punchClock";

		PunchClockCommand cmd = new PunchClockCommand();
		cmd.setWifiMac("mac-address-02"); 
		cmd.setLatitude(24.0);
		cmd.setLongitude(111.0);
		cmd.setEnterpriseId(ownerId); 
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue( response.getErrorCode().equals(10006));

	}
}
