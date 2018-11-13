// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.news.NewsService;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneResponse;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
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
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	
    @Override
	public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

		OPPushInstanceConfig config = (OPPushInstanceConfig) StringHelper.fromJsonString(instanceConfig.toString(),
				OPPushInstanceConfig.class);

		ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());
		Long type = StringUtils.isBlank(app.getCustomTag()) ? null : Long.parseLong(app.getCustomTag());

		AllianceCommonCommand cmd = new AllianceCommonCommand();
		cmd.setNamespaceId(app.getNamespaceId());
		cmd.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
		cmd.setOwnerId(context.getCommunityId());
		cmd.setType(type);
		List<AllianceOperateService> dtos = allianceOperateServiceProvider.listOperateServices(cmd);
		List<OPPushCard> listCards = new ArrayList<>();
		String host = "service-alliance";
		
		String pageRealDisplayType = getDisplayType(app);
		for (AllianceOperateService dto : dtos) {
			OPPushCard card = new OPPushCard();
			ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(dto.getServiceId(), null, null);
			if (null == sa) {
				continue;
			}
			card.setClientHandlerType(ClientHandlerType.INSIDE_URL.getCode());
			card.setRouterPath("/detail");
			String url = processDetailUrl(sa.getId(), sa.getOwnerId(), context.getCommunityId(), pageRealDisplayType);
			try {
				card.setRouterQuery("url=" + URLEncoder.encode(url, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("encode Err, url:"+url+" e:"+e.getMessage());
			}
			
			String router = "zl://" + host + card.getRouterPath() + "?moduleId=40500&clientHandlerType="
					+ card.getClientHandlerType() // 固定参数
					+ "&" + card.getRouterQuery();
			card.setRouter(router);

			List<Object> properties = new ArrayList<>();
			String imgUrl = getUrlFromUri(sa.getPosterUri());
			properties.add(imgUrl == null ? "" : imgUrl);
			properties.add(sa.getName() == null ? "" : sa.getName());
			if (null == sa.getUpdateTime()) {
				properties.add(DateUtil.dateToStr(sa.getCreateTime(), "yyyy-MM-dd HH:mm"));
			} else {
				properties.add(DateUtil.dateToStr(sa.getUpdateTime(), "yyyy-MM-dd HH:mm"));
			}
			card.setProperties(properties);
			listCards.add(card);
		}
		
		return listCards;
	}
    
    
    private String getUrlFromUri(String uri) {
    	if (StringUtils.isBlank(uri)) {
    		return null;
    	}
    	
		try {
			return contentServerService.parserUri(uri, EntityType.USER.getCode(),
					UserContext.current().getUser().getId());
		} catch (Exception e) {
			LOGGER.error("Failed to parse poster uri of ServiceAlliances, uri =" + uri + " e:" + e);
		}
		
		return null;
    }
    
	private String processDetailUrl(Long serviceId, Long communityId, Long type, String pageRealDisplayType) {

		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");

		StringBuilder detailUrl = new StringBuilder("/service-alliance-web/build/detail.html?id=" + serviceId);
		detailUrl.append("&ns=" + UserContext.getCurrentNamespaceId());
		detailUrl.append("&communityId=" + communityId);
		detailUrl.append("&type=" + type);
		Long userId = UserContext.currentUserId();
		if (null != userId) {
			detailUrl.append("&userId=" + userId);
		}
		detailUrl.append("&supportZoom=1");
		detailUrl.append("#/home/" + pageRealDisplayType);
		detailUrl.append("#sign_suffix");

		return homeUrl + detailUrl.toString();
	}
	
	
	private String getDisplayType(ServiceModuleApp app) {
		
		ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = (ServiceAllianceInstanceConfig) StringHelper
				.fromJsonString(app.getInstanceConfig(), ServiceAllianceInstanceConfig.class);
		if (null == serviceAllianceInstanceConfig) {
			return null;
		}
		
		return serviceAllianceInstanceConfig.getDisplayType();
	}
}
