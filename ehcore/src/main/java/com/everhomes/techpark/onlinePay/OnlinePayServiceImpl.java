package com.everhomes.techpark.onlinePay;

import java.sql.Timestamp;
import java.util.Date;

import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.onlinePay.PayStatus;
import com.everhomes.rest.techpark.onlinePay.RechargeStatus;
import com.everhomes.rest.techpark.park.RechargeInfoDTO;
import com.everhomes.techpark.park.RechargeInfo;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class OnlinePayServiceImpl implements OnlinePayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OnlinePayServiceImpl.class);
	
	@Autowired
	private OnlinePayProvider onlinePayProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Override
	public RechargeInfoDTO onlinePayBill(OnlinePayBillCommand cmd) {
		
		RechargeInfo order = new RechargeInfo();
		//fail
		if(cmd.getPayStatus().toLowerCase().equals("fail"))
			order = this.onlinePayBillFail(cmd);
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
			order = this.onlinePayBillSuccess(cmd);

		RechargeInfoDTO info = ConvertHelper.convert(order, RechargeInfoDTO.class);
		return info;
	}
	
	private RechargeInfo onlinePayBillFail(OnlinePayBillCommand cmd) {
		
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		
		RechargeInfo order = this.checkOrder(orderId);
				
		Date cunnentTime = new Date();
		Timestamp currentTimestamp = new Timestamp(cunnentTime.getTime());
		this.updateOrderStatus(order, currentTimestamp, PayStatus.INACTIVE.getCode(), RechargeStatus.INACTIVE.getCode());
		
		return order;
	}
	
	private RechargeInfo onlinePayBillSuccess(OnlinePayBillCommand cmd) {
		
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillSuccess");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());
		
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		RechargeInfo order = this.checkOrder(orderId);
		
		double payAmount = new Double(cmd.getPayAmount());
		
		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);
		
		this.checkVendorTypeFormat(cmd.getVendorType());
		
		if(order.getPaymentStatus().byteValue() == PayStatus.WAITING_FOR_PAY.getCode()) {
			order.setRechargeAmount(payAmount);
			this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode(), RechargeStatus.UPDATING.getCode());
			
		}
		
		return order;
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

	@Override
	public Long createBillId(Long time) {
		
		String bill = String.valueOf(time) + String.valueOf(generateRandomNumber(3));
		return Long.valueOf(bill);
	}

	public static void main(String[] args) {
		OnlinePayServiceImpl o = new OnlinePayServiceImpl();

		System.out.println(o.createBillId(DateHelper.currentGMTTime().getTime()));
	}

	/**
	 *
	 * @param n 创建n位随机数
	 * @return
	 */
	private long generateRandomNumber(int n){
		return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
	}

}
