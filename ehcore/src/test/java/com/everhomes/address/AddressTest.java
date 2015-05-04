// @formatter:off
package com.everhomes.address;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.Tuple;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class AddressTest extends TestCase {
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
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
        User user = new User();
        user.setId(10001L);
        UserContext.current().setUser(user);
        communityProvider.createCommunity(community);
        communityCleanupList.add(community);
        
        for(int i = 1; i < 5; i++) {
            Community c = new Community();
            c.setAddress("Fake address " + i);
            c.setAreaName("Fake area " + i);
            c.setStatus(CommunityAdminStatus.ACTIVE.getCode());
            c.setName("Fake " + i);
            c.setCityId(1L);
            c.setAreaId(1L);
            
            communityProvider.createCommunity(c);
            communityCleanupList.add(c);
        }
        
        CommunityGeoPoint geoPoint1 = new CommunityGeoPoint();
        geoPoint1.setCommunityId(community.getId());
        geoPoint1.setDescription("Archor North");
        geoPoint1.setLatitude(0.0);
        geoPoint1.setLongitude(0.0);
        geoPoint1.setGeohash(GeoHashUtils.encode(24.123456, 116.123456));
        communityProvider.createCommunityGeoPoint(geoPoint1);
        this.communityGeopointsCleanupList.add(geoPoint1);
        
        CommunityGeoPoint geoPoint2 = new CommunityGeoPoint();
        geoPoint2.setCommunityId(community.getId());
        geoPoint2.setDescription("Archor South");
        geoPoint2.setLatitude(1.0);
        geoPoint2.setLongitude(1.0);
        communityProvider.createCommunityGeoPoint(geoPoint2);
        this.communityGeopointsCleanupList.add(geoPoint2);
    }
    
    @After
    public void teardown() {
        for(CommunityGeoPoint p : this.communityGeopointsCleanupList) {
            communityProvider.deleteCommunityGeoPoint(p);
        }
        
        for(Community c : this.communityCleanupList) {
            communityProvider.deleteCommunity(c);
        }
    }
    
    @Ignore @Test
    public void testGeoPoints() {
        List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(
            this.communityCleanupList.get(0).getId());
 
        for(CommunityGeoPoint p : points) {
            System.out.println(p.toString());
        }
    }
    
    @Test
    public void testFindNearbyCommuunities() {
        ListNearbyCommunityCommand cmd = new ListNearbyCommunityCommand();
        cmd.setCityId(1L);
        cmd.setLongitude(116.123);
        cmd.setLatigtue(24.123);
        Tuple<Integer, List<CommunityDTO>> communityList = this.addressService.listNearbyCommunities(cmd);
        
        for(CommunityDTO c : communityList.second()) {
            System.out.println(c.toString());
        }
    }
    
    @Ignore @Test
    public void testDbBackedPagination() {
        
        ListCommunityByKeywordCommand cmd = new ListCommunityByKeywordCommand();
        cmd.setKeyword("Fake");
        
        Tuple<Integer, List<CommunityDTO>> results;
        
        for(int page = 1; page <= 5; page++) {
            System.out.println("List community in page " + page);
            cmd.setPageOffset((long)page);
            results = addressService.listCommunitiesByKeyword(cmd);
            for(CommunityDTO c : results.second()) {
                System.out.println("Community: " + c.toString());
            }
        }
    }
    
    @Ignore @Test
    public void testListBuilding() {
        Address addr = new Address();
        addr.setId(1L);
        addr.setCityId(1L);
        addr.setCommunityId(1L);
        addr.setBuildingName("Building 1");
        addr.setBuildingAliasName("Building 1 alias");
        addr.setApartmentName("APT 1");
        this.addressProvider.createAddress(addr);
        
        Address addr2 = new Address();
        addr2.setId(2L);
        addr2.setCityId(1L);
        addr2.setCommunityId(1L);
        addr2.setBuildingName("Building 1");
        addr2.setBuildingAliasName("Building 1 alias");
        addr2.setApartmentName("APT 2");
        this.addressProvider.createAddress(addr2);
        
        Address addr3 = new Address();
        addr3.setId(3L);
        addr3.setCityId(1L);
        addr3.setCommunityId(1L);
        addr3.setBuildingName("Building 2");
        addr3.setBuildingAliasName("Building 2 alias");
        addr3.setApartmentName("APT 1");
        this.addressProvider.createAddress(addr3);
        
        Address addr4 = new Address();
        addr4.setId(4L);
        addr4.setCityId(1L);
        addr4.setCommunityId(1L);
        addr4.setBuildingName("Building 2");
        addr4.setBuildingAliasName("Building 2 alias");
        addr4.setApartmentName("APT 2");
        this.addressProvider.createAddress(addr4);
        
        ListBuildingByKeywordCommand cmd = new ListBuildingByKeywordCommand();
        cmd.setCommunitId(1L);
        cmd.setKeyword("Building");
        Tuple<Integer, List<BuildingDTO>> results = this.addressService.listBuildingsByKeyword(cmd);
        
        System.out.println("List building results: ");
        for(BuildingDTO b : results.second()) {
            System.out.println(b.toString());
        }
        
        this.addressProvider.deleteAddress(addr4);
        this.addressProvider.deleteAddress(addr3);
        this.addressProvider.deleteAddress(addr2);
        this.addressProvider.deleteAddress(addr);
    }
    
    @Test
    public void testListlistAppartments(){
        Address addr = new Address();
        addr.setId(1L);
        addr.setCityId(1L);
        addr.setCommunityId(1L);
        addr.setBuildingName("Building 1");
        addr.setBuildingAliasName("Building 1 alias");
        addr.setApartmentName("APT 1");
        this.addressProvider.createAddress(addr);
        
        Address addr2 = new Address();
        addr2.setId(2L);
        addr2.setCityId(1L);
        addr2.setCommunityId(1L);
        addr2.setBuildingName("Building 1");
        addr2.setBuildingAliasName("Building 1 alias");
        addr2.setApartmentName("APT 2");
        this.addressProvider.createAddress(addr2);
        
        Address addr3 = new Address();
        addr3.setId(3L);
        addr3.setCityId(1L);
        addr3.setCommunityId(1L);
        addr3.setBuildingName("Building 2");
        addr3.setBuildingAliasName("Building 2 alias");
        addr3.setApartmentName("APT 1");
        this.addressProvider.createAddress(addr3);
        
        Address addr4 = new Address();
        addr4.setId(4L);
        addr4.setCityId(1L);
        addr4.setCommunityId(1L);
        addr4.setBuildingName("Building 2");
        addr4.setBuildingAliasName("Building 2 alias");
        addr4.setApartmentName("APT 2");
        this.addressProvider.createAddress(addr4);
        
        ListApartmentByKeywordCommand cmd = new ListApartmentByKeywordCommand();
        cmd.setKeyword("APT");
        cmd.setCommunitId(1L);
        cmd.setBuildingName("Building 1");
        Tuple<Integer, List<String>> results = this.addressService.listApartmentsByKeyword(cmd);
        for(String apt : results.second()){
            System.out.println(apt);
        }
        
        this.addressProvider.deleteAddress(addr4);
        this.addressProvider.deleteAddress(addr3);
        this.addressProvider.deleteAddress(addr2);
        this.addressProvider.deleteAddress(addr);
    }
    
    @Ignore @Test
    public void testListCommunities(){
        Tuple<Integer, List<CommunitySummaryDTO>> result = addressService.listSuggestedCommunities();
        List<CommunitySummaryDTO> list = result.second();
        for(CommunitySummaryDTO dto : list){
            System.out.println(dto);
        }
    }
    
    @Ignore @Test
    public void testClaimAddress(){
        ClaimAddressCommand cmd = new ClaimAddressCommand();
        cmd.setCommunityId(1L);
        cmd.setBuildingName("Building 1");
        cmd.setApartmentName("apt 1");
        addressService.claimAddress(cmd);
    }
    
    @Ignore @Test
    public void testDisclaimAddress(){
        DisclaimAddressCommand cmd = new DisclaimAddressCommand();
        cmd.setAddressId(1L);
        addressService.disclaimAddress(cmd);
    }
}
