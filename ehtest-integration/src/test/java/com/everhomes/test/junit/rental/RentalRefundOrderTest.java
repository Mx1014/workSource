package com.everhomes.test.junit.rental;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.admin.AdminGetRefundOrderListRestResponse;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class RentalRefundOrderTest extends BaseLoginAuthTestCase {

	Integer namespaceId = 0;
	String userIdentifier = "10001";
	String plainTexPassword = "123456";
	
	private Long resoureceTypeId = 510L;
	private String ownerType = RentalOwnerType.COMMUNITY.getCode();
	private Long ownerId = 419L;
	private Long organizationId = 1L;
	private Long hourSitenumberSiteId = 2L;
	private Long hourSiteId = 3L;
	private int[] weekdays={0,1,2,3,4,5,6,7};
	@Before
	public void setUp() {
		super.setUp(); 
	}

	private void truncateRentalTable() {

		String serverInitfilePath = "data/tables/rental2.0_truncate_tables.sql";
		dbProvider.runClassPathSqlFile(serverInitfilePath);

	}
	//查询接口放到一个test里，一面删数据加数据浪费时间
	@Test
	public void testFindAPI(){ 
		testGetRefundOrderList(); 
		testGetWXRefundOrderList();
		try {
			testTimeRefundOrderList();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGetRefundOrderList() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/getRefundOrderList";

		GetRefundOrderListCommand cmd = new GetRefundOrderListCommand();
		cmd.setResourceTypeId(resoureceTypeId);  

		AdminGetRefundOrderListRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminGetRefundOrderListRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 总共6单
		assertEquals(6, response.getResponse().getRefundOrders().size());
		//时间倒序 第一单为5L
		assertEquals(5L, response.getResponse().getRefundOrders().get(0).getId().longValue());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	} 


	public void testGetWXRefundOrderList() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/getRefundOrderList";

		GetRefundOrderListCommand cmd = new GetRefundOrderListCommand();
		cmd.setResourceTypeId(resoureceTypeId);  
		cmd.setVendorType("10002");
		AdminGetRefundOrderListRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminGetRefundOrderListRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 微信支付 3单
		assertEquals(3, response.getResponse().getRefundOrders().size());
		//时间倒序 第一单为2L
		assertEquals(2L, response.getResponse().getRefundOrders().get(0).getId().longValue());
	} 


	public void testTimeRefundOrderList() throws ParseException {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/getRefundOrderList";
		
		GetRefundOrderListCommand cmd = new GetRefundOrderListCommand();
		cmd.setResourceTypeId(resoureceTypeId);  
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = df.parse("2016-7-18");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long timestamp = cal.getTimeInMillis();
	
		cmd.setEndTime(timestamp);
		AdminGetRefundOrderListRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminGetRefundOrderListRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 18日之前是4单
		assertEquals(4, response.getResponse().getRefundOrders().size());
		//时间倒序 第一单为3L
		assertEquals(3L, response.getResponse().getRefundOrders().get(0).getId().longValue());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	} 
	
	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}
	@Override
	protected void initCustomData() {
		String sourceInfoFilePath = "data/json/rental2.0-test-data-resource-160627.txt";
		String filePath = dbProvider
				.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		sourceInfoFilePath = "data/json/rental2.0-test-data-addbill-rules-160630.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		sourceInfoFilePath = "data/json/rental2.0-test-data-items-160627.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);

		sourceInfoFilePath = "data/json/rental2.0-test-data-bills-160704.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);


        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
		sourceInfoFilePath = "data/json/rental2.0-test-data-refund-orders-160713.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}
 
	
}
