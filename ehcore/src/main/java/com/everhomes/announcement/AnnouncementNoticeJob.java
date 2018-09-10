// @formatter:off
package com.everhomes.announcement;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.message.MessageService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.announcement.AnnouncementDetailActionData;
import com.everhomes.rest.announcement.AnnouncementLocalStringCode;
import com.everhomes.rest.announcement.AnnouncementNotificationTemplateCode;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.ActivityDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.admin.CommunityUserAddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.RouterBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnouncementNoticeJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementNoticeJob.class);

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityProvider communityProvider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                Integer namespaceId = jobExecutionContext.getMergedJobDataMap().getIntValue("namespaceId");
                List<Long> communityIds = (List<Long>) jobExecutionContext.getMergedJobDataMap().get("communityIds");
                PostDTO postDTO = (PostDTO) jobExecutionContext.getMergedJobDataMap().get("postDTO");
                coordinationProvider.getNamedLock(CoordinationLocks.ANNOUNCEMENT_CREATE_NOTICE_USER.getCode()).tryEnter(() -> {
                    LocalDateTime nowDateTime = LocalDateTime.now();
                    LOGGER.info("AnnouncementNoticeJob has been started at " + nowDateTime);
                    sendMsgToUserWhenCreateAnnouncement(namespaceId, communityIds, postDTO);
                    LOGGER.info("AnnouncementNoticeJob has been ended at " + nowDateTime);
                });
            }
        } catch (Exception e) {
            LOGGER.error("AnnouncementNoticeJob Failed!", e);
        }
    }

    private void sendMsgToUserWhenCreateAnnouncement(Integer namespaceId, List<Long> communityIds, PostDTO postDTO) {
        List<Integer> status = new ArrayList<Integer>();
        status.add(AuthFlag.AUTHENTICATED.getCode());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        AnnouncementDetailActionData actionData = new AnnouncementDetailActionData();
        actionData.setBulletinId(postDTO.getId());
        String url = RouterBuilder.build(Router.BULLETIN_DETAIL, actionData);
        Map map = new HashMap();
        map.put("subject", postDTO.getSubject());
        for (Long communityId : communityIds) {
            Community community = this.communityProvider.findCommunityById(communityId);
            if (community == null)
                return;

            if (CommunityType.COMMERCIAL.getCode() == community.getCommunityType()) {
                ListCommunityUsersCommand command = new ListCommunityUsersCommand();
                do {
                    command.setNamespaceId(namespaceId);
                    command.setCommunityId(communityId);
                    command.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
                    command.setPageSize(10000);
                    CommunityUserResponse res = communityService.listUserCommunitiesV2(command);
                    if (!CollectionUtils.isEmpty(res.getUserCommunities())) {
                        for (CommunityUserDto communityUserDto : res.getUserCommunities()) {
                            User user = this.userProvider.findUserById(communityUserDto.getUserId());
                            String subject = localeStringService.getLocalizedString(AnnouncementLocalStringCode.SCOPE,
                                    String.valueOf(AnnouncementLocalStringCode.ANNOUNCEMENT_MESSAGE),
                                    user.getLocale(),
                                    "Announcement create");
                            Map<String, String> meta = createAnnouncementRouterMeta(url, subject);
                            sendMessageCode(user.getId(), user.getLocale(), map, AnnouncementNotificationTemplateCode.ANNOUNCEMENT_CREATE, meta);
                        }
                    }
                    command.setPageAnchor(res.getNextPageAnchor());
                }while (command.getPageAnchor() != null);
            }else {
                ListCommunityUsersCommand command = new ListCommunityUsersCommand();
                do {
                    command.setNamespaceId(namespaceId);
                    command.setCommunityId(communityId);
                    command.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
                    command.setPageSize(10000);

                    CommunityUserAddressResponse res =  communityService.listUserBycommunityId(command);
                    if (!CollectionUtils.isEmpty(res.getDtos())) {
                        for (CommunityUserAddressDTO communityUserAddressDTO: res.getDtos()) {
                            User user = this.userProvider.findUserById(communityUserAddressDTO.getUserId());
                            String subject = localeStringService.getLocalizedString(AnnouncementLocalStringCode.SCOPE,
                                    String.valueOf(AnnouncementLocalStringCode.ANNOUNCEMENT_MESSAGE),
                                    user.getLocale(),
                                    "Announcement create");
                            Map<String, String> meta = createAnnouncementRouterMeta(url, subject);
                            sendMessageCode(user.getId(), user.getLocale(), map, AnnouncementNotificationTemplateCode.ANNOUNCEMENT_CREATE, meta);
                        }
                    }
                }while (command.getPageAnchor() != null);
            }
        }
    }

    private Map<String, String> createAnnouncementRouterMeta(String url, String subject){
        Map<String, String> meta = new HashMap<String, String>();
        RouterMetaObject routerMetaObject = new RouterMetaObject();
        routerMetaObject.setUrl(url);
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, "message.router");
        meta.put(MessageMetaConstant.META_OBJECT, routerMetaObject.toString());
        if(subject != null){
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subject);
        }
        return meta;
    }
    private void sendMessageCode(Long uid, String locale, Map<String, String> map, int code, Map<String, String> meta) {

        String scope = AnnouncementNotificationTemplateCode.SCOPE;

        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

        sendMessageToUser(uid, notifyTextForOther, meta);
    }

    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
}
