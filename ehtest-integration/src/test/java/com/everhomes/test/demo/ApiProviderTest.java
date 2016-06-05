// @formatter:off
package com.everhomes.test.demo;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.test.core.api.ApiProvider;
import com.everhomes.test.core.base.BaseServerTestCase;
import com.everhomes.test.core.persist.DbProvider;

public class ApiProviderTest extends BaseServerTestCase {
    @Autowired
    private DbProvider dbProvider; 
    
    @Autowired
    private ApiProvider apiProvider;
    
    /**
     * <p>先找一个有数据的数据库，把配置文件的数据库改为指定数据库，把指定的数据查出来转化为JSON字符串，并格式化写到一个文件上</p>
     */
    @Ignore @Test
    public void syncSequence() {        
        String truncateTablefilePath = "data/tables/3.4.x_truncate_tables.sql";
        //dbProvider.runClassPathSqlFile(truncateTablefilePath);
        
        String systemInitDatafilePath = "data/tables/3.0.x_ehcore-system-init.sql";
        //dbProvider.runClassPathSqlFile(systemInitDatafilePath);
        
        apiProvider.syncSequence();
    }
}

