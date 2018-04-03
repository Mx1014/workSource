// @formatter:off
package com.everhomes.test.junit.user.admin;

import com.everhomes.rest.user.admin.ListUserAppealLogsCommand;
import com.everhomes.rest.user.admin.UpdateUserAppealLogCommand;
import com.everhomes.rest.user.admin.UserListUserAppealLogsRestResponse;
import com.everhomes.rest.user.admin.UserUpdateUserAppealLogRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhUserAppealLogsRecord;
import com.everhomes.server.schema.tables.records.EhUserIdentifiersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserAdminTest extends BaseLoginAuthTestCase {

    // 用户申诉列表
    private static final String listUserAppealLogsURL = "/admin/user/listUserAppealLogs";
    // 修改用户申诉状态
    private static final String updateUserAppealLogURL = "/admin/user/updateUserAppealLog";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 用户申诉列表
    @Test
    public void testListUserAppealLogs() {
        ListUserAppealLogsCommand cmd = new ListUserAppealLogsCommand();
        // cmd.setAnchor(1L);
        cmd.setPageSize(2);
        cmd.setStatus((byte) 1);

        UserListUserAppealLogsRestResponse response = httpClientService.restPost(listUserAppealLogsURL, cmd, UserListUserAppealLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        assertTrue(response.getResponse().getAppealLogs().size() == 2);
        assertTrue(response.getResponse().getNextPageAnchor() == 1);
    }

    // 修改用户申诉状态
    @Test
    public void testUpdateUserAppealLog() {
        UpdateUserAppealLogCommand cmd = new UpdateUserAppealLogCommand();
        cmd.setId(1L);
        cmd.setStatus((byte) 0);

        UserUpdateUserAppealLogRestResponse response = httpClientService.restPost(updateUserAppealLogURL, cmd, UserUpdateUserAppealLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        DSLContext dslContext = dbProvider.getDslContext();
        EhUserAppealLogsRecord record = dslContext.selectFrom(Tables.EH_USER_APPEAL_LOGS)
                .where(Tables.EH_USER_APPEAL_LOGS.STATUS.eq((byte) 0))
                .and(Tables.EH_USER_APPEAL_LOGS.ID.eq(1L))
                .fetchAny();

        assertNotNull(record);
    }

    // 修改用户申诉状态
    @Test
    public void testUpdateUserAppealLog1() {
        UpdateUserAppealLogCommand cmd = new UpdateUserAppealLogCommand();
        cmd.setId(1L);
        cmd.setStatus((byte) 2);

        UserUpdateUserAppealLogRestResponse response = httpClientService.restPost(updateUserAppealLogURL, cmd, UserUpdateUserAppealLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        DSLContext dslContext = dbProvider.getDslContext();
        EhUserAppealLogsRecord appealLogRecord = dslContext.selectFrom(Tables.EH_USER_APPEAL_LOGS)
                .where(Tables.EH_USER_APPEAL_LOGS.STATUS.eq((byte) 2))
                .and(Tables.EH_USER_APPEAL_LOGS.ID.eq(1L))
                .fetchAny();

        assertNotNull(appealLogRecord);

        EhUserIdentifiersRecord identifierRecord = dslContext.selectFrom(Tables.EH_USER_IDENTIFIERS)
                .where(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(appealLogRecord.getNewIdentifier()))
                .and(Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(appealLogRecord.getOwnerUid()))
                .fetchAny();

        assertNotNull(identifierRecord);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before public void setUp() {
        super.newSetUp();
        logon();
    }

    @Override protected void initCustomData() {
        String jsonFilePath = "data/json/reset-identifier-1.0-test-data-170630.json";
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
