// @formatter:off
package com.everhomes.rest.sms;

public interface SmsTemplateCode {
    String SCOPE = "sms.default";

    String YZX_SUFFIX = "yzx";
    String SCOPE_YZX = SCOPE + "." + YZX_SUFFIX;

    String KEY_VCODE = "vcode";
    
    String KEY_YEAR = "year";
    String KEY_MONTH = "month";
    String KEY_DUEAMOUNT = "dueAmount";
    String KEY_OWEAMOUNT = "oweAmount";
    String KEY_PAYAMOUNT = "payAmount";
    String KEY_BALANCE = "balance";
    String KEY_DESCRIPTION = "description";

    String KEY_USERNAME = "userName";
    String KEY_USERPHONE = "userPhone";
    String KEY_APPLYTIME = "applyTime";
    String KEY_APPLYTYPE = "applyType";
    String KEY_LOCATION = "location";
    String KEY_AREA = "area";
    String KEY_ENTERPRISENAME = "enterpriseName"; 

    String KEY_PHONE = "phone";
    String KEY_TOPICTYPE = "topicType";
    String KEY_MSG = "msg";

    int VERIFICATION_CODE = 1; // 验证码
    int WY_BILL_CODE = 3; //物业账单信息
    int ORGANIZATION_ASSIGNED_CODE = 4; //给被分配人员发短信:分配请求服务帖任务给处理员
    int WY_SEND_MSG_CODE = 5; // 物业一键推送消息
    int PM_TASK_PROCESS_MSG_CODE = 6; // 任务处理消息
    int PM_TASK_PUSH_MSG_CODE = 7; // 任务发布消息
    int ACLINK_VISITOR_MSG_CODE = 8; // 访客授权链接
    int WEIXIN_APPLY_RENEW_CODE = 9; // 威新-看楼申请
    
    int PM_TASK_ASSIGN_CODE = 10; //任务分配消息
    int PM_TASK_CREATOR_CODE = 11;//任务创建消息
    int PM_TASK_ASSIGN_TO_CREATOR_CODE = 15;//任务分配，发送给任务发起者

    int RENTAL_SUCCESS_EXCLUSIVE_CODE = 12;//付费预约成功 独占资源
    int RENTAL_SUCCESS_NOSITENUMBER_CODE = 13;//付费预约成功 非独占资源，不需要选择资源编号
    int RENTAL_SUCCESS_SITENUMBER_CODE = 14;//付费预约成功 非独占资源，需要选择资源编号
    
    int PUSH_MESSAGE_TO_BUSINESS_AND_ADMIN_CODE = 16;//发送短信给业务联系人和管理员
    
    int PUSH_MESSAGE_BACK_TWO_MONTHS_WITH_SERVICE_USER_CODE = 17;//合同到期前两个月发送短信（有客服人员）: 尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。
    int PUSH_MESSAGE_BACK_TWO_MONTHS_WITHOUT_SERVICE_USER_CODE = 18;//合同到期前两个月发送短信（无客服人员）：尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。
    int PUSH_MESSAGE_BACK_ONE_MONTH_WITH_SERVICE_USER_CODE = 19;//合同到期前一个月发送短信（有客服人员）：尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。
    int PUSH_MESSAGE_BACK_ONE_MONTH_WITHOUT_SERVICE_USER_CODE = 20;//合同到期前一个月发送短信（无客服人员）：尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。
    int PUSH_MESSAGE_TO_NEW_ORGANIZATION_WITH_SERVICE_USER_CODE = 21;//发送短信给新企业（有客服人员）：尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，欢迎入住${communityName}，有任何问题请随时与我联系。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。
    int PUSH_MESSAGE_TO_NEW_ORGANIZATION_WITHOUT_SERVICE_USER_CODE = 22;//发送短信给新企业（无客服人员）：尊敬的客户您好，欢迎入住${communityName}。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。

    int PARKING_ENTER_QUEQUEING_NODE = 23; // 进入排队节点
    int PARKING_CANCEL_QUEQUEING = 24; // 排队中 驳回
    int PARKING_ENTER_PROCESSING_NODE = 25; // 进入 待办理节点
    int PARKING_CANCEL_PROCESSING = 26; // 办理

