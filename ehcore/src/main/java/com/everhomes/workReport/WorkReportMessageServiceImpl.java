package com.everhomes.workReport;

import com.alibaba.fastjson.JSON;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.workReport.*;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkReportMessageServiceImpl implements WorkReportMessageService {

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private WorkReportService workReportService;

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private WorkReportValProvider workReportValProvider;

    @Autowired
    private WorkReportTimeService workReportTimeService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkReportMessageServiceImpl.class);

    @Override
    public void workReportPostMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user){
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }

        //  1.get the content
        Map<String, String> model = new HashMap<>();
        model.put("applierName", reportVal.getApplierName());
        model.put("reportName", report.getReportName());
        model.put("reportTime", workReportTimeService.displayReportTime(report.getReportType(), reportVal.getReportTime().getTime()));
        String content = localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                WorkReportNotificationTemplateCode.POST_WORK_REPORT_VAL,
                locale,
                model,
                "Template Not Found"
        );

        //  2.send it
        sendDetailsMessage(content, "新的汇报", receiverId, reportVal);
    }

    @Override
    public void workReportUpdateMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user){
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }

        //  1.get the content
        Map<String, String> model = new HashMap<>();
        model.put("applierName", reportVal.getApplierName());
        model.put("reportName", report.getReportName());
        model.put("reportTime", workReportTimeService.displayReportTime(report.getReportType(), reportVal.getReportTime().getTime()));
        String content = localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                WorkReportNotificationTemplateCode.UPDATE_WORK_REPORT_VAL,
                locale,
                model,
                "Template Not Found"
        );

        //  2.send it
        sendDetailsMessage(content, "汇报更新", receiverId, reportVal);
    }

    /**
     * 根据不同的评论情况发送消息
     * 具体规则参考RP
     */
    @Override
    public void workReportCommentMessage(User user, WorkReportVal reportVal, WorkReport report, Long parentCommentId) {
        // 当前提交评论的人id与name
        Long commentatorId = user.getId();
        String commentatorName = workReportService.fixUpUserName(commentatorId, reportVal.getOwnerId());
        // 汇报的提交人id与name
        Long applierId = reportVal.getApplierUserId();
        String applierName = workReportService.fixUpUserName(applierId, reportVal.getOwnerId());

        // 消息正文
        String content;
        // 消息标题
        String title;
        // 汇报名称
        String reportName = report.getReportName();
        // 汇报时间
        String reportTime = workReportTimeService.displayReportTime(report.getReportType(), reportVal.getReportTime().getTime());

        WorkReportValComment parentComment = workReportValProvider.getWorkReportValCommentById(parentCommentId);
        if (commentatorId.longValue() == applierId.longValue()) {
            if (parentComment != null) {
                if (parentComment.getCreatorUserId().longValue() == applierId.longValue())
                    return;
                else {
                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_1,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_1);
                    sendDetailsMessage(content, title, parentComment.getCreatorUserId(), reportVal);
                }
            } else {
                content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_2,
                        commentatorName, applierName, reportName, reportTime);
                title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_2);
                List<WorkReportValReceiverMap> receivers = workReportValProvider.listReportValReceiversByValId(reportVal.getId());
                for (WorkReportValReceiverMap r : receivers)
                    sendDetailsMessage(content, title, r.getReceiverUserId(), reportVal);
            }
        } else {
            if (parentComment != null) {
                if (parentComment.getCreatorUserId().longValue() == applierId) {
                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_3,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_3);
                    sendDetailsMessage(content, title, applierId, reportVal);
                } else {
                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_4,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_4);
                    sendDetailsMessage(content, title, parentComment.getCreatorUserId(), reportVal);

                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5);
                    sendDetailsMessage(content, title, applierId, reportVal);
                }
            } else {
                content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5,
                        commentatorName, applierName, reportName, reportTime);
                title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5);
                sendDetailsMessage(content, title, applierId, reportVal);
            }
        }
    }

    private String setCommentMsgContent(Integer templateCode, String commentatorName, String applierName,
                                        String reportName, String reportTime) {
        // set the message
        Map<String, String> model = new HashMap<>();
        model.put("commentatorName", commentatorName);
        model.put("applierName", applierName);
        model.put("reportName", reportName);
        model.put("reportTime", reportTime);
        return localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                templateCode,
                Locale.SIMPLIFIED_CHINESE.toString(),
                model,
                "Template Not Found"
        );
    }

    private String setCommentMsgTitle(Integer templateCode) {
        String subject = "";
        switch (templateCode) {
            case WorkReportNotificationTemplateCode.COMMENT_MESSAGE_2:
            case WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5:
                subject = "汇报评论";
                break;
            case WorkReportNotificationTemplateCode.COMMENT_MESSAGE_1:
            case WorkReportNotificationTemplateCode.COMMENT_MESSAGE_3:
            case WorkReportNotificationTemplateCode.COMMENT_MESSAGE_4:
                subject = "汇报回复";
                break;
        }
        return subject;
    }

    private void sendDetailsMessage(String content, String title, Long receiverId, WorkReportVal reportVal){

        //  set the route
        WorkReportDetailsActionData actionData = new WorkReportDetailsActionData();
        actionData.setReportId(reportVal.getReportId());
        actionData.setReportValId(reportVal.getId());
        actionData.setOrganizationId(reportVal.getOrganizationId());
        String url = RouterBuilder.build(Router.WORK_REPORT_DETAILS, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);

        // set the message
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, title);
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiverId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    /**
     * 汇总提醒消息发送
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    @Override
    public void workReportRxMessage() {
        if (scheduleProvider.getRunningFlag() != RunningFlag.TRUE.getCode())
            return;
        coordinationProvider.getNamedLock(CoordinationLocks.WORK_REPORT_RX_MSG.getCode()).tryEnter(() -> {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm");
            Timestamp startTime = Timestamp.valueOf(currentTime.minusMinutes(1));
            Timestamp endTime = Timestamp.valueOf(currentTime.plusMinutes(1));
            List<WorkReportValReceiverMsg> results = workReportValProvider.listReportValReceiverMsgByTime(startTime, endTime);
            if (results.size() == 0)
                return;
            LOGGER.info("The work report Rx message task start!");
            Map<Long, List<WorkReportValReceiverMsg>> group1 = results.stream().collect(Collectors.groupingBy(WorkReportValReceiverMsg::getReceiverUserId));
            group1.forEach((k1, v1) -> {
                Map<Long, List<WorkReportValReceiverMsg>> group2 = v1.stream().collect(Collectors.groupingBy(WorkReportValReceiverMsg::getReportId));
                group2.forEach((k2, v2) -> {
                    String content = "截止于" + formatter.format(currentTime)
                            + "，共接收到" + v2.size() + "条"
                            + v2.get(0).getReportName() + "（"
                            + workReportTimeService.displayReportTime(v2.get(0).getReportType(), v2.get(0).getReportTime().getTime()) + "）";
                    sendIndexMessage(content, "汇报情况", "我接收的", v2.get(0).getOrganizationId(), 2L, k1);
                });
            });
        });
        LOGGER.info("The work report Rx message task end!");
    }

    /**
     * 定时清除汇总信息
     * 确保汇总信息表的数据可控，提高后续查询速度
     */
    @Scheduled(cron = "0 0 11 * * ?")
    private void deleteWorkReportRxMessage(){
        workReportValProvider.deleteReportValReceiverMsg();
    }

    /**
     * 员工提交提醒消息发送
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    @Override
    public void workReportAuMessage() {
        if (scheduleProvider.getRunningFlag() != RunningFlag.TRUE.getCode())
            return;
        coordinationProvider.getNamedLock(CoordinationLocks.WORK_REPORT_AU_MSG.getCode()).tryEnter(() -> {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm");
            Timestamp startTime = Timestamp.valueOf(currentTime.minusMinutes(1));
            Timestamp endTime = Timestamp.valueOf(currentTime.plusMinutes(1));
            List<WorkReportScopeMsg> results = workReportProvider.listWorkReportScopeMsgByTime(startTime, endTime);
            if (results.size() == 0)
                return;
            LOGGER.info("The work report Au message task start!");
            results.forEach(r -> {
                String content = "可以提交" + r.getReportName() + "（"
                        + workReportTimeService.displayReportTime(r.getReportType(), r.getReportTime().getTime())
                        + "）了，截止时间为" + workReportTimeService.formatTime(r.getEndTime().toLocalDateTime());
                if (r.getScopeIds() != null) {
                    List<Long> scopeIds = JSON.parseArray(r.getScopeIds(), Long.class);
                    scopeIds.forEach(s -> {
                        sendIndexMessage(content, "汇报填写", "写汇报", r.getOrganizationId(), 0L, s);
                    });
                }
            });
        });
        LOGGER.info("The work report Au message task end!");
    }

    /**
     * 生成员工提交提醒消息
     * 提前生成避免临时计算量过大
     */
    @Scheduled(cron = "0 0 5 * * ?")
    @Override
    public void createWorkReportAuMessage() {
        if (scheduleProvider.getRunningFlag() != RunningFlag.TRUE.getCode())
            return;
        coordinationProvider.getNamedLock(CoordinationLocks.WORK_REPORT_AU_BASIC_MSG.getCode()).tryEnter(() -> {
            List<WorkReport> results = workReportProvider.queryWorkReports(new ListingLocator(), (locator1, query) -> {
                query.addConditions(Tables.EH_WORK_REPORTS.AUTHOR_MSG_TYPE.eq(ReportAuthorMsgType.YES.getCode()));
                query.addConditions(Tables.EH_WORK_REPORTS.STATUS.eq(WorkReportStatus.RUNNING.getCode()));
                return query;
            });
            LocalDateTime time = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            for (WorkReport r : results) {
                //  获取汇报时间
                ReportValiditySettingDTO validitySetting = JSON.parseObject(r.getValiditySetting(), ReportValiditySettingDTO.class);
                ReportMsgSettingDTO auMsgSetting = JSON.parseObject(r.getAuthorMsgSeeting(), ReportMsgSettingDTO.class);
                Timestamp reportTime = workReportTimeService.getReportTime(r.getReportType(), time, validitySetting);
                createWorkReportScopeMsg(r, auMsgSetting, validitySetting, reportTime);
            }
        });
    }

    @Override
    public WorkReportScopeMsg createWorkReportScopeMsg(WorkReport report, ReportMsgSettingDTO auMsgSetting, ReportValiditySettingDTO validitySetting, Timestamp reportTime) {
        //  获取提醒时间
        Timestamp reminderTime = Timestamp.valueOf(workReportTimeService.getSettingTime(report.getReportType(), reportTime.getTime(),
                auMsgSetting.getMsgTimeType(), auMsgSetting.getMsgTimeMark(), auMsgSetting.getMsgTime()));
        Timestamp endTime = Timestamp.valueOf(workReportTimeService.getSettingTime(report.getReportType(), reportTime.getTime(),
                validitySetting.getEndType(), validitySetting.getEndMark(), validitySetting.getEndTime()));
        //  删除已生成提醒数据
        workReportProvider.deleteWorkReportScopeMsgByReportId(report.getId());
        // WorkReportScopeMsg msg = workReportProvider.findWorkReportScopeMsg(report.getId(), workReportTimeService.toSqlDate(reportTime.getTime()));
        WorkReportScopeMsg msg = new WorkReportScopeMsg();
        msg.setNamespaceId(report.getNamespaceId());
        msg.setOrganizationId(report.getOrganizationId());
        msg.setReportId(report.getId());
        msg.setReportName(report.getReportName());
        msg.setReportType(report.getReportType());
        msg.setReportTime(workReportTimeService.toSqlDate(reportTime.getTime()));
        msg.setReminderTime(reminderTime);
        msg.setEndTime(endTime);
        msg.setScopeIds(listScopeIds(report));
        workReportProvider.createWorkReportScopeMsg(msg);
        return msg;
    }

    private String listScopeIds(WorkReport report) {
        List<WorkReportScopeMap> results = workReportProvider.listWorkReportScopesMap(report.getId());
        if(results.size()==0)
            return null;
        Set<Long> scopeIds = new HashSet();
        for(WorkReportScopeMap r : results){
            if(UniongroupTargetType.fromCode(r.getSourceType()) == UniongroupTargetType.MEMBERDETAIL)
                scopeIds.add(r.getSourceId());
            else{
                List<Long> resultIds = organizationProvider.queryOrganizationPersonnelTargetIds(new ListingLocator(), r.getSourceId(), null);
                if(resultIds.size() >0)
                    scopeIds.addAll(resultIds);
            }
        }
        return JSON.toJSONString(new ArrayList<>(scopeIds));
    }

    /**
     * 定时清除汇总信息
     * 确保汇总信息表的数据可控，提高后续查询速度
     */
    @Scheduled(cron = "0 0 12 * * ?")
    private void deleteWorkReportAuMessage(){
        workReportProvider.deleteWorkReportScopeMsg();
    }

    private void sendIndexMessage(String content, String title, String displayName, Long organizationId, Long tabIndex, Long receiverId){
        //  set the route
        WorkReportIndexActionData actionData = new WorkReportIndexActionData();
        actionData.setDisplayName(displayName);
        actionData.setAppId(null);
        actionData.setOrganizationId(organizationId);
        actionData.setTabIndex(tabIndex);
        String url = RouterBuilder.build(Router.WORK_REPORT_INDEX, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);

        // set the message
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, title);
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiverId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }
}
