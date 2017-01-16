package com.everhomes.rest.acl;

import java.util.Arrays;
import java.util.List;

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
    public static final long PM_SUPER_ADMIN = 1001L;
    
    /** 机构管理: 普通管理员，相对于超级管理员，少了对管理员的操作 */
    public static final long PM_ORDINARY_ADMIN = 1002L;
    
    /** 机构管理: 财务管理 */
    public static final long ORGANIZATION_ACCOUNTING = 1003L;
    /** 机构管理: 招商管理 */
    public static final long ORGANIZATION_PROERTY_MGT = 1004L;
    
    
    /** 企业管理:企业超级管理员  拥有内部管理的全部权限 */
    public static final long ENTERPRISE_SUPER_ADMIN = 1005L;
    /** 企业管理:企业普通管理员，相对于超级管理员，少了对管理员的操作 */
    public static final long ENTERPRISE_ORDINARY_ADMIN = 1006L;
    
    
    
    /** 机构管理： 客服管理 */
    public static final long PM_KEFU = 1007L;
    
    /** 设备巡检：设备及其配件的操作 */
    public static final long EQUIPMENT_MANAGER = 1010L;
    
    /** 任务管理：执行人员 */
    public static final long PM_TASK_EXECUTOR = 1017L;
    /** 任务管理：维修人员 */
    public static final long PM_TASK_REPAIRMEN = 1018L;

    /** 黑名单角色 */
    public static final long BLACKLIST = 2000L;
    
    /** 平台定义的物业角色 */
    public static final List<Long> PLATFORM_PM_ROLES = Arrays.asList(
    		PM_SUPER_ADMIN, 
    		PM_ORDINARY_ADMIN,
    		PM_KEFU
    );
    
    /** 平台定义的企业角色 */
    public static final List<Long> PLATFORM_ENTERPRISE_ROLES = Arrays.asList(
    		ENTERPRISE_SUPER_ADMIN, 
    		ENTERPRISE_ORDINARY_ADMIN
    );
}
