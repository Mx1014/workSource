package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerBehaviorsCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerBehaviorsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerBehaviorsTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerBehaviors() {
        logon();
        String api = "/pm/listOrganizationOwnerBehaviors";
        ListOrganizationOwnerBehaviorsCommand cmd = new ListOrganizationOwnerBehaviorsCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);

        ListOrganizationOwnerBehaviorsRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnerBehaviorsRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());
        assertEquals(2, response.getResponse().size());

        assertEquals("迁入", response.getResponse().get(0).getBehaviorType());
        assertEquals("迁出", response.getResponse().get(1).getBehaviorType());
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
