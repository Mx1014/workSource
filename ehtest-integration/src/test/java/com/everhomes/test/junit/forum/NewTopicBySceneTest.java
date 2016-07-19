// @formatter:off
package com.everhomes.test.junit.forum;

import static com.everhomes.server.schema.Tables.*;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.ui.forum.ForumNewTopicBySceneRestResponse;
import com.everhomes.rest.ui.forum.NewTopicBySceneCommand;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class NewTopicBySceneTest extends BaseLoginAuthTestCase {
    @Before @Ignore
    public void setUp() {
        super.setUp();
    }
    
    @Test @Ignore
    public void testNewTopicByScene1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        NewTopicBySceneCommand cmd = new NewTopicBySceneCommand();
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
        cmd.setVisibleRegionId(24210090697425925L);
        cmd.setSubject("我发一个新帖试试看");
        cmd.setForumId(1L);
        cmd.setContentCategory(1001L);
        cmd.setContent("这是我心法的贴内容");
        String commandRelativeUri = "/ui/forum/newTopicByScene";
        ForumNewTopicBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumNewTopicBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
       
        List<EhForumPosts> resultItem = new ArrayList<>();
        DSLContext dslContext = dbProvider.getDslContext();
        dslContext.select().from(EH_FORUM_POSTS) 
            .where(EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()))
            .and(EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(24210090697425925L))
            .and(EH_FORUM_POSTS.FORUM_ID.eq(1L))
            .and(EH_FORUM_POSTS.CATEGORY_ID.eq(1001L))
            .and(EH_FORUM_POSTS.CREATOR_TAG.eq("USER"))
            .and(EH_FORUM_POSTS.SUBJECT.eq("我发一个新帖试试看"))
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhForumPosts.class));
                return null;
        });
        
        assertEquals(1, resultItem.size());
        
        
        cmd.setVisibleRegionType(null);
        cmd.setVisibleRegionId(null);
        response = httpClientService.restGet(commandRelativeUri, cmd, ForumNewTopicBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
       
        List<EhForumPosts> resultItem1 = new ArrayList<>();
        dslContext = dbProvider.getDslContext();
        dslContext.select().from(EH_FORUM_POSTS) 
            .where(EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()))
            .and(EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(24210090697425925L))
            .and(EH_FORUM_POSTS.FORUM_ID.eq(1L))
            .and(EH_FORUM_POSTS.CATEGORY_ID.eq(1001L))
            .and(EH_FORUM_POSTS.CREATOR_TAG.eq("USER"))
            .and(EH_FORUM_POSTS.SUBJECT.eq("我发一个新帖试试看"))
            .fetch().map((r) -> {
            	resultItem1.add(ConvertHelper.convert(r, EhForumPosts.class));
                return null;
        });
        
        assertEquals(2, resultItem1.size());
    }
    
    @Test @Ignore
    public void testNewTopicByScene2() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        NewTopicBySceneCommand cmd = new NewTopicBySceneCommand();
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");
        cmd.setVisibleRegionType(VisibleRegionType.REGION.getCode());
        cmd.setVisibleRegionId(1000750L);
        cmd.setSubject("我发一个新帖试试看");
        cmd.setForumId(1L);
        cmd.setContentCategory(1001L);
        cmd.setContent("这是我心法的贴内容");
        String commandRelativeUri = "/ui/forum/newTopicByScene";
        ForumNewTopicBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumNewTopicBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhForumPosts> resultItem1 = new ArrayList<>();
        DSLContext dslContext = dbProvider.getDslContext();
        dslContext.select().from(EH_FORUM_POSTS) 
            .where(EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()))
            .and(EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(1000750L))
            .and(EH_FORUM_POSTS.FORUM_ID.eq(1L))
            .and(EH_FORUM_POSTS.CATEGORY_ID.eq(1001L))
            .and(EH_FORUM_POSTS.SUBJECT.eq("我发一个新帖试试看"))
            .and(EH_FORUM_POSTS.CREATOR_TAG.eq(OrganizationType.PM.getCode()))
            .fetch().map((r) -> {
            	resultItem1.add(ConvertHelper.convert(r, EhForumPosts.class));
                return null;
        });
        
        assertEquals(1, resultItem1.size());
        
        cmd.setVisibleRegionType(null);
        cmd.setVisibleRegionId(null);
        response = httpClientService.restGet(commandRelativeUri, cmd, ForumNewTopicBySceneRestResponse.class, context);
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhForumPosts> resultItem2 = new ArrayList<>();
        dslContext.select().from(EH_FORUM_POSTS) 
            .where(EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()))
            .and(EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(1000750L))
            .and(EH_FORUM_POSTS.FORUM_ID.eq(1L))
            .and(EH_FORUM_POSTS.CATEGORY_ID.eq(1001L))
            .and(EH_FORUM_POSTS.SUBJECT.eq("我发一个新帖试试看"))
            .and(EH_FORUM_POSTS.CREATOR_TAG.eq(OrganizationType.PM.getCode()))
            .fetch().map((r) -> {
            	resultItem2.add(ConvertHelper.convert(r, EhForumPosts.class));
                return null;
        });
        
        assertEquals(2, resultItem2.size());
    }
    
    @Test @Ignore
    public void testNewTopicByScene3() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000010"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        NewTopicBySceneCommand cmd = new NewTopicBySceneCommand();
        cmd.setSceneToken("EflmS6AN5qOrNFS3bjViBaIZSnLB_SSj3RwMcojT1rYXQrEitiL9miy4GHhPjtpTpWhX6ydmrPc8SO7k65f6mhmV6Yi1RajfzWCX96itiUvtkW9_pwoCjPzKE6D6djVc0uG79BFC6EIBJpWPhxLxdLXtLYwNCPsiaCOsHkOxrgg");
        cmd.setSubject("我发一个新帖试试看");
        cmd.setForumId(1L);
        cmd.setContentCategory(1001L);
        cmd.setContent("这是我心法的贴内容");
        String commandRelativeUri = "/ui/forum/newTopicByScene";
        
        ForumNewTopicBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumNewTopicBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhForumPosts> resultItem = new ArrayList<>();
        DSLContext dslContext = dbProvider.getDslContext();
        dslContext.select().from(EH_FORUM_POSTS) 
            .where(EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()))
            .and(EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(24210090697425925L))
            .and(EH_FORUM_POSTS.FORUM_ID.eq(1L))
            .and(EH_FORUM_POSTS.CATEGORY_ID.eq(1001L))
            .and(EH_FORUM_POSTS.CREATOR_TAG.eq("USER"))
            .and(EH_FORUM_POSTS.SUBJECT.eq("我发一个新帖试试看"))
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhForumPosts.class));
                return null;
        });
        
        assertEquals(1, resultItem.size());
    }
        
    @Test
    public void testNewTopicByScene4() {
            Integer namespaceId = 0;
            String userIdentifier = "12000000020"; // 管理员帐号
            String plainTexPassword = "123456";
            logon(null, userIdentifier, plainTexPassword);
            
            NewTopicBySceneCommand cmd = new NewTopicBySceneCommand();
            cmd.setSceneToken("ttggEZEFyEgKvu073JVxBn1_OqS9rU7TbTaqQJCuumwXQrEitiL9miy4GHhPjtpTkV50vhe84lSqx1AjJHupXhUJVr-EwUdkmafZaDG2aawEtYcN8vXWBmKu2SHmUDj7s6JJa6btlzpKXroEngpiLNk06w5MEGg4r-Jpc5VEMWk");
            cmd.setSubject("我发一个新帖试试看");
            cmd.setForumId(1L);
            cmd.setContentCategory(1001L);
            cmd.setContent("这是我心法的贴内容");
            String commandRelativeUri = "/ui/forum/newTopicByScene";
            ForumNewTopicBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ForumNewTopicBySceneRestResponse.class, context);
            
            assertNotNull("The reponse of may not be null", response);
            assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
            
            List<EhForumPosts> resultItem = new ArrayList<>();
            DSLContext dslContext = dbProvider.getDslContext();
            dslContext.select().from(EH_FORUM_POSTS) 
                .where(EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()))
                .and(EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(1000760L))
                .and(EH_FORUM_POSTS.FORUM_ID.eq(1L))
                .and(EH_FORUM_POSTS.CATEGORY_ID.eq(1001L))
                .and(EH_FORUM_POSTS.CREATOR_TAG.eq("USER"))
                .and(EH_FORUM_POSTS.SUBJECT.eq("我发一个新帖试试看"))
                .fetch().map((r) -> {
                	resultItem.add(ConvertHelper.convert(r, EhForumPosts.class));
                    return null;
            });
            
            assertEquals(1, resultItem.size());
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
        
        jsonFilePath = "data/json/3.7.x-test-data-category_160709.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-organization_member_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
       
    }
}

