package com.everhomes.messaging;

import com.everhomes.address.AddressMessage;
import com.everhomes.address.AddressMessageProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Name;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;

@Component
@Name("address")
public class AddressMessageRoutingHandler implements MessageRoutingHandler, LocalBusOneshotSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressMessageRoutingHandler.class);
    
    @Autowired 
    private GroupProvider groupProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private AddressMessageProvider addressMessageProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @PostConstruct
    void setup() {
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhGroupMembers.class, null), this);
        }
    
    @Override
    public boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, MessageDTO message) {
        return true;
    }
    
    @Override
    public void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, MessageDTO message, int deliveryOption) {
        long addressId = Long.parseLong(dstChannelToken);
        boolean isOk = false;
        Family f = familyProvider.findFamilyByAddressId(addressId);
        if(null != f) {
            ListingLocator locator = new ListingLocator(f.getId().longValue());
            List<GroupMember> members = this.groupProvider.listGroupMembers(locator, 1);
            if(members.size() > 0) {
              //members in this address, route it as like group
                for(MessageChannel mc: message.getChannels()) {
                    if(mc.getChannelType().equals(MessageChannelType.ADDRESS.getCode())) {
                        //Only one hear
                        mc.setChannelType(MessageChannelType.GROUP.getCode());     
                        mc.setChannelToken(Long.toString(f.getId())); 
                        break;
                        }
                    }
                this.messagingService.routeMessage(context, senderLogin, appId, MessageChannelType.GROUP.getCode(), dstChannelToken, message, deliveryOption);
                isOk = true;
                }
            }
        if(!isOk) {
            AddressMessage adMessage = new AddressMessage();
            adMessage.setAddressId(addressId);
            adMessage.setBody(message.getBody());
            adMessage.setBodyType(message.getBodyType());
            adMessage.setAppid(message.getAppId());
            adMessage.setSenderTag(message.getSenderTag());
            adMessage.setSenderToken(new Long(senderLogin.getUserId()).toString());
            adMessage.setSenderType(MessageChannelType.USER.getCode());
            adMessage.setMeta(StringHelper.toJsonString(message));  //Save all to meta
            adMessage.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            adMessage.setSenderIdentify(senderLogin.getDeviceIdentifier());
            adMessage.setSenderLoginId(senderLogin.getLoginId());
            adMessage.setDeliveryoption(deliveryOption);
            //adMessage.setDestToken(dstChannelToken);
            //adMessage.setDestType(dstChannelType); TODO remove it, NOT used hear
            addressMessageProvider.CreateAddressMessage(adMessage);
        }
    }

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
        try {
            Long groupMemberId = (Long)arg2;
            GroupMember groupMember = this.groupProvider.findGroupMemberById(groupMemberId);
            if(null == groupMember) {
                LOGGER.error("None of groupMember");
                return Action.none;
                }
            
            if(groupMember.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode()) {
                return Action.none;
                }
            
            ListingLocator locator = new ListingLocator();
            List<AddressMessage> messages = addressMessageProvider.findMessageByAddressId(groupMember.getGroupId(), locator, 100);
            if(null != messages && messages.size() > 0) {
                String appVersion = UserContext.current().getVersion();
                for(AddressMessage adMsg : messages) {
                    UserLogin userLogin = new UserLogin(Namespace.DEFAULT_NAMESPACE
                            , Long.parseLong(adMsg.getSenderToken())
                            , adMsg.getSenderLoginId()
                            , adMsg.getSenderIdentify(), null, appVersion);
                    MessageDTO msg = (MessageDTO) StringHelper.fromJsonString(adMsg.getMeta(), MessageDTO.class);
                    if(null != msg) {
                        for(MessageChannel mc: msg.getChannels()) {
                            if(mc.getChannelType().equals(MessageChannelType.ADDRESS.getCode())) {
                                mc.setChannelToken(groupMember.getGroupId().toString());
                                mc.setChannelType(MessageChannelType.GROUP.getCode());
                                break;
                                    }
                                }
                        
                        messagingService.routeMessage(userLogin
                                , adMsg.getAppid()
                                , MessageChannelType.GROUP.getCode()
                                , Long.toString(groupMember.getGroupId())
                                , msg
                                , adMsg.getDeliveryoption());
                            }
                }
                //route it.
                //groupMember.getMemberId();
                }
        } catch(Exception e) {
            LOGGER.error("onLocalBusMessage error " + e.getMessage());
            }

        return Action.none;
    }

    @Override
    public void onLocalBusListeningTimeout() {
        LOGGER.error("onLocalBusListeningTimeout");
    }
}
