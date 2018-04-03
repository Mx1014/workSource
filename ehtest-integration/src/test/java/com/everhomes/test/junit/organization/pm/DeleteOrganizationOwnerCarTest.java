package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.DeleteOrganizationOwnerCarCommand;
import com.everhomes.rest.organization.pm.OrganizationOwnerCarStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerCarAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerCarsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerOwnerCarRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class DeleteOrganizationOwnerCarTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testDeleteOrganizationOwnerCar() {
        DSLContext context = dbProvider.getDslContext();
        EhOrganizationOwnerCarsRecord ownersRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_CARS)
                .where(Tables.EH_ORGANIZATION_OWNER_CARS.ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.STATUS.eq(OrganizationOwnerCarStatus.NORMAL.getCode()))
                .fetchOne();

        assertNotNull("The ownersRecord should not be not null.", ownersRecord);

        EhOrganizationOwnerCarAttachmentsRecord attachmentsRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.OWNER_ID.eq(1L))
                .fetchAny();

        assertNotNull("The attachmentsRecord should not be not null.", attachmentsRecord);

        EhOrganizationOwnerOwnerCarRecord ownerOwnerCarRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(1L))
                .fetchAny();

        assertNotNull("The ownerOwnerCarRecord should not be not null.", ownerOwnerCarRecord);

        logon();
        String api = "/pm/deleteOrganizationOwnerCar";
        DeleteOrganizationOwnerCarCommand cmd = new DeleteOrganizationOwnerCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);

        ownersRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_CARS)
                .where(Tables.EH_ORGANIZATION_OWNER_CARS.ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.STATUS.eq(OrganizationOwnerCarStatus.DELETE.getCode()))
                .fetchOne();

        assertNotNull("The ownersRecord should be null.", ownersRecord);

        attachmentsRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.OWNER_ID.eq(1L))
                .fetchOne();

        assertNull("The attachmentsRecord should be null.", attachmentsRecord);

        ownerOwnerCarRecord = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(1L))
                .fetchOne();

        assertNull("The ownerOwnerCarRecord should be null.", ownerOwnerCarRecord);
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
        // userInfoFilePath = "data/json/customer-manage-delete-owner-car-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
