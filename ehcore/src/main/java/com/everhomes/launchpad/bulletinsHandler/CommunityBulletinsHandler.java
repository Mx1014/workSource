// @formatter:off
package com.everhomes.launchpad.bulletinsHandler;

import com.everhomes.activity.ActivityService;
import com.everhomes.announcement.AnnouncementService;
import com.everhomes.forum.ForumService;
import com.everhomes.launchpad.BulletinsHandler;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityEntryConfigulation;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.ListAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementResponse;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.TopicPublishStatus;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.BulletinsCard;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.launchpadbase.routerjson.CommunityBulletinsContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.ui.forum.ListNoticeBySceneCommand;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
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
@Component(BulletinsHandler.BULLETINS_HANDLER_TYPE + 10300)
public class CommunityBulletinsHandler implements BulletinsHandler {

    @Autowired
    private ForumService forumService;


    @Autowired
    private RouterInfoService routerService;

    @Autowired
    private AnnouncementService announcementService;


    @Override
    public List<BulletinsCard> listBulletinsCards(Long appId, AppContext context, Integer rowCount) {

        UserContext.current().setAppContext(context);

        ListAnnouncementCommand cmd = new ListAnnouncementCommand();
        cmd.setPageSize(rowCount);
        cmd.setPublishStatus(TopicPublishStatus.PUBLISHED.getCode());
        Long communityId = UserContext.current().getAppContext().getCommunityId();
        cmd.setCommunityId(communityId);
        ListAnnouncementResponse postRest = this.announcementService.listAnnouncement(cmd);
        if(postRest == null || postRest.getAnnouncementDTOs() == null || postRest.getAnnouncementDTOs().size() == 0){
            return null;
        }

        List<BulletinsCard> cards = new ArrayList<>();

        for (AnnouncementDTO dto: postRest.getAnnouncementDTOs()){
            BulletinsCard  card  = new BulletinsCard();
            card.setTitle(dto.getSubject());
            card.setContent(dto.getContent());
            card.setClientHandlerType(ClientHandlerType.NATIVE.getCode());

            CommunityBulletinsContentRouterJson contentRouterJson = new CommunityBulletinsContentRouterJson();
            contentRouterJson.setBulletinId(dto.getId());
            String queryStr = routerService.getQueryInDefaultWay(contentRouterJson.toString());

            card.setRouterPath("/detail");
            card.setRouterQuery(queryStr);

            cards.add(card);
        }

        return cards;
    }

}
