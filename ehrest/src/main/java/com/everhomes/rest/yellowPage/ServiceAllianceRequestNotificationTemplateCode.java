package com.everhomes.rest.yellowPage;

public interface ServiceAllianceRequestNotificationTemplateCode {

	static final String SCOPE = "serviceAlliance.request.notification";
    
    static final int REQUEST_NOTIFY_ADMIN = 1; // 提交申请通知给管理员
    static final int REQUEST_NOTIFY_ORG = 2; // 提交申请通知给机构
    static final int REQUEST_MAIL_SUBJECT = 3; // 提交申请通知给机构
}
