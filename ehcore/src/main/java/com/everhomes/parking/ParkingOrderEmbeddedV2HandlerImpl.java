package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.rest.general.order.GorderPayType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;

/**
 * 支付3.0回调版本
 */
@Component
public class ParkingOrderEmbeddedV2HandlerImpl implements ParkingOrderEmbeddedV2Handler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingOrderEmbeddedV2HandlerImpl.class);

    @Autowired
    private ParkingProvider parkingProvider;
	@Autowired
	private CoordinationProvider coordinationProvider;
	@Autowired
	private ConfigurationProvider configProvider;
	@Autowired
	private LocalBus localBus;
	@Autowired
	private LocaleStringService localeService;

	@Autowired
	private BusBridgeProvider busBridgeProvider;
	@Autowired
	private ParkingService parkingService;
	private String transferOrderNo(String bizOrderNum) {
		String[] split = bizOrderNum.split(ParkingServiceImpl.BIZ_ORDER_NUM_SPILT);
		if(split.length==3){
			return split[2];
		}
		return bizOrderNum;
	}

	private void paySuccess(MerchantPaymentNotificationCommand cmd) {
		this.checkOrderNoIsNull(cmd.getBizOrderNum());//检查停车业务订单号
		this.checkPayAmountIsNull(cmd.getAmount());//



		BigDecimal payAmount = new BigDecimal(cmd.getAmount()).divide(new BigDecimal(100));
		BigDecimal couponAmount = new BigDecimal(cmd.getCouponAmount() == null ? 0L : cmd.getCouponAmount()).divide(new BigDecimal(100));

		//支付宝回调时，可能会同时回调多次，
		this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_UPDATE_ORDER_STATUS.getCode() + cmd.getBizOrderNum()).enter(()-> {
			ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderByGeneralOrderId(cmd.getMerchantOrderId());
			if (order == null) { //做一下兼容
				Long orderId = Long.parseLong(transferOrderNo(cmd.getBizOrderNum()));//获取下单时候的支付id
				order = checkOrder(orderId);
			}
			//加一个开关，方便在beta环境测试
			boolean flag = configProvider.getBooleanValue("parking.order.amount", false);

			if (!flag) {
				if (0 != order.getPrice().compareTo(payAmount.add(couponAmount))) {
					LOGGER.error("Order amount is not equal to payAmount, cmd={}, order={}", cmd, order);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
							"Order amount is not equal to payAmount.");
				}
			}
			Long payTime = System.currentTimeMillis();
			Timestamp payTimeStamp = new Timestamp(payTime);

			ParkingLot lot = parkingProvider.findParkingLotById(order.getParkingLotId());
			String vendorName = lot.getVendorName();
			ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
			//先将状态置为已付款
			if(order.getStatus() == ParkingRechargeOrderStatus.UNPAID.getCode()) {
				order.setStatus(ParkingRechargeOrderStatus.PAID.getCode());
				order.setPaidTime(payTimeStamp);
				order.setPayOrderNo(cmd.getOrderId()+"");//保存支付系统的订单号
				order.setPaidType(transferPaidType(cmd.getPaymentType()));
				order.setOrderNo(parkingService.createOrderNo(lot));
				if (cmd.getPaymentType() == 29)
					order.setPayMode(GorderPayType.ENTERPRISE_PAID.getCode());
				parkingProvider.updateParkingRechargeOrder(order);
			}
			if(order.getStatus() == ParkingRechargeOrderStatus.PAID.getCode()) {
				try{
					if (handler.notifyParkingRechargeOrderPayment(order)) {
						order.setStatus(ParkingRechargeOrderStatus.RECHARGED.getCode());
						order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
						parkingProvider.updateParkingRechargeOrder(order);

						LOGGER.info("Notify parking recharge success, cmd={}, order={}", cmd, order);
					}else {
						//充值失败
						order.setStatus(ParkingRechargeOrderStatus.FAILED.getCode());
						//充值失败时，将返回的错误信息记录下来
						if (StringUtils.isBlank(order.getErrorDescription())) {
							String locale = Locale.SIMPLIFIED_CHINESE.toString();
							String scope = ParkingErrorCode.SCOPE;
							String code = String.valueOf(ParkingErrorCode.ERROR_RECHARGE_ORDER);
							String defaultText = localeService.getLocalizedString(scope, code, locale, "");
							order.setErrorDescription(defaultText);
						}
						parkingProvider.updateParkingRechargeOrder(order);

					}
				}catch (Exception e) {
					LOGGER.error("Notify parking recharge failed, cmd={}, order={}", cmd, order, e);
				}finally {
					ParkingRechargeOrderDTO dto = ConvertHelper.convert(order, ParkingRechargeOrderDTO.class);
					Long orderId = order.getId();
					ExecutorUtil.submit(new Runnable() {
						@Override
						public void run() {

							LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
							localBusSubscriber.onLocalBusMessage(null, "Parking-Recharge" + orderId, JSONObject.toJSONString(dto), null);

							localBus.publish(this, "Parking-Recharge" + cmd.getBizOrderNum(), JSONObject.toJSONString(dto));
						}
					});
				}

			}

			if(order.getStatus() == ParkingRechargeOrderStatus.RECHARGED_NOTCALL.getCode()) {
				order.setStatus(ParkingRechargeOrderStatus.RECHARGED.getCode());
				order.setPaidTime(payTimeStamp);
				order.setPayOrderNo(cmd.getOrderId()+"");//保存支付系统的订单号
				order.setPaidType(transferPaidType(cmd.getPaymentType()));
				if (cmd.getPaymentType() == 29)
					order.setPayMode(GorderPayType.ENTERPRISE_PAID.getCode());
				order.setOrderNo(parkingService.createOrderNo(lot));
				parkingProvider.updateParkingRechargeOrder(order);
			}

			if(order.getStatus() == ParkingRechargeOrderStatus.FAILED_NOTCALL.getCode()) {
				order.setStatus(ParkingRechargeOrderStatus.FAILED.getCode());
				order.setPaidTime(payTimeStamp);
				order.setPayOrderNo(cmd.getOrderId()+"");//保存支付系统的订单号
				order.setPaidType(transferPaidType(cmd.getPaymentType()));
				if (cmd.getPaymentType() == 29)
					order.setPayMode(GorderPayType.ENTERPRISE_PAID.getCode());
				order.setOrderNo(parkingService.createOrderNo(lot));
				parkingProvider.updateParkingRechargeOrder(order);
			}

			return null;
		});

	}

	/**
	 * 将3.0版本支付的支付方式转为老的支付宝和微信的支付方式
	 * @param paymentType
	 * @return
	 */
	private String transferPaidType(Integer paymentType) {
						/* 支付类型
						 * WECHAT_APPPAY(1): 微信APP支付
						 * GATEWAY_PAY(2): 网关支付
						 * POS_PAY(3): 订单POS
						 * REALNAME_PAY(4): 实名付（单笔）
						 * REALNAME_BATCHPAY(5): 实名付（批量）
						 * WITHOLD(6): 通联通代扣
						 * WECHAT_SCAN_PAY(7):  微信扫码支付(正扫)
						 * ALI_SCAN_PAY(8): 支付宝扫码支付(正扫)
						 * WECHAT_JS_PAY(9): 微信JS 支付（公众号）
						 * ALI_JS_PAY(10): 支付宝JS 支付（生活号）
						 * QQWALLET_JS_PAY(11): QQ 钱包JS 支付
						 * WCHAT_CODE_PAY(12): 微信刷卡支付（被扫）
						 * ALI_CODE_PAY(13): 支付宝刷卡支付(被扫)
						 * QQWALLET_CODE_PAY(14): QQ 钱包刷卡支付(被扫)
						 * BALANCE_PAY(15): 账户余额
						 * COUPON_PAY(16): 代金券（不建议使用，建议使用下面的批量代金券方式）
						 * COUPON_BATCHPAY(17): 批量代金券
						 * WITHDRAW(18): 通联通代付
						 * QUICK_PAY(19):
						 * WITHDRAW_AUTO(20):*/
		if(paymentType==1 || paymentType==7 || paymentType==9 || paymentType==12 || paymentType==21)
			return "10002";
		if(paymentType==8 || paymentType==10 || paymentType==13)
			return "10001";
		return paymentType+"";
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
	
	private void checkPayAmountIsNull(Long payAmount) {
		if(payAmount==null){
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

	@Override
	public void payCallBack(MerchantPaymentNotificationCommand cmd) {
		//检查签名
		if(!PayUtil.verifyCallbackSignature(cmd)){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"sign verify faild");
		}

		// * RAW(0)：
		// * SUCCESS(1)：支付成功
		// * PENDING(2)：挂起
		// * ERROR(3)：错误
		if(cmd.getPaymentStatus()== null || 1!=cmd.getPaymentStatus()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"invaild paymentstatus,"+cmd.getPaymentStatus());
		}//检查状态

		//检查orderType
		//RECHARGE(1), WITHDRAW(2), PURCHACE(3), REFUND(4);
		//充值，体现，支付，退款
		if(cmd.getOrderType()==null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"invaild ordertype,"+cmd.getOrderType());
		}
		if(cmd.getOrderType() == 3) {
			paySuccess(cmd);
		}
		else if(cmd.getOrderType() == 4){
			refundSuccess(cmd);
		}
	}

	private void refundSuccess(MerchantPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.

		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderByGeneralOrderId(cmd.getMerchantOrderId());
		if (order == null) { //做一下兼容
			Long orderId = Long.parseLong(transferOrderNo(cmd.getBizOrderNum()));//获取下单时候的支付id
			order = checkOrder(orderId);
		}

		if(order == null){
			LOGGER.error("the order {} not found.",cmd.getBizOrderNum());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		order.setStatus(ParkingRechargeOrderStatus.REFUNDED.getCode());
		order.setRefundTime(new Timestamp(System.currentTimeMillis()));
		parkingProvider.updateParkingRechargeOrder(order);
	}
}
