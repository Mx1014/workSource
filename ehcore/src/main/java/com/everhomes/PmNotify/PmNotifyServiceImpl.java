package com.everhomes.PmNotify;

import com.everhomes.entity.EntityType;
import com.everhomes.equipment.EquipmentInspectionTasks;
import com.everhomes.equipment.EquipmentProvider;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.pmNotify.*;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.User;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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

    private String queueDelay = "pmtaskdelays";
    private String queueNoDelay = "pmtasknodelays";

    private void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueDelay);
        workerPoolFactory.getWorkerPool().addQueue(queueNoDelay);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        setup();
    }

    @Override
    public void pushPmNotifyRecord(PmNotifyRecord record) {

        pmNotifyProvider.createPmNotifyRecord(record);

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
    public void processPmNotifyRecord(PmNotifyRecord record) {
        PmNotifyReceiverList receiverList = (PmNotifyReceiverList) StringHelper.fromJsonString(record.getReceiverJson(), PmNotifyReceiverList.class);
        if(receiverList != null) {
            Set<Long> notifyUsers = resolveUserSelection(receiverList.getReceivers(), record.getOwnerType(), record.getOwnerId());
            String taskName = "";
            Timestamp time = null;
            PmNotifyType notify = PmNotifyType.fromCode(record.getNotifyType());
            if (EntityType.EQUIPMENT_TASK.getCode().equals(record.getOwnerType())) {
                EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(record.getId());
                taskName = task.getTaskName();
                if(PmNotifyType.BEFORE_START.equals(notify)) {
                    time = task.getExecutiveStartTime();
                } else if(PmNotifyType.BEFORE_DELAY.equals(notify)){
                    time = task.getExecutiveExpireTime();
                }
            }
            for (Long userId : notifyUsers) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("processPmNotifyRecord, userId={}, recordId={}", userId, record.getId());
                }
                PmNotifyMode notifyMode = PmNotifyMode.fromCode(record.getNotifyMode());
                switch (notifyMode) {
                    case MESSAGE:
                        sendMessage(userId, taskName, time, notify);
                        break;
                    case SMS:
//                        sndSMS(userId, record);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void sendMessage(Long userId, String taskName, Timestamp time, PmNotifyType type) {

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
        Set<Long> userIds = new HashSet<>();
        receivers.forEach(receiver -> {
            PmNotifyReceiverType receiverType = PmNotifyReceiverType.fromCode(receiver.getReceiverType());
            switch(receiverType) {
                case EXECUTOR:
                    if(EntityType.EQUIPMENT_TASK.getCode().equals(ownerType)) {
                        Set<Long> ids = equipmentService.getTaskGroupUsers(ownerId, QualityGroupType.EXECUTIVE_GROUP.getCode());
                        if(ids != null && ids.size() > 0) {
                            userIds.addAll(ids);
                        }
                    }
                    break;
                case REVIEWER:
                    if(EntityType.EQUIPMENT_TASK.getCode().equals(ownerType)) {
                        Set<Long> ids = equipmentService.getTaskGroupUsers(ownerId, QualityGroupType.REVIEW_GROUP.getCode());
                        if(ids != null && ids.size() > 0) {
                            userIds.addAll(ids);
                        }
                    }
                    break;
                case ORGANIZATION:
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
                    userIds.addAll(receiver.getReceiverIds());
                    break;
                default:
                    break;
            }
        });
        return userIds;
    }
}
