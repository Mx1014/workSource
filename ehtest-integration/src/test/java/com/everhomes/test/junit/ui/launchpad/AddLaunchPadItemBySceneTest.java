// @formatter:off
package com.everhomes.test.junit.ui.launchpad;

import static com.everhomes.server.schema.Tables.*;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.launchpad.ItemDisplayFlag;
import com.everhomes.rest.ui.launchpad.AddLaunchPadItemBySceneCommand;
//import com.everhomes.rest.ui.launchpad.AddLaunchPadItemBySceneCommand;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.server.schema.tables.pojos.EhUserLaunchPadItems;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class AddLaunchPadItemBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testAddLaunchPadItemByScene1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
//        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        
        String commandRelativeUri = "/ui/launchpad/addLaunchPadItemByScene";
        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        cmd.setId(24L);
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");

        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        
        assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
      
        List<EhUserLaunchPadItems> resultItem = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_LAUNCH_PAD_ITEMS) 
            .where(EH_USER_LAUNCH_PAD_ITEMS.OWNER_TYPE.eq("EhCommunities"))
            .and(EH_USER_LAUNCH_PAD_ITEMS.OWNER_ID.eq(24210090697425925L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.USER_ID.eq(10001L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.ITEM_ID.eq(24L))
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhUserLaunchPadItems.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user item info may not be null", resultItem);
        assertEquals(1, resultItem.size());
        
        EhUserLaunchPadItems userItem = resultItem.get(0);
        
        byte flag = resultItem.get(0).getDisplayFlag();
        
        assertEquals(ItemDisplayFlag.DISPLAY.getCode(), flag);
        
        assertEquals(SceneType.DEFAULT.getCode(), userItem.getSceneType());
        
    }
    
    @Test
    public void testAddLaunchPadItemByScene2() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/ui/launchpad/addLaunchPadItemByScene";
        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        cmd.setId(47L);
        cmd.setSceneToken("fIB4fBRQ26xciIi6txwidlyf-DzNfs11myX5Fd_qE1UXQrEitiL9miy4GHhPjtpTlO4jeFDn_vz6rKJnUwQ9Iovgi3TC9lqz3Khq-42upiD78FLa4loT1DgJ4gx8t4GnLMqxtgIBK-pV2HORGE2r9d0eomL0M4CCyxLDK0Knh_E");

        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        
        assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
      
        List<EhUserLaunchPadItems> resultItem = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_LAUNCH_PAD_ITEMS) 
            .where(EH_USER_LAUNCH_PAD_ITEMS.OWNER_TYPE.eq("EhOrganizations"))
            .and(EH_USER_LAUNCH_PAD_ITEMS.OWNER_ID.eq(1000750L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.USER_ID.eq(10001L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.ITEM_ID.eq(47L))
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhUserLaunchPadItems.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user item info may not be null", resultItem);
        assertEquals(1, resultItem.size());
        
        EhUserLaunchPadItems userItem = resultItem.get(0);
        
        byte flag = resultItem.get(0).getDisplayFlag();
        
        assertEquals(ItemDisplayFlag.DISPLAY.getCode(), flag);
        
        assertEquals(SceneType.PM_ADMIN.getCode(), userItem.getSceneType());
        
    }
    
    @Test
    public void testAddLaunchPadItemByScene3() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000010"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/ui/launchpad/addLaunchPadItemByScene";
        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        cmd.setId(24L);
        cmd.setSceneToken("EflmS6AN5qOrNFS3bjViBaIZSnLB_SSj3RwMcojT1rYXQrEitiL9miy4GHhPjtpTpWhX6ydmrPc8SO7k65f6mhmV6Yi1RajfzWCX96itiUvtkW9_pwoCjPzKE6D6djVc0uG79BFC6EIBJpWPhxLxdLXtLYwNCPsiaCOsHkOxrgg");

        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class, context);
        
        assertNotNull("The reponse of may not be null", response);
      
        assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhUserLaunchPadItems> resultItem = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_LAUNCH_PAD_ITEMS) 
            .where(EH_USER_LAUNCH_PAD_ITEMS.OWNER_TYPE.eq("EhCommunities"))
            .and(EH_USER_LAUNCH_PAD_ITEMS.OWNER_ID.eq(24210090697425925L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.USER_ID.eq(10010L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.ITEM_ID.eq(24L))
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhUserLaunchPadItems.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user item info may not be null", resultItem);
        assertEquals(1, resultItem.size());
        
        EhUserLaunchPadItems userItem = resultItem.get(0);
        
        byte flag = resultItem.get(0).getDisplayFlag();
        
        assertEquals(ItemDisplayFlag.DISPLAY.getCode(), flag);
        
        assertEquals(SceneType.DEFAULT.getCode(), userItem.getSceneType());
        
    }
    
    @Test
    public void testAddLaunchPadItemByScene4() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000020"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
//        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        
        String commandRelativeUri = "/ui/launchpad/addLaunchPadItemByScene";
        AddLaunchPadItemBySceneCommand cmd = new AddLaunchPadItemBySceneCommand();
        cmd.setId(46L);
        cmd.setSceneToken("ttggEZEFyEgKvu073JVxBn1_OqS9rU7TbTaqQJCuumwXQrEitiL9miy4GHhPjtpTkV50vhe84lSqx1AjJHupXhUJVr-EwUdkmafZaDG2aawEtYcN8vXWBmKu2SHmUDj7s6JJa6btlzpKXroEngpiLNk06w5MEGg4r-Jpc5VEMWk");

        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        
        assertTrue("The user scenes should be get from server, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
      
        List<EhUserLaunchPadItems> resultItem = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_LAUNCH_PAD_ITEMS) 
            .where(EH_USER_LAUNCH_PAD_ITEMS.OWNER_TYPE.eq("EhOrganizations"))
            .and(EH_USER_LAUNCH_PAD_ITEMS.OWNER_ID.eq(1000760L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.USER_ID.eq(10020L))
            .and(EH_USER_LAUNCH_PAD_ITEMS.ITEM_ID.eq(46L))
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhUserLaunchPadItems.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user item info may not be null", resultItem);
        assertEquals(1, resultItem.size());
        
        EhUserLaunchPadItems userItem = resultItem.get(0);
        
        byte flag = resultItem.get(0).getDisplayFlag();
        
        assertEquals(ItemDisplayFlag.DISPLAY.getCode(), flag);
        
        assertEquals(SceneType.PARK_TOURIST.getCode(), userItem.getSceneType());
        
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

