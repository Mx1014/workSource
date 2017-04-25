package com.everhomes.rest.yellowPage;

public interface ServiceAllianceRequestNotificationTemplateCode {

	static final String SCOPE = "serviceAlliance.request.notification";
    
    static final int REQUEST_NOTIFY_ADMIN = 1; // 提交申请通知给管理员
    static final int REQUEST_NOTIFY_ORG = 2; // 提交申请通知给机构
    static final int REQUEST_MAIL_SUBJECT = 3; // 提交申请通知给机构
    static final int REQUEST_MAIL_ORG_ADMIN_IN_HTML = 4; // 提交申请通知给机构和管理员，通过html方式，不是文字邮件的方式
    static final int REQUEST_MAIL_TO_PDF = 5; // 邮件内容生成pdf
    
    //add by dengs 20170425 纯文字，放到eh_locale_strings表的
    static final String AN_APPLICATION_FORM = "10001"; // 的申请单
    static final String RESERVE_PEOPLE  = "10002"; // \n预订人:
    static final String PHONE = "10003"; // \n手机号:
    static final String ORGANIZATION_NAME = "10004"; // \n公司名称:
    static final String SERVICE_NAME = "10005"; // \n服务名称:
}
