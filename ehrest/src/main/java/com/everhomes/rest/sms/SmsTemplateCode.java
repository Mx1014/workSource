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

    static final String KEY_USERNAME = "userName";
    static final String KEY_USERPHONE = "userPhone";
    static final String KEY_APPLYTIME = "applyTime";
    static final String KEY_APPLYTYPE = "applyType";
    static final String KEY_LOCATION = "location";
    static final String KEY_AREA = "area";
    static final String KEY_ENTERPRISENAME = "enterpriseName"; 

    static final String KEY_PHONE = "phone";
    static final String KEY_TOPICTYPE = "topicType";
    static final String KEY_MSG = "msg";

    static final int VERIFICATION_CODE = 1; // 验证码
    static final int WY_BILL_CODE = 3; //物业账单信息
    static final int ORGANIZATION_ASSIGNED_CODE = 4; //给被分配人员发短信:分配请求服务帖任务给处理员
    static final int WY_SEND_MSG_CODE = 5; // 物业一键推送消息
    static final int PM_TASK_PROCESS_MSG_CODE = 6; // 任务处理消息
    static final int PM_TASK_PUSH_MSG_CODE = 7; // 任务发布消息
    static final int ACLINK_VISITOR_MSG_CODE = 8; // 访客授权链接
    static final int WEIXIN_APPLY_RENEW_CODE = 9; // 威新-看楼申请
    
    int PM_TASK_ASSIGN_CODE = 10; //任务分配消息
    int PM_TASK_CREATOR_CODE = 11;//任务创建消息
    int PM_TASK_ASSIGN_TO_CREATOR_CODE = 15;//任务分配，发送给任务发起者

    int RENTAL_SUCCESS_EXCLUSIVE_CODE = 12;//付费预约成功 独占资源
    int RENTAL_SUCCESS_NOSITENUMBER_CODE = 13;//付费预约成功 非独占资源，不需要选择资源编号
    int RENTAL_SUCCESS_SITENUMBER_CODE = 14;//付费预约成功 非独占资源，需要选择资源编号
}
