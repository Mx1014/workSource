package com.everhomes.rest.acl;

public interface PrivilegeConstants {
    // System defined privileges
    public static final long All = 1L;
    public static final long Visible = 2L;
    public static final long Read = 3L; 
    public static final long Create = 4L;
    public static final long Write = 5L;
    public static final long Delete = 6L;

    // privileges defined by Forum module
    public static final long ForumNewTopic = 100L;
    public static final long ForumDeleteTopic = 101L;
    public static final long ForumNewReply = 102L;
    public static final long ForumDeleteReply = 103L;
    
    // privileges defined by Group module
    public static final long GroupListMember = 151L;
    public static final long GroupAdminOps = 152L;
    public static final long GroupInviteJoin = 153L;
    public static final long GroupUpdateMember = 154L;
    public static final long GroupApproveMember = 155L;
    public static final long GroupRejectMember = 156L;
    public static final long GroupRevokeMember = 157L;
    
    public static final long GroupRequestAdminRole = 161L;
    public static final long GroupInviteAdminRole = 162L;
    
    public static final long GroupPostTopic = 171L;
    
    // privileges defined by Task module
    public static final long TaskAllListPosts = 305L;
    public static final long TaskGuaranteeListPosts = 303L; 
    public static final long TaskSeekHelpListPosts = 304L;
    public static final long TaskAcceptAndRefuse = 306L;
    public static final long TaskProcessingAndGrab = 307L; 
    
    // privileges defined by pm Task2.0 module
    public static final long LISTALLTASK = 904L;
    public static final long LISTUSERTASK = 805L;
    public static final long ASSIGNTASK = 331L;
    public static final long COMPLETETASK = 332L; 
    public static final long CLOSETASK = 333L;
    public static final long REVISITTASK = 920L;
    
    // privileges defined by Notice module
    public static final long NoticeManagementPost = 200L;
    
    // privileges defined by Role module
    public static final long RolePrivilegeList = 606L;
    public static final long RolePrivilegeUpdate = 607L;
    
    // privileges defined by Admin module
    public static final long OrgAdminList = 604L;
    public static final long OrgAdminUpdate = 605L;
    
    public static final long AclinkManager = 720L;
    
    public static final long AclinkInnerManager = 725L;
    
    public static final long OfficialActivity = 310L;

    //超级管理员权限
    public static final long ORGANIZATION_SUPER_ADMIN = 10L;

    //公司管理员权限
    public static final long ORGANIZATION_ADMIN = 15L;

    //管理员管理
    public static final long ADMIN_MANAGE = 10043L;

    //业务授权
    public static final long SERVICE_AUTHORIZATION = 10044L;
    
    //11开头是资源预约权限 
    public static final long RENTAL_CHECK = 1101L; 
}
