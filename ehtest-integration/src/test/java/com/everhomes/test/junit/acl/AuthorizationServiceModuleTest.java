package com.everhomes.test.junit.acl;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.acl.admin.AuthorizationServiceModule;
import com.everhomes.rest.acl.admin.AuthorizationServiceModuleCommand;
import com.everhomes.rest.acl.admin.CreateServiceModuleAdminCommand;
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

/**
 * Created by sfyan on 2016/11/3.
 */
public class AuthorizationServiceModuleTest extends BaseLoginAuthTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        logoff();
    }

    @Test
    public void testAuthorizationServiceModule(){
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        String commandRelativeUri = "/acl/authorizationServiceModule";
        AuthorizationServiceModuleCommand cmd = new AuthorizationServiceModuleCommand();
        cmd.setTargetType("EhOrganizations");
        cmd.setTargetId(1000751L);
        cmd.setOrganizationId(1000750L);

        List<Long> moduleIds1 = new ArrayList<>();
        moduleIds1.add(1L);
        moduleIds1.add(2L);

        List<Long> moduleIds2 = new ArrayList<>();
        moduleIds2.add(3L);
        moduleIds2.add(4L);
        moduleIds2.add(5L);

        List<AuthorizationServiceModule> serviceModuleAuthorizations = new ArrayList<>();
        AuthorizationServiceModule serviceModuleAuthorization1 = new AuthorizationServiceModule();
        serviceModuleAuthorization1.setAllModuleFlag((byte)0);
        serviceModuleAuthorization1.setModuleIds(moduleIds1);
        serviceModuleAuthorization1.setResourceId(24210090697425925L);
        serviceModuleAuthorization1.setResourceType("EhCommunities");
        serviceModuleAuthorizations.add(serviceModuleAuthorization1);
        AuthorizationServiceModule serviceModuleAuthorization2 = new AuthorizationServiceModule();
        serviceModuleAuthorization2.setAllModuleFlag((byte)0);
        serviceModuleAuthorization2.setModuleIds(moduleIds2);
        serviceModuleAuthorization2.setResourceId(24210090697425926L);
        serviceModuleAuthorization2.setResourceType("EhCommunities");
        serviceModuleAuthorizations.add(serviceModuleAuthorization2);
        AuthorizationServiceModule serviceModuleAuthorization3 = new AuthorizationServiceModule();
        serviceModuleAuthorization3.setAllModuleFlag((byte)1);
        serviceModuleAuthorization3.setResourceId(24210090697425927L);
        serviceModuleAuthorization3.setResourceType("EhCommunities");
        serviceModuleAuthorizations.add(serviceModuleAuthorization3);
        cmd.setServiceModuleAuthorizations(serviceModuleAuthorizations);


        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));


//        List<EhAclRoleAssignments> resultAcl = new ArrayList<>();
//
        DSLContext context = dbProvider.getDslContext();
//        context.select().from(EH_ACL_ROLE_ASSIGNMENTS)
//                .where(EH_ACL_ROLE_ASSIGNMENTS.OWNER_TYPE.eq("EhOrganizations"))
//                .and(EH_ACL_ROLE_ASSIGNMENTS.OWNER_ID.eq(1000750L))
//                .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_TYPE.eq("EhUsers"))
//                .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_ID.eq(10001L))
//                .and(EH_ACL_ROLE_ASSIGNMENTS.ROLE_ID.eq(1005L))
//                .fetch().map((r) -> {
//            resultAcl.add(ConvertHelper.convert(r, EhAclRoleAssignments.class));
//            return null;
//        });
//        assertNotNull("The reponse of getting acl role assignments info may not be null", resultAcl);
//        assertEquals(1, resultAcl.size());

        List<EhServiceModuleAssignments> resultModuleAssignments = new ArrayList<>();

        context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS)
                .where(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq("EhOrganizations"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(1000751L))
                .fetch().map((r) -> {
            resultModuleAssignments.add(ConvertHelper.convert(r, EhServiceModuleAssignments.class));
            return null;
        });
        assertNotNull("The reponse of getting service module assignments info may not be null", resultModuleAssignments);
        assertEquals(6, resultModuleAssignments.size());

        List<EhServiceModuleAssignments> resultModuleAssignments1 = new ArrayList<>();

        context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS)
                .where(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq("EhCommunities"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(24210090697425925L))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq("EhOrganizations"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(1000751L))
                .fetch().map((r) -> {
            resultModuleAssignments1.add(ConvertHelper.convert(r, EhServiceModuleAssignments.class));
            return null;
        });
        assertNotNull("The reponse of getting service module assignments info may not be null", resultModuleAssignments1);
        assertEquals(2, resultModuleAssignments1.size());

        List<EhServiceModuleAssignments> resultModuleAssignments2 = new ArrayList<>();

        context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS)
                .where(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq("EhCommunities"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(24210090697425926L))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq("EhOrganizations"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(1000751L))
                .fetch().map((r) -> {
            resultModuleAssignments2.add(ConvertHelper.convert(r, EhServiceModuleAssignments.class));
            return null;
        });
        assertNotNull("The reponse of getting service module assignments info may not be null", resultModuleAssignments2);
        assertEquals(3, resultModuleAssignments2.size());

        List<EhServiceModuleAssignments> resultModuleAssignments3 = new ArrayList<>();
        context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS)
                .where(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq("EhCommunities"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(24210090697425927L))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq("EhOrganizations"))
                .and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(1000751L))
                .fetch().map((r) -> {
            resultModuleAssignments3.add(ConvertHelper.convert(r, EhServiceModuleAssignments.class));
            return null;
        });
        assertNotNull("The reponse of getting service module assignments info may not be null", resultModuleAssignments3);
        assertEquals(1, resultModuleAssignments3.size());

    }

    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        userInfoFilePath = "data/json/3.4.x-test-data-create-organization_member_160605.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        userInfoFilePath = "data/json/service_module_privilege-test-data_161017.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

    }
}
