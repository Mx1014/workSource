// @formatter:off
package com.everhomes.family;

public interface FamilyProvideErrorCode {
    static final String SCOPE = "family";

    static final int ERROR_FAMILY_NOT_EXIST = 10001; //家庭不存在
    static final int ERROR_USER_NOT_IN_FAMILY = 10002; //用户不在家庭中
    static final int ERROR_USER_STATUS_INVALID = 10003;//非正常状态，无权限
    static final int ERROR_USER_REVOKE_SELF = 1004; //不能剔除自己
    static final int ERROR_USER_REJECT_SELF = 1005; //不能拒绝自己

}
