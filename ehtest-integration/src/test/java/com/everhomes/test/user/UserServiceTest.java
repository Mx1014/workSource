// @formatter:off
package com.everhomes.test.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.rest.user.GetUserInfoRestResponse;
import com.everhomes.rest.user.LogonCommand;
import com.everhomes.test.core.BaseServerTestCase;
import com.everhomes.test.core.UserContext;
import com.everhomes.test.core.http.HttpClientService;
import com.everhomes.util.StringHelper;

public class UserServiceTest extends BaseServerTestCase {
    
    @Autowired
    private HttpClientService httpClientService;
    
    private UserContext context;
    
    @Before
    public void setUp() {
        context = new UserContext(httpClientService);
        LogonCommand cmd = new LogonCommand();
        cmd.setDeviceIdentifier("none");
        cmd.setNamespaceId(0);
        cmd.setUserIdentifier("13927485221");
        cmd.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        boolean result = context.logon(cmd);
        assertTrue("User should login successfully, context=" + context, result);
    }
    
    @Test
    public void testGetUserInfo() {
        String commandRelativeUri = "/user/getUserInfo";
        GetUserInfoRestResponse response = httpClientService.restGet(commandRelativeUri, null, GetUserInfoRestResponse.class);
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }
    
    @After
    public void tearDown() {
        if(context != null) {
            boolean result = context.logoff();
            assertTrue("User should logoff successfully, context=" + context, result);
        }
    }
}

