package com.everhomes.rest.rentalv2;

public interface RentalServiceErrorCode {
	static final String SCOPE = "rental";

	static final int ERROR_RESERVE_TOO_EARLY = 10001; // 未到预定时间
	static final int ERROR_RESERVE_TOO_LATE = 10002; // 超过预定时间
	static final int ERROR_HAVE_BILL = 10003; // 有订单了
	static final int ERROR_NO_ENOUGH_SITES = 10004; // site数量不够！
	static final int ERROR_BILL_OVERTIME = 10005; // 订单超时失效了！
	static final int ERROR_CANCEL_OVERTIME = 10006; // 未到预定时间
	static final int ERROR_DID_NOT_PAY = 10007; // 未到预定时间
	static final int ERROR_NOT_SUCCESS = 10008; // 未到预定时间
	static final int ERROR_NOT_COMPLETE = 10009; // 未到预定时间
	static final int ERROR_ORDER_DUPLICATE = 10010; // 预定时间冲突
	static final int ERROR_CREATE_EXCEL = 10011; // 生成预订信息有问题
	static final int ERROR_DOWNLOAD_EXCEL = 10012; // 下载预订信息有问题

	static final int ERROR_NO_ENOUGH_ITEMS = 10020; // 商品数量不够！
 
	static final int ERROR_REPEAT_SITE_ASSGIN = 11000; // 分配资源编号重复
	static final int ERROR_SITE_ASSGIN_NULL = 11001; // 资源空指针

	static final int ERROR_LOOP_TOOMUCH = 12000; // 循环过多次

	static final int ERROR_DEFAULT_RULE_NOTFOUND = 13000;// 默认的rule没有找到
	
	
	static final int ERROR_REFOUND_ERROR = 15000; // 退款失敗

	static final int ERROR_TIME_STEP = 10050; // 开始时间不能大于等于结束时间！
}
