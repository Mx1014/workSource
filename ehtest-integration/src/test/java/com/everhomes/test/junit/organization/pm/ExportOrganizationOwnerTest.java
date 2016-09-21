package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.ImportOrganizationsOwnersCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ExportOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testImportOrganizationOwner() throws IOException {
        logon();
        String api = "/pm/exportOrganizationOwners";
        ImportOrganizationsOwnersCommand cmd = new ImportOrganizationsOwnersCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);

        RestResponseBase resp = httpClientService.restPost(api, cmd, RestResponseBase.class);
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
        userInfoFilePath = "data/json/customer-manage-search-owner-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    @Override
    public void tearDown() {
        logoff();
    }
}
