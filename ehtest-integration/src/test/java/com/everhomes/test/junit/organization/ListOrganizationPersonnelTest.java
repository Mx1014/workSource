// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.VisibleFlag;
import com.everhomes.rest.organization.admin.OrgListOrganizationPersonnelsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListOrganizationPersonnelTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListOrganizationPersonnel() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/listOrganizationPersonnels";

        ListOrganizationContactCommand cmd = new ListOrganizationContactCommand();
        cmd.setOrganizationId(1000750L);
        OrgListOrganizationPersonnelsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, OrgListOrganizationPersonnelsRestResponse.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertNotNull("The reponse of getting response not be null", response.getResponse());
        assertNotNull("The reponse of getting members not be null", response.getResponse().getMembers());
        assertEquals(3, response.getResponse().getMembers().size());

        List<Long> userIds = new ArrayList<>();
        userIds.add(10001L);
        userIds.add(10002L);
        userIds.add(10003L);

        for (OrganizationMemberDTO member: response.getResponse().getMembers()) {
            assertEquals(true, userIds.contains(member.getTargetId()));
            if(10001L == member.getTargetId()){
                assertNotNull("The reponse of getting job position not be null", member.getJobPositions());
                assertEquals(2, member.getJobPositions().size());
                assertNotNull("The reponse of getting job level not be null", member.getJobLevels());
                assertEquals(1, member.getJobLevels().size());
            }

            if(10002L == member.getTargetId()){
                assertNotNull("The reponse of getting job position not be null", member.getJobPositions());
                assertEquals(1, member.getJobPositions().size());
            }

            if(10003L == member.getTargetId()){
                assertNotNull("The reponse of getting job position not be null", member.getJobPositions());
                assertEquals(1, member.getJobPositions().size());

                assertNotNull("The reponse of getting job level not be null", member.getJobLevels());
                assertEquals(1, member.getJobLevels().size());

                assertNotNull("The reponse of getting job level not be null", member.getDepartments());
                assertEquals(2, member.getDepartments().size());
            }
        }

    }




    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
        userInfoFilePath = "data/json/organization-member-test-data_161110.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
}

