// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.CreateOrganizationCommand;
import com.everhomes.rest.organization.UpdateOrganizationsCommand;
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

public class UpdateChildrenOrganizationTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testUpdateChildrenDepartment() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);

        String commandRelativeUri = "/admin/org/updateOrganization";

        UpdateOrganizationsCommand cmd = new UpdateOrganizationsCommand();
        cmd.setId(1000752L);
        cmd.setName("帮帮忙");
        List<Long> delManagerMemberIds = new ArrayList<>();
        delManagerMemberIds.add(9L);
        cmd.setDelManagerMemberIds(delManagerMemberIds);
        List<Long> addManagerMemberIds = new ArrayList<>();
        addManagerMemberIds.add(1L);
        addManagerMemberIds.add(3L);
        cmd.setAddManagerMemberIds(addManagerMemberIds);

        List<Long> managerMemberIds = new ArrayList<>();
        managerMemberIds.add(1L);
        managerMemberIds.add(3L);
        cmd.setAddManagerMemberIds(managerMemberIds);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhOrganizations> resultOrganizations = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext();

        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAME.eq("帮帮忙"))
                .and(Tables.EH_ORGANIZATIONS.ID.eq(1000752L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("DEPARTMENT"))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultOrganizations.size());

        List<EhOrganizations> resultManagers = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(1000752L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("MANAGER"))
                .fetch().map((r) -> {
            resultManagers.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultManagers.size());

        List<Long> userIds = new ArrayList<>();
        userIds.add(10001L);
        userIds.add(10002L);
        userIds.add(10015L);
        List<EhOrganizationMembers> resultMember = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(1000790L))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.in(userIds))
                .fetch().map((r) -> {
            resultMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        assertEquals(3, resultMember.size());

    }


    @Test
    public void testUpdateChildrenJobPosistion() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);

        String commandRelativeUri = "/admin/org/updateOrganization";

        UpdateOrganizationsCommand cmd = new UpdateOrganizationsCommand();
        cmd.setName("gggwwww");
        cmd.setId(1000770L);

        List<Long> delMemberIds = new ArrayList<>();
        delMemberIds.add(5L);
        cmd.setDelMemberIds(delMemberIds);

        List<Long> addMemberIds = new ArrayList<>();
        addMemberIds.add(1L);
        addMemberIds.add(3L);
        cmd.setAddMemberIds(addMemberIds);

        List<Long> jobPosistionIds = new ArrayList<>();
        jobPosistionIds.add(1L);
        jobPosistionIds.add(2L);
        cmd.setJobPositionIds(jobPosistionIds);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhOrganizations> resultOrganizations = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext();

        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAME.eq("gggwwww"))
                .and(Tables.EH_ORGANIZATIONS.ID.eq(1000770L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("JOB_POSITION"))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultOrganizations.size());

        List<EhOrganizationJobPositionMaps> resultMaps = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS)
                .where(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ORGANIZATION_ID.eq(1000770L))
                .and(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.JOB_POSITION_ID.in(jobPosistionIds))
                .fetch().map((r) -> {
            resultMaps.add(ConvertHelper.convert(r, EhOrganizationJobPositionMaps.class));
            return null;
        });
        assertEquals(2, resultMaps.size());
        List<EhOrganizationMembers> resultMember = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(1000770L))
                .fetch().map((r) -> {
            resultMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        assertEquals(3, resultMember.size());

    }

    @Test
    public void testUpdateChildrenJobLevel() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);

        String commandRelativeUri = "/admin/org/updateOrganization";

        UpdateOrganizationsCommand cmd = new UpdateOrganizationsCommand();
        cmd.setName("zzzzzjjjjj");
        cmd.setId(1000780L);

        List<Long> delMemberIds = new ArrayList<>();
        delMemberIds.add(7L);
        cmd.setDelMemberIds(delMemberIds);

        List<Long> addMemberIds = new ArrayList<>();
        addMemberIds.add(1L);
        addMemberIds.add(3L);
        cmd.setAddMemberIds(addMemberIds);

        cmd.setSize(100);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhOrganizations> resultOrganizations = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext();

        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAME.eq("zzzzzjjjjj"))
                .and(Tables.EH_ORGANIZATIONS.ID.eq(1000780L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("JOB_LEVEL"))
                .and(Tables.EH_ORGANIZATIONS.SIZE.eq(100))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultOrganizations.size());

        List<EhOrganizationMembers> resultMember = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(1000780L))
                .fetch().map((r) -> {
            resultMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        assertEquals(2, resultMember.size());

    }


    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
        userInfoFilePath = "data/json/organization-member-test-data_161105.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

    }
}

