package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.DeleteOrganizationOwnerBehaviorCommand;
import com.everhomes.rest.organization.pm.OrganizationOwnerBehaviorStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerBehaviorsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class DeleteOrganizationOwnerBehaviorTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testDeleteOrganizationOwnerBehavior() {
        logon();
        String api = "/pm/deleteOrganizationOwnerBehavior";
        DeleteOrganizationOwnerBehaviorCommand cmd = new DeleteOrganizationOwnerBehaviorCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);
        cmd.setId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK.", httpClientService.isReponseSuccess(response));

        EhOrganizationOwnerBehaviorsRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ID.eq(1L))
                .fetchOne();

        assertEquals("The deleted record status should be " + OrganizationOwnerBehaviorStatus.DELETE.getCode(),
                OrganizationOwnerBehaviorStatus.DELETE.getCode(), record.getStatus());
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
        // userInfoFilePath = "data/json/customer-manage-delete-owner-behavior-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
