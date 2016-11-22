// @formatter:off
package com.everhomes.test.junit.organization;

import static com.everhomes.server.schema.Tables.*;

import java.util.ArrayList;
import java.util.List;


import com.everhomes.rest.organization.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;









import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.admin.OrgListChildrenOrganizationsRestResponse;
import com.everhomes.rest.ui.launchpad.LaunchpadGetLastLaunchPadLayoutBySceneRestResponse;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.schema.tables.pojos.EhAclRoleAssignments;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class ListChildrenOrganizationsTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListChildrenOrganizations() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/listChildrenOrganizations";
        
        ListAllChildrenOrganizationsCommand cmd = new ListAllChildrenOrganizationsCommand();
        cmd.setId(1000750L);
        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        cmd.setGroupTypes(groupTypes);

        OrgListChildrenOrganizationsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, OrgListChildrenOrganizationsRestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getDtos());
        assertEquals(6, response.getResponse().getDtos().size());

        boolean flag = false;
        for (OrganizationDTO dto: response.getResponse().getDtos()) {
            if(1000752L == dto.getId()){
                assertEquals(2, dto.getManagers().size());
                for (OrganizationManagerDTO managerDto:dto.getManagers()) {
                    if(managerDto.getMemberId() == 8L || managerDto.getMemberId() == 9L){
                        flag = true;
                    }else{
                        flag = false;
                    }
                }
            }
        }

        assertEquals(true, flag);
    }
    
    
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
        String filePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath = "data/json/organizations-data-test_20161014.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

    }
}

