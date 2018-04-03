package com.everhomes.test.junit.user;

import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.user.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhUserNotificationSettingsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xq.tian on 2017/4/18.
 */
public class UserNotificationSettingTest extends BaseLoginAuthTestCase {

    //1. 设置会话推送免打扰
    private static final String updateUserNotificationSettingUrl = "/user/updateUserNotificationSetting";
    //2. 获取会话推送免打扰设置
    private static final String getUserNotificationSettingUrl = "/user/getUserNotificationSetting";

    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";


    //1. 设置会话推送免打扰
    @Test
    public void testUpdateMuteNotificationFlag() {
        logon();
        UpdateUserNotificationSettingCommand cmd = new UpdateUserNotificationSettingCommand();
        Long targetId = 212505L;
        cmd.setTargetId(targetId);
        cmd.setMuteFlag(UserMuteNotificationFlag.MUTE.getCode());
        cmd.setTargetType(EntityType.USER.getCode());

        UpdateUserNotificationSettingRestResponse response = httpClientService.restPost(updateUserNotificationSettingUrl, cmd, UpdateUserNotificationSettingRestResponse.class);
        assertNotNull(response);
        assertNotNull(response.getResponse());

        assertEquals("targetId should be equal", response.getResponse().getTargetId(), targetId);

        EhUserNotificationSettingsRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_USER_NOTIFICATION_SETTINGS)
                .where(Tables.EH_USER_NOTIFICATION_SETTINGS.TARGET_ID.eq(targetId))
                .and(Tables.EH_USER_NOTIFICATION_SETTINGS.TARGET_TYPE.eq(EntityType.USER.getCode()))
                .fetchAny();

        assertNotNull(record);
        assertTrue("mute flag should be mute", record.getMuteFlag() == UserMuteNotificationFlag.MUTE.getCode());

        cmd = new UpdateUserNotificationSettingCommand();
        cmd.setTargetId(targetId);
        cmd.setMuteFlag(UserMuteNotificationFlag.NONE.getCode());
        cmd.setTargetType(EntityType.USER.getCode());

        response = httpClientService.restPost(updateUserNotificationSettingUrl, cmd, UpdateUserNotificationSettingRestResponse.class);
        assertNotNull(response);
        assertNotNull(response.getResponse());

        assertEquals("targetId should be equal", response.getResponse().getTargetId(), targetId);

        record = dbProvider.getDslContext().selectFrom(Tables.EH_USER_NOTIFICATION_SETTINGS)
                .where(Tables.EH_USER_NOTIFICATION_SETTINGS.TARGET_ID.eq(targetId))
                .and(Tables.EH_USER_NOTIFICATION_SETTINGS.TARGET_TYPE.eq(EntityType.USER.getCode()))
                .fetchAny();

        assertNotNull(record);
        assertTrue("mute flag should be none", record.getMuteFlag() == UserMuteNotificationFlag.NONE.getCode());
    }

    //1. 获取会话推送免打扰设置
    @Test
    public void testGetMuteNotificationFlag() {
        logon();

        testUpdateMuteNotificationFlag();

        GetUserNotificationSettingCommand cmd = new GetUserNotificationSettingCommand();
        Long targetId = 212505L;
        cmd.setTargetId(targetId);
        cmd.setTargetType(EntityType.USER.getCode());

        GetUserNotificationSettingRestResponse response = httpClientService.restPost(getUserNotificationSettingUrl, cmd, GetUserNotificationSettingRestResponse.class);
        assertNotNull(response);
        assertNotNull(response.getResponse());

        assertEquals("targetId should be equal", response.getResponse().getTargetId(), targetId);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before
    public void setUp() {
        super.newSetUp();
    }

    @Override
    protected void initCustomData() {
        // String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        // String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        // jsonFilePath = "data/json/organizationfile-1.0-test-data-170217.json";
        // fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
}
