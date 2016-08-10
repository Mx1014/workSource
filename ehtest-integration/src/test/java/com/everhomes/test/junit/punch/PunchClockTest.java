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

		jsonPath = "data/json/2.0.0-punch-test-data-addbill-rules-160810.txt";
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

	@Test
	public void PunchClockTest() {
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
}
