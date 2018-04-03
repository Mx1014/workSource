package com.everhomes.test.junit.energy;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.energy.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFormulasDao;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterReadingLogsDao;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterSettingLogsDao;
import com.everhomes.server.schema.tables.daos.EhEnergyMetersDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulas;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterSettingLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
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
    //20. 抄表
    private static final String READ_ENERGY_METER_URL = "/energy/readEnergyMeter";
    //21. 获取表信息
    private static final String GET_ENERGY_METER_URL = "/energy/getEnergyMeter";
    //22. 删除公式
    private static final String DELETE_ENERGY_METER_FORMULA_URL = "/energy/deleteEnergyMeterFormula";
    //23. setting记录列表
    private static final String LIST_ENERGY_METER_SETTING_LOGS_URL = "/energy/listEnergyMeterSettingLogs";

    //24. 每日水电总表
    private static final String GET_ENERGY_STAT_BY_DAY_URL = "/energy/getEnergyStatByDay";
    //25. 月度水电分析表
    private static final String GET_ENERGY_STAT_BY_MONTH_URL = "/energy/getEnergyStatByMonth";
    //26. 年度水电用量收支对比表
    private static final String GET_ENERGY_STATISTIC_BY_YEAR_URL = "/energy/getEnergyStatisticByYear";
    //27. 各项目月水电能耗情况（与去年同期相比)
    private static final String GET_ENERGY_STATISTIC_BY_YOY_URL = "/energy/getEnergyStatisticByYoy";

    @Autowired
    private SearchProvider searchProvider;

    // @Value("elastic.index")
    // private String elasticsearchIndex;

    private CreateEnergyMeterRestResponse getMeter() {
        CreateEnergyMeterCommand cmd = new CreateEnergyMeterCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
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

    //2. 修改表记(水表,电表等),表记分类,性质,账单项目不能修改
    @Test
    public void testUpdateEnergyMeter() {
        logon();
        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        UpdateEnergyMeterCommand cmd = new UpdateEnergyMeterCommand();
        cmd.setOrganizationId(1000001L);
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
        assertTrue(logs.size() == 5);
    }

    //3. 搜索表记
    @Test
    public void testSearchEnergyMeter() {
        testImportEnergyMeter();
        logon();
        SearchEnergyMeterCommand cmd = new SearchEnergyMeterCommand();
        cmd.setOrganizationId(1000001L);
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

    //4. 更改表记状态(删除,报废)
    @Test
    public void testUpdateEnergyMeterStatus() {
        logon();
        getMeter();
        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        UpdateEnergyMeterStatusCommand cmd = new UpdateEnergyMeterStatusCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setMeterId(meter.getId());
        cmd.setStatus(EnergyCommonStatus.INACTIVE.getCode());

        RestResponseBase response = httpClientService.restPost(UPDATE_ENERGY_METER_STATUS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        EhEnergyMetersRecord record = context.selectFrom(Tables.EH_ENERGY_METERS).where(Tables.EH_ENERGY_METERS.ID.eq(meter.getId())).fetchOne();

        assertNotSame("The meter status should be not equal", EnergyCommonStatus.INACTIVE.getCode(), meter.getStatus());
        assertEquals("The status should be 1", EnergyCommonStatus.INACTIVE.getCode(), record.getStatus());
    }

    //5. 换表
    @Test
    public void testChangeEnergyMeter() {
        logon();
        ChangeEnergyMeterCommand cmd = new ChangeEnergyMeterCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(1L);
        cmd.setOldReading(new BigDecimal("8888"));
        cmd.setNewReading(new BigDecimal("100"));
        cmd.setMaxReading(new BigDecimal("10000"));
        cmd.setMeterId(1L);

        RestResponseBase response = httpClientService.restPost(CHANGE_ENERGY_METER_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        DSLContext context = dbProvider.getDslContext();
        EhEnergyMeterChangeLogsRecord record = context.selectFrom(Tables.EH_ENERGY_METER_CHANGE_LOGS)
                .where(Tables.EH_ENERGY_METER_CHANGE_LOGS.METER_ID.eq(1L))
                .and(Tables.EH_ENERGY_METER_CHANGE_LOGS.OLD_READING.eq(new BigDecimal("8888")))
                .and(Tables.EH_ENERGY_METER_CHANGE_LOGS.NEW_READING.eq(new BigDecimal("100")))
                .and(Tables.EH_ENERGY_METER_CHANGE_LOGS.MAX_READING.eq(new BigDecimal("10000")))
                .fetchAny();

        assertNotNull("The energy meter change log should be not null.", record);

        EhEnergyMetersRecord meter = context.selectFrom(Tables.EH_ENERGY_METERS).where(Tables.EH_ENERGY_METERS.ID.eq(1L)).fetchOne();
        assertEquals(meter.getMaxReading().doubleValue(), new BigDecimal("10000").doubleValue());

        EhEnergyMeterReadingLogsRecord readingLog = context().selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(1L))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.READING.eq(new BigDecimal("100")))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.CHANGE_METER_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()))
                .fetchAny();

        assertNotNull("The change meter flag reading log should not null.", readingLog);
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
        cmd.setOrganizationId(1000001L);
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

    //7.1 搜索读表记录
    @Test
    public void testSearchEnergyMeterReadingLogs() {
        logon();
        bulkReadingLogData();
        SearchEnergyMeterReadingLogsCommand cmd = new SearchEnergyMeterReadingLogsCommand();
        cmd.setOrganizationId(1000001L);
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

    //7.2. 搜索读表记录
    @Test
    public void testSearchEnergyMeterReadingLogs1() {
        logon();
        bulkReadingLogData();
        SearchEnergyMeterReadingLogsCommand cmd = new SearchEnergyMeterReadingLogsCommand();
        cmd.setOrganizationId(1000001L);
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

    //7.3. 根据id获取读表记录
    @Test
    public void testSearchEnergyMeterReadingLogs2() {
        logon();
        bulkReadingLogData();
        SearchEnergyMeterReadingLogsCommand cmd = new SearchEnergyMeterReadingLogsCommand();
        cmd.setOrganizationId(1000001L);
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


    //8. 删除读表记录(只能删除当天的记录)
    @Test
    public void testDeleteEnergyMeterReadingLog() {
        logon();
        DeleteEnergyMeterReadingLogCommand cmd = new DeleteEnergyMeterReadingLogCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setLogId(1L);

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_READING_LOG_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertEquals(response.getErrorCode()+"", "10006");
    }

    //9. 修改表记的默认属性值
    @Test
    public void testUpdateEnergyMeterDefaultSetting() {
        logon();
        UpdateEnergyMeterDefaultSettingCommand cmd = new UpdateEnergyMeterDefaultSettingCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setSettingId(1L);
        cmd.setSettingValue(new BigDecimal("111"));
        cmd.setFormulaId(1L);

        UpdateEnergyMeterDefaultSettingRestResponse response = httpClientService.restPost(UPDATE_ENERGY_METER_DEFAULT_SETTING_URL, cmd, UpdateEnergyMeterDefaultSettingRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterDefaultSettingsRecord setting = context().selectFrom(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS).where(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.ID.eq(1L)).fetchOne();
        assertEquals(setting.getSettingValue().doubleValue(), new BigDecimal("111").doubleValue());
    }

    //10.1 新建计算公式
    @Test
    public void testCreateEnergyMeterFormula() {
        logon();
        CreateEnergyMeterFormulaCommand cmd = new CreateEnergyMeterFormulaCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        String name = "新建的公式1";
        cmd.setName(name);
        cmd.setExpression("[[单价]]*[[倍率]]*[[读表用量差]]+(2*[[单价]])");
        cmd.setFormulaType((byte)1);

        CreateEnergyMeterFormulaRestResponse response = httpClientService.restPost(CREATE_ENERGY_METER_FORMULA_URL, cmd, CreateEnergyMeterFormulaRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterFormulasRecord newFormula = context().selectFrom(Tables.EH_ENERGY_METER_FORMULAS)
                .where(Tables.EH_ENERGY_METER_FORMULAS.NAME.eq(name))
                .and(Tables.EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq((byte) 1)).fetchAny();

        assertNotNull("The created formula should be not null.", newFormula);
    }

    //10.2. 新建计算公式, 格式错误
    @Test
    public void testCreateEnergyMeterFormula1() {
        logon();
        CreateEnergyMeterFormulaCommand cmd = new CreateEnergyMeterFormulaCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        String name = "新建的公式1";
        cmd.setName(name);
        cmd.setExpression("[[单价]]*[[倍率]]*[[读表用量差]]+(2*[[单价]])1");// 公式结构错误,无法计算
        cmd.setFormulaType((byte)1);

        CreateEnergyMeterFormulaRestResponse response = httpClientService.restPost(CREATE_ENERGY_METER_FORMULA_URL, cmd, CreateEnergyMeterFormulaRestResponse.class);
        assertNotNull(response);

        EhEnergyMeterFormulasRecord newFormula = context().selectFrom(Tables.EH_ENERGY_METER_FORMULAS)
                .where(Tables.EH_ENERGY_METER_FORMULAS.NAME.eq(name))
                .and(Tables.EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq((byte) 1)).fetchAny();

        assertNull("The created formula should be null.", newFormula);
    }

    //11. 新建表记的分类(项目,性质)
    @Test
    public void testCreateEnergyMeterCategory() {
        logon();
        CreateEnergyMeterCategoryCommand cmd = new CreateEnergyMeterCategoryCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        String name = "自用部分";
        cmd.setName(name);
        cmd.setCategoryType(EnergyCategoryType.BILL.getCode());

        CreateEnergyMeterCategoryRestResponse response = httpClientService.restPost(CREATE_ENERGY_METER_CATEGORY_URL, cmd, CreateEnergyMeterCategoryRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertEquals(response.getResponse().getName(), name);

        EhEnergyMeterCategoriesRecord createdCategory = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES)
                .where(Tables.EH_ENERGY_METER_CATEGORIES.NAME.eq(name))
                .and(Tables.EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(EnergyCategoryType.BILL.getCode()))
                .fetchAny();

        assertNotNull(createdCategory);
    }

    //12. 修改表记的分类
    @Test
    public void testUpdateEnergyMeterCategory() {
        logon();
        EhEnergyMeterCategoriesRecord category = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES).fetchAny();
        UpdateEnergyMeterCategoryCommand cmd = new UpdateEnergyMeterCategoryCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCategoryId(category.getId());
        String updateName = category.getName() + ":updated";
        cmd.setName(updateName);

        UpdateEnergyMeterCategoryRestResponse response = httpClientService.restPost(UPDATE_ENERGY_METER_CATEGORY_URL, cmd, UpdateEnergyMeterCategoryRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterCategoriesRecord updatedCty = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES)
                .where(Tables.EH_ENERGY_METER_CATEGORIES.ID.eq(category.getId()))
                .fetchOne();

        assertNotNull("The updated energy meter category should be not null", updatedCty);
        assertEquals("The updated energy meter name should be " + updateName, updatedCty.getName(), updateName);
    }

    //13. 删除表记的分类(项目,性质)
    @Test
    public void testDeleteEnergyMeterCategory() {
        logon();
        EhEnergyMeterCategoriesRecord category = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES).fetchAny();
        DeleteEnergyMeterCategoryCommand cmd = new DeleteEnergyMeterCategoryCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCategoryId(category.getId());

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_CATEGORY_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterCategoriesRecord deletedCty = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES)
                .where(Tables.EH_ENERGY_METER_CATEGORIES.ID.eq(category.getId()))
                .fetchOne();

        assertNull(deletedCty);
    }

    //14. 导入表记(Excel)
    @Test
    public void testImportEnergyMeter() {
        try {
            logon();
            ImportEnergyMeterCommand cmd = new ImportEnergyMeterCommand();
            cmd.setOwnerId(1000001L);
            cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            cmd.setCommunityId(1L);
            String filePath;
            filePath = new File("").getCanonicalPath()+"\\src\\test\\data\\excel\\energy_meter_template.xlsx";
            RestResponseBase response = httpClientService.postFile(IMPORT_ENERGY_METER_URL, cmd, new File(filePath), RestResponseBase.class);
            assertNotNull(response);
            assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

            Result<EhEnergyMetersRecord> result = context().selectFrom(EH_ENERGY_METERS).fetch();
            assertTrue(result.size() == 4);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }

    //15.1. 表记分类列表(bill)
    @Test
    public void testListEnergyMeterCategories() {
        logon();
        ListEnergyMeterCategoriesCommand cmd = new ListEnergyMeterCategoriesCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setCategoryType(EnergyCategoryType.BILL.getCode());

        ListEnergyMeterCategoriesRestResponse response = httpClientService.restPost(LIST_ENERGY_METER_CATEGORIES_URL, cmd, ListEnergyMeterCategoriesRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue("The energy meter bill category list sie should be 3", response.getResponse().size() == 3);
    }

    //15.2. 表记分类列表(service)
    @Test
    public void testListEnergyMeterCategories1() {
        logon();
        ListEnergyMeterCategoriesCommand cmd = new ListEnergyMeterCategoriesCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setCategoryType(EnergyCategoryType.SERVICE.getCode());

        ListEnergyMeterCategoriesRestResponse response = httpClientService.restPost(LIST_ENERGY_METER_CATEGORIES_URL, cmd, ListEnergyMeterCategoriesRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue("The energy meter service category list sie should be 3", response.getResponse().size() == 3);
    }

    //16. 公式变量列表
    @Test
    public void testListEnergyFormulaVariables() {
        logon();
        ListEnergyFormulaVariablesRestResponse response = httpClientService.restPost(LIST_ENERGY_FORMULA_VARIABLES_URL, null, ListEnergyFormulaVariablesRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue(response.getResponse().size() == 3);
    }

    //17. 换表记录列表
    @Test
    public void testListEnergyMeterChangeLogs() {
        logon();
        testChangeEnergyMeter();
        ListEnergyMeterChangeLogsCommand cmd = new ListEnergyMeterChangeLogsCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setMeterId(1L);

        ListEnergyMeterChangeLogsRestResponse response = httpClientService.restPost(LIST_ENERGY_METER_CHANGE_LOGS_URL, cmd, ListEnergyMeterChangeLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertTrue(response.getResponse().size() == 1);
        assertEquals(response.getResponse().get(0).getMaxReading().doubleValue(), new BigDecimal("10000").doubleValue());
    }

    //18. 获取计算公式的列表
    @Test
    public void testListEnergyMeterFormulas() {
        logon();
        ListEnergyMeterFormulasCommand cmd = new ListEnergyMeterFormulasCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setFormulaType(EnergyFormulaType.AMOUNT.getCode());

        ListEnergyMeterFormulasRestResponse response = httpClientService.restPost(LIST_ENERGY_METER_FORMULAS_URL, cmd, ListEnergyMeterFormulasRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue(response.getResponse().size() == 1);
    }

    //19.1. 获取默认设置
    @Test
    public void testListEnergyDefaultSettings() {
        logon();
        ListEnergyDefaultSettingsCommand cmd = new ListEnergyDefaultSettingsCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setMeterType(EnergyMeterType.WATER.getCode());

        ListEnergyDefaultSettingsRestResponse response = httpClientService.restPost(GET_ENERGY_DEFAULT_SETTINGS_URL, cmd, ListEnergyDefaultSettingsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertTrue(response.getResponse().size() == 4);
    }

    //19.2. 获取默认设置(水表和电表的默认设置)
    @Test
    public void testListEnergyDefaultSettings1() {
        logon();
        ListEnergyDefaultSettingsCommand cmd = new ListEnergyDefaultSettingsCommand();
        cmd.setOwnerId(1000001L);
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        // cmd.setMeterType((byte)1);

        ListEnergyDefaultSettingsRestResponse response = httpClientService.restPost(GET_ENERGY_DEFAULT_SETTINGS_URL, cmd, ListEnergyDefaultSettingsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        System.err.println(response.getResponse());
        assertTrue(response.getResponse().size() == 5);
    }

    //20.1 抄表(复始计量)
    @Test
    public void testReadEnergyMeter1() {
        logon();
        ReadEnergyMeterCommand cmd = new ReadEnergyMeterCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(1L);
        cmd.setMeterId(1L);
        cmd.setResetMeterFlag(TrueOrFalseFlag.TRUE.getCode());
        cmd.setLastReading(new BigDecimal("234"));
        BigDecimal currReading = new BigDecimal("1234");
        cmd.setCurrReading(currReading);

        ReadEnergyMeterRestResponse response = httpClientService.restPost(READ_ENERGY_METER_URL, cmd, ReadEnergyMeterRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterReadingLogsRecord readingLogs = context().selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(1L))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.READING.eq(currReading))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.RESET_METER_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()))
                .fetchOne();

        assertNotNull("The reading logs should be exist.", readingLogs);

        EhEnergyMetersRecord meter = context().selectFrom(Tables.EH_ENERGY_METERS)
                .where(Tables.EH_ENERGY_METERS.ID.eq(1L)).fetchOne();

        assertEquals("The meter property lastReading should be updated.", meter.getLastReading().doubleValue(), currReading.doubleValue());
    }

    //20.2 抄表
    @Test
    public void testReadEnergyMeter() {
        logon();
        ReadEnergyMeterCommand cmd = new ReadEnergyMeterCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(1L);
        cmd.setMeterId(1L);
        cmd.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
        cmd.setLastReading(new BigDecimal("234"));
        BigDecimal currReading = new BigDecimal("1234");
        cmd.setCurrReading(currReading);

        ReadEnergyMeterRestResponse response = httpClientService.restPost(READ_ENERGY_METER_URL, cmd, ReadEnergyMeterRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterReadingLogsRecord readingLogs = context().selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(1L))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.READING.eq(currReading))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.RESET_METER_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()))
                .fetchOne();

        assertNotNull("The reading logs should be exist.", readingLogs);

        EhEnergyMetersRecord meter = context().selectFrom(Tables.EH_ENERGY_METERS)
                .where(Tables.EH_ENERGY_METERS.ID.eq(1L)).fetchOne();

        assertEquals("The meter property lastReading should be updated.", meter.getLastReading().doubleValue(), currReading.doubleValue());
    }

    //21. get获取表记
    @Test
    public void testGetEnergyMeter() {
        logon();
        testUpdateEnergyMeter();
        EhEnergyMetersRecord meter = context().selectFrom(EH_ENERGY_METERS).fetchAny();
        GetEnergyMeterCommand cmd = new GetEnergyMeterCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setMeterId(meter.getId());
        GetEnergyMeterRestResponse response = httpClientService.restPost(GET_ENERGY_METER_URL, cmd, GetEnergyMeterRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EnergyMeterDTO dto = response.getResponse();

        assertNotNull("The response should be not null.", meter);

        assertEquals(dto.getRate(), new BigDecimal("3.00"));
        assertEquals(dto.getPrice(), new BigDecimal("3.00"));
    }

    //22.1. 删除公式
    @Test
    public void testDeleteEnergyMeterFormula() {
        logon();
        DeleteEnergyMeterFormulaCommand cmd = new DeleteEnergyMeterFormulaCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setFormulaId(1L);

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_FORMULA_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhEnergyMeterFormulasDao dao = new EhEnergyMeterFormulasDao(dbProvider.getDslContext().configuration());
        EhEnergyMeterFormulas formula = dao.findById(1L);
        assertNull(formula);
    }


    //22.2. 删除公式失败
    @Test
    public void testDeleteEnergyMeterFormula1() {
        logon();
        DeleteEnergyMeterFormulaCommand cmd = new DeleteEnergyMeterFormulaCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setFormulaId(2L);

        RestResponseBase response = httpClientService.restPost(DELETE_ENERGY_METER_FORMULA_URL, cmd, RestResponseBase.class);
        assertNotNull(response);

        EhEnergyMeterFormulasDao dao = new EhEnergyMeterFormulasDao(dbProvider.getDslContext().configuration());
        EhEnergyMeterFormulas formula = dao.findById(2L);
        assertEquals("The deleted formula status should be inactive", formula.getStatus(), EnergyCommonStatus.ACTIVE.getCode());
    }

    //23. setting记录列表
    @Test
    public void testListEnergyMeterSettingLogs() {
        logon();
        ListEnergyMeterSettingLogsCommand cmd = new ListEnergyMeterSettingLogsCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setMeterId(1L);
        cmd.setSettingType((byte)1);

        ListEnergyMeterSettingLogsRestResponse response = httpClientService.restPost(LIST_ENERGY_METER_SETTING_LOGS_URL, cmd, ListEnergyMeterSettingLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertTrue(response.getResponse().size() == 1);
    }

    //24. 生成数据
    // @Test
    public void initStatData() {
        logon();
        List<EhEnergyMeters> meters = initEnergyMeter();
        initEnergyMeterReadingLogs(meters);
        initEnergyMeterChangeLogs(meters);
        initEnergyMeterSettingLogs(meters);
    }

    private void initEnergyMeterSettingLogs(List<EhEnergyMeters> meters) {
        Long id = context().select(DSL.max(Tables.EH_ENERGY_METER_SETTING_LOGS.ID)).from(Tables.EH_ENERGY_METER_SETTING_LOGS).fetchOneInto(Long.class);
        Result<EhEnergyMeterFormulasRecord> amountFormula = context().selectFrom(Tables.EH_ENERGY_METER_FORMULAS)
                .where(Tables.EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq(EnergyFormulaType.AMOUNT.getCode())).fetch();
        Result<EhEnergyMeterFormulasRecord> costFormula = context().selectFrom(Tables.EH_ENERGY_METER_FORMULAS)
                .where(Tables.EH_ENERGY_METER_FORMULAS.FORMULA_TYPE.eq(EnergyFormulaType.COST.getCode())).fetch();
        for (EhEnergyMeters meter : meters) {
            for (EnergyMeterSettingType settingType : EnergyMeterSettingType.values()) {
                for (int i = 0; i < 10; i++) {
                    EhEnergyMeterSettingLogs log = new EhEnergyMeterSettingLogs();
                    log.setId(++id);
                    log.setMeterId(meter.getId());
                    log.setSettingType(settingType.getCode());
                    log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                    int endTime = RandomGenerator.getRandomNumberBetween(1, 31);
                    log.setEndTime(Timestamp.valueOf(LocalDateTime.of(2016, 10, 1, 0, 0).plusDays(-1).plusDays(i).plusDays(endTime)));
                    log.setStartTime(Timestamp.valueOf(LocalDateTime.of(2016, 10, 1, 0, 0).plusDays(-1).plusDays(RandomGenerator.getRandomNumberBetween(1, endTime))));
                    if (settingType == EnergyMeterSettingType.AMOUNT_FORMULA) {
                        int idx = RandomGenerator.getRandomNumberBetween(0, amountFormula.size()-1);
                        log.setFormulaId(amountFormula.get(idx).getId());
                    } else if (settingType == EnergyMeterSettingType.COST_FORMULA) {
                        int idx = RandomGenerator.getRandomNumberBetween(0, costFormula.size()-1);
                        log.setFormulaId(costFormula.get(idx).getId());
                    } else if (settingType == EnergyMeterSettingType.PRICE) {
                        log.setSettingValue(new BigDecimal(RandomGenerator.getRandomNumberBetween(1, 4)));
                    } else {
                        log.setSettingValue(new BigDecimal(RandomGenerator.getRandomNumberBetween(2, 4)));
                    }
                    EhEnergyMeterSettingLogsDao dao = new EhEnergyMeterSettingLogsDao(context().configuration());
                    dao.insert(log);
                }
                for (int i = 0; i < 10; i++) {
                    EhEnergyMeterSettingLogs log = new EhEnergyMeterSettingLogs();
                    log.setId(++id);
                    log.setMeterId(meter.getId());
                    log.setSettingType(settingType.getCode());
                    log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                    int endTime = RandomGenerator.getRandomNumberBetween(1, 31);
                    log.setEndTime(Timestamp.valueOf(LocalDateTime.of(2015, 10, 1, 0, 0).plusDays(-1).plusDays(i).plusDays(endTime)));
                    log.setStartTime(Timestamp.valueOf(LocalDateTime.of(2015, 10, 1, 0, 0).plusDays(-1).plusDays(RandomGenerator.getRandomNumberBetween(1, endTime))));
                    if (settingType == EnergyMeterSettingType.AMOUNT_FORMULA) {
                        int idx = RandomGenerator.getRandomNumberBetween(0, amountFormula.size()-1);
                        log.setFormulaId(amountFormula.get(idx).getId());
                    } else if (settingType == EnergyMeterSettingType.COST_FORMULA) {
                        int idx = RandomGenerator.getRandomNumberBetween(0, costFormula.size()-1);
                        log.setFormulaId(costFormula.get(idx).getId());
                    } else if (settingType == EnergyMeterSettingType.PRICE) {
                        log.setSettingValue(new BigDecimal(RandomGenerator.getRandomNumberBetween(1, 4)));
                    } else {
                        log.setSettingValue(new BigDecimal(RandomGenerator.getRandomNumberBetween(2, 4)));
                    }
                    EhEnergyMeterSettingLogsDao dao = new EhEnergyMeterSettingLogsDao(context().configuration());
                    dao.insert(log);
                }
            }
        }
    }

    private void initEnergyMeterChangeLogs(List<EhEnergyMeters> meters) {

    }

    private void initEnergyMeterReadingLogs(List<EhEnergyMeters> meters) {
        // Byte[] arr = {TrueOrFalseFlag.FALSE.getCode(), TrueOrFalseFlag.FALSE.getCode(), TrueOrFalseFlag.TRUE.getCode(), TrueOrFalseFlag.FALSE.getCode()};
        Long id = context().select(DSL.max(Tables.EH_ENERGY_METER_READING_LOGS.ID)).from(Tables.EH_ENERGY_METER_READING_LOGS).fetchOneInto(Long.class);
        for (EhEnergyMeters meter : meters) {
            int lastReading = 0;
            for (int i = 0; i < 31; i++) {
                EhEnergyMeterReadingLogs log = new EhEnergyMeterReadingLogs();
                log.setId(++id);
                log.setMeterId(meter.getId());
                log.setCommunityId(1L);
                log.setCreateTime(Timestamp.valueOf(LocalDateTime.of(2016, 10, 1, 0, 0).plusDays(-1).plusDays(i)));
                log.setCreateTime(log.getCreateTime());
                log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                // int changeFlag = RandomGenerator.getRandomNumberBetween(0, arr.length);
                // log.setChangeMeterFlag((byte)changeFlag);
                int reading = RandomGenerator.getRandomNumberBetween(lastReading > 100 ? lastReading - 10 : lastReading, meter.getMaxReading().intValue());
                log.setChangeMeterFlag(TrueOrFalseFlag.FALSE.getCode());
                log.setReading(new BigDecimal(reading));
                if (reading < lastReading) {
                    log.setResetMeterFlag(TrueOrFalseFlag.TRUE.getCode());
                }
                lastReading = reading;
                EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context().configuration());
                dao.insert(log);
            }
            for (int i = 0; i < 31; i++) {
                EhEnergyMeterReadingLogs log = new EhEnergyMeterReadingLogs();
                log.setId(++id);
                log.setMeterId(meter.getId());
                log.setCommunityId(1L);
                log.setCreateTime(Timestamp.valueOf(LocalDateTime.of(2015, 10, 1, 0, 0).plusDays(-1).plusDays(i)));
                log.setCreateTime(log.getCreateTime());
                log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                // int changeFlag = RandomGenerator.getRandomNumberBetween(0, arr.length);
                // log.setChangeMeterFlag((byte)changeFlag);
                int reading = RandomGenerator.getRandomNumberBetween(lastReading > 100 ? lastReading - 10 : lastReading, meter.getMaxReading().intValue());
                log.setChangeMeterFlag(TrueOrFalseFlag.FALSE.getCode());
                log.setReading(new BigDecimal(reading));
                if (reading < lastReading) {
                    log.setResetMeterFlag(TrueOrFalseFlag.TRUE.getCode());
                }
                lastReading = reading;
                EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context().configuration());
                dao.insert(log);
            }
        }
    }

    private List<EhEnergyMeters> initEnergyMeter() {
        List<EhEnergyMeters> metersList = new ArrayList<>();

        Result<EhEnergyMeterCategoriesRecord> billCategories = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES)
                .where(Tables.EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(EnergyCategoryType.BILL.getCode()))
                .fetch();
        Result<EhEnergyMeterCategoriesRecord> serviceCategories = context().selectFrom(Tables.EH_ENERGY_METER_CATEGORIES)
                .where(Tables.EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(EnergyCategoryType.SERVICE.getCode()))
                .fetch();

        Long id = context().select(DSL.max(Tables.EH_ENERGY_METERS.ID)).from(Tables.EH_ENERGY_METERS).fetchOneInto(Long.class);
        for (EhEnergyMeterCategoriesRecord billCategory : billCategories) {
            for (EhEnergyMeterCategoriesRecord serviceCategory : serviceCategories) {
                for (int i = 1; i < 5; i++) {
                    EhEnergyMeters meter = new EhEnergyMeters();
                    meter.setBillCategoryId(billCategory.getId());
                    meter.setServiceCategoryId(serviceCategory.getId());
                    meter.setName("水表"+i);
                    meter.setCommunityId(1L);
                    meter.setMaxReading(new BigDecimal(10000L+i*10));
                    meter.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                    meter.setMeterNumber("W:132456789:"+id);
                    meter.setStartReading(new BigDecimal(100-i));
                    meter.setId(++id);

                    EhEnergyMetersDao dao = new EhEnergyMetersDao(context().configuration());
                    dao.insert(meter);
                    metersList.add(meter);
                }
            }
        }
        return metersList;
    }

    private void bulkReadingLogData() {
        searchProvider.clearType("energyMeterReadingLog");
        searchProvider.bulk("energyMeterReadingLog", "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"1\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"2\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":2,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"3\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824100\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"4\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小黑\",\"operateTime\":\"1478069824200\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"5\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":2,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824300\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"6\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"7\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }\n" +
                "{ \"index\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"energyMeterReadingLog\", \"_id\" : \"8\" } }\n" +
                "{ \"communityId\" : 1, \"meterId\":1,\"meterType\":1,\"billCategoryId\":1,\"serviceCategoryId\":1,\"reading\":222.22,\"resetFlag\":1,\"meterName\":\"电表1\",\"meterNumber\":\"123\",\"operatorName\":\"小红\",\"operateTime\":\"1478069824400\" }");
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
}
