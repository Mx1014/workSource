package com.everhomes.rest.enterprisepaymentauth;

/**
 * <ul>
 * <li>PAYMENT_SCENE: 支付场景</li>
 * <li>LIMIT_AMOUNT: 每月额度</li>
 * </ul>
 */
/**
 * Created by wuhan on 2018/10/16.
 */
public enum OperateSourceType {
    PAYMENT_SCENE("PAYMENT_SCENE"),LIMIT_AMOUNT("LIMIT_AMOUNT");

    private String code;
    private OperateSourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static OperateSourceType fromCode(String code) {
        if (null == code) {
            return null;
        }
        for(OperateSourceType t : OperateSourceType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }

        return null;
    }
}
