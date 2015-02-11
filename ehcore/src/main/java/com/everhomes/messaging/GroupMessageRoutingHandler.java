// @formatter:off
package com.everhomes.messaging;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberLocator;
import com.everhomes.group.GroupMemberStatus;
import com.everhomes.group.GroupServiceProvider;
import com.everhomes.user.UserLogin;
import com.everhomes.util.Name;

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
    private GroupServiceProvider groupProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    @Value("${messaging.routing.block.size}")
    private int groupRoutingBlockSize;
    
    @Override
    public boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, Map<String, String> messageMeta,
            String messageBody) {
        return true;
    }

    @Override
    public void routeMessage(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, Map<String, String> messageMeta,
            String messageBody, int deliveryOption) {
        long groupId = Long.parseLong(dstChannelToken);
        
        GroupMemberLocator locator = new GroupMemberLocator(groupId);
        do {
            List<GroupMember> members = this.groupProvider.listGroupMembers(locator, groupRoutingBlockSize);
            
            members.parallelStream().filter((m)-> { 
                return m.getMemberStatus() == GroupMemberStatus.active.ordinal();
            }).forEach((m)->{
                this.messagingService.routeMessage(senderLogin, appId, m.getMemberType(), 
                    String.valueOf(m.getMemberId()), messageMeta, messageBody, deliveryOption);
            });
            
            if(members.size() < groupRoutingBlockSize)
                break;

            // TODO only the first block of group members are for immediate dispatch, remaining blocks
            // needs to be processed through task system
        } while(true);
    }
}
