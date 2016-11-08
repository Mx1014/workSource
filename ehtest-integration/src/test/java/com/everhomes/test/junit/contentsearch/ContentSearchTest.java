package com.everhomes.test.junit.contentsearch;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.ui.user.ListSearchTypesBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.UserListSearchTypesBySceneRestResponse;
import com.everhomes.rest.ui.user.UserSearchContentsBySceneRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ContentSearchTest extends BaseLoginAuthTestCase {
	
	private static final String SEARCH_TYPES_LIST_URI = "/ui/user/listSearchTypesByScene";
	private static final String SEARCH_CONTENTS_URI = "/ui/user/searchContentsByScene";

	Integer namespaceId = 0;
    String userIdentifier = "12000000020"; // 管理员帐号
    String plainTexPassword = "123456";
    
	@Before
    public void setUp() {
        super.setUp();
    }
	
	@Test
	 public void testListSearchTypesByScene() {
	    	
		logon(null, userIdentifier, plainTexPassword);
		
		ListSearchTypesBySceneCommand cmd = new ListSearchTypesBySceneCommand();
		cmd.setSceneToken("ttggEZEFyEgKvu073JVxBn1_OqS9rU7TbTaqQJCuumwXQrEitiL9miy4GHhPjtpTkV50vhe84lSqx1AjJHupXhUJVr-EwUdkmafZaDG2aawEtYcN8vXWBmKu2SHmUDj7s6JJa6btlzpKXroEngpiLNk06w5MEGg4r-Jpc5VEMWk");
    	
		UserListSearchTypesBySceneRestResponse response = httpClientService.restGet(SEARCH_TYPES_LIST_URI, cmd, UserListSearchTypesBySceneRestResponse.class, context);
    	
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + 
	                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		assertEquals(4, response.getResponse().getSearchTypes().size());
	}
	
	@Test
    public void testSearchContentsByScene() {
        
        logon(null, userIdentifier, plainTexPassword);
        
//        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        
        SearchContentsBySceneCommand cmd = new SearchContentsBySceneCommand();
        
        cmd.setSceneToken("ttggEZEFyEgKvu073JVxBn1_OqS9rU7TbTaqQJCuumwXQrEitiL9miy4GHhPjtpTkV50vhe84lSqx1AjJHupXhUJVr-EwUdkmafZaDG2aawEtYcN8vXWBmKu2SHmUDj7s6JJa6btlzpKXroEngpiLNk06w5MEGg4r-Jpc5VEMWk");
        //cmd.setKeyword(keyword);
        cmd.setContentType(SearchContentType.ACTIVITY.getCode());
        
        UserSearchContentsBySceneRestResponse response = httpClientService.restGet(SEARCH_CONTENTS_URI, cmd, UserSearchContentsBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        
        assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
      
        
    }
	
	@After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	String jsonFilePath = "data/json/contentsearch-test-data.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        
        jsonFilePath = "data/json/3.4.x-test-data-userinfo_160618.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-community_address_160628.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-family_160628.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-organization_member_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
    }
}
