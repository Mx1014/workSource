package com.everhomes.test.junit.quality;



import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.quality.CreatQualityStandardCommand;
import com.everhomes.rest.quality.CreatQualityStandardRestResponse;
import com.everhomes.rest.quality.CreateQualityInspectionTaskCommand;
import com.everhomes.rest.quality.CreateQualityInspectionTaskRestResponse;
import com.everhomes.rest.quality.ListQualityCategoriesCommand;
import com.everhomes.rest.quality.ListQualityCategoriesRestResponse;
import com.everhomes.rest.quality.ListQualityInspectionLogsCommand;
import com.everhomes.rest.quality.ListQualityInspectionLogsRestResponse;
import com.everhomes.rest.quality.ListQualityInspectionTasksCommand;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.ListQualityInspectionTasksRestResponse;
import com.everhomes.rest.quality.ListUserRelateOrgGroupsRestResponse;
import com.everhomes.rest.quality.StandardGroupDTO;
import com.everhomes.rest.quality.UpdateQualityCategoryCommand;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class QualityTest extends BaseLoginAuthTestCase {
	
	Integer namespaceId = 999992;
	String userIdentifier = "19112349996";
	String plainTexPassword = "123456";
	
	Long ownerId = (long) 1000750;
	String ownerType = "PM";
	
	@Before
    public void setUp() {
        super.setUp();
    }
    
	@Test
    public void testQualityCategories() {

        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        UpdateQualityCategoryCommand cmd = new UpdateQualityCategoryCommand();
    	
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(ownerType);
    	cmd.setName("类型1");
    	cmd.setScore(20.0);
    	cmd.setDescription("description");
    	
    	String commandRelativeUri = "/quality/updateQualityCategory";
    	StringRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
    			StringRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        ListQualityCategoriesCommand command = new ListQualityCategoriesCommand();
        command.setOwnerId(ownerId);
        command.setOwnerType(ownerType);
    	String commandUri = "/quality/listQualityCategories";
    	ListQualityCategoriesRestResponse restresponse = httpClientService.restGet(commandUri, command, 
    			ListQualityCategoriesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", restresponse);
        assertTrue("response=" + 
            StringHelper.toJsonString(restresponse), httpClientService.isReponseSuccess(restresponse));
        assertNotNull(restresponse.getResponse());
        assertEquals(2, restresponse.getResponse().getCategories().size());
    }
	
	 @Test
	 public void testListUserRelateOrgGroups() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	String commandRelativeUri = "/quality/listUserRelateOrgGroups";
	    	ListUserRelateOrgGroupsRestResponse response = httpClientService.restGet(commandRelativeUri, null, 
	    			ListUserRelateOrgGroupsRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals(2, response.getResponse().size());
	    }
    
	@Test
    public void testQualityInspectionLogs () {
    	
    	// 登录时不传namepsace，默认为左邻域空间
    	logon(999992, userIdentifier, plainTexPassword);
    	
    	CreatQualityStandardCommand command = new CreatQualityStandardCommand();
    	command.setOwnerId(ownerId);
    	command.setOwnerType(ownerType);
//    	command.setCategoryId(1L);
    	command.setDescription("feferfre");
    	command.setName("test");
    	command.setStandardNumber("test-001");
    	RepeatSettingsDTO repeat = new RepeatSettingsDTO();
    	repeat.setEndDate(1467216000000L);
    	repeat.setStartDate(1465315200000L);
    	repeat.setForeverFlag((byte) 0);
    	repeat.setOwnerId(ownerId);
    	repeat.setOwnerType(ownerType);
    	repeat.setRepeatType((byte) 1);
    	repeat.setTimeRanges("{\\\"ranges\\\":[{\\\"startTime\\\":\\\"14:00:00\\\",\\\"endTime\\\":\\\"17:00:00\\\",\\\"duration\\\":\\\"180m\\\"}]}");
    	repeat.setExpression("{\\\"expression\\\":[]}");
    	
    	command.setRepeat(repeat);
    	
    	List<StandardGroupDTO> group = new ArrayList<StandardGroupDTO>();
    	StandardGroupDTO group1 = new StandardGroupDTO();
    	group1.setGroupId(1000754L);
    	group1.setGroupName("执行二队");
    	group1.setGroupType((byte) 1);
    	group1.setInspectorUid(211304L);
    	group.add(group1);
    	StandardGroupDTO group2 = new StandardGroupDTO();
    	group2.setGroupId(1000753L);
    	group2.setGroupName("执行一队");
    	group2.setGroupType((byte) 2);
    	group.add(group2);
    	
    	command.setGroup(group);
    	
    	String commandUri = "/quality/creatQualityStandard";
    	CreatQualityStandardRestResponse restresponse = httpClientService.restGet(commandUri, command, 
    			CreatQualityStandardRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", restresponse);
        assertTrue("response=" + 
            StringHelper.toJsonString(restresponse), httpClientService.isReponseSuccess(restresponse));
    	
    	
    	
    	ListQualityInspectionLogsCommand cmd = new ListQualityInspectionLogsCommand();
    	String targetType = "standard";
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(ownerType);
    	cmd.setTargetType(targetType);
    	
    	String commandRelativeUri = "/quality/listQualityInspectionLogs";
    	ListQualityInspectionLogsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
    			ListQualityInspectionLogsRestResponse.class, context);
    	
    	assertNotNull("The reponse of may not be null", response);
    	assertTrue("response=" + 
    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    	assertNotNull(response.getResponse());
    	assertEquals(1, response.getResponse().getDtos().size());
    }
    
	@Test
    public void testQualityTasks() {
    	
    	// 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        CreateQualityInspectionTaskCommand cmd = new CreateQualityInspectionTaskCommand();
        StandardGroupDTO group = new StandardGroupDTO();
        group.setGroupId(1000753L);
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(ownerType);
    	cmd.setName("任务");
//    	cmd.setCategoryId(1L);
    	cmd.setGroup(group);
    	cmd.setExecutiveExpireTime(1469504822000L);
    	
    	String commandRelativeUri = "/quality/createQualityInspectionTask";
    	CreateQualityInspectionTaskRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
    			CreateQualityInspectionTaskRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        ListQualityInspectionTasksCommand command = new ListQualityInspectionTasksCommand();
        command.setOwnerId(ownerId);
        command.setOwnerType(ownerType);
        command.setTaskType((byte) 1);
    	String commandUri = "/quality/listQualityInspectionTasks";
    	ListQualityInspectionTasksRestResponse restresponse = httpClientService.restGet(commandUri, command, 
    			ListQualityInspectionTasksRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", restresponse);
        assertTrue("response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(restresponse));
        assertNotNull(restresponse.getResponse());
        assertEquals(1, restresponse.getResponse().getTasks().size());
    }
    
    
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    	
        userInfoFilePath = "data/json/quality1.1-test-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
