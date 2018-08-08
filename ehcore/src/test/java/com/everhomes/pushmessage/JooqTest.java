package com.everhomes.pushmessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
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

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.tables.pojos.EhDoorUserPermission;
import com.everhomes.user.base.LoginAuthTestCase;

public class JooqTest extends LoginAuthTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqTest.class);
    
    @Autowired
    private DbProvider dbProvider;
    
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
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testJooq() {
        DSLContext create = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorUserPermission.class));
        Field field = DSL.field("namespace_id");
        Field[] fields = new Field[] { DSL.field("namespace_id"), DSL.field("id") };
        Table table = DSL.table("eh_door_user_permission");
        Table[] tables = new Table[] { table };
        
//        DSL.fieldByName("namespace_id");
//        DSL.abs(new Integer(10));
//        DSL.condition(field)
        Condition condition = field.eq(new Integer(1000000));
        Result<Record> r = create.select(fields).from(tables).where(condition).fetch();
        
        LOGGER.info("result=" + r);
    }
    
    @Test
    public void testScriptCompile() {
        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("js");

        try {
            engine.eval("function add (a, b) {c = a + b; return c; }");
            Invocable jsInvoke = (Invocable) engine;

            Object result1 = jsInvoke.invokeFunction("add", new Object[] { 10, 5 });
            System.out.println("result1="+result1);

            Adder adder = jsInvoke.getInterface(Adder.class);
            int result2 = adder.add(10, 5);
            LOGGER.info("result2=" + result2);
    
        } catch(Exception ex) {
            LOGGER.error("error", ex);
        }
    }
    
    @Test
    public void testScriptParams() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("a", 1);
        engine.put("b", 5);

        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        Object a = bindings.get("a");
        Object b = bindings.get("b");
        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Object result;
        try {
            result = engine.eval("c = a + b;");
            System.out.println("a + b = " + result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFile() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream("src/my.js"), "UTF-8");
            System.out.println(reader.getEncoding());
            engine.eval(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testList() {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factoryList = manager.getEngineFactories();
        System.out.println(factoryList.size());
        for (ScriptEngineFactory factory : factoryList) {
            System.out.println(factory.getEngineName() + "="
                    + factory.getLanguageName());
        }
    }
}
