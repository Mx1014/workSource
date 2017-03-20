package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnersByCarCommand;
import com.everhomes.rest.organization.pm.ListOrganizationOwnersByCarRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerByCarTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerByCar() {
        logon();
        String api = "/pm/listOrganizationOwnersByCar";
        ListOrganizationOwnersByCarCommand cmd = new ListOrganizationOwnersByCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCarId(1L);

        ListOrganizationOwnersByCarRestResponse response = httpClientService.restPost(api, cmd, ListOrganizationOwnersByCarRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());

        assertTrue("The orgOwnerAddressDTOList size should be 1", response.getResponse().size() == 1);

        assertEquals("张三", response.getResponse().get(0).getContactName());
        assertEquals("是", response.getResponse().get(0).getPrimaryFlag());
        assertEquals("12345678910", response.getResponse().get(0).getContactToken());
        assertEquals("业主", response.getResponse().get(0).getOrgOwnerType());
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

        /*userInfoFilePath = "data/json/customer-manage-list-owner-car-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        userInfoFilePath = "data/json/customer-manage-owner-type-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);*/

        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
