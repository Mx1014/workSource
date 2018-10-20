package com.everhomes.general.order;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.asset.PaymentConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pay.order.SourceType;
import com.everhomes.print.SiyinPrintServiceImpl;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.general.order.OrderCallBackType;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OrderType.OrderTypeEnum;
import com.everhomes.rest.print.PayPrintGeneralOrderCommand;
import com.everhomes.rest.print.PrintOwnerType;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreateGeneralBillInfo;
import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.OrderErrorCode;
import com.everhomes.rest.promotion.order.PayerInfoDTO;
import com.everhomes.rest.promotion.order.controller.CreateMerchantOrderRestResponse;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

public abstract class DefaultGeneralOrderHandler implements GeneralOrderBizHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGeneralOrderHandler.class);
	
    @Autowired
    protected GeneralOrderService orderService;
    
	@Autowired
	UserProvider userProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	
    @Value("${server.contextPath:}")
    private String contextPath;
    
    //子类实现
    abstract OrderTypeEnum getOrderTypeEnum();
	abstract void fillEnterprisePaySpecificInfo(CreateGeneralBillInfo billInfo, CreateOrderBaseInfo baseInfo);
	abstract void dealInvoiceCallBack(OrderCallBackCommand cmd);
	abstract void dealEnterprisePayCallBack(OrderCallBackCommand cmd);
	
	@Override
	public CreateMerchantOrderResponse createOrder(CreateOrderBaseInfo commonInfo) {
		
		CreateMerchantOrderCommand createOrderCmd = buildOrderCommand(commonInfo);
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
	
	private CreateMerchantOrderCommand buildOrderCommand(CreateOrderBaseInfo info) {
		CreateMerchantOrderCommand orderCmd = new CreateMerchantOrderCommand();
		buildPayerInfo(orderCmd, info); // 用户信息
		buildGoods(orderCmd, info); // 商品
		buildGeneralBillInfo(orderCmd, info); // 企业支付参数
		buildOtherPayInfo(orderCmd, info); // 个人支付参数
		if (info.getReturnUrl() != null && info.getReturnUrl().length() >0)
			orderCmd.setReturnUrl(info.getReturnUrl());
		return orderCmd;
	}
	
	private void buildGeneralBillInfo(CreateMerchantOrderCommand orderCmd, CreateOrderBaseInfo baseInfo) {
		CreateGeneralBillInfo createBillInfo = new CreateGeneralBillInfo();
		
		//添加通用的企业支付信息
		createBillInfo.setNamespaceId(UserContext.getCurrentNamespaceId());
		createBillInfo.setOwnerType("community");
		createBillInfo.setOwnerId(baseInfo.getOwnerId());
		createBillInfo.setTargetType(AssetTargetType.ORGANIZATION.getCode());
		createBillInfo.setTargetId(baseInfo.getOrganizationId());
		
		//用户信息
		fillGeneralBillUserInfo(createBillInfo, baseInfo.getOrganizationId());
		
		//添加业务特定信息
		fillEnterprisePaySpecificInfo(createBillInfo, baseInfo); 
		
		//补空
		if (StringUtils.isBlank(createBillInfo.getSourceName())) {
			createBillInfo.setSourceName(baseInfo.getOrderTitle());
		}
		
		orderCmd.setCreateBillInfo(createBillInfo);//将billInfo放到cmd
	}
	
	private void fillGeneralBillUserInfo(CreateGeneralBillInfo createBillInfo, Long organizationId) {
		Long userId = UserContext.currentUserId();
		createBillInfo.setConsumeUserId(userId);
		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organizationId);
		if(null != organizationMember) {
			createBillInfo.setConsumeUserName(organizationMember.getContactName());
		}
	}
	
	private void buildOtherPayInfo(CreateMerchantOrderCommand preOrderCommand, CreateOrderBaseInfo info) {
		preOrderCommand.setPaymentMerchantId(info.getPaymentMerchantId());
		preOrderCommand.setAmount(changePayAmount(info.getTotalAmount()));
		preOrderCommand.setAccountCode("NS"+ UserContext.getCurrentNamespaceId());
		preOrderCommand.setClientAppName(info.getClientAppName());
		preOrderCommand.setBusinessOrderType(getOrderTypeEnum().getPycode());
		preOrderCommand.setBusinessPayerType(BusinessPayerType.USER.getCode());
		preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
		preOrderCommand.setBusinessPayerParams(getBusinessPayerParams());
		String homeUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(), "home.url", "");
		String backUrl = homeUrl + contextPath + info.getCallBackUrl();
		preOrderCommand.setCallbackUrl(backUrl);
		preOrderCommand.setExtendInfo(getOrderTypeEnum().getMsg());
		preOrderCommand.setGoodsName(info.getOrderTitle());
		preOrderCommand.setGoodsDescription(getOrderTypeEnum().getMsg());
		preOrderCommand.setSourceType(info.getPaySourceType());
		preOrderCommand.setOrderRemark1(info.getOrderTitle());
		preOrderCommand.setOrderRemark3(String.valueOf(info.getOwnerId()));
		String systemId = configProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_SYSTEM_ID,
				"");
		preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
	}
	private void buildGoods(CreateMerchantOrderCommand orderCmd, CreateOrderBaseInfo info) {
		orderCmd.setGoods(info.getGoods());
		orderCmd.setGoodsDetail(info.getGoodsDetail());
	}
	
	private void buildPayerInfo(CreateMerchantOrderCommand orderCmd, CreateOrderBaseInfo info) {
		PayerInfoDTO payerInfo = new PayerInfoDTO();
		payerInfo.setNamespaceId(UserContext.getCurrentNamespaceId());
		payerInfo.setOrganizationId(info.getOrganizationId()); // 左邻公司
//		LOGGER.info("user target ID : "  + UserContext.currentUserId());
//		OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(UserContext.currentUserId(),info.getOrganizationId());
//		LOGGER.info("user source ID : "  + detail.getId());
		payerInfo.setUserId(UserContext.currentUserId());
		payerInfo.setAppId(info.getAppOriginId());
		orderCmd.setPayerInfo(payerInfo);
	}

	@Override
	public void dealCallBack(OrderCallBackCommand cmd) {
		if (OrderCallBackType.ENTERPRISE_PAY.getCode().equals(cmd.getCallBackType())) {
			
			dealEnterprisePayCallBack(cmd);
			return;
		}
		
		dealInvoiceCallBack(cmd);
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
    
	private String getBusinessPayerParams() {

		Long businessPayerId = UserContext.currentUserId();
		UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId,
				UserContext.getCurrentNamespaceId());
		String buyerPhone = null;
		if (buyerIdentifier != null) {
			buyerPhone = buyerIdentifier.getIdentifierToken();
		}
		// 找不到手机号则默认一个
		if (buyerPhone == null || buyerPhone.trim().length() == 0) {
			buyerPhone = configProvider.getValue(UserContext.getCurrentNamespaceId(),
					PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("businessPayerPhone", buyerPhone);
		return StringHelper.toJsonString(map);
	}
	
    public Long changePayAmount(BigDecimal amount){
        if(amount == null){
            return 0L;
        }
        return  amount.multiply(new BigDecimal(100)).longValue();
    }
}
