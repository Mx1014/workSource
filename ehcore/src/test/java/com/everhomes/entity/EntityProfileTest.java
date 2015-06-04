// @formatter:off
package com.everhomes.entity;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqDiscover;
import com.everhomes.jooq.JooqMetaInfo;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhBanners;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class EntityProfileTest extends TestCase {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private EntityProfileProvider profileProvider;
    
    @Value("${db.master}")
    private String dbUrl;
    
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
            FreeMarkerAutoConfiguration.class
        })
    static class ContextConfiguration {
    }
    
    
    @Ignore @Test
    public void testJooqDiscovery() {
        Class<?> pojoClz = EhActivities.class;
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClz);
        System.out.println("table name: " + meta.getTableName());
        System.out.println("record class: " + meta.getRecordClass().getName());
        System.out.println("dao class: " + meta.getDaoClass().getName());
    }
}
