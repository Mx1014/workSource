package com.everhomes.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.RefundCallbackCommand;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;

@Service
public class OrderServiceImpl implements OrderService{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private AppProvider appProvider;
	
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

	private RefundEmbeddedHandler getRefundHandler(String orderType) {
		return PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+this.getOrderTypeCode(orderType));
	}
	@Override
	public void refundCallback(RefundCallbackCommand cmd) { 
		checkSignature(cmd);
		if(StringUtils.isEmpty(cmd.getOrderNo())||StringUtils.isEmpty(cmd.getRefundOrderNo())){
			LOGGER.error("Invalid parameter,orderNo or refundOrderNo is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderNo or refundOrderNo is null");
		}
		if(StringUtils.isEmpty(cmd.getOrderType())||StringUtils.isEmpty(cmd.getOnlinePayStyleNo())){
			LOGGER.error("Invalid parameter,orderType or onlinePayStyleNo is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderType or onlinePayStyleNo is null");
		}
		RefundEmbeddedHandler handler = this.getRefundHandler(cmd.getOrderType());
		LOGGER.debug("OrderEmbeddedHandler="+handler.getClass().getName());
	 
		handler.paySuccess(cmd);
		 
	}
	private void checkSignature(RefundCallbackCommand cmd){
		App app = appProvider.findAppByKey(cmd.getAppKey());
		Map<String,String> map = new HashMap<String, String>();
		if(cmd.getAppKey() != null)
			map.put("appKey",cmd.getAppKey());
		if(cmd.getTimestamp() != null)
			map.put("timestamp",cmd.getTimestamp()+"");
		if(cmd.getNonce() != null)
			map.put("nonce",cmd.getNonce()+"");
		if(cmd.getRefundOrderNo() != null)
			map.put("refundOrderNo",cmd.getRefundOrderNo());
		if(cmd.getOrderNo() != null)
			map.put("orderNo", cmd.getOrderNo());
		if(cmd.getOnlinePayStyleNo() != null)
			map.put("onlinePayStyleNo",cmd.getOnlinePayStyleNo() );
		if(cmd.getOrderType() != null)
			map.put("orderType",cmd.getOrderType() );
		if(cmd.getRefundAmount() != null)
			map.put("refundAmount", cmd.getRefundAmount().doubleValue()+"");
		if(cmd.getCrypto() != null)
			map.put("crypto", cmd.getCrypto()); 
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		if(!signature.equals(cmd.getSignature())){
			LOGGER.error("Invalid parameter,signature error");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,signature error");
		} 
	}
}
