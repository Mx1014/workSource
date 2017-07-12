package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.AddOrganizationOwnerCarUserCommand;
import com.everhomes.rest.organization.pm.AddOrganizationOwnerCarUserRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerOwnerCar;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class AddOrganizationOwnerCarUserTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testAddOrganizationOwnerCarUser() {
        logon();
        String api = "/pm/addOrganizationOwnerCarUser";
        AddOrganizationOwnerCarUserCommand cmd = new AddOrganizationOwnerCarUserCommand();

        Long ownerId = 4L;
        Long carId = 1L;

        cmd.setOrgOwnerId(ownerId);
        cmd.setOrganizationId(1000001L);
        cmd.setCarId(carId);

        AddOrganizationOwnerCarUserRestResponse response = httpClientService.restPost(api, cmd, AddOrganizationOwnerCarUserRestResponse.class);

        assertNotNull("The addOrganizationOwnerCarUser response should not be null.", response);
        assertNotNull("The addOrganizationOwnerCarUser response OrganizationOwnerDTO should not be null.", response.getResponse());

        OrganizationOwnerDTO dto = response.getResponse();
        assertEquals("The addOrganizationOwnerCarUser response OrganizationOwnerDTO contactName should be equal.", "赵六", dto.getContactName());
        assertEquals("The addOrganizationOwnerCarUser response OrganizationOwnerDTO contactToken should be equal.", "12345678913", dto.getContactToken());

        DSLContext context = dbProvider.getDslContext();
        EhOrganizationOwnerOwnerCar ownerOwnerCar = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID.eq(ownerId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(carId))
                .fetchOneInto(EhOrganizationOwnerOwnerCar.class);

        assertNotNull("The ownerCar user should be not null.", ownerOwnerCar);
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
