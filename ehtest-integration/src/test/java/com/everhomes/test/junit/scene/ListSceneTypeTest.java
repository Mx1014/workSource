// @formatter:off
package com.everhomes.test.junit.scene;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.rest.scene.SceneTypeInfoDTO;
import com.everhomes.rest.scene.admin.SceneListSceneTypesRestResponse;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ListSceneTypeTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListSceneTypes() {
        Integer namespaceId = 0;
        String userIdentifier = "12900000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        ListSceneTypesCommand cmd = new ListSceneTypesCommand();
        cmd.setNamespaceId(0);
        
        String commandRelativeUri = "/admin/scene/listSceneTypes";
        SceneListSceneTypesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            SceneListSceneTypesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(6, response.getResponse().size());
        int flag = 0;
        for(SceneTypeInfoDTO scene : response.getResponse()) {
            SceneType sceneType = SceneType.fromCode(scene.getName());
            if(SceneType.DEFAULT == sceneType) {
                flag = flag | 0x01;
            }
            if(SceneType.PM_ADMIN == sceneType) {
                flag = flag | 0x02;
            }
            if(SceneType.FAMILY == sceneType) {
                flag = flag | 0x04;
            }
            if(SceneType.PARK_TOURIST == sceneType) {
                flag = flag | 0x08;
            }
            if(SceneType.ENTERPRISE == sceneType) {
                flag = flag | 0x10;
            }
            if(SceneType.ENTERPRISE_NOAUTH == sceneType) {
                flag = flag | 0x20;
            }
        }
        assertEquals(0x3F, flag);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

