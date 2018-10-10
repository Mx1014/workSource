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

    long MODULE_CONF_RELATION_LIST = 40023L; //模块配置关系列表
    long MODULE_CONF_RELATION_CREATE = 40024L; //创建模块配置关系
    long MODULE_CONF_RELATION_UPDATE = 40025L; //修改模块配置关系
    long MODULE_CONF_RELATION_DELETE = 40026L; //删除模块配置关系

    // ----------- 物业报修权限 --------------
    long PMTASK_LIST = 30090L;
    long PMTASK_AGENCY_SERVICE = 30091L;
    long PMTASK_SERVICE_CATEGORY_CREATE = 30092L;
    long PMTASK_SERVICE_CATEGORY_DELETE = 30093L;
    long PMTASK_DETAIL_CATEGORY_CREATE = 30094L;
    long PMTASK_DETAIL_CATEGORY_DELETE = 30095L;
    long PMTASK_TASK_STATISTICS_LIST = 30096L;
    long PMTASK_ALL_TASK_STATISTICS_LIST = 30097L;
    
    // ---------------- 门禁权限 -----------------------
    long MODULE_ACLINK_MANAGER = 10041L; //公司门禁 管理员
    long MODULE_ACLINK_VISITOR_DENY = 1009L; //禁止门禁访客访问

    // 能耗权限
    long METER_CREATE = 21000L;
    long METER_BATCHUPDATE = 21001L;
    long METER_IMPORT = 21002L;
    long METER_READ = 21003L;
    long METER_LIST = 21004L;
    long METER_CHANGE = 21005L;
    long METER_INACTIVE = 21006L;
    long METER_READING_SEARCH = 21007L;
    long METER_READING_DELETE = 21008L;
    long ENERGY_MODULE = 49100L;
    long ENERGY_STAT_BY_DAY = 21009L;
    long ENERGY_STAT_BY_MONTH = 21010L;
    long ENERGY_STAT_BY_YEAR = 21011L;
    long ENERGY_STAT_BY_YOY = 21012L;
    long ENERGY_SETTING = 21013L;
    long ENERGY_PLAN_CREATE = 21014L;
    long ENERGY_PLAN_LIST = 21015L;
    long ENERGY_PLAN_DELETE = 21016L;
    //物业缴费权限 by great wentian
    long ASSET_MODULE_ID = 20400L;
    long ASSET_DEAL_VIEW = 204001006L;
    long ASSET_MANAGEMENT_VIEW = 204001001L;
    long ASSET_MANAGEMENT_CREATE = 204001002L;
    long ASSET_MANAGEMENT_NOTICE = 204001003L;
    long ASSET_MANAGEMENT_CHANGE_STATUS = 204001004L;
    long ASSET_MANAGEMENT_MODIFY = 204001007L;
    long ASSET_MANAGEMENT_DELETE = 204001007L;
    long ASSET_MANAGEMENT_TO_SETTLED = 204001008L;//未出批量转已出
    long ASSET_MANAGEMENT_TO_PAID = 204001009L;//未缴批量转已缴
    long ASSET_MANAGEMENT_MODIFY_BILL_SUBITEM = 204001010L;//批量减免
    
    long ASSET_STATISTICS_VIEW = 204001005L;

    // ---------------- 组织架构权限 -----------------------

    //部门
    long CREATE_DEPARTMENT = 41001L;//新增部门
    long MODIFY_DEPARTMENT = 41002L;//修改部门
    long DELETE_DEPARTMENT = 41003L;//删除部门
    long CHANGE_DEPARTMENT_ORDER = 41004L;//修改部门顺序

    //岗位
    long CREATE_JOB_POSITION = 41005L;//创建岗位
    long MODIFY_JOB_POSITION = 41006L;//修改岗位
    long DELETE_JOB_POSITION = 41007L;//删除岗位
    long MODIFY_DEPARTMENT_JOB_POSITION = 41008L;//修改部门岗位

    //人员
    long CREATE_OR_MODIFY_PERSON = 41009L;//创建或修改人员
    long DELETE_PERSON = 41010L;//删除人员
    long BATCH_IMPORT_PERSON = 41011L;//批量导入人员
    long BATCH_EXPORT_PERSON = 41012L;//批量导出人员

