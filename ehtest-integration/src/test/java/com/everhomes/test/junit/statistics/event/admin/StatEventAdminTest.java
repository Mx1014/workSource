// @formatter:off
package com.everhomes.test.junit.statistics.event.admin;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.statistics.event.ListEventPortalStatCommand;
import com.everhomes.rest.statistics.event.StatExecuteEventTaskCommand;
import com.everhomes.rest.statistics.event.StatListEventStatCommand;
import com.everhomes.rest.statistics.event.admin.EventAdminListEventPortalStatRestResponse;
import com.everhomes.rest.statistics.event.admin.EventAdminListEventStatRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventContentLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventContentLogs;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StatEventAdminTest extends BaseLoginAuthTestCase {

    // 执行统计任务
    private static final String executeEventTaskURL = "/stat/event/admin/executeEventTask";
    // 获取事件统计结果
    private static final String listEventStatURL = "/stat/event/admin/listEventStat";
    // 获取配置记录
    private static final String listEventPortalStatURL = "/stat/event/admin/listEventPortalStat";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 执行统计任务
    @Test
    public void testExecuteEventTask() {
        initData();
        StatExecuteEventTaskCommand cmd = new StatExecuteEventTaskCommand();
        cmd.setStartDate("20170810");
        cmd.setEndDate("20170810");

        RestResponseBase response = httpClientService.restPost(executeEventTaskURL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        // assertNotNull("response should be not null", response.getResponse());
    }

    private void initData() {
        String str = "[{\"sessionId\":\"1\",\"logId\":1,\"deviceTime\":111111,\"eventName\":\"portal_on_bottom_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"ServiceMarketMain\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":2,\"deviceTime\":111111,\"eventName\":\"portal_on_bottom_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"ServiceMarketMain\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":3,\"deviceTime\":111111,\"eventName\":\"portal_on_bottom_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"Biz\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":4,\"deviceTime\":111111,\"eventName\":\"portal_on_bottom_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"Activity\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":5,\"deviceTime\":111111,\"eventName\":\"portal_on_bottom_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"Me\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":6,\"deviceTime\":111111,\"eventName\":\"portal_on_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"Search\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":7,\"deviceTime\":111111,\"eventName\":\"portal_on_navigation_click\",\"version\":1,\"param\":{\"identifier\":\"Scan\",\"position\":\"1\"}},{\"sessionId\":\"1\",\"logId\":8,\"deviceTime\":111111,\"eventName\":\"launchpad_on_banner_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":9,\"deviceTime\":111111,\"eventName\":\"launchpad_on_banner_click\",\"version\":1,\"param\":{\"id\":\"2\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":10,\"deviceTime\":111111,\"eventName\":\"launchpad_on_bulletin_click\",\"version\":1,\"param\":{\"layoutId\":\"1\",\"location\":\"/home\"}},{\"sessionId\":\"1\",\"logId\":11,\"deviceTime\":111111,\"eventName\":\"launchpad_on_bulletin_click\",\"version\":1,\"param\":{\"layoutId\":\"2\",\"location\":\"/home\"}},{\"sessionId\":\"1\",\"logId\":12,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":13,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"2\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":14,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"3\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":15,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"4\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":16,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"5\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":17,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"6\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":18,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"7\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":19,\"deviceTime\":111111,\"eventName\":\"launchpad_on_launch_pad_item_click\",\"version\":1,\"param\":{\"id\":\"8\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":20,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_activity_item_click\",\"version\":1,\"param\":{\"topicId\":\"1\",\"forumId\":\"22\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":21,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_activity_item_click\",\"version\":1,\"param\":{\"topicId\":\"1\",\"forumId\":\"22\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":22,\"deviceTime\":111111,\"eventName\":\"launchpad_on_news_item_click\",\"version\":1,\"param\":{\"newsToken\":\"ZJZMbvOo-6hUmarjTE3PAExBz1qcJLCbupjn3oQE1vA\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":23,\"deviceTime\":111111,\"eventName\":\"launchpad_on_news_item_click\",\"version\":1,\"param\":{\"newsToken\":\"ZJZMbvOo-6hUmarjTE3PAExBz1qcJLCbupjn3oQE1vA\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":24,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_service_alliance_item_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":25,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_service_alliance_item_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":26,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_service_alliance_item_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":27,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_service_alliance_item_click\",\"version\":1,\"param\":{\"id\":\"2\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":28,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_service_alliance_item_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":29,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_biz_item_click\",\"version\":1,\"param\":{\"id\":\"2\",\"layoutId\":\"2\"}},{\"sessionId\":\"1\",\"logId\":30,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_biz_item_click\",\"version\":1,\"param\":{\"id\":\"1\",\"layoutId\":\"1\"}},{\"sessionId\":\"1\",\"logId\":31,\"deviceTime\":111111,\"eventName\":\"launchpad_on_oppush_biz_item_click\",\"version\":1,\"param\":{\"id\":\"2\",\"layoutId\":\"2\"}}]";

        DSLContext context = dbProvider.getDslContext();
        EhStatEventContentLogs log = context.selectFrom(Tables.EH_STAT_EVENT_CONTENT_LOGS).fetchAnyInto(EhStatEventContentLogs.class);
        log.setContent(str);
        EhStatEventContentLogsDao dao = new EhStatEventContentLogsDao(context.configuration());
        dao.update(log);
        // context.update(Tables.EH_STAT_EVENT_CONTENT_LOGS).set(Tables.EH_STAT_EVENT_CONTENT_LOGS.CONTENT, str).execute();
    }

    // 获取事件统计结果
    @Test
    public void testListEventStat() {
        testExecuteEventTask();
        StatListEventStatCommand cmd = new StatListEventStatCommand();
        cmd.setNamespaceId(999992);
        cmd.setParentId(1L);
        cmd.setIdentifier("ServiceMarketLayout:Navigator:Bizs");
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
        testExecuteEventTask();
        ListEventPortalStatCommand cmd = new ListEventPortalStatCommand();
        cmd.setNamespaceId(999992);
        cmd.setParentId(902L);
        cmd.setStatType((byte)2);
        cmd.setStartDate(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000);
        cmd.setEndDate(System.currentTimeMillis());

        EventAdminListEventPortalStatRestResponse response = httpClientService.restPost(listEventPortalStatURL, cmd, EventAdminListEventPortalStatRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        assertTrue(response.getResponse().getStatList().size() == 3);
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

        assertTrue(response.getResponse().getStatList().size() == 2);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before public void setUp() {
        super.newSetUp();
        logon();
    }

    @Override protected void initCustomData() {
        // String jsonFilePath = "data/json/stat-event-test-data-170802.json";
        String jsonFilePath = "data/json/stat-event-test-data1-170802.json";
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
