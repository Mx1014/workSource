// @formatter:off
package com.everhomes.test.junit.forum;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.TopicPublishStatus;
import com.everhomes.rest.ui.forum.ForumListNoticeBySceneRestResponse;
import com.everhomes.rest.ui.forum.ListNoticeBySceneCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ListNoticeBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListNoticeByScene1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListNoticeBySceneCommand cmd = new ListNoticeBySceneCommand();
        cmd.setPageSize(5);
        cmd.setPublishStatus(TopicPublishStatus.PUBLISHED.getCode());
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        
        String commandRelativeUri = "/ui/forum/listNoticeByScene";
        ForumListNoticeBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumListNoticeBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getPosts());
        assertEquals(2, response.getResponse().getPosts().size());
       
        boolean flag = false;
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        ids.add(3L);
        
        for (PostDTO dto : response.getResponse().getPosts()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
			}
		}
        
        assertEquals(true, flag);
    }
    
    @Test
    public void testListNoticeByScene2() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListNoticeBySceneCommand cmd = new ListNoticeBySceneCommand();
        cmd.setPageSize(5);
        cmd.setPublishStatus(TopicPublishStatus.PUBLISHED.getCode());
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");
        
        String commandRelativeUri = "/ui/forum/listNoticeByScene";
        ForumListNoticeBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumListNoticeBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getPosts());
        assertEquals(2, response.getResponse().getPosts().size());
        
        boolean flag = false;
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        ids.add(3L);
        
        for (PostDTO dto : response.getResponse().getPosts()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
			}
		}
        
        assertEquals(true, flag);
    }
    
    @Test
    public void testListNoticeByScene3() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000010"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListNoticeBySceneCommand cmd = new ListNoticeBySceneCommand();
        cmd.setPageSize(5);
        cmd.setPublishStatus(TopicPublishStatus.PUBLISHED.getCode());
        cmd.setSceneToken("EflmS6AN5qOrNFS3bjViBaIZSnLB_SSj3RwMcojT1rYXQrEitiL9miy4GHhPjtpTpWhX6ydmrPc8SO7k65f6mhmV6Yi1RajfzWCX96itiUvtkW9_pwoCjPzKE6D6djVc0uG79BFC6EIBJpWPhxLxdLXtLYwNCPsiaCOsHkOxrgg");
        
        String commandRelativeUri = "/ui/forum/listNoticeByScene";
        ForumListNoticeBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumListNoticeBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getPosts());
        assertEquals(2, response.getResponse().getPosts().size());
        
        boolean flag = false;
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        ids.add(3L);
        
        for (PostDTO dto : response.getResponse().getPosts()) {
        	
			if(ids.contains(dto.getId())){
				flag = true;
			}else{
				flag = false;
				break;
			}
		}
        
        assertEquals(true, flag);
    }
        
    @Test
    public void testListNoticeByScene4() {
            Integer namespaceId = 0;
            String userIdentifier = "12000000020"; // 管理员帐号
            String plainTexPassword = "123456";
            logon(null, userIdentifier, plainTexPassword);
            
            ListNoticeBySceneCommand cmd = new ListNoticeBySceneCommand();
            cmd.setPageSize(5);
            cmd.setPublishStatus(TopicPublishStatus.PUBLISHED.getCode());
            cmd.setSceneToken("ttggEZEFyEgKvu073JVxBn1_OqS9rU7TbTaqQJCuumwXQrEitiL9miy4GHhPjtpTkV50vhe84lSqx1AjJHupXhUJVr-EwUdkmafZaDG2aawEtYcN8vXWBmKu2SHmUDj7s6JJa6btlzpKXroEngpiLNk06w5MEGg4r-Jpc5VEMWk");
            
            String commandRelativeUri = "/ui/forum/listNoticeByScene";
            ForumListNoticeBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumListNoticeBySceneRestResponse.class, context);
            
            assertNotNull("The reponse of may not be null", response);
            assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
            assertNotNull(response.getResponse());
            assertNotNull(response.getResponse().getPosts());
            assertEquals(2, response.getResponse().getPosts().size());
            
            boolean flag = false;
            
            List<Long> ids = new ArrayList<Long>();
            ids.add(1L);
            ids.add(3L);
            
            for (PostDTO dto : response.getResponse().getPosts()) {
            	
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
        
        String jsonFilePath = "data/json/3.4.x-test-data-userinfo_160618.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-community_address_160628.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-family_160628.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-forum-posts_160620.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
       
    }
}

