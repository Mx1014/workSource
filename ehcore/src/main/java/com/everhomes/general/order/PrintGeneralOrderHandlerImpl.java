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
import com.everhomes.general.order.GeneralOrderBizHandler;
import com.everhomes.goods.GoodsService;
import com.everhomes.pay.order.SourceType;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.print.SiyinPrintOrderProvider;
import com.everhomes.print.SiyinPrintServiceImpl;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.general.order.CreateOrderCommonInfo;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OrderType.OrderTypeEnum;
import com.everhomes.rest.print.PayPrintGeneralOrderCommand;
import com.everhomes.rest.print.PayPrintOrderCommandV2;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.print.PrintOwnerType;
import com.everhomes.rest.print.PrintPayType;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreateGeneralBillInfo;
import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.PayerInfoDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component(GeneralOrderBizHandler.GENERAL_ORDER_HANDLER + "printOrder") //与OrderTypeEnum保持一致
public class PrintGeneralOrderHandlerImpl extends DefaultGeneralOrderHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintGeneralOrderHandlerImpl.class);
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	UserProvider userProvider;
	
	@Autowired
	SiyinPrintOrderProvider siyinPrintOrderProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;


	@Override
	void dealInvoiceCallBack(OrderCallBackCommand cmd) {
		SiyinPrintOrder order = siyinPrintOrderProvider
				.findSiyinPrintOrderByGeneralOrderId(cmd.getCallBackInfo().getBusinessOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_PRINT_ORDER_NOT_FOUND, "order not exist");
		}
		
		//TODO 更新发票状态
		order.setIsInvoiced(TrueOrFalseFlag.TRUE.getCode());
		siyinPrintOrderProvider.updateSiyinPrintOrder(order);
	}

	@Override
	void dealEnterprisePayCallBack(OrderCallBackCommand cmd) {
		
		SiyinPrintOrder order = siyinPrintOrderProvider
				.findSiyinPrintOrderByGeneralOrderId(cmd.getCallBackInfo().getBusinessOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_PRINT_ORDER_NOT_FOUND, "order not exist");
		}
		
		//TODO 更新支付状态
		LOGGER.info("dealEnterprisePayCallBack:"+StringHelper.toJsonString(cmd));
		order.setPayMode(PrintPayType.ENTERPRISE_PAID.getCode());
		siyinPrintOrderProvider.updateSiyinPrintOrder(order);
	}

	@Override
	OrderTypeEnum getOrderTypeEnum() {
		return OrderTypeEnum.PRINT_ORDER;
	}

	@Override
	CreateOrderCommonInfo getCreateOrderInfo(Object bussinessCommand) {
		PayPrintGeneralOrderCommand cmd = (PayPrintGeneralOrderCommand) bussinessCommand;
		CreateOrderCommonInfo info = ConvertHelper.convert(cmd, CreateOrderCommonInfo.class);
		List<GoodDTO> goods = new ArrayList<>();
		GoodDTO good = new GoodDTO();
		good.setNamespace("NS");
		good.setTag1("print");
		good.setTag2("copy");
		good.setServeApplyName("这里填写服务提供商名称"); //
		good.setGoodTag("这里填写商品标志");// 商品标志
		good.setGoodName("这里填写商品名称");// 商品名称
		good.setGoodDescription("这里填写商品描述");// 商品描述
		good.setCounts(1);
		good.setPrice(new BigDecimal("0.01"));
		good.setTotalPrice(new BigDecimal("0.01"));
		goods.add(good);
		info.setGoods(goods);
		info.setTotalAmount(new BigDecimal("0.01"));
		String backUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.v2.callback.url.siyinprint",
				"/siyinprint/notifySiyinprintOrderPaymentV2");
		info.setCallBackUrl(backUrl);
		info.setOrderTitle("云打印");
		info.setOrderTitle("云打印");
		info.setSourceType(SourceType.PC.getCode());
		return info;
	}

}
