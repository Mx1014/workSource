// @formatter:off
package com.everhomes.message;

import com.everhomes.rest.message.PersistListMessageRecordsCommand;
import com.everhomes.rest.message.PersistMessageRecordCommand;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.statistics.event.StatEventDeviceLogProvider;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController extends ControllerBase {
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private StatEventDeviceLogProvider statEventDeviceLogProvider;

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
	 * @param cmd
	 */
	@RequestMapping("persistMessage")
	@RestReturn(String.class)
	public RestResponse pushMessageToAdminAndBusinessContacts(PersistListMessageRecordsCommand cmd){
		if(cmd.getDtos() != null){
			List<String> dtos = (List<String>)StringHelper.fromJsonString(cmd.getDtos(), List.class);
			List<MessageRecord> records = new ArrayList<>();
			for (String singleString : dtos) {
				PersistMessageRecordCommand singleCmd = (PersistMessageRecordCommand)StringHelper.fromJsonString(singleString, PersistMessageRecordCommand.class);
				MessageRecord record = (MessageRecord)StringHelper.fromJsonString(singleCmd.getMessageRecordDto(), MessageRecord.class);

				//当存在自sessionToken时，使用sessionToken解析接收者
				if(StringUtils.isNotEmpty(singleCmd.getSessionToken())){
					LoginToken login = WebTokenGenerator.getInstance().fromWebToken(singleCmd.getSessionToken(), LoginToken.class);
					if(login != null){
						record.setDstChannelToken(String.valueOf(login.getUserId()));
						record.setDstChannelType(ChannelType.USER.getCode());
					}
				}

				//当deviceId存在时，使用deviceId解析接收者
				if(StringUtils.isNotEmpty(singleCmd.getDeviceId())){
					Long dstChannelToken = statEventDeviceLogProvider.findUidByDeviceId(singleCmd.getDeviceId());
					if(dstChannelToken != null && dstChannelToken != 0L){
						record.setDstChannelToken(dstChannelToken.toString());
						record.setDstChannelType(ChannelType.USER.getCode());
					}
				}

				records.add(record);
			}

			messageService.persistMessage(records);
		}

		return new RestResponse();
	}

}