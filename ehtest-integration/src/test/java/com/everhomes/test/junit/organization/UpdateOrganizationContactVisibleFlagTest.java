// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.organization.ListAllChildrenOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.UpdateOrganizationContactVisibleFlagCommand;
import com.everhomes.rest.organization.VisibleFlag;
import com.everhomes.rest.organization.admin.OrgListChildrenOrganizationsRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrganizationContactVisibleFlagTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateOrganizationContactVisibleFlag() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/org/updateOrganizationContactVisibleFlag";

        UpdateOrganizationContactVisibleFlagCommand cmd = new UpdateOrganizationContactVisibleFlagCommand();
        cmd.setOrganizationId(1000750L);
        cmd.setContactToken("12000000001");
        cmd.setVisibleFlag(VisibleFlag.HIDE.getCode());
        RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));


        List<EhOrganizationMembers> members = new ArrayList<EhOrganizationMembers>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(1000750L).and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq("12000000001")).and(Tables.EH_ORGANIZATION_MEMBERS.VISIBLE_FLAG.eq(VisibleFlag.HIDE.getCode()))).fetch().map((r) -> {
            members.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });

        assertNotNull("The reponse of getting organization may not be null", members);
        assertEquals(1, members.size());
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

