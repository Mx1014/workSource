// @formatter:off
package com.everhomes.test.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.test.core.BaseServerTestCase;
import com.everhomes.test.core.GsonHelper;
import com.everhomes.test.core.persist.DbProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class PrepareNewDbTest extends BaseServerTestCase {
    @Autowired
    private DbProvider dbProvider; 
    
    /**
     * <p>先找一个有数据的数据库，把配置文件的数据库改为指定数据库，把指定的数据查出来转化为JSON字符串，并格式化写到一个文件上</p>
     */
    @Test
    public void prepareJsonData() {
        String truncateTablefilePath = "data/tables/3.4.x_truncate_tables.sql";
        dbProvider.runClassPathSqlFile(truncateTablefilePath);
        
        String systemInitDatafilePath = "data/tables/3.0.x_ehcore-system-init.sql";
        dbProvider.runClassPathSqlFile(systemInitDatafilePath);
        
        String systemInitDatafilePath = "data/tables/3.0.x_ehcore-system-init.sql";
        dbProvider.runClassPathSqlFile(systemInitDatafilePath);
        
        apiProvider.syncSequence();
    }
}

