package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnersByAddressCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnersByAddressRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerByAddressTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerByAddress() {
        logon();
        String api = "/pm/listOrganizationOwnersByAddress";
        ListOrganizationOwnersByAddressCommand cmd = new ListOrganizationOwnersByAddressCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setAddressId(1L);

        ListOrganizationOwnersByAddressRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnersByAddressRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());

        assertTrue("The orgOwnerAddressDTOList size should be 1", response.getResponse().size() == 1);

        assertEquals("1", response.getResponse().get(0).getId()+"");
        assertEquals("张三", response.getResponse().get(0).getContactName());
        assertEquals("未认证", response.getResponse().get(0).getAuthType());
        assertEquals("是", response.getResponse().get(0).getLivingStatus());
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
        userInfoFilePath = "data/json/customer-manage-list-owner-addresses-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
