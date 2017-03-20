package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticByGenderRestResponse;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerStatisticByGenderTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerStatisticByGender() {
        logon();
        String api = "/pm/listOrganizationOwnerStatisticByGender";
        ListOrganizationOwnerStatisticCommand cmd = new ListOrganizationOwnerStatisticCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setOrgOwnerTypeIds(Collections.singletonList(1L));

        ListOrganizationOwnerStatisticByGenderRestResponse response = httpClientService.restPost(
                api, cmd, ListOrganizationOwnerStatisticByGenderRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());

        assertTrue("The orgOwnerAddressDTOList size should be 2", response.getResponse().size() == 2);

        assertEquals(response.getResponse().get(0).getFirst(), "男");
        assertEquals(response.getResponse().get(0).getSecond()+"", "2");
        assertEquals(response.getResponse().get(0).getThird(), "66%");

        assertEquals(response.getResponse().get(1).getFirst(), "女");
        assertEquals(response.getResponse().get(1).getSecond()+"", "1");
        assertEquals(response.getResponse().get(1).getThird(), "33%");
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
        // userInfoFilePath = "data/json/customer-manage-list-statistic-by-gender-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
