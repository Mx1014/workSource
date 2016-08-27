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
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesRestResponse;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessories;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchConstant;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class EquipmentAccessoryTest extends BaseLoginAuthTestCase {

	private static final String UPDATE_EQUIPMENT_ACCESSORY_URI = "/equipment/updateEquipmentAccessories";
	private static final String DELETE_EQUIPMENT_ACCESSORY_URI = "/equipment/deleteEquipmentAccessories";
	private static final String SEARCH_EQUIPMENT_ACCESSORY_URI = "/equipment/searchEquipmentAccessories";
	private static final String EXPORT_EQUIPMENT_ACCESSORY_URI = "/equipment/exportEquipmentAccessories";
	private static final String IMPORT_EQUIPMENT_ACCESSORY_URI = "/equipment/importEquipmentAccessories";
	
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
	 public void testUpdateEquipmentAccessories() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	UpdateEquipmentAccessoriesCommand cmd = new UpdateEquipmentAccessoriesCommand();
	    	cmd.setLocation("location");
	    	cmd.setManufacturer("manufacturer");
	    	cmd.setModelNumber("modelNumber");
	    	cmd.setName("name");
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setSpecification("specification");
	    	cmd.setTargetId(1000753L);
	    	cmd.setTargetType("PM");
	    	
	    	
	    	UpdateEquipmentAccessoriesRestResponse response = httpClientService.restGet(UPDATE_EQUIPMENT_ACCESSORY_URI, cmd, 
	    			UpdateEquipmentAccessoriesRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals("name", response.getResponse().getName());
	    	
	    	cmd.setName("newName");
	    	response = httpClientService.restGet(UPDATE_EQUIPMENT_ACCESSORY_URI, cmd, UpdateEquipmentAccessoriesRestResponse.class);
	    	assertEquals("newName", response.getResponse().getName());
	
	}
	
	@Test @Ignore
	 public void testDeleteEquipmentAccessories() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	DeleteEquipmentAccessoriesCommand cmd = new DeleteEquipmentAccessoriesCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setId(1L);
	    	
	    	
	    	httpClientService.restGet(DELETE_EQUIPMENT_ACCESSORY_URI, cmd, StringRestResponse.class, context);
	    	
	    	List<EhEquipmentInspectionAccessories> dbAccessories = getDbAccessories();
			assertEquals(1, dbAccessories.size());
			EhEquipmentInspectionAccessories accessory = dbAccessories.get(0);
			assertTrue("status should be 0", accessory.getStatus().byteValue() == EquipmentStandardStatus.INACTIVE.getCode());
	
	}
	
	@Test
	public void testImportEquipmentAccessory() {
		
		// 登录时不传namepsace，默认为左邻域空间
		logon(999992, userIdentifier, plainTexPassword);
		
		importEquipmentAccessory();
		
	}
	
	@Test
	public void testSearchEquipmentAccessory() {
		
		// 登录时不传namepsace，默认为左邻域空间
		logon(999992, userIdentifier, plainTexPassword);
		searchProvider.clearType(SearchConstant.EQUIPMENTACCESSORYINDEXTYPE);
		importEquipmentAccessory();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SearchEquipmentAccessoriesCommand cmd = new SearchEquipmentAccessoriesCommand();
		cmd.setKeyword("东方航空1");
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setTargetId(1000753L);
		cmd.setTargetType("GROUP");
		
		SearchEquipmentAccessoriesRestResponse response = httpClientService.restPost(SEARCH_EQUIPMENT_ACCESSORY_URI, cmd, SearchEquipmentAccessoriesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		SearchEquipmentAccessoriesResponse searchAccessoriesResponse = response.getResponse();
		System.err.println(searchAccessoriesResponse);
		assertNotNull(searchAccessoriesResponse);
		assertTrue("nextPageAnchor should be null", null == searchAccessoriesResponse.getNextPageAnchor());
		assertTrue("list size should be 1", 1 == searchAccessoriesResponse.getAccessories().size());
		
	}
	
	private void importEquipmentAccessory() {
		try {
			String uri = IMPORT_EQUIPMENT_ACCESSORY_URI;
			ImportOwnerCommand cmd = new ImportOwnerCommand();
			cmd.setOwnerId(ownerId);
			cmd.setOwnerType(ownerType);
			cmd.setTargetId(1000753L);
			cmd.setTargetType("GROUP");
			File file;
			file = new File(new File("").getCanonicalPath() + "\\src\\test\\data\\excel\\accessories_template.xlsx");
			RestResponseBase response = httpClientService.postFile(uri, cmd, file, RestResponseBase.class);
			assertNotNull(response);
			assertTrue("response= " + StringHelper.toJsonString(response),
					httpClientService.isReponseSuccess(response));
			assertTrue("errorCode should be 200", response.getErrorCode().intValue() == ErrorCodes.SUCCESS);

			DSLContext context = dbProvider.getDslContext();
			Integer count = (Integer) context.selectCount().from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES).fetchOne().getValue(0);
			assertTrue("the count should be 5", count.intValue() == 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<EhEquipmentInspectionAccessories> getDbAccessories() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES).fetch().map(r -> ConvertHelper.convert(r, EhEquipmentInspectionAccessories.class));
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
