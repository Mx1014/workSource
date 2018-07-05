package com.everhomes.rest.wx;

/**
 * BIND: 微信已绑定手机号
 * NOT_BIND: 微信未绑定手机号
 */
public enum WxAuthBindPhoneType {

    BIND((byte) 1), NOT_BIND((byte) 0);
    private Byte code;

    WxAuthBindPhoneType(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static WxAuthBindPhoneType fromString(String code) {
        for (WxAuthBindPhoneType type : WxAuthBindPhoneType.values()) {
            if (type.name().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static WxAuthBindPhoneType fromCode(Byte code) {
        for (WxAuthBindPhoneType type : WxAuthBindPhoneType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
