// @formatter:off
package com.everhomes.message;

import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.messaging.BlockingEventCommand;
import com.everhomes.rest.messaging.BlockingEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/message")
public class MessageController extends ControllerBase {
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private MessagingService messagingService;
	
	/**
	 * <p>1.推送消息给管理员和业务联系人</p>
	 * <b>URL: /message/pushMessageToAdminAndBusinessContacts</b>
	 */
	@RequestMapping("pushMessageToAdminAndBusinessContacts")
	@RestReturn(String.class)
	public RestResponse pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd){
		messageService.pushMessageToAdminAndBusinessContacts(cmd);
		return new RestResponse();
	}


	@RequestMapping("blockingEvent")
	public DeferredResult<Object> blockingEvent(BlockingEventCommand cmd){
		return messagingService.blockingEvent(cmd.getSubjectId(), cmd.getType(), cmd.getTimeOut(), null);
	}

	@RequestMapping("signalBlockingEvent")
	public String signalBlockingEvent(BlockingEventCommand cmd){
		return messagingService.signalBlockingEvent(cmd.getSubjectId(), cmd.getMessage(), cmd.getTimeOut());
	}

}