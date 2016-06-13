// @formatter:off
package com.everhomes.region;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.user.UserContext;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

public class RegionProviderTest extends CoreServerTestCase {
     
    @Autowired
    RegionProvider regionProvider;
    
    List<Long> newRecordIds = new ArrayList<Long>();
    
    @Before
    public void setup() {
        Region region = new Region();
        region.setName("China");
        region.setPath("China");
        region.setScopeCode(RegionScope.COUNTRY.getCode());
        region.setStatus(RegionAdminStatus.ACTIVE.getCode());
        region.setLevel(0);
        this.regionProvider.createRegion(region);
        this.newRecordIds.add(region.getId());
        
        Region region2 = new Region();
        region2.setParentId(region.getParentId());
        region2.setName("Beijing");
        region2.setPath("China/Beijing");
        region2.setScopeCode(RegionScope.CITY.getCode());
        region2.setStatus(RegionAdminStatus.ACTIVE.getCode());
        region2.setLevel(1);
        this.regionProvider.createRegion(region2);
        this.newRecordIds.add(region2.getId());

        region2 = new Region();
        region2.setParentId(region.getParentId());
        region2.setName("Shanghai");
        region2.setPath("China/Shanghai");
        region2.setScopeCode(RegionScope.CITY.getCode());
        region2.setStatus(RegionAdminStatus.ACTIVE.getCode());
        region2.setLevel(1);
        this.regionProvider.createRegion(region2);
        this.newRecordIds.add(region2.getId());
        
        region2 = new Region();
        region2.setParentId(region.getParentId());
        region2.setName("Hubei");
        region2.setPath("China/Hubei");
        region2.setScopeCode(RegionScope.PROVINCE.getCode());
        region2.setStatus(RegionAdminStatus.ACTIVE.getCode());
        region2.setLevel(1);
        this.regionProvider.createRegion(region2);
        this.newRecordIds.add(region2.getId());
        
        Region region3 = new Region();
        region3.setParentId(region2.getParentId());
        region3.setName("Wuhan");
        region3.setPath("China/Hubei/Wuhan");
        region3.setScopeCode(RegionScope.CITY.getCode());
        region3.setStatus(RegionAdminStatus.ACTIVE.getCode());
        region3.setLevel(2);
        this.regionProvider.createRegion(region3);
        this.newRecordIds.add(region3.getId());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        List<Region> regions = this.regionProvider.listRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), RegionScope.CITY, null, 
                new Tuple<String, SortOrder>("name", SortOrder.DESC));
        
        for(Region region: regions) {
            System.out.println("region: " + region.toString());
        }
        
        regions = this.regionProvider.listDescendantRegions(0, null, RegionScope.CITY, null, 
                new Tuple<String, SortOrder>("name", SortOrder.DESC));
        
        for(Region region: regions) {
            System.out.println("region: " + region.toString());
        }
    }
    
    @After
    public void tearDown() {
        for(Long id : this.newRecordIds) {
            this.regionProvider.deleteRegionById(id);
        }
    }
}
