package com.everhomes.test.officecubicle;

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.CityDTO;
import com.everhomes.rest.officecubicle.DeleteSpaceCommand;
import com.everhomes.rest.officecubicle.OfficeAttachmentDTO;
import com.everhomes.rest.officecubicle.OfficeCategoryDTO;
import com.everhomes.rest.officecubicle.OfficeRentType;
import com.everhomes.rest.officecubicle.OfficeSpaceDTO;
import com.everhomes.rest.officecubicle.OfficeSpaceType;
import com.everhomes.rest.officecubicle.OfficeStatus;
import com.everhomes.rest.officecubicle.QueryCitiesRestResponse;
import com.everhomes.rest.officecubicle.QuerySpacesCommand;
import com.everhomes.rest.officecubicle.QuerySpacesRestResponse;
import com.everhomes.rest.officecubicle.admin.AddSpaceCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpacesRestResponse;
import com.everhomes.rest.officecubicle.admin.UpdateSpaceCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleAttachments;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleCategories;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSpaces;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class OfficeCubicleSpaceTest extends BaseLoginAuthTestCase {

	Integer namespaceId = 999989;
	String userIdentifier = "10002";
	String plainTexPassword = "123456";

	@Before
	public void setUp() {
		super.setUp();
	}

	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}

	@Test
	public void addSpaceTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/addSpace";

		AddSpaceCommand cmd = new AddSpaceCommand();
		cmd.setName("空间名称");
		cmd.setProvinceId(100L);
		cmd.setProvinceName("Shandong");
		cmd.setCityId(100100L);
		cmd.setCityName("Jinan");
		cmd.setAddress("HTC 32F A QUEEN ROAD JINAN SHANDONG CHINA");
		cmd.setLatitude(133.332);
		cmd.setLongitude(62.445);
		cmd.setContactPhone("055-8898754");
		cmd.setManagerUid(1L);
		cmd.setDescription("<b><h1> this is a </h1> <br> <p>what the fuck </p><br></b>");
		cmd.setCoverUri("cs://1/image/aW1hZ2UvTVRveFpqaGlZbVJpWlRjMlkyVTRPVGt3TkRZMU1Ea3lNek0zTXpRMll6ZGhOdw");

		cmd.setAttachments(new ArrayList<OfficeAttachmentDTO>());
		OfficeAttachmentDTO attachmentDTO = new OfficeAttachmentDTO();
		attachmentDTO.setContentType("image");
		attachmentDTO.setContentUri("cs://1/image/aW1hZ2UvTVRveE4yTmlNMlZsWldReU9ESmlNVEUxWVRNMllqQXhOVGRqTkdZeVpqTTROZw");
		cmd.getAttachments().add(attachmentDTO);
		attachmentDTO = new OfficeAttachmentDTO();
		attachmentDTO.setContentType("image");
		attachmentDTO.setContentUri("cs://1/image/aW1hZ2UvTVRvNU1UZzJZakJqTWpFM1ptUmpNRGc1WlRrME5UTmxaRFE0TmpWbE1tSXhOUQ");
		cmd.getAttachments().add(attachmentDTO);

		cmd.setCategories(new ArrayList<OfficeCategoryDTO>());
		OfficeCategoryDTO categoryDTO = new OfficeCategoryDTO();
		categoryDTO.setRentType(OfficeRentType.OPENSITE.getCode());
		categoryDTO.setSpaceType(OfficeSpaceType.UNIT.getCode());
		categoryDTO.setSize(1000);
		cmd.getCategories().add(categoryDTO);
		categoryDTO = new OfficeCategoryDTO();
		categoryDTO.setRentType(OfficeRentType.WHOLE.getCode());
		categoryDTO.setSpaceType(OfficeSpaceType.UNIT.getCode());
		categoryDTO.setSize(30);
		cmd.getCategories().add(categoryDTO);
		categoryDTO = new OfficeCategoryDTO();
		categoryDTO.setRentType(OfficeRentType.WHOLE.getCode());
		categoryDTO.setSpaceType(OfficeSpaceType.UNIT.getCode());
		categoryDTO.setSize(50);
		cmd.getCategories().add(categoryDTO);
		categoryDTO = new OfficeCategoryDTO();
		categoryDTO.setRentType(OfficeRentType.OPENSITE.getCode());
		categoryDTO.setSpaceType(OfficeSpaceType.SQ_METRE.getCode());
		categoryDTO.setSize(500);
		cmd.getCategories().add(categoryDTO);

		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		DSLContext dslContext = dbProvider.getDslContext();

		List<EhOfficeCubicleSpaces> spaces = new ArrayList<EhOfficeCubicleSpaces>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_SPACES)
		// .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch().map((r) -> {
					spaces.add(ConvertHelper.convert(r, EhOfficeCubicleSpaces.class));
					return null;
				});
		assertEquals(1, spaces.size());
		EhOfficeCubicleSpaces space = spaces.get(0);
		assertEquals(cmd.getName(), space.getName());
		assertEquals(cmd.getAddress(), space.getAddress());
		assertEquals(cmd.getCityName(), space.getCityName());
		assertEquals(cmd.getContactPhone(), space.getContactPhone());
		assertEquals(cmd.getCoverUri(), space.getCoverUri());
		assertEquals(cmd.getDescription(), space.getDescription());
		assertEquals(cmd.getLatitude(), space.getLatitude());
		assertEquals(cmd.getLongitude(), space.getLongitude());
		assertEquals(cmd.getProvinceName(), space.getProvinceName());
		assertEquals(cmd.getCityId(), space.getCityId());
		assertEquals(cmd.getManagerUid(), space.getManagerUid());
		assertEquals(cmd.getProvinceId(), space.getProvinceId());
		assertEquals(namespaceId, space.getNamespaceId());
		assertEquals(OfficeStatus.NORMAL.getCode(), space.getStatus().byteValue());

		List<EhOfficeCubicleAttachments> attachments = new ArrayList<EhOfficeCubicleAttachments>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS)
		// .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch().map((r) -> {
					attachments.add(ConvertHelper.convert(r, EhOfficeCubicleAttachments.class));
					return null;
				});
		assertEquals(2, attachments.size());
		assertEquals(space.getId(), attachments.get(0).getOwnerId());
		List<EhOfficeCubicleCategories> categories = new ArrayList<EhOfficeCubicleCategories>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_CATEGORIES)
		// .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch().map((r) -> {
					categories.add(ConvertHelper.convert(r, EhOfficeCubicleCategories.class));
					return null;
				});
		assertEquals(4, categories.size());
		assertEquals(space.getId(), categories.get(0).getSpaceId());
		// 更新写在这里面省掉了初始化的时间
		updateSpaceTest(space.getId());
		// 删除写在这里也是省掉初始化时间
		deleteSpaceTest(space.getId());
	}

	/**
	 * test update space
	 * */
	public void updateSpaceTest(Long id) {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/updateSpace";

		UpdateSpaceCommand cmd = new UpdateSpaceCommand();
		cmd.setId(id);
		cmd.setName("更新后空间名称");
		cmd.setProvinceId(600L);
		cmd.setProvinceName("四川");
		cmd.setCityId(601L);
		cmd.setCityName("成都");
		cmd.setAddress("地址是中文");
		cmd.setLatitude(153.332);
		cmd.setLongitude(42.445);
		cmd.setContactPhone("0838-8898754");
		cmd.setManagerUid(1L);
		cmd.setDescription("<b><h1> this is a </h1> <br> <p>what the fuck </p><br></b>");
		cmd.setCoverUri("cs://1/image/aW1hZ2UvTVRveFpqaGlZbVJpWlRjMlkyVTRPVGt3TkRZMU1Ea3lNek0zTXpRMll6ZGhOdw");

		// cmd.setAttachments(new ArrayList<OfficeAttachmentDTO>());
		// OfficeAttachmentDTO attachmentDTO = new OfficeAttachmentDTO();
		// attachmentDTO.setContentType("image");
		// attachmentDTO.setContentUri("cs://1/image/aW1hZ2UvTVRveE4yTmlNMlZsWldReU9ESmlNVEUxWVRNMllqQXhOVGRqTkdZeVpqTTROZw");
		// cmd.getAttachments().add(attachmentDTO);
		// attachmentDTO = new OfficeAttachmentDTO();
		// attachmentDTO.setContentType("image");
		// attachmentDTO.setContentUri("cs://1/image/aW1hZ2UvTVRvNU1UZzJZakJqTWpFM1ptUmpNRGc1WlRrME5UTmxaRFE0TmpWbE1tSXhOUQ");
		// cmd.getAttachments().add(attachmentDTO);

		cmd.setCategories(new ArrayList<OfficeCategoryDTO>());
		OfficeCategoryDTO categoryDTO = new OfficeCategoryDTO();
		categoryDTO.setRentType(OfficeRentType.OPENSITE.getCode());
		categoryDTO.setSpaceType(OfficeSpaceType.UNIT.getCode());
		categoryDTO.setSize(1000);
		cmd.getCategories().add(categoryDTO);
		// categoryDTO = new OfficeCategoryDTO();
		// categoryDTO.setRentType(OfficeRentType.WHOLE.getCode());
		// categoryDTO.setSpaceType(OfficeSpaceType.UNIT.getCode());
		// categoryDTO.setSize(30);
		// cmd.getCategories().add(categoryDTO);
		// categoryDTO = new OfficeCategoryDTO();
		// categoryDTO.setRentType(OfficeRentType.WHOLE.getCode());
		// categoryDTO.setSpaceType(OfficeSpaceType.UNIT.getCode());
		// categoryDTO.setSize(50);
		// cmd.getCategories().add(categoryDTO);
		// categoryDTO = new OfficeCategoryDTO();
		// categoryDTO.setRentType(OfficeRentType.OPENSITE.getCode());
		// categoryDTO.setSpaceType(OfficeSpaceType.SQ_METRE.getCode());
		// categoryDTO.setSize(500);
		// cmd.getCategories().add(categoryDTO);

		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		DSLContext dslContext = dbProvider.getDslContext();

		List<EhOfficeCubicleSpaces> spaces = new ArrayList<EhOfficeCubicleSpaces>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_SPACES)
		// .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch().map((r) -> {
					spaces.add(ConvertHelper.convert(r, EhOfficeCubicleSpaces.class));
					return null;
				});
		assertEquals(1, spaces.size());
		EhOfficeCubicleSpaces space = spaces.get(0);
		assertEquals(cmd.getName(), space.getName());
		assertEquals(cmd.getAddress(), space.getAddress());
		assertEquals(cmd.getCityName(), space.getCityName());
		assertEquals(cmd.getContactPhone(), space.getContactPhone());
		assertEquals(cmd.getCoverUri(), space.getCoverUri());
		assertEquals(cmd.getDescription(), space.getDescription());
		assertEquals(cmd.getLatitude(), space.getLatitude());
		assertEquals(cmd.getLongitude(), space.getLongitude());
		assertEquals(cmd.getProvinceName(), space.getProvinceName());
		assertEquals(cmd.getCityId(), space.getCityId());
		assertEquals(cmd.getManagerUid(), space.getManagerUid());
		assertEquals(cmd.getProvinceId(), space.getProvinceId());
		assertEquals(namespaceId, space.getNamespaceId());
		assertEquals(OfficeStatus.NORMAL.getCode(), space.getStatus().byteValue());

		List<EhOfficeCubicleAttachments> attachments = new ArrayList<EhOfficeCubicleAttachments>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS)
		// .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch().map((r) -> {
					attachments.add(ConvertHelper.convert(r, EhOfficeCubicleAttachments.class));
					return null;
				});
		assertEquals(0, attachments.size());
		// assertEquals(space.getId(),attachments.get(0).getOwnerId());
		List<EhOfficeCubicleCategories> categories = new ArrayList<EhOfficeCubicleCategories>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_CATEGORIES)
		// .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch().map((r) -> {
					categories.add(ConvertHelper.convert(r, EhOfficeCubicleCategories.class));
					return null;
				});
		assertEquals(1, categories.size());
		assertEquals(space.getId(), categories.get(0).getSpaceId());
		assertEquals(categoryDTO.getRentType(), categories.get(0).getRentType());
		assertEquals(categoryDTO.getSpaceType(), categories.get(0).getSpaceType());
		assertEquals(categoryDTO.getSize(), categories.get(0).getSpaceSize());
	}

	/**
	 * test delete space
	 * */
	public void deleteSpaceTest(Long id) {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/deleteSpace";
		DeleteSpaceCommand cmd = new DeleteSpaceCommand();
		cmd.setId(id);
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		DSLContext dslContext = dbProvider.getDslContext();

		List<EhOfficeCubicleSpaces> spaces = new ArrayList<EhOfficeCubicleSpaces>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_SPACES).where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(id)).fetch()
				.map((r) -> {
					spaces.add(ConvertHelper.convert(r, EhOfficeCubicleSpaces.class));
					return null;
				});
		assertEquals(1, spaces.size());
		EhOfficeCubicleSpaces space = spaces.get(0);
		assertEquals(OfficeStatus.DELETED.getCode(), space.getStatus().byteValue());
	}

	@Test
	public void testSeach() {
		initSpaceData();
		searchSpacesTest();
		searchSpacesByKeyWordTest();
		queryCitiesTest();
		querySpacesByCityTest();
		querySpacesTest();
	}

	/**
	 * search spaces
	 * */
	public void searchSpacesTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/searchSpaces";	
		SearchSpacesAdminCommand cmd = new SearchSpacesAdminCommand();
		cmd.setPageSize(5);
		SearchSpacesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchSpacesRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		//56789
		assertEquals(5, response.getResponse().getNextPageAnchor().intValue());
		List<OfficeSpaceDTO>  spaceDTOs = response.getResponse().getSpaces();
		assertEquals(5, spaceDTOs.size());
		for(Long i = 9L;i>=5;i--){
			assertEquals(i.longValue() , spaceDTOs.get(9-i.intValue()).getId().longValue());
		}
	}

	/**
	 * search spaces
	 * */
	public void searchSpacesByKeyWordTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/searchSpaces";	
		SearchSpacesAdminCommand cmd = new SearchSpacesAdminCommand();
		cmd.setPageSize(5);
		cmd.setKeyWords("名称1");
		SearchSpacesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchSpacesRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		 
		List<OfficeSpaceDTO>  spaceDTOs = response.getResponse().getSpaces();
		assertEquals(1, spaceDTOs.size());
		assertEquals("空间名称1", spaceDTOs.get(0).getName());
	}
	/**
	 * query cities
	 * */
	public void queryCitiesTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/queryCities";	 
		QueryCitiesRestResponse response = httpClientService.restGet(commandRelativeUri, null, QueryCitiesRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		 
		List<CityDTO>  CityDTOs = response.getResponse();
		//根据id区分,如果有一样名称和不同id的济南市,也会出现2个济南市,所以有全国,深圳,北京,济南,济南5个
		assertEquals(5, CityDTOs.size());
		assertEquals("全国", CityDTOs.get(0).getCityName());
	}
	/**
	 * query spaces 
	 * */
	public void querySpacesTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/querySpaces";	
		QuerySpacesCommand cmd = new QuerySpacesCommand();
//		cmd.setPageSize(5);
//		cmd.se("名称11");
		QuerySpacesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, QuerySpacesRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		 
		List<OfficeSpaceDTO>  spaceDTOs = response.getResponse().getSpaces();
		//1-9的status是正常,10 11是已删除
		assertEquals(9, spaceDTOs.size());
//		assertEquals("空间名称11", spaceDTOs.get(0).getName());
	}
	/**
	 * query spaces by city
	 * */
	public void querySpacesByCityTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/querySpaces";	
		QuerySpacesCommand cmd = new QuerySpacesCommand();
//		cmd.setPageSize(5);
		cmd.setCityId(201L);
		QuerySpacesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, QuerySpacesRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		 
		List<OfficeSpaceDTO>  spaceDTOs = response.getResponse().getSpaces();
		//cityId=201,三个
		assertEquals(3, spaceDTOs.size());
//		assertEquals("空间名称11", spaceDTOs.get(0).getName());
	}
	@Override
	public void initCustomData() {
		String sourceInfoFilePath = "data/json/ibase-office-test-data-userinfo_160726.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}

	public void initSpaceData() {
		String sourceInfoFilePath = "data/json/ibase-office-test-data-spaces_160727.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

	}
}
