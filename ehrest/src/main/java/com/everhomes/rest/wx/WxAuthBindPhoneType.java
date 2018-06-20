package com.everhomes.rest.wx;

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
