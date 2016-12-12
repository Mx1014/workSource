// @formatter:off
package com.everhomes.test.junit.organization;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.AddOrganizationPersonnelCommand;
import com.everhomes.rest.organization.CreateOrganizationCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositionMaps;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateChildrenOrganizationTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testCreateChildrenDepartment() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/org/createChildrenOrganization";

        CreateOrganizationCommand cmd = new CreateOrganizationCommand();
        cmd.setGroupType("DEPARTMENT");
        cmd.setName("bumen");
        cmd.setParentId(1000750L);

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
                .where(Tables.EH_ORGANIZATIONS.NAME.eq("bumen"))
                .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(1000750L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("DEPARTMENT"))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultOrganizations.size());
        EhOrganizations organization = resultOrganizations.get(0);

        List<EhOrganizations> resultManagers = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(organization.getId()))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("MANAGER"))
                .fetch().map((r) -> {
            resultManagers.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultManagers.size());
        EhOrganizations manager = resultManagers.get(0);

        List<EhOrganizationMembers> resultMember = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(manager.getId()))
                .fetch().map((r) -> {
            resultMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        assertEquals(2, resultMember.size());

    }


    @Test
    public void testCreateChildrenJobPosistion() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);

        String commandRelativeUri = "/admin/org/createChildrenOrganization";

        CreateOrganizationCommand cmd = new CreateOrganizationCommand();
        cmd.setGroupType("JOB_POSITION");
        cmd.setName("gangwei");
        cmd.setParentId(1000750L);

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(1L);
        memberIds.add(3L);
        cmd.setAddMemberIds(memberIds);

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
                .where(Tables.EH_ORGANIZATIONS.NAME.eq("gangwei"))
                .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(1000750L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("JOB_POSITION"))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultOrganizations.size());
        EhOrganizations organization = resultOrganizations.get(0);

        List<EhOrganizationJobPositionMaps> resultMaps = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS)
                .where(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ORGANIZATION_ID.eq(organization.getId()))
                .and(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.JOB_POSITION_ID.in(jobPosistionIds))
                .fetch().map((r) -> {
            resultMaps.add(ConvertHelper.convert(r, EhOrganizationJobPositionMaps.class));
            return null;
        });
        assertEquals(2, resultMaps.size());
        List<EhOrganizationMembers> resultMember = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(organization.getId()))
                .fetch().map((r) -> {
            resultMember.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        assertEquals(2, resultMember.size());

    }

    @Test
    public void testCreateChildrenJobLevel() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);

        String commandRelativeUri = "/admin/org/createChildrenOrganization";

        CreateOrganizationCommand cmd = new CreateOrganizationCommand();
        cmd.setGroupType("JOB_LEVEL");
        cmd.setName("zhiji");
        cmd.setParentId(1000750L);

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(1L);
        memberIds.add(5L);
        cmd.setAddMemberIds(memberIds);

        cmd.setSize(100);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);

        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhOrganizations> resultOrganizations = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext();

        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAME.eq("zhiji"))
                .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(1000750L))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq("JOB_LEVEL"))
                .and(Tables.EH_ORGANIZATIONS.SIZE.eq(100))
                .fetch().map((r) -> {
            resultOrganizations.add(ConvertHelper.convert(r, EhOrganizations.class));
            return null;
        });
        assertEquals(1, resultOrganizations.size());
        EhOrganizations organization = resultOrganizations.get(0);

        List<EhOrganizationMembers> resultMember = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(organization.getId()))
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
        
        userInfoFilePath = "data/json/organization-member-test-data_161104.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);


        
    }
}

