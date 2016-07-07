package com.everhomes.test.junit.hottag;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.hotTag.DeleteHotTagCommand;
import com.everhomes.rest.hotTag.HotTagServiceType;
import com.everhomes.rest.hotTag.ListHotTagCommand;
import com.everhomes.rest.hotTag.ListHotTagRestResponse;
import com.everhomes.rest.hotTag.SearchTagCommand;
import com.everhomes.rest.hotTag.SearchTagRestResponse;
import com.everhomes.rest.hotTag.SetHotTagCommand;
import com.everhomes.rest.hotTag.SetHotTagRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class HotTagTest extends BaseLoginAuthTestCase {
	@Before
    public void setUp() {
        super.setUp();
    }
    
	
	@Test
    public void testSetHotTag() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        SetHotTagCommand cmd = new SetHotTagCommand();
        
        cmd.setName("活动标签");
        cmd.setServiceType(HotTagServiceType.ACTIVITY.getCode());
        String commandRelativeUri = "/hotTag/setHotTag";
        SetHotTagRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		SetHotTagRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals("活动标签", response.getResponse().getName());
        
        
        DeleteHotTagCommand command = new DeleteHotTagCommand();
        
        command.setId(response.getResponse().getId());
        
        commandRelativeUri = "/hotTag/deleteHotTag";
        StringRestResponse resp = httpClientService.restGet(commandRelativeUri, command, 
        		StringRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", resp);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
        
    }
	
	@Test
    public void testListHotTag() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListHotTagCommand cmd = new ListHotTagCommand();
        cmd.setServiceType(HotTagServiceType.ACTIVITY.getCode());
        
        String commandRelativeUri = "/hotTag/listHotTag";
        ListHotTagRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListHotTagRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().size());
        
    }
	
	@Test
    public void testSearchHotTagAll() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        SearchTagCommand command = new SearchTagCommand();
        command.setKeyword("活动标签");
        command.setServiceType(HotTagServiceType.ACTIVITY.getCode());
        String commandRelativeUri = "/hotTag/searchTag";
        SearchTagRestResponse response = httpClientService.restGet(commandRelativeUri, command, 
        		SearchTagRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals("活动标签", response.getResponse().getTags().get(0).getName());
        
    }
	
	@Test
    public void testSearchHotTagSingle() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        SearchTagCommand command = new SearchTagCommand();
        command.setKeyword("动");
        command.setServiceType(HotTagServiceType.ACTIVITY.getCode());
        String commandRelativeUri = "/hotTag/searchTag";
        SearchTagRestResponse response = httpClientService.restGet(commandRelativeUri, command, 
        		SearchTagRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals("活动标签", response.getResponse().getTags().get(0).getName());
        
    }
	
	@Test
    public void testSearchHotTagDouble() {
		
        String userIdentifier = "12000000002";
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        SearchTagCommand command = new SearchTagCommand();
        command.setKeyword("标签");
        command.setServiceType(HotTagServiceType.ACTIVITY.getCode());
        String commandRelativeUri = "/hotTag/searchTag";
        SearchTagRestResponse response = httpClientService.restGet(commandRelativeUri, command, 
        		SearchTagRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals("活动标签", response.getResponse().getTags().get(0).getName());
        
    }
	
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.7.x-test-data-hottag.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
    }
}
