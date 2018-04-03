package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.UpdateOrganizationOwnerCarCommand;
import com.everhomes.rest.organization.pm.UpdateOrganizationOwnerCarRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerCarsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class UpdateOrganizationOwnerCarTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testUpdateOrganizationOwnerCar() {
        logon();
        String api = "/pm/updateOrganizationOwnerCar";
        UpdateOrganizationOwnerCarCommand cmd = new UpdateOrganizationOwnerCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setId(1L);
        String color = "黑色";
        cmd.setColor(color);
        String contactNumber = "132456789";
        cmd.setContactNumber(contactNumber);

        UpdateOrganizationOwnerCarRestResponse response = httpClientService.restPost(api, cmd, UpdateOrganizationOwnerCarRestResponse.class);

        assertNotNull("response should not be null-1", response);
        assertNotNull("response should not be null-2", response.getResponse());

        assertEquals("The color should be equal", response.getResponse().getColor(), color);
        assertEquals("The contactNumber should be equal", response.getResponse().getContactNumber(), contactNumber);

        EhOrganizationOwnerCarsRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_ORGANIZATION_OWNER_CARS)
                .where(Tables.EH_ORGANIZATION_OWNER_CARS.COLOR.eq(color))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.CONTACT_NUMBER.eq(contactNumber))
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
        // userInfoFilePath = "data/json/customer-manage-create-owner-car-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
