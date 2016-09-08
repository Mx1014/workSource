// @formatter:off
package com.everhomes.test.junit.pmtask;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryRestResponse;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.records.EhCategoriesRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class ListTaskCategoriesTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
        
        testCreateCategory();
    }
    
    @Test
    public void testListTaskCategories() {
        String keyword = "";
        Long pageAnchor = null;
        Integer pageSize = 5;
        Long parentId = null;
        
        String userIdentifier = "13760240661";
        String plainTexPassword = "123456";
        Integer namespaceId = 999991;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        ListTaskCategoriesCommand cmd = new ListTaskCategoriesCommand();
        cmd.setNamespaceId(namespaceId);
//        cmd.setParentId(parentId);
        cmd.setKeyword(keyword);
       
        cmd.setPageAnchor(pageAnchor);
        cmd.setPageSize(pageSize);
        
        String commandRelativeUri = "/pmtask/listTaskCategories";
        ListTaskCategoriesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListTaskCategoriesRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
		SelectQuery<EhCategoriesRecord> query = context.selectQuery(Tables.EH_CATEGORIES);
    	
		if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_CATEGORIES.ID.gt(pageAnchor));
		if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_CATEGORIES.NAME.like("%"+keyword+"%"));
		
        if(null != namespaceId)
        	query.addConditions(Tables.EH_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        if(null != parentId)
        	query.addConditions(Tables.EH_CATEGORIES.PARENT_ID.eq(parentId));
       
    	query.addConditions(Tables.EH_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_CATEGORIES.ID.asc());
        	query.addLimit(pageSize);
        
        List<EhCategories> result =  query.fetch().map(r -> 
			ConvertHelper.convert(r, EhCategories.class));
        List<CategoryDTO> list = response.getResponse().getRequests();
        assertEquals(list.size(), result.size());
        
    }
    
    public void testCreateCategory() {
        String keyword = "";
        Long pageAnchor = null;
        Integer pageSize = 5;
        Long parentId = null;
        
        String userIdentifier = "13760240661";
        String plainTexPassword = "123456";
        Integer namespaceId = 999991;
        // 登录时不传namepsace，默认为左邻域空间
//        logon(namespaceId, userIdentifier, plainTexPassword);
        
        CreateTaskCategoryCommand cmd = new CreateTaskCategoryCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setParentId(parentId);
        cmd.setName("报修");
        
        String commandRelativeUri = "/pmtask/createTaskCategory";
        CreateTaskCategoryRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, CreateTaskCategoryRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext dslcontext = dbProvider.getDslContext();
		SelectQuery<EhCategoriesRecord> query = dslcontext.selectQuery(Tables.EH_CATEGORIES);
    	
		query.addConditions(Tables.EH_CATEGORIES.ID.eq(response.getResponse().getId()));
        
		EhCategories result = ConvertHelper.convert(query.fetchOne(), EhCategories.class);
       
		CategoryDTO dto = response.getResponse();
		
        assertEquals(dto.getName(), result.getName());
        
        DeleteTaskCategoryCommand cmd2 = new DeleteTaskCategoryCommand();
        cmd2.setNamespaceId(namespaceId);
        cmd2.setId(dto.getId());
        String commandRelativeUri2 = "/pmtask/deleteTaskCategory";
        RestResponse response2 = httpClientService.restGet(commandRelativeUri2, cmd2, RestResponse.class,context);
        
        SelectQuery<EhCategoriesRecord> query2 = dslcontext.selectQuery(Tables.EH_CATEGORIES);
    	
		query.addConditions(Tables.EH_CATEGORIES.ID.eq(dto.getId()));
        
		EhCategories result2 = ConvertHelper.convert(query.fetchOne(), EhCategories.class);
        assertEquals(result2.getStatus().byteValue(), (byte)0);

    }
    
    @After
    public void tearDown() {
    	super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/repair-test-data-repair_160824.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

