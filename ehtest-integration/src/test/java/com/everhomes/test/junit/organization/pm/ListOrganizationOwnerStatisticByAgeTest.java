package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticByAgeRestResponse;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ListOrganizationOwnerStatisticByAgeTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testListOrganizationOwnerStatisticByAge() {
        logon();
        String api = "/pm/listOrganizationOwnerStatisticByAge";
        ListOrganizationOwnerStatisticCommand cmd = new ListOrganizationOwnerStatisticCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setOrgOwnerTypeIds(Collections.singletonList(1L));

        ListOrganizationOwnerStatisticByAgeRestResponse response = httpClientService.restPost(
                api, cmd, ListOrganizationOwnerStatisticByAgeRestResponse.class);

        assertNotNull("response should not be null.1", response);
        assertNotNull("response should not be null.2", response.getResponse());

        assertTrue("The male list size should be 3", response.getResponse().getMale().size() == 1);
        assertTrue("The female list size should be 1", response.getResponse().getFemale().size() == 1);
        assertTrue("The female list size should be 1", response.getResponse().getTotal().size() == 1);

        assertEquals(response.getResponse().getMale().get(0).getFirst(), "0-10");
        assertEquals(response.getResponse().getMale().get(0).getSecond()+"", "2");
        assertEquals(response.getResponse().getMale().get(0).getThird(), "100");

        assertEquals(response.getResponse().getFemale().get(0).getFirst(), "0-10");
        assertEquals(response.getResponse().getFemale().get(0).getSecond()+"", "1");
        assertEquals(response.getResponse().getFemale().get(0).getThird(), "100");
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
        // userInfoFilePath = "data/json/customer-manage-list-statistic-by-age-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
