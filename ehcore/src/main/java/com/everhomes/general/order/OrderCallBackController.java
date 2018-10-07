package com.everhomes.general.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.print.GetPrintSettingCommand;
import com.everhomes.rest.print.GetPrintSettingResponse;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value="orderCallBack controller", site="general_order")
@RestController
@RequestMapping("/general_order")
public class OrderCallBackController extends ControllerBase {
	
	@Autowired
	GeneralOrderBizService generalOrderService;
	
	 /**
	  * <b>URL: /general_order/orderCallBack</b>
	  * <p>回调通知</p>
	  */
	 @RequestMapping("orderCallBack")
	 @RestReturn(value=String.class)
	 @RequireAuthentication(false)
	 public RestResponse orderCallBack(OrderCallBackCommand cmd) {
		 generalOrderService.orderCallBack(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	
}
