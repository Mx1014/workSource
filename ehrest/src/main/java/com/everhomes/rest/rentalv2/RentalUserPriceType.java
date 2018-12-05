package com.everhomes.rest.rentalv2;

/**
 * 定价类型
 * 1 统一
 * 2 按用户类型
 * 3 按会员等级
 */
public enum RentalUserPriceType {

    UNIFICATION((byte)1),USER_TYPE((byte)2),VIP_TYPE((byte)3);
    private byte code;
    private RentalUserPriceType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static RentalUserPriceType fromCode(byte code) {
        for(RentalUserPriceType t : RentalUserPriceType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
