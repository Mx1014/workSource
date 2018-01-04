package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCIAL_SECURITY_IN((byte) 0): 社保增员</li>
 * <li>SOCIAL_SECURITY_OUT((byte) 1): 社保减员</li>
 * <li>ACCUMULATION_FUND_IN((byte) 2): 公积金增员</li>
 * <li>ACCUMULATION_FUND_OUT((byte) 3): 公积金减员</li>
 * <li>((byte) 4): 社保补缴</li>
 * <li>((byte) 5): 公积金补缴</li>
 * </ul>
 */
public enum InOutLogType {
    SOCIAL_SECURITY_IN((byte) 0), SOCIAL_SECURITY_OUT((byte) 1),
    ACCUMULATION_FUND_IN((byte) 2), ACCUMULATION_FUND_OUT((byte) 3);

    private byte code;

    private InOutLogType(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static InOutLogType fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for (InOutLogType t : InOutLogType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
    }
