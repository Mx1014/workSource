package com.everhomes.test.junit.parking.clearance;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.parking.clearance.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkingClearanceOperatorsDao;
import com.everhomes.server.schema.tables.records.EhParkingClearanceLogsRecord;
import com.everhomes.server.schema.tables.records.EhParkingClearanceOperatorsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by xq.tian on 2016/12/5.
 */
public class ParkingClearanceTest extends BaseLoginAuthTestCase {

    //1. 新增车辆放行的申请人员或处理人员
    private static final String CREATE_CLEARANCE_OPERATOR_URL = "/clearance/createClearanceOperator";
    //2. 删除车辆放行的申请人员或处理人员
    private static final String DELETE_CLEARANCE_OPERATOR_URL = "/clearance/deleteClearanceOperator";
    //3. 车辆放行的申请人员或处理人员列表
    private static final String LIST_CLEARANCE_OPERATOR_URL = "/clearance/listClearanceOperator";
    //4. 创建车辆放行log
    private static final String CREATE_CLEARANCE_LOG_URL = "/clearance/createClearanceLog";
    //5. 搜索车辆放行log
    private static final String SEARCH_CLEARANCE_LOG_URL = "/clearance/searchClearanceLog";

    private Long organizationId = 1000001L;
    private Long communityId = 1L;
    private Long parkingLotId = 1L;


