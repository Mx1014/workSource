package com.everhomes.rest.yellowPage;

public interface ServiceAllianceRequestNotificationTemplateCode {

	static final String SCOPE = "serviceAlliance.request.notification";
    
    static final int REQUEST_NOTIFY_ADMIN = 1; // 提交申请通知给管理员
    static final int REQUEST_NOTIFY_ORG = 2; // 提交申请通知给机构
    static final int REQUEST_MAIL_SUBJECT = 3; // 提交申请通知给机构
    static final int REQUEST_MAIL_ORG_ADMIN_IN_HTML = 4; // 提交申请通知给机构和管理员，通过html方式，不是文字邮件的方式
    static final int REQUEST_MAIL_TO_PDF = 5; // 邮件内容生成pdf
    static final int REQUEST_MAIL_TO_PDF_SUBJECT = 6; // 邮件内容生成pdf的主题
    
    //add by dengs 20170425 纯文字，放到eh_locale_strings表的
    static final String AN_APPLICATION_FORM = "10001"; // 的申请单
    static final String SEE_MAIL_ATTACHMENT = "10002"; // 见邮件附件
    static final String APPLY_STRING = "10003"; // 审批
    static final String ORDER_STRING = "10004"; // 序号
    static final String USERNAME_STRING = "10005"; // 用户姓名
    static final String PHONE_STRING = "10006"; // 手机号码
    static final String COMPANY_STRING = "10007"; // 企业
    static final String SERVICE_ALLIANCE_STRING = "10008"; // 服务机构
    static final String SUBMIT_TIME_STRING= "10009"; // 提交时间
}
