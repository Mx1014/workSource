// @formatter:off
package com.everhomes.test.junit.community.admin;

import com.everhomes.rest.community.GetCommunityAuthPopupConfigCommand;
import com.everhomes.rest.community.UpdateCommunityAuthPopupConfigCommand;
import com.everhomes.rest.community.admin.CommunityGetCommunityAuthPopupConfigRestResponse;
import com.everhomes.rest.community.admin.CommunityUpdateCommunityAuthPopupConfigRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommunityAdminTest extends BaseLoginAuthTestCase {

    // 修改用户认证弹窗设置
    private static final String updateCommunityAuthPopupConfigURL = "/admin/community/updateCommunityAuthPopupConfig";
    // 获取用户认证弹窗设置
    private static final String getCommunityAuthPopupConfigURL = "/admin/community/getCommunityAuthPopupConfig";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 修改用户认证弹窗设置
    @Test
    public void testUpdateCommunityAuthPopupConfig() {
        UpdateCommunityAuthPopupConfigCommand cmd = new UpdateCommunityAuthPopupConfigCommand();
        cmd.setNamespaceId(1);
        cmd.setStatus((byte) 0);

        CommunityUpdateCommunityAuthPopupConfigRestResponse response = httpClientService.restPost(updateCommunityAuthPopupConfigURL, cmd, CommunityUpdateCommunityAuthPopupConfigRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        // assertNotNull("response should be not null", response.getResponse());
    }

    // 获取用户认证弹窗设置
    @Test
    public void testGetCommunityAuthPopupConfig() {
        GetCommunityAuthPopupConfigCommand cmd = new GetCommunityAuthPopupConfigCommand();
        cmd.setNamespaceId(1);

        CommunityGetCommunityAuthPopupConfigRestResponse response = httpClientService.restPost(getCommunityAuthPopupConfigURL, cmd, CommunityGetCommunityAuthPopupConfigRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before public void setUp() {
        super.newSetUp();
        logon();
    }

    @Override protected void initCustomData() {
        // String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        // String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        // jsonFilePath = "data/json/organizationfile-1.0-test-data-170217.json";
        // fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After public void tearDown() {
        super.tearDown();
        logoff();
    }
}
