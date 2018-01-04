package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCIAL_SECURITY((byte) 0): 社保</li>
 * <li>ACCUMULATION_FUND((byte) 1): 公积金</li>
 * </ul>
 */
public enum InOutTimeType {
    SOCIAL_SECURITY((byte) 0), ACCUMULATION_FUND((byte) 1);

    private byte code;

    private InOutTimeType(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static InOutTimeType fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for (InOutTimeType t : InOutTimeType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
