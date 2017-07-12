package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.address.AddressLivingStatus;
import com.everhomes.rest.organization.pm.OrganizationOwnerBehaviorType;
import com.everhomes.rest.organization.pm.UpdateOrganizationOwnerAddressStatusCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhAddressesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAddressRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerBehaviorsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class UpdateOrganizationOwnerAddressStatusTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testUpdateOrganizationOwnerAddressStatusActive() {
        DSLContext context = dbProvider.getDslContext();
        EhOrganizationOwnerAddressRecord ownerAddress = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS.eq(AddressLivingStatus.INACTIVE.getCode()))
                .fetchOne();

        assertNotNull("The ownerAddress should not be null.", ownerAddress);
        assertEquals("The ownerAddress livingStatus should be "+AddressLivingStatus.INACTIVE.getCode(), Byte.valueOf(AddressLivingStatus.INACTIVE.getCode()), ownerAddress.getLivingStatus());

        EhAddressesRecord address = context.selectFrom(Tables.EH_ADDRESSES).where(Tables.EH_ADDRESSES.ID.eq(ownerAddress.getAddressId()))
                .fetchOne();

        assertNotNull("The address should not be null.", address);

        logon();
        String api = "/pm/updateOrganizationOwnerAddressStatus";
        UpdateOrganizationOwnerAddressStatusCommand cmd = new UpdateOrganizationOwnerAddressStatusCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(address.getId());
        cmd.setBehaviorType(OrganizationOwnerBehaviorType.IMMIGRATION.getCode());
        cmd.setOrgOwnerId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK", httpClientService.isReponseSuccess(response));

        EhOrganizationOwnerAddressRecord updatedOwnerAddress = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS.eq(AddressLivingStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(address.getId()))
                .fetchOne();

        assertNotNull("The updatedOwnerAddress should be not null.", updatedOwnerAddress);
        assertEquals("The updatedOwnerAddress livingStatus should be "+AddressLivingStatus.ACTIVE.getCode(), Byte.valueOf(AddressLivingStatus.ACTIVE.getCode()), updatedOwnerAddress.getLivingStatus());

        EhOrganizationOwnerBehaviorsRecord ownerBehaviorsRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ADDRESS_ID.eq(address.getId()))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.BEHAVIOR_TYPE.eq(OrganizationOwnerBehaviorType.IMMIGRATION.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.OWNER_ID.eq(updatedOwnerAddress.getOrganizationOwnerId()))
                .fetchAny();

        assertNotNull("The ownerBehaviorsRecord should not null.", ownerBehaviorsRecord);
    }

    @Test
    public void testUpdateOrganizationOwnerAddressStatusInActive() {
        DSLContext context = dbProvider.getDslContext();
        EhOrganizationOwnerAddressRecord ownerAddress = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS.eq(AddressLivingStatus.ACTIVE.getCode()))
                .fetchOne();

        assertNotNull("The ownerAddress should not be null.", ownerAddress);
        assertEquals("The ownerAddress livingStatus should be "+AddressLivingStatus.ACTIVE.getCode(), Byte.valueOf(AddressLivingStatus.ACTIVE.getCode()), ownerAddress.getLivingStatus());

        EhAddressesRecord address = context.selectFrom(Tables.EH_ADDRESSES).where(Tables.EH_ADDRESSES.ID.eq(ownerAddress.getAddressId()))
                .fetchOne();

        assertNotNull("The address should not be null.", address);

        logon();
        String api = "/pm/updateOrganizationOwnerAddressStatus";
        UpdateOrganizationOwnerAddressStatusCommand cmd = new UpdateOrganizationOwnerAddressStatusCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(address.getId());
        cmd.setBehaviorType(OrganizationOwnerBehaviorType.EMIGRATION.getCode());
        cmd.setOrgOwnerId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK", httpClientService.isReponseSuccess(response));

        EhOrganizationOwnerAddressRecord updatedOwnerAddress = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS.eq(AddressLivingStatus.INACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(address.getId()))
                .fetchOne();

        assertNotNull("The updatedOwnerAddress should be not null.", updatedOwnerAddress);
        assertEquals("The updatedOwnerAddress livingStatus should be "+AddressLivingStatus.INACTIVE.getCode(), Byte.valueOf(AddressLivingStatus.INACTIVE.getCode()), updatedOwnerAddress.getLivingStatus());

        EhOrganizationOwnerBehaviorsRecord ownerBehaviorsRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ADDRESS_ID.eq(address.getId()))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.BEHAVIOR_TYPE.eq(OrganizationOwnerBehaviorType.EMIGRATION.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.OWNER_ID.eq(updatedOwnerAddress.getOrganizationOwnerId()))
                .fetchAny();

        assertNotNull("The ownerBehaviorsRecord should not null.", ownerBehaviorsRecord);
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
