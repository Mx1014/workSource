package com.everhomes.test.junit.rental;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
import com.everhomes.server.schema.tables.pojos.EhRentalv2Items;
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
		
		List<EhRentalv2Items> result1 =new ArrayList<EhRentalv2Items>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.RENTAL_RESOURCE_ID.eq(cmd.getRentalSiteId())) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
					return null;
				});
		//生成11个，json里3个，总共14
		assertEquals(14, result1.size());
		assertEquals(cmd.getItemName(), result1.get(0).getName());
		assertEquals(cmd.getItemType(), result1.get(0).getItemType());
		
	}
 	@Test
	public void testGetItemList() { 
 
//		initItemsData();
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
		//2号资源的商品是id 1到3
		for(int i =0 ;i< 3 ;i++){
			SiteItemDTO dto=response.getResponse().getSiteItems().get(i);
			//default order 和id一样，顺序排列则是从1到10
			assertEquals(i+1, dto.getId().intValue());
			if(dto.getId().equals(1L)){
				//1号商品是售卖的，有20个订单所以这里库存80
				assertEquals(Integer.valueOf(80),dto.getCounts()  );
			}
			else {

				//2号商品是租赁的，不管订单所以这里库存100
				assertEquals(Integer.valueOf(100), dto.getCounts() );
			}
			
		}
		 
		
	}

 	@Test
	public void testUpdateItem() { 
  
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

		List<EhRentalv2Items> result1 =new ArrayList<EhRentalv2Items>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.ID.eq(cmd.getId())) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
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

		List<EhRentalv2Items> result2 =new ArrayList<EhRentalv2Items>(); 
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.ID.eq(cmd.getId())) 
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
					return null;
				});
		assertEquals(1, result1.size());
		assertEquals(cmd.getItemName(), result2.get(0).getName());
		assertEquals(cmd.getItemType(), result2.get(0).getItemType());
		assertEquals(Integer.valueOf(cmd.getCounts()+100), result2.get(0).getCounts());
		 
	}

 	@Test
	public void testDeleteItem() {
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
		
		List<EhRentalv2Items> result1 =new ArrayList<EhRentalv2Items>();
		DSLContext dslContext = dbProvider.getDslContext();
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.RENTAL_RESOURCE_ID.eq(2L)) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
					return null;
				});
		//原本3个减掉1个是2个
		assertEquals(2, result1.size());
		
 
		List<EhRentalv2Items> result2 =new ArrayList<EhRentalv2Items>(); 
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.ID.eq(1L)) 
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
					return null;
				});
		assertEquals(0, result2.size()); 
		
	}
	
	@Test
	public void testUpdateItems() { 
  		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		//展示全部
		UpdateItemsAdminCommand cmd = new UpdateItemsAdminCommand();
		cmd.setItemDTOs(new ArrayList<SiteItemDTO>()); 
		
		for(int i =1 ; i <=10 ; i ++ ){
			SiteItemDTO dto = new SiteItemDTO();
			dto.setId(Long.valueOf(i));
			//将资源倒序排列
			dto.setDefaultOrder(20-i);
			dto.setCounts(300);
			dto.setItemType(RentalItemType.SALE.getCode());
			//暂时不支持售卖和租赁互转这里会判断
			if(i==2)
				dto.setItemType(RentalItemType.RENTAL.getCode());
			cmd.getItemDTOs().add(dto);
		}
		  
		 
 
		
		String commandRelativeUri = "/rental/admin/updateItems";
		RestResponseBase response = httpClientService.restPost(commandRelativeUri,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		//	1号物品由于售卖了20个，所以是设置的300库存+20个，总共320
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalv2Items> result1 =new ArrayList<EhRentalv2Items>();
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.ID.eq(1L)) 
				.fetch()
				.map((r) -> {
					result1.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
					return null;
				});
		assertEquals(1, result1.size());
		assertEquals(Integer.valueOf(320), result1.get(0).getCounts());
//		assertEquals(null, result1.get(0).getName());
//		assertEquals(RentalItemType.SALE.getCode(), result1.get(0).getItemType().byteValue());
		//3号商品没有售卖订单，所以是300个
		List<EhRentalv2Items> result2 =new ArrayList<EhRentalv2Items>();
		dslContext.select()
				.from(Tables.EH_RENTALV2_ITEMS)
				.where(Tables.EH_RENTALV2_ITEMS.ID.eq(3L)) 
				.fetch()
				.map((r) -> {
					result2.add(ConvertHelper.convert(r,
							EhRentalv2Items.class));
					return null;
				});
		assertEquals(1, result1.size()); 
		assertEquals(Integer.valueOf(300), result2.get(0).getCounts());
//		assertEquals(null, result2.get(0).getName());
//		assertEquals(RentalItemType.RENTAL.getCode(), result2.get(0).getItemType().byteValue());
		//通过接口查询，看是否按照更新后顺序输出
		GetItemListAdminCommand cmd2 = new GetItemListAdminCommand();
		cmd2.setRentalSiteId(2L);
		commandRelativeUri = "/rental/admin/getItemList";
		AdminGetItemListRestResponse getItemRsp = httpClientService.restPost(commandRelativeUri,
				cmd2, AdminGetItemListRestResponse.class, context);
		assertNotNull("The reponse of may not be null", getItemRsp);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(getItemRsp),
				httpClientService.isReponseSuccess(getItemRsp));
		assertNotNull("The default rule not null ", getItemRsp.getResponse());
		System.out.print(getItemRsp.getResponse().toString()); 
		assertEquals(3, getItemRsp.getResponse().getSiteItems().size());
		for(int i =0 ;i< 3 ;i++){
			SiteItemDTO dto=getItemRsp.getResponse().getSiteItems().get(i);
			//变成了倒序排列
			assertEquals(3-i, dto.getId().intValue()); 
			//无论是否售卖，接口返回都是设置的库存300个
			assertEquals(300, dto.getCounts().intValue());
		}
		 
		
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
