package com.everhomes.welfare;

/**
 * Created by wuhan on 2018/6/11.
 */
public class WelfareConstants {

    static final String SCOPE = "welfare";

//    static final int ERROR_CANNOT_DELETE = 10000;  //不能删除
    static final int ERROR_WELFARE_SENDED = 10001;  //已发放
//    static final int ERROR_CANNOT_DRAFT = 10002;  //不能草稿
static final int ERROR_WELFARE_NOT_FOUND = 10003;  //找不到该福利
    static final int ERROR_PRIVILEGE = 999;  //权限不足

    public static final String SEND_NOTIFICATION_SCOPE = "welfare.msg";
    public static final int SEND_NOTIFICATION_CODE = 1 ;
}
