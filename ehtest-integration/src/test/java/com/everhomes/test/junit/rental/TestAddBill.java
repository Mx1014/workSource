package com.everhomes.test.junit.rental;

import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.rentalv2.AddRentalBillItemCommand;
import com.everhomes.rest.rentalv2.AddRentalItemBillRestResponse;
import com.everhomes.rest.rentalv2.FindRentalSiteWeekStatusRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class TestAddBill extends BaseLoginAuthTestCase {
	@Before
	public void setUp() {

	}
	String userIdentifier = "root";
	String plainTexPassword = "123456";
	// 正常订单
	@Test
	public void testAddItemBill() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		AddRentalBillItemCommand cmd = new AddRentalBillItemCommand();
		cmd.setRentalBillId(8L);
		cmd.setRentalSiteId(74L); 
		String commandRelativeUri = "/rental/addRentalItemBill";
		AddRentalItemBillRestResponse response = httpClientService
				.restGet(commandRelativeUri, cmd,
						AddRentalItemBillRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
	}
}
