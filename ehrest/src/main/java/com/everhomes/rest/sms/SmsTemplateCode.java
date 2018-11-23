// @formatter:off
package com.everhomes.rest.sms;

public interface SmsTemplateCode {
    String SCOPE = "sms.default";

    String YZX_SUFFIX = "yzx";
    String YOU_XUN_TONG_SUFFIX = "YouXunTong";
    String LIAN_XIN_TONG_SUFFIX = "LianXinTong";
    String YUN_PIAN_SUFFIX = "YunPian";

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

    int SIGN_CODE = 0; // 签名code

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

    //资源预约短信(工作流模板)
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

    //物业缴费催款
    int PAYMENT_NOTICE_CODE = 54;

    //任务消息提醒短信
    int PM_NOTIFY_BEFORE_TASK = 55;
    int PM_NOTIFY_BEFORE_TASK_DELAY = 56;
    int PM_NOTIFY_AFTER_TASK_DELAY = 57;

    //资源预约： vip车位预约
    int UNPAID_ORDER_OVER_TIME = 58; //订单超时未付款
    int PAY_ORDER_SUCCESS = 59; //支付成功 给车主发短信
    int RENTAL_USER_CANCEL_ORDER = 60; //vip车位预约用户取消订单 给车主发短信

    int RENTAL_PARKING_LOCK_RAISE = 61; //vip车位预约用户车锁升起

    int RENEW_RENTAL_ORDER_SUCCESS = 62; //使用中：延时成功 尊敬的“车主姓名”，用户（“预约人姓名”：“预约人手机号”）已为您将预约的VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）延时到“新结束时间”，请点击以下链接使用：https://core.zuolin.com/evh/aclink/id=1283jh213a，感谢您的使用。

    int RENTAL_ORDER_WILL_END = 63; //使用中：即将超时（结束时间前15分钟提醒）

    int SYSTEM_AUTO_CANCEL_ORDER = 64; ///系统自动取消订单并退款成功：尊敬的用户，您预约的VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）由于前序订单使用超时，且无其他空闲车位可更换，已自动取消并全额退款，为此我们深感抱歉，期待下次为您服务。

    int SYSTEM_AUTO_UPDATE_SPACE_RESERVER = 65; //订单变更通知 给预约人发短信

    int SYSTEM_AUTO_UPDATE_SPACE_PLATE_OWNER = 66; //订单变更通知 给车主发短信

    //物业缴费自动催款模板SCOPE
    static final String ASSET_MSG_SCOPE = "asset.sms";
    int VISITORSYS_VERIFICATION_CODER = 67; //访客管理发送访客验证码
    int VISITORSYS_INVT_INVITATION_LETTER = 68; //预约访客发送访客邀请函
    int VISITORSYS_TEMP_INVITATION_LETTER = 69; //临时访客发送访客邀请函
    
    int POINT_VERIFICATION_CODE = 70 ; // 积分记账时需要给管理员发送验证码


    //装修办理
    int DECORATION_APPLY_SUCCESS = 70; //装修申请通过 通知负责人:尊敬的“装修公司负责人姓名”，用户（“申请人姓名”：“申请人手机号”）提交的装修申请（“租户公司名称”）已审核通过，需尽快提交相关装修资料，请前往APP查看详情，您可以点击以下链接下载APP，并使用本机号码进行注册：https://core.zuolin.com/evh/aclink/id=1283jh213a

    int DECORATION_APPROVAL_SUCCESS = 71; //资料审核成功 通知负责人:尊敬的“装修公司负责人姓名”，关于“租户公司名称”的装修资料已审核通过，请等待管理公司上传费用清单，您可前往APP查看详情。

    int DECORATION_APPROVAL_FAIL = 72; //资料审核失败 通知负责人:尊敬的“装修公司负责人姓名”，关于“租户公司名称”的装修资料审核不通过，装修办理流程已中止，您可前往APP查看详情。

    int DECORATION_FEE_GENERATE = 73; // 装修费用清单生成：短信通知租户，装修公司负责人 :尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修费用清单已上传，需尽快缴费，您可前往APP查看详情。

    int DECORATION_FEE_CONFIRM = 74;//缴费完成：短信通知租户，装修公司负责人 : 尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修费用已缴纳，施工人员可凭证进场，请遵守施工相关规定，感谢您的配合。

