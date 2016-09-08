// @formatter:off
package com.everhomes.test.junit.payment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.payment.GetCardUserStatisticsCommand;
import com.everhomes.rest.payment.GetCardUserStatisticsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class GetCardUserStatisticsTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testGetCardUserStatistics() {
        String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/payment/getCardUserStatistics";
        GetCardUserStatisticsCommand cmd = new GetCardUserStatisticsCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        GetCardUserStatisticsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, GetCardUserStatisticsRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertEquals("User count should be equal", 2, response.getResponse().getTotalCount().intValue());
        assertEquals(1,response.getResponse().getCardUserCount().intValue());
        
        
    }
    
    @After
    public void tearDown() {
    	super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/3.4.x-test-data-cardissuer_160617.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

