package com.everhomes.test.junit.energy;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.energy.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFormulasDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulas;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterSettingLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.server.schema.tables.records.EhEnergyMeterFormulasRecord;
import com.everhomes.server.schema.tables.records.EhEnergyMeterSettingLogsRecord;
import com.everhomes.server.schema.tables.records.EhEnergyMetersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METERS;
import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_SETTING_LOGS;

/**
 * Created by xq.tian on 2016/10/31.
 */
public class EnergyConsumptionTest extends BaseLoginAuthTestCase{

    //1. 新建表记(水表,电表等)
    private static final String CREATE_ENERGY_METER_URL = "/energy/createEnergyMeter";
    //2. 修改表记(水表,电表等),表记分类,性质,账单项目不能修改
    private static final String UPDATE_ENERGY_METER_URL = "/energy/updateEnergyMeter";
    //3. 搜索表记
    private static final String SEARCH_ENERGY_METER_URL = "/energy/searchEnergyMeter";
    //4. 更改表记状态(删除,报废)
    private static final String UPDATE_ENERGY_METER_STATUS_URL = "/energy/updateEnergyMeterStatus";
    //5. 换表
    private static final String CHANGE_ENERGY_METER_URL = "/energy/changeEnergyMeter";
    //6. 批量修改表记属性
    private static final String BATCH_UPDATE_ENERGY_METER_SETTINGS_URL = "/energy/batchUpdateEnergyMeterSettings";
    //7. 搜索读表记录
    private static final String SEARCH_ENERGY_METER_READING_LOGS_URL = "/energy/searchEnergyMeterReadingLogs";
    //7. 搜索读表记录
    private static final String LIST_ENERGY_METER_READING_LOGS_URL = "/energy/listEnergyMeterReadingLogsByMeter";
    //8. 删除读表记录(只能删除当天的记录)
    private static final String DELETE_ENERGY_METER_READING_LOG_URL = "/energy/deleteEnergyMeterReadingLog";
    //9. 修改表记的默认属性值
    private static final String UPDATE_ENERGY_METER_DEFAULT_SETTING_URL = "/energy/updateEnergyMeterDefaultSetting";
    //10. 新建计算公式
    private static final String CREATE_ENERGY_METER_FORMULA_URL = "/energy/createEnergyMeterFormula";
    //11. 新建表记的分类(项目,性质)
    private static final String CREATE_ENERGY_METER_CATEGORY_URL = "/energy/createEnergyMeterCategory";
    //12. 修改表记的分类
    private static final String UPDATE_ENERGY_METER_CATEGORY_URL = "/energy/updateEnergyMeterCategory";
    //13. 删除表记的分类(项目,性质)
    private static final String DELETE_ENERGY_METER_CATEGORY_URL = "/energy/deleteEnergyMeterCategory";
    //14. 导入表记(Excel)
    private static final String IMPORT_ENERGY_METER_URL = "/energy/importEnergyMeter";
    //15. 表记分类列表
    private static final String LIST_ENERGY_METER_CATEGORIES_URL = "/energy/listEnergyMeterCategories";
    //16. 公式变量列表
    private static final String LIST_ENERGY_FORMULA_VARIABLES_URL = "/energy/listEnergyFormulaVariables";
    //17. 换表记录列表
    private static final String LIST_ENERGY_METER_CHANGE_LOGS_URL = "/energy/listEnergyMeterChangeLogs";
    //18. 获取计算公式的列表
    private static final String LIST_ENERGY_METER_FORMULAS_URL = "/energy/listEnergyMeterFormulas";
    //19. 获取默认设置
    private static final String GET_ENERGY_DEFAULT_SETTINGS_URL = "/energy/listEnergyDefaultSettings";
    //20. 每日水电总表
    private static final String GET_ENERGY_STAT_BY_DAY_URL = "/energy/getEnergyStatByDay";
    //21. 月度水电分析表
    private static final String GET_ENERGY_STAT_BY_MONTH_URL = "/energy/getEnergyStatByMonth";
    //22. 年度水电用量收支对比表
    private static final String GET_ENERGY_STATISTIC_BY_YEAR_URL = "/energy/getEnergyStatisticByYear";
    //23. 各项目月水电能耗情况（与去年同期相比)
    private static final String GET_ENERGY_STATISTIC_BY_YOY_URL = "/energy/getEnergyStatisticByYoy";
    //24. 抄表
    private static final String READ_ENERGY_METER_URL = "/energy/readEnergyMeter";
    //25. 获取表信息
    private static final String GET_ENERGY_METER_URL = "/energy/getEnergyMeter";
    //26. 删除公式
    private static final String DELETE_ENERGY_METER_FORMULA_URL = "/energy/deleteEnergyMeterFormula";
    //20. setting记录列表
    private static final String LIST_ENERGY_METER_SETTING_LOGS_URL = "/energy/listEnergyMeterSettingLogs";