    int DECORATION_CHECK_SUCCESS = 75;//竣工验收工作流正常结束 短信通知装修公司负责人 :尊敬的“装修公司负责人姓名”，关于“租户公司名称”的装修已通过竣工验收，请等待管理公司确认押金退费信息，您可前往APP查看详情。

    int DECORATION_CHECK_FAIL = 76;//竣工验收工作流异常结束 短信通知装修公司负责人:尊敬的“装修公司负责人姓名”，关于“租户公司名称”的装修竣工验收未通过，装修办理流程已中止，您可前往APP查看详情。

    int DECORATION_REFOUND_GENERATE = 77;//押金退回信息生成：短信通知租户，装修公司负责人:尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修押金退费信息已上传，您可前往APP查看详情。

    int DECORATION_REFOUND_CONFIRM = 78;//押金退回成功，装修办理完成：短信通知租户，装修公司负责人 :尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修押金已退回，装修办理完成，感谢您的使用。

    int DECORATION_CREATE_WORKER = 79;//装修公司负责人登记施工人员成功：短信通知该施工人员 : 尊敬的“施工人员姓名”，用户（“装修公司负责人姓名”：“手机号”）已登记您为此次装修工程（“租户公司名称”）的施工人员，请前往APP查看详情，您可以点击以下链接下载APP，并使用本机号码进行注册：https://core.zuolin.com/evh/aclink/id=1283jh213a 。

    int DECORATION_CANCEL = 80;//装修流程被管理员在后台手动中止：短信通知租户，装修公司负责人 :尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修流程已被管理公司（“中止操作执行人姓名”；“手机号”）中止，您可前往APP查看详情。

    int DECORATION_MOTIFY_FEE = 81;//管理员修改装修费用的时候，短信提醒租户和装修公司 尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修费用清单有更新，请前往APP查看详情。

    int DECORATION_MOTIFY_REFUND = 82;//修改退费信息后，短信提醒租户和装修公司负责人 尊敬的“申请人姓名”/“装修公司负责人姓名”，关于“租户公司名称”的装修押金退费信息有更新，请前往APP查看详情。

    //云打印
    int PRINT_UNPAID_MESSAGE = 83; //未支付的短信

    int RENTAL_CANCEL_ORDER_REFUND = 84;//尊敬的用户，您预约的${resourceName}(${useDetail})已退款成功，订单编号：$订单编号$，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。

    int RENTAL_CANCEL_ORDER_NO_REFUND = 85;//尊敬的用户，您预约的${resourceName}(${useDetail})已成功取消，订单编号：$订单编号$，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。

    int RENTAL_CANCEL_ORDER = 86;//尊敬的用户，您预约的${resourceName}(${useDetail})已成功取消，订单编号：$订单编号$，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。

    int RENTAL_ADMIN_NOTIFY = 87;//$预订人$（$联系方式$）预约了$资源名称$，使用时间：$预订时间$，需要物资：$物资名称$、$物资名称$，购买商品：$商品名称$*$数量$、$商品名称$*$数量$

    int RENTAL_BEGIN_NOTIFY = 88;//您预约的${resourceName}已临近使用时间，预订时间为$预订时间$

    //服务联盟
    int SERVICE_ALLIANCE_NEW_APPLY_TO_MANAGE = 87; //服务联盟V3.7.1 - 【app名称】$发起人姓名$（$发起人手机号$）提交了$服务名称$申请，请及时处理
    int SERVICE_ALLIANCE_NEW_APPLY_TO_USER = 88; //服务联盟V3.7.1 - 【app名称】你提交的$服务名称$申请正在处理，可在app“我”-“我的申请”中查看处理进度

    int RENTAL_END_NOTIFY_HOUR_USER = 89;//您预约的${resourceName}已临近结束时间，预订时间为$预订时间$

    int RENTAL_CREATE_FLOW = 90;//${requestorName}(${requestorPhone})发起了${resourceName}的预约申请，请及时核查订单并处理

    int RENTAL_CANCEL_NOT_PAY = 91;//抱歉，由于您未在规定时间内完成支付，您预约的${resourceName}（${useDetail}）已自动取消，订单编号：${orderNum}，期待下次为您服务
}
