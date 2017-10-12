package com.everhomes.print;

import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.PRINT_ORDER_CODE)
public class SiYinPrintOrderEmbeddedV2Handler implements PaymentCallBackHandler{

	@Autowired
	public SiYinPrintOrderEmbeddedHandler handler;

	@Autowired
	private PayService payService;

	@Override
	public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
		handler.paySuccess(changeCmd(cmd));
	}

	@Override
	public void payFail(SrvOrderPaymentNotificationCommand cmd) {
		handler.payFail(changeCmd(cmd));
	}

	@Override
	public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {

	}

	@Override
	public void refundFail(SrvOrderPaymentNotificationCommand cmd) {

	}

	public PayCallbackCommand changeCmd(SrvOrderPaymentNotificationCommand from){
		PayCallbackCommand cmd = new PayCallbackCommand();
		cmd.setOrderNo(String.valueOf(from.getOrderId()));
//		cmd.setVendorType(String.valueOf(from.getOrderType()));
		cmd.setPayAmount(payService.changePayAmount(from.getAmount()).toString());
		return cmd;
	}
}
