// @formatter:off
package com.everhomes.test.junit.banner;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.rest.ui.banner.BannerGetBannersBySceneRestResponse;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.rest.ui.launchpad.GetLaunchPadLayoutBySceneCommand;
import com.everhomes.rest.ui.launchpad.LaunchpadGetLastLaunchPadLayoutBySceneRestResponse;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class GetBannerBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Ignore @Test
    public void testGetBannersByScene() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/ui/user/listUserRelatedScenes";
        UserListUserRelatedScenesRestResponse response = httpClientService.restGet(commandRelativeUri, null, 
            UserListUserRelatedScenesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(2, response.getResponse().size());
        // 确保两个场景不一样
        assertNotSame(response.getResponse().get(0).getSceneToken(), response.getResponse().get(1).getSceneToken());
        
        // 使用两种不同的场景应该都能取到相同的数据
        commandRelativeUri = "/ui/banner/getBannersByScene";
        String bannerLocation = "/home";
        String bannerGroup = "Default";
        String sceneToken = response.getResponse().get(0).getSceneToken();
        
        // 使用第一场景获取banner
        GetBannersBySceneCommand cmd = new GetBannersBySceneCommand();
        cmd.setBannerLocation(bannerLocation);
        cmd.setBannerGroup(bannerGroup);
        cmd.setSceneToken(sceneToken);
        BannerGetBannersBySceneRestResponse bannerResponse1 = httpClientService.restGet(commandRelativeUri, cmd, 
            BannerGetBannersBySceneRestResponse.class, context);
        assertNotNull("The reponse of may not be null", bannerResponse1);
        assertTrue("The banners should be get from server, response=" + 
            StringHelper.toJsonString(bannerResponse1), httpClientService.isReponseSuccess(bannerResponse1));
        assertNotNull(bannerResponse1.getResponse());
        assertEquals(1, bannerResponse1.getResponse().size());
        
        // 使用第二场景获取banner
        sceneToken = response.getResponse().get(1).getSceneToken();
        cmd.setSceneToken(sceneToken);
        BannerGetBannersBySceneRestResponse bannerResponse2 = httpClientService.restGet(commandRelativeUri, cmd, 
            BannerGetBannersBySceneRestResponse.class, context);
        assertNotNull("The reponse of may not be null", bannerResponse2);
        assertTrue("The banners should be get from server, response=" + 
            StringHelper.toJsonString(bannerResponse2), httpClientService.isReponseSuccess(bannerResponse2));
        assertNotNull(bannerResponse2.getResponse());
        assertEquals(1, bannerResponse2.getResponse().size());
    }
    
    @Ignore @Test
    public void testGetLastLaunchPadLayoutByScene2() {
    	
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String bannerLocation = "/home";
        String bannerGroup = "Default";
        GetBannersBySceneCommand cmd = new GetBannersBySceneCommand();
        cmd.setBannerLocation(bannerLocation);
        cmd.setBannerGroup(bannerGroup);
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        String commandRelativeUri = "/ui/banner/getBannersByScene";
        BannerGetBannersBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, BannerGetBannersBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().size());
        assertEquals(true, response.getResponse().get(0).getId() == 4L);
  
    }
    
    @Test
    public void testGetLaunchPadItemsByScene3() {
    	Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
         
        String bannerLocation = "/home";
        String bannerGroup = "Default";
        GetBannersBySceneCommand cmd = new GetBannersBySceneCommand();
        cmd.setBannerLocation(bannerLocation);
        cmd.setBannerGroup(bannerGroup);
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");
        String commandRelativeUri = "/ui/banner/getBannersByScene";
        BannerGetBannersBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, BannerGetBannersBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(2, response.getResponse().size());
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(3L);
        ids.add(5L);
        boolean flag = false;
        for (BannerDTO dto : response.getResponse()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
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
        String jsonFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-community_address_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-zuolin_default_forum_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-family_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-banner_160617.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

