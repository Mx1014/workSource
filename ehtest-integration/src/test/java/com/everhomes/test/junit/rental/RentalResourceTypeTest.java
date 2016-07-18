package com.everhomes.test.junit.rental;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.admin.AdminGetResourceTypeListRestResponse;
import com.everhomes.rest.techpark.rental.admin.CreateResourceTypeCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceTypeListCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalResourceTypes;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalResourceTypeTest extends BaseLoginAuthTestCase {

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
	
	@Test
	public void testCreateResourceType(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/createResourceType";

		CreateResourceTypeCommand cmd = new CreateResourceTypeCommand();
		cmd.setIconUri("cs://1/image/aW1hZ2UvTVRwak5qQm1OVGRqT1RjelpqWXpORFV3WXpsaU9UQm1Nalk1WVRsalltWmlOZw");
		cmd.setName("资源的预约");
		cmd.setNamespaceId(namespaceId);
		cmd.setPageType((byte)1);
		cmd.setStatus(null);

		RestResponseBase response = httpClientService.restGet(
				commandRelativeUri, cmd, RestResponseBase.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalResourceTypes> resultBill = new ArrayList<EhRentalResourceTypes>();
		dslContext
				.select()
				.from(Tables.EH_RENTAL_RESOURCE_TYPES)
//				.where(Tables.EH_RENTAL_RESOURCE_TYPES.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					resultBill.add(ConvertHelper.convert(r,
							EhRentalResourceTypes.class));
					return null;
				});
		assertEquals(1, resultBill.size());
		testGetResourceTypeList();
	}
	//查询接口放到一个test里，一面删数据加数据浪费时间
//	@Test
	public void testFindAPI(){ 
		testGetResourceTypeList();  
	}

	public void testGetResourceTypeList() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/getResourceTypeList";

		GetResourceTypeListCommand cmd = new GetResourceTypeListCommand();
		cmd.setNamespaceId(namespaceId);
		
		AdminGetResourceTypeListRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminGetResourceTypeListRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
//		// 总共1个
		assertEquals(1, response.getResponse().getResourceTypes().size());
		assertEquals("资源的预约", response.getResponse().getResourceTypes().get(0).getName());
		assertEquals((byte)0, response.getResponse().getResourceTypes().get(0).getStatus().byteValue());
//		//时间倒序 第一单为5L
//		assertEquals(5L, response.getResponse().getRefundOrders().get(0).getId().longValue());
////		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
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
