package com.everhomes.asset;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.asset.zjgkVOs.ZjgkPaymentConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.gorder.sdk.order.OrderService;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.pay.order.SourceType;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.BillIdAndAmount;
import com.everhomes.rest.asset.CreatePaymentBillOrder;
import com.everhomes.rest.gorder.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.gorder.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.gorder.order.PurchaseOrderCommandResponse;
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
	
	public final long EXPIRE_TIME_15_MIN_IN_SEC = 15 * 60L;
    
    public PreOrderDTO createOrder(CreatePaymentBillOrder cmd) {
        checkPaymentBillOrderPaidStatus(cmd);
        PaymentBillGroup billGroup = checkBillGroup(cmd);
        checkPaymentPayeeId(cmd, billGroup);
        
        String businessOrderType = OrderType.OrderTypeEnum.WUYE_CODE.getPycode();

        assetProvider.createBillOrderMaps(cmd.getBills(), businessOrderType);
        
        CreatePurchaseOrderCommand preOrderCommand = preparePaymentBillOrder(cmd, billGroup);
        
        PreOrderDTO preOrderDTO = sendCreatePreOrderRequest(preOrderCommand);


        //5、更新统一订单信息
        //afterPaymentBillOrderCreate(orderCommandResponse, order.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());
        
                
	    return preOrderDTO;
	}
	
	protected PreOrderDTO sendCreatePreOrderRequest(CreatePurchaseOrderCommand createOrderCommand) {
		PreOrderDTO preOrderDTO = null;
        CreatePurchaseOrderRestResponse  createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
        if(!checkOrderRestResponseIsSuccess(createOrderResp)) {
            LOGGER.error("Failed to create bill order, params={}", createOrderCommand);
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.BILL_ORDER_CREATION_FAILED,
                    "Failed to create bill order");
        }
        OrderCommandResponse orderCommandResponse = createOrderResp.getResponse();
        
        //4、组装支付方式
        //preOrderDTO = orderCommandResponseToDto(orderCommandResponse, createOrderCommand);

        //6、返回
        return preOrderDTO;
	}
	

    
    protected CreatePurchaseOrderCommand preparePaymentBillOrder(CreatePaymentBillOrder cmd, PaymentBillGroup billGroup) {
        CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();
        
        BigDecimal totalAmountCents = calculateBillOrderAmount(cmd);
        preOrderCommand.setAmount(totalAmountCents.longValue());
        
        String accountCode = generateAccountCode(cmd.getNamespaceId());
        preOrderCommand.setAccountCode(accountCode);
        preOrderCommand.setClientAppName(cmd.getClientAppName());

        // 移到统一订单系统完成
        // String BizOrderNum  = getOrderNum(orderId, OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
        // preOrderCommand.setBizOrderNum(BizOrderNum);
        
        preOrderCommand.setBuyerType(cmd.getBusinessPayerType());
        preOrderCommand.setBuyerId(cmd.getBusinessPayerId());
        getBusinessPayerParams(cmd, billGroup);
        
        preOrderCommand.setPayeeId(billGroup.getBizPayeeId());
        //preOrderCommand.setBizPayeeType(billGroup.getBizPayeeType());

        preOrderCommand.setExtendInfo(genPaymentExtendInfo(cmd, billGroup));
        preOrderCommand.setPaymentParams(getPaymentParams(cmd, billGroup));
        preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
        preOrderCommand.setBackUrl(getPayCallbackUrl(cmd));
        preOrderCommand.setExtendInfo(cmd.getExtendInfo());
        //localeStringProvider.find(AssetServiceErrorCode.SCOPE, AssetServiceErrorCode.PAYMENT_GOOD_NAME, buyer.getLocale());
        preOrderCommand.setGoodsName("物业缴费");
        preOrderCommand.setGoodsDescription(null);
        preOrderCommand.setIndustryName(null);
        preOrderCommand.setIndustryCode(null);
        preOrderCommand.setSourceType(cmd.getSourceType());
        preOrderCommand.setOrderRemark1("物业缴费");
        //preOrderCommand.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        preOrderCommand.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        preOrderCommand.setOrderRemark4(null);
        preOrderCommand.setOrderRemark5(null);
        
        return preOrderCommand;
    }
	
	/**
	 * 通用版本，帐单数据在左邻系统，直接从系统中读取数据来进行校验；
	 * 当帐单已经被支付，则不需要再支付；
	 * @param cmd 命令参数
	 * @throws RuntimeErrorException 当所有帐单都已经被支付，则不需要再进行支付，此时抛异常
	 */
	protected void checkPaymentBillOrderPaidStatus(CreatePaymentBillOrder cmd) {
	    List<BillIdAndAmount> bills = cmd.getBills();
        List<String> billIds = new ArrayList<>();
        for(BillIdAndAmount billIdAndAmount : bills){
            billIds.add(billIdAndAmount.getBillId());
        }
        
        List<PaymentBills> paidBills = assetProvider.findPaidBillsByIds(billIds);
        if( paidBills.size() >0 )  {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Bill orders have been paid, billIds={}, billGroupId={}", billIds, cmd.getBillGroupId());
            }
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.HAS_PAID_BILLS, "Bills have been paid");
        }
	}
	
	protected PaymentBillGroup checkBillGroup(CreatePaymentBillOrder cmd) {
        PaymentBillGroup billGroup = assetProvider.getBillGroupById(cmd.getBillGroupId());
        if(billGroup == null) {
            LOGGER.error("Bill group not found, billGroupId={}", cmd.getBillGroupId());
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.BILL_GROUP_NOT_FOUND, "Bill group not found");
        }
        
        return billGroup;
	}
	
	/**
	 * 校验收款方帐号是否配置到了帐单组中，没有收款方帐号无法支付
	 * @param cmd 下单请求信息
	 * @param billGroup 帐单组信息
	 * @return
	 */
	protected void checkPaymentPayeeId(CreatePaymentBillOrder cmd, PaymentBillGroup billGroup) {
        //通过账单组获取到账单组的bizPayeeType（收款方账户类型）和bizPayeeId（收款方账户id）
	    // TODO: 收款方类型也应该校验一下，但未确定以往数据是否已经补上该信息，故暂时不校验
        // String payeeuserType = billGroup.getBizPayeeType();
        
        // 支付系统中的收款方ID
        Long payeeUserId = billGroup.getBizPayeeId();
        if(payeeUserId == null) {
            LOGGER.error("Payee user id not found(id in payment system), billGroupId={}, cmd={}");
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.PAYMENT_PAYEE_NOT_CONFIG,
                    "Payee user id not found");
        } 
	}
	
	/**
	 * 计算帐单金额，以分为单位；
	 * 帐单里记录的可能是浮点型的金额，需要先转为整形，然后再进行+计算；
	 * @param cmd 下单请求信息
	 * @return 总金额，以分为单位
	 */
	protected BigDecimal calculateBillOrderAmount(CreatePaymentBillOrder cmd) {
	    List<BillIdAndAmount> bills = cmd.getBills();
	    BigDecimal totalAmountCents = new BigDecimal(0);
        for(BillIdAndAmount billIdAndAmount : bills){
            String billAmountStr = billIdAndAmount.getAmountOwed();
            BigDecimal billAmountCents = new BigDecimal(billAmountStr).multiply(new BigDecimal(100));
            totalAmountCents.add(billAmountCents);
        }
        
        return totalAmountCents;
	}
	
	/**
	 * 获取支付的额外参数信息，按通联的定义，在微信公众号支付时，必须填写微信的openid
     * @param cmd 下单请求信息
     * @param billGroup 帐单组
	 * @return 支付的额外参数信息
	 */
	protected Map<String, String> getPaymentParams(CreatePaymentBillOrder cmd, PaymentBillGroup billGroup) {
	    Map<String, String> map = null;
	    
	    PaymentType paymentType = PaymentType.fromCode(cmd.getPaymentType());
        if (paymentType == PaymentType.WECHAT_JS_ORG_PAY) {
            String businessPayerIdStr = cmd.getBusinessPayerId();
            Long businessPayerId = UserContext.currentUserId();
            try {
                businessPayerId = Long.parseLong(businessPayerIdStr);
            } catch (Exception e) {
                LOGGER.error("Payer user id invalid in parameter, current user id will be used, orgPayerId={}, currentUserId={}", businessPayerIdStr, businessPayerId, e);
            }
            
            User payer = userProvider.findUserById(businessPayerId);
            if(payer != null) {
                map = new HashMap<String, String>();
                map.put("acct", payer.getNamespaceUserToken());
            } else {
                LOGGER.error("User not found, userId={}, billGroupId={}", businessPayerIdStr, billGroup.getId());
            }
        }
        
        return map;
	}
	
	/**
	 * 获取付款方的额外参数信息，比如当个人帐号下单时，必须绑定一个手机号，此时手机号为额外信息；
	 * 如果是企业作为付款方，可能需要更多额外信息（目前未实现企业付款）；
     * @param cmd 下单请求信息
     * @param billGroup 帐单组
	 * @return 取付款方的额外参数信息
	 */
	protected String getBusinessPayerParams(CreatePaymentBillOrder cmd, PaymentBillGroup billGroup) {
	    OwnerType businessPayerType = OwnerType.fromCode(cmd.getBusinessPayerType());
	    if(businessPayerType == OwnerType.ORGANIZATION) {
	        // TODO: 未考虑企业帐号支付，先全部认为是个人帐号支付
	    }
	    businessPayerType = OwnerType.USER;
	    
	    // 如果参数中的付款方ID不正确，则使用当前用户ID
	    String businessPayerIdStr = cmd.getBusinessPayerId();
	    Long businessPayerId = UserContext.currentUserId();
	    try {
	        businessPayerId = Long.parseLong(businessPayerIdStr);
	    } catch (Exception e) {
            LOGGER.error("Payer user id invalid in parameter, current user id will be used, orgPayerId={}, currentUserId={}", businessPayerIdStr, businessPayerId, e);
        }
	    
	    UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, cmd.getNamespaceId());
	    String buyerPhone = null;
        if(buyerIdentifier != null) {
            buyerPhone = buyerIdentifier.getIdentifierToken();
        } 
        // 找不到手机号则默认一个
        if(buyerPhone == null || buyerPhone.trim().length() == 0) {
            buyerPhone = configurationProvider.getValue("gorder.default.personal_bind_phone", "");
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("businessPayerPhone", buyerPhone);
        return StringHelper.toJsonString(map);
	}
	
	/**
	 * 构造扩展信息，该信息在支付结束时会透传回来给业务系统
	 * @param cmd 下单请求信息
	 * @param billGroup 帐单组
	 * @return 扩展信息
	 */
	protected String genPaymentExtendInfo(CreatePaymentBillOrder cmd, PaymentBillGroup billGroup) {
	    // 通过账单ID找到ownerID，再通过ownerID找到项目名称
        String projectName = "";
        if(cmd.getBills() != null) {
            Long billId = Long.parseLong(cmd.getBills().get(0).getBillId());
            projectName = assetProvider.getProjectNameByBillID(billId);
        }

        String billGroupName = "";
        if(billGroup.getName() != null) {
            billGroupName = billGroup.getName();
        }
        
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("项目名称:");
        strBuilder.append(projectName);
        strBuilder.append(", ");
        strBuilder.append("账单组名称:");
        strBuilder.append(billGroupName);
        return strBuilder.toString();
	}
	
	private String getPayCallbackUrl(CreatePaymentBillOrder cmd) {
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
	
	/*
     * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
       CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
     */
    private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
        if(response != null && response.getErrorCode() != null 
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }

}