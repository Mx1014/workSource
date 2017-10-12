// @formatter:off
package com.everhomes.questionnaire;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.questionnaire.QuestionnaireRangeDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author:dengs 2017年4月26日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuestionnaireAsynchronizedAction implements Runnable {
	private static final Logger LOGGER= LoggerFactory.getLogger(QuestionnaireAsynchronizedAction.class);

	@Autowired
	protected ContentServerService contentServerService;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private QuestionnaireProvider questionnaireProvider;

	@Autowired
	private MessagingService messagingService;

	private Long questionnaireId;

	public QuestionnaireAsynchronizedAction(final Long questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	@Override
	public void run(){
		Questionnaire questionnaire = questionnaireProvider.findQuestionnaireById(questionnaireId);
		if(questionnaire == null || questionnaire.getUserScope() == null || questionnaire.getUserScope().length() == 0){
			return ;
		}

		List<QuestionnaireRangeDTO> rangeDTOS = JSONObject.parseObject(questionnaire.getUserScope(),new TypeReference<List<QuestionnaireRangeDTO>>(){});

		rangeDTOS.forEach(r->{
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(
					new MessageChannel(MessageChannelType.USER.getCode(), r.getRange()),
					new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId()))
			);

			messageDto.setBodyType(MessageBodyType.LINK.getCode());
			LinkBody linkBody = new LinkBody();
			linkBody.setActionUrl(contentServerService.parserUri(questionnaire.getPosterUri()));
			linkBody.setContent("hello,你大爷的");
			linkBody.setTitle("hello,nidaye ");
			linkBody.setCoverUrl(contentServerService.parserUri(questionnaire.getPosterUri()));
			String bodyStr = StringHelper.toJsonString(linkBody);

			messageDto.setBody(bodyStr);
			messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

			Map<String, String> meta = new HashMap<String, String>();
			meta.put("popup-flag", "1");
			messageDto.setMeta(meta);

			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
					r.getRange(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
		});
	}

}