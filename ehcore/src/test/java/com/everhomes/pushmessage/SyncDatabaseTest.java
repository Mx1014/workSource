package com.everhomes.pushmessage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Assert;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.db.DbProvider;
import com.everhomes.dbsync.DataGraph;
import com.everhomes.dbsync.DatabaseQuery;
import com.everhomes.dbsync.JSDataGraphObject;
import com.everhomes.dbsync.JSDataQueryObject;
import com.everhomes.dbsync.JSMappingBelongObject;
import com.everhomes.dbsync.JSMappingObjectItem;
import com.everhomes.dbsync.JSQueryObjectItem;
import com.everhomes.dbsync.NashornHttpObject;
import com.everhomes.dbsync.NashornMappingObject;
import com.everhomes.dbsync.NashornObject;
import com.everhomes.dbsync.NashornObjectService;
import com.everhomes.dbsync.NashornProcessService;
import com.everhomes.dbsync.SyncApp;
import com.everhomes.dbsync.SyncAppProvider;
import com.everhomes.dbsync.SyncDatabaseService;
import com.everhomes.dbsync.SyncMapping;
import com.everhomes.dbsync.SyncMappingProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.dbsync.CreateSyncMappingCommand;
import com.everhomes.rest.dbsync.SyncAppCreateCommand;
import com.everhomes.rest.dbsync.SyncAppDTO;
import com.everhomes.rest.dbsync.SyncMappingDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class SyncDatabaseTest extends LoginAuthTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqTest.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private NashornProcessService jsService;
    
    @Autowired
    private NashornObjectService nashornObjectService;
    
    @Autowired
    private SyncDatabaseService syncDatabaseService;
    
    @Autowired
    private SyncAppProvider syncAppProvider;
    
    @Autowired
    private SyncMappingProvider syncMappingProvider;
    
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
    public void testQuery() {
        DatabaseQuery query = new DatabaseQuery();
        DataGraph dataGraph = nashornObjectService.getGraph("testGraph");
        query.setDataGraph(dataGraph);
        query.addCondition(dataGraph.getTable().getTableName(), "eh_users.id = $userId");
        query.putInput("userId", "227281");
        
        List<Map<String, Object>> records = nashornObjectService.query(query);
        String jsonStr = StringHelper.toJsonString(records);
        LOGGER.info("records=" + jsonStr);
        
        Assert.assertTrue(jsonStr.indexOf("identifier_token") > 0);
    }
    
    @Test
    public void testCreateApp() {
        
        SyncAppCreateCommand cmd = new SyncAppCreateCommand();
        cmd.setAppKey("test");
        cmd.setDescription("description");
        cmd.setName("testCreateApp");
        cmd.setOwnerId(0l);
        cmd.setOwnerType("testType");
        
        SyncAppDTO dto = syncDatabaseService.createApp(cmd);
        
        Assert.assertTrue(dto.getId() > 0);
        
        SyncApp app = syncAppProvider.getSyncAppById(dto.getId());
        syncAppProvider.deleteSyncApp(app);
    }
    
    @Test
    public void testGraphCreate() {
    	JSDataGraphObject obj = new JSDataGraphObject();
    	obj.setAppName("testApp");
    	obj.setMapName("testMap");
    	List<String> tables = new ArrayList<String>();
    	tables.add("eh_user_identifiers");
    	tables.add("eh_users");
    	obj.setTables(tables);
    	
    	JSMappingObjectItem mItem = new JSMappingObjectItem();
    	List<String> fields = new ArrayList<String>();
    	//"id", "owner_uid", "identifier_type", "identifier_token", "claim_status"
    	fields.add("id");
    	fields.add("owner_uid");
    	fields.add("identifier_token");
    	fields.add("claim_status");
    	fields.add("identifier_type");
    	mItem.setFields(fields);
    	
    	List<JSMappingBelongObject> belongs = new ArrayList<JSMappingBelongObject>();
    	mItem.setBelong(belongs);
    	JSMappingBelongObject bItem = new JSMappingBelongObject();
    	bItem.setFieldA("owner_uid");
    	bItem.setFieldB("id");
    	bItem.setJoin("INNER_JOIN");
    	bItem.setTable(tables.get(1));
    	belongs.add(bItem);
    	
    	obj.getMapping().put(tables.get(0), mItem);
    	
    	//"id", "uuid", "account_name", "nick_name"
    	mItem = new JSMappingObjectItem();
    	fields = new ArrayList<String>();
    	fields.add("id");
    	fields.add("uuid");
    	fields.add("account_name");
    	fields.add("nick_name");
    	mItem.setFields(fields);
    	obj.getMapping().put(tables.get(1), mItem);
    	
    	DataGraph dataGraph = nashornObjectService.createGraph(StringHelper.toJsonString(obj));
    	Assert.assertTrue(dataGraph != null);
    	
    	JSDataQueryObject queryObj = new JSDataQueryObject();
    	List<JSQueryObjectItem> queryItems = new ArrayList<JSQueryObjectItem>();
    	JSQueryObjectItem oItem = new JSQueryObjectItem();
    	oItem.getConditions().add(" eh_user_identifiers.claim_status = $claimStatus ");
    	oItem.getConditions().add(" eh_users.id = $userId ");
    	oItem.getDefaults().put("claimStatus", "3");
    	
    	queryItems.add(oItem);
    	
    	queryObj.put("getByUserId", queryItems);
    	
    	nashornObjectService.createQueryBase(obj.getAppName(), obj.getMapName(), StringHelper.toJsonString(queryObj));
    }
    
    @Test
    public void testCreateMapping() {
    	CreateSyncMappingCommand cmd = new CreateSyncMappingCommand();
    	cmd.setContent("mapping001.js");
    	cmd.setName("mapping001");
    	cmd.setSyncAppId(1l);
    	SyncMappingDTO dto = syncDatabaseService.createMapping(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    	
    	SyncMapping mapping = syncMappingProvider.getSyncMappingById(dto.getId());
    	syncMappingProvider.deleteSyncMapping(mapping);
    	
        try {
            Thread.sleep(15*1000l);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void jsInvokeTest(ScriptEngine engine, NashornObject nobj) {
        try {
        	
            try {
            	if(null == nobj.getJSFunc()) {
            		return;
            	}
            	
                nashornObjectService.put(nobj);
                Invocable jsInvoke = (Invocable) engine;
                jsInvoke.invokeFunction(nobj.getJSFunc(), new Object[] {nobj.getId()});
                nobj.onComplete();
            } catch(Exception e) {
                nobj.onError(e);
                LOGGER.error("eval js error", e);
                }
                //try clear it
                nashornObjectService.clear(nobj.getId());
            } catch (Exception e) {
                // weird if we reached here - something wrong is happening, but we shouldn't stop the service anyway!
                LOGGER.warn("Unexpected message caught... Shouldn't be here", e);
        }        
    }
    
    @Test
    public void testCreateMappingJob() {
    	CreateSyncMappingCommand cmd = new CreateSyncMappingCommand();
    	cmd.setContent("mapping001.js");
    	cmd.setName("mapping001");
    	cmd.setSyncAppId(1l);
    	
    	SyncMapping map = ConvertHelper.convert(cmd, SyncMapping.class);
      map.setStatus((byte)1);//mark as valid
      syncMappingProvider.createSyncMapping(map);
    	Assert.assertTrue(map.getId() > 0);
    	
    	ScriptEngineManager manager = new ScriptEngineManager();
    	ScriptEngine engine = null;
    	InputStreamReader reader;
    	try {
            engine = manager.getEngineByName("nashorn");
            engine.put("nashornObjs", nashornObjectService);
            engine.put("jThreadId", String.valueOf(Thread.currentThread().getId()));
            
            Resource js = new ClassPathResource("/dbsync/jvm-npm.js");
            reader = new InputStreamReader(js.getInputStream(), "UTF-8");
            engine.eval(reader);
            
            js = new ClassPathResource("/dbsync/init.js");
            reader = new InputStreamReader(js.getInputStream(), "UTF-8");
            engine.eval(reader);
            
        } catch (ScriptException | IOException e) {
            LOGGER.error("start js engine error", e);
        }
    	
    	SyncApp app = syncAppProvider.getSyncAppById(map.getSyncAppId());
    	NashornMappingObject mapObj = new NashornMappingObject();
    	mapObj.setName(map.getName());
    	mapObj.setAppName(app.getName());
    	jsInvokeTest(engine, mapObj);
    	
    	DataGraph dataGraph = nashornObjectService.getGraph("testCreateApp$mapping001");
    	Assert.assertTrue(dataGraph != null);
    	
    	DatabaseQuery query = new DatabaseQuery();
    	query.setDataGraph(dataGraph);
    	//" eh_users.id = $userId ", " eh_user_identifiers.claim_status = $claimStatus "
    	query.addCondition(dataGraph.getTable().getTableName(), " eh_users.id = $userId ");
    	query.addCondition(dataGraph.getTable().getTableName(), "eh_user_identifiers.claim_status = $claimStatus");
    	query.putInput("userId", "227281");
    	query.putInput("claimStatus", "3");
    	
    	List<Map<String, Object>> records = nashornObjectService.query(query);
    	String jsonStr = StringHelper.toJsonString(records);
    	LOGGER.info("records=" + jsonStr);
    	Assert.assertTrue(jsonStr.indexOf("identifier_token") > 0);
    	
    	SyncMapping mapping = syncMappingProvider.getSyncMappingById(map.getId());
    	syncMappingProvider.deleteSyncMapping(mapping);
    }
    
    @Test
    public void testBaseQuery() {
    	CreateSyncMappingCommand cmd = new CreateSyncMappingCommand();
    	cmd.setContent("mapping001.js");
    	cmd.setName("mapping001");
    	cmd.setSyncAppId(1l);
    	
    	SyncMapping map = ConvertHelper.convert(cmd, SyncMapping.class);
      map.setStatus((byte)1);//mark as valid
      syncMappingProvider.createSyncMapping(map);
    	Assert.assertTrue(map.getId() > 0);
    	
    	ScriptEngineManager manager = new ScriptEngineManager();
    	ScriptEngine engine = null;
    	InputStreamReader reader;
    	try {
            engine = manager.getEngineByName("nashorn");
            engine.put("nashornObjs", nashornObjectService);
            engine.put("jThreadId", String.valueOf(Thread.currentThread().getId()));
            
            Resource js = new ClassPathResource("/dbsync/jvm-npm.js");
            reader = new InputStreamReader(js.getInputStream(), "UTF-8");
            engine.eval(reader);
            
            js = new ClassPathResource("/dbsync/init.js");
            reader = new InputStreamReader(js.getInputStream(), "UTF-8");
            engine.eval(reader);
            
        } catch (ScriptException | IOException e) {
            LOGGER.error("start js engine error", e);
        }
    	
    	SyncApp app = syncAppProvider.getSyncAppById(map.getSyncAppId());
    	NashornMappingObject mapObj = new NashornMappingObject();
    	mapObj.setName(map.getName());
    	mapObj.setAppName(app.getName());
    	jsInvokeTest(engine, mapObj);
    	
    	DataGraph dataGraph = nashornObjectService.getGraph("testCreateApp$mapping001");
    	Assert.assertTrue(dataGraph != null);
    	
    	String jsonStr = nashornObjectService.makeQuery(app.getName(), map.getName(), "getByUserId", "{\"userId\": 227281}");
    	LOGGER.info("records=" + jsonStr);
    	Assert.assertTrue(jsonStr.indexOf("identifier_token") > 0);
    	
    	SyncMapping mapping = syncMappingProvider.getSyncMappingById(map.getId());
    	syncMappingProvider.deleteSyncMapping(mapping);
    }
    
    @Test
    public void testHttpRequest() {
    	try {
    		//wait other threads ok
    		Thread.sleep(15*1000l);
    	} catch (InterruptedException e) {
    			e.printStackTrace();
    	}
    	
    	DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>();
    	NashornHttpObject obj = new NashornHttpObject(deferredResult);
    	obj.setUrl("/testCreateApp/mapping001/getByUserId");
    	obj.setAppName("testCreateApp");
    	obj.setMapName("mapping001");
    	obj.setQuery("getByUserId");
    	obj.setBody("{\"userId\": 227281}");
    	jsService.push(obj);
    	
    	LOGGER.info("result" + deferredResult.getResult());
    	
    	try {
    		Thread.sleep(15*1000l);
    	} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
    }
    
    @Test
    public void testRawQuery() {
    	//TODO named bindings: http://www.jooq.org/doc/2.5/manual/sql-building/bind-values/named-parameters/
    	
    	String sql1 = "select * from eh_users join eh_user_identifiers on eh_users.id=eh_user_identifiers.owner_uid where eh_user_identifiers.identifier_token = ?";
    	Result<Record> records = DSL.using(nashornObjectService.configure()).fetch(sql1, "13564546106");
    	LOGGER.info("records=" + records);
    	
    	List<String> strs = new ArrayList<String>();
    	strs.add("eh_users.id");
    	strs.add("eh_users.uuid");
    	strs.add("eh_users.account_name");
    	LOGGER.info("" + DSL.using(nashornObjectService.configure()).select(nashornObjectService.fields(strs))
    			.from(nashornObjectService.getTableMeta("eh_users").getTableJOOQ())
    			.where("eh_users.account_name like ?", "1%").limit(10).fetch());
    	
    	String sql2 = "select * from eh_users join eh_user_identifiers on eh_users.id=eh_user_identifiers.owner_uid where eh_user_identifiers.identifier_token = $phone";
    	String jsonResult = nashornObjectService.makeRawQuery(sql2, "{\"regCityId\": 10002}", "{\"phone\": \"13564546106\"}");
    	LOGGER.info("result=" + jsonResult);
    }
    
    @Test
    public void testRawQueryObj() {
    	try {
    		//wait other threads ok
    		Thread.sleep(15*1000l);
    	} catch (InterruptedException e) {
    			e.printStackTrace();
    	}
    	
    	DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>();
    	NashornHttpObject obj = new NashornHttpObject(deferredResult);
    	obj.setUrl("/testCreateApp/mapping001/getByPhone");
    	obj.setAppName("testCreateApp");
    	obj.setMapName("mapping001");
    	obj.setQuery("getByPhone");
    	obj.setBody("{\"phone\": \"13564546106\"}");//What about like ???
    	jsService.push(obj);
    	
    	LOGGER.info("result" + deferredResult.getResult());
    	
    	try {
    		Thread.sleep(15*1000l);
    	} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
    }
    
    @Test
    public void testDSLJS() {
    	ScriptEngineManager manager = new ScriptEngineManager();
    	ScriptEngine engine = null;
    	InputStreamReader reader;
    	try {
            engine = manager.getEngineByName("nashorn");
            engine.put("nashornObjs", nashornObjectService);
            engine.put("jThreadId", String.valueOf(Thread.currentThread().getId()));
            
            Resource js = new ClassPathResource("/dbsync/jvm-npm.js");
            reader = new InputStreamReader(js.getInputStream(), "UTF-8");
            engine.eval(reader);
            
            js = new ClassPathResource("/dbsync/init.js");
            reader = new InputStreamReader(js.getInputStream(), "UTF-8");
            engine.eval(reader);
            
        } catch (ScriptException | IOException e) {
            LOGGER.error("start js engine error", e);
        }
    	
        Invocable jsInvoke = (Invocable) engine;
        try {
			jsInvoke.invokeFunction("testDSL");
		} catch (NoSuchMethodException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testDSLQueryObj() {
    	try {
    		//wait other threads ok
    		Thread.sleep(15*1000l);
    	} catch (InterruptedException e) {
    			e.printStackTrace();
    	}
    	
    	DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>();
    	NashornHttpObject obj = new NashornHttpObject(deferredResult);
    	obj.setUrl("/testCreateApp/mapping001/findByAccountName");
    	obj.setAppName("testCreateApp");
    	obj.setMapName("mapping001");
    	obj.setQuery("findByAccountName");
    	obj.setBody("{\"accountName\": \"1\"}");//What about like ???
    	jsService.push(obj);
    	
    	LOGGER.info("result:" + deferredResult.getResult());
    	
    	try {
    		Thread.sleep(15*1000l);
    	} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
    }
}
