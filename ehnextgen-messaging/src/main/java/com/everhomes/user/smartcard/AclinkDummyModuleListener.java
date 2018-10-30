package com.everhomes.user.smartcard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.user.SmartCardHandler;
import com.everhomes.rest.user.SmartCardHandlerItem;
import com.everhomes.rest.user.SmartCardType;

@Component
public class AclinkDummyModuleListener implements SmartCardListener {

	@Override
	public SmartCardModuleInfo initModule() {
		SmartCardModuleInfo info = new SmartCardModuleInfo();
		info.setModuleId(41014L);
		return info;
	}

	/**
	 * 融合在一卡通里面的二维码
	 */
	@Override
	public List<SmartCardHandler> generateSmartCards(
			SmartCardProcessorContext ctx) {
		List<SmartCardHandler> hs = new ArrayList<SmartCardHandler>();
		SmartCardHandler aclinkCard = new SmartCardHandler(); 
		aclinkCard.setData("test-aclink-only222");
		aclinkCard.setTitle("公共门禁222");
		aclinkCard.setSmartCardType(SmartCardType.SMART_CARD_ACLINK.getCode());
		hs.add(aclinkCard);
		
		return hs;
	}

	/**
	 * 每个独立的二维码
	 */
	@Override
	public List<SmartCardHandler> generateStandaloneCards(
			SmartCardProcessorContext ctx) {
		List<SmartCardHandler> hs = new ArrayList<SmartCardHandler>();
		SmartCardHandler aclinkCard = new SmartCardHandler(); 
		aclinkCard.setData("test-aclink-only222");
		aclinkCard.setTitle("公共门禁222");
		aclinkCard.setSmartCardType(SmartCardType.SMART_CARD_ACLINK.getCode());
		
		List<SmartCardHandlerItem> items = new ArrayList<SmartCardHandlerItem>();
		SmartCardHandlerItem item = new SmartCardHandlerItem();
		item.setTitle("楼层");
		item.setRouterUrl("zl://aclink/index");
		item.setName("aclink-floor");
		item.setDefaultValue("5");
		items.add(item);
		
		item = new SmartCardHandlerItem();
		item.setTitle("VIP");
		item.setRouterUrl("zl://aclink/index");
		item.setName("aclink-vip");
		item.setDefaultValue("VIP3");
		items.add(item);      
		aclinkCard.setItems(items);
        
		hs.add(aclinkCard);
		
		return hs;
	}

}
