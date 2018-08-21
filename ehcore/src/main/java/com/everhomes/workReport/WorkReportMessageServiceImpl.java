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
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class WorkReportMessageServiceImpl implements WorkReportMessageService {

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void postWorkReportMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user){
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }

        //  1.get the content
        Map<String, String> model = new HashMap<>();
        model.put("applierName", reportVal.getApplierName());
        model.put("reportName", report.getReportName());
        model.put("reportTime", WorkReportUtil.displayReportTime(report.getReportType(), reportVal.getReportTime()));
        String content = localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                WorkReportNotificationTemplateCode.POST_WORK_REPORT_VAL,
                locale,
                model,
                "Template Not Found"
        );

        //  2.get the route
        WorkReportDetailsActionData actionData = new WorkReportDetailsActionData();
        actionData.setReportId(reportVal.getReportId());
        actionData.setReportValId(reportVal.getId());
        actionData.setOrganizationId(reportVal.getOrganizationId());

        //  3.send it
        sendMessage(content, receiverId, actionData, "新的汇报");
    }

    @Override
    public void updateWorkReportMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user){
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }

        //  1.get the content
        Map<String, String> model = new HashMap<>();
        model.put("applierName", reportVal.getApplierName());
        model.put("reportName", report.getReportName());
        model.put("reportTime", WorkReportUtil.displayReportTime(report.getReportType(), reportVal.getReportTime()));
        String content = localeTemplateService.getLocaleTemplateString(
                Namespace.DEFAULT_NAMESPACE,
                WorkReportNotificationTemplateCode.SCOPE,
                WorkReportNotificationTemplateCode.UPDATE_WORK_REPORT_VAL,
                locale,
                model,
                "Template Not Found"
        );

        //  2.get the route
        WorkReportDetailsActionData actionData = new WorkReportDetailsActionData();
        actionData.setReportId(reportVal.getReportId());
        actionData.setReportValId(reportVal.getId());
        actionData.setOrganizationId(reportVal.getOrganizationId());

        //  3.send it
        sendMessage(content, receiverId, actionData, "汇报更新");
    }

    private void sendMessage(String content, Long receiverId, WorkReportDetailsActionData actionData, String title){
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));

        String url = RouterBuilder.build(Router.WORK_REPORT_DETAILS, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
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

    /*private void sendMessageAfterEditWorkReportVal(
            String messageType, String applierName, String reportName, Long receiverId, Long reportId, Long reportValId, Long organizationId, User user) {

        // set the message
        "";
        if (messageType.equals("post")) {
            content =
        } else if (messageType.equals("update")) {

        }
    }*/
}
