package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.UploadOrganizationOwnerAttachmentCommand;
import com.everhomes.rest.organization.pm.UploadOrganizationOwnerAttachmentRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAttachmentsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class CreateOrganizationOwnerAttachmentsTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testCreateOrganizationOwnerAttachment() {
        logon();
        String api = "/pm/uploadOrganizationOwnerAttachment";
        UploadOrganizationOwnerAttachmentCommand cmd = new UploadOrganizationOwnerAttachmentCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);
        String contentUri = "cs://2/doc/aW1hZ2UvTVRvMk5EWTFZekJtTnpaa1lqSmpObVV6WlRObFpUVmhaVGN3TlRCaVkyTmpOQQ";
        cmd.setContentUri(contentUri);
        String attachmentName = "奖金.doc";
        cmd.setAttachmentName(attachmentName);

        // cmd.setOwnerId();
        // cmd.setOwnerType(EhCommunities.class.getSimpleName());

        UploadOrganizationOwnerAttachmentRestResponse response = httpClientService.restPost(api, cmd, UploadOrganizationOwnerAttachmentRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());

        assertEquals("attachmentName should be equal", attachmentName, response.getResponse().getAttachmentName());
        assertEquals("contentUri should be equal", contentUri, response.getResponse().getContentUri());

        EhOrganizationOwnerAttachmentsRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.eq(response.getResponse().getId()))
                .and(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.CONTENT_URI.eq(contentUri))
                .and(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ATTACHMENT_NAME.eq(attachmentName))
                .fetchOne();

        assertNotNull(record);
        assertNotNull(record.getId());
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

        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