    // 资源预约短信模板
    // 线下支付模式 审批通过短信
    int RENTAL_APPLY_SUCCESS_CODE = 28; // 申请成功短信：【正中会】您申请预约的{使用时间}的{资源名称}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系{收款人姓名}（{收款人手机号}）上门收费，2. 到{收银台地址}付款；感谢您的使用。
    int RENTAL_APPLY_FAILURE_CODE = 29; // 申请失败短信：【正在会】您申请预约的{使用时间}的{资源名称}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。
    int RENTAL_PAY_SUCCESS_CODE = 30; // 支付成功短信：【正中会】您已完成支付，成功预约{使用时间}的{资源名称}，请按照预约的时段使用资源，感谢您的使用。
//    int RENTAL_SUBSCRIBE_FAILURE_CODE = 31; // 预约失败短信：【正中会】您申请预约的{使用时间}的{资源名称}已经被其他客户抢先预约成功，您可以继续申请预约其他时段，由此给您造成的不便，敬请谅解，感谢您的使用。
    // 审批线上支付模式 审批通过短信
    int APPROVE_RENTAL_APPLY_SUCCESS_CODE = 31; // 申请成功短信：【正中会】您申请预约的{使用时间}的{资源名称}已通过审批，为确保您成功预约，请尽快到APP完成在线支付，感谢您的使用。

    //    int RENTAL_TIMEOUT_AND_CANCEL_CODE = 32; // 超时自动取消短信：【正中会】您申请预约的{使用时间}的{资源名称}由于超时未支付，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。
    int RENTAL_CANCEL_CODE = 32; // 取消短信：【正中会】您申请预约的{使用时间}的{资源名称}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。
    int RENTAL_REMIND_CODE = 33; // 资催办-正中会:【正中发】客户（{申请人姓名}{申请人联系电话}）提交资源预约的线下支付申请，预约{资源名称}，使用时间：{使用时间}，订单金额{订单金额}，请尽快联系客户完成支付

    //物业报修，工作流短信模版
    int PM_TASK_FLOW_ASSIGN_CODE = 34;

    //园区入驻短信
    int APPLY_ENTRY_PROCESSING_NODE_CODE = 35;
    int APPLY_ENTRY_PROCESSING_BUTTON_APPROVE_CODE = 36;

    int APPLY_ENTRY_PROCESSING_BUTTON_ABSORT_CODE = 37;
    int APPLY_ENTRY_PROCESSING_BUTTON_REMINDER_CODE = 38;
    int APPLY_ENTRY_COMPLETED_CODE = 39;

    //资源预约短信
    int RENTAL_PROCESSING_NODE_CODE = 40;
    int RENTAL_PROCESSING_NODE_SUPERVISE_CODE = 41;

    int RENTAL_PROCESSING_BUTTON_APPROVE_CODE = 42;

    int RENTAL_PROCESSING_BUTTON_ABSORT_CODE = 43;
    int RENTAL_PROCESSING_BUTTON_REMINDER_CODE = 44;
    int RENTAL_COMPLETED_CODE = 45;

    //物业报修，工作流短信模版
    int PM_TASK_ACCEPTING_NODE_SUPERVISE_CODE= 46;

    int PM_TASK_ASSIGN_NODE_CODE= 47;
    int PM_TASK_ASSIGN_NODE_SUPERVISE_CODE= 48;
    int PM_TASK_PROCESSING_BUTTON_APPROVE_CODE= 49; 
    
    // 视频会议短信
    int VIDEO_EXPIRATION_REMINDER = 51;
    int VIDEO_TRIAL_EXPIRATION_REMINDER = 52;
    
    // 资源预约短信模板
    int RENTAL_PROCESSOR_SUCCESS_CODE = 50; // 正中会-预成功:【正在会】客户{客户姓名}（{客户联系方式}）完成支付，成功预约{使用时间}的{资源名称}，请提前做好相关准备工作。

    // 申诉修改手机号审核成功
    int RESET_IDENTIFIER_APPEAL_SUCCESS_CODE = 53;// 申诉手机号：【深圳科技园】您的申诉已通过，账号手机已更新为{newIdentifier}，若非本人操作请联系客服，感谢您的使用。
}
