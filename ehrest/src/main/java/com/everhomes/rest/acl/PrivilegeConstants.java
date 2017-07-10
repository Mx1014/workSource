package com.everhomes.rest.acl;

public interface PrivilegeConstants {
    // System defined privileges
    long All = 1L;
    long Visible = 2L;
    long Read = 3L; 
    long Create = 4L;
    long Write = 5L;
    long Delete = 6L;

    // privileges defined by Forum module
    long ForumNewTopic = 100L;
    long ForumDeleteTopic = 101L;
    long ForumNewReply = 102L;
    long ForumDeleteReply = 103L;
    
    // privileges defined by Group module
    long GroupListMember = 151L;
    long GroupAdminOps = 152L;
    long GroupInviteJoin = 153L;
    long GroupUpdateMember = 154L;
    long GroupApproveMember = 155L;
    long GroupRejectMember = 156L;
    long GroupRevokeMember = 157L;
    
    long GroupRequestAdminRole = 161L;
    long GroupInviteAdminRole = 162L;
    
    long GroupPostTopic = 171L;
    
    // privileges defined by Task module
    long TaskAllListPosts = 305L;
    long TaskGuaranteeListPosts = 303L; 
    long TaskSeekHelpListPosts = 304L;
    long TaskAcceptAndRefuse = 306L;
    long TaskProcessingAndGrab = 307L; 
    
    // privileges defined by pm Task2.0 module
    long LISTALLTASK = 904L;
    long LISTUSERTASK = 805L;
    long ASSIGNTASK = 331L;
    long COMPLETETASK = 332L; 
    long CLOSETASK = 333L;
    long REVISITTASK = 920L;
    long PM_TASK_MODULE = 10008L;
    long REPLACE_CREATE_TASK = 10138L;

    
    // privileges defined by Notice module
    long NoticeManagementPost = 200L;
    
    // privileges defined by Role module
    long RolePrivilegeList = 606L;
    long RolePrivilegeUpdate = 607L;
    
    // privileges defined by Admin module
    long OrgAdminList = 604L;
    long OrgAdminUpdate = 605L;
    
    long AclinkManager = 720L;
    
    long AclinkInnerManager = 725L;
    
    long OfficialActivity = 310L;

    //超级管理员权限
    long ORGANIZATION_SUPER_ADMIN = 10L;

    //公司管理员权限
    long ORGANIZATION_ADMIN = 15L;

    //管理员管理
    long ADMIN_MANAGE = 10043L;

    //普通管理员管理
    long ENTERPRISE_ADMIN_MANAGE = 10095L;

    //业务授权
    long SERVICE_AUTHORIZATION = 10044L;

    //黑名单话题投票发帖、评论、回复评论
    long BLACKLIST_COMMON_POLLING_POST = 1001L;

    //黑名单活动发帖、评论、回复评论
    long BLACKLIST_ACTIVITY_POST = 1002L;

    //黑名单公告发帖、评论、回复评论
    long BLACKLIST_NOTICE_POST = 1003L;

    //黑名单园区快讯发帖、评论、回复评论
    long BLACKLIST_NEWS = 1004L;

    //黑名单创建俱乐部
    long BLACKLIST_CLUP = 1005L;

    //黑名单物业发帖、评论、回复评论
    long BLACKLIST_PROPERTY_POST = 1006L;

    //黑名单发消息
    long BLACKLIST_SEND_MESSAGE = 1007L;

    //黑名单意见反馈论坛
    long BLACKLIST_FEEDBACK_FORUM = 1008L;


    //11开头是资源预约权限 
    long RENTAL_CHECK = 1101L;

    // ----------- 客户资料管理权限 --------------
    long CUSTOMER_CREATE    = 10127L;// 创建客户
    long CUSTOMER_IMPORT    = 10128L;// 批量导入
    long CUSTOMER_EXPORT    = 10129L;// 批量导出
    long CUSTOMER_STATISTIC = 10130L;// 查看统计信息
    long CUSTOMER_UPDATE    = 10131L;// 修改客户资料
    long CUSTOMER_LIST      = 10132L;// 查看客户资料
    long CUSTOMER_MANAGE    = 10133L;// 管理客户资料
    long CUSTOMER_DELETE    = 10134L;// 删除客户资料


    long DELETE_OHTER_TOPIC      = 10140L;// 普通贴删除权限
    long DELETE_NOTIC_TOPIC    = 10141L;// 公告贴删除权限
    long DELETE_ACTIVITY_TOPIC0    = 10142L;// 非官方活动贴删除权限
    long DELETE_ACTIVITY_TOPIC1    = 10143L;// 官方活动贴删除权限

    long DELETE_OHTER_COMMENT      = 10150L;// 普通评论删除权限
    long DELETE_NOTIC_COMMENT    = 10151L;// 公告评论删除权限
    long DELETE_ACTIVITY_COMMENT0    = 10152L;// 非官方活动评论删除权限
    long DELETE_ACTIVITY_COMMENT1    = 10153L;// 官方活动评论删除权限

    long PUBLISH_NOTICE_TOPIC    = 10155L;// 发布公告
    
    long FEEDBACK_MANAGEMENT    = 10160L;// 举报管理模块权限

    long ALL_SERVICE_MODULE = 10000L;

    long AUTH_RELATION_LIST = 40001L; //授权关系列表
    long AUTH_RELATION_CREATE = 40002L; //创建授权关系
    long AUTH_RELATION_UPDATE = 40003L; //修改授权关系
    long AUTH_RELATION_DELETE = 40004L; //删除授权关系

    long SUPER_ADMIN_LIST = 40007L; //超级管理员列表
    long SUPER_ADMIN_CREATE = 40008L; //创建超级管理员
    long SUPER_ADMIN_UPDATE = 40009L; //修改超级管理员
    long SUPER_ADMIN_DELETE = 40010L; //删除超级管理员

    long ORG_ADMIN_LIST = 40013L; //公司管理员列表
    long ORG_ADMIN_CREATE = 40014L; //创建公司管理员
    long ORG_ADMIN_UPDATE = 40015L; //修改公司管理员
    long ORG_ADMIN_DELETE = 40016L; //删除公司管理员

    long MODULE_ADMIN_LIST = 40019L; //模块管理员列表
    long MODULE_ADMIN_CREATE = 40020L; //创建模块管理员
    long MODULE_ADMIN_UPDATE = 40021L; //修改模块管理员
    long MODULE_ADMIN_DELETE = 40022L; //删除模块管理员

    long MODULE_CONF_RELATION_LIST = 40019L; //模块配置关系列表
    long MODULE_CONF_RELATION_CREATE = 40020L; //创建模块配置关系
    long MODULE_CONF_RELATION_UPDATE = 40021L; //修改模块配置关系
    long MODULE_CONF_RELATION_DELETE = 40022L; //删除模块配置关系
}
