package com.everhomes.pushmessage;

import java.util.List;
import java.util.Map;

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

import com.everhomes.db.DbProvider;
import com.everhomes.dbsync.DataGraph;
import com.everhomes.dbsync.DatabaseQuery;
import com.everhomes.dbsync.NashornObjectService;
import com.everhomes.dbsync.NashornProcessService;
import com.everhomes.dbsync.SyncApp;
import com.everhomes.dbsync.SyncAppProvider;
import com.everhomes.dbsync.SyncDatabaseService;
import com.everhomes.dbsync.SyncMapping;
import com.everhomes.dbsync.SyncMappingProvider;
import com.everhomes.rest.dbsync.CreateSyncMappingCommand;
import com.everhomes.rest.dbsync.SyncAppCreateCommand;
import com.everhomes.rest.dbsync.SyncAppDTO;
import com.everhomes.rest.dbsync.SyncMappingDTO;
import com.everhomes.user.base.LoginAuthTestCase;
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
    public void testCreateMapping() {
    	CreateSyncMappingCommand cmd = new CreateSyncMappingCommand();
    	cmd.setContent("testdb.js");
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
}
