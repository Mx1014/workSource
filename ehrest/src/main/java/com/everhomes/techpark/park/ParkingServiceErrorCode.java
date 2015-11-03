package com.everhomes.techpark.park;

public interface ParkingServiceErrorCode {

	static final String SCOPE = "parking";

    static final int ERROR_PLATE_LENGTH = 10001;  //车牌号长度错误
    static final int ERROR_PLATE_EXIST = 10002;  //车牌已有月卡
    static final int ERROR_PLATE_APPLIED = 10003;  //车牌已申请
    
}
