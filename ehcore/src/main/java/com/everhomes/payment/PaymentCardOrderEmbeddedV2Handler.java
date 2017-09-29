package com.everhomes.payment;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/25.
 */
@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.PAYMENT_CARD_CODE )
public class PaymentCardOrderEmbeddedV2Handler implements PaymentCallBackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardOrderEmbeddedV2Handler.class);

    @Autowired
    private PaymentCardProvider paymentCardProvider;
    @Autowired
    private PayService payservice;
    @Autowired
    private CoordinationProvider coordinationProvider;

    @Override
    public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
        this.checkOrderNoIsNull(cmd.getOrderId());
        LOGGER.info("PaymentCardOrderEmbeddedV2Handler paySuccess start cmd = {}", cmd);
        PaymentCardRechargeOrder order = checkOrder(cmd.getOrderId());
        if (0 != order.getAmount().compareTo(payservice.changePayAmount(cmd.getAmount()))) {
            LOGGER.error("Order amount is not equal to payAmount, cmd={}, order={}", cmd, order);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Order amount is not equal to payAmount.");
        }

        this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()+cmd.getOrderId()).tryEnter(()-> {
            PaymentCard paymentCard = paymentCardProvider.findPaymentCardById(order.getCardId());
            PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());

            Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
            order.setPayStatus(CardOrderStatus.PAID.getCode());
            order.setPaidTime(payTimeStamp);
            order.setPaidType(String.valueOf(cmd.getPaymentType()));
            paymentCardProvider.updatePaymentCardRechargeOrder(order);

            handler.rechargeCard(order, paymentCard);
        });

        LOGGER.info("PaymentCardOrderEmbeddedV2Handler paySuccess end");
    }

    @Override
     public void payFail(SrvOrderPaymentNotificationCommand cmd) {
        this.checkOrderNoIsNull(cmd.getOrderId());
        PaymentCardRechargeOrder order = checkOrder(cmd.getOrderId());
        Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
        order.setPayStatus(CardOrderStatus.INACTIVE.getCode());
        order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
        order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
        order.setPaidTime(payTimeStamp);
        order.setPaidType(String.valueOf(cmd.getPaymentType()));
        paymentCardProvider.updatePaymentCardRechargeOrder(order);
    }

    @Override
    public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {

    }

    @Override
    public void refundFail(SrvOrderPaymentNotificationCommand cmd) {

    }

    private PaymentCardRechargeOrder checkOrder(Long orderId) {
        PaymentCardRechargeOrder order = paymentCardProvider.findPaymentCardRechargeOrderById(orderId);

        if(order == null){
            LOGGER.error("the order {} not found.",orderId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "the order not found.");
        }
        return order;
    }

    private PaymentCardVendorHandler getPaymentCardVendorHandler(String vendorName) {
        PaymentCardVendorHandler handler = null;

        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }

        return handler;
    }

    private void checkOrderNoIsNull(Long orderNo) {
        if(orderNo==null){
            LOGGER.error("orderNo is null or empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "orderNo is null or empty.");
        }
    }


}
