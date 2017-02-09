package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.UpdateOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.UpdateOrganizationOwnerRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class UpdateOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testUpdateOrganizationOwner() {
        logon();
        String api = "/pm/updateOrganizationOwner";
        UpdateOrganizationOwnerCommand cmd = new UpdateOrganizationOwnerCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setId(1L);
        String idCardNumber = "654225199222222220";
        cmd.setIdCardNumber(idCardNumber);
        String company = "永佳天成";
        cmd.setCompany(company);

        UpdateOrganizationOwnerRestResponse response = httpClientService.restPost(api, cmd, UpdateOrganizationOwnerRestResponse.class);

        assertNotNull("response should not be null-1", response);
        assertNotNull("response should not be null-2", response.getResponse());
        assertEquals("organization id should be 1000001", Long.valueOf(1000001), response.getResponse().getOrganizationId());

        assertEquals("The company should be equal", response.getResponse().getCompany(), company);
        assertEquals("The idCardNumber should be equal", response.getResponse().getIdCardNumber(), idCardNumber);

        EhOrganizationOwnersRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.COMPANY.eq(company))
                .and(Tables.EH_ORGANIZATION_OWNERS.ID_CARD_NUMBER.eq(idCardNumber))
                .fetchOne();

        assertNotNull(record);
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
        // userInfoFilePath = "data/json/customer-manage-create-owner-data-2.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
