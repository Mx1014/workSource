// @formatter:off
package com.everhomes.test.junit.wanke;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.wanke.ListCommunityServiceCommand;
import com.everhomes.rest.wanke.ListCommunityServicesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhCommunityServicesRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

public class ListCommunityServicesTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListCommunityServices() {
    	String ownerType = "community";
        Long ownerId = 240111044331051460L;
        Byte scopeCode = 1;
        Long scopeId = 1L;
        Long pageAnchor = 0L;
        Integer pageSize = 5;
        
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        ListCommunityServiceCommand cmd = new ListCommunityServiceCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setScopeCode(scopeCode);
        cmd.setScopeId(scopeId);
        cmd.setPageAnchor(pageAnchor);
        cmd.setPageSize(pageSize);
        
        String commandRelativeUri = "/serviceConf/listCommunityServices";
        ListCommunityServicesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListCommunityServicesRestResponse.class,context);
       
        assertNotNull("The reponse of getting community services may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
    	SelectQuery<EhCommunityServicesRecord> query = context.selectQuery(Tables.EH_COMMUNITY_SERVICES);
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.OWNER_ID.eq(ownerId));
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.OWNER_TYPE.eq(ownerType));
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.SCOPE_CODE.eq(scopeCode));
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.SCOPE_ID.eq(scopeId));
    		query.addConditions(Tables.EH_COMMUNITY_SERVICES.ID.gt(pageAnchor));
    		query.addOrderBy(Tables.EH_COMMUNITY_SERVICES.ID.desc());
    		query.addLimit(pageSize);
    	
    	List<EhCommunityServicesRecord> result = query.fetch();
        assertEquals(response.getResponse().getRequests().size(), result.size());
        
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

