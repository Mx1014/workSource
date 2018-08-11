package com.everhomes.asset;

import java.util.ArrayList;
import java.util.List;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.asset.BillIdAndAmount;
import com.everhomes.rest.asset.CreatePaymentBillOrder;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.util.RuntimeErrorException;

/**
 * @author created by ycx
 * @date 下午2:57:09
 */
public class DefaultAssetVendorHandler extends AssetVendorHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAssetVendorHandler.class);
    
    @Autowired
    private AssetProvider assetProvider;
    
    public PreOrderDTO createOrder(CreatePaymentBillOrder cmd) {
    	//保存订单数据 eh_payment_bill_orders
    	
    	//组装command ， 请求支付模块的下预付单
    	PreOrderCommand order = preparePaymentBillOrder(cmd);
	    return createPreOrder(order);
	}
	
	protected PreOrderDTO createPreOrder(PreOrderCommand order) {
	    PreOrderDTO preOrderDTO = null;
	    /*//1、检查买方（付款方）是否有会员，无则创建
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
*/	
	    //6、返回
	    return preOrderDTO;
	}
	
	protected PreOrderCommand preparePaymentBillOrder(CreatePaymentBillOrder cmd) {
		AssetVendor vendor = checkAssetVendor(cmd.getNamespaceId(),0);
        AssetVendorHandler handler = getAssetVendorHandler(vendor.getVendorName());
		//组装command ， 请求支付模块的下预付单
        return handler.preparePaymentBillOrder(cmd);
	}
	
	protected void afterAssetOrderCreate() {
		
	}
	
	private AssetVendor checkAssetVendor(Integer namespaceId,Integer defaultNamespaceId){
        if(null == namespaceId) {
            LOGGER.error("checkAssetVendor namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "checkAssetVendor namespaceId cannot be null.");
        }
        AssetVendor assetVendor = assetProvider.findAssetVendorByNamespace(namespaceId);
        if(null == assetVendor && defaultNamespaceId!=null)  assetVendor = assetProvider.findAssetVendorByNamespace(defaultNamespaceId);
        if(null == assetVendor) {
            LOGGER.error("assetVendor not found, assetVendor namespaceId={}, targetId={}", namespaceId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "assetVendor not found");
        }
        return assetVendor;
    }
	
	private AssetVendorHandler getAssetVendorHandler(String vendorName) {
        AssetVendorHandler handler = null;

        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = AssetVendorHandler.ASSET_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }

        return handler;
    }

}