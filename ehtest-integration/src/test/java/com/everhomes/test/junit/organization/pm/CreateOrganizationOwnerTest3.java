package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerRestResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerBehaviorsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

import java.sql.Date;
import java.util.Collections;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class CreateOrganizationOwnerTest3 extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    /**
     * 有迁入行为的创建业主
     */
    @Test
    public void testCreateOrganizationOwner() {
        logon();
        String api = "/pm/createOrganizationOwner";
        CreateOrganizationOwnerCommand cmd = new CreateOrganizationOwnerCommand();

        String contactToken = "13245678933";
        String contactName = "zuolin";
        Long birthday = Date.valueOf("2016-01-01").getTime();
        Long communityId = 24206890946790405L;
        Long orgOwnerTypeId = 1L;

        cmd.setContactToken(contactToken);
        cmd.setContactName(contactName);
        cmd.setOrgOwnerTypeId(orgOwnerTypeId);
        cmd.setBirthday(birthday);
        cmd.setCommunityId(communityId);
        cmd.setOrganizationId(1000001L);

        OrganizationOwnerAddressCommand addressCommand = new OrganizationOwnerAddressCommand();
        Long addressId = 24206890946797812L;
        addressCommand.setAddressId(addressId);
        addressCommand.setLivingStatus((byte)1);
        addressCommand.setCheckInDate(System.currentTimeMillis());
        cmd.setAddresses(Collections.singletonList(addressCommand));

        CreateOrganizationOwnerRestResponse response = httpClientService.restPost(api, cmd, CreateOrganizationOwnerRestResponse.class);

        assertNotNull("The createOrganizationOwner response should be not null.", response);
        assertNotNull("The createOrganizationOwner response DTO should be not null.", response.getResponse());

        OrganizationOwnerDTO dto = response.getResponse();
        assertEquals("The createOrganizationOwner response DTO birthday should be equal.", birthday, dto.getBirthday());
        assertEquals("The createOrganizationOwner response DTO contactName should be equal.", contactName, dto.getContactName());
        assertEquals("The createOrganizationOwner response DTO contactToken be equal.", contactToken, dto.getContactToken());

        DSLContext dslContext = dbProvider.getDslContext();
        EhOrganizationOwnersRecord record = dslContext.selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.eq(contactName))
                .and(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken))
                .fetchOne();

        assertNotNull("The created organizationOwner should be not null.", record);
        assertEquals("The created organizationOwner birthday should be equal.", new Date(birthday), record.getBirthday());
        assertEquals("The created organizationOwner contactName should be equal.", contactName, record.getContactName());
        assertEquals("The created organizationOwner contactToken should be equal.", contactToken, record.getContactToken());
        assertEquals("The created organizationOwner communityId should be equal.", communityId, record.getCommunityId());
        assertEquals("The created organizationOwner orgOwnerTypeId should be equal.", orgOwnerTypeId, record.getOrgOwnerTypeId());

        EhOrganizationOwnerBehaviorsRecord behaviorsRecord = dslContext.selectFrom(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.OWNER_ID.eq(record.getId()))
                .fetchOne();

        assertNotNull("The organizationOwner behavior should be not null.", behaviorsRecord);
        assertEquals("The behavior ownerId should be equal.", behaviorsRecord.getOwnerId(), record.getId());
        assertEquals("The behavior addressId should be equal.", behaviorsRecord.getAddressId(), addressId);
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
        // userInfoFilePath = "data/json/customer-manage-create-owner-data-3.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
