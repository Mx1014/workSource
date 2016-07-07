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
import com.everhomes.rest.ui.launchpad.ReorderLaunchPadItemBySceneCommand;
import com.everhomes.rest.ui.launchpad.ReorderLaunchPadItemBySceneCommand;
import com.everhomes.rest.ui.user.LaunchPadItemSort;
//import com.everhomes.rest.ui.launchpad.ReorderLaunchPadItemBySceneCommand;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.server.schema.tables.pojos.EhUserLaunchPadItems;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class ReorderLaunchPadItemBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testReorderLaunchPadItemByScene1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/ui/launchpad/reorderLaunchPadItemByScene";
        ReorderLaunchPadItemBySceneCommand cmd = new ReorderLaunchPadItemBySceneCommand();
        
        List<LaunchPadItemSort> sorts = new ArrayList<LaunchPadItemSort>();
        LaunchPadItemSort sort = new LaunchPadItemSort();
        sort.setId(36L);
        sort.setDefaultOrder(1);
        sorts.add(sort);
        LaunchPadItemSort sort1 = new LaunchPadItemSort();
        sort1.setId(7L);
        sort1.setDefaultOrder(2);
        sorts.add(sort1);
        
        LaunchPadItemSort sort2 = new LaunchPadItemSort();
        sort2.setId(6L);
        sort2.setDefaultOrder(3);
        sorts.add(sort2);
        
        LaunchPadItemSort sort3 = new LaunchPadItemSort();
        sort3.setId(1L);
        sort3.setDefaultOrder(4);
        sorts.add(sort3);
        cmd.setSorts(sorts);
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
            .and(EH_USER_LAUNCH_PAD_ITEMS.DISPLAY_FLAG.eq(ItemDisplayFlag.DISPLAY.getCode()))
            .and(EH_USER_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(SceneType.DEFAULT.getCode()))
            .orderBy(EH_USER_LAUNCH_PAD_ITEMS.DEFAULT_ORDER)
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhUserLaunchPadItems.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user item info may not be null", resultItem);
        assertEquals(4, resultItem.size());
        
        boolean flag = false;
        
        for (EhUserLaunchPadItems ehUserLaunchPadItems : resultItem) {
			if(ehUserLaunchPadItems.getDefaultOrder() == 1 && ehUserLaunchPadItems.getItemId() == 36L){
				flag = true;
			}else if(ehUserLaunchPadItems.getDefaultOrder() == 2 && ehUserLaunchPadItems.getItemId() == 7L){
				flag = true;
			}else if(ehUserLaunchPadItems.getDefaultOrder() == 3 && ehUserLaunchPadItems.getItemId() == 6L){
				flag = true;
			}else if(ehUserLaunchPadItems.getDefaultOrder() == 4 && ehUserLaunchPadItems.getItemId() == 1L){
				flag = true;
			}else{
				flag = false;
			}
		}
        
        assertEquals(true, flag);
    }
    
    @Test
    public void testReorderLaunchPadItemByScene2() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/ui/launchpad/reorderLaunchPadItemByScene";
        ReorderLaunchPadItemBySceneCommand cmd = new ReorderLaunchPadItemBySceneCommand();
        List<LaunchPadItemSort> sorts = new ArrayList<LaunchPadItemSort>();
        LaunchPadItemSort sort = new LaunchPadItemSort();
        sort.setId(14L);
        sort.setDefaultOrder(1);
        sorts.add(sort);
        LaunchPadItemSort sort1 = new LaunchPadItemSort();
        sort1.setId(39L);
        sort1.setDefaultOrder(2);
        sorts.add(sort1);
        LaunchPadItemSort sort2 = new LaunchPadItemSort();
        sort2.setId(10L);
        sort2.setDefaultOrder(3);
        sorts.add(sort2);
        LaunchPadItemSort sort3 = new LaunchPadItemSort();
        sort3.setId(9L);
        sort3.setDefaultOrder(4);
        sorts.add(sort3);
        cmd.setSorts(sorts);
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
            .and(EH_USER_LAUNCH_PAD_ITEMS.DISPLAY_FLAG.eq(ItemDisplayFlag.DISPLAY.getCode()))
            .and(EH_USER_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(SceneType.PM_ADMIN.getCode()))
            .orderBy(EH_USER_LAUNCH_PAD_ITEMS.DEFAULT_ORDER)
            .fetch().map((r) -> {
            	resultItem.add(ConvertHelper.convert(r, EhUserLaunchPadItems.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user item info may not be null", resultItem);
        assertEquals(4, resultItem.size());
        
        boolean flag = false;
        
        for (EhUserLaunchPadItems ehUserLaunchPadItems : resultItem) {
			if(ehUserLaunchPadItems.getDefaultOrder() == 1 && ehUserLaunchPadItems.getItemId() == 14L){
				flag = true;
			}else if(ehUserLaunchPadItems.getDefaultOrder() == 2 && ehUserLaunchPadItems.getItemId() == 39L){
				flag = true;
			}else if(ehUserLaunchPadItems.getDefaultOrder() == 3 && ehUserLaunchPadItems.getItemId() == 10L){
				flag = true;
			}else if(ehUserLaunchPadItems.getDefaultOrder() == 4 && ehUserLaunchPadItems.getItemId() == 9L){
				flag = true;
			}else{
				flag = false;
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
        
        jsonFilePath = "data/json/3.4.x-test-data-organization_member_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-user-launch_pad_items_160616.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

