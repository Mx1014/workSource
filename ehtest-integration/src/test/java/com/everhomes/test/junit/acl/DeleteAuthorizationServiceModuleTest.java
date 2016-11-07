package com.everhomes.test.junit.acl;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.acl.DeleteAuthorizationServiceModuleCommand;
import com.everhomes.rest.acl.DeleteServiceModuleAdministratorsCommand;
import com.everhomes.schema.tables.pojos.EhAcls;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignments;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.everhomes.schema.Tables.EH_ACLS;

/**
 * Created by sfyan on 2016/11/3.
 */
public class DeleteAuthorizationServiceModuleTest extends BaseLoginAuthTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        logoff();
    }

    @Test
    public void testDeleteAuthorizationServiceModule(){
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        String commandRelativeUri = "/acl/deleteAuthorizationServiceModule";
        DeleteAuthorizationServiceModuleCommand cmd = new DeleteAuthorizationServiceModuleCommand();
        cmd.setOwnerId(1000750L);
        cmd.setOwnerType("EhOrganizations");
        cmd.setOrganizationId(1000751L);
        cmd.setResourceId(24210090697425926L);
        cmd.setResourceType("EhCommunities");
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        List<EhAcls> resultAcls = new ArrayList<>();
        context.select().from(EH_ACLS)
                .where(EH_ACLS.OWNER_TYPE.eq("EhCommunities"))
                .and(EH_ACLS.OWNER_ID.eq(24210090697425926L))
                .and(EH_ACLS.ROLE_TYPE.eq("EhOrganizations"))
                .and(EH_ACLS.ROLE_ID.eq(1000751L))
                .fetch().map((r) -> {
            resultAcls.add(ConvertHelper.convert(r, EhAcls.class));
            return null;
        });
        assertEquals(0, resultAcls.size());

        List<EhAcls> resultAcls1 = new ArrayList<>();
        context.select().from(EH_ACLS)
                .where(EH_ACLS.OWNER_TYPE.eq("EhCommunities"))
                .and(EH_ACLS.OWNER_ID.eq(24210090697425926L))
                .and(EH_ACLS.ROLE_TYPE.eq("EhUsers"))
                .and(EH_ACLS.ROLE_ID.eq(10010L))
                .and(EH_ACLS.PRIVILEGE_ID.eq(2L))
                .fetch().map((r) -> {
            resultAcls1.add(ConvertHelper.convert(r, EhAcls.class));
            return null;
        });
        assertEquals(0, resultAcls1.size());

        List<EhServiceModuleAssignments> results = new ArrayList<>();

        context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS)
                .where(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq("EhCommunities"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(24210090697425926L))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq("EhOrganizations"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(1000751L))
                .fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, EhServiceModuleAssignments.class));
            return null;
        });
        assertNotNull("The reponse of getting service module assignments info may not be null", results);
        assertEquals(0, results.size());

        List<EhServiceModuleAssignments> results1 = new ArrayList<>();

        context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS)
                .where(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq("EhCommunities"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(24210090697425926L))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq("EhUsers"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(10010L))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.eq(2L))
                .fetch().map((r) -> {
            results1.add(ConvertHelper.convert(r, EhServiceModuleAssignments.class));
            return null;
        });
        assertNotNull("The reponse of getting service module assignments info may not be null", results1);
        assertEquals(0, results1.size());
    }

    protected void initCustomData() {

        String file = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(file);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        filePath= "data/json/organization-member-test-data_161104.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/service_module_assignments-test-data_161017.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/acl_role_assignments-test-data_161017.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

    }
}
