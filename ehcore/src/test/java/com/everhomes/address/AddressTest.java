// @formatter:off
package com.everhomes.address;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.region.RegionProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.Tuple;

public class AddressTest extends CoreServerTestCase {
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private RegionProvider regionProvider;
    
    private List<Community> communityCleanupList = new ArrayList<>();
    private List<CommunityGeoPoint> communityGeopointsCleanupList = new ArrayList<>();
    
    //@Before
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
        communityProvider.createCommunity(user.getId(), community);
        communityCleanupList.add(community);
        
        for(int i = 1; i < 5; i++) {
            Community c = new Community();
            c.setAddress("Fake address " + i);
            c.setAreaName("Fake area " + i);
            c.setStatus(CommunityAdminStatus.ACTIVE.getCode());
            c.setName("Fake " + i);
            c.setCityId(1L);
            c.setAreaId(1L);
            
            communityProvider.createCommunity(user.getId(), c);
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
    
    //@After
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
    
    @Ignore @Test
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
    
    @Test
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
        cmd.setCommunityId(1L);
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
    
    @Ignore @Test
    public void testListlistAppartments(){
        Address addr = new Address();
        addr.setCityId(1L);
        addr.setCommunityId(1L);
        addr.setBuildingName("Building 1");
        addr.setBuildingAliasName("Building 1 alias");
        addr.setApartmentName("APT 1");
        this.addressProvider.createAddress(addr);
        
        Address addr2 = new Address();
        addr2.setCityId(1L);
        addr2.setCommunityId(1L);
        addr2.setBuildingName("Building 1");
        addr2.setBuildingAliasName("Building 1 alias");
        addr2.setApartmentName("APT 2");
        this.addressProvider.createAddress(addr2);
        
        Address addr3 = new Address();
        addr3.setCityId(1L);
        addr3.setCommunityId(1L);
        addr3.setBuildingName("Building 2");
        addr3.setBuildingAliasName("Building 2 alias");
        addr3.setApartmentName("APT 1");
        this.addressProvider.createAddress(addr3);
        
        Address addr4 = new Address();
        addr4.setCityId(1L);
        addr4.setCommunityId(1L);
        addr4.setBuildingName("Building 2");
        addr4.setBuildingAliasName("Building 2 alias");
        addr4.setApartmentName("APT 2");
        this.addressProvider.createAddress(addr4);
        
        ListPropApartmentsByKeywordCommand cmd = new ListPropApartmentsByKeywordCommand();
        cmd.setKeyword("APT");
        cmd.setCommunityId(1L);
        cmd.setBuildingName("Building 1");
        Tuple<Integer, List<ApartmentDTO>> results = this.addressService.listApartmentsByKeyword(cmd);
        for(ApartmentDTO apt : results.second()){
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
    
    @Test
    public void testClaimAddress(){
        ClaimAddressCommand cmd = new ClaimAddressCommand();
        cmd.setCommunityId(56L);
        //cmd.setReplacedAddressId(15L);
        cmd.setBuildingName("Building 1");
        cmd.setApartmentName("APT 3");
        ClaimedAddressInfo addressInfo = addressService.claimAddress(cmd);
        assertNotNull(addressInfo);
        System.out.println(addressInfo);
    }
    
    @Ignore @Test
    public void testDisclaimAddress(){
        DisclaimAddressCommand cmd = new DisclaimAddressCommand();
        cmd.setAddressId(16L);
        addressService.disclaimAddress(cmd);
    }
    
    @Test
    public void testListAddressByCommunity(){
        ListAddressCommand cmd = new ListAddressCommand();
        cmd.setCommunityId(56L);
        Tuple<Integer, List<Address>> results = addressService.listAddressByCommunityId(cmd);
        for(Address address : results.second()){
            System.out.println(address);
        }
    }
    
    @Test
    public void testAdminImportCommunity(){
    	Community community  = communityProvider.findCommunityByAreaIdAndName(2897l, "丰盛华庭");
    	System.out.println(community);
     	Community community2  = communityProvider.findCommunityByAreaIdAndName(4150l, "荟芳园");
    	System.out.println(community2);
    }
    
    @Test
    public void testCreateServiceAddress(){
        User user = new User();
        user.setId(10021L);
        UserContext.current().setUser(user);
        CreateServiceAddressCommand cmd = new CreateServiceAddressCommand();
        cmd.setAddress("京基百纳");
        cmd.setAreaId(4150l);
        cmd.setCityId(5636851l);
        AddressDTO addressDTO = addressService.createServiceAddress(cmd);
        System.out.println(addressDTO);
    }
    
//    @Test
//    public void testUserRelateServiceAddress(){
//        User user = new User();
//        user.setId(10021L);
//        UserContext.current().setUser(user);
//        List<AddressDTO> addresses = addressService.getUserRelateServiceAddress();
//        if(addresses != null){
//            addresses.forEach(r ->{
//                System.out.println(r);
//            });
//        }
//    }
    
    @Test
    public void geoHash(){
        List<String> list = new ArrayList<String>();
        list.add("24206890946790527 24206890946790527 NULL             116.464483      39.99117");
        list.add("24206890946790622 24206890946790624 NULL                116.469       39.9972");
        list.add("24206890946790772 24206890946790774 NULL             116.243044     40.218246");
        list.add("24206890946790973 24206890946790975 NULL                116.518       39.9492");
        list.add("24206890946791796 24206890946791798 NULL                116.374       40.0713");
        list.add("24206890946792671 24206890946792673 NULL                 116.62       39.9306");
        list.add("24206890946793325 24206890946793327 NULL                116.247       39.9286");
        list.add("24206890946793330 24206890946793332 NULL                116.245       39.9293");
        list.add("24206890946793735 24206890946793737 NULL                116.708       39.8881");
        list.add("24206890946793740 24206890946793742 NULL                116.705       39.8792");
        list.add("24206890946795339 24206890946795352 NULL        116.2792663574 39.901473999");
        list.add("24206890946796358 24206890946796375 NULL              39.879318    116.703015");
        list.add("24206890946797054 24206890946797067 NULL             116.265314     40.214079");
        list.add("24209300423443704 24209300423443704 NULL             120.371088     30.273112");
        list.add("24209300423443969 24209300423443969 NULL             120.202812     30.358188");
        
        for(String str : list) {
            String[] segs = str.split("\t");
            double latitude = Double.parseDouble(segs[4].trim());
            double longitude = Double.parseDouble(segs[3].trim());
            String geohash = GeoHashUtils.encode(latitude, longitude);
            System.out.println(segs[1] + "\t" + geohash);
        }
        
    }
}
