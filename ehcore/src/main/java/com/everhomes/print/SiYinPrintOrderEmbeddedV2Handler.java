package com.everhomes.print;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.print.PrintOrderLockType;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.PRINT_ORDER_CODE)
public class SiYinPrintOrderEmbeddedV2Handler implements PaymentCallBackHandler{

	@Autowired
	public SiYinPrintOrderEmbeddedHandler handler;

	@Autowired
	private PayService payService;

	@Override
	public void paySuccess(OrderPaymentNotificationCommand cmd) {
		handler.paySuccess(changeCmd(cmd));
	}

	@Override
	public void payFail(OrderPaymentNotificationCommand cmd) {
		handler.payFail(changeCmd(cmd));
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
		cmd.setVendorType(String.valueOf(from.getOrderType()));
		cmd.setPayAmount(payService.changePayAmount(from.getAmount()).toString());
		return cmd;
	}
}
