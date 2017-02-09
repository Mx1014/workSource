package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerAddressesCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerAddressesRestResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

import java.util.List;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerAddressesTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerAddresses() {
        logon();
        String api = "/pm/listOrganizationOwnerAddresses";
        ListOrganizationOwnerAddressesCommand cmd = new ListOrganizationOwnerAddressesCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);

        ListOrganizationOwnerAddressesRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnerAddressesRestResponse.class);

        assertNotNull("response should not be null.1", response);
        List<OrganizationOwnerAddressDTO> dtoList = response.getResponse();
        assertNotNull("response should not be null.2", dtoList);
        assertEquals(2, dtoList.size());

        assertEquals("否", dtoList.get(0).getLivingStatus());
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
}
