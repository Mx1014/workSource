// @formatter:off
package com.everhomes.test.demo;

import org.jooq.DSLContext;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.test.core.BaseServerTestCase;
import com.everhomes.test.core.persist.DbProvider;

public class PrepareDbTest extends BaseServerTestCase {
    
    @Autowired
    private DSLContext dbContext;
    
    @Autowired
    private DbProvider dbProvider; 
    
    /**
     * <p>先准备一个SQL文件（包含所有建表SQL语句，先DROP表，再CREATE表），执行该SQL文件进行重新建库。</p>
     * <p>速度最慢，大概需要3到5分钟，不同性能的机器表现不一样。好处是数据库中没有表，也可以重新建库。</p>
     */
    @Ignore @Test
    public void prepareDbWithDropAndCreateSql() {
    	String createTablefilePath = "data/tables/3.4.x_create_tables.sql";
        long startTime = System.currentTimeMillis();
        dbProvider.runClassPathSqlFile(createTablefilePath);
        long endTime = System.currentTimeMillis();
        System.out.println("run create tables sql file, elapse=" + (endTime - startTime));
    }
    
    /**
     * <p>先准备一个SQL文件（包含所有表的TRUNCATE语句），执行该SQL文件把数据库中的数据全部去掉。</p>
     * <p>速度次之，大概需要2分钟，不同性能的机器表现不一样。执行前要先导入一份数据库（它是基于有表结构的情况下清数据的）。</p>
     */
    @Ignore @Test
    public void prepareDbWithTruncateSql() {
        String truncateTablefilePath = "data/tables/3.4.x_truncate_tables.sql";
        long startTime = System.currentTimeMillis();
        dbProvider.runClassPathSqlFile(truncateTablefilePath);
        long endTime = System.currentTimeMillis();
        System.out.println("run truncate tables sql file, elapse=" + (endTime - startTime));
    }
    
    /**
     * <p>把数据库中的所有表结构都读取出来，然后一个个把数据删除掉。不依赖SQL文件，但数据库版本需要支持能够拿到meta信息。</p>
     * <p>速度大概需要1分钟，不同性能的机器表现不一样。执行前要先导入一份数据库（它是基于有表结构的情况下清数据的）。</p>
     */
    @Ignore @Test
    public void prepareDbWithTruncateTables() {
        long startTime = System.currentTimeMillis();
        dbProvider.truncateAllTables();
        long endTime = System.currentTimeMillis();
        System.out.println("run truncate tables sql file, elapse=" + (endTime - startTime));
    }
}

