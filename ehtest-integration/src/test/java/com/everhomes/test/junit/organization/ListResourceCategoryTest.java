// @formatter:off
package com.everhomes.test.junit.organization;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community.CreateResourceCategoryCommand;
import com.everhomes.rest.community.ListResourceCategoriesRestResponse;
import com.everhomes.rest.community.ListResourceCategoryCommand;
import com.everhomes.rest.community.admin.DeleteResourceCategoryCommand;
import com.everhomes.rest.community.admin.UpdateResourceCategoryCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhResourceCategoriesRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ListResourceCategoryTest extends BaseLoginAuthTestCase {
	
	
    @Before
    public void setUp() {
        super.setUp();
        testCreateResourceCategory();
    }
    
    @Test
    public void testListResourceCategory() {
        Integer namespaceId = 0;
        String userIdentifier = "13798204538";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/community/listResourceCategories";
        Long ownerId = 100750L ;
        String ownerType = "EhOrganizations";
        
        ListResourceCategoryCommand cmd = new ListResourceCategoryCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        
        ListResourceCategoriesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListResourceCategoriesRestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        assertNotNull(response.getResponse());
        assertEquals(4, response.getResponse().size());
    }
    
    public void testCreateResourceCategory() {
        Integer namespaceId = 0;
        String userIdentifier = "13798204538";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/community/createResourceCategory";
        Long ownerId = 100750L ;
        String ownerType = "EhOrganizations";
        String name = "测试qqq";
        
        CreateResourceCategoryCommand cmd = new CreateResourceCategoryCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setName(name);
        
        RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext dslContext = dbProvider.getDslContext();
        SelectQuery<EhResourceCategoriesRecord> query = dslContext.selectQuery(Tables.EH_RESOURCE_CATEGORIES);
        query.addConditions(Tables.EH_RESOURCE_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_RESOURCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_RESOURCE_CATEGORIES.NAME.eq(name));
        query.addConditions(Tables.EH_RESOURCE_CATEGORIES.STATUS.eq((byte)2));
        EhResourceCategoriesRecord record = query.fetchOne();
        
        assertNotNull(record);
        assertTrue(name.equals(record.getName()));
        
        /*----------------update------------*/
        commandRelativeUri = "/community/updateResourceCategory";
        name = "测试qqqqqq";
        
        UpdateResourceCategoryCommand cmd2 = new UpdateResourceCategoryCommand();
        cmd2.setId(record.getId());
        cmd2.setName(name);
        
        response = httpClientService.restGet(commandRelativeUri, cmd2, RestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        SelectQuery<EhResourceCategoriesRecord> query2 = dslContext.selectQuery(Tables.EH_RESOURCE_CATEGORIES);
        query2.addConditions(Tables.EH_RESOURCE_CATEGORIES.OWNER_ID.eq(ownerId));
        query2.addConditions(Tables.EH_RESOURCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query2.addConditions(Tables.EH_RESOURCE_CATEGORIES.NAME.eq(name));
        query.addConditions(Tables.EH_RESOURCE_CATEGORIES.STATUS.eq((byte)2));
        EhResourceCategoriesRecord record2 = query2.fetchOne();
        
        assertNotNull(record2);
        assertTrue(name.equals(record2.getName()));
        
        /*----------------delete------------*/
        commandRelativeUri = "/community/deleteResourceCategory";
        
        DeleteResourceCategoryCommand cmd3 = new DeleteResourceCategoryCommand();

        cmd3.setId(record2.getId());
        
        response = httpClientService.restGet(commandRelativeUri, cmd3, RestResponse.class, context);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        SelectQuery<EhResourceCategoriesRecord> query3 = dslContext.selectQuery(Tables.EH_RESOURCE_CATEGORIES);
        query3.addConditions(Tables.EH_RESOURCE_CATEGORIES.OWNER_ID.eq(ownerId));
        query3.addConditions(Tables.EH_RESOURCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query3.addConditions(Tables.EH_RESOURCE_CATEGORIES.ID.eq(record2.getId()));
        query3.addConditions(Tables.EH_RESOURCE_CATEGORIES.STATUS.eq((byte)2));
        EhResourceCategoriesRecord record3 = query3.fetchOne();
        
        assertNull(record3);
    }
    

    
    
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
        String userInfoFilePath = "data/json/resource-category-test-data_161108.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
}

