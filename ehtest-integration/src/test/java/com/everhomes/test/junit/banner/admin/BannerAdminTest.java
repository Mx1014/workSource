// @formatter:off
package com.everhomes.test.junit.banner.admin;

import com.everhomes.rest.banner.admin.BannerCreateBannerRestResponse;
import com.everhomes.rest.banner.admin.CreateBannerCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BannerAdminTest extends BaseLoginAuthTestCase {

    // 创建banner
    private static final String createBannerURL = "/admin/banner/createBanner";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 创建banner
    @Test
    public void testCreateBanner() {
        CreateBannerCommand cmd = new CreateBannerCommand();
        cmd.setNamespaceId(1);
        cmd.setName("name");
        List<Long> scopesList = new ArrayList<>();
        scopesList.add(1L);
        cmd.setScopes(scopesList);
        cmd.setPosterPath("posterPath");
        cmd.setTargetType("targetType");
        cmd.setTargetData("targetData");

        BannerCreateBannerRestResponse response = httpClientService.restPost(createBannerURL, cmd, BannerCreateBannerRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        // assertNotNull("response should be not null", response.getResponse());
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
