package com.everhomes.rest.rentalv2;

/**
 * 定价类型
 * 0 按时长定价
 * 1 起步价模式
 */
public enum RentalPriceType {

    LINEARITY((byte)0),INITIATE((byte)1);
    private byte code;
    private RentalPriceType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static RentalPriceType fromCode(byte code) {
        for(RentalPriceType t : RentalPriceType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
