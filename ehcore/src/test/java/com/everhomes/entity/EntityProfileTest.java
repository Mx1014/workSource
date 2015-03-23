package com.everhomes.entity;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
import com.everhomes.server.schema.tables.daos.EhBannerProfilesDao;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhBannerProfiles;
import com.everhomes.util.ConvertHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class EntityProfileTest extends TestCase {
    @Autowired
    private DbProvider dbProvider;
    
    @Value("${db.master}")
    private String dbUrl;
    
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

    @Ignore @Test
    public void testBannerProfile() {
        EhBannerProfilesDao dao = new EhBannerProfilesDao(
                dbProvider.getDslContext(dbUrl).configuration());
        EhBannerProfiles item = new EhBannerProfiles();
        item.setOwnerId(100L);
        item.setAppId(1L);
        item.setItemName("testProfileName");
        item.setItemKind((byte)1);
        item.setTargetType("EhUsers");
        item.setTargetId(1L);
        item.setId(1L);
        dao.insert(item);
    }
    
    @Ignore @Test
    public void test() {
        EhBannerProfilesDao dao = new EhBannerProfilesDao(
                dbProvider.getDslContext(dbUrl).configuration());

        EhBannerProfiles item = dao.findById(1L);
        EntityProfileItem profile = ConvertHelper.convert(item, EntityProfileItem.class);
        
        System.out.println(profile.getItemName());
    }
    
    @Test
    public void testJooqDiscovery() {
        Class<?> pojoClz = EhActivities.class;
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClz);
        System.out.println("table name: " + meta.getTableName());
        System.out.println("record class: " + meta.getRecordClass().getName());
        System.out.println("dao class: " + meta.getDaoClass().getName());
    }
}
