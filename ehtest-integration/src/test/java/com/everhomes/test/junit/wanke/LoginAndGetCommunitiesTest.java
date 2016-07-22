// @formatter:off
package com.everhomes.test.junit.wanke;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.db.AccessSpec;
import com.everhomes.rest.wanke.ListCommunityCommand;
import com.everhomes.rest.wanke.ListCommunityServiceCommand;
import com.everhomes.rest.wanke.ListCommunityServicesRestResponse;
import com.everhomes.rest.wanke.LoginAndGetCommunitiesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.records.EhCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhCommunityServicesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class LoginAndGetCommunitiesTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testLoginAndGetCommunities() {
        Long pageAnchor = 0L;
        Integer pageSize = 5;
        
        ListCommunityCommand cmd = new ListCommunityCommand();
        cmd.setToken("");
        cmd.setNamespaceId(0);
        cmd.setType("wanke");
        cmd.setPageAnchor(pageAnchor);
        cmd.setPageSize(pageSize);
        cmd.setDebugged(true);
        
        String commandRelativeUri = "/serviceConf/loginAndGetCommunities";
        LoginAndGetCommunitiesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, LoginAndGetCommunitiesRestResponse.class);
       
        assertNotNull("The reponse of getting community services may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
    	SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
    	query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ORGANIZATION_TOKEN.eq(""));
    	EhOrganizationsRecord record = query.fetchOne();

    	SelectQuery<EhOrganizationCommunitiesRecord> query1 = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(record.getId()));
		List<EhOrganizationCommunitiesRecord> list = query1.fetch();
		EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
		List<EhCommunities> communities = new ArrayList<EhCommunities>();
		for(EhOrganizationCommunitiesRecord r:list){
			EhCommunities community = dao.findById(r.getCommunityId());
			if(null != community)
				communities.add(community);
		}
        assertEquals(response.getResponse().getCommunities().size(), communities.size());
        
    }
    
    @After
    public void tearDown() {
    	super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/3.7.x-test-data-wanke_160721.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

