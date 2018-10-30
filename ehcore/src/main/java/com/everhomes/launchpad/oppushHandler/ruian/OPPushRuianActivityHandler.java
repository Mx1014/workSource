// @formatter:off
package com.everhomes.launchpad.oppushHandler.ruian;

import com.everhomes.activity.ActivityService;
import com.everhomes.activity.ruian.ActivityButtService;
import com.everhomes.activity.ruian.ActivityButtServiceImpl;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.launchpad.OPPushUrlDetailHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.activity.ruian.ActivityRuianDetail;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.ui.activity.ruian.ListRuianActivityBySceneReponse;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.rest.widget.OPPushUrlInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;

/**
 * 瑞安活动抓取
 */
@Component
public class OPPushRuianActivityHandler implements OPPushUrlDetailHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OPPushRuianActivityHandler.class);

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private ActivityButtService activityButtService ;

    private final String DEFAULT_URL = "https://m.mallcoo.cn/a/custom/10764/xtd/activitylist";
    private final String DEFAULT_MALLID = "10764";

    @Override
    public boolean checkUrl(Object instanceConfig) {
        //客户端传来的url应该长这个样子：
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String url = configProvider.getValue(namespaceId,"mall.ruian.url.activity", DEFAULT_URL) ;
        LOGGER.info("checkUrl　get defaultUrl :{}",url);
        OPPushUrlInstanceConfig config = (OPPushUrlInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushUrlInstanceConfig.class);
        if(config == null){
            LOGGER.info("checkUrl　OPPushUrlInstanceConfig is null");
            return false;
        }
        LOGGER.info("checkUrl　get OPPushUrlInstanceConfig :{}",config.getUrl());
        if(url.equals(config.getUrl())){//检测链接一致则返回true
            return true;
        }
        return false;
    }

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

        OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);
        Integer size  = config.getNewsSize()==null?3:config.getNewsSize();
        ListRuianActivityBySceneReponse res = activityButtService.listActivityRuiAnEntitiesByScene(size);

        List<OPPushCard> listCards = new ArrayList<>();
        if(res != null && res.getEntities() != null){

            for(ActivityRuianDetail dto :res.getEntities()){
                OPPushCard card = new OPPushCard();
                card.setRouterPath("/detail");

                try {
                    card.setRouterQuery("url=" + URLEncoder.encode(dto.getPageUrl(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.warn("url encode, url=", dto.getPageUrl());
                }

                card.setClientHandlerType(ClientHandlerType.OUTSIDE_URL.getCode());

                String host = "default";
                String router = "zl://" + host + card.getRouterPath() + "?moduleId=90100&clientHandlerType=1&appId="+ context.getAppId()+"&" + card.getRouterQuery();
                card.setRouter(router);


                card.setClientHandlerType(ClientHandlerType.OUTSIDE_URL.getCode());
                List<Object> properties = new ArrayList<>();
                properties.add(dto.getPhoto());
                properties.add(dto.getName());
                properties.add(dto.getStartTime().substring(0, 10));
                card.setProperties(properties);
                listCards.add(card);

            }
        }
        return listCards;
    }

    @Override
    public String refreshInstanceConfig( String instanceConfig) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String url = configProvider.getValue(namespaceId,"mall.ruian.url.activity", DEFAULT_URL) ;
        OPPushUrlInstanceConfig config = (OPPushUrlInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushUrlInstanceConfig.class);
        if(config == null){
            LOGGER.info("checkUrl　OPPushUrlInstanceConfig is null");
            return instanceConfig;
        }
        LOGGER.info("checkUrl　get OPPushUrlInstanceConfig :{}",config.getUrl());
        String str = "ruian.mall.id";
        AppContext appContext = UserContext.current().getAppContext();
        Long communityId = null;
        if(appContext != null){
            communityId = appContext.getCommunityId() ;
        }

        if(communityId != null && communityId != 0){
            str = str + "."+ communityId ;
        }

        if(url.equals(config.getUrl())){//检测链接一致则返回true
            String mallId = configProvider.getValue(namespaceId,str, DEFAULT_MALLID) ;
            //https://m.mallcoo.cn/a/custom/10764/xtd/activitylist
            instanceConfig =  instanceConfig.replace("10764",mallId);
            return instanceConfig;
        }
        return instanceConfig;
    }
}
