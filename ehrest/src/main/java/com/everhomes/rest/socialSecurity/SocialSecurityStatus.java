package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>PENDING((byte) 0): 未缴</li>
 * <li>PAYING((byte) 1): 在缴</li>
 * </ul>
 */
public enum SocialSecurityStatus {

    PENDING((byte) 0), PAYING((byte) 1);

    private byte code;

    private SocialSecurityStatus(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static SocialSecurityStatus fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for (SocialSecurityStatus t : SocialSecurityStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
