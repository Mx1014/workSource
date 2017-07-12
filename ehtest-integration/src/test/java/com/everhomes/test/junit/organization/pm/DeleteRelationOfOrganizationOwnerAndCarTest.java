package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.DeleteRelationOfOrganizationOwnerAndCarCommand;
import com.everhomes.rest.organization.pm.OrganizationOwnerOwnerCarPrimaryFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

import java.util.Map;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class DeleteRelationOfOrganizationOwnerAndCarTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    /**
     * 测试移除普通联系人的情况
     */
    @Test
    public void testDeleteRelationOfOrganizationOwnerAndCar() {
        logon();
        String api = "/pm/deleteRelationOfOrganizationOwnerAndCar";
        DeleteRelationOfOrganizationOwnerAndCarCommand cmd = new DeleteRelationOfOrganizationOwnerAndCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(1L);
        cmd.setCarId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK.", httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        Map<String, Object> result = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.eq(1L))
                .fetchOneMap();

        assertNull("The primary result should be null.", result);
    }

    /**
     * 测试移除首要联系人的情况
     */
    @Test
    public void testDeleteRelationOfOrganizationOwnerAndCarOfDeletePrimary() {
        logon();
        String api = "/pm/deleteRelationOfOrganizationOwnerAndCar";
        DeleteRelationOfOrganizationOwnerAndCarCommand cmd = new DeleteRelationOfOrganizationOwnerAndCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setOrgOwnerId(2L);
        cmd.setCarId(1L);

        RestResponseBase response = httpClientService.restPost(api, cmd, RestResponseBase.class);

        assertNotNull("The response should not be null.", response);
        assertTrue("The response should be OK.", httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        Map<String, Object> result = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.eq(1L))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.PRIMARY_FLAG.eq(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode()))
                .fetchOneMap();

        assertNotNull("The primary result should be null.", result);
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
        // userInfoFilePath = "data/json/customer-manage-delete-owner-car-relation-primary-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
