package com.everhomes.rest.acl;

public interface RoleConstants {
    // system defined roles
    public static final long Anonymous = 1L;
    public static final long SystemAdmin = 2L;
    public static final long AuthenticatedUser = 3L;
    public static final long ResourceCreator = 4L;
    public static final long ResourceAdmin = 5L;
    public static final long ResourceOperator = 6L;
    public static final long ResourceUser = 7L;
    public static final long SystemExtension = 8L;

    /** 机构管理，超级管理员，拥有 所有权限 */
    public static final long ORGANIZATION_ADMIN = 1001L;
    
    
    /** 机构管理: 普通管理员，相对于超级管理员，少了对管理员的操作 */
    public static final long ORGANIZATION_TASK_MGT = 1002L;
    
    /** 机构管理: 财务管理 */
    public static final long ORGANIZATION_ACCOUNTING = 1003L;
    /** 机构管理: 招商管理 */
    public static final long ORGANIZATION_PROERTY_MGT = 1004L;
    
    
    /** 企业管理:企业超级管理员  拥有内部管理的全部权限 */
    public static final long ORGANIZATION_GROUP_MEMBER_MGT = 1005L;
    /** 企业管理:企业普通管理员，相对于超级管理员，少了对管理员的操作 */
    public static final long ORGANIZATION_SERVICE_OPERATOR = 1006L;
    
    
    
    /** 机构管理： 客服管理 */
    public static final long ORGANIZATION_KEFU = 1007L;
}
