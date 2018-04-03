// @formatter:off
package com.everhomes.test.junit.talent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.approval.CommonStatus;
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
import com.everhomes.rest.talent.TalentDTO;
import com.everhomes.rest.talent.TalentQueryHistoryDTO;
import com.everhomes.rest.talent.TopTalentCommand;
import com.everhomes.server.schema.Tables;
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


	//1. 分类列表（已完成）
	@Test
	public void testListTalentCategory() {
		String url = LIST_TALENT_CATEGORY_URL;
		logon();

		ListTalentCategoryCommand cmd = new ListTalentCategoryCommand();
		cmd.setOrganizationId(1000750L);

		ListTalentCategoryRestResponse response = httpClientService.restPost(url, cmd, ListTalentCategoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTalentCategoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<TalentCategoryDTO> list = myResponse.getTalentCategories();
		assertNotNull(list);
		assertTrue(list.size() > 0);

	}

	//2. 新增或更新分类（已完成）
	@Test
	public void testCreateOrUpdateTalentCategory() {
		String url = CREATE_OR_UPDATE_TALENT_CATEGORY_URL;
		logon();

		CreateOrUpdateTalentCategoryCommand cmd = new CreateOrUpdateTalentCategoryCommand();
		cmd.setOrganizationId(1000750L);
		List<TalentCategoryDTO> talentCategoryDTOList = new ArrayList<>();
		TalentCategoryDTO talentCategoryDTO = new TalentCategoryDTO();
		talentCategoryDTO.setName("我是分类");
		talentCategoryDTOList.add(talentCategoryDTO);
		TalentCategoryDTO talentCategoryDTO2 = new TalentCategoryDTO();
		talentCategoryDTO2.setName("我是分类2");
		talentCategoryDTOList.add(talentCategoryDTO2);
		cmd.setTalentCategories(talentCategoryDTOList);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> result = dbProvider.getDslContext().select().from(Tables.EH_TALENT_CATEGORIES).where(Tables.EH_TALENT_CATEGORIES.NAME.like("我是分类%")).fetch();
		assertEquals(2, result.size());

	}

	//3. 删除分类（已完成）
	@Test
	public void testDeleteTalentCategory() {
		String url = DELETE_TALENT_CATEGORY_URL;
		logon();

		DeleteTalentCategoryCommand cmd = new DeleteTalentCategoryCommand();
		cmd.setOrganizationId(1000750L);
		cmd.setIds(Arrays.asList(1L));

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_TALENT_CATEGORIES).where(Tables.EH_TALENT_CATEGORIES.ID.eq(1L)).fetchOne();
		assertNotNull(record);
		assertEquals(CommonStatus.INACTIVE.getCode(), record.getValue(Tables.EH_TALENT_CATEGORIES.STATUS).byteValue());

	}

	//4. 人才列表（已完成）
	@Test
	public void testListTalent() {
		String url = LIST_TALENT_URL;
		logon();

		ListTalentCommand cmd = new ListTalentCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
//		cmd.setCategoryId(1L);
//		cmd.setGender((byte)1);
//		cmd.setExperience((byte)1);
//		cmd.setDegree((byte)1);
//		cmd.setKeyword("分类");
//		cmd.setPageAnchor(0L);
		cmd.setPageSize(2);
//		cmd.setHistoryFlag((byte)1);

		ListTalentRestResponse response = httpClientService.restPost(url, cmd, ListTalentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTalentResponse myResponse = response.getResponse();
		assertNotNull(myResponse);

		List<TalentDTO> list = myResponse.getTalents();
		assertNotNull(list);
		assertEquals(2, list.size());
	}

	//5. 创建或更新人才信息（已完成）
//	@Test
	public void testCreateOrUpdateTalent() {
		String url = CREATE_OR_UPDATE_TALENT_URL;
		logon();

		CreateOrUpdateTalentCommand cmd = new CreateOrUpdateTalentCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
//		cmd.setId(1L);
		cmd.setName("tt_testxx");
		cmd.setAvatarUri("https://www.baidu.com");
		cmd.setPosition("软件");
		cmd.setCategoryId(1L);
		cmd.setGender((byte)1);
		cmd.setExperience(10);
		cmd.setGraduateSchool("中山大学");
		cmd.setDegree((byte)1);
		cmd.setPhone("13265767391");
		cmd.setRemark("我是详情");

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_TALENTS).where(Tables.EH_TALENTS.NAME.eq("tt_testxx")).fetchOne();
		assertNotNull(record);
		assertEquals("13265767391", record.getValue(Tables.EH_TALENTS.PHONE));

	}

	//6. 打开/关闭人才信息（已完成）
	@Test
	public void testEnableTalent() {
		String url = ENABLE_TALENT_URL;
		logon();

		EnableTalentCommand cmd = new EnableTalentCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
		cmd.setId(2L);
		cmd.setEnabled((byte)0);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_TALENTS).where(Tables.EH_TALENTS.ID.eq(2L)).fetchOne();
		assertNotNull(record);
		assertEquals((byte)0, record.getValue(Tables.EH_TALENTS.ENABLED).byteValue());
		
	}

	//7. 删除人才信息（已完成）
	@Test
	public void testDeleteTalent() {
		String url = DELETE_TALENT_URL;
		logon();

		DeleteTalentCommand cmd = new DeleteTalentCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_TALENTS).where(Tables.EH_TALENTS.ID.eq(1L)).fetchOne();
		assertNotNull(record);
		assertEquals(CommonStatus.INACTIVE.getCode(), record.getValue(Tables.EH_TALENTS.STATUS).byteValue());

	}

	//8. 置顶人才信息（已完成）
	@Test
	public void testTopTalent() {
		String url = TOP_TALENT_URL;
		logon();

		Record record1 = dbProvider.getDslContext().select().from(Tables.EH_TALENTS).where(Tables.EH_TALENTS.ID.eq(2L)).fetchOne();
		assertNotNull(record1);
		
		TopTalentCommand cmd = new TopTalentCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
		cmd.setId(2L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record2 = dbProvider.getDslContext().select().from(Tables.EH_TALENTS).orderBy(Tables.EH_TALENTS.ID.desc()).limit(1).fetchOne();
		assertNotNull(record2);
		assertEquals(record1.getValue(Tables.EH_TALENTS.NAME), record2.getValue(Tables.EH_TALENTS.NAME));
		
	}

	//9. 导入人才信息
	@Test
	public void testImportTalent() throws IOException {
		String url = IMPORT_TALENT_URL;
		logon();

		ImportTalentCommand cmd = new ImportTalentCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
		File file = new File(new File("").getCanonicalPath() + "/src/test/data/excel/talent_template.xlsx");
		RestResponseBase response = httpClientService.postFile(url, cmd, file, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> result = dbProvider.getDslContext().select().from(Tables.EH_TALENTS).where(Tables.EH_TALENTS.NAME.like("import%")).fetch();
		assertEquals(3, result.size());
	}

	//10. 获取人才信息详情（已完成）
	@Test
	public void testGetTalentDetail() {
		String url = GET_TALENT_DETAIL_URL;
		logon();

		GetTalentDetailCommand cmd = new GetTalentDetailCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051304L);
		cmd.setOrganizationId(1000750L);
		cmd.setId(1L);

		GetTalentDetailRestResponse response = httpClientService.restPost(url, cmd, GetTalentDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetTalentDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		TalentDTO talentDTO = myResponse.getTalent();
		assertNotNull(talentDTO);
		assertNotNull(talentDTO.getName());
		
	}

	//11. 人才信息查询记录（已完成）
	@Test
	public void testListTalentQueryHistory() {
		String url = LIST_TALENT_QUERY_HISTORY_URL;
		logon();

		ListTalentQueryHistoryCommand cmd = new ListTalentQueryHistoryCommand();
		cmd.setOrganizationId(1000750L);

		ListTalentQueryHistoryRestResponse response = httpClientService.restPost(url, cmd, ListTalentQueryHistoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTalentQueryHistoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);

		List<TalentQueryHistoryDTO> list = myResponse.getTalentQueryHistories();
		assertNotNull(list);
		assertEquals(3, list.size());
		
	}

	//12. 删除人才信息查询记录（已完成）
	@Test
	public void testDeleteTalentQueryHistory() {
		String url = DELETE_TALENT_QUERY_HISTORY_URL;
		logon();

		DeleteTalentQueryHistoryCommand cmd = new DeleteTalentQueryHistoryCommand();
		cmd.setOrganizationId(1000750L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_TALENT_QUERY_HISTORIES).where(Tables.EH_TALENT_QUERY_HISTORIES.ID.eq(1L)).fetchOne();
		assertNotNull(record);
		assertEquals(CommonStatus.INACTIVE.getCode(), record.getValue(Tables.EH_TALENT_QUERY_HISTORIES.STATUS).byteValue());

	}

	//13. 清空人才信息查询记录（已完成）
	@Test
	public void testClearTalentQueryHistory() {
		String url = CLEAR_TALENT_QUERY_HISTORY_URL;
		logon();

		ClearTalentQueryHistoryCommand cmd = new ClearTalentQueryHistoryCommand();
		cmd.setOrganizationId(1000750L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_TALENT_QUERY_HISTORIES).where(Tables.EH_TALENT_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode())).fetchOne();
		assertNull(record);

	}
	
	
	

	@Before
	public void setUp() {
		super.newSetUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/talent-1.0.0-test-data-170515.txt";
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
		Integer namespaceId = 999992;
		logon(namespaceId, userIdentifier, plainTexPassword);
	}
}
