package com.everhomes.test.general_approval;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalCategoryRestResponse;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalSupportType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.admin.General_approvalCreateApprovalFormRestResponse;
import com.everhomes.rest.general_approval.admin.General_approvalCreateGeneralApprovalRestResponse;
import com.everhomes.rest.general_approval.admin.General_approvalGetApprovalFormRestResponse;
import com.everhomes.rest.general_approval.admin.General_approvalListApprovalFormsRestResponse;
import com.everhomes.rest.general_approval.admin.General_approvalUpdateApprovalFormRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class GeneralApprovalTest extends BaseLoginAuthTestCase {

	private static final String CREATEAPPROVALFORM_URL = "/admin/general_approval/createApprovalForm";
	private static final String UPDATEAPPROVALFORM_URL = "/admin/general_approval/updateApprovalForm";
	private static final String GETAPPROVALFORM_URL = "/admin/general_approval/getApprovalForm";
	private static final String LISTAPPROVALFORMS_URL = "/admin/general_approval/listApprovalForms";
	private static final String DELETEAPPROVALFORMBYID_URL = "/admin/general_approval/deleteApprovalFormById";
	private static final String CREATEGENERALAPPROVAL_URL = "/admin/general_approval/createGeneralApproval";
	private static final String LISTGENERALAPPROVAL_URL = "/admin/general_approval/listGeneralApproval";
	private static final String UPDATEGENERALAPPROVAL_URL = "/admin/general_approval/updateGeneralApproval";
	private static final String DELETEGENERALAPPROVAL_URL = "/admin/general_approval/deleteGeneralApproval";

	private Long moduleId = 40500L;
	private Long ownerId = 12021L;
	private String ownerType = "COMMUNITY";
	private Long projectId = 12021L;
	private String projectType = "COMMUNITY";
	private String moduleType = FlowModuleType.NO_MODULE.getCode();
	private Long organizationId = 1000750L;

	public GeneralFormDTO testCreateApprovalForm() {
 
		CreateApprovalFormCommand cmd = new CreateApprovalFormCommand();
		cmd.setModuleId(moduleId);
		cmd.setModuleType(moduleType);
		cmd.setOrganizationId(organizationId);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setFormName("form1");
		List<GeneralFormFieldDTO> formFields = new ArrayList<GeneralFormFieldDTO>();
		GeneralFormFieldDTO e = new GeneralFormFieldDTO();
		e.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		e.setFieldName("name");
		e.setFieldDisplayName("展示name");
		formFields.add(e);
		cmd.setFormFields(formFields );
		

		String url = CREATEAPPROVALFORM_URL;
		General_approvalCreateApprovalFormRestResponse response= httpClientService.restPost(url , cmd, General_approvalCreateApprovalFormRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		return response.getResponse();
	}


	public GeneralFormDTO testUpdateApprovalForm(Long id) {

		UpdateApprovalFormCommand cmd = new UpdateApprovalFormCommand();
		cmd.setFormOriginId(id);
		cmd.setModuleId(moduleId);
		cmd.setModuleType(moduleType);
		cmd.setOrganizationId(organizationId);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setFormName("form2");
		List<GeneralFormFieldDTO> formFields = new ArrayList<GeneralFormFieldDTO>();
		GeneralFormFieldDTO e = new GeneralFormFieldDTO();
		e.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		e.setFieldName("name");
		e.setFieldDisplayName("展示name");
	
		formFields.add(e);
		cmd.setFormFields(formFields );
		

		String url = UPDATEAPPROVALFORM_URL;
		General_approvalUpdateApprovalFormRestResponse response= httpClientService.restPost(url , cmd, General_approvalUpdateApprovalFormRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		return response.getResponse();
		
	}
	
	public GeneralFormDTO testGetApprovalForm(Long id ){

		ApprovalFormIdCommand cmd = new ApprovalFormIdCommand();
		cmd.setFormOriginId(id);  
		
		
		String url = GETAPPROVALFORM_URL;
		General_approvalGetApprovalFormRestResponse response= httpClientService.restPost(url , cmd, General_approvalGetApprovalFormRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		return response.getResponse();
	}

	public ListGeneralFormResponse listApprovalForm(){

		ListApprovalFormsCommand cmd = new ListApprovalFormsCommand(); 
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		
		String url = LISTAPPROVALFORMS_URL;
		General_approvalListApprovalFormsRestResponse response= httpClientService.restPost(url , cmd, General_approvalListApprovalFormsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		return response.getResponse();
	}
	
	public GeneralApprovalDTO testCreateGeneralApproval(Long formid) {
		 
		CreateGeneralApprovalCommand cmd = new CreateGeneralApprovalCommand();
		cmd.setModuleId(moduleId);
		cmd.setModuleType(moduleType);
		cmd.setOrganizationId(organizationId);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setProjectType(projectType);
		cmd.setProjectId(projectId);
		cmd.setApprovalName("shenpi");
		cmd.setFormOriginId(formid);
		cmd.setSupportType(GeneralApprovalSupportType.APP_AND_WEB.getCode());
		
		String url = CREATEAPPROVALFORM_URL;
		General_approvalCreateGeneralApprovalRestResponse response= httpClientService.restPost(url , cmd, General_approvalCreateGeneralApprovalRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		return response.getResponse();
	}

	@Test
	public void test1(){
		logon();
		GeneralFormDTO dto1 = testCreateApprovalForm();
		GeneralFormDTO dto2 = testUpdateApprovalForm(dto1.getFormOriginId());
		GeneralFormDTO dto3 = testGetApprovalForm(dto1.getFormOriginId());
		testCreateApprovalForm();
		testCreateApprovalForm();
		testCreateApprovalForm();
		ListGeneralFormResponse listGeneralFormResponse = listApprovalForm();
		
	}
	private DSLContext context() {
		return dbProvider.getDslContext();
	}

	private void logon() {
		String userIdentifier = "12900000001";
		String plainTextPwd = "123456";
		Integer namespaceId = 0;
		logon(namespaceId, userIdentifier, plainTextPwd);
	}

	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

		// jsonFilePath = "data/json/energy-test-data-161031.json";
		// fileAbsolutePath =
		// dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		// dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

		jsonFilePath = "data/json/organizations-data-test_20161014.txt";
		fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

		jsonFilePath = "data/json/energy-reading-log-3-test-data-161104.json";
		fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}

	@After
	public void tearDown() {
		logoff();
	}

}
