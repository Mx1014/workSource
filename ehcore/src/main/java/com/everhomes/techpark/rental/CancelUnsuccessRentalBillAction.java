package com.everhomes.techpark.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;  
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.app.AppConstants;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.pushmessage.PushMessageProvider;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelUnsuccessRentalBillAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CancelUnsuccessRentalBillAction.class);
	
	
	private   Long  rentalBillId;
	@Autowired
	private RentalProvider rentalProvider;

     
    

	@Autowired
	private MessagingService messagingService;
	
	public CancelUnsuccessRentalBillAction(final String id) { 
		this.rentalBillId =  Long.valueOf(id) ;
		
	}

	

	private void sendMessageToUser(Long userId, String content) {
		User user = UserContext.current().getUser();
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(user.getId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(user.getId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        LOGGER.debug("messageDTO : ++++ \n "+messageDto);
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}
	
	
	@Override
	public void run() {
		// 如果还没成功付全款，则取消订单
		//TODO：加锁
		RentalBill rentalBill = rentalProvider.findRentalBillById(Long.valueOf(rentalBillId));
		if (!rentalBill.getStatus().equals(SiteBillStatus.SUCCESS.getCode()) ) {
			rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
			rentalProvider.updateRentalBill(rentalBill);
		}
		
		sendMessageToUser(rentalBill.getRentalUid(),"超期未支付全款，您的订单被取消了");
		
	}

}
