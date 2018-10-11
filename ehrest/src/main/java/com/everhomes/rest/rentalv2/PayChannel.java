package com.everhomes.rest.rentalv2;

public enum  PayChannel {
    NORMAL_PAY((byte)0,"个人支付"),
    ENTERPRISE_PAY((byte)1,"企业支付(记账)"),
    ENTERPRISE_PAY_COMPLETE((byte)2,"企业支付(完成)");

    private byte code;
    private String describe;

    private PayChannel(byte code,String describe) {
        this.code = code;
        this.describe = describe;
    }

    public byte getCode() {
        return this.code;
    }

    public static PayChannel fromCode(byte code) {
        for(PayChannel t : PayChannel.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }

    public String getDescribe() {
        return describe;
    }
}
