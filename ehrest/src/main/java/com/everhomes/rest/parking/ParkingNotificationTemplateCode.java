package com.everhomes.rest.parking;

public interface ParkingNotificationTemplateCode {

	String SCOPE = "park.notification";
    
    int USER_APPLY_CARD = 1; // 申请月卡
    
    int DEFAULT_RATE_NAME = 2; // 月卡默认费率名称

    int DEFAULT_MINUTE_UNIT = 3; //分钟
}
