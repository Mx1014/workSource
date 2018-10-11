package com.everhomes.rest.rentalv2;

public enum  PayChannel {
    NORMAL_PAY("normal","个人支付"),
    ENTERPRISE_PAY("enterprise","企业支付"),
    ENTERPRISE_PAY_CHARGE("enterprise/charge","企业支付(记账)"),
    ENTERPRISE_PAY_COMPLETE("enterprise/complete","企业支付(已付)");

    private String code;
    private String describe;

    private PayChannel(String code,String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return this.code;
    }

    public static PayChannel fromCode(String code) {
        for(PayChannel t : PayChannel.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }

        return null;
    }

    public String getDescribe() {
        return describe;
    }
}
