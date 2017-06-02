// @formatter:off
package com.everhomes.messaging;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.msgbox.MessageLocator;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.user.FetchMessageCommandResponse;
import com.everhomes.rest.user.FetchPastToRecentMessageCommand;
import com.everhomes.rest.user.FetchRecentToPastMessageAdminCommand;
import com.everhomes.rest.user.FetchRecentToPastMessageCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * Implements MessagingService
 * 
 * @author Kelven Yang
 *
 */
@Component
public class MessagingServiceImpl implements MessagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingServiceImpl.class);

    @Autowired
    private List<MessageRoutingHandler> handlers; //TODO 给企业一个新的消息信道？
    private Map<String, MessageRoutingHandler> handlerMap = new ConcurrentHashMap<>();
    
    @Autowired
    private MessageBoxProvider messageBoxProvider;

    @Autowired
    private ConfigurationProvider configProvider;
    
    public MessagingServiceImpl() {
    }
    
    @PostConstruct
    public void setup() {
        if(handlers != null) {
            handlers.forEach((handler) -> {
                Name name = handler.getClass().getAnnotation(Name.class);
                if(name != null && name.value() != null) {
                    registerRoutingHandler(name.value(), handler);
                } else {
                    LOGGER.error("MessageRoutingHandler " + handler.getClass().getName() + " is not properly annotated with @Name");
                }
            });
        }
    }
    
    @Override
    public FetchMessageCommandResponse fetchPastToRecentMessages(FetchPastToRecentMessageCommand cmd) {
        String messageBoxKey = UserMessageRoutingHandler.getMessageBoxKey(UserContext.current().getLogin(), 
                cmd.getNamespaceId() != null ? cmd.getNamespaceId().intValue() : 0, cmd.getAppId());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fetch messages(past to recent), messageBoxKey=" + messageBoxKey + ", cmd=" + cmd);
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getCount());
        boolean removeOld = false;
        if(cmd.getRemoveOld() != null && cmd.getRemoveOld().byteValue() != 0)
            removeOld = true;
        
        FetchMessageCommandResponse response = new FetchMessageCommandResponse();
        
        MessageLocator locator = new MessageLocator(messageBoxKey);
        locator.setAnchor(cmd.getAnchor());
        List<Message> messages = this.messageBoxProvider.getPastToRecentMessages(locator, pageSize + 1, removeOld);
        if(messages.size() > pageSize) {
            messages.remove(messages.size() - 1);
            response.setNextPageAnchor(messages.get(messages.size() - 1).getStoreSequence());
        }
        
        //Added by Janson, ignore the message which channel token is empty.
//        List<MessageDTO> dtoMessages = messages.stream().map((r)-> { return toMessageDto(r); }).collect(Collectors.toList());
        List<MessageDTO> dtoMessages = new ArrayList<MessageDTO>();
        for(Message r : messages) {
            if(r.getChannelToken() == null || r.getChannelToken().isEmpty()) {
                LOGGER.warn("channel is empty!" + r.getStoreSequence());
            } else {
             dtoMessages.add(toMessageDto(r));   
            }
        }
        
        //TODO Fix for anchor. if there are no messages, set back anchor to null
