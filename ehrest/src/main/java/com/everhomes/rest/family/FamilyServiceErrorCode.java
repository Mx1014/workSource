package com.everhomes.rest.family;

public class FamilyServiceErrorCode {
    public static final String SCOPE = "family";
    
    public static final int ERROR_FAMILY_NOT_EXIST = 10001; //家庭不存在
    public static final int ERROR_USER_NOT_IN_FAMILY = 10002; //用户不在家庭中
    public static final int ERROR_USER_STATUS_INVALID = 10003;//非正常状态，无权限
    public static final int ERROR_USER_REVOKE_SELF = 10004; //不能剔除自己
    public static final int ERROR_USER_REJECT_SELF = 10005; //不能拒绝自己
    public static final int ERROR_USER_FAMILY_EXIST = 10006; //用户已经加入该家庭。
}
