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
import com.everhomes.rest.organization.CreateOrganizationAccountCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.schema.tables.pojos.EhAclRoleAssignments;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class CreateOrganizationAccountTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testCreateOrganizationAccount1() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/createOrganizationAccount";
        
        CreateOrganizationAccountCommand cmd = new CreateOrganizationAccountCommand();
        cmd.setAccountName("张三");
        cmd.setAccountPhone("12000000001");
        cmd.setOrganizationId(1000750L);
        
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        
        List<EhAclRoleAssignments> resultAcl = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_ACL_ROLE_ASSIGNMENTS) 
            .where(EH_ACL_ROLE_ASSIGNMENTS.OWNER_TYPE.eq("EhOrganizations"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.OWNER_ID.eq(1000750L))
            .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_TYPE.eq("EhUsers"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_ID.eq(10001L))
            .and(EH_ACL_ROLE_ASSIGNMENTS.ROLE_ID.eq(1005L))
            .fetch().map((r) -> {
            	resultAcl.add(ConvertHelper.convert(r, EhAclRoleAssignments.class));
                return null;
            });
        
        assertNotNull("The reponse of getting acl role assignments info may not be null", resultAcl);
        assertEquals(1, resultAcl.size());
    }
    
    @Test
    public void testCreateOrganizationAccount2() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/createOrganizationAccount";
        
        CreateOrganizationAccountCommand cmd = new CreateOrganizationAccountCommand();
        cmd.setAccountName("lisisi");
        cmd.setAccountPhone("12000000002");
        cmd.setOrganizationId(1000750L);
        
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        
        List<EhUserIdentifiers> resultUser = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_IDENTIFIERS)
            .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq("12000000002"))
            .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
            .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
            .fetch().map((r) -> {
            	resultUser.add(ConvertHelper.convert(r, EhUserIdentifiers.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user info may not be null", resultUser);
        assertEquals(1, resultUser.size());
        
        Long userId = resultUser.get(0).getOwnerUid();
        
        List<EhOrganizationMembers> resultOrgMember = new ArrayList<>();
        
        context.select().from(EH_ORGANIZATION_MEMBERS)
        .where(EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq("12000000002"))
        .and(EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(1000750L))
        .fetch().map((r) -> {
        	resultOrgMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        
        assertNotNull("The reponse of getting organization member info may not be null", resultOrgMember);
        assertEquals(1, resultOrgMember.size());
        EhOrganizationMembers member = resultOrgMember.get(0);
        
        assertEquals("USER", member.getTargetType());
        assertEquals(userId, member.getTargetId());
        
        List<EhAclRoleAssignments> resultAcl = new ArrayList<>();
        
        context.select().from(EH_ACL_ROLE_ASSIGNMENTS)
            .where(EH_ACL_ROLE_ASSIGNMENTS.OWNER_TYPE.eq("EhOrganizations"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.OWNER_ID.eq(1000750L))
            .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_TYPE.eq("EhUsers"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_ID.eq(userId))
            .and(EH_ACL_ROLE_ASSIGNMENTS.ROLE_ID.eq(1005L))
            .fetch().map((r) -> {
            	resultAcl.add(ConvertHelper.convert(r, EhAclRoleAssignments.class));
                return null;
            });
        
        assertNotNull("The reponse of getting acl role assignments info may not be null", resultAcl);
        assertEquals(1, resultAcl.size());
    }
    
    @Test
    public void testCreateOrganizationAccount3() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/createOrganizationAccount";
        
        CreateOrganizationAccountCommand cmd = new CreateOrganizationAccountCommand();
        cmd.setAccountName("ww");
        cmd.setAccountPhone("12000000003");
        cmd.setOrganizationId(1000750L);
        
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        
        List<EhUserIdentifiers> resultUser = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_IDENTIFIERS)
            .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq("12000000003"))
            .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
            .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
            .and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq((byte) 0))
            .fetch().map((r) -> {
            	resultUser.add(ConvertHelper.convert(r, EhUserIdentifiers.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user info may not be null", resultUser);
        assertEquals(1, resultUser.size());
        
        Long userId = resultUser.get(0).getOwnerUid();
        
        List<EhOrganizationMembers> resultOrgMember = new ArrayList<>();
        
        context.select().from(EH_ORGANIZATION_MEMBERS)
        .where(EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq("12000000003"))
        .and(EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(1000750L))
        .and(EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq("USER"))
        .and(EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId))
        .and(EH_ORGANIZATION_MEMBERS.STATUS.eq((byte) 3))
        .fetch().map((r) -> {
        	resultOrgMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        
        assertNotNull("The reponse of getting organization member may not be null", resultOrgMember);
        assertEquals(1, resultOrgMember.size());
        
        List<EhAclRoleAssignments> resultAcl = new ArrayList<>();
        
        context.select().from(EH_ACL_ROLE_ASSIGNMENTS)
            .where(EH_ACL_ROLE_ASSIGNMENTS.OWNER_TYPE.eq("EhOrganizations"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.OWNER_ID.eq(1000750L))
            .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_TYPE.eq("EhUsers"))
            .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_ID.eq(userId))
            .and(EH_ACL_ROLE_ASSIGNMENTS.ROLE_ID.eq(1005L))
            .fetch().map((r) -> {
            	resultAcl.add(ConvertHelper.convert(r, EhAclRoleAssignments.class));
                return null;
            });
        
        assertNotNull("The reponse of getting acl role assignments info may not be null", resultAcl);
        assertEquals(1, resultAcl.size());
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

