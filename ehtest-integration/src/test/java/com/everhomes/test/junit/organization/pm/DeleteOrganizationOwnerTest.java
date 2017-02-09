package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.DeleteOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.OrganizationOwnerStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhGroupMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerOwnerCarRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class DeleteOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    // 删除业主及删除业主关联的车辆及附件记录
    @Test
    public void testDeleteOrganizationOwner() {
        // initOwnerData();
        DSLContext context = dbProvider.getDslContext();
        EhOrganizationOwnersRecord record = context.selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
                .fetchOne();

        assertNotNull("The ownersRecord should be not null.", record);

        EhOrganizationOwnerAttachmentsRecord attachmentsRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.OWNER_ID.eq(1L))
                .fetchAny();

        assertNotNull("The attachmentsRecord should be not null.", attachmentsRecord);

        EhOrganizationOwnerOwnerCarRecord ownerOwnerCarRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(1L))
                .fetchAny();

        assertNotNull("The ownerOwnerCarRecord should be not null.", ownerOwnerCarRecord);

        Object[] addressRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .fetchAnyArray();

        assertNotNull("The addressRecord should be not null.", addressRecord);

        logon();
        String api = "/pm/deleteOrganizationOwner";
        DeleteOrganizationOwnerCommand cmd = new DeleteOrganizationOwnerCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);

        addressRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(1L))
                .fetchAnyArray();

        assertNull("The addressRecord should not be null.", addressRecord);

        record = context.selectFrom(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.DELETE.getCode()))
                .fetchOne();

        assertNotNull("The ownersRecord should not be not null.", record);

        attachmentsRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.OWNER_ID.eq(1L))
                .fetchOne();

        assertNull("The attachmentsRecord should be null.", attachmentsRecord);

        ownerOwnerCarRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID.eq(1L))
                .fetchOne();

        assertNull("The ownerOwnerCarRecord should be null.", ownerOwnerCarRecord);
    }

    // 删除业主, 同时删除对应的group member
    @Test
    public void testDeleteOrganizationOwnerWithDeleteGroupMember() {
        // initGroupMemberData();
        DSLContext context = dbProvider.getDslContext();

        logon();
        String api = "/pm/deleteOrganizationOwner";
        DeleteOrganizationOwnerCommand cmd = new DeleteOrganizationOwnerCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);

        EhGroupMembersRecord membersRecord = context.selectFrom(Tables.EH_GROUP_MEMBERS)
                .where(Tables.EH_GROUP_MEMBERS.ID.eq(100001L)).fetchOne();

        assertNull("The ownerOwnerCarRecord should be null.", membersRecord);
    }

    private void logon() {
        Integer namespaceId = 0;
        String userIdentifier = "12900000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
    }

    @Override
    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);

        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    /*private void initOwnerData() {
        String userInfoFilePath = "data/json/customer-manage-delete-owner-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    private void initGroupMemberData() {
        String userInfoFilePath = "data/json/customer-manage-update-owner-address-authtype-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }*/
}
