// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.organization.GetContactTopDepartmentCommand;
import com.everhomes.rest.organization.GetContactTopDepartmentRestResponse;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationContactsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListContactTopDepartmentTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListContactTopDepartment() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/org/getContactTopDepartment";

        GetContactTopDepartmentCommand cmd = new GetContactTopDepartmentCommand();
        cmd.setOrganizationId(1000750L);

        GetContactTopDepartmentRestResponse response= httpClientService.restGet(commandRelativeUri, cmd, GetContactTopDepartmentRestResponse.class, context);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(true, response.getResponse().getId() == 1000752L);
    }

    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        String organizationFilePath = "data/json/organizations-data-test_20161014.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(organizationFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        String organizationMemberFilePath = "data/json/organization-member-test-data_161017.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(organizationMemberFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
}

