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
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateRentalBillStatusToPayingFinalAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateRentalBillStatusToPayingFinalAction.class);
	private   Long  rentalBillId;
	@Autowired
	RentalProvider rentalProvider;

	public UpdateRentalBillStatusToPayingFinalAction(final String id) {
		this.rentalBillId =  Long.valueOf(id) ;
	}
	
	@Autowired
	private MessagingService messagingService;

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
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}
	
	@Override
	public void run() {
		// 如果还没成功付全款，则取消订单
		RentalBill rentalBill = rentalProvider.findRentalBillById(Long.valueOf(rentalBillId));
		if ((!rentalBill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) && 
				(!rentalBill.getStatus().equals(SiteBillStatus.SUCCESS.getCode()))) {
			rentalBill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());
			rentalProvider.updateRentalBill(rentalBill);
			//TODO: 发通知
			sendMessageToUser(rentalBill.getRentalUid(),"你有一个预定场所需要付全款");
		}
	}

}
