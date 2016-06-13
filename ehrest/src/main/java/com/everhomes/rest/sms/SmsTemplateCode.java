// @formatter:off
package com.everhomes.rest.sms;

public interface SmsTemplateCode {
    static final String SCOPE = "sms.default";

    static final String YZX_SUFFIX = "yzx";
    static final String SCOPE_YZX = SCOPE + "." + YZX_SUFFIX;

    static final String KEY_VCODE = "vcode";
    
    static final String KEY_YEAR = "year";
    static final String KEY_MONTH = "month";
    static final String KEY_DUEAMOUNT = "dueAmount";
    static final String KEY_OWEAMOUNT = "oweAmount";
    static final String KEY_PAYAMOUNT = "payAmount";
    static final String KEY_BALANCE = "balance";
    static final String KEY_DESCRIPTION = "description";
    

    static final String KEY_PHONE = "phone";
    static final String KEY_TOPICTYPE = "topicType";
    static final String KEY_MSG = "msg";

    static final int VERIFICATION_CODE = 1; // 验证码
    static final int WY_BILL_CODE = 3; //物业账单信息
    static final int ORGANIZATION_ASSIGNED_CODE = 4; //给被分配人员发短信:分配请求服务帖任务给处理员
    static final int WY_SEND_MSG_CODE = 5; // 物业一键推送消息
    static final int PM_TASK_PROCESS_MSG_CODE = 6; // 任务处理消息
    static final int PM_TASK_PUSH_MSG_CODE = 7; // 任务发布消息
}
