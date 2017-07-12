package com.everhomes.test.junit.equipment;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentParameterDTO;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentTaskProcessResult;
import com.everhomes.rest.equipment.EquipmentTaskResult;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ListLogsByTaskIdCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdRestResponse;
import com.everhomes.rest.equipment.ListParametersByEquipmentIdRestResponse;
import com.everhomes.rest.equipment.ReportEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReportEquipmentTaskRestResponse;
import com.everhomes.rest.equipment.ReviewEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class EquipmentTaskTest extends BaseLoginAuthTestCase {
	private static final String REPORT_EQUIPMENT_TASK_URI = "/equipment/reportEquipmentTask";
	private static final String REVIEW_EQUIPMENT_TASK_URI = "/equipment/reviewEquipmentTask";
	private static final String LIST_EQUIPMENT_TASK_LOG_URI = "/equipment/listLogsByTaskId";
	private static final String LIST_EQUIPMENT_TASK_URI = "/equipment/listEquipmentTasks";
	private static final String SEARCH_EQUIPMENT_TASK_URI = "/equipment/searchEquipmentTasks";
	private static final String EXPORT_EQUIPMENT_TASK_URI = "/equipment/exportEquipmentTasks";
	private static final String LIST_EQUIPMENT_PARAMETERS_URI = "/equipment/listParametersByEquipmentId";
	
	Integer namespaceId = 999992;
	String userIdentifier = "19112349996";
	String plainTexPassword = "123456";
	
	Long equipmentId = 2L;
	
	Long ownerId = (long) 1000750;
	String ownerType = "PM";
	
	@Before
    public void setUp() {
        super.setUp();
    }
	
	@Test
	public void testReportEquipmentTask() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	DeleteEquipmentsCommand command = new DeleteEquipmentsCommand();
	    	command.setEquipmentId(equipmentId);
	    	command.setOwnerId(ownerId);
	    	command.setOwnerType(ownerType);
	    	ListParametersByEquipmentIdRestResponse paras = httpClientService.restGet(LIST_EQUIPMENT_PARAMETERS_URI, command, 
	    			ListParametersByEquipmentIdRestResponse.class, context);
	    	assertNotNull("The reponse of may not be null", paras);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(paras), httpClientService.isReponseSuccess(paras));
	    	assertNotNull(paras.getResponse());
	    	
	    	
	    	ReportEquipmentTaskCommand cmd = new ReportEquipmentTaskCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setMessage("dggttrbn");
	    	cmd.setTaskId(1L);
	    	cmd.setVerificationResult(EquipmentTaskResult.COMPLETE_OK.getCode());
	    	
	    	ReportEquipmentTaskRestResponse response = httpClientService.restGet(REPORT_EQUIPMENT_TASK_URI, cmd, 
	    			ReportEquipmentTaskRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals(EquipmentTaskStatus.CLOSE.getCode(), response.getResponse().getStatus().byteValue());
	    	assertEquals(EquipmentTaskResult.COMPLETE_DELAY.getCode(), response.getResponse().getResult().byteValue());
	    	
	}
	
	@Test
	public void testReviewEquipmentTask() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	ReviewEquipmentTaskCommand cmd = new ReviewEquipmentTaskCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setTaskId(2L);
	    	cmd.setReviewResult(ReviewResult.QUALIFIED.getCode());
	    	
	    	httpClientService.restGet(REVIEW_EQUIPMENT_TASK_URI, cmd, StringRestResponse.class, context);
	    	
	    	
	    	ListLogsByTaskIdCommand command = new ListLogsByTaskIdCommand();
	    	command.setTaskId(2L);
	    	command.setOwnerId(ownerId);
	    	command.setOwnerType(ownerType);
	    	ListLogsByTaskIdRestResponse response = httpClientService.restGet(LIST_EQUIPMENT_TASK_LOG_URI, 
	    			command, ListLogsByTaskIdRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals(1, response.getResponse().getLogs().size());
	    	assertEquals(ProcessType.REVIEW, ProcessType.fromStatus(response.getResponse().getLogs().get(0).getProcessType()));
	    	assertEquals(EquipmentTaskProcessResult.REVIEW_QUALIFIED, EquipmentTaskProcessResult.fromStatus(response.getResponse().getLogs().get(0).getProcessResult()));
	}
	
	@After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    	
        userInfoFilePath = "data/json/equipment-test-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
