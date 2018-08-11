package com.everhomes.asset;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.gorder.sdk.order.OrderService;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.SourceType;
import com.everhomes.rest.asset.CreatePaymentBillOrder;
import com.everhomes.rest.gorder.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.gorder.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PayServiceErrorCode;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午2:57:09
 */
public class DefaultAssetVendorHandler extends AssetVendorHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAssetVendorHandler.class);
    
    @Autowired
    private AssetProvider assetProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
	private ContentServerService contentServerService;
    
    @Value("${server.contextPath:}")
    private String contextPath;
	
	@Autowired
	private OrderService orderService;
    
    public PreOrderDTO createOrder(CreatePaymentBillOrder cmd) {
    	//组装command ， 请求支付模块的下预付单
    	PreOrderCommand order = preparePaymentBillOrder(cmd);
	    return createPreOrder(order);
	}
	
	protected PreOrderDTO createPreOrder(PreOrderCommand order) {
		PreOrderDTO preOrderDTO = null;
        //1、检查买方（付款方）是否有会员，无则创建
        User buyer = userProvider.findUserById(UserContext.currentUserId());

        //2、收款方是否有会员，无则报错
        Long payeeUserId = order.getBizPayeeId();
        if(payeeUserId == null) {
            LOGGER.error("Payee user id not found(id in payment system), order={}", order);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
                    "Payee user id not found");
        }
        
        //3、组装报文，发起下单请求
        CreatePurchaseOrderCommand createOrderCommand = preparePurchaseOrderByBuyer(order, buyer);
        CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
        if(!createOrderResp.getErrorCode().equals(200)) {
            LOGGER.error("create order fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "create order fail");
        }
        OrderCommandResponse orderCommandResponse = createOrderResp.getResponse();
        
        //4、组装支付方式
        preOrderDTO = orderCommandResponseToDto(orderCommandResponse, order);

        //5、更新统一订单信息
        afterPaymentBillOrderCreate(orderCommandResponse, order.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());

        //6、返回
        return preOrderDTO;
	}
	
	protected PreOrderCommand preparePaymentBillOrder(CreatePaymentBillOrder cmd) {
		AssetVendor vendor = checkAssetVendor(cmd.getNamespaceId(),0);
        AssetVendorHandler handler = getAssetVendorHandler(vendor.getVendorName());
		//组装command ， 请求支付模块的下预付单
        return handler.preparePaymentBillOrder(cmd);
	}
	
	protected void afterPaymentBillOrderCreate(OrderCommandResponse orderCommandResponse, Long orderId, Integer paymentOrderType) {
//      PaymentOrderRecord record = ConvertHelper.convert(orderCommandResponse, PaymentOrderRecord.class);
//      record.setOrderNum(orderCommandResponse.getBizOrderNum());//业务订单编号
//      record.setOrderId(orderId);//业务订单ID
//      //PaymentOrderId为支付系统传来的orderId
//      record.setPaymentOrderId(orderCommandResponse.getOrderId());//支付订单ID
//      record.setPaymentOrderType(paymentOrderType);
//      record.setOrderType("wuyeCode");
//      payProvider.createPaymentOrderRecord(record);
		
		//更新eh_payment_bill_orders表的统一订单ID、支付方式
		
		
		
	}
	
	private CreatePurchaseOrderCommand preparePurchaseOrderByBuyer(PreOrderCommand cmd, User buyer){
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        CreatePurchaseOrderCommand createOrderCmd = new CreatePurchaseOrderCommand();
        
        String accountCode = generateAccountCode(cmd.getNamespaceId());
        createOrderCmd.setAccountCode(accountCode);
        createOrderCmd.setClientAppName(cmd.getClientAppName());

        String BizOrderNum  = getOrderNum(cmd.getOrderId(),cmd.getOrderType());
        createOrderCmd.setBizOrderNum(BizOrderNum);
        
        createOrderCmd.setBuyerType(OwnerType.USER.getCode());
        createOrderCmd.setBuyerId(String.valueOf(buyer.getId()));
        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(buyer.getId(), cmd.getNamespaceId());
        if(buyerIdentifier != null) {
            String buyerPhone = buyerIdentifier.getIdentifierToken();
            createOrderCmd.setBuyerPhone(buyerPhone);
        }
        
        createOrderCmd.setPayeeUserId(cmd.getBizPayeeId());//收款方ID
        createOrderCmd.setAmount(cmd.getAmount());
        createOrderCmd.setPaymentParams(flattenMap);
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        
        createOrderCmd.setBackUrl(getPayCallbackUrl(cmd));
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        //localeStringProvider.find(AssetServiceErrorCode.SCOPE, AssetServiceErrorCode.PAYMENT_GOOD_NAME, buyer.getLocale());
        createOrderCmd.setGoodsName("物业缴费");
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
        createOrderCmd.setOrderRemark1("物业缴费");
        createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        createOrderCmd.setOrderRemark4(null);
        createOrderCmd.setOrderRemark5(null);
        
        return createOrderCmd;
    }
	
	private String getPayCallbackUrl(PreOrderCommand cmd) {
        String configKey = "pay.v2.callback.url.asset";
        String backUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), configKey, "");
        if(backUrl == null || backUrl.trim().length() == 0) {
            LOGGER.error("Payment callback url empty, configKey={}, cmd={}", configKey, cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAY_CALLBACK_URL_EMPTY,
                    "Payment callback url empty");
        }
        
        if(!backUrl.toLowerCase().startsWith("http")) {
            String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
            backUrl = homeUrl + contextPath + backUrl;
        }
        
        return backUrl;
    }
    
    private String generateAccountCode(Integer namespaceId) {
        StringBuilder strBuilder = new StringBuilder();
        
        strBuilder.append("NS");
        strBuilder.append(namespaceId);
        
        return strBuilder.toString();
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
	
	private String getOrderNum(Long orderId, String orderType) {
        String v2code = OrderType.OrderTypeEnum.getV2codeByPyCode(orderType);
        DecimalFormat df = new DecimalFormat("00000000000000000");
        String orderIdStr = df.format(orderId);
        if(orderIdStr!=null && orderIdStr.length()>17){
            orderIdStr = orderIdStr.substring(2);
        }
        return v2code+orderIdStr;
    }
	
	private PreOrderDTO orderCommandResponseToDto(OrderCommandResponse orderCommandResponse, PreOrderCommand cmd){
        PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
        List<PayMethodDTO> payMethods = new ArrayList<>();//业务系统自己的支付方式格式
        List<com.everhomes.pay.order.PayMethodDTO> bizPayMethods = orderCommandResponse.getPaymentMethods();//支付系统传回来的支付方式
        String format = "{\"getOrderInfoUrl\":\"%s\"}";
        for(com.everhomes.pay.order.PayMethodDTO bizPayMethod : bizPayMethods) {
        	PayMethodDTO payMethodDTO = new PayMethodDTO();//支付方式
        	payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
        	payMethodDTO.setExtendInfo(String.format(format, orderCommandResponse.getOrderPaymentStatusQueryUrl()));
        	String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
        	payMethodDTO.setPaymentLogo(paymentLogo);
        	payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
        	PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
        	com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
        	if(bizPaymentParamsDTO != null) {
        		paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
        	}
        	payMethodDTO.setPaymentParams(paymentParamsDTO);
        	payMethods.add(payMethodDTO);
        }
        dto.setPayMethod(payMethods);
        dto.setExpiredIntervalTime(orderCommandResponse.getExpirationMillis());
        dto.setAmount(cmd.getAmount());
        dto.setOrderId(cmd.getOrderId());
        return dto;
    }
	

}