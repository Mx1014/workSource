// @formatter:off
package com.everhomes.launchpad.bulletinsHandler;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.news.NewsService;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.rest.forum.ForumEntryConfigulation;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneResponse;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动
 */
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 10800)
public class OPPushNewsHandler implements OPPushHandler{
	
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	
	@Autowired
	NewsService newsService;

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {
    	
        OPPushInstanceConfig config = (OPPushInstanceConfig) StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);

        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());
        Long categoryId = StringUtils.isBlank(app.getCustomTag()) ? 0L : Long.parseLong(app.getCustomTag());
        
        UserContext.current().setAppContext(context);
        ListNewsBySceneCommand cmd = new ListNewsBySceneCommand();
        cmd.setCategoryId(categoryId);
        cmd.setPageSize(config.getEntityCount());
        ListNewsBySceneResponse resp = newsService.listNewsByScene(cmd);
        
        
        List<OPPushCard> listCards = new ArrayList<>();
        
       if (null != resp && null != resp.getNewsList()) {
    	   
    	   for (BriefNewsDTO dto : resp.getNewsList() ) {
               OPPushCard card = new OPPushCard();
               card.setClientHandlerType(ClientHandlerType.INSIDE_URL.getCode());
               card.setRouterPath("/detail");
               card.setRouterQuery("url:"+dto.getNewsUrl());
               List<Object> properties = new ArrayList<>();
               properties.add(dto.getCoverUri());
               properties.add(dto.getNewsUrl());
               properties.add(dto.getTitle());
               properties.add(DateUtil.dateToStr(dto.getPublishTime(), "yyyy-MM-dd HH:mm"));
               card.setProperties(properties);
               listCards.add(card);
           }
    	   
       }

        return listCards;
    }
}
