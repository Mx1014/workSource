package com.everhomes.test.junit.equipment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.everhomes.rest.equipment.DeleteEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentParameterDTO;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentServiceErrorCode;
import com.everhomes.rest.equipment.EquipmentStandardMapDTO;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.FindEquipmentRestResponse;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.ListEquipmentsCategoriesRestResponse;
import com.everhomes.rest.equipment.ListParametersByEquipmentIdRestResponse;
import com.everhomes.rest.equipment.ListRelatedOrgGroupsCommand;
import com.everhomes.rest.equipment.ListRelatedOrgGroupsRestResponse;
import com.everhomes.rest.equipment.ReviewEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.equipment.SearchEquipmentsRestResponse;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsRestResponse;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.news.GetNewsDetailInfoCommand;
import com.everhomes.rest.news.GetNewsDetailInfoResponse;
import com.everhomes.rest.news.GetNewsDetailInfoRestResponse;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SearchNewsResponse;
import com.everhomes.rest.news.SearchNewsRestResponse;
import com.everhomes.rest.quality.CreatQualityStandardRestResponse;
import com.everhomes.rest.quality.ListUserRelateOrgGroupsRestResponse;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchConstant;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.StringHelper;

public class EquipmentTest extends BaseLoginAuthTestCase {

	private static final String REVIEW_RELATION_URI = "/equipment/reviewEquipmentStandardRelations";
	private static final String DELETE_RELATION_URI = "/equipment/deleteEquipmentStandardRelations";
	private static final String SEARCH_STANDARD_RELATION_URI = "/equipment/searchEquipmentStandardRelations";
	private static final String UPDATE_EQUIPMENT_URI = "/equipment/updateEquipments";
	private static final String DELETE_EQUIPMENT_URI = "/equipment/deleteEquipments";
	private static final String FIND_EQUIPMENT_URI = "/equipment/findEquipment";
	private static final String SEARCH_EQUIPMENT_URI = "/equipment/searchEquipments";
	private static final String LIST_EQUIPMENT_CATEGORIES_URI = "/equipment/listEquipmentsCategories";
	private static final String EXPORT_EQUIPMENT_URI = "/equipment/exportEquipments";
	private static final String IMPORT_EQUIPMENT_URI = "/equipment/importEquipments";
	private static final String LIST_EQUIPMENT_PARAMETER_URI = "/equipment/listParametersByEquipmentId";
	private static final String VERIFY_EQUIPMENT_LOCATION_QRCODE_URI = "/equipment/verifyEquipmentLocation";
	private static final String LIST_RELATED_ORG_GROUPS_URI = "/equipment/listRelatedOrgGroups";
	
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
	public void testDeleteEquipment() {
		logon(999992, userIdentifier, plainTexPassword);
		
		Long equipmentId = 2L;
		EquipmentsDTO dto = findEquipment(equipmentId);
		assertEquals(EquipmentStatus.IN_USE, EquipmentStatus.fromStatus(dto.getStatus()));
		
		DeleteEquipmentsCommand cmd = new DeleteEquipmentsCommand();
		cmd.setEquipmentId(equipmentId);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		
		httpClientService.restPost(DELETE_EQUIPMENT_URI, cmd, StringRestResponse.class);
		
		dto = findEquipment(equipmentId);
		assertEquals(EquipmentStatus.INACTIVE, EquipmentStatus.fromStatus(dto.getStatus()));
		
	}
	
	@Test @Ignore
	public void testListEquipmentParameters() {
		logon(999992, userIdentifier, plainTexPassword);
		
		DeleteEquipmentsCommand cmd = new DeleteEquipmentsCommand();
		cmd.setEquipmentId(2L);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);

		ListParametersByEquipmentIdRestResponse response = httpClientService.restPost(LIST_EQUIPMENT_PARAMETER_URI, 
				cmd, ListParametersByEquipmentIdRestResponse.class);
		
		assertNotNull("The reponse of may not be null", response);
    	assertTrue("response=" + 
    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    	assertNotNull(response.getResponse());
    	assertEquals(1, response.getResponse().size());
    	assertEquals("温度", response.getResponse().get(0).getParameterName());
		
	}
	
	@Test @Ignore
	public void testVerifyEquipmentLocation() {
		logon(999992, userIdentifier, plainTexPassword);
		
		VerifyEquipmentLocationCommand cmd = new VerifyEquipmentLocationCommand();
		cmd.setLatitude(25.549794);
		cmd.setLongitude(114.953143);
		cmd.setQrCodeToken("6IkjO6bMwid616gNxWyeLnlAB2zlBh6kfcwPr7vVMks5uzGZcKa1qCBXKxaPb8BiYiW-gTwxkEj59R708PijdaxSal5PAivO_RthhKPzHSfL-GREuT6Ny5ocOG44_DjshfK1JCEJKdKNQaMT7sEJYxceRgGo29GAwCND-_o-utE");
		
		StringRestResponse response = httpClientService.restPost(VERIFY_EQUIPMENT_LOCATION_QRCODE_URI, cmd, StringRestResponse.class);
		
		assertNotNull("The reponse of may not be null", response);
		assertEquals(EquipmentServiceErrorCode.ERROR_USER_NOT_IN_AREA, response.getErrorCode().intValue());
		
	}
	
