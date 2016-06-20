// @formatter:off
package com.everhomes.test.junit.payment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.payment.GetCardUserStatisticsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class getCardUserStatisticsTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testGetCardUserStatistics() {
        Integer namespaceId = 999990;
        
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/payment/getCardUserStatistics";
        GetCardUserStatisticsRestResponse response = httpClientService.restGet(commandRelativeUri, null, GetCardUserStatisticsRestResponse.class,context);
        
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

