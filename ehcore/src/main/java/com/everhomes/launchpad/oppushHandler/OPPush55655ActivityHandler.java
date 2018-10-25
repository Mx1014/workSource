// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.activity.ActivityService;
import com.everhomes.activity.ruian.ActivityButtService;
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
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;

/**
 * 活动
 */
@Component
public class OPPush55655ActivityHandler implements OPPushUrlDetailHandler {


    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private RouterInfoService routerService;

    @Autowired
    private ActivityButtService activityButtService ;

    @Override
    public boolean checkUrl(Object instanceConfig) {
        return false;
    }

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

        /*OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);
        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());*/

        ListRuianActivityBySceneReponse res = activityButtService.listActivityRuiAnEntitiesByScene();

        List<OPPushCard> listCards = new ArrayList<>();
        if(res != null && res.getEntities() != null){

            for(ActivityRuianDetail dto :res.getEntities()){
                OPPushCard card = new OPPushCard();
                card.setRouterPath(dto.getPageUrl());
                card.setRouterQuery("");
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

}
