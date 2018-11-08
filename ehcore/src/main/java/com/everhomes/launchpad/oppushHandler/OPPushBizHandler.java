// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.activity.ActivityService;
import com.everhomes.business.BusinessService;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityEntryConfigulation;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.business.ListBusinessPromotionEntitiesCommand;
import com.everhomes.rest.business.ListBusinessPromotionEntitiesReponse;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.GsonUtil;
import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;

/**
 * 微商城
 */
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 92100)
public class OPPushBizHandler implements OPPushHandler {


    @Autowired
    private BusinessService businessService;

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

        ListBusinessPromotionEntitiesCommand cmd = new ListBusinessPromotionEntitiesCommand();
        OPPushInstanceConfig config = (OPPushInstanceConfig)StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);
        cmd.setPageSize(config.getNewsSize());
        ListBusinessPromotionEntitiesReponse listBisz = businessService.listBusinessPromotionEntities(cmd);

        List<OPPushCard> listCards = new ArrayList<>();
        if(listBisz != null && listBisz.getEntities() != null && listBisz.getEntities().size() > 0){

            for(ModulePromotionEntityDTO dto: listBisz.getEntities()){
                OPPushCard card = new OPPushCard();

                card.setClientHandlerType(ClientHandlerType.INSIDE_URL.getCode());
                card.setRouterPath("/detail");

                try {
                    HashMap<String, Object> hashMap = new ObjectMapper().readValue(dto.getMetadata(), HashMap.class);
                    card.setRouterQuery("url=" + URLEncoder.encode(hashMap.get("url").toString(), "UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String host = "default";
                String router = "zl://" + host + card.getRouterPath() + "?moduleId=92100&clientHandlerType=2&" + card.getRouterQuery();
                card.setRouter(router);

                List<Object> properties = new ArrayList<>();
                properties.add(dto.getPosterUrl());
                properties.add(dto.getSubject());
                if(dto.getInfoList() != null && dto.getInfoList().size() > 0){
                    properties.add(dto.getInfoList().get(0).getContent());
                }

                card.setProperties(properties);
                listCards.add(card);
            }
        }

        return listCards;
    }
}
