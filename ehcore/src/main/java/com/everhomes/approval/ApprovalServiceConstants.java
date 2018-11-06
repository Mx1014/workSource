package com.everhomes.approval;

public class ApprovalServiceConstants {
    public static final String SCOPE = "approval.tip.info";
    public static final int TIP_INFO_FOR_APPROVAL_CATEGORY = 10000; // $假期类型名称$：按$请假单位$请假，最小请假0.5$请假单位$
    public static final int TIP_INFO_FOR_REMAIN_APPROVAL_CATEGORY = 10001; // $假期类型名称$：按$请假单位$请假，最小请假0.5$请假单位$；余额：$余额值$
    public static final int VACATION_BALANCE_INCR_FOR_CANCEL_ASK_FOR_LEAVE = 10003;  // 员工请假取消余额增加
    public static final int VACATION_BALANCE_INCR_FOR_REJECT_ASK_FOR_LEAVE = 10004;  // 员工请假驳回余额增加
    public static final int VACATION_BALANCE_DEC_FOR_ASK_FOR_LEAVE = 10005;  // 员工请假余额扣减
    public static final int VACATION_BALANCE_CHANGED_NOTIFICATION_TITLE = 10006; // 假期余额变动消息提醒标题：假期余额变动
    public static final int VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_ADMIN = 10007; // 假期余额变动消息提醒内容：（触发条件-管理员调整假期余额）
    public static final int VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_REQUEST_CANCELD = 10008; // 假期余额变动消息提醒内容：（触发条件-年假/调休的审批被撤销/终止/删除）
    public static final int VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_REQUEST_SUBMIT = 10009; // 假期余额变动消息提醒内容：（触发条件-请假审批提交）
    public static final int TIME_UNIT_OF_DAY = 1;   // 单位：天
    public static final int TIME_UNIT_OF_HOUR = 2;  // 单位：小时
    public static final int TIME_UNIT_OF_MINUTE = 3;  // 单位：分钟
    public static final String ANNUAL_LEAVE = "年假";
    public static final String OVERTIME_COMPENSATION = "调休";
}
