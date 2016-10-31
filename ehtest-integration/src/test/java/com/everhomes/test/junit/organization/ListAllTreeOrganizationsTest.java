// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.admin.OrgListChildrenOrganizationsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListAllTreeOrganizationsTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListAllTreeOrganizations() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/org/listAllTreeOrganizations";

        ListAllTreeOrganizationsCommand cmd = new ListAllTreeOrganizationsCommand();
        cmd.setOrganizationId(1000750L);

        ListAllTreeOrganizationsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListAllTreeOrganizationsRestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        OrganizationTreeDTO dto = response.getResponse();
        this.assertOrganizationTree(dto, 0);
    }

    private void assertOrganizationTree(OrganizationTreeDTO dto, Integer num){
        boolean flag = true;
        if(1000751L == dto.getOrganizationId()){
            flag = (1 == dto.getTrees().size());
            num ++;
        }

        if(1000752L == dto.getOrganizationId()){
            flag = (2 == dto.getTrees().size());
            num ++;
        }

        if(1000752L == dto.getOrganizationId()){
            flag = (2 == dto.getTrees().size());
            num ++;
        }

        if(1000758L == dto.getOrganizationId()){
            flag = (2 == dto.getTrees().size());
            num ++;
        }
        if(1000756L == dto.getOrganizationId()){
            flag = (2 == dto.getTrees().size());
            num ++;
        }

        assertEquals(true, flag);

        if(5 == num){
            return;
        }

        for (OrganizationTreeDTO treeDTO: dto.getTrees()) {
            if(null != treeDTO.getTrees()){
                this.assertOrganizationTree(treeDTO, num);
            }
        }

        return;
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
        
    }
}

