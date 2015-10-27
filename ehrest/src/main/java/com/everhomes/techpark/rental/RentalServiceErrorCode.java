package com.everhomes.techpark.rental;

public interface RentalServiceErrorCode {
    static final String SCOPE = "rental";

    static final int ERROR_RESERVE_TOO_EARLY = 10001;  //未到预定时间
    static final int ERROR_RESERVE_TOO_LATE = 10002;  //超过预定时间
    static final int ERROR_HAVE_BILL = 10003;  //有订单了
//    static final int ERROR_PUNCH_ADD_DAYLOG = 10004;  //计算打卡日志有问题
//    static final int ERROR_PUNCH_REFRESH_DAYLOG = 10005;  //计算打卡日志有问题
    
}
