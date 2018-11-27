package com.everhomes.rest.rentalv2;

public enum RentalBillSource {
    NORMAL((byte)1,"用户发起"),
    BACK_GROUND((byte)2,"后台录入");

    private byte code;
    private String describe;
    private RentalBillSource(byte code,String describe) {
        this.code = code;
        this.describe = describe;
    }

    public byte getCode() {
        return this.code;
    }

    public static RentalBillSource fromCode(byte code) {
        for(RentalBillSource t : RentalBillSource.values()) {
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
