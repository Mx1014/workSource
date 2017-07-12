//@formatter:off
package com.everhomes.test.junit.business;

import com.everhomes.rest.business.ListBusinessPromotionEntitiesCommand;
import com.everhomes.rest.business.ListBusinessPromotionEntitiesRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xq.tian on 2017/1/10.
 */
public class BusinessTest extends BaseLoginAuthTestCase {

    // 1. 电商运营数据API
    private static final String LIST_BUSINESS_PROMOTION_ENTITY_URL = "/business/listBusinessPromotionEntities";

    @Test
    public void testListBusinessPromotionEntities() {
        logon();
        ListBusinessPromotionEntitiesCommand cmd = new ListBusinessPromotionEntitiesCommand();
        cmd.setPageSize(3);

        ListBusinessPromotionEntitiesRestResponse response = httpClientService.restPost(LIST_BUSINESS_PROMOTION_ENTITY_URL, cmd, ListBusinessPromotionEntitiesRestResponse.class);
        assertTrue("Response should be OK "+ StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("The business promotion response should be not null.", response);
        assertNotNull("The business promotion response should be not null.", response.getResponse());

        assertTrue("The business promotion list size should be 3, actual is "+response.getResponse().getEntities().size(), response.getResponse().getEntities().size() == 3);
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        jsonFilePath = "data/json/module-promotion-1.0-test-data-170110.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    public void logon() {
        Integer namespaceId = 0;
        String identifierToken = "12900000001";
        String password = "123456";
        logon(namespaceId, identifierToken, password);
    }

    @Before
    public void setUp(){
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
}
