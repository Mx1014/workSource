package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.util.RuntimeErrorException;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PARKING_CODE )
public class ParkingOrderEmbeddedHandler implements OrderEmbeddedHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingOrderEmbeddedHandler.class);

    @Autowired
    private ParkingProvider parkingProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		
    	ParkingRechargeOrder order = onlinePayBillSuccess(cmd);
    	String venderName = parkingProvider.findParkingLotById(order.getParkingLotId()).getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(venderName);

		//支付宝回调时，可能会同时回调多次，
		this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_UPDATE_ORDER_STATUS.getCode()).enter(()-> {

			handler.notifyParkingRechargeOrderPayment(order,cmd.getPayStatus());
			return null;
		});
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = Long.parseLong(cmd.getOrderNo());
		
		ParkingRechargeOrder order = checkOrder(orderId);
				
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setStatus(ParkingRechargeOrderStatus.INACTIVE.getCode());
		order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.NONE.getCode());
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		//order.setPaidTime(cmd.getPayTime());
		parkingProvider.updateParkingRechargeOrder(order);
	}
	
	private void checkOrderNoIsNull(String orderNo) {
		if(StringUtils.isBlank(orderNo)){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
	}
	
	private ParkingRechargeOrder checkOrder(Long orderId) {
    	ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(orderId);
		
		if(order == null){
			LOGGER.error("the order {} not found.",orderId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}
	
	private void checkPayAmountIsNull(String payAmount) {
		if(StringUtils.isBlank(payAmount)){
			LOGGER.error("payAmount is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAmount or is null or empty.");
		}
	}

	private void checkVendorTypeIsNull(String vendorType) {
		if(StringUtils.isBlank(vendorType)){
			LOGGER.error("vendorType is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendorType is null or empty.");
		}
	}
	
	private void checkVendorTypeFormat(String vendorType) {
		if(VendorType.fromCode(vendorType) == null){
			LOGGER.error("vendor type is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendor type is wrong.");
		}
	}
	
	private ParkingVendorHandler getParkingVendorHandler(String vendorName) {
		ParkingVendorHandler handler = null;
	        
		if(vendorName != null && vendorName.length() > 0) {
			String handlerPrefix = ParkingVendorHandler.PARKING_VENDOR_PREFIX;
			handler = PlatformContext.getComponent(handlerPrefix + vendorName);
		}
		return handler;
	}
	
	private ParkingRechargeOrder onlinePayBillSuccess(PayCallbackCommand cmd) {
		
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillSuccess");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());
		
		Long orderId = Long.parseLong(cmd.getOrderNo());
		ParkingRechargeOrder order = checkOrder(orderId);
		
		BigDecimal payAmount = new BigDecimal(cmd.getPayAmount());
		
		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);
		
		this.checkVendorTypeFormat(cmd.getVendorType());
		
		if(order.getStatus().byteValue() == ParkingRechargeOrderStatus.UNPAID.getCode()) {
//			order.setPrice(payAmount);
			order.setStatus(ParkingRechargeOrderStatus.PAID.getCode());
			order.setPaidTime(payTimeStamp);
			order.setPaidType(cmd.getVendorType());
			//order.setPaidTime(cmd.getPayTime());
			parkingProvider.updateParkingRechargeOrder(order);
		}
		
		return order;
	}
}
