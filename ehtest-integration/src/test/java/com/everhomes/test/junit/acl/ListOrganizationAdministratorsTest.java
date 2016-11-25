package com.everhomes.test.junit.acl;

import com.everhomes.rest.acl.ListOrganizationAdministratorsRestResponse;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsRestResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfyan on 2016/11/3.
 */
public class ListOrganizationAdministratorsTest extends BaseLoginAuthTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        logoff();
    }

    @Test
    public void testListOrganizationAdministrators(){
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        String commandRelativeUri = "/acl/listOrganizationAdministrators";
        ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
        cmd.setOrganizationId(1000750L);
        cmd.setOwnerType("EhOrganizations");
        cmd.setOwnerId(1000750L);
        ListOrganizationAdministratorsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListOrganizationAdministratorsRestResponse.class, context);
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().size());

        List<Long> ids = new ArrayList<>();
        ids.add(10003L);
        for (OrganizationContactDTO dto: response.getResponse()) {
            assertEquals(true, ids.contains(dto.getTargetId()));
        }

    }

    protected void initCustomData() {
        String filePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath = "data/json/organization-member-test-data_161104.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath = "data/json/acl_role_assignments-test-data_161017.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);
    }
}
