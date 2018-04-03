// @formatter:off
package com.everhomes.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayServiceErrorCode;
import com.everhomes.rest.order.PaymentWithdrawOrderStatus;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.util.RuntimeErrorException;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.WITHDRAW_CODE )
public class WithdrawOrderCallBackHandler implements PaymentCallBackHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawOrderCallBackHandler.class);
        
	@Autowired
	private CoordinationProvider coordinationProvider;

    @Autowired
    private PayProvider payProvider;

	@Override
	public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
		PaymentWithdrawOrder order = payProvider.findPaymentWithdrawOrderByOrderNo(cmd.getOrderId());
		if(order == null){
			LOGGER.info("Withdraw order not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_WITHDRAW_ORDER_NOT_FOUND,
					"Withdraw order not found");
		}
		
		PaymentWithdrawOrderStatus status = PaymentWithdrawOrderStatus.fromCode(order.getStatus());
		if(status == PaymentWithdrawOrderStatus.WAITING_FOR_CONFIRM) {
		    order.setStatus(PaymentWithdrawOrderStatus.SUCCESS.getCode());
		    payProvider.updatePaymentWithdrawOrder(order);
            LOGGER.info("Confirm withdrawing order as success status, cmd={}", cmd);
		} else {
		    LOGGER.warn("Receive duplicated notification of withdrawing success, cmd={}", cmd);
		}
	}

	@Override
	public void payFail(SrvOrderPaymentNotificationCommand cmd) {
	    PaymentWithdrawOrder order = payProvider.findPaymentWithdrawOrderByOrderNo(cmd.getOrderId());
        if(order == null){
            LOGGER.info("Withdraw order not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_WITHDRAW_ORDER_NOT_FOUND,
                    "Withdraw order not found");
        }
        
        PaymentWithdrawOrderStatus status = PaymentWithdrawOrderStatus.fromCode(order.getStatus());
        if(status == PaymentWithdrawOrderStatus.WAITING_FOR_CONFIRM) {
            order.setStatus(PaymentWithdrawOrderStatus.SUCCESS.getCode());
            payProvider.updatePaymentWithdrawOrder(order);
            LOGGER.info("Confirm withdrawing order as failed status, cmd={}", cmd);
        } else {
            LOGGER.warn("Receive duplicated notification of withdrawing fail, cmd={}", cmd);
        }
	}

	@Override
	public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
	}

	@Override
	public void refundFail(SrvOrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
	}
}
