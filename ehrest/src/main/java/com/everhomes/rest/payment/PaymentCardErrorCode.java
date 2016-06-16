package com.everhomes.rest.payment;

public interface PaymentCardErrorCode {
	static final String SCOPE = "paymentCard";

	static final int ERROR_SERVER_REQUEST = 10000;  //服务器通讯失败，请检查网络连接并重试！
    static final int ERROR_OLD_PASSWORD = 10001;  //旧密码有误
    static final int ERROR_VERIFY_CODE = 10002;  //验证码错误
    static final int ERROR_NOT_EXISTS_CARD = 10003;  //卡不存在
}
