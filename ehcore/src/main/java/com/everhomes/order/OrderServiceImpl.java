package com.everhomes.order;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.util.RuntimeErrorException;

@Service
public class OrderServiceImpl implements OrderService{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public void payCallback(PayCallbackCommand cmd) {
		if(StringUtils.isEmpty(cmd.getOrderNo())||StringUtils.isEmpty(cmd.getPayStatus())){
			LOGGER.error("Invalid parameter,orderNo or payStatus is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderNo or payStatus is null");
		}
		if(StringUtils.isEmpty(cmd.getOrderType())||StringUtils.isEmpty(cmd.getVendorType())){
			LOGGER.error("Invalid parameter,orderType or vendorType is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderType or vendorType is null");
		}
		OrderEmbeddedHandler handler = this.getOrderHandler(cmd.getOrderType());
		LOGGER.debug("OrderEmbeddedHandler="+handler.getClass().getName());
		if(cmd.getPayStatus().equalsIgnoreCase("success"))
			handler.paySuccess(cmd);
		if(cmd.getPayStatus().equalsIgnoreCase("fail"))
			handler.payFail(cmd);
	}

	private OrderEmbeddedHandler getOrderHandler(String orderType) {
		return PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+this.getOrderTypeCode(orderType));
	}

	private String getOrderTypeCode(String orderType) {
		Integer code = OrderType.OrderTypeEnum.getCodeByPyCode(orderType);
		if(code==null){
			LOGGER.error("Invalid parameter,orderType not found in OrderType.orderType="+orderType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderType not found in OrderType");
		}
		LOGGER.debug("orderTypeCode="+code);
		return String.valueOf(code);
	}

}
