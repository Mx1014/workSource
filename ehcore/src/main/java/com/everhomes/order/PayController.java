package com.everhomes.order;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.order.RefundCallbackCommand;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/pay")
public class PayController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private PayService payService;
	
	/**
	 * <b>URL: /pay/payNotify</b>
	 * <p>支付模块回调接口，通知支付结果</p>
	 */
	@RequestMapping("payNotify")
	@RestReturn(value=String.class)
	@RequireAuthentication(false)
	public RestResponse payNotify(@Valid OrderPaymentNotificationCommand cmd) {
		payService.payNotify(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

//	/**
//	 * <b>URL: /pay/createPreOrder</b>
//	 * <p>用于测试</p>
//	 */
//	@RequestMapping("createPreOrder")
//	@RestReturn(value=PreOrderDTO.class)
//	public RestResponse createPreOrder(PreOrderCommand cmd) {
//		PreOrderDTO dto = payService.createPreOrder(cmd);
//		RestResponse response = new RestResponse(dto);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}



//	/**
//	 * <b>URL: /order/refundCallback</b>
//	 * <p>支付模块回调接口，通知支付结果</p>
//	 */
//	@RequestMapping("refundCallback")
//	@RestReturn(value=String.class)
//	public RestResponse refundCallback(@Valid RefundCallbackCommand  cmd) {
//		payService.refundCallback(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
}
