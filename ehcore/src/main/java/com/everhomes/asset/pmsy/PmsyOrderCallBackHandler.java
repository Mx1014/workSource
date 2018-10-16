// @formatter:off
package com.everhomes.asset.pmsy;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.PM_SIYUAN_CODE )
public class PmsyOrderCallBackHandler implements PaymentCallBackHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsyOrderCallBackHandler.class);

	@Override
	public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
		PayCallbackCommand cmd2 = new PayCallbackCommand();
		if(cmd.getOrderId() != null) {
			cmd2.setOrderNo(cmd.getOrderId().toString());
		}
		cmd2.setOrderType(cmd.getOrderType());
		if(cmd.getPayerUserId() != null) {
			cmd2.setPayAccount(cmd.getPayerUserId().toString());
		}
		if(cmd.getAmount() != null) {
			cmd2.setPayAmount(cmd.getAmount().toString());
		}
		if(cmd.getPaymentStatus() != null) {
			cmd2.setPayStatus(cmd.getPaymentStatus().toString());
		}
		cmd2.setPayTime(cmd.getPayDatetime());
		
		
		OrderEmbeddedHandler orderEmbeddedHandler = PlatformContext.getComponent(
				OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+OrderType.PM_SIYUAN_CODE);
		if(cmd2.getPayStatus().equalsIgnoreCase("1"))
			orderEmbeddedHandler.paySuccess(cmd2);
	}
	
	@Override
	public void payFail(SrvOrderPaymentNotificationCommand cmd) {
		//TODO: 失败
		LOGGER.error("Parking pay failed, cmd={}", cmd);
	}

	@Override
	public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {

	}

	@Override
	public void refundFail(SrvOrderPaymentNotificationCommand cmd) {
		
	}

}
