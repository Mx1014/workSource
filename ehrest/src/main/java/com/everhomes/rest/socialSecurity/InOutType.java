package com.everhomes.rest.socialSecurity;

public enum InOutType {
    SOCIAL_SECURITY((byte) 0), ACCUMULATION_FUND((byte) 1);

    private byte code;

    private InOutType(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static InOutType fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for (InOutType t : InOutType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
