package com.everhomes.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.payment.taotaogu.ResponseEntiy;
import com.everhomes.payment.taotaogu.TAOTAOGUHttpUtil;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PAYMENT_CARD_CODE )
public class PaymentCardOrderEmbeddedHandler implements OrderEmbeddedHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardOrderEmbeddedHandler.class);

    @Autowired
    private PaymentCardProvider paymentCardProvider;
    
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = Long.parseLong(cmd.getOrderNo());
		PaymentCardRechargeOrder order = checkOrder(orderId);
		PaymentCard paymentCard = paymentCardProvider.findPaymentCardById(order.getCardId());
		PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		
		
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setPayStatus(CardOrderStatus.PAID.getCode());
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		paymentCardProvider.updatePaymentCardRechargeOrder(order);
		
		handler.rechargeCard(order, paymentCard);
    	
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {

		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = Long.parseLong(cmd.getOrderNo());
		PaymentCardRechargeOrder order = checkOrder(orderId);
				
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setPayStatus(CardOrderStatus.INACTIVE.getCode());
		order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
		order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		paymentCardProvider.updatePaymentCardRechargeOrder(order);
	}
	
	private void checkOrderNoIsNull(String orderNo) {
		if(StringUtils.isBlank(orderNo)){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
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
}
