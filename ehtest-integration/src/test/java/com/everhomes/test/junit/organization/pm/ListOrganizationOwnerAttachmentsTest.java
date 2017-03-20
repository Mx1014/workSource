package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerAttachmentsCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerAttachmentsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerAttachmentsTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerAttachments() {
        logon();
        String api = "/pm/listOrganizationOwnerAttachments";
        ListOrganizationOwnerAttachmentsCommand cmd = new ListOrganizationOwnerAttachmentsCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);

        ListOrganizationOwnerAttachmentsRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnerAttachmentsRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());
        assertEquals(2, response.getResponse().size());
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
