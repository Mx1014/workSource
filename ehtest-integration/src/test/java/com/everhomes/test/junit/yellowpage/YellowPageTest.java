package com.everhomes.test.junit.yellowpage;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.GetServiceAllianceCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailRestResponse;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseListCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceRestResponse;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicRestResponse;
import com.everhomes.rest.yellowPage.ListServiceAllianceEnterpriseRestResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDefaultOrderCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDefaultOrderRestResponse;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDisplayFlagCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class YellowPageTest extends BaseLoginAuthTestCase {
	
	private static final String UPDATE_SA_CATEGORY_URI = "/yellowPage/updateServiceAllianceCategory";
	private static final String DELETE_SA_CATEGORY_URI = "/yellowPage/deleteServiceAllianceCategory";
	private static final String GET_SA_ENTERPRISE_DETAIL_URI = "/yellowPage/getServiceAllianceEnterpriseDetail";
	private static final String GET_SA_URI = "/yellowPage/getServiceAlliance";
	private static final String LIST_SA_ENTERPRISE_URI = "/yellowPage/ListServiceAllianceEnterprise";
	private static final String UPDATE_SA_URI = "/yellowPage/updateServiceAlliance";
	private static final String DELETE_SA_ENTERPRISE_URI = "/yellowPage/deleteServiceAllianceEnterprise";
	private static final String UPDATE_SA_ENTERPRISE_URI = "/yellowPage/updateServiceAllianceEnterprise";
	private static final String UPDATE_YELLOW_PAGE_URI = "/yellowPage/updateYellowPage";
	private static final String GET_YELLOW_PAGE_TOPIC = "/yellowPage/getYellowPageTopic";
	private static final String UPDATE_DEFAULT_ORDER = "/yellowPage/updateServiceAllianceEnterpriseDefaultOrder";
	private static final String UPDATE_DISPLAY_FLAG = "/yellowPage/updateServiceAllianceEnterpriseDisplayFlag";
	

	String ownerType = "community";
	Long ownerId = 240111044331048623L;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	
	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/yellowpage-test-data.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	@Test
	public void testUpdateSACategory() {
		String uri = UPDATE_SA_CATEGORY_URI;
		logon();

//		UpdateServiceAllianceCategoryCommand cmd = new UpdateServiceAllianceCategoryCommand();
//		cmd.setName("name");
//		cmd.setOwnerId(ownerId);
//		cmd.setOwnerType(ownerType);
//
//		StringRestResponse response = httpClientService.restPost(uri, cmd, StringRestResponse.class);
//		assertNotNull(response);
//		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
//
//		List<EhCategories> categories = getDbCategory();
//		assertNotNull(categories);
//		assertTrue("name".equals(categories.get(0).getName()));
	}
	
	@Test
	public void testDeleteSACategory() {
		String uri = DELETE_SA_CATEGORY_URI;
		logon();

		DeleteServiceAllianceCategoryCommand cmd = new DeleteServiceAllianceCategoryCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setCategoryId(1L);

		StringRestResponse response = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<EhCategories> categories = getDbCategoryById(1L);
		assertNotNull(categories);
		assertTrue(categories.get(0).getStatus() == 0);
	}
	
	@Test
	public void testGetSAEnterpriseDetail() {
		String uri = GET_SA_ENTERPRISE_DETAIL_URI;
		logon();

		GetServiceAllianceEnterpriseDetailCommand cmd = new GetServiceAllianceEnterpriseDetailCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setId(1L);

		GetServiceAllianceEnterpriseDetailRestResponse response = httpClientService.restPost(uri, cmd, GetServiceAllianceEnterpriseDetailRestResponse.class);
		assertNotNull(response);

		assertTrue("深圳市创卓企业管理顾问有限公司".equals(response.getResponse().getName()));
	}
	
	@Test
	public void testListServiceAllianceEnterprisel() {
		String uri = LIST_SA_ENTERPRISE_URI;
		logon();
		
		GetServiceAllianceEnterpriseListCommand cmd = new GetServiceAllianceEnterpriseListCommand();
		cmd.setOwnerId(1000001L);
		cmd.setOwnerType("organaization");
		cmd.setParentId(11L);
		cmd.setSourceRequestType((byte)1);
		
		ListServiceAllianceEnterpriseRestResponse response = httpClientService.restPost(uri, cmd, ListServiceAllianceEnterpriseRestResponse.class);
		System.out.println(response);
		assertNotNull(response);
		assertEquals(1, response.getResponse().getDtos().size());
		assertTrue("金融服务".equals(response.getResponse().getDtos().get(0).getServiceType()));
		
		GetServiceAllianceEnterpriseListCommand cmdapp = new GetServiceAllianceEnterpriseListCommand();
		cmdapp.setOwnerId(240111044331048623L);
		cmdapp.setOwnerType("community");
		cmdapp.setParentId(11L);
		cmdapp.setCategoryId(100011L);
		
		response = httpClientService.restPost(uri, cmdapp, ListServiceAllianceEnterpriseRestResponse.class);
		assertNotNull(response);
		assertEquals(5, response.getResponse().getDtos().size());
	}
	
	@Test
	public void testUpdateSAEnterprise() {
		String uri = UPDATE_SA_ENTERPRISE_URI;
		logon();
		
		deleteDbYP();
		UpdateServiceAllianceEnterpriseCommand cmd = new UpdateServiceAllianceEnterpriseCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setName("enterprise");
		
		StringRestResponse response = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		assertNotNull(response);
		
		List<EhYellowPages> yp = getDbYP();
		assertNotNull(yp);
		assertEquals(1, yp.size());
		assertTrue("enterprise".equals(yp.get(0).getName()));
		assertTrue(YellowPageStatus.ACTIVE.equals(YellowPageStatus.fromCode(yp.get(0).getStatus())));
		
		uri = DELETE_SA_ENTERPRISE_URI;
		
		DeleteServiceAllianceEnterpriseCommand command = new DeleteServiceAllianceEnterpriseCommand();
		command.setId(yp.get(0).getId());
		command.setOwnerId(ownerId);
		command.setOwnerType(ownerType);
		
		StringRestResponse resp = httpClientService.restPost(uri, command, StringRestResponse.class);
		assertNotNull(resp);
		
		yp = getDbYP();
		assertNotNull(yp);
		assertEquals(1, yp.size());
		assertTrue(YellowPageStatus.INACTIVE.equals(YellowPageStatus.fromCode(yp.get(0).getStatus())));
		
	}
	
	@Test
	public void testUpdateServiceAlliance() {
		String uri = UPDATE_SA_URI;
		logon();
		deleteDbYP();
		UpdateServiceAllianceCommand cmd = new UpdateServiceAllianceCommand();

		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setName("name");
		
		StringRestResponse response = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		uri = GET_SA_URI;
		GetServiceAllianceCommand command = new GetServiceAllianceCommand();
		command.setOwnerId(ownerId);
		command.setOwnerType(ownerType);
		GetServiceAllianceRestResponse resp = httpClientService.restPost(uri, command, GetServiceAllianceRestResponse.class);
		
		assertNotNull("The reponse of getting user info may not be null", resp);
		assertTrue("response= " + StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
        assertEquals("name", resp.getResponse().getName());
	}

	@After
	public void tearDown() {
		logoff();
	}
	
	@Test
	public void testUpdateYellowPage(){
		String uri = this.UPDATE_YELLOW_PAGE_URI;
		UpdateYellowPageCommand cmd = new UpdateYellowPageCommand();
		cmd.setId(1L);
		cmd.setBuildingId(2L);
		DSLContext context = dbProvider.getDslContext();
		EhYellowPages yellowPage = context.select().from(Tables.EH_YELLOW_PAGES).where(Tables.EH_YELLOW_PAGES.ID.eq(cmd.getId()))
				.fetch().map(r -> ConvertHelper.convert(r, EhYellowPages.class)).get(0);
		assertEquals(cmd.getBuildingId(), yellowPage.getBuildingId());
	}
	
	@Test
	public void testListYellowPageTopic(){
		String uri = this.GET_YELLOW_PAGE_TOPIC;
		GetYellowPageTopicCommand cmd = new GetYellowPageTopicCommand();
		cmd.setType((byte) 3);
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331048623L);
		GetYellowPageTopicRestResponse resp = httpClientService.restPost(uri, cmd, GetYellowPageTopicRestResponse.class);
		assertNotNull("The reponse of getting user info may not be null", resp);
		assertTrue("response= " + StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
        assertEquals("楼栋名-A座", resp.getResponse().getBuildingName());
        assertEquals(1, resp.getResponse().getBuildingId().intValue());
		
		
		
	}
	@Test
	public void testUpdateServiceAllianceEnterpriseDefaultOrder(){
		String uri = UPDATE_DEFAULT_ORDER;
		logon();
		UpdateServiceAllianceEnterpriseDefaultOrderCommand cmd = new UpdateServiceAllianceEnterpriseDefaultOrderCommand();
		List<ServiceAllianceDTO> values = new ArrayList<ServiceAllianceDTO>();
		ServiceAllianceDTO dto = new ServiceAllianceDTO();
		dto.setId(1L);
		ServiceAllianceDTO dto2 = new ServiceAllianceDTO();
		dto2.setId(200031L);
		
		values.add(dto2);
		values.add(dto);
		cmd.setValues(values);
		UpdateServiceAllianceEnterpriseDefaultOrderRestResponse resp = httpClientService.restPost(uri, cmd, UpdateServiceAllianceEnterpriseDefaultOrderRestResponse.class);
		
		assertNotNull(resp);
		assertNotNull(resp.getResponse());
		
		assertNotNull(resp.getResponse().getDtos());
		assertTrue(resp.getResponse().getDtos().size() ==2 );
		
		List<ServiceAllianceDTO> dtos = resp.getResponse().getDtos();
		assertTrue(dtos.get(0).getDefaultOrder() == 1L);
		assertTrue(dtos.get(1).getDefaultOrder() == 200031L);
	}
	
	@Test
	public void testUpdateServiceAllianceEnterpriseDisplayFlag(){
		String uri = UPDATE_DISPLAY_FLAG;
		logon();
		UpdateServiceAllianceEnterpriseDisplayFlagCommand cmd = new UpdateServiceAllianceEnterpriseDisplayFlagCommand();
		cmd.setId(1L);
		cmd.setDisplayFlag(DisplayFlagType.HIDE.getCode());
		StringRestResponse resp = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		
		DSLContext context = dbProvider.getDslContext();
		List<EhServiceAlliances> serviceAllianceList = context.select().from(Tables.EH_SERVICE_ALLIANCES).where(Tables.EH_SERVICE_ALLIANCES.ID.eq(1L)).fetch()
			.map(r->ConvertHelper.convert(r, EhServiceAlliances.class));
		
		assertNotNull(serviceAllianceList);
		assertTrue(serviceAllianceList.size() == 1);
		assertTrue(serviceAllianceList.get(0).getDisplayFlag() == DisplayFlagType.HIDE.getCode());
	}
	
	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}
	
	private List<EhYellowPages> getDbYP() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_YELLOW_PAGES).fetch().map(r -> ConvertHelper.convert(r, EhYellowPages.class));
	}
	
	private List<EhCategories> getDbCategory() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_CATEGORIES).
				where(Tables.EH_CATEGORIES.PARENT_ID.eq(CategoryConstants.CATEGORY_ID_YELLOW_PAGE))
				.fetch().map(r -> ConvertHelper.convert(r, EhCategories.class));
	}
	
	private List<EhCategories> getDbCategoryById(Long id) {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_CATEGORIES).
				where(Tables.EH_CATEGORIES.PARENT_ID.eq(CategoryConstants.CATEGORY_ID_YELLOW_PAGE))
				.and(Tables.EH_CATEGORIES.ID.eq(id))
				.fetch().map(r -> ConvertHelper.convert(r, EhCategories.class));
	}
	
	private void deleteDbCategory() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_CATEGORIES).execute();
	}
	
	private void deleteDbYP() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_YELLOW_PAGES).execute();
	}
}
