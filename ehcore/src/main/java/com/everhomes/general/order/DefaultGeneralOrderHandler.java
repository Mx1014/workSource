package com.everhomes.general.order;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;
import com.everhomes.rest.promotion.order.OrderErrorCode;
import com.everhomes.rest.promotion.order.controller.CreateMerchantOrderRestResponse;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

public abstract class DefaultGeneralOrderHandler implements GeneralOrderBizHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGeneralOrderHandler.class);
	
    @Autowired
    protected GeneralOrderService orderService;
    
	abstract CreateMerchantOrderCommand buildOrderCommand(Object cmd) ;
    abstract String getHandlerName();
	
	@Override
	public CreateMerchantOrderResponse createOrder(Object cmd) {

		CreateMerchantOrderCommand createOrderCmd = buildOrderCommand(cmd);
		LOGGER.info("createOrderCmd:"+StringHelper.toJsonString(createOrderCmd));
		CreateMerchantOrderRestResponse createOrderResp = orderService.createMerchantOrder(createOrderCmd);
		LOGGER.info("createOrderResp:"+StringHelper.toJsonString(createOrderResp));
		if (!checkOrderRestResponseIsSuccess(createOrderResp)) {
			String scope = OrderErrorCode.SCOPE;
			int code = OrderErrorCode.ERROR_CREATE_ORDER_FAILED;
			String description = "Failed to create order";
			if (createOrderResp != null) {
				code = (createOrderResp.getErrorCode() == null) ? code : createOrderResp.getErrorCode();
				scope = (createOrderResp.getErrorScope() == null) ? scope : createOrderResp.getErrorScope();
				description = (createOrderResp.getErrorDescription() == null) ? description
						: createOrderResp.getErrorDescription();
			}
			throw RuntimeErrorException.errorWith(scope, code, description);
		}

		CreateMerchantOrderResponse orderCommandResponse = createOrderResp.getResponse();
		return orderCommandResponse;
	}

	/*
     * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
       CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
     */
    private boolean checkOrderRestResponseIsSuccess(CreateMerchantOrderRestResponse response){
        if(response != null && response.getErrorCode() != null
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }

}
