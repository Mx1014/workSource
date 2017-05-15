// @formatter:off
package com.everhomes.test.junit.talent;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.talent.ClearTalentQueryHistoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCategoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteTalentCategoryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.DeleteTalentQueryHistoryCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.GetTalentDetailRestResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListTalentCategoryCommand;
import com.everhomes.rest.talent.ListTalentCategoryResponse;
import com.everhomes.rest.talent.ListTalentCategoryRestResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentQueryHistoryRestResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.ListTalentRestResponse;
import com.everhomes.rest.talent.TalentCategoryDTO;
import com.everhomes.rest.talent.TopTalentCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class TalentTest extends BaseLoginAuthTestCase {
	//1. 分类列表
	private static final String LIST_TALENT_CATEGORY_URL = "/talent/listTalentCategory";
	//2. 新增或更新分类
	private static final String CREATE_OR_UPDATE_TALENT_CATEGORY_URL = "/talent/createOrUpdateTalentCategory";
	//3. 删除分类
	private static final String DELETE_TALENT_CATEGORY_URL = "/talent/deleteTalentCategory";
	//4. 人才列表
	private static final String LIST_TALENT_URL = "/talent/listTalent";
	//5. 创建或更新人才信息
	private static final String CREATE_OR_UPDATE_TALENT_URL = "/talent/createOrUpdateTalent";
	//6. 打开/关闭人才信息
	private static final String ENABLE_TALENT_URL = "/talent/enableTalent";
	//7. 删除人才信息
	private static final String DELETE_TALENT_URL = "/talent/deleteTalent";
	//8. 置顶人才信息
	private static final String TOP_TALENT_URL = "/talent/topTalent";
	//9. 导入人才信息
	private static final String IMPORT_TALENT_URL = "/talent/importTalent";
	//10. 获取人才信息详情
	private static final String GET_TALENT_DETAIL_URL = "/talent/getTalentDetail";
	//11. 人才信息查询记录
	private static final String LIST_TALENT_QUERY_HISTORY_URL = "/talent/listTalentQueryHistory";
	//12. 删除人才信息查询记录
	private static final String DELETE_TALENT_QUERY_HISTORY_URL = "/talent/deleteTalentQueryHistory";
	//13. 清空人才信息查询记录
	private static final String CLEAR_TALENT_QUERY_HISTORY_URL = "/talent/clearTalentQueryHistory";


	//1. 分类列表
	//@Test
	public void testListTalentCategory() {
		String url = LIST_TALENT_CATEGORY_URL;
		logon();

		ListTalentCategoryCommand cmd = new ListTalentCategoryCommand();
		cmd.setOrganizationId(1L);

		ListTalentCategoryRestResponse response = httpClientService.restPost(url, cmd, ListTalentCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTalentCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//2. 新增或更新分类
	//@Test
	public void testCreateOrUpdateTalentCategory() {
		String url = CREATE_OR_UPDATE_TALENT_CATEGORY_URL;
		logon();

		CreateOrUpdateTalentCategoryCommand cmd = new CreateOrUpdateTalentCategoryCommand();
		cmd.setOrganizationId(1L);
		List<TalentCategoryDTO> talentCategoryDTOList = new ArrayList<>();
		TalentCategoryDTO talentCategoryDTO = new TalentCategoryDTO();
		talentCategoryDTO.setId(1L);
		talentCategoryDTO.setName("");
		talentCategoryDTOList.add(talentCategoryDTO);
		cmd.setTalentCategories(talentCategoryDTOList);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//3. 删除分类
	//@Test
	public void testDeleteTalentCategory() {
		String url = DELETE_TALENT_CATEGORY_URL;
		logon();

		DeleteTalentCategoryCommand cmd = new DeleteTalentCategoryCommand();
		cmd.setOrganizationId(1L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//4. 人才列表
	//@Test
	public void testListTalent() {
		String url = LIST_TALENT_URL;
		logon();

		ListTalentCommand cmd = new ListTalentCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);
		cmd.setCategoryId(1L);
		cmd.setGender((byte)1);
		cmd.setExperience((byte)1);
		cmd.setDegree((byte)1);
		cmd.setKeyword("");
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);
		cmd.setHistoryFlag((byte)1);

		ListTalentRestResponse response = httpClientService.restPost(url, cmd, ListTalentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTalentResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//5. 创建或更新人才信息
	//@Test
	public void testCreateOrUpdateTalent() {
		String url = CREATE_OR_UPDATE_TALENT_URL;
		logon();

		CreateOrUpdateTalentCommand cmd = new CreateOrUpdateTalentCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);
		cmd.setId(1L);
		cmd.setName("");
		cmd.setAvatarUri("");
		cmd.setPosition("");
		cmd.setCategoryId(1L);
		cmd.setGender((byte)1);
		cmd.setExperience(0);
		cmd.setGraduateSchool("");
		cmd.setDegree((byte)1);
		cmd.setPhone("");
		cmd.setRemark("");

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//6. 打开/关闭人才信息
	//@Test
	public void testEnableTalent() {
		String url = ENABLE_TALENT_URL;
		logon();

		EnableTalentCommand cmd = new EnableTalentCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);
		cmd.setId(1L);
		cmd.setEnabled((byte)1);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//7. 删除人才信息
	//@Test
	public void testDeleteTalent() {
		String url = DELETE_TALENT_URL;
		logon();

		DeleteTalentCommand cmd = new DeleteTalentCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//8. 置顶人才信息
	//@Test
	public void testTopTalent() {
		String url = TOP_TALENT_URL;
		logon();

		TopTalentCommand cmd = new TopTalentCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//9. 导入人才信息
	//@Test
	public void testImportTalent() {
		String url = IMPORT_TALENT_URL;
		logon();

		ImportTalentCommand cmd = new ImportTalentCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//10. 获取人才信息详情
	//@Test
	public void testGetTalentDetail() {
		String url = GET_TALENT_DETAIL_URL;
		logon();

		GetTalentDetailCommand cmd = new GetTalentDetailCommand();
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setOrganizationId(1L);
		cmd.setId(1L);

		GetTalentDetailRestResponse response = httpClientService.restPost(url, cmd, GetTalentDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetTalentDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//11. 人才信息查询记录
	//@Test
	public void testListTalentQueryHistory() {
		String url = LIST_TALENT_QUERY_HISTORY_URL;
		logon();

		ListTalentQueryHistoryCommand cmd = new ListTalentQueryHistoryCommand();
		cmd.setOrganizationId(1L);

		ListTalentQueryHistoryRestResponse response = httpClientService.restPost(url, cmd, ListTalentQueryHistoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTalentQueryHistoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//12. 删除人才信息查询记录
	//@Test
	public void testDeleteTalentQueryHistory() {
		String url = DELETE_TALENT_QUERY_HISTORY_URL;
		logon();

		DeleteTalentQueryHistoryCommand cmd = new DeleteTalentQueryHistoryCommand();
		cmd.setOrganizationId(1L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//13. 清空人才信息查询记录
	//@Test
	public void testClearTalentQueryHistory() {
		String url = CLEAR_TALENT_QUERY_HISTORY_URL;
		logon();

		ClearTalentQueryHistoryCommand cmd = new ClearTalentQueryHistoryCommand();
		cmd.setOrganizationId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}



	
	
	
	
	
	
	
	
	
	

	@Before
	public void setUp() {
		super.newSetUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/1.0.0-questionnaire-test-data-170220.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	private void initListData() {
		String jsonFilePath = "data/json/1.0.0-approval-test-data-list-160907.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
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
