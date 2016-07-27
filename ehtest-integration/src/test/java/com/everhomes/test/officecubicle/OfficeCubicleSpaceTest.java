package com.everhomes.test.officecubicle;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.OfficeAttachmentDTO;
import com.everhomes.rest.officecubicle.OfficeCategoryDTO;
import com.everhomes.rest.officecubicle.OfficeRentType;
import com.everhomes.rest.officecubicle.OfficeSpaceType;
import com.everhomes.rest.officecubicle.admin.AddSpaceCommand;
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
		dslContext
				.select()
				.from(Tables.EH_OFFICE_CUBICLE_SPACES)
//				.where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch()
				.map((r) -> {
					spaces.add(ConvertHelper.convert(r,
							EhOfficeCubicleSpaces.class));
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
		
		
		List<EhOfficeCubicleAttachments> attachments = new ArrayList<EhOfficeCubicleAttachments>();
		dslContext
				.select()
				.from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS)
//				.where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch()
				.map((r) -> {
					attachments.add(ConvertHelper.convert(r,
							EhOfficeCubicleAttachments.class));
					return null;
				});
		assertEquals(2, attachments.size());
		assertEquals(space.getId(),attachments.get(0).getOwnerId());
		List<EhOfficeCubicleCategories> categories = new ArrayList<EhOfficeCubicleCategories>();
		dslContext
				.select()
				.from(Tables.EH_OFFICE_CUBICLE_CATEGORIES)
//				.where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(cm()))
				.fetch()
				.map((r) -> {
					categories.add(ConvertHelper.convert(r,
							EhOfficeCubicleCategories.class));
					return null;
				});
		assertEquals(4, categories.size());
		assertEquals(space.getId(),categories.get(0).getSpaceId());
		
		for(int i = 0;i<10;i++)
			httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

	}

	@Override
	public void initCustomData() {
		String sourceInfoFilePath = "data/json/ibase-office-test-data-userinfo_160726.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}

}
