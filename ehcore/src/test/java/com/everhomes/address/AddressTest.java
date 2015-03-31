// @formatter:off
package com.everhomes.address;

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
import com.everhomes.util.Tuple;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class AddressTest extends TestCase {
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private AddressService addressService;
    
    private List<Community> communityCleanupList = new ArrayList<>();
    private List<CommunityGeoPoint> communityGeopointsCleanupList = new ArrayList<>();
    
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
        Community community = new Community();
        community.setAddress("Fake address");
        community.setAreaName("Fake area");
        community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
        community.setName("Fake");
        community.setCityId(1L);
        community.setAreaId(1L);
        
        addressProvider.createCommunity(community);
        communityCleanupList.add(community);
        
        CommunityGeoPoint geoPoint1 = new CommunityGeoPoint();
        geoPoint1.setCommunityId(community.getId());
        geoPoint1.setDescription("Archor North");
        geoPoint1.setLatitude(0.0);
        geoPoint1.setLongitude(0.0);
        addressProvider.createCommunityGeoPoint(geoPoint1);
        this.communityGeopointsCleanupList.add(geoPoint1);
        
        CommunityGeoPoint geoPoint2 = new CommunityGeoPoint();
        geoPoint2.setCommunityId(community.getId());
        geoPoint2.setDescription("Archor South");
        geoPoint2.setLatitude(1.0);
        geoPoint2.setLongitude(1.0);
        addressProvider.createCommunityGeoPoint(geoPoint2);
        this.communityGeopointsCleanupList.add(geoPoint2);
    }
    
    @After
    public void teardown() {
        for(CommunityGeoPoint p : this.communityGeopointsCleanupList) {
            addressProvider.deleteCommunityGeoPoint(p);
        }
        
        for(Community c : this.communityCleanupList) {
            addressProvider.deleteCommunity(c);
        }
    }
    
    @Test
    public void testGeoPoints() {
        List<CommunityGeoPoint> points = addressProvider.listCommunitGeoPoints(
            this.communityCleanupList.get(0).getId());
 
        for(CommunityGeoPoint p : points) {
            System.out.println(p.toString());
        }
    }
    
    @Test
    public void testFindNearbyCommuunities() {
        ListNearbyCommunityCommand cmd = new ListNearbyCommunityCommand();
        cmd.setCityId(1L);
        cmd.setLongitude(1.0);
        cmd.setLatigtue(1.0);
        Tuple<Integer, List<CommunityDTO>> communityList = this.addressService.listNearbyCommunities(cmd);
        
        for(CommunityDTO c : communityList.second()) {
            System.out.println(c.toString());
        }
    }
}
