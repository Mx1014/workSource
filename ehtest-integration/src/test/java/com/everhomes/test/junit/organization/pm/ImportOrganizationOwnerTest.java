package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.ImportOrganizationsOwnersCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOrganizationOwners;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerAddress;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerBehaviors;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ImportOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testImportOrganizationOwner() throws IOException {
        logon();
        String api = "/pm/importOrganizationOwners";
        ImportOrganizationsOwnersCommand cmd = new ImportOrganizationsOwnersCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);

        String filePath = new File("").getCanonicalPath() + "/src/test/data/excel/organization_owner_template.xlsx";
        RestResponseBase resp = httpClientService.postFile(api, cmd, new File(filePath), RestResponseBase.class);

        assertNotNull(resp);
        assertTrue("The response should be OK", httpClientService.isReponseSuccess(resp));

        DSLContext context = dbProvider.getDslContext();
        List<EhOrganizationOwners> owners = context.selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .fetchInto(EhOrganizationOwners.class);
        assertTrue("The owners list size should be 5.", owners.size() == 5);

        List<EhOrganizationOwnerBehaviors> behaviorsList = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .fetchInto(EhOrganizationOwnerBehaviors.class);
        assertTrue("The owner behavior list size should be 2.", behaviorsList.size() == 2);

        List<EhOrganizationOwnerAddress> addressList = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .fetchInto(EhOrganizationOwnerAddress.class);
        assertTrue("The owner address size should be 4.", addressList.size() == 4);
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
        userInfoFilePath = "data/json/customer-manage-import-owner-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    @Override
    public void tearDown() {
        logoff();
    }
}
