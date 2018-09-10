package com.everhomes.payment;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
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
    private CoordinationProvider coordinationProvider;

    @Override
    public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
        this.checkOrderNoIsNull(cmd.getBizOrderNum());
        LOGGER.info("PaymentCardOrderEmbeddedV2Handler paySuccess start cmd = {}", cmd);
        PaymentCardRechargeOrder order = checkOrder(cmd.getBizOrderNum());


        this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()+cmd.getOrderId()).tryEnter(()-> {
            PaymentCard paymentCard = paymentCardProvider.findPaymentCardById(order.getCardId());
            PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());

            Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
            order.setPayStatus(CardOrderStatus.PAID.getCode());
            order.setPaidTime(payTimeStamp);
            switch (cmd.getPaymentType()){
                case 1:
                case 7:
                case 9:
                case 21: order.setPaidType(VendorType.WEI_XIN.getCode());break;
                default: order.setPaidType(VendorType.ZHI_FU_BAO.getCode());break;
            }
            paymentCardProvider.updatePaymentCardRechargeOrder(order);

            handler.rechargeCard(order, paymentCard);
        });

        LOGGER.info("PaymentCardOrderEmbeddedV2Handler paySuccess end");
    }

    @Override
     public void payFail(SrvOrderPaymentNotificationCommand cmd) {
//        this.checkOrderNoIsNull(cmd.getOrderId());
//        PaymentCardRechargeOrder order = checkOrder(cmd.getOrderId());
//        Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
//        order.setPayStatus(CardOrderStatus.INACTIVE.getCode());
//        order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
//        order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
//        order.setPaidTime(payTimeStamp);
//        order.setPaidType(String.valueOf(cmd.getPaymentType()));
//        paymentCardProvider.updatePaymentCardRechargeOrder(order);
    }

    @Override
    public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {
        this.checkOrderNoIsNull(cmd.getBizOrderNum());
        LOGGER.info("PaymentCardOrderEmbeddedV2Handler refundSuccess start cmd = {}", cmd);
        PaymentCardRechargeOrder order = checkOrder(cmd.getBizOrderNum());
        this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()+cmd.getOrderId()).tryEnter(()-> {
            order.setPayStatus(CardRechargeStatus.REFUNDED.getCode());
            paymentCardProvider.updatePaymentCardRechargeOrder(order);
        });
        LOGGER.info("PaymentCardOrderEmbeddedV2Handler refundSuccess end");
    }

    @Override
    public void refundFail(SrvOrderPaymentNotificationCommand cmd) {

    }

    private PaymentCardRechargeOrder checkOrder(String bizOrderNum) {
        PaymentCardRechargeOrder order = paymentCardProvider.findPaymentCardRechargeOrderByBizOrderNum(bizOrderNum);

        if(order == null){
            LOGGER.error("the order {} not found.",bizOrderNum);
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

    private void checkOrderNoIsNull(String bizOrderNum) {
        if(StringUtils.isBlank(bizOrderNum)){
            LOGGER.error("orderNo is null or empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "orderNo is null or empty.");
        }
    }


}
