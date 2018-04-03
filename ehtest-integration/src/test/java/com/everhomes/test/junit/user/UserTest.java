// @formatter:off
package com.everhomes.test.junit.user;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.user.CreateResetIdentifierAppealCommand;
import com.everhomes.rest.user.CreateResetIdentifierAppealRestResponse;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.SendVerificationCodeByResetIdentifierCommand;
import com.everhomes.rest.user.VerifyResetIdentifierCodeCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhUserAppealLogsRecord;
import com.everhomes.server.schema.tables.records.EhUserIdentifierLogsRecord;
import com.everhomes.server.schema.tables.records.EhUserIdentifiersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest extends BaseLoginAuthTestCase {

    // 申诉修改手机号
    private static final String createResetIdentifierAppealURL = "/user/createResetIdentifierAppeal";
    // 核实修改手机号的短信验证码
    private static final String verifyResetIdentifierCodeURL = "/user/verifyResetIdentifierCode";
    // 发送修改手机号的短信验证码
    private static final String sendVerificationCodeByResetIdentifierURL = "/user/sendVerificationCodeByResetIdentifier";
    // -------------------------------------
    private String userIdentifier = "13246687272";
    private String plainTextPwd = "123456";
    private String newIdentifier = "13246687273";

    private DSLContext dslContext;

    // 申诉修改手机号
    @Test
    public void testCreateResetIdentifierAppeal() {
        CreateResetIdentifierAppealCommand cmd = new CreateResetIdentifierAppealCommand();
        cmd.setOldIdentifier("oldIdentifier");
        cmd.setOldRegionCode(1);
        cmd.setNewIdentifier("newIdentifier");
        cmd.setNewRegionCode(1);
        cmd.setName("name");
        cmd.setEmail("email");
        cmd.setRemarks("remarks");

        CreateResetIdentifierAppealRestResponse response = httpClientService.restPost(createResetIdentifierAppealURL, cmd, CreateResetIdentifierAppealRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        EhUserAppealLogsRecord record = dslContext.selectFrom(Tables.EH_USER_APPEAL_LOGS)
                .where(Tables.EH_USER_APPEAL_LOGS.OLD_IDENTIFIER.eq("oldIdentifier"))
                .and(Tables.EH_USER_APPEAL_LOGS.NEW_IDENTIFIER.eq("newIdentifier"))
                .fetchAny();

        assertNotNull("appeal log should be not null", record);
    }

    // 测试整个修改手机号流程
    @Test
    public void testResetIdentifier() {
        // 第一次发送验证码
        SendVerificationCodeByResetIdentifierCommand sendCodeCmd = new SendVerificationCodeByResetIdentifierCommand();

        RestResponseBase response = httpClientService.restPost(sendVerificationCodeByResetIdentifierURL, sendCodeCmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhUserIdentifierLogsRecord logRecord = dslContext.selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.IDENTIFIER_TOKEN.eq(userIdentifier))
                .and(Tables.EH_USER_IDENTIFIER_LOGS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMING.getCode()))
                .fetchAny();

        assertNotNull("send user identifier log should be not null", logRecord);

        // 校验验证码
        VerifyResetIdentifierCodeCommand verifyCmd = new VerifyResetIdentifierCodeCommand();
        verifyCmd.setVerificationCode(logRecord.getVerificationCode());

        response = httpClientService.restPost(verifyResetIdentifierCodeURL, verifyCmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        logRecord = dslContext.selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.IDENTIFIER_TOKEN.eq(userIdentifier))
                .and(Tables.EH_USER_IDENTIFIER_LOGS.CLAIM_STATUS.eq(IdentifierClaimStatus.VERIFYING.getCode()))
                .fetchAny();

        assertNotNull("verify user identifier log should be not null", logRecord);

        // 第二次发送验证码
        sendCodeCmd = new SendVerificationCodeByResetIdentifierCommand();
        sendCodeCmd.setIdentifier(newIdentifier);
        sendCodeCmd.setRegionCode(86);

        response = httpClientService.restPost(sendVerificationCodeByResetIdentifierURL, sendCodeCmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        logRecord = dslContext.selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.IDENTIFIER_TOKEN.eq(newIdentifier))
                .and(Tables.EH_USER_IDENTIFIER_LOGS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                .fetchAny();

        assertNotNull("send user identifier log should be not null", logRecord);

        // 校验验证码
        verifyCmd = new VerifyResetIdentifierCodeCommand();
        verifyCmd.setVerificationCode(logRecord.getVerificationCode());

        response = httpClientService.restPost(verifyResetIdentifierCodeURL, verifyCmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        logRecord = dslContext.selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.IDENTIFIER_TOKEN.eq(newIdentifier))
                .and(Tables.EH_USER_IDENTIFIER_LOGS.CLAIM_STATUS.eq(IdentifierClaimStatus.TAKEN_OVER.getCode()))
                .fetchAny();

        assertNotNull("verify user identifier log should be not null", logRecord);

        EhUserIdentifiersRecord identifierRecord = dslContext.selectFrom(Tables.EH_USER_IDENTIFIERS)
                .where(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(newIdentifier))
                .and(Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(context.getLoggedUid()))
                .fetchAny();

        assertNotNull("new identifier record should be not null", logRecord);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before public void setUp() {
        dslContext = dbProvider.getDslContext();
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
