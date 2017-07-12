package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerCarByOrgOwnerCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerCarsByOrgOwnerRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerCarsByOrgOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerCarsByOrgOwner() {
        logon();
        String api = "/pm/listOrganizationOwnerCarsByOrgOwner";
        ListOrganizationOwnerCarByOrgOwnerCommand cmd = new ListOrganizationOwnerCarByOrgOwnerCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);

        ListOrganizationOwnerCarsByOrgOwnerRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnerCarsByOrgOwnerRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());
        assertEquals(1, response.getResponse().size());
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
        // userInfoFilePath = "data/json/customer-manage-list-owner-car-by-owner-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
