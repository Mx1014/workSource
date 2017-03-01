package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.SearchOrganizationOwnerCarCommand;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnerCarsRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class SearchOrganizationOwnerCarTest extends BaseLoginAuthTestCase {

    @Autowired
    private SearchProvider searchProvider;

    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testSearchOrganizationOwnerCarByName() {
        bulkData();
        logon();
        String api = "/pm/searchOrganizationOwnerCars";
        SearchOrganizationOwnerCarCommand cmd = new SearchOrganizationOwnerCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setParkingType((byte)1);
        cmd.setKeyword("粤B77777");

        SearchOrganizationOwnerCarsRestResponse resp = httpClientService.restPost(api, cmd, SearchOrganizationOwnerCarsRestResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        assertNotNull(resp.getResponse().getCars());

        assertTrue("The search response list size should be 3.", resp.getResponse().getCars().size() == 3);
    }

    @Test
    public void testSearchOrganizationOwnerByContactToken() {
        bulkData();
        logon();
        String api = "/pm/searchOrganizationOwnerCars";
        SearchOrganizationOwnerCarCommand cmd = new SearchOrganizationOwnerCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setParkingType((byte)1);
        cmd.setKeyword("豪车");

        SearchOrganizationOwnerCarsRestResponse resp = httpClientService.restPost(api, cmd, SearchOrganizationOwnerCarsRestResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        assertNotNull(resp.getResponse().getCars());

        assertTrue("The search response list size should be 1.", resp.getResponse().getCars().size() == 1);
    }

    @Test
    public void testSearchOrganizationOwnerByDifferentCommunity() {
        bulkData2();
        logon();
        String api = "/pm/searchOrganizationOwnerCars";
        SearchOrganizationOwnerCarCommand cmd = new SearchOrganizationOwnerCarCommand();
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setParkingType((byte)1);
        cmd.setKeyword("粤");

        SearchOrganizationOwnerCarsRestResponse resp = httpClientService.restPost(api, cmd, SearchOrganizationOwnerCarsRestResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        assertNotNull(resp.getResponse().getCars());

        assertTrue("The search response list size should be 3. actual is "+resp.getResponse().getCars().size(), resp.getResponse().getCars().size() == 3);
    }

    private void logon() {
        Integer namespaceId = 0;
        String userIdentifier = "12900000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
    }

    @Override
    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        // userInfoFilePath = "data/json/customer-manage-search-owner-car-data.txt";
        userInfoFilePath = "data/json/customer-test-data-170206.json";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    private void bulkData(){
        searchProvider.clearType("organizationOwnerCar");
        String json = "{ \"create\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"organizationOwnerCar\", \"_id\" : \"1\" }}\\n\n" +
                "{ \"id\": 4, \"namespace_id\": 0, \"brand\": \"Mercedes-G500\", \"parking_space\": \"20F\", \"parkingType\": 1, \"plateNumber\": \"粤B77777\", \"contacts\": \"xiaoxiao\", \"contact_number\": \"1111\", \"content_uri\": null, \"color\": \"white\", \"status\": 1, \"create_time\": \"2016-09-09 18:43:27\", \"update_time\": \"2016-09-09 18:43:27\", \"update_uid\": 1001, \"communityId\": 24206890946790405 }\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"organizationOwnerCar\", \"_id\" : \"2\" }}\\n\n" +
                "{ \"id\": 5, \"namespace_id\": 0, \"brand\": \"Mercedes-G500\", \"parking_space\": \"20F\", \"parkingType\": 1, \"plateNumber\": \"粤B66666\", \"contacts\": \"王五\", \"contact_number\": \"2222\", \"content_uri\": null, \"color\": \"white\", \"status\": 1, \"create_time\": \"2016-09-09 18:43:27\", \"update_time\": \"2016-09-09 18:43:27\", \"update_uid\": 1001, \"communityId\": 24206890946790405 }\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"organizationOwnerCar\", \"_id\" : \"3\" }}\\n\n" +
                "{ \"id\": 6, \"namespace_id\": 0, \"brand\": \"Mercedes-G500\", \"parking_space\": \"20F\", \"parkingType\": 1, \"plateNumber\": \"粤B55777\", \"contacts\": \"五百万豪车\", \"contact_number\": \"3333\", \"content_uri\": null, \"color\": \"white\", \"status\": 1, \"create_time\": \"2016-09-09 18:43:27\", \"update_time\": \"2016-09-09 18:43:27\", \"update_uid\": 1001, \"communityId\": 24206890946790405 }\\n\n";
        searchProvider.bulk("organizationOwnerCar", json);
    }

    private void bulkData2(){
        searchProvider.clearType("organizationOwnerCar");
        String json = "{ \"create\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"organizationOwnerCar\", \"_id\" : \"1\" }}\\n\n" +
                "{ \"id\": 4, \"namespace_id\": 0, \"brand\": \"Mercedes-G500\", \"parking_space\": \"20F\", \"parkingType\": 1, \"plateNumber\": \"粤B77777\", \"contacts\": \"xiaoxiao\", \"contact_number\": \"1111\", \"content_uri\": null, \"color\": \"white\", \"status\": 1, \"create_time\": \"2016-09-09 18:43:27\", \"update_time\": \"2016-09-09 18:43:27\", \"update_uid\": 1001, \"communityId\": 24206890946790405 }\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"organizationOwnerCar\", \"_id\" : \"2\" }}\\n\n" +
                "{ \"id\": 5, \"namespace_id\": 0, \"brand\": \"Mercedes-G500\", \"parking_space\": \"20F\", \"parkingType\": 1, \"plateNumber\": \"粤B66666\", \"contacts\": \"王五\", \"contact_number\": \"2222\", \"content_uri\": null, \"color\": \"white\", \"status\": 1, \"create_time\": \"2016-09-09 18:43:27\", \"update_time\": \"2016-09-09 18:43:27\", \"update_uid\": 1001, \"communityId\": 24206890946790405 }\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv3\", \"_type\" : \"organizationOwnerCar\", \"_id\" : \"3\" }}\\n\n" +
                "{ \"id\": 6, \"namespace_id\": 0, \"brand\": \"Mercedes-G500\", \"parking_space\": \"20F\", \"parkingType\": 1, \"plateNumber\": \"粤B55777\", \"contacts\": \"五百万豪车\", \"contact_number\": \"3333\", \"content_uri\": null, \"color\": \"white\", \"status\": 1, \"create_time\": \"2016-09-09 18:43:27\", \"update_time\": \"2016-09-09 18:43:27\", \"update_uid\": 1001, \"communityId\": 1 }\\n\n";
        searchProvider.bulk("organizationOwnerCar", json);
    }
}
