package com.everhomes.test.junit.rental;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.core.joran.conditional.ElseAction;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.rentalv2.AddItemAdminCommand;
import com.everhomes.rest.rentalv2.DeleteItemAdminCommand;
import com.everhomes.rest.rentalv2.GetItemListAdminCommand;
import com.everhomes.rest.rentalv2.RentalItemType;
import com.everhomes.rest.rentalv2.RentalOwnerType;
import com.everhomes.rest.rentalv2.SiteItemDTO;
import com.everhomes.rest.rentalv2.UpdateItemAdminCommand;
import com.everhomes.rest.rentalv2.admin.AdminGetItemListRestResponse;
import com.everhomes.rest.rentalv2.admin.UpdateItemsAdminCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalAdminItemTest extends BaseLoginAuthTestCase {
	

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
	} 
	private void truncateRentalTable() {

		String serverInitfilePath = "data/tables/rental2.0_truncate_tables.sql";
		dbProvider.runClassPathSqlFile(serverInitfilePath);
		
		

	}
	 


	@Test
	public void testAddItem() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/addItem";
		
		AddItemAdminCommand cmd = new AddItemAdminCommand();
		cmd.setRentalSiteId(2L);
		cmd.setCounts(100);
		cmd.setItemName("shangpin");
		cmd.setItemPrice(new BigDecimal(100));
		cmd.setItemType(RentalItemType.SALE.getCode());
		for(int i =0;i<=10;i++){
			cmd.setDefaultOrder(i);

			RestResponseBase response = httpClientService.restPost(commandRelativeUri,
					cmd, RestResponseBase.class, context);
			assertNotNull("The reponse of may not be null", response);
			assertTrue("The user scenes should be get from server, response="
					+ StringHelper.toJsonString(response),
					httpClientService.isReponseSuccess(response));
		}
		
		List<EhRentalSiteItems> result1 =new ArrayList<EhRentalSiteItems>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.RENTAL_SITE_ID.eq(cmd.getRentalSiteId())) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(11, result1.size());
		assertEquals(cmd.getItemName(), result1.get(0).getName());
		assertEquals(cmd.getItemType(), result1.get(0).getItemType());
		
	}
	@Test
	public void testGetItemList() { 

		truncateRentalTable();
		initItemsData();
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		//展示全部
		GetItemListAdminCommand cmd = new GetItemListAdminCommand();
		cmd.setRentalSiteId(2L);
		String commandRelativeUri = "/rental/admin/getItemList";
		AdminGetItemListRestResponse response = httpClientService.restPost(commandRelativeUri,
				cmd, AdminGetItemListRestResponse.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertNotNull("The default rule not null ", response.getResponse());
		System.out.print(response.getResponse().toString()); 
		assertEquals(3, response.getResponse().getSiteItems().size());
		for(SiteItemDTO dto : response.getResponse().getSiteItems()){
			if(dto.getId().equals(1L)){
				assertEquals(Integer.valueOf(80),dto.getCounts()  );
			}
			else {
				assertEquals(Integer.valueOf(100), dto.getCounts() );
			}
			
		}
		 
		
	}

	@Test
	public void testUpdateItem() { 

		truncateRentalTable();
		initItemsData();
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		//展示全部
		UpdateItemAdminCommand cmd = new UpdateItemAdminCommand();
		cmd.setId(1L);
		cmd.setRentalSiteId(2L);
		cmd.setCounts(300);
		cmd.setItemName("testUpdate");
		cmd.setItemPrice(new BigDecimal(700));
		cmd.setItemType(RentalItemType.RENTAL.getCode()); 
		String commandRelativeUri = "/rental/admin/updateItem";
		RestResponseBase response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 

		List<EhRentalSiteItems> result1 =new ArrayList<EhRentalSiteItems>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.ID.eq(cmd.getId())) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(1, result1.size());
		assertEquals(cmd.getItemName(), result1.get(0).getName());
		assertEquals(cmd.getItemType(), result1.get(0).getItemType());
		assertEquals(cmd.getCounts(), result1.get(0).getCounts());
		
		
		cmd = new UpdateItemAdminCommand();
		cmd.setId(2L);
		cmd.setRentalSiteId(2L);
		cmd.setCounts(300);
		cmd.setItemName("testUpdate");
		cmd.setItemPrice(new BigDecimal(700));
		cmd.setItemType(RentalItemType.SALE.getCode());  
		response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 

		List<EhRentalSiteItems> result2 =new ArrayList<EhRentalSiteItems>(); 
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.ID.eq(cmd.getId())) 
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(1, result1.size());
		assertEquals(cmd.getItemName(), result2.get(0).getName());
		assertEquals(cmd.getItemType(), result2.get(0).getItemType());
		assertEquals(Integer.valueOf(cmd.getCounts()+100), result2.get(0).getCounts());
		 
	}

	@Test
	public void testDeleteItem() {

		truncateRentalTable();
		initItemsData();
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/deleteItem";
		
		DeleteItemAdminCommand cmd = new DeleteItemAdminCommand(); 
		cmd.setItemId(1L); 
		RestResponseBase response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		
		List<EhRentalSiteItems> result1 =new ArrayList<EhRentalSiteItems>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.RENTAL_SITE_ID.eq(2L)) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(9, result1.size());
		
 
		List<EhRentalSiteItems> result2 =new ArrayList<EhRentalSiteItems>(); 
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.ID.eq(1L)) 
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(0, result2.size()); 
		
	}
	
	@Test
	public void testUpdateItems() { 

		truncateRentalTable();
		initItemsData();
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		//展示全部
		UpdateItemsAdminCommand cmd = new UpdateItemsAdminCommand();
		cmd.setItemDTOs(new ArrayList<SiteItemDTO>()); 
		SiteItemDTO dto = new SiteItemDTO();
		dto.setId(1L);
		dto.setDefaultOrder(333); 
		dto.setCounts(300);
		dto.setItemType(RentalItemType.SALE.getCode());
		cmd.getItemDTOs().add(dto);
		 
		dto = new SiteItemDTO();
		dto.setId(3L);
		dto.setDefaultOrder(333); 
		dto.setCounts(300);
		dto.setItemType(RentalItemType.RENTAL.getCode());
		cmd.getItemDTOs().add(dto);
		 
 
		
		String commandRelativeUri = "/rental/admin/updateItems";
		RestResponseBase response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
 
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalSiteItems> result1 =new ArrayList<EhRentalSiteItems>();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.ID.eq(1L)) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(1, result1.size());
		assertEquals(cmd.getItemDTOs().get(0).getDefaultOrder(), result1.get(0).getDefaultOrder());
		assertEquals(Integer.valueOf(400), result1.get(0).getCounts());
		assertEquals(null, result1.get(0).getName());
		
		List<EhRentalSiteItems> result2 =new ArrayList<EhRentalSiteItems>();
		dslContext.select()
				.from(Tables.EH_RENTAL_SITE_ITEMS)
				.where(Tables.EH_RENTAL_SITE_ITEMS.ID.eq(3L)) 
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhRentalSiteItems.class));
					return null;
				});
		assertEquals(1, result1.size());
		assertEquals(cmd.getItemDTOs().get(0).getDefaultOrder(), result2.get(0).getDefaultOrder());
		assertEquals(Integer.valueOf(300), result2.get(0).getCounts());
		assertEquals(null, result2.get(0).getName());
		 
		 
	}
	
	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}
	 
    protected void initSrouceData() {
        String sourceInfoFilePath = "data/json/rental2.0-test-data-resource-160627.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
    
    protected void initItemsData() {
        String sourceInfoFilePath = "data/json/rental2.0-test-data-items-160627.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
	

    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        initSrouceData();
        initItemsData();
        
    }
	
}