//    -------------- 考勤 ------------ 
    long PUNCH_RULE_QUERY_ALL = 42000L;//    查看打卡规则 （细化权限，下同）
    long PUNCH_RULE_QUERY_CREATOR = 42001L;//    查看自己创建的打卡规则
    long PUNCH_RULE_CREATE = 42002L;//    新建打卡规则
    long PUNCH_RULE_UPDATE = 42003L;//    编辑打卡规则
    long PUNCH_RULE_DELETE = 42004L;//    删除打卡规则 
    long PUNCH_STATISTIC_QUERY = 42005L;//    查看打卡记录 （细化权限，下同）
    long PUNCH_STATISTIC_EXPORT = 42006L;//    导出打卡记录

    //设备巡检权限
    long EQUIPMENT_STANDARD_UPDATE = 30067L;  //	设备巡检 标准新增修改权限
    long EQUIPMENT_STANDARD_LIST = 30068L;   //设备巡检 标准查看权限
    long EQUIPMENT_STANDARD_DELETE = 30069L;  //设备巡检 标准删除权限
    long EQUIPMENT_RELATION_LIST = 30082L;    //设备巡检 巡检关联审批查看权限
    long EQUIPMENT_RELATION_REVIEW = 30070L;  //设备巡检 巡检关联审批审核权限
    long EQUIPMENT_RELATION_DELETE = 30071L;  //设备巡检 巡检关联审批删除失效关联权限
    long EQUIPMENT_LIST = 30072L;            //设备巡检 巡检对象查看权限
    long EQUIPMENT_UPDATE = 30073L;        //设备巡检 巡检对象新增修改权限
    long EQUIPMENT_DELETE = 30074L;          //设备巡检 巡检对象删除权限
    long EQUIPMENT_TASK_LIST = 30075L;       //设备巡检 任务查询权限
    long EQUIPMENT_ITEM_LIST = 30076L;        //设备巡检 巡检项查看权限
    long EQUIPMENT_ITEM_CREATE = 30077L;      //设备巡检 巡检项新增权限
    long EQUIPMENT_ITEM_DELETE = 30078L;       //设备巡检 巡检项删除权限
    long EQUIPMENT_ITEM_UPDATE = 30079L;       //设备巡检 巡检项修改权限
    long EQUIPMENT_STAT_PANDECT = 30080L;      //设备巡检 统计总览权限
    long EQUIPMENT_STAT_ALLTASK = 30081L;     //设备巡检 统计查看所有任务权限
    long EQUIPMENT_PLAN_CREATE = 30083L;     //设备巡检 计划创建
    long EQUIPMENT_PLAN_UPDATE = 30084L;     //设备巡检 计划修改
    long EQUIPMENT_PLAN_LIST = 30085L;     //设备巡检  计划查看
    long EQUIPMENT_PLAN_DELETE = 30086L;     //设备巡检 计划删除
    long EQUIPMENT_PLAN_REVIEW = 30087L;     //设备巡检 计划审批




    //客户和合同的权限
    long ENTERPRISE_CUSTOMER_CREATE = 21101L;//客户的增加
    long ENTERPRISE_CUSTOMER_UPDATE = 21102L;//客户的修改
    long ENTERPRISE_CUSTOMER_IMPORT = 21103L;//客户的导入
    long ENTERPRISE_CUSTOMER_EXPORT = 21114L;//客户的导出
    long ENTERPRISE_CUSTOMER_SYNC = 21104L;//客户的同步
    long ENTERPRISE_CUSTOMER_DELETE = 21105L;//客户的删
    long ENTERPRISE_CUSTOMER_LIST = 21106L;//客户的查
    long ENTERPRISE_CUSTOMER_MANAGE_LIST = 21107L;//管理的查
    long ENTERPRISE_CUSTOMER_MANAGE_CREATE = 21108L;//管理的新增
    long ENTERPRISE_CUSTOMER_MANAGE_UPDATE = 21109L;//管理的修改
    long ENTERPRISE_CUSTOMER_MANAGE_DELETE = 21110L;//管理的删
    long ENTERPRISE_CUSTOMER_MANAGE_IMPORT = 21111L;//管理的导入
    long ENTERPRISE_CUSTOMER_MANAGE_EXPORT = 21112L;//管理的导出
    long ENTERPRISE_CUSTOMER_STAT = 21113L;//客户的统计分析查看


    long ENTERPRISE_CUSTOMER_MANNAGER_SET = 21115L;// 查看企业管理员
    long ENTERPRISE_CUSTOMER_CHANGE_APTITUDE = 21116L;// 转为资质客户



    long CONTRACT_MODULE = 21200L; //合同模块id
    long CONTRACT_CREATE = 21201L;//新增合同
    long CONTRACT_LAUNCH = 21202L;//签约 发起审批
    long CONTRACT_UPDATE = 21203L;//修改
    long CONTRACT_DELETE = 21204L;//删除
    long CONTRACT_INVALID = 21205L;//作废
    long CONTRACT_ENTRY = 21206L;//入场
    long CONTRACT_LIST = 21207L;//查看
    long CONTRACT_RENEW = 21208L;//续约
    long CONTRACT_CHANGE = 21209L;//变更
    long CONTRACT_PARAM_LIST = 21210L;//合同参数查看
    long CONTRACT_PARAM_UPDATE = 21211L;//合同参数修改
    long CONTRACT_FLOW = 21212L;//合同工作流
    long CONTRACT_SYNC = 21213L;//从第三方同步合同
    long CONTRACT_DENUNCIATION = 21214L;//退约
    
    long CONTRACT_PREVIEW = 21215L;//打印预览 
    long CONTRACT_PRINT = 21216L;//打印


    long PAYMENT_CONTRACT_CREATE = 21215L;//新增付款合同
    long PAYMENT_CONTRACT_LAUNCH = 21216L;//签约 发起 付款审批
    long PAYMENT_CONTRACT_UPDATE = 21217L;//修改 付款
    long PAYMENT_CONTRACT_DELETE = 21218L;//删除 付款
    long PAYMENT_CONTRACT_INVALID = 21219L;//作废 付款
    long PAYMENT_CONTRACT_LIST = 21220L;//查看 付款
    long PAYMENT_CONTRACT_RENEW = 21221L;//续约 付款
    long PAYMENT_CONTRACT_CHANGE = 21222L;//变更 付款
    long PAYMENT_CONTRACT_DENUNCIATION = 21223L;//退约 付款

