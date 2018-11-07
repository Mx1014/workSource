package com.everhomes.user.smartcard;

import com.everhomes.aclink.DoorAccessService;

import com.everhomes.rest.aclink.DoorAccessQRKeyDTO;
import com.everhomes.rest.aclink.ListDoorAccessQRKeyResponse;
import com.everhomes.rest.user.SmartCardHandler;
import com.everhomes.rest.user.SmartCardHandlerItem;
import com.everhomes.rest.user.SmartCardType;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AclinkModuleListener implements SmartCardListener {
	@Autowired
	private DoorAccessService doorAccessService;
	@Autowired
	private ServiceModuleAppService serviceModuleAppService;

	@Override
	public SmartCardModuleInfo initModule() {
		SmartCardModuleInfo info = new SmartCardModuleInfo();
		info.setModuleId(41000L);
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
		ListDoorAccessQRKeyResponse resp = new ListDoorAccessQRKeyResponse();
		resp = doorAccessService.listDoorAccessQRKey();
		if(null != resp.getKeys() && !resp.getKeys().isEmpty()){
			List<Long> longList = new ArrayList<Long>();
			longList.add(41000L);
            if(null != serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(),longList) && !serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(),longList).isEmpty()){
                Long appOriginId = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(),longList).get(0).getOriginId();
                aclinkCard.setAppOriginId(appOriginId);
            }
			aclinkCard.setData(resp.getKeys().get(0).toString());
			aclinkCard.setTitle("门禁" + resp.getKeys().get(0).getDoorDisplayName());
			aclinkCard.setSmartCardType(SmartCardType.SMART_CARD_ACLINK.getCode());
			hs.add(aclinkCard);
		}
		return hs;
	}

	/**
	 * 每个独立的二维码
	 */
	@Override
	public List<SmartCardHandler> generateStandaloneCards(
			SmartCardProcessorContext ctx) {
		List<SmartCardHandler> hs = new ArrayList<SmartCardHandler>();

		ListDoorAccessQRKeyResponse resp = new ListDoorAccessQRKeyResponse();
		resp = doorAccessService.listDoorAccessQRKey();
		if(null != resp.getKeys() && resp.getKeys().size() > 1){
			for (int i = 1; i < resp.getKeys().size(); i++) {
				SmartCardHandler aclinkCard = new SmartCardHandler();
				aclinkCard.setModuleId(41000L);
				List<Long> longList = new ArrayList<Long>();
				longList.add(41000L);
                if(null != serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(),longList) && !serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(),longList).isEmpty()){
                    Long appOriginId = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(),longList).get(0).getOriginId();
                    aclinkCard.setAppOriginId(appOriginId);
                }
				aclinkCard.setData(resp.getKeys().get(i).toString());
				aclinkCard.setTitle("门禁" + resp.getKeys().get(i).getDoorDisplayName());
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
			}
		}


		return hs;
	}

}
