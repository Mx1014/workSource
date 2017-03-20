package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.GetOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.GetOrganizationOwnerRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class GetOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testGetOrganizationOwner() {
        logon();
        String api = "/pm/getOrganizationOwner";
        GetOrganizationOwnerCommand cmd = new GetOrganizationOwnerCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setId(1L);

        GetOrganizationOwnerRestResponse response = httpClientService.restPost(api, cmd, GetOrganizationOwnerRestResponse.class);

        assertNotNull("response should not be null", response);
        OrganizationOwnerDTO dto = response.getResponse();
        assertNotNull("response should not be null", dto);
        assertEquals("Organization owner organization id should be 1000001", Long.valueOf(1000001), dto.getOrganizationId());
        assertEquals("Organization owner orgOwnerType should be equal", "业主", dto.getOrgOwnerType());
        assertEquals("Organization owner gender should be equal", "女", dto.getGender());
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
        // userInfoFilePath = "data/json/customer-manage-search-owner-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
