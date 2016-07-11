package com.everhomes.order;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.RefundCallbackCommand;


@RestController
@RequestMapping("/order")
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * <b>URL: /order/payCallback</b>
	 * <p>支付模块回调接口，通知支付结果</p>
	 */
	@RequestMapping("payCallback")
	@RestReturn(value=String.class)
	public RestResponse payCallback(@Valid PayCallbackCommand  cmd) {
		orderService.payCallback(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /order/refundCallback</b>
	 * <p>支付模块回调接口，通知支付结果</p>
	 */
	@RequestMapping("refundCallback")
	@RestReturn(value=String.class)
	public RestResponse refundCallback(@Valid RefundCallbackCommand  cmd) {
		orderService.refundCallback(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
