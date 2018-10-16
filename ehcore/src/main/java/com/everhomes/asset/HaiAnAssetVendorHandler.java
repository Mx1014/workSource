package com.everhomes.asset;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pmsy.PmsyProvider;
import com.everhomes.organization.pmsy.PmsyService;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.CreatePaymentBillOrderCommand;
import com.everhomes.rest.gorder.controller.GetPurchaseOrderRestResponse;
import com.everhomes.rest.gorder.order.GetPurchaseOrderCommand;
import com.everhomes.rest.gorder.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.gorder.order.PurchaseOrderDTO;
import com.everhomes.rest.gorder.order.PurchaseOrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午10:13:12
 */
@Component(DefaultAssetVendorHandler.DEFAULT_ASSET_VENDOR_PREFIX + "HAIAN")
public class HaiAnAssetVendorHandler extends DefaultAssetVendorHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(HaiAnAssetVendorHandler.class);
    
    @Autowired
    private AssetProvider assetProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
	private PmsyService pmsyService;
    
    @Autowired
    private PmsyProvider pmsyProvider;
    
    protected void checkPaymentBillOrderPaidStatus(CreatePaymentBillOrderCommand cmd) {
    	
    }
    
    protected BigDecimal calculateBillOrderAmount(CreatePaymentBillOrderCommand cmd) {
    	return new BigDecimal(cmd.getAmount());
    }
    
    protected void afterBillOrderCreated(CreatePaymentBillOrderCommand cmd, PurchaseOrderCommandResponse orderResponse) {
        List<PaymentBillOrder> billOrderList = new ArrayList<PaymentBillOrder>();
        PaymentBillOrder orderBill  = new PaymentBillOrder();
        orderBill.setBillId(cmd.getPmsyOrderId());
        orderBill.setNamespaceId(cmd.getNamespaceId());
        orderBill.setAmount(new BigDecimal(cmd.getAmount()).divide(new BigDecimal(100)));
        orderBill.setOrderNumber(orderResponse.getBusinessOrderNumber());
        orderBill.setPaymentStatus(orderResponse.getPaymentStatus());
        orderBill.setPaymentOrderId(orderResponse.getPayResponse().getOrderId());//支付订单ID
        orderBill.setGeneralOrderId(orderResponse.getOrderId());//统一订单ID
        orderBill.setPaymentType(orderResponse.getPaymentType());
        orderBill.setPaymentChannel(orderResponse.getPaymentChannel());
        orderBill.setPaymentOrderType(orderResponse.getPaymentOrderType());
        orderBill.setUid(UserContext.currentUserId());
        orderBill.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        billOrderList.add(orderBill);
        assetProvider.createBillOrderMaps(billOrderList);
    }
    
    /**
	 * 构造扩展信息，该信息在支付结束时会透传回来给业务系统
	 * @param cmd 下单请求信息
	 * @param billGroup 帐单组
	 * @return 扩展信息
	 */
	protected String genPaymentExtendInfo(CreatePaymentBillOrderCommand cmd, PaymentBillGroup billGroup) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("来源:");
        strBuilder.append(cmd.getExtendInfo());
        return strBuilder.toString();
	}
    
    public void payNotify(OrderPaymentNotificationCommand cmd) {
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("payNotify-command=" + GsonUtil.toJson(cmd));
    	}
    	if(cmd == null) {
    		LOGGER.error("payNotify fail, cmd={}", cmd);
    	}
    	//检查订单是否存在
    	List<PaymentBillOrder> paymentBillOrderList = assetProvider.findPaymentBillOrderRecordByOrderNum(cmd.getBizOrderNum());
        if(paymentBillOrderList == null || paymentBillOrderList.size() == 0){
            LOGGER.error("can not find order record by BizOrderNum={}", cmd.getBizOrderNum());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find order record");
        }
        
        GetPurchaseOrderCommand getPurchaseOrderCommand = new GetPurchaseOrderCommand();
        String systemId = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "gorder.system_id", "");
        getPurchaseOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        String accountCode = generateAccountCode(UserContext.getCurrentNamespaceId());
        getPurchaseOrderCommand.setAccountCode(accountCode);
        getPurchaseOrderCommand.setBusinessOrderNumber(cmd.getBizOrderNum());
        
        if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("payNotify-GetPurchaseOrderCommand=" + GsonUtil.toJson(getPurchaseOrderCommand));
    	}
        GetPurchaseOrderRestResponse response = orderService.getPurchaseOrder(getPurchaseOrderCommand);
        if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("payNotify-getPurchaseOrder response=" + GsonUtil.toJson(response));
    	}
        if(response == null || !response.getErrorCode().equals(200)) {
        	throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.PAYMENT_PAYEE_NOT_CONFIG,
                    "PayNotify getPurchaseOrder by bizOrderNum is error!");
        }
        PurchaseOrderDTO purchaseOrderDTO = response.getResponse();
        if(purchaseOrderDTO != null) {
        	com.everhomes.pay.order.OrderType orderType = com.everhomes.pay.order.OrderType.fromCode(purchaseOrderDTO.getPaymentOrderType());
            if(orderType != null) {
                switch (orderType) {
                    case PURCHACE:
                        if(purchaseOrderDTO.getPaymentStatus() == PurchaseOrderPaymentStatus.PAID.getCode()){
                            //支付成功
                            paySuccess(purchaseOrderDTO);
                        }
                        break;
                    default:
                        LOGGER.error("unsupport orderType, orderType={}, cmd={}", orderType.getCode(), StringHelper.toJsonString(cmd));
                }
            }else {
                LOGGER.error("orderType is null, cmd={}", StringHelper.toJsonString(cmd));
            }
        }
    }
    
    public void paySuccess(PurchaseOrderDTO purchaseOrderDTO) {
        LOGGER.error("default payment success call back, purchaseOrderDTO={}", purchaseOrderDTO);
        //这个没有请求第三发，所以直接走
        this.dbProvider.execute((TransactionStatus status) -> {
        	//更新eh_payment_bill_orders表
            assetProvider.updatePaymentBillOrder(purchaseOrderDTO.getBusinessOrderNumber(), purchaseOrderDTO.getPaymentStatus(),
            		purchaseOrderDTO.getPaymentType(), purchaseOrderDTO.getPaymentSucessTime(), purchaseOrderDTO.getPaymentChannel());
            return null;
        });
        List<PaymentBillOrder> paymentBillOrderList = assetProvider.findPaymentBillOrderRecordByOrderNum(purchaseOrderDTO.getBusinessOrderNumber());
        PayCallbackCommand cmd2 = new PayCallbackCommand();
		if(paymentBillOrderList != null) {
			PaymentBillOrder paymentBillOrder = paymentBillOrderList.get(0);
			if(paymentBillOrder != null) {
				cmd2.setOrderNo(paymentBillOrder.getBillId());
				this.dbProvider.execute((TransactionStatus status) -> {
		        	//更新eh_pmsy_order_items表的支付状态
					pmsyProvider.updatePmsyOrderItemByOrderId(Long.parseLong(paymentBillOrder.getBillId()));
		            return null;
		        });
			}
		}
		cmd2.setOrderType(purchaseOrderDTO.getPaymentOrderType().toString());
		if(purchaseOrderDTO.getBusinessPayerId() != null) {
			cmd2.setPayAccount(purchaseOrderDTO.getBusinessPayerId().toString());
		}
		if(purchaseOrderDTO.getAmount() != null) {
			cmd2.setPayAmount(purchaseOrderDTO.getAmount().toString());
		}
		if(purchaseOrderDTO.getPaymentStatus() != null) {
			cmd2.setPayStatus(purchaseOrderDTO.getPaymentStatus().toString());
		}
		cmd2.setPayTime(purchaseOrderDTO.getPaymentSucessTime().toString());
		
		OrderEmbeddedHandler orderEmbeddedHandler = PlatformContext.getComponent(
				OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+OrderType.PM_SIYUAN_CODE);
		if(cmd2.getPayStatus().equalsIgnoreCase("2")) {
			//判断是否处于左邻测试环境，如果是，则不走海岸的回调
	        if(pmsyService.isZuolinTest()) {
	        	return;
	        }else {
	        	//如果是正式环境，则要走海岸的支付回调
	        	orderEmbeddedHandler.paySuccess(cmd2);
	        }
		}
    }
    
    private String generateAccountCode(Integer namespaceId) {
        StringBuilder strBuilder = new StringBuilder();
        
        strBuilder.append("NS");
        strBuilder.append(namespaceId);
        
        return strBuilder.toString();
    }
 
}