// @formatter:off
package com.everhomes.test.junit.ui.launchpad;

import com.everhomes.rest.launchpad.CategryItemDTO;
import com.everhomes.rest.launchpad.ItemGroup;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.rest.ui.launchpad.GetLaunchPadItemsBySceneCommand;
import com.everhomes.rest.ui.launchpad.LaunchpadGetLaunchPadItemsBySceneRestResponse;
import com.everhomes.rest.ui.launchpad.LaunchpadGetAllCategryItemsBySceneRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GetAllCategryItemsBySceneTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testGetAllCategryItemsByScene1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        GetLaunchPadItemsBySceneCommand cmd = new GetLaunchPadItemsBySceneCommand();
        cmd.setItemGroup(ItemGroup.BIZS.getCode());
        cmd.setItemLocation("/home");
        cmd.setSceneToken("K06rblY1jXyf_8BDY9W884n2RqcC5riME1F2g2bZRBMXQrEitiL9miy4GHhPjtpT74ANDdQD58BEJzwPAC938zx40-TXSnATOWnEatD6NvWiP3sBm2g5h-wU5iGG2BaH5-n0NVMSkGOVNb1JiAY61A");
        
        
        String commandRelativeUri = "/ui/launchpad/getAllCategryItemsByScene";
        LaunchpadGetAllCategryItemsBySceneRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LaunchpadGetAllCategryItemsBySceneRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(4, response.getResponse().size());

        List<CategryItemDTO> dtos =  response.getResponse();

        List<Long> categryIds = new ArrayList<Long>();
        categryIds.add(1L);
        categryIds.add(2L);
        categryIds.add(3L);
        categryIds.add(4L);
        boolean flag = false;
        for (CategryItemDTO dto : dtos ) {
            if(categryIds.contains(dto.getCategryId())){
                flag = true;
            }else{
                flag = false;
                break;
            }

            if(2L == dto.getCategryId()){
                List<Long> ids = new ArrayList<Long>();
                ids.add(48L);
                List<LaunchPadItemDTO> items = dto.getLaunchPadItems();
                flag = false;
                for (LaunchPadItemDTO item : items) {
                    if(ids.contains(item.getId())){
                        flag = true;
                        break;
                    }
                }
            }
            assertEquals(true, flag);

            if(3L == dto.getCategryId()){
                List<Long> ids = new ArrayList<Long>();
                ids.add(24L);
                List<LaunchPadItemDTO> items = dto.getLaunchPadItems();
                for (LaunchPadItemDTO item : items) {
                    if(ids.contains(item.getId())){
                        flag = true;
                        break;
                    }
                }
            }

            assertEquals(true, flag);
            if(4L == dto.getCategryId()){
                List<Long> ids = new ArrayList<Long>();
                ids.add(25L);
                ids.add(33L);
                List<LaunchPadItemDTO> items = dto.getLaunchPadItems();
                for (LaunchPadItemDTO item : items) {
                    if(ids.contains(item.getId())){
                        flag = true;
                        break;
                    }
                }
            }

            assertEquals(true, flag);
        }
        

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
        
        jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-organization_member_160605.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-user-launch_pad_items_160616.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-businesses_160704.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        jsonFilePath = "data/json/item-service-categry_20161020.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

