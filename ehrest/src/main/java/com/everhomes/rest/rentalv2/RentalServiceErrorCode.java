package com.everhomes.rest.rentalv2;

public interface RentalServiceErrorCode {
	String SCOPE = "rental";

	int ERROR_RESERVE_TOO_EARLY = 10001; // 还没有到预定时间
	int ERROR_RESERVE_TOO_LATE = 10002; // 已经过了预定时间
	int ERROR_HAVE_BILL = 10003; // 已经被预定了
	int ERROR_NO_ENOUGH_SITES = 10004; // 场所数量不够
	int ERROR_BILL_OVERTIME = 10005; // 订单超时失效了
	int ERROR_CANCEL_OVERTIME = 10006; // 已经过了预定取消时间
	int ERROR_DID_NOT_PAY = 10007; // 您还未付款！
	int ERROR_NOT_SUCCESS = 10008; // 不是已预订订单
	int ERROR_NOT_COMPLETE = 10009; // 不是已完成订单
	int ERROR_ORDER_DUPLICATE = 10010; // 您在这个时间段已经有别的预约了哟
	int ERROR_CREATE_EXCEL = 10011; // 生成预订信息有问题
	int ERROR_DOWNLOAD_EXCEL = 10012; // 下载预订信息有问题

	int ERROR_NO_ENOUGH_ITEMS = 10020; // 商品数量不够！
 
	int ERROR_REPEAT_SITE_ASSGIN = 11000; // 分配资源编号重复
	int ERROR_SITE_ASSGIN_NULL = 11001; // 资源空指针

	int ERROR_LOOP_TOOMUCH = 12000; // 循环过多次

	int ERROR_DEFAULT_RULE_NOT_FOUND = 13000;// 默认的rule没有找到
	
	int ERROR_OFFLINE_PAY_UPDATE_RESOURCE_STATUS = 14000; //请补充线下模式负责人信息！

	int ERROR_REFUND_ERROR = 15000; // 退款失敗

	int ERROR_TIME_STEP = 10050; // 开始时间不能大于等于结束时间！

	int ERROR_ORDER_CANCELED = 10051; // 订单已取消
	int ERROR_ORDER_CANCEL_OVERTIME = 10052; //已经进入预约使用时间，无法取消订单
	int ERROR_DOWN_PARKING_LOCK = 10053; //无法结束使用，车锁未升起
	int ERROR_ORDER_RENEW_OVERTIME = 10054; //无法延长，已经超时

	int ERROR_INVALID_PARAMETER = 506; //非法参数
	int ERROR_LOST_PARAMETER = 507; //参数缺失
	int ERRPR_LOST_RESOURCE_RULE = 508;//资源或资源规则缺失
	int ERROR_CANNOT_FIND_ORDER = 509;//找不到订单或订单状态错误
	int ERROR_CREATE_ORDER = 510;//下单失败
}
