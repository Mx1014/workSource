// @formatter:off
package com.everhomes.message;

import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;

@RestController
@RequestMapping("/message")
public class MessageController extends ControllerBase {
	
	@Autowired
	private MessageService messageService;
	
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

	/**
	 * <p>消息持久化</p>
	 * <b>URL: /message/persistMessage</b>
	 */
	@RequestMapping("persistMessage")
	@RestReturn(String.class)
	public RestResponse pushMessageToAdminAndBusinessContacts(String record){
		messageService.persistMessage((MessageRecord)StringHelper.fromJsonString(record, MessageRecord.class));
		return new RestResponse();
	}

}