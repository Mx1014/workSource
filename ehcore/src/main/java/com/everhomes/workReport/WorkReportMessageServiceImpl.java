package com.everhomes.workReport;

import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.workReport.WorkReportDetailsActionData;
import com.everhomes.rest.workReport.WorkReportNotificationTemplateCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class WorkReportMessageServiceImpl implements WorkReportMessageService {

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private WorkReportService workReportService;

    @Autowired
    private WorkReportValProvider workReportValProvider;

    @Autowired
    private MessagingService messagingService;

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
        model.put("reportTime", WorkReportUtil.displayReportTime(report.getReportType(), reportVal.getReportTime().getTime()));
        String content = localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                WorkReportNotificationTemplateCode.POST_WORK_REPORT_VAL,
                locale,
                model,
                "Template Not Found"
        );

        //  2.send it
        sendMessage(content, "新的汇报", receiverId, reportVal);
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
        model.put("reportTime", WorkReportUtil.displayReportTime(report.getReportType(), reportVal.getReportTime().getTime()));
        String content = localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                WorkReportNotificationTemplateCode.UPDATE_WORK_REPORT_VAL,
                locale,
                model,
                "Template Not Found"
        );

        //  2.send it
        sendMessage(content, "汇报更新", receiverId, reportVal);
    }

    /**
     * 根据不同的情况发送消息
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
        String reportTime = WorkReportUtil.displayReportTime(report.getReportType(), reportVal.getReportTime().getTime());

        WorkReportValComment parentComment = workReportValProvider.getWorkReportValCommentById(parentCommentId);
        if (commentatorId.longValue() == applierId.longValue()) {
            if (parentComment != null) {
                if (parentComment.getCreatorUserId().longValue() == applierId.longValue())
                    return;
                else {
                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_1,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_1);
                    sendMessage(content, title, parentComment.getCreatorUserId(), reportVal);
                }
            } else {
                content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_2,
                        commentatorName, applierName, reportName, reportTime);
                title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_2);
                List<WorkReportValReceiverMap> receivers = workReportValProvider.listReportValReceiversByValId(reportVal.getId());
                for (WorkReportValReceiverMap r : receivers)
                    sendMessage(content, title, r.getReceiverUserId(), reportVal);
            }
        } else {
            if (parentComment != null) {
                if (parentComment.getCreatorUserId().longValue() == applierId) {
                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_3,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_3);
                    sendMessage(content, title, applierId, reportVal);
                } else {
                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_4,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_4);
                    sendMessage(content, title, parentComment.getCreatorUserId(), reportVal);

                    content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5,
                            commentatorName, applierName, reportName, reportTime);
                    title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5);
                    sendMessage(content, title, applierId, reportVal);
                }
            } else {
                content = setCommentMsgContent(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5,
                        commentatorName, applierName, reportName, reportTime);
                title = setCommentMsgTitle(WorkReportNotificationTemplateCode.COMMENT_MESSAGE_5);
                sendMessage(content, title, applierId, reportVal);
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

    private void sendMessage(String content, String title, Long receiverId, WorkReportVal reportVal){

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
}
