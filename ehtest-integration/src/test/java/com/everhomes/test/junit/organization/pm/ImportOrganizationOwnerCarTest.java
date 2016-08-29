package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.organization.pm.ImportOrganizationOwnerCarsCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerCars;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import org.jooq.DSLContext;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class ImportOrganizationOwnerCarTest extends BaseLoginAuthTestCase {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testImportOrganizationOwnerCar() throws IOException {
        logon();
        String api = "/pm/importOrganizationOwnerCars";
        ImportOrganizationOwnerCarsCommand cmd = new ImportOrganizationOwnerCarsCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);

        String filePath = new File("").getCanonicalPath() + "/src/test/data/excel/organization_owner_car_template.xlsx";
        RestResponseBase resp = httpClientService.postFile(api, cmd, new File(filePath), RestResponseBase.class);

        assertNotNull(resp);
        assertTrue("The response should be OK", httpClientService.isReponseSuccess(resp));

        DSLContext context = dbProvider.getDslContext();
        List<EhOrganizationOwnerCars> cars = context.selectFrom(Tables.EH_ORGANIZATION_OWNER_CARS)
                .fetchInto(EhOrganizationOwnerCars.class);
        assertTrue("The owners list size should be 5.", cars.size() == 5);

        for (int i = 0; i < cars.size(); i++) {
            EhOrganizationOwnerCars r = cars.get(i);
            assertEquals("奔驰", r.getBrand());
            assertEquals("B20", r.getParkingSpace());
            assertEquals("京A1234" + (i + 1), r.getPlateNumber());
            assertEquals("张三", r.getContacts());
            assertEquals("白色", r.getColor());
            assertEquals("24206890946790405", r.getCommunityId()+"");
            assertEquals("13800138000", r.getContactNumber());
        }
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
        userInfoFilePath = "data/json/customer-manage-import-owner-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    @Override
    public void tearDown() {
        logoff();
    }
}
