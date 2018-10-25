package com.everhomes.rest.paymentauths;

/**
 * <ul>停车充值月卡申请状态
 * <li>CLOUD_PRINT("云打印"),: 云打印</li>
 * </ul>
 */
public enum PaymentAuthsAPPType {
	CLOUD_PRINT("66348"),PARKING("500005516"),RENTALSCREEN("500002666"),RENTALVIP("203"),RENTALROOM("201"),RENTALSERVICE("500002668");

	
    private String code;

    PaymentAuthsAPPType(String code) {
        this.code = code;
    }

    public static PaymentAuthsAPPType fromCode(String code) {
        for (PaymentAuthsAPPType APPType : PaymentAuthsAPPType.values()) {
            if (APPType.code.equals(code) ) {
                return APPType;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
