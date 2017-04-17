// @formatter:off
package com.everhomes.messaging;

import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.user.UserMuteNotificationFlag;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserNotificationSetting;
import com.everhomes.user.UserProvider;
import com.everhomes.util.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Routing handler to dispatch group messages
 * 
 * @author Kelven Yang
 *
 */
@Component
@Name("group")
public class GroupMessageRoutingHandler implements MessageRoutingHandler {   
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserProvider userProvider;
    
    @Value("${messaging.routing.block.size}")
    private int groupRoutingBlockSize;
    
    private static final Map<String, String> entityTypeToChannelType = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                private static final long serialVersionUID = 1409549585863553699L;

            {
                put(EntityType.USER.getCode(), "user");
                put(EntityType.GROUP.getCode(), "group");
            }});
    
    @Override
    public boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, MessageDTO message) {
        return true;
    }

    @Override
    public void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, MessageDTO message, int deliveryOption) {
        long groupId = Long.parseLong(dstChannelToken);
        
        //TODO do better hear, now only use include list to route message.
        // Should check if the user is a member of group, Or if the id is not user id but group member id.
        if(context.getIncludeUsers() != null) {
            for(int i = 0; i < context.getIncludeUsers().size(); i++) {
                Long l = context.getIncludeUsers().get(i);
                this.messagingService.routeMessage(context, senderLogin, appId, entityTypeToChannelType.get(EntityType.USER.getCode()), 
                        String.valueOf(l), message, deliveryOption);  
                }
            return;
        }
        
        ListingLocator locator = new ListingLocator(groupId);
        do {
            List<GroupMember> members = this.groupService.listMessageGroupMembers(locator, groupRoutingBlockSize);
            
            members.parallelStream()
            //.filter((m)-> { 
            //    return m.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode();
                //})
            .forEach((m)->{
                String channelType = entityTypeToChannelType.get(m.getMemberType());
                if(channelType != null) {

                    // 判断group的成员是否设置了推送免打扰     add by xq.tian 2017/04/17
                    UserNotificationSetting notificationSetting = userProvider.findUserNotificationSetting(
                            com.everhomes.rest.common.EntityType.USER.getCode(),
                            m.getMemberId(),
                            com.everhomes.rest.common.EntityType.GROUP.getCode(),
                            groupId
                    );
                    int newDeliveryOption = deliveryOption;
                    if (notificationSetting != null) {
                        UserMuteNotificationFlag flag = UserMuteNotificationFlag.fromCode(notificationSetting.getMuteFlag());
                        if (flag == UserMuteNotificationFlag.MUTE) {
                            newDeliveryOption &= ~MessagingConstants.MSG_FLAG_PUSH_ENABLED.getCode();
                        }
                    }

                    this.messagingService.routeMessage(context, senderLogin, appId, channelType, 
                           String.valueOf(m.getMemberId()), message, newDeliveryOption);
                }
            });
            
            if(members.size() < groupRoutingBlockSize)
                break;

            // TODO only the first block of group members are for immediate dispatch, remaining blocks
            // needs to be processed through task system
        } while(true);
    }
}
