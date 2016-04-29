package com.everhomes.organization.pmsy;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.parking.ParkingOrderEmbeddedHandler;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PM_SIYUAN_CODE )
public class PmsyOrderEmbeddedHandler implements OrderEmbeddedHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyOrderEmbeddedHandler.class);
	
	@Autowired
	private PmsyProvider pmsyProvider;
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		
//		String json = PmsyHttpUtil.post("UserRev_PayFee", cmd.getCustomerId(), TimeToString(cmd.getBillDateStr()),
//				TimeToString(cmd.getBillDateStr()), cmd.getProjectId(), "01", "", "");
//		Gson gson = new Gson();
//		Map map = gson.fromJson(json, Map.class);
//		List list = (List) map.get("UserRev_PayFee");
//		Map map2 = (Map) list.get(0);
//		List list2 = (List) map2.get("Syswin");
		
		
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = Long.parseLong(cmd.getOrderNo());
		PmsyOrder order = checkOrder(orderId);
				
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setStatus(PmsyOrderStatus.INACTIVE.getCode());
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		//order.setPaidTime(cmd.getPayTime());
		pmsyProvider.updatePmsyOrder(order);
		
	}

	private PmsyOrder checkOrder(Long orderId) {
		PmsyOrder order = pmsyProvider.findPmsyOrderById(orderId);
		if(order == null){
			LOGGER.error("the order {} not found.",orderId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}
	
	private void checkOrderNoIsNull(String orderNo) {
		if(StringUtils.isBlank(orderNo)){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
	}
}
