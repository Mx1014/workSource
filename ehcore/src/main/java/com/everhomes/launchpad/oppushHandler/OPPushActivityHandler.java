// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.activity.ActivityService;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityEntryConfigulation;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.launchpadbase.ContextDTO;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.GsonUtil;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;

@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + OPPushHandler.OPPUSHACTIVITY)
public class OPPushActivityHandler implements OPPushHandler {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LaunchPadService launchPadService;

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, ContextDTO context) {

        ListNearbyActivitiesBySceneCommand listCmd = new ListNearbyActivitiesBySceneCommand();
        OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);
        Long categoryId = 1L;
        if(config.getServiceModuleAppInstanceConfig() != null){
            ActivityEntryConfigulation configulation = (ActivityEntryConfigulation)StringHelper.fromJsonString(config.getServiceModuleAppInstanceConfig().toString(), ActivityEntryConfigulation.class);
            if(configulation != null){
                categoryId = configulation.getCategoryId();
            }
        }
        listCmd.setCategoryId(categoryId);

        String scenetoken = launchPadService.getSceneTokenByCommunityId(context.getCommunityId());
        listCmd.setSceneToken(scenetoken);
        listCmd.setPageSize(config.getEntityCount());
        // 只要查询预告中与进行中的活动
        listCmd.setActivityStatusList(Arrays.asList(NOTSTART.getCode(), UNDERWAY.getCode()));

        ListActivitiesReponse activityReponse = activityService.listOfficialActivitiesByScene(listCmd);

        List<OPPushCard> listCards = new ArrayList<>();
        if(activityReponse != null && activityReponse.getActivities() != null){

            for (ActivityDTO dto: activityReponse.getActivities()){
                OPPushCard card = new OPPushCard();
                card.setContentId(dto.getPostId().toString());
                List<Object> properties = new ArrayList<>();
                properties.add(dto.getPosterUrl());
                properties.add(dto.getSubject());
                properties.add(dto.getStartTime());
                card.setProperties(properties);
                listCards.add(card);
            }
        }

        return listCards;
    }

    @Override
    public Long getModuleId() {
        return 10600L;
    }

    @Override
    public Byte getActionType() {
        return 62;
    }

    @Override
    public String getInstanceConfig(Object instanceConfig) {

        OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);
        return config.getServiceModuleAppInstanceConfig().toString();
    }
}
