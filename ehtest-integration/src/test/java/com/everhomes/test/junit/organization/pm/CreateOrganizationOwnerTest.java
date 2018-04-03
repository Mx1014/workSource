package com.everhomes.test.junit.organization.pm;

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
public class CreateOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    /**
     * 创建失败
     */
    @Test
    public void testCreateOrganizationOwner() {
        logon();
        String api = "/pm/createOrganizationOwner";
        CreateOrganizationOwnerCommand cmd = new CreateOrganizationOwnerCommand();

        String contactToken = "12345678910";
        String contactName = "zuolin";
        Long birthday = Date.valueOf("2016-01-01").getTime();

        cmd.setContactToken(contactToken);
        cmd.setContactName(contactName);
        cmd.setOrgOwnerTypeId(1L);
        cmd.setBirthday(birthday);
        cmd.setCommunityId(24206890946790405L);
        cmd.setOrganizationId(1000001L);

        OrganizationOwnerAddressCommand addressCommand = new OrganizationOwnerAddressCommand();
        addressCommand.setAddressId(24206890946797812L);
        addressCommand.setLivingStatus((byte)0);
        cmd.setAddresses(Collections.singletonList(addressCommand));

        CreateOrganizationOwnerRestResponse response = httpClientService.restPost(api, cmd, CreateOrganizationOwnerRestResponse.class);

        assertEquals("OrganizationOwner are already exist, contactToken = 12345678910.", response.getErrorDescription());

        EhOrganizationOwnersRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.eq(contactName))
                .and(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken))
                .fetchOne();

        assertNull("The created organization owner should be null.", record);
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
