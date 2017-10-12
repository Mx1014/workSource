// @formatter:off
package com.everhomes.questionnaire;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.app.AppProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.common.ThirdPartActionData;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.questionnaire.QuestionnaireRangeDTO;
import com.everhomes.rest.questionnaire.QuestionnaireServiceErrorCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RouterBuilder;
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

	@Autowired
	private LocaleStringService stringService;

	@Autowired
	private NamespaceProvider namespaceProvider;

	public QuestionnaireAsynchronizedAction(final String questionnaireId) {
		this.questionnaireId = Long.valueOf(questionnaireId);
	}

	@Override
	public void run(){
		Questionnaire questionnaire = questionnaireProvider.findQuestionnaireById(questionnaireId);
		if(questionnaire == null || questionnaire.getUserScope() == null || questionnaire.getUserScope().length() == 0){
			return ;
		}

		List<QuestionnaireRangeDTO> rangeDTOS = JSONObject.parseObject(questionnaire.getUserScope(),new TypeReference<List<QuestionnaireRangeDTO>>(){});

		Namespace namespace = namespaceProvider.findNamespaceById(questionnaire.getNamespaceId());

		rangeDTOS.forEach(r->{
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(
					new MessageChannel(MessageChannelType.USER.getCode(), r.getRange()),
					new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId()))
			);

			messageDto.setBodyType(MessageBodyType.TEXT.getCode());
			String string1 = stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.UNKNOWN1, "zh_CN", "邀请您参与《");
			String string2 = stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.UNKNOWN2, "zh_CN", "》问卷调查。");
			messageDto.setBody(new StringBuffer().append(namespace.getName()).append(string1).append(questionnaire.getQuestionnaireName()).append(string2).append(questionnaire.getDescription()).toString());
			messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);


			// 组装路由
			OfficialActionData actionData = new OfficialActionData();
			actionData.setUrl("http://www.baidu.com");

        	String url = RouterBuilder.build(Router.BROWSER_I, actionData);
			RouterMetaObject metaObject = new RouterMetaObject();
			metaObject.setUrl(url);

			Map<String, String> meta = new HashMap<String, String>();
			meta.put(MessageMetaConstant.MESSAGE_SUBJECT,stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN, "zh_CN","问卷调查邀请函"));
			meta.put(MessageMetaConstant.META_OBJECT_TYPE,MetaObjectType.MESSAGE_ROUTER.getCode());
			meta.put(MessageMetaConstant.META_OBJECT,StringHelper.toJsonString(metaObject));

			messageDto.setMeta(meta);

			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
					r.getRange(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
		});
	}

}