// @formatter:off
package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.activity.Activity;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.activity.ActivityService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.PARKING_CODE )
public class ParkingOrderCallBackHandler implements PaymentCallBackHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingOrderCallBackHandler.class);

    @Autowired
    private ActivityProivider activityProvider;
    
    @Autowired
    private ActivityService activityService;

	@Autowired
	private PayService payService;

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

	@Override
	public void paySuccess(OrderPaymentNotificationCommand cmd) {

//		ActivityRoster roster = activityProvider.findRosterByOrderNo(cmd.getOrderId());
//		if(roster == null){
//			LOGGER.info("can not find roster by orderno = {}", cmd.getOrderId());
//			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
//					"no roster.");
//		}
//		Activity activity = activityProvider.findActivityById(roster.getActivityId());
//		if(activity == null){
//			LOGGER.info("can not find activity by id = {}", roster.getActivityId());
//			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
//					"no activity.");
//		}
//		//检验支付结果和应价格是否相等
//		checkPayAmount(cmd.getAmount(), activity.getChargePrice());
//		//支付宝回调时，可能会同时回调多次，
//		roster.setPayFlag(ActivityRosterPayFlag.PAY.getCode());
//		Long paytime = DateHelper.parseDataString(cmd.getPayDatetime(), "YYYY-MM-DD HH:mm:ss").getTime();
//		roster.setPayTime(new Timestamp(paytime));
//		BigDecimal amount = payService.changePayAmount(cmd.getAmount());
//		roster.setPayAmount(amount);
//		roster.setVendorType(String.valueOf(cmd.getPaymentType()));
//		roster.setOrderType(String.valueOf(cmd.getPaymentType()));
//		roster.setPayVersion(ActivityRosterPayVersionFlag.V2.getCode());
//		activityProvider.updateRoster(roster);


		LOGGER.info("Parking pay info, cmd={}", cmd);

//		this.checkOrderNoIsNull(cmd.getOrderNo());
//		this.checkVendorTypeIsNull(cmd.getVendorType());
//		this.checkPayAmountIsNull(cmd.getPayAmount());
//		this.checkVendorTypeFormat(cmd.getVendorType());

		Long orderId = cmd.getOrderId();
		BigDecimal payAmount = payService.changePayAmount(cmd.getAmount());

		//支付宝回调时，可能会同时回调多次，
		this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_UPDATE_ORDER_STATUS.getCode() + orderId).enter(()-> {

			ParkingRechargeOrder order = checkOrder(orderId);
			//加一个开关，方便在beta环境测试
			boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
			if (!flag) {
				if (0 != order.getPrice().compareTo(payAmount)) {
					LOGGER.error("Order amount is not equal to payAmount, cmd={}, order={}", cmd, order);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
							"Order amount is not equal to payAmount.");
				}
			}

			Long payTime = System.currentTimeMillis();
			Timestamp payTimeStamp = new Timestamp(payTime);

			String vendorName = parkingProvider.findParkingLotById(order.getParkingLotId()).getVendorName();
			ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

			//先将状态置为已付款
			if(order.getStatus() == ParkingRechargeOrderStatus.UNPAID.getCode()) {
				order.setStatus(ParkingRechargeOrderStatus.PAID.getCode());
				order.setPaidTime(payTimeStamp);
				PaymentType paymentType = PaymentType.fromCode(cmd.getPaymentType());
				if (null != paymentType) {
					if (paymentType.name().toUpperCase().startsWith("WECHAT")) {
						order.setPaidType(VendorType.WEI_XIN.getCode());
					}else {
						order.setPaidType(VendorType.ZHI_FU_BAO.getCode());
					}
				}
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

					ExecutorUtil.submit(new Runnable() {
						@Override
						public void run() {

							LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
							localBusSubscriber.onLocalBusMessage(null, "Parking-Recharge" + order.getId(), JSONObject.toJSONString(dto), null);

							localBus.publish(this, "Parking-Recharge" + order.getId(), JSONObject.toJSONString(dto));
						}
					});
				}

			}
			return null;
		});
	}

	@Override
	public void payFail(OrderPaymentNotificationCommand cmd) {
		//TODO: 失败
		LOGGER.error("Parking pay failed, cmd={}", cmd);
	}

	@Override
	public void refundSuccess(OrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
		Long orderId = cmd.getOrderId();

		ParkingRechargeOrder order = checkOrder(orderId);

		order.setStatus(ParkingRechargeOrderStatus.REFUNDED.getCode());
		order.setRefundTime(new Timestamp(System.currentTimeMillis()));
		parkingProvider.updateParkingRechargeOrder(order);
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
}
