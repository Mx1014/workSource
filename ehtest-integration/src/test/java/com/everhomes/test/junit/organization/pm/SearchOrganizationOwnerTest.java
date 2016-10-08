package com.everhomes.test.junit.organization.pm;

import com.everhomes.rest.organization.pm.SearchOrganizationOwnersCommand;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnersRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class SearchOrganizationOwnerTest extends BaseLoginAuthTestCase {

    @Autowired
    private SearchProvider searchProvider;

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testSearchOrganizationOwnerByName() {
        bulkData();
        logon();
        String api = "/pm/searchOrganizationOwners";
        SearchOrganizationOwnersCommand cmd = new SearchOrganizationOwnersCommand();
        cmd.setOrgOwnerTypeId(1L);
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setKeyword("王五");

        SearchOrganizationOwnersRestResponse resp = httpClientService.restPost(api, cmd, SearchOrganizationOwnersRestResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        assertNotNull(resp.getResponse().getOwners());
        assertEquals(1, resp.getResponse().getOwners().size());

        assertEquals(cmd.getKeyword(), resp.getResponse().getOwners().get(0).getContactName());
    }

    @Test
    public void testSearchOrganizationOwnerByContactToken() {
        bulkData();
        logon();
        String api = "/pm/searchOrganizationOwners";
        SearchOrganizationOwnersCommand cmd = new SearchOrganizationOwnersCommand();
        cmd.setOrgOwnerTypeId(1L);
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setKeyword("12345678911");

        SearchOrganizationOwnersRestResponse resp = httpClientService.restPost(api, cmd, SearchOrganizationOwnersRestResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        assertNotNull(resp.getResponse().getOwners());
        assertEquals(2, resp.getResponse().getOwners().size());
    }

    @Test
    public void testSearchOrganizationOwnerByContactToken2() {
        bulkData2();
        logon();
        String api = "/pm/searchOrganizationOwners";
        SearchOrganizationOwnersCommand cmd = new SearchOrganizationOwnersCommand();
        cmd.setOrgOwnerTypeId(1L);
        cmd.setOrganizationId(1000001L);
        cmd.setCommunityId(24206890946790405L);
        cmd.setKeyword("12345678911");

        SearchOrganizationOwnersRestResponse resp = httpClientService.restPost(api, cmd, SearchOrganizationOwnersRestResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        assertNotNull(resp.getResponse().getOwners());
        assertEquals(1, resp.getResponse().getOwners().size());

        assertEquals(cmd.getKeyword(), resp.getResponse().getOwners().get(0).getContactToken());
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
        userInfoFilePath = "data/json/customer-manage-search-owner-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }

    private void bulkData(){
        searchProvider.clearType("pmowner");
        String json = "{ \"create\" : { \"_index\" : \"everhomesv31x\", \"_type\" : \"pmowner\", \"_id\" : \"1\" }}\\n\n" +
                "{\"id\": 1,\"organization_id\": 1000001,\"contactName\": \"张三\",\"contact_type\": 0,\"contactToken\": \"13800138000\",\"contact_description\": null,\"address_id\": 1,\"address\": null,\"creator_uid\": 1001,\"create_time\": \"2016-09-05 10:00:43\",\"namespace_id\": 0,\"orgOwnerTypeId\": 1,\"communityId\": 24206890946790405}\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv31x\", \"_type\" : \"pmowner\", \"_id\" : \"2\" }}\\n\n" +
                "{\"id\": 2,\"organization_id\": 1000001,\"contactName\": \"李四\",\"contact_type\": 0,\"contactToken\": \"12345678911\",\"contact_description\": null,\"address_id\": 1,\"address\": null,\"creator_uid\": 1001,\"create_time\": \"2016-09-05 10:00:43\",\"namespace_id\": 0,\"orgOwnerTypeId\": 1,\"communityId\": 24206890946790405}\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv31x\", \"_type\" : \"pmowner\", \"_id\" : \"3\" }}\\n\n" +
                "{\"id\": 2,\"organization_id\": 1000001,\"contactName\": \"王五\",\"contact_type\": 0,\"contactToken\": \"12345678911\",\"contact_description\": null,\"address_id\": 1,\"address\": null,\"creator_uid\": 1001,\"create_time\": \"2016-09-05 10:00:43\",\"namespace_id\": 0,\"orgOwnerTypeId\": 1,\"communityId\": 24206890946790405}\\n\n";
        searchProvider.bulk("pmowner", json);
    }
    private void bulkData2(){
        searchProvider.clearType("pmowner");
        String json = "{ \"create\" : { \"_index\" : \"everhomesv31x\", \"_type\" : \"pmowner\", \"_id\" : \"1\" }}\\n\n" +
                "{\"id\": 1,\"organization_id\": 1000001,\"contactName\": \"张三\",\"contact_type\": 0,\"contactToken\": \"13800138000\",\"contact_description\": null,\"address_id\": 1,\"address\": null,\"creator_uid\": 1001,\"create_time\": \"2016-09-05 10:00:43\",\"namespace_id\": 0,\"orgOwnerTypeId\": 1,\"communityId\": 24206890946790405}\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv31x\", \"_type\" : \"pmowner\", \"_id\" : \"2\" }}\\n\n" +
                "{\"id\": 2,\"organization_id\": 1000001,\"contactName\": \"李四\",\"contact_type\": 0,\"contactToken\": \"12345678911\",\"contact_description\": null,\"address_id\": 1,\"address\": null,\"creator_uid\": 1001,\"create_time\": \"2016-09-05 10:00:43\",\"namespace_id\": 0,\"orgOwnerTypeId\": 1,\"communityId\": 24206890946790405}\\n\n" +
                "{ \"create\" : { \"_index\" : \"everhomesv31x\", \"_type\" : \"pmowner\", \"_id\" : \"3\" }}\\n\n" +
                "{\"id\": 3,\"organization_id\": 1000001,\"contactName\": \"王五\",\"contact_type\": 0,\"contactToken\": \"12345678911\",\"contact_description\": null,\"address_id\": 1,\"address\": null,\"creator_uid\": 1001,\"create_time\": \"2016-09-05 10:00:43\",\"namespace_id\": 0,\"orgOwnerTypeId\": 2,\"communityId\": 24206890946790405}\\n\n";
        searchProvider.bulk("pmowner", json);
    }

    @Override
    public void tearDown() {
        logoff();
    }
}
