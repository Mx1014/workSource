package com.everhomes.pushmessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.db.DbProvider;
import com.everhomes.dbsync.DataGraph;
import com.everhomes.dbsync.DataTable;
import com.everhomes.dbsync.DatabaseQuery;
import com.everhomes.dbsync.NashornHttpObject;
import com.everhomes.dbsync.NashornObjectService;
import com.everhomes.dbsync.NashornProcessService;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.Ehcore;
import com.everhomes.server.schema.Keys;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhDoorUserPermission;

public class NashornTest extends LoginAuthTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqTest.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    NashornProcessService jsService;
    
    @Autowired
    private NashornObjectService nashornObjectService;
   
    ScriptEngineManager engineManager = new ScriptEngineManager();
    ScriptEngine engine;
    
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        engine = engineManager.getEngineByName("nashorn");
    }
    
    @After
    public void tearDown() {
    }
    
    //https://github.com/shekhargulati/java8-the-missing-tutorial/blob/master/10-nashorn.md
    @Test
    public void shouldStringifyCorrectly() throws ScriptException {
        String filename = "/home/janson/everhomes/jjs-test/db.js";
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            engineManager = new ScriptEngineManager();
            engine = engineManager.getEngineByName("nashorn");
            engine.eval(reader);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testEval() throws ScriptException {
        String filename = "/home/janson/everhomes/jjs-test/http.js";
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            engineManager = new ScriptEngineManager();
            engine = engineManager.getEngineByName("nashorn");
            engine.put("nashornObjs", nashornObjectService);
            engine.eval(reader);
            engine.eval("httpProcess(0);");
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  
    
    @Test
    public void testEval2() throws ScriptException {
        String filename = "/home/janson/everhomes/jjs-test/http.js";
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            engineManager = new ScriptEngineManager();
            engine = engineManager.getEngineByName("nashorn");
            engine.put("nashornObjs", nashornObjectService);
            //DSL.using(dbsyncMap.configure()).select(Tables.TABLES.TABLE_SCHEMA).from(Tables.TABLES).fetch();
            engine.eval(reader);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } 
    
    @Test
    public void testDSL() {
        Result<Record> records = DSL.using(nashornObjectService.configure()).fetch("select * from information_schema.`COLUMNS` where table_schema = 'ehcore' and table_name = 'eh_activities'");
        records.get(0);
        LOGGER.info("" + records.get(0));
        
        EhDoorUserPermission ep = EhDoorUserPermission.EH_DOOR_USER_PERMISSION;
        Field<?> field = ep.fields()[0];
        LOGGER.info("name:" + field.getName() + " datatype:" + field.getDataType()
                + " isdefaulted:" + field.getDataType().defaulted()
                + " isnumber:" + field.getDataType().isNumeric() + " type:" + field.getType());
        LOGGER.info("fields" + ep.fields());
    }
    
    @Test
    public void testQuery() {
        DatabaseQuery query = new DatabaseQuery();
        DataGraph dataGraph = nashornObjectService.getGraph("testGraph");
        query.setDataGraph(dataGraph);
        query.addCondition(dataGraph.getTable().getTableName(), "eh_users.id = $userId");
        query.putInput("userId", "227281");
        
        List<Map<String, Object>> records = nashornObjectService.query(query);
        LOGGER.info("records=" + StringHelper.toJsonString(records));
    }
    
    @Test
    public void testPattern() {
        Pattern pParam = Pattern.compile("\\$\\w+");
        String sql = "eh_users.id = $id and eh_door_user_permission.user_id = $user_id;a";
        String newSql = "";
        int start = 0;
        Matcher m = pParam.matcher(sql);
        while(m.find()) {
            newSql += sql.substring(start, m.start());
            newSql += "?";
            start = m.end();
            String sub = sql.substring(m.start(), m.end());
            LOGGER.info("sub=" + sub);
        }
        
        if(start > 0) {
            newSql += sql.substring(start, sql.length());
        }
        
        LOGGER.info("newSql=" + newSql);
    }
    
    @Test
    public void testHttpObjectQueue() {
        String restOfTheUrl = "aaaaa";
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        NashornHttpObject obj = new NashornHttpObject(deferredResult);
        obj.setUrl(restOfTheUrl);
        jsService.push(obj);  
        try {
            Thread.sleep(5*1000l);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        LOGGER.info("result" + deferredResult.getResult());
    }
}