    @Autowired
    private SearchProvider searchProvider;

    // @Value("elastic.index")
    // private String elasticsearchIndex;

    //1. 新建表记(水表,电表等)
    @Test
    public void testCreateEnergyMeter() {
        logon();
        CreateEnergyMeterRestResponse response = getMeter();
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).where(EH_ENERGY_METERS.NAME.eq("电表1"))
                .and(EH_ENERGY_METERS.METER_NUMBER.eq("123456"))
                .and(EH_ENERGY_METERS.METER_TYPE.eq(EnergyMeterType.ELECTRIC.getCode()))
                .and(EH_ENERGY_METERS.MAX_READING.eq(new BigDecimal("99999")))
                .and(EH_ENERGY_METERS.PRICE.eq(new BigDecimal("1.6")))
                .and(EH_ENERGY_METERS.RATE.eq(new BigDecimal("2")))
                .and(EH_ENERGY_METERS.START_READING.eq(new BigDecimal("100")))
                .fetchOne();

        assertNotNull("The created meter should be not null", meter);

        List<EhEnergyMeterSettingLogs> logs = context().selectFrom(Tables.EH_ENERGY_METER_SETTING_LOGS)
                .where(Tables.EH_ENERGY_METER_SETTING_LOGS.METER_ID.eq(meter.getId())).fetchInto(EhEnergyMeterSettingLogs.class);
        assertTrue(logs.size() == 4);
    }

    private CreateEnergyMeterRestResponse getMeter() {
        CreateEnergyMeterCommand cmd = new CreateEnergyMeterCommand();
        cmd.setOrganizationId(1L);
        cmd.setCommunityId(1L);
        cmd.setName("电表1");
        cmd.setMeterNumber("123456");
        cmd.setMeterType(EnergyMeterType.ELECTRIC.getCode());
        cmd.setBillCategoryId(1L);
        cmd.setServiceCategoryId(2L);
        cmd.setMaxReading(new BigDecimal("99999"));
        cmd.setStartReading(new BigDecimal("100"));
        cmd.setRate(new BigDecimal("2"));
        cmd.setPrice(new BigDecimal("1.6"));
        cmd.setCostFormulaId(1L);
        cmd.setAmountFormulaId(1L);
        return httpClientService.restPost(CREATE_ENERGY_METER_URL, cmd, CreateEnergyMeterRestResponse.class);
    }

    //2. 修改表记(水表,电表等),表记分类,性质,账单项目不能修改
    @Test
    public void testUpdateEnergyMeter() {
        logon();
        getMeter();
        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        UpdateEnergyMeterCommand cmd = new UpdateEnergyMeterCommand();
        cmd.setOrganizationId(1L);
        cmd.setMeterId(meter.getId());
        cmd.setName("修改电表");
        cmd.setMeterNumber("654321");
        cmd.setRate(new BigDecimal("3"));
        cmd.setPrice(new BigDecimal("3"));
        cmd.setCostFormulaId(1L);
        cmd.setAmountFormulaId(1L);
        cmd.setStartTime(DateHelper.currentGMTTime().getTime());
        cmd.setEndTime(Timestamp.valueOf(LocalDateTime.now().plusYears(1)).getTime());

        UpdateEnergyMeterRestResponse response = httpClientService.restPost(UPDATE_ENERGY_METER_URL, cmd, UpdateEnergyMeterRestResponse.class);
        assertNotNull(response);

        meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        assertEquals("修改电表", meter.getName());
        assertEquals("654321", meter.getMeterNumber());

        List<EhEnergyMeterSettingLogs> logs = context().selectFrom(Tables.EH_ENERGY_METER_SETTING_LOGS)
                .where(Tables.EH_ENERGY_METER_SETTING_LOGS.METER_ID.eq(meter.getId())).fetchInto(EhEnergyMeterSettingLogs.class);
        assertTrue(logs.size() == 8);
    }

    //3. 搜索表记
    @Test
    public void testSearchEnergyMeter() {
        testImportEnergyMeter();
        logon();
        SearchEnergyMeterCommand cmd = new SearchEnergyMeterCommand();
        cmd.setOrganizationId(1L);
        cmd.setCommunityId(1L);
        // cmd.setKeyword("");
        cmd.setBillCategoryId(4L);
        cmd.setServiceCategoryId(1L);
        cmd.setMeterType((byte)1);
        cmd.setStatus(2L);
        cmd.setPageAnchor(0L);
        cmd.setPageSize(10);

        SearchEnergyMeterRestResponse response = httpClientService.restPost(SEARCH_ENERGY_METER_URL, cmd, SearchEnergyMeterRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchEnergyMeterResponse myResponse = response.getResponse();
        assertNotNull(myResponse);
        assertNotNull(myResponse.getMeters());
        assertTrue(myResponse.getMeters().size() > 0);
    }

    //6. 更改表记状态(删除,报废)
    @Test
    public void testUpdateEnergyMeterStatus() {
        logon();
        getMeter();
        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        UpdateEnergyMeterStatusCommand cmd = new UpdateEnergyMeterStatusCommand();
        cmd.setOrganizationId(1L);
        cmd.setMeterId(meter.getId());
        cmd.setStatus(EnergyCommonStatus.INACTIVE.getCode());

        RestResponseBase response = httpClientService.restPost(UPDATE_ENERGY_METER_STATUS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        EhEnergyMetersRecord record = context.selectFrom(Tables.EH_ENERGY_METERS).where(Tables.EH_ENERGY_METERS.ID.eq(meter.getId())).fetchOne();

        assertEquals("The status should be 1", EnergyCommonStatus.INACTIVE.getCode(), record.getStatus());
    }

    //6. 批量修改表记属性
    @Test
    public void testBatchUpdateEnergyMeterSettings() {
        logon();
        testImportEnergyMeter();
        List<EhEnergyMeters> metersList = context().selectFrom(EH_ENERGY_METERS).fetchInto(EhEnergyMeters.class);
        assertNotNull(metersList);
        assertTrue(metersList.size() > 2);

        BatchUpdateEnergyMeterSettingsCommand cmd = new BatchUpdateEnergyMeterSettingsCommand();
        cmd.setOrganizationId(1L);
        List<Long> longList = new ArrayList<>();
        longList.add(metersList.get(0).getId());
        longList.add(metersList.get(1).getId());
        cmd.setMeterIds(longList);
        cmd.setPrice(new BigDecimal("22"));
        cmd.setRate(new BigDecimal("33"));
        cmd.setPriceStart(DateHelper.currentGMTTime().getTime());
        cmd.setRateStart(DateHelper.currentGMTTime().getTime());

        RestResponseBase response = httpClientService.restPost(BATCH_UPDATE_ENERGY_METER_SETTINGS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterSettingLogsRecord result = context().selectFrom(EH_ENERGY_METER_SETTING_LOGS)
                .where(EH_ENERGY_METER_SETTING_LOGS.METER_ID.eq(metersList.get(0).getId()))
                .and(EH_ENERGY_METER_SETTING_LOGS.SETTING_VALUE.eq(new BigDecimal("22")))
                .and(EH_ENERGY_METER_SETTING_LOGS.SETTING_TYPE.eq(EnergyMeterSettingType.PRICE.getCode()))
                .fetchAny();

        assertNotNull("The new price setting log should be not null.", result);

        result = context().selectFrom(EH_ENERGY_METER_SETTING_LOGS)
                .where(EH_ENERGY_METER_SETTING_LOGS.METER_ID.eq(metersList.get(0).getId()))
                .and(EH_ENERGY_METER_SETTING_LOGS.SETTING_VALUE.eq(new BigDecimal("33")))
                .and(EH_ENERGY_METER_SETTING_LOGS.SETTING_TYPE.eq(EnergyMeterSettingType.RATE.getCode()))
                .fetchAny();

        assertNotNull("The new rate setting log should be not null.", result);
    }

    //7. 搜索读表记录
    @Test
    public void testSearchEnergyMeterReadingLogs() {
        logon();
        bulkReadingLogData();
        SearchEnergyMeterReadingLogsCommand cmd = new SearchEnergyMeterReadingLogsCommand();
        cmd.setOrganizationId(1L);
        cmd.setCommunityId(1L);
        // cmd.setKeyword("");
        // cmd.setMeterId(1L);
        // cmd.setBillCategoryId(1L);
        // cmd.setServiceCategoryId(1L);
        // cmd.setMeterType((byte)1);
        cmd.setStartTime(1478069824100L);
        cmd.setEndTime(1478069824300L);
        cmd.setPageAnchor(0L);
        cmd.setPageSize(10);

        SearchEnergyMeterReadingLogsRestResponse response = httpClientService.restPost(SEARCH_ENERGY_METER_READING_LOGS_URL, cmd, SearchEnergyMeterReadingLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchEnergyMeterReadingLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue(myResponse.getLogs().size() == 3);
    }

    //8. 搜索读表记录
    @Test
    public void testSearchEnergyMeterReadingLogs1() {
        logon();
        bulkReadingLogData();
        SearchEnergyMeterReadingLogsCommand cmd = new SearchEnergyMeterReadingLogsCommand();
        cmd.setOrganizationId(1L);
        cmd.setCommunityId(1L);
        cmd.setKeyword("小黑");
        // cmd.setMeterId(1L);
        // cmd.setBillCategoryId(1L);
        // cmd.setServiceCategoryId(1L);
        // cmd.setMeterType((byte)1);
        cmd.setStartTime(1478069824100L);
        cmd.setEndTime(1478069824300L);
        cmd.setPageAnchor(0L);
        cmd.setPageSize(10);

        SearchEnergyMeterReadingLogsRestResponse response = httpClientService.restPost(SEARCH_ENERGY_METER_READING_LOGS_URL, cmd, SearchEnergyMeterReadingLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchEnergyMeterReadingLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue(myResponse.getLogs().size() == 1);
    }

    //9. 根据id获取读表记录
    @Test
    public void testSearchEnergyMeterReadingLogs2() {
        logon();
        bulkReadingLogData();
        SearchEnergyMeterReadingLogsCommand cmd = new SearchEnergyMeterReadingLogsCommand();
        cmd.setOrganizationId(1L);
        cmd.setCommunityId(1L);
        cmd.setMeterId(2L);
        cmd.setPageAnchor(0L);
        cmd.setPageSize(10);

        SearchEnergyMeterReadingLogsRestResponse response = httpClientService.restPost(SEARCH_ENERGY_METER_READING_LOGS_URL, cmd, SearchEnergyMeterReadingLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchEnergyMeterReadingLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue(myResponse.getLogs().size() == 2);
    }

    //10. 删除读表记录(只能删除当天的记录)
    @Test
    public void testDeleteEnergyMeterReadingLog() {
        logon();
        DeleteEnergyMeterReadingLogCommand cmd = new DeleteEnergyMeterReadingLogCommand();
        cmd.setOrganizationId(1L);
        cmd.setLogId(1L);

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_READING_LOG_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        // assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //11. 删除公式
    @Test
    public void testDeleteEnergyMeterFormula() {
        logon();
        DeleteEnergyMeterFormulaCommand cmd = new DeleteEnergyMeterFormulaCommand();
        cmd.setOrganizationId(1L);
        cmd.setFormulaId(1L);

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_FORMULA_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterFormulasDao dao = new EhEnergyMeterFormulasDao(dbProvider.getDslContext().configuration());
        EhEnergyMeterFormulas formula = dao.findById(1L);
        assertEquals("The deleted formula status should be inactive", formula.getStatus(), EnergyCommonStatus.INACTIVE.getCode());
    }

    //12. 删除公式失败
    @Test
    public void testDeleteEnergyMeterFormula1() {
        logon();
        DeleteEnergyMeterFormulaCommand cmd = new DeleteEnergyMeterFormulaCommand();
        cmd.setOrganizationId(1L);
        cmd.setFormulaId(2L);

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_FORMULA_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        // assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterFormulasDao dao = new EhEnergyMeterFormulasDao(dbProvider.getDslContext().configuration());
        EhEnergyMeterFormulas formula = dao.findById(2L);
        assertEquals("The deleted formula status should be inactive", formula.getStatus(), EnergyCommonStatus.ACTIVE.getCode());
    }

    //13. 新建计算公式
    @Test
    public void testCreateEnergyMeterFormula() {
        logon();
        CreateEnergyMeterFormulaCommand cmd = new CreateEnergyMeterFormulaCommand();
        cmd.setOrganizationId(1L);
        cmd.setName("新建的公式1");
        cmd.setExpression("[[单价]]*[[倍率]]*[[读表用量差]]+(2*[[单价]])");
        cmd.setFormulaType((byte)1);

        CreateEnergyMeterFormulaRestResponse response = httpClientService.restPost(CREATE_ENERGY_METER_FORMULA_URL, cmd, CreateEnergyMeterFormulaRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterFormulasRecord newFormula = context().selectFrom(Tables.EH_ENERGY_METER_FORMULAS)
                .where(Tables.EH_ENERGY_METER_FORMULAS.NAME.eq("新建的公式1"))
                .and(Tables.EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq((byte) 1)).fetchAny();

        assertNotNull("The created formula should be not null.", newFormula);
    }

    //14. 新建计算公式, 格式错误
    @Test
    public void testCreateEnergyMeterFormula1() {
        logon();
        CreateEnergyMeterFormulaCommand cmd = new CreateEnergyMeterFormulaCommand();
        cmd.setOrganizationId(1L);
        cmd.setName("新建的公式1");
        cmd.setExpression("[[单价]]*[[倍率]]*[[读表用量差]]+(2*[[单价]])1");// 公式结构错误,无法计算
        cmd.setFormulaType((byte)1);

        CreateEnergyMeterFormulaRestResponse response = httpClientService.restPost(CREATE_ENERGY_METER_FORMULA_URL, cmd, CreateEnergyMeterFormulaRestResponse.class);
        assertNotNull(response);

        EhEnergyMeterFormulasRecord newFormula = context().selectFrom(Tables.EH_ENERGY_METER_FORMULAS)
                .where(Tables.EH_ENERGY_METER_FORMULAS.NAME.eq("新建的公式1"))
                .and(Tables.EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq((byte) 1)).fetchAny();

        assertNull("The created formula should be null.", newFormula);
    }

    //15. setting记录列表
    @Test
    public void testListEnergyMeterSettingLogs() {
        logon();
        ListEnergyMeterSettingLogsCommand cmd = new ListEnergyMeterSettingLogsCommand();
        cmd.setOrganizationId(1L);
        cmd.setMeterId(1L);
        cmd.setSettingType((byte)1);

        ListEnergyMeterSettingLogsRestResponse response = httpClientService.restPost(LIST_ENERGY_METER_SETTING_LOGS_URL, cmd, ListEnergyMeterSettingLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertTrue(response.getResponse().size() == 1);
    }

    private void bulkReadingLogData() {
        searchProvider.clearType("energyMeterReadingLog");
        searchProvider.bulk("energyMeterReadingLog", "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"1\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"2\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":2,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"3\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"4\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"5\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":2,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"6\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"7\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"8\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }");
    }


    //14. 导入表记(Excel)
    @Test
    public void testImportEnergyMeter() {
        try {
            logon();
            ImportEnergyMeterCommand cmd = new ImportEnergyMeterCommand();
            cmd.setOrganizationId(1L);
            cmd.setCommunityId(1L);
            String filePath = null;
                filePath = new File("").getCanonicalPath()+"\\src\\test\\data\\excel\\energy_meter_template.xlsx";
            RestResponseBase response = httpClientService.postFile(IMPORT_ENERGY_METER_URL, cmd, new File(filePath), RestResponseBase.class);
            assertNotNull(response);
            assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

            Result<EhEnergyMetersRecord> result = context().selectFrom(EH_ENERGY_METERS).fetch();
            assertTrue(result.size() == 3);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    //15. get获取表记
    @Test
    public void testGetEnergyMeter() {
        logon();
        testUpdateEnergyMeter();
        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        GetEnergyMeterCommand cmd = new GetEnergyMeterCommand();
        cmd.setOrganizationId(1L);
        cmd.setMeterId(meter.getId());
        GetEnergyMeterRestResponse response = httpClientService.restPost(GET_ENERGY_METER_URL, cmd, GetEnergyMeterRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EnergyMeterDTO dto = response.getResponse();

        assertNotNull("The response should be not null.", meter);

        assertEquals(dto.getRate(), new BigDecimal("3.00"));
        assertEquals(dto.getPrice(), new BigDecimal("3.00"));

        // Result<EhEnergyMetersRecord> result = context().selectFrom(EH_ENERGY_METERS).fetch();
        // assertTrue(result.size() == 3);
    }

    private DSLContext context() {
        return dbProvider.getDslContext();
    }

    private void logon(){
        String userIdentifier = "12900000001";
        String plainTextPwd = "123456";
        Integer namespaceId = 0;
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

        jsonFilePath = "data/json/energy-test-data-161031.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }
}
