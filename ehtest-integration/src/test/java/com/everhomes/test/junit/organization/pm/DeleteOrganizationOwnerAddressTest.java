package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.DeleteOrganizationOwnerAddressCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.records.EhGroupMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAddressRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class DeleteOrganizationOwnerAddressTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    // 删除业主对应的地址记录
    @Test
    public void testDeleteOrganizationOwnerAddress() {
        // initOwnerAddressData();
        DSLContext context = dbProvider.getDslContext();
        String address = "101-1-102";
        EhAddresses ehAddress = context.selectFrom(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.ADDRESS.eq(address))
                .fetchAnyInto(EhAddresses.class);

        assertNotNull("The ehAddress should be not null.", ehAddress);

        logon();
        String api = "/pm/deleteOrganizationOwnerAddress";
        DeleteOrganizationOwnerAddressCommand cmd = new DeleteOrganizationOwnerAddressCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(ehAddress.getId());
        cmd.setOrgOwnerId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);

        EhOrganizationOwnerAddressRecord addressRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ID.eq(ehAddress.getId()))
                .fetchOne();

        assertNull("The addressRecord should not be null.", addressRecord);
    }

    // 删除业主对应的地址记录,同时删除group member
    @Test
    public void testDeleteOrganizationOwnerAddressWithDeleteGroupMember() {
        // initOwnerData();
        DSLContext context = dbProvider.getDslContext();
        logon();
        String api = "/pm/deleteOrganizationOwnerAddress";
        DeleteOrganizationOwnerAddressCommand cmd = new DeleteOrganizationOwnerAddressCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(24206890946797812L);
        cmd.setOrgOwnerId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);

        EhOrganizationOwnerAddressRecord addressRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(24206890946797812L))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .fetchOne();

        assertNull("The addressRecord should be null.", addressRecord);

        EhGroupMembersRecord membersRecord = context.selectFrom(Tables.EH_GROUP_MEMBERS)
                .where(Tables.EH_GROUP_MEMBERS.ID.eq(100001L)).fetchOne();

        assertNull(membersRecord);
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

    /*private void initOwnerAddressData() {
        String userInfoFilePath = "data/json/customer-manage-list-owner-addresses-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    private void initOwnerData() {
        String userInfoFilePath = "data/json/customer-manage-update-owner-address-authtype-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }*/
}
