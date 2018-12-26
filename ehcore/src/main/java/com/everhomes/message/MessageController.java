// @formatter:off
package com.everhomes.message;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.MessagingService;
import com.everhomes.pushmessagelog.PushMessageLogService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.message.PersistListMessageRecordsCommand;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.messaging.BlockingEventCommand;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.SearchMessageRecordCommand;
import com.everhomes.rest.messaging.SearchMessageRecordResponse;
import com.everhomes.rest.pushmessagelog.PushStatusCode;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.statistics.event.StatEventDeviceLogProvider;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private StatEventDeviceLogProvider statEventDeviceLogProvider;
	
	@Autowired
   	private PushMessageLogService pushMessageLogService;

	/**
	 * <p>1.推送消息给管理员和业务联系人</p>
	 * <b>URL: /message/pushMessageToAdminAndBusinessContacts</b>
	 */
	@RequestMapping("pushMessageToAdminAndBusinessContacts")
	@RestReturn(String.class)
	public RestResponse pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd){
		messageService.pushMessageToAdminAndBusinessContacts(cmd);
		//更新推送记录状态为完成
        pushMessageLogService.updatePushStatus(cmd.getLogId(), PushStatusCode.FINISH.getCode());
        LOGGER.info("update pushMessageLog status finish .");
		return new RestResponse();
	}


	@RequestMapping("blockingEvent")
	public DeferredResult<RestResponse> blockingEvent(BlockingEventCommand cmd){
		return messagingService.blockingEvent(cmd.getSubjectId(), cmd.getType(), cmd.getTimeOut(), null);
	}

	@RequestMapping("signalBlockingEvent")
	public String signalBlockingEvent(BlockingEventCommand cmd){
		return messagingService.signalBlockingEvent(cmd.getSubjectId(), cmd.getMessage(), cmd.getTimeOut());
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
			List<Map> dtos = (List<Map>)StringHelper.fromJsonString(cmd.getDtos(), List.class);
			List<MessageRecord> records = new ArrayList<>();
			for (Map singleCmd : dtos) {
				MessageRecord record = (MessageRecord)StringHelper.fromJsonString((String)singleCmd.get("messageRecordDto"), MessageRecord.class);

				//当存在自sessionToken时，使用sessionToken解析接收者
				if(singleCmd.get("sessionToken") != null && StringUtils.isNotEmpty(singleCmd.get("sessionToken").toString())){
					LoginToken login = WebTokenGenerator.getInstance().fromWebToken(singleCmd.get("sessionToken").toString(), LoginToken.class);
					if(login != null){
						record.setDstChannelToken(String.valueOf(login.getUserId()));
						record.setDstChannelType(ChannelType.USER.getCode());
					}
				}

				//当deviceId存在时，使用deviceId解析接收者
				if(singleCmd.get("deviceId") != null && StringUtils.isNotEmpty(singleCmd.get("deviceId").toString())){
					Long dstChannelToken = statEventDeviceLogProvider.findUidByDeviceId(singleCmd.get("deviceId").toString());
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

	/**
	 * <p>同步所有消息记录</p>
	 * <b>URL: /message/syncMessageRecord</b>
	 */
	@RequestMapping("syncMessageRecord")
	@RestReturn(String.class)
	public RestResponse syncMessageRecord(){
		messageService.syncMessageRecord();
		return new RestResponse();
	}

	/**
	 * <p>根据条件查询消息记录</p>
	 * <b>URL: /message/searchMessageRecord</b>
	 */
	@RequestMapping("searchMessageRecord")
	@RestReturn(value = SearchMessageRecordResponse.class)
	public RestResponse searchMessageRecord(SearchMessageRecordCommand cmd){
		RestResponse response = new RestResponse(messageService.searchMessageRecord(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>根据条件查询消息记录--byIndexId</p>
	 * <b>URL: /message/searchMessageRecordByIndexId</b>
	 */
	@RequestMapping("searchMessageRecordByIndexId")
	@RestReturn(value = SearchMessageRecordResponse.class)
	public RestResponse searchMessageRecordByIndexId(SearchMessageRecordCommand cmd){
		RestResponse response = new RestResponse(messageService.searchMessageRecordByIndexId(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}