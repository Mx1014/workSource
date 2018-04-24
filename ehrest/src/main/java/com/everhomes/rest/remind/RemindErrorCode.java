package com.everhomes.rest.remind;

public class RemindErrorCode {

    public static final String SCOPE = "calendarRemind";

    public static final int REMIND_CATEGORY_NAME_EXIST_ERROR = 10000;   // 分类名称已存在
    public static final int TRACK_REMIND_NOT_EXIST_ERROR = 10001;  // 此日程已被删除
    public static final int REMIND_CANCEL_SHARE_ERROR = 10002;   // 此日程已取消共享
    public static final int REMIND_CATEGORY_DELETE_LAST_ONE_ERROR = 10003; // 最后一个分类不能删除

}