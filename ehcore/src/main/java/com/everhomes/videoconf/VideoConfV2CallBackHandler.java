// @formatter:off
package com.everhomes.videoconf;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.order.PaymentServiceConfigHandler;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PaymentCallBackCommand;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX  + OrderType.VIDEOCONF_CODE )
public class VideoConfV2CallBackHandler implements PaymentCallBackHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoConfV2CallBackHandler.class);
 

	@Autowired
	private VideoConfService videoConfService;
	
	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private PayService payService;

	@Override
	public void paySuccess(OrderPaymentNotificationCommand cmd) {
		OnlinePayBillCommand cmd1 = new OnlinePayBillCommand();
		cmd1.setOrderNo(String.valueOf(cmd.getOrderId()));
		cmd1.setPayAccount(cmd.getAmount().toString());
		cmd1.setPayStatus("success");
		videoConfService.onlinePayBillSuccess(cmd1);

	}

	@Override
	public void payFail(OrderPaymentNotificationCommand cmd) {

		OnlinePayBillCommand cmd1 = new OnlinePayBillCommand();
		cmd1.setOrderNo(String.valueOf(cmd.getOrderId()));
		cmd1.setPayAccount(cmd.getAmount().toString());
		cmd1.setPayStatus("fail");
		videoConfService.onlinePayBillFail(cmd1);
	}

	@Override
	public void refundSuccess(OrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
	}

	@Override
	public void refundFail(OrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
	}

	private void checkPayAmount(Long payAmount, BigDecimal chargePrice) {
		if(payAmount == null || chargePrice == null || payAmount.longValue() != chargePrice.multiply(new BigDecimal(100)).longValue()){
			LOGGER.error("payAmount and chargePrice is not equal.");
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_PAYAMOUNT_ERROR,
					"payAmount and chargePrice is not equal.");
		}
	}
}
