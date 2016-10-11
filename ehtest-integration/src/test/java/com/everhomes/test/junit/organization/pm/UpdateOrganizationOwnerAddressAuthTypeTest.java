package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressAuthType;
import com.everhomes.rest.organization.pm.UpdateOrganizationOwnerAddressAuthTypeCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class UpdateOrganizationOwnerAddressAuthTypeTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testUpdateOrganizationOwnerAddressAuthType() {
        logon();
        String api = "/pm/updateOrganizationOwnerAddressAuthType";
        UpdateOrganizationOwnerAddressAuthTypeCommand cmd = new UpdateOrganizationOwnerAddressAuthTypeCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOwnerId(1L);
        cmd.setAddressId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK", httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        Result<Record> groupMember = context.select().from(Tables.EH_GROUP_MEMBERS)
                .where(Tables.EH_GROUP_MEMBERS.ID.eq(100001L)).fetch();
        assertTrue(groupMember.size() == 0);

        Result<Record> ownerAddress = context.select().from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.AUTH_TYPE.eq(OrganizationOwnerAddressAuthType.INACTIVE.getCode()))
                .fetch();
        assertNotNull(ownerAddress);
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
        userInfoFilePath = "data/json/customer-manage-update-owner-address-authtype-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
