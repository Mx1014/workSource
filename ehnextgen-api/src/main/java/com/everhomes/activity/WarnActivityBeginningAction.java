package com.everhomes.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.activity.ActivityNotificationTemplateCode;
import com.everhomes.rest.activity.ActivityWarningResponse;
import com.everhomes.rest.activity.GetActivityWarningCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarnActivityBeginningAction implements Runnable {
	
	private static final Logger  LOGGER = LoggerFactory.getLogger(WarnActivityBeginningAction.class);

	public static final String QUEUE_NAME = "activityWarning";
	
	private Long activityId;
	
	@Autowired
	private ActivityProivider activityProivider;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
	private UserProvider userProvider;

	@Autowired
	ScheduleProvider scheduleProvider;
	
	public WarnActivityBeginningAction(String id) {
		super();
		this.activityId = Long.valueOf(id);
		LOGGER.debug("创建了一个活动提醒");
	}

	@Override
	public void run() {
		LOGGER.info("WarnActivityBeginningAction start ! activityId=" + activityId);
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			LOGGER.info("WarnActivityBeginningAction run ! activityId=" + activityId);
			LOGGER.debug("活动提醒开始：" + activityId);

			Activity activity = activityProivider.findActivityById(activityId);
			if (activity == null) {
				return;
			}

//		Integer namespaceId = activity.getNamespaceId();
//		WarningSetting warningSetting = warningSettingProvider.findWarningSettingByNamespaceAndType(namespaceId, EhActivities.class.getSimpleName());
//		Long time = 3600*1000L;
//		if (warningSetting != null) {
//			time = warningSetting.getTime();
//		}
			ActivityWarningResponse queryActivityWarningResponse = activityService.queryActivityWarning(new GetActivityWarningCommand(activity.getNamespaceId()));
			String time = (queryActivityWarningResponse.getDays() == null || queryActivityWarningResponse.getDays().intValue() == 0 ? "" : queryActivityWarningResponse.getDays() + "天") + queryActivityWarningResponse.getHours() + "小时";
			List<ActivityRoster> activityRosters = activityProivider.listRosters(activityId);
			String scope = ActivityNotificationTemplateCode.SCOPE;
			int code = ActivityNotificationTemplateCode.ACTIVITY_WARNING_PARTICIPANT;
			String locale = "zh_CN";
			User user = userProvider.findUserById(activity.getCreatorUid());
			if (user != null) {
				locale = user.getLocale();
			}
			Map<String, Object> map = new HashMap<>();
			map.put("tag", activity.getTag());
			map.put("title", activity.getSubject());
			map.put("time", time);
			final String content = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			activityRosters.forEach(r -> {
				if (r.getUid().longValue() != activity.getCreatorUid().longValue()) {
					sendMessageToUser(r.getUid().longValue(), content, null);
					LOGGER.debug("活动提醒——给用户发了消息，userId：" + r.getUid());
				}
			});

			LOGGER.debug("活动提醒结束：" + activityId);
		}
		LOGGER.info("WarnActivityBeginningAction end ! activityId=" + activityId);
	}
	
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_GROUP);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
}
