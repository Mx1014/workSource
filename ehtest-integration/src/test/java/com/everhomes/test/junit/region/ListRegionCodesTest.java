// @formatter:off
package com.everhomes.test.junit.region;

import com.everhomes.rest.region.ListRegionCodesRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ListRegionCodesTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        // 清除表数据
        tuncateDbData();

        // 初始化系统数据（如系统帐号、权限、配置等）
        initSystemData();

        // 初始化自定义数据
        initCustomData();

        // 清除缓存
        clearRedisCache();
    }
    
    @Test
    public void testListRegionCodes() {
        String commandRelativeUri = "/region/listRegionCodes";
        ListRegionCodesRestResponse response = httpClientService.restGet(commandRelativeUri, null, ListRegionCodesRestResponse.class, context);
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The user scenes should be get from server, response=" +
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(2, response.getResponse().size());
        assertEquals("X", response.getResponse().get(0).getFirstLetter());
        assertEquals("Z", response.getResponse().get(1).getFirstLetter());
    }

    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
    	String jsonFilePath = "data/json/region_code-test-data-161011.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

    }
}

