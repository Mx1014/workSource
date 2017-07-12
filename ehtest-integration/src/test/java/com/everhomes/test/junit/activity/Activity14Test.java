package com.everhomes.test.junit.activity;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.activity.ActivityWarningResponse;
import com.everhomes.rest.activity.GetActivityWarningCommand;
import com.everhomes.rest.activity.GetActivityWarningRestResponse;
import com.everhomes.rest.activity.SetActivityWarningCommand;
import com.everhomes.rest.activity.SetActivityWarningRestResponse;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class Activity14Test extends BaseLoginAuthTestCase {
	
	//1. 设置活动提醒
	private static final String SET_ACTIVITY_WARNING_URL = "/activity/setActivityWarning";
	//2. 查询活动提醒
	private static final String GET_ACTIVITY_WARNING_URL = "/activity/getActivityWarning";


	//1. 设置活动提醒
	@Test
	public void testSetActivityWarning() {
		String url = SET_ACTIVITY_WARNING_URL;
		logon();
		setActivityWarning(url);

	}

	private void setActivityWarning(String url) {
		SetActivityWarningCommand cmd = new SetActivityWarningCommand();
		cmd.setNamespaceId(999995);
		cmd.setDays(1);
		cmd.setHours(2);

		SetActivityWarningRestResponse response = httpClientService.restPost(url, cmd, SetActivityWarningRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ActivityWarningResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals(999995, myResponse.getNamespaceId().intValue());
		assertEquals(1, myResponse.getDays().intValue());
		assertEquals(2, myResponse.getHours().intValue());
	}
	
	//2. 查询活动提醒
	@Test
	public void testGetActivityWarning() {
		String url = GET_ACTIVITY_WARNING_URL;
		logon();
		setActivityWarning(SET_ACTIVITY_WARNING_URL);
		
		GetActivityWarningCommand cmd = new GetActivityWarningCommand();
		cmd.setNamespaceId(999995);

		GetActivityWarningRestResponse response = httpClientService.restPost(url, cmd, GetActivityWarningRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ActivityWarningResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals(999995, myResponse.getNamespaceId().intValue());
		assertEquals(1, myResponse.getDays().intValue());
		assertEquals(2, myResponse.getHours().intValue());

	}
	
	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/1.0.0-activity-test-data-161018.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}

	private String getSceneToken() {
		String uri = "/ui/user/listUserRelatedScenes";
		UserListUserRelatedScenesRestResponse response = httpClientService.restPost(uri, null,
				UserListUserRelatedScenesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<SceneDTO> list = response.getResponse();
		assertTrue("list size should be greater than 0", list != null && list.size() > 0);

		return list.get(0).getSceneToken();
	}
	
	@After
	public void tearDown() {
		logoff();
	}

	private void logon() {
		String userIdentifier = "tt";
		String plainTexPassword = "123456";
		Integer namespaceId = 999995;
		logon(namespaceId, userIdentifier, plainTexPassword);
	}
}
