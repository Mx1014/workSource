package com.everhomes.test.junit.activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.activity.GetActivityShareDetailCommand;
import com.everhomes.rest.ui.forum.ForumNewTopicBySceneRestResponse;
import com.everhomes.rest.ui.forum.NewTopicBySceneCommand;
import com.everhomes.rest.user.GetUserInfoRestResponse;
import com.everhomes.rest.user.ListActivityFavoriteRestResponse;
import com.everhomes.rest.user.ListPostedActivitiesRestResponse;
import com.everhomes.rest.user.ListPostedActivityByOwnerIdCommand;
import com.everhomes.rest.user.ListPostedTopicByOwnerIdCommand;
import com.everhomes.rest.user.ListPostedTopicsRestResponse;
import com.everhomes.rest.user.ListSignupActivitiesCommand;
import com.everhomes.rest.user.ListSignupActivitiesRestResponse;
import com.everhomes.rest.user.ListTopicFavoriteRestResponse;
import com.everhomes.rest.user.ListUserFavoriteActivityCommand;
import com.everhomes.rest.user.ListUserFavoriteTopicCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ActivityTest extends BaseLoginAuthTestCase {
	@Before
    public void setUp() {
        super.setUp();
    }
    
	@Test
    public void testGetUserinfo() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        
        String commandRelativeUri = "/user/getUserInfo";
        GetUserInfoRestResponse response = httpClientService.restGet(commandRelativeUri, null, 
        		GetUserInfoRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        
        assertEquals("我已加入左邻“13”天", response.getResponse().getRegisterDaysDesc());
        
        namespaceId = 1000000;
        logon(namespaceId, userIdentifier, plainTexPassword);
        response = httpClientService.restGet(commandRelativeUri, null, 
        		GetUserInfoRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        
        assertEquals("", response.getResponse().getRegisterDaysDesc());
    }
	
	@Test
    public void testListSignupActivities() {
		
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListSignupActivitiesCommand cmd = new ListSignupActivitiesCommand();
        
        String commandRelativeUri = "/user/listSignupActivities";
        ListSignupActivitiesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListSignupActivitiesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().getActivities().size());
        assertEquals(2, response.getResponse().getActivities().get(0).getActivityId().longValue());
    }
	
	@Test
    public void testListUserFavoriteActivity() {
		
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListUserFavoriteActivityCommand cmd = new ListUserFavoriteActivityCommand();
        
        String commandRelativeUri = "/user/listActivityFavorite";
        ListActivityFavoriteRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListActivityFavoriteRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().getActivities().size());
        assertEquals(102951, response.getResponse().getActivities().get(0).getPostId().longValue());
    }
	
	@Test
    public void testListUserFavorite() {
		
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListUserFavoriteTopicCommand cmd = new ListUserFavoriteTopicCommand();
        
        String commandRelativeUri = "/user/listTopicFavorite";
        ListTopicFavoriteRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListTopicFavoriteRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().getPostDtos().size());
        assertEquals(124391, response.getResponse().getPostDtos().get(0).getId().longValue());
    }
	
	@Test
    public void testListPostedActivities() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListPostedActivityByOwnerIdCommand cmd = new ListPostedActivityByOwnerIdCommand();
        
        String commandRelativeUri = "/user/listPostedActivities";
        ListPostedActivitiesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListPostedActivitiesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().getActivities().size());
        assertEquals(102951, response.getResponse().getActivities().get(0).getPostId().longValue());
        
    }
	
	@Test
    public void testListPostTopics() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListPostedTopicByOwnerIdCommand cmd = new ListPostedTopicByOwnerIdCommand();
        
        String commandRelativeUri = "/user/listPostedTopics";
        ListPostedTopicsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListPostedTopicsRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().getPostDtos().size());
        assertEquals(124391, response.getResponse().getPostDtos().get(0).getId().longValue());
        
    }
	
	@Test
    public void testGetActivityShareDetail() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetActivityShareDetailCommand cmd = new GetActivityShareDetailCommand();
        
        String commandRelativeUri = "/activity/getActivityShareDetail";
        ListPostedTopicsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListPostedTopicsRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().getPostDtos().size());
        assertEquals(124391, response.getResponse().getPostDtos().get(0).getId().longValue());
        
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.7.x-test-data-activity.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
    }
}
