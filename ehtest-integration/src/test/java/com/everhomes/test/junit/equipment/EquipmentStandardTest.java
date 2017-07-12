package com.everhomes.test.junit.equipment;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.FindEquipmentRestResponse;
import com.everhomes.rest.equipment.FindEquipmentStandardRestResponse;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesRestResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardsRestResponse;
import com.everhomes.rest.equipment.StandardType;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardRestResponse;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessories;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchConstant;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class EquipmentStandardTest extends BaseLoginAuthTestCase {
	
	private static final String UPDATE_STANDARD_URI = "/equipment/updateEquipmentStandard";
	private static final String FIND_STANDARD_URI = "/equipment/findEquipmentStandard";
	private static final String DELETE_STANDARD_URI = "/equipment/deleteEquipmentStandard";
	private static final String SEARCH_STANDARD_URI = "/equipment/searchEquipmentStandards";
	private static final String EXPORT_STANDARD_URI = "/equipment/exportEquipmentStandards";
	private static final String IMPORT_STANDARD_URI = "/equipment/importEquipmentStandards";
	private static final String SEARCH_STANDARD_RELATION_URI = "/equipment/searchEquipmentStandardRelations";
	private static final String FIND_EQUIPMENT_URI = "/equipment/findEquipment";
	
	@Autowired
	private SearchProvider searchProvider;
	
	Integer namespaceId = 999992;
	String userIdentifier = "19112349996";
	String plainTexPassword = "123456";
	
	Long ownerId = (long) 1000750;
	String ownerType = "PM";
	
	@Before
    public void setUp() {
        super.setUp();
    }
	
	@Test @Ignore
	 public void testUpdateEquipmentStandard() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	RepeatSettingsDTO repeat = new RepeatSettingsDTO();
	    	repeat.setEndDate(1472623200000L);
	    	repeat.setStartDate(1469944800000L);
	    	repeat.setForeverFlag((byte) 0);
	    	repeat.setOwnerId(ownerId);
	    	repeat.setOwnerType(ownerType);
	    	repeat.setRepeatType((byte) 1);
	    	repeat.setTimeRanges("{\\\"ranges\\\":[{\\\"startTime\\\":\\\"14:00:00\\\",\\\"endTime\\\":\\\"17:00:00\\\",\\\"duration\\\":\\\"180m\\\"}]}");
	    	repeat.setExpression("{\\\"expression\\\":[]}");
	    	
	    	UpdateEquipmentStandardCommand cmd = new UpdateEquipmentStandardCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setName("name");
	    	cmd.setDescription("description");
	    	cmd.setRemarks("remarks");
	    	cmd.setRepeat(repeat);
	    	cmd.setStandardNumber("standardNumber");
	    	cmd.setStandardSource("standardSource");
	    	cmd.setStandardType(StandardType.ROUTING_INSPECTION.getCode());
	    	
	    	UpdateEquipmentStandardRestResponse response = httpClientService.restGet(UPDATE_STANDARD_URI, cmd, 
	    			UpdateEquipmentStandardRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals("name", response.getResponse().getName());
	}
	
	@Test @Ignore
	 public void testFindEquipmentStandard() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	DeleteEquipmentStandardCommand cmd = new DeleteEquipmentStandardCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setStandardId(1L);
	    	
	    	FindEquipmentStandardRestResponse response = httpClientService.restGet(FIND_STANDARD_URI, cmd, 
	    			FindEquipmentStandardRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals("设备标准", response.getResponse().getName());
	}
	
	@Test @Ignore
	public void testDeleteEquipmentStandard() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	DeleteEquipmentStandardCommand cmd = new DeleteEquipmentStandardCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setStandardId(1L);
	    	
	    	httpClientService.restGet(DELETE_STANDARD_URI, cmd, StringRestResponse.class, context);
	    	
	    	List<EhEquipmentInspectionStandards> dbStandards = getDbStandards();
			assertEquals(1, dbStandards.size());
			EhEquipmentInspectionStandards standard = dbStandards.get(0);
			assertTrue("status should be 0", standard.getStatus().byteValue() == EquipmentStandardStatus.INACTIVE.getCode());
	
//			EquipmentsDTO dto = findEquipment(2L);
//			assertEquals(EquipmentReviewStatus.INACTIVE, EquipmentReviewStatus.fromStatus(dto.getReviewStatus()));
	}
	
	@Test
	public void testImportEquipmentStandard() {
		
		// 登录时不传namepsace，默认为左邻域空间
		logon(999992, userIdentifier, plainTexPassword);
		
		importEquipmentStandard();
	}
	
	@Test
	public void testSearchEquipmentAccessory() {
		
		// 登录时不传namepsace，默认为左邻域空间
		logon(999992, userIdentifier, plainTexPassword);
		searchProvider.clearType(SearchConstant.EQUIPMENTSTANDARDINDEXTYPE);
		importEquipmentStandard();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SearchEquipmentStandardsCommand cmd = new SearchEquipmentStandardsCommand();
		cmd.setKeyword("standard-001");
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		
		SearchEquipmentStandardsRestResponse response = httpClientService.restPost(SEARCH_STANDARD_URI, cmd, SearchEquipmentStandardsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		SearchEquipmentStandardsResponse searchStandardsResponse = response.getResponse();
		System.err.println(searchStandardsResponse);
		assertNotNull(searchStandardsResponse);
		assertTrue("nextPageAnchor should be null", null == searchStandardsResponse.getNextPageAnchor());
		assertTrue("list size should be 1", 1 == searchStandardsResponse.getEqStandards().size());
		assertTrue("standard status should be not complete", 
				EquipmentStandardStatus.NOT_COMPLETED.equals(EquipmentStandardStatus.fromStatus(searchStandardsResponse.getEqStandards().get(0).getStatus())));
		
	}
	
	private void importEquipmentStandard() {
		try {
			String uri = IMPORT_STANDARD_URI;
			ImportOwnerCommand cmd = new ImportOwnerCommand();
			cmd.setOwnerId(ownerId);
			cmd.setOwnerType(ownerType);
			File file;
			file = new File(new File("").getCanonicalPath() + "\\src\\test\\data\\excel\\standard_template.xlsx");
			RestResponseBase response = httpClientService.postFile(uri, cmd, file, RestResponseBase.class);
			assertNotNull(response);
			assertTrue("response= " + StringHelper.toJsonString(response),
					httpClientService.isReponseSuccess(response));
			assertTrue("errorCode should be 200", response.getErrorCode().intValue() == ErrorCodes.SUCCESS);

			DSLContext context = dbProvider.getDslContext();
			Integer count = (Integer) context.selectCount().from(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS).fetchOne().getValue(0);
			assertTrue("the count should be 5", count.intValue() == 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<EhEquipmentInspectionStandards> getDbStandards() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS).fetch().map(r -> ConvertHelper.convert(r, EhEquipmentInspectionStandards.class));
	}
	
	private EquipmentsDTO findEquipment(Long equipmentId) {
		
		DeleteEquipmentsCommand cmd = new DeleteEquipmentsCommand();
		cmd.setEquipmentId(equipmentId);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		
		FindEquipmentRestResponse response = httpClientService.restPost(FIND_EQUIPMENT_URI, cmd,
				FindEquipmentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		EquipmentsDTO equipment = response.getResponse();

		return equipment;
	}
	
	@After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    	
        userInfoFilePath = "data/json/equipment-test-data.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}
