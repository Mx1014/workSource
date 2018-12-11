package com.everhomes.rentalv2;

public interface RentalNotificationTemplateCode {
    String SCOPE = "rental.notification";
    //local template table
    int RENTAL_BEGIN_NOTIFY = 1; //预定快开始的推送
    int RENTAL_ADMIN_NOTIFY = 2; //$预订人$（$联系方式$）预约了$资源名称$，使用时间：$预订时间$，需要物资：$物资名称$、$物资名称$，购买商品：$商品名称$*$数量$、$商品名称$*$数量$
    int RENTAL_APPLY_SUCCESS_CODE = 3; // 申请成功短信：预定成功给管理员发推送','您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系${offlinePayeeName}（${offlinePayeeContact}）上门收费，2. 到${offlineCashierAddress}付款；感谢您的使用。
    int RENTAL_APPLY_FAILURE_CODE = 4; // 申请失败短信：【正在会】您申请预约的{使用时间}的{资源名称}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。
    int RENTAL_PAY_SUCCESS_CODE = 5; // 支付成功推送：您已成功预约了$资源名称$，使用时间：$预订时间$。如需取消，请在预订开始时间前取消，感谢您的使用。
    int APPROVE_RENTAL_APPLY_SUCCESS_CODE = 6; // 审批线上支付模式，申请成功短信：【正中会】您申请预约的{使用时间}的{资源名称}已通过审批，为确保您成功预约，请尽快到APP完成在线支付，感谢您的使用。
    int RENTAL_CANCEL_CODE = 7; // 取消短信：【正中会】您申请预约的{使用时间}的{资源名称}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。
    int RENTAL_REMIND_CODE = 8; // 资催办-正中会:【正中发】客户（{申请人姓名}{申请人联系电话}）提交资源预约的线下支付申请，预约{资源名称}，使用时间：{使用时间}，订单金额{订单金额}，请尽快联系客户完成支付
	int RENTAL_PROCESSOR_SUCCESS_CODE = 9; // 正中会-预成功:【正在会】客户{客户姓名}（{客户联系方式}）完成支付，成功预约{使用时间}的{资源名称}，请提前做好相关准备工作。
	int RENTAL_END_NOTIFY_HOUR = 10;//预定快结束的推送(小时/半天):温馨提醒：{资源名称}资源的使用将在15分钟后结束，使用客户{客户姓名}（{客户联系方式}），请进行确认
	int RENTAL_END_NOTIFY_DAY = 11;//(废弃)预定快结束的推送(天/月):温馨提醒：{资源名称}资源的使用将在今日结束，使用客户{客户姓名}（{客户联系方式}），请进行确认
    int RENTAL_CHANGE_AMOUNT = 12;//修改金额的推送  您申请预订的$资源名称$，使用时间：$使用时间$，订单金额调整为$调整后的金额$
    int RENTAL_CANCEL_NOT_PAY = 24;//取消未支付的订单 您预约的$资源名称$（$预订时间$）已成功取消，期待下次为您服务
    int RENTAL_CANCEL_ORDER_REFUND = 25;//尊敬的用户，您预约的${useDetail}已退款成功，订单编号：$订单编号$，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。
    int RENTAL_CANCEL_ORDER_NO_REFUND = 26;//尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。
    int RENTAL_CANCEL_ORDER = 27;//尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。
    int RENTAL_END_NOTIFY_HOUR_USER = 28;//预定快结束的推送 用户接收 您预约的${resourceName}已临近结束时间，预订时间为$预订时间$
    int RENTAL_BEGIN_CHARGE_NOTIFY = 29;//预订开始 负责人的推送 ${requestorName}(${requestorPhone})预约的${resourceName}已临近使用时间，预订时间为${useDetail}，请做好会前准备
    // vip 车位预约 消息模版
    int RENTAL_USER_CANCEL_ORDER = 13;  //vip车位预约用户取消订单推送消息    订单取消通知：您的${VIP车位预约}订单已成功取消。
    int UNPAID_ORDER_OVER_TIME = 14; //订单超时取消通知：由于您未在15分钟内完成支付，您预约的${VIP车位（科兴科学园停车场AE003车位：2017-11-15 10:00 - 2017-11-15 12:00）}已自动取消，期待下次为您服务。
    int PAY_ORDER_SUCCESS = 15;//尊敬的用户，您预约的${VIP车位（科兴科学园停车场AE003车位：2017-11-15 10:00 - 2017-11-15 12:00）}已成功提交，您可以在预约时间内控制车位锁以使用车位（地址：${负二层A区}），如需延时，请在预约结束时间前提交申请，否则超时将产生额外费用，感谢您的谅解。
    int RENTAL_USER_CANCEL_ORDER_REFUND = 16;  //vip车位预约用户取消订单退款推送消息   尊敬的用户，您预约的${VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）}已退款成功，订单金额：${“订单金额”}元，退款金额：${“退款金额”}元，期待下次为您服务。

    int RENEW_RENTAL_ORDER_SUCCESS = 17; //使用中：延时成功  尊敬的用户，您预约的VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）已成功延时到“新结束时间”，感谢您的使用。

    int RENTAL_ORDER_OWING_FEE = 18; //订单欠费通知：尊敬的用户，您预约的VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）由于超时使用产生欠费，欠费金额：“欠费金额”元，请前往订单详情完成支付，否则将影响下次使用。
    int COMPLETE_RENTAL_ORDER = 19; //订单完成通知：尊敬的用户，您预约的VIP车位服务（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）已完成，本次服务金额：“订单金额”元，感谢您的使用。

    int SYSTEM_AUTO_CANCEL_ORDER = 20; //系统自动取消订单并退款成功：尊敬的用户，您预约的VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）由于前序订单使用超时，且无其他空闲车位可更换，已自动取消并全额退款，为此我们深感抱歉，期待下次为您服务。
    int SYSTEM_AUTO_UPDATE_SPACE = 21; //订单变更通知：尊敬的用户，您预约的VIP车位（“停车场名称”“车位编号”车位：“开始时间” - “结束时间”）由于前序订单使用超时，系统自动为您更换至“车位编号”车位，给您带来的不便我们深感抱歉，感谢您的使用。
    int RENTAL_ORDER_USE_DETAIL = 22; // 资源订单使用详情 VIP车位（${parkingLotName}${spaceNo}车位：${startTime} - ${endTime}）
    int RENTAL_ORDER_TIME_UP = 23;//尊敬的用户，您预约的${useDetail}剩余使用时长：15分钟，如需延时，请前往APP进行操作，否则超时系统将继续计时计费，感谢您的使用。

    String locale = "zh_CN";

    String FLOW_SCOPE = "rental.flow";
    int RENTAL_FLOW_CONTENT = 1; //工作流content
    int RENTAL_FLOW_OFFLINE_INFO = 2; //工作流 线下支付的info

    //local string table
    int RENTAL_TEXT_REMARK = 10; // 资源提交信息   请输入备注
    int RENTAL_SHOW_CONTENT = 11; // 资源提交信息  请输入显示内容
    int RENTAL_ORDER_NOT_REFUND_TIP = 12; // 不支持退款的提示信息 亲爱的用户，为保障资源使用效益，现在取消订单，系统将不予退款，恳请您谅解。确认要取消订单吗？
    int RENTAL_ORDER_NOT_REFUND_TIP2 = 13; // 不支持退款的提示信息 亲爱的用户，为保障资源使用效益，现在取消订单，系统将不予退款，恳请您谅解。
}
