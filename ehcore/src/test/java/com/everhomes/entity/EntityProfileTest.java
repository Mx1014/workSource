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
import com.everhomes.server.schema.tables.daos.EhBannerProfilesDao;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhBannerProfiles;
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
    
    @Test
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
        System.out.println("item id: " + item.getId());

        EntityProfileItem item2 = new EntityProfileItem();
        item2.setOwnerId(100L);
        item2.setAppId(1L);
        item2.setItemName("item2");
        item2.setItemKind((byte)1);
        item2.setTargetType("EhUsers");
        item2.setTargetId(101L);
        //item2.setId(2L);
        
        profileProvider.createProfileItem(EhBanners.class, 1, EhBannerProfiles.class, item2);
        System.out.println("item id: " + item2.getId());
    }
    
    @Ignore @Test
    public void testJooqDiscovery() {
        Class<?> pojoClz = EhActivities.class;
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClz);
        System.out.println("table name: " + meta.getTableName());
        System.out.println("record class: " + meta.getRecordClass().getName());
        System.out.println("dao class: " + meta.getDaoClass().getName());
    }
    
    @Ignore @Test
    public void testFindProfileItemById() {
        EntityProfileItem item = this.profileProvider.findProfileItemById(EhBanners.class, EhBannerProfiles.class, 2);
        
        if(item != null)
            System.out.println("item name: " + item.getItemName());
        else
            System.out.println("item not found");
        
        // using debugger to verify if cache has been successfully put in-use for second call
        item = this.profileProvider.findProfileItemById(EhBanners.class, EhBannerProfiles.class, 2);
        if(item != null)
            System.out.println("item name: " + item.getItemName());
        else
            System.out.println("item not found");
    }
    
    @Ignore @Test
    public void testListProfileItems() {
        List<EntityProfileItem> items = this.profileProvider.listEntityProfileItems(
            EhBanners.class, 100, EhBannerProfiles.class);
        
        for(EntityProfileItem item: items) {
            System.out.println("item name: " + item.getItemName());
        }
        
        // udate item to test profile list cache-invalidation
        items.get(0).setItemName("item1");
        this.profileProvider.updateProfileItem(items.get(0));
        
        items = this.profileProvider.listEntityProfileItems(
                EhBanners.class, 100, EhBannerProfiles.class);
            
        for(EntityProfileItem item: items) {
            System.out.println("item name: " + item.getItemName());
        }
    }
}
