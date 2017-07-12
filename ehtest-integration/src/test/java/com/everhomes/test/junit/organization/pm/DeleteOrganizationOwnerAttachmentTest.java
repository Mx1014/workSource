package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.DeleteOrganizationOwnerAttachmentCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAttachmentsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class DeleteOrganizationOwnerAttachmentTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testDeleteOrganizationOwnerAttachment() {
        DSLContext context = dbProvider.getDslContext();
        EhOrganizationOwnerAttachmentsRecord record = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.eq(1L))
                .fetchOne();

        assertNotNull("The attachment record should be not null.", record);

        logon();
        String api = "/pm/deleteOrganizationOwnerAttachment";
        DeleteOrganizationOwnerAttachmentCommand cmd = new DeleteOrganizationOwnerAttachmentCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);
        cmd.setId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK.", httpClientService.isReponseSuccess(response));

        record = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.eq(1L))
                .fetchOne();

        assertNull("The deleted record should be null.", record);
    }

    private void logon() {
        Integer namespaceId = 0;
        String userIdentifier = "12900000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
    }

    @Override
    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        // userInfoFilePath = "data/json/customer-manage-list-owner-attachments-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
