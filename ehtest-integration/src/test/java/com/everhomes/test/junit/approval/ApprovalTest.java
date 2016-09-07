package com.everhomes.test.junit.approval;

import org.junit.After;
import org.junit.Before;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.approval.ApproveApprovalRequestCommand;
import com.everhomes.rest.approval.CreateAbsenceRequestBySceneCommand;
import com.everhomes.rest.approval.CreateAbsenceRequestBySceneResponse;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalCategoryRestResponse;
import com.everhomes.rest.approval.CreateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.CreateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.CreateApprovalFlowInfoRestResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelRestResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.CreateApprovalRuleRestResponse;
import com.everhomes.rest.approval.CreateForgotRequestBySceneCommand;
import com.everhomes.rest.approval.CreateForgotRequestBySceneResponse;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneResponse;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestResponse;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestRestResponse;
import com.everhomes.rest.approval.ListAbsenceRequestCommand;
import com.everhomes.rest.approval.ListAbsenceRequestResponse;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.ListApprovalCategoryRestResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestRestResponse;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalFlowRestResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestRestResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestRestResponse;
import com.everhomes.rest.approval.ListApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalRuleRestResponse;
import com.everhomes.rest.approval.ListApprovalUserCommand;
import com.everhomes.rest.approval.ListApprovalUserResponse;
import com.everhomes.rest.approval.ListApprovalUserRestResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowRestResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleRestResponse;
import com.everhomes.rest.approval.ListForgotRequestCommand;
import com.everhomes.rest.approval.ListForgotRequestResponse;
import com.everhomes.rest.approval.RejectApprovalRequestCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalCategoryRestResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoRestResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelRestResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleRestResponse;
import com.everhomes.rest.techpark.punch.PunchListAbsenceRequestRestResponse;
import com.everhomes.rest.techpark.punch.PunchListForgotRequestRestResponse;
import com.everhomes.rest.ui.approval.ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalFlowOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalLogAndFlowOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalLogOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalRequestBySceneRestResponse;
import com.everhomes.rest.ui.techpark.punch.TechparkPunchCreateAbsenceRequestBySceneRestResponse;
import com.everhomes.rest.ui.techpark.punch.TechparkPunchCreateForgotRequestBySceneRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ApprovalTest extends BaseLoginAuthTestCase {
	//增加审批类别，如请假的公出、事假等
	private static final String CREATE_APPROVAL_CATEGORY_URL = "/approval/createApprovalCategory";
	//更新审批类别
	private static final String UPDATE_APPROVAL_CATEGORY_URL = "/approval/updateApprovalCategory";
	//列出审批类别
	private static final String LIST_APPROVAL_CATEGORY_URL = "/approval/listApprovalCategory";
	//删除审批类别
	private static final String DELETE_APPROVAL_CATEGORY_URL = "/approval/deleteApprovalCategory";
	//设置审批流程信息
	private static final String CREATE_APPROVAL_FLOW_INFO_URL = "/approval/createApprovalFlowInfo";
	//更新审批流程信息
	private static final String UPDATE_APPROVAL_FLOW_INFO_URL = "/approval/updateApprovalFlowInfo";
	//设置审批流程级别
	private static final String CREATE_APPROVAL_FLOW_LEVEL_URL = "/approval/createApprovalFlowLevel";
	//更新审批流程级别
	private static final String UPDATE_APPROVAL_FLOW_LEVEL_URL = "/approval/updateApprovalFlowLevel";
	//审批流程列表
	private static final String LIST_APPROVAL_FLOW_URL = "/approval/listApprovalFlow";
	//审批流程简短列表
	private static final String LIST_BRIEF_APPROVAL_FLOW_URL = "/approval/listBriefApprovalFlow";
	//删除审批流程
	private static final String DELETE_APPROVAL_FLOW_URL = "/approval/deleteApprovalFlow";
	//增加审批规则
	private static final String CREATE_APPROVAL_RULE_URL = "/approval/createApprovalRule";
	//更新审批规则
	private static final String UPDATE_APPROVAL_RULE_URL = "/approval/updateApprovalRule";
	//删除审批规则
	private static final String DELETE_APPROVAL_RULE_URL = "/approval/deleteApprovalRule";
	//审批规则列表
	private static final String LIST_APPROVAL_RULE_URL = "/approval/listApprovalRule";
	//审批规则简短列表
	private static final String LIST_BRIEF_APPROVAL_RULE_URL = "/approval/listBriefApprovalRule";
	//同意申请
	private static final String APPROVE_APPROVAL_REQUEST_URL = "/approval/approveApprovalRequest";
	//驳回申请
	private static final String REJECT_APPROVAL_REQUEST_URL = "/approval/rejectApprovalRequest";
	//获取申请的审批基本信息
	private static final String GET_APPROVAL_BASIC_INFO_OF_REQUEST_URL = "/approval/getApprovalBasicInfoOfRequest";
	//获取申请的审批日志与审批流程列表
	private static final String LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_URL = "/approval/listApprovalLogAndFlowOfRequest";
	//获取申请的审批日志列表
	private static final String LIST_APPROVAL_LOG_OF_REQUEST_URL = "/approval/listApprovalLogOfRequest";
	//获取申请的审批流程列表
	private static final String LIST_APPROVAL_FLOW_OF_REQUEST_URL = "/approval/listApprovalFlowOfRequest";
	//人员列表，可按部门、姓名筛选
	private static final String LIST_APPROVAL_USER_URL = "/approval/listApprovalUser";


	//增加审批类别，如请假的公出、事假等
	//@Test
	public void testCreateApprovalCategory() {
		String url = CREATE_APPROVAL_CATEGORY_URL;
		logon();
		CreateApprovalCategoryCommand cmd = new CreateApprovalCategoryCommand();

		CreateApprovalCategoryRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//更新审批类别
	//@Test
	public void testUpdateApprovalCategory() {
		String url = UPDATE_APPROVAL_CATEGORY_URL;
		logon();
		UpdateApprovalCategoryCommand cmd = new UpdateApprovalCategoryCommand();

		UpdateApprovalCategoryRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//列出审批类别
	//@Test
	public void testListApprovalCategory() {
		String url = LIST_APPROVAL_CATEGORY_URL;
		logon();
		ListApprovalCategoryCommand cmd = new ListApprovalCategoryCommand();

		ListApprovalCategoryRestResponse response = httpClientService.restPost(url, cmd, ListApprovalCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//删除审批类别
	//@Test
	public void testDeleteApprovalCategory() {
		String url = DELETE_APPROVAL_CATEGORY_URL;
		logon();
		DeleteApprovalCategoryCommand cmd = new DeleteApprovalCategoryCommand();

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//设置审批流程信息
	//@Test
	public void testCreateApprovalFlowInfo() {
		String url = CREATE_APPROVAL_FLOW_INFO_URL;
		logon();
		CreateApprovalFlowInfoCommand cmd = new CreateApprovalFlowInfoCommand();

		CreateApprovalFlowInfoRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalFlowInfoRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalFlowInfoResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//更新审批流程信息
	//@Test
	public void testUpdateApprovalFlowInfo() {
		String url = UPDATE_APPROVAL_FLOW_INFO_URL;
		logon();
		UpdateApprovalFlowInfoCommand cmd = new UpdateApprovalFlowInfoCommand();

		UpdateApprovalFlowInfoRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalFlowInfoRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalFlowInfoResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//设置审批流程级别
	//@Test
	public void testCreateApprovalFlowLevel() {
		String url = CREATE_APPROVAL_FLOW_LEVEL_URL;
		logon();
		CreateApprovalFlowLevelCommand cmd = new CreateApprovalFlowLevelCommand();

		CreateApprovalFlowLevelRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalFlowLevelRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalFlowLevelResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//更新审批流程级别
	//@Test
	public void testUpdateApprovalFlowLevel() {
		String url = UPDATE_APPROVAL_FLOW_LEVEL_URL;
		logon();
		UpdateApprovalFlowLevelCommand cmd = new UpdateApprovalFlowLevelCommand();

		UpdateApprovalFlowLevelRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalFlowLevelRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalFlowLevelResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//审批流程列表
	//@Test
	public void testListApprovalFlow() {
		String url = LIST_APPROVAL_FLOW_URL;
		logon();
		ListApprovalFlowCommand cmd = new ListApprovalFlowCommand();

		ListApprovalFlowRestResponse response = httpClientService.restPost(url, cmd, ListApprovalFlowRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalFlowResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//审批流程简短列表
	//@Test
	public void testListBriefApprovalFlow() {
		String url = LIST_BRIEF_APPROVAL_FLOW_URL;
		logon();
		ListBriefApprovalFlowCommand cmd = new ListBriefApprovalFlowCommand();

		ListBriefApprovalFlowRestResponse response = httpClientService.restPost(url, cmd, ListBriefApprovalFlowRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBriefApprovalFlowResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//删除审批流程
	//@Test
	public void testDeleteApprovalFlow() {
		String url = DELETE_APPROVAL_FLOW_URL;
		logon();
		DeleteApprovalFlowCommand cmd = new DeleteApprovalFlowCommand();

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//增加审批规则
	//@Test
	public void testCreateApprovalRule() {
		String url = CREATE_APPROVAL_RULE_URL;
		logon();
		CreateApprovalRuleCommand cmd = new CreateApprovalRuleCommand();

		CreateApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//更新审批规则
	//@Test
	public void testUpdateApprovalRule() {
		String url = UPDATE_APPROVAL_RULE_URL;
		logon();
		UpdateApprovalRuleCommand cmd = new UpdateApprovalRuleCommand();

		UpdateApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//删除审批规则
	//@Test
	public void testDeleteApprovalRule() {
		String url = DELETE_APPROVAL_RULE_URL;
		logon();
		DeleteApprovalRuleCommand cmd = new DeleteApprovalRuleCommand();

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//审批规则列表
	//@Test
	public void testListApprovalRule() {
		String url = LIST_APPROVAL_RULE_URL;
		logon();
		ListApprovalRuleCommand cmd = new ListApprovalRuleCommand();

		ListApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, ListApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//审批规则简短列表
	//@Test
	public void testListBriefApprovalRule() {
		String url = LIST_BRIEF_APPROVAL_RULE_URL;
		logon();
		ListBriefApprovalRuleCommand cmd = new ListBriefApprovalRuleCommand();

		ListBriefApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, ListBriefApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBriefApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//同意申请
	//@Test
	public void testApproveApprovalRequest() {
		String url = APPROVE_APPROVAL_REQUEST_URL;
		logon();
		ApproveApprovalRequestCommand cmd = new ApproveApprovalRequestCommand();

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//驳回申请
	//@Test
	public void testRejectApprovalRequest() {
		String url = REJECT_APPROVAL_REQUEST_URL;
		logon();
		RejectApprovalRequestCommand cmd = new RejectApprovalRequestCommand();

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//获取申请的审批基本信息
	//@Test
	public void testGetApprovalBasicInfoOfRequest() {
		String url = GET_APPROVAL_BASIC_INFO_OF_REQUEST_URL;
		logon();
		GetApprovalBasicInfoOfRequestCommand cmd = new GetApprovalBasicInfoOfRequestCommand();

		GetApprovalBasicInfoOfRequestRestResponse response = httpClientService.restPost(url, cmd, GetApprovalBasicInfoOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetApprovalBasicInfoOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批日志与审批流程列表
	//@Test
	public void testListApprovalLogAndFlowOfRequest() {
		String url = LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_URL;
		logon();
		ListApprovalLogAndFlowOfRequestCommand cmd = new ListApprovalLogAndFlowOfRequestCommand();

		ListApprovalLogAndFlowOfRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalLogAndFlowOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogAndFlowOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批日志列表
	//@Test
	public void testListApprovalLogOfRequest() {
		String url = LIST_APPROVAL_LOG_OF_REQUEST_URL;
		logon();
		ListApprovalLogOfRequestCommand cmd = new ListApprovalLogOfRequestCommand();

		ListApprovalLogOfRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalLogOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批流程列表
	//@Test
	public void testListApprovalFlowOfRequest() {
		String url = LIST_APPROVAL_FLOW_OF_REQUEST_URL;
		logon();
		ListApprovalFlowOfRequestCommand cmd = new ListApprovalFlowOfRequestCommand();

		ListApprovalFlowOfRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalFlowOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalFlowOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//人员列表，可按部门、姓名筛选
	//@Test
	public void testListApprovalUser() {
		String url = LIST_APPROVAL_USER_URL;
		logon();
		ListApprovalUserCommand cmd = new ListApprovalUserCommand();

		ListApprovalUserRestResponse response = httpClientService.restPost(url, cmd, ListApprovalUserRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalUserResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}


	//忘打卡申请列表
	private static final String LIST_FORGOT_REQUEST_URL = "/techpark/punch/listForgotRequest";
	//请假申请列表
	private static final String LIST_ABSENCE_REQUEST_URL = "/techpark/punch/listAbsenceRequest";


	//忘打卡申请列表
	//@Test
	public void testListForgotRequest() {
		String url = LIST_FORGOT_REQUEST_URL;
		logon();
		ListForgotRequestCommand cmd = new ListForgotRequestCommand();

		PunchListForgotRequestRestResponse response = httpClientService.restPost(url, cmd, PunchListForgotRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListForgotRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//请假申请列表
	//@Test
	public void testListAbsenceRequest() {
		String url = LIST_ABSENCE_REQUEST_URL;
		logon();
		ListAbsenceRequestCommand cmd = new ListAbsenceRequestCommand();

		PunchListAbsenceRequestRestResponse response = httpClientService.restPost(url, cmd, PunchListAbsenceRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListAbsenceRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}


	//创建请假申请（客户端）
	private static final String CREATE_ABSENCE_REQUEST_BY_SCENE_URL = "/ui/techpark/punch/createAbsenceRequestByScene";
	//创建忘打卡申请（客户端）
	private static final String CREATE_FORGOT_REQUEST_BY_SCENE_URL = "/ui/techpark/punch/createForgotRequestByScene";


	//创建请假申请（客户端）
	//@Test
	public void testCreateAbsenceRequestByScene() {
		String url = CREATE_ABSENCE_REQUEST_BY_SCENE_URL;
		logon();
		CreateAbsenceRequestBySceneCommand cmd = new CreateAbsenceRequestBySceneCommand();

		TechparkPunchCreateAbsenceRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, TechparkPunchCreateAbsenceRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateAbsenceRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//创建忘打卡申请（客户端）
	//@Test
	public void testCreateForgotRequestByScene() {
		String url = CREATE_FORGOT_REQUEST_BY_SCENE_URL;
		logon();
		CreateForgotRequestBySceneCommand cmd = new CreateForgotRequestBySceneCommand();

		TechparkPunchCreateForgotRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, TechparkPunchCreateForgotRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateForgotRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}


	//申请列表（客户端）
	private static final String LIST_APPROVAL_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalRequestByScene";
	//获取申请的审批基本信息（客户端）
	private static final String GET_APPROVAL_BASIC_INFO_OF_REQUEST_BY_SCENE_URL = "/ui/approval/getApprovalBasicInfoOfRequestByScene";
	//获取申请的审批日志与审批流程列表（客户端）
	private static final String LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalLogAndFlowOfRequestByScene";
	//获取申请的审批日志列表（客户端）
	private static final String LIST_APPROVAL_LOG_OF_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalLogOfRequestByScene";
	//获取申请的审批流程列表（客户端）
	private static final String LIST_APPROVAL_FLOW_OF_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalFlowOfRequestByScene";


	//申请列表（客户端）
	//@Test
	public void testListApprovalRequestByScene() {
		String url = LIST_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		ListApprovalRequestBySceneCommand cmd = new ListApprovalRequestBySceneCommand();

		ApprovalListApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批基本信息（客户端）
	//@Test
	public void testGetApprovalBasicInfoOfRequestByScene() {
		String url = GET_APPROVAL_BASIC_INFO_OF_REQUEST_BY_SCENE_URL;
		logon();
		GetApprovalBasicInfoOfRequestBySceneCommand cmd = new GetApprovalBasicInfoOfRequestBySceneCommand();

		ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetApprovalBasicInfoOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批日志与审批流程列表（客户端）
	//@Test
	public void testListApprovalLogAndFlowOfRequestByScene() {
		String url = LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_BY_SCENE_URL;
		logon();
		ListApprovalLogAndFlowOfRequestBySceneCommand cmd = new ListApprovalLogAndFlowOfRequestBySceneCommand();

		ApprovalListApprovalLogAndFlowOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalLogAndFlowOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogAndFlowOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批日志列表（客户端）
	//@Test
	public void testListApprovalLogOfRequestByScene() {
		String url = LIST_APPROVAL_LOG_OF_REQUEST_BY_SCENE_URL;
		logon();
		ListApprovalLogOfRequestBySceneCommand cmd = new ListApprovalLogOfRequestBySceneCommand();

		ApprovalListApprovalLogOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalLogOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//获取申请的审批流程列表（客户端）
	//@Test
	public void testListApprovalFlowOfRequestByScene() {
		String url = LIST_APPROVAL_FLOW_OF_REQUEST_BY_SCENE_URL;
		logon();
		ListApprovalFlowOfRequestBySceneCommand cmd = new ListApprovalFlowOfRequestBySceneCommand();

		ApprovalListApprovalFlowOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalFlowOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalFlowOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}



	
	
	
	
	
	
	
	
	
	
	
	
	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/3.4.x-test-data-news-organization-160627.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}

	@After
	public void tearDown() {
		logoff();
	}

	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}

}
