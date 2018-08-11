package com.everhomes.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.asset.CreatePaymentBillOrder;
import com.everhomes.rest.order.PreOrderDTO;

/**
 * @author created by ycx
 * @date 下午2:57:09
 */
public class DefaultAssetVendorHandler extends AssetVendorHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAssetVendorHandler.class);
    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private CommunityProvider communityProvider;
    
    public PreOrderDTO createOrder(CreatePaymentBillOrder cmd) {
	    XxxAssetBillOrder order = prepareAssetOrder(cmd);
	    // eh_payment_bill_orders
	    return createPreOrder(order);
	}
	
	protected PreOrderDTO createPreOrder(XxxAssetBillOrder order) {
	    PreOrderDTO preOrderDTO = null;
	    //1、检查买方（付款方）是否有会员，无则创建
	    User buyer = userProvider.findUserById(UserContext.currentUserId());
	
	    //2、收款方是否有会员，无则报错
	    Long payeeUserId = cmd.getBizPayeeId();
	    if(payeeUserId == null) {
	        LOGGER.error("Payee user id not found(id in payment system), cmd={}", cmd);
	        throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
	                "Payee user id not found");
	    }
	    
	    CreatePurchaseOrderCommand createOrderCommand = preparePurchaseOrderByBuyer(cmd, buyer);
	    CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
	    if(!createOrderResp.getErrorCode().equals(200)) {
	        LOGGER.error("create order fail");
	        throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
	                "create order fail");
	    }
	    OrderCommandResponse response = createOrderResp.getResponse();
	
	    //5、保存订单信息
	    //saveOrderRecord(orderCommandResponse, cmd.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());
	
	    //6、返回
	    return preOrderDTO;
	}
	
	protected XxxAssetBillOrder prepareAssetOrder(PlaceAnAssetOrderCommand cmd) {
	    return null;
	}
	
	protected afterAssetOrderCreate() {
	    
	}

}