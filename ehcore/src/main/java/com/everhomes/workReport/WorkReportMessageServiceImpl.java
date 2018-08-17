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

    private void sendMessageAfterEditWorkReportVal(
            String messageType, String applierName, String reportName, Long receiverId, Long reportId, Long reportValId, Long organizationId, User user) {

        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }

        // set the message
        Map<String, String> model = new HashMap<>();
        model.put("applierName", applierName);
        model.put("reportName", reportName);
        String content = "";
        if (messageType.equals("post")) {
            content = localeTemplateService.getLocaleTemplateString(
                    Namespace.DEFAULT_NAMESPACE,
                    WorkReportNotificationTemplateCode.SCOPE,
                    WorkReportNotificationTemplateCode.POST_WORK_REPORT_VAL,
                    locale,
                    model,
                    "Template Not Found"
            );
        } else if (messageType.equals("update")) {
            content = localeTemplateService.getLocaleTemplateString(
                    Namespace.DEFAULT_NAMESPACE,
                    WorkReportNotificationTemplateCode.SCOPE,
                    WorkReportNotificationTemplateCode.UPDATE_WORK_REPORT_VAL,
                    locale,
                    model,
                    "Template Not Found"
            );
        }
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));

        //  set the route
        WorkReportDetailsActionData actionData = new WorkReportDetailsActionData();
        actionData.setReportId(reportId);
        actionData.setReportValId(reportValId);
        actionData.setOrganizationId(organizationId);
        String url = RouterBuilder.build(Router.WORK_REPORT_DETAILS, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        if (messageType.equals("post"))
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, "新的汇报");
        else if (messageType.equals("update"))
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, "汇报更新");
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
