package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.AddOrganizationOwnerAddressCommand;
import com.everhomes.rest.organization.pm.AddOrganizationOwnerAddressRestResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAddressRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class AddOrganizationOwnerAddressTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testAddOrganizationOwnerAddress() {
        logon();
        String api = "/pm/addOrganizationOwnerAddress";
        AddOrganizationOwnerAddressCommand cmd = new AddOrganizationOwnerAddressCommand();

        Long addressId = 24206890946797813L;
        Long ownerId = 1L;
        Byte livingStatus = Byte.valueOf("1");

        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(addressId);
        cmd.setLivingStatus(livingStatus);
        cmd.setOrgOwnerId(ownerId);

        // cmd.setOwnerType(EhCommunities.class.getSimpleName());
        // cmd.setOwnerId();

        AddOrganizationOwnerAddressRestResponse response = httpClientService.restPost(api, cmd, AddOrganizationOwnerAddressRestResponse.class);

        assertNotNull("The addOrganizationOwnerAddress response should not be null.", response);
        assertNotNull("The OrganizationOwnerAddressDTO should not be null.", response.getResponse());

        OrganizationOwnerAddressDTO dto = response.getResponse();
        assertEquals("The response OrganizationOwnerAddressDTO addressId should be equal.", addressId, dto.getAddressId());
        assertEquals("The response OrganizationOwnerAddressDTO livingStatus should be equal.", "是", dto.getLivingStatus());
        assertEquals("The response OrganizationOwnerAddressDTO apartment should be equal.", "102", dto.getApartment());
        assertEquals("The response OrganizationOwnerAddressDTO building should be equal.", "101-1", dto.getBuilding());

        DSLContext context = dbProvider.getDslContext();
        Long maxId = context.select(DSL.max(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ID))
                .from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .fetchOne().value1();

        EhOrganizationOwnerAddressRecord record = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ID.eq(maxId))
                .fetchOne();

        assertNotNull("Record should not be null.", record);
        assertEquals("LivingStatus should be equal.", livingStatus, record.getLivingStatus());
        assertEquals("Address id should be equal.", addressId, record.getAddressId());
        assertEquals("Owner id should be equal.", ownerId, record.getOrganizationOwnerId());
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
        // userInfoFilePath = "data/json/customer-manage-list-owner-addresses-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
