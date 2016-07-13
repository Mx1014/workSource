package com.everhomes.test.junit.rental;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.RentalSiteDTO;
import com.everhomes.rest.techpark.rental.admin.AddResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AdminGetResourceListRestResponse;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminCommand;
import com.everhomes.rest.techpark.rental.admin.SiteOwnerDTO;
import com.everhomes.rest.techpark.rental.admin.UpdateResourceAdminCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteOwners;
import com.everhomes.server.schema.tables.pojos.EhRentalSitePics;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalAdminResourceTest extends BaseLoginAuthTestCase {
	

	Integer namespaceId = 0;
	String userIdentifier = "root";
	String plainTexPassword = "123456";
	
	
	private Long launchPadItemId = 510L;
	private String ownerType = RentalOwnerType.COMMUNITY.getCode();
	private Long ownerId = 419L;
	private Long organizationId=1L;
	@Before
	public void setUp() {
		super.setUp();
		truncateRentalTable();
	}
	@Test
	public void testMain(){ 
		testAddResource();
		testNullOrganizationIdorLaunchPadItemId();
		testGetResourceList();
		testUpdateResource();
	}
	private void truncateRentalTable() {

		String serverInitfilePath = "data/tables/rental2.0_truncate_tables.sql";
		dbProvider.runClassPathSqlFile(serverInitfilePath);
		
		

	}
	 
     
    public void testNullOrganizationIdorLaunchPadItemId() {
    	//addResource
        // realm字段为null
		Integer namespaceId = 0;
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		AddResourceAdminCommand cmd = new AddResourceAdminCommand();
		cmd.setLaunchPadItemId(this.launchPadItemId);
		String commandRelativeUri = "/rental/admin/addResource";
		RestResponse response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponse.class, context);
        assertNotNull(response);
        assertEquals("general", response.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER 
        
		cmd = new AddResourceAdminCommand();
		cmd.setOrganizationId(this.organizationId);  
		response = httpClientService.restPost(commandRelativeUri,cmd, RestResponse.class, context);
        assertNotNull(response);
        assertEquals("general", response.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER 
        
        //getResoureceList
        commandRelativeUri = "/rental/admin/getResourceList";
        GetResourceListAdminCommand cmd2 = new GetResourceListAdminCommand();
		cmd2.setLaunchPadItemId(this.launchPadItemId);
		AdminGetResourceListRestResponse response2 = httpClientService.restPost(commandRelativeUri,
				cmd2, AdminGetResourceListRestResponse.class, context);
        assertNotNull(response2);
        assertEquals("general", response2.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response2.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER 
        commandRelativeUri = "/rental/admin/getResourceList";
        cmd2 = new GetResourceListAdminCommand();
		cmd2.setOrganizationId(this.organizationId); 
		response2 = httpClientService.restPost(commandRelativeUri,
				cmd2, AdminGetResourceListRestResponse.class, context);
        assertNotNull(response2);
        assertEquals("general", response2.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response2.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER 

    }
    public void testUpdateResource(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		List<EhRentalSites> result1 = new ArrayList<>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITES)
				.where(Tables.EH_RENTAL_SITES.ORGANIZATION_ID.eq(this.organizationId))
				.and(Tables.EH_RENTAL_SITES.LAUNCH_PAD_ITEM_ID.eq(this.launchPadItemId)) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalSites.class));
					return null;
				});
		UpdateResourceAdminCommand cmd = new UpdateResourceAdminCommand();
		cmd.setId(result1.get(0).getId()); 
		cmd.setOwners(new ArrayList<SiteOwnerDTO>());
		
		for(int i=0;i<=20;i++){
			SiteOwnerDTO ownerDTO=new SiteOwnerDTO();
			ownerDTO.setOwnerId(this.ownerId+i);
			ownerDTO.setOwnerType(this.ownerType);
			cmd.getOwners().add(ownerDTO);
		}
		  
		cmd.setSiteName("更改后的场所资源");
		cmd.setSpec("规格：111座");
		cmd.setAddress("地址不用获取");
		cmd.setLatitude(22.5655);
		cmd.setLongitude(115.5654);
		cmd.setContactPhonenum("135-8644-4564");
		cmd.setChargeUid(3L);
		cmd.setIntroduction("详情");
		cmd.setCoverUri("cs://image1");
		cmd.setDetailUris(new ArrayList<String>());
		for(int i=0;i<=20;i++){
			cmd.getDetailUris().add("cs://image"+String.valueOf(i));
		}
		
		String commandRelativeUri = "/rental/admin/updateResource";
		RestResponseBase response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		
		
		
		List<EhRentalSites> result = new ArrayList<>();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITES)
				.where(Tables.EH_RENTAL_SITES.ID.eq(cmd.getId())) 
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhRentalSites.class));
					return null;
				});
		//验证资源表修改
		assertEquals(cmd.getSiteName(), result.get(0).getSiteName());

		List<EhRentalSiteOwners> resultOwners = new ArrayList<>();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_OWNERS)
				.where(Tables.EH_RENTAL_SITE_OWNERS.RENTAL_SITE_ID.eq(cmd.getId()))
				.fetch()
				.map((r) -> {
					resultOwners.add(ConvertHelper.convert(r,
							EhRentalSiteOwners.class));
					return null;
				});
		assertEquals(21, resultOwners.size());
		
		List<EhRentalSitePics> resultPics = new ArrayList<>();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_PICS)
				.where(Tables.EH_RENTAL_SITE_PICS.OWNER_ID.eq(cmd.getId()))
				.and(Tables.EH_RENTAL_SITE_PICS.OWNER_TYPE.eq(EhRentalSites.class.getSimpleName())) 
				.fetch()
				.map((r) -> {
					resultPics.add(ConvertHelper.convert(r,
							EhRentalSitePics.class));
					return null;
				});
		assertEquals(21, resultPics.size());
		
    }
	public void testAddResource(){
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		AddResourceAdminCommand cmd = new AddResourceAdminCommand();
		cmd.setLaunchPadItemId(this.launchPadItemId);
		cmd.setOrganizationId(this.organizationId);
		cmd.setOwners(new ArrayList<SiteOwnerDTO>());
		SiteOwnerDTO ownerDTO=new SiteOwnerDTO();
		ownerDTO.setOwnerId(this.ownerId);
		ownerDTO.setOwnerType(this.ownerType);
		cmd.getOwners().add(ownerDTO);
		SiteOwnerDTO ownerDTO2=new SiteOwnerDTO();
		ownerDTO2.setOwnerId(233L);
		ownerDTO2.setOwnerType(this.ownerType);
		cmd.getOwners().add(ownerDTO2);
		cmd.setSiteName("资源名称");
		cmd.setSpec("规格：40座");
		cmd.setAddress("地址不用获取");
		cmd.setLatitude(22.5655);
		cmd.setLongitude(115.5654);
		cmd.setContactPhonenum("135-8644-4564");
		cmd.setChargeUid(3L);
		cmd.setIntroduction("详情");
		cmd.setCoverUri("cs://image1");
		cmd.setDetailUris(new ArrayList<String>());
		cmd.getDetailUris().add("cs://image2");
		cmd.getDetailUris().add("cs://image3");
		cmd.getDetailUris().add("cs://image4"); 
		
		String commandRelativeUri = "/rental/admin/addResource";
		RestResponse response =null;
		for(int i=0;i<30;i++){
			response= httpClientService.restPost(commandRelativeUri,
					cmd, RestResponse.class, context);
		}
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		DSLContext context = dbProvider.getDslContext();
		List<EhRentalSites> result = new ArrayList<>();
		context.select()
				.from(Tables.EH_RENTAL_SITES)
				.where(Tables.EH_RENTAL_SITES.ORGANIZATION_ID.eq(cmd
						.getOrganizationId()))
				.and(Tables.EH_RENTAL_SITES.LAUNCH_PAD_ITEM_ID.eq(cmd
						.getLaunchPadItemId())) 
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhRentalSites.class));
					return null;
				});
		assertEquals(30, result.size());

		List<EhRentalSiteOwners> resultOwners = new ArrayList<>();
		context.select()
				.from(Tables.EH_RENTAL_SITE_OWNERS)
				.where(Tables.EH_RENTAL_SITE_OWNERS.RENTAL_SITE_ID.eq(result.get(0).getId()))
				.fetch()
				.map((r) -> {
					resultOwners.add(ConvertHelper.convert(r,
							EhRentalSiteOwners.class));
					return null;
				});
		assertEquals(2, resultOwners.size());
		
		List<EhRentalSitePics> resultPics = new ArrayList<>();
		context.select()
				.from(Tables.EH_RENTAL_SITE_PICS)
				.where(Tables.EH_RENTAL_SITE_PICS.OWNER_ID.eq(result.get(0).getId()))
				.and(Tables.EH_RENTAL_SITE_PICS.OWNER_TYPE.eq(EhRentalSites.class.getSimpleName())) 
				.fetch()
				.map((r) -> {
					resultPics.add(ConvertHelper.convert(r,
							EhRentalSitePics.class));
					return null;
				});
		assertEquals(3, resultPics.size());
	}
	
	public void testGetResourceList() {
//
//		truncateRentalTable();
//		
		Integer namespaceId = 0;
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		//不设置pagesize默认20
		GetResourceListAdminCommand cmd = new GetResourceListAdminCommand();
		cmd.setLaunchPadItemId(this.launchPadItemId);
		cmd.setOrganizationId(this.organizationId);
		cmd.setOwnerId(this.ownerId);
		cmd.setOwnerType(this.ownerType);
		String commandRelativeUri = "/rental/admin/getResourceList";
		AdminGetResourceListRestResponse response = httpClientService.restPost(commandRelativeUri,
				cmd, AdminGetResourceListRestResponse.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertNotNull("The default rule not null ", response.getResponse());
		System.out.print(response.getResponse().toString()); 
		assertEquals(20, response.getResponse().getRentalSites().size());
		for(RentalSiteDTO dto : response.getResponse().getRentalSites()){
			assertEquals(2, dto.getOwners().size());
			assertEquals(3, dto.getSitePics().size());
		}
		
		//设置pageSize30
		cmd.setPageSize(30);
		response = httpClientService.restPost(commandRelativeUri,
				cmd, AdminGetResourceListRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertNotNull("The default rule not null ", response.getResponse());
		System.out.print(response.getResponse().toString()); 
		assertEquals(30, response.getResponse().getRentalSites().size());
		for(RentalSiteDTO dto : response.getResponse().getRentalSites()){
			assertEquals(2, dto.getOwners().size());
			assertEquals(3, dto.getSitePics().size());
		}
	}

	
	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}
	 
    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
	
	
}
