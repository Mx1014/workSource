package com.everhomes.general.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.everhomes.asset.PaymentConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.goods.GoodsService;
import com.everhomes.pay.order.SourceType;
import com.everhomes.print.SiyinPrintServiceImpl;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.print.PayPrintGeneralOrderCommand;
import com.everhomes.rest.print.PayPrintOrderCommandV2;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.PayerInfoDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;

@Component(GeneralOrderHandler.GENERAL_ORDER_HANDLER + OrderType.PRINT_ORDER_CODE) //要与OrderTypeEnum的co一致
public class PrintGeneralOrderHandlerImpl extends DefaultGeneralOrderHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintGeneralOrderHandlerImpl.class);
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	UserProvider userProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
    @Value("${server.contextPath:}")
    private String contextPath;

	@Override
	CreateMerchantOrderCommand buildOrderCommand(Object cmd) {
		PayPrintGeneralOrderCommand cmd2 = (PayPrintGeneralOrderCommand) cmd;
		Long PaymentPayeeId = 100004L;
		Long totalAmount = 1L;
		Integer goodCount = 1;
		Long organizationId = 1023080L;
		Long ownerId = 240111044331058733L;
		String clientAppName = "当前clientName";
		
		Long appId = 66348L;
		CreateMerchantOrderCommand preOrderCommand = new CreateMerchantOrderCommand();
		preOrderCommand.setPaymentMerchantId(PaymentPayeeId);
		PayerInfoDTO payerInfo = new PayerInfoDTO();
		payerInfo.setNamespaceId(UserContext.getCurrentNamespaceId());
		payerInfo.setOrganizationId(organizationId); // 左邻公司
		payerInfo.setUserId(UserContext.currentUserId());
		payerInfo.setAppId(appId);
		preOrderCommand.setPayerInfo(payerInfo);
		List<GoodDTO> goods = new ArrayList<>();
		GoodDTO good = new GoodDTO();
		good.setNamespace("NS");
		good.setTag1("print");
		good.setTag2("copy");
		good.setServeApplyName("这里填写服务提供商名称"); //
		good.setGoodTag("这里填写商品标志");// 商品标志
		good.setGoodName("这里填写商品名称");// 商品名称
		good.setGoodDescription("这里填写商品描述");// 商品描述
		good.setCounts(goodCount);
		good.setPrice(new BigDecimal((totalAmount * 1.0) / 100));
		goods.add(good);
		preOrderCommand.setGoods(goods);
		preOrderCommand.setAmount(totalAmount);
		String accountCode = SiyinPrintServiceImpl.BIZ_ACCOUNT_PRE + UserContext.getCurrentNamespaceId();
		preOrderCommand.setAccountCode(accountCode);
		preOrderCommand.setClientAppName(clientAppName);
		preOrderCommand.setBusinessOrderType(OrderType.OrderTypeEnum.PRINT_ORDER.getV2code());
		// 移到统一订单系统完成
		// String BizOrderNum = getOrderNum(orderId,
		// OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
		BusinessPayerType payerType = BusinessPayerType.USER;
//	        preOrderCommand.setBusinessOrderNumber(generateBizOrderNum(accountCode,OrderType.OrderTypeEnum.PRINT_ORDER.getPycode(),order.getOrderNo()));
		preOrderCommand.setBusinessPayerType(payerType.getCode());
		preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
		String businessPayerParams = getBusinessPayerParams();
		preOrderCommand.setBusinessPayerParams(businessPayerParams);
		// preOrderCommand.setPaymentPayeeType(billGroup.getBizPayeeType()); 不填会不会有问题?
		preOrderCommand.setPaymentPayeeId(PaymentPayeeId); // 不知道填什么
//	        preOrderCommand.setPaymentParams(flattenMap);
		// preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
		String homeUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(), "home.url", "");
		String backUri = configProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.v2.callback.url.siyinprint",
				"/siyinprint/notifySiyinprintOrderPaymentV2");
		String backUrl = homeUrl + contextPath + backUri;
		preOrderCommand.setCallbackUrl(backUrl);
		preOrderCommand.setExtendInfo(OrderType.OrderTypeEnum.PRINT_ORDER.getMsg());
		preOrderCommand.setGoodsName("云打印");
		preOrderCommand.setGoodsDescription(OrderType.OrderTypeEnum.PRINT_ORDER.getMsg());
		preOrderCommand.setIndustryName(null);
		preOrderCommand.setIndustryCode(null);
		preOrderCommand.setSourceType(SourceType.PC.getCode());
		preOrderCommand.setOrderRemark1(configProvider.getValue("siyinprint.pay.OrderRemark1", "云打印"));
		// preOrderCommand.setOrderRemark2(String.valueOf(cmd.getOrderId()));
		preOrderCommand.setOrderRemark3(String.valueOf(ownerId));
		preOrderCommand.setOrderRemark4(null);
		preOrderCommand.setOrderRemark5(null);
		String systemId = configProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_SYSTEM_ID,
				"");
		preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
		return preOrderCommand;
	}

	@Override
	public String getHandlerName() {
		return OrderType.PRINT_ORDER_CODE+"";
	}
	
    private String getBusinessPayerParams() {

        Long businessPayerId = UserContext.currentUserId();

        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, UserContext.getCurrentNamespaceId());
        String buyerPhone = null;
        if(buyerIdentifier != null) {
            buyerPhone = buyerIdentifier.getIdentifierToken();
        }
        // 找不到手机号则默认一个
        if(buyerPhone == null || buyerPhone.trim().length() == 0) {
            buyerPhone = configProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("businessPayerPhone", buyerPhone);
        return StringHelper.toJsonString(map);
    
	}

}
