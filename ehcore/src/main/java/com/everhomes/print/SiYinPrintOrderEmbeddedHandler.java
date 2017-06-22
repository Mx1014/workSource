package com.everhomes.print;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.configuration.ConfigurationProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.print.PrintOrderLockType;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.util.RuntimeErrorException;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PRINT_ORDER_CODE )
public class SiYinPrintOrderEmbeddedHandler implements OrderEmbeddedHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SiYinPrintOrderEmbeddedHandler.class);

    @Autowired
    private SiyinPrintOrderProvider siyinPrintOrderProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		onlinePayBillSuccess(cmd);
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail cmd = {}", cmd);
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderNo = Long.parseLong(cmd.getOrderNo());
		checkOrder(orderNo);
				
	}
	
	private void checkOrderNoIsNull(String orderNo) {
		if(StringUtils.isBlank(orderNo)){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
	}
	
	private SiyinPrintOrder checkOrder(Long orderNo) {
    	SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderByOrderNo(orderNo);
		
		if(order == null){
			LOGGER.error("the order {} not found.",orderNo);
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
	
	private SiyinPrintOrder onlinePayBillSuccess(PayCallbackCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillSuccess");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());
		
		Long orderId = Long.parseLong(cmd.getOrderNo());
		SiyinPrintOrder order = checkOrder(orderId);
		
		BigDecimal payAmount = new BigDecimal(cmd.getPayAmount());

		//加一个开关，方便在beta环境测试
		boolean flag = configProvider.getBooleanValue("beta.print.order.amount", false);
		if (!flag) {
			if (0 != order.getOrderTotalAmount().compareTo(payAmount)) {
				LOGGER.error("Order amount is not equal to payAmount, cmd={}, order={}", cmd, order);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Order amount is not equal to payAmount.");
			}
		}

		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);
		
		this.checkVendorTypeFormat(cmd.getVendorType());
		
		if(order.getOrderStatus().byteValue() == PrintOrderStatusType.UNPAID.getCode()) {
			order.setOrderStatus(PrintOrderStatusType.PAID.getCode());
			order.setLockFlag(PrintOrderLockType.LOCKED.getCode());
			order.setPaidTime(payTimeStamp);
			order.setPaidType(cmd.getVendorType());
			siyinPrintOrderProvider.updateSiyinPrintOrder(order);
		}
		
		return order;
	}
}
