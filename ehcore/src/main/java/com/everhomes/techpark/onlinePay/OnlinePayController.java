package com.everhomes.techpark.onlinePay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.park.RechargeInfoDTO;
import com.everhomes.techpark.onlinePay.OnlinePayService;

@RestDoc(value = "OnlinePay controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/onlinePay")
public class OnlinePayController  extends ControllerBase{

	@Autowired
	private OnlinePayService onlinePay;
	/**
	 * <b>URL: /techpark/onlinePay/onlinePayBill</b>
	 * <p>线上支付
	 */
	@RequestMapping("onlinePayBill")
	@RestReturn(value=RechargeInfoDTO.class)
	public RestResponse onlinePayBill(OnlinePayBillCommand cmd){
		
		RechargeInfoDTO order = onlinePay.onlinePayBill(cmd);
		RestResponse response = new RestResponse(order);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
