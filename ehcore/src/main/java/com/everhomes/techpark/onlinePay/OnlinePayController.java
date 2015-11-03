package com.everhomes.techpark.onlinePay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.park.RechargeInfo;

@RestDoc(value = "OnlinePay controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/onlinePay")
public class OnlinePayController  extends ControllerBase{

	@Autowired
	private OnlinePayService onlinePay;
	/**
	 * <b>URL: /techpark/onlinePay/onlinePayBill
	 * <p>线上支付
	 */
	@RequestMapping("onlinePayBill")
	@RestReturn(value=RechargeInfo.class)
	public RestResponse onlinePayBill(OnlinePayBillCommand cmd){
		
		RechargeInfo order = onlinePay.onlinePayBill(cmd);
		RestResponse response = new RestResponse(order);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
