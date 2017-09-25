// @formatter:off
package com.everhomes.express;

import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.EXPRESS_ORDER_CODE )
public class ExpressOrderEmbeddedV2Handler implements PaymentCallBackHandler {

	@Autowired
	private ExpressService expressService;

	@Autowired
	private PayService payService;

	@Override
	public void paySuccess(OrderPaymentNotificationCommand cmd) {
		expressService.paySuccess(changeCmd(cmd));
	}

	@Override
	public void payFail(OrderPaymentNotificationCommand cmd) {
		expressService.payFail(changeCmd(cmd));
	}

	@Override
	public void refundSuccess(OrderPaymentNotificationCommand cmd) {

	}

	@Override
	public void refundFail(OrderPaymentNotificationCommand cmd) {

	}

	public PayCallbackCommand changeCmd(OrderPaymentNotificationCommand from){
		PayCallbackCommand cmd = new PayCallbackCommand();
		cmd.setOrderNo(from.getBizOrderNum());
		cmd.setPayAmount(payService.changePayAmount(from.getAmount()).toString());
		return cmd;
	}
}
