// @formatter:off
package com.everhomes.rest.acl;

public interface PrivilegeServiceErrorCode {
    static final String SCOPE = "privilege";
    
    static final int ERROR_INVALID_PARAMETER = 100011;

    //管理员列表已存在
    static final int ERROR_ADMINISTRATORS_LIST_EXISTS = 100051;

    //权限列表已存在
    static final int ERROR_PERMISSIONS_LIST_EXISTS = 100052;

    //管理员列表不存在
    static final int ERROR_ADMINISTRATORS_LIST_NONEXISTS = 100053;

    //管理员列表不存在
    static final int ERROR_PERMISSIONS_LIST_NONEXISTS = 100054;
}
