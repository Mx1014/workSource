package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerCarsByAddressCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerCarsByAddressRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerCarsByAddressTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerCarsByAddress() {
        logon();
        String api = "/pm/listOrganizationOwnerCarsByAddress";
        ListOrganizationOwnerCarsByAddressCommand cmd = new ListOrganizationOwnerCarsByAddressCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(24206890946797814L);

        ListOrganizationOwnerCarsByAddressRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnerCarsByAddressRestResponse.class);

        assertNotNull("The response should not be null.", response);
        assertNotNull("The response DTO list should not be null.", response.getResponse());

        assertTrue("The response DTO list size should be 2, actual is "+response.getResponse().size(), response.getResponse().size() == 2);

        assertEquals("Mercedes-G500", response.getResponse().get(0).getBrand());
        assertEquals("粤B77777", response.getResponse().get(0).getPlateNumber());
        assertEquals("1111", response.getResponse().get(0).getContactNumber());
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
        // userInfoFilePath = "data/json/customer-manage-list-owner-car-by-address-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
