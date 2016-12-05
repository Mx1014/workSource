package com.everhomes.test.junit.group;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.group.ApprovalGroupRequestCommand;
import com.everhomes.rest.group.CreateBroadcastCommand;
import com.everhomes.rest.group.CreateBroadcastResponse;
import com.everhomes.rest.group.CreateBroadcastRestResponse;
import com.everhomes.rest.group.CreateGroupCategoryCommand;
import com.everhomes.rest.group.CreateGroupCategoryResponse;
import com.everhomes.rest.group.CreateGroupCategoryRestResponse;
import com.everhomes.rest.group.DeleteGroupCategoryCommand;
import com.everhomes.rest.group.GetBroadcastByTokenCommand;
import com.everhomes.rest.group.GetBroadcastByTokenResponse;
import com.everhomes.rest.group.GetBroadcastByTokenRestResponse;
import com.everhomes.rest.group.GetGroupParametersCommand;
import com.everhomes.rest.group.GroupParametersResponse;
import com.everhomes.rest.group.ListBroadcastsCommand;
import com.everhomes.rest.group.ListBroadcastsResponse;
import com.everhomes.rest.group.ListBroadcastsRestResponse;
import com.everhomes.rest.group.ListGroupCategoriesCommand;
import com.everhomes.rest.group.ListGroupCategoriesResponse;
import com.everhomes.rest.group.ListGroupCategoriesRestResponse;
import com.everhomes.rest.group.ListGroupsByApprovalStatusCommand;
import com.everhomes.rest.group.ListGroupsByApprovalStatusResponse;
import com.everhomes.rest.group.ListGroupsByApprovalStatusRestResponse;
import com.everhomes.rest.group.ListUserGroupPostCommand;
import com.everhomes.rest.group.ListUserGroupPostResponse;
import com.everhomes.rest.group.ListUserGroupPostRestResponse;
import com.everhomes.rest.group.RejectGroupRequestCommand;
import com.everhomes.rest.group.SetGroupParametersCommand;
import com.everhomes.rest.group.SetGroupParametersRestResponse;
import com.everhomes.rest.group.TransferCreatorPrivilegeCommand;
import com.everhomes.rest.group.UpdateGroupCategoryCommand;
import com.everhomes.rest.group.UpdateGroupCategoryResponse;
import com.everhomes.rest.group.UpdateGroupCategoryRestResponse;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhBroadcasts;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class ClubTest extends BaseLoginAuthTestCase {
	//1. 查询我加入的俱乐部发的贴子
	private static final String LIST_USER_GROUP_POST_URL = "/group/listUserGroupPost";
	//2. 转移创建者权限
	private static final String TRANSFER_CREATOR_PRIVILEGE_URL = "/group/transferCreatorPrivilege";
	//3. 创建广播消息
	private static final String CREATE_BROADCAST_URL = "/group/createBroadcast";
	//4. 获取广播详情
	private static final String GET_BROADCAST_BY_TOKEN_URL = "/group/getBroadcastByToken";
	//5. 列出广播消息
	private static final String LIST_BROADCASTS_URL = "/group/listBroadcasts";
	//6. 设置俱乐部参数
	private static final String SET_GROUP_PARAMETERS_URL = "/group/setGroupParameters";
	//7. 获取俱乐部参数
	private static final String GET_GROUP_PARAMETERS_URL = "/group/getGroupParameters";
	//8. 按审核状态查询圈
	private static final String LIST_GROUPS_BY_APPROVAL_STATUS_URL = "/group/listGroupsByApprovalStatus";
	//9. 审核通过俱乐部申请
	private static final String APPROVAL_GROUP_REQUEST_URL = "/group/approvalGroupRequest";
	//10. 拒绝俱乐部申请
	private static final String REJECT_GROUP_REQUEST_URL = "/group/rejectGroupRequest";
	//11. 创建圈分类
	private static final String CREATE_GROUP_CATEGORY_URL = "/group/createGroupCategory";
	//12. 更新圈分类
	private static final String UPDATE_GROUP_CATEGORY_URL = "/group/updateGroupCategory";
	//13. 删除圈分类
	private static final String DELETE_GROUP_CATEGORY_URL = "/group/deleteGroupCategory";
	//14. 列出圈分类
	private static final String LIST_GROUP_CATEGORIES_URL = "/group/listGroupCategories";


	//1. 查询我加入的俱乐部发的贴子
	//@Test
	public void testListUserGroupPost() {
		String url = LIST_USER_GROUP_POST_URL;
		logon();
		ListUserGroupPostCommand cmd = new ListUserGroupPostCommand();
		cmd.setPageSize(20);

		ListUserGroupPostRestResponse response = httpClientService.restPost(url, cmd, ListUserGroupPostRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListUserGroupPostResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getPosts());
		assertEquals(2, myResponse.getPosts().size());
		assertEquals(2L, myResponse.getPosts().get(0).getId().longValue());

	}

	//2. 转移创建者权限
	//@Test
	public void testTransferCreatorPrivilege() {
		String url = TRANSFER_CREATOR_PRIVILEGE_URL;
		logon();
		TransferCreatorPrivilegeCommand cmd = new TransferCreatorPrivilegeCommand();
		cmd.setGroupId(1L);
		cmd.setUserId(2L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

	}

	//3. 创建广播消息
	//@Test
	public void testCreateBroadcast() {
		String url = CREATE_BROADCAST_URL;
		logon();
		
		CreateBroadcastResponse myResponse = createBroadcast(url);

		List<EhBroadcasts> ehBroadcastList = dbProvider.getDslContext().select().from(Tables.EH_BROADCASTS)
			.where(Tables.EH_BROADCASTS.TITLE.eq("b1"))
			.fetch()
			.map(r->ConvertHelper.convert(r, EhBroadcasts.class));
		
		assertNotNull(ehBroadcastList);
		assertEquals(1, ehBroadcastList.size());
		assertEquals(myResponse.getBroadcast().getContent(), ehBroadcastList.get(0).getContent());
	}

	private CreateBroadcastResponse createBroadcast(String url) {
		CreateBroadcastCommand cmd = new CreateBroadcastCommand();
		cmd.setOwnerType("group");
		cmd.setOwnerId(1L);
		cmd.setTitle("b1");
		cmd.setContentType("text");
		cmd.setContent("bbbbbbbbbbbbbb");

		CreateBroadcastRestResponse response = httpClientService.restPost(url, cmd, CreateBroadcastRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateBroadcastResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getBroadcast());
		assertNotNull(myResponse.getBroadcast().getTitle());
		
		return myResponse;
	}
	
	//4. 获取广播详情
	//@Test
	public void testGetBroadcastByToken() {
		String url = GET_BROADCAST_BY_TOKEN_URL;
		logon();
		CreateBroadcastResponse createBroadcastResponse = createBroadcast(CREATE_BROADCAST_URL);
		GetBroadcastByTokenCommand cmd = new GetBroadcastByTokenCommand();
		cmd.setBroadcastToken(createBroadcastResponse.getBroadcast().getBroadcastToken());

		GetBroadcastByTokenRestResponse response = httpClientService.restPost(url, cmd, GetBroadcastByTokenRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetBroadcastByTokenResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getBroadcast());
		assertEquals(myResponse.getBroadcast().getContent(), createBroadcastResponse.getBroadcast().getContent());

	}

	//5. 列出广播消息
	//@Test
	public void testListBroadcasts() {
		String url = LIST_BROADCASTS_URL;
		logon();
		ListBroadcastsCommand cmd = new ListBroadcastsCommand();
		cmd.setOwnerType("group");
		cmd.setOwnerId(1L);
		cmd.setPageSize(2);

		ListBroadcastsRestResponse response = httpClientService.restPost(url, cmd, ListBroadcastsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBroadcastsResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getBroadcasts());
		assertEquals(12L, myResponse.getNextPageAnchor().longValue());
		assertEquals("xxx13", myResponse.getBroadcasts().get(0).getTitle());

	}

	//6. 设置俱乐部参数
	//@Test
	public void testSetGroupParameters() {
		String url = SET_GROUP_PARAMETERS_URL;
		logon();
		SetGroupParametersCommand cmd = new SetGroupParametersCommand();
		cmd.setNamespaceId(999995);
		cmd.setCreateFlag((byte)1);
		cmd.setVerifyFlag((byte)1);
		cmd.setMemberPostFlag((byte)1);
		cmd.setMemberCommentFlag((byte)1);
		cmd.setAdminBroadcastFlag((byte)1);
		cmd.setBroadcastCount(5);

		SetGroupParametersRestResponse response = httpClientService.restPost(url, cmd, SetGroupParametersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GroupParametersResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getAdminBroadcastFlag());
		assertEquals((byte)1, myResponse.getAdminBroadcastFlag().byteValue());
		assertEquals(5, myResponse.getBroadcastCount().intValue());

	}

	//7. 获取俱乐部参数
	//@Test
	public void testGetGroupParameters() {
		String url = GET_GROUP_PARAMETERS_URL;
		logon();
		GetGroupParametersCommand cmd = new GetGroupParametersCommand();
		cmd.setNamespaceId(999996);

		SetGroupParametersRestResponse response = httpClientService.restPost(url, cmd, SetGroupParametersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GroupParametersResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getAdminBroadcastFlag());
		assertEquals((byte)1, myResponse.getAdminBroadcastFlag().byteValue());
		assertEquals(5, myResponse.getBroadcastCount().intValue());

	}

	//11. 创建圈分类
	//@Test
	public void testCreateGroupCategory() {
		String url = CREATE_GROUP_CATEGORY_URL;
		logon();
		CreateGroupCategoryCommand cmd = new CreateGroupCategoryCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType("organization");
		cmd.setOwnerId(1L);
		cmd.setCategoryName("我爱运动");

		CreateGroupCategoryRestResponse response = httpClientService.restPost(url, cmd, CreateGroupCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateGroupCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategory());
		assertEquals("我爱运动", myResponse.getCategory().getCategoryName());

	}

	//12. 更新圈分类
	//@Test
	public void testUpdateGroupCategory() {
		String url = UPDATE_GROUP_CATEGORY_URL;
		logon();
		UpdateGroupCategoryCommand cmd = new UpdateGroupCategoryCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType("organization");
		cmd.setOwnerId(1L);
		cmd.setCategoryId(111L);
		cmd.setCategoryName("我爱哈哈");

		UpdateGroupCategoryRestResponse response = httpClientService.restPost(url, cmd, UpdateGroupCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateGroupCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategory());
		assertEquals("我爱哈哈", myResponse.getCategory().getCategoryName());

	}

	//13. 删除圈分类
	//@Test
	public void testDeleteGroupCategory() {
		String url = DELETE_GROUP_CATEGORY_URL;
		logon();
		DeleteGroupCategoryCommand cmd = new DeleteGroupCategoryCommand();
		cmd.setNamespaceId(999995);
		cmd.setOwnerType("organization");
		cmd.setOwnerId(1L);
		cmd.setCategoryId(111L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

	}

	//14. 列出圈分类
	//@Test
	public void testListGroupCategories() {
		String url = LIST_GROUP_CATEGORIES_URL;
		logon();
		ListGroupCategoriesCommand cmd = new ListGroupCategoriesCommand();
		cmd.setNamespaceId(999995);

		ListGroupCategoriesRestResponse response = httpClientService.restPost(url, cmd, ListGroupCategoriesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListGroupCategoriesResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getCategories());
		assertEquals(3, myResponse.getCategories().size());

	}

	//8. 按审核状态查询圈
	//@Test
	public void testListGroupsByApprovalStatus() {
		String url = LIST_GROUPS_BY_APPROVAL_STATUS_URL;
		logon();
		ListGroupsByApprovalStatusCommand cmd = new ListGroupsByApprovalStatusCommand();
		cmd.setNamespaceId(0);
		cmd.setApprovalStatus((byte)1);
		cmd.setPrivateFlag((byte)1);
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		ListGroupsByApprovalStatusRestResponse response = httpClientService.restPost(url, cmd, ListGroupsByApprovalStatusRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListGroupsByApprovalStatusResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//9. 审核通过俱乐部申请
	//@Test
	public void testApprovalGroupRequest() {
		String url = APPROVAL_GROUP_REQUEST_URL;
		logon();
		ApprovalGroupRequestCommand cmd = new ApprovalGroupRequestCommand();
		cmd.setGroupId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//10. 拒绝俱乐部申请
	//@Test
	public void testRejectGroupRequest() {
		String url = REJECT_GROUP_REQUEST_URL;
		logon();
		RejectGroupRequestCommand cmd = new RejectGroupRequestCommand();
		cmd.setGroupId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}





	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/club-2.0.0-test-data-161101.txt";
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

}
