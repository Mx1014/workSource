// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.AddOrganizationPersonnelCommand;
import com.everhomes.rest.organization.CreateOrganizationAccountCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.schema.tables.pojos.EhAclRoleAssignments;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS;
import static com.everhomes.server.schema.Tables.EH_ORGANIZATION_MEMBERS;
import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;

public class AddOrganizationPersonnelTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testAddOrganizationPersonnel() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/addOrganizationPersonnel";
        
        AddOrganizationPersonnelCommand cmd = new AddOrganizationPersonnelCommand();
        cmd.setContactName("zhangsan");
        cmd.setContactToken("12000000022");
        cmd.setEmployeeNo("4");
        cmd.setTargetId(10022L);
        cmd.setTargetType("EhUsers");
        cmd.setOrganizationId(1000750L);
        cmd.setGender((byte)1);
        List<Long> departmentIds = new ArrayList<>();
        departmentIds.add(1000752L);
        departmentIds.add(1000753L);
        cmd.setDepartmentIds(departmentIds);

        List<Long> jobPositionIds = new ArrayList<>();
        jobPositionIds.add(1000770L);
        jobPositionIds.add(1000771L);
        cmd.setJobPositionIds(jobPositionIds);
        cmd.setOrganizationId(1000750L);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        
        List<EhOrganizationMembers> resultMember = new ArrayList<>();

        List<Long> ids = new ArrayList<>();
        ids.add(1000750L);
        ids.add(1000752L);
        ids.add(1000753L);
        ids.add(1000770L);
        ids.add(1000771L);

        DSLContext context = dbProvider.getDslContext();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
            .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(ids))
            .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(10022L))
            .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq("EhUsers"))
            .fetch().map((r) -> {
            resultMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
                return null;
        });
        
        assertEquals(5, resultMember.size());

    }




    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
        userInfoFilePath = "data/json/organizations-data-test_20161108.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
}

