// @formatter:off
package com.everhomes.test.junit.statistics.event;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.statistics.event.EventPostDeviceRestResponse;
import com.everhomes.rest.statistics.event.StatPostDeviceCommand;
import com.everhomes.rest.statistics.event.StatPostLogFileCommand;
import com.everhomes.rest.statistics.event.StatPostLogFileDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhStatEventAppAttachmentLogsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StatEventTest extends BaseLoginAuthTestCase {

    // 上传App log文件
    private static final String postLogFileURL = "/stat/event/postLogFile";
    // 上传设备信息, 返回sessionId和上传策略
    private static final String postDeviceURL = "/stat/event/postDevice";
    // 上传事件log
    private static final String postLogURL = "/stat/event/postLog";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 上传App log文件
    @Test
    public void testPostLogFile() {
        StatPostLogFileCommand cmd = new StatPostLogFileCommand();
        List<StatPostLogFileDTO> filesList = new ArrayList<>();
        StatPostLogFileDTO files = new StatPostLogFileDTO();
        files.setSessionId("sessionId");
        files.setUri("uri");
        filesList.add(files);
        cmd.setFiles(filesList);

        RestResponseBase response = httpClientService.restPost(postLogFileURL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        // assertNotNull("response should be not null", response.getResponse());

        Result<EhStatEventAppAttachmentLogsRecord> records = dbProvider.getDslContext().selectFrom(Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS).fetch();

        assertTrue(records != null);
        assertTrue(records.size() == 1);
    }

    // 上传设备信息, 返回sessionId和上传策略
    @Test
    public void testPostDevice() {
        StatPostDeviceCommand cmd = new StatPostDeviceCommand();
        cmd.setSceneToken("sceneToken");
        cmd.setDeviceTime(1L);
        cmd.setDeviceId("deviceId");
        cmd.setDeviceBrand("deviceBrand");
        cmd.setDeviceModel("deviceModel");
        cmd.setOsVersion("osVersion");
        cmd.setAccess("access");
        cmd.setCountry("country");
        cmd.setLanguage("language");
        cmd.setMc("mc");
        cmd.setImei("imei");
        cmd.setResolution("resolution");
        cmd.setTimezone("timezone");
        cmd.setCarrier("carrier");

        EventPostDeviceRestResponse response = httpClientService.restPost(postDeviceURL, cmd, EventPostDeviceRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        assertTrue(response.getResponse().getUploadStrategy().size() == 3);
        assertNotNull(response.getResponse().getSessionId());
    }

    // 上传事件log
    @Test
    public void testPostLog() {
        RestResponseBase response = httpClientService.restPost(postLogURL, null, RestResponseBase.class);
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