	@Test @Ignore
	public void testListEquipmentsCategories() {
		logon(999992, userIdentifier, plainTexPassword);

		ListEquipmentsCategoriesRestResponse response = httpClientService.restPost(LIST_EQUIPMENT_CATEGORIES_URI, 
				null, ListEquipmentsCategoriesRestResponse.class);
		
		assertNotNull("The reponse of may not be null", response);
    	assertTrue("response=" + 
    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    	assertNotNull(response.getResponse());
    	assertEquals(3, response.getResponse().size());
		
	}
	
	@Test
	 public void testUpdateEquipment() {
	    	
	    	// 登录时不传namepsace，默认为左邻域空间
	    	logon(999992, userIdentifier, plainTexPassword);
	    	
	    	UpdateEquipmentsCommand cmd = new UpdateEquipmentsCommand();
	    	cmd.setOwnerId(ownerId);
	    	cmd.setOwnerType(ownerType);
	    	cmd.setTargetId(1000753L);
	    	cmd.setTargetType(ownerType);
	    	cmd.setName("发电机");
	    	cmd.setManufacturer("发热管");
	    	cmd.setEquipmentModel("fsd-34342");
	    	cmd.setCategoryId(9L);
	    	cmd.setCategoryPath("强电");
	    	cmd.setLocation("金融基地7楼");
	    	cmd.setLongitude(113.908764);
	    	cmd.setLatitude(22.576676);
	    	cmd.setQrCodeFlag((byte) 1);
	    	cmd.setStatus((byte) 2);
	    	cmd.setInstallationTime(1457244000000L);
	    	cmd.setRepairTime(1465192800000L);
	    	cmd.setInitialAssetValue("121000");
	    	cmd.setManager("大闸蟹");
	    	cmd.setId(147L);
	    	
	    	List<EquipmentStandardMapDTO> eqStandardMap = new ArrayList<EquipmentStandardMapDTO>();
	    	EquipmentStandardMapDTO map = new EquipmentStandardMapDTO();
	    	map.setId(104L);
	    	map.setStandardId(155L);
	    	map.setStandardName("空调标准");
	    	map.setEquipmentId(147L);
	    	eqStandardMap.add(map);
	    	
	    	map = new EquipmentStandardMapDTO();
	    	map.setStandardId(154L);
	    	map.setStandardName("测试关联");
	    	map.setEquipmentId(147L);
	    	eqStandardMap.add(map);
	    	
	    	cmd.setEqStandardMap(eqStandardMap);
	    	
	    	UpdateEquipmentsRestResponse response = httpClientService.restGet(UPDATE_EQUIPMENT_URI, cmd, 
	    			UpdateEquipmentsRestResponse.class, context);
	    	
	    	assertNotNull("The reponse of may not be null", response);
	    	assertTrue("response=" + 
	    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	    	assertNotNull(response.getResponse());
	    	assertEquals("发电机", response.getResponse().getName());
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
	
	@Test @Ignore
	public void testImportEquipment() {
		
		// 登录时不传namepsace，默认为左邻域空间
		logon(999992, userIdentifier, plainTexPassword);
		
		importEquipment();
		
	}
	
	@Test @Ignore
	public void testSearchEquipment() {
		
		// 登录时不传namepsace，默认为左邻域空间
		logon(999992, userIdentifier, plainTexPassword);
		searchProvider.clearType(SearchConstant.EQUIPMENTINDEXTYPE);
		importEquipment();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SearchEquipmentsCommand cmd = new SearchEquipmentsCommand();
		cmd.setKeyword("收到过1");
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setTargetId(1000753L);
		cmd.setTargetType("GROUP");
		
		SearchEquipmentsRestResponse response = httpClientService.restPost(SEARCH_EQUIPMENT_URI, cmd, SearchEquipmentsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		SearchEquipmentsResponse searchEquipmentsResponse = response.getResponse();
		System.err.println(searchEquipmentsResponse);
		assertNotNull(searchEquipmentsResponse);
		assertTrue("nextPageAnchor should be null", null == searchEquipmentsResponse.getNextPageAnchor());
		assertTrue("list size should be 1", 1 == searchEquipmentsResponse.getEquipment().size());
		assertTrue("equipment status should be incomplete", 
				EquipmentStatus.INCOMPLETE.equals(EquipmentStatus.fromStatus(searchEquipmentsResponse.getEquipment().get(0).getStatus())));
		
	}
	
	private void importEquipment() {
		try {
			String uri = IMPORT_EQUIPMENT_URI;
			ImportOwnerCommand cmd = new ImportOwnerCommand();
			cmd.setOwnerId(ownerId);
			cmd.setOwnerType(ownerType);
			cmd.setTargetId(1000753L);
			cmd.setTargetType("GROUP");
			File file;
			file = new File(new File("").getCanonicalPath() + "\\src\\test\\data\\excel\\equipments_template.xlsx");
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
	
	@Test @Ignore
	public void testListRelatedOrgGroups() {
		String uri = LIST_RELATED_ORG_GROUPS_URI;
		logon(999992, userIdentifier, plainTexPassword);
		

		ListRelatedOrgGroupsCommand cmd = new ListRelatedOrgGroupsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		ListRelatedOrgGroupsRestResponse response = httpClientService.restPost(uri, cmd,
				ListRelatedOrgGroupsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		assertNotNull(response.getResponse());
		assertEquals(1, response.getResponse().size());
		
		logon(999992, "13510551322", plainTexPassword);
		
		response = httpClientService.restPost(uri, cmd, ListRelatedOrgGroupsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		assertNotNull(response.getResponse());
		assertEquals(2, response.getResponse().size());
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