    //1. 新增车辆放行的申请人员或处理人员
    @Test
    public void testCreateClearanceOperator() {
        logon();
        CreateClearanceOperatorCommand cmd = new CreateClearanceOperatorCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setOperatorType("APPLICANT");
        cmd.setUserIds(Collections.singletonList(1001L));

        RestResponse response = httpClientService.restPost(CREATE_CLEARANCE_OPERATOR_URL, cmd, RestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhParkingClearanceOperatorsRecord operator = context().selectFrom(Tables.EH_PARKING_CLEARANCE_OPERATORS)
                .where(Tables.EH_PARKING_CLEARANCE_OPERATORS.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_PARKING_CLEARANCE_OPERATORS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_PARKING_CLEARANCE_OPERATORS.PARKING_LOT_ID.eq(parkingLotId))
                .fetchAny();

        assertNotNull("The created operator should be not null.", operator);
    }

    //2. 删除车辆放行的申请人员或处理人员
    @Test
    public void testDeleteClearanceOperator() {
        logon();
        DeleteClearanceOperatorCommand cmd = new DeleteClearanceOperatorCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setId(3L);

        RestResponseBase response = httpClientService.restPost(DELETE_CLEARANCE_OPERATOR_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertNull("The deleted operator should be null.", new EhParkingClearanceOperatorsDao(context().configuration()).findById(3L));
    }

    //3.1 车辆放行的申请人员或处理人员列表
    @Test
    public void testListClearanceOperator1() {
        logon();
        ListClearanceOperatorCommand cmd = new ListClearanceOperatorCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setOperatorType("APPLICANT");
        cmd.setPageSize(2);

        ListClearanceOperatorRestResponse response = httpClientService.restPost(LIST_CLEARANCE_OPERATOR_URL, cmd, ListClearanceOperatorRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        ListClearanceOperatorResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue("The parking clearance operator list size should be 2", myResponse.getOperators().size() == 2);
    }

    //3.2 车辆放行的申请人员或处理人员列表
    @Test
    public void testListClearanceOperator2() {
        logon();
        ListClearanceOperatorCommand cmd = new ListClearanceOperatorCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setOperatorType("APPLICANT");
        cmd.setPageSize(2);
        cmd.setPageAnchor(1480930012000L);

        ListClearanceOperatorRestResponse response = httpClientService.restPost(LIST_CLEARANCE_OPERATOR_URL, cmd, ListClearanceOperatorRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        ListClearanceOperatorResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue("The parking clearance operator list size should be 1", myResponse.getOperators().size() == 1);
    }

    //4. 创建车辆放行log
    @Test
    public void testCreateClearanceLog() {
        logon();
        CreateClearanceLogCommand cmd = new CreateClearanceLogCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setPlateNumber("粤B:33333");
        cmd.setClearanceTime(1L);
        cmd.setRemarks("remarks");

        CreateClearanceLogRestResponse response = httpClientService.restPost(CREATE_CLEARANCE_LOG_URL, cmd, CreateClearanceLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        Result<EhParkingClearanceLogsRecord> logs = context().selectFrom(Tables.EH_PARKING_CLEARANCE_LOGS)
                .where(Tables.EH_PARKING_CLEARANCE_LOGS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_PARKING_CLEARANCE_LOGS.PARKING_LOT_ID.eq(parkingLotId))
                .and(Tables.EH_PARKING_CLEARANCE_LOGS.PLATE_NUMBER.eq("粤B:33333"))
                .fetch();

        assertTrue("The created parking clearance log list size should be 1", logs.size() == 1);
    }

    //5.1 搜索车辆放行log
    @Test
    public void testSearchClearanceLog1() {
        logon();
        SearchClearanceLogCommand cmd = new SearchClearanceLogCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        // cmd.setIdentifierToken("1");
        cmd.setPlateNumber("333");
        cmd.setStatus((byte)4);
        cmd.setPageSize(10);

        SearchClearanceLogRestResponse response = httpClientService.restPost(SEARCH_CLEARANCE_LOG_URL, cmd, SearchClearanceLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchClearanceLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue("The search parking clearance log list size should be 4, actual is "+myResponse.getLogs().size(), myResponse.getLogs().size() == 4);
    }

    //5.2 搜索车辆放行log
    @Test
    public void testSearchClearanceLog2() {
        logon();
        SearchClearanceLogCommand cmd = new SearchClearanceLogCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setApplicant("左邻");
        cmd.setIdentifierToken("1");
        cmd.setPageSize(10);

        SearchClearanceLogRestResponse response = httpClientService.restPost(SEARCH_CLEARANCE_LOG_URL, cmd, SearchClearanceLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchClearanceLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue("The search parking clearance log list size should be 6, actual is "+myResponse.getLogs().size(), myResponse.getLogs().size() == 6);
    }

    //5.3 搜索车辆放行log
    @Test
    public void testSearchClearanceLog3() {
        logon();
        SearchClearanceLogCommand cmd = new SearchClearanceLogCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setPageSize(10);
        cmd.setPlateNumber("22");

        SearchClearanceLogRestResponse response = httpClientService.restPost(SEARCH_CLEARANCE_LOG_URL, cmd, SearchClearanceLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchClearanceLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue("The search parking clearance log list size should be 1, actual is "+myResponse.getLogs().size(), myResponse.getLogs().size() == 1);
    }

    //5.4 搜索车辆放行log
    @Test
    public void testSearchClearanceLog4() {
        logon();
        SearchClearanceLogCommand cmd = new SearchClearanceLogCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setParkingLotId(parkingLotId);
        cmd.setPageSize(2);

        SearchClearanceLogRestResponse response = httpClientService.restPost(SEARCH_CLEARANCE_LOG_URL, cmd, SearchClearanceLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchClearanceLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue("The search parking clearance log list size should be 2, actual is "+myResponse.getLogs().size(), myResponse.getLogs().size() == 2);
        assertTrue(myResponse.getLogs().get(0).getId() == 6);
        assertTrue(myResponse.getLogs().get(1).getId() == 5);
        assertTrue(myResponse.getNextPageAnchor() == 1480995716000L);
    }

    private void logon() {
        String userIdentifier = "12900000001";
        String plainTextPwd = "123456";
        Integer namespaceId = 0;
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    private DSLContext context(){
        return dbProvider.getDslContext();
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

        jsonFilePath = "data/json/parking-clearance-test-data-161205.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }
}
