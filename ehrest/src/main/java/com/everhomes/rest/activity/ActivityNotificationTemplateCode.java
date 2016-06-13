package com.everhomes.rest.activity;

public class ActivityNotificationTemplateCode {
    public static final String SCOPE = "activity.notification";
    public static final int ACTIVITY_SIGNUP_TO_CREATOR = 1;        //有人报名了活动，通知活动发起者
    public static final int ACTIVITY_SIGNUP_CANCEL_TO_CREATOR = 2; //李祥涛取消了活动“瑞地自由度”报名
    public static final int ACTIVITY_CREATOR_CONFIRM_TO_USER = 3;    //活动被管理员同意，通知活动报名者
    public static final int ACTIVITY_CREATOR_REJECT_TO_USER = 4;   //活动被管理员拒绝，通知活动报名者
}
