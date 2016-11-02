// @formatter:off
package com.everhomes.test.junit.pmtask;


import java.util.ArrayList;
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
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CompleteTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskByUserRestResponse;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryRestResponse;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesRestResponse;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.server.schema.tables.records.EhCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhPmTasksRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class CreateTaskTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
        
        testCreateCategory();
    }
    
    @Test
    public void testCreateTask() {
    	String ownerType = "community";
        Long ownerId = 240111044331053517L;
    	Long categoryId = 200890L;
    	String address = "南山区科技园";
    	String content = "厨房水龙头坏了";
    	String nickName;
    	String mobile;
        List<String> attachments = new ArrayList<String>();
        attachments.add("cs://1/image/aW1hZ2UvTVRvek5XSTVNell4TTJRME0yTXlaRFZsT1RZeE1HTTBOVGxrWWpJeFpHTmpNUQ");
    	
        String userIdentifier = "13760240661";
        String plainTexPassword = "123456";
        Integer namespaceId = 999991;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        CreateTaskCommand cmd = new CreateTaskCommand();
        cmd.setOwnerType(ownerType);
        cmd.setOwnerId(ownerId);
        cmd.setCategoryId(categoryId);
        cmd.setContent(content);
        cmd.setAddress(address);
        
        String commandRelativeUri = "/pmtask/createTask";
        CreateTaskByUserRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, CreateTaskByUserRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext dslcontext = dbProvider.getDslContext();
		SelectQuery<EhPmTasksRecord> query = dslcontext.selectQuery(Tables.EH_PM_TASKS);
    	
        query.addConditions(Tables.EH_PM_TASKS.ID.eq(response.getResponse().getId()));
       
        EhPmTasks result = ConvertHelper.convert(query.fetchOne(), EhPmTasks.class);
        
        PmTaskDTO dto = response.getResponse();
        
        assertEquals(result.getContent(), dto.getContent());
        assertEquals(result.getStatus().byteValue(), (byte)1);
        //分配任务
        AssignTaskCommand cmd2 = new AssignTaskCommand();
        cmd2.setOwnerType(ownerType);
        cmd2.setOwnerId(ownerId);
        cmd2.setId(dto.getId());
        cmd2.setTargetId(1205L);
        
        String commandRelativeUri2 = "/pmtask/assignTask";
        RestResponse response2 = httpClientService.restGet(commandRelativeUri2, cmd2, RestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response2);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response2), httpClientService.isReponseSuccess(response2));
        
        EhPmTasks result2 = ConvertHelper.convert(query.fetchOne(), EhPmTasks.class);
        assertEquals(result2.getContent(), dto.getContent());
        assertEquals(result2.getStatus().byteValue(), (byte)2);
        
        
        //完成任务
        CompleteTaskCommand cmd3 = new CompleteTaskCommand();
        cmd3.setOwnerType(ownerType);
        cmd3.setOwnerId(ownerId);
        cmd3.setId(dto.getId());
        cmd3.setContent("测试测试测试");
        
        String commandRelativeUri3 = "/pmtask/completeTask";
        RestResponse response3 = httpClientService.restGet(commandRelativeUri3, cmd3, RestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response3);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response3), httpClientService.isReponseSuccess(response3));
        
        EhPmTasks result3 = ConvertHelper.convert(query.fetchOne(), EhPmTasks.class);
        assertEquals(result3.getContent(), dto.getContent());
        assertEquals(result3.getStatus().byteValue(), (byte)3);
        
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

