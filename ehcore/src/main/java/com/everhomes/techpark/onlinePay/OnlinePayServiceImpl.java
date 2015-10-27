package com.everhomes.techpark.onlinePay;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.organization.VendorType;
import com.everhomes.techpark.park.RechargeInfo;
import com.everhomes.util.RuntimeErrorException;

@Component
public class OnlinePayServiceImpl implements OnlinePayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OnlinePayServiceImpl.class);
	
	@Autowired
	private OnlinePayProvider onlinePayProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Override
	public void onlinePayBill(OnlinePayBillCommand cmd) {
		
		//fail
		if(cmd.getPayStatus().toLowerCase().equals("fail"))
			this.onlinePayPmBillFail(cmd);
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
			this.onlinePayPmBillSuccess(cmd);

	}
	
	private void onlinePayPmBillFail(OnlinePayBillCommand cmd) {
		
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayPmBillFail");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		
		RechargeInfo order = this.checkOrder(orderId);
				
		Date cunnentTime = new Date();
		Timestamp currentTimestamp = new Timestamp(cunnentTime.getTime());
		this.updateOrderStatus(order, currentTimestamp, PayStatus.INACTIVE.getCode(), RechargeStatus.INACTIVE.getCode());
	}
	
	private void onlinePayPmBillSuccess(OnlinePayBillCommand cmd) {
		
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayPmBillSuccess");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());
		
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		RechargeInfo order = this.checkOrder(orderId);
		
		Integer payAmount = new Integer(cmd.getPayAmount());
		
		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);
		
		this.checkVendorTypeFormat(cmd.getVendorType());
		
		if(order.getPaymentStatus().byteValue() == PayStatus.WAITING_FOR_PAY.getCode()) {
			order.setRechargeAmount(payAmount);
			this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode(), RechargeStatus.UPDATING.getCode());
			
		}
		
		if(order.getNumberType().byteValue() == 0) {
			//停车系统接口：根据车牌查到有效期结束时间，加上充值月份，为新的到期时间
			
			while(true) {
				//调用停车系统接口：返回充值结果
				
				if(true) {
					this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode(), RechargeStatus.SUCCESS.getCode());
					break;
				}
			}
			
		}
		
	}
	
	private void checkPayAmountIsNull(String payAmount) {
		
		if(payAmount == null || payAmount.trim().equals("")){
			LOGGER.error("payAmount is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAmount or is null or empty.");
		}

	}

	private void checkVendorTypeIsNull(String vendorType) {
		
		if(vendorType == null || vendorType.trim().equals("")){
			LOGGER.error("vendorType is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendorType is null or empty.");
		}

	}

	private void checkOrderNoIsNull(String orderNo) {
		
		if(orderNo == null || orderNo.trim().equals("")){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}

	}
	
	private void checkVendorTypeFormat(String vendorType) {
		if(VendorType.fromCode(vendorType) == null){
			LOGGER.error("vendor type is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendor type is wrong.");
		}
	}
	
	private RechargeInfo checkOrder(Long orderId) {
		
		RechargeInfo order = this.onlinePayProvider.findRechargeInfoByOrderId(orderId);
		
		if(order == null){
			LOGGER.error("the order not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}
	
	private void updateOrderStatus(RechargeInfo order, Timestamp payTimeStamp, byte paymentStatus, byte rechargeStatus) {
		
		order.setRechargeTime(payTimeStamp);
		order.setPaymentStatus(paymentStatus);
		order.setRechargeStatus(rechargeStatus);
		this.onlinePayProvider.updateRehargeInfo(order);
	}
	
	private Long convertOrderNoToOrderId(String orderNo) {
		
		return Long.valueOf(orderNo);
	}

}
