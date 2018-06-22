package com.everhomes.rest.activity;

public class ActivityNotificationTemplateCode {
    public static final String SCOPE = "activity.notification";
    public static final int ACTIVITY_SIGNUP_TO_CREATOR = 1;        //有人报名了活动，通知活动发起者
    public static final int ACTIVITY_SIGNUP_CANCEL_TO_CREATOR = 2; //李祥涛取消了活动“瑞地自由度”报名
    public static final int ACTIVITY_CREATOR_CONFIRM_TO_USER = 3;    //活动被管理员同意，通知活动报名者
    public static final int ACTIVITY_CREATOR_REJECT_TO_USER = 4;   //活动被管理员拒绝，通知活动报名者
    public static final int CREATOR_DELETE_ACTIVITY = 5;   //很抱歉通知您：您报名的活动<${tab} 丨 ${title}>因故取消。\n更多活动敬请继续关注。
    public static final int ACTIVITY_WARNING_PARTICIPANT = 6;   //您报名的活动 <${tab} 丨 ${title}> 还有 ${time}就要开始了 >>
    public static final int ACTIVITY_SIGNUP_TO_CREATOR_CONFIRM = 8;    //活动待确认，通知活动创建者进行确认
    public static final int ACTIVITY_SIGNUP_TO_USER_HAVE_CONFIRM = 9;    //活动被管理员同意，并且不需要支付，通知活动报名者 
    public static final int ACTIVITY_CREATOR_CONFIRM_TO_USER_TO_PAY = 10;    //活动被管理员同意，通知活动报名者去支付

    public static final int ACTIVITY_CHANGE_SUBJECT = 11;        //活动发布后，修改标题
    public static final int ACTIVITY_CHANGE_TIME = 12;           //活动发布后，修改时间
    public static final int ACTIVITY_CHANGE_ADDRESS = 13;        //活动发布后，修改地址
    public static final int ACTIVITY_CHANGE_SUBJECT_TIME = 14;   //活动发布后，修改标题和时间
    public static final int ACTIVITY_CHANGE_SUBJECT_ADDRESS = 15;   //活动发布后，修改标题和地址
    public static final int ACTIVITY_CHANGE_TIME_ADDRESS = 16;      //活动发布后，修改时间和地址
    public static final int ACTIVITY_CHANGE_SUBJECT_TIME_ADDRESS = 17;    //活动发布后，修改标题、时间和地点

}
