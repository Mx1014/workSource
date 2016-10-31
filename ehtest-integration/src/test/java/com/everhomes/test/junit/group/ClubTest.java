package com.everhomes.test.junit.group;

import java.util.List;

import org.junit.After;
import org.junit.Before;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.group.TransferCreatorPrivilegeCommand;
import com.everhomes.rest.ui.group.CreateGroupCategoryCommand;
import com.everhomes.rest.ui.group.CreateGroupCategoryResponse;
import com.everhomes.rest.ui.group.DeleteGroupCategoryCommand;
import com.everhomes.rest.ui.group.GetGroupParametersCommand;
import com.everhomes.rest.ui.group.GetGroupParametersResponse;
import com.everhomes.rest.ui.group.ListGroupCategoriesCommand;
import com.everhomes.rest.ui.group.ListGroupCategoriesResponse;
import com.everhomes.rest.ui.group.ListGroupsByApprovalStatusCommand;
import com.everhomes.rest.ui.group.ListGroupsByApprovalStatusResponse;
import com.everhomes.rest.ui.group.ListPostOfMyGroupCommand;
import com.everhomes.rest.ui.group.ListPostOfMyGroupResponse;
import com.everhomes.rest.ui.group.SetGroupParametersCommand;
import com.everhomes.rest.ui.group.SetGroupParametersResponse;
import com.everhomes.rest.ui.group.TransferPrivilegeCommand;
import com.everhomes.rest.ui.group.UpdateGroupCategoryCommand;
import com.everhomes.rest.ui.group.UpdateGroupCategoryResponse;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ClubTest extends BaseLoginAuthTestCase {
	//1. 查询我加入的俱乐部发的贴子
	private static final String LIST_POST_OF_MY_GROUP_URL = "/group/listPostOfMyGroup";
	//2. 转移创建者权限
	private static final String TRANSFER_PRIVILEGE_URL = "/group/transferCreatorPrivilege";
	//3. 设置俱乐部参数
	private static final String SET_GROUP_PARAMETERS_URL = "/group/setGroupParameters";
	//4. 获取俱乐部参数
	private static final String GET_GROUP_PARAMETERS_URL = "/group/getGroupParameters";
	//5. 按审核状态查询圈
	private static final String LIST_GROUPS_BY_APPROVAL_STATUS_URL = "/group/listGroupsByApprovalStatus";
	//6. 创建圈分类
	private static final String CREATE_GROUP_CATEGORY_URL = "/group/createGroupCategory";
	//7. 更新圈分类
	private static final String UPDATE_GROUP_CATEGORY_URL = "/group/updateGroupCategory";
	//8. 删除圈分类
	private static final String DELETE_GROUP_CATEGORY_URL = "/group/deleteGroupCategory";
	//9. 列出圈分类
	private static final String LIST_GROUP_CATEGORIES_URL = "/group/listGroupCategories";


	//1. 查询我加入的俱乐部发的贴子
	//@Test
	public void testListPostOfMyGroup() {
		String url = LIST_POST_OF_MY_GROUP_URL;
		logon();
		ListPostOfMyGroupCommand cmd = new ListPostOfMyGroupCommand();
		cmd.setSceneToken("");
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		ListPostOfMyGroupBySceneRestResponse response = httpClientService.restPost(url, cmd, ListPostOfMyGroupBySceneRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListPostOfMyGroupResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//2. 转移创建者权限
	//@Test
	public void testTransferPrivilege() {
		String url = TRANSFER_PRIVILEGE_URL;
		logon();
		TransferCreatorPrivilegeCommand cmd = new TransferCreatorPrivilegeCommand();
		cmd.setGroupId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//3. 设置俱乐部参数
	//@Test
	public void testSetGroupParameters() {
		String url = SET_GROUP_PARAMETERS_URL;
		logon();
		SetGroupParametersCommand cmd = new SetGroupParametersCommand();
		cmd.setNamespaceId(0);
		cmd.setAllowCreate((byte)1);
		cmd.setJoinPolicy(0);
		cmd.setAllowMemberPost((byte)1);
		cmd.setAllowMemberComment((byte)1);
		cmd.setAllowAdminBroadcast((byte)1);
		cmd.setBroadcastCountPerDay(0);

		SetGroupParametersRestResponse response = httpClientService.restPost(url, cmd, SetGroupParametersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		SetGroupParametersResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//4. 获取俱乐部参数
	//@Test
	public void testGetGroupParameters() {
		String url = GET_GROUP_PARAMETERS_URL;
		logon();
		GetGroupParametersCommand cmd = new GetGroupParametersCommand();
		cmd.setNamespaceId(0);

		GetGroupParametersRestResponse response = httpClientService.restPost(url, cmd, GetGroupParametersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetGroupParametersResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//5. 按审核状态查询圈
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

	//6. 创建圈分类
	//@Test
	public void testCreateGroupCategory() {
		String url = CREATE_GROUP_CATEGORY_URL;
		logon();
		CreateGroupCategoryCommand cmd = new CreateGroupCategoryCommand();
		cmd.setNamespaceId(0);
		cmd.setCategoryName("");

		CreateGroupCategoryRestResponse response = httpClientService.restPost(url, cmd, CreateGroupCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateGroupCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//7. 更新圈分类
	//@Test
	public void testUpdateGroupCategory() {
		String url = UPDATE_GROUP_CATEGORY_URL;
		logon();
		UpdateGroupCategoryCommand cmd = new UpdateGroupCategoryCommand();
		cmd.setNamespaceId(0);
		cmd.setCategoryId(1L);
		cmd.setCategoryName("");

		UpdateGroupCategoryRestResponse response = httpClientService.restPost(url, cmd, UpdateGroupCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		UpdateGroupCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//8. 删除圈分类
	//@Test
	public void testDeleteGroupCategory() {
		String url = DELETE_GROUP_CATEGORY_URL;
		logon();
		DeleteGroupCategoryCommand cmd = new DeleteGroupCategoryCommand();
		cmd.setNamespaceId(0);
		cmd.setCategoryId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//9. 列出圈分类
	//@Test
	public void testListGroupCategories() {
		String url = LIST_GROUP_CATEGORIES_URL;
		logon();
		ListGroupCategoriesCommand cmd = new ListGroupCategoriesCommand();
		cmd.setNamespaceId(0);

		ListGroupCategoriesRestResponse response = httpClientService.restPost(url, cmd, ListGroupCategoriesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListGroupCategoriesResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}


	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/1.0.0-activity-test-data-161018.txt";
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
