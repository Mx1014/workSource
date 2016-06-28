// @formatter:off
package com.everhomes.test.junit.ui.launchpad;


import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.ui.launchpad.GetLaunchPadLayoutBySceneCommand;
import com.everhomes.rest.ui.launchpad.LaunchpadGetLastLaunchPadLayoutBySceneRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class GetLastLaunchPadLayoutBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Ignore @Test
    public void testGetLastLaunchPadLayoutByScene1() {
        String jsonFilePath = "data/json/3.4.x-test-data-launch_pad_layouts_160628.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadLayoutBySceneCommand cmd = new GetLaunchPadLayoutBySceneCommand();
        cmd.setName("ServiceMarketLayout");
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        cmd.setVersionCode(22016021402L);
        String commandRelativeUri = "/ui/launchpad/getLastLaunchPadLayoutByScene";
        LaunchpadGetLastLaunchPadLayoutBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLastLaunchPadLayoutBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull("The id not be null", response.getResponse().getId());
        assertEquals(true, response.getResponse().getId() == 2L);
    }
    
    @Ignore @Test
    public void testGetLastLaunchPadLayoutByScene2() {
    	
    	String jsonFilePath = "data/json/3.4.x-test-data-launch_pad_layouts_160628_1.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadLayoutBySceneCommand cmd = new GetLaunchPadLayoutBySceneCommand();
        cmd.setName("ServiceMarketLayout");
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        cmd.setVersionCode(22016021402L);
        String commandRelativeUri = "/ui/launchpad/getLastLaunchPadLayoutByScene";
        LaunchpadGetLastLaunchPadLayoutBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLastLaunchPadLayoutBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(true, response.getResponse().getLayoutJson().equals("44444444444444"));
        
  
    }
    
    @Ignore @Test
    public void testGetLastLaunchPadLayoutByScene3() {
    	String jsonFilePath = "data/json/3.4.x-test-data-launch_pad_layouts_160628_2.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadLayoutBySceneCommand cmd = new GetLaunchPadLayoutBySceneCommand();
        cmd.setName("ServiceMarketLayout");
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");
        cmd.setVersionCode(22016021402L);
        
        String commandRelativeUri = "/ui/launchpad/getLastLaunchPadLayoutByScene";
        LaunchpadGetLastLaunchPadLayoutBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLastLaunchPadLayoutBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(true, response.getResponse().getId() == 2L);
    }
        
    @Test
    public void testGetLaunchPadItemsByScene4() {
    	String jsonFilePath = "data/json/3.4.x-test-data-launch_pad_layouts_160628_3.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    	
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadLayoutBySceneCommand cmd = new GetLaunchPadLayoutBySceneCommand();
        cmd.setName("ServiceMarketLayout");
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");
        cmd.setVersionCode(22016021402L);
        
        String commandRelativeUri = "/ui/launchpad/getLastLaunchPadLayoutByScene";
        LaunchpadGetLastLaunchPadLayoutBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetLastLaunchPadLayoutBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(true, response.getResponse().getId() == 1L);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        
        String jsonFilePath = "data/json/3.4.x-test-data-userinfo_160618.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
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
        
        jsonFilePath = "data/json/3.4.x-test-data-organization_member_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
       
    }
}

