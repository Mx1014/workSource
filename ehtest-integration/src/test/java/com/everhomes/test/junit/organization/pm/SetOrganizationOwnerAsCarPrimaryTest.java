package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.OrganizationOwnerOwnerCarPrimaryFlag;
import com.everhomes.rest.organization.pm.SetOrganizationOwnerAsCarPrimaryCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

import java.util.Map;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class SetOrganizationOwnerAsCarPrimaryTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testSetOrganizationOwnerAsCarPrimary() {
        logon();
        String api = "/pm/setOrganizationOwnerAsCarPrimary";
        SetOrganizationOwnerAsCarPrimaryCommand cmd = new SetOrganizationOwnerAsCarPrimaryCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);
        cmd.setCarId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);

        DSLContext context = dbProvider.getDslContext();
        Map<String, Object> result = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.PRIMARY_FLAG.eq(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.eq(1L))
                .fetchOneMap();

        assertNotNull("The primary result should be not null.", result);

        result = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.PRIMARY_FLAG.eq(OrganizationOwnerOwnerCarPrimaryFlag.NORMAL.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.eq(2L))
                .fetchOneMap();

        assertNotNull("The normal result should be not null.", result);
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
        // userInfoFilePath = "data/json/customer-manage-set-owner-car-primary-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
