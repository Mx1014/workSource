// @formatter:off
package com.everhomes.test.junit.statistics.event.admin;

import com.everhomes.rest.statistics.event.ListEventPortalStatCommand;
import com.everhomes.rest.statistics.event.StatListEventStatCommand;
import com.everhomes.rest.statistics.event.admin.EventAdminListEventPortalStatRestResponse;
import com.everhomes.rest.statistics.event.admin.EventAdminListEventStatRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StatEventAdminTest extends BaseLoginAuthTestCase {

    // 获取事件统计结果
    private static final String listEventStatURL = "/stat/event/admin/listEventStat";
    // 获取配置记录
    private static final String listEventPortalStatURL = "/stat/event/admin/listEventPortalStat";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 获取事件统计结果
    @Test
    public void testListEventStat() {
        StatListEventStatCommand cmd = new StatListEventStatCommand();
        cmd.setNamespaceId(999992);
        cmd.setParentId(1L);
        cmd.setIdentifier("NavigationBizs1");
        cmd.setStartDate(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000);
        cmd.setEndDate(System.currentTimeMillis());

        EventAdminListEventStatRestResponse response = httpClientService.restPost(listEventStatURL, cmd, EventAdminListEventStatRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());
    }

    // 获取配置记录
    @Test
    public void testListEventPortalStat() {
        ListEventPortalStatCommand cmd = new ListEventPortalStatCommand();
        cmd.setNamespaceId(999992);
        cmd.setParentId(0L);
        cmd.setStatType((byte)1);
        cmd.setStartDate(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000);
        cmd.setEndDate(System.currentTimeMillis());

        EventAdminListEventPortalStatRestResponse response = httpClientService.restPost(listEventPortalStatURL, cmd, EventAdminListEventPortalStatRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        assertTrue(response.getResponse().size() == 3);
    }

    // 获取配置记录1
    @Test
    public void testListEventPortalStat1() {
        ListEventPortalStatCommand cmd = new ListEventPortalStatCommand();
        cmd.setNamespaceId(999992);
        cmd.setParentId(1L);
        cmd.setStartDate(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000);
        cmd.setEndDate(System.currentTimeMillis());

        EventAdminListEventPortalStatRestResponse response = httpClientService.restPost(listEventPortalStatURL, cmd, EventAdminListEventPortalStatRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        assertTrue(response.getResponse().size() == 2);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before public void setUp() {
        super.newSetUp();
        logon();
    }

    @Override protected void initCustomData() {
        String jsonFilePath = "data/json/stat-event-test-data-170802.json";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        // jsonFilePath = "data/json/organizationfile-1.0-test-data-170217.json";
        // fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After public void tearDown() {
        super.tearDown();
        logoff();
    }
}
