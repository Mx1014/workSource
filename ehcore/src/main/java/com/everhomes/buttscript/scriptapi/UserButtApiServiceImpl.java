package com.everhomes.buttscript.scriptapi;

import com.everhomes.flow.*;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.common.ThirdPartActionData;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.scriptengine.nashorn.NashornModuleApiService;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserButtApiServiceImpl implements NashornModuleApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserButtApiServiceImpl.class);


    @Autowired
    private UserService userService ;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Override
    public String name() {
        return "userButtService";
    }


    /**
     * 更新会员等级
     */
    public void updateUserVipLevel(Long userId, Integer vipLevel ,String vipLevelText){
        LOGGER.info("the script call the api updateUserVipLevel . userId:{},vipLevel:{},vipLevelText:{}",userId,vipLevel,vipLevelText);
        userService.updateUserVipLevel( userId,  vipLevel ,vipLevelText);

    }

    /**
     * 获取用户认证企业的企业数
     * @param userId
     * @param namespaceId
     * @return
     */
    public int getUserRelateOrganizationCount(Long userId,Integer namespaceId){
        int count = 0 ;
        //获取用户认证的企业
        OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
        List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
        LOGGER.info("organizationList :{}",organizationList);
        if(CollectionUtils.isEmpty(organizationList)){
            LOGGER.info("count :{}",count);
            return count;
        }
        List<OrganizationDTO> list = new ArrayList<OrganizationDTO>();
        //只考虑正常的企业
        for(OrganizationDTO dto :organizationList){
            LOGGER.info("OrganizationDTO :{}",dto);
            if(OrganizationMemberStatus.ACTIVE.getCode() == dto.getMemberStatus()
                    && CommunityType.COMMERCIAL.getCode() == dto.getCommunityType()){
                count = count + 1 ;
            }

        }
        LOGGER.info("count :{}",count);
        return count ;
    }

    public void sendVipLevelMessageToUser(Long userId, String levelName, String thirdUrl) {
        LOGGER.info("the script call the api sendVipLevelMessageToUser, userId= {}, levelName={}",userId,levelName);
        Map<String, String> map = new HashMap<String, String>();
        map.put("levelName", levelName);
        User user = this.userProvider.findUserById(userId);
        String locale = "zh_CN";
        if (user != null) {
            locale = user.getLocale();
            if (levelName.equals(user.getVipLevelText())) {
                LOGGER.info("vip level is the same, can not send message to user, user={}", user);
                return;
            }
        }
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString("ruian.message", 1, locale, map, "您已成为" + levelName +"会员");
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(notifyTextForApplicant);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        messageDto.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(userId)));


        if (!StringUtils.isEmpty(thirdUrl)) {
            ThirdPartActionData actionData = new ThirdPartActionData();
            actionData.setUrl(thirdUrl);
            String url = RouterBuilder.build(Router.BROWSER_THIRD, actionData);
            RouterMetaObject metaObject = new RouterMetaObject();
            metaObject.setUrl(url);
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
            messageDto.setMeta(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(userId),
                messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());

    }

//    public static void main(String[] args) {
//        ThirdPartActionData actionData = new ThirdPartActionData();
//        actionData.setUrl("www.baidu.com");
//        String url = RouterBuilder.build(Router.BROWSER_THIRD, actionData);
//        System.out.println(url);
//    }
    public void testCall() {
        LOGGER.debug("this is test api call");
    }
}