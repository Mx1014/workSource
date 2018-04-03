// @formatter:off
package com.everhomes.test.junit.express;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.express.AddExpressUserCommand;
import com.everhomes.rest.express.CancelExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderResponse;
import com.everhomes.rest.express.CreateExpressOrderRestResponse;
import com.everhomes.rest.express.CreateExpressUserDTO;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressResponse;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressRestResponse;
import com.everhomes.rest.express.DeleteExpressAddressCommand;
import com.everhomes.rest.express.DeleteExpressUserCommand;
import com.everhomes.rest.express.ExpressOrderDTO;
import com.everhomes.rest.express.ExpressOrderStatus;
import com.everhomes.rest.express.ExpressServiceAddressDTO;
import com.everhomes.rest.express.GetExpressLogisticsDetailCommand;
import com.everhomes.rest.express.GetExpressLogisticsDetailRestResponse;
import com.everhomes.rest.express.GetExpressOrderDetailCommand;
import com.everhomes.rest.express.GetExpressOrderDetailResponse;
import com.everhomes.rest.express.GetExpressOrderDetailRestResponse;
import com.everhomes.rest.express.ListExpressAddressCommand;
import com.everhomes.rest.express.ListExpressAddressResponse;
import com.everhomes.rest.express.ListExpressAddressRestResponse;
import com.everhomes.rest.express.ListExpressCompanyCommand;
import com.everhomes.rest.express.ListExpressCompanyResponse;
import com.everhomes.rest.express.ListExpressCompanyRestResponse;
import com.everhomes.rest.express.ListExpressOrderCommand;
import com.everhomes.rest.express.ListExpressOrderResponse;
import com.everhomes.rest.express.ListExpressOrderRestResponse;
import com.everhomes.rest.express.ListExpressQueryHistoryResponse;
import com.everhomes.rest.express.ListExpressQueryHistoryRestResponse;
import com.everhomes.rest.express.ListExpressUserCommand;
import com.everhomes.rest.express.ListExpressUserResponse;
import com.everhomes.rest.express.ListExpressUserRestResponse;
import com.everhomes.rest.express.ListPersonalExpressOrderCommand;
import com.everhomes.rest.express.ListPersonalExpressOrderResponse;
import com.everhomes.rest.express.ListPersonalExpressOrderRestResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;
import com.everhomes.rest.express.ListServiceAddressRestResponse;
import com.everhomes.rest.express.PayExpressOrderCommand;
import com.everhomes.rest.express.PrintExpressOrderCommand;
import com.everhomes.rest.express.UpdatePaySummaryCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ExpressTest extends BaseLoginAuthTestCase{
	//1. 自寄服务地址列表
	private static final String LIST_SERVICE_ADDRESS_URL = "/express/listServiceAddress";
	//2. 快递公司列表
	private static final String LIST_EXPRESS_COMPANY_URL = "/express/listExpressCompany";
	//3. 快递人员列表
	private static final String LIST_EXPRESS_USER_URL = "/express/listExpressUser";
	//4. 添加快递人员
	private static final String ADD_EXPRESS_USER_URL = "/express/addExpressUser";
	//5. 删除快递人员
	private static final String DELETE_EXPRESS_USER_URL = "/express/deleteExpressUser";
	//6. 快递订单列表（后台）
	private static final String LIST_EXPRESS_ORDER_URL = "/express/listExpressOrder";
	//7. 快递订单详情
	private static final String GET_EXPRESS_ORDER_DETAIL_URL = "/express/getExpressOrderDetail";
	//8. 修改付费总计
	private static final String UPDATE_PAY_SUMMARY_URL = "/express/updatePaySummary";
	//9. 立即支付
	private static final String PAY_EXPRESS_ORDER_URL = "/express/payExpressOrder";
	//10. 出单
	private static final String PRINT_EXPRESS_ORDER_URL = "/express/printExpressOrder";
	//11. 添加地址
	private static final String CREATE_OR_UPDATE_EXPRESS_ADDRESS_URL = "/express/createOrUpdateExpressAddress";
	//12. 删除地址
	private static final String DELETE_EXPRESS_ADDRESS_URL = "/express/deleteExpressAddress";
	//13. 地址列表
	private static final String LIST_EXPRESS_ADDRESS_URL = "/express/listExpressAddress";
	//14. 寄快递
	private static final String CREATE_EXPRESS_ORDER_URL = "/express/createExpressOrder";
	//15. 快递订单列表（个人）
	private static final String LIST_PERSONAL_EXPRESS_ORDER_URL = "/express/listPersonalExpressOrder";
	//16. 取消订单
	private static final String CANCEL_EXPRESS_ORDER_URL = "/express/cancelExpressOrder";
	//17. 查看物流详情
	private static final String GET_EXPRESS_LOGISTICS_DETAIL_URL = "/express/getExpressLogisticsDetail";
	//18. 查询快递历史列表
	private static final String LIST_EXPRESS_QUERY_HISTORY_URL = "/express/listExpressQueryHistory";
	//19. 清空快递查询历史
	private static final String CLEAR_EXPRESS_QUERY_HISTORY_URL = "/express/clearExpressQueryHistory";

	@Value("${namespace.id}")
	private Integer namespaceId;
	
	//1. 自寄服务地址列表（已完成）
	//@Test
	public void testListServiceAddress() {
		String url = LIST_SERVICE_ADDRESS_URL;
		logon();

		ListServiceAddressCommand cmd = new ListServiceAddressCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);

		ListServiceAddressRestResponse response = httpClientService.restPost(url, cmd, ListServiceAddressRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListServiceAddressResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		List<ExpressServiceAddressDTO> list = myResponse.getServiceAddressDTOs();
		assertNotNull(list);
		assertEquals(2, list.size());
		ExpressServiceAddressDTO dto = list.get(0);
		assertNotNull(dto.getId());
		assertNotNull(dto.getName());

	}

	//2. 快递公司列表（已完成）
	//@Test
	public void testListExpressCompany() {
		String url = LIST_EXPRESS_COMPANY_URL;
		logon();

		ListExpressCompanyCommand cmd = new ListExpressCompanyCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);

		ListExpressCompanyRestResponse response = httpClientService.restPost(url, cmd, ListExpressCompanyRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListExpressCompanyResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressCompanyDTOs());
		assertEquals(2, myResponse.getExpressCompanyDTOs().size());

	}

	//3. 快递人员列表（已完成）
	//@Test
	public void testListExpressUser() {
		String url = LIST_EXPRESS_USER_URL;
		logon();

		ListExpressUserCommand cmd = new ListExpressUserCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setServiceAddressId(1L);
		cmd.setExpressCompanyId(11L);
//		cmd.setPageAnchor(2L);
		cmd.setPageSize(2);

		ListExpressUserRestResponse response = httpClientService.restPost(url, cmd, ListExpressUserRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListExpressUserResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressUserDTOs());
		assertNotNull(myResponse.getNextPageAnchor());
		assertEquals(2, myResponse.getExpressUserDTOs().size());


	}

	//4. 添加快递人员（已完成）
	//@Test
	public void testAddExpressUser() {
		String url = ADD_EXPRESS_USER_URL;
		logon();

		AddExpressUserCommand cmd = new AddExpressUserCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setServiceAddressId(1L);
		cmd.setExpressCompanyId(11L);
		List<CreateExpressUserDTO> createExpressUserDTOList = new ArrayList<>();
		CreateExpressUserDTO createExpressUserDTO = new CreateExpressUserDTO();
		createExpressUserDTO.setOrganizationId(1000760L);
		createExpressUserDTO.setOrganizationMemberId(2101442L);
		createExpressUserDTOList.add(createExpressUserDTO);
		CreateExpressUserDTO createExpressUserDTO2 = new CreateExpressUserDTO();
		createExpressUserDTO2.setOrganizationId(1000760L);
		createExpressUserDTO2.setOrganizationMemberId(2101440L);
		createExpressUserDTOList.add(createExpressUserDTO2);
		cmd.setExpressUsers(createExpressUserDTOList);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> records = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_USERS).where(Tables.EH_EXPRESS_USERS.ORGANIZATION_ID.eq(1000760L)).fetch();
		assertNotNull(records);
		assertEquals(2, records.size());
		
	}

	//5. 删除快递人员（已完成）
	//@Test
	public void testDeleteExpressUser() {
		String url = DELETE_EXPRESS_USER_URL;
		logon();

		DeleteExpressUserCommand cmd = new DeleteExpressUserCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> records = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_USERS).where(Tables.EH_EXPRESS_USERS.ID.eq(1L)).fetch();
		assertNotNull(records);
		assertEquals(CommonStatus.INACTIVE.getCode(), records.get(0).getValue(Tables.EH_EXPRESS_USERS.STATUS).byteValue());

	}

	//6. 快递订单列表（后台）（已完成）
	//@Test
	public void testListExpressOrder() {
		String url = LIST_EXPRESS_ORDER_URL;
		logon();

		ListExpressOrderCommand cmd = new ListExpressOrderCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setServiceAddressId(1L);
		cmd.setExpressCompanyId(11L);
		cmd.setStatus((byte)1);
		cmd.setKeyword("t");
//		cmd.setPageAnchor(2L);
		cmd.setPageSize(2);

		ListExpressOrderRestResponse response = httpClientService.restPost(url, cmd, ListExpressOrderRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListExpressOrderResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressOrderDTOs());
		assertNotNull(myResponse.getNextPageAnchor());
		assertEquals(2, myResponse.getExpressOrderDTOs().size());


	}

	//7. 快递订单详情（已完成）
	//@Test
	public void testGetExpressOrderDetail() {
		String url = GET_EXPRESS_ORDER_DETAIL_URL;
		logon();

		GetExpressOrderDetailCommand cmd = new GetExpressOrderDetailCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(1L);

		GetExpressOrderDetailRestResponse response = httpClientService.restPost(url, cmd, GetExpressOrderDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetExpressOrderDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		ExpressOrderDTO dto = myResponse.getExpressOrderDTO();
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertNotNull(dto.getOrderNo());


	}

	//8. 修改付费总计（已完成）
	//@Test
	public void testUpdatePaySummary() {
		String url = UPDATE_PAY_SUMMARY_URL;
		logon();

		UpdatePaySummaryCommand cmd = new UpdatePaySummaryCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(1L);
		cmd.setPaySummary(new BigDecimal("111.23"));

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_ORDERS).where(Tables.EH_EXPRESS_ORDERS.ID.eq(1L)).fetchOne();
		assertNotNull(record);
		assertEquals(new BigDecimal("111.23"), record.getValue(Tables.EH_EXPRESS_ORDERS.PAY_SUMMARY));
		
		
	}

	//9. 立即支付
	////@Test
	public void testPayExpressOrder() {
		String url = PAY_EXPRESS_ORDER_URL;
		logon();

		PayExpressOrderCommand cmd = new PayExpressOrderCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//10. 出单（已完成）
	//@Test
	public void testPrintExpressOrder() {
		String url = PRINT_EXPRESS_ORDER_URL;
		logon();

		PrintExpressOrderCommand cmd = new PrintExpressOrderCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(4L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_ORDERS).where(Tables.EH_EXPRESS_ORDERS.ID.eq(4L)).fetchOne();
		assertNotNull(record);
		assertEquals(ExpressOrderStatus.PRINTED.getCode(), record.getValue(Tables.EH_EXPRESS_ORDERS.STATUS));

	}

	//11. 添加地址（已完成）
	//@Test
	public void testCreateOrUpdateExpressAddress() {
		String url = CREATE_OR_UPDATE_EXPRESS_ADDRESS_URL;
		logon();

		CreateOrUpdateExpressAddressCommand cmd = new CreateOrUpdateExpressAddressCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setCategory((byte)1);
		cmd.setUserName("tt1");
		cmd.setPhone("13265767393");
		cmd.setOrganizationId(1000750L);
		cmd.setOrganizationName("深业集团（深圳）物业管理有限公司");
		cmd.setProvince("广东省");
		cmd.setCity("深圳市");
		cmd.setCounty("南山区");
		cmd.setDetailAddress("金融基地2栋7F");
		cmd.setDefaultFlag((byte)0);

		CreateOrUpdateExpressAddressRestResponse response = httpClientService.restPost(url, cmd, CreateOrUpdateExpressAddressRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateOrUpdateExpressAddressResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressAddressDTO());
		assertEquals("tt1", myResponse.getExpressAddressDTO().getUserName());


	}

	//12. 删除地址（已完成）
	//@Test
	public void testDeleteExpressAddress() {
		String url = DELETE_EXPRESS_ADDRESS_URL;
		logon();

		DeleteExpressAddressCommand cmd = new DeleteExpressAddressCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_ADDRESSES).where(Tables.EH_EXPRESS_ADDRESSES.ID.eq(1L)).fetchOne();
		assertNotNull(record);
		assertEquals(CommonStatus.INACTIVE.getCode(), record.getValue(Tables.EH_EXPRESS_ADDRESSES.STATUS).byteValue());

	}

	//13. 地址列表（已完成）
	//@Test
	public void testListExpressAddress() {
		String url = LIST_EXPRESS_ADDRESS_URL;
		logon();

		ListExpressAddressCommand cmd = new ListExpressAddressCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setCategory((byte)1);

		ListExpressAddressRestResponse response = httpClientService.restPost(url, cmd, ListExpressAddressRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListExpressAddressResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressAddressDTOs());
		assertEquals(3, myResponse.getExpressAddressDTOs().size());

	}

	//14. 寄快递（已完成）
	//@Test
	public void testCreateExpressOrder() {
		String url = CREATE_EXPRESS_ORDER_URL;
		logon();

		CreateExpressOrderCommand cmd = new CreateExpressOrderCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setSendAddressId(1L);
		cmd.setReceiveAddressId(4L);
		cmd.setExpressCompanyId(11L);
		cmd.setSendType((byte)1);
		cmd.setSendMode((byte)1);
		cmd.setServiceAddressId(1L);
		cmd.setPayType((byte)1);
		cmd.setInternal("文件");
		cmd.setInsuredPrice(new BigDecimal("10.23"));

		CreateExpressOrderRestResponse response = httpClientService.restPost(url, cmd, CreateExpressOrderRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateExpressOrderResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressOrderDTO());
		assertNotNull(myResponse.getExpressOrderDTO().getSendName());
		assertNotNull(myResponse.getExpressOrderDTO().getReceiveName());

	}

	//15. 快递订单列表（个人）（已完成）
	//@Test
	public void testListPersonalExpressOrder() {
		String url = LIST_PERSONAL_EXPRESS_ORDER_URL;
		logon();

		ListPersonalExpressOrderCommand cmd = new ListPersonalExpressOrderCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
//		cmd.setStatus((byte)1);
//		cmd.setPageAnchor(2L);
		cmd.setPageSize(2);

		ListPersonalExpressOrderRestResponse response = httpClientService.restPost(url, cmd, ListPersonalExpressOrderRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListPersonalExpressOrderResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressOrderDTOs());


	}

	//16. 取消订单（已完成）
	//@Test
	public void testCancelExpressOrder() {
		String url = CANCEL_EXPRESS_ORDER_URL;
		logon();

		CancelExpressOrderCommand cmd = new CancelExpressOrderCommand();
		cmd.setOwnerType("community");
		cmd.setOwnerId(240111044331051300L);
		cmd.setId(3L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_ORDERS).where(Tables.EH_EXPRESS_ORDERS.ID.eq(3L)).fetchOne();
		assertNotNull(record);
		assertEquals(ExpressOrderStatus.CANCELLED.getCode(), record.getValue(Tables.EH_EXPRESS_ORDERS.STATUS));
		
	}

	//17. 查看物流详情（已完成）
	//@Test
	public void testGetExpressLogisticsDetail() {
		String url = GET_EXPRESS_LOGISTICS_DETAIL_URL;
		logon();

		GetExpressLogisticsDetailCommand cmd = new GetExpressLogisticsDetailCommand();
		cmd.setExpressCompanyId(1L);
		cmd.setBillNo("TEST105370795");

		GetExpressLogisticsDetailRestResponse response = httpClientService.restPost(url, cmd, GetExpressLogisticsDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

//		GetExpressLogisticsDetailResponse myResponse = response.getResponse();
//		assertNotNull(myResponse);
		//这个没办法测，没有正常的EMS订单

	}

	//18. 查询快递历史列表（已完成）
	@Test
	public void testListExpressQueryHistory() {
		String url = LIST_EXPRESS_QUERY_HISTORY_URL;
		logon();
		ListPersonalExpressOrderCommand cmd = new ListPersonalExpressOrderCommand();
		cmd.setPageSize(4);
		
		ListExpressQueryHistoryRestResponse response = httpClientService.restPost(url, cmd, ListExpressQueryHistoryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListExpressQueryHistoryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getExpressQueryHistoryDTOs());
		assertEquals(3, myResponse.getExpressQueryHistoryDTOs().size());

	}

	//19. 清空快递查询历史（已完成）
	//@Test
	public void testClearExpressQueryHistory() {
		String url = CLEAR_EXPRESS_QUERY_HISTORY_URL;
		logon();

		RestResponseBase response = httpClientService.restPost(url, null, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Result<Record> result = dbProvider.getDslContext().select().from(Tables.EH_EXPRESS_QUERY_HISTORIES).where(Tables.EH_EXPRESS_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode())).fetch();
		assertTrue(result == null || result.isEmpty());
		
	}





	@After
	public void tearDown() {
		logoff();
	}

	private void logon() {
		String userIdentifier = "tt";
		String plainTexPassword = "123456";
		Integer namespaceId = this.namespaceId;
		logon(namespaceId, userIdentifier, plainTexPassword);
	}
	
	@Before
	public void setUp() {
		super.newSetUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/express-1.0.0-test-data-170420.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
}
