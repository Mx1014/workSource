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
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
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
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 10800)
public class OPPushNewsHandler implements OPPushHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(OPPushNewsHandler.class);
	
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	
	@Autowired
	NewsService newsService;

    @Override
	public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

		OPPushInstanceConfig config = (OPPushInstanceConfig) StringHelper.fromJsonString(instanceConfig.toString(),
				OPPushInstanceConfig.class);

		ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());
		Long categoryId = StringUtils.isBlank(app.getCustomTag()) ? 0L : Long.parseLong(app.getCustomTag());

		UserContext.current().setAppContext(context);
		ListNewsBySceneCommand cmd = new ListNewsBySceneCommand();
		cmd.setCategoryId(categoryId);
		cmd.setPageSize(config.getNewsSize());
		ListNewsBySceneResponse resp = newsService.listNewsByScene(cmd);

		List<OPPushCard> listCards = new ArrayList<>();
		String host = "news-feed";
		if (null != resp && null != resp.getNewsList()) {

			for (BriefNewsDTO dto : resp.getNewsList()) {
				OPPushCard card = new OPPushCard();
				card.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
				card.setRouterPath("/detail");
				card.setRouterQuery("newsToken=" + dto.getNewsToken()); //native时需要的参数
				String router = 
						"zl://" + host + card.getRouterPath() 
						+ "?moduleId=10800&clientHandlerType="+ card.getClientHandlerType()  //固定参数
						+ "&" + card.getRouterQuery();
				card.setRouter(router); 

				List<Object> properties = new ArrayList<>();
				properties.add(dto.getCoverUri() == null ? "" : dto.getCoverUri());
				properties.add(dto.getTitle() == null ? "" : dto.getTitle());
				properties.add(dto.getTopFlag() == null ? "0" : "" + dto.getTopFlag());
				properties.add(DateUtil.dateToStr(dto.getPublishTime(), "yyyy-MM-dd HH:mm"));
				properties.add(dto.getLikeCount() == null ? "0" : "" + dto.getLikeCount());
				card.setProperties(properties);
				listCards.add(card);
			}
		}

		return listCards;
	}
}
