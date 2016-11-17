package com.everhomes.test.junit.approval;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.aclink.ActiveRestResponse;
import com.everhomes.rest.approval.*;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.ui.approval.ApprovalCreateApprovalRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalCategoryBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalFlowOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalLogAndFlowOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalLogOfRequestBySceneRestResponse;
import com.everhomes.rest.ui.approval.ApprovalListApprovalRequestBySceneRestResponse;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhApprovalCategories;
import com.everhomes.server.schema.tables.pojos.EhApprovalFlows;
import com.everhomes.server.schema.tables.pojos.EhApprovalRequests;
import com.everhomes.server.schema.tables.pojos.EhApprovalRuleFlowMap;
import com.everhomes.server.schema.tables.pojos.EhApprovalRules;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class ApprovalTest extends BaseLoginAuthTestCase {
	//1. 增加审批类别，如请假的公出、事假等
	private static final String CREATE_APPROVAL_CATEGORY_URL = "/approval/createApprovalCategory";
	//2. 更新审批类别
	private static final String UPDATE_APPROVAL_CATEGORY_URL = "/approval/updateApprovalCategory";
	//3. 列出审批类别
	private static final String LIST_APPROVAL_CATEGORY_URL = "/approval/listApprovalCategory";
	//4. 删除审批类别
	private static final String DELETE_APPROVAL_CATEGORY_URL = "/approval/deleteApprovalCategory";
	//5. 设置审批流程信息
	private static final String CREATE_APPROVAL_FLOW_INFO_URL = "/approval/createApprovalFlowInfo";
	//6. 更新审批流程信息
	private static final String UPDATE_APPROVAL_FLOW_INFO_URL = "/approval/updateApprovalFlowInfo";
	//7. 设置审批流程级别
	private static final String CREATE_APPROVAL_FLOW_LEVEL_URL = "/approval/createApprovalFlowLevel";
	//8. 更新审批流程级别
	private static final String UPDATE_APPROVAL_FLOW_LEVEL_URL = "/approval/updateApprovalFlowLevel";
	//9. 审批流程列表
	private static final String LIST_APPROVAL_FLOW_URL = "/approval/listApprovalFlow";
	//10. 审批流程简短列表
	private static final String LIST_BRIEF_APPROVAL_FLOW_URL = "/approval/listBriefApprovalFlow";
	//11. 删除审批流程
	private static final String DELETE_APPROVAL_FLOW_URL = "/approval/deleteApprovalFlow";
	//12. 增加审批规则
	private static final String CREATE_APPROVAL_RULE_URL = "/approval/createApprovalRule";
	//13. 更新审批规则
	private static final String UPDATE_APPROVAL_RULE_URL = "/approval/updateApprovalRule";
	//14. 删除审批规则
	private static final String DELETE_APPROVAL_RULE_URL = "/approval/deleteApprovalRule";
	//15. 审批规则列表
	private static final String LIST_APPROVAL_RULE_URL = "/approval/listApprovalRule";
	//16. 审批规则简短列表
	private static final String LIST_BRIEF_APPROVAL_RULE_URL = "/approval/listBriefApprovalRule";
	//17. 同意申请
	private static final String APPROVE_APPROVAL_REQUEST_URL = "/approval/approveApprovalRequest";
	//18. 驳回申请
	private static final String REJECT_APPROVAL_REQUEST_URL = "/approval/rejectApprovalRequest";
	//19. 获取申请的审批基本信息
	private static final String GET_APPROVAL_BASIC_INFO_OF_REQUEST_URL = "/approval/getApprovalBasicInfoOfRequest";
	//20. 获取申请的审批日志与审批流程列表
	private static final String LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_URL = "/approval/listApprovalLogAndFlowOfRequest";
	//21. 获取申请的审批日志列表
	private static final String LIST_APPROVAL_LOG_OF_REQUEST_URL = "/approval/listApprovalLogOfRequest";
	//22. 获取申请的审批流程列表
	private static final String LIST_APPROVAL_FLOW_OF_REQUEST_URL = "/approval/listApprovalFlowOfRequest";
	//23. 人员列表，可按部门、姓名筛选
	private static final String LIST_APPROVAL_USER_URL = "/approval/listApprovalUser";
	//24. 查询申请列表
	private static final String LIST_APPROVAL_REQUEST_URL = "/approval/listApprovalRequest";
	//25. 个人申请列表（客户端）
	private static final String LIST_APPROVAL_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalRequestByScene";
	//26. 获取申请的审批基本信息（客户端）
	private static final String GET_APPROVAL_BASIC_INFO_OF_REQUEST_BY_SCENE_URL = "/ui/approval/getApprovalBasicInfoOfRequestByScene";
	//27. 获取申请的审批日志与审批流程列表（客户端）
	private static final String LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalLogAndFlowOfRequestByScene";
	//28. 获取申请的审批日志列表（客户端）
	private static final String LIST_APPROVAL_LOG_OF_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalLogOfRequestByScene";
	//29. 获取申请的审批流程列表（客户端）
	private static final String LIST_APPROVAL_FLOW_OF_REQUEST_BY_SCENE_URL = "/ui/approval/listApprovalFlowOfRequestByScene";
	//30. 创建申请（客户端）
	private static final String CREATE_APPROVAL_REQUEST_BY_SCENE_URL = "/ui/approval/createApprovalRequestByScene";
	//31. 取消申请（客户端）
	private static final String CANCEL_APPROVAL_REQUEST_BY_SCENE_URL = "/ui/approval/cancelApprovalRequestByScene";
	//32. 列出审批类别（客户端）
	private static final String LIST_APPROVAL_CATEGORY_BY_SCENE_URL = "/ui/approval/listApprovalCategoryByScene";

	
	//33. 列出我的审批列表（客户端）
	private static final String LIST_MY_APPROVALS_URL = "/ui/approval/listMyApprovalsByScene";
	//34. 同意申请（客户端）
	private static final String APPROVE_APPROVAL_REQUEST_BY_SCENE_URL = "/ui/approval/approveApprovalRequestByScene";
	//35. 驳回申请（客户端）
	private static final String REJECT_APPROVAL_REQUEST_BY_SCENE_URL = "/ui/approval/rejectApprovalRequestByScene";

	//36. 查询具体某机构/人 的审批规则
	private static final String GET_TARGET_APPROVAL_RULE_URL = "/approval/getTargetApprovalRule";
	//37. 更新具体某机构/人 的审批规则
	private static final String UPDATE_TARGET_APPROVAL_RULE_URL = "/approval/updateTargetApprovalRule";
	//38. 删除具体某机构/人 的审批规则
	private static final String DELETE_TARGET_APPROVAL_RULE_URL = "/approval/deleteTargetApprovalRule";
	
	//1. 增加审批类别，如请假的公出、事假等（完成）
	@Test
	public void testCreateApprovalCategory() {
		String url = CREATE_APPROVAL_CATEGORY_URL;
		logon();
		
		createApprovalCategory(url);
	}

	private Long createApprovalCategory(String url) {
		CreateApprovalCategoryCommand cmd = new CreateApprovalCategoryCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
		cmd.setCategoryName("公出");

		CreateApprovalCategoryRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategory());
		assertNotNull(myResponse.getCategory().getId());
		assertNotNull(myResponse.getCategory().getOwnerType());
		assertNotNull(myResponse.getCategory().getCategoryName());
		assertNotNull(myResponse.getCategory().getOwnerId());
		assertNotNull(myResponse.getCategory().getNamespaceId());
		assertNotNull(myResponse.getCategory().getApprovalType());

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_CATEGORIES).fetchOne();
		assertNotNull(record);
		EhApprovalCategories category = ConvertHelper.convert(record, EhApprovalCategories.class);
		assertEquals(myResponse.getCategory().getId(), category.getId());
		assertEquals(myResponse.getCategory().getCategoryName(), category.getCategoryName());
		
		return category.getId();
	}
	
	//2. 更新审批类别（完成）
	@Test
	public void testUpdateApprovalCategory() {
		String url = UPDATE_APPROVAL_CATEGORY_URL;
		logon();
		Long id = createApprovalCategory(CREATE_APPROVAL_CATEGORY_URL);
		UpdateApprovalCategoryCommand cmd = new UpdateApprovalCategoryCommand();
		cmd.setId(id);
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
		cmd.setCategoryName("事假");

		UpdateApprovalCategoryRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategory());
		assertNotNull(myResponse.getCategory().getId());
		assertNotNull(myResponse.getCategory().getOwnerType());
		assertNotNull(myResponse.getCategory().getCategoryName());
		assertNotNull(myResponse.getCategory().getOwnerId());
		assertNotNull(myResponse.getCategory().getNamespaceId());
		assertNotNull(myResponse.getCategory().getApprovalType());

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_CATEGORIES).fetchOne();
		assertNotNull(record);
		EhApprovalCategories category = ConvertHelper.convert(record, EhApprovalCategories.class);
		assertEquals(myResponse.getCategory().getId(), category.getId());
		assertEquals(myResponse.getCategory().getCategoryName(), category.getCategoryName());
		assertEquals("事假", category.getCategoryName());

	}

	//3. 列出审批类别（完成）
	@Test
	public void testListApprovalCategory() {
		String url = LIST_APPROVAL_CATEGORY_URL;
		logon();
		initListData();
		
		ListApprovalCategoryCommand cmd = new ListApprovalCategoryCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());

		ListApprovalCategoryRestResponse response = httpClientService.restPost(url, cmd, ListApprovalCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategoryList());
		assertEquals(5, myResponse.getCategoryList().size());
		assertEquals("公出", myResponse.getCategoryList().get(0).getCategoryName());
		assertEquals("年休", myResponse.getCategoryList().get(4).getCategoryName());

	}

	//4. 删除审批类别（完成）
	@Test
	public void testDeleteApprovalCategory() {
		String url = DELETE_APPROVAL_CATEGORY_URL;
		logon();
		initListData();
		DeleteApprovalCategoryCommand cmd = new DeleteApprovalCategoryCommand();
		cmd.setId(2L);
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> result = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_CATEGORIES).where(Tables.EH_APPROVAL_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode())).fetch();
		assertNotNull(result);
		assertEquals(4, result.size());
	}

	//5. 设置审批流程信息（完成）
	@Test
	public void testCreateApprovalFlowInfo() {
		String url = CREATE_APPROVAL_FLOW_INFO_URL;
		logon();
		createApprovalFlowInfo(url);
	}

	private Long createApprovalFlowInfo(String url) {
		CreateApprovalFlowInfoCommand cmd = new CreateApprovalFlowInfoCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setName("小彤彤专属流程");

		CreateApprovalFlowInfoRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalFlowInfoRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalFlowInfoResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getBriefApprovalFlow());
		assertNotNull(myResponse.getBriefApprovalFlow().getId());
		assertNotNull(myResponse.getBriefApprovalFlow().getOwnerType());
		assertNotNull(myResponse.getBriefApprovalFlow().getName());
		assertNotNull(myResponse.getBriefApprovalFlow().getOwnerId());
		assertNotNull(myResponse.getBriefApprovalFlow().getNamespaceId());

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_FLOWS).fetchOne();
		assertNotNull(record);
		EhApprovalFlows flow = ConvertHelper.convert(record, EhApprovalFlows.class);
		assertEquals(myResponse.getBriefApprovalFlow().getId(), flow.getId());
		assertEquals(myResponse.getBriefApprovalFlow().getName(), flow.getName());

		return myResponse.getBriefApprovalFlow().getId();
	}
	
	//6. 更新审批流程信息（完成）
	@Test
	public void testUpdateApprovalFlowInfo() {
		String url = UPDATE_APPROVAL_FLOW_INFO_URL;
		logon();
		Long id = createApprovalFlowInfo(CREATE_APPROVAL_FLOW_INFO_URL);
		UpdateApprovalFlowInfoCommand cmd = new UpdateApprovalFlowInfoCommand();
		cmd.setId(id);
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setName("小唐唐专属流程");

		UpdateApprovalFlowInfoRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalFlowInfoRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalFlowInfoResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getBriefApprovalFlow());
		assertNotNull(myResponse.getBriefApprovalFlow().getId());
		assertNotNull(myResponse.getBriefApprovalFlow().getOwnerType());
		assertNotNull(myResponse.getBriefApprovalFlow().getName());
		assertNotNull(myResponse.getBriefApprovalFlow().getOwnerId());
		assertNotNull(myResponse.getBriefApprovalFlow().getNamespaceId());

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_FLOWS).fetchOne();
		assertNotNull(record);
		EhApprovalFlows flow = ConvertHelper.convert(record, EhApprovalFlows.class);
		assertEquals(myResponse.getBriefApprovalFlow().getId(), flow.getId());
		assertEquals(myResponse.getBriefApprovalFlow().getName(), flow.getName());
		assertEquals("小唐唐专属流程", flow.getName());

	}

	//7. 设置审批流程级别（完成）
	@Test
	public void testCreateApprovalFlowLevel() {
		String url = CREATE_APPROVAL_FLOW_LEVEL_URL;
		logon();
		Long flowId = createApprovalFlowInfo(CREATE_APPROVAL_FLOW_INFO_URL);
		
		//第一次请求
		CreateApprovalFlowLevelCommand cmd = new CreateApprovalFlowLevelCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setFlowId(flowId);
		cmd.setLevel((byte)1);
		List<ApprovalUser> approvalUserList = new ArrayList<>();
		ApprovalUser approvalUser = new ApprovalUser();
		approvalUser.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser.setTargetId(1L);
		approvalUserList.add(approvalUser);
		ApprovalUser approvalUser2 = new ApprovalUser();
		approvalUser2.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser2.setTargetId(2L);
		approvalUserList.add(approvalUser2);
		cmd.setApprovalUserList(approvalUserList);

		CreateApprovalFlowLevelRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalFlowLevelRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalFlowLevelResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals(flowId.longValue(), myResponse.getFlowId().longValue());
		assertNotNull(myResponse.getApprovalFlowLevel());
		assertEquals((byte)1, myResponse.getApprovalFlowLevel().getLevel().byteValue());
		assertNotNull(myResponse.getApprovalFlowLevel().getApprovalUserList());
		assertEquals(2, myResponse.getApprovalFlowLevel().getApprovalUserList().size());
		assertNotNull(myResponse.getApprovalFlowLevel().getApprovalUserList().get(0).getTargetName());
		
		//第二次请求
		cmd = new CreateApprovalFlowLevelCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setFlowId(flowId);
		cmd.setLevel((byte)2);
		approvalUserList = new ArrayList<>();
		approvalUser = new ApprovalUser();
		approvalUser.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser.setTargetId(2L);
		approvalUserList.add(approvalUser);
		approvalUser2 = new ApprovalUser();
		approvalUser2.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser2.setTargetId(3L);
		approvalUserList.add(approvalUser2);
		cmd.setApprovalUserList(approvalUserList);

		response = httpClientService.restPost(url, cmd, CreateApprovalFlowLevelRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> result = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_FLOW_LEVELS).fetch();
		assertNotNull(result);
		assertEquals(4, result.size());
		
	}

	//8. 更新审批流程级别（完成）
	@Test
	public void testUpdateApprovalFlowLevel() {
		String url = UPDATE_APPROVAL_FLOW_LEVEL_URL;
		logon();
		initListData();
		UpdateApprovalFlowLevelCommand cmd = new UpdateApprovalFlowLevelCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setFlowId(1L);
		cmd.setLevel((byte)1);
		List<ApprovalUser> approvalUserList = new ArrayList<>();
		ApprovalUser approvalUser = new ApprovalUser();
		approvalUser.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser.setTargetId(3L);
		approvalUserList.add(approvalUser);
		cmd.setApprovalUserList(approvalUserList);

		UpdateApprovalFlowLevelRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalFlowLevelRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalFlowLevelResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals(1L, myResponse.getFlowId().longValue());
		assertNotNull(myResponse.getApprovalFlowLevel());
		assertEquals((byte)1, myResponse.getApprovalFlowLevel().getLevel().byteValue());
		assertNotNull(myResponse.getApprovalFlowLevel().getApprovalUserList());
		assertEquals(1, myResponse.getApprovalFlowLevel().getApprovalUserList().size());
		assertNotNull(myResponse.getApprovalFlowLevel().getApprovalUserList().get(0).getTargetName());

	}

	//9. 审批流程列表（完成）
	@Test
	public void testListApprovalFlow() {
		String url = LIST_APPROVAL_FLOW_URL;
		logon();
		initListData();
		ListApprovalFlowCommand cmd = new ListApprovalFlowCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setPageSize(2);

		ListApprovalFlowRestResponse response = httpClientService.restPost(url, cmd, ListApprovalFlowRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalFlowResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		//assertEquals(3L, myResponse.getNextPageAnchor().longValue());
		assertNotNull(myResponse.getApprovalFlowList());
		//assertEquals(2, myResponse.getApprovalFlowList().size());
		
		cmd.setPageAnchor(myResponse.getNextPageAnchor().longValue());
		response = httpClientService.restPost(url, cmd, ListApprovalFlowRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNull(myResponse.getNextPageAnchor());
		assertNotNull(myResponse.getApprovalFlowList());
		//assertEquals(1, myResponse.getApprovalFlowList().size()); //这边逻辑在2.0都改了，不能这样检查

	}

	//10. 审批流程简短列表（完成）
	@Test
	public void testListBriefApprovalFlow() {
		String url = LIST_BRIEF_APPROVAL_FLOW_URL;
		logon();
		initListData();
		ListBriefApprovalFlowCommand cmd = new ListBriefApprovalFlowCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);

		ListBriefApprovalFlowRestResponse response = httpClientService.restPost(url, cmd, ListBriefApprovalFlowRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBriefApprovalFlowResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getApprovalFlowList());
		assertEquals(4, myResponse.getApprovalFlowList().size());
		assertNotNull(myResponse.getApprovalFlowList().get(0).getName());

	}

	//11. 删除审批流程（完成）
	@Test
	public void testDeleteApprovalFlow() {
		String url = DELETE_APPROVAL_FLOW_URL;
		logon();
		initListData();
		DeleteApprovalFlowCommand cmd = new DeleteApprovalFlowCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setId(4L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_FLOWS).where(Tables.EH_APPROVAL_FLOWS.ID.eq(4L)).fetchOne();
		assertNotNull(record);
		EhApprovalFlows ehApprovalFlows = ConvertHelper.convert(record, EhApprovalFlows.class);
		assertEquals(CommonStatus.INACTIVE.getCode(), ehApprovalFlows.getStatus().byteValue());

	}

	//12. 增加审批规则（完成）
	@Test
	public void testCreateApprovalRule() {
		String url = CREATE_APPROVAL_RULE_URL;
		logon();
		initListData();
		CreateApprovalRuleCommand cmd = new CreateApprovalRuleCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setName("小彤彤规则");
		List<RuleFlowMap> ruleFlowMapList = new ArrayList<>();
		RuleFlowMap ruleFlowMap = new RuleFlowMap();
		ruleFlowMap.setApprovalType(ApprovalType.ABSENCE.getCode());
		ruleFlowMap.setFlowId(1L);
		ruleFlowMapList.add(ruleFlowMap);
		RuleFlowMap ruleFlowMap2 = new RuleFlowMap();
		ruleFlowMap2.setApprovalType(ApprovalType.EXCEPTION.getCode());
		ruleFlowMap2.setFlowId(2L);
		ruleFlowMapList.add(ruleFlowMap2);
		cmd.setRuleFlowMapList(ruleFlowMapList);

		CreateApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, CreateApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getApprovalRule());
		assertNotNull(myResponse.getApprovalRule().getId());
		assertNotNull(myResponse.getApprovalRule().getName());
		assertNotNull(myResponse.getApprovalRule().getRuleFlowMapList());
		assertEquals(2, myResponse.getApprovalRule().getRuleFlowMapList().size());
		

	}

	//13. 更新审批规则（完成）
	@Test
	public void testUpdateApprovalRule() {
		String url = UPDATE_APPROVAL_RULE_URL;
		logon();
		initListData();
		UpdateApprovalRuleCommand cmd = new UpdateApprovalRuleCommand();
		cmd.setId(11L);
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setName("tt rules");
		List<RuleFlowMap> ruleFlowMapList = new ArrayList<>();
		RuleFlowMap ruleFlowMap = new RuleFlowMap();
		ruleFlowMap.setApprovalType(ApprovalType.ABSENCE.getCode());
		ruleFlowMap.setFlowId(3L);
		ruleFlowMapList.add(ruleFlowMap);
		RuleFlowMap ruleFlowMap2 = new RuleFlowMap();
		ruleFlowMap2.setApprovalType(ApprovalType.EXCEPTION.getCode());
		ruleFlowMap2.setFlowId(3L);
		ruleFlowMapList.add(ruleFlowMap2);
		cmd.setRuleFlowMapList(ruleFlowMapList);

		UpdateApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, UpdateApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getApprovalRule());
		assertNotNull(myResponse.getApprovalRule().getId());
		assertNotNull(myResponse.getApprovalRule().getName());
		assertEquals("tt rules", myResponse.getApprovalRule().getName());
		assertNotNull(myResponse.getApprovalRule().getRuleFlowMapList());
		assertEquals(2, myResponse.getApprovalRule().getRuleFlowMapList().size());

	}

	//14. 删除审批规则（完成）
	@Test
	public void testDeleteApprovalRule() {
		String url = DELETE_APPROVAL_RULE_URL;
		logon();
		initListData();
		DeleteApprovalRuleCommand cmd = new DeleteApprovalRuleCommand();
		cmd.setId(11L);
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_RULES).where(Tables.EH_APPROVAL_RULES.ID.eq(11L)).fetchOne();
		assertNotNull(record);
		EhApprovalRules ehApprovalRules = ConvertHelper.convert(record, EhApprovalRules.class);
		assertEquals(CommonStatus.INACTIVE.getCode(), ehApprovalRules.getStatus().byteValue());
		
		Result<Record> result = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_RULE_FLOW_MAP).where(Tables.EH_APPROVAL_RULE_FLOW_MAP.RULE_ID.eq(11L)).fetch();
		assertNotNull(record);
		List<EhApprovalRuleFlowMap> list = result.map(r->ConvertHelper.convert(r, EhApprovalRuleFlowMap.class));
		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals(CommonStatus.INACTIVE.getCode(), list.get(0).getStatus().byteValue());
	}

	//15. 审批规则列表（完成）
	@Test
	public void testListApprovalRule() {
		String url = LIST_APPROVAL_RULE_URL;
		logon();
		initListData();
		ListApprovalRuleCommand cmd = new ListApprovalRuleCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setPageSize(2);

		ListApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, ListApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getNextPageAnchor());
		assertNotNull(myResponse.getApprovalRuleList());
		assertEquals(2, myResponse.getApprovalRuleList().size());
		
		cmd.setPageAnchor(myResponse.getNextPageAnchor());
		response = httpClientService.restPost(url, cmd, ListApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNull(myResponse.getNextPageAnchor());
		assertNotNull(myResponse.getApprovalRuleList());
		assertEquals(1, myResponse.getApprovalRuleList().size());
		
	}

	//16. 审批规则简短列表（完成）
	@Test
	public void testListBriefApprovalRule() {
		String url = LIST_BRIEF_APPROVAL_RULE_URL;
		logon();
		initListData();
		ListBriefApprovalRuleCommand cmd = new ListBriefApprovalRuleCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);

		ListBriefApprovalRuleRestResponse response = httpClientService.restPost(url, cmd, ListBriefApprovalRuleRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBriefApprovalRuleResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getApprovalRuleList());
		assertEquals(3, myResponse.getApprovalRuleList().size());
		assertNotNull(myResponse.getApprovalRuleList().get(0).getName());

	}

	//17. 同意申请（完成）
	@Test
	public void testApproveApprovalRequest() {
		String url = APPROVE_APPROVAL_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		createApprovalRequestBySceneAbsence2(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		
		approveApprovalRequest(url);

	}

	private void approveApprovalRequest(String url){
		List<EhApprovalRequests> dataList = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).fetch().map(r->ConvertHelper.convert(r, EhApprovalRequests.class));
		
		ApproveApprovalRequestCommand cmd = new ApproveApprovalRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		List<Long> longList = new ArrayList<>();
		dataList.forEach(d->longList.add(d.getId()));
		cmd.setRequestIdList(longList);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	
	//18. 驳回申请（完成）
	@Test
	public void testRejectApprovalRequest() {
		String url = REJECT_APPROVAL_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		createApprovalRequestBySceneAbsence2(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		rejectApprovalRequest(url);

	}

	private void rejectApprovalRequest(String url) {
		List<EhApprovalRequests> dataList = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).fetch().map(r->ConvertHelper.convert(r, EhApprovalRequests.class));
		
		RejectApprovalRequestCommand cmd = new RejectApprovalRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		List<Long> longList = new ArrayList<>();
		dataList.forEach(d->longList.add(d.getId()));
		cmd.setRequestIdList(longList);
		cmd.setReason("我是驳回");

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	
	//19. 获取申请的审批基本信息（完成）
	@Test
	public void testGetApprovalBasicInfoOfRequest() {
		String url = GET_APPROVAL_BASIC_INFO_OF_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		createApprovalRequestBySceneException1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		
		List<EhApprovalRequests> dataList = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).orderBy(Tables.EH_APPROVAL_REQUESTS.ID.asc()).fetch().map(r->ConvertHelper.convert(r, EhApprovalRequests.class));
		
		//请假的
		GetApprovalBasicInfoOfRequestCommand cmd = new GetApprovalBasicInfoOfRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setRequestId(dataList.get(0).getId());

		GetApprovalBasicInfoOfRequestRestResponse response = httpClientService.restPost(url, cmd, GetApprovalBasicInfoOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		GetApprovalBasicInfoOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequestDTO = myResponse.getApprovalBasicInfoOfRequest();
		assertNotNull(approvalBasicInfoOfRequestDTO);
		assertEquals(ApprovalType.ABSENCE.getCode(), approvalBasicInfoOfRequestDTO.getApproveType().byteValue());

		//异常申请的
		cmd.setRequestId(dataList.get(1).getId());

		response = httpClientService.restPost(url, cmd, GetApprovalBasicInfoOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		myResponse = response.getResponse();
		assertNotNull(myResponse);
		approvalBasicInfoOfRequestDTO = myResponse.getApprovalBasicInfoOfRequest();
		assertNotNull(approvalBasicInfoOfRequestDTO);
		assertEquals(ApprovalType.EXCEPTION.getCode(), approvalBasicInfoOfRequestDTO.getApproveType().byteValue());

	}

	//20. 获取申请的审批日志与审批流程列表（完成）
	@Test
	public void testListApprovalLogAndFlowOfRequest() {
		String url = LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneException1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		
		List<EhApprovalRequests> dataList = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).orderBy(Tables.EH_APPROVAL_REQUESTS.ID.asc()).fetch().map(r->ConvertHelper.convert(r, EhApprovalRequests.class));
		assertEquals(1, dataList.size());
		
		ListApprovalLogAndFlowOfRequestCommand cmd = new ListApprovalLogAndFlowOfRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setRequestId(dataList.get(0).getId());

		ListApprovalLogAndFlowOfRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalLogAndFlowOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogAndFlowOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalLogAndFlowOfRequestDTO> list = myResponse.getApprovalLogAndFlowOfRequestList();
		assertNotNull(list);
		assertEquals(3, list.size());
		
	}

	//21. 获取申请的审批日志列表（完成）
	@Test
	public void testListApprovalLogOfRequest() {
		String url = LIST_APPROVAL_LOG_OF_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneException1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		
		List<EhApprovalRequests> dataList = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).orderBy(Tables.EH_APPROVAL_REQUESTS.ID.asc()).fetch().map(r->ConvertHelper.convert(r, EhApprovalRequests.class));
		assertEquals(1, dataList.size());
		
		ListApprovalLogOfRequestCommand cmd = new ListApprovalLogOfRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setRequestId(dataList.get(0).getId());

		ListApprovalLogOfRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalLogOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalLogOfRequestDTO> list = myResponse.getApprovalLogOfRequestList();
		assertEquals(3, list.size());

	}

	//22. 获取申请的审批流程列表（完成）
	@Test
	public void testListApprovalFlowOfRequest() {
		String url = LIST_APPROVAL_FLOW_OF_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneException1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);

		List<EhApprovalRequests> dataList = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).orderBy(Tables.EH_APPROVAL_REQUESTS.ID.asc()).fetch().map(r->ConvertHelper.convert(r, EhApprovalRequests.class));
		assertEquals(1, dataList.size());
		
		ListApprovalFlowOfRequestCommand cmd = new ListApprovalFlowOfRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setRequestId(dataList.get(0).getId());

		ListApprovalFlowOfRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalFlowOfRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalFlowOfRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalFlowOfRequestDTO> list = myResponse.getApprovalFlowOfRequestList();
		assertEquals(2, list.size());

	}

	//23. 人员列表，可按部门、姓名筛选（完成）
	@Test
	public void testListApprovalUser() {
		String url = LIST_APPROVAL_USER_URL;
		logon();
		initListData();
		ListApprovalUserCommand cmd = new ListApprovalUserCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setFlowId(1L);
		cmd.setLevel((byte)1);
//		cmd.setDepartmentId(1L);
		cmd.setKeyword("堡");
		cmd.setPageSize(20);
//		cmd.setPageAnchor(1L);

		ListApprovalUserRestResponse response = httpClientService.restPost(url, cmd, ListApprovalUserRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalUserResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalUserDTO> list = myResponse.getApprovalUserList();
		assertEquals(1, list.size());
		

	}

	//24. 查询申请列表（完成）
	@Test
	public void testListApprovalRequest() {
		String url = LIST_APPROVAL_REQUEST_URL;
		logon();
		initListData();
		createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		createApprovalRequestBySceneAbsence2(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		
		ListApprovalRequestCommand cmd = new ListApprovalRequestCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
//		cmd.setCategoryId(1L);
//		cmd.setFromDate(1L);
//		cmd.setEndDate(1L);
//		cmd.setNickName("");
		cmd.setQueryType(ApprovalQueryType.WAITING_FOR_APPROVE.getCode());
		cmd.setPageSize(20);
//		cmd.setPageAnchor(4L);

		ListApprovalRequestRestResponse response = httpClientService.restPost(url, cmd, ListApprovalRequestRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalRequestResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<RequestDTO> listJson = myResponse.getListJson(); 
		assertEquals(2, listJson.size());

	}

	//25. 个人申请列表（客户端）（完成）
	@Test
	public void testListApprovalRequestByScene() {
		String url = LIST_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		createApprovalRequestBySceneAbsence2(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		
		ListApprovalRequestBySceneCommand cmd = new ListApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
//		cmd.setCategoryId(1L);
//		cmd.setPageAnchor(1L);
		cmd.setPageSize(20);

		ApprovalListApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<BriefApprovalRequestDTO> list = myResponse.getApprovalRequestList();
		assertEquals(2, list.size());
		
	}

	//26. 获取申请的审批基本信息（客户端）（完成）
	@Test
	public void testGetApprovalBasicInfoOfRequestByScene() {
		String url = GET_APPROVAL_BASIC_INFO_OF_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		String absenceToken = createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		String exceptionToken = createApprovalRequestBySceneException1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		
		//请假的
		GetApprovalBasicInfoOfRequestBySceneCommand cmd = new GetApprovalBasicInfoOfRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setRequestToken(absenceToken);

		ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetApprovalBasicInfoOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequestDTO = myResponse.getApprovalBasicInfoOfRequest();
		assertNotNull(approvalBasicInfoOfRequestDTO);
		assertEquals(ApprovalType.ABSENCE.getCode(), approvalBasicInfoOfRequestDTO.getApproveType().byteValue());
		
		//异常申请的
		cmd.setRequestToken(exceptionToken);

		response = httpClientService.restPost(url, cmd, ApprovalGetApprovalBasicInfoOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		myResponse = response.getResponse();
		assertNotNull(myResponse);
		approvalBasicInfoOfRequestDTO = myResponse.getApprovalBasicInfoOfRequest();
		assertNotNull(approvalBasicInfoOfRequestDTO);
		assertEquals(ApprovalType.EXCEPTION.getCode(), approvalBasicInfoOfRequestDTO.getApproveType().byteValue());
	}

	//27. 获取申请的审批日志与审批流程列表（客户端）（完成）
	@Test
	public void testListApprovalLogAndFlowOfRequestByScene() {
		String url = LIST_APPROVAL_LOG_AND_FLOW_OF_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		String requestToken = createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
//		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		rejectApprovalRequest(REJECT_APPROVAL_REQUEST_URL);
		
		ListApprovalLogAndFlowOfRequestBySceneCommand cmd = new ListApprovalLogAndFlowOfRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setRequestToken(requestToken);

		ApprovalListApprovalLogAndFlowOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalLogAndFlowOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogAndFlowOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalLogAndFlowOfRequestDTO> list = myResponse.getApprovalLogAndFlowOfRequestList();
		assertNotNull(list);
		assertEquals(3, list.size());
		
	}

	//28. 获取申请的审批日志列表（客户端）（完成）1
	@Test
	public void testListApprovalLogOfRequestByScene() {
		String url = LIST_APPROVAL_LOG_OF_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		String requestToken = createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		
		ListApprovalLogOfRequestBySceneCommand cmd = new ListApprovalLogOfRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setRequestToken(requestToken);

		ApprovalListApprovalLogOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalLogOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalLogOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalLogOfRequestDTO> list = myResponse.getApprovalLogOfRequestList();
		assertEquals(3, list.size());

	}

	//29. 获取申请的审批流程列表（客户端）（完成）
	@Test
	public void testListApprovalFlowOfRequestByScene() {
		String url = LIST_APPROVAL_FLOW_OF_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		String requestToken = createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		approveApprovalRequest(APPROVE_APPROVAL_REQUEST_URL);
		
		ListApprovalFlowOfRequestBySceneCommand cmd = new ListApprovalFlowOfRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setRequestToken(requestToken);

		ApprovalListApprovalFlowOfRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalFlowOfRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalFlowOfRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ApprovalFlowOfRequestDTO> list = myResponse.getApprovalFlowOfRequestList();
		assertEquals(2, list.size());

	}

	//30. 创建请假申请（客户端），跨天上下午（完成）
	@Test
	public void testCreateApprovalRequestBySceneAbsence1() {
		String url = CREATE_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		
		createApprovalRequestBySceneAbsence1(url);
	}

	private String createApprovalRequestBySceneAbsence1(String url) {
		//创建请假申请
		CreateApprovalRequestBySceneCommand cmd = new CreateApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
		cmd.setCategoryId(1L);
		cmd.setReason("我是请假原因");
		List<TimeRange> timeRangeList = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		timeRange.setType(TimeRangeType.ALL_DAY.getCode());
		timeRange.setFromTime(1474247173000L);
		timeRange.setEndTime(1474527973000L);
		timeRangeList.add(timeRange);
		cmd.setTimeRangeList(timeRangeList);
		List<AttachmentDescriptor> attachmentDescriptorList = new ArrayList<>();
		AttachmentDescriptor attachmentDescriptor = new AttachmentDescriptor();
		attachmentDescriptor.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor);
		AttachmentDescriptor attachmentDescriptor2 = new AttachmentDescriptor();
		attachmentDescriptor2.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor2.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor2);
		cmd.setAttachmentList(attachmentDescriptorList);

		ApprovalCreateApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalCreateApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals("3.3.21", myResponse.getApprovalRequest().getDescription());
		
		return myResponse.getApprovalRequest().getRequestToken();
	}
	
	//30. 创建请假申请（客户端），跨天包含周末（完成）
	@Test
	public void testCreateApprovalRequestBySceneAbsence2() {
		String url = CREATE_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		
		createApprovalRequestBySceneAbsence2(url);
	}
	
	private String createApprovalRequestBySceneAbsence2(String url) {
		//创建请假申请
		CreateApprovalRequestBySceneCommand cmd = new CreateApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
		cmd.setCategoryId(1L);
		cmd.setReason("我是请假原因2");
		List<TimeRange> timeRangeList = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		timeRange.setType(TimeRangeType.ALL_DAY.getCode());
		timeRange.setFromTime(1474592100000L);
		timeRange.setEndTime(1474962675000L);
		timeRangeList.add(timeRange);
		cmd.setTimeRangeList(timeRangeList);
		List<AttachmentDescriptor> attachmentDescriptorList = new ArrayList<>();
		AttachmentDescriptor attachmentDescriptor = new AttachmentDescriptor();
		attachmentDescriptor.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor);
		AttachmentDescriptor attachmentDescriptor2 = new AttachmentDescriptor();
		attachmentDescriptor2.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor2.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor2);
		cmd.setAttachmentList(attachmentDescriptorList);

		ApprovalCreateApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalCreateApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals("2.4.6", myResponse.getApprovalRequest().getDescription());
		
		return myResponse.getApprovalRequest().getRequestToken();
	}

	//30. 创建请假申请（客户端），不跨天（完成）
	@Test
	public void testCreateApprovalRequestBySceneAbsence3() {
		String url = CREATE_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		//创建请假申请
		CreateApprovalRequestBySceneCommand cmd = new CreateApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
		cmd.setCategoryId(1L);
		cmd.setReason("我是请假原因2");
		List<TimeRange> timeRangeList = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		timeRange.setType(TimeRangeType.ALL_DAY.getCode());
		timeRange.setFromTime(1474247173000L);
		timeRange.setEndTime(1474271475000L);
		timeRangeList.add(timeRange);
		cmd.setTimeRangeList(timeRangeList);
		List<AttachmentDescriptor> attachmentDescriptorList = new ArrayList<>();
		AttachmentDescriptor attachmentDescriptor = new AttachmentDescriptor();
		attachmentDescriptor.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor);
		AttachmentDescriptor attachmentDescriptor2 = new AttachmentDescriptor();
		attachmentDescriptor2.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor2.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor2);
		cmd.setAttachmentList(attachmentDescriptorList);

		ApprovalCreateApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalCreateApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		//assertEquals("0.4.6", myResponse.getApprovalRequest().getDescription());  //这边逻辑在2.0都改了，不能这样检查

	}

	//30. 创建请假申请（客户端），跨天同上午（完成）
	@Test
	public void testCreateApprovalRequestBySceneAbsence4() {
		String url = CREATE_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		//创建请假申请
		CreateApprovalRequestBySceneCommand cmd = new CreateApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());
		cmd.setCategoryId(1L);
		cmd.setReason("我是请假原因2");
		List<TimeRange> timeRangeList = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		timeRange.setType(TimeRangeType.ALL_DAY.getCode());
		timeRange.setFromTime(1474247173000L);
		timeRange.setEndTime(1474506075000L);
		timeRangeList.add(timeRange);
		cmd.setTimeRangeList(timeRangeList);
		List<AttachmentDescriptor> attachmentDescriptorList = new ArrayList<>();
		AttachmentDescriptor attachmentDescriptor = new AttachmentDescriptor();
		attachmentDescriptor.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor);
		AttachmentDescriptor attachmentDescriptor2 = new AttachmentDescriptor();
		attachmentDescriptor2.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor2.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor2);
		cmd.setAttachmentList(attachmentDescriptorList);

		ApprovalCreateApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalCreateApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals("3.0.0", myResponse.getApprovalRequest().getDescription());

	}

	//30. 创建异常申请（客户端）（完成）
	@Test
	public void testCreateApprovalRequestBySceneException1() {
		String url = CREATE_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		createApprovalRequestBySceneException1(url);
	}

	private String createApprovalRequestBySceneException1(String url) {
		//创建异常申请
		CreateApprovalRequestBySceneCommand cmd = new CreateApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.EXCEPTION.getCode());
		cmd.setReason("我是异常申请原因2");
		List<AttachmentDescriptor> attachmentDescriptorList = new ArrayList<>();
		AttachmentDescriptor attachmentDescriptor = new AttachmentDescriptor();
		attachmentDescriptor.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor);
		AttachmentDescriptor attachmentDescriptor2 = new AttachmentDescriptor();
		attachmentDescriptor2.setContentType(NewsCommentContentType.IMAGE.getCode());
		attachmentDescriptor2.setContentUri("http://content-1.zuolin.com:80/image/aW1hZ2UvTVRvMlpUTmhNVGRqTVRrNE0yUXpNR0k0WWpJM1pEUmhPVEUxWmpKbFpqaG1OQQ?token=K08C4RsCo8wl-S31M4wo3qLhkYBhpF-aHW_fjc7OyRj-Z_1EKEsaBTRVOtWeH8tXje_LMI7geEo_B_IYnzhwyiOMG_n3k_V9yKwJCnjtj-W-4LZsOnC4krMFe1l3OD8u");
		attachmentDescriptorList.add(attachmentDescriptor2);
		cmd.setAttachmentList(attachmentDescriptorList);
		ApprovalExceptionContent content = new ApprovalExceptionContent();
		content.setPunchDate(1474214400000L);
		content.setExceptionRequestType(ExceptionRequestType.ALL_DAY.getCode());
		content.setPunchDetail("9:00/无");
		content.setPunchStatusName("忘打卡");
		cmd.setContentJson(JSON.toJSONString(content));

		ApprovalCreateApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalCreateApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getApprovalRequest().getRequestToken());

		return myResponse.getApprovalRequest().getRequestToken();
	}
	
	//31. 取消申请（客户端）（完成）
	@Test
	public void testCancelApprovalRequestByScene() {
		String url = CANCEL_APPROVAL_REQUEST_BY_SCENE_URL;
		logon();
		initListData();
		String requestToken = createApprovalRequestBySceneException1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		CancelApprovalRequestBySceneCommand cmd = new CancelApprovalRequestBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setRequestToken(requestToken);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_APPROVAL_REQUESTS).fetchOne();
		assertNotNull(record);
		EhApprovalRequests requests = ConvertHelper.convert(record, EhApprovalRequests.class);
		assertEquals(CommonStatus.INACTIVE.getCode(), requests.getStatus().byteValue());

	}

	//32. 列出审批类别（客户端）（完成）
	@Test
	public void testListApprovalCategoryByScene() {
		String url = LIST_APPROVAL_CATEGORY_BY_SCENE_URL;
		logon();
		initListData();
		ListApprovalCategoryBySceneCommand cmd = new ListApprovalCategoryBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setApprovalType(ApprovalType.ABSENCE.getCode());

		ApprovalListApprovalCategoryBySceneRestResponse response = httpClientService.restPost(url, cmd, ApprovalListApprovalCategoryBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalCategoryBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategoryList());
		assertEquals(5, myResponse.getCategoryList().size());
		assertEquals("公出", myResponse.getCategoryList().get(0).getCategoryName());
		assertEquals("年休", myResponse.getCategoryList().get(4).getCategoryName());

	}
	
	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/1.0.0-approval-test-data-160907.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	private void initListData() {
		String jsonFilePath = "data/json/1.0.0-approval-test-data-list-160907.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}

	private String getSceneToken() {
		String uri = "/ui/user/listUserRelatedScenes";
		UserListUserRelatedScenesRestResponse response = httpClientService.restPost(uri, null,
				UserListUserRelatedScenesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<SceneDTO> list = response.getResponse();
		assertTrue("list size should be greater than 0", list != null && list.size() > 0);

		return list.get(0).getSceneToken();
	}
	
	@After
	public void tearDown() {
		logoff();
	}

	private void logon() {
		String userIdentifier = "tt";
		String plainTexPassword = "123456";
		Integer namespaceId = 999995;
		logon(namespaceId, userIdentifier, plainTexPassword);
	}
	 

	// 测试设置查看和删除规则
	@Test
	public void testTargetApprovalRule() {

		String url = ApprovalTest.UPDATE_TARGET_APPROVAL_RULE_URL;
		logon();
		// initListData();

		UpdateTargetApprovalRuleCommand cmd = new UpdateTargetApprovalRuleCommand();

		// 设两个级别的审批人
		List<ApprovalUser> approvalUserList = new ArrayList<ApprovalUser>();
		ApprovalUser approvalUser = new ApprovalUser();
		approvalUser.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser.setTargetId(1L);
		approvalUserList.add(approvalUser);
		ApprovalFlowLevelDTO fldto = new ApprovalFlowLevelDTO((byte) 1, approvalUserList);
		List<ApprovalUser> approvalUserList2 = new ArrayList<ApprovalUser>();
		ApprovalUser approvalUser2 = new ApprovalUser();
		approvalUser2.setTargetType(ApprovalTargetType.USER.getCode());
		approvalUser2.setTargetId(2L);
		approvalUserList2.add(approvalUser2);
		ApprovalFlowLevelDTO fldto2 = new ApprovalFlowLevelDTO((byte) 2, approvalUserList2);
		cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
		cmd.setOwnerId(1L);
		cmd.setTargetId(1L);
		cmd.setTargetType(ApprovalOwnerType.ORGANIZATION.getCode());

		List<RuleFlowMap> ruleFlowMapList = new ArrayList<>();

		RuleFlowMap ruleFlowMap = new RuleFlowMap();
		ruleFlowMap.setApprovalType(ApprovalType.ABSENCE.getCode());
		ruleFlowMap.setLevelList(new ArrayList<ApprovalFlowLevelDTO>());
		ruleFlowMap.getLevelList().add(fldto);
		ruleFlowMap.getLevelList().add(fldto2);
		ruleFlowMapList.add(ruleFlowMap);

		RuleFlowMap ruleFlowMap2 = new RuleFlowMap();
		ruleFlowMap2.setApprovalType(ApprovalType.EXCEPTION.getCode());
		ruleFlowMap2.setLevelList(ruleFlowMap.getLevelList());
		ruleFlowMapList.add(ruleFlowMap2);

		RuleFlowMap ruleFlowMap3 = new RuleFlowMap();
		ruleFlowMap3.setApprovalType(ApprovalType.OVERTIME.getCode());
		ruleFlowMap3.setLevelList(ruleFlowMap.getLevelList());
		ruleFlowMapList.add(ruleFlowMap3);

		cmd.setRuleFlowMapList(ruleFlowMapList);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		url = ApprovalTest.GET_TARGET_APPROVAL_RULE_URL;
		GetTargetApprovalRuleCommand getCmd = ConvertHelper.convert(cmd, GetTargetApprovalRuleCommand.class);
		GetTargetApprovalRuleRestResponse getResp = httpClientService.restPost(url, getCmd, GetTargetApprovalRuleRestResponse.class);

		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(getResp), httpClientService.isReponseSuccess(response));
		assertEquals(ruleFlowMapList.size(), getResp.getResponse().getRuleFlowMapList().size());

		url = ApprovalTest.DELETE_TARGET_APPROVAL_RULE_URL;
		RestResponseBase deleteResp = httpClientService.restPost(url, getCmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(deleteResp), httpClientService.isReponseSuccess(response));

		url = ApprovalTest.GET_TARGET_APPROVAL_RULE_URL;
		getCmd = ConvertHelper.convert(cmd, GetTargetApprovalRuleCommand.class);
		getResp = httpClientService.restPost(url, getCmd, GetTargetApprovalRuleRestResponse.class);

		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(getResp), httpClientService.isReponseSuccess(response));
		assertEquals(null, getResp.getResponse());
	}

	//   查看我的审批（客户端）（完成）
	@Test
	public void testListMyApprovalsByScene() {
		String url = LIST_MY_APPROVALS_URL;
		logon();
		initListData();
		createApprovalRequestBySceneAbsence1(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);
		createApprovalRequestBySceneAbsence2(CREATE_APPROVAL_REQUEST_BY_SCENE_URL);

		ListMyApprovalsBySceneCommand cmd = new ListMyApprovalsBySceneCommand();
		cmd.setSceneToken(getSceneToken());
		cmd.setQueryType(ApprovalQueryType.WAITING_FOR_APPROVE.getCode());;
		// cmd.setCategoryId(1L);
		// cmd.setPageAnchor(1L);
		cmd.setPageSize(20);
		
		ApprovalListApprovalRequestBySceneRestResponse response = httpClientService.restPost(url, cmd,
				ApprovalListApprovalRequestBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListApprovalRequestBySceneResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<BriefApprovalRequestDTO> list = myResponse.getApprovalRequestList();
		assertEquals(2, list.size());

	}
}
