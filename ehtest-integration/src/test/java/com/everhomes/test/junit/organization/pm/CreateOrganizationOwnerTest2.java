package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerRestResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

import java.sql.Date;
import java.util.Collections;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class CreateOrganizationOwnerTest2 extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    /**
     * 创建成功
     */
    @Test
    public void testCreateOrganizationOwner() {
        logon();
        String api = "/pm/createOrganizationOwner";
        CreateOrganizationOwnerCommand cmd = new CreateOrganizationOwnerCommand();

        String contactToken = "13245678911";
        String contactName = "zuolin";
        Long birthday = Date.valueOf("2016-01-01").getTime();
        Long communityId = 111L;
        Long orgOwnerTypeId = 1L;

        cmd.setContactToken(contactToken);
        cmd.setContactName(contactName);
        cmd.setOrgOwnerTypeId(orgOwnerTypeId);
        cmd.setBirthday(birthday);
        cmd.setCommunityId(communityId);
        cmd.setOrganizationId(1000001L);

        OrganizationOwnerAddressCommand addressCommand = new OrganizationOwnerAddressCommand();
        addressCommand.setAddressId(1L);
        addressCommand.setLivingStatus((byte)0);
        cmd.setAddresses(Collections.singletonList(addressCommand));

        CreateOrganizationOwnerRestResponse response = httpClientService.restPost(api, cmd, CreateOrganizationOwnerRestResponse.class);

        assertNotNull("The createOrganizationOwner response should be not null.", response);
        assertNotNull("The createOrganizationOwner response DTO should be not null.", response.getResponse());

        OrganizationOwnerDTO dto = response.getResponse();
        assertEquals("The createOrganizationOwner response DTO birthday should be equal.", new Date(birthday), dto.getBirthday());
        assertEquals("The createOrganizationOwner response DTO contactName should be equal.", contactName, dto.getContactName());
        assertEquals("The createOrganizationOwner response DTO contactToken be equal.", contactToken, dto.getContactToken());

        EhOrganizationOwnersRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.eq(contactName))
                .and(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken))
                .fetchOne();

        assertNotNull("The created organizationOwner should be not null.", record);
        assertEquals("The created organizationOwner birthday should be equal.", new Date(birthday), record.getBirthday());
        assertEquals("The created organizationOwner contactName should be equal.", contactName, record.getContactName());
        assertEquals("The created organizationOwner contactToken should be equal.", contactToken, record.getContactToken());
        assertEquals("The created organizationOwner communityId should be equal.", communityId, record.getCommunityId());
        assertEquals("The created organizationOwner orgOwnerTypeId should be equal.", orgOwnerTypeId, record.getOrgOwnerTypeId());
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
    }
}
