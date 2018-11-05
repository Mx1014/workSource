// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.news.NewsService;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneResponse;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.AllianceOperateService;
import com.everhomes.yellowPage.AllianceOperateServiceProvider;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 快讯
 */
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 40500)
public class OPPushServiceAllianceHandler implements OPPushHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(OPPushServiceAllianceHandler.class);
	
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	@Autowired
	AllianceOperateServiceProvider allianceOperateServiceProvider;
	
	@Autowired
	YellowPageService yellowPageService;
	
	@Autowired
	YellowPageProvider yellowPageProvider;
	
    @Override
	public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

//		OPPushInstanceConfig config = (OPPushInstanceConfig) StringHelper.fromJsonString(instanceConfig.toString(),
//				OPPushInstanceConfig.class);
//
//		ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());
//		Long type = StringUtils.isBlank(app.getCustomTag()) ? 0L : Long.parseLong(app.getCustomTag());
//
//		AllianceCommonCommand cmd = new AllianceCommonCommand();
//		List<AllianceOperateService> dtos = allianceOperateServiceProvider.listOperateServices(cmd);
//		List<OPPushCard> listCards = new ArrayList<>();
//		String host = "service-alliance";
//
//		for (AllianceOperateService dto : dtos) {
//			OPPushCard card = new OPPushCard();
//			ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(dto.getId(), null, null);
//			card.setClientHandlerType(ClientHandlerType.INSIDE_URL.getCode());
//			card.setRouterPath("/detail");
//			String url = yellowPageService.processDetailUrl(sa.getId(), sa.getName(), sa.getOwnerType(), sa.getOwnerId());
//			card.setRouterQuery("url=" + url); // native时需要的参数
//			String router = 
//					"zl://" + host + card.getRouterPath() 
//					+ "?moduleId=40500&clientHandlerType=" + card.getClientHandlerType() // 固定参数
//					+ "&" + card.getRouterQuery();
//			card.setRouter(router); 
//
//			List<Object> properties = new ArrayList<>();
//			properties.add(dto.getCoverUri() == null ? "" : dto.getCoverUri());
//			properties.add(dto.getTitle() == null ? "" : dto.getTitle());
//			properties.add(dto.getTopFlag() == null ? "0" : "" + dto.getTopFlag());
//			properties.add(DateUtil.dateToStr(dto.getPublishTime(), "yyyy-MM-dd HH:mm"));
//			properties.add(dto.getLikeCount() == null ? "0" : "" + dto.getLikeCount());
//			card.setProperties(properties);
//			listCards.add(card);
//		}
//
//		return listCards;
		return null;
	}
}
