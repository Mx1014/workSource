package com.everhomes.test.junit.energy;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.energy.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterSettingLogs;
import com.everhomes.server.schema.tables.records.EhEnergyMetersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METERS;

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


    //1. 新建表记(水表,电表等)
    @Test
    public void testCreateEnergyMeter() {
        logon();
        CreateEnergyMeterRestResponse response = createMeter();
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

    private CreateEnergyMeterRestResponse createMeter() {
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
        createMeter();
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
        // cmd.setStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        // cmd.setEndTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

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
    public void testSearchEnergyMeter() throws IOException {
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

    //14. 导入表记(Excel)
    @Test
    public void testImportEnergyMeter() throws IOException {
        logon();
        ImportEnergyMeterCommand cmd = new ImportEnergyMeterCommand();
        cmd.setOrganizationId(1L);
        cmd.setCommunityId(1L);
        String filePath = new File("").getCanonicalPath()+"\\src\\test\\data\\excel\\energy_meter_template.xlsx";
        RestResponseBase response = httpClientService.postFile(IMPORT_ENERGY_METER_URL, cmd, new File(filePath), RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        Result<EhEnergyMetersRecord> result = context().selectFrom(EH_ENERGY_METERS).fetch();
        assertTrue(result.size() == 3);
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
