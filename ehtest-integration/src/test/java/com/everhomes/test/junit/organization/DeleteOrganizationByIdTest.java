// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.CreateOrganizationCommand;
import com.everhomes.rest.organization.DeleteOrganizationIdCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositionMaps;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DeleteOrganizationByIdTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testDeleteOrganizationById() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/deleteOrganizationById";

        DeleteOrganizationIdCommand cmd = new DeleteOrganizationIdCommand();
        cmd.setId(1000750L);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhOrganizations> resultOrganizations = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext();

        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.PATH.like("1000750%"))
                .and(Tables.EH_ORGANIZATIONS.STATUS.eq((byte)2))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(0, resultOrganizations.size());

    }


    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	
        String filePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath = "data/json/organizations-data-test_20161108.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);


        
    }
}

