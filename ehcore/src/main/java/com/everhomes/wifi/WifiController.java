// @formatter:off
package com.everhomes.wifi;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.pmsy.CreatePmsyBillOrderCommand;
import com.everhomes.rest.pmsy.GetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.GetPmsyBills;
import com.everhomes.rest.pmsy.ListPmsyBillsCommand;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.rest.pmsy.PmsyCommunityDTO;
import com.everhomes.rest.pmsy.PmsyPayerDTO;
import com.everhomes.rest.pmsy.PmsyBillsResponse;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.SearchBillsOrdersResponse;
import com.everhomes.rest.pmsy.SetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.SearchBillsOrdersCommand;

@RestController
@RequestMapping("/wifi")
public class WifiController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(WifiController.class);

	@Autowired
	private WifiService wifiService;

	/**
	 * <b>URL: /pmsy/listPmPayers</b>
	 * <p>获取用户填写过的有效缴费用户信息</p>
	 */
	@RequestMapping("listPmPayers")
	@RestReturn(value=PmsyPayerDTO.class,collection=true)
	public RestResponse listPmPayers(/*@Valid ListPmPayerCommand cmd*/) {
		RestResponse response = new RestResponse();

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

}
