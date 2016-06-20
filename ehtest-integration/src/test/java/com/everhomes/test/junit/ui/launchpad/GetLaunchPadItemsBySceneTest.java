// @formatter:off
package com.everhomes.test.junit.ui.launchpad;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.GetLaunchPadItemsCommandResponse;
import com.everhomes.rest.launchpad.ItemGroup;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.rest.scene.SceneTypeInfoDTO;
import com.everhomes.rest.scene.admin.SceneListSceneTypesRestResponse;
import com.everhomes.rest.ui.launchpad.GetLaunchPadItemsBySceneCommand;
import com.everhomes.rest.ui.launchpad.LaunchpadGetLaunchPadItemsBySceneRestResponse;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

public class GetLaunchPadItemsBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testGetLaunchPadItemsByScene1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadItemsBySceneCommand cmd = new GetLaunchPadItemsBySceneCommand();
        cmd.setItemGroup(ItemGroup.BIZS.getCode());
        cmd.setItemLocation("/home");
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        
        String commandRelativeUri = "/ui/launchpad/getLaunchPadItemsByScene";
        LaunchpadGetLaunchPadItemsBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLaunchPadItemsBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getLaunchPadItems());
        assertEquals(4, response.getResponse().getLaunchPadItems().size());
       
        boolean flag = false;
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        ids.add(6L);
        ids.add(7L);
        ids.add(36L);
        
        Integer order = -1;
        for (LaunchPadItemDTO dto : response.getResponse().getLaunchPadItems()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
			}
			
			if(order < dto.getDefaultOrder()){
				order = dto.getDefaultOrder();
				flag = true;
			}else{
				flag = false;
				break;
			}
			
			if(1L == dto.getId()){
				if(20 != dto.getDefaultOrder()){
					flag = false;
					break;
				}
			}
			
			if(36L == dto.getId()){
				if(80 != dto.getDefaultOrder()){
					flag = false;
					break;
				}
			}
		}
        
        assertEquals(true, flag);
    }
    
    @Test
    public void testGetLaunchPadItemsByScene2() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadItemsBySceneCommand cmd = new GetLaunchPadItemsBySceneCommand();
        cmd.setItemGroup(ItemGroup.BIZS.getCode());
        cmd.setItemLocation("/home");
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");
        
        String commandRelativeUri = "/ui/launchpad/getLaunchPadItemsByScene";
        LaunchpadGetLaunchPadItemsBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLaunchPadItemsBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getLaunchPadItems());
        assertEquals(4, response.getResponse().getLaunchPadItems().size());
       
        boolean flag = false;
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(10L);
        ids.add(14L);
        ids.add(9L);
        ids.add(39L);
        
        Integer order = -1;
        for (LaunchPadItemDTO dto : response.getResponse().getLaunchPadItems()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
			}
			
			if(order < dto.getDefaultOrder()){
				order = dto.getDefaultOrder();
				flag = true;
			}else{
				flag = false;
				break;
			}
			
			if(10L == dto.getId()){
				if(22 != dto.getDefaultOrder()){
					flag = false;
					break;
				}
			}
			
			if(39L == dto.getId()){
				if(75 != dto.getDefaultOrder()){
					flag = false;
					break;
				}
			}
		}
        
        assertEquals(true, flag);
    }
    
    @Test
    public void testGetLaunchPadItemsByScene3() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000010"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadItemsBySceneCommand cmd = new GetLaunchPadItemsBySceneCommand();
        cmd.setItemGroup(ItemGroup.BIZS.getCode());
        cmd.setItemLocation("/home");
        cmd.setSceneToken("EflmS6AN5qOrNFS3bjViBaIZSnLB_SSj3RwMcojT1rYXQrEitiL9miy4GHhPjtpTpWhX6ydmrPc8SO7k65f6mhmV6Yi1RajfzWCX96itiUvtkW9_pwoCjPzKE6D6djVc0uG79BFC6EIBJpWPhxLxdLXtLYwNCPsiaCOsHkOxrgg");
        
        String commandRelativeUri = "/ui/launchpad/getLaunchPadItemsByScene";
        LaunchpadGetLaunchPadItemsBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLaunchPadItemsBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getLaunchPadItems());
        assertEquals(4, response.getResponse().getLaunchPadItems().size());
       
        boolean flag = false;
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        ids.add(6L);
        ids.add(7L);
        ids.add(36L);
        
        Integer order = -1;
        for (LaunchPadItemDTO dto : response.getResponse().getLaunchPadItems()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
			}
			
			if(order < dto.getDefaultOrder()){
				order = dto.getDefaultOrder();
				flag = true;
			}else{
				flag = false;
				break;
			}
			
			if(1L == dto.getId()){
				if(20 != dto.getDefaultOrder()){
					flag = false;
					break;
				}
			}
			
			if(36L == dto.getId()){
				if(80 != dto.getDefaultOrder()){
					flag = false;
					break;
				}
			}
		}
    }
        
    @Test
    public void testGetLaunchPadItemsByScene4() {
            Integer namespaceId = 0;
            String userIdentifier = "12000000020"; // 管理员帐号
            String plainTexPassword = "123456";
            logon(null, userIdentifier, plainTexPassword);
            
            GetLaunchPadItemsBySceneCommand cmd = new GetLaunchPadItemsBySceneCommand();
            cmd.setItemGroup(ItemGroup.BIZS.getCode());
            cmd.setItemLocation("/home");
            cmd.setSceneToken("ttggEZEFyEgKvu073JVxBn1_OqS9rU7TbTaqQJCuumwXQrEitiL9miy4GHhPjtpTkV50vhe84lSqx1AjJHupXhUJVr-EwUdkmafZaDG2aawEtYcN8vXWBmKu2SHmUDj7s6JJa6btlzpKXroEngpiLNk06w5MEGg4r-Jpc5VEMWk");
            
            String commandRelativeUri = "/ui/launchpad/getLaunchPadItemsByScene";
            LaunchpadGetLaunchPadItemsBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLaunchPadItemsBySceneRestResponse.class, context);
            
            assertNotNull("The reponse of may not be null", response);
            assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
            assertNotNull(response.getResponse());
            assertNotNull(response.getResponse().getLaunchPadItems());
            assertEquals(4, response.getResponse().getLaunchPadItems().size());
           
            boolean flag = false;
            
            List<Long> ids = new ArrayList<Long>();
            ids.add(8L);
            ids.add(11L);
            ids.add(18L);
            ids.add(43L);
            
            Integer order = -1;
            for (LaunchPadItemDTO dto : response.getResponse().getLaunchPadItems()) {
            	
    			if(ids.contains(dto.getId())){
    				flag = true;
    			}else{
    				flag = false;
    				break;
    			}
    			
    			if(order < dto.getDefaultOrder()){
    				order = dto.getDefaultOrder();
    				flag = true;
    			}else{
    				flag = false;
    				break;
    			}
    			
    			if(43L == dto.getId()){
    				if(88 != dto.getDefaultOrder()){
    					flag = false;
    					break;
    				}
    			}
    		}
        assertEquals(true, flag);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-launch_pad_items_160616.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        
        jsonFilePath = "data/json/3.4.x-test-data-userinfo_160618.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-community_address_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-family_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-user-launch_pad_items_160616.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
       
    }
}

