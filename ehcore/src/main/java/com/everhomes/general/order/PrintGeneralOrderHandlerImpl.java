package com.everhomes.general.order;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.general.order.GeneralOrderBizHandler;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.print.SiyinPrintOrderProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.general.order.GorderPayType;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.order.OrderType.OrderTypeEnum;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.promotion.order.CreateGeneralBillInfo;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component(GeneralOrderBizHandler.GENERAL_ORDER_HANDLER + "printOrder") //与OrderTypeEnum的pyCode保持一致
public class PrintGeneralOrderHandlerImpl extends DefaultGeneralOrderHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintGeneralOrderHandlerImpl.class);
	
	@Autowired
	SiyinPrintOrderProvider siyinPrintOrderProvider;
	

	@Override
	void dealInvoiceCallBack(OrderCallBackCommand cmd) {
		SiyinPrintOrder order = siyinPrintOrderProvider
				.findSiyinPrintOrderByGeneralOrderId(cmd.getCallBackInfo().getBusinessOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_ORDER_NOT_EXIST, "order not exist");
		}
		
		order.setIsInvoiced(TrueOrFalseFlag.TRUE.getCode());
		siyinPrintOrderProvider.updateSiyinPrintOrder(order);
	}

	@Override
	void dealEnterprisePayCallBack(OrderCallBackCommand cmd) {
		
		SiyinPrintOrder order = siyinPrintOrderProvider
				.findSiyinPrintOrderByGeneralOrderId(cmd.getCallBackInfo().getBusinessOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_ORDER_NOT_EXIST, "order not exist");
		}
		
		//TODO 更新支付状态
		LOGGER.info("dealEnterprisePayCallBack:"+StringHelper.toJsonString(cmd));
		order.setPayMode(GorderPayType.ENTERPRISE_PAID.getCode());
		siyinPrintOrderProvider.updateSiyinPrintOrder(order);
	}

	@Override
	OrderTypeEnum getOrderTypeEnum() {
		return OrderTypeEnum.PRINT_ORDER;
	}

	@Override
	void fillEnterprisePaySpecificInfo(CreateGeneralBillInfo createBillInfo, CreateOrderBaseInfo baseInfo) {
		createBillInfo.setSourceType(AssetSourceType.PRINT_MODULE);
		createBillInfo.setSourceId(null); //单应用设置为空
	}
}
