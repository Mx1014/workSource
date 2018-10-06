package com.everhomes.general.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.goods.GoodsService;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.PayerInfoDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

@Component(GeneralOrderHandler.GENERAL_ORDER_HANDLER + OrderType.PRINT_ORDER_CODE) //要与OrderTypeEnum的co一致
public class PrintGeneralOrderHandlerImpl extends DefaultGeneralOrderHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintGeneralOrderHandlerImpl.class);
	
	@Autowired
	GoodsService goodsService;

	@Override
	CreateMerchantOrderCommand buildOrderCommand(Object cmd) {
		final String UNKNOWN = "????";
		CreateMerchantOrderCommand createOrderCmd = new CreateMerchantOrderCommand();
		createOrderCmd.setPaymentMerchantId(1000L); 
		PayerInfoDTO payerInfo = new PayerInfoDTO();
		payerInfo.setNamespaceId(UserContext.getCurrentNamespaceId());
		payerInfo.setOrganizationId(1023080L); //左邻公司
		payerInfo.setUserId(UserContext.currentUserId());
		payerInfo.setAppId(66348L);
		createOrderCmd.setPayerInfo(payerInfo);
		
		List<GoodDTO> goods =new  ArrayList<>();
		GoodDTO good = new GoodDTO();
		good.setNamespace("NS");
		good.setTag1("print");
		good.setTag2("copy");
		good.setServeApplyName("这里填写服务提供商名称"); //
		good.setGoodTag("这里填写商品标志");//商品标志
		good.setGoodName("这里填写商品名称");//商品名称
		good.setGoodDescription("这里填写商品描述");//商品描述
		good.setCounts(1);
		good.setPrice(new BigDecimal("0.01"));
		goods.add(good);
		createOrderCmd.setGoods(goods);
        LOGGER.info("preOrderCommand:"+StringHelper.toJsonString(createOrderCmd));
        return createOrderCmd;
	}

	@Override
	public String getHandlerName() {
		return OrderType.PRINT_ORDER_CODE+"";
	}

}
