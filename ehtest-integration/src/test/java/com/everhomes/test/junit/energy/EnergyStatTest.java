package com.everhomes.test.junit.energy;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.energy.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFormulasDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulas;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterSettingLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.server.schema.tables.records.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METERS;
import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_SETTING_LOGS;

/**
 * Created by xq.tian on 2016/10/31.
 */
public class EnergyStatTest extends BaseLoginAuthTestCase{

    //1. 新建表记(水表,电表等)
    private static final String CACULATE_MONTH_STAT = "/energy/caculateEnergyMonthStatByDate";
    //2. 修改表记(水表,电表等),表记分类,性质,账单项目不能修改
    private static final String CACULATE_DAY_STAT = "/energy/caculateEnergyDayStatByDate"; 
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
 
	SimpleDateFormat daySF = new SimpleDateFormat("yyyyMMdd");
    //1. 准备数据: 2016年10月的数据和2015年10月数据,
    @Test
    public void testCaculateDayStat() throws ParseException {
        logon();
        EnergyStatCommand cmd = new EnergyStatCommand(); 
        //2016年
        Calendar cal = Calendar.getInstance();
        cal.setTime(daySF.parse("20161101"));
        Calendar anchor = Calendar.getInstance();
        anchor.setTime(daySF.parse("20161001"));
        for(;anchor.compareTo(cal) <=0 ;anchor.add(Calendar.DAY_OF_MONTH, 1)){
	        cmd.setStatDate(anchor.getTime().getTime());
	        RestResponseBase response = httpClientService.restPost(CACULATE_DAY_STAT, cmd, RestResponseBase.class);
	        assertNotNull(response);
	        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        }
        //计算到最后一天刷月的(传11月刷前一月)
        RestResponseBase response = httpClientService.restPost(CACULATE_MONTH_STAT, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        //2015 年 
        cal.setTime(daySF.parse("20151101")); 
        anchor.setTime(daySF.parse("20151001"));
        for(;anchor.compareTo(cal) <=0 ;anchor.add(Calendar.DAY_OF_MONTH, 1)){
	        cmd.setStatDate(anchor.getTime().getTime());
	        response = httpClientService.restPost(CACULATE_DAY_STAT, cmd, RestResponseBase.class);
	        assertNotNull(response);
	        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        }
        //计算到最后一天刷月的(传11月刷前一月)
        response = httpClientService.restPost(CACULATE_MONTH_STAT, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
 
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

//        jsonFilePath = "data/json/energy-test-data-161031.json";
//        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
//        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/energy-reading-log-3-test-data-161104.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }
}
