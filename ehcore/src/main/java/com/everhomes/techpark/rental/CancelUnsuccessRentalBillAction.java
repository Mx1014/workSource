package com.everhomes.techpark.rental;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;  
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.messaging.MessagingService;
import com.everhomes.pushmessage.PushMessageProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.rental.RentalType;
import com.everhomes.rest.techpark.rental.SiteBillStatus;
import com.everhomes.rest.user.MessageChannelType;
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
		// 如果还没成功付全款，则取消订单
		//TODO：加锁
		RentalBill rentalBill = rentalProvider.findRentalBillById(Long.valueOf(rentalBillId));
		if (!rentalBill.getStatus().equals(SiteBillStatus.SUCCESS.getCode()) ) {
			rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
			rentalProvider.updateRentalBill(rentalBill);
			RentalSite site = this.rentalProvider.getRentalSiteById(rentalBill.getRentalSiteId());
			RentalRule rule = this.rentalProvider.getRentalRule(site.getOwnerId(), site.getOwnerType(), site.getSiteType());
			StringBuffer sb = new StringBuffer();
			sb.append("您预定的："); 
			sb.append(site.getSiteName());
			sb.append("(时间:");
			if (rule.getRentalType().equals(RentalType.HOUR)){
				SimpleDateFormat  datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sb.append(datetimeSF.format(rentalBill.getStartTime()));
			}else{
				SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
				sb.append(dateSF.format(rentalBill.getRentalDate()));
			}
			sb.append(")");
			sb.append("由于超期被取消了 > <,请重新预订");
			sendMessageToUser(rentalBill.getRentalUid(),sb.toString());
		}
		
		
	}

}
