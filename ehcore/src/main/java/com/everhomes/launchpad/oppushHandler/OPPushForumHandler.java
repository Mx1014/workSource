// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.module.RouterInfoService;
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
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
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
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 10100)
public class OPPushForumHandler implements OPPushHandler{

    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_MINUTES = 60000L;

    @Autowired
    private ForumService forumService;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private RouterInfoService routerInfoService;


    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {
        ListTopicCommand listTopicCommand = new ListTopicCommand();

        OPPushInstanceConfig config = (OPPushInstanceConfig) StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);

        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());

        if(app != null && app.getInstanceConfig() != null){
            ForumEntryConfigulation configulation = (ForumEntryConfigulation)StringHelper.fromJsonString(app.getInstanceConfig(), ForumEntryConfigulation.class);
            if(configulation != null){
                listTopicCommand.setForumEntryId(configulation.getForumEntryId());
                listTopicCommand.setTag(configulation.getTag());
            }
        }

        Forum forum = forumService.findFourmByNamespaceId(UserContext.getCurrentNamespaceId());
        if(forum != null){
            listTopicCommand.setForumId(forum.getId());
        }
        listTopicCommand.setVisibilityScope(VisibilityScope.COMMUNITY.getCode());
        listTopicCommand.setCommunityId(context.getCommunityId());
        listTopicCommand.setPageSize(config.getNewsSize());
        List<Long> excludeCategories = new ArrayList<>();
        excludeCategories.add(1010L);
        listTopicCommand.setExcludeCategories(excludeCategories);

        ListPostCommandResponse response = this.forumService.listTopics(listTopicCommand);

        List<OPPushCard> listCards = new ArrayList<>();

        if (response != null && !CollectionUtils.isEmpty(response.getPosts())) {
            for (PostDTO postDTO : response.getPosts()) {
                OPPushCard card = new OPPushCard();
                ActivityContentRouterJson contentRouterJson = new ActivityContentRouterJson();

                contentRouterJson.setForumId(postDTO.getForumId());
                contentRouterJson.setTopicId(postDTO.getId());

                RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(10100L, app.getId(),"论坛", contentRouterJson.toString(), "/detail",null,null, ClientHandlerType.NATIVE.getCode());

                List<Object> properties = new ArrayList<>();
                String imageUrl = "";
                String router = "";
                if (postDTO.getEmbeddedAppId() != null && postDTO.getEmbeddedAppId().equals(AppConstants.APPID_ACTIVITY)) {
                    if (!StringUtils.isBlank(postDTO.getEmbeddedJson())) {
                        ActivityDTO activityDTO = (ActivityDTO) StringHelper.fromJsonString(postDTO.getEmbeddedJson(), ActivityDTO.class);
                        if (activityDTO != null) {
                            imageUrl = activityDTO.getPosterUrl();
                        }
                    }
                    routerInfo = routerInfoService.getRouterInfo(10600L, "/detail", contentRouterJson.toString());
                    card.setRouterPath(routerInfo.getPath());
                    card.setRouterQuery(routerInfo.getQuery());
                    card.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
                    router = "zl://activity" + card.getRouterPath() + "?moduleId=10600&clientHandlerType=0&" + card.getRouterQuery();
                }else {
                    if (!CollectionUtils.isEmpty(postDTO.getAttachments())) {
                        for (int i=0;i<postDTO.getAttachments().size();i++) {
                            AttachmentDTO attachmentDTO = ConvertHelper.convert(postDTO.getAttachments().get(i), AttachmentDTO.class);
                            if (PostContentType.IMAGE.getCode().toUpperCase().equals(attachmentDTO.getContentType())) {
                                imageUrl = contentServerService.parserUri(attachmentDTO.getContentUri(), EntityType.TOPIC.getCode(), postDTO.getId());
                                break;
                            }
                        }
                    }
                    card.setRouterPath(routerInfo.getPath());
                    card.setRouterQuery(routerInfo.getQuery());
                    card.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
                    router = "zl://post" + card.getRouterPath() + "?" + card.getRouterQuery();
                }
                card.setRouter(router);
                properties.add(imageUrl);
                String tag = "";
                String title = "";
                if (postDTO.getEmbeddedAppId() != null && postDTO.getEmbeddedAppId().equals(AppConstants.APPID_ACTIVITY)) {
                    title = postDTO.getSubject();
                    tag = "活动";
                }else if (postDTO.getCategoryId() != null && postDTO.getCategoryId().equals(CategoryConstants.CATEGORY_ID_TOPIC_POLLING)){
                    title = postDTO.getContent();
                    tag = "投票";
                }else {
                    title = postDTO.getContent();
                    tag = "话题";
                }
                properties.add(title);
                properties.add(tag);
                properties.add(getTimeStr(postDTO.getCreateTime().getTime()));
                card.setProperties(properties);
                listCards.add(card);
            }
        }

        return listCards;
    }

    private String getTimeStr(Long time) {
        String timeStr = "";
        Long interval = DateHelper.currentGMTTime().getTime() - time;
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        boolean thisYear = year.format(new Date()).equals(year.format(new Date(time)));
        boolean today = day.format(new Date()).equals(day.format(new Date(time)));
        if (!thisYear) {
            timeStr = day.format(new Date(time));
        }else if(!today) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            timeStr = sdf.format(new Date(time));
        }else if (ONE_DAY > interval && interval >= ONE_HOUR) {
            SimpleDateFormat hour = new SimpleDateFormat("HH:mm");
            timeStr = hour.format(time);
        }else if (ONE_HOUR > interval && interval >= ONE_MINUTES){
            long minutes = interval / ONE_MINUTES;
            timeStr = minutes+"分钟前";
        }else {
            timeStr = "刚刚";
        }
        return timeStr;
    }
}
