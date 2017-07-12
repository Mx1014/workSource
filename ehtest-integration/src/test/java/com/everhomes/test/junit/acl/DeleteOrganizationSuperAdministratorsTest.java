package com.everhomes.test.junit.acl;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.schema.tables.EhAclRoleAssignments;
import com.everhomes.schema.tables.pojos.EhAcls;
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
import static com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS;

/**
 * Created by sfyan on 2016/11/3.
 */
public class DeleteOrganizationSuperAdministratorsTest extends BaseLoginAuthTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        logoff();
    }

    @Test
    public void testDeleteOrganizationSuperAdministrators(){
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        String commandRelativeUri = "/acl/deleteOrganizationSuperAdministrators";
        DeleteOrganizationAdminCommand cmd = new DeleteOrganizationAdminCommand();
        cmd.setOwnerId(1000750L);
        cmd.setOwnerType("EhOrganizations");
        cmd.setOrganizationId(1000750L);
        cmd.setUserId(10001L);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));


        List<EhAclRoleAssignments> resultRoles = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_ACL_ROLE_ASSIGNMENTS)
                .where(EH_ACL_ROLE_ASSIGNMENTS.OWNER_TYPE.eq("EhOrganizations"))
                .and(EH_ACL_ROLE_ASSIGNMENTS.OWNER_ID.eq(1000750L))
                .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_TYPE.eq("EhUsers"))
                .and(EH_ACL_ROLE_ASSIGNMENTS.TARGET_ID.eq(10001L))
                .and(EH_ACL_ROLE_ASSIGNMENTS.ROLE_ID.eq(1001L))
                .fetch().map((r) -> {
            resultRoles.add(ConvertHelper.convert(r, EhAclRoleAssignments.class));
            return null;
        });
        assertEquals(0, resultRoles.size());

        List<EhAcls> resultAcls = new ArrayList<>();
        context.select().from(EH_ACLS)
                .where(EH_ACLS.OWNER_TYPE.eq("EhOrganizations"))
                .and(EH_ACLS.OWNER_ID.eq(1000750L))
                .and(EH_ACLS.ROLE_TYPE.eq("EhUsers"))
                .and(EH_ACLS.ROLE_ID.eq(10001L))
                .and(EH_ACLS.PRIVILEGE_ID.eq(10L))
                .fetch().map((r) -> {
            resultAcls.add(ConvertHelper.convert(r, EhAcls.class));
            return null;
        });
        assertEquals(0, resultAcls.size());
    }

    protected void initCustomData() {

        String file = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(file);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        filePath= "data/json/acl_role_assignments-test-data_161017.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/organization-member-test-data_161104.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);
    }
}
