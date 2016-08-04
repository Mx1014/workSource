// @formatter:off
package com.everhomes.test.junit.journal;


import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.journal.CreateJournalCommand;
import com.everhomes.rest.journal.GetJournalCommand;
import com.everhomes.rest.journal.GetJournalConfigCommand;
import com.everhomes.rest.journal.GetJournalConfigRestResponse;
import com.everhomes.rest.journal.GetJournalRestResponse;
import com.everhomes.rest.journal.JournalStatus;
import com.everhomes.rest.journal.ListJournalsCommand;
import com.everhomes.rest.journal.ListJournalsRestResponse;
import com.everhomes.rest.journal.UpdateJournalCommand;
import com.everhomes.rest.journal.UpdateJournalConfigCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhJournalConfigsRecord;
import com.everhomes.server.schema.tables.records.EhJournalsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class JournalTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListJournals() {
    	
    	Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        ListJournalsCommand cmd = new ListJournalsCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setKeyword("测试2");
        
        String commandRelativeUri = "/journal/listJournals";
        ListJournalsRestResponse response = httpClientService.restPost(commandRelativeUri, cmd, ListJournalsRestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhJournalsRecord> query = context.selectQuery(Tables.EH_JOURNALS);
		query.addConditions(Tables.EH_JOURNALS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_JOURNALS.STATUS.eq(JournalStatus.INACTIVE.getCode()));
		Result<EhJournalsRecord> result = query.fetch();
        
		assertNotNull(result);
		assertEquals(1, result.size());
        
    }
    
    public void testCreateJournal() {
    	
    	Integer namespaceId = 0;
        
        CreateJournalCommand cmd = new CreateJournalCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setTitle("test");
        cmd.setContentType((byte)1);
        cmd.setContent("www.baidu.com");
        cmd.setCoverUri("c://123123123");
        
        String commandRelativeUri = "/journal/createJournal";
        RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhJournalsRecord> query = context.selectQuery(Tables.EH_JOURNALS);
		query.addConditions(Tables.EH_JOURNALS.TITLE.eq("test"));
		Result<EhJournalsRecord> result = query.fetch();
		assertEquals(1, result.size());

    }
    
    public void testUpdateJournal() {
    	
    	Integer namespaceId = 0;
        
        UpdateJournalCommand cmd = new UpdateJournalCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setId(2L);
        cmd.setTitle("test");
        cmd.setContentType((byte)1);
        cmd.setContent("www.baidu.com111");
        cmd.setCoverUri("c://123123123");
        
        String commandRelativeUri = "/journal/updateJournal";
        RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
    }
    
    public void testGetJournal() {
    	
    	Integer namespaceId = 0;
        
    	GetJournalCommand cmd = new GetJournalCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setId(2L);
        String commandRelativeUri = "/journal/getJournal";
        GetJournalRestResponse response = httpClientService.restPost(commandRelativeUri, cmd, GetJournalRestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
		assertEquals("www.baidu.com111", response.getResponse().getContent());
		assertEquals("c://123123123", response.getResponse().getCoverUri());
		assertEquals("test", response.getResponse().getTitle());
    }
    
    public void testdeleteJournal() {
    	
    	Integer namespaceId = 0;
        Long id = 2L;
    	GetJournalCommand cmd = new GetJournalCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setId(id);
        String commandRelativeUri = "/journal/deleteJournal";
        RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhJournalsRecord> query = context.selectQuery(Tables.EH_JOURNALS);
		query.addConditions(Tables.EH_JOURNALS.ID.eq(id));
		query.addConditions(Tables.EH_JOURNALS.STATUS.eq(JournalStatus.ACTIVE.getCode()));

		
		assertEquals(0, query.fetchCount());
    }
    
    public void testGetJournalConfig() {
    	
    	Integer namespaceId = 0;
        GetJournalConfigCommand cmd = new GetJournalConfigCommand();
        cmd.setNamespaceId(namespaceId);
        String commandRelativeUri = "/journal/getJournalConfig";
        GetJournalConfigRestResponse response = httpClientService.restPost(commandRelativeUri, cmd, GetJournalConfigRestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhJournalConfigsRecord> query = context.selectQuery(Tables.EH_JOURNAL_CONFIGS);
		query.addConditions(Tables.EH_JOURNAL_CONFIGS.NAMESPACE_ID.eq(namespaceId));
		
		assertEquals(response.getResponse().getId(), query.fetchOne().getId());
    }
    public void testUpdateJournalConfig() {
    	
    	Integer namespaceId = 0;
    	UpdateJournalConfigCommand cmd = new UpdateJournalConfigCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setId(1L);
        cmd.setDescription("测试");
        String commandRelativeUri = "/journal/updateJournalConfig";
        RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
      
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhJournalConfigsRecord> query = context.selectQuery(Tables.EH_JOURNAL_CONFIGS);
		query.addConditions(Tables.EH_JOURNAL_CONFIGS.NAMESPACE_ID.eq(namespaceId));
		
		assertEquals("测试", query.fetchOne().getDescription());
    }    
    @After
    public void tearDown() {
    	testCreateJournal();
    	testUpdateJournal();
    	testGetJournal();
    	testdeleteJournal();
    	super.tearDown();
        logoff();
    }
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/journal-test-data-journal_160804.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

