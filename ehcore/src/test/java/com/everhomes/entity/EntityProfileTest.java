// @formatter:off
package com.everhomes.entity;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqDiscover;
import com.everhomes.jooq.JooqMetaInfo;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.server.schema.tables.pojos.EhActivities;

public class EntityProfileTest extends CoreServerTestCase {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private EntityProfileProvider profileProvider;
    
    @Value("${db.master}")
    private String dbUrl;
    @Ignore @Test
    public void testJooqDiscovery() {
        Class<?> pojoClz = EhActivities.class;
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClz);
        System.out.println("table name: " + meta.getTableName());
        System.out.println("record class: " + meta.getRecordClass().getName());
        System.out.println("dao class: " + meta.getDaoClass().getName());
    }
}
