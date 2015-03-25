// @formatter:off
package com.everhomes.region;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.junit.PropertyInitializer;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class RegionProviderTest  extends TestCase {
     
    @Autowired
    RegionProvider regionProvider;
    
    List<Integer> newRecordIds = new ArrayList<Integer>();
    
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
    public void setup() {
        Region region = new Region();
        region.setName("China");
        region.setPath("China");
        region.setScopeEnum(RegionScope.COUNTRY);
        region.setStatusEnum(RegionAdminStatus.ACTIVE);
        region.setLevel(0);
        this.regionProvider.createRegion(region);
        this.newRecordIds.add(region.getId());
        
        Region region2 = new Region();
        region2.setParentId(region.getParentId());
        region2.setName("Beijing");
        region2.setPath("China/Beijing");
        region2.setScopeEnum(RegionScope.CITY);
        region2.setStatusEnum(RegionAdminStatus.ACTIVE);
        region2.setLevel(1);
        this.regionProvider.createRegion(region2);
        this.newRecordIds.add(region2.getId());

        region2 = new Region();
        region2.setParentId(region.getParentId());
        region2.setName("Shanghai");
        region2.setPath("China/Shanghai");
        region2.setScopeEnum(RegionScope.CITY);
        region2.setStatusEnum(RegionAdminStatus.ACTIVE);
        region2.setLevel(1);
        this.regionProvider.createRegion(region2);
        this.newRecordIds.add(region2.getId());
        
        region2 = new Region();
        region2.setParentId(region.getParentId());
        region2.setName("Hubei");
        region2.setPath("China/Hubei");
        region2.setScopeEnum(RegionScope.PROVINCE);
        region2.setStatusEnum(RegionAdminStatus.ACTIVE);
        region2.setLevel(1);
        this.regionProvider.createRegion(region2);
        this.newRecordIds.add(region2.getId());
        
        Region region3 = new Region();
        region3.setParentId(region2.getParentId());
        region3.setName("Wuhan");
        region3.setPath("China/Hubei/Wuhan");
        region3.setScopeEnum(RegionScope.CITY);
        region3.setStatusEnum(RegionAdminStatus.ACTIVE);
        region3.setLevel(2);
        this.regionProvider.createRegion(region3);
        this.newRecordIds.add(region3.getId());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        List<Region> regions = this.regionProvider.listRegion(RegionScope.CITY, null, 
                new Tuple<String, SortOrder>("name", SortOrder.DESC));
        
        for(Region region: regions) {
            System.out.println("region: " + region.toString());
        }
        
        regions = this.regionProvider.listDescendantRegion(null, RegionScope.CITY, null, 
                new Tuple<String, SortOrder>("name", SortOrder.DESC));
        
        for(Region region: regions) {
            System.out.println("region: " + region.toString());
        }
    }
    
    @After
    public void tearDown() {
        for(Integer id : this.newRecordIds) {
            this.regionProvider.deleteRegionById(id);
        }
    }
}
