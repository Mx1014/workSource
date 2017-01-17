package com.everhomes.test.junit.pmkexing;

import com.everhomes.rest.pmkexing.*;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PmKeXingBillTest extends BaseLoginAuthTestCase {

    //1. 获取用户为管理员的公司列表
    private static final String LIST_ORGANIZATIONS_BY_PM_ADMIN_URL = "/pmkexing/listOrganizationsByPmAdmin";
    //2. 获取公司账单列表
    private static final String LIST_PM_KE_XING_BILLS_URL = "/pmkexing/listPmKeXingBills";
    //3. 获取公司物业账单统计信息
    private static final String GET_PM_KE_XING_BILL_STAT_URL = "/pmkexing/getPmKeXingBillStat";
    //4. 获取账单详情
    private static final String GET_PM_KE_XING_BILL_URL = "/pmkexing/getPmKeXingBill";

    private String userIdentifier = "12900000001";
    private String plainTextPwd = "123456";
    private Integer namespaceId = 0;
    private Long organizationId = 1000001L;

    //1. 获取用户为管理员的公司列表
    @Test
    public void testListOrganizationsByPmAdmin() {
        logon();

        ListOrganizationsByPmAdminRestResponse response = httpClientService.restPost(LIST_ORGANIZATIONS_BY_PM_ADMIN_URL, null, ListOrganizationsByPmAdminRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<ListOrganizationsByPmAdminDTO> organizations = response.getResponse();
        assertNotNull(organizations);

        assertTrue("The organizations list size should be 1, actual is " + organizations.size(), organizations.size() == 1);
        assertTrue("The organizations addresses list size should be 3, actual is " + organizations.get(0).getAddresses().size(),
                organizations.get(0).getAddresses().size() == 3);
    }

    //2. 获取公司账单列表
    @Test
    public void testListPmKeXingBills() {
        logon();
        ListPmKeXingBillsCommand cmd = new ListPmKeXingBillsCommand();
        cmd.setOrganizationId(organizationId);
        // cmd.setBillStatus((byte) 1);
        cmd.setPageSize(10);
        cmd.setPageOffset(3);
        ListPmKeXingBillsRestResponse response = httpClientService.restPost(LIST_PM_KE_XING_BILLS_URL, cmd, ListPmKeXingBillsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        ListPmKeXingBillsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertNotNull("The kexing pm bills list should not be null.", myResponse.getBills());
    }

    //3. 获取公司物业账单统计信息
    @Test
    public void testGetPmKeXingBillStat() {
        logon();
        GetPmKeXingBillStatCommand cmd = new GetPmKeXingBillStatCommand();
        cmd.setOrganizationId(organizationId);
        GetPmKeXingBillStatRestResponse response = httpClientService.restPost(GET_PM_KE_XING_BILL_STAT_URL, cmd, GetPmKeXingBillStatRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //4. 获取账单详情
    @Test
    public void testGetPmKeXingBill() {
        logon();
        GetPmKeXingBillCommand cmd = new GetPmKeXingBillCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setDateStr("2017-01");
        GetPmKeXingBillRestResponse response = httpClientService.restPost(GET_PM_KE_XING_BILL_URL, cmd, GetPmKeXingBillRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        jsonFilePath = "data/json/pmkexing-2.0-test-data-161229.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }
}