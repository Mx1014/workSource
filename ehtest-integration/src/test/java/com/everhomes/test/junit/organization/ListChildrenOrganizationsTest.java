// @formatter:off
package com.everhomes.test.junit.organization;

import static com.everhomes.server.schema.Tables.*;
import static com.everhomes.schema.Tables.*;

import java.util.ArrayList;
import java.util.List;







import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;







import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.ListAllChildrenOrganizationsCommand;
import com.everhomes.rest.organization.ListOrganizationsCommandResponse;
import com.everhomes.rest.organization.OrganizationGroupType;
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
    public void testListChildrenOrganizations1() {
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
        cmd.setGroupTypes(groupTypes);
        
        ListOrganizationsCommandResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListOrganizationsCommandResponse.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
    }
    
    
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
        userInfoFilePath = "data/json/3.4.x-test-data-create-organization_member_160605.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
}

