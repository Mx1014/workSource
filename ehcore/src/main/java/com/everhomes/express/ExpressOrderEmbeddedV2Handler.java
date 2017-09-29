// @formatter:off
package com.everhomes.express;

import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.EXPRESS_ORDER_CODE )
public class ExpressOrderEmbeddedV2Handler implements PaymentCallBackHandler {

	@Autowired
	private ExpressService expressService;

	@Autowired
	private PayService payService;

	@Override
	public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
		expressService.paySuccess(changeCmd(cmd));
	}

	@Override
	public void payFail(SrvOrderPaymentNotificationCommand cmd) {
		expressService.payFail(changeCmd(cmd));
	}

	@Override
	public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {

	}

	@Override
	public void refundFail(SrvOrderPaymentNotificationCommand cmd) {

	}

	public PayCallbackCommand changeCmd(SrvOrderPaymentNotificationCommand from){
		PayCallbackCommand cmd = new PayCallbackCommand();
		cmd.setOrderNo(from.getBizOrderNum());
		cmd.setPayAmount(payService.changePayAmount(from.getAmount()).toString());
		return cmd;
	}
}
