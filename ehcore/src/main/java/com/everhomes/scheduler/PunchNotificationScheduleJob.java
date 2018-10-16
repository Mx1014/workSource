package com.everhomes.scheduler;

import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.techpark.punch.PunchNotificationActionData;
import com.everhomes.rest.techpark.punch.PunchType;
import com.everhomes.techpark.punch.PunchConstants;
import com.everhomes.techpark.punch.PunchNotification;
import com.everhomes.techpark.punch.PunchNotificationCommand;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.techpark.punch.QueryPunchNotificationCondition;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 打卡提醒动态调度任务
 */
@Component
public class PunchNotificationScheduleJob extends QuartzJobBean {
    public static final String PUNCH_NOTIFICATION_SCHEDULE = "punch-notification-";

    @Autowired
    private PunchProvider punchProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    private MessagingService messagingService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            PunchNotificationCommand cmd = new PunchNotificationCommand(jobExecutionContext.getMergedJobDataMap());
            cmd.setRemindTimeBetweenFrom(new Timestamp(DateHelper.currentGMTTime().getTime() - 5 * 60 * 1000));
            cmd.setRemindTimeBetweenTo(new Timestamp(DateHelper.currentGMTTime().getTime()));
            punchNotification(cmd);
        }
    }

    private void punchNotification(PunchNotificationCommand cmd) {
        QueryPunchNotificationCondition condition = ConvertHelper.convert(cmd, QueryPunchNotificationCondition.class);
        List<PunchNotification> notifications = punchProvider.findPunchNotificationList(condition);
        if (CollectionUtils.isEmpty(notifications)) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Map<String, Object> model = new HashMap<>();
        model.put("punchType", cmd.getPunchType().getCode());
        int contentCode = PunchConstants.PUNCH_NOTIFICATION_OFF_DUTY_CONTENT;
        if (PunchType.ON_DUTY == cmd.getPunchType()) {
            model.put("onDutyTime", sdf.format(cmd.getFlexOnDuty()));
            contentCode = PunchConstants.PUNCH_NOTIFICATION_ON_DUTY_CONTENT;
        }

        String title = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_NOTIFICATION_SCOPE, PunchConstants.PUNCH_NOTIFICATION_TITLE, PunchConstants.locale, model, "");
        String content = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_NOTIFICATION_SCOPE, contentCode, PunchConstants.locale, model, "");

        for (PunchNotification punchNotification : notifications) {
            sendPunchRemindMessage(punchNotification.getEnterpriseId(), punchNotification.getUserId(), title, content);
        }
        punchProvider.invalidPunchNotificationList(condition);
    }

    private void sendPunchRemindMessage(Long organizationId,Long receiveUserId, String subject, String content) {
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content.toString());
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiveUserId)));

        //  set the route
        RouterMetaObject metaObject = new RouterMetaObject();
        String displayName = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_NOTIFICATION_SCOPE, PunchConstants.PUNCH_NOTIFICATION_DISPLAY_NAME, PunchConstants.locale, new HashMap<>(), "打卡");
        metaObject.setUrl(RouterBuilder.build(Router.ATTENDANCE_PUNCH, new PunchNotificationActionData(organizationId), displayName));
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subject);
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiveUserId),
                message,
                MessagingConstants.MSG_FLAG_STORED_PUSH.getCode()
        );
    }
}
