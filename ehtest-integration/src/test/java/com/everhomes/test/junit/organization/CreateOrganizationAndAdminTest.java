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
import com.everhomes.rest.organization.AddNewOrganizationInZuolinCommand;
import com.everhomes.schema.tables.pojos.EhAclRoleAssignments;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.util.EncryptionUtils;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class CreateOrganizationAndAdminTest extends BaseLoginAuthTestCase{
	
	@Before
    public void setUp() {
        super.setUp();
    }
	
	@Test
    public void testCreateOrganizationAndAdmin() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/addNewOrganization";
        
        AddNewOrganizationInZuolinCommand cmd = new AddNewOrganizationInZuolinCommand();
//        orgName: 企业名称 
//        organizationType: 企业类型 参考com.everhomes.rest.organization.OrganizationType 
//        contactor: 管理员姓名 
//        mobile: 手机号 
//        namespaceId: 域空间id 
//        communityId: 园区id 
        cmd.setContactor("admin");
        cmd.setMobile("12099998888");
        cmd.setOrganizationType("ENTERPRISE");
        cmd.setOrgName("superdan");
        
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhOrganizations> orgs = new ArrayList<EhOrganizations>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_ORGANIZATIONS).fetch().map((r) -> {
        	orgs.add(ConvertHelper.convert(r,EhOrganizations.class));
        	return null;
        });
        
        assertNotNull("The reponse of getting organization may not be null", orgs);
        assertEquals(1, orgs.size());
        assertEquals("ENTERPRISE", orgs.get(0).getOrganizationType());
        assertEquals("superdan", orgs.get(0).getName());
        
        
        List<EhAclRoleAssignments> resultAcl = new ArrayList<EhAclRoleAssignments>();
        
        context.select().from(EH_ACL_ROLE_ASSIGNMENTS) 
            .where(EH_ACL_ROLE_ASSIGNMENTS.OWNER_TYPE.eq("EhOrganizations"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.OWNER_ID.eq(orgs.get(0).getId()))
            .and(EH_ACL_ROLE_ASSIGNMENTS.ROLE_ID.eq(1005L))
            .fetch().map((r) -> {
            	resultAcl.add(ConvertHelper.convert(r, EhAclRoleAssignments.class));
                return null;
            });
        
        assertNotNull("The reponse of getting acl role assignments info may not be null", resultAcl);
        assertEquals(1, resultAcl.size());
        
        List<EhUsers> user = new ArrayList<EhUsers>();
        context.select().from(EH_USERS) 
        .where(EH_USERS.ID.eq(resultAcl.get(0).getTargetId()))
        .fetch().map((r) -> {
        	user.add(ConvertHelper.convert(r, EhUsers.class));
            return null;
        });
        String password = EncryptionUtils.hashPassword(String.format("%s%s", plainTexPassword, user.get(0).getSalt()));
        assertEquals(password, user.get(0).getPasswordHash());
    }
    
	@After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
        userInfoFilePath = "data/json/videoconf3.0-test-data-create-organization_and_admin.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
}