//        if(dtoMessages.size() == 0) {
//            response.setNextPageAnchor(null);
//        }
        
        response.setMessages(dtoMessages);
        return response;
    }

    @Override
    public FetchMessageCommandResponse fetchRecentToPastMessages(FetchRecentToPastMessageCommand cmd) {
        FetchRecentToPastMessageAdminCommand adminCmd = ConvertHelper.convert(cmd, FetchRecentToPastMessageAdminCommand.class);
        return fetchRecentToPastMessagesAny(adminCmd);
    }
    
    @Override
    public FetchMessageCommandResponse fetchRecentToPastMessagesAny(FetchRecentToPastMessageAdminCommand cmd) {
        UserLogin userLogin;
        if(cmd.getLoginId() == null || cmd.getUserId() == null) {
             userLogin = UserContext.current().getLogin();
            cmd.setLoginId(userLogin.getLoginId());
        } else {
            userLogin = new UserLogin(cmd.getNamespaceId(), cmd.getUserId(), cmd.getLoginId(), "", "");
        }
        
        String messageBoxKey = UserMessageRoutingHandler.getMessageBoxKey(userLogin, 
                cmd.getNamespaceId(), cmd.getAppId());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fetch messages(recent to past), messageBoxKey=" + messageBoxKey + ", cmd=" + cmd);
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getCount());
        
        FetchMessageCommandResponse response = new FetchMessageCommandResponse();
        
        MessageLocator locator = new MessageLocator(messageBoxKey);
        locator.setAnchor(cmd.getAnchor());
        List<Message> messages = this.messageBoxProvider.getRecentToPastMessages(locator, pageSize + 1);
        if(messages.size() > pageSize) {
            messages.remove(messages.size() - 1);
            response.setNextPageAnchor(messages.get(messages.size() - 1).getStoreSequence());
        }
        
        List<MessageDTO> dtoMessages = messages.stream().map((r)-> { return toMessageDto(r); }).collect(Collectors.toList());
        response.setMessages(dtoMessages);
        return response;
    }
    
    @Override
    public void routeMessage(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, MessageDTO message, int deliveryOption) {
        this.routeMessage(null, senderLogin, appId, dstChannelType, dstChannelToken, message, deliveryOption);
    }
    
    //直接转换会把Long传成Double
    private List<Long> jsonToLongList(String inStr) {
        //List<Long> includes = (List<Long>)StringHelper.fromJsonString(inStr, (new ArrayList<Long>()).getClass());
        //return includes;
        if(inStr.length() < 2) {
            return null;
        }
        inStr = inStr.substring(1, inStr.length()-1);
        String[] ss = inStr.split(",");
        if(ss.length == 0) {
            return null;
        }
        List<Long> ls = new ArrayList<Long>();
        for(int i = 0; i < ss.length; i++) {
            ls.add(Long.parseLong(ss[i]));
        }
        
        return ls;
    }
    
    @Override
    public void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            MessageDTO message, int deliveryOption) {
        MessageRoutingHandler handler = handlerMap.get(dstChannelType);
        if(handler != null) {
            if(handler.allowToRoute(senderLogin, appId, dstChannelType, dstChannelToken, message)) {
            
                if(null == context) {
                    MessageRoutingContext newCtx = new MessageRoutingContext();
                    String inStr = null;
                    if(null != message.getMeta() && (null != (inStr = message.getMeta().get(MessageMetaConstant.INCLUDE)))) {
                        newCtx.setIncludeUsers(jsonToLongList(inStr));
                        inStr = null;
                    }
                    if(null != message.getMeta() && (null != (inStr = message.getMeta().get(MessageMetaConstant.EXCLUDE)))) {
                        newCtx.setExcludeUsers(jsonToLongList(inStr));
                    }
                    
                    handler.routeMessage(newCtx, senderLogin, appId, dstChannelType, dstChannelToken, message, deliveryOption);
                } else {
                    handler.routeMessage(context, senderLogin, appId, dstChannelType, dstChannelToken, message, deliveryOption);    
                }
                
            } else {
                if(LOGGER.isDebugEnabled())
                    LOGGER.debug(String.format("Message to %s:%s is dropped due to filtering", dstChannelType, dstChannelToken));  
            }
        } else {
            LOGGER.error(String.format("Unable to route message %s:%s", dstChannelType, dstChannelToken));
        }        
    }
    
    @Override
    public long getMessageCountInLoginMessageBox(UserLogin login) {
        
        String messageBoxKey = UserMessageRoutingHandler.getMessageBoxKey(login, 
               login.getNamespaceId(), AppConstants.APPID_MESSAGING);
        
        return this.messageBoxProvider.getBoxMessageCount(messageBoxKey);
    }

    @Override
    public void registerRoutingHandler(String channelType,
            MessageRoutingHandler handler) {
        assert(channelType != null);
        
        handlerMap.put(channelType, handler);
    }

    @Override
    public void unregisterRoutingHandler(String channelType) {
        assert(channelType != null);
        
        handlerMap.remove(channelType);
    }
    
    private MessageDTO toMessageDto(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setAppId(message.getAppId());
        dto.setSenderUid(message.getSenderUid());
        dto.setContextType(message.getContextType());
        dto.setContextToken(message.getContextToken());
        dto.setSenderTag(message.getSenderTag());
        dto.setStoreSequence(message.getStoreSequence());
        dto.setBody(message.getContent());
        dto.setChannels(new MessageChannel(message.getChannelType(), message.getChannelToken()));
        dto.setCreateTime(message.getCreateTime());
        
        String bodyType = message.getMetaParam("bodyType");
        message.getMeta().remove("bodyType");
        dto.setMeta(message.getMeta());
        if(message.getMetaAppId() == null) {
            message.setMetaAppId(0L);
        }
        dto.getMeta().put("metaAppId", message.getMetaAppId().toString());
        dto.setBodyType(bodyType);
        
        return dto;
    }
}
