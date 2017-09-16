package com.everhomes.PmNotify;

import com.everhomes.entity.EntityType;
import com.everhomes.equipment.EquipmentInspectionTasks;
import com.everhomes.equipment.EquipmentProvider;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmNotify.PmNotifyLog;
import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.equipment.EquipmentNotificationTemplateCode;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.pmNotify.*;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by ying.xiong on 2017/9/12.
 */
@Component
public class PmNotifyServiceImpl implements PmNotifyService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmNotifyServiceImpl.class);

    @Autowired
    WorkerPoolFactory workerPoolFactory;

    @Autowired
    JesqueClientFactory jesqueClientFactory;

    @Autowired
    private PmNotifyProvider pmNotifyProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private EquipmentProvider equipmentProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private SmsProvider smsProvider;

    @Autowired
    private UserProvider userProvider;

    private String queueDelay = "pmtaskdelays";
    private String queueNoDelay = "pmtasknodelays";

    private void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueDelay);
        workerPoolFactory.getWorkerPool().addQueue(queueNoDelay);
    }

    @PostConstruct
    public void init() {
        //重启时内存中启的job都会消失 所以把没发消息的job加上
        List<PmNotifyRecord> records = pmNotifyProvider.listUnsendRecords();
        if(records != null && records.size() > 0) {
            records.forEach(record -> {
                pushIntoEnqueue(record);
            });
        }
    }

    private void pushIntoEnqueue(PmNotifyRecord record) {
        if (record.getId() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("pmNotifyRecordId", record.getId());

            if (record.getNotifyTime().getTime() > (System.currentTimeMillis() + 10L)) {
                scheduleProvider.scheduleSimpleJob(
                        queueDelay + record.getId(),
                        queueDelay + record.getId(),
                        new Date(record.getNotifyTime().getTime()),
                        PmNotifytJob.class,
                        map
                );

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("pushPmNotifyRecord delayedEnqueue record = {}", record);
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("pushPmNotifyRecord enqueue record = {}", record);
                }
                scheduleProvider.scheduleSimpleJob(
                        queueNoDelay + record.getId(),
                        queueNoDelay + record.getId(),
                        new Date(System.currentTimeMillis() + 1000),
                        PmNotifytJob.class,
                        map
                );
            }
        } else {
            LOGGER.error("create PmNotifyRecord error! record=" + record.toString());
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        setup();
    }

    @Override
    public void pushPmNotifyRecord(PmNotifyRecord record) {

        pmNotifyProvider.createPmNotifyRecord(record);

        pushIntoEnqueue(record);
    }

    @Override
    public void processPmNotifyRecord(PmNotifyRecord record) {
        PmNotifyReceiverList receiverList = (PmNotifyReceiverList) StringHelper.fromJsonString(record.getReceiverJson(), PmNotifyReceiverList.class);
        LOGGER.info("processPmNotifyRecord receiverList:{}", receiverList);
        if(receiverList != null) {
            Set<Long> notifyUsers = resolveUserSelection(receiverList.getReceivers(), record.getOwnerType(), record.getOwnerId());
            String taskName = "";
            Timestamp time = null;
            int code = 0;
            String scope = "";
            String locale = "zh_CN";
            PmNotifyType notify = PmNotifyType.fromCode(record.getNotifyType());
            if (EntityType.EQUIPMENT_TASK.getCode().equals(record.getOwnerType())) {
                scope = EquipmentNotificationTemplateCode.SCOPE;
                EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(record.getOwnerId());
                taskName = task.getTaskName();
                if(PmNotifyType.BEFORE_START.equals(notify)) {
                    time = task.getExecutiveStartTime();
                    code = EquipmentNotificationTemplateCode.EQUIPMENT_TASK_BEFORE_BEGIN;
                } else if(PmNotifyType.BEFORE_DELAY.equals(notify)){
                    time = task.getExecutiveExpireTime();
                    code = EquipmentNotificationTemplateCode.EQUIPMENT_TASK_BEFORE_DELAY;
                } else if(PmNotifyType.AFTER_DELAY.equals(notify)){
                    time = task.getExecutiveExpireTime();
                    code = EquipmentNotificationTemplateCode.EQUIPMENT_TASK_AFTER_DELAY;
                    //过期提醒的notifytime即为任务的截止时间，所以先关掉任务
                    equipmentProvider.closeTask(task);
                }
            }
            for (Long userId : notifyUsers) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("processPmNotifyRecord, userId={}, recordId={}", userId, record.getId());
                }
                PmNotifyLog log = new PmNotifyLog();
                log.setOwnerType(record.getOwnerType());
                log.setOwnerId(record.getOwnerId());
                log.setReceiverId(userId);
                PmNotifyMode notifyMode = PmNotifyMode.fromCode(record.getNotifyMode());
                String notifyTextForApplicant = getMessage(taskName, time, notify, scope, locale, code);
                switch (notifyMode) {
                    case MESSAGE:
                        sendMessageToUser(userId, notifyTextForApplicant);
                        log.setNotifyText(notifyTextForApplicant);
                        break;
                    case SMS:
                        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
                        List<Tuple<String, Object>> variables = new ArrayList<>();
                        variables.add(new Tuple<String, Object>("taskName", taskName));
                        variables.add(new Tuple<String, Object>("time", timeToStr(time)));
                        smsProvider.sendSms(userIdentifier.getNamespaceId(), userIdentifier.getIdentifierToken(), scope, code, locale, variables);
                        log.setNotifyText(notifyTextForApplicant);
                        break;
                    default:
                        break;
                }
                pmNotifyProvider.createPmNotifyLog(log);
            }
        }

    }

    private String getMessage(String taskName, Timestamp time, PmNotifyType type, String scope, String locale, int code) {
        Map<String, Object> notifyMap = new HashMap<String, Object>();
        notifyMap.put("taskName", taskName);
        notifyMap.put("time", timeToStr(time));
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
        return notifyTextForApplicant;
    }

    private String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    private void sendMessageToUser(Long userId, String content) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    private Set<Long> resolveUserSelection(List<PmNotifyReceiver> receivers, String ownerType, Long ownerId) {
        LOGGER.info("processPmNotifyRecord resolveUserSelection");
        Set<Long> userIds = new HashSet<>();
        receivers.forEach(receiver -> {
            PmNotifyReceiverType receiverType = PmNotifyReceiverType.fromCode(receiver.getReceiverType());
            LOGGER.info("processPmNotifyRecord ReceiverType: {}", receiver.getReceiverType());
            switch(receiverType) {
                case EXECUTOR:
                    LOGGER.info("processPmNotifyRecord ReceiverType: EXECUTOR");
                    if(EntityType.EQUIPMENT_TASK.getCode().equals(ownerType)) {
                        LOGGER.info("processPmNotifyRecord ownerType: EhEquipmentInspectionTasks");
                        Set<Long> ids = equipmentService.getTaskGroupUsers(ownerId, QualityGroupType.EXECUTIVE_GROUP.getCode());
                        if(ids != null && ids.size() > 0) {
                            userIds.addAll(ids);
                        }
                    }
                    break;
                case REVIEWER:
                    LOGGER.info("processPmNotifyRecord ReceiverType: REVIEWER");
                    if(EntityType.EQUIPMENT_TASK.getCode().equals(ownerType)) {
                        LOGGER.info("processPmNotifyRecord ownerType: EhEquipmentInspectionTasks");
                        Set<Long> ids = equipmentService.getTaskGroupUsers(ownerId, QualityGroupType.REVIEW_GROUP.getCode());
                        if(ids != null && ids.size() > 0) {
                            userIds.addAll(ids);
                        }
                    }
                    break;
                case ORGANIZATION:
                    LOGGER.info("processPmNotifyRecord ReceiverType: ORGANIZATION");
                    receiver.getReceiverIds().forEach(receiverId -> {
                        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(receiverId);
                        if(members != null && members.size() > 0) {
                            members.forEach(member -> {
                                userIds.add(member.getTargetId());
                            });

                        }
                    });

                    break;
                case USER:
                    LOGGER.info("processPmNotifyRecord ReceiverType: USER");
                    userIds.addAll(receiver.getReceiverIds());
                    break;
                default:
                    break;
            }
        });
        LOGGER.info("processPmNotifyRecord userIds: {}", StringHelper.toJsonString(userIds));
        return userIds;
    }
}
