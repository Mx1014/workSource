package com.everhomes.test.officecubicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.AddSpaceOrderCommand;
import com.everhomes.rest.officecubicle.DeleteUserSpaceOrderCommand;
import com.everhomes.rest.officecubicle.GetUserOrdersRestResponse;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeOrderStatus;
import com.everhomes.rest.officecubicle.OfficeOrderType;
import com.everhomes.rest.officecubicle.OfficeRentType;
import com.everhomes.rest.officecubicle.OfficeSpaceType;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleOrders;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSpaces;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class OfficeCubicleOrderTest extends BaseLoginAuthTestCase {

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
	public void addOrderTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/addSpaceOrder";
		Long spaceId= 1L;
		AddSpaceOrderCommand cmd = new AddSpaceOrderCommand();  
		cmd.setOrderType(OfficeOrderType.ORDER.getCode());
		cmd.setSpaceId(spaceId);
		cmd.setRentType(OfficeRentType.OPENSITE.getCode());
		cmd.setSpaceType(OfficeSpaceType.SQ_METRE.getCode());
		cmd.setSize(232);
		cmd.setReserveContactToken("1321122335");
		cmd.setReserveEnterprise("apple");
		cmd.setReserverName("Zone");
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		DSLContext dslContext = dbProvider.getDslContext();

		List<EhOfficeCubicleSpaces> spaces = new ArrayList<EhOfficeCubicleSpaces>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_SPACES)
		 .where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.eq(spaceId))
				.fetch().map((r) -> {
					spaces.add(ConvertHelper.convert(r, EhOfficeCubicleSpaces.class));
					return null;
				}); 
		EhOfficeCubicleSpaces space = spaces.get(0);
		List<EhOfficeCubicleOrders> orders = new ArrayList<EhOfficeCubicleOrders>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS)
		 .where(Tables.EH_OFFICE_CUBICLE_ORDERS.SPACE_ID.eq(spaceId))
				.fetch().map((r) -> {
					orders.add(ConvertHelper.convert(r, EhOfficeCubicleOrders.class));
					return null;
				}); 
		EhOfficeCubicleOrders order = orders.get(0);
		assertEquals(order.getSpaceName(), space.getName());
		assertEquals(order.getAddress(), space.getAddress());
		assertEquals(order.getCityName(), space.getCityName());
		assertEquals(order.getContactPhone(), space.getContactPhone());
		assertEquals(order.getCoverUri(), space.getCoverUri());
		assertEquals(order.getDescription(), space.getDescription());
		assertEquals(order.getLatitude(), space.getLatitude());
		assertEquals(order.getLongitude(), space.getLongitude());
		assertEquals(order.getProvinceName(), space.getProvinceName());
		assertEquals(order.getCityId(), space.getCityId());
		assertEquals(order.getManagerUid(), space.getManagerUid());
		assertEquals(order.getProvinceId(), space.getProvinceId()); 
		assertEquals(cmd.getRentType(), order.getRentType());
		assertEquals(cmd.getSpaceType(), order.getSpaceType());
		assertEquals(cmd.getReserveContactToken(), order.getReserveContactToken());
		assertEquals(cmd.getReserverName(), order.getReserverName());
		assertEquals(cmd.getReserveEnterprise(), order.getReserveEnterprise());
		assertEquals(cmd.getSize().toString(), order.getSpaceSize());
		deleteUserSpaceOrderTest(order.getId());
	}
  
	public void deleteUserSpaceOrderTest(Long id){
		String commandRelativeUri = "/officecubicle/deleteUserSpaceOrder";
		DeleteUserSpaceOrderCommand cmd = new DeleteUserSpaceOrderCommand();
		cmd.setOrderId(id);
		RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhOfficeCubicleOrders> orders = new ArrayList<EhOfficeCubicleOrders>();
		dslContext.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS)
		 .where(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.eq(id))
				.fetch().map((r) -> {
					orders.add(ConvertHelper.convert(r, EhOfficeCubicleOrders.class));
					return null;
				}); 
		EhOfficeCubicleOrders order = orders.get(0);
		assertEquals(OfficeOrderStatus.UNVISABLE.getCode(), order.getStatus().byteValue());
	}
	@Test
	public void testSearch(){
		initOrderData();
		getUserOrdersTest();
	}
	/**
	 * query user orders
	 * */
	public void getUserOrdersTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/getUserOrders";	 
		GetUserOrdersRestResponse response = httpClientService.restGet(commandRelativeUri, null, GetUserOrdersRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		 
		List<OfficeOrderDTO>  orderDTOs = response.getResponse() ;
		//36个可见的
		assertEquals(36, orderDTOs.size());
//		assertEquals("空间名称11", spaceDTOs.get(0).getName());
	}
	/**
	 * search orders 
	 * */
	public void searchSpaceOrdersTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/searchSpaceOrders";	 
		SearchSpaceOrdersCommand cmd =new SearchSpaceOrdersCommand();
		cmd.setPageSize(50);
		SearchSpaceOrdersRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchSpaceOrdersRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		List<OfficeOrderDTO>  orderDTOs = response.getResponse().getOrders() ;
		//40个全部查询出来
		assertEquals(40, orderDTOs.size());
//		assertEquals("空间名称11", spaceDTOs.get(0).getName());
	}

	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * search orders  by time
	 * */
	public void searchSpaceOrdersByTimeTest() {
		logon(namespaceId, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/officecubicle/searchSpaceOrders"; 
		SearchSpaceOrdersCommand cmd =new SearchSpaceOrdersCommand();
		
		cmd.setPageSize(50);
		try {
			cmd.setBeginDate(dateSF.parse("2016-07-24").getTime());
			cmd.setEndDate(dateSF.parse("2016-07-25").getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SearchSpaceOrdersRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchSpaceOrdersRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response=" + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		 
		List<OfficeOrderDTO>  orderDTOs = response.getResponse().getOrders() ;
		//只有一个24日的查出来
		assertEquals(1, orderDTOs.size());
		assertEquals("空间名称1", orderDTOs.get(0).getSpaceName());
	}
	@Override
	public void initCustomData() {
		String sourceInfoFilePath = "data/json/ibase-office-test-data-userinfo_160726.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
		initSpaceData();
	}

	public void initSpaceData() {
		String sourceInfoFilePath = "data/json/ibase-office-test-data-spaces_160727.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

	}
	

	public void initOrderData() {
		String sourceInfoFilePath = "data/json/ibase-office-test-data-orders_160727.txt";
		String filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

	}
	
}
