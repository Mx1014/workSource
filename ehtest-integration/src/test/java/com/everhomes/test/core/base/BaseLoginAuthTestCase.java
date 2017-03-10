// @formatter:off
package com.everhomes.test.core.base;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.rest.user.LogonCommand;
import com.everhomes.test.core.api.ApiProvider;
import com.everhomes.test.core.http.HttpClientService;
import com.everhomes.test.core.persist.DbProvider;
import com.everhomes.test.core.redis.RedisProvider;
import com.everhomes.test.core.util.EncryptionUtils;

public class BaseLoginAuthTestCase extends BaseServerTestCase {
    
    @Autowired
    protected HttpClientService httpClientService;
    
    @Autowired
    protected DbProvider dbProvider;
    
    @Autowired
    protected RedisProvider redisProvider;
    
    @Autowired
    protected ApiProvider apiProvider;
    
    protected UserContext context;
    
    @Before
    public void setUp() {
        // 清除表数据
        tuncateDbData();
        
        // 初始化系统数据（如系统帐号、权限、配置等）
        initSystemData();
        
        // 初始化自定义数据
        initCustomData();
        
        // 清除缓存
        clearRedisCache();
        
        // 同步索引
        syncSequence();
    }
    
    public void newSetUp() {
    	// 清除表数据
        tuncateDbData();
        
        // 初始化系统数据（如系统帐号、权限、配置等）
        initNewSystemData();
        
        // 初始化自定义数据
        initCustomData();
        
        // 清除缓存
        clearRedisCache();
        
        // 同步索引
        syncSequence();
    }
    
    @After
    public void tearDown() {
        
    }
    
    protected void tuncateDbData() {
        //String truncateTablefilePath = "data/tables/3.4.x_truncate_tables.sql";
        //dbProvider.runClassPathSqlFile(truncateTablefilePath);
        dbProvider.truncateAllTables();
    }
    
    protected void initSystemData() {
        String serverInitfilePath = "data/tables/3.0.x_ehcore-system-init.sql";
        dbProvider.runClassPathSqlFile(serverInitfilePath);
    }
    
    protected void initNewSystemData() {
    	long start = System.currentTimeMillis();
    	String serverInitfilePath = "data/tables/init_system_data.sql";
    	dbProvider.runClassPathSqlFile(serverInitfilePath);
    	System.err.println("new set up need time: "+(System.currentTimeMillis()-start));
    }
    
    protected void initCustomData() {
        
    }
    
    protected void clearRedisCache() {
        redisProvider.redisCacheFlushAll();
    }
    
    protected void syncSequence() {
        apiProvider.syncSequence();
    }
    
    protected void logon(Integer namespaceId, String userIdentifier, String plainTexPassword) {
        assertTrue("User identifier may not be empty", userIdentifier != null && userIdentifier.trim().length() > 0);
        assertTrue("User password may not be empty", plainTexPassword != null && plainTexPassword.trim().length() > 0);
        
        context = new UserContext(httpClientService, namespaceId);
        LogonCommand cmd = new LogonCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setUserIdentifier(userIdentifier);
        cmd.setPassword(EncryptionUtils.hashPassword(plainTexPassword));
        boolean result = context.logon(cmd);
        assertTrue("User should login successfully, context=" + context, result);
    }
    
    protected void logoff() {
        if(context != null) {
            boolean result = context.logoff();
            assertTrue("User should logoff successfully, context=" + context, result);
        }
    }
}

