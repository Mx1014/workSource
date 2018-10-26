// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.activity.ActivityService;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityEntryConfigulation;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
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
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 10600)
public class OPPushActivityHandler implements OPPushHandler {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LaunchPadService launchPadService;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private RouterInfoService routerService;

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

        ListNearbyActivitiesBySceneCommand listCmd = new ListNearbyActivitiesBySceneCommand();
        OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);

        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());

        Long categoryId = 1L;
        if(app != null && app.getInstanceConfig() != null){
            ActivityEntryConfigulation configulation = (ActivityEntryConfigulation)StringHelper.fromJsonString(app.getInstanceConfig(), ActivityEntryConfigulation.class);
            if(configulation != null){
                categoryId = configulation.getCategoryId();
            }
        }
        listCmd.setCategoryId(categoryId);

        //String scenetoken = launchPadService.getSceneTokenByCommunityId(context.getCommunityId());
        //listCmd.setSceneToken(scenetoken);

        listCmd.setPageSize(config.getNewsSize());
        // 只要查询预告中与进行中的活动
        listCmd.setActivityStatusList(Arrays.asList(NOTSTART.getCode(), UNDERWAY.getCode()));

        ListActivitiesReponse activityReponse = activityService.listOfficialActivitiesByScene(listCmd);

        List<OPPushCard> listCards = new ArrayList<>();
        if(activityReponse != null && activityReponse.getActivities() != null){

            for (ActivityDTO dto: activityReponse.getActivities()){
                OPPushCard card = new OPPushCard();
                ActivityContentRouterJson contentRouterJson = new ActivityContentRouterJson();
                contentRouterJson.setForumId(dto.getForumId());
                contentRouterJson.setTopicId(dto.getPostId());
                RouterInfo routerInfo = routerService.getRouterInfo(10600L, "/detail", contentRouterJson.toString());

                card.setRouterPath(routerInfo.getPath());
                card.setRouterQuery(routerInfo.getQuery());
                card.setClientHandlerType(ClientHandlerType.NATIVE.getCode());


                String host = "activity";
                String router = "zl://" + host + card.getRouterPath() + "?moduleId=10600&clientHandlerType=0&" + card.getRouterQuery();
                card.setRouter(router);

                List<Object> properties = new ArrayList<>();
                properties.add(dto.getPosterUrl());
                properties.add(dto.getSubject());
                properties.add(dto.getStartTime().substring(0, 10));
                card.setProperties(properties);
                listCards.add(card);
            }
        }

        return listCards;
    }

//    @Override
//    public String getInstanceConfig(Object instanceConfig) {
//
//        OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);
//        return StringHelper.toJsonString(config.getAppConfig());
//    }
}
