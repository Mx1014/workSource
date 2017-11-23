package com.everhomes.rest.rentalv2;

public interface RentalServiceErrorCode {
	String SCOPE = "rental";

	int ERROR_RESERVE_TOO_EARLY = 10001; // 未到预定时间
	int ERROR_RESERVE_TOO_LATE = 10002; // 超过预定时间
	int ERROR_HAVE_BILL = 10003; // 有订单了
	int ERROR_NO_ENOUGH_SITES = 10004; // site数量不够！
	int ERROR_BILL_OVERTIME = 10005; // 订单超时失效了！
	int ERROR_CANCEL_OVERTIME = 10006; // 未到预定时间
	int ERROR_DID_NOT_PAY = 10007; // 未到预定时间
	int ERROR_NOT_SUCCESS = 10008; // 未到预定时间
	int ERROR_NOT_COMPLETE = 10009; // 未到预定时间
	int ERROR_ORDER_DUPLICATE = 10010; // 预定时间冲突
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
}
