package com.everhomes.rentalv2;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.messaging.MessagingService;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.rentalv2.RentalType;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendMessageAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SendMessageAction.class);
	private   Long  userId;
	private   String content;
	@Autowired
	Rentalv2Provider rentalProvider;

	public SendMessageAction(final Long userId, final String content) {
		this.userId = userId ;
		this.content = content ;
	}
	
	@Autowired
	private MessagingService messagingService;

	private void sendMessageToUser(Long userId, String content) {
//		User user = UserContext.current().getUser();
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        LOGGER.debug("messageDTO : ++++ \n "+messageDto);
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}
	@Override
	public void run() {
		// 变成可支付全款
		sendMessageToUser(userId, content);
		 
	}

}
