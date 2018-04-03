package com.everhomes.test.junit.oauth2client;

import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.After;
import org.junit.Before;

public class OAuth2ClientTest extends BaseLoginAuthTestCase {

    //1. 需要和第三方进行OAuth2认证的功能先访问该接口获取重定向的url
    private static final String REDIRECT_URL = "/oauth2cli/redirect/{vendor}";
    //2. 用户授权后第三方重定向到此，此接口获取第三方返回的token
    private static final String CALLBACK_URL = "/oauth2cli/callback/{vendor}";
    //3. 通用的调用第三方api接口，原样返回第三方返回的数据
    private static final String API_URL = "/oauth2cli/api/{vendor}";

    private String userIdentifier = "12900000001";
    private String plainTextPwd = "123456";
    private Integer namespaceId = 0;

    /*//1. 需要和第三方进行OAuth2认证的功能先访问该接口获取重定向的url
    @Test
    public void testRedirect() {
        logon();
        RestResponseBase response = httpClientService.restPost(REDIRECT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //2. 用户授权后第三方重定向到此，此接口获取第三方返回的token
    @Test
    public void testCallback() {
        logon();
        RestResponseBase response = httpClientService.restPost(CALLBACK_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }*/

    //3. 通用的调用第三方api接口，原样返回第三方返回的数据
    /*@Test
    public void testApi() {
        logon();
        OAuth2ClientApiCommand cmd = new OAuth2ClientApiCommand();
        cmd.setContentType("");
        cmd.setMethod("POST");
        cmd.setUrl("");
        cmd.setParam("");

        RestResponse response = httpClientService.restPost(API_URL, cmd, RestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }*/

    @Before
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        jsonFilePath = "data/json/oauth2-client-1.0-test-data-170311.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
}