package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
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
import com.everhomes.util.RuntimeErrorException;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PARKING_CODE )
public class ParkingOrderEmbeddedHandler implements OrderEmbeddedHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingOrderEmbeddedHandler.class);

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
	public void paySuccess(PayCallbackCommand cmd) {

		LOGGER.info("Parking pay info, cmd={}", cmd);

		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());
		this.checkVendorTypeFormat(cmd.getVendorType());

		Long orderId = Long.parseLong(cmd.getOrderNo());
		BigDecimal payAmount = new BigDecimal(cmd.getPayAmount());

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
				order.setPaidType(cmd.getVendorType());
				parkingProvider.updateParkingRechargeOrder(order);
			}
			if(order.getStatus() == ParkingRechargeOrderStatus.PAID.getCode()) {
				try{
					if (handler.notifyParkingRechargeOrderPayment(order)) {
						order.setStatus(ParkingRechargeOrderStatus.RECHARGED.getCode());
						order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
						parkingProvider.updateParkingRechargeOrder(order);

						LOGGER.info("Notify parking recharge failed, cmd={}, order={}", cmd, order);
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
	public void payFail(PayCallbackCommand cmd) {
		//TODO: 失败
		LOGGER.error("Parking pay failed, cmd={}", cmd);
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
