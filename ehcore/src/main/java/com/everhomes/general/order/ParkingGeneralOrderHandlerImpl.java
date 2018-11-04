package com.everhomes.general.order;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.general.order.GeneralOrderBizHandler;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.general.order.GorderPayType;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.order.OrderType.OrderTypeEnum;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.promotion.order.CreateGeneralBillInfo;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component(GeneralOrderBizHandler.GENERAL_ORDER_HANDLER + "parking") //与OrderTypeEnum的pyCode保持一致
public class ParkingGeneralOrderHandlerImpl extends DefaultGeneralOrderHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingGeneralOrderHandlerImpl.class);
	
	
	@Autowired
	private ParkingProvider parkingProvider;

	@Override
	void dealInvoiceCallBack(OrderCallBackCommand cmd) {
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderByBizOrderNum(cmd.getCallBackInfo().getBusinessOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_ORDER_NOT_EXIST, "order not exist");
		}
		
		order.setInvoiceStatus((byte)2);
		
		parkingProvider.updateParkingRechargeOrder(order);
	}

	@Override
	void dealEnterprisePayCallBack(OrderCallBackCommand cmd) {
		
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderByGeneralOrderId(Long.valueOf(cmd.getCallBackInfo().getBusinessOrderId()));
		if (null == order) {
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GENERATE_ORDER_NO, "order not exist");
		}
		
		//TODO 更新支付状态
		LOGGER.info("dealEnterprisePayCallBack:"+StringHelper.toJsonString(cmd));
		order.setPayMode(GorderPayType.ENTERPRISE_PAID.getCode());
		parkingProvider.updateParkingRechargeOrder(order);
	}

	@Override
	OrderTypeEnum getOrderTypeEnum() {
		return OrderTypeEnum.PARKING;
	}

	@Override
	void fillEnterprisePaySpecificInfo(CreateGeneralBillInfo createBillInfo, CreateOrderBaseInfo baseInfo) {
		createBillInfo.setSourceType(AssetSourceType.PARKING_MODULE);
		createBillInfo.setSourceId(null); //单应用设置为空
	}
}
