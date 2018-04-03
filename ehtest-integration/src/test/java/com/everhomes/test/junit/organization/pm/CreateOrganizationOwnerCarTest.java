package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.CreateOrganizationOwnerCarCommand;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerCarRestResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerCarDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerCarsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class CreateOrganizationOwnerCarTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testCreateOrganizationOwnerCar() {
        logon();
        String api = "/pm/createOrganizationOwnerCar";
        CreateOrganizationOwnerCarCommand cmd = new CreateOrganizationOwnerCarCommand();

        Long communityId = 24206890946790405L;
        String plateNumber = "粤B77767";
        String white = "white";
        String brand = "Mercedes-G500";
        String parkingSpace = "20F";
        String contactNumber = "123456897";

        cmd.setParkingType((byte)1);
        cmd.setBrand(brand);
        cmd.setParkingSpace(parkingSpace);
        cmd.setContactNumber(contactNumber);
        cmd.setPlateNumber(plateNumber);
        cmd.setColor(white);
        cmd.setCommunityId(communityId);
        cmd.setOrganizationId(1000001L);

        CreateOrganizationOwnerCarRestResponse response = httpClientService.restPost(api, cmd, CreateOrganizationOwnerCarRestResponse.class);

        assertTrue("The response should be OK", httpClientService.isReponseSuccess(response));
        assertNotNull("The createOrganizationOwnerCar response should be not null.", response.getResponse());

        OrganizationOwnerCarDTO dto = response.getResponse();
        assertEquals("The createOrganizationOwnerCar response dto plateNumber should be equal.", plateNumber, dto.getPlateNumber());
        assertEquals("The createOrganizationOwnerCar response dto brand should be equal.", brand, dto.getBrand());
        assertEquals("The createOrganizationOwnerCar response dto color should be equal.", white, dto.getColor());
        assertEquals("The createOrganizationOwnerCar response dto parkingSpace should be equal.", parkingSpace, dto.getParkingSpace());
        assertEquals("The createOrganizationOwnerCar response dto contactNumber should be equal.", contactNumber, dto.getContactNumber());

        EhOrganizationOwnerCarsRecord record = dbProvider.getDslContext()
                .selectFrom(Tables.EH_ORGANIZATION_OWNER_CARS)
                .where(Tables.EH_ORGANIZATION_OWNER_CARS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.PLATE_NUMBER.eq(plateNumber))
                .fetchOne();

        assertNotNull("The created car should not be null.", record);
        assertEquals("The created car plateNumber should be equal.", plateNumber, record.getPlateNumber());
        assertEquals("The created car brand should be equal.", brand, record.getBrand());
        assertEquals("The created car color should be equal.", white, record.getColor());
        assertEquals("The created car parkingSpace should be equal.", parkingSpace, record.getParkingSpace());
        assertEquals("The created car contactNumber should be equal.", contactNumber, record.getContactNumber());
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