//    -------- 付款申请单 --------
    long PAYMENT_APPLICATION_CREATE = 21301L;//创建
    long PAYMENT_APPLICATION_LIST = 21302L;//查看

//    ----------品质核查----------
    long QUALITY_CATEGORY_LIST = 30044L;//品质核查 类型管理查看权限
    long QUALITY_CATEGORY_CREATE = 30045L;//品质核查 类型管理新增权限
    long QUALITY_CATEGORY_DELETE = 30046L;//品质核查 类型管理删除权限
    long QUALITY_CATEGORY_UPDATE = 30047L;//品质核查 类型管理修改权限
    long QUALITY_SPECIFICATION_LIST = 30048L;//品质核查 规范管理查看权限
    long QUALITY_SPECIFICATION_CREATE = 30049L;//品质核查 规范管理新增权限
    long QUALITY_SPECIFICATION_DELETE = 30050L;//品质核查 规范管理删除权限
    long QUALITY_SPECIFICATION_UPDATE = 30051L;//品质核查 规范管理修改权限
    long QUALITY_STANDARD_LIST = 30052L;//品质核查 标准管理查看权限
    long QUALITY_STANDARD_CREATE = 30053L;//品质核查 标准管理新增权限
    long QUALITY_STANDARD_DELETE = 30054L;//品质核查 标准管理删除权限
    long QUALITY_STANDARD_UPDATE = 30055L;//品质核查 标准管理修改权限
    long QUALITY_STANDARDREVIEW_LIST = 30056L;//品质核查 标准审批查看权限
    long QUALITY_STANDARDREVIEW_REVIEW = 30057L;//品质核查 标准审批审核权限
    long QUALITY_TASK_LIST = 30058L;//品质核查 任务查询权限
    long QUALITY_STAT_SCORE = 30059L;//品质核查 分数统计查看权限
    long QUALITY_STAT_TASK = 30060L;//品质核查 任务数统计查看权限
    long QUALITY_UPDATELOG_LIST = 30061L;//品质核查 修改记录查看权限
    long QUALITY_SAMPLE_LIST = 30062L;//品质核查 绩效考核查看权限
    long QUALITY_SAMPLE_CREATE = 30063L;//品质核查 绩效考核新增权限
    long QUALITY_SAMPLE_UPDATE = 30064L;//品质核查 绩效考核修改权限
    long QUALITY_SAMPLE_DELETE = 30065L;//品质核查 绩效考核删除权限
    long QUALITY_STAT_SAMPLE = 30066L;//品质核查 检查统计查看权限

    // 仓库管理
    long WAREHOUSE_MODULE_ID = 21000L;
    long WAREHOUSE_REPO_VIEW = 210001001L;
    long WAREHOUSE_REPO_OPERATION = 210001002L;
    long WAREHOUSE_MATERIAL_CATEGORY_ALL = 210001003L;
    long WAREHOUSE_MATERIAL_INFO_ALL = 210001004L;
    long WAREHOUSE_REPO_MAINTAIN_SEARCH = 210001005L;
    long WAREHOUSE_REPO_MAINTAIN_INSTOCK = 210001006L;
    //和210001005L進行合并
    long WAREHOUSE_REPO_MAINTAIN_LOG_SEARCH = 210001007L;
    long WAREHOUSE_REPO_MAINTAIN_LOG_EXPORT = 210001008L;
    long WAREHOUSE_CLAIM_MANAGEMENT_SEARCH = 210001009L;
    long WAREHOUSE_CLAIM_MANAGEMENT_APPLICATION = 210001010L;
    long WAREHOUSE_PARAMETER_FLOWCASE_CONFIG = 210001011L;
    long WAREHOUSE_PARAMETER_CONFIG = 210001012L;
    long WAREHOUSE_REPO_MAINTAIN_OUTSTOCK = 210001013L;
    long PURCHASE_MODULE = 26000L;
    long PURCHASE_VIEW = 260001001L;
    long PURCHASE_OPERATION = 260001002L;
    long PURCHASE_ENTRY_STOCK = 260001003L;
    long SUPPLIER_MODULE = 27000L;
    long SUPPLIER_VIEW = 270001001L;
    long SUPPLIER_OPERATION = 270001002L;
    long REQUISITION_MODULE = 25000L;
    long REQUISITION_VIEW = 250001001L;
    long REQUISITION_CREATE = 250001002L;
    long REQUISITION_DELETE = 250001003L;

    //企业管理
    long ORGANIZATION_CREATE = 33001L;
    long ORGANIZATION_UPDATE = 33002L;
    long ORGANIZATION_DELETE = 33003L;
    long ORGANIZATION_LIST = 33004L;
    long ORGANIZATION_IMPORT = 33005L;
    long ORGANIZATION_EXPORT = 33006L;
    long ORGANIZATION_SET_ADMIN = 33007L;

    // ------- 启动广告权限 --------
    long LAUNCHAD_ALL = 1090010000L;

    // ------- 园区访客权限 --------
    long VISITORSYS_BOOKING_MANAGEMENT = 4180041810L;//预约管理权限
    long VISITORSYS_VISITOR_MANAGEMENT = 4180041820L;//访客管理权限
    long VISITORSYS_DEV_MANAGEMENT = 4180041840L;//设备管理权限
    long VISITORSYS_MODILE_MAMAGEMENT = 4180041850L;//移动端管理权限

    // ------- 企业访客权限 --------
    long VISITORSYS_BOOKING_MANAGEMENT_ENT = 5210052110L;//预约管理权限
    long VISITORSYS_VISITOR_MANAGEMENT_ENT = 5210052120L;//访客管理权限
    long VISITORSYS_DEV_MANAGEMENT_ENT = 5210052130L;//设备管理权限
    long VISITORSYS_MODILE_MAMAGEMENT_ENT = 5210052140L;//移动端管理权限

    // -----------用户认证-------------
    long AUTHENTIFICATION_LIST_VIEW = 42007; //查看用户认证列表
    long AUTHENTIFICATION_AUDITING = 42008; // 审核权限
    // ----------用户认证 END----------------

    // 停车缴费权限常量
    long PARKING_APPLY_MANAGERMENT = 4080040810L;//
    long PARKING_ORDER_MANAGERMENT = 4080040820L;


    // ----------招商客户权限---------
    long INVITED_CUSTOMER_VIEW = 150001L;
    long INVITED_CUSTOMER_CREATE = 150002L;
    long INVITED_CUSTOMER_UPDATE = 150003L;
    long INVITED_CUSTOMER_DELETE = 150004L;
    long INVITED_CUSTOMER_CHANGE_ENTERPRISE_CUSTOMER = 150005L;
    long INVITED_CUSTOMER_SIGN = 150006L;
    long INVITED_CUSTOMER_CONTINUE = 150007L;
    long INVITED_CUSTOMER_IMPORT = 150008L;
    long INVITED_CUSTOMER_EXPORT = 150009L;
    long INVITED_CUSTOMER_CHANGE_APTITUDE = 150010L;


    //------- 房源招商 --------
    long INVESTMENT_ADVERTISEMENT_CREATE = 150101L;//发布招商信息权限
    long INVESTMENT_ADVERTISEMENT_UPDATE = 150102L;//编辑招商信息权限
    long INVESTMENT_ADVERTISEMENT_DELETE = 150103L;//删除招商信息权限
    long INVESTMENT_ADVERTISEMENT_EXPORT = 150104L; //导出招商信息权限
    long INVESTMENT_ADVERTISEMENT_CHANGE_ORDER = 150105L; //排序权限
    long INVESTMENT_APPLY_EXPORT = 150106L;//导出申请记录权限
    long INVESTMENT_APPLY_TRANSFORM_TO_CUSTOMER = 150107L;//转为意向客户权限
    long INVESTMENT_APPLY_DELETE = 150108L;//删除申请记录权限
    
}
