package com.everhomes.test.junit.rental;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.techpark.rental.BillQueryStatus;
import com.everhomes.rest.techpark.rental.CompleteBillCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommand;
import com.everhomes.rest.techpark.rental.FindUserRentalBillsRestResponse;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.techpark.rental.SiteBillStatus;
import com.everhomes.rest.techpark.rental.admin.AdminCompleteBillRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalRefundOrderTest extends BaseLoginAuthTestCase {

	Integer namespaceId = 0;
	String userIdentifier = "10001";
	String plainTexPassword = "123456";
	
	private Long launchPadItemId = 510L;
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
		testFindUserValiRentalBills();
		testFindUserCancelRentalBills();
		testFindUserFinishedRentalBills();
		testListAllRentalBills();
		testListOnePageRentalBills();
		testCompleteFailBill();
		testCompleteOvertimeBill();
		testCompleteRefoundBill();
		testCompleteSucessBill();
	}

	public void testFindUserValiRentalBills() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findUserRentalBills";

		FindRentalBillsCommand cmd = new FindRentalBillsCommand();
		cmd.setLaunchPadItemId(launchPadItemId);
		cmd.setBillStatus(BillQueryStatus.VALID.getCode());
		

		FindUserRentalBillsRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, FindUserRentalBillsRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 有效订单：3号已成功预约 和 4号待支付
		assertEquals(2, response.getResponse().getRentalBills().size());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	}
	public void testFindUserCancelRentalBills() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findUserRentalBills";

		FindRentalBillsCommand cmd = new FindRentalBillsCommand();
		cmd.setLaunchPadItemId(launchPadItemId);
		cmd.setBillStatus(BillQueryStatus.CANCELED.getCode());
		

		FindUserRentalBillsRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, FindUserRentalBillsRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 取消订单：2号失败 和5号退款中 6号已退款
		assertEquals(3, response.getResponse().getRentalBills().size());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	}
	public void testFindUserFinishedRentalBills() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/findUserRentalBills";

		FindRentalBillsCommand cmd = new FindRentalBillsCommand();
		cmd.setLaunchPadItemId(launchPadItemId);
		cmd.setBillStatus(BillQueryStatus.FINISHED.getCode());
		

		FindUserRentalBillsRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, FindUserRentalBillsRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 完成订单：7号已完成 和8号到22号已过期,23和24号不是该用户
		assertEquals(16, response.getResponse().getRentalBills().size());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	}
 

	public void testListOnePageRentalBills() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/listRentalBills";

		ListRentalBillsCommand cmd = new ListRentalBillsCommand();
		cmd.setOrganizationId(organizationId);
		cmd.setLaunchPadItemId(launchPadItemId);
//		cmd.setBillStatus(BillQueryStatus.FINISHED.getCode());
		cmd.setPageSize(10);

		FindUserRentalBillsRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, FindUserRentalBillsRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 一页10个，24到15L，下一页anchor是15L
		assertEquals(10, response.getResponse().getRentalBills().size());
		assertEquals(15L, response.getResponse().getNextPageAnchor().longValue());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	}

	public void testListAllRentalBills() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/listRentalBills";

		ListRentalBillsCommand cmd = new ListRentalBillsCommand();
		cmd.setOrganizationId(organizationId);
		cmd.setLaunchPadItemId(launchPadItemId);
//		cmd.setBillStatus(BillQueryStatus.FINISHED.getCode());
		cmd.setPageSize(40); 
		FindUserRentalBillsRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, FindUserRentalBillsRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		// 一页10个，24到15L，下一页anchor是15L
		assertEquals(23, response.getResponse().getRentalBills().size());
//		assertEquals(15L, response.getResponse().getNextPageAnchor().longValue());
//		assertEquals(Double.valueOf(2), response.getResponse().getRentalSites().get(0).getTimeStep());
	}

	public void testCompleteSucessBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/completeBill";

		CompleteBillCommand cmd = new CompleteBillCommand();
		cmd.setRentalBillId(3L); 
		AdminCompleteBillRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminCompleteBillRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertEquals(SiteBillStatus.COMPLETE.getCode(), response.getResponse().getStatus().byteValue());

		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalBills> resultBill = new ArrayList<EhRentalBills>();
		dslContext
				.select()
				.from(Tables.EH_RENTAL_BILLS)
				.where(Tables.EH_RENTAL_BILLS.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					resultBill.add(ConvertHelper.convert(r,
							EhRentalBills.class));
					return null;
				});
		assertEquals(SiteBillStatus.COMPLETE.getCode(), resultBill.get(0).getStatus().byteValue());
	}

	public void testCompleteOvertimeBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/completeBill";

		CompleteBillCommand cmd = new CompleteBillCommand();
		cmd.setRentalBillId(22L); 
		AdminCompleteBillRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminCompleteBillRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		assertEquals(SiteBillStatus.COMPLETE.getCode(), response.getResponse().getStatus().byteValue());
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalBills> resultBill = new ArrayList<EhRentalBills>();
		dslContext
				.select()
				.from(Tables.EH_RENTAL_BILLS)
				.where(Tables.EH_RENTAL_BILLS.ID.eq( cmd.getRentalBillId()))
				.fetch()
				.map((r) -> {
					resultBill.add(ConvertHelper.convert(r,
							EhRentalBills.class));
					return null;
				});
		assertEquals(SiteBillStatus.COMPLETE.getCode(), resultBill.get(0).getStatus().byteValue());
	}

	public void testCompleteFailBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/completeBill";

		CompleteBillCommand cmd = new CompleteBillCommand();
		cmd.setRentalBillId(4L); 
		AdminCompleteBillRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminCompleteBillRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				response.getErrorCode().equals(RentalServiceErrorCode.ERROR_NOT_SUCCESS));
 
	}
	
	public void testCompleteRefoundBill(){

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/completeBill";

		CompleteBillCommand cmd = new CompleteBillCommand();
		cmd.setRentalBillId(5L); 
		AdminCompleteBillRestResponse response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminCompleteBillRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				response.getErrorCode().equals(RentalServiceErrorCode.ERROR_NOT_SUCCESS));
		

		cmd.setRentalBillId(6L); 
		response = httpClientService.restGet(
				commandRelativeUri, cmd, AdminCompleteBillRestResponse.class,
				context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				response.getErrorCode().equals(RentalServiceErrorCode.ERROR_NOT_SUCCESS));
 
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


		sourceInfoFilePath = "rental2.0-test-data-refund-orders-160713.txt";
		filePath = dbProvider.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}
 
	
}
