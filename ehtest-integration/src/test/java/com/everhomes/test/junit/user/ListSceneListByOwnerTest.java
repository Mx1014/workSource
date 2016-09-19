// @formatter:off
package com.everhomes.test.junit.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.ui.user.ListScentTypeByOwnerCommand;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.ListSceneTypeByOwnerRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ListSceneListByOwnerTest extends BaseLoginAuthTestCase {
	@Before
    public void setUp() {
        super.setUp();
    }
	
	@Test
    public void testListSceneList() {
        Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        ListScentTypeByOwnerCommand cmd = new ListScentTypeByOwnerCommand();
        cmd.setCommunityId(24210090697425925L);
        
        String commandRelativeUri = "/user/listSceneTypeByOwner";
        ListSceneTypeByOwnerRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
        		ListSceneTypeByOwnerRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(2, response.getResponse().size());

        List<String> list = new ArrayList<>();
        response.getResponse().forEach(c -> {
        	list.add(c.getName());
        });
        assertTrue("The scene type list should contains "+SceneType.DEFAULT.getCode(), list.contains(SceneType.DEFAULT.getCode()));
        assertTrue("The scene type list should contains "+SceneType.PM_ADMIN.getCode(), list.contains(SceneType.PM_ADMIN.getCode()));
    }
    
    @After
    public void tearDown() {
        super.tearDown();
    }
    
    protected void initCustomData() {
    	String jsonFilePath = "data/json/create-banner-by-owner-create-test-data.txt";
    	String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
    	dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
    	jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
    	fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
    	dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
    	jsonFilePath = "data/json/3.4.x-test-data-community_address_160628.txt";
    	fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
    	dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}
